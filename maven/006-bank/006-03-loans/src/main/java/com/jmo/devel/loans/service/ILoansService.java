package com.jmo.devel.loans.service;

import com.jmo.devel.loans.dto.LoanDto;

public interface ILoansService {

    void    createLoan( String mobileNumber );
    LoanDto fetchLoan ( String mobileNumber );
    boolean updateLoan( LoanDto loanDto );
    boolean deleteLoan( String mobileNumber );

}
