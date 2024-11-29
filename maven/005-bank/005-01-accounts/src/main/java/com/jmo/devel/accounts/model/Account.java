package com.jmo.devel.accounts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class Account extends BaseEntity {

    @Id
    private Long   accountNumber;
    private Long   customerId;
    private String accountType;
    private String branchAddress;

}
