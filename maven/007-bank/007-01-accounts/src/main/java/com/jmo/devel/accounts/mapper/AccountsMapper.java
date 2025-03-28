package com.jmo.devel.accounts.mapper;

import com.jmo.devel.accounts.dto.AccountDto;
import com.jmo.devel.accounts.model.Account;

public class AccountsMapper {

    public static AccountDto toAccountDto(Account account ) {
        return AccountDto.builder()
            .accountNumber( account.getAccountNumber() )
            .accountType  ( account.getAccountType()   )
            .branchAddress( account.getBranchAddress() )
            .build();
    }

    public static Account toAccount( AccountDto accountDto ) {
        return Account.builder()
            .accountNumber( accountDto.getAccountNumber() )
            .accountType  ( accountDto.getAccountType()   )
            .branchAddress( accountDto.getBranchAddress() )
            .build();
    }

    public static Account updateAccount( Account account, AccountDto accountDto ) {
        account.setAccountNumber( accountDto.getAccountNumber() );
        account.setAccountType  ( accountDto.getAccountType()   );
        account.setBranchAddress( accountDto.getBranchAddress() );
        return account;
    }

}
