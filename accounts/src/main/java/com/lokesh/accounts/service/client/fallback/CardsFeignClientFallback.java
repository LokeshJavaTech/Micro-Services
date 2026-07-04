package com.lokesh.accounts.service.client.fallback;

import com.lokesh.accounts.dto.CardsDto;
import com.lokesh.accounts.service.client.CardsFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFeignClientFallback implements CardsFeignClient {

    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String mobileNumber) {
        return ResponseEntity.ok(new CardsDto());
    }
}
