package com.lokesh.accounts.service.impl;

import com.lokesh.accounts.dto.AccountsDto;
import com.lokesh.accounts.dto.CustomerDto;
import com.lokesh.accounts.entity.Accounts;
import com.lokesh.accounts.entity.Customer;
import com.lokesh.accounts.exception.CustomerAlreadyExistsException;
import com.lokesh.accounts.exception.ResourceNotFoundException;
import com.lokesh.accounts.mapper.AccountsMapper;
import com.lokesh.accounts.mapper.CustomerMapper;
import com.lokesh.accounts.repository.AccountsRepository;
import com.lokesh.accounts.repository.CustomerRepository;
import com.lokesh.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;

    @Override
    public AccountsDto createAccount(CustomerDto customerDto) {

        Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        existingCustomer.ifPresent(customer -> {
            throw new CustomerAlreadyExistsException(
                    String.format("Customer %s already exist with phone %s", customer.getName(), customerDto.getMobileNumber()));
        });

        Customer customer = CustomerMapper.mapCustomerDtoToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        Accounts accounts = new Accounts();
        accounts.setCustomerId(savedCustomer.getCustomerId());
        accounts.setAccountType("SAVINGS");
        accounts.setAccountNumber(new Random().nextLong(1_000_000_000));
        accounts.setBranchAddress("101, Montreal Downtown, Quebec, Canada");
        accountsRepository.save(accounts);

        return AccountsMapper.mapAccountsToAccountsDto(accounts, new AccountsDto());
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapCustomerToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapAccountsToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public String updateAccount(CustomerDto customerDto) {
        if (customerDto.getAccountsDto() != null) {
            Accounts accounts = accountsRepository.findById(customerDto.getAccountsDto().getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Accounts",
                            "accountNumber",
                            String.valueOf(customerDto.getAccountsDto().getAccountNumber())));

            Customer customer = customerRepository.findById(accounts.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer",
                            "customerId",
                            String.valueOf(accounts.getCustomerId())));

            AccountsMapper.mapAccountsDtoToAccounts(customerDto.getAccountsDto(), accounts);
            CustomerMapper.mapCustomerDtoToCustomer(customerDto, customer);
            accountsRepository.save(accounts);
            customerRepository.save(customer);

            return String.format("Account %s and customer %s updated successfully!", accounts.getAccountNumber(), customer.getCustomerId());
        } else {
            throw new IllegalArgumentException("Please add accountsDto parameter in request payload!");
        }
    }

    @Override
    public String deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));
        customerRepository.delete(customer);
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        return String.format("Customer %d deleted successfully with mobile number %s", customer.getCustomerId(), customer.getMobileNumber());
    }
}
