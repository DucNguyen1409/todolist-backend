package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Find user in database by userName
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userName -> userRepository.findByEmail(userName)
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Provider authentication with DB information
     *
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // data access object
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // fetch info about user in DB
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        
        return authenticationProvider;
    }

    /**
     * Authentication manager
     *
     * @param authenticationConfig AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception ex
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig)
            throws Exception {
        return authenticationConfig.getAuthenticationManager();
    }

    /**
     * Password encoder by Bcrypt
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
