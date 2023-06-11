package com.example.inventoryservice.hotel.service;

import com.example.inventoryservice.customExceptions.ResourceAlreadyExistsException;
import com.example.inventoryservice.hotel.dto.*;
import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.Contact;
import com.example.inventoryservice.hotel.model.Hotel;
import com.example.inventoryservice.hotel.model.HotelImage;
import com.example.inventoryservice.hotel.repo.HotelRepo;
import com.example.inventoryservice.room.dto.RoomResponseDto;
import com.example.inventoryservice.room.model.Room;
import com.example.inventoryservice.room.model.RoomImage;
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
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;

    private HotelResponseDto convertToHotelResponseDto(Hotel hotel) {

        HotelResponseDto hotelResponseDto = new HotelResponseDto();

        hotelResponseDto.setHotelName(hotel.getHotelName());

        Address address = hotel.getAddress();
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setProvince(address.getProvince());
        addressResponseDto.setCity(address.getCity());
        addressResponseDto.setArea(address.getArea());

        hotelResponseDto.setAddress(addressResponseDto);
        hotelResponseDto.setDescription(hotel.getDescription());

        List<HotelImage> hotelImages = hotel.getHotelImages();

        List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

        for (HotelImage hotelImage : hotelImages) {

            ImageResponseDto image = new ImageResponseDto();

            String imagePath = hotelImage.getImageUrl().replace("InventoryService\\src\\main\\resources\\static\\", "");
            String finalImagePath = "http://localhost:8081/" + imagePath.replace("\\", "/");

            image.setImageUrl(finalImagePath);

            imageResponseDtoList.add(image);
        }

        hotelResponseDto.setHotelImages(imageResponseDtoList);

        List<Contact> contactList = hotel.getContacts();

        List<ContactResponseDto> contactResponseDtoList = new ArrayList<>();

        for (Contact contact : contactList) {

            ContactResponseDto contactResponseDto = new ContactResponseDto();

            contactResponseDto.setContactNumber(contact.getContactNumber());
            contactResponseDto.setEmail(contact.getEmail());

            contactResponseDtoList.add(contactResponseDto);
        }

        hotelResponseDto.setContacts(contactResponseDtoList);

        List<Room> roomList = hotel.getRooms();

        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();

        for (Room room : roomList) {

            RoomResponseDto roomResponseDto = new RoomResponseDto();

            roomResponseDto.setRoomNumber(room.getRoomNumber());
            roomResponseDto.setRoomType(room.getRoomType());
            roomResponseDto.setRoomStatus(room.getRoomStatus());
            roomResponseDto.setPrice(room.getPrice());

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

            roomResponseDtoList.add(roomResponseDto);
        }

        hotelResponseDto.setRooms(roomResponseDtoList);

        return hotelResponseDto;
    }

    @Override
    public void addNewHotel(HotelRequestDto hotelRequestDto, String path, List<MultipartFile> hotelImages) throws IOException {

        Hotel existingHotel = hotelRepo.findByHotelNameAndAddress_Area(hotelRequestDto.getHotelName(), hotelRequestDto.getAddress().getArea());

        if (existingHotel != null) {
            throw new ResourceAlreadyExistsException("This hotel already exists. Please confirm the hotel name and address again!!");
        }

        Hotel hotel = new Hotel();

        hotel.setHotelName(hotelRequestDto.getHotelName());
        hotel.setAddress(hotelRequestDto.getAddress());
        hotel.setDescription(hotelRequestDto.getDescription());
        hotel.setCreatedBy(hotelRequestDto.getCreatedBy());

        String relativePath = path + "images\\" + hotelRequestDto.getHotelName().replaceAll("\\s", "");
        // returns the current working directory of user
        String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;

        File filePath = new File(absolutePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        List<HotelImage> hotelImageList = new ArrayList<>();

        for (MultipartFile image : hotelImages) {
            Files.copy(image.getInputStream(), Paths.get(absolutePath, image.getOriginalFilename()));

            HotelImage hotelImage = new HotelImage();
            hotelImage.setImageUrl(relativePath + File.separator + image.getOriginalFilename());

            hotelImageList.add(hotelImage);
        }

        hotel.setHotelImages(hotelImageList);

        List<Contact> contactList = hotelRequestDto.getContacts();
        hotel.setContacts(contactList);

        hotelRepo.save(hotel);
    }

    @Override
    public List<HotelResponseDto> getAllHotels() {
        List<Hotel> hotels = hotelRepo.findAll();

        List<HotelResponseDto> hotelResponseDtoList = new ArrayList<>();

        for (Hotel hotel : hotels) {
            HotelResponseDto hotelResponseDto = convertToHotelResponseDto(hotel);

            hotelResponseDtoList.add(hotelResponseDto);
        }

        return hotelResponseDtoList;
    }

    @Override
    public HotelResponseDto getHotelByHotelId(Long hotelId) {

        Hotel hotel = hotelRepo.findById(hotelId).get();

        HotelResponseDto hotelResponseDto = convertToHotelResponseDto(hotel);

        return hotelResponseDto;
    }

    @Override
    public List<HotelResponseDto> getHotelsByLocation(String location) {
        List<Hotel> hotels = hotelRepo.findByAddress_Area(location);

        List<HotelResponseDto> hotelResponseDtoList = new ArrayList<>();

        for (Hotel hotel : hotels) {

            HotelResponseDto hotelResponseDto = convertToHotelResponseDto(hotel);

            hotelResponseDtoList.add(hotelResponseDto);
        }

        return hotelResponseDtoList;
    }
}
