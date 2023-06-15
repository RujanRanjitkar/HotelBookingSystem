package com.example.inventoryservice.room.service;

import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.dto.RoomResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    void addNewRoom(RoomRequestDto roomRequestDto, String path, List<MultipartFile> roomImages) throws IOException;

    RoomResponseDto getRoomByRoomId(Long roomId);

    void updateRoomStatus(Long roomId);

    void updateRoomInfo(Long roomId, RoomRequestDto roomRequestDto, String path, List<MultipartFile> roomImages, String authorizationHeader) throws IOException;
}
