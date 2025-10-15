package com.avengers.musinsa.domain.product.service;

import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService{

    private final ProductVariantRepository productVariantRepository;

    @Override
    public ProductVariantDto findProductVariantByOptionName(Long productId, String optionName){
        return productVariantRepository.findProductVariantByOptionName(productId, optionName);
    }
}
