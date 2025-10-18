<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>좋아요 화면 - 상품 | MUSINSA</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif;
            background-color: #f8f8f8;
            color: #333;
        }

        /* 헤더 스타일 */
        .header {
            background-color: #333;
            color: white;
            padding: 12px 0;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
        }

        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            align-items: center;
            padding: 0 20px;
        }

        .menu-icon {
            margin-right: 20px;
            cursor: pointer;
        }

        .nav-menu {
            display: flex;
            gap: 30px;
            flex: 1;
        }

        .nav-item {
            color: white;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
        }

        .nav-item:hover {
            color: #ddd;
        }

        .nav-item.snap {
            color: #ff6b35;
        }

        .header-right {
            display: flex;
            align-items: center;
            gap: 15px;
            font-size: 12px;
        }

        .header-right a {
            color: white;
            text-decoration: none;
        }

        /* 메인 컨텐츠 */
        .main-container {
            max-width: 1200px;
            margin: 80px auto 0;
            padding: 30px 20px;
            background-color: white;
            min-height: calc(100vh - 80px);
        }

        .page-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #333;
        }

        /* 탭 메뉴 */
        .tab-menu {
            display: flex;
            border-bottom: 2px solid #f0f0f0;
            margin-bottom: 30px;
        }

        .tab-item {
            padding: 15px 0;
            margin-right: 30px;
            color: #666;
            text-decoration: none;
            font-size: 16px;
            border-bottom: 2px solid transparent;
            transition: all 0.3s;
        }

        .tab-item.active {
            color: #ff6b35;
            border-bottom-color: #ff6b35;
            font-weight: bold;
        }

        /* 필터 섹션 */
        .filter-section {
            margin-bottom: 30px;
        }

        .filter-row {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .filter-label {
            font-weight: bold;
            margin-right: 20px;
            min-width: 80px;
        }

        .filter-input {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-right: 10px;
        }

        /* 카테고리 필터 */
        .category-filters {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        .category-item {
            padding: 8px 15px;
            background-color: #f5f5f5;
            border: 1px solid #ddd;
            border-radius: 20px;
            text-decoration: none;
            color: #666;
            font-size: 14px;
            transition: all 0.3s;
        }

        .category-item:hover,
        .category-item.active {
            background-color: #333;
            color: white;
            border-color: #333;
        }

        /* 정렬 옵션 */
        .sort-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }

        .sort-left {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .sort-right {
            position: relative;
        }

        .sort-dropdown {
            padding: 8px 30px 8px 12px;
            border: 1px solid #ddd;
            background-color: white;
            cursor: pointer;
            font-size: 14px;
        }

        /* 상품 그리드 */
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }

        .product-card {
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
            transition: transform 0.3s, box-shadow 0.3s;
            position: relative;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .product-image {
            position: relative;
            aspect-ratio: 3/4;
            overflow: hidden;
            background-color: #f8f8f8;
        }

        .product-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s;
        }

        .product-card:hover .product-image img {
            transform: scale(1.05);
        }

        .like-btn {
            position: absolute;
            top: 15px;
            right: 15px;
            width: 35px;
            height: 35px;
            background-color: rgba(255,255,255,0.9);
            border: none;
            border-radius: 50%;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            color: #ff4757;
            transition: all 0.3s;
        }

        .like-btn:hover {
            background-color: white;
            transform: scale(1.1);
        }

        .badge {
            position: absolute;
            top: 15px;
            left: 15px;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 11px;
            font-weight: bold;
            color: white;
        }

        .badge.fw {
            background-color: #ff6b35;
        }

        .badge.new {
            background-color: #2ed573;
        }

        .product-info {
            padding: 20px;
        }

        .brand-name {
            font-size: 14px;
            color: #999;
            margin-bottom: 8px;
        }

        .product-title {
            font-size: 16px;
            font-weight: 500;
            color: #333;
            margin-bottom: 12px;
            line-height: 1.4;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .price-info {
            margin-bottom: 10px;
        }

        .original-price {
            color: #999;
            text-decoration: line-through;
            font-size: 14px;
            margin-bottom: 4px;
        }

        .current-price {
            font-size: 18px;
            font-weight: bold;
            color: #333;
        }

        .discount-price {
            color: #ff4757;
            font-size: 14px;
            font-weight: bold;
        }

        .rating {
            display: flex;
            align-items: center;
            gap: 5px;
            font-size: 13px;
            color: #666;
        }

        .star {
            color: #ff6b35;
        }

        .rating-count {
            color: #999;
        }

        /* 정렬 드롭다운 */
        .sort-dropdown-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            min-width: 150px;
            z-index: 100;
            display: none;
        }

        .sort-dropdown-menu.active {
            display: block;
        }

        .sort-option {
            padding: 10px 15px;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 14px;
        }

        .sort-option:hover {
            background-color: #f5f5f5;
        }

        .sort-option.selected {
            background-color: #ff6b35;
            color: white;
        }

        /* 로딩 및 빈 상태 */
        .empty-state {
            text-align: center;
            padding: 80px 20px;
            color: #666;
        }

        .empty-icon {
            font-size: 48px;
            margin-bottom: 20px;
            color: #ddd;
        }

        .empty-message {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .empty-submessage {
            font-size: 14px;
            color: #999;
        }

        /* 반응형 */
        @media (max-width: 768px) {
            .header-content {
                padding: 0 15px;
            }

            .nav-menu {
                display: none;
            }

            .main-container {
                padding: 20px 15px;
                margin-top: 60px;
            }

            .product-grid {
                grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
                gap: 15px;
            }

            .category-filters {
                gap: 10px;
            }

            .sort-section {
                flex-direction: column;
                gap: 15px;
                align-items: flex-start;
            }

            .tab-menu {
                flex-wrap: wrap;
                gap: 10px;
            }

            .tab-item {
                margin-right: 15px;
            }
        }
    </style>
</head>
<body>
<!-- 헤더 -->
<header class="header">
    <div class="header-content">
        <div class="menu-icon">☰</div>
        <nav class="nav-menu">
            <a href="#" class="nav-item">MUSINSA</a>
            <a href="#" class="nav-item">BEAUTY</a>
            <a href="#" class="nav-item">PLAYER</a>
            <a href="#" class="nav-item">OUTLET</a>
            <a href="#" class="nav-item">BOUTIQUE</a>
            <a href="#" class="nav-item">SHOES</a>
            <a href="#" class="nav-item">KIDS</a>
            <a href="#" class="nav-item">USED</a>
            <a href="#" class="nav-item snap">SNAP</a>
        </nav>
        <div class="header-right">
            <a href="#">오프라인 스토어</a>
            <span>|</span>
            <a href="#">검색</a>
            <span>|</span>
            <a href="#">통합검색</a>
            <span>|</span>
            <a href="#">마이</a>
            <span>|</span>
            <a href="#">찜하기</a>
            <span>|</span>
            <a href="#">로그인/회원가입</a>
        </div>
    </div>
</header>

<!-- 메인 컨텐츠 -->
<main class="main-container">
    <h1 class="page-title">좋아요</h1>

    <!-- 탭 메뉴 -->
    <nav class="tab-menu">
        <a href="#" class="tab-item active">상품</a>
        <a href="#" class="tab-item">브랜드</a>
    </nav>

    <!-- 필터 섹션 -->
    <div class="filter-section">
        <div class="filter-row">
            <span class="filter-label">상품 7</span>
            <span class="filter-label">브랜드 1</span>
            <input type="text" class="filter-input" placeholder="검색어 입력">
        </div>

        <!-- 카테고리 필터 -->
        <div class="category-filters">
            <a href="#" class="category-item active">전체</a>
            <a href="#" class="category-item">신발</a>
            <a href="#" class="category-item">상의</a>
            <a href="#" class="category-item">아우터</a>
            <a href="#" class="category-item">바지</a>
            <a href="#" class="category-item">가방</a>
            <a href="#" class="category-item">패션소품</a>
            <a href="#" class="category-item">스포츠/레저</a>
        </div>
    </div>

    <!-- 정렬 섹션 -->
    <div class="sort-section">
        <div class="sort-left">
            <span>총 7개</span>
        </div>
        <div class="sort-right">
            <select class="sort-dropdown" id="sortDropdown">
                <option value="latest">담은순 ▼</option>
                <option value="price-low">가격낮은순</option>
                <option value="price-high">가격높은순</option>
                <option value="popular">인기순</option>
                <option value="review">후기많은순</option>
            </select>
        </div>
    </div>

    <!-- 상품 그리드 -->
    <div class="product-grid">
        <!-- 상품 1 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ik0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="타미스더브 남성 원피스 패딩 하프 라운지 수영복">
                <button class="like-btn">♥</button>
            </div>
            <div class="product-info">
                <div class="brand-name">타미스더브</div>
                <h3 class="product-title">남스 원피스 라이더 래핑 패딩 헤미 라운지 수영복</h3>
                <div class="price-info">
                    <div class="current-price">48,000원</div>
                </div>
                <div class="rating">
                    <span class="star">♥</span>
                    <span>937</span>
                    <span class="star">★</span>
                    <span>4.5</span>
                    <span class="rating-count">(10)</span>
                </div>
            </div>
        </div>

        <!-- 상품 2 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ik0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="컬쳐컬잇츠 브라운 네클리스">
                <button class="like-btn">♥</button>
            </div>
            <div class="product-info">
                <div class="brand-name">컬쳐컬잇츠</div>
                <h3 class="product-title">이름<br>Anhoa-O653T-Brown</h3>
                <div class="price-info">
                    <div class="discount-price">38,500원 가격</div>
                    <div class="current-price">♥ 1.4만 ★ 5.0(9)</div>
                </div>
                <div class="rating">
                    <span class="star">☆</span>
                    <span>아모</span>
                    <span>수</span>
                </div>
            </div>
        </div>

        <!-- 상품 3 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ik0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="일광고 버키백">
                <button class="like-btn">♥</button>
            </div>
            <div class="product-info">
                <div class="brand-name">일광고</div>
                <h3 class="product-title">버키 나일론 패딩 백팩</h3>
                <div class="price-info">
                    <div class="current-price">74,000원</div>
                </div>
                <div class="rating">
                    <span class="star">♥</span>
                    <span>193</span>
                    <span class="star">★</span>
                    <span>5.0(1)</span>
                </div>
            </div>
        </div>

        <!-- 상품 4 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ik0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="글리버 3팩 드로워즈">
                <button class="like-btn">♥</button>
                <div class="badge fw">FW 신상</div>
            </div>
            <div class="product-info">
                <div class="brand-name">글리버</div>
                <h3 class="product-title">[2PACK] 에어링크 드로워즈 기능성 쿨 드로즈 실크 허닛 텍스</h3>
                <div class="price-info">
                    <div class="current-price">24,900원</div>
                </div>
                <div class="rating">
                    <span class="star">♥</span>
                    <span>552</span>
                    <span class="star">★</span>
                    <span>4.7(37)</span>
                </div>
            </div>
        </div>

        <!-- 상품 5 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ek0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="젠플레이드아웃도 후드티">
                <button class="like-btn">♥</button>
            </div>
            <div class="product-info">
                <div class="brand-name">젠플레이드아웃도</div>
                <h3 class="product-title">고양이 귀 겨드 지드 - 블랙</h3>
                <div class="price-info">
                    <div class="current-price">223,200원</div>
                </div>
                <div class="rating">
                    <span class="star">♥</span>
                    <span>168</span>
                    <span class="star">★</span>
                    <span>4.5(8)</span>
                </div>
            </div>
        </div>

        <!-- 상품 6 -->
        <div class="product-card">
            <div class="product-image">
                <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM3MyIgdmlld0JveD0iMCAwIDI4MCAzNzMiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzczIiBmaWxsPSIjRjhGOEY4Ii8+CjxwYXRoIGQ9Ik0xNDAgMTg2LjVMMTUwIDIwNkgxMzBMMTQwIDE4Ni41WiIgZmlsbD0iI0NDQyIvPgo8L3N2Zz4K" alt="아답 후드티">
                <button class="like-btn">♥</button>
                <div class="badge fw">FW 신상</div>
            </div>
            <div class="product-info">
                <div class="brand-name">아답</div>
                <h3 class="product-title">Studded Leaf Hooded Long Sleeve (Beige)</h3>
                <div class="price-info">
                    <div class="current-price">83,300원</div>
                </div>
                <div class="rating">
                    <span class="star">♥</span>
                    <span>558</span>
                    <span class="star">★</span>
                    <span>4.8(79)</span>
                </div>
            </div>
        </div>
    </div>

    <!-- 우측 정렬 옵션 (이미지에서 보이는 부분) -->
    <div style="position: fixed; top: 50%; right: 50px; transform: translateY(-50%); background: white; border: 1px solid #ddd; border-radius: 8px; padding: 20px; box-shadow: 0 5px 15px rgba(0,0,0,0.1); min-width: 150px;">
        <div style="font-weight: bold; margin-bottom: 15px; color: #ff6b35;">담은 순 클릭 시</div>
        <div style="color: #999; font-size: 14px; margin-bottom: 5px;">담은순 ▲</div>
        <div style="margin-bottom: 10px;">
            <div style="color: #333; margin-bottom: 5px;">담은순</div>
            <div style="color: #666; font-size: 14px; margin-bottom: 5px;">낮은가격순</div>
            <div style="color: #666; font-size: 14px; margin-bottom: 5px;">높은가격순</div>
            <div style="color: #666; font-size: 14px; margin-bottom: 5px;">할인율순</div>
            <div style="color: #666; font-size: 14px;">브랜드이름순</div>
        </div>
    </div>
</main>

<script>
    // 좋아요 버튼 클릭 이벤트
    document.querySelectorAll('.like-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            // 하트 애니메이션 효과
            this.style.transform = 'scale(1.3)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 200);

            // 실제로는 여기서 서버에 좋아요 상태 업데이트 요청
            console.log('좋아요 취소');
        });
    });

    // 카테고리 필터 클릭 이벤트
    document.querySelectorAll('.category-item').forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();

            // 활성 상태 변경
            document.querySelectorAll('.category-item').forEach(cat => {
                cat.classList.remove('active');
            });
            this.classList.add('active');

            // 실제로는 여기서 필터링 로직 수행
            console.log('카테고리 필터:', this.textContent);
        });
    });

    // 탭 메뉴 클릭 이벤트
    document.querySelectorAll('.tab-item').forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();

            // 활성 탭 변경
            document.querySelectorAll('.tab-item').forEach(t => {
                t.classList.remove('active');
            });
            this.classList.add('active');

            console.log('탭 변경:', this.textContent);
        });
    });

    // 정렬 옵션 변경 이벤트
    document.getElementById('sortDropdown').addEventListener('change', function() {
        console.log('정렬 방식 변경:', this.value);
        // 실제로는 여기서 상품 목록 재정렬
    });

    // 상품 카드 클릭 시 상세 페이지로 이동
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('click', function(e) {
            // 좋아요 버튼 클릭이 아닐 때만 상세 페이지로 이동
            if (!e.target.classList.contains('like-btn')) {
                console.log('상품 상세 페이지로 이동');
                // window.location.href = '/product/detail/' + productId;
            }
        });
    });

    // 검색 기능
    document.querySelector('.filter-input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            const searchTerm = this.value.trim();
            console.log('검색어:', searchTerm);
            // 실제로는 여기서 검색 필터링 수행
        }
    });

    // 반응형 메뉴 토글
    document.querySelector('.menu-icon').addEventListener('click', function() {
        const navMenu = document.querySelector('.nav-menu');
        navMenu.style.display = navMenu.style.display === 'none' ? 'flex' : 'none';
    });

    // 스크롤 이벤트로 헤더 고정
    let lastScrollTop = 0;
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        const header = document.querySelector('.header');

        if (scrollTop > lastScrollTop && scrollTop > 100) {
            // 아래로 스크롤
            header.style.transform = 'translateY(-100%)';
        } else {
            // 위로 스크롤
            header.style.transform = 'translateY(0)';
        }
        lastScrollTop = scrollTop;
    });

    // 상품 카드 호버 효과
    document.querySelectorAll('.product-card').forEach(card => {
        const img = card.querySelector('.product-image img');

        card.addEventListener('mouseenter', function() {
            this.style.boxShadow = '0 15px 35px rgba(0,0,0,0.15)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.boxShadow = 'none';
        });
    });

    // 로딩 상태 시뮬레이션
    function showLoading() {
        const grid = document.querySelector('.product-grid');
        grid.innerHTML = '<div style="text-align: center; padding: 50px; color: #999; grid-column: 1/-1;">상품을 불러오는 중...</div>';
    }

    // 빈 상태 표시
    function showEmptyState() {
        const grid = document.querySelector('.product-grid');
        grid.innerHTML = `
                <div class="empty-state" style="grid-column: 1/-1;">
                    <div class="empty-icon">♡</div>
                    <div class="empty-message">좋아요한 상품이 없습니다</div>
                    <div class="empty-submessage">마음에 드는 상품을 찜해보세요</div>
                </div>
            `;
    }
</script>
</body>
</html>