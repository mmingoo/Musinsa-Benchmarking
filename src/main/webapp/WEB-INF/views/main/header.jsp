<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<%
    // Authorization 쿠키 확인
    String authToken = null;
    boolean isLoggedIn = false;

    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("Authorization".equals(cookie.getName())) {
                authToken = cookie.getValue();
                if (authToken != null && !authToken.trim().isEmpty()) {
                    isLoggedIn = true;
                }
                break;
            }
        }
    }
%>

<%-- 헤더 관련 CSS --%>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">

<%-- 카테고리 include (카테고리 관련 CSS, JS 포함) --%>
<%@ include file="category.jsp" %>
<header class="musinsa-header">
    <div class="inner">
        <%-- 로고와 메인 메뉴 --%>
        <div class="header-left">
            <button type="button" id="menuBtn" class="hamburger-btn" aria-label="카테고리 메뉴 열기">
                <i class="fas fa-bars"></i>
            </button>

            <%-- 햄버거 버튼과 로고 사이에 구분선 추가 --%>
            <div class="logo-separator">|</div>

            <div class="logo">
                <a href="${pageContext.request.contextPath}/main">MUSINSA</a>
            </div>

            <nav class="gnb">
                <ul>
                    <li><a href="#">BEAUTY</a></li>
                    <li><a href="#">PLAYER</a></li>
                    <li><a href="#">OUTLET</a></li>
                    <li><a href="#">BOUTIQUE</a></li>
                    <li><a href="#">SHOES</a></li>
                    <li><a href="#">KIDS</a></li>
                    <li><a href="#">USED</a></li>
                    <li class="separator">|</li>
                    <li><a href="#">[S]:SNAP</a></li>
                </ul>
            </nav>
        </div>

        <%-- 우측 사용자 메뉴 --%>
        <div class="header-right">
            <ul class="user-menu">
                <li><a href="#">오프라인스토어</a></li>
                <li class="separator">|</li>
                <li><a href="${pageContext.request.contextPath}/main/search" id="searchBtn"><i
                        class="fas fa-search"></i>검색</a></li>
                <li class="separator">|</li>
                <li><a href="#"><i class="far fa-heart"></i>좋아요</a></li>
                <li class="separator">|</li>
                <li><a href="${pageContext.request.contextPath}/mypage"><i class="far fa-user"></i>마이</a></li>
                <li class="separator">|</li>
                <li class="cart-item">
                    <a href="${pageContext.request.contextPath}/cart">
                        <i class="fas fa-shopping-cart"></i>장바구니
                        <% if (isLoggedIn) { %>
                        <span class="cart-badge" id="cartBadge" style="display: none;">0</span>
                        <% } %>
                    </a>
                </li>
                <li class="separator">|</li>
                <%-- 쿠키 기반 로그인/로그아웃 처리 --%>
                <% if (!isLoggedIn) { %>
                <li class="btn-outline-dark"><a href="${pageContext.request.contextPath}/login">로그인</a></li>
                <% } else { %>
                <li class="btn-outline-dark">
                    <a href="#" onclick="logout(); return false;">로그아웃</a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</header>

<script>
    function logout() {
        if (confirm('로그아웃 하시겠습니까?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/custom-logout';
            document.body.appendChild(form);
            form.submit();
        }
    }
</script>

<div id="searchOverlayContainer" data-overlay-container="recent"></div>

<script>
    window.appContextPath = window.appContextPath || '${pageContext.request.contextPath}';
    window.userIsLoggedIn = <%= isLoggedIn %>; // 쿠키 기반으로 전달
    window.musinsaUserId = null; // 필요 없으면 아예 제거 가능

    // 장바구니 개수 업데이트 함수
    function updateCartBadge() {
        const cartBadge = document.getElementById('cartBadge');
        if (!cartBadge) return;

        fetch(window.appContextPath + '/api/v1/carts/count', {
            method: 'GET',
            credentials: 'include' // Authorization 쿠키 자동 전송
        })
            .then(response => {
                if (response.ok) return response.json();
                throw new Error('장바구니 개수 조회 실패');
            })
            .then(count => {
                if (count > 0) {
                    cartBadge.textContent = count > 99 ? '99+' : count;
                    cartBadge.style.display = 'flex';
                } else {
                    cartBadge.style.display = 'none';
                }
            })
            .catch(error => {
                console.log('장바구니 개수 조회 중 오류:', error);
                cartBadge.style.display = 'none';
            });
    }

    // 페이지 로드 시 장바구니 개수 조회
    document.addEventListener('DOMContentLoaded', function () {
        updateCartBadge();
    });

    // 전역에서 호출 가능하도록
    window.updateCartBadge = updateCartBadge;
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/recentSearches.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/header.js"></script>
