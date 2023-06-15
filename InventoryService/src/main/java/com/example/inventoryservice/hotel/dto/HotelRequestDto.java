package com.example.inventoryservice.hotel.dto;

import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.Contact;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelRequestDto {
    @NotBlank(message = "Hotel name cannot be blank")
    private String hotelName;
    private Address address;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private String createdBy;
    private List<Contact> contacts;
    private String hotelOwnerEmail;
}
