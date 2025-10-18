(function (win, doc) {
    'use strict';

    const contextPath = win.appContextPath || '';
    const FALLBACK_IMAGE = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200"><rect width="100%" height="100%" fill="%23f0f0f0"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%23999" font-size="20">No%20Image</text></svg>';
    const overlayContainer = doc.getElementById('searchOverlayContainer');
    const searchTrigger = doc.getElementById('searchBtn');
    const RECENT_STORAGE_KEY = 'musinsaRecentSearches';

    const state = {
        overlayLoaded: false,
        overlayEl: null,
        recentContent: null,
        resultsContent: null,
        resultsBody: null,
        resultsTitle: null,
        backBtn: null,
        view: 'recent',
        currentKeyword: '',
        loading: false,
        searchInput: null,
        popularLists: [],
        recentSection: null,
        brandSection: null,
        popularSection: null,
        recentListEl: null,
        brandListEl: null,
        recentKeywords: []
    };

    function ensureOverlayMarkup() {
        if (!overlayContainer) {
            return Promise.reject(new Error('검색 오버레이 컨테이너를 찾을 수 없습니다.'));
        }

        if (state.overlayLoaded && state.overlayEl) {
            return Promise.resolve(state.overlayEl);
        }

        ensureStyles();
        const url = contextPath + '/search/overlay?fragment=true';
        return fetch(url, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'include'
        })
            .then(function (response) {
                if (!response.ok) {
                    throw new Error('검색 오버레이 마크업을 불러오지 못했습니다.');
                }
                return response.text();
            })
            .then(function (html) {
                overlayContainer.innerHTML = html;
                overlayContainer.style.display = 'none';

                state.overlayEl = overlayContainer.querySelector('#searchOverlay');
                state.recentContent = overlayContainer.querySelector('#recentOverlayContent');
                state.resultsContent = overlayContainer.querySelector('#searchResultsContent');
                state.resultsBody = overlayContainer.querySelector('#searchResultsBody');
                state.resultsTitle = overlayContainer.querySelector('#searchResultsTitle');
                state.backBtn = overlayContainer.querySelector('#searchResultsBackBtn');
                state.searchInput = overlayContainer.querySelector('#overlaySearchInput');
                state.popularLists = Array.from(overlayContainer.querySelectorAll('.ranked-list'));
                state.recentSection = overlayContainer.querySelector('#recentSearchSection');
                state.brandSection = overlayContainer.querySelector('#recentBrandSection');
                state.popularSection = overlayContainer.querySelector('#popularKeywordSection');
                state.recentListEl = overlayContainer.querySelector('#recentSearchTags');
                state.brandListEl = overlayContainer.querySelector('#recentBrandTags');

                bindOverlayEvents();
                state.overlayLoaded = true;
                switchToRecentView();
                return state.overlayEl;
            });
    }

    function ensureStyles() {
        const head = doc.head || doc.getElementsByTagName('head')[0];
        if (!head) {
            return;
        }
        if (doc.getElementById('recentSearchesStyles')) {
            return;
        }
        const link = doc.createElement('link');
        link.id = 'recentSearchesStyles';
        link.rel = 'stylesheet';
        link.href = contextPath + '/resources/css/recentSearches.css';
        head.appendChild(link);
    }

    function bindOverlayEvents() {
        if (!state.overlayEl) {
            return;
        }

        const closeBtn = state.overlayEl.querySelector('#closeOverlayBtn');
        const submitBtn = state.overlayEl.querySelector('#searchSubmitBtn');
        const searchForm = state.overlayEl.querySelector('form');

        if (closeBtn) {
            closeBtn.addEventListener('click', closeOverlay);
        }

        if (submitBtn) {
            submitBtn.addEventListener('click', handleSearchSubmit);
        }

        if (searchForm) {
            searchForm.addEventListener('submit', function (event) {
                event.preventDefault();
                handleSearchSubmit(event);
            });
        }

        if (state.searchInput) {
            state.searchInput.addEventListener('keydown', function (event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    executeSearch();
                }
            });
        }

        if (state.backBtn) {
            state.backBtn.addEventListener('click', function () {
                switchToRecentView();
            });
        }

        state.overlayEl.addEventListener('click', function (event) {
            if (event.target === state.overlayEl) {
                closeOverlay();
            }
        });

        state.overlayEl.addEventListener('click', function (event) {
            const target = event.target;

            if (target.matches('.search-tag') || (target.closest && target.closest('.search-tag'))) {
                const tag = target.matches('.search-tag') ? target : target.closest('.search-tag');
                if (tag) {
                    const textEl = tag.querySelector('span');
                    if (textEl) {
                        setInputValue(textEl.textContent.trim());
                        executeSearch();
                    }
                }
                return;
            }

            if (target.matches('.brand-tag') || (target.closest && target.closest('.brand-tag'))) {
                const brandEl = target.matches('.brand-tag') ? target : target.closest('.brand-tag');
                if (brandEl) {
                    const textEl = brandEl.querySelector('span');
                    if (textEl) {
                        setInputValue(textEl.textContent.trim());
                        executeSearch();
                    }
                }
                return;
            }

            if (target.matches('.delete-tag-btn')) {
                event.preventDefault();
                event.stopPropagation();
                const tag = target.closest('.search-tag');
                if (tag) {
                    const textEl = tag.querySelector('span');
                    if (textEl) {
                        removeRecentKeyword(textEl.textContent.trim());
                    }
                }
                return;
            }

            if (target.matches('.ranked-list li') || (target.closest && target.closest('.ranked-list li'))) {
                const item = target.matches('.ranked-list li') ? target : target.closest('.ranked-list li');
                if (item) {
                    const termEl = item.querySelector('.term');
                    if (termEl) {
                        setInputValue(termEl.textContent.trim());
                        executeSearch();
                    }
                }
            }
        });

        const deleteAllRecentBtn = state.overlayEl.querySelector('#deleteAllRecentBtn');
        if (deleteAllRecentBtn) {
            deleteAllRecentBtn.addEventListener('click', handleDeleteAllRecent);
        }

        const deleteAllBrandBtn = state.overlayEl.querySelector('#deleteAllRecentBrandsBtn');
        if (deleteAllBrandBtn) {
            deleteAllBrandBtn.addEventListener('click', handleDeleteAllBrands);
        }
    }

    function openOverlay(event) {
        if (event && isModifierClick(event)) {
            return;
        }
        if (event) {
            event.preventDefault();
        }

        ensureOverlayMarkup()
            .then(function () {
                if (!state.overlayEl) {
                    return;
                }
                overlayContainer.style.display = 'flex';
                state.overlayEl.style.display = 'flex';
                doc.body.style.overflow = 'hidden';
                switchToRecentView();
                loadSearchData();
                if (state.searchInput) {
                    state.searchInput.focus({preventScroll: true});
                }
            })
            .catch(function (error) {
                console.error(error);
                win.alert('검색창을 여는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
            });
    }

    function closeOverlay() {
        if (state.overlayEl) {
            state.overlayEl.style.display = 'none';
        }
        overlayContainer.style.display = 'none';
        doc.body.style.overflow = 'auto';
        switchToRecentView();
        if (state.searchInput) {
            state.searchInput.value = '';
        }
        state.currentKeyword = '';
    }

    function loadSearchData() {
        loadRecentKeywords();
        loadRecentBrands();
        loadPopularKeywords();
    }

    function loadRecentKeywords() {
        if (isLoggedIn()) {
            const url = new URL(contextPath + '/api/v1/search/recent', win.location.origin);

            fetch(url, {credentials: 'include'})
                .then(function (response) {
                    if (!response.ok) {
                        throw new Error('status ' + response.status);
                    }
                    return handleJsonResponse(response);
                })
                .then(function (data) {
                    const list = Array.isArray(data) ? data : (data && data.data ? data.data : []);
                    renderRecentKeywords(list);
                })
                .catch(function () {
                    // 서버 응답이 없으면 로컬 저장소 기반으로라도 보여준다.
                    renderRecentKeywords(getLocalRecentKeywords());
                });
        } else {
            renderRecentKeywords(getLocalRecentKeywords());
        }
    }

    function renderRecentKeywords(items) {
        if (!state.recentListEl || !state.recentSection) {
            return;
        }
        state.recentListEl.innerHTML = '';

        const normalized = (items || []).map(function (item) {
            if (typeof item === 'string') {
                return item;
            }
            if (item && typeof item === 'object') {
                return item.searchText || item.keyword || '';
            }
            return '';
        }).filter(Boolean);

        if (normalized.length === 0) {
            state.recentSection.classList.add('is-hidden');
            state.recentKeywords = [];
            return;
        }

        state.recentSection.classList.remove('is-hidden');
        state.recentKeywords = normalized;
        normalized.forEach(function (keyword) {
            const li = doc.createElement('li');
            li.className = 'search-tag';
            li.innerHTML = '<span>' + escapeHtml(keyword) + '</span><button class="delete-tag-btn" type="button" aria-label="검색어 삭제">×</button>';
            state.recentListEl.appendChild(li);
        });
    }

    function loadRecentBrands() {
        if (!state.brandListEl || !state.brandSection) {
            return;
        }
        state.brandListEl.innerHTML = '<div class="loading-text">로딩 중...</div>';

        fetch(contextPath + '/api/v1/search/recent-keyword', {
            credentials: 'include'
        })
            .then(handleJsonResponse)
            .then(function (data) {
                const list = Array.isArray(data) ? data : (data && data.data ? data.data : []);
                renderRecentBrands(list);
            })
            .catch(function () {
                renderRecentBrands([]);
            });
    }

    function renderRecentBrands(items) {
        if (!state.brandListEl || !state.brandSection) {
            return;
        }

        state.brandListEl.innerHTML = '';
        const normalized = (items || []).map(function (item) {
            if (typeof item === 'string') {
                return {name: item, image: null};
            }
            if (item && typeof item === 'object') {
                return {
                    name: item.brandName || item.brandEngName || '',
                    image: item.brandImageUrl || item.brandImage || ''
                };
            }
            return {name: '', image: ''};
        }).filter(function (item) {
            return item.name;
        });

        if (normalized.length === 0) {
            state.brandSection.classList.add('is-hidden');
            return;
        }

        state.brandSection.classList.remove('is-hidden');
        normalized.forEach(function (brand) {
            const li = doc.createElement('li');
            li.className = 'brand-tag';
            li.innerHTML = '<span>' + escapeHtml(brand.name) + '</span>';
            state.brandListEl.appendChild(li);
        });
    }

    function loadPopularKeywords() {
        fetch(contextPath + '/api/v1/search/popular-keyword', {
            credentials: 'include'
        })
            .then(handleJsonResponse)
            .then(function (data) {
                const timestamp = formatTimestamp(data && data.basedOnTime);
                const results = data && Array.isArray(data.results) ? data.results : [];
                renderPopularKeywords(timestamp, results);
            })
            .catch(function () {
                renderPopularKeywords(null, []);
            });
    }

    function renderPopularKeywords(timestamp, items) {
        const listA = state.popularLists[0];
        const listB = state.popularLists[1];
        const timestampEl = state.overlayEl ? state.overlayEl.querySelector('#popularTimestamp') : null;

        if (timestampEl) {
            timestampEl.textContent = timestamp ? timestamp + ' 기준' : '데이터 없음';
        }

        if (!listA || !listB) {
            return;
        }

        listA.innerHTML = '';
        listB.innerHTML = '';

        const normalized = (items || []).map(function (item, idx) {
            return {
                rank: item.rank || (idx + 1),
                keyword: item.keyword || ''
            };
        }).filter(function (item) {
            return item.keyword;
        });

        if (normalized.length === 0) {
            listA.innerHTML = '<li><div class="no-results">인기 검색어를 불러올 수 없습니다.</div></li>';
            listB.innerHTML = '';
            return;
        }

        normalized.forEach(function (item, index) {
            const li = doc.createElement('li');
            li.innerHTML = '<span class="rank">' + item.rank + '</span>' +
                '<span class="term">' + escapeHtml(item.keyword) + '</span>' +
                '<span class="status-indicator">-</span>';
            if (index < 5) {
                listA.appendChild(li);
            } else {
                listB.appendChild(li);
            }
        });
    }

    function handleSearchSubmit(event) {
        if (event) {
            event.preventDefault();
        }
        executeSearch();
    }

    function saveSearchLog(keyword) {
        if (!keyword) {
            return;
        }

        // 서버 API는 /api/v1/search/search-logs 에 매핑되어 있으므로 정확한 경로로 호출한다.
        fetch(contextPath + '/api/v1/search/search-logs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ keyword }),
            credentials: 'include'
        }).then(function (response) {
            if (!response.ok) {
                throw new Error('status ' + response.status);
            }
        }).catch(function (err) {
            console.error('검색어 저장 실패:', err);
            // 서버에 저장하지 못하더라도 사용자는 즉시 기록을 확인할 수 있어야 하므로 로컬에 보관한다.
            saveLocalRecentKeyword(keyword);
        });
    }

    function executeSearch() {
        if (!state.searchInput) {
            return;
        }
        const keyword = state.searchInput.value.trim();
        if (!keyword) {
            win.alert('검색어를 입력해주세요.');
            state.searchInput.focus();
            return;
        }

        state.currentKeyword = keyword;
        if (isLoggedIn()) {
            upsertRecentKeyword(keyword); // 서버 응답을 기다리지 않고 즉시 표시
            saveSearchLog(keyword);
        } else {
            saveLocalRecentKeyword(keyword);
        }
        //메인 상품 리스트 페이지로 이동
        win.location.href = contextPath + '/products?keyword=' + encodeURIComponent(keyword);
    }

    /*
        // function fetchSearchResults(keyword) {
    //     ensureOverlayMarkup()
    //         .then(function () {
    //             switchToResultsView();
    //             showResultsLoading();
    //
    //             const url = new URL(contextPath + '/search', win.location.origin);
    //             url.searchParams.set('keyword', keyword);
    //
    //             return fetch(url, { credentials: 'include' });
    //         })
    //         .then(handleJsonResponse)
    //         .then(function (data) {
    //             renderSearchResults(data);
    //         })
    //         .catch(function (error) {
    //             console.error(error);
    //             renderSearchResults(null);
    //         });
    // }
     */


    function renderSearchResults(data) {
        if (!state.resultsBody || !state.resultsTitle) {
            return;
        }

        state.resultsBody.innerHTML = '';

        if (!data || typeof data === 'string') {
            state.resultsTitle.textContent = state.currentKeyword ? '"' + state.currentKeyword + '" 검색 결과' : '검색 결과';
            state.resultsBody.innerHTML = '<div class="no-results">검색 결과가 없습니다.</div>';
            return;
        }

        if (typeof data === 'object' && !('searchKeyword' in data)) {
            state.resultsTitle.textContent = state.currentKeyword ? '"' + state.currentKeyword + '" 검색 결과' : '검색 결과';
            state.resultsBody.innerHTML = '<div class="no-results">검색 결과가 없습니다.</div>';
            return;
        }

        state.resultsTitle.textContent = data.searchKeyword ? '"' + data.searchKeyword + '" 검색 결과' : '검색 결과';

        if (data.brandInfo) {
            state.resultsBody.appendChild(buildBrandSummary(data.brandInfo));
        }

        const products = buildProductGrid(data.products || (data.brandInfo ? data.brandInfo.products : []) || []);
        state.resultsBody.appendChild(products);
    }

    function buildBrandSummary(info) {
        const wrapper = doc.createElement('div');
        wrapper.className = 'brand-summary';

        const image = doc.createElement('img');
        image.alt = info.brandNameKr || info.brandNameEn || '브랜드 이미지';
        image.src = info.brandImage || FALLBACK_IMAGE;

        const meta = doc.createElement('div');
        meta.className = 'brand-info';
        meta.innerHTML = '<span class="brand-name">' + escapeHtml(info.brandNameKr || info.brandNameEn || '브랜드') + '</span>' +
            '<span class="brand-meta">상품 ' + (info.totalCount != null ? info.totalCount : (info.products ? info.products.length : 0)) + '개 · 좋아요 ' + (info.brandLikes != null ? info.brandLikes : 0) + '</span>';

        wrapper.appendChild(image);
        wrapper.appendChild(meta);
        return wrapper;
    }

    function buildProductGrid(list) {
        const grid = doc.createElement('div');
        grid.className = 'product-grid';

        if (!Array.isArray(list) || list.length === 0) {
            grid.innerHTML = '<div class="no-results">일치하는 상품이 없습니다.</div>';
            return grid;
        }

        list.forEach(function (item) {
            const card = doc.createElement('article');
            card.className = 'product-card';

            const img = doc.createElement('img');
            img.src = item.productImage || FALLBACK_IMAGE;
            img.alt = item.productName || '상품 이미지';
            card.appendChild(img);

            const name = doc.createElement('div');
            name.className = 'product-name';
            name.textContent = item.productName || '상품명 미정';
            card.appendChild(name);

            const brand = doc.createElement('div');
            brand.className = 'product-brand';
            brand.textContent = item.brandNameKr || item.brandNameEn || '';
            card.appendChild(brand);

            const price = doc.createElement('div');
            price.className = 'product-price';
            price.textContent = typeof item.price === 'number' ? formatPrice(item.price) + '원' : '';
            card.appendChild(price);

            const meta = doc.createElement('div');
            meta.className = 'product-meta';
            meta.textContent = '좋아요 ' + (item.productLikes != null ? item.productLikes : 0);
            card.appendChild(meta);

            card.addEventListener('click', function () {
                const productId = item.productId;
                if (productId) {
                    win.location.href = contextPath + '/product/productDetail?productId=' + encodeURIComponent(productId);
                }
            });

            grid.appendChild(card);
        });

        return grid;
    }

    function showResultsLoading() {
        if (state.resultsBody) {
            state.resultsBody.innerHTML = '<div class="loading-text">검색 중...</div>';
        }
        if (state.resultsTitle) {
            state.resultsTitle.textContent = state.currentKeyword ? '"' + state.currentKeyword + '" 검색 결과' : '검색 결과';
        }
    }

    function switchToResultsView() {
        if (state.recentContent) {
            state.recentContent.classList.add('is-hidden');
        }
        if (state.resultsContent) {
            state.resultsContent.classList.remove('is-hidden');
        }
        state.view = 'results';
    }

    function switchToRecentView() {
        if (state.resultsContent) {
            state.resultsContent.classList.add('is-hidden');
        }
        if (state.recentContent) {
            state.recentContent.classList.remove('is-hidden');
        }
        if (state.resultsBody) {
            state.resultsBody.innerHTML = '<div class="placeholder-text">검색어를 입력하면 결과가 표시됩니다.</div>';
        }
        if (state.resultsTitle) {
            state.resultsTitle.textContent = '';
        }
        state.view = 'recent';
    }

    function handleDeleteAllRecent() {
        if (!win.confirm('최근 검색어를 모두 삭제하시겠습니까?')) {
            return;
        }

        if (isLoggedIn()) {
            fetch(contextPath + '/api/v1/search/recent/all', {
                method: 'DELETE',
                credentials: 'include'
            }).finally(loadRecentKeywords);
        } else {
            win.localStorage.removeItem(RECENT_STORAGE_KEY);
            loadRecentKeywords();
        }
    }

    function handleDeleteAllBrands() {
        if (!win.confirm('최근 방문 브랜드를 모두 삭제하시겠습니까?')) {
            return;
        }

        fetch(contextPath + '/api/v1/search/recent-keyword/all', {
            method: 'DELETE',
            credentials: 'include'
        }).finally(loadRecentBrands);
    }

    function removeRecentKeyword(keyword) {
        if (!keyword) {
            return;
        }

        if (isLoggedIn()) {
            const formData = new URLSearchParams();
            formData.set('keyword', keyword);
            fetch(contextPath + '/api/v1/search/recent', {
                method: 'DELETE',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData
            }).finally(loadRecentKeywords);
        } else {
            const list = getLocalRecentKeywords().filter(function (item) {
                return item !== keyword;
            });
            saveLocalRecentKeywords(list);
            renderRecentKeywords(list);
        }
    }

    function upsertRecentKeyword(keyword) {
        if (!keyword) {
            return;
        }
        const normalized = keyword.trim();
        if (!normalized) {
            return;
        }

        const existing = Array.isArray(state.recentKeywords) ? state.recentKeywords : [];
        const updated = [normalized].concat(existing.filter(function (item) {
            return item !== normalized;
        })).slice(0, 10);

        state.recentKeywords = updated;
        renderRecentKeywords(updated);
        saveLocalRecentKeywords(updated);
    }

    function saveLocalRecentKeyword(keyword) {
        if (!keyword) {
            return;
        }
        const list = getLocalRecentKeywords().filter(function (item) {
            return item !== keyword;
        });
        list.unshift(keyword);
        const limited = list.slice(0, 10);
        saveLocalRecentKeywords(limited);
        renderRecentKeywords(limited);
    }

    function getLocalRecentKeywords() {
        try {
            const data = win.localStorage.getItem(RECENT_STORAGE_KEY);
            if (!data) {
                return [];
            }
            const parsed = JSON.parse(data);
            return Array.isArray(parsed) ? parsed : [];
        } catch (error) {
            console.error('최근 검색어를 불러오지 못했습니다.', error);
            return [];
        }
    }

    function saveLocalRecentKeywords(list) {
        try {
            win.localStorage.setItem(RECENT_STORAGE_KEY, JSON.stringify(list));
        } catch (error) {
            console.error('최근 검색어를 저장하지 못했습니다.', error);
        }
    }

    function handleJsonResponse(response) {
        if (!response) {
            return Promise.reject(new Error('응답이 없습니다.'));
        }
        const contentType = response.headers.get('Content-Type') || '';
        if (contentType.includes('application/json')) {
            return response.json();
        }
        return response.text();
    }

    function escapeHtml(value) {
        return String(value || '')
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    function formatTimestamp(value) {
        if (!value) {
            return '';
        }
        let date = null;
        try {
            date = new Date(value);
        } catch (e) {
            date = null;
        }
        if (!date || isNaN(date.getTime())) {
            return value;
        }
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hour = String(date.getHours()).padStart(2, '0');
        const minute = String(date.getMinutes()).padStart(2, '0');
        return year + '.' + month + '.' + day + ' ' + hour + ':' + minute;
    }

    function formatPrice(value) {
        if (typeof value !== 'number') {
            return value;
        }
        return value.toLocaleString('ko-KR');
    }

    function isModifierClick(event) {
        return event.metaKey || event.ctrlKey || event.shiftKey || event.altKey || (typeof event.button === 'number' && event.button !== 0);
    }

    function setInputValue(value) {
        if (state.searchInput) {
            state.searchInput.value = value;
            state.searchInput.focus({preventScroll: true});
        }
    }

    function isLoggedIn() {
        return Boolean(win.userIsLoggedIn);
    }

    function getUserId() {
        if (win.musinsaUserId === undefined || win.musinsaUserId === null) {
            return null;
        }
        if (win.musinsaUserId === '' || win.musinsaUserId === 'null' || win.musinsaUserId === 'undefined') {
            return null;
        }
        const parsed = Number(win.musinsaUserId);
        return Number.isNaN(parsed) ? win.musinsaUserId : parsed;
    }

    function handleQueryAutoOpen() {
        const params = new URLSearchParams(win.location.search);
        if (params.get('openSearchOverlay') === 'true') {
            openOverlay();
            params.delete('openSearchOverlay');
            const newQuery = params.toString();
            const newUrl = win.location.pathname + (newQuery ? '?' + newQuery : '') + win.location.hash;
            win.history.replaceState(null, '', newUrl);
        }
    }

    if (searchTrigger) {
        searchTrigger.addEventListener('click', openOverlay);
    }

    win.musinsaSearchOverlay = {
        ensure: ensureOverlayMarkup,
        open: openOverlay,
        close: closeOverlay
    };

    win.musinsaHeader = win.musinsaHeader || {};
    win.musinsaHeader.ensureSearchOverlay = ensureOverlayMarkup;
    win.musinsaHeader.openSearchOverlay = openOverlay;

    if (doc.readyState === 'loading') {
        doc.addEventListener('DOMContentLoaded', handleQueryAutoOpen);
    } else {
        handleQueryAutoOpen();
    }

})(window, document);