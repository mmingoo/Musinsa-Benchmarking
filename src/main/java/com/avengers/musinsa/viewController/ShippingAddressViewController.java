package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.order.dto.response.ShippingAddressDto;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressCreateDTO;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.shipments.service.ShippingAddressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class ShippingAddressViewController {

    private final ShippingAddressService shippingAddressService;

    public ShippingAddressViewController(ShippingAddressService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }


    @GetMapping("/shipping-address-popup")
    public String shippingAddressPopup(){
        System.out.println("배송지팝업 호출 확인");
        return "shipments/shippingAddressList";
    }


    @GetMapping("/shipping-address-add")
    public String shippingAddressAddForm(HttpSession session, Model model){
        System.out.println("배송지 추가 팝업 호출 확인");


        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            // 테스트용 임시 값 (실제로는 로그인 체크 필요)
            userId = 1L;
        }
        model.addAttribute("userId", userId);

        return "shipments/shippingAddressForm";
    }


    @GetMapping("/shipping-address-edit")
    public String shippingAddressEditForm(@RequestParam("shippingAddressId") Long shippingAddressId,
                                          HttpSession session, Model model, RedirectAttributes redirectAttributes){

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) userId = 1L; // 테스트용

        ShippingAddressCreateDTO shippingAddressCreate = shippingAddressService.getShippingAddressDefault(userId, shippingAddressId);

        if (shippingAddressCreate == null) {
            // 없는 경우 404 페이지나 에러 처리
           redirectAttributes.addFlashAttribute("msg", "해당 배송지를 찾을 수 없습니다");
           return "redirect:/shipping-address-popup";
        }
        // JSP에 데이터 전달
        model.addAttribute("address", shippingAddressCreate);
        model.addAttribute("userId", userId);
        model.addAttribute("shippingAddressId", shippingAddressCreate.getShippingAddressId());
        model.addAttribute("isDefault", shippingAddressCreate.getIsDefault());
        return "shipments/shippingAddressEditForm";
    }


}