package com.jmo.devel.loans.mapper;

import com.jmo.devel.loans.dto.LoanDto;
import com.jmo.devel.loans.model.Loan;

public class LoansMapper {

    public static LoanDto toLoanDto( Loan loan ) {
        return LoanDto.builder()
            .loanNumber       ( loan.getLoanNumber()        )
            .loanType         ( loan.getLoanType()          )
            .mobileNumber     ( loan.getMobileNumber()      )
            .totalLoan        ( loan.getTotalLoan()         )
            .amountPaid       ( loan.getAmountPaid()        )
            .outstandingAmount( loan.getOutstandingAmount() )
            .build();
    }

    public static Loan toLoan( LoanDto loanDto ) {
        return Loan.builder()
            .loanNumber       ( loanDto.getLoanNumber()        )
            .loanType         ( loanDto.getLoanType()          )
            .mobileNumber     ( loanDto.getMobileNumber()      )
            .totalLoan        ( loanDto.getTotalLoan()         )
            .amountPaid       ( loanDto.getAmountPaid()        )
            .outstandingAmount( loanDto.getOutstandingAmount() )
            .build();
    }

    public static Loan updateLoan( Loan loan, LoanDto loanDto ) {
        loan.setLoanNumber       ( loanDto.getLoanNumber()        );
        loan.setLoanType         ( loanDto.getLoanType()          );
        loan.setMobileNumber     ( loanDto.getMobileNumber()      );
        loan.setTotalLoan        ( loanDto.getTotalLoan()         );
        loan.setAmountPaid       ( loanDto.getAmountPaid()        );
        loan.setOutstandingAmount( loanDto.getOutstandingAmount() );
        return loan;
    }

}
