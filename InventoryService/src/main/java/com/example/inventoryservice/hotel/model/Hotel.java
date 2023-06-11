package com.example.inventoryservice.hotel.model;

import com.example.inventoryservice.room.model.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;
    private String hotelName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    private String description;

    @Column(updatable = false)
    private String createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_id")
    private List<HotelImage> hotelImages;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_id")
    private List<Contact> contacts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Room> rooms;
}
