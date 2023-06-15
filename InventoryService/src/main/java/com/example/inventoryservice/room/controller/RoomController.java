package com.example.inventoryservice.room.controller;

import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.dto.RoomResponseDto;
import com.example.inventoryservice.room.service.RoomService;
import com.example.inventoryservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final JwtUtil jwtUtil;

    @Value(("${project.image}"))
    private String path;

    @PostMapping("/add_new_room")
    public ResponseEntity<String> addNewRoom(@Valid @RequestPart RoomRequestDto roomRequestDto, @RequestPart List<MultipartFile> roomImages) throws IOException {

        roomService.addNewRoom(roomRequestDto, path, roomImages);
        return new ResponseEntity<>("New Room Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get_room_by_roomId/{roomId}")
    public ResponseEntity<RoomResponseDto> getRoomByRoomId(@PathVariable Long roomId) {

        RoomResponseDto roomResponseDto = roomService.getRoomByRoomId(roomId);
        return new ResponseEntity<>(roomResponseDto, HttpStatus.OK);
    }

    @PutMapping("/update_room_status/{roomId}")
    public ResponseEntity<String> updateRoomStatus(@PathVariable Long roomId) {

        roomService.updateRoomStatus(roomId);
        return new ResponseEntity<>("Updated Room Status Successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update_roomInfo/{roomId}")
    public ResponseEntity<String> updateRoomInfo(@PathVariable Long roomId, @RequestPart RoomRequestDto roomRequestDto, @RequestPart List<MultipartFile> roomImages, @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        String token = authorizationHeader.substring(7); //Remove "Bearer " from token
        String userName = jwtUtil.extractUserName(token);

        roomService.updateRoomInfo(roomId, roomRequestDto, path, roomImages, userName);
        return new ResponseEntity<>("Room Updated Successfully", HttpStatus.CREATED);
    }
}
