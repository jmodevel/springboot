package com.jmo.devel.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Schema(
    name = "Account",
    description = "Schema to hold account information"
)
@Data
@Builder
public class AccountDto {

    @Schema(
        description = "Account number",
        example = "123456789"
    )
    @NotEmpty( message = "account number cannot be null or empty" )
    @Pattern ( regexp = "^[0-9]{9}$", message = "account number is not valid" )
    private Long   accountNumber;

    @Schema(
        description = "Account type",
        example = "Savings"
    )
    @NotEmpty( message = "customer id cannot be null or empty" )
    private String accountType;

    @Schema(
        description = "Branch address",
        example = "123 Main Street, New York, NY 10001"
    )
    @NotEmpty( message = "branch address cannot be null or empty" )
    private String branchAddress;

}
