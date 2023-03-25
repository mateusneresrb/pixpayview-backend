package dev.mateusneres.pixpayviewbackend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class RolePrefixingMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(authority -> {
                    if (authority.getAuthority().startsWith("ROLE_")) {
                        return authority;
                    } else {
                        return new SimpleGrantedAuthority("ROLE_" + authority.getAuthority());
                    }
                })
                .collect(Collectors.toList());
    }

}
