
package ar.edu.utn.frc.solicitudes.config;

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
     * Este Bean protege TODOS los endpoints del microservicio de Solicitudes.
     * Define reglas de acceso específicas por rol para cada recurso.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(authorize -> authorize
            
            // 1. Swagger/OpenAPI debe ser público
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            // 2. REGLAS PARA /api/solicitudes
            // Un CLIENTE puede crear una solicitud
            .requestMatchers(HttpMethod.POST, "/api/solicitudes").hasRole("CLIENTE")
            // Un CLIENTE o un OPERADOR pueden ver una solicitud
            .requestMatchers(HttpMethod.GET, "/api/solicitudes/{id}").hasAnyRole("CLIENTE", "OPERADOR")
            .requestMatchers(HttpMethod.GET, "/api/solicitudes/{id}/contenedor").hasAnyRole("CLIENTE", "OPERADOR")
            // Solo un OPERADOR puede asignar una ruta o finalizar la solicitud
            .requestMatchers(HttpMethod.PATCH, "/api/solicitudes/{id}/asignar-ruta").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PATCH, "/api/solicitudes/{id}/finalizar").hasRole("OPERADOR")

            // 3. REGLAS PARA /api/clientes
            // Solo un OPERADOR puede gestionar clientes (crear, ver todos)
            .requestMatchers(HttpMethod.POST, "/api/clientes").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.GET, "/api/clientes").hasRole("OPERADOR")
            // Un CLIENTE puede ver su propia info (asumimos), un OPERADOR puede ver cualquiera
            .requestMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("CLIENTE", "OPERADOR")

            // 4. REGLAS PARA /api/contenedores
            // Un CLIENTE o un OPERADOR pueden registrar un contenedor
            .requestMatchers(HttpMethod.POST, "/api/contenedores").hasAnyRole("CLIENTE", "OPERADOR")
            // Solo OPERADOR ve la lista completa o la lista de pendientes
            .requestMatchers(HttpMethod.GET, "/api/contenedores").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.GET, "/api/contenedores/pendientes").hasRole("OPERADOR")
            // CLIENTE y OPERADOR pueden ver un contenedor específico
            .requestMatchers(HttpMethod.GET, "/api/contenedores/{id}").hasAnyRole("CLIENTE", "OPERADOR")
            // TODOS (incluido TRANSPORTISTA) pueden querer ver el estado de un contenedor
            .requestMatchers(HttpMethod.GET, "/api/contenedores/{id}/estado").hasAnyRole("CLIENTE", "OPERADOR", "TRANSPORTISTA")
            // OPERADOR y TRANSPORTISTA pueden actualizar el estado (ej: "EN_VIAJE", "ENTREGADO")
            .requestMatchers(HttpMethod.PATCH, "/api/contenedores/{id}/estado").hasAnyRole("OPERADOR", "TRANSPORTISTA")

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
     * Este Bean es el "traductor" de roles (Idéntico al del Gateway y otros servicios).
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