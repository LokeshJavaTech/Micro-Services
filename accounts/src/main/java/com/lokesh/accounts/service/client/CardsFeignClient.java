package com.lokesh.accounts.service.client;

import com.lokesh.accounts.dto.CardsDto;
import com.lokesh.accounts.service.client.fallback.CardsFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards-ms", fallback = CardsFeignClientFallback.class)
public interface CardsFeignClient
{
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);
}
