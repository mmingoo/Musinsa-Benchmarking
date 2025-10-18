<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>오류 발생</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        .error-icon {
            font-size: 64px;
            color: #dc3545;
            margin-bottom: 20px;
        }
        h1 {
            color: #dc3545;
            margin-bottom: 20px;
        }
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
            border-left: 4px solid #dc3545;
        }
        .error-description {
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 10px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #545b62;
        }
        .actions {
            margin-top: 30px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="error-icon">⚠️</div>
    <h1>오류가 발생했습니다</h1>

    <c:choose>
        <c:when test="${not empty error}">
            <div class="error-message">
                <strong>오류 내용:</strong><br>
                    ${error}
            </div>
        </c:when>
        <c:otherwise>
            <div class="error-message">
                알 수 없는 오류가 발생했습니다.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="error-description">
        요청을 처리하는 중에 문제가 발생했습니다.<br>
        잠시 후 다시 시도해 주시거나 관리자에게 문의해 주세요.
    </div>

    <div class="actions">
        <a href="/" class="btn">홈으로 돌아가기</a>
        <a href="javascript:history.back()" class="btn btn-secondary">이전 페이지로</a>
    </div>
</div>
</body>
</html>