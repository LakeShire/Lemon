package com.github.lakeshire.lemonapp.view.pulltofresh;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

public interface PtrUIHandler {

    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    public void onUIReset(EnhancePtrFrameLayout frame);

    /**
     * prepare for loading
     *
     * @param frame
     */
    public void onUIRefreshPrepare(EnhancePtrFrameLayout frame);

    /**
     * perform refreshing UI
     */
    public void onUIRefreshBegin(EnhancePtrFrameLayout frame);

    /**
     * perform UI after refresh
     */
    public void onUIRefreshComplete(EnhancePtrFrameLayout frame);

    public void onUIPositionChange(EnhancePtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
