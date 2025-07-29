package com.example.ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "id must contain digits only")
    private String id;

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 6, message = "Name have to be more than 5 length")
    private String username;


    @NotBlank(message = "password is required")
    @Size(min = 7, message = "password must be more than 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$", message = "password must contain at least one letter and one digit")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Must be valid email")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "^(Admin|Customer)$" , message = "role must be either 'Admin' or 'Customer'")
    private String role;

    @NotNull(message = "Balance must not be empty")
    @PositiveOrZero(message = "Balance must be positive or zero")
    private double balance;

    private int points;


}
