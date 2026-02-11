// ¡OJO! Cambia este paquete por el de tu microservicio de rutas
package ar.edu.utn.frc.rutas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Este Bean protege los endpoints del microservicio de Rutas.
     * Define reglas de acceso específicas por rol para cada recurso.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(authorize -> authorize
            
            // 1. Swagger/OpenAPI debe ser público
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            // 2. CAMIONES y DEPÓSITOS: Solo el OPERADOR puede gestionarlos [cite: 796, 820]
            .requestMatchers("/api/camiones/**").hasRole("OPERADOR")
            .requestMatchers("/api/depositos/**").hasRole("OPERADOR")

            // 3. RUTAS:
            // El OPERADOR puede crear/modificar Rutas [cite: 810]
            .requestMatchers(HttpMethod.POST, "/api/rutas").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PUT, "/api/rutas/**").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PATCH, "/api/rutas/**").hasRole("OPERADOR")
            // El CLIENTE y el OPERADOR pueden consultar Rutas [cite: 793, 794, 809]
            .requestMatchers(HttpMethod.GET, "/api/rutas/**").hasAnyRole("CLIENTE", "OPERADOR")

            // 4. TRAMOS:
            // El OPERADOR puede crear/modificar Tramos (ej. asignar camión) [cite: 797, 813]
            .requestMatchers(HttpMethod.POST, "/api/tramos").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PUT, "/api/tramos/**").hasRole("OPERADOR")
            // El TRANSPORTISTA puede actualizar Tramos (iniciar/fin) [cite: 801, 814] y el OPERADOR también
            .requestMatchers(HttpMethod.PATCH, "/api/tramos/**").hasAnyRole("OPERADOR", "TRANSPORTISTA")
            // Todos pueden consultar Tramos (para seguimiento o gestión) [cite: 793, 800, 809]
            .requestMatchers(HttpMethod.GET, "/api/tramos/**").hasAnyRole("CLIENTE", "OPERADOR", "TRANSPORTISTA")
            
            // 5. Cualquier otra petición no definida, por seguridad, debe estar autenticada.
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> 
            oauth2.jwt(jwt -> 
                // Le dice a Spring que use nuestro "traductor" de roles
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );

        return http.build();
    }

    /**
     * Este Bean es el "traductor" de roles (Idéntico al del Gateway).
     * Lee los roles del claim "realm_access" de Keycloak
     * y los convierte al formato "ROLE_NOMBRE_ROL" que Spring Security espera.
     */
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {
                Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");

                if (realmAccess == null || realmAccess.isEmpty()) {
                    return new JwtAuthenticationToken(jwt, List.of());
                }

                List<GrantedAuthority> authorities = realmAccess.get("roles")
                    .stream()
                    // El prefijo "ROLE_" es OBLIGATORIO para Spring Security
                    .map(roleName -> "ROLE_" + roleName.toUpperCase()) 
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

                return new JwtAuthenticationToken(jwt, authorities);
            }
        };
    }
}