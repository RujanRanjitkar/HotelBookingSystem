package com.example.inventoryservice.room.service;

import com.example.inventoryservice.customExceptions.ResourceNotFoundException;
import com.example.inventoryservice.hotel.model.Hotel;
import com.example.inventoryservice.hotel.repo.HotelRepo;
import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.enums.RoomStatus;
import com.example.inventoryservice.room.model.Room;
import com.example.inventoryservice.room.model.RoomImage;
import com.example.inventoryservice.room.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final HotelRepo hotelRepo;

    @Override
    public void addNewRoom(RoomRequestDto roomRequestDto, String path, List<MultipartFile> roomImages) throws IOException {
        Hotel existingHotel = hotelRepo.findByHotelNameAndAddress_Area(roomRequestDto.getHotel().getHotelName(), roomRequestDto.getHotel().getAddress().getArea());

        if (existingHotel != null) {
            Room existingRoom = roomRepo.findByRoomNumber(roomRequestDto.getRoomNumber());
            if (existingRoom != null) {
                throw new ResourceNotFoundException("Room already exists");
            }

            Room room = new Room();

            room.setRoomNumber(roomRequestDto.getRoomNumber());
            room.setRoomType(roomRequestDto.getRoomType());
            room.setRoomStatus(RoomStatus.AVAILABLE);
            room.setPrice(roomRequestDto.getPrice());

            if (roomRequestDto.getHotel().getHotelName().equals(existingHotel.getHotelName())) {
                room.setHotel(existingHotel);
            } else {
                Hotel hotel = new Hotel();
                hotel.setHotelName(roomRequestDto.getHotel().getHotelName());
                hotel.setAddress(roomRequestDto.getHotel().getAddress());
                room.setHotel(hotel);
            }

            String relativePath = path + "images\\" + roomRequestDto.getHotel().getHotelName().replaceAll("\\s", "") + "\\" + roomRequestDto.getRoomNumber();
            String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;

            File filePath = new File(absolutePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            List<RoomImage> roomImageList = new ArrayList<>();

            for (MultipartFile image : roomImages) {
                Files.copy(image.getInputStream(), Paths.get(absolutePath, image.getOriginalFilename()));

                RoomImage roomImage = new RoomImage();
                roomImage.setImageUrl(relativePath + File.separator + image.getOriginalFilename());

                roomImageList.add(roomImage);
            }
            room.setRoomImages(roomImageList);

            roomRepo.save(room);
        } else {
            throw new ResourceNotFoundException("This hotel does not exists. Please check the hotel name and address again");
        }

    }

}
