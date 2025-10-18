package com.avengers.musinsa.domain.shipments.service;

import com.avengers.musinsa.domain.shipments.dto.ShippingAddressCreateDTO;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.shipments.repository.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ShippingAddressService {
    private final ShippingAddressRepository shippingAddressRepository;

    public List<ShippingAddressOrderDTO> getShippingAddressesUserId(Long userId) {
        return shippingAddressRepository.findByUserId(userId);
    }

    private String[] splitAddress(String fullAddress){
        if ( fullAddress == null || fullAddress.trim().isEmpty()){
            return new String[]{"",""};
        }
        String[] parts = fullAddress.split(" ",2);
        if (parts.length == 1){
            return new String[]{parts[0],""};
        }

        return parts;
    }

    //  배송지 추가
    @Transactional
    public void insertShippingAddress(ShippingAddressCreateDTO shippingAddressCreate) {

        if (Boolean.TRUE.equals(shippingAddressCreate.getIsDefault())) {
            shippingAddressRepository.resetDefaultShippingAddress(shippingAddressCreate.getUserId());
        }
        shippingAddressRepository.insertShippingAddress(shippingAddressCreate);
    }

    //배송지 수정 전 기본정보 노출
    public ShippingAddressCreateDTO getShippingAddressDefault(Long userId, Long shippingAddressId) {
        return shippingAddressRepository.getShippingAddressDefault(userId, shippingAddressId);
    }

    //배송지 수정
    public void updateShippingAddress(Long userId, Long shippingAddressId, ShippingAddressCreateDTO shippingAddressCreate) {

        shippingAddressCreate.setUserId(userId);
        shippingAddressCreate.setShippingAddressId(shippingAddressId);

        if (Boolean.TRUE.equals(shippingAddressCreate.getIsDefault())) {
            shippingAddressRepository.resetDefaultShippingAddress(userId);
        }


        shippingAddressRepository.updateShippingAddress(userId, shippingAddressId, shippingAddressCreate);
    }


    //배송지 삭제
    public void deleteShippingAddress(Long shippingAddressId, Long userId) {
        shippingAddressRepository.deleteShippingAddress(shippingAddressId, userId);
    }

    //기본 배송지 변경
    public void changeDefaultShippingAddress(Long userId, Long shippingAddressId){
        shippingAddressRepository.resetDefaultShippingAddress(userId);
        shippingAddressRepository.setDefaultShippingAddress(shippingAddressId, userId);
        shippingAddressRepository.insertShippingAddressDefault(userId, shippingAddressId);
    }

    //배송지 변경






}

