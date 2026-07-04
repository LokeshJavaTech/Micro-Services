package com.lokesh.accounts.service.client.fallback;

import com.lokesh.accounts.dto.LoansDto;
import com.lokesh.accounts.service.client.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFeignClientFallback implements LoansFeignClient {

    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber) {
        return ResponseEntity.ok(new LoansDto());
    }
}
