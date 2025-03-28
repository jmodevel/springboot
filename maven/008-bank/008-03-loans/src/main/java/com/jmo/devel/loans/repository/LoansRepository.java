package com.jmo.devel.loans.repository;

import com.jmo.devel.loans.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByMobileNumber( String mobileNumber );
    Optional<Loan> findByLoanNumber( String loanNumber );

}
