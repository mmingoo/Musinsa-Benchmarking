package com.avengers.musinsa.domain.shipments.controller;

import com.avengers.musinsa.domain.shipments.dto.ShippingAddressCreateDTO;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.shipments.service.ShippingAddressService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ShippingAddressController {
    private final ShippingAddressService shippingAddressService;

    //제외
//    private final TokenProviderService tokenProviderService;
//    private final JWTUtil jWTUtil;
//
//    // 배송지 목록 조회 (userId를 PathVariable로 받도록 수정)
//    @GetMapping("/orders/address-list")
//    public ResponseEntity<List<ShippingAddressOrderDTO>> getShippingAddressesUserId(
//            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
//
//        Long userId = null;
//        try {
//            // 토큰 검증 (필요한 경우)
//            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
//                userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
//
//            }
//
//            List<ShippingAddressOrderDTO> shippingAddresses =
//                    shippingAddressService.getShippingAddressesUserId(userId);
//
//            return ResponseEntity.ok(shippingAddresses);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

    //배솧지 추가\

    @PostMapping("/add")
    public ResponseEntity<String> addShippingAddress(@RequestBody ShippingAddressCreateDTO shippingAddressCreate) {
        try {
            shippingAddressService.insertShippingAddress(shippingAddressCreate);
            return ResponseEntity.ok("배송지가 저장되었습니다./");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("저장 실패: " + e.getMessage());
        }
    }

    //배송지 수정 전 기본정보 노출
    @GetMapping("/default/{userId}/{shippingAddress}")
    public ResponseEntity<ShippingAddressCreateDTO> getShippingAddressDefault (@PathVariable("userId") Long userId,
                                                                               @PathVariable("shippingAddress") Long shippingAddressId){
        ShippingAddressCreateDTO shippingAddressCreate = shippingAddressService.getShippingAddressDefault(userId, shippingAddressId);
        return ResponseEntity.ok(shippingAddressCreate);
    }





    //배송지 수정
    @PutMapping("/update/{userId}/{shippingAddressId}")
    public String updateShippingAddress(@PathVariable("userId") Long userId,
                                        @PathVariable("shippingAddressId") Long shippingAddressId,
                                        @RequestBody ShippingAddressCreateDTO shippingAddressCreate) {
        shippingAddressService.updateShippingAddress(userId, shippingAddressId, shippingAddressCreate);
        return "ok";
    }


    //배송지 삭제
    @DeleteMapping("/delete/{shippingAddressId}/{userId}")
    public String deleteShippingAddress(@PathVariable("shippingAddressId") Long shippingAddressId,
                                        @PathVariable("userId") Long userId) {
        shippingAddressService.deleteShippingAddress(shippingAddressId, userId);
        return "ok";
    }

    //기본 배송지 변경
    @PutMapping("/change-default/{userId}/{shippingAddressId}")
    public String changeDefaultShippingAddress(@PathVariable("userId") Long userId,
                                                @PathVariable("shippingAddressId") Long shippingAddressId){
        shippingAddressService.changeDefaultShippingAddress(userId, shippingAddressId);

        return "ok";
    }




}
