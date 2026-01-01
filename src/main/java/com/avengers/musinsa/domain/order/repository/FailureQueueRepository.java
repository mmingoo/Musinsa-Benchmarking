package com.avengers.musinsa.domain.order.repository;

public interface FailureQueueRepository {
    void insert(String failureType, Long orderId, String failureData);

}
