document.addEventListener('DOMContentLoaded', function () {
    const menuBtn = document.getElementById('menuBtn');
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    const closeSidebar = document.getElementById('closeSidebar');
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    const alphabetButtons = document.querySelectorAll('.alphabet-btn');
    const brandSearchInput = document.getElementById('brandSearchInput');

    if (menuBtn && sidebar && sidebarOverlay) {
        menuBtn.addEventListener('click', openSidebar);

        if (closeSidebar) {
            closeSidebar.addEventListener('click', closeSidebarFunc);
        }

        sidebarOverlay.addEventListener('click', closeSidebarFunc);

        function openSidebar() {
            sidebar.classList.add('open');
            sidebarOverlay.classList.add('show');
            sidebarOverlay.style.display = 'block';
            document.body.style.overflow = 'hidden';
        }

        function closeSidebarFunc() {
            sidebar.classList.remove('open');
            sidebarOverlay.classList.remove('show');
            setTimeout(function () {
                sidebarOverlay.style.display = 'none';
            }, 300);
            document.body.style.overflow = 'auto';
        }

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape' && sidebar.classList.contains('open')) {
                closeSidebarFunc();
            }
        });

        document.addEventListener('click', function (e) {
            if (e.target.closest('.category-item')) {
                const categoryName = e.target.closest('.category-item').querySelector('.category-name').textContent;
                alert('선택된 카테고리: ' + categoryName);
                closeSidebarFunc();
            }

            if (e.target.closest('.brand-item') && !e.target.classList.contains('like-btn')) {
                const brandName = e.target.closest('.brand-item').querySelector('.brand-name').textContent;
                alert('선택된 브랜드: ' + brandName);
                closeSidebarFunc();
            }
        });
    }

    tabButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const tabName = this.getAttribute('data-tab');

            tabButtons.forEach(function (btn) { btn.classList.remove('active'); });
            this.classList.add('active');

            tabContents.forEach(function (content) { content.classList.remove('active'); });
            const target = document.getElementById(tabName + '-tab');
            if (target) {
                target.classList.add('active');
            }
        });
    });

    alphabetButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            alphabetButtons.forEach(function (btn) { btn.classList.remove('active'); });
            this.classList.add('active');
            console.log('선택된 초성:', this.textContent);
        });
    });

    if (brandSearchInput) {
        brandSearchInput.addEventListener('input', function () {
            const searchTerm = this.value.toLowerCase();
            const brandItems = document.querySelectorAll('.brand-item');

            brandItems.forEach(function (item) {
                const nameEl = item.querySelector('.brand-name');
                const nameEnEl = item.querySelector('.brand-name-eng');
                const brandName = nameEl ? nameEl.textContent.toLowerCase() : '';
                const brandNameEng = nameEnEl ? nameEnEl.textContent.toLowerCase() : '';

                if (brandName.includes(searchTerm) || brandNameEng.includes(searchTerm)) {
                    item.style.display = 'flex';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }

    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('like-btn')) {
            e.preventDefault();
            e.stopPropagation();

            if (e.target.textContent === '♡') {
                e.target.textContent = '♥';
                e.target.style.color = '#e74c3c';
            } else {
                e.target.textContent = '♡';
                e.target.style.color = '#ccc';
            }
        }
    });
});

// 상품 카드 호버 효과 제거됨 - 관련 코드 삭제
