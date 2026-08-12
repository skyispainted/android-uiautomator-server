package com.github.uiautomator.stub;

import android.app.UiAutomation;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayDeque;

final class PurchasePageStateScanner {
    private static final String PAGE_WAITING_HOME = "waiting_home";
    private static final String PAGE_PRICE_SELECTION = "price_selection";
    private static final String PAGE_RETRY = "retry";
    private static final String PAGE_RESERVATION = "reservation";
    private static final String PAGE_ORDER_CONFIRMATION = "order_confirmation";
    private static final String PAGE_UNKNOWN = "unknown";

    private static final String RESERVE_BUTTON_ID =
            "cn.damai:id/trade_project_detail_purchase_status_bar_container_fl";
    private static final String COUNTDOWN_LAYOUT_ID =
            "cn.damai:id/project_item_bottom_time_stagory";
    private static final String RETRY_REFRESH_BUTTON_ID =
            "cn.damai:id/state_view_refresh_btn";
    private static final String BUY_BUTTON_ID = "cn.damai:id/btn_buy_view";
    private static final String PERFORM_CONTAINER_ID = "cn.damai:id/layout_perform_view";
    private static final String PRICE_CONTAINER_ID =
            "cn.damai:id/project_detail_perform_price_flowlayout";
    private static final String RESERVATION_CANCEL_ID =
            "cn.damai:id/btn_cancel_reservation";
    private static final String RESERVATION_SESSION_TITLE_ID = "cn.damai:id/tv_perform_name";
    private static final String RESERVATION_SESSION_TITLE_TEXT = "预约想看场次";
    private static final String RESERVATION_PRICE_TITLE_ID = "cn.damai:id/tv_price_name";
    private static final String RESERVATION_PRICE_TITLE_TEXT = "预约想看票档";
    private static final String ORDER_TITLE_ID = "cn.damai:id/order_activity_title";
    private static final String ORDER_TITLE_TEXT = "确认购买";

    private PurchasePageStateScanner() {
    }

    static PurchasePageState scan(UiAutomation uiAutomation) {
        AccessibilityNodeInfo root = uiAutomation.getRootInActiveWindow();
        if (root == null) {
            return new PurchasePageState(
                    PAGE_UNKNOWN, false, false, null, false, null, null, false, false, false);
        }

        ObservedNodes observed = new ObservedNodes();
        ArrayDeque<AccessibilityNodeInfo> pending = new ArrayDeque<>();
        pending.push(root);
        while (!pending.isEmpty()) {
            AccessibilityNodeInfo node = pending.pop();
            try {
                if (!node.isVisibleToUser()) {
                    continue;
                }
                observe(node, observed);
                int childCount = node.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    AccessibilityNodeInfo child = node.getChild(index);
                    if (child != null) {
                        pending.push(child);
                    }
                }
            } finally {
                node.recycle();
            }
        }
        return observed.toPurchasePageState();
    }

    private static void observe(AccessibilityNodeInfo node, ObservedNodes observed) {
        String resourceId = toString(node.getViewIdResourceName());
        if (RESERVE_BUTTON_ID.equals(resourceId)) {
            observed.reserveVisible = true;
            observed.reserve = boundsOf(node);
        } else if (COUNTDOWN_LAYOUT_ID.equals(resourceId)) {
            observed.countdownVisible = true;
        } else if (RETRY_REFRESH_BUTTON_ID.equals(resourceId)) {
            observed.retryRefreshVisible = true;
            observed.retryRefresh = boundsOf(node);
        } else if (BUY_BUTTON_ID.equals(resourceId)) {
            observed.confirmPurchase = boundsOf(node);
        } else if (PERFORM_CONTAINER_ID.equals(resourceId)) {
            observed.sessionContainerVisible = true;
        } else if (PRICE_CONTAINER_ID.equals(resourceId)) {
            observed.priceContainerVisible = true;
        } else if (RESERVATION_CANCEL_ID.equals(resourceId)) {
            observed.reservationCancelVisible = true;
        } else if (RESERVATION_SESSION_TITLE_ID.equals(resourceId)) {
            observed.reservationSessionTitleVisible = RESERVATION_SESSION_TITLE_TEXT.equals(
                    toString(node.getText()));
        } else if (RESERVATION_PRICE_TITLE_ID.equals(resourceId)) {
            observed.reservationPriceTitleVisible = RESERVATION_PRICE_TITLE_TEXT.equals(
                    toString(node.getText()));
        } else if (ORDER_TITLE_ID.equals(resourceId)) {
            observed.orderConfirmationVisible = ORDER_TITLE_TEXT.equals(toString(node.getText()));
        }
    }

    private static PurchasePageState.Bounds boundsOf(AccessibilityNodeInfo node) {
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        if (bounds.isEmpty()) {
            return null;
        }
        return new PurchasePageState.Bounds(
                bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    private static String toString(CharSequence value) {
        return value == null ? "" : value.toString();
    }

    private static final class ObservedNodes {
        boolean reserveVisible;
        PurchasePageState.Bounds reserve;
        boolean countdownVisible;
        boolean retryRefreshVisible;
        PurchasePageState.Bounds retryRefresh;
        PurchasePageState.Bounds confirmPurchase;
        boolean sessionContainerVisible;
        boolean priceContainerVisible;
        boolean reservationCancelVisible;
        boolean reservationSessionTitleVisible;
        boolean reservationPriceTitleVisible;
        boolean orderConfirmationVisible;

        PurchasePageState toPurchasePageState() {
            return new PurchasePageState(
                    pageState(),
                    true,
                    reserveVisible,
                    reserve,
                    countdownVisible,
                    retryRefresh,
                    confirmPurchase,
                    sessionContainerVisible,
                    priceContainerVisible,
                    orderConfirmationVisible);
        }

        private String pageState() {
            if (retryRefreshVisible) {
                return PAGE_RETRY;
            }
            if (reservationCancelVisible
                    && reservationSessionTitleVisible
                    && reservationPriceTitleVisible) {
                return PAGE_RESERVATION;
            }
            if (priceContainerVisible) {
                return PAGE_PRICE_SELECTION;
            }
            if (reserveVisible) {
                return PAGE_WAITING_HOME;
            }
            if (orderConfirmationVisible) {
                return PAGE_ORDER_CONFIRMATION;
            }
            return PAGE_UNKNOWN;
        }
    }
}
