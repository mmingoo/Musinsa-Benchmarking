# 🛍️ 무신사 벤치마킹 서비스

<div align="center">
<img width="1972" height="1650" alt="image" src="https://github.com/user-attachments/assets/22c9c253-5bcc-4c7c-8390-4b6f54f8207e" />


![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-19c-F80000?style=flat-square&logo=oracle&logoColor=white)
![Python](https://img.shields.io/badge/Python-3.10-3776AB?style=flat-square&logo=python&logoColor=white)

**무신사의 핵심 기능을 구현한 이커머스 프로젝트**

[🔗 GitHub Repository](https://github.com/The-Avengers-kernel/musinsa-server-v1)

</div>
https://github.com/mmingoo/Musinsa-Benchmarking/blob/main/README.md#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%86%8C%EA%B0%9C
---

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [성능 개선](#-성능-개선)
- [프로젝트 통계](#-프로젝트-통계)
- [시작하기](#-시작하기)
- [팀원](#-팀원)

---

## 🎯 프로젝트 소개

무신사의 대표적인 기능들을 벤치마킹하여 구현한 토이 프로젝트입니다.

- **프로젝트 기간**: 2024.09.17 ~ 2024.10.10 (24일)
- **프로젝트 유형**: 팀 프로젝트
- **목적**: 대규모 이커머스 플랫폼의 핵심 기능 구현 및 성능 최적화
- **추가자료**: [어벤져스_1팀_토이프로젝트1_erd.png:화면 정의서:발표자료.zip](https://github.com/user-attachments/files/22289218/_1._.1_erd.png.zip)

---

## 🛠 기술 스택

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **ORM**: MyBatis
- **Database**: Oracle 19c

### Frontend
- **Template Engine**: JSP

### DevOps & Tools
- **Performance Testing**: K6
- **Data Crawling**: Python 3.10

---

## ✨ 주요 기능

### 🛒 상품 관리
- 상품 목록 조회 및 정렬
- 상품 상세 페이지
- 무한 스크롤 구현

### 🔍 검색 시스템
- 상품 검색 기능
- 최근 검색어 저장
- 인기 검색어 제공

### 🎯 추천 시스템
- AI 기반 아이템 추천

### 🛍️ 주문 시스템
- 장바구니 담기 및 관리
- 장바구니에서 구매하기
- 상품 상세 페이지에서 즉시 구매

### ❤️ 사용자 인터랙션
- 상품 좋아요 기능
- 베이지안 보정으로 소수 리뷰 평점 왜곡을 방지하고, 판매량(0.3), 리뷰 수(0.2), 좋아요(0.2), 보정 평점(0.4) 가중치 기반 추천 알고리즘 구현
---

## 🚀 성능 개선

### 1️⃣ 좋아요 기능 동시성 제어

**문제 상황**
- 동시 요청 환경에서 Lost Update 문제 발생
- 데이터 정합성 이슈

**해결 방안**
- 원자적 UPDATE 쿼리 적용

**개선 결과**
```
✅ 1000명 동시 좋아요 요청 시 정합성 100% 달성
✅ 비관적 락 대비 46.4% 성능 개선
   └─ 2,610ms → 1,398ms
```

---

### 2️⃣ 주문 기능 성능 최적화

**문제 상황**
- N+1 문제로 인한 과도한 DB 접근
- 느린 처리 속도

**해결 방안**
- 배치 연산 적용
- 쿼리 최적화

**개선 결과**
```
✅ DB 접근 84% 감소
   └─ 32회 → 5회
   
✅ 처리 시간 39.3% 단축
   └─ 122,861ms → 74,627ms
```

**K6 부하 테스트 결과** (1000 VUs)
| 지표 | 개선 전 | 개선 후 | 개선율 |
|------|---------|---------|--------|
| TPS | 28.27/s | 29.75/s | +5.2% |
| Latency | 35.71s | 33.18s | -7.1% |

---

## 📊 프로젝트 통계

```
📌 총 SQL 쿼리: 84개
📌 총 API 개수: 43개
📌 상품 데이터: 약 60,000개
```

### 데이터 수집
무신사 API를 Python으로 크롤링하여 INSERT 문을 생성하고, 약 6만 개의 실제 상품 데이터를 확보했습니다.

---

<div align="center">

</div>
