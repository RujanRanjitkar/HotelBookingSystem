package com.example.inventoryservice.room.model;

import com.example.inventoryservice.hotel.model.Hotel;
import com.example.inventoryservice.room.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomNumber;
    private String roomType;
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;
    private double price;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private List<RoomImage> roomImages;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
