<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@ include file="../main/header.jsp" %>
    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <title>주문서 - MUSINSA</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Malgun Gothic', sans-serif;
            background-color: #ffffff;
            color: #333;
        }

        .container {
            background: #fafafa;
            max-width: 1400px;
            margin: 0 auto;
            padding: 20px 40px;
            display: flex;
            border: 1px solid #e9ecef;
            gap: 20px;
            justify-content: center;
        }

        .left-section {
            max-width: 700px;
            flex: 2;
            background: #ffffff;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 30px;
        }

        .right-section {
            flex: 1;
            max-width: 500px;
            background: #ffffff;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 30px;
            height: fit-content;
            position: sticky;
            top: 20px;
        }

        .section-title {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 20px;
            color: #333;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .change-address-btn {
            background-color: #fff;
            border: 1px solid #ddd;
            color: #666;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .change-address-btn:hover {
            background-color: #f5f5f5;
            border-color: #999;
        }

        .buyer-info, .recipient-info {
            margin-bottom: 30px;
        }

        .info-row {
            display: flex;
            margin-bottom: 10px;
        }

        .info-label {
            width: 100px;
            font-weight: bold;
            color: #666;
        }

        .info-value {
            flex: 1;
            color: #333;
        }

        /* 배송 요청사항 스타일 */
        .delivery-request-section {
            margin: 20px 0;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
        }

        .delivery-request-select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            margin-top: 10px;
            cursor: pointer;
            background-color: white;
        }

        .delivery-request-input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            margin-top: 10px;
            display: none;
        }

        .delivery-request-input.active {
            display: block;
        }

        .delivery-request-info {
            font-size: 12px;
            color: #666;
            margin-top: 8px;
            line-height: 1.4;
        }

        .product-section {
            margin-bottom: 30px;
        }

        .product-item {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 15px;
            display: flex;
            gap: 15px;
        }

        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }

        .product-details {
            flex: 1;
        }

        .brand-name {
            font-size: 14px;
            color: #666;
            margin-bottom: 5px;
        }

        .product-name {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }

        .product-option {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }

        .product-price {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .price-info {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .original-price {
            text-decoration: line-through;
            color: #999;
            font-size: 14px;
            font-weight: normal;
        }

        .discount-rate {
            color: #ff0000;
            font-size: 14px;
            font-weight: bold;
        }

        .summary-section {
            border-top: 1px solid #e0e0e0;
            padding-top: 20px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            font-size: 14px;
        }

        .summary-row.total {
            font-size: 18px;
            font-weight: bold;
            color: #ff1200;
            border-top: 1px solid #e0e0e0;
            padding-top: 12px;
            margin-top: 12px;
        }

        .summary-row.total .label {
            color: #333; /* 단어 검정색 */
            font-weight: bold;
        }

        .points-section {
            margin: 20px 0;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
        }

        .points-input {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-top: 10px;
        }

        .points-input input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .points-input button {
            padding: 10px 15px;
            background-color: #333;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;
        }

        .points-input button:hover {
            background-color: #555;
        }

        .points-input button.cancel {
            background-color: #2934e6;
        }

        .points-input button.cancel:hover {
            background-color: #323232;
        }

        .order-button {
            width: 100%;
            padding: 15px;
            background-color: #333;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 20px;
        }

        .order-button:hover {
            background-color: #555;
        }

        .discount-price {
            color: #000fff;
        }

        .payment-info {
            font-size: 12px;
            color: #666;
            margin-top: 15px;
            line-height: 1.4;
        }

        .divider {
            border-top: 1px solid #e0e0e0;
            margin: 20px 0;
        }

        .divider-thin {
            border-top: 1px solid #f0f0f0;
            margin: 15px 0;
        }

        /* 결제 수단 스타일 */
        .payment-method-section {
            margin: 20px 0;
        }

        .payment-option {
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            position: relative;
        }

        .payment-option input[type="radio"] {
            width: 20px;
            height: 20px;
            margin-right: 15px;
            cursor: pointer;
        }

        .payment-label {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            cursor: pointer;
            padding: 6px 0;
        }

        .payment-icon-wrapper {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .payment-icon {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            object-fit: cover;
            display: block;
        }

        .payment-name {
            font-size: 16px;
            font-weight: 400;
            color: #333;
        }

        .payment-benefits {
            display: flex;
            align-items: center;
        }

        .benefit-tag {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 400;
            background-color: transparent;
            color: #008dff;
            border: 1px solid #e0e0e0;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 왼쪽 섹션: 주문자/수령자 정보 및 상품 목록 -->
    <div class="left-section">
        <!-- 수령자 정보 -->
        <div class="buyer-info">
            <div class="section-title">
                수령인
                <button type="button" class="change-address-btn" onclick="openAddressPopup()">배송지 변경</button>
            </div>
            <div class="info-row">
                <span class="info-label">받는 사람</span>
                <span class="info-value" id="recipient-name">${buyerRecipientInfoAdapter.recipientName}</span>
            </div>
            <div class="info-row">
                <span class="info-label">연락처</span>
                <span class="info-value" id="recipient-phone">${buyerRecipientInfoAdapter.recipientPhone}</span>
            </div>
            <div class="info-row">
                <span class="info-label">주소</span>
                <span class="info-value" id="recipient-address">
                    (<span id="recipient-postcode">${buyerRecipientInfoAdapter.recipientPostCode}</span>)
                    <span id="recipient-full-address">${buyerRecipientInfoAdapter.recipientAddress}${buyerRecipientInfoAdapter.recipientDetailAddress}</span>
                </span>
            </div>

            <!-- 배송 요청사항 (수령인 내부) -->
            <div style="margin-top: 20px;">
                <select id="deliveryRequestSelect" class="delivery-request-select"
                        onchange="handleDeliveryRequestChange()">
                    <option value="">배송 요청사항을 선택해주세요</option>
                    <option value="문 앞에 놓아주세요">문 앞에 놓아주세요</option>
                    <option value="경비실에 맡겨주세요">경비실에 맡겨주세요</option>
                    <option value="택배함에 넣어주세요">택배함에 넣어주세요</option>
                    <option value="배송 전에 연락 주세요">배송 전에 연락 주세요</option>
                    <option value="custom">직접입력</option>
                </select>
                <input
                        type="text"
                        id="customDeliveryRequest"
                        class="delivery-request-input"
                        placeholder="배송 요청사항을 입력해주세요 (최대 50자)"
                        maxlength="50"
                >
            </div>
        </div>

        <div class="divider"></div>

        <!-- 상품 목록 -->
        <div class="product-section">
            <div class="section-title">
                주문 상품 <c:out value="${orderProductAdapters.size()}"/>개
            </div>

            <c:forEach var="product" items="${orderProductAdapters}">
                <div class="product-item">
                    <img src="${product.productImage}" alt="${product.productName}" class="product-image">
                    <div class="product-details">
                        <div class="brand-name">${product.brandName}</div>
                        <div class="product-name">${product.productName}</div>
                        <div class="product-option">${product.optionName} / ${product.quantity}개</div>
                        <div class="product-price">
                            <div class="price-info">
                                <c:if test="${product.discountRate > 0}">
                                    <span class="original-price">
                                        <fmt:formatNumber value="${product.originalPrice * product.quantity}"
                                                          type="number"/>원
                                    </span>
                                    <span class="discount-rate">${product.discountRate}%</span>
                                </c:if>
                                <span>
                                    <fmt:formatNumber value="${product.finalPrice * product.quantity}" type="number"/>원
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="divider-thin"></div>

        <!-- 보유 적립금 사용 -->
        <div class="points-section">
            <div class="section-title">보유 적립금 사용</div>
            <div class="points-input">
                <input type="number" id="pointsInput" placeholder="0" min="0" max="" readonly>
                <button type="button" id="pointsToggleBtn" onclick="togglePoints()">최대 사용</button>
            </div>
            <div class="payment-info">
                적립 한도(7%) <span id="pointsLimit"></span>원 / 보유 <fmt:formatNumber
                    value="${buyerRecipientInfoAdapter.mileage}" type="number"/>원
            </div>
        </div>

        <div class="divider-thin"></div>

        <!-- 결제 수단 -->
        <div class="payment-method-section">
            <div class="section-title">결제 수단</div>

            <!-- 농협페이 -->
            <div class="payment-option">
                <input type="radio" id="NHPay" name="paymentMethod" value="NHPay" checked>
                <label for="NHPay" class="payment-label">
                    <div class="payment-icon-wrapper">
                        <img src="https://image.msscdn.net/musinsaUI/store/order/finance/28x28/logo-finance-nh.png"
                             alt="농협페이" class="payment-icon">
                        <span class="payment-name">농협페이</span>
                    </div>
                    <div class="payment-benefits">
                        <span class="benefit-tag">혜택</span>
                    </div>
                </label>
            </div>

            <!-- 토스페이 -->
            <div class="payment-option">
                <input type="radio" id="tossPay" name="paymentMethod" value="tossPay">
                <label for="tossPay" class="payment-label">
                    <div class="payment-icon-wrapper">
                        <img src="https://image.msscdn.net/musinsaUI/store/order/finance/logo-finance-toss.png"
                             alt="토스페이" class="payment-icon">
                        <span class="payment-name">토스페이</span>
                    </div>
                    <div class="payment-benefits">
                        <span class="benefit-tag">혜택</span>
                    </div>
                </label>
            </div>

            <!-- 카카오페이 -->
            <div class="payment-option">
                <input type="radio" id="kakaoPay" name="paymentMethod" value="kakaoPay">
                <label for="kakaoPay" class="payment-label">
                    <div class="payment-icon-wrapper">
                        <img src="https://image.msscdn.net/musinsaUI/store/order/finance/logo-finance-kakaopay.png"
                             alt="카카오페이" class="payment-icon">
                        <span class="payment-name">카카오페이</span>
                    </div>
                    <div class="payment-benefits">
                        <span class="benefit-tag">혜택</span>
                    </div>
                </label>
            </div>

            <!-- 페이코 -->
            <div class="payment-option">
                <input type="radio" id="payco" name="paymentMethod" value="payco">
                <label for="payco" class="payment-label">
                    <div class="payment-icon-wrapper">
                        <img src="https://image.msscdn.net/musinsaUI/store/order/finance/logo-finance-payco.png"
                             alt="페이코" class="payment-icon">
                        <span class="payment-name">페이코</span>
                    </div>
                    <div class="payment-benefits">
                        <span class="benefit-tag">혜택</span>
                    </div>
                </label>
            </div>
        </div>

        <div class="divider-thin"></div>
    </div>

    <!-- 오른쪽 섹션: 결제 금액 -->
    <div class="right-section">
        <div class="section-title">결제 금액</div>

        <div class="summary-section">
            <!-- 가격 계산 -->
            <c:set var="totalOriginalProductPrice" value="0"/>
            <c:set var="totalDiscount" value="0"/>
            <c:set var="totalOrderPrice" value="0"/>

            <c:forEach var="product" items="${orderProductAdapters}">
                <c:set var="totalOriginalProductPrice"
                       value="${totalOriginalProductPrice + (product.originalPrice*product.quantity)}"/>
                <c:set var="totalDiscount"
                       value="${totalDiscount + (product.originalPrice * product.quantity - product.totalPrice)}"/>
            </c:forEach>


            <c:set var="totalOrderPrice" value="${totalOriginalProductPrice - totalDiscount}"/>

            <div class="summary-row">
                <span>상품 금액</span>
                <span id="originalPrice">
                    <fmt:formatNumber value="${totalOriginalProductPrice}" type="number"/>원
                </span>
            </div>
            <div class="summary-row">
                <span>할인 금액</span>
                <span class="discount-price" id="discountAmount">
                    -<fmt:formatNumber value="${totalDiscount}" type="number"/>원
                </span>
            </div>
            <div class="summary-row">
                <span>적립금 사용</span>
                <span class="discount-price" id="pointsUsed">
                    -<span id="pointsAmount">0</span>원
                </span>
            </div>
            <div class="summary-row">
                <span>배송비</span>
                <span id="shippingCost">무료배송</span>
            </div>

            <div class="summary-row total">
                <span class="label">총 결제 금액</span>
                <span id="totalPrice">
                    <span id="finalAmount"><fmt:formatNumber value="${totalOrderPrice}" type="number"/>원</span>
                </span>
            </div>
        </div>

        <!-- 적립 혜택 -->
        <div class="points-section">
            <div class="section-title">적립 혜택</div>
            <c:set var="earnedPoints" value="${totalOrderPrice * 0.015}"/>
            <c:set var="maxReviewPoints" value="2500"/>
            <c:set var="totalEarnedPoints" value="${earnedPoints + maxReviewPoints}"/>

            <div class="summary-row">
                <span>LV5 실버 · 1.5% 적립</span>
                <span><fmt:formatNumber value="${earnedPoints}" maxFractionDigits="0"/>원</span>
            </div>
            <div class="summary-row">
                <span>후기 적립</span>
                <span>최대 <fmt:formatNumber value="${maxReviewPoints}" type="number"/>원</span>
            </div>
            <div class="summary-row total">
                 <span class="label">총 적립 금액</span>
                <span><fmt:formatNumber value="${totalEarnedPoints}" maxFractionDigits="0"/>원</span>
            </div>
        </div>

        <div class="payment-info">
            주문 내용을 확인하였으며, 정보 제공 등에 동의합니다.<br>
            회원님의 개인정보는 안전하게 관리됩니다.<br><br>
            무신사는 통신판매중개자로, 연체 배송 상품의 상품정보/상품취소/<br>
            거래 등에 대한 책임은 무신사가 아닌 판매업체에 있습니다.
        </div>

        <div class="summary-row" style="margin-top: 20px;">
            <span>이번 주문으로 받을 혜택</span>
            <span style="color: #2196F3; font-weight: bold;">
                <fmt:formatNumber value="${totalEarnedPoints}" maxFractionDigits="0"/>원
            </span>
        </div>

        <button class="order-button" onclick="processOrder()">
            <span id="orderButtonAmount"><fmt:formatNumber value="${totalOrderPrice}" type="number"/>원</span> 결제하기
        </button>
    </div>
</div>

<script>
    // 전역 변수
    let totalProductPrice = ${totalOrderPrice};
    let maxPoints = ${buyerRecipientInfoAdapter.mileage};
    let maxUsablePoints = Math.floor(totalProductPrice * 0.07);
    let currentPointsUsed = 0;
    let isPointsUsed = false;
    let actualMaxUsable = Math.min(maxPoints, maxUsablePoints);

    // 페이지 로드 시 초기화
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('pointsLimit').textContent = maxUsablePoints.toLocaleString();
        document.getElementById('pointsInput').setAttribute('max', actualMaxUsable);
        updatePriceDisplay();
    });

    // 배송 요청사항 변경 처리
    function handleDeliveryRequestChange() {
        const select = document.getElementById('deliveryRequestSelect');
        const customInput = document.getElementById('customDeliveryRequest');

        if (select.value === 'custom') {
            customInput.classList.add('active');
            customInput.focus();
        } else {
            customInput.classList.remove('active');
            customInput.value = '';
        }
    }

    // 적립금 사용 토글
    function togglePoints() {
        const pointsInput = document.getElementById('pointsInput');
        const toggleBtn = document.getElementById('pointsToggleBtn');

        if (!isPointsUsed) {
            currentPointsUsed = actualMaxUsable;
            pointsInput.value = actualMaxUsable;
            toggleBtn.textContent = '사용 취소';
            toggleBtn.classList.add('cancel');
            isPointsUsed = true;
        } else {
            currentPointsUsed = 0;
            pointsInput.value = 0;
            toggleBtn.textContent = '최대 사용';
            toggleBtn.classList.remove('cancel');
            isPointsUsed = false;
        }

        updatePriceDisplay();
    }

    // 가격 표시 업데이트
    function updatePriceDisplay() {
        const finalPrice = totalProductPrice - currentPointsUsed;

        document.getElementById('pointsAmount').textContent = currentPointsUsed.toLocaleString();
        document.getElementById('finalAmount').textContent = finalPrice.toLocaleString() + '원';
        document.getElementById('orderButtonAmount').textContent = finalPrice.toLocaleString() + '원';
    }

    // 배송 요청사항 가져오기
    function getDeliveryRequest() {
        const select = document.getElementById('deliveryRequestSelect');
        const customInput = document.getElementById('customDeliveryRequest');

        if (select.value === 'custom') {
            return customInput.value.trim();
        } else if (select.value === '') {
            return null;
        } else {
            return select.options[select.selectedIndex].text;
        }
    }

    // 주문 처리
    function processOrder() {
        const finalAmount = totalProductPrice - currentPointsUsed;
        const deliveryRequest = getDeliveryRequest();

        if (!confirm(finalAmount.toLocaleString() + '원을 결제하시겠습니까?')) {
            return;
        }

        const orderData = {
            userId: null,
            addressId: null,
            shipping: {
                recipientName: document.getElementById('recipient-name').textContent,
                recipientPhone: document.getElementById('recipient-phone').textContent,
                postalCode: document.getElementById('recipient-postcode').textContent,
                recipientAddress: document.getElementById('recipient-full-address').textContent,
                shippingDirectRequest: deliveryRequest
            },
            payment: {
                totalAmount: finalAmount,
                discountAmount: ${totalDiscount},
                userOfReward: currentPointsUsed,
                deliveryFee: 0,
                paymentMethodId: getSelectedPaymentMethodId()
            },
            product: getProductsData()
        };

        console.log('전송할 주문 데이터:', orderData);

        $.ajax({
            url: '/orders/completion-order',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(orderData),
            success: function (response) {


                // orderId 값 확인 후 리다이렉트
                if (response && response.orderId) {
                    window.location.href = '/orders/order-complete/' + response.orderId;
                } else {
                    console.error('응답에서 orderId를 찾을 수 없습니다:', response);
                    alert('주문이 완료되었지만 주문 완료 페이지로 이동할 수 없습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error('주문 실패:', error);
                alert('주문 처리 중 오류가 발생했습니다.');
            }
        });
    }

    // 배송지 변경 팝업 열기
    function openAddressPopup() {
        const popup = window.open('/shipping-address-popup', 'addressPopup',
            'width=600,height=700,scrollbars=yes,resizable=yes');

        window.updateAddressFromPopup = function (addressData) {
            document.getElementById('recipient-name').textContent = addressData.recipientName;
            document.getElementById('recipient-phone').textContent = addressData.recipientPhone;
            document.getElementById('recipient-postcode').textContent = addressData.postalCode;
            document.getElementById('recipient-full-address').textContent = addressData.recipientAddress;

            console.log('배송지 정보가 업데이트되었습니다:', addressData);
            popup.close();
        };
    }

    function getSelectedPaymentMethodId() {
        const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
        const paymentIds = {
            'NHPay': 1,
            'tossPay': 2,
            'kakaoPay': 3,
            'payco': 4
        };

        return selectedPayment ? paymentIds[selectedPayment.value] : 1;
    }

    function getProductsData() {
        const products = [];

        <c:forEach var="product" items="${orderProductAdapters}">
        products.push({
            productId: ${product.productId},
            variantId: null,
            options: {},
            finalPrice: ${product.finalPrice},
            quantity: ${product.quantity},
            optionName: '${product.optionName}',
            discountRate: ${product.discountRate},
            productDiscountAmount: ${(product.originalPrice - product.finalPrice) * product.quantity }
        });
        </c:forEach>

        return products;
    }
</script>
</body>
</html>