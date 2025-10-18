<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>후드 집업 - MUSINSA</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f8f9fa;
        }

        /* Header - 기존과 동일 */
        .header {
            background-color: #000;
            color: white;
            padding: 0;
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .header-top {
            background-color: #333;
            padding: 8px 0;
            font-size: 12px;
            text-align: right;
            padding-right: 20px;
        }

        .header-top a {
            color: #ccc;
            text-decoration: none;
            margin: 0 5px;
        }

        .header-main {
            display: flex;
            align-items: center;
            padding: 15px 20px;
        }

        .menu-btn {
            background: none;
            border: none;
            color: white;
            font-size: 18px;
            margin-right: 20px;
            cursor: pointer;
        }

        .logo {
            font-size: 24px;
            font-weight: bold;
            margin-right: 30px;
        }

        .nav-menu {
            display: flex;
            list-style: none;
            margin: 0;
            padding: 0;
        }

        .nav-menu li {
            margin-right: 30px;
        }

        .nav-menu a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            font-size: 14px;
        }

        .search-container {
            flex: 1;
            max-width: 600px;
            margin-left: auto;
            margin-right: 20px;
        }

        .search-box {
            width: 100%;
            padding: 15px 20px;
            border: none;
            border-radius: 25px;
            background-color: #fff;
            color: #333;
            font-size: 14px;
        }

        .user-menu {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .notification-btn {
            background: none;
            border: none;
            color: white;
            font-size: 18px;
            cursor: pointer;
        }

        /* 카테고리 네비게이션 */
        .category-nav {
            background-color: #f8f9fa;
            padding: 15px 0;
            border-bottom: 1px solid #dee2e6;
        }

        .category-nav-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .breadcrumb {
            font-size: 13px;
            color: #666;
            margin-bottom: 10px;
        }

        .breadcrumb a {
            color: #666;
            text-decoration: none;
        }

        .breadcrumb a:hover {
            color: #333;
        }

        .category-tabs {
            display: flex;
            gap: 15px;
            overflow-x: auto;
        }

        .category-tab {
            background: none;
            border: 1px solid #dee2e6;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 13px;
            color: #666;
            cursor: pointer;
            white-space: nowrap;
            transition: all 0.2s;
        }

        .category-tab.active {
            background-color: #000;
            color: white;
            border-color: #000;
        }

        .category-tab:hover {
            background-color: #f0f0f0;
        }

        .category-tab.active:hover {
            background-color: #333;
        }

        /* 메인 컨테이너 */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
        }

        /* 상품 정보 헤더 */
        .product-info-header {
            margin-bottom: 20px;
        }

        .product-count {
            font-size: 16px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }

        /* 이벤트 해시태그 */
        .event-tags {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            overflow-x: auto;
        }

        .event-tag {
            background: none;
            border: 1px solid #007bff;
            color: #007bff;
            padding: 6px 12px;
            border-radius: 15px;
            font-size: 12px;
            cursor: pointer;
            white-space: nowrap;
            transition: all 0.2s;
        }

        .event-tag:hover {
            background-color: #007bff;
            color: white;
        }

        /* 필터 영역 */
        .filter-section {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 15px 0;
            border-top: 1px solid #dee2e6;
            border-bottom: 1px solid #dee2e6;
            margin-bottom: 30px;
        }

        .filters {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .filter-item {
            position: relative;
        }

        .filter-btn {
            background: white;
            border: 1px solid #ddd;
            padding: 8px 12px;
            border-radius: 20px;
            font-size: 13px;
            color: #333;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 4px;
            white-space: nowrap;
        }

        .filter-btn:hover {
            background-color: #f8f9fa;
        }

        .filter-btn.active {
            background-color: #000;
            color: white;
            border-color: #000;
        }

        .filter-arrow {
            width: 0;
            height: 0;
            border-left: 3px solid transparent;
            border-right: 3px solid transparent;
            border-top: 4px solid #666;
            margin-left: 2px;
        }

        .filter-btn.active .filter-arrow {
            border-top-color: white;
        }

        /* 전체 필터 모달 */
        .filter-modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 3000;
            display: none;
        }

        .filter-modal {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 90%;
            max-width: 900px;
            height: 80vh;
            background: white;
            border-radius: 12px;
            z-index: 3001;
            overflow: hidden;
        }

        .filter-modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .filter-modal-title {
            font-size: 18px;
            font-weight: bold;
        }

        .filter-modal-close {
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: #666;
        }

        .filter-modal-content {
            padding: 20px;
            height: calc(80vh - 140px);
            overflow-y: auto;
        }

        .filter-modal-tabs {
            display: flex;
            border-bottom: 1px solid #eee;
            margin-bottom: 20px;
        }

        .filter-modal-tab {
            padding: 15px 20px;
            border: none;
            background: none;
            cursor: pointer;
            border-bottom: 2px solid transparent;
            font-size: 14px;
            color: #666;
        }

        .filter-modal-tab.active {
            color: #333;
            border-bottom-color: #000;
            font-weight: 500;
        }

        .filter-modal-section {
            margin-bottom: 30px;
        }

        .filter-modal-section-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 15px;
            color: #333;
        }

        .filter-options {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .filter-option {
            padding: 8px 16px;
            border: 1px solid #ddd;
            border-radius: 20px;
            background: white;
            font-size: 13px;
            color: #333;
            cursor: pointer;
            transition: all 0.2s;
        }

        .filter-option:hover {
            background-color: #f8f9fa;
        }

        .filter-option.selected {
            background-color: #000;
            color: white;
            border-color: #000;
        }

        .filter-modal-footer {
            padding: 20px;
            border-top: 1px solid #eee;
            display: flex;
            gap: 10px;
            justify-content: center;
        }

        .filter-reset-btn {
            padding: 12px 24px;
            border: 1px solid #ddd;
            background: white;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }

        .filter-apply-btn {
            padding: 12px 24px;
            border: none;
            background: #000;
            color: white;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }

        /* 정렬 드롭다운 */
        .sort-dropdown {
            position: relative;
        }

        .sort-btn {
            background: none;
            border: 1px solid #dee2e6;
            padding: 8px 15px;
            border-radius: 4px;
            font-size: 13px;
            color: #333;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .sort-btn:hover {
            background-color: #f8f9fa;
        }

        .sort-dropdown-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            min-width: 150px;
            display: none;
            z-index: 100;
        }

        .sort-dropdown-menu.show {
            display: block;
        }

        .sort-option {
            padding: 10px 15px;
            font-size: 13px;
            color: #333;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .sort-option:hover {
            background-color: #f8f9fa;
        }

        .sort-option.active {
            background-color: #007bff;
            color: white;
        }

        /* 상품 그리드 */
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            gap: 0;
        }

        .product-card {
            border-right: 1px solid #f0f0f0;
            border-bottom: 1px solid #f0f0f0;
            padding: 20px;
            cursor: pointer;
            background: white;
            transition: background-color 0.2s;
        }

        .product-card:hover {
            background-color: #fafafa;
        }

        .product-image {
            width: 100%;
            height: 300px;
            background-color: #f8f9fa;
            border-radius: 4px;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #999;
            font-size: 14px;
        }

        .product-brand {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
            font-weight: 500;
        }

        .product-name {
            font-size: 13px;
            color: #333;
            margin-bottom: 8px;
            line-height: 1.3;
            height: 34px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
        }

        .product-price {
            display: flex;
            align-items: center;
            gap: 5px;
            margin-bottom: 8px;
        }

        .discount-rate {
            color: #e74c3c;
            font-weight: bold;
            font-size: 14px;
        }

        .current-price {
            font-weight: bold;
            font-size: 14px;
            color: #333;
        }

        .original-price {
            color: #999;
            text-decoration: line-through;
            font-size: 12px;
        }

        .product-stats {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 11px;
            color: #999;
        }

        .product-stats span {
            display: flex;
            align-items: center;
            gap: 2px;
        }

        /* 이벤트 태그 */
        .product-event-tag {
            position: absolute;
            top: 10px;
            left: 10px;
            background-color: #007bff;
            color: white;
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: bold;
        }

        /* 반응형 */
        @media (max-width: 768px) {
            .nav-menu {
                display: none;
            }

            .search-container {
                max-width: 150px;
            }

            .container {
                padding: 15px;
            }

            .product-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .product-card {
                padding: 15px;
            }

            .product-image {
                height: 200px;
            }

            .filters {
                flex-wrap: wrap;
                gap: 8px;
            }

            .filter-section {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }
        }
    </style>
</head>
<body>
<!-- Header -->
<header class="header">
    <div class="header-top">
        <a href="#">오프라인 스토어</a> |
        <a href="#">검색</a>
        <a href="#">좋아요</a>
        <a href="#">마이</a>
        <a href="#">장바구니</a>
        <span style="background: #007bff; padding: 2px 6px; border-radius: 3px; margin-left: 5px;">6</span>
    </div>
    <div class="header-main">
        <button class="menu-btn" id="menuBtn">☰</button>
        <div class="logo">MUSINSA</div>
        <nav>
            <ul class="nav-menu">
                <li><a href="#">MUSINSA</a></li>
                <li><a href="#">BEAUTY</a></li>
                <li><a href="#">PLAYER</a></li>
                <li><a href="#">OUTLET</a></li>
                <li><a href="#">BOUTIQUE</a></li>
                <li><a href="#">SHOES</a></li>
                <li><a href="#">KIDS</a></li>
                <li><a href="#">USED</a></li>
            </ul>
        </nav>
        <div class="search-container">
            <input type="text" class="search-box" placeholder="후드집업">
        </div>
        <div class="user-menu">
            <button class="notification-btn">🔔</button>
        </div>
    </div>
</header>

<!-- 카테고리 네비게이션 -->
<div class="category-nav">
    <div class="category-nav-container">
        <div class="breadcrumb">
            <a href="#">전체</a> > <a href="#">나일론/코치 재킷</a> > <a href="#">트러커 재킷</a> > <a href="#">블루종/MA-1</a> >
            <strong>후드 집업</strong> > <a href="#">환절기 코트</a> > <a href="#">레더/라이더스 재킷</a>
        </div>
        <div class="category-tabs">
            <button class="category-tab">전체</button>
            <button class="category-tab">나일론/코지 재킷</button>
            <button class="category-tab">트러커 재킷</button>
            <button class="category-tab">블루종/MA-1</button>
            <button class="category-tab active">후드 집업</button>
            <button class="category-tab">환절기 코트</button>
            <button class="category-tab">레더/라이더스 재킷</button>
            <button class="category-tab">스타디움 재킷</button>
            <button class="category-tab">트레이닝 재킷</button>
            <button class="category-tab">숏패딩/베스트 아우터</button>
            <button class="category-tab">아노락 재킷</button>
            <button class="category-tab">겨울 기타 코트</button>
            <button class="category-tab">슈트/블레이저 재킷</button>
            <button class="category-tab">겨울 싱글 코트</button>
            <button class="category-tab">플리스/뽀글이</button>
            <button class="category-tab">사파리/헌팅 재킷</button>
        </div>
    </div>
</div>

<!-- 메인 컨테이너 -->
<div class="container">
    <!-- 상품 정보 헤더 -->
    <div class="product-info-header">
        <div class="product-count">상품 17,316개</div>

        <!-- 이벤트 해시태그 -->
        <div class="event-tags">
            <button class="event-tag">🌟 FW 신상</button>
            <button class="event-tag">📱 무배한정</button>
            <button class="event-tag">📈 쿠폰</button>
            <button class="event-tag">⏰ 타임세일</button>
            <button class="event-tag">Ⓜ️ 단독</button>
        </div>
    </div>

    <!-- 필터 영역 -->
    <div class="filter-section">
        <div class="filters">
            <div class="filter-item">
                <button class="filter-btn" onclick="openFilterModal()">
                    필터
                    <div class="filter-arrow"></div>
                </button>
            </div>
            <div class="filter-item">
                <button class="filter-btn">
                    남
                    <div class="filter-arrow"></div>
                </button>
            </div>
            <div class="filter-item">
                <button class="filter-btn">
                    여
                    <div class="filter-arrow"></div>
                </button>
            </div>
            <div class="filter-item">
                <button class="filter-btn">
                    컬러
                    <div class="filter-arrow"></div>
                </button>
            </div>
            <div class="filter-item">
                <button class="filter-btn">
                    가격
                    <div class="filter-arrow"></div>
                </button>
            </div>
            <div class="filter-item">
                <button class="filter-btn">
                    브랜드
                    <div class="filter-arrow"></div>
                </button>
            </div>
        </div>

        <!-- 정렬 드롭다운 -->
        <div class="sort-dropdown">
            <button class="sort-btn" id="sortBtn">
                무신사 추천순
                <div class="filter-arrow"></div>
            </button>
            <div class="sort-dropdown-menu" id="sortMenu">
                <div class="sort-option active" data-value="recommend">무신사 추천순</div>
                <div class="sort-option" data-value="price_low">낮은 가격순</div>
                <div class="sort-option" data-value="price_high">높은 가격순</div>
                <div class="sort-option" data-value="review">리뷰수</div>
                <div class="sort-option" data-value="sales">판매수량순</div>
            </div>
        </div>
    </div>

    <!-- 필터 모달 -->
    <div class="filter-modal-overlay" id="filterModalOverlay">
        <div class="filter-modal">
            <div class="filter-modal-header">
                <div class="filter-modal-title">필터</div>
                <button class="filter-modal-close" onclick="closeFilterModal()">✕</button>
            </div>

            <div class="filter-modal-tabs">
                <button class="filter-modal-tab active" data-tab="all">전체</button>
                <button class="filter-modal-tab" data-tab="gender">성별</button>
                <button class="filter-modal-tab" data-tab="color">컬러</button>
                <button class="filter-modal-tab" data-tab="price">가격</button>
                <button class="filter-modal-tab" data-tab="size">사이즈</button>
                <button class="filter-modal-tab" data-tab="brand">브랜드</button>
                <button class="filter-modal-tab" data-tab="detail">상세조건</button>
                <button class="filter-modal-tab" data-tab="type">상품유형</button>
            </div>

            <div class="filter-modal-content">
                <!-- 전체 탭 -->
                <div class="filter-tab-content" id="all-content">
                    <div class="filter-modal-section">
                        <div class="filter-modal-section-title">패션 페스타</div>
                        <div class="filter-options">
                            <div class="filter-option">패션 페스타</div>
                            <div class="filter-option">무배한정</div>
                            <div class="filter-option">쿠폰</div>
                            <div class="filter-option">무신사단독</div>
                        </div>
                    </div>

                    <div class="filter-modal-section">
                        <div class="filter-modal-section-title">성별</div>
                        <div class="filter-options">
                            <div class="filter-option">남성</div>
                            <div class="filter-option">여성</div>
                            <div class="filter-option">판매예정자리</div>
                        </div>
                    </div>

                    <div class="filter-modal-section">
                        <div class="filter-modal-section-title">컬러</div>
                        <div class="filter-options">
                            <div class="filter-option">화이트</div>
                            <div class="filter-option">블랙</div>
                            <div class="filter-option">그레이</div>
                            <div class="filter-option">네이비</div>
                            <div class="filter-option">베이지</div>
                            <div class="filter-option">브라운</div>
                            <div class="filter-option">레드</div>
                            <div class="filter-option">핑크</div>
                            <div class="filter-option">옐로우</div>
                            <div class="filter-option">그린</div>
                            <div class="filter-option">블루</div>
                            <div class="filter-option">퍼플</div>
                        </div>
                    </div>

                    <div class="filter-modal-section">
                        <div class="filter-modal-section-title">가격</div>
                        <div class="filter-options">
                            <div class="filter-option">10만원 이하</div>
                            <div class="filter-option">10만원 ~ 20만원</div>
                            <div class="filter-option">20만원 ~ 30만원</div>
                            <div class="filter-option">30만원 ~ 50만원</div>
                            <div class="filter-option">50만원 이상</div>
                        </div>
                    </div>

                    <div class="filter-modal-section">
                        <div class="filter-modal-section-title">브랜드</div>
                        <div class="filter-options">
                            <div class="filter-option">나이키</div>
                            <div class="filter-option">아디다스</div>
                            <div class="filter-option">무신사 스탠다드</div>
                            <div class="filter-option">디스이즈네버댓</div>
                            <div class="filter-option">마크곤잘레스</div>
                            <div class="filter-option">챔피온</div>
                        </div>
                    </div>
                </div>

                <!-- 다른 탭들의 내용도 동일한 형식으로 추가 가능 -->
            </div>

            <div class="filter-modal-footer">
                <button class="filter-reset-btn">초기화</button>
                <button class="filter-apply-btn">179개의 상품보기</button>
            </div>
        </div>
    </div>

    <!-- 상품 그리드 -->
    <div class="product-grid">
        <!-- 상품 1 -->
        <div class="product-card">
            <div class="product-image">
                <span class="product-event-tag">FW 신상</span>
                이미지
            </div>
            <div class="product-brand">디펜드파크</div>
            <div class="product-name">ASI 후드집 에센셜 피그먼트 후드집업, 차콜</div>
            <div class="product-price">
                <span class="discount-rate">27%</span>
                <span class="current-price">36,900원</span>
            </div>
            <div class="product-stats">
                <span>♥ 162</span>
                <span>★ 4.8(54)</span>
            </div>
        </div>

        <!-- 상품 2 -->
        <div class="product-card">
            <div class="product-image">
                <span class="product-event-tag">FW 신상</span>
                이미지
            </div>
            <div class="product-brand">레이디</div>
            <div class="product-name">레이브 ZIP UP HOODIE(WINE)</div>
            <div class="product-price">
                <span class="discount-rate">35%</span>
                <span class="current-price">61,750원</span>
            </div>
            <div class="product-stats">
                <span>♥ 3,658</span>
                <span>★ 4.9(134)</span>
            </div>
        </div>

        <!-- 상품 3 -->
        <div class="product-card">
            <div class="product-image">
                <span class="product-event-tag">FW 신상</span>
                이미지
            </div>
            <div class="product-brand">롱드디</div>
            <div class="product-name">레어투돈 아트워크 후드집업 - 브라운</div>
            <div class="product-price">
                <span class="discount-rate">30%</span>
                <span class="current-price">78,400원</span>
            </div>
            <div class="product-stats">
                <span>♥ 279</span>
                <span>★ 4.4(334)</span>
            </div>
        </div>

        <!-- 상품 4 -->
        <div class="product-card">
            <div class="product-image">이미지</div>
            <div class="product-brand">디럭스라벨프로젝트</div>
            <div class="product-name">디치트 피그먼트 후드집업 다크그레이 MJO 17437</div>
            <div class="product-price">
                <span class="discount-rate">36%</span>
                <span class="current-price">49,800원</span>
            </div>
            <div class="product-stats">
                <span>♥ 140</span>
                <span>★ 4.7(81)</span>
            </div>
        </div>

        <!-- 상품 5 -->
        <div class="product-card">
            <div class="product-image">이미지</div>
            <div class="product-brand">브랜드드</div>
            <div class="product-name">CLOUD HOODIE ZIP-UP [MELANGE G RAY]</div>
            <div class="product-price">
                <span class="discount-rate">30%</span>
                <span class="current-price">89,600원</span>
            </div>
            <div class="product-stats">
                <span>♥ 755</span>
                <span>★ 5.0(5)</span>
            </div>
        </div>

        <!-- 상품 6 -->
        <div class="product-card">
            <div class="product-image">이미지</div>
            <div class="product-brand">메멘토</div>
            <div class="product-name">뎁컨 민녹 디자인 차칠 재킷터</div>
            <div class="product-price">
                <span class="current-price">113,050원</span>
            </div>
            <div class="product-stats">
                <span>♥ 122</span>
                <span>★ 4.8(338)</span>
            </div>
        </div>

        <!-- 더 많은 상품들 추가 가능 -->
        <c:forEach var="product" items="${products}" varStatus="status">
            <div class="product-card" onclick="location.href='/product/${product.id}'">
                <div class="product-image">
                    <c:if test="${product.isNewArrival}">
                        <span class="product-event-tag">FW 신상</span>
                    </c:if>
                    <img src="${product.imageUrl}" alt="${product.name}" style="width: 100%; height: 100%; object-fit: cover;">
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
                <div class="product-stats">
                    <span>♥ ${product.likeCount}</span>
                    <span>★ ${product.rating}(${product.reviewCount})</span>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script>
    // 필터 모달 관련 함수들
    function openFilterModal() {
        document.getElementById('filterModalOverlay').style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    function closeFilterModal() {
        document.getElementById('filterModalOverlay').style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    // 필터 모달 외부 클릭시 닫기
    document.getElementById('filterModalOverlay').addEventListener('click', function(e) {
        if (e.target === this) {
            closeFilterModal();
        }
    });

    // 필터 옵션 선택 기능
    const filterOptions = document.querySelectorAll('.filter-option');
    filterOptions.forEach(option => {
        option.addEventListener('click', function() {
            this.classList.toggle('selected');
            updateFilterCount();
        });
    });

    // 선택된 필터 개수 업데이트
    function updateFilterCount() {
        const selectedCount = document.querySelectorAll('.filter-option.selected').length;
        const applyBtn = document.querySelector('.filter-apply-btn');
        if (selectedCount > 0) {
            applyBtn.textContent = `${selectedCount}개 조건으로 ${Math.floor(Math.random() * 500 + 100)}개의 상품보기`;
        } else {
            applyBtn.textContent = '179개의 상품보기';
        }
    }

    // 필터 초기화
    document.querySelector('.filter-reset-btn').addEventListener('click', function() {
        document.querySelectorAll('.filter-option.selected').forEach(option => {
            option.classList.remove('selected');
        });
        updateFilterCount();
    });

    // 필터 적용
    document.querySelector('.filter-apply-btn').addEventListener('click', function() {
        const selectedFilters = Array.from(document.querySelectorAll('.filter-option.selected'))
            .map(option => option.textContent);
        console.log('적용된 필터:', selectedFilters);
        closeFilterModal();
    });

    // 정렬 드롭다운 기능
    const sortBtn = document.getElementById('sortBtn');
    const sortMenu = document.getElementById('sortMenu');
    const sortOptions = document.querySelectorAll('.sort-option');

    sortBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        sortMenu.classList.toggle('show');
    });

    sortOptions.forEach(option => {
        option.addEventListener('click', function() {
            // 기존 active 제거
            sortOptions.forEach(opt => opt.classList.remove('active'));
            // 선택한 옵션에 active 추가
            this.classList.add('active');

            // 버튼 텍스트 변경
            sortBtn.innerHTML = this.textContent + '<div class="filter-arrow"></div>';

            // 드롭다운 닫기
            sortMenu.classList.remove('show');

            // 정렬 로직 실행
            const sortValue = this.getAttribute('data-value');
            console.log('선택된 정렬:', sortValue);
        });
    });

    // 외부 클릭시 드롭다운 닫기
    document.addEventListener('click', function() {
        sortMenu.classList.remove('show');
    });

    // 카테고리 탭 클릭 이벤트
    const categoryTabs = document.querySelectorAll('.category-tab');
    categoryTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            categoryTabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            console.log('선택된 카테고리:', this.textContent);
        });
    });

    // 개별 필터 버튼 클릭 이벤트
    const filterBtns = document.querySelectorAll('.filter-btn');
    filterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // 필터 버튼이 아닌 경우에만 개별 필터 로직 실행
            if (this.textContent.trim() !== '필터') {
                console.log('필터 클릭:', this.textContent);
                // 개별 필터 드롭다운 구현 가능
            }
        });
    });

    // 이벤트 태그 클릭 이벤트
    const eventTags = document.querySelectorAll('.event-tag');
    eventTags.forEach(tag => {
        tag.addEventListener('click', function() {
            this.classList.toggle('active');
            console.log('이벤트 태그 선택:', this.textContent);
        });
    });
</script>
</body>
</html>