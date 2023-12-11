package dev.cafemanagement.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsUser = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        var userDetailsAdmin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();
        var userDetailsManager = User.withDefaultPasswordEncoder()
                .username("manager")
                .password("password")
                .roles("MANAGER")
                .build();
        return new InMemoryUserDetailsManager(userDetailsUser, userDetailsAdmin, userDetailsManager);
    }

    @Bean
    public SecurityConfigurer securityConfigurer() {
        return new SecurityConfigurer();
    }

    public static class SecurityConfigurer extends AbstractHttpConfigurer<SecurityConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/user/**").hasRole("USER")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/manager/**").hasRole("MANAGER")
                            .anyRequest().authenticated()
                    )
                    .httpBasic(withDefaults());
        }
    }
}
