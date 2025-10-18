<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MUSINSA - 로그인</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f5f5f5;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            background: white;
            padding: 60px 50px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            width: 100%;
            max-width: 400px;
            text-align: center;
            position: relative;
        }

        .close-btn {
            position: absolute;
            top: 20px;
            left: 20px;
            background: none;
            border: none;
            font-size: 16px;
            color: #999;
            cursor: pointer;
            padding: 8px;
            border-radius: 50%;
            transition: background-color 0.2s;
        }

        .close-btn:hover {
            background-color: #f8f9fa;
        }

        .logo-section {
            margin-bottom: 50px;
        }

        .logo {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            margin-bottom: 8px;
            letter-spacing: 2px;
        }

        .service-name {
            color: #333;
            font-size: 28px;
            font-weight: 300;
            margin-bottom: 15px;
        }

        .subtitle {
            color: #666;
            font-size: 14px;
            line-height: 1.4;
        }

        .login-form {
            margin-bottom: 30px;
        }

        .social-login {
            margin-bottom: 25px;
        }

        .social-btn {
            width: 100%;
            padding: 16px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            margin-bottom: 15px;
        }

        .naver-btn {
            background-color: #03C75A;
            color: white;
        }

        .naver-btn:hover {
            background-color: #02b351;
        }

        .naver-icon {
            width: 20px;
            height: 20px;
            background: white;
            border-radius: 2px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            color: #03C75A;
            font-size: 12px;
        }

        .guest-login {
            margin-bottom: 30px;
        }

        .guest-link {
            color: #666;
            text-decoration: none;
            font-size: 14px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 2px;
            transition: all 0.2s;
        }

        .guest-link:hover {
            color: #333;
            border-bottom-color: #333;
        }

        .divider {
            text-align: center;
            margin: 30px 0;
            color: #999;
            font-size: 14px;
        }

        .footer-links {
            margin-top: 40px;
            text-align: center;
        }

        .footer-links a {
            color: #999;
            text-decoration: none;
            font-size: 12px;
            margin: 0 8px;
        }

        .footer-links a:hover {
            color: #666;
            text-decoration: underline;
        }

        /* 반응형 */
        @media (max-width: 480px) {
            .login-container {
                margin: 20px;
                padding: 40px 30px;
            }

            .logo {
                font-size: 24px;
            }

            .service-name {
                font-size: 24px;
            }

            .subtitle {
                font-size: 13px;
            }
        }

        /* 애니메이션 */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .login-container {
            animation: fadeInUp 0.4s ease;
        }
    </style>
</head>
<body>
<div class="login-container">
    <button class="close-btn" onclick="history.back()">&times;</button>

    <div class="logo-section">
        <div class="logo">MUSINSA</div>
        <div class="subtitle">스타일을 만나다<br>패션 플랫폼 무신사</div>
    </div>

    <div class="login-form">
        <div class="social-login">
            <a href="/oauth2/authorization/naver" class="social-btn naver-btn">
                <div class="naver-icon">N</div>
                네이버로 시작하기
            </a>
        </div>
    </div>

    <div class="footer-links">
        <a href="#" onclick="alert('서비스 준비중입니다.')">이용약관</a>
        <a href="#" onclick="alert('서비스 준비중입니다.')">개인정보처리방침</a>
        <a href="#" onclick="alert('서비스 준비중입니다.')">고객센터</a>
    </div>
</div>

<script>
    // 로그인 버튼 클릭 이벤트
    document.addEventListener('DOMContentLoaded', function() {
        const naverBtn = document.querySelector('.naver-btn');

        // 네이버 로그인 버튼에 로딩 효과 추가
        naverBtn.addEventListener('click', function(e) {
            const originalText = this.innerHTML;
            this.innerHTML = '<div class="naver-icon">N</div>로그인 중...';
            this.style.pointerEvents = 'none';
            this.style.opacity = '0.8';

            // 만약 로그인이 실패하면 원래 상태로 복원 (실제로는 페이지 이동)
            setTimeout(() => {
                this.innerHTML = originalText;
                this.style.pointerEvents = 'auto';
                this.style.opacity = '1';
            }, 5000);
        });
    });

    // 키보드 네비게이션 지원
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            const focusedElement = document.activeElement;
            if (focusedElement.classList.contains('social-btn')) {
                focusedElement.click();
            }
        }
        // ESC 키로 뒤로가기
        if (e.key === 'Escape') {
            history.back();
        }
    });
</script>
</body>
</html>