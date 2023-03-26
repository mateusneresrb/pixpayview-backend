package dev.mateusneres.pixpayviewbackend.security.jwt;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Setter
public class JwtUserDetails implements UserDetails {

    private final UUID uuid;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(UUID uuid, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



