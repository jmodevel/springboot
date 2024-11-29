package com.jmo.devel.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Schema(
    name="Customer",
    description = "Schema to hold Customer and Account information"
)
@Data
@Builder
public class CustomerDto {

    @Schema(
        description = "Name of the customer",
        example = "JMO Devel"
    )
    @NotEmpty( message = "name cannot be null or empty" )
    @Size( min=5, max=30, message = "name must be between 5 and 30 characters")
    private String     name;

    @Schema(
        description = "Email of the customer",
        example = "jmo.devel@gmail.com"
    )
    @NotEmpty( message = "email cannot be null or empty" )
    @Email( message = "email is not valid" )
    private String     email;

    @Schema(
        description = "Mobile number of the customer",
        example = "612345789"
    )
    @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
    private String     mobileNumber;

    @Schema(
        description = "Account details of the customer"
    )
    private AccountDto account;

}
