package com.avengers.musinsa.domain.product.repository;

import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;

public interface ProductVariantRepository {
    ProductVariantDto findProductVariantByOptionName(Long productId, String optionName);

    void decrementStock(Long productVariantId, int quantity);
}
