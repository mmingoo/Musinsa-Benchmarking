<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <title>검색 화면</title>

    <%-- Resource URLs --%>
    <c:url value="/resources/js/common/likeToggle.js" var="jsLikeToggle"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/search.css'/>">

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

        .product-image {
            position: relative;
        }
    </style>
</head>
<body>
<%@ include file="../main/header.jsp" %>

<main class="container">
    <section class="section">

        <%--        <!-- 검색바 -->--%>
        <%--        <form class="search-header" action="<c:url value='/search'/>" method="get">--%>
        <%--            <input class="search-input" type="text" name="keyword"--%>
        <%--                   placeholder="브랜드/상품/카테고리 검색"--%>
        <%--                   value="${keyword != null ? keyword : result.searchKeyword}"/>--%>
        <%--            <button class="search-btn" type="submit">검색</button>--%>
        <%--        </form>--%>

        <!-- 키워드/결과 수 -->
        <div class="headline">
            "<strong><c:out value='${keyword != null ? keyword : result.searchKeyword}'/></strong>"
           검색 결과
        </div>

        <!-- 브랜드 정보(brandInfo 있을 때만 노출) -->
        <c:if test="${not empty result.brandInfo}">
            <a class="brand-box" href='<c:url value='/brands/${result.brandInfo.brandId}'/>'>
                <c:if test="${not empty result.brandInfo.brandImage}">
                    <img src="<c:out value='${result.brandInfo.brandImage}'/>"
                         alt="<c:out value='${result.brandInfo.brandNameKr}'/>">
                </c:if>
                <div>
                    <div style="font-weight:700;"><c:out value='${result.brandInfo.brandNameKr}'/></div>
                    <div class="brand-en"><c:out value='${result.brandInfo.brandNameEn}'/></div>
                </div>
                <div class="brand-count">
                    <fmt:formatNumber value="${result.brandInfo.totalCount}" pattern="#,###"/>개
                </div>
            </a>
        </c:if>

        <!-- 정렬 옵션 -->
        <div class="sort-options">
            <button class="sort-btn" data-sort="LIKE">좋아요순</button>
            <button class="sort-btn" data-sort="PRICE_LOW">낮은 가격순</button>
            <button class="sort-btn" data-sort="PRICE_HIGH">높은 가격순</button>
        </div>

        <%-- 어떤 리스트를 쓸지 선택 --%>
        <c:choose>
            <c:when test="${not empty result.brandInfo}">
                <c:set var="productList" value="${result.brandInfo.products}"/>
            </c:when>
            <c:otherwise>
                <c:set var="productList" value="${result.products}"/>
            </c:otherwise>
        </c:choose>

        <!-- 결과 그리드 -->
        <c:choose>
            <c:when test="${empty productList}">
                <div class="empty">검색 결과가 없습니다.</div>
            </c:when>
            <c:otherwise>
                <div class="search-grid">
                    <c:forEach var="p" items="${productList}">
                        <a class="product-card" href='<c:url value='/products/${p.productId}'/>'
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
                            <div class="product-brand">
                                <c:out value="${p.brandNameKr}"/> / <c:out value="${p.brandNameEn}"/>
                            </div>
                            <div class="product-name"><c:out value='${p.productName}'/></div>
                            <div class="product-price">
                                <span class="current-price"><fmt:formatNumber value='${p.price}'
                                                                              pattern='#,###'/>원</span>
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

<script>
    document.addEventListener('DOMContentLoaded', function () {
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
</script>

<%-- jQuery --%>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<%-- 공통 좋아요 토글 스크립트 --%>
<script src="${jsLikeToggle}"></script>

<script>
    // 검색 페이지 상품 좋아요 토글 함수
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
        const keyword = urlParams.get('keyword') || '';
        const sortBy = urlParams.get('sortBy') || 'LIKE';

        const requestData = {
            keyword: keyword,
            sortBy: sortBy,
            size: 12
        };

        // 첫 페이지가 아니면 커서 값 추가 (page 대신)
        if (lastId !== null && lastValue !== null) {
            requestData.lastId = lastId;
            requestData.lastValue = lastValue;
            requestData.page = 1; // 더미 값 (기존 API 호환)
        } else {
            requestData.page = 0; // 첫 페이지
        }

        $.ajax({
            url: '/api/v1/products/search',
            method: 'GET',
            data: requestData,
            success: function (result) {
                let products = [];
                if (result.brandInfo && result.brandInfo.products) {
                    products = result.brandInfo.products;
                } else if (result.products) {
                    products = result.products;
                }

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
                } else {
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
                        '<div class="product-brand">' + (p.brandNameKr || '') + ' / ' + (p.brandNameEn || '') + '</div>' +
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
