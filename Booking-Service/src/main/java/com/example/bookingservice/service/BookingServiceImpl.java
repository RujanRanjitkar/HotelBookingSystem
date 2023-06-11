package com.example.bookingservice.service;
import java.util.List;
import java.time.LocalDate;

import com.example.bookingservice.dto.BookingDetailRequestDto;
import com.example.bookingservice.enums.BookingStatus;
import com.example.bookingservice.model.BookingDetail;
import com.example.bookingservice.repo.BookingDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingDetailService{
    private final BookingDetailRepo bookingDetailRepo;

    @Override
    public void bookRooms(BookingDetailRequestDto bookingDetailRequestDto) {

        BookingDetail bookingDetail=new BookingDetail();

        bookingDetail.setCheckInDate(bookingDetailRequestDto.getCheckInDate());
        bookingDetail.setCheckOutDate(bookingDetailRequestDto.getCheckOutDate());
        bookingDetail.setNumberOfGuests(bookingDetailRequestDto.getNumberOfGuests());
        bookingDetail.setSpecialRequest(bookingDetailRequestDto.getSpecialRequest());
        bookingDetail.setBookingStatus(BookingStatus.PENDING);
        bookingDetail.setTotalPrice(bookingDetailRequestDto.getTotalPrice());

        bookingDetailRepo.save(bookingDetail);
    }
}
