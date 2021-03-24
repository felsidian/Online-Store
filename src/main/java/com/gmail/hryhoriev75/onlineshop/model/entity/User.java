package com.gmail.hryhoriev75.onlineshop.model.entity;

import java.util.List;
import java.util.Objects;

public class User extends Entity {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private boolean blocked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public enum Role {
        ADMIN, USER;

        public boolean isUser() {
            return this == USER;
        }
        public boolean isAdmin() {
            return this == ADMIN;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
