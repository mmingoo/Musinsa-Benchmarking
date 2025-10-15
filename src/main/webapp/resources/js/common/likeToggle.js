// ==============================
// likeToggle.js - 공통 좋아요 토글 기능
// ==============================

const LikeToggle = (() => {
    const $ = window.jQuery;

    // 숫자 포맷팅 유틸
    function formatKoreanNumber(num) {
        if (num >= 100000000) {
            const eok = Math.floor(num / 100000000);
            const remainder = num % 100000000;
            let result = eok + '억';
            if (remainder >= 10000) {
                const man = Math.floor(remainder / 10000);
                result += man + '만';
            }
            return result;
        } else if (num >= 10000) {
            const man = Math.floor(num / 10000);
            return man + '만';
        } else if (num >= 1000) {
            return Math.floor(num / 1000) + '천';
        } else {
            return String(num);
        }
    }

    /**
     * 상품 좋아요 토글
     * @param {number} productId - 상품 ID
     * @param {object} options - 옵션 객체
     * @param {string} options.heartSelector - 하트 아이콘 셀렉터 (기본: '.wishlist-icon .heart-icon')
     * @param {string} options.countSelector - 좋아요 카운트 셀렉터 (선택사항)
     * @param {function} options.onSuccess - 성공 콜백 (선택사항)
     * @param {function} options.onError - 에러 콜백 (선택사항)
     */
    function toggleProductLike(productId, options = {}) {
        const {
            heartSelector = '.wishlist-icon .heart-icon',
            countSelector = null,
            onSuccess = null,
            onError = null
        } = options;

        if (!productId) {
            alert('상품 정보를 불러오지 못했습니다.');
            return;
        }

        $.ajax({
            url: `/api/v1/products/${productId}/liked`,
            method: 'POST',
            dataType: 'json',
            success: function (response) {
                // 좋아요 카운트 업데이트
                if (countSelector && typeof response.likeCount !== 'undefined') {
                    const $likeCnt = $(countSelector);
                    $likeCnt.text(formatKoreanNumber(response.likeCount))
                        .addClass('like-count-updated');
                    setTimeout(() => $likeCnt.removeClass('like-count-updated'), 300);
                }

                // 하트 아이콘 토글
                const $heart = $(heartSelector);
                if (response.liked) {
                    $heart.removeClass('far').addClass('fas liked');
                } else {
                    $heart.removeClass('fas liked').addClass('far');
                }

                // 성공 콜백
                if (onSuccess) onSuccess(response);
            },
            error: function (xhr) {
                console.error('상품 좋아요 실패:', xhr.responseText || xhr);
                if (onError) {
                    onError(xhr);
                } else {
                    alert('좋아요 처리에 실패했습니다.');
                }
            }
        });
    }

    /**
     * 브랜드 좋아요 토글
     * @param {number} brandId - 브랜드 ID
     * @param {object} options - 옵션 객체
     * @param {string} options.heartSelector - 하트 아이콘 셀렉터 (기본: '.brand-wishlist-icon .heart-icon')
     * @param {string} options.countSelector - 좋아요 카운트 셀렉터 (선택사항)
     * @param {function} options.onSuccess - 성공 콜백 (선택사항)
     * @param {function} options.onError - 에러 콜백 (선택사항)
     */
    function toggleBrandLike(brandId, options = {}) {
        const {
            heartSelector = '.brand-wishlist-icon .heart-icon',
            countSelector = null,
            onSuccess = null,
            onError = null
        } = options;

        if (!brandId) {
            alert('브랜드 정보를 불러오지 못했습니다.');
            return;
        }

        $.ajax({
            url: `/api/v1/brands/${brandId}/liked`,
            method: 'POST',
            xhrFields: { withCredentials: true },
            dataType: 'json',
            success: function (response) {
                // 좋아요 카운트 업데이트
                if (countSelector && typeof response.likeCount !== 'undefined') {
                    const $likeCnt = $(countSelector);
                    $likeCnt.text(formatKoreanNumber(response.likeCount))
                        .addClass('like-count-updated');
                    setTimeout(() => $likeCnt.removeClass('like-count-updated'), 300);
                }

                // 하트 아이콘 토글
                const $heart = $(heartSelector);
                if (response.liked) {
                    $heart.removeClass('far').addClass('fas liked');
                } else {
                    $heart.removeClass('fas liked').addClass('far');
                }

                // 성공 콜백
                if (onSuccess) onSuccess(response);
            },
            error: function (xhr) {
                console.error('브랜드 좋아요 실패:', xhr.responseText || xhr);
                if (onError) {
                    onError(xhr);
                } else {
                    alert('좋아요 처리에 실패했습니다.');
                }
            }
        });
    }

    // Public API
    return {
        toggleProductLike,
        toggleBrandLike,
        formatKoreanNumber // 유틸 함수도 export
    };
})();

// 전역으로 사용 가능하도록
window.LikeToggle = LikeToggle;
