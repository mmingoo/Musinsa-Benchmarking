<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>제품 상세</title>

    <!-- 정적 리소스 경로 일원화 -->
    <c:url value="/resources/css/productDetailPage.css" var="cssProduct"/>
    <c:url value="/resources/css/header.css" var="cssHeader"/>
    <c:url value="/resources/js/common/likeToggle.js" var="jsLikeToggle"/>
    <c:url value="/resources/js/productDetailPage.js" var="jsDetail"/>

    <!-- CSS: 우선순위 높은 것 먼저 로드 -->
    <link rel="stylesheet" href="${cssHeader}"/>
    <link rel="stylesheet" href="${cssProduct}"/>

    <!-- 아이콘(CDN): 무결성 옵션(있으면 더 안전) -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
          referrerpolicy="no-referrer"/>

    <!-- jQuery: defer로 렌더링 지연 방지 -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" defer></script>
    <!-- 공통 좋아요 스크립트: productDetailPage.js 전에 로드되어야 함 -->
    <script src="${jsLikeToggle}" defer></script>
    <!-- 페이지 전용 스크립트: 항상 body 끝에 두는 게 이상적이지만, defer면 head에 있어도 괜찮음 -->
    <script src="${jsDetail}" defer></script>

    <!-- viewport -->
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
</head>

<body>
<%@ include file="../main/header.jsp" %>

<main id="main" class="container" role="main">
    <section class="product-layout" aria-label="제품 상세 레이아웃">

        <!-- 왼쪽: 이미지 + 상세 설명 + 사이즈 + 리뷰 -->
        <div class="product-left-column">
            <!-- 상품 이미지 영역 -->
            <section class="product-images-section" aria-labelledby="product-images-title">
                <h2 id="product-images-title" class="sr-only">상품 이미지</h2>
                <div class="image-previews" id="thumbnailImages" aria-live="polite"></div>
                <div class="main-image-container" id="mainImageContainer" aria-live="polite"></div>
            </section>

            <!-- 상품 상세 설명 -->
            <section class="product-detail-section" aria-labelledby="detail-desc-title">
                <h2 id="detail-desc-title" class="sr-only">상품 상세 설명</h2>
                <div id="detailDescription" class="product-description-url"></div>
            </section>

            <!-- 사이즈 정보 -->
            <section class="product-size-section" aria-labelledby="size-info-title">
                <h2 id="size-info-title" class="sr-only">사이즈 정보</h2>
                <div id="detailSizeImage" class="product-detailSizeImage-url"></div>
                <div id="productDetailSizeList" class="product-detail-size-list"></div>
            </section>

            <!-- 리뷰 섹션 -->
            <section class="product-review-section" aria-labelledby="review-title">
                <h2 id="review-title" class="sr-only">리뷰</h2>

                <!-- ✅ 리뷰 요약 -->
                <div class="review-summary" aria-label="평균 평점 요약">
                    <div class="review-summary-stars" id="avgStars" aria-hidden="true"></div>
                    <div class="review-summary-text">
                        <strong id="avgScore">0.0</strong> / 5
                        <span id="reviewTotalCount" class="muted">후기 0개</span>
                    </div>
                </div>

                <!-- ✅ 리뷰 작성 -->
                <form class="review-write" id="reviewWriteForm" onsubmit="return false;">
                    <div class="star-input" id="writeStars" data-value="0" role="radiogroup" aria-label="평점 선택">
                        <button type="button" class="star-btn" data-score="1" aria-label="1점">★</button>
                        <button type="button" class="star-btn" data-score="2" aria-label="2점">★</button>
                        <button type="button" class="star-btn" data-score="3" aria-label="3점">★</button>
                        <button type="button" class="star-btn" data-score="4" aria-label="4점">★</button>
                        <button type="button" class="star-btn" data-score="5" aria-label="5점">★</button>
                    </div>

                    <div class="review-write-row">
                        <input type="text" id="reviewPurchaseOption" placeholder="구매 옵션 (예: 블랙 / M)"/>
                    </div>
                    <div class="review-write-row">
                        <input id="reviewContent" rows="4" placeholder="리뷰 내용을 입력하세요">
                    </div>
                    <div class="review-write-actions">
                        <button type="button" id="btnReviewSubmit" class="btn primary">리뷰 등록</button>
                    </div>
                </form>

                <!-- ✅ 리뷰 목록 -->
                <div id="productsReviews" class="product-reviews" aria-live="polite"></div>
            </section>

        </div>

        <!-- 오른쪽: 제품 정보 및 구매 옵션 -->
        <aside class="product-right-column" aria-label="구매 정보">
            <div class="product-info-sticky">

                <!-- 브랜드 및 상품명 -->
                <section class="seller-info" aria-labelledby="brand-and-product">
                    <h2 id="brand-and-product" class="sr-only">브랜드 및 상품 정보</h2>

                    <div class="brand-header">
                        <span class="brand-image-container" id="brand-image-container" aria-hidden="true"></span>
                        <span class="brand-name" id="brandName"></span>

                        <!-- 브랜드 좋아요 버튼 -->
                        <button type="button" class="brand-wishlist-icon" id="brandLikeButton" aria-pressed="false"
                                aria-label="브랜드 좋아요">
                            <i class="far fa-heart heart-icon" aria-hidden="true"></i>
                            <span id="brandLikeCnt" aria-live="polite"></span>
                        </button>
                    </div>

                    <div id="categoryName" class="product-categories" aria-label="카테고리"></div>
                    <h3 class="product-title" id="productName"></h3>

                    <!-- 별점 및 리뷰 (JS가 값 채움) -->
                    <div class="product-rating" aria-label="평점 요약">
                        <!-- ✅ 여러 개 별을 동적으로 채울 자리 -->
                        <span class="star-icon" aria-hidden="true">★</span> <!-- 이 한 개만 유지 -->
                        <span class="rating-score" id="summaryRatingScore">0.0</span>
                        <span class="review-count" id="summaryReviewCount">후기 0개</span>
                    </div>

                </section>

                <!-- 색상 스와치 -->
                <section aria-labelledby="color-title">
                    <h2 id="color-title" class="sr-only">색상 선택</h2>
                    <div class="color-swatches" id="colorSwatches"></div>
                </section>

                <!-- 가격 섹션 -->
                <section class="price-section" aria-labelledby="price-title">
                    <h2 id="price-title" class="sr-only">가격 정보</h2>

                    <div class="price-header">
                        <span class="original-price" id="productPrice" aria-label="정가"></span>
                    </div>

                    <div class="price-main">
                        <span class="sale-percentage" id="productDiscount" aria-label="할인율"></span>
                        <span class="discounted-price" id="productTotalPrice" aria-label="할인가"></span>
                    </div>



                    <!-- 혜택 상세(토글) -->
                    <div id="benefitDetail" class="benefit-detail" hidden></div>
                </section>

                <!-- 적립금 섹션 (정적 가이드 문구라면 유지, 동적이면 JS로 교체) -->
                <section class="point-section" aria-labelledby="point-title">
                    <h2 id="point-title" class="sr-only">적립 혜택</h2>
                    <p class="max-point-amount">9,250원 최대적립</p>
                    <ul>
                        <li>· 등급 적립 (LV.5 실버 • 1.5%) <span>2,530원</span></li>
                        <li>· 후기 적립 <span>2,500원</span></li>
                        <li>· 무신사머니 결제 시 적립 2.5% <span>4,220원</span></li>
                    </ul>
                </section>

                <!-- M Money 배너 -->
                <section class="money-benefit-banner" aria-label="무신사머니 혜택">
                    <span class="money-logo" aria-hidden="true">M Money</span>
                    <p>무신사머니 첫 결제 시 <strong>10%</strong> 추가 적립</p>
                    <span aria-hidden="true">›</span>
                </section>

                <!-- 옵션 선택 -->
                <section class="options-section" aria-labelledby="option-title">
                    <h2 id="option-title" class="sr-only">옵션 선택</h2>

                    <label for="color-select" class="sr-only">컬러</label>
                    <select id="color-select" class="color-select">
                        <option value="">컬러</option>
                    </select>

                    <label for="size-select" class="sr-only">사이즈</label>
                    <select id="size-select" class="size-select" aria-disabled="true">
                        <option value="">사이즈</option>
                    </select>
                </section>

                <!-- 수량 선택 -->
                <section class="quantity-section" aria-labelledby="qty-title">
                    <h2 id="qty-title" class="sr-only">수량 선택</h2>
                    <div class="quantity-controls" role="group" aria-label="수량 조절">
                        <button type="button" class="quantity-btn" id="decrease-btn" aria-label="수량 감소">−</button>
                        <input type="text" id="quantity-input" value="1" inputmode="numeric" aria-live="polite"
                               aria-label="현재 수량" readonly/>
                        <button type="button" class="quantity-btn" id="increase-btn" aria-label="수량 증가">+</button>
                    </div>
                </section>

                <!-- 하단 액션 버튼들 -->
                <div class="bottom-action-buttons" role="group" aria-label="동작 버튼">
                    <button type="button" class="wishlist-icon" id="productLikeButton" aria-pressed="false"
                            aria-label="상품 좋아요">
                        <i class="far fa-heart heart-icon" aria-hidden="true"></i>
                        <span id="productLikeCnt" aria-live="polite"></span>
                    </button>
                    <button type="button" class="add-to-cart-btn large-btn" id="addToCartBtn">장바구니</button>
                    <button type="button" class="buy-now large-btn" id="buyNowBtn">구매하기</button>
                </div>

                <!-- 숨겨진 데이터: data-*로 관리 (DOM 탐색 용이, 시맨틱 보존) -->
                <div id="pageMeta"
                     data-brand-id=""
                     data-product-id=""
                     hidden></div>
            </div>
        </aside>
    </section>
</main>
</body>
</html>
