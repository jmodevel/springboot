package com.jmo.devel.accounts.service;

import com.jmo.devel.accounts.dto.CustomerDto;

public interface IAccountsService {

    void        createAccount( CustomerDto customerDto );
    CustomerDto fetchAccount ( String mobileNumber );
    boolean     updateAccount( CustomerDto customerDto );
    boolean     deleteAccount( String mobileNumber );

}
