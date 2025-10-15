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
    <!-- 검색 화면과 동일 톤 유지 -->
    <link rel="stylesheet" href="<c:url value='/resources/css/categoryProductsPage.css'/>">

    <%-- Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>

    <style>
        /* 좋아요 하트 아이콘 스타일 */
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

        /* 인라인 스타일 오버라이드 제거 - CSS 파일에서 관리 */
    </style>
</head>
<body>
<%@ include file="../main/header.jsp" %>

<main class="container">
    <section class="section">
        <div class="filter-bar">
            <!-- 검색 화면의 headline 스타일 재사용 -->
            <div class="headline">
                "<strong id="categoryTitle"></strong>" 결과
            </div>

            <!-- 정렬 옵션 -->
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
                                <%-- 좋아요 하트 아이콘 --%>
                                <i class="fa-heart product-like-icon ${p.isLiked ? 'fas filled' : 'far empty'}"
                                   data-product-id="${p.productId}"
                                   onclick="event.preventDefault(); event.stopPropagation(); toggleProductLike(this);"></i>
                            </div>

                            <!-- 검색 화면 구조와 동일: 브랜드/이름/가격/좋아요 -->
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

                // URL 파라미터 업데이트
                const newParams = new URLSearchParams(window.location.search);
                newParams.set('sortBy', sortType);

                // 페이지 리로드
                window.location.search = newParams.toString();
            });
        });
    });

    // 카테고리 페이지 상품 좋아요 토글 함수
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

                // response.liked가 true이면 좋아요 상태, false이면 좋아요 해제 상태
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

    // 커서 기반 무한 스크롤 기능
    let lastId = null;
    let lastValue = null;
    let isLoading = false;
    let hasMoreData = true;

    // 페이지 로드 시 이미 렌더링된 마지막 상품의 커서 정보 추출
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
    });

    function loadMoreProducts() {
        if (isLoading || !hasMoreData) return;

        isLoading = true;

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

        $.ajax({
            url: '/api/v1/products/category/' + categoryId,
            method: 'GET',
            data: requestData,
            success: function (products) {
                if (products.length === 0) {
                    hasMoreData = false;
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

                const $grid = $('.search-grid');
                products.forEach(function (p) {
                    let likesHtml = p.productLikes ? '<span class="likes">♥ ' + p.productLikes.toLocaleString() + '</span>' : '';
                    let starsHtml = p.ratingAverage ? '<span class="stars">★' + p.ratingAverage.toFixed(1) + '(' + p.reviewCount + ')</span>' : '';

                    const productHtml = '<a class="product-card" href="/products/' + p.productId + '">' +
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
            },
            error: function (xhr) {
                console.error('상품 로드 실패:', xhr);
                isLoading = false;
            }
        });
    }

    // 스크롤 이벤트 리스너
    $(window).on('scroll', function () {
        if ($(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
            loadMoreProducts();
        }
    });

</script>
</body>
</html>