package com.example.bookingservice.service;

import com.example.bookingservice.customException.DateMisMatchException;
import com.example.bookingservice.customException.RoomNotAvailableException;
import com.example.bookingservice.customException.RoomNotFoundException;
import com.example.bookingservice.dto.BookingDetailRequestDto;
import com.example.bookingservice.dto.PaymentRequestDto;
import com.example.bookingservice.dto.PaymentResponseDto;
import com.example.bookingservice.enums.BookingStatus;
import com.example.bookingservice.externalservice.EmailService;
import com.example.bookingservice.externalservice.InventoryService;
import com.example.bookingservice.externalservice.PaymentService;
import com.example.bookingservice.model.BookingDetail;
import com.example.bookingservice.model.EmailSender;
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
    private final EmailService emailService;

    public void sendMail(String userEmail){
        EmailSender emailSender=new EmailSender();
        emailSender.setToEmail(userEmail);
        emailSender.setBody("Your booking has been successful");
        emailSender.setSubject("Welcome to Hotel Booking System");
        emailService.sendEmail(emailSender);
    }
    @Override
    public void bookRooms(BookingDetailRequestDto bookingDetailRequestDto) {

        if (bookingDetailRequestDto.getCheckInDate().isBefore(java.time.LocalDate.now().plusDays(1)) ||
                bookingDetailRequestDto.getCheckOutDate().isBefore(bookingDetailRequestDto.getCheckInDate().plusDays(1))) {
            throw new DateMisMatchException("Check-in and check-out date cannot be before or equal to present date");
        }

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
            if (room.getRoomStatus() == RoomStatus.AVAILABLE) {

                PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
                paymentRequestDto.setAmount(room.getPrice());

                if (bookingDetailRequestDto.getPaymentOption().equals("eSewa")) {

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto = paymentService.makePaymentUsingeSewa(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                } else if (bookingDetailRequestDto.getPaymentOption().equals("ImePay")) {

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto = paymentService.makePaymentUsingImePay(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                } else {

                    paymentRequestDto.setPaymentOption(bookingDetail.getPaymentOption());
                    PaymentResponseDto paymentResponseDto = paymentService.makePaymentUsingfonePay(paymentRequestDto);

                    log.info(paymentResponseDto.getTransaction_code());
                }

                bookingDetail.setBookingStatus(BookingStatus.CONFIRMED);
                bookingDetailRepo.save(bookingDetail);

                inventoryService.updateRoomStatus(bookingDetailRequestDto.getRoomId());

                sendMail(bookingDetailRequestDto.getBookedBy());

            } else {
                throw new RoomNotAvailableException("Room is not available for booking.");
            }
        } else {
            throw new RoomNotFoundException("Room not found.");
        }
    }
}
