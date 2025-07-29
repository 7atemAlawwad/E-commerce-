package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    @NotEmpty(message = "ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "id must contain digits only")
    private String id;

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 3, message = "Name have to be more than 3 length")
    private String name;
}
