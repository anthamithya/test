import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Permit your own internal/public endpoints
                .requestMatchers("/public/**", "/internal/**").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            // If using OAuth2 login
            .oauth2Login()
            .and()
            // If using OAuth2 resource server (JWT)
            //.oauth2ResourceServer().jwt()
            ;

        return http.build();
    }
}
