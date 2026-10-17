package dev.lebenkov.finly.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

    @NotBlank(message = "Account name must not be blank")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    String name;

    @NotBlank(message = "Currency code must not be blank")
    @Size(min = 3, max = 3, message = "Currency code must contain 3 characters")
    String currency;

    @NotNull(message = "Balance must not be null")
    @DecimalMin(value = "0.0", message = "Balance must not be negative")
    BigDecimal balance;
}
