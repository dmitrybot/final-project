package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security;

public enum Permission {
    BASE("base"),
    ADMIN("admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
