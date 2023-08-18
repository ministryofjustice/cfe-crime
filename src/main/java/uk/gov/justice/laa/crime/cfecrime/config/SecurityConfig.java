package uk.gov.justice.laa.crime.cfecrime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    including oauth2 in the project attempts to secure *our* endpoints with oauth2, when what we want to do
    // is just *talk to* an oauth2-secured endpoint (and not be secured ourselves) so we have to disable it
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());
        return http.build();
    }
}
