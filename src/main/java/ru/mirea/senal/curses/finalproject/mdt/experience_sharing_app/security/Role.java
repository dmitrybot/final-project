package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {

    USER(new HashSet<>(Stream.of(Permission.BASE)
            .collect(Collectors.toList()))),
    ADMIN(new HashSet<>(Stream.of(Permission.BASE,
            Permission.ADMIN).collect(Collectors.toList())));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
