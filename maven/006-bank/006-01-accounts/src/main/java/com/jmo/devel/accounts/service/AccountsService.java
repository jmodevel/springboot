package com.jmo.devel.accounts.service;

import com.jmo.devel.accounts.constants.AccountsConstants;
import com.jmo.devel.accounts.dto.AccountDto;
import com.jmo.devel.accounts.dto.CustomerDto;
import com.jmo.devel.accounts.mapper.AccountsMapper;
import com.jmo.devel.accounts.model.Account;
import com.jmo.devel.accounts.model.Customer;
import com.jmo.devel.accounts.repository.AccountsRepository;
import com.jmo.devel.accounts.repository.CustomersRepository;
import com.jmo.devel.accounts.exception.CustomerAlreadyExistsException;
import com.jmo.devel.accounts.exception.ResourceNotFoundException;
import com.jmo.devel.accounts.mapper.CustomersMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsService implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomersRepository customersRepository;

    @Override
    public void createAccount( CustomerDto customerDto ) {
        Optional<Customer> existing = customersRepository.findByMobileNumber( customerDto.getMobileNumber() );
        if( existing.isPresent() ) {
            throw new CustomerAlreadyExistsException( "Customer already registered with mobile number: " + customerDto.getMobileNumber() );
        }
        Customer customer = CustomersMapper.toCustomer( customerDto );
        accountsRepository.save(
            createAccount( customersRepository.save( customer ) )
        );
    }

    private Account createAccount(Customer customer ) {
        Account account = new Account();
        account.setAccountNumber(
            1000000000L + new Random().nextInt( 900000000 )
        );
        account.setCustomerId   ( customer.getCustomerId()    );
        account.setAccountType  ( AccountsConstants.SAVINGS   );
        account.setBranchAddress( AccountsConstants.ADDRESS   );
        return account;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customersRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "customer", "mobileNumber", mobileNumber )
        );
        Account account = accountsRepository.findByCustomerId( customer.getCustomerId() ).orElseThrow(
            () -> new ResourceNotFoundException( "account", "customerId", customer.getCustomerId() )
        );
        CustomerDto customerDto = CustomersMapper.toCustomerDto( customer );
        customerDto.setAccount( AccountsMapper.toAccountDto( account ) );
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean updated = false;
        AccountDto accountDto = customerDto.getAccount();
        if( accountDto != null ) {
            Account account = accountsRepository.findById( accountDto.getAccountNumber() ).orElseThrow(
                () -> new ResourceNotFoundException( "account", "accountNumber", accountDto.getAccountNumber() )
            );
            account = accountsRepository.save(
                AccountsMapper.updateAccount( account, accountDto )
            );
            Long customerId = account.getCustomerId();
            Customer customer = customersRepository.findById( customerId ).orElseThrow(
                () -> new ResourceNotFoundException( "customer", "customerId", customerId )
            );
            customersRepository.save(
                CustomersMapper.updateCustomer( customer, customerDto )
            );
            updated = true;
        }
        return updated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customersRepository.findByMobileNumber( mobileNumber ).orElseThrow(
            () -> new ResourceNotFoundException( "customer", "mobileNumber", mobileNumber )
        );
        accountsRepository.deleteByCustomerId( customer.getCustomerId() );
        customersRepository.delete( customer );
        return true;
    }

}
