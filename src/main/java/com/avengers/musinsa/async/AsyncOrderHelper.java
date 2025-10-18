package com.avengers.musinsa.async;


import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EnableAsync
@RequiredArgsConstructor
@Component
public class AsyncOrderHelper {

    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;


    @Async
    public CompletableFuture<Void> asyncSaveOrderItems(Long orderId , List<OrderCreateRequest.ProductLine> orderProducts, Long couponId){
        orderRepository.batchCreateOrderItems(orderId, orderProducts, couponId);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> asyncDecrementStock(List<OrderCreateRequest.ProductLine> orderProducts){
        for (OrderCreateRequest.ProductLine orderProduct : orderProducts){
            productVariantRepository.decrementStock(orderProduct.getVariantId(), orderProduct.getQuantity());
        }
        return CompletableFuture.completedFuture(null);

    }

}
