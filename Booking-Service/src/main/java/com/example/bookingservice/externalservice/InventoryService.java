package com.example.bookingservice.externalservice;

import com.example.bookingservice.model.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryService {

    @GetMapping("api/rooms/get_room_by_roomId/{roomId}")
    Room getRoomByRoomId(@PathVariable Long roomId);

    @PutMapping("api/rooms/update_room_status/{roomId}")
    void updateRoomStatus(@PathVariable Long roomId);
}
