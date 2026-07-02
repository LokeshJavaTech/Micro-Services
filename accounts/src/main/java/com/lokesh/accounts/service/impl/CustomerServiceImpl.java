package com.lokesh.accounts.service.impl;

import com.lokesh.accounts.dto.AccountsDto;
import com.lokesh.accounts.dto.CardsDto;
import com.lokesh.accounts.dto.CustomerDetailsDto;
import com.lokesh.accounts.dto.LoansDto;
import com.lokesh.accounts.entity.Accounts;
import com.lokesh.accounts.entity.Customer;
import com.lokesh.accounts.exception.ResourceNotFoundException;
import com.lokesh.accounts.mapper.AccountsMapper;
import com.lokesh.accounts.mapper.CustomerMapper;
import com.lokesh.accounts.repository.AccountsRepository;
import com.lokesh.accounts.repository.CustomerRepository;
import com.lokesh.accounts.service.ICustomerService;
import com.lokesh.accounts.service.client.CardsFeignClient;
import com.lokesh.accounts.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapCustomerToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapAccountsToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
