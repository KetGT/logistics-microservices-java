package ar.edu.utn.frc.tarifas.config; 

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
     * Este Bean protege los endpoints del microservicio de Tarifas.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(authorize -> authorize
            
            // 1. Swagger/OpenAPI debe ser público
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            // 2. ENDPOINTS DE CÁLCULO (Uso)
            // Clientes y Operadores pueden pedir estimaciones
            .requestMatchers(HttpMethod.POST, "/api/tarifas/calcular/estimado").hasAnyRole("CLIENTE", "OPERADOR")
            // Operadores y Transportistas disparan el cálculo real
            .requestMatchers(HttpMethod.POST, "/api/tarifas/calcular/real").hasAnyRole("OPERADOR", "TRANSPORTISTA")

            // 3. ENDPOINTS DE GESTIÓN (Administración)
            
            // --- /api/tarifas/configuracion ---
            // Operadores y Clientes pueden VER la configuración (para estimar)
            .requestMatchers(HttpMethod.GET, "/api/tarifas/configuracion").hasAnyRole("CLIENTE", "OPERADOR")
            // Solo Operadores pueden MODIFICAR la configuración
            .requestMatchers(HttpMethod.PUT, "/api/tarifas/configuracion").hasRole("OPERADOR")

            // --- /api/tarifas/volumen ---
            // Operadores y Clientes pueden VER las tarifas por volumen
            .requestMatchers(HttpMethod.GET, "/api/tarifas/volumen").hasAnyRole("CLIENTE", "OPERADOR")
            // Solo Operadores pueden CREAR, MODIFICAR y BORRAR tarifas por volumen
            .requestMatchers(HttpMethod.POST, "/api/tarifas/volumen").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PUT, "/api/tarifas/volumen/**").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.DELETE, "/api/tarifas/volumen/**").hasRole("OPERADOR")
            
            // 4. Cualquier otra petición no definida, por seguridad, debe estar autenticada.
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
     * y los convierte al formato "ROLE_NOMBRE_ROL" que Spring Security espera [cite: 597-598].
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
                    // El prefijo "ROLE_" es OBLIGATORIO para Spring Security [cite: 597-598]
                    .map(roleName -> "ROLE_" + roleName.toUpperCase()) 
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

                return new JwtAuthenticationToken(jwt, authorities);
            }
        };
    }
}