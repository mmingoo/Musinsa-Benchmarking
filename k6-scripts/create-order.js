import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {

    // duration: '5m',
    // vus: 1000 ,
    stages: [
        { duration: '3m', target: 500 },   // 30초 동안 0→100명으로 증가
        { duration: '3m', target: 1000 }   // 1분 동안 100명 유지
    ],
    thresholds: {
        http_req_duration: ['p(95)<5000'],
        http_req_failed: ['rate<0.1'],
    },
};

export default function() {
    // 10개 상품 주문 (테스트 코드와 동일)
    const payload = JSON.stringify({
        userId: null,
        addressId: 1,
        couponId: null,
        shipping: {
            recipientName: "홍길동",
            recipientPhone: "010-1234-5678",
            recipientAddress: "서울시 강남구 테헤란로 123",
            shippingDirectRequest: "문 앞에 놓아주세요",
            postalCode: "12345"
        },
        payment: {
            totalAmount: 100000,
            discountAmount: 0,
            userOfReward: 0,
            deliveryFee: 3000,
            paymentMethodId: 1
        },
        product: [
            {
                productId: 1,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 3,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 5,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 10,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 11,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 13,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 15,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 16,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 18,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            },
            {
                productId: 20,
                variantId: 3,
                options: {},
                finalPrice: 30000,
                quantity: 3,
                optionName: "Red / S",
                discountRate: 0,
                productDiscountAmount: 0
            }
        ]
    });

    const response = http.post(
        'http://localhost:8080/orders/completion-order',
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                'Cookie': 'Authorization=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im5hdmVyIGRzZjJpUnR6UkZOTHBuS05DQXEtRVk4SzJjOHhZZUYxcl9ZOC01MUVGTVUiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzYwODQ5MjYyLCJleHAiOjE3NjA5MzU2NjJ9.baOVdm2yrgzzBUlX93jcmq0haZWyKBP2OR0_mU0AHeY; grafana_session=7d27aaebc220470ca3efb77a7b10c4f0; grafana_session_expiry=1760850066'
            }
        }
    );


    const checkResult = check(response, {
        '✅ 상태 코드 200': (r) => r.status === 200,
        '✅ 응답 시간 < 5초': (r) => r.timings.duration < 5000,
    });


    sleep(1);
}