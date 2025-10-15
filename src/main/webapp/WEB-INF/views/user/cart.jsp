<%--cartTest.jsp--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <title>장바구니</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/cart.css'/>">
</head>
<body>
<%@ include file="../main/header.jsp" %>

<c:set var="cartCount" value="${fn:length(cartProducts)}"/>
<c:set var="sumProduct" value="0"/>

<c:forEach var="p" items="${cartProducts}">
    <c:set var="sumProduct" value="${sumProduct + (p.totalPrice != null ? p.totalPrice : 0)}"/>
</c:forEach>

<c:set var="disc" value="${sumProduct * 0.08}"/>
<c:set var="finalPay" value="${sumProduct - disc}"/>

<div class="container">
    <div class="page-title">
        장바구니
        <span class="summary-sub" id="cart-count">전체 <c:out value="${cartCount}"/></span>
    </div>

    <%--    <%@ include file="../fragments/cartTest/_toolbarTest.jspf" %>--%>
    <div class="bar">
        <div class="bar-left">
            <label class="checkbox"><input type="checkbox" id="select-all" disabled/></label>
            <div>전체 선택</div>
            <!-- DTO에 soldOut 없음 → 고정 문구는 숨김 -->
            <div class="muted" id="soldout-note" style="display:none;">품절된 상품이 있습니다.</div>
        </div>
        <div class="bar-right">
            <button class="btn-ghost small" id="btn-remove-selected" disabled>선택 삭제</button>
        </div>
    </div>

    <div class="grid">
        <div>
            <%--            <%@ include file="../fragments/cartTest/_brandListTest.jspf" %>--%>
            <c:choose>
            <c:when test="${empty cartProducts}">
                <div class="empty">장바구니가 비어 있어요.</div>
            </c:when>
            <c:otherwise>
            <div id="brand-lists" class="list">
                <c:set var="currentBrand" value="__INIT__"/>
                <c:forEach var="item" items="${cartProducts}" varStatus="st">
                <!-- 브랜드 헤더 -->
                <c:if test="${item.productBrand ne currentBrand}">
                <!-- 이전 그룹 닫기 -->
                <c:if test="${st.index ne 0}">
            </div> <!-- .items -->
            </c:if>

            <div class="brand-row">
                <label class="checkbox"><input type="checkbox" disabled/></label>
                <div class="brand-name"><c:out value="${item.productBrand}"/></div>
                <span class="chip badge-free">무료배송</span>
                <span class="chip badge-fast">빠른배송</span>
                <div style="margin-left:auto;">
                    <button class="btn-ghost small" disabled>브랜드삭제</button>
                </div>
            </div>
            <div class="items">
                <c:set var="currentBrand" value="${item.productBrand}"/>
                </c:if>

                <!-- 아이템 행 -->
                <div class="item" data-id="${item.productId}">
                    <div class="checkbox"><input type="checkbox" disabled/></div>
                    <div class="thumb">
                        <a href="${pageContext.request.contextPath}/products/${item.productId}">
                            <img src="<c:out value='${item.imageUrl}'/>" alt="상품 이미지"/>
                        </a>
                    </div>
                    <div>
                        <!--상품명 누르면 상품상세화면으로 이동-->
                        <div class="name">
                            <a href="${pageContext.request.contextPath}/products/${item.productId}">
                            <c:out value="${item.productName}"/>
                            </a>
                        </div>
                        <div class="meta">
                            <c:out value="${item.optionName}"/> / <c:out value="${item.quantity}"/>개
                        </div>
                        <div class="price">
                            <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0"/>원
                        </div>
                    </div>
                    <div class="x-btn" title="삭제">✕</div>
                    <div class="row-ctrls">
                        <button class="btn-ghost ctrl" disabled>옵션 변경</button>
                    </div>
                </div>

                <!-- 마지막이면 열린 .items 닫기 -->
                <c:if test="${st.last}">
            </div> <!-- .items -->
            </c:if>
                <%--            <%@ include file="../cartTest/_optionModalTest.jspf" %>--%>

            </c:forEach>
        </div>
        </c:otherwise>
        </c:choose>
    </div>
    <aside class="right">
        <div class="sticky">
            <%--            <%@ include file="../fragments/cartTest/_summaryTest.jspf" %>--%>

            <div class="panel">
                <h4>구매 금액</h4>
                <div class="kv">
                    <div class="k">상품 금액</div>
                    <div class="v" id="sum-product"><fmt:formatNumber value="${sumProduct}" pattern="#,##0"/>원</div>
                </div>
                <div class="kv">
                    <div class="k">할인 금액</div>
                    <div class="v" id="sum-discount" style="color:#1c7ed6;">
                        -<fmt:formatNumber value="${disc}" pattern="#,##0"/>원
                    </div>
                </div>
                <div class="kv">
                    <div class="k">배송비</div>
                    <div class="v">무료배송</div>
                </div>
                <div class="divider"></div>
                <div class="kv big">
                    <div class="k">총 구매 금액</div>
                    <div class="v">
                        <span id="sum-final"><fmt:formatNumber value="${finalPay}" pattern="#,##0"/>원</span>
                        <span class="emph" id="disc-rate"><c:if test="${sumProduct gt 0}"> 8%</c:if></span>
                    </div>
                </div>
                <div class="hint" id="benefit-hint">
                    적립혜택 예상 최대 <fmt:formatNumber value="${finalPay * 0.047}" pattern="#,##0"/>원
                </div>
            </div>

            <div class="panel" style="margin-top:12px;">
                <h4>결제 혜택</h4>
                <div class="kv help">
                    <div>즉시 할인</div>
                    <div>카드/간편결제 적용</div>
                </div>
                <div class="divider"></div>
                <div class="kv help">
                    <div>무신사 현대카드 혜택</div>
                    <div>
                        <button class="btn-ghost small" disabled>할인 받기</button>
                    </div>
                </div>
                <div class="coupon-panel">
                    <div style="display:flex;align-items:center;gap:10px;">
                        <div style="width:36px;height:36px;border-radius:8px;border:1px solid #e9ecef;display:flex;align-items:center;justify-content:center;">
                            ₩
                        </div>
                        <div style="flex:1;">
                            <div>첫 결제 시 <b>10% 추가 적립</b></div>
                            <div class="help">혜택은 결제 수단에 따라 상이할 수 있어요</div>
                        </div>
                        <button class="btn-ghost small" disabled>혜택확인</button>
                    </div>
                </div>
            </div>

            <div class="panel notice">
                <h4>유의사항</h4>
                <div class="help">쿠폰/적립 적용 조건은 주문서에서 최종 확정됩니다.</div>
            </div>

            <div class="banner">
                <div class="help">인기 아우터 3000원 장바구니 쿠폰 • 지금 구매 시 장바구니 즉시 적용!</div>
            </div>

            <div id="order-form">
                <button type="button" id="btn-checkout" class="paymentBtn" disabled>주문하기</button>
            </div>

        </div>
    </aside>
</div>
</div>

<%--<%@ include file="../fragments/cartTest/_optionModalTest.jspf" %>--%>


<div id="option-modal" class="modal" aria-hidden="true">
    <div class="modal-backdrop" data-close-modal></div>
    <div class="modal-sheet" role="dialog" aria-modal="true" aria-labelledby="option-modal-title">
        <div class="modal-header">
            <strong id="option-modal-title">옵션 변경</strong>
            <button class="modal-close" data-close-modal>✕</button>
        </div>
        <div class="modal-body">
            <div class="help" style="margin-bottom:8px;">
                상품: <b id="opt-name"></b> <span class="chip" id="opt-brand-chip"></span>
            </div>

            <!-- 옵션 그룹 리스트가 주입 될 컨테이너 -->
            <div id="opt-groups" style="display:grid;gap:12px;"></div>

            <!-- 수량 -->
            <div style="display:grid;grid-template-columns:120px 1fr;gap:10px;align-items:center;margin-top:8px;">
                <label>수량</label>
                <div style="display:flex;align-items:center;gap:8px;">
                    <button type="button" class="btn-ghost small" id="qty-dec">-</button>
                    <input id="opt-qty" type="number" min="1" value="1" style="width:120px"/>
                    <button type="button" class="btn-ghost small" id="qty-inc">+</button>
                </div>
            </div>
            <div class="divider" style="margin:14px 0;"></div>
            <div class="help" id="opt-price-preview">변경 후 금액 미리보기: -</div>
            <div class="help" id="opt-error" style="color:#e03131; display:none;"></div>
        </div>
        <div class="modal-footer">
            <button class="btn-ghost small" data-close-modal>취소</button>
            <button id="opt-save" class="btn-ghost small">적용</button>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
    window.__CTX__ = {
        base: '<c:out value="${pageContext.request.contextPath}"/>'  // ✅ 끝에 슬래시 없음
    };
</script>
<script defer src="<c:url value='/resources/js/cart.js'/>"></script>
</body>
</html>

