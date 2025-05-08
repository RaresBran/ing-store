package com.ing.store.service;

import com.ing.store.dto.OrderRequestDto;
import com.ing.store.dto.OrderResponseDto;
import com.ing.store.model.entity.Order;
import com.ing.store.model.entity.OrderItem;
import com.ing.store.model.entity.Product;
import com.ing.store.model.entity.User;
import com.ing.store.repository.OrderRepository;
import com.ing.store.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderResponseDto placeOrder(OrderRequestDto dto) {
        User user = userService.getCurrentUser();

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequestDto.OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .build();

            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .customer(user)
                .orderDate(LocalDateTime.now())
                .orderItems(orderItems)
                .build();

        // set reverse relation
        orderItems.forEach(item -> item.setOrder(order));

        productRepository.saveAll(orderItems.stream().map(OrderItem::getProduct).toList());
        return OrderResponseDto.fromOrder(orderRepository.save(order));
    }

    public List<OrderResponseDto> getOrdersByCustomer() {
        User user = userService.getCurrentUser();

        List<Order> orders = orderRepository.findByCustomer_Id(user.getId());

        return orders.stream()
                .map(OrderResponseDto::fromOrder)
                .toList();
    }
}

