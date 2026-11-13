package kh.edu.istad.iam.config;

import kh.edu.istad.iam.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities != null ? new ArrayList<>(authorities) : List.of();
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : List.of();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getAccountNonExpired());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getAccountNonLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getCredentialsNonExpired());
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomUserDetails that) {
            return this.user.getUsername().equals(that.user.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return user.getUsername().hashCode();
    }
}
