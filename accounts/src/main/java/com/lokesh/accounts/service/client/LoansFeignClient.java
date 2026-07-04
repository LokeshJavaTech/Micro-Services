package com.lokesh.accounts.service.client;

import com.lokesh.accounts.dto.LoansDto;
import com.lokesh.accounts.service.client.fallback.LoansFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans-ms", fallback = LoansFeignClientFallback.class)
public interface LoansFeignClient
{
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
