package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(UserEntity user, boolean b) {
        id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRole().getAuthorities().stream().collect(Collectors.toList());
        this.isActive = b;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    /*public static UserDetails fromUser(UserEntity user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getRole().getAuthorities()
        );
    }*/
}
