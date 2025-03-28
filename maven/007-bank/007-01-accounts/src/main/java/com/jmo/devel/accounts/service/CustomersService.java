package com.jmo.devel.accounts.service;

import com.jmo.devel.accounts.dto.CardDto;
import com.jmo.devel.accounts.dto.CustomerDetailsDto;
import com.jmo.devel.accounts.dto.LoanDto;
import com.jmo.devel.accounts.exception.ResourceNotFoundException;
import com.jmo.devel.accounts.mapper.AccountsMapper;
import com.jmo.devel.accounts.mapper.CustomersMapper;
import com.jmo.devel.accounts.model.Account;
import com.jmo.devel.accounts.model.Customer;
import com.jmo.devel.accounts.repository.AccountsRepository;
import com.jmo.devel.accounts.repository.CustomersRepository;
import com.jmo.devel.accounts.service.client.CardsFeignClient;
import com.jmo.devel.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersService implements ICustomersService {

    private AccountsRepository  accountsRepository;
    private CustomersRepository customersRepository;
    private CardsFeignClient    cardsFeignClient;
    private LoansFeignClient    loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {

        Customer customer = customersRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "customer", "mobileNumber", mobileNumber )
        );
        Account account = accountsRepository.findByCustomerId( customer.getCustomerId() ).orElseThrow(
            () -> new ResourceNotFoundException( "account", "customerId", customer.getCustomerId() )
        );

        CustomerDetailsDto customerDetailsDto = CustomersMapper.toCustomerDetailsDto( customer );
        customerDetailsDto.setAccount( AccountsMapper.toAccountDto( account ) );

        ResponseEntity<LoanDto> loanDtoResponseEntity = loansFeignClient.fetchLoanDetails( mobileNumber );
        customerDetailsDto.setLoan( loanDtoResponseEntity.getBody() );

        ResponseEntity<CardDto> cardDtoResponseEntity = cardsFeignClient.fetchCardDetails( mobileNumber );
        customerDetailsDto.setCard( cardDtoResponseEntity.getBody() );

        return customerDetailsDto;

    }

}
