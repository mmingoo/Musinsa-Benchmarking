package com.avengers.musinsa.domain.product.service;

import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;

public interface ProductVariantService {

    ProductVariantDto findProductVariantByOptionName(Long productId, String optionName);

    }
