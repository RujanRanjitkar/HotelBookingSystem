package com.example.bookingservice.externalservice;

import com.example.bookingservice.dto.PaymentRequestDto;
import com.example.bookingservice.dto.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "external", url = "https://33767a5a-077b-458c-bf16-0faba3ab21b9.mock.pstmn.io")
public interface PaymentService {

    @PostMapping("/makePayment/eSewa")
    PaymentResponseDto makePaymentUsingeSewa(@RequestBody PaymentRequestDto paymentRequestDto);

    @PostMapping("/makePayment/ImePay")
    PaymentResponseDto makePaymentUsingImePay(@RequestBody PaymentRequestDto paymentRequestDto);

    @PostMapping("/makePayment/fonePay")
    PaymentResponseDto makePaymentUsingfonePay(@RequestBody PaymentRequestDto paymentRequestDto);
}
