package com.ing.store.controller;

import com.ing.store.dto.OrderRequestDto;
import com.ing.store.dto.OrderResponseDto;
import com.ing.store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return orderService.placeOrder(orderRequestDto);
    }

    @GetMapping
    public List<OrderResponseDto> getCustomerOrderHistory() {
        return orderService.getOrdersByCustomer();
    }
}
