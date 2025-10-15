$(document).ready(() => {

    // --------------------------------------------------------
    // 0. 브랜드 목록 렌더링 함수 및 데이터 비동기 로딩
    // --------------------------------------------------------
    const $brandItemList = $("#brandItemList");
    const BRAND_PLACEHOLDER = "https://image.musinsa.com/images/brand/logo/default.png";

    function renderBrands(data) {
        if (!$brandItemList.length) return;
        $brandItemList.empty();
        if (!Array.isArray(data)) return;

        const items = data.map(brands => `
            <li>
                <a href="/products?keyword=${brands.brandNameKr}">
                    <div class="brand-info">
                        <img src="${brands.brandImage || BRAND_PLACEHOLDER}" alt="${brands.brandNameKr || ''} 로고">
                        <div class="text-info">
                            <p class="kor-name">
                                ${brands.brandNameKr || ''}<br>
                                <span class="eng-name">${brands.brandNameEn || ''}</span>
                            </p>
                        </div>
                    </div>
                </a>
            </li>
        `).join('');

        $brandItemList.html(items);
    }

    // 현재 선택된 카테고리와 초성을 저장하는 변수
    let currentCategoryId = "all";
    let currentInitial = "popular";
    let allBrands = [];
    let currentCategoryBrands = []; // 현재 카테고리의 브랜드 목록

    // 전체 브랜드 목록을 한 번에 불러오는 함수
    function loadAllBrands() {
        $.ajax({
            url: "/api/v1/categories/brands",
            type: "GET",
            dataType: "json",
            success: function (data) {
                allBrands = data;
                console.log("전체 브랜드 로드 완료", allBrands);
                // 초기 로드 시 전체 브랜드 표시
                filterAndRenderBrands(currentCategoryId, currentInitial);
            },
            error: function (xhr, status, error) {
                console.error("전체 브랜드 로딩 실패:", error);
                if ($brandItemList.length) {
                    $brandItemList.html("<li>브랜드 정보를 불러오지 못했습니다.</li>");
                }
            }
        });
    }

    // 카테고리와 초성에 따라 브랜드를 필터링하고 렌더링하는 함수
    function filterAndRenderBrands(categoryId, initial = "popular") {
        currentCategoryId = categoryId;
        currentInitial = initial;

        // 카테고리별 필터링 (전체가 아닌 경우)
        if (categoryId !== "all") {
            // 카테고리별 브랜드 API 호출
            loadBrandsByCategory(categoryId, initial);
        } else {
            // 전체 브랜드에서 초성별 필터링
            currentCategoryBrands = allBrands; // 전체 브랜드를 현재 카테고리로 설정
            applyInitialFilter(allBrands, initial);
        }
    }

    // 카테고리별 브랜드를 로드하는 함수
    function loadBrandsByCategory(categoryId, initial = "popular") {
        console.log(`카테고리 ${categoryId}별 브랜드 로딩 시작`);

        $.ajax({
            url: `/api/v1/categories/${categoryId}/brands`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.log(`카테고리 ${categoryId} 브랜드 로드 완료:`, data);
                // 현재 카테고리의 브랜드 목록 저장
                currentCategoryBrands = data;
                // 로드된 카테고리별 브랜드에 초성 필터 적용
                applyInitialFilter(data, initial);
            },
            error: function (xhr, status, error) {
                console.error(`카테고리 ${categoryId} 브랜드 로딩 실패:`, error);
                if ($brandItemList.length) {
                    $brandItemList.html("<li>해당 카테고리의 브랜드 정보를 불러오지 못했습니다.</li>");
                }
            }
        });
    }

    // 초성별 필터링을 적용하는 함수 (현재 카테고리 내에서만 필터링)
    function applyInitialFilter(brands, initial) {
        // 인기순인 경우 전달받은 브랜드 목록 그대로 렌더링
        if (initial === "popular") {
            renderBrands(brands);
            return;
        }

        // 초성별 클라이언트 사이드 필터링
        const filteredBrands = brands.filter(brand => {
            // 한글 초성 체크
            if (/[ㄱ-ㅎ]/.test(initial)) {
                const brandNameKr = brand.brandNameKr || '';
                if (brandNameKr) {
                    const firstChar = brandNameKr.charAt(0);
                    const chosung = getChosung(firstChar);
                    return chosung === initial;
                }
                return false;
            }
            // 영문 체크
            else if (/[A-Z]/.test(initial)) {
                const brandNameEn = brand.brandNameEn || '';
                if (brandNameEn) {
                    const firstChar = brandNameEn.charAt(0);
                    return firstChar.toUpperCase() === initial;
                }
                return false;
            }
            return false;
        });

        console.log(`${initial} 초성으로 필터링된 브랜드:`, filteredBrands.length, "개");
        renderBrands(filteredBrands);
    }

    // 한글 초성 추출 함수
    function getChosung(char) {
        const chosungList = ['ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'];
        const charCode = char.charCodeAt(0);
        if (charCode >= 0xAC00 && charCode <= 0xD7A3) {
            return chosungList[Math.floor((charCode - 0xAC00) / 588)];
        }
        return char;
    }


    loadAllBrands();

    // --------------------------------------------------------
    // 0.1 상품 카테고리 렌더링 함수 및 데이터 비동기 로딩
    // --------------------------------------------------------
    function renderSubCategories(parentCategoryId, subcategories) {
        const targetSectionId = getCategoryTargetId(parentCategoryId);
        const $targetSection = $(`#${targetSectionId}`);

        console.log("렌더링 시작 - 타겟 섹션:", targetSectionId, "카테고리 개수:", subcategories?.length || 0);
        console.log("타겟 섹션 DOM 찾음:", $targetSection.length > 0);

        if (!$targetSection.length) {
            console.error("타겟 섹션을 찾을 수 없음:", targetSectionId);
            return;
        }

        if (!Array.isArray(subcategories)) {
            console.error("subcategories가 배열이 아님:", subcategories);
            return;
        }

        const items = subcategories.map(category => {
            console.log("카테고리 아이템:", category.categoryName, "ID:", category.productCategoryId);
            const href = `/products/category/${category.productCategoryId}`;
            return `
                <a href="${href}" class="item" data-name="${category.categoryName}">
                    <img src="${category.categoryImage || 'https://image.msscdn.net/thumbnails/images/goods_img/default.jpg'}" alt="${category.categoryName}">
                    <span>${category.categoryName}</span>
                </a>
            `;
        }).join('');

        // 클릭할 때 categoryName 저장
        $(document).on('click', 'a.item', function () {
            const name = $(this).data('name');
            if (name) sessionStorage.setItem('categoryName', name);
        });

        console.log("생성된 HTML 길이:", items.length);

        // .detail-items 영역만 업데이트
        const $detailItems = $targetSection.find('.detail-items');
        console.log("detail-items 영역 찾음:", $detailItems.length > 0);

        if ($detailItems.length) {
            $detailItems.html(items);
            console.log("HTML 업데이트 완료");
        } else {
            console.error("detail-items 영역을 찾을 수 없음");
        }
    }

    function getCategoryTargetId(categoryId) {
        const mapping = {
            1: 'tops',
            2: 'outers',
            3: 'pants',
            4: 'dresses',
            5: 'bags',
            6: 'accessories',
            7: 'underwear'
        };
        return mapping[categoryId] || 'tops';
    }


    function loadSubCategories(parentCategoryId) {
        console.log("API 호출 시작 - 부모 카테고리 ID:", parentCategoryId);
        $.ajax({
            url: `/api/v1/categories/products?parentCategoryId=${parentCategoryId}`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.log("API 응답 성공:", data);
                console.log("데이터 길이:", data ? data.length : 0);
                renderSubCategories(parentCategoryId, data);
            },
            error: function (xhr, status, error) {
                console.error(`중분류 카테고리 로딩 실패 (ID: ${parentCategoryId}):`, error);
                console.error("HTTP 상태:", xhr.status);
                console.error("응답 텍스트:", xhr.responseText);
            }
        });
    }

    // --------------------------------------------------------
    // 1. DOM 요소 선택
    // --------------------------------------------------------
    const $menuBtn = $("#menuBtn");
    const $overlay = $("#categoryDropdownOverlay");
    const $tabLinks = $(".tab-link");
    const $tabContents = $(".tab-content");
    const $toggleToKoreanBtn = $('#toggleToKorean');
    const $toggleToEnglishBtn = $('#toggleToEnglish');
    const $koreanList = $('#koreanInitialList');
    const $englishList = $('#englishInitialList');

    // --------------------------------------------------------
    // 2. 햄버거 메뉴 열기/닫기
    // -------------------      -------------------------------------
    $menuBtn.on("click", () => $overlay.addClass("active"));
    $overlay.on("click", (e) => {
        if (e.target === e.currentTarget) {
            $overlay.removeClass("active");
        }
    });

    // --------------------------------------------------------
    // 3. 상단 탭 전환 (카테고리/브랜드)
    // --------------------------------------------------------
    $tabLinks.on("click", function (e) {
        e.preventDefault();
        const targetId = $(this).data("tab");

        $tabLinks.removeClass("active");
        $(this).addClass("active");

        $tabContents.removeClass("active");
        $(`#${targetId}`).addClass("active");

        // 탭 전환 시 기본 카테고리로 초기화
        if (targetId === 'product') {
            // 상품 탭: "상의" 선택
            $('#product .category-list a[data-target="tops"]').trigger('click');
        } else if (targetId === 'brand') {
            // 브랜드 탭: "전체" 선택
            $('#brand .category-list a[data-id="all"]').trigger('click');
        }
    });

    // --------------------------------------------------------
    // 4. 상품(Product) 탭: 왼쪽 목록 클릭 시 우측 상세 섹션으로 스크롤 및 API 호출
    // --------------------------------------------------------
    $("#product .category-list").on("click", ".list-item-link", function (e) {
        e.preventDefault();
        const targetSectionId = $(this).data("target");
        const categoryId = $(this).data("category-id");

        $("#product .list-item-link").removeClass("active");
        $(this).addClass("active");

        // 중분류 카테고리 데이터 로드
        if (categoryId) {
            console.log("카테고리 클릭됨 - categoryId:", categoryId);
            loadSubCategories(categoryId);
        }

        // "상의" 클릭 시 맨 위로 스크롤, 다른 카테고리는 해당 섹션으로 스크롤
        if (targetSectionId === "tops") {
            // 상의는 카테고리 상세 영역의 맨 위로 스크롤
            const $categoryDetails = $('.category-details');
            if ($categoryDetails.length) {
                $categoryDetails[0].scrollTo({top: 0, behavior: "smooth"});
            }
        } else {
            // 다른 카테고리는 해당 섹션으로 스크롤
            const $targetSection = $(`#${targetSectionId}`);
            if ($targetSection.length) {
                $targetSection[0].scrollIntoView({behavior: "smooth", block: "start"});
            }
        }
    });

    // --------------------------------------------------------
    // 5. 왼쪽 브랜드 카테고리 클릭 이벤트
    // --------------------------------------------------------
    $("#brand .category-list").on("click", ".list-item-link", function (e) {
        e.preventDefault();
        const categoryId = $(this).data("id");

        // 모든 링크에서 active 클래스 제거 후 현재 클릭된 링크에만 추가
        $("#brand .category-list .list-item-link").removeClass("active");
        $(this).addClass("active");

        // 해당 카테고리 ID로 브랜드 목록 필터링
        filterAndRenderBrands(categoryId);

        // 초성 목록 '인기'로 초기화
        $('.initial-list a').removeClass('active');
        $('.initial-list.active a:first-child').addClass('active');
    });

    // --------------------------------------------------------
    // 6. 초성/ABC순 목록 전환 버튼 클릭 이벤트
    // --------------------------------------------------------
    function handleToggleClick(e) {
        e.preventDefault();
        const isEnglishTarget = $(this).data('target') === 'english';

        $englishList.toggleClass('active', isEnglishTarget);
        $koreanList.toggleClass('active', !isEnglishTarget);

        $toggleToEnglishBtn.toggleClass('active', !isEnglishTarget);
        $toggleToKoreanBtn.toggleClass('active', isEnglishTarget);

        // 목록 전환 후, 선택 상태를 '인기'로 초기화
        $('.initial-list a').removeClass('active');
        $('.initial-list.active a:first-child').addClass('active');
    }

    $toggleToKoreanBtn.on('click', handleToggleClick);
    $toggleToEnglishBtn.on('click', handleToggleClick);

    // --------------------------------------------------------
    // 7. 초성 항목 (인기, ㄱ, A...) 클릭 이벤트
    // --------------------------------------------------------
    $(".initial-list").on("click", "a", function (e) {
        e.preventDefault();
        $(this).siblings().removeClass("active");
        $(this).addClass("active");

        const initialValue = $(this).data("initial") || $(this).text().trim();

        // 초성에 따른 브랜드 목록 필터링
        if (initialValue === "인기" || initialValue === "Popular") {
            filterAndRenderBrands(currentCategoryId, "popular");
        } else {
            filterAndRenderBrands(currentCategoryId, initialValue);
        }
    });

    // --------------------------------------------------------
    // 8. 초기 활성화 설정
    // --------------------------------------------------------
    // 초기 로드 시 모든 대분류의 중분류 카테고리를 로드하는 함수
    function loadAllSubCategories() {
        const parentCategoryIds = [1, 2, 3, 4, 5, 6, 7]; // 상의, 아우터, 바지, 원피스/스커트, 가방, 패션소품, 속옷/홈웨어

        parentCategoryIds.forEach(categoryId => {
            loadSubCategories(categoryId);
        });
    }

    function initialize() {
        // 초기 로드 시 모든 중분류 카테고리 로드
        loadAllSubCategories();

        // 상품 카테고리 "상의"를 활성화 (API 호출은 이미 위에서 함)
        $('#product .category-list a[data-target="tops"]').addClass('active');

        // 브랜드 관련 초기 설정은 별도로 처리 (브랜드 탭이 비활성 상태이므로)
        $('#koreanInitialList a:first-child').addClass('active');
        $('#englishInitialList a:first-child').addClass('active');
        $toggleToKoreanBtn.addClass('active');
        $toggleToEnglishBtn.removeClass('active');
    }

    // --------------------------------------------------------
    // 9. 스크롤 시 카테고리 탭 자동 변경 (Intersection Observer)
    // --------------------------------------------------------
    function setupScrollSpy() {
        const sections = document.querySelectorAll('#product .detail-section');
        const navLinks = document.querySelectorAll('#product .list-item-link');

        if (sections.length === 0) return;

        const observerOptions = {
            root: document.querySelector('.category-details'),
            rootMargin: '-20% 0px -60% 0px',
            threshold: 0.1
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const targetId = entry.target.id;

                    // 모든 카테고리 링크에서 active 클래스 제거
                    navLinks.forEach(link => link.classList.remove('active'));

                    // 해당 섹션에 맞는 카테고리 링크에 active 클래스 추가
                    const correspondingLink = document.querySelector(`#product .list-item-link[data-target="${targetId}"]`);
                    if (correspondingLink) {
                        correspondingLink.classList.add('active');
                    }
                }
            });
        }, observerOptions);

        // 모든 섹션을 관찰 대상으로 등록
        sections.forEach(section => {
            observer.observe(section);
        });
    }

    initialize();
    setupScrollSpy();
});
