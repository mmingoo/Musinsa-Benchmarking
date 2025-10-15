<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Basic Long Sleeve T Shirt - 제품 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/test.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <%@ include file="../main/header.jsp" %>
    <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>">
</head>

<body>

<div class="container product-page-main">
    <div class="content-wrapper">
        <div class="product-left">
            <div class="image-previews" id="thumbnailImages">
            </div>
            <div class="main-image-container">
            </div>
        </div>

        <div class="product-right">
            <div class="seller-info">
                <div class="brand-header">
                    <span class="brand-image-container" id="brand-image-container"></span>
                    <span class="brand-name" id="brandName"></span>
                </div>
                <span class="product-title" id="productName"></span>
            </div>

            <div class="price-section">
                <span class="original-price" id="productPrice"></span>
                <button class="coupon-btn">쿠폰받기</button>

                <div class="final-price-wrapper">
                    <span class="discounted-price" id="productTotalPrice"></span>
                    <span class="sale-percentage" id="productDiscount"></span>
                    <span class="max-benefit-label">최대혜택가</span>
                    <span class="detail-link">자세히 <i class="arrow-down"></i></span>
                </div>
            </div>

            <div class="point-section">
                <p class="max-point-amount">9,250원 최대적립</p>
                <ul>
                    <li>등급 적립 (LV.5 실버 • 1.5%) <span>2,530원</span></li>
                    <li>후기 적립 <span>2,500원</span></li>
                    <li>무신사머니 결제 시 적립 2.5% <span>4,220원</span></li>
                </ul>
            </div>

            <div class="money-benefit-banner">
                <span class="money-logo">M Money</span>
                <p>무신사머니 첫 결제 시 **10%** <span>추가 적립</span></p>
                <i class="arrow-right"></i>
            </div>

            <div class="shipping-info">
                <p>무료배송 | 오늘출발 가능</p>
            </div>

            <div class="options-section">
                <label for="color-select">색상</label>
                <select id="color-select" class="color-select">
                    <option value="">색상 선택</option>
                </select>

                <label for="size-select" style="margin-left: 10px;">사이즈</label>
                <select id="size-select" class="size-select">
                    <option value="">사이즈 선택</option>
                </select>


            </div>

            <div class="bottom-action-buttons">
                <button class="wishlist-icon">
                    <span class="heart-icon">❤️</span>
                    <span id="productLikeCnt">23만</span>
                </button>

                <button class="add-to-cart-btn large-btn">장바구니</button>

                <button class="buy-now large-btn">구매하기</button>
            </div>

            <div class="additional-info">
                <p><strong>쿠폰 안내</strong>: 회원 등급별 할인 쿠폰 적용 가능</p>
                <p id="brandId" style="display:none;"></p>
                <p id="productId" style="display:none;"></p>
            </div>
        </div>
    </div>

    <hr>

    <div class="product-description-area">
        <div class="tabs">
            <a href="#" class="active">제품 정보</a>
            <a href="#">리뷰</a>
            <a href="#">Q&A</a>
            <a href="#">교환/반품</a>
        </div>

        <div class="product-specs">
            <h2 class="description-heading">제품 상세 정보</h2>

            <div class="full-description-image">
            </div>
        </div>
    </div>
</div>
<div id="detailSizeImage" class="product-detailSizeImage-url"></div>
<script>
    // --------------------------------- 제품 상세 정보 및 옵션 AJAX ---------------------------------
    $(document).ready(function () {
        $.ajax({
            url: '/api/v1/products/1',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                // 기존 상품 정보 설정 코드...
                $('#productId').text(data.productId);
                $('#productName').text(data.productName);
                $('#brandName').text(data.brandName);

                $('#productPrice').text(data.price.toLocaleString() + '원');
                $('#productDiscount').text(data.brandDiscount + '%');
                $('#productTotalPrice').text(data.finalprice.toLocaleString() + '원');
                $('#productLikeCnt').text(data.brandLikeCnt.toLocaleString());

                if (data.brandImage) {
                    const $brandImageContainer = $('#brand-image-container').empty();
                    const imgTag = $('<img>').attr('src', data.brandImage).attr('alt', data.brandName).addClass('brand-logo-img');
                    $brandImageContainer.append(imgTag);
                }

                // 상품 이미지 처리
                if (data && data.detailSizeImage) {
                    const imgTag = $('<img>').attr('src', data.detailSizeImage).attr('alt', '제품 실측 이미지').addClass('detail-size-img');
                    $('#detailSizeImage').empty().append(imgTag);
                }

                const $mainImageContainer = $('.main-image-container').empty();
                const $thumbnailImages = $('#thumbnailImages').empty();

                if (data.productImageList && data.productImageList.length) {
                    data.productImageList.forEach(function (img) {
                        const imgTag = $('<img>').attr('src', img.imageUrl).attr('alt', img.imageType);
                        if (img.imageType === 'MAIN') {
                            $mainImageContainer.append(imgTag.clone());
                        } else {
                            const $previewItem = $('<div>').addClass('preview-item').append(imgTag.clone());
                            $thumbnailImages.append($previewItem);
                        }
                    });
                }

                const productId = data.productId;
                if (productId) {
                    $.ajax({
                        url: `/api/v1/products/${productId}/options`,
                        method: 'GET',
                        dataType: 'json',
                        success: function (variants) {
                            console.log('서버에서 받은 상품 조합 목록:', variants);

                            // 올바른 필드명으로 수정
                            const colors = [...new Set(variants.map(v => v.productColor))];
                            const sizes = [...new Set(variants.map(v => v.productsSize))];

                            const colorSelect = $('#color-select');
                            const sizeSelect = $('#size-select');

                            // 색상 드롭다운 채우기
                            colorSelect.empty().append('<option value="">-- 색상 선택 --</option>');
                            colors.forEach(color => {
                                if (color && color.trim() !== '') {
                                    colorSelect.append('<option value="' + color + '">' + color + '</option>');
                                }
                            });

                            // 사이즈 드롭다운 채우기
                            sizeSelect.empty().append('<option value="">-- 사이즈 선택 --</option>');
                            sizes.forEach(size => {
                                if (size && size.trim() !== '') {
                                    sizeSelect.append('<option value="' + size + '">' + size + '</option>');
                                }
                            });
                            // 드롭다운 변경 이벤트 추가
                            colorSelect.change(function() {
                                const selectedColor = $(this).val();
                                console.log('선택된 색상:', selectedColor);
                            });

                            sizeSelect.change(function() {
                                const selectedSize = $(this).val();
                                console.log('선택된 사이즈:', selectedSize);
                            });
                        },
                        error: function(xhr, status, error) {
                            console.error('상품 옵션 로드 실패:', error);
                            alert('상품 옵션을 불러오는 데 실패했습니다.');
                        }
                    });
                }
            },
            error: function (xhr, status, error) {
                console.error('제품 정보 로드 오류:', error);
            }
        });
    });
</script>

<div id="categoryName" class="product-categories"></div>
<script>
    $(document).ready(function () {
        $.ajax({
            url: '/api/v1/products/${productId}/categories', // url 소문자
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                $('#categoryName').empty();

                if (data.productCategoryList && data.productCategoryList.length) {
                    // categoryName만 뽑아서 배열로 변환
                    const names = data.productCategoryList.map(function (category) {
                        return category.categoryName;
                    });

                    // " > "로 이어붙이기
                    const joined = names.join(" > ");

                    // span 하나에 출력
                    const valueSpan = $('<span>')
                        .text(joined)
                        .addClass('product-categories');

                    $('#categoryName').append(valueSpan);
                }
            },
            error: function (xhr, status, error) {
                console.error('제품 카테고리 정보 로드 오류:', error);
            }
        });
    });
</script>

<div id="detailDescription" class="product-description-url"></div>
<script>
    $(document).ready(function () {
        $.ajax({
            url: '/api/v1/products/${productId}/detail-Info',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                if (data.detailDescription) {
                    const imgTag = $('<img>')
                        .attr('src', data.detailDescription)
                        .attr('alt', '제품 상세 이미지')
                        .addClass('detail-description-img'); // 필요하면 CSS 클래스 추가

                    $('#detailDescription').empty().append(imgTag);
                }
            },
            error: function (xhr, status, error) {
                console.error('제품 상세 설명 정보 로드 오류:', error);
            }
        });
    });
</script>


<div id="productDetailSizeList" class="product-detail-size-list">
    <!-- 사이즈 표가 여기에 동적으로 생성됩니다. -->
</div>
<script>
    $(document).ready(function () {
        $.ajax({
            url: '/api/v1/products/${productId}/detail-size-list',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                let firstImageId = null;
                const $sizeListContainer = $('#productDetailSizeList');
                $sizeListContainer.empty();

                if (data && data.length > 0 && data[0].sizeDetailImageId) {
                    firstImageId = data[0].sizeDetailImageId;
                }

                if (firstImageId === 1 || firstImageId === 2) {
                    // 상의 사이즈 처리
                    const title = '<h3>상의 실측 사이즈 (cm)</h3>';
                    const $table = $('<table>').addClass('size-table');
                    const $thead = $('<thead>').html(
                        '<tr>' +
                        '<th>사이즈</th>' +
                        '<th>총장</th>' +
                        '<th>어깨너비</th>' +
                        '<th>가슴단면</th>' +
                        '<th>소매길이</th>' +
                        '</tr>'
                    );
                    const $tbody = $('<tbody>');

                    data.forEach(function (size) {
                        const $row = $('<tr>').append(
                            $('<td>').text(size.cm),
                            $('<td>').text(size.length),
                            $('<td>').text(size.shoulderWidth),
                            $('<td>').text(size.chestWidth),
                            $('<td>').text(size.sleaveLength)
                        );
                        $tbody.append($row);
                    });

                    $table.append($thead).append($tbody);
                    $sizeListContainer.append(title).append($table);

                } else if (firstImageId === 3 || firstImageId === 4) {
                    // 하의 사이즈 처리: 제공된 데이터 구조에 맞춰 테이블 생성
                    const title = '<h3>하의 실측 사이즈 (cm)</h3>';
                    const $table = $('<table>').addClass('size-table');
                    const $thead = $('<thead>').html(
                        '<tr>' +
                        '<th>사이즈</th>' +
                        '<th>총장</th>' +
                        '<th>허리단면</th>' +
                        '<th>엉덩이단면</th>' +
                        '<th>허벅지단면</th>' +
                        '<th>밑위</th>' +
                        '<th>밑단단면</th>' +
                        '</tr>'
                    );
                    const $tbody = $('<tbody>');

                    data.forEach(function (size) {
                        const $row = $('<tr>').append(
                            $('<td>').text(size.cm),
                            $('<td>').text(size.length),
                            $('<td>').text(size.waist),
                            $('<td>').text(size.hip),
                            $('<td>').text(size.thigh),
                            $('<td>').text(size.rise),
                            $('<td>').text(size.hemWidth)
                        );
                        $tbody.append($row);
                    });

                    $table.append($thead).append($tbody);
                    $sizeListContainer.append(title).append($table);

                } else {
                    $sizeListContainer.text('표시할 상세 사이즈 정보가 없습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error('제품 사이즈 정보 로드 오류:', error);
                $('#productDetailSizeList').text('정보를 불러오는 데 실패했습니다.');
            }
        });
    });
</script>

<div id="productsReviews" class="product-reviews">
    <!-- 리뷰 목록이 여기에 동적으로 생성됩니다. -->
</div>
<script>
    $(document).ready(function () {
        $.ajax({
            url: '/api/v1/products/${productId}/reviews',
            method: 'GET',
            dataType: 'json',
            success: function (reviews) {
                const $reviewsContainer = $('#productsReviews');
                $reviewsContainer.empty(); // 기존 내용 비우기

                if (reviews && reviews.length > 0) {
                    reviews.forEach(function (review) {
                        const $reviewItem = $('<div>').addClass('review-item');

                        const $reviewHeader = $('<div>').addClass('review-header').append(
                            $('<strong>').addClass('review-nickname').text(review.nickName)
                        );
                        const $reviewBody = $('<div>').addClass('review-body').append(
                            $('<p>').addClass('review-content').text(review.content)
                        );
                        const $reviewFooter = $('<div>').addClass('review-footer').append(
                            $('<span>').addClass('review-purchase-options').text('구매옵션: ' + review.purchaseOptions),
                            $('<button>').addClass('review-help-button').text('도움이 돼요 ').append(
                                $('<span>').addClass('review-help-count').text(review.helpCount)
                            )
                        );
                        $reviewItem.append($reviewHeader, $reviewBody, $reviewFooter);
                        $reviewsContainer.append($reviewItem);
                    })
                } else {
                    $reviewsContainer.text('작성된 리뷰가 없습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error('리뷰 정보 로드 오류:', error);
                $('#productsReviews').text('리뷰를 불러오는 데 실패했습니다.');
            }
        });
    });

    // --------------------------------- 좋아요 버튼 클릭 이벤트 ---------------------------------
    $(document).ready(function () {
        $('.wishlist-icon').on('click', function () {

            // 페이지의 숨겨진 p 태그에서 productId 값을 읽어온다.
            const productId = parseInt($('#productId').text());
            if(!productId){
                alert('상품 ID를 찾을 수 없습니다.');
                return;
            }

            // 추천해 드린 동적 URL 방식으로 POST 요청을 보냅니다.
            $.ajax({
                url: `/api/v1/1/products/${productId}/liked`,
                method: 'POST',
                // 만약 서버에서 CSRF 토큰이나 인증 헤더를 요구한다면 여기에 추가해야 합니다.
                // headers: { 'X-CSRF-TOKEN': 'some-token-value' },
                dataType: 'json', // 서버로부터 받을 응답 데이터 타입
                success: function (response) {
                    // 성공적으로 처리되었을 때 서버로부터 받은 응"답(response)으로 UI 업데이트
                    console.log('좋아요 처리 성공:', response);

                    // 예: 서버가 응답으로 새로운 '좋아요' 총 개수를 보내준다고 가정
                    if (response.updatedLikeCount !== undefined) {
                        $('#productLikeCnt').text(response.updatedLikeCount.toLocaleString());
                    }

                    // 하트 아이콘에 'liked' 클래스를 추가/제거하여 색상 등을 변경할 수 있도록 함
                    $('.wishlist-icon .heart-icon').toggleClass('liked');

                },
                error: function (xhr, status, error) {
                    console.error('좋아요 처리 실패:', error);
                    // 실제 서비스에서는 사용자에게 더 친절한 에러 메시지를 보여주는 것이 좋습니다.
                    alert('요청을 처리하는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
                }
            });
        });
    });
</script>
</body>
</html>
