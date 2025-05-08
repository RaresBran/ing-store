package com.ing.store.dto;

import com.ing.store.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private String productName;
        private Integer quantity;
        private Double price;
    }

    public static OrderResponseDto fromOrder(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .items(order.getOrderItems().stream()
                        .map(item -> new OrderResponseDto.OrderItemDto(
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getProduct().getPrice()))
                        .toList())
                .build();
    }
}

