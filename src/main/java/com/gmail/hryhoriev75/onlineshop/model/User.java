package com.gmail.hryhoriev75.onlineshop.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    @NonNull
    long id;
    @NonNull
    String name;
    @NonNull
    String email;
    @NonNull
    String password;
    @NonNull
    String salt;
    String phoneNumber;
    @NonNull
    String role; // "user" or "admin"
    @NonNull
    boolean blocked;

}
