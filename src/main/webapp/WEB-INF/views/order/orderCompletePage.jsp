<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <title>주문 완료</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/order-complete.css'/>">
</head>
<body>
<fmt:setLocale value="ko_KR"/>
<fmt:setTimeZone value="Asia/Seoul"/>
<%@ include file="../main/header.jsp" %>

<div class="mu-container">
    <div class="mu-page-head">
        <h1 class="mu-title">주문 완료</h1>
        <p class="mu-sub">주문이 정상 접수되었어요. 아래에서 상세 정보를 확인하세요.</p>
    </div>

    <c:choose>
        <c:when test="${not empty orderData}">
            <div class="mu-grid">
                <div class="mu-col">
                    <!-- 주문 요약 -->
                    <section class="mu-card mu-order-head">
                        <div class="mu-order-head__left">
                            <c:if test="${not empty orderData.orderDateTime}">
                                <fmt:parseDate value="${orderData.orderDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss"
                                               var="od"/>
                                <div class="mu-order-date">
                                    <fmt:formatDate value="${od}" pattern="yy.MM.dd(E)"/>
                                </div>
                            </c:if>
                            <div class="mu-order-code">주문코드 ${orderData.orderCode}</div>
                        </div>
                        <div class="mu-order-head__right">
                            <a type="button" class="mu-btn mu-btn--line" href="<c:url value='/orders/order-list'/>">주문내역
                                보기</a>
                            <!-- ↓↓↓ 동일 스타일(라인 버튼)로 변경 -->
                            <a class="mu-btn mu-btn--line mu-btn--dark" href="<c:url value='/main'/>">쇼핑 계속하기</a>
                        </div>
                    </section>

                    <!-- 구매자 -->
                    <section class="mu-card">
                        <div class="mu-card__title">구매자 정보</div>
                        <dl class="mu-kv">
                            <dt>이름</dt>
                            <dd>${orderData.buyerDto.name}</dd>
                            <dt>이메일</dt>
                            <dd>${orderData.buyerDto.email}</dd>
                            <dt>휴대폰</dt>
                            <dd>${orderData.buyerDto.mobile}</dd>
                        </dl>
                    </section>

                    <!-- 배송지 -->
                    <section class="mu-card">
                        <div class="mu-card__title">배송지 정보</div>
                        <dl class="mu-kv">
                            <dt>수령인</dt>
                            <dd>${orderData.shippingAddressDto.recipientName}</dd>
                            <dt>연락처</dt>
                            <dd>${orderData.shippingAddressDto.phone}</dd>
                            <dt>주소</dt>
                            <dd>${orderData.shippingAddressDto.address}</dd>
                            <dt>우편번호</dt>
                            <dd><c:out
                                    value="${orderData.shippingAddressDto.postCode != null ? orderData.shippingAddressDto.postCode : '-'}"/></dd>
                        </dl>
                    </section>

                    <!-- 결제/금액  -->
                    <section class="mu-card mu-card--tight">
                        <div class="mu-card__title">결제/금액 정보</div>
                        <dl class="mu-kv">
                            <dt>총 상품 금액</dt>
                            <dd><fmt:formatNumber
                                    value="${orderData.priceInfoDto.totalPrice + orderData.priceInfoDto.orderDiscountAmount}"
                                    type="number"/>원
                            </dd>
                            <dt>할인 금액</dt>
                            <dd><fmt:formatNumber value="${orderData.priceInfoDto.orderDiscountAmount}"
                                                  type="number"/>원
                            </dd>
                            <dt>배송비</dt>
                            <dd><fmt:formatNumber
                                    value="${orderData.priceInfoDto.shippingFee != null ? orderData.priceInfoDto.shippingFee : 0}"
                                    type="number"/>원
                            </dd>
                        </dl>
                        <div class="mu-divider"></div>
                        <div class="mu-total">
                            <span>최종 결제금액</span>
                            <strong><fmt:formatNumber value="${orderData.priceInfoDto.totalPrice}"
                                                      type="number"/>원</strong>
                        </div>
                    </section>

                    <!-- 상품 리스트 -->
                    <section class="mu-card">
                        <div class="mu-card__title">주문 상품</div>
                        <c:choose>
                            <c:when test="${not empty orderData.orderItemsDto}">
                                <ul class="mu-order-list">
                                    <c:forEach var="it" items="${orderData.orderItemsDto}">
                                        <li class="mu-order-item">
                                            <div class="mu-thumb">
                                                <c:choose>
                                                    <c:when test="${not empty it.imageUrl}">
                                                        <img src="${it.imageUrl}" alt="${it.productName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="<c:url value='/resources/img/placeholder.png'/>"
                                                             alt="no image">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="mu-item-main">
                                                <div class="mu-item-meta">
                                                    <c:if test="${not empty it.brandNameKr}">
                                                        <a class="mu-link-title--brand"
                                                           href="<c:url value='/brands/${it.brandId}'/>">
                                                                ${it.brandNameKr}
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${not empty it.discountRate}">
                                                        <span class="mu-badge mu-badge--line">-${it.discountRate}%</span>
                                                    </c:if>
                                                </div>
                                                <div class="mu-item-title">
                                                    <a class="mu-link-title"
                                                       href="<c:url value='/products/${it.productId}'/>">
                                                            ${it.productName}
                                                    </a></div>
                                                <div class="mu-qty"> ${it.quantity}개</div>
                                                <div class="mu-item-price">
                                                    <div class="mu-line-sum"><fmt:formatNumber
                                                            value="${it.totalPrice}"
                                                            type="number"/>원
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <div class="mu-empty">주문 상품이 없습니다.</div>
                            </c:otherwise>
                        </c:choose>
                    </section>

                    <!-- 도움말 -->
                    <section class="mu-help mu-card">
                        고객센터 <span class="mu-badge mu-badge--line">10:00~18:00</span>
                        <p class="mu-help__sub">결제/배송 문의는 주문번호와 함께 말씀해주세요.</p>
                    </section>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <section class="mu-card">
                <div class="mu-card__title">주문 정보를 불러올 수 없습니다</div>
                <p class="mu-help__sub">잠시 후 다시 시도하거나 고객센터로 문의해주세요.</p>
            </section>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
