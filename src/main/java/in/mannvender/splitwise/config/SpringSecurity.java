package in.mannvender.splitwise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpsSecurity) throws Exception {
        httpsSecurity.cors().disable();
        httpsSecurity.csrf().disable();
        httpsSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return httpsSecurity.build();
    }
}
