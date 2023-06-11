package com.example.inventoryservice.room.controller;

import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Value(("${project.image}"))
    private String path;

    @PostMapping("/add_new_room")
    public ResponseEntity<String> addNewRoom(@RequestPart RoomRequestDto roomRequestDto, @RequestPart List<MultipartFile> roomImages) throws IOException {

        roomService.addNewRoom(roomRequestDto, path, roomImages);
        return ResponseEntity.ok("New Room Added Successfully");
    }
}
