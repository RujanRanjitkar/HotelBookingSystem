package com.example.inventoryservice.room.repo;

import com.example.inventoryservice.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room, Long> {

    Room findByRoomNumberAndHotel_HotelId(String roomNumber, Long hotelId);
}
