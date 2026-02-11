package ar.edu.utn.frc.distancias.config;

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
     * Este Bean protege los endpoints del microservicio de Distancias.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(authorize -> authorize
            
            // 1. Swagger/OpenAPI debe ser público
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

            // 2. ENDPOINT DE DISTANCIA
            // Todos los roles del sistema necesitan calcular distancias
            .requestMatchers(HttpMethod.GET, "/api/distancia").hasAnyRole("CLIENTE", "OPERADOR", "TRANSPORTISTA")
            
            // 3. Cualquier otra petición no definida, por seguridad, debe estar autenticada.
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