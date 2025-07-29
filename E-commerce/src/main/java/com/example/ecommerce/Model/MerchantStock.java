package com.example.ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "id must contain digits only")
    private String id;

    @NotEmpty(message = "Product ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "Product id must contain digits only")
    private String productid;

    @NotEmpty(message = "Merchant ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "Merchant id must contain digits only")
    private String merchantid;

    @NotNull(message = "Stock must not be empty")
    @Min(value = 10, message = "Stock have to be more than 10 at start")
    private int stock;
}
