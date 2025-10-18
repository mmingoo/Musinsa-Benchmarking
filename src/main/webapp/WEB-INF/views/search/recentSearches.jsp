<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="isFragment" value="${(param.fragment eq 'true') or (forceFragment eq true)}" />

<c:if test="${not isFragment}">
    <!DOCTYPE html>
    <html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>MUSINSA - 검색</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
    </head>
    <body>

    <%@ include file="../main/header.jsp" %>
</c:if>

<div id="searchOverlay" class="search-overlay" role="dialog" aria-modal="true" aria-labelledby="recentSearchTitle">
    <div class="search-overlay-inner">
        <div class="overlay-header">
            <!-- ✅ form 추가 -->
            <form action="${pageContext.request.contextPath}/search" method="get" style="flex:1; display:flex; gap:8px;">
                <div class="search-input-container">
                    <input type="text" name="keyword" class="overlay-search-box" id="overlaySearchInput"
                           placeholder="브랜드, 상품명 등을 입력하세요" aria-label="검색어 입력" required>
                    <button class="search-submit-btn" type="submit" aria-label="검색 실행">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                            <path d="M21 21L16.514 16.506L21 21ZM19 10.5C19 15.194 15.194 19 10.5 19C5.806 19 2 15.194 2 10.5C2 5.806 5.806 2 10.5 2C15.194 2 19 5.806 19 10.5Z"
                                  stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
            </form>
            <!-- 닫기 버튼은 그대로 유지 -->
            <button class="close-overlay-btn" id="closeOverlayBtn" type="button" aria-label="검색창 닫기">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        </div>

        <div class="overlay-content" id="recentOverlayContent">
            <section class="search-section" id="recentSearchSection" aria-labelledby="recentSearchTitle">
                <div class="search-section-header">
                    <h3 id="recentSearchTitle">최근 검색어</h3>
                    <button class="clear-all-btn" id="deleteAllRecentBtn" type="button">모두삭제</button>
                </div>
                <ul class="tag-list" id="recentSearchTags">
                    <div class="loading-text">로딩 중...</div>
                </ul>
            </section>
<!--<section class="search-section" id="recentBrandSection" aria-labelledby="recentBrandTitle">
                <div class="search-section-header">
                    <h3 id="recentBrandTitle">최근 방문한 브랜드</h3>
                    <button class="clear-all-btn" id="deleteAllRecentBrandsBtn" type="button">모두삭제</button>
                </div>
                <ul class="brand-list" id="recentBrandTags">
                    <div class="loading-text">로딩 중...</div>
                </ul>
            </section> -->


            <section class="search-section" id="popularKeywordSection" aria-labelledby="popularKeywordTitle">
                <div class="search-section-header">
                    <h3 id="popularKeywordTitle">인기 검색어</h3>
                    <span class="timestamp" id="popularTimestamp">로딩 중...</span>
                </div>
                <div class="ranked-lists-container">
                    <ol class="ranked-list" id="popularRankList1"></ol>
                    <ol class="ranked-list" start="6" id="popularRankList2"></ol>
                </div>
            </section>
        </div>

        <div class="search-results is-hidden" id="searchResultsContent" aria-live="polite">
            <div class="search-results-header">
                <button class="back-button" id="searchResultsBackBtn" type="button">최근 검색 보기</button>
                <h3 class="search-results-title" id="searchResultsTitle"></h3>
            </div>
            <div class="search-results-body" id="searchResultsBody">
                <div class="placeholder-text">검색어를 입력하면 결과가 표시됩니다.</div>
            </div>
        </div>
    </div>
</div>

<c:if test="${not isFragment}">
    </body>
    </html>
</c:if>