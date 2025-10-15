-- =====================================================
-- 브랜드 관련 테이블
-- =====================================================

-- 브랜드 기본 정보
CREATE TABLE brands
(
    brand_id              NUMBER(10)    NOT NULL,
    brand_name_kr         VARCHAR2(50)  NOT NULL,
    brand_name_eng        VARCHAR2(50)  NOT NULL,
    brand_image           VARCHAR2(200) NULL,
    brand_likes           NUMBER(5) DEFAULT 0,
    brand_info            VARCHAR2(200) NULL,
    brand_born            VARCHAR2(4)   NULL,
    brand_first_letter_kr VARCHAR2(1)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_brands PRIMARY KEY (brand_id)
);

-- 브랜드 카테고리
CREATE TABLE brand_categories
(
    brand_category_id NUMBER(10)   NOT NULL,
    category_name     VARCHAR2(50) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_brand_categories PRIMARY KEY (brand_category_id)
);

-- 브랜드-카테고리 연결 테이블
CREATE TABLE brand_has_categories
(
    brand_has_category_id NUMBER(10) NOT NULL,
    brand_category_id     NUMBER(10) NOT NULL,
    brand_id              NUMBER(10) NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_brand_has_categories PRIMARY KEY (brand_has_category_id),
    CONSTRAINT fk_brand_has_categories_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id) ON DELETE CASCADE,
    CONSTRAINT fk_brand_has_categories_category
        FOREIGN KEY (brand_category_id) REFERENCES brand_categories (brand_category_id) ON DELETE CASCADE
);

-- 국가 정보
CREATE TABLE national_infos
(
    national_info_id NUMBER(10)    NOT NULL,
    nation_image     VARCHAR2(500) NULL,
    national_name    VARCHAR2(50)  NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_national_infos PRIMARY KEY (national_info_id)
);

-- 광고
CREATE TABLE advertisements
(
    advertisement_id    NUMBER(10) NOT NULL,
    brand_id            NUMBER(10) NOT NULL,
    advertisement_price NUMBER(10) NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_advertisements PRIMARY KEY (advertisement_id),
    CONSTRAINT fk_advertisements_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id)
);

-- 이벤트 해시태그
CREATE TABLE event_hashtags
(
    event_hashtag_id  NUMBER(10)   NOT NULL,
    hashtag_name      VARCHAR2(50) NOT NULL,
    hashtag_image_url VARCHAR2(50) NOT NULL,
    event_start_date  TIMESTAMP    NULL,
    event_end_date    TIMESTAMP    NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_event_hashtags PRIMARY KEY (event_hashtag_id)
);

-- 택배 회사
CREATE TABLE courier_companies
(
    courier_company_id   NUMBER(10)   NOT NULL,
    brand_id             NUMBER(10)   NOT NULL,
    courier_company_name VARCHAR2(50) NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_courier_companies PRIMARY KEY (courier_company_id),
    CONSTRAINT fk_courier_companies_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id)
);

-- 공지사항
CREATE TABLE notices
(
    notice_id  NUMBER(10)    NOT NULL,
    title      VARCHAR2(200) NOT NULL,
    content    VARCHAR2(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_notices PRIMARY KEY (notice_id)
);

-- Q&A
CREATE TABLE qna
(
    qna_id     VARCHAR2(255) NOT NULL,
    question   VARCHAR2(200) NOT NULL,
    answer     VARCHAR2(200) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_qna PRIMARY KEY (qna_id)
);

-- =====================================================
-- 사용자 관련 테이블
-- =====================================================

-- 사용자 기본 정보
CREATE TABLE users
(
    user_id       NUMBER(10)   NOT NULL,
    user_name     VARCHAR2(20) NULL,
    nickname      VARCHAR2(20) NOT NULL,
    gender        VARCHAR2(10) NULL,
    birthday      DATE         NULL,
    social_id     VARCHAR2(20) NULL,
    social_type   VARCHAR2(2)  NOT NULL,
    active_status VARCHAR2(20) NULL,
    inactive_time TIMESTAMP    NULL,
    user_mileage  NUMBER(10)   NULL,
    is_member     VARCHAR2(1) DEFAULT 'Y',
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT chk_users_is_member CHECK (is_member IN ('Y', 'N'))
);

-- 사용자 배송지
CREATE TABLE user_addresses
(
    user_address_id NUMBER(10)    NOT NULL,
    user_id         NUMBER(10)    NOT NULL,
    address_name    VARCHAR2(50)  NULL,
    recipient_name  VARCHAR2(20)  NOT NULL,
    phone_number    VARCHAR2(50)  NOT NULL,
    is_default      NUMBER(1) DEFAULT 0,
    is_recent       NUMBER(1) DEFAULT 0,
    address_line1   VARCHAR2(200) NOT NULL,
    address_line2   VARCHAR2(200) NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_addresses PRIMARY KEY (user_address_id),
    CONSTRAINT fk_user_addresses_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT chk_user_addresses_is_default CHECK (is_default IN (0, 1)),
    CONSTRAINT chk_user_addresses_is_recent CHECK (is_recent IN (0, 1))
);

-- 마일리지 타입
CREATE TABLE user_mileage_types
(
    user_mileage_type_id NUMBER(10)   NOT NULL,
    mileage_type         VARCHAR2(50) NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_mileage_types PRIMARY KEY (user_mileage_type_id)
);

-- 은행 목록
CREATE TABLE bank_name_lists
(
    bank_name_list_id NUMBER(10)   NOT NULL,
    bank_name         VARCHAR2(50) NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_bank_name_lists PRIMARY KEY (bank_name_list_id)
);

-- 사용자 환불계좌
CREATE TABLE user_bank_refund_accounts
(
    user_bank_refund_account_id NUMBER(10)   NOT NULL,
    user_id                     NUMBER(10)   NOT NULL,
    bank_name_list_id           NUMBER(10)   NOT NULL,
    user_bank_refund_account    VARCHAR2(50) NOT NULL,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_bank_refund_accounts PRIMARY KEY (user_bank_refund_account_id),
    CONSTRAINT fk_user_bank_refund_accounts_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_bank_refund_accounts_bank
        FOREIGN KEY (bank_name_list_id) REFERENCES bank_name_lists (bank_name_list_id)
);

-- 사용자 등급
CREATE TABLE user_grades
(
    grade_id   NUMBER(10)   NOT NULL,
    user_id    NUMBER(10)   NOT NULL,
    grade_name VARCHAR2(20) NULL,
    grade_code VARCHAR2(10) NULL,
    min_amount NUMBER(10)   NULL,
    max_amount NUMBER(10)   NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_grades PRIMARY KEY (grade_id),
    CONSTRAINT fk_user_grades_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- 사용자 문의
CREATE TABLE user_inquiries
(
    user_inquiry_id       NUMBER(10)    NOT NULL,
    user_id               NUMBER(10)    NOT NULL,
    title                 VARCHAR2(200) NULL,
    content               CLOB          NULL,
    inquiry_status        VARCHAR2(30)  NULL,
    inquiry_number        VARCHAR2(30)  NULL,
    response_completed_at TIMESTAMP     NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_inquiries PRIMARY KEY (user_inquiry_id),
    CONSTRAINT fk_user_inquiries_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- 사용자 문의 이미지
CREATE TABLE user_inquiry_images
(
    user_inquiry_image_id NUMBER(10)    NOT NULL,
    user_inquiry_id       NUMBER(10)    NOT NULL,
    image_url             VARCHAR2(200) NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_inquiry_images PRIMARY KEY (user_inquiry_image_id),
    CONSTRAINT fk_user_inquiry_images_inquiry
        FOREIGN KEY (user_inquiry_id) REFERENCES user_inquiries (user_inquiry_id) ON DELETE CASCADE
);

-- 문의 유형
CREATE TABLE inquiry_types
(
    inquiry_type_id NUMBER(10)   NOT NULL,
    user_inquiry_id NUMBER(10)   NOT NULL,
    type_name       VARCHAR2(30) NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_inquiry_types PRIMARY KEY (inquiry_type_id),
    CONSTRAINT fk_inquiry_types_user_inquiry
        FOREIGN KEY (user_inquiry_id) REFERENCES user_inquiries (user_inquiry_id) ON DELETE CASCADE
);

-- 문의 세부 유형
CREATE TABLE inquiry_subtypes
(
    inquiry_subtype_id NUMBER(10)   NOT NULL,
    inquiry_type_id    NUMBER(10)   NOT NULL,
    subtype_name       VARCHAR2(30) NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_inquiry_subtypes PRIMARY KEY (inquiry_subtype_id),
    CONSTRAINT fk_inquiry_subtypes_type
        FOREIGN KEY (inquiry_type_id) REFERENCES inquiry_types (inquiry_type_id) ON DELETE CASCADE
);

-- 사용자 쿠폰
CREATE TABLE user_coupons
(
    user_coupon_id NUMBER(10) NOT NULL,
    user_id        NUMBER(10) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_coupons PRIMARY KEY (user_coupon_id),
    CONSTRAINT fk_user_coupons_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- 사용자 프로필 이미지
CREATE TABLE user_profile_images
(
    user_profile_image_id NUMBER(10)    NOT NULL,
    user_id               NUMBER(10)    NOT NULL,
    profile_image_url     VARCHAR2(500) NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_profile_images PRIMARY KEY (user_profile_image_id),
    CONSTRAINT fk_user_profile_images_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- 사용자 신체 정보
CREATE TABLE user_physical_infos
(
    user_physical_info_id NUMBER(10) NOT NULL,
    user_id               NUMBER(10) NOT NULL,
    height                NUMBER(3)  NULL,
    weight                NUMBER(3)  NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_physical_infos PRIMARY KEY (user_physical_info_id),
    CONSTRAINT fk_user_physical_infos_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- =====================================================
-- 상품 관련 테이블
-- =====================================================

-- 상품 카테고리
CREATE TABLE product_categories
(
    product_category_id        NUMBER(10)    NOT NULL,
    parent_product_category_id NUMBER(10)    NULL,
    category_name              VARCHAR2(200) NULL,
    category_image             VARCHAR2(200) NULL,
    created_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_categories PRIMARY KEY (product_category_id),
    CONSTRAINT fk_product_categories_parent
        FOREIGN KEY (parent_product_category_id) REFERENCES product_categories (product_category_id)
);

-- 옵션 타입
CREATE TABLE option_types
(
    option_type_id      NUMBER(10)    NOT NULL,
    option_type         VARCHAR2(200) NULL,
    option_type_display NUMBER(3)     NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_option_types PRIMARY KEY (option_type_id),
    CONSTRAINT chk_option_types_display CHECK (option_type_display > 0)
);

-- 상품 기본 정보
CREATE TABLE products
(
    product_id          NUMBER(10)   NOT NULL,
    brand_id            NUMBER(10)   NOT NULL,
    product_category_id NUMBER(10)   NOT NULL,
    product_name        VARCHAR2(50) NULL,
    price               NUMBER(10)   NULL,
    product_likes       NUMBER(5) DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_products PRIMARY KEY (product_id),
    CONSTRAINT fk_products_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id),
    CONSTRAINT fk_products_category
        FOREIGN KEY (product_category_id) REFERENCES product_categories (product_category_id),
    CONSTRAINT chk_products_price CHECK (price > 0)
);

-- 상품-이벤트 연결 테이블
CREATE TABLE product_has_events
(
    product_id       NUMBER(10) NOT NULL,
    event_hashtag_id NUMBER(10) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_has_events PRIMARY KEY (product_id, event_hashtag_id),
    CONSTRAINT fk_product_has_events_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_product_has_events_event
        FOREIGN KEY (event_hashtag_id) REFERENCES event_hashtags (event_hashtag_id)
);

-- 상품 옵션 타입
CREATE TABLE product_option_types
(
    product_option_id NUMBER(10) NOT NULL,
    product_id        NUMBER(10) NOT NULL,
    option_type_id    NUMBER(10) NOT NULL,
    display_order     NUMBER(3)  NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_option_types PRIMARY KEY (product_option_id),
    CONSTRAINT fk_product_option_types_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_product_option_types_option
        FOREIGN KEY (option_type_id) REFERENCES option_types (option_type_id),
    CONSTRAINT chk_product_option_types_order CHECK (display_order > 0)
);

-- 상품 변형/SKU
CREATE TABLE product_variants
(
    product_variant_id NUMBER(10)    NOT NULL,
    product_id         NUMBER(10)    NOT NULL,
    sku_code           VARCHAR2(50)  NULL,
    variant_name       VARCHAR2(100) NULL,
    price              NUMBER(10)    NULL,
    stock_quantity     NUMBER(10)    NULL,
    size_value         VARCHAR2(20)  NULL,
    color_value        VARCHAR2(20)  NULL,
    material_value     VARCHAR2(50)  NULL,
    extra_price        NUMBER(10)    NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_variants PRIMARY KEY (product_variant_id),
    CONSTRAINT fk_product_variants_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 상품 이미지
CREATE TABLE product_images
(
    product_image_id NUMBER(10)    NOT NULL,
    product_id       NUMBER(10)    NOT NULL,
    image_type       VARCHAR2(200) NULL,
    image_url        VARCHAR2(200) NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_images PRIMARY KEY (product_image_id),
    CONSTRAINT fk_product_images_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT chk_product_images_type CHECK (image_type IN ('MAIN', 'SUB', 'DETAIL'))
);

-- 상품 연관 태그
CREATE TABLE product_connection_tags
(
    product_connection_tag_id NUMBER(10)    NOT NULL,
    product_id                NUMBER(10)    NOT NULL,
    tag_name                  VARCHAR2(200) NULL,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_connection_tags PRIMARY KEY (product_connection_tag_id),
    CONSTRAINT fk_product_connection_tags_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 상품 문의
CREATE TABLE product_inquiries
(
    product_inquiry_id NUMBER(10)    NOT NULL,
    product_id         NUMBER(10)    NOT NULL,
    user_id            NUMBER(10)    NOT NULL,
    inquiry_type       VARCHAR2(50)  NULL,
    title              VARCHAR2(200) NULL,
    answer_state       VARCHAR2(200) NULL,
    nickname           VARCHAR2(200) NULL,
    content            VARCHAR2(200) NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_inquiries PRIMARY KEY (product_inquiry_id),
    CONSTRAINT fk_product_inquiries_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_product_inquiries_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT chk_product_inquiries_type
        CHECK (inquiry_type IN ('상품문의', '배송문의', '교환/환불', '기타')),
    CONSTRAINT chk_product_inquiries_state
        CHECK (answer_state IN ('대기중', '답변완료', '처리중', '보류'))
);

-- 상품 문의 이미지
CREATE TABLE product_inquiry_images
(
    product_inquiry_image_id NUMBER(10)    NOT NULL,
    product_inquiry_id       NUMBER(10)    NOT NULL,
    image_url                VARCHAR2(200) NULL,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_inquiry_images PRIMARY KEY (product_inquiry_image_id),
    CONSTRAINT fk_product_inquiry_images_inquiry
        FOREIGN KEY (product_inquiry_id) REFERENCES product_inquiries (product_inquiry_id)
);

-- 상품 문의 답변
CREATE TABLE product_inquiry_answers
(
    product_inquiry_answer_id NUMBER(10)    NOT NULL,
    product_inquiry_id        NUMBER(10)    NOT NULL,
    manager_name              VARCHAR2(200) NULL,
    content                   CLOB          NULL,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_inquiry_answers PRIMARY KEY (product_inquiry_answer_id),
    CONSTRAINT fk_product_inquiry_answers_inquiry
        FOREIGN KEY (product_inquiry_id) REFERENCES product_inquiries (product_inquiry_id)
);

-- =====================================================
-- 리뷰 및 평가 관련 테이블
-- =====================================================

-- 리뷰
CREATE TABLE reviews
(
    review_id        NUMBER(10)    NOT NULL,
    product_id       NUMBER(10)    NOT NULL,
    user_id          NUMBER(10)    NOT NULL,
    nickname         VARCHAR2(20)  NULL,
    content          VARCHAR2(200) NULL,
    purchase_options VARCHAR2(50)  NULL,
    help_count       NUMBER(10)    NULL,
    rating           NUMBER(2)     NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_reviews PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- 리뷰 이미지
CREATE TABLE review_images
(
    review_image_id NUMBER(10)    NOT NULL,
    review_id       NUMBER(10)    NOT NULL,
    image_url       VARCHAR2(200) NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_review_images PRIMARY KEY (review_image_id),
    CONSTRAINT fk_review_images_review
        FOREIGN KEY (review_id) REFERENCES reviews (review_id)
);

-- 상품 리뷰 통계
CREATE TABLE product_review_stats
(
    product_review_stats_id NUMBER(10) NOT NULL,
    review_id               NUMBER(10) NOT NULL,
    review_count            NUMBER(10) NULL,
    total_reviews           NUMBER(10) NULL,
    rating_avg              NUMBER(10) NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_review_stats PRIMARY KEY (product_review_stats_id),
    CONSTRAINT fk_product_review_stats_review
        FOREIGN KEY (review_id) REFERENCES reviews (review_id),
    CONSTRAINT chk_product_review_stats_rating CHECK (rating_avg BETWEEN 0 AND 5)
);

-- 댓글
CREATE TABLE comments
(
    comment_id NUMBER(10)    NOT NULL,
    review_id  NUMBER(10)    NOT NULL,
    nickname   VARCHAR2(20)  NULL,
    content    VARCHAR2(200) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_comments PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_review
        FOREIGN KEY (review_id) REFERENCES reviews (review_id)
);

-- 사용자 상품 좋아요 (product_likes 테이블 제거하고 이것만 사용)
CREATE TABLE user_product_likes
(
    user_product_like_id NUMBER(10) NOT NULL,
    user_id              NUMBER(10) NOT NULL,
    product_id           NUMBER(10) NOT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_product_likes PRIMARY KEY (user_product_like_id),
    CONSTRAINT fk_user_product_likes_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_user_product_likes_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 사용자 브랜드 좋아요
CREATE TABLE user_brand_likes
(
    user_brand_like_id NUMBER(19) NOT NULL,
    brand_id           NUMBER(10) NOT NULL,
    user_id            NUMBER(10) NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_brand_likes PRIMARY KEY (user_brand_like_id),
    CONSTRAINT fk_user_brand_likes_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_user_brand_likes_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id)
);

-- 상품 조회 로그
CREATE TABLE product_view_logs
(
    product_view_log_id NUMBER(10)   NOT NULL,
    product_id          NUMBER(10)   NOT NULL,
    view_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    product_view_type   VARCHAR2(50) NULL,
    user_id             NUMBER(10)   NULL,

    CONSTRAINT pk_product_view_logs PRIMARY KEY (product_view_log_id),
    CONSTRAINT fk_product_view_logs_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 최근 상품 조회 이력
CREATE TABLE recent_product_view_histories
(
    recent_product_view_history_id NUMBER(10) NOT NULL,
    user_id                        NUMBER(10) NOT NULL,
    product_id                     NUMBER(10) NOT NULL,
    view_time                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_recent_product_view_histories PRIMARY KEY (recent_product_view_history_id),
    CONSTRAINT fk_recent_product_view_histories_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_recent_product_view_histories_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- 검색 키워드
CREATE TABLE search_keywords
(
    search_keyword_id         NUMBER(10)    NOT NULL,
    keyword                   VARCHAR2(50)  NOT NULL,
    keyword_initial           VARCHAR2(20)  NOT NULL,
    daily_count               NUMBER(10)    NOT NULL,
    current_hour_search_count NUMBER(10)    NOT NULL,
    growth_rate               NUMBER(10, 2) NULL,
    is_trending               VARCHAR2(1)   NULL,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_search_keywords PRIMARY KEY (search_keyword_id),
    CONSTRAINT chk_search_keywords_trending CHECK (is_trending IN ('Y', 'N'))
);

-- 검색 로그
CREATE TABLE search_logs
(
    search_log_id   NUMBER(10)   NOT NULL,
    search_text     VARCHAR2(50) NULL,
    search_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    search_count    NUMBER(10)   NULL,
    user_id         NUMBER(10)   NULL,

    CONSTRAINT pk_search_logs PRIMARY KEY (search_log_id),
    CONSTRAINT fk_search_logs_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- =====================================================
-- 배송 관련 테이블
-- =====================================================

-- 배송 상태
CREATE TABLE shipping_statuses
(
    shipping_status_id NUMBER(10)    NOT NULL,
    status             VARCHAR2(50)  NOT NULL,
    hub_name           VARCHAR2(100) NULL,
    hub_status         VARCHAR2(50)  NULL,
    arrival_time       TIMESTAMP     NULL,
    departure_time     TIMESTAMP     NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipping_statuses PRIMARY KEY (shipping_status_id)
);

-- 배송 요청 타입
CREATE TABLE shipping_request_types
(
    shipping_request_type_id NUMBER(10)   NOT NULL,
    shipping_request_type    VARCHAR2(50) NULL,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipping_request_types PRIMARY KEY (shipping_request_type_id)
);

-- 예정 배송 정보
CREATE TABLE scheduled_delivery_informations
(
    scheduled_delivery_information_id NUMBER(10)   NOT NULL,
    local                             VARCHAR2(20) NULL,
    expected_arrival_date             NUMBER(1)    NULL,
    created_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_scheduled_delivery_informations PRIMARY KEY (scheduled_delivery_information_id)
);

-- 배송
CREATE TABLE shipments
(
    shipping_id                       NUMBER(10)    NOT NULL,
    scheduled_delivery_information_id NUMBER(10)    NOT NULL,
    shipping_request_type_id          NUMBER(10)    NOT NULL,
    shipping_status_id                NUMBER(10)    NOT NULL,
    shipping_inquiry                  VARCHAR2(100) NULL,
    recipient_name                    VARCHAR2(100) NOT NULL,
    recipient_phone                   VARCHAR2(20)  NOT NULL,
    recipient_address                 VARCHAR2(300) NULL,
    shipping_direct_request           VARCHAR2(500) NULL,
    postal_code                       VARCHAR2(20)  NOT NULL,
    created_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipments PRIMARY KEY (shipping_id),
    CONSTRAINT fk_shipments_scheduled_delivery
        FOREIGN KEY (scheduled_delivery_information_id)
            REFERENCES scheduled_delivery_informations (scheduled_delivery_information_id),
    CONSTRAINT fk_shipments_request_type
        FOREIGN KEY (shipping_request_type_id)
            REFERENCES shipping_request_types (shipping_request_type_id),
    CONSTRAINT fk_shipments_status
        FOREIGN KEY (shipping_status_id)
            REFERENCES shipping_statuses (shipping_status_id)
);

-- 배송비
CREATE TABLE shipping_fees
(
    shipping_fee_id NUMBER(10) NOT NULL,
    fee_amount      NUMBER(10) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipping_fees PRIMARY KEY (shipping_fee_id)
);

-- 배송비 조건
CREATE TABLE shipping_fee_conditions
(
    shipping_fee_condition_id NUMBER(10)    NOT NULL,
    condition_name            VARCHAR2(100) NOT NULL,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipping_fee_conditions PRIMARY KEY (shipping_fee_condition_id)
);

-- 배송비 정책
CREATE TABLE shipping_fee_policies
(
    shipping_fee_policy_id NUMBER(10) NOT NULL,
    shipping_fee_id        NUMBER(10) NOT NULL,
    shipping_condition_id  NUMBER(10) NOT NULL,
    brand_id               NUMBER(10) NOT NULL,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_shipping_fee_policies PRIMARY KEY (shipping_fee_policy_id),
    CONSTRAINT fk_shipping_fee_policies_fee
        FOREIGN KEY (shipping_fee_id) REFERENCES shipping_fees (shipping_fee_id),
    CONSTRAINT fk_shipping_fee_policies_condition
        FOREIGN KEY (shipping_condition_id) REFERENCES shipping_fee_conditions (shipping_fee_condition_id),
    CONSTRAINT fk_shipping_fee_policies_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id)
);

-- =====================================================
-- 쿠폰 관련 테이블
-- =====================================================

-- 쿠폰
CREATE TABLE coupons
(
    coupon_id    NUMBER(10)   NOT NULL,
    user_id      NUMBER(10)   NOT NULL,
    brand_id     NUMBER(10)   NOT NULL,
    coupon_name  VARCHAR2(20) NOT NULL,
    coupon_code  VARCHAR2(30) NOT NULL,
    description  VARCHAR2(50) NULL,
    start_date   DATE         NOT NULL,
    end_date     DATE         NOT NULL,
    is_stackable CHAR(1)      NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_coupons PRIMARY KEY (coupon_id),
    CONSTRAINT fk_coupons_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_coupons_brand
        FOREIGN KEY (brand_id) REFERENCES brands (brand_id),
    CONSTRAINT chk_coupons_stackable CHECK (is_stackable IN ('Y', 'N'))
);

-- 쿠폰 조건
CREATE TABLE coupon_conditions
(
    coupon_condition_id NUMBER(10) NOT NULL,
    grade_id            NUMBER(10) NOT NULL,
    user_id             NUMBER(10) NOT NULL,
    coupon_id           NUMBER(10) NOT NULL,
    condition_type      NUMBER(10) NOT NULL,
    min_purchase_amount NUMBER(5)  NULL,
    min_quantity        NUMBER(5)  NULL,
    min_product_count   NUMBER(5)  NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_coupon_conditions PRIMARY KEY (coupon_condition_id),
    CONSTRAINT fk_coupon_conditions_grade
        FOREIGN KEY (grade_id) REFERENCES user_grades (grade_id),
    CONSTRAINT fk_coupon_conditions_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_coupon_conditions_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id)
);

-- 쿠폰 할인 규칙
CREATE TABLE coupon_discount_rules
(
    coupon_discount_rule_id NUMBER(10) NOT NULL,
    coupon_id               NUMBER(10) NOT NULL,
    user_id                 NUMBER(10) NOT NULL,
    discount_percent        NUMBER(5)  NULL,
    discount_amount         NUMBER(10) NULL,
    max_discount_amount     NUMBER(10) NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_coupon_discount_rules PRIMARY KEY (coupon_discount_rule_id),
    CONSTRAINT fk_coupon_discount_rules_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id),
    CONSTRAINT fk_coupon_discount_rules_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- 쿠폰 타입
CREATE TABLE coupon_types
(
    coupon_type_id NUMBER(10)   NOT NULL,
    coupon_id      NUMBER(10)   NOT NULL,
    user_id        NUMBER(10)   NOT NULL,
    type_code      VARCHAR2(20) NOT NULL,
    type_name      VARCHAR2(20) NOT NULL,
    apply_scope    VARCHAR2(20) NOT NULL,
    discount_type  VARCHAR2(10) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_coupon_types PRIMARY KEY (coupon_type_id),
    CONSTRAINT fk_coupon_types_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id),
    CONSTRAINT fk_coupon_types_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- =====================================================
-- 결제 관련 테이블
-- =====================================================

-- 결제 방법
CREATE TABLE payment_methods
(
    payment_method_id NUMBER(10)   NOT NULL,
    payment_name      VARCHAR2(50) NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payment_methods PRIMARY KEY (payment_method_id)
);

-- 결제 혜택
CREATE TABLE payment_benefits
(
    payment_benefit_id NUMBER(10)   NOT NULL,
    payment_method_id  NUMBER(10)   NOT NULL,
    benefit_type       VARCHAR2(10) NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payment_benefits PRIMARY KEY (payment_benefit_id),
    CONSTRAINT fk_payment_benefits_method
        FOREIGN KEY (payment_method_id) REFERENCES payment_methods (payment_method_id)
);

-- 결제 회사
CREATE TABLE payment_companies
(
    payment_company_id NUMBER(10)   NOT NULL,
    payment_benefit_id NUMBER(10)   NOT NULL,
    name               VARCHAR2(50) NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payment_companies PRIMARY KEY (payment_company_id),
    CONSTRAINT fk_payment_companies_benefit
        FOREIGN KEY (payment_benefit_id) REFERENCES payment_benefits (payment_benefit_id)
);

-- 결제 회사 할인 가격
CREATE TABLE payment_company_discount_prices
(
    payment_company_discount_price_id NUMBER(10) NOT NULL,
    payment_benefit_id                NUMBER(10) NOT NULL,
    discount_price                    NUMBER(10) NULL,
    created_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payment_company_discount_prices PRIMARY KEY (payment_company_discount_price_id),
    CONSTRAINT fk_payment_company_discount_prices_benefit
        FOREIGN KEY (payment_benefit_id) REFERENCES payment_benefits (payment_benefit_id),
    CONSTRAINT chk_payment_company_discount_prices CHECK (discount_price > 0)
);

-- 결제 회사 할인 계약 조건
CREATE TABLE payment_company_discount_contract_terms
(
    payment_company_discount_contract_terms_id NUMBER(10) NOT NULL,
    payment_benefit_id                         NUMBER(10) NOT NULL,
    contract_terms_price                       NUMBER(10) NULL,
    created_at                                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payment_company_discount_contract_terms PRIMARY KEY (payment_company_discount_contract_terms_id),
    CONSTRAINT fk_payment_company_discount_contract_terms_benefit
        FOREIGN KEY (payment_benefit_id) REFERENCES payment_benefits (payment_benefit_id),
    CONSTRAINT chk_payment_company_discount_contract_terms CHECK (contract_terms_price > 0)
);

-- =====================================================
-- 주문 관련 테이블
-- =====================================================

-- 주문
CREATE TABLE orders
(
    order_id              NUMBER(10)   NOT NULL,
    user_id               NUMBER(10)   NOT NULL,
    user_address_id       NUMBER(10)   NOT NULL,
    shipping_id           NUMBER(10)   NOT NULL,
    order_number          NUMBER(20)   NULL,
    total_price           NUMBER(10)   NULL,
    order_discount_amount NUMBER(10)   NULL,
    final_price           NUMBER(10)   NULL,
    order_status          VARCHAR2(30) NULL,
    payment_method_id     NUMBER(10)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_orders PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_orders_user_address
        FOREIGN KEY (user_address_id) REFERENCES user_addresses (user_address_id),
    CONSTRAINT fk_orders_shipment
        FOREIGN KEY (shipping_id) REFERENCES shipments (shipping_id),
    CONSTRAINT fk_orders_payment_method
        FOREIGN KEY (payment_method_id) REFERENCES payment_methods (payment_method_id),
    CONSTRAINT chk_orders_total_price CHECK (total_price >= 0),
    CONSTRAINT chk_orders_final_price CHECK (final_price >= 0)
);

-- 주문 항목
CREATE TABLE order_items
(
    order_item_id NUMBER(10) NOT NULL,
    product_id    NUMBER(10) NOT NULL,
    coupon_id     NUMBER(10) NOT NULL,
    order_id      NUMBER(10) NOT NULL,
    quantity      NUMBER(10) NULL,
    unit_price    NUMBER(10) NULL,
    total_price   NUMBER(10) NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_order_items PRIMARY KEY (order_item_id),
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_order_items_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT chk_order_items_unit_price CHECK (unit_price > 0),
    CONSTRAINT chk_order_items_total_price CHECK (total_price > 0)
);

-- 사용자 마일리지 내역
CREATE TABLE user_mileage_histories
(
    user_mileage_id      NUMBER(10)   NOT NULL,
    user_id              NUMBER(10)   NOT NULL,
    order_item_id        NUMBER(10)   NOT NULL,
    user_mileage_type_id NUMBER(10)   NOT NULL,
    amount               NUMBER(10)   NULL,
    transaction_type     VARCHAR2(40) NULL,
    expires_at           TIMESTAMP    NULL,
    earned_at            TIMESTAMP    NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_mileage_histories PRIMARY KEY (user_mileage_id),
    CONSTRAINT fk_user_mileage_histories_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_mileage_histories_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id),
    CONSTRAINT fk_user_mileage_histories_type
        FOREIGN KEY (user_mileage_type_id) REFERENCES user_mileage_types (user_mileage_type_id)
);

-- 상품 배송 상태
CREATE TABLE product_shipping_statuses
(
    product_shipping_status_id NUMBER(10)    NOT NULL,
    shipping_status_id         NUMBER(10)    NOT NULL,
    order_item_id              NUMBER(10)    NOT NULL,
    track_number               VARCHAR2(100) NULL,
    created_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product_shipping_statuses PRIMARY KEY (product_shipping_status_id),
    CONSTRAINT fk_product_shipping_statuses_status
        FOREIGN KEY (shipping_status_id) REFERENCES shipping_statuses (shipping_status_id),
    CONSTRAINT fk_product_shipping_statuses_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id)
);

-- 주문 취소/반품/교환
CREATE TABLE order_cancellation_return_exchanges
(
    order_cancellation_return_exchange_id NUMBER(10)    NOT NULL,
    order_item_id                         NUMBER(10)    NOT NULL,
    cancellation_return_exchange_reason   VARCHAR2(200) NULL,
    return_method                         VARCHAR2(30)  NULL,
    return_pickup_location                VARCHAR2(30)  NULL,
    return_destination                    VARCHAR2(30)  NULL,
    return_shipping_fee                   NUMBER(10)    NULL,
    return_date                           TIMESTAMP     NULL,
    exchange_method                       VARCHAR2(30)  NULL,
    exchange_pickup_location              VARCHAR2(30)  NULL,
    exchange_destination                  VARCHAR2(30)  NULL,
    exchange_shipping_fee                 NUMBER(10)    NULL,
    cancellation_return_exchange_status   VARCHAR2(30)  NULL,
    return_carrier                        VARCHAR2(30)  NULL,
    return_tracking_number                VARCHAR2(30)  NULL,
    reshipment_carrier                    VARCHAR2(30)  NULL,
    reship_tracking_number                VARCHAR2(30)  NULL,
    refund_method                         VARCHAR2(30)  NULL,
    refund_status                         VARCHAR2(30)  NULL,
    notice_id                             VARCHAR2(30)  NULL,
    completion_date                       TIMESTAMP     NULL,
    created_at                            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_order_cancellation_return_exchanges PRIMARY KEY (order_cancellation_return_exchange_id),
    CONSTRAINT fk_order_cancellation_return_exchanges_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id),
    CONSTRAINT chk_order_cancellation_return_exchanges_shipping_fee
        CHECK (exchange_shipping_fee >= 0),
    CONSTRAINT chk_order_cancellation_return_exchanges_status
        CHECK (cancellation_return_exchange_status IN ('요청', '승인', '거절', '환불 처리', '상품 회수', '환불', '새 상품 발송', '완료')),
    CONSTRAINT chk_order_cancellation_return_exchanges_refund_status
        CHECK (refund_status IN ('환불요청', '승인', '거절', '환불진행', '환불완료', '환불실패'))
);

-- 사용자 장바구니
CREATE TABLE user_carts
(
    user_cart_id  NUMBER(10)    NOT NULL,
    user_id       NUMBER(10)    NOT NULL,
    product_id    NUMBER(10)    NOT NULL,
    cart_quantity NUMBER(10)    NULL,
    cart_option   VARCHAR2(200) NULL,
    cart_status   VARCHAR2(200) NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_carts PRIMARY KEY (user_cart_id),
    CONSTRAINT fk_user_carts_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_user_carts_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT chk_user_carts_quantity CHECK (cart_quantity > 0)
);

-- 판매 기록
CREATE TABLE sale_records
(
    sale_record_id NUMBER(10) NOT NULL,
    order_item_id  NUMBER(10) NOT NULL,
    user_id        NUMBER(10) NOT NULL,
    quantity       NUMBER(10) NULL,
    sale_price     NUMBER(10) NULL,
    sale_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_sale_records PRIMARY KEY (sale_record_id),
    CONSTRAINT fk_sale_records_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id),
    CONSTRAINT fk_sale_records_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT chk_sale_records_quantity CHECK (quantity > 0),
    CONSTRAINT chk_sale_records_price CHECK (sale_price > 0)
);

-- =====================================================
-- 시퀀스 생성
-- =====================================================

-- 브랜드 관련 시퀀스
CREATE SEQUENCE seq_brands START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_brand_categories START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_brand_has_categories START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_national_infos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_advertisements START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_event_hashtags START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_courier_companies START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_notices START WITH 1 INCREMENT BY 1;

-- 사용자 관련 시퀀스
CREATE SEQUENCE seq_users START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_addresses START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_mileage_types START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_mileage_histories START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_bank_name_lists START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_bank_refund_accounts START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_grades START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_inquiries START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_inquiry_images START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_inquiry_types START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_inquiry_subtypes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_coupons START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_profile_images START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_physical_infos START WITH 1 INCREMENT BY 1;

-- 상품 관련 시퀀스
CREATE SEQUENCE seq_products START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_categories START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_option_types START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_option_types START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_variants START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_images START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_connection_tags START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_inquiries START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_inquiry_images START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_inquiry_answers START WITH 1 INCREMENT BY 1;

-- 리뷰 관련 시퀀스
CREATE SEQUENCE seq_reviews START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_review_images START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_review_stats START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_comments START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_product_likes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_brand_likes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_view_logs START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_recent_product_view_histories START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_search_keywords START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_search_logs START WITH 1 INCREMENT BY 1;

-- 배송 관련 시퀀스
CREATE SEQUENCE seq_shipping_statuses START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_shipping_request_types START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_scheduled_delivery_informations START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_shipments START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_shipping_fees START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_shipping_fee_conditions START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_shipping_fee_policies START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_shipping_statuses START WITH 1 INCREMENT BY 1;

-- 쿠폰 관련 시퀀스
CREATE SEQUENCE seq_coupons START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_coupon_conditions START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_coupon_discount_rules START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_coupon_types START WITH 1 INCREMENT BY 1;

-- 결제 관련 시퀀스
CREATE SEQUENCE seq_payment_methods START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_benefits START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_companies START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_company_discount_prices START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_company_discount_contract_terms START WITH 1 INCREMENT BY 1;

-- 주문 관련 시퀀스
CREATE SEQUENCE seq_orders START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_order_items START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_order_cancellation_return_exchanges START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_user_carts START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sale_records START WITH 1 INCREMENT BY 1;