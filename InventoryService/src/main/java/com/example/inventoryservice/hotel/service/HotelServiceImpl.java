package com.example.inventoryservice.hotel.service;

import com.example.inventoryservice.customExceptions.ResourceAlreadyExistsException;
import com.example.inventoryservice.customExceptions.ResourceNotFoundException;
import com.example.inventoryservice.hotel.dto.*;
import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.Contact;
import com.example.inventoryservice.hotel.model.Hotel;
import com.example.inventoryservice.hotel.model.HotelImage;
import com.example.inventoryservice.hotel.repo.HotelRepo;
import com.example.inventoryservice.room.dto.RoomResponseDto;
import com.example.inventoryservice.room.model.Room;
import com.example.inventoryservice.room.model.RoomImage;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            ImageResponseDto myHotelImage = new ImageResponseDto();

            String imagePath = hotelImage.getImageUrl().replace("InventoryService\\src\\main\\resources\\static\\", "");
            String finalImagePath = "http://localhost:8081/" + imagePath.replace("\\", "/");

            myHotelImage.setImageId(hotelImage.getImageId());
            myHotelImage.setImageUrl(finalImagePath);

            imageResponseDtoList.add(myHotelImage);
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

        //owner email
        hotelResponseDto.setHotelOwnerEmail(hotel.getHotelOwnerEmail());

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

                ImageResponseDto myRoomImage = new ImageResponseDto();

                String imagePath = roomImage.getImageUrl().replace("InventoryService\\src\\main\\resources\\static\\", "");
                String finalImagePath = "http://localhost:8081/" + imagePath.replace("\\", "/");

                myRoomImage.setImageId(roomImage.getImageId());
                myRoomImage.setImageUrl(finalImagePath);

                imageResponseDtoLists.add(myRoomImage);
            }

            roomResponseDto.setRoomImages(imageResponseDtoLists);

            roomResponseDtoList.add(roomResponseDto);
        }

        hotelResponseDto.setRooms(roomResponseDtoList);

        return hotelResponseDto;
    }

    private List<HotelImage> saveHotelImage(List<MultipartFile> hotelImages, String relativePath) throws IOException {

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
        return hotelImageList;
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
        hotel.setHotelOwnerEmail(hotelRequestDto.getHotelOwnerEmail());

        String relativePath = path + "images\\" + hotelRequestDto.getHotelName().replaceAll("\\s", "");

        List<HotelImage> hotelImageList = saveHotelImage(hotelImages, relativePath);

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

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel with id " +  hotelId + " does not exists"));

        return convertToHotelResponseDto(hotel);
    }

    @Override
    public String getHotelByHotelOwnerEmail(String email) {

        Optional<Hotel> hotel = hotelRepo.findByHotelOwnerEmail(email);

        if (hotel.isPresent()) {
            return hotel.get().getHotelOwnerEmail();
        }
        return "hotel";
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

    @Override
    public List<HotelResponseDto> getHotels(String hotelName, String location, String city, Double price, Pageable pageable) {

        List<Hotel> hotelList = hotelRepo.findAll((Specification<Hotel>) (root, query, criteriaBuilder) -> {


            Predicate predicate = criteriaBuilder.conjunction();
            // Navigate to the "area" field within the "address" entity
            Path<String> areaPath = root.get("address").get("area");

            Path<String> cityPath = root.get("address").get("city");

            Path<Double> pricePath = root.get("rooms").get("price");

            if (!StringUtils.isEmpty(location)) {
                String lowercaseArea = location.toLowerCase();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.lower(areaPath), lowercaseArea));
            }

            if (!StringUtils.isEmpty(city)) {
                String lowercaseCity = city.toLowerCase();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.lower(cityPath), lowercaseCity));
            }

            if (!StringUtils.isEmpty(hotelName)) {
                String lowercaseHotelName = hotelName.toLowerCase();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("hotelName")), "%" + lowercaseHotelName + "%"));
            }

            if (!StringUtils.isEmpty(price)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(pricePath, Double.valueOf(price)));
            }

            query.orderBy(criteriaBuilder.desc(root.join("address").get("area")), criteriaBuilder.asc(root.get("id")));
            return predicate;
        }, pageable).getContent();

        if (hotelList.isEmpty()) {
            throw new ResourceNotFoundException("We dont have hotel as per your request");
        }
        List<HotelResponseDto> hotelResponseDtoList = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            HotelResponseDto hotelResponseDto = convertToHotelResponseDto(hotel);
            hotelResponseDtoList.add(hotelResponseDto);
        }
        return hotelResponseDtoList;
    }

    @Override
    public void updateHotelInfo(Long hotelId, HotelRequestDto hotelRequestDto, String path, List<MultipartFile> hotelImages, String authorizationHeader) throws IOException {

        Hotel existingHotel = hotelRepo.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel does not exists"));

        if (existingHotel.getHotelOwnerEmail().equals(authorizationHeader)) {
            String relativePath = path + "images\\" + existingHotel.getHotelName().replaceAll("\\s", "");

            List<HotelImage> hotelImageList = saveHotelImage(hotelImages, relativePath);

            List<HotelImage> existingHotelImageList = existingHotel.getHotelImages();
            existingHotelImageList.addAll(hotelImageList);

            existingHotel.setHotelName(hotelRequestDto.getHotelName());

            Address existingAddress = existingHotel.getAddress();
            existingAddress.setProvince(hotelRequestDto.getAddress().getProvince());
            existingAddress.setCity(hotelRequestDto.getAddress().getCity());
            existingAddress.setArea(hotelRequestDto.getAddress().getArea());

            existingHotel.setAddress(existingAddress);

            existingHotel.setDescription(hotelRequestDto.getDescription());
            existingHotel.setCreatedBy(hotelRequestDto.getCreatedBy());


            List<Contact> existingContacts = existingHotel.getContacts();

            List<Contact> hotelContacts = hotelRequestDto.getContacts();

            for (int i = 0; i < existingContacts.size(); i++) {
                for (int j = 0; j < hotelContacts.size(); j++) {
                    if (i == j) {
                        existingContacts.get(i).setContactNumber(hotelContacts.get(i).getContactNumber());
                        existingContacts.get(i).setEmail(hotelContacts.get(i).getEmail());
                    }
                }
            }

            existingHotel.setContacts(existingContacts);

            existingHotel.setHotelOwnerEmail(hotelRequestDto.getHotelOwnerEmail());

            hotelRepo.save(existingHotel);
        } else {
            throw new AccessDeniedException("You do not have the access to update hotel");
        }
    }
}
