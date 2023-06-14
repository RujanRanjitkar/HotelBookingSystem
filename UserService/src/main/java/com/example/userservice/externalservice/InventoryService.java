package com.example.userservice.externalservice;

import com.example.userservice.model.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryService {

    @GetMapping("api/hotels/get_hotel_by_hotel_owner_email/{email}")
    String getHotelByHotelOwnerEmail(@PathVariable String email);
}
