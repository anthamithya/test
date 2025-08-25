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
                .anyRequest().permitAll() // ALL requests to your app are allowed without auth
            )
            .csrf().disable() // optional: disable CSRF for APIs
            .httpBasic().disable() // optional: disable basic auth
            .formLogin().disable(); // optional: disable default login page

        return http.build();
    }
}
