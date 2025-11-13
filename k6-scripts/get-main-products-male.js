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

    });

    const response = http.get(
        'http://localhost:8080/api/v1/products/recommendations/male',
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                'Cookie': 'Authorization=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im5hdmVyIGRzZjJpUnR6UkZOTHBuS05DQXEtRVk4SzJjOHhZZUYxcl9ZOC01MUVGTVUiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzYwOTQ1MjM3LCJleHAiOjE3NjEwMzE2Mzd9.u_Uc-rrVvhWCizFs4QUcrUQU5qX7HAN3dWkc9YGRh5U'
            }
        }
    );


    const checkResult = check(response, {
        '✅ 상태 코드 200': (r) => r.status === 200,
        '✅ 응답 시간 < 5초': (r) => r.timings.duration < 5000,
    });


    sleep(1);
}