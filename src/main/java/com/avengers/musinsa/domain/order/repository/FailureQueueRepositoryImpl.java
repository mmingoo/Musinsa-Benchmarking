package com.avengers.musinsa.domain.order.repository;

import org.springframework.stereotype.Repository;

@Repository
public class FailureQueueRepositoryImpl implements  FailureQueueRepository{
    private final FailureQueueMapper failureQueueMapper;

    @Override
    public void insert(String failureType, Long orderId, String failureData) {
        failureQueueMapper.insertFailureQueue(failureType, orderId, failureData);
    }
}
