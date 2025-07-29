package com.example.ecommerce.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "ID must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "id must contain digits only")
    private String id;

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 3, message = "Name have to be more than 3 length")
    private String name;

    @FutureOrPresent(message = "vacationFrom must be in the future Or present")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate vacationFrom;

    @Future(message = "toDate must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate vacationTo;


}
