package com.example.bookingservice.service;

import com.example.bookingservice.customException.RoomNotAvailableException;
import com.example.bookingservice.customException.RoomNotFoundException;
import com.example.bookingservice.dto.BookingDetailRequestDto;
import com.example.bookingservice.dto.PaymentRequestDto;
import com.example.bookingservice.dto.PaymentResponseDto;
import com.example.bookingservice.enums.BookingStatus;
import com.example.bookingservice.externalservice.InventoryService;
import com.example.bookingservice.externalservice.PaymentService;
import com.example.bookingservice.model.BookingDetail;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.model.RoomStatus;
import com.example.bookingservice.repo.BookingDetailRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingDetailService {
    private final BookingDetailRepo bookingDetailRepo;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    @Override
    public void bookRooms(BookingDetailRequestDto bookingDetailRequestDto) {

        BookingDetail bookingDetail = new BookingDetail();

        bookingDetail.setCheckInDate(bookingDetailRequestDto.getCheckInDate());
        bookingDetail.setCheckOutDate(bookingDetailRequestDto.getCheckOutDate());
        bookingDetail.setSpecialRequest(bookingDetailRequestDto.getSpecialRequest());
        bookingDetail.setBookingStatus(BookingStatus.PENDING);
        bookingDetail.setPaymentOption(bookingDetailRequestDto.getPaymentOption());
        bookingDetail.setRoomId(bookingDetailRequestDto.getRoomId());
        bookingDetail.setBookedBy(bookingDetailRequestDto.getBookedBy());


        Room room = inventoryService.getRoomByRoomId(bookingDetailRequestDto.getRoomId());

        if (room != null) {
            if(room.getRoomStatus() == RoomStatus.AVAILABLE){

                bookingDetail.setHotelName(room.getHotel().getHotelName());

                PaymentRequestDto paymentRequestDto=new PaymentRequestDto();
                paymentRequestDto.setAmount(10000.0);

                if(bookingDetailRequestDto.getPaymentOption().equals("eSewa")){

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto=paymentService.makePaymentUsingeSewa(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                }
                else if(bookingDetailRequestDto.getPaymentOption().equals("ImePay")){

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto=paymentService.makePaymentUsingImePay(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                }
                else {

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto=paymentService.makePaymentUsingfonePay(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                }

                bookingDetail.setBookingStatus(BookingStatus.CONFIRMED);
                bookingDetailRepo.save(bookingDetail);

                inventoryService.updateRoomStatus(bookingDetailRequestDto.getRoomId());
            }
            else{
                throw new RoomNotAvailableException("Room is not available for booking.");
            }
        } else {
            throw new RoomNotFoundException("Room not found.");
        }
    }
}
