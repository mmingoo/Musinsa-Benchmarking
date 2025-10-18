<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <title>카테고리 상품 목록</title>

    <%-- Resource URLs --%>
    <c:url value="/resources/js/common/likeToggle.js" var="jsLikeToggle"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/categoryProductsPage.css'/>">

    <%-- Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>

    <style>
        .product-like-icon {
            position: absolute;
            bottom: 8px;
            right: 8px;
            cursor: pointer;
            z-index: 10;
            font-size: 15px;
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
    </style>
</head>
<body>
<%@ include file="../main/header.jsp" %>

<main class="container">
    <section class="section">
        <div class="filter-bar">
            <div class="headline">
                "<strong id="categoryTitle"></strong>" 결과
            </div>

            <div class="sort-options">
                <button class="sort-btn" data-sort="LIKE">좋아요순</button>
                <button class="sort-btn" data-sort="PRICE_LOW">낮은 가격순</button>
                <button class="sort-btn" data-sort="PRICE_HIGH">높은 가격순</button>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty products}">
                <div class="empty">검색 결과가 없습니다.</div>
            </c:when>
            <c:otherwise>
                <div class="search-grid">
                    <c:forEach var="p" items="${products}">
                        <a class="product-card" href="<c:url value='/products/${p.productId}'/>"
                           data-product-id="${p.productId}"
                           data-price="${p.price}"
                           data-likes="${p.productLikes}">
                            <div class="product-image">
                                <c:choose>
                                    <c:when test="${not empty p.productImage}">
                                        <img src="<c:out value='${p.productImage}'/>"
                                             alt="<c:out value='${p.productName}'/>">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="<c:url value='/resources/img/placeholder.png'/>" alt="no image">
                                    </c:otherwise>
                                </c:choose>
                                <i class="fa-heart product-like-icon ${p.isLiked ? 'fas filled' : 'far empty'}"
                                   data-product-id="${p.productId}"
                                   onclick="event.preventDefault(); event.stopPropagation(); toggleProductLike(this);"></i>
                            </div>

                            <div class="product-brand">
                                <c:out value="${p.brandName}"/>
                            </div>
                            <div class="product-name"><c:out value='${p.productName}'/></div>

                            <div class="product-price">
                                <span class="current-price">
                                    <fmt:formatNumber value='${p.price}' pattern='#,###'/>원
                                </span>
                            </div>
                            <div class="likes-and-stars">
                                <c:if test='${p.productLikes != null}'>
                                    <span class="likes">♥ <fmt:formatNumber value='${p.productLikes}'
                                                                            pattern='#,###'/></span>
                                </c:if>
                                <c:if test='${p.ratingAverage != null}'>
                                    <span class="stars">★<fmt:formatNumber value='${p.ratingAverage}'
                                                                           pattern='0.0'/>(<fmt:formatNumber
                                            value='${p.reviewCount}'/>)</span>
                                </c:if>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </section>
</main>

<%-- jQuery --%>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<%-- 공통 좋아요 토글 스크립트 --%>
<script src="${jsLikeToggle}"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // 카테고리 이름 표시
        const name = sessionStorage.getItem('categoryName');
        if (name) {
            const safe = document.createTextNode(name);
            const target = document.getElementById('categoryTitle');
            target.textContent = '';
            target.appendChild(safe);
        }

        // 현재 URL에서 sortBy 파라미터 읽기
        const urlParams = new URLSearchParams(window.location.search);
        const currentSort = urlParams.get('sortBy') || 'LIKE';

        // 현재 정렬 버튼에 active 클래스 추가
        const sortButtons = document.querySelectorAll('.sort-btn');
        sortButtons.forEach(btn => {
            if (btn.dataset.sort === currentSort) {
                btn.classList.add('active');
            }

            // 클릭 이벤트 추가
            btn.addEventListener('click', function () {
                const sortType = this.dataset.sort;
                const newParams = new URLSearchParams(window.location.search);
                newParams.set('sortBy', sortType);
                window.location.search = newParams.toString();
            });
        });
    });

    // 상품 좋아요 토글 함수
    function toggleProductLike(iconElement) {
        const $icon = $(iconElement);
        const productId = $icon.attr('data-product-id');

        console.log('토글 시작 - productId:', productId);

        if (!productId) {
            alert('상품 정보를 찾을 수 없습니다.');
            return;
        }

        $.ajax({
            url: '/api/v1/products/' + productId + '/liked',
            method: 'POST',
            dataType: 'json',
            success: function (response) {
                console.log('좋아요 토글 성공:', response);

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

    // ========== 무한 스크롤 기능 ==========
    let lastId = null;
    let lastValue = null;
    let isLoading = false;
    let hasMoreData = true;

    // 페이지 로드 시 초기 커서 정보 추출
    $(document).ready(function() {
        const productCards = $('.product-card');
        if (productCards.length > 0) {
            const lastCard = productCards.last();
            lastId = parseInt(lastCard.attr('data-product-id'));

            const urlParams = new URLSearchParams(window.location.search);
            const sortBy = urlParams.get('sortBy') || 'LIKE';

            // sortBy에 따라 lastValue 설정
            if (sortBy === 'PRICE_LOW' || sortBy === 'PRICE_HIGH') {
                lastValue = parseInt(lastCard.attr('data-price'));
            } else {
                lastValue = parseInt(lastCard.attr('data-likes'));
            }

            console.log('초기 커서 설정:', {lastId, lastValue, sortBy});
        }

        // 🔥 초기 높이 체크 및 자동 로드
        setTimeout(() => {
            const docHeight = $(document).height();
            const winHeight = $(window).height();
            console.log('초기 높이 체크 - 문서:', docHeight, '윈도우:', winHeight);

            if (docHeight <= winHeight + 100) {
                console.log('초기 높이 부족 - 자동 로드 시작');
                loadMoreProducts();
            }
        }, 500);
    });

    function loadMoreProducts() {
        if (isLoading || !hasMoreData) {
            console.log('로드 중단 - isLoading:', isLoading, 'hasMoreData:', hasMoreData);
            return;
        }

        isLoading = true;
        console.log('상품 로드 시작...');

        const urlParams = new URLSearchParams(window.location.search);
        const categoryId = window.location.pathname.split('/').pop();
        const sortBy = urlParams.get('sortBy') || 'LIKE';

        const requestData = {
            sortBy: sortBy,
            size: 12
        };

        // 첫 페이지가 아니면 커서 값 추가
        if (lastId !== null && lastValue !== null) {
            requestData.lastId = lastId;
            requestData.lastValue = lastValue;
        }

        console.log('API 요청:', '/api/v1/products/category/' + categoryId, requestData);

        $.ajax({
            url: '/api/v1/products/category/' + categoryId,
            method: 'GET',
            data: requestData,
            success: function (products) {
                console.log('응답 받음 - 상품 수:', products.length);

                if (products.length === 0) {
                    hasMoreData = false;
                    console.log('더 이상 데이터 없음');
                    return;
                }

                // 마지막 상품의 커서 값 업데이트
                const lastProduct = products[products.length - 1];
                lastId = lastProduct.productId;

                // sortBy에 따라 lastValue 설정
                if (sortBy === 'PRICE_LOW' || sortBy === 'PRICE_HIGH') {
                    lastValue = lastProduct.price;
                } else if (sortBy === 'LIKE') {
                    lastValue = lastProduct.productLikes;
                }

                console.log('커서 업데이트:', {lastId, lastValue});

                const $grid = $('.search-grid');
                products.forEach(function (p) {
                    let likesHtml = p.productLikes ? '<span class="likes">♥ ' + p.productLikes.toLocaleString() + '</span>' : '';
                    let starsHtml = p.ratingAverage ? '<span class="stars">★' + p.ratingAverage.toFixed(1) + '(' + p.reviewCount + ')</span>' : '';

                    const productHtml = '<a class="product-card" href="/products/' + p.productId + '" ' +
                        'data-product-id="' + p.productId + '" ' +
                        'data-price="' + p.price + '" ' +
                        'data-likes="' + p.productLikes + '">' +
                        '<div class="product-image">' +
                        '<img src="' + (p.productImage || '/resources/img/placeholder.png') + '" alt="' + p.productName + '">' +
                        '<i class="fa-heart product-like-icon ' + (p.isLiked ? 'fas filled' : 'far empty') + '" ' +
                        'data-product-id="' + p.productId + '" ' +
                        'onclick="event.preventDefault(); event.stopPropagation(); toggleProductLike(this);"></i>' +
                        '</div>' +
                        '<div class="product-brand">' + p.brandName + '</div>' +
                        '<div class="product-name">' + p.productName + '</div>' +
                        '<div class="product-price">' +
                        '<span class="current-price">' + p.price.toLocaleString() + '원</span>' +
                        '</div>' +
                        '<div class="likes-and-stars">' + likesHtml + starsHtml + '</div>' +
                        '</a>';
                    $grid.append(productHtml);
                });

                isLoading = false;
                console.log('상품 추가 완료');
            },
            error: function (xhr) {
                console.error('상품 로드 실패:', xhr);
                isLoading = false;
            }
        });
    }

    // 🔥 개선된 스크롤 이벤트 리스너
    $(window).on('scroll', function () {
        const scrollTop = $(window).scrollTop();
        const windowHeight = $(window).height();
        const docHeight = $(document).height();
        const scrollBottom = scrollTop + windowHeight;

        // 디버깅 (스크롤 시작 시 한 번만)
        if (scrollTop > 0 && scrollTop < 10) {
            console.log('스크롤 위치:', {
                scrollTop: scrollTop,
                windowHeight: windowHeight,
                docHeight: docHeight,
                trigger: docHeight * 0.8
            });
        }

        // 🔥 80% 지점에서 트리거 (더 일찍 로드)
        if (scrollBottom >= docHeight * 0.8) {
            console.log('스크롤 트리거 발동!');
            loadMoreProducts();
        }
    });

</script>
</body>
</html>