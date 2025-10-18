// cart.js (refactored)
(function () {
    "use strict";

    // ===== Constants / Utils =====
    var DISCOUNT_RATE = 0.08;
    var POINT_RATE = 0.047;

    var BASE = (window.__CTX__ && window.__CTX__.base) || "";
    var $doc = $(document);
    var $root = $("#brand-lists");
    var $selectAll = $("#select-all");
    var $removeSelected = $("#btn-remove-selected");
    var $checkout = $("#btn-checkout");
    var $soldoutNote = $("#soldout-note");
    var $cartCount = $("#cart-count");

    function toCurrency(n) {
        n = Number(n) || 0;
        return n.toLocaleString("ko-KR") + "원";
    }

    function escapeHtml(str) {
        return String(str).replace(/[&<>"']/g, function (s) {
            return {"&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"}[s];
        });
    }

    function escapeAttr(str) {
        return String(str).replace(/["&<>]/g, function (s) {
            return {"&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;"}[s];
        });
    }

    function by(arr, key) {
        var m = new Map();
        (arr || []).forEach(function (it) {
            m.set(it[key], it);
        });
        return m;
    }

    // ===== State =====
    /**
     * raw: 서버에서 내려온 원본 배열
     * indexByProductId / indexByCartId: 빠른 조회용 맵
     * groups: {brand: CartItem[]}
     * order: brand 표시 순서
     * selected: Set<userCartId>  ← ★ 선택 기준을 cartId로 통일
     * soldoutExists: Boolean
     */
    var state = {
        raw: [],
        indexByProductId: new Map(),
        indexByCartId: new Map(),
        groups: {},
        order: [],
        selected: new Set(),
        soldoutExists: false,
    };

    // ===== API Layer =====
    var api = {
        fetchCart: function () {
            return $.getJSON(BASE + "/api/v1/carts");
        },
        patchCartItem: function (productId, payload) {
            return $.ajax({
                url: BASE + "/api/v1/carts/" + encodeURIComponent(productId),
                type: "PATCH",
                contentType: "application/json",
                data: JSON.stringify(payload),
            });
        },
        createOrderFromCart: function (cartItemIds) {
            return $.ajax({
                url: BASE + "/orders/orders-page",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({type: "FROM_CART", cartItemIds: cartItemIds}),
            });
        },
        deleteCartItems: function (cartIds) {
            return $.ajax({
                url: BASE + "/api/v1/carts",
                type: "DELETE",
                contentType: "application/json",
                data: JSON.stringify(cartIds),
            });
        },
    };

    // ===== Group / Index =====
    function rebuildIndexes() {
        state.indexByProductId = by(state.raw, "productId");
        state.indexByCartId = by(state.raw, "userCartId");
    }

    function groupByBrand(list) {
        var g = {};
        var order = [];
        (list || []).forEach(function (it) {
            var b = it.productBrand || "기타";
            if (!g[b]) {
                g[b] = [];
                order.push(b);
            }
            g[b].push(it);
        });
        state.groups = g;
        state.order = order;
    }

    function recalc() {
        rebuildIndexes();
        groupByBrand(state.raw);
        syncSelectionsWithExistingRows();
    }

    // 서버 재조회나 데이터 대체 시, 존재하지 않는 선택값 정리
    function syncSelectionsWithExistingRows() {
        var next = new Set();
        state.selected.forEach(function (cid) {
            if (state.indexByCartId.has(cid)) next.add(cid);
        });
        state.selected = next;
    }

    // ===== Render =====
    function tplBrandHeader(brand) {
        return (
            '<div class="brand-row" data-brand="' +
            escapeAttr(brand) +
            '">' +
            '  <label class="checkbox"><input type="checkbox" class="brand-check"></label>' +
            '  <div class="brand-name">' +
            escapeHtml(brand) +
            "</div>" +
            '  <span class="chip badge-free">무료배송</span>' +
            '  <span class="chip badge-fast">빠른배송</span>' +
            '  <div style="margin-left:auto;">' +
            '    <button class="btn-ghost small btn-clear-brand">브랜드삭제</button>' +
            "  </div>" +
            "</div>"
        );
    }

    function tplItemRow(item, brand) {
        var checked = state.selected.has(item.userCartId) ? " checked" : "";
        var img = item.imageUrl || "";
        var name = item.productName || "";
        var opt = item.optionName || "";
        var qty = Number(item.quantity) || 0;
        var price = Number(item.totalPrice) || 0;
        var productId = Number(item.productId);

        return (
            '<div class="item" ' +
            'data-id="' + productId + '" ' +
            'data-cart-id="' + Number(item.userCartId) + '" ' +
            'data-brand="' + escapeAttr(brand) + '">' +
            '  <div class="checkbox"><input type="checkbox" class="row-check"' + checked + "></div>" +
            '  <div class="thumb">' + `<a href="${BASE}/products/${productId}"><img src="${escapeAttr(img)}" alt=""></a>`+ "</div>" +
            "  <div>" +
            '    <div class="name">' + `<a href="${BASE}/products/${productId}">${escapeHtml(name)}</a>` + "</div>" +
            '    <div class="meta">' + escapeHtml(opt) + " / " + qty + "개" + "</div>" +
            '    <div class="price">' + toCurrency(price) + "</div>" +
            "  </div>" +
            '  <div class="x-btn" title="삭제">✕</div>' +
            '  <div class="row-ctrls">' +
            '    <button class="btn-ghost ctrl btn-opt">옵션 변경</button>' +
            "  </div>" +
            "</div>"
        );
    }

    function render() {
        $root.empty();

        if (state.order.length === 0) {
            $root.hide();
            $cartCount.text("전체 0");
            updateTotals();
            syncTopBar();
            return;
        }

        $root.show();
        $cartCount.text("전체 " + state.raw.length);

        var html = [];
        state.order.forEach(function (brand) {
            html.push(tplBrandHeader(brand));
            html.push('<div class="items">');
            (state.groups[brand] || []).forEach(function (it) {
                html.push(tplItemRow(it, brand));
            });
            html.push("</div>");
        });
        $root.html(html.join(""));

        updateTotals();
        syncBrandChecks();
        syncSelectAllCheck();
        syncTopBar();
    }

    // ===== Selection / Sync =====
    function setRowSelected($row, on) {
        var cartId = Number($row.data("cart-id"));
        var $check = $row.find(".row-check");
        $check.prop("checked", on);
        if (on) state.selected.add(cartId);
        else state.selected.delete(cartId);
    }

    function syncBrandChecks() {
        $(".brand-row").each(function () {
            var brand = $(this).data("brand");
            var $checks = $('.item[data-brand="' + brand + '"] .row-check');
            if ($checks.length === 0) {
                $(this).find(".brand-check").prop("checked", false);
                return;
            }
            var checkedCount = $checks.filter(":checked").length;
            $(this).find(".brand-check").prop("checked", checkedCount === $checks.length);
        });
    }

    function syncSelectAllCheck() {
        var $checks = $(".row-check");
        if ($checks.length === 0) {
            $selectAll.prop("checked", false);
            return;
        }
        $selectAll.prop("checked", $checks.filter(":checked").length === $checks.length);
    }

    function syncTopBar() {
        var hasRows = state.raw.length > 0;
        var hasSel = state.selected.size > 0;
        $removeSelected.prop("disabled", !hasSel);
        $checkout.prop("disabled", !hasSel);
        $selectAll.prop("disabled", !hasRows);
        $soldoutNote.toggle(!!state.soldoutExists);
    }

    // ===== Totals =====
    function updateTotals() {
        // 선택된 cartId 기준으로 합산
        var sum = 0;
        state.selected.forEach(function (cid) {
            var it = state.indexByCartId.get(cid);
            if (it) sum += Number(it.totalPrice) || 0;
        });

        var disc = Math.floor(sum * DISCOUNT_RATE);
        var finalPay = sum - disc;

        $("#sum-product").text(toCurrency(sum));
        $("#sum-discount").text("-" + toCurrency(disc));
        $("#sum-final").text(toCurrency(finalPay));
        $("#disc-rate").text(sum > 0 ? " 8%" : "");
        $("#benefit-hint").text("적립혜택 예상 최대 " + toCurrency(Math.floor(finalPay * POINT_RATE)));
    }

    // ===== Remove / Clear =====
    function removeByCartIds(cartIds) {
        if (!cartIds || cartIds.length === 0) return;

        // 서버에 삭제 요청
        api.deleteCartItems(cartIds)
            .done(function() {
                var rmSet = new Set(cartIds.map(Number));
                state.raw = state.raw.filter(function (it) {
                    return !rmSet.has(Number(it.userCartId));
                });

                // 선택도 정리
                rmSet.forEach(function (cid) {
                    state.selected.delete(cid);
                });

                recalc();
                render();

                // 장바구니 뱃지 업데이트
                if (window.updateCartBadge) window.updateCartBadge();
            })
            .fail(function(xhr) {
                console.error("장바구니 삭제 실패:", xhr);
                alert("장바구니 삭제에 실패했습니다.");
            });

    }

    function removeBrand(brand) {
        var ids = [];
        $('.item[data-brand="' + brand + '"]').each(function () {
            ids.push(Number($(this).data("cart-id")));
        });
        removeByCartIds(ids);
    }

    // ===== Option Modal =====
    function openModal() {
        $("#option-modal").addClass("is-open").attr("aria-hidden", "false");
        $("body").css("overflow", "hidden");
    }

    function closeModal() {
        $("#option-modal").removeClass("is-open").attr("aria-hidden", "true");
        $("body").css("overflow", "");
        $("#opt-error").hide().text("");
    }

    function parseSelectedFromOptionName(name) {
        return (name || "")
            .split("/")
            .map(function (s) {
                return s.trim();
            })
            .filter(Boolean);
    }

    function optionTypeIdOf(name) {
        var n = (name || "").toLowerCase();
        if (n === "color" || n === "색상") return 1;
        if (n === "size" || n === "사이즈") return 2;
        if (n === "material" || n === "소재") return 3;
        return 0;
    }

    function renderOptionGroups(groups, selectedMap) {
        var $wrap = $("#opt-groups");
        if (!$wrap.length) return;
        $wrap.empty();

        if (!Array.isArray(groups) || groups.length === 0) return;

        groups.forEach(function (g) {
            if (!g) return;
            var title = g.optionType || "";
            var values = Array.isArray(g.optionValues) ? g.optionValues : [];
            if (values.length === 0) return;

            var $g = $('<div class="opt-group" style="margin-top:14px;"></div>');
            $g.append('<div class="opt-group-title" style="font-weight:700;margin-bottom:8px;">' + escapeHtml(title) + "</div>");

            var $list = $('<div class="opt-list" style="display:flex;flex-wrap:wrap;gap:8px;"></div>');
            values.forEach(function (val) {
                var $btn = $('<button type="button" class="opt-item btn-ghost small"></button>')
                    .text(val)
                    .css({borderRadius: "8px", padding: "6px 10px"});

                if (selectedMap && selectedMap[title] === val) {
                    $btn.addClass("is-selected").css({borderColor: "#111", fontWeight: 600});
                }
                $list.append($btn);
            });

            $g.append($list);
            $wrap.append($g);
        });
    }

    function updateOptionPricePreview() {
        var unit = Number($("#opt-save").data("unitPrice") || 0);
        var qty = Math.max(1, Number($("#opt-qty").val() || 1));
        var newTotal = unit * qty;
        $("#opt-price-preview").text(
            "변경 후 금액 미리보기: " + (newTotal ? newTotal.toLocaleString("ko-KR") + "원" : "-")
        );
    }

    function startOptionUpdate($row) {
        var productId = Number($row.data("id"));
        var brand = String($row.data("brand"));
        var product = state.indexByProductId.get(productId);
        if (!product) {
            console.warn("[optionUpdate] product not found:", productId);
            return;
        }

        var name = $row.find(".name").text().trim();
        var q0 = Math.max(1, Number(product.quantity) || 1);
        var unitPrice =
            typeof product.unitPrice === "number"
                ? product.unitPrice
                : Math.round((Number(product.totalPrice) || 0) / q0);

        $("#opt-name").text(name);
        $("#opt-brand-chip").text(brand);
        $("#opt-qty").val(q0);
        $("#opt-save").data("productId", productId).data("unitPrice", unitPrice);

        // 초기 선택 맵
        var picked = parseSelectedFromOptionName(product.optionName);
        var initialSelected = {};
        (product.optionGroups || []).forEach(function (g) {
            var vals = Array.isArray(g.optionValues) ? g.optionValues : [];
            var hit = picked.find(function (p) {
                return vals.indexOf(p) >= 0;
            });
            if (hit) initialSelected[g.optionType] = hit;
        });

        renderOptionGroups(product.optionGroups || [], initialSelected);
        updateOptionPricePreview();
        openModal();
    }

    function collectModalSelections() {
        var optionGroups = [];
        $("#opt-groups .opt-group").each(function () {
            var title = $(this).find(".opt-group-title").text().trim();
            var sel = $(this).find(".opt-item.is-selected").text().trim();
            if (title && sel) {
                optionGroups.push({
                    optionTypeId: optionTypeIdOf(title),
                    optionValue: sel,
                });
            }
        });
        return optionGroups;
    }

    function tryApplyOptionChange() {
        var productId = Number($("#opt-save").data("productId"));
        var unit = Number($("#opt-save").data("unitPrice") || 0);
        var newQty = Math.max(1, Number($("#opt-qty").val() || 1));
        var optionGroups = collectModalSelections();

        var required = $("#opt-groups .opt-group").length;
        if (required > 0 && optionGroups.length < required) {
            alert("옵션을 모두 선택해주세요");
            return;
        }

        var payload = {optionGroups: optionGroups, quantity: newQty};

        api
            .patchCartItem(productId, payload)
            .done(function (res) {
                // 컨트롤러가 List<ProductsInCartInfoResponse> 반환 or wrapper(result.productList)
                var list = Array.isArray(res)
                    ? res
                    : res && res.result && Array.isArray(res.result.productList)
                        ? res.result.productList
                        : [];

                state.raw = list || [];
                // 기본값: 전체 선택(UX 유지) — 필요 시 정책 변경 가능
                state.selected = new Set(state.raw.map(function (it) {
                    return it.userCartId;
                }));
                recalc();
                render();
                closeModal();
            })
            .fail(function (xhr) {
                var msg = "옵션 변경에 실패했습니다.";
                var data = xhr.responseJSON;
                if (!data && xhr.responseText) {
                    try {
                        data = JSON.parse(xhr.responseText);
                    } catch (e) {
                    }
                }
                if (data) msg = data.detail || data.message || data.error_description || data.error || msg;
                else if (xhr.responseText) msg = xhr.responseText || msg;

                msg = String(msg).replace(/^[A-Z_]+\d+/, "").trim();
                $("#opt-error").text(msg).show();
                $("#opt-qty").focus();
            });
    }

    // ===== Events (Delegation) =====
    // 개별 행 체크
    $root.on("change", ".row-check", function () {
        var $row = $(this).closest(".item");
        setRowSelected($row, this.checked);
        syncBrandChecks();
        syncSelectAllCheck();
        updateTotals();
        syncTopBar();
    });

    // 행 삭제
    $root.on("click", ".item .x-btn", function () {
        var $row = $(this).closest(".item");
        var cartId = Number($row.data("cart-id"));
        removeByCartIds([cartId]);
    });

    // 브랜드 체크
    $root.on("change", ".brand-check", function () {
        var $brand = $(this).closest(".brand-row");
        var brand = String($brand.data("brand"));
        var on = this.checked;

        var $rows = $('.item[data-brand="' + brand + '"]');
        $rows.each(function () {
            setRowSelected($(this), on);
            $(this).find(".row-check").prop("checked", on);
        });

        syncBrandChecks();
        syncSelectAllCheck();
        updateTotals();
        syncTopBar();
    });

    // 브랜드 삭제
    $root.on("click", ".btn-clear-brand", function () {
        var brand = String($(this).closest(".brand-row").data("brand"));
        removeBrand(brand);
    });

    // 옵션 변경
    $root.on("click", ".btn-opt", function () {
        var $row = $(this).closest(".item");
        if ($("#option-modal").length === 0) return;
        startOptionUpdate($row);
    });

    // 모달 공통 닫기
    $doc.on("click", "[data-close-modal]", closeModal);
    $("#option-modal").on("click", function (e) {
        if (e.target === this) closeModal();
    });
    $doc.on("keydown", function (e) {
        if (e.key === "Escape") closeModal();
    });

    // 모달 내부
    $("#qty-dec").on("click", function () {
        var v = Math.max(1, Number($("#opt-qty").val() || 1) - 1);
        $("#opt-qty").val(v);
        updateOptionPricePreview();
    });
    $("#qty-inc").on("click", function () {
        var v = Math.max(1, Number($("#opt-qty").val() || 1) + 1);
        $("#opt-qty").val(v);
        updateOptionPricePreview();
    });
    $("#opt-qty").on("input change", updateOptionPricePreview);

    $("#opt-groups").on("click", ".opt-item", function () {
        var $list = $(this).closest(".opt-list");
        $list.find(".opt-item").removeClass("is-selected").css({borderColor: "#ddd", fontWeight: 400});
        $(this).addClass("is-selected").css({borderColor: "#111", fontWeight: 600});
        updateOptionPricePreview();
    });

    $("#opt-save").on("click", tryApplyOptionChange);

    // 상단 전체 선택
    $selectAll.on("change", function () {
        var on = this.checked;
        var $rows = $(".item");
        $rows.each(function () {
            setRowSelected($(this), on);
            $(this).find(".row-check").prop("checked", on);
        });
        syncBrandChecks();
        updateTotals();
        syncTopBar();
    });

    // 선택 삭제
    $removeSelected.on("click", function () {
        if (state.selected.size === 0) return;
        removeByCartIds(Array.from(state.selected));
    });

    // 결제
    $checkout.on("click", function (e) {
        e.preventDefault();
        if (state.selected.size === 0) {
            alert("주문할 상품을 선택하세요.");
            return;
        }
        var ids = Array.from(state.selected);
        api
            .createOrderFromCart(ids)
            .done(function (response) {
                if (response && response.success) {
                    // 장바구니 뱃지 업데이트 (주문 성공 시 장바구니에서 제거됨)
                    if (window.updateCartBadge) window.updateCartBadge();
                    window.location.href = BASE + response.redirectUrl;
                } else {
                    alert((response && response.message) || "주문 처리에 실패했습니다.");
                }
            })
            .fail(function (xhr, status, error) {
                console.error("주문 처리 실패:", error);
                alert("주문 처리 중 오류가 발생했습니다.");
            });
    });

    // ===== Bootstrap =====
    function bootstrap() {
        api
            .fetchCart()
            .done(function (list) {
                state.raw = Array.isArray(list) ? list : [];
                // 기본 정책: 최초 로드 시 전체 선택
                state.selected = new Set(state.raw.map(function (it) {
                    return it.userCartId;
                }));
                recalc();
                render();

                // 서버가 렌더한 disabled 컨트롤 활성화
                $selectAll.prop("disabled", state.raw.length === 0);
                $removeSelected.prop("disabled", state.selected.size === 0);
                $checkout.prop("disabled", state.selected.size === 0);
            })
            .fail(function () {
                alert("장바구니를 불러오지 못했습니다.");
            });
    }

    $(bootstrap);
})();
