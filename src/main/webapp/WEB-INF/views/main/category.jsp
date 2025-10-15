<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%-- 카테고리 관련 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/category.css">

<div class="category-dropdown-overlay" id="categoryDropdownOverlay">
    <div class="category-dropdown-menu">
        <div class="dropdown-header">
            <a href="#product" class="tab-link active" data-tab="product">카테고리</a>
            <a href="#brand" class="tab-link" data-tab="brand">브랜드</a>
        </div>

        <div class="dropdown-content-container">

            <%-- 카테고리 (상품) 탭 내용 --%>
            <div class="tab-content active" id="product">
                <div class="dropdown-content">
                    <div class="category-list">
                        <ul>
                            <%-- data-target과 data-category-id 속성 추가 --%>
                            <li><a href="#" class="list-item-link" data-target="tops" data-category-id="1">상의</a></li>
                            <li><a href="#" class="list-item-link" data-target="outers" data-category-id="2">아우터</a>
                            </li>
                            <li><a href="#" class="list-item-link" data-target="pants" data-category-id="3">바지</a></li>
                            <li><a href="#" class="list-item-link" data-target="dresses"
                                   data-category-id="4">원피스/스커트</a></li>
                            <li><a href="#" class="list-item-link" data-target="bags" data-category-id="5">가방</a></li>
                            <li><a href="#" class="list-item-link" data-target="accessories"
                                   data-category-id="6">패션소품</a></li>
                            <li><a href="#" class="list-item-link" data-target="underwear"
                                   data-category-id="7">속옷/홈웨어</a></li>
                        </ul>
                    </div>
                    <div class="category-details">
                        <%-- 각 섹션에 id 추가 - 동적으로 채워질 예정 --%>
                        <div id="tops" class="detail-section">
                            <div class="detail-header">
                                <h4>상의</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="outers" class="detail-section">
                            <div class="detail-header">
                                <h4>아우터</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="pants" class="detail-section">
                            <div class="detail-header">
                                <h4>바지</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="dresses" class="detail-section">
                            <div class="detail-header">
                                <h4>원피스/스커트</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="bags" class="detail-section">
                            <div class="detail-header">
                                <h4>가방</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="accessories" class="detail-section">
                            <div class="detail-header">
                                <h4>패션소품</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                        <div id="underwear" class="detail-section">
                            <div class="detail-header">
                                <h4>속옷/홈웨어</h4>
                                <a href="#" class="view-all">전체 보기</a>
                            </div>
                            <div class="detail-items">
                                <%-- JavaScript에서 동적으로 채워짐 --%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%-- 브랜드 탭 내용 (무신사 스타일 초성 토글 적용) --%>
            <div class="tab-content" id="brand">
                <div class="dropdown-content brand-layout-content">

                    <%-- 왼쪽 브랜드 카테고리 목록 --%>
                    <div class="category-list">
                        <ul>
                            <%-- data-id 속성 추가 --%>
                            <li><a href="#" class="list-item-link" data-id="all">전체</a></li>
                            <li><a href="#" class="list-item-link" data-id="1">의류</a></li>
                            <li><a href="#" class="list-item-link" data-id="2">뷰티</a></li>
                            <li><a href="#" class="list-item-link" data-id="3">신발</a></li>
                            <li><a href="#" class="list-item-link" data-id="4">스니커즈</a></li>
                            <li><a href="#" class="list-item-link" data-id="5">가방</a></li>
                            <li><a href="#" class="list-item-link" data-id="6">패션소품</a></li>
                            <li><a href="#" class="list-item-link" data-id="7">속옷/홈웨어</a></li>
                            <li><a href="#" class="list-item-link" data-id="8">스포츠/레저</a></li>
                            <li><a href="#" class="list-item-link" data-id="9">디지털/라이프</a></li>
                            <li><a href="#" class="list-item-link" data-id="10">키즈</a></li>
                        </ul>
                    </div>

                    <%-- 오른쪽 브랜드 상세 내용 --%>
                    <div class="brand-details-container">

                        <%-- ✨ 초성 목록 및 버튼 영역 (수정 반영) ✨ --%>
                        <div class="brand-initial-list-wrapper">

                            <div class="initial-list-scroll-container">
                                <%-- 가나다순 목록 (초기 비활성: ABC가 먼저 보이므로) --%>
                                <div class="initial-list" id="koreanInitialList">
                                    <a href="#" class="active">인기</a>
                                    <a href="#">ㄱ</a>
                                    <a href="#">ㄴ</a>
                                    <a href="#">ㄷ</a>
                                    <a href="#">ㄹ</a>
                                    <a href="#">ㅁ</a>
                                    <a href="#">ㅂ</a>
                                    <a href="#">ㅅ</a>
                                    <a href="#">ㅇ</a>
                                    <a href="#">ㅈ</a>
                                    <a href="#">ㅊ</a>
                                    <a href="#">ㅋ</a>
                                    <a href="#">ㅌ</a>
                                    <a href="#">ㅍ</a>
                                    <a href="#">ㅎ</a>
                                    <a href="#">#</a>
                                </div>

                                <%-- ABC순 목록 (초기 활성: 무신사 기본) --%>
                                <div class="initial-list active" id="englishInitialList">
                                    <a href="#" class="active">인기</a>
                                    <a href="#">A</a>
                                    <a href="#">B</a>
                                    <a href="#">C</a>
                                    <a href="#">D</a>
                                    <a href="#">E</a>
                                    <a href="#">F</a>
                                    <a href="#">G</a>
                                    <a href="#">H</a>
                                    <a href="#">I</a>
                                    <a href="#">J</a>
                                    <a href="#">K</a>
                                    <a href="#">L</a>
                                    <a href="#">M</a>
                                    <a href="#">N</a>
                                    <a href="#">O</a>
                                    <a href="#">P</a>
                                    <a href="#">Q</a>
                                    <a href="#">R</a>
                                    <a href="#">S</a>
                                    <a href="#">T</a>
                                    <a href="#">U</a>
                                    <a href="#">V</a>
                                    <a href="#">W</a>
                                    <a href="#">X</a>
                                    <a href="#">Y</a>
                                    <a href="#">Z</a>
                                </div>
                            </div>

                            <%-- 버튼 영역 (하나만 보이도록 겹쳐 놓음) --%>
                            <%-- 초기에는 ABC 목록이 보이므로, '가나다순' 버튼이 표시되어야 합니다 (active) --%>
                            <a href="#" class="sort-toggle-btn active" id="toggleToKorean" data-target="korean">
                                <span class="btn-icon-text">가나다순</span>
                                <i class="toggle-icon">⇌</i>
                            </a>
                            <%-- 'ABC순' 버튼은 숨겨져 있다가 '가나다순' 버튼 클릭 시 나타납니다 --%>
                            <a href="#" class="sort-toggle-btn" id="toggleToEnglish" data-target="english">
                                <span class="btn-icon-text">ABC순</span>
                                <i class="toggle-icon">⇌</i>
                            </a>
                        </div>

                        <%-- 브랜드 리스트 (스크롤 영역) --%>
                        <div class="brand-scroll-list">
                            <ul class="brand-item-list" id="brandItemList">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%-- 카테고리 관련 JS --%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/category.js"></script>