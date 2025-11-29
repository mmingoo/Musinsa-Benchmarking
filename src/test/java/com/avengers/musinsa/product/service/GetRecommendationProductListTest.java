package com.avengers.musinsa.product.service;

import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.product.service.ProductService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class GetRecommendationProductListTest {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("추천 상품 조회 쿼리 성능 비교")
    public void compareRecommendationQueries() {

        // 개선 잔 두 번째 쿼리 측정
        Gender gender = Gender.FEMALE;
        Long userId = 1L;

        long oldStart = System.currentTimeMillis();
        for(int i =0 ; i<10; i++){
                productService.getRecommendationProductList(gender, userId);
            }
        long oldEnd = System.currentTimeMillis();
        long oldRuntime = oldEnd - oldStart;
        System.out.println("oldRuntime: " + oldRuntime + "ms");


        // 개선 후 두 쿼리 측정
        long newStart = System.currentTimeMillis();
        for(int i =0 ; i<10; i++){
            productService.getUpdateRecommendationProductList(gender, userId);
        }
        long newEnd = System.currentTimeMillis();
        long newRuntime = newEnd - newStart;


        System.out.println("개선 전 쿼리: " + oldRuntime + "ms");
        System.out.println("개선 후 쿼리: " + newRuntime + "ms");
        System.out.printf("개선율: %.2f%%\n",
        (double)(oldRuntime - newRuntime) / oldRuntime * 100);
    }

}


