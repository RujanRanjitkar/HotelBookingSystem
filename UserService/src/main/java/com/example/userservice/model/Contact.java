package com.example.userservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private Long contactId;
    private String contactNumber;
    private String email;
}
