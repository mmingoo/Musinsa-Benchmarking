package com.avengers.musinsa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FailureQueueMapper {
    void insertFailureQueue(
            @Param("failureType") String failureType,
            @Param("orderId") Long orderId,
            @Param("failureData") String failureData
    );
}