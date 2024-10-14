package in.mannvender.splitwise.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.crypto.SecretKey;

@Configuration
public class SpringSecurity {
    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurity(@Lazy JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/login", "/auth/signup", "/auth/validate-token").permitAll() // Open endpoints
            .anyRequest().authenticated() // Secure all other endpoints
        );
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3030");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecretKey getSecretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        return secretKey;
    }
}