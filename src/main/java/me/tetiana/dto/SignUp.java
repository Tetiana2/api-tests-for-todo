package me.tetiana.dto;

import lombok.Value;

@Value
public class SignUp {
    String userId = "unique()";
    String name;
    String email;
    String password;
}
