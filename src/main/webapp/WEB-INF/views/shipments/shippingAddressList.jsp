<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>배송지 정보</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        /* 글로벌 스타일 */
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", "Oxygen", "Ubuntu", "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #fff; /* 배경색을 흰색으로 변경 (이미지 반영) */
        }

        /* 헤더 스타일 */
        h1 {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee; /* 이미지에 맞게 얇게 유지 */
            color: #333;
        }

        /* 검색창 (인라인 스타일을 CSS 클래스로 변경) */
        .search-container {
            margin-bottom: 20px;
            position: relative;
        }
        .search-input {
            width: 100%;
            padding: 12px 40px 12px 15px;
            border: 1px solid #ddd; /* 이미지에 맞춰 테두리색 조정 */
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 14px;
            background-color: #f7f7f7; /* 이미지에 맞춰 배경색 조정 */
        }
        .search-icon {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #888;
        }

        /* 배송지 추가 버튼 */
        .add-button {
            width: 100%;
            padding: 15px;
            margin-bottom: 20px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
        }

        /* 배송지 항목 스타일 */
        .address-item {
            display: flex;
            align-items: flex-start;
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
            cursor: pointer;
        }

        #address-list-container{
            /* 버튼 높이(52) + 바 패딩(16*2) ≈ 84 + iOS 안전영역 */
            padding-bottom: calc(84px + env(safe-area-inset-bottom));
        }

        .address-item:first-child {
            border-top: 1px solid #f0f0f0;
        }

        /* 라디오 버튼 컨테이너 */
        .radio-container {
            margin-right: 15px;
            padding-top: 3px;
        }

        /* 사용자 정의 라디오 버튼 스타일 */
        input[type="radio"] {
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            width: 18px;
            height: 18px;
            border: 1px solid #ccc;
            border-radius: 50%;
            outline: none;
            cursor: pointer;
            position: relative;
        }

        input[type="radio"]:checked {
            border: 5px solid #000;
            background-color: #fff;
        }

        /* 주소 내용 컨테이너 */
        .address-content {
            flex-grow: 1;
        }

        .address-line {
            display: flex;
            align-items: center;
            margin-bottom: 5px;
        }

        /* 배송지 이름 스타일 */
        .recipientName {
            font-weight: 500;
            font-size: 16px;
            margin-right: 8px;
            color: #111;
        }


        /* 기본 배송지 태그 스타일 */
        .tag-default {
            background-color: #8a8a8a1a;
            color: #666666;
            border-radius: 2px;
            margin-left: 4px;
            padding: 1px 4px;
            font-size: 11px;
            font-weight: 400;
            line-height: 14px;
            overflow: hidden /* 기본적으로 숨김 */
        }
        /* 기본 배송지일 경우 태그 노출 */
        .isDefault[data-isdefault="true"] + .tag-default {
            display: inline-block;
        }

        /* 주소 및 연락처 스타일 */
        .address-info {
            font-size: 14px;
            color: #111; /* 주소는 진하게 */
            line-height: 1.4;
            margin-bottom: 5px; /* 주소와 연락처 사이에 약간의 간격 */
        }
        .recipientPhone {
            color: #666; /* 연락처는 조금 연하게 */
            display: block;
        }

        /* 버튼 컨테이너 */
        .actions-container {
            margin-top: 10px; /* 이미지에 맞춰 여백 조정 */
        }

        /* 수정/삭제 버튼 스타일 */
        .action-button {
            background-color: #fff;
            border: 1px solid #ddd; /* 이미지처럼 테두리 추가 */
            color: #111; /* 텍스트 색상 검정 */
            font-size: 13px;
            font-weight: 500;
            cursor: pointer;
            padding: 5px 10px;
            margin-right: 5px;
            border-radius: 3px;
            text-decoration: none; /* 밑줄 제거 */
        }

        /* 템플릿은 화면에 보이지 않게 숨깁니다. */
        #address-template {
            display: none;
        }

        /* 하단 '변경하기' 버튼 스타일 */
        .change-button {
            position: fixed;
            bottom: 0;
            left: 0;
            width: 100%;
            padding: 15px 20px; /* 좌우 패딩을 20px으로 설정 */
            background-color: #fff;
            box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.05);
            box-sizing: border-box;
            text-align: center;
            z-index: 100;
        }
        .change-button button {
            width: 100%;
            height: 50px;
            padding: 15px;
            background-color: #000;
            color: #fff;
            font-size: 16px;
            font-weight: 700;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<h1>배송지 정보</h1>

<div style="margin-bottom: 20px; position: relative;">
    <input type="text" id="search-input" placeholder="배송지 이름, 주소, 연락처로 검색하세요"
           style="width: 100%; padding: 12px 40px 12px 15px; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; font-size: 14px;">
    <span style="position: absolute; right: 15px; top: 50%; transform: translateY(-50%); color: #888;">🔍</span>
</div>

<button style="width: 100%; padding: 15px; margin-bottom: 20px; background-color: #fff; border: 1px solid #ddd; border-radius: 5px; font-size: 15px; font-weight: 600; cursor: pointer;"
    onclick="location.href='/shipping-address-add'">
    배송지 추가하기
</button>

<div id="search-empty" style="display:none; padding: 20px 0; color:#666; text-align:center;">
    검색 결과가 없습니다.
</div>

<div id="address-list-container">
</div>

<div id="address-template">
    <div class="address-item">
        <div class="radio-container">
            <input type="radio" name="shippingAddress" class="radio-select" value="" />
        </div>

        <div class="address-content">
            <div class="address-line">
                <span class="recipientName"></span>
                <span class="tag-default">기본 배송지</span>
                <span class="isDefault" data-default=""></span> </div>

            <div class="address-info">
                <span class="postalCode"></span>
                <span class="recipientAddress"></span><br>
                <span class="recipientPhone"></span>
            </div>

            <div class="actions-container">
                <button class="action-button modify-button">수정</button>
                <button class="action-button delete-button">삭제</button>
            </div>
        </div>
        <p class="shippingAddressId" style="display:none;"></p> <p class="postalCode" style="display:none;"></p>
    </div>
</div>

<div class="change-button">
    <button onclick="changeAddress()">배송지 변경하기</button>
</div>

<script>
    $(document).ready(function () {

        const $listContainer = $('#address-list-container');
        // 템플릿에서 복제할 요소를 선택합니다.
        const $template = $('#address-template > .address-item');
        const $searchInput = $('#search-input');

        // --- 검색 유틸 ---
        function normalize(str) {
            return (str || '')
                .toString()
                .toLowerCase()
                .replace(/[\s-]/g, ''); // 공백/하이픈 제거
        }
        function debounce(fn, delay) {
            let t; return function() {
                const ctx = this, args = arguments;
                clearTimeout(t); t = setTimeout(function(){ fn.apply(ctx, args); }, delay);
            };
        }
        function filterList(query) {
            const q = normalize(query);
            let visibleCount = 0;
            $('#address-list-container .address-item').each(function(){
                const $item = $(this);
                const hay = $item.attr('data-search') || '';
                const matched = q === '' || hay.indexOf(q) !== -1;
                $item.toggle(matched);
                if (matched) visibleCount++;
            });
            // 비어있는 상태 메시지 토글
            if (visibleCount === 0) {
                $('#search-empty').show();
            } else {
                $('#search-empty').hide();
            }
        }

        // 입력 시 실시간 필터링 (디바운스)
        $searchInput.on('input', debounce(function(){
            filterList($(this).val());
        }, 120));



        $.ajax({
            // 실제 API URL로 수정해야 합니다. 현재는 예시 '/api/v1/orders/address-list/1'
            url: '/api/v1/orders/address-list',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                $listContainer.empty();

                // 데이터가 배열이 아닌 경우를 대비하여 배열 형태로 감싸줍니다. (API 응답 형식에 따라 변경 가능)
                const addressList = Array.isArray(data) ? data : (data && Array.isArray(data.addresses) ? data.addresses : []);

                // If USER_ID is not provided by JSP, try to infer from the first address item
                if ((!USER_ID || USER_ID === 'null' || USER_ID === '') && addressList.length > 0 && addressList[0].userId) {
                    USER_ID = String(addressList[0].userId);
                    window.USER_ID = USER_ID; // cache for other pages if needed
                }


                if (addressList.length > 0) {
                    addressList.forEach(function(address) {
                        // 템플릿을 복제합니다.
                        const $newItem = $template.clone();

                        // **배송지 ID**를 라디오 버튼의 value와 숨겨진 요소에 설정
                        $newItem.find('.radio-select').val(address.shippingAddressId);
                        $newItem.find('.shippingAddressId').text(address.shippingAddressId);

                        $newItem.find('data-user-id', address.userId);

                        // 복제한 템플릿에 데이터를 채웁니다.
                        $newItem.find('.recipientName').text(address.recipientName);
                        $newItem.find('.recipientPhone').text(address.recipientPhone);
                        $newItem.find('.postalCode').text(address.postalCode);
                        $newItem.find('.recipientAddress').text(address.recipientAddress);

                        // 검색 인덱스 구성 (이름/주소/우편번호/연락처)
                        const searchable = [
                            address.recipientName,
                            address.recipientAddress,
                            address.postalCode,
                            address.recipientPhone
                        ].join(' ');
                        $newItem.attr('data-search', normalize(searchable));


                        // **기본 배송지 처리**
                        const isDefault = address.isDefault;

                        // data-isdefault 속성 설정
                        $newItem.find('.isDefault').attr('data-isdefault', isDefault ? 'true' : 'false');

                        if (isDefault) {
                            $newItem.find('.radio-select').prop('checked', true); // 라디오 버튼 선택
                            $newItem.find('.delete-button').remove(); // 삭제 버튼 숨김

                            $newItem.find('.tag-default').show();
                        } else {
                            $newItem.find('.tag-default').remove();
                            $newItem.find('.tag-recently-used').remove();
                            // 기본 배송지가 아니면 '기본 배송지' 태그가 노출되지 않도록 처리됨 (CSS와 isDefault="false"로 충분)
                            // 추가적으로 태그 요소를 완전히 제거할 필요는 없지만, 일관성을 위해 기본 배송지 태그는 남겨둡니다.
                        }


                        // 완성된 항목을 목록에 추가합니다.
                        $listContainer.append($newItem);

                        // 항목 클릭 시 라디오 버튼도 선택되도록 이벤트 추가
                        $newItem.on('click', function() {
                            $(this).find('.radio-select').prop('checked', true);
                        });



                        // 버튼 클릭 이벤트는 별도로 처리하여 버블링 방지
                        <%--$newItem.find('.action-button').on('click', function(e) {--%>
                        <%--    e.stopPropagation();--%>
                        <%--    const action = $(this).hasClass('modify-button') ? '수정' : '삭제';--%>
                        <%--    alert(`${action} 버튼 클릭: ID ${address.shippingAddressId}`);--%>
                        <%--});--%>
                    });
                    filterList($searchInput.val());
                } else {
                    $listContainer.empty();
                    $('#search-empty').text('등록된 배송지 정보가 없습니다.').show();
                    //$listContainer.text('등록된 배송지 정보가 없습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error('주소 정보 로드 오류:', error);
                $listContainer.empty();
                $('search-empty').text('주소 정보를 불러오는 데 실패했습니다.').show();
            }
        });

        // 수정 버튼 이벤트
        $(document).on('click', '.modify-button', function(e){
            e.stopPropagation();
            const id = $(this).closest('.address-item').find('.radio-select').val();
            location.href = '${pageContext.request.contextPath}/shipping-address-edit?shippingAddressId=' + encodeURIComponent(id);
        });

        // 삭제 버튼 이벤트
        // Context path and user id constants
        const CTX = '${pageContext.request.contextPath}';
        let USER_ID = (('${userId}' !== '' && '${userId}' !== 'null') ? '${userId}' : (window.USER_ID || null));
        $(document).on('click', '.delete-button', function(e){
            e.stopPropagation();
            //const id = $(this).closest('.address-item').find('.radio-select').val();
            const $item = $(this).closest('.address-item');
            const id = $item.find('.radio-select').val();
            const userId = $item.data('user-id') || USER_ID; // fallback to global if needed

            if (confirm('정말 삭제하시겠습니까?')) {
                if (!userId){
                    alert('사용자 식별자를 확인할 수 없습니다. 다시 로그인한 후 시도해주세요.');
                    return;
                }

                const $btn = $(this);
                $btn.prop('disabled', true);

                $.ajax({
                    type: 'DELETE',
                    url: CTX + '/api/v1/delete/' + encodeURIComponent(id) + '/' + encodeURIComponent(userId),
                    success: () => {
                        alert('삭제가 완료되었습니다.');
                        $item.remove();
                        if ($('#address-list-container .address-item').length === 0) {
                            $('#search-empty').text('등록된 배송지 정보가 없습니다.').show();
                        }
                    },
                    error: (xhr) => {
                        alert('삭제 실패: ' + (xhr.responseText || '알 수 없는 오류'));
                        $btn.prop('disabled', false);
                    }
                });
            }
        });

    });

    //검색 기능 추가


    // 배송지 변경 기능
    function changeAddress() {
        const selectedRadio = $('input[name="shippingAddress"]:checked');

        if (selectedRadio.length === 0) {
            alert('변경할 배송지를 선택해주세요.');
            return;
        }

        const selectedItem = selectedRadio.closest('.address-item');

        // 선택된 배송지의 정보를 수집
        const addressData = {
            shippingAddressId: selectedRadio.val(),
            recipientName: selectedItem.find('.recipientName').text(),
            recipientPhone: selectedItem.find('.recipientPhone').text(),
            postalCode: selectedItem.find('.postalCode').text(),
            recipientAddress: selectedItem.find('.recipientAddress').text(),
            recipientDetailAddress: '' // 필요시 추가 데이터 처리
        };

        // 부모 창에 배송지 정보 전달
        if (window.opener && window.opener.updateAddressFromPopup) {
            window.opener.updateAddressFromPopup(addressData);
        } else {
            alert('부모 창과의 연결에 문제가 있습니다.');
        }
    }





</script>

</body>
</html>