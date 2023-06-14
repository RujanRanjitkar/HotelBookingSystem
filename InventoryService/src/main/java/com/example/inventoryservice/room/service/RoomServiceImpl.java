package com.example.inventoryservice.room.service;

import com.example.inventoryservice.customExceptions.ResourceNotFoundException;
import com.example.inventoryservice.hotel.dto.AddressResponseDto;
import com.example.inventoryservice.hotel.dto.ImageResponseDto;
import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.Hotel;
import com.example.inventoryservice.hotel.repo.HotelRepo;
import com.example.inventoryservice.room.dto.HotelRoomRequestDto;
import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.dto.RoomResponseDto;
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

    private List<RoomImage> saveRoomImage(List<MultipartFile> roomImages, String relativePath) throws IOException {

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

        return roomImageList;
    }

    @Override
    public void addNewRoom(RoomRequestDto roomRequestDto, String path, List<MultipartFile> roomImages) throws IOException {
        Hotel existingHotel = hotelRepo.findByHotelNameAndAddress_Area(roomRequestDto.getHotel().getHotelName(), roomRequestDto.getHotel().getAddress().getArea());

        if (existingHotel != null) {
            Room existingRoom = roomRepo.findByRoomNumberAndHotel_HotelId(roomRequestDto.getRoomNumber(), existingHotel.getHotelId());
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

                Address address = new Address();
                address.setArea(roomRequestDto.getHotel().getAddress().getArea());

                hotel.setAddress(address);
                room.setHotel(hotel);
            }

            String relativePath = path + "images\\" + roomRequestDto.getHotel().getHotelName().replaceAll("\\s", "") + "\\" + roomRequestDto.getRoomNumber();

            List<RoomImage> roomImageList = saveRoomImage(roomImages, relativePath);
            room.setRoomImages(roomImageList);

            roomRepo.save(room);
        } else {
            throw new ResourceNotFoundException("This hotel does not exists. Please check the hotel name and address again");
        }

    }

    @Override
    public RoomResponseDto getRoomByRoomId(Long roomId) {

        Room room = roomRepo.findById(roomId).get();

        RoomResponseDto roomResponseDto = new RoomResponseDto();
        roomResponseDto.setRoomNumber(room.getRoomNumber());
        roomResponseDto.setRoomType(room.getRoomType());
        roomResponseDto.setRoomStatus(room.getRoomStatus());

        List<RoomImage> roomImages = room.getRoomImages();

        List<ImageResponseDto> imageResponseDtoLists = new ArrayList<>();

        for (RoomImage roomImage : roomImages) {

            ImageResponseDto image = new ImageResponseDto();

            String imagePath = roomImage.getImageUrl().replace("InventoryService\\src\\main\\resources\\static\\", "");
            String finalImagePath = "http://localhost:8081/" + imagePath.replace("\\", "/");

            image.setImageUrl(finalImagePath);

            imageResponseDtoLists.add(image);
        }

        roomResponseDto.setRoomImages(imageResponseDtoLists);

        roomResponseDto.setPrice(room.getPrice());
        Hotel hotel = room.getHotel();

        HotelRoomRequestDto hotelRoomRequestDto = new HotelRoomRequestDto();

        hotelRoomRequestDto.setHotelName(hotel.getHotelName());

        AddressResponseDto addressResponse = new AddressResponseDto();
        addressResponse.setProvince(hotel.getAddress().getProvince());
        addressResponse.setCity(hotel.getAddress().getCity());
        addressResponse.setArea(hotel.getAddress().getArea());

        hotelRoomRequestDto.setAddress(addressResponse);

        roomResponseDto.setHotel(hotelRoomRequestDto);




        return roomResponseDto;
    }

    @Override
    public void updateRoomStatus(Long roomId) {

        Room room = roomRepo.findById(roomId).get();

        room.setRoomStatus(RoomStatus.BOOKED);

        roomRepo.save(room);
    }

    @Override
    public void updateRoomInfo(Long roomId, RoomRequestDto roomRequestDto, String path, List<MultipartFile> roomImages) throws IOException {

        Room existingRoom = roomRepo.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room doest not exists"));

        if(existingRoom!=null){
            Hotel hotel= existingRoom.getHotel();
            existingRoom.setHotel(hotel);
        }

        String relativePath = path + "images\\" + existingRoom.getHotel().getHotelName().replaceAll("\\s", "") + "\\" + roomRequestDto.getRoomNumber();

        List<RoomImage> roomImageList = saveRoomImage(roomImages, relativePath);

        List<RoomImage> existingRoomImageList = existingRoom.getRoomImages();
        existingRoomImageList.addAll(roomImageList);

        existingRoom.setRoomNumber(roomRequestDto.getRoomNumber());
        existingRoom.setRoomType(roomRequestDto.getRoomType());
        existingRoom.setPrice(roomRequestDto.getPrice());

        roomRepo.save(existingRoom);
    }

}
