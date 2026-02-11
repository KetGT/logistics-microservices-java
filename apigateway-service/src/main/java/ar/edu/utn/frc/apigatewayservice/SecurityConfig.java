package ar.edu.utn.frc.apigatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        
        http.authorizeExchange(exchange -> exchange
            // 1. Permite el acceso a la UI de Swagger
            .pathMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
            .pathMatchers("/webjars/swagger-ui/index.html").permitAll()
            
            // 2. Permite el acceso a los 'api-docs' de los microservicios
            .pathMatchers("/v3/api-docs/**").permitAll() 
            .pathMatchers("/*/v3/api-docs/**").permitAll()
            
            // 3. Tus rutas protegidas
            .pathMatchers("/api/solicitudes/**").authenticated()
            .pathMatchers("/api/rutas/**").authenticated()
            .pathMatchers("/api/distancias/**").authenticated()
            .pathMatchers("/api/tarifas/**").authenticated()
            .anyExchange().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        return http.build();
    }

    /**
     * SOLUCIÓN AL CATCH-22:
     * 1. El @Bean es PÚBLICO (para que 'mvn clean package' funcione)
     * 2. Devuelve una CLASE INTERNA (para que el 'runtime' de Docker funcione)
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        // Ya no usamos una lambda, sino una clase concreta.
        return new JwtAuthConverter(); 
    }

    /**
     * Esta clase interna reemplaza a la lambda.
     * Spring SÍ puede leer los tipos genéricos (<Jwt, Mono<...>>) de esta clase,
     * solucionando el error 'IllegalArgumentException' en Docker.
     */
    private static class JwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

        @Override
        public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
            Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null || realmAccess.isEmpty()) {
                return Mono.just(new JwtAuthenticationToken(jwt, List.of()));
            }

            List<GrantedAuthority> authorities = realmAccess.get("roles")
                    .stream()
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        }
    }
}