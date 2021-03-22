package com.gmail.hryhoriev75.onlineshop.model.entity;

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
    String phoneNumber;
    @NonNull
    String roleName; // "user" or "admin"
    @NonNull
    boolean blocked;

}
