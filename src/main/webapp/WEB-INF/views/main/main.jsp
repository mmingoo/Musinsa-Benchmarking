<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %><!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MUSINSA - 무신사</title>

    <%-- Resource URLs --%>
    <c:url value="/resources/js/common/likeToggle.js" var="jsLikeToggle"/>

    <%-- Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>

    <%-- Swiper CSS --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css"/>

    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #ffffff;
            margin: 0;
        }
        /* 배너를 컨테이너 밖으로 확장하여 양옆 공백 제거 */
        .section-banner {
            margin: -20px -21px 0 -21px; /* 컨테이너의 padding을 상쇄 */
        }

        .banner-swiper {
            width: 100%;
            margin-bottom: 30px;
            position: relative;
        }

        .banner-swiper .swiper-slide {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 0 !important;
            margin: 0 !important;
            position: relative;
        }

        .banner-text {
            position: absolute;
            bottom: 45px;
            left: 6px;
            color: white;
            font-size: 17px;
            font-weight: bold;
            z-index: 10;
            padding-left: 13px;
        }
        .banner-brand {
            position: absolute;
            bottom: 20px;
            left: 20px;
            color: white;
            font-size: 11px;
            font-weight: bold;
            z-index: 10;
        }

        .banner-swiper img {
            width: 100%;
            height: 450px;
            object-fit: cover;
            object-position: center;
            border-radius: 0;
            display: block;
        }

        /* 배너 네비게이션 버튼 */
        .banner-prev, .banner-next {
            color: #000000;
            width: 30px;
            height: 30px;
        }
        .banner-prev { left: 10px; }
        .banner-next { right: 10px; }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 0 40px;
            background: #ffffff;
        }

        .section {
            margin-bottom: 0;
            background: #fafafa;
            padding: 20px;
            border: 1px solid #e9ecef;
            border-top: 0;
        }

        .section-title {
            font-size: 20px;
            font-weight: bold;
            margin-top: 0px;
            margin-bottom: 5px;
            color: #333;
        }

        .section-subtitle {
            font-size: 14px;
            color: #666;
            margin-top: 5px;
            margin-bottom: 15px;
        }

        .sub-title-woman{
            margin-top: 70px;
        }

        /* 공통 스와이퍼 스타일 - men-swiper, women-swiper 모두 적용 */
        .swiper,
        .men-swiper,
        .women-swiper {
            width: 100%;
            height: auto;
            position: relative;
            overflow: hidden;
        }

        .swiper-wrapper {
            margin: 0 !important;
            padding: 0 !important;
        }

        .swiper-slide {
            margin: 0 !important;
            padding: 0 !important;
            border: none !important;
            box-sizing: border-box;
            flex-shrink: 0;
        }

        /* 모든 nth-child 마진 완전 제거 */
        .swiper-slide:nth-child(n) {
            margin-right: 0 !important;
            margin-left: 0 !important;
            padding-right: 0 !important;
            padding-left: 0 !important;
        }

        .product-card {
            cursor: pointer;
            margin: 0;
            padding: 0;
            border: none;
            margin-bottom: 20px;
        }

        .product-card:hover {
            transform: none;
        }

        .product-image {
            width: 100%;
            height: 240px;
            background-color: #e9ecef;
            margin: 0 0 8px 0;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #e9ecef;
            font-size: 14px;
            overflow: hidden;
            position: relative;
            border: none;
            border-radius: 0;
        }

        .product-image img {
            margin-top: 0px;
            width: 100%;
            height: 100%;
            object-fit: cover;
            display: block;
        }

        .product-image.placeholder {
            background-color: #f0f0f0;
        }

        /* 좋아요 하트 아이콘 스타일 */
        .product-like-icon {
            position: absolute;
            bottom: 8px;
            right: 8px;
            cursor: pointer;
            z-index: 10;
            font-size: 15px;
            color: #ff4444;
            transition: transform 0.2s ease;

        }

        .product-like-icon:hover {
            transform: scale(1.2);
        }

        .product-like-icon.empty {
            color: #ddd;
        }

        .product-like-icon.filled {
            color: #ff4444;
        }

        .product-brand {
            font-size: 11px;
            color: #666;
            margin-bottom: 4px;
        }

        .product-name {
            font-size: 12px;
            font-weight: 500;
            margin-bottom: 2px;
            margin-right: 6px;
            line-height: 1.3;
            word-break: break-word;
            overflow-wrap: break-word;
            hyphens: auto;
        }

        .product-price {
            display: flex;
            align-items: center;
            gap: 6px;
            flex-wrap: wrap;
        }

        .discount-rate {
            color: #e74c3c;
            font-weight: bold;
            font-size: 12px;
        }

        .current-price {
            font-weight: bold;
            font-size: 12px;
        }

        .original-price {
            font-size: 10px;
            color: #999;
            text-decoration: line-through;
        }

        /* Navigation 버튼 스타일 - 모든 스와이퍼에 적용 */
        .men-swiper .swiper-button-next,
        .men-swiper .swiper-button-prev,
        .women-swiper .swiper-button-next,
        .women-swiper .swiper-button-prev {
            color: #000000;
            width: 30px;
            height: 30px;
        }

        /* 반응형 - 5.5개 상품 기준으로 조정 */
        @media (max-width: 1200px) {
            .swiper-slide {
                width: 22.22% !important; /* 4.5개 상품 */
            }
        }

        @media (max-width: 768px) {
            .swiper-slide {
                width: 45% !important; /* 2.2개 상품 */
            }
            .product-image {
                height: 180px;
            }
        }

        @media (max-width: 480px) {
            .swiper-slide {
                width: 90% !important; /* 1.1개 상품 */
            }
            .product-image {
                height: 160px;
            }
        }
    </style>

</head>
<body>
<%-- 헤더 include (헤더 관련 CSS, JS, 카테고리 모두 포함) --%>
<%@ include file="../main/header.jsp" %>
<main class="container">
    <section class="section">
        <section class = section-banner>
            <div class="swiper banner-swiper">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/e3fdccde17774155863f22d1f2e42ae9.jpg" alt="배너1">
                        <div class="banner-text">품절 대란<br>인기 경량 패딩 </div>
                        <div class="banner-brand">무신사 스탠사드, 스파오 외</div>

                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/25/07fa8436dca44704b24ab045746893c2.jpg" alt="배너2">
                        <div class="banner-text">인기 백팩<br>20% 쿠폰 증정 </div>
                        <div class="banner-brand">잔스포츠 외</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/687ffbe6e14842029cab5585daada692.jpg" alt="배너3" >
                        <div class="banner-text">라이브 특가<br>09.26 22시</div>
                        <div class="banner-brand">펜필드</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/25/7fe42e21285a4ea38aff44523636fe30.jpg" alt="배너4">
                        <div class="banner-text">풍성한 추석 빅세일<br>최대 55% 할인</div>
                        <div class="banner-brand">무신사 스탠다드 스포츠</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/25/1aad2910df144acc81c80355503915ad.jpg" alt="배너5">
                        <div class="banner-text">가을 슈즈 신상<br>최대 20% 쿠폰</div>
                        <div class="banner-brand">살라몬 리복 외</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/0b24d1c9e4164602a9c47d7d0d240ef8.jpg" alt="배너6" >
                        <div class="banner-text">무탠다드 총출동<br>최대 80% 할인</div>
                        <div class="banner-brand">무신사 스탠다드 외</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/e09ba08c86f140b1831e277e01d4c2d4.jpg" alt="배너7" >
                        <div class="banner-text">50주년 기념<br>로스트 가든 에디션</div>
                        <div class="banner-brand">펜필드</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/f7a5493dcb494cf28827e8baf9c36f85.jpg" alt="배너8" >
                        <div class="banner-text">시그니처 특가<br>최대 30% 할인</div>
                        <div class="banner-brand">시그니처, 르아르 외</div>
                    </div>
                    <div class="swiper-slide">
                        <img src="https://image.msscdn.net/display/images/2025/09/24/31555606f2de4339837cf8c9a4548204.jpg" alt="배너9" >
                        <div class="banner-text">명절 선물로 제격<br>추석 빅세일</div>
                        <div class="banner-brand">수아레, 제로 외</div>
                    </div>
                </div>
                <!-- 좌우 버튼 -->
                <div class="swiper-button-prev banner-prev"></div>
                <div class="swiper-button-next banner-next"></div>
            </div>
        </section>

        <!-- 남성 아이템 -->
        <h3 class="sub-title">남성 아이템 추천</h3>
        <div class="swiper men-swiper">
            <div class="swiper-wrapper">
                <c:forEach var="product" items="${menProducts}">
                    <div class="swiper-slide">
                        <div class="product-card" onclick="location.href='/products/${product.id}'">
                            <div class="product-image">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${product.imageUrl}" alt="${product.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="placeholder">이미지 없음</div>
                                    </c:otherwise>
                                </c:choose>
                                <%-- 좋아요 하트 아이콘 --%>
                                <i class="fa-heart product-like-icon ${product.isLiked ? 'fas filled' : 'far empty'}"
                                   data-product-id="${product.id}"
                                   onclick="event.stopPropagation(); toggleProductLike(this);"></i>
                            </div>
                            <div class="product-brand">${product.brandName}</div>
                            <div class="product-name">${product.name}</div>
                            <div class="product-price">
                                <c:if test="${product.discountRate > 0}">
                                    <span class="discount-rate">${product.discountRate}%</span>
                                </c:if>
                                <span class="current-price">
                                    <fmt:formatNumber value="${product.currentPrice}" pattern="#,###"/>원
                                </span>
                                <c:if test="${product.originalPrice > product.currentPrice}">
                                    <span class="original-price">
                                        <fmt:formatNumber value="${product.originalPrice}" pattern="#,###"/>원
                                    </span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="swiper-button-next"></div>
            <div class="swiper-button-prev"></div>
        </div>

        <!-- 여성 아이템 -->
        <h3 class="sub-title-woman">여성 아이템 추천</h3>
        <div class="swiper women-swiper">
            <div class="swiper-wrapper">
                <c:forEach var="product" items="${womenProducts}">
                    <div class="swiper-slide">
                        <div class="product-card" onclick="location.href='/products/${product.id}'">
                            <div class="product-image">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${product.imageUrl}" alt="${product.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="placeholder">이미지 없음</div>
                                    </c:otherwise>
                                </c:choose>
                                <%-- 좋아요 하트 아이콘 --%>
                                <i class="fa-heart product-like-icon ${product.isLiked ? 'fas filled' : 'far empty'}"
                                   data-product-id="${product.id}"
                                   onclick="event.stopPropagation(); toggleProductLike(this);"></i>
                            </div>
                            <div class="product-brand">${product.brandName}</div>
                            <div class="product-name">${product.name}</div>
                            <div class="product-price">
                                <c:if test="${product.discountRate > 0}">
                                    <span class="discount-rate">${product.discountRate}%</span>
                                </c:if>
                                <span class="current-price">
                                    <fmt:formatNumber value="${product.currentPrice}" pattern="#,###"/>원
                                </span>
                                <c:if test="${product.originalPrice > product.currentPrice}">
                                    <span class="original-price">
                                        <fmt:formatNumber value="${product.originalPrice}" pattern="#,###"/>원
                                    </span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="swiper-button-next"></div>
            <div class="swiper-button-prev"></div>
        </div>
    </section>
</main>

<%-- jQuery --%>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<%-- 공통 좋아요 토글 스크립트 --%>
<script src="${jsLikeToggle}"></script>

<%-- Swiper JS --%>
<script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
<script>
    // 공통 스와이퍼 설정
    const swiperConfig = {
        slidesPerView: 5.5,   // 한 줄에 5.5개
        slidesPerGroup: 4,    // 슬라이드할 때 4개씩 이동
        spaceBetween: 0,
        speed: 600,
        loop: false,

        grid: {
            rows: 2,          // 2줄로 배치
            fill: 'row'
        },

        breakpoints: {
            320: {
                slidesPerView: 1.1,
                slidesPerGroup: 2,
                grid: { rows: 2 }
            },
            480: {
                slidesPerView: 2.2,
                slidesPerGroup: 2,
                grid: { rows: 2 }
            },
            768: {
                slidesPerView: 3.2,
                slidesPerGroup: 3,
                grid: { rows: 2 }
            },
            1024: {
                slidesPerView: 4.5,
                slidesPerGroup: 4,
                grid: { rows: 2 }
            },
            1200: {
                slidesPerView: 5.5,
                slidesPerGroup: 4,
                grid: { rows: 2 }
            }
        }
    };

    // 남성 스와이퍼 초기화
    const menSwiper = new Swiper('.men-swiper', {
        ...swiperConfig,
        navigation: {
            nextEl: '.men-swiper .swiper-button-next',
            prevEl: '.men-swiper .swiper-button-prev',
        }
    });

    // 여성 스와이퍼 초기화
    const womenSwiper = new Swiper('.women-swiper', {
        ...swiperConfig,
        navigation: {
            nextEl: '.women-swiper .swiper-button-next',
            prevEl: '.women-swiper .swiper-button-prev',
        }
    });

    const bannerSwiper = new Swiper('.banner-swiper', {
        slidesPerView: 3,      // 한 번에 보이는 배너 3개
        slidesPerGroup: 3,     // 슬라이드 이동 시 3개 단위
        spaceBetween: 0,      // 배너 간 간격
        loop: true,            // 무한 루프
        autoplay: {
            delay: 3000,       // 3초마다 자동 슬라이드
            disableOnInteraction: false,
        },
        navigation: {
            nextEl: '.banner-next',
            prevEl: '.banner-prev',
        },
        speed: 600
    });

    // 메인 페이지 상품 좋아요 토글 함수
    function toggleProductLike(iconElement) {
        const $icon = $(iconElement);
        const productId = $icon.attr('data-product-id');

        console.log('토글 시작 - productId:', productId);
        console.log('아이콘 엘리먼트:', iconElement);

        if (!productId) {
            alert('상품 정보를 찾을 수 없습니다.');
            return;
        }

        $.ajax({
            url: '/api/v1/products/' + productId + '/liked',
            method: 'POST',
            dataType: 'json',
            success: function (response) {
                // response.liked가 true이면 좋아요 상태, false이면 좋아요 해제 상태
                console.log('좋아요 토글 성공:', response);
                console.log('liked 값:', response.liked, '타입:', typeof response.liked);

                if (response.liked === true || response.liked === 1) {
                    $icon.removeClass('far empty').addClass('fas filled');
                } else {
                    $icon.removeClass('fas filled').addClass('far empty');
                }
            },
            error: function (xhr) {
                console.error('좋아요 토글 실패:', xhr);
                alert('좋아요 처리에 실패했습니다.');
            }
        });
    }
</script>

</body>
</html>
