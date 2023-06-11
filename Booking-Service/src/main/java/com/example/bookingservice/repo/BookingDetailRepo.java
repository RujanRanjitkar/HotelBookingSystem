package com.example.bookingservice.repo;

import com.example.bookingservice.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailRepo extends JpaRepository<BookingDetail, Long> {
}
