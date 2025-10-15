package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductVariantMapper {

    ProductVariantDto findProductVariantByOptionName(@Param("productId") Long productId, @Param("optionName") String optionName);

    void decrementStock(@Param("productVariantId") Long productVariantId, @Param("quantity") int quantity);
}
