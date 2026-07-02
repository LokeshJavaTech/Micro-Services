package com.lokesh.accounts.service;

import com.lokesh.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
