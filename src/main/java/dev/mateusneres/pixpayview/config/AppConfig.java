package dev.mateusneres.pixpayview.config;

import dev.mateusneres.pixpayview.entities.User;
import dev.mateusneres.pixpayview.enums.Role;
import dev.mateusneres.pixpayview.repositories.UserRepository;
import dev.mateusneres.pixpayview.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.existsUserByEmail("admin@pixpayview.com")) return;

            User user = new User("admin@pixpayview.com",
                    "Admin PixPayView",
                    Role.ROLE_ADMIN,
                    bCryptPasswordEncoder().encode("pixpayview010101"),
                    new Timestamp(new Date().getTime()));

            userRepository.save(user);

            JwtUserDetails jwtUserDetails = new JwtUserDetails(
                    user.getUserID(),
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
            userDetailsService().createUser(jwtUserDetails);
        };
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

}