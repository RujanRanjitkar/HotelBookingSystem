package com.example.inventoryservice.hotel.repo;

import com.example.inventoryservice.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

//    @Query(value = "select hotel_id,h.address_id,description,hotel_name,area,city,province from hotels as h inner join address as a on h.address_id=a.address_id where h.hotel_name=?1 and a.area=?2", nativeQuery = true)
//    Hotel findByHotelNameAndArea(String hotelName, String area);

    List<Hotel> findByAddress_Area(String area);

    Hotel findByHotelNameAndAddress_Area(String hotelName, String area);
}
