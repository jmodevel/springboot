package com.jmo.devel.loans.service;

import com.jmo.devel.loans.constants.LoansConstants;
import com.jmo.devel.loans.dto.LoanDto;
import com.jmo.devel.loans.exception.LoanAlreadyExistsException;
import com.jmo.devel.loans.exception.ResourceNotFoundException;
import com.jmo.devel.loans.mapper.LoansMapper;
import com.jmo.devel.loans.model.Loan;
import com.jmo.devel.loans.repository.LoansRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansService implements ILoansService {

    private LoansRepository loansRepository;

    @Override
    public void createLoan( String mobileNumber ) {
        Optional<Loan> optionalLoan= loansRepository.findByMobileNumber( mobileNumber );
        if( optionalLoan.isPresent() ){
            throw new LoanAlreadyExistsException( "Loan already registered with given mobileNumber " + mobileNumber );
        }
        loansRepository.save( createNewLoan( mobileNumber ) );
    }

    private Loan createNewLoan( String mobileNumber ) {
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber       ( Long.toString( randomLoanNumber ) );
        newLoan.setMobileNumber     ( mobileNumber                      );
        newLoan.setLoanType         ( LoansConstants.HOME_LOAN          );
        newLoan.setTotalLoan        ( LoansConstants.NEW_LOAN_LIMIT     );
        newLoan.setAmountPaid       ( 0                                 );
        newLoan.setOutstandingAmount( LoansConstants.NEW_LOAN_LIMIT     );
        return newLoan;
    }

    @Override
    public LoanDto fetchLoan( String mobileNumber ) {
        Loan loan = loansRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "loan", "mobileNumber", mobileNumber )
        );
        return LoansMapper.toLoanDto( loan );
    }

    @Override
    public boolean updateLoan( LoanDto loanDto ) {
        Loan loan = loansRepository.findByLoanNumber( loanDto.getLoanNumber() ).orElseThrow(
            () -> new ResourceNotFoundException( "loan", "loanNumber", loanDto.getLoanNumber() )
        );
        loansRepository.save( LoansMapper.updateLoan( loan, loanDto ) );
        return true;
    }

    @Override
    public boolean deleteLoan( String mobileNumber ) {
        Loan loans = loansRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "loan", "mobileNumber", mobileNumber )
        );
        loansRepository.deleteById( loans.getLoanId() );
        return true;
    }

}
