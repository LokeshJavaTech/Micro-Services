package com.lokesh.accounts.service;

import com.lokesh.accounts.dto.AccountsDto;
import com.lokesh.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     * @param customerDto - CustomerDto object
     * @return AccountsDto
     */
    AccountsDto createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    String updateAccount(CustomerDto customerDto);

    String deleteAccount(String mobileNumber);
}
