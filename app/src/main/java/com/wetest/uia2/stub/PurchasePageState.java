package com.wetest.uia2.stub;

public final class PurchasePageState {
    public static final int VERSION = 1;

    private final int version;
    private final String pageState;
    private final boolean activeWindowAvailable;
    private final boolean reserveVisible;
    private final Bounds reserve;
    private final boolean countdownVisible;
    private final Bounds retryRefresh;
    private final Bounds confirmPurchase;
    private final boolean sessionContainerVisible;
    private final boolean priceContainerVisible;
    private final boolean orderConfirmationVisible;

    public PurchasePageState(
            String pageState,
            boolean activeWindowAvailable,
            boolean reserveVisible,
            Bounds reserve,
            boolean countdownVisible,
            Bounds retryRefresh,
            Bounds confirmPurchase,
            boolean sessionContainerVisible,
            boolean priceContainerVisible,
            boolean orderConfirmationVisible) {
        this.version = VERSION;
        this.pageState = pageState;
        this.activeWindowAvailable = activeWindowAvailable;
        this.reserveVisible = reserveVisible;
        this.reserve = reserve;
        this.countdownVisible = countdownVisible;
        this.retryRefresh = retryRefresh;
        this.confirmPurchase = confirmPurchase;
        this.sessionContainerVisible = sessionContainerVisible;
        this.priceContainerVisible = priceContainerVisible;
        this.orderConfirmationVisible = orderConfirmationVisible;
    }

    public int getVersion() {
        return version;
    }

    public String getPageState() {
        return pageState;
    }

    public boolean isActiveWindowAvailable() {
        return activeWindowAvailable;
    }

    public boolean isReserveVisible() {
        return reserveVisible;
    }

    public Bounds getReserve() {
        return reserve;
    }

    public boolean isCountdownVisible() {
        return countdownVisible;
    }

    public Bounds getRetryRefresh() {
        return retryRefresh;
    }

    public Bounds getConfirmPurchase() {
        return confirmPurchase;
    }

    public boolean isSessionContainerVisible() {
        return sessionContainerVisible;
    }

    public boolean isPriceContainerVisible() {
        return priceContainerVisible;
    }

    public boolean isOrderConfirmationVisible() {
        return orderConfirmationVisible;
    }

    public static final class Bounds {
        private final int left;
        private final int top;
        private final int right;
        private final int bottom;

        public Bounds(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

        public int getRight() {
            return right;
        }

        public int getBottom() {
            return bottom;
        }
    }
}
