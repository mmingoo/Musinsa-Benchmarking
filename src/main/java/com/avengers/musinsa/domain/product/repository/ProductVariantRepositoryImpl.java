package com.avengers.musinsa.domain.product.repository;

import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.mapper.ProductVariantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductVariantRepositoryImpl implements ProductVariantRepository{
    private final ProductVariantMapper productVariantMapper;



    @Override
    public ProductVariantDto findProductVariantByOptionName(Long productId, String optionName) {
        return productVariantMapper.findProductVariantByOptionName(productId,optionName);
    }

    @Override
    public void decrementStock(Long productVariantId, int quantity) {
        productVariantMapper.decrementStock(productVariantId, quantity);
    }
}
