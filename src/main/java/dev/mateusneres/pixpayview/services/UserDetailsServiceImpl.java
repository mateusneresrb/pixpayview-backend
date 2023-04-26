package dev.mateusneres.pixpayview.services;

import dev.mateusneres.pixpayview.entities.User;
import dev.mateusneres.pixpayview.repositories.UserRepository;
import dev.mateusneres.pixpayview.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final UserRepository userRepository;

    public void updateUserDetails(User user) {
        JwtUserDetails userDetails = (JwtUserDetails) loadUserByUsername(user.getEmail());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));

        inMemoryUserDetailsManager.updateUser(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            return null;
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        return new JwtUserDetails(
                user.getUserID(), user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

}
