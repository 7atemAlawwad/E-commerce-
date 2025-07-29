package com.example.ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    @NotEmpty(message = "ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "id must contain digits only")
    private String id;

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 3, message = "Name have to be more than 3 length")
    private String name;

    @NotNull(message = "Price must not be empty")
    @Positive(message = "Price must be positive number")
    private double price;

    @NotEmpty(message = "Category ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "Category ID must contain digits only")
    private String categoryID;
}
