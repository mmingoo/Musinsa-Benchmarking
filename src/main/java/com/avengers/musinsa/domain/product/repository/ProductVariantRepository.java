package com.avengers.musinsa.domain.product.repository;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.domain.product.entity.ProductVariant;

import java.util.List;

public interface ProductVariantRepository {
    ProductVariantDto findProductVariantByOptionName(Long productId, String optionName);

    void decrementStock(Long productVariantId, int quantity);

    void batchDecrementStock(List<OrderCreateRequest.ProductLine> products);

    List<ProductVariantDto> findProductVariantsByOptionNames(List<OrderCreateRequest.ProductLine> products);

    ProductVariant findByIdWithLock(Long productVariantId);

    ProductVariant findById(Long variantId);
}
