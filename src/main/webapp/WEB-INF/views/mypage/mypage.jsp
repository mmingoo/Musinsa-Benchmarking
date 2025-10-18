<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지 - MUSINSA</title>
    <%@ include file="../main/header.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        body { font-family: 'Noto Sans KR', sans-serif; background: #f8f9fa; margin: 0; }
        .container { max-width: 1100px; margin: 0 auto; padding: 20px; }

        /* 프로필 카드 */
        .profile-card {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 20px;
            background: #fff;
            border-bottom: 1px solid #eee;
        }
        .profile-info { display: flex; align-items: center; gap: 15px; }
        .profile-img { width: 60px; height: 60px; border-radius: 50%; overflow: hidden; }
        .profile-img img { width: 100%; height: 100%; object-fit: cover; }
        .nickname { font-size: 18px; font-weight: bold; margin: 0; }
        .grade { font-size: 14px; color: #888; margin: 2px 0 0; }

        .summary-section {
            display: flex;
            justify-content: space-around;
            background: #fff;
            border-bottom: 1px solid #eee;
            padding: 15px 0;
        }

        .profile-text p { margin: 2px 0; font-size: 14px; color: #333; }
        .profile-text .name { font-weight: bold; font-size: 16px; }
        .profile-actions { text-align: right; font-size: 12px; }
        .profile-actions a { margin-left: 10px; color: #007bff; text-decoration: none; }

        /* 요약 박스 */
        .summary-box {
            display: flex; justify-content: space-around;
            background: #f9fafb; border-radius: 8px; padding: 15px; margin-top: 10px;
        }
        .summary-item { text-align: center; font-size: 13px; color: #555; }
        .summary-value { font-weight: bold; font-size: 15px; margin-top: 4px; color: #111; }

        /* 주문 현황 */
        .order-section {
            background: #fff; border-radius: 10px;
            padding: 20px; margin-bottom: 20px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
        }
        .order-title { font-size: 15px; font-weight: bold; margin-bottom: 15px; color: #333; }
        .order-status {
            display: grid; grid-template-columns: repeat(5, 1fr); gap: 10px;
        }
        .order-card {
            background: #f9fafb; border-radius: 6px; padding: 15px 0;
            text-align: center; font-size: 13px; cursor: pointer;
            transition: background .2s;
        }
        .order-card:hover { background: #f1f3f5; }
        .order-count { font-weight: bold; font-size: 16px; color: #111; margin-bottom: 5px; }

        /* 메뉴 리스트 */
        .menu-section {
            background: #fff; border-radius: 10px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            margin-bottom: 20px;
        }
        .menu-item {
            padding: 15px; border-bottom: 1px solid #eee;
            display: flex; justify-content: space-between; align-items: center;
            font-size: 14px; color: #333; cursor: pointer;
        }
        .menu-item:last-child { border-bottom: none; }
        .menu-item:hover { background: #f9fafb; }
        .menu-arrow { color: #999; }

        /* 로그아웃 */
        .logout-box {
            background: #fff; border-radius: 10px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            padding: 15px; text-align: center;
        }
        .logout-box a {
            color: #666; text-decoration: none; font-size: 14px; font-weight: bold;
        }
        .logout-box a:hover { color: #000; }
    </style>
</head>
<body>
<main class="container">

    <!-- 프로필 카드 -->
    <section class="profile-card">
        <div class="profile-info">
            <div class="profile-img">
                <c:choose>
                    <c:when test="${not empty profile.profileImage}">
                        <img id="profileImage" src="${profile.profileImage}" alt="프로필">
                    </c:when>
                    <c:otherwise>
                        <img id="profileImage" src="${pageContext.request.contextPath}/resources/img/default_profile.png" alt="기본 프로필">
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- 프로필 텍스트 -->
            <div class="profile-text">
                <p id="profileName" class="name">
                    <c:choose>
                        <c:when test="${not empty profile.nickname}">${profile.nickname} 님</c:when>
                        <c:otherwise>회원 님</c:otherwise>
                    </c:choose>
                </p>

            </div>
            <div class="profile-summary">
                <div class="summary-item">
                    <span class="label">적립금</span>
                    <span id="profileMileage" class="summary-value">
                        <fmt:formatNumber value="${userMileage}" pattern="#,##0"/> P
                    </span>
                </div>
            </div>

        </div>

        <div class="profile-actions">
            <a href="${pageContext.request.contextPath}/mypage/settings">프로필 수정</a>
            <a href="${pageContext.request.contextPath}/mypage/settings">⚙️</a>
        </div>

    </section>


    <!-- 주문 현황 -->
    <section class="order-section">
        <div class="order-title">주문/배송 현황</div>
        <div class="order-status">
            <div class="order-card"><div class="order-count">${orderCounts.ready}</div><div>입금대기</div></div>
            <div class="order-card"><div class="order-count">${orderCounts.paid}</div><div>결제완료</div></div>
            <div class="order-card"><div class="order-count">${orderCounts.shipping}</div><div>배송중</div></div>
            <div class="order-card"><div class="order-count">${orderCounts.delivered}</div><div>배송완료</div></div>
            <div class="order-card"><div class="order-count">${orderCounts.cancel}</div><div>취소/환불</div></div>
        </div>
    </section>

    <!-- 메뉴 리스트 -->
    <section class="menu-section">
        <div class="menu-item">주문 내역 <span class="menu-arrow">›</span></div>
        <div class="menu-item">취소/반품/교환 내역 <span class="menu-arrow">›</span></div>
        <div class="menu-item">최근 본 상품 <span class="menu-arrow">›</span></div>
        <div class="menu-item">내 쿠폰 <span class="menu-arrow">›</span></div>
        <div class="menu-item">적립금 내역 <span class="menu-arrow">›</span></div>


    </section>


    <!-- 고객센터 / 공지사항 -->
    <section class="menu-section">
        <div class="menu-item">고객센터 <span class="menu-arrow">›</span></div>
        <div class="menu-item">1:1 문의 내역 <span class="menu-arrow">›</span></div>
        <div class="menu-item">상품 문의 내역 <span class="menu-arrow">›</span></div>
        <div class="menu-item">공지사항 <span class="menu-arrow">›</span></div>
    </section>

    <!-- 로그아웃 -->
    <div class="logout-box">
        <a href="/logout">로그아웃</a>
    </div>

    <script>



        $(function(){

            // 사용자 정보 불러오기
            $.getJSON("/api/mypage/profile", function(user){
                const name = (user.name && user.name.trim().length > 0) ? user.name : user.username;
                if(user.nickname && user.nickname.trim().length > 0){
                    $("#profileName").text(user.nickname + " 님");
                } else {
                    $("#profileName").text("회원 님");
                }

                const mileage = typeof user.userMileage === "number" ? user.userMileage : 0;
                $("#profileMileage").text(mileage.toLocaleString() + " P");

                if(user.profileImage) {
                    $("#profileImage").attr("src", user.profileImage);
                }
            });


            // 주문 현황 불러오기
            $.getJSON("/mypage/order-status", function(status){
                $("#ready").text(status.ready);
                $("#paid").text(status.paid);
                $("#shipping").text(status.shipping);
                $("#delivered").text(status.delivered);
                $("#cancel").text(status.cancel);
            });
        });
    </script>

</main>
</body>
</html>
