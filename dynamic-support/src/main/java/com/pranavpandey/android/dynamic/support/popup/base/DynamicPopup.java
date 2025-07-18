/*
 * Copyright 2018-2024 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.popup.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.transition.TransitionManager;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;
import com.pranavpandey.android.dynamic.util.DynamicWindowUtils;
import com.pranavpandey.android.dynamic.util.product.DynamicFlavor;
import com.pranavpandey.android.dynamic.util.product.DynamicProductFlavor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Base {@link PopupWindow} to provide the basic functionality to its descendants.
 * <p>Extend this class to create popup windows according to the requirements.
 */
public abstract class DynamicPopup implements DynamicProductFlavor {

    /**
     * Interface to hold the view types supported by the popup.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

        /**
         * Constant for default view type.
         */
        int NONE = -1;

        /**
         * Constant for list view type.
         */
        int LIST = 0;

        /**
         * Constant for grid view type.
         */
        int GRID = 1;

        /**
         * Constant for the default view type.
         */
        int DEFAULT = GRID;
    }

    /**
     * View root to add scroll indicators if the content can be scrolled.
     */
    private View mViewRoot;

    /**
     * Anchor view used by this popup.
     */
    protected View mAnchor;

    /**
     * View type used by this popup.
     */
    protected @Type int mViewType;

    /**
     * Color for the popup window background.
     */
    protected Integer mPopupWindowColor;

    /**
     * Popup window displayed by this class.
     */
    private PopupWindow mPopupWindow;

    /**
     * Get the anchor view to display the popup.
     *
     * @return The anchor view to display the popup.
     */
    public @NonNull View getAnchor() {
        return mAnchor;
    }

    /**
     * Set the anchor view to display the popup.
     *
     * @param anchor The anchor view to be set.
     */
    public void setAnchor(@NonNull View anchor) {
        this.mAnchor = anchor;
    }

    /**
     * Returns the header view for the popup.
     * <p>Default is {@code null} to hide the header.
     *
     * @return The header view for the popup.
     */
    protected @Nullable View getHeaderView() {
        return null;
    }

    /**
     * This method will be called to return the content view for the popup.
     *
     * @return The content view for the popup.
     */
    protected abstract @Nullable View getView();

    /**
     * Returns the footer view for the popup.
     * <p>Default is {@code null} to hide the footer.
     *
     * @return The footer view for the popup.
     */
    protected @Nullable View getFooterView() {
        return null;
    }

    /**
     * Returns the popup window displayed by this class.
     *
     * @return The popup window displayed by this class.
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * Returns the root view for the popup.
     *
     * @return The view root to add scroll indicators if the content can be scrolled.
     */
    public @Nullable View getViewRoot() {
        return mViewRoot;
    }

    /**
     * Set the view root for this the popup.
     *
     * @param viewRoot The view root to be set.
     */
    public void setViewRoot(@Nullable View viewRoot) {
        this.mViewRoot = viewRoot;
    }

    /**
     * Returns the view type for the popup.
     *
     * @return The view type used by the popup.
     */
    public @Type int getViewType() {
        return mViewType;
    }

    /**
     * Set the view type used by the popup.
     *
     * @param viewType The view type to be set.
     */
    public void setViewType(@Type int viewType) {
        this.mViewType = viewType;
    }

    /**
     * Returns the popup window color.
     *
     * @return The popup window color used by the popup.
     */
    public @Nullable Integer getPopupWindowColor() {
        return mPopupWindowColor;
    }

    /**
     * Set the popup window color.
     *
     * @param popupWindowColor The popup window color to be set.
     */
    public void setPopupWindowColor(@ColorInt int popupWindowColor) {
        this.mPopupWindowColor = popupWindowColor;
    }

    /**
     * Build this popup and make it ready to show.
     * <p>Please call {@link #show()} method to show the popup.
     *
     * @return The popup after building it according to the supplied parameters.
     */
    protected abstract @NonNull DynamicPopup build();

    /**
     * Returns the window animation style for the popup.
     *
     * @return The window animation style for the popup.
     */
    protected @StyleRes int getWindowAnimationStyle() {
        return DynamicMotion.getInstance().isMotion()
                ? DynamicResourceUtils.getWindowPopupAnimation() : 0;
    }

    /**
     * Returns the maximum width for the popup.
     *
     * @return The maximum width for the popup.
     */
    protected int getMaxWidth() {
        return (int) getAnchor().getContext().getResources()
                .getDimension(R.dimen.ads_popup_max_width);
    }

    /**
     * The offset to adjust the size of the window if enough space is not available.
     *
     * @return The offset to adjust the size of the window if enough space is not available.
     */
    protected int getSizeOffset() {
        return (int) getAnchor().getContext().getResources()
                .getDimension(R.dimen.ads_popup_offset);
    }

    /**
     * The offset to adjust the location of the window on the x-axis.
     *
     * @return The offset to adjust the location of the window on the x-axis.
     */
    protected int getLocationOffsetX() {
        return (int) getAnchor().getContext().getResources()
                .getDimension(R.dimen.ads_popup_offset_x);
    }

    /**
     * The offset to adjust the location of the window on the y-axis.
     *
     * @return The offset to adjust the location of the window on the y-axis.
     */
    protected int getLocationOffsetY() {
        return (int) getAnchor().getContext().getResources()
                .getDimension(R.dimen.ads_popup_offset_y);
    }

    /**
     * This method will be called just before showing this popup.
     *
     * @param popupWindow The popup window to be displayed by this popup.
     * @param content The content to be displayed by this popup.
     * @param backgroundColor The background color of this popup.
     */
    protected void onCustomisePopup(@NonNull PopupWindow popupWindow,
            @NonNull View content, @ColorInt int backgroundColor) { }

    /**
     * Build and show {@link PopupWindow} according to the supplied parameters.
     */
    @SuppressLint("PrivateResource")
    public void show() {
        View view = LayoutInflater.from(getAnchor().getContext()).inflate(
                R.layout.ads_popup, (ViewGroup) getAnchor().getRootView(), false);
        ViewGroup card = view.findViewById(R.id.ads_popup_card);
        ViewGroup layout = view.findViewById(R.id.ads_popup_content_layout);
        ViewGroup header = view.findViewById(R.id.ads_popup_header);
        ViewGroup content = view.findViewById(R.id.ads_popup_content);
        ViewGroup footer = view.findViewById(R.id.ads_popup_footer);
        View indicatorUp = view.findViewById(R.id.ads_popup_scroll_indicator_up);
        View indicatorDown = view.findViewById(R.id.ads_popup_scroll_indicator_down);
        @ColorInt int backgroundColor = DynamicTheme.getInstance().get().getSurfaceColor();

        if (mPopupWindowColor != null) {
            Dynamic.setColor(card, mPopupWindowColor);
        }

        if (card instanceof CardView) {
            backgroundColor = Dynamic.getColor(card,
                    ((CardView) card).getCardBackgroundColor().getDefaultColor());
            Dynamic.setContrastWithColor(indicatorUp, backgroundColor);
            Dynamic.setContrastWithColor(indicatorDown, backgroundColor);
        }

        if (getHeaderView() != null) {
            DynamicViewUtils.addView(header, getHeaderView(), true);
        } else {
            Dynamic.setVisibility(header, View.GONE);
        }

        if (getFooterView() != null) {
            DynamicViewUtils.addView(footer, getFooterView(), true);
        } else {
            Dynamic.setVisibility(footer, View.GONE);
        }

        if (getView() != null) {
            DynamicViewUtils.addView(content, getView(), true);

            if (mViewRoot != null) {
                final int indicators = (getHeaderView() != null
                        ? ViewCompat.SCROLL_INDICATOR_TOP : 0)
                        | (getFooterView() != null
                        ? ViewCompat.SCROLL_INDICATOR_BOTTOM : 0);
                final int mask = ViewCompat.SCROLL_INDICATOR_TOP
                        | ViewCompat.SCROLL_INDICATOR_BOTTOM;

                if ((indicators & ViewCompat.SCROLL_INDICATOR_TOP) == 0) {
                    layout.removeView(indicatorUp);
                    indicatorUp = null;
                }

                if ((indicators & ViewCompat.SCROLL_INDICATOR_BOTTOM) == 0) {
                    layout.removeView(indicatorDown);
                    indicatorDown = null;
                }

                if (indicatorUp != null || indicatorDown != null) {
                    final View top = indicatorUp;
                    final View bottom = indicatorDown;

                    mViewRoot.getViewTreeObserver().addOnScrollChangedListener(
                            new ViewTreeObserver.OnScrollChangedListener() {
                                @Override
                                public void onScrollChanged() {
                                    DynamicViewUtils.manageScrollIndicators(mViewRoot, top, bottom);
                                }
                            });
                    mViewRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            DynamicViewUtils.manageScrollIndicators(mViewRoot, top, bottom);
                        }
                    });
                }
            }
        } else {
            Dynamic.setVisibility(content, View.GONE);
        }

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        PopupWindowCompat.setWindowLayoutType(mPopupWindow,
                WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);
        PopupWindowCompat.setOverlapAnchor(mPopupWindow, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(getWindowAnimationStyle());

        if (getMaxWidth() + getSizeOffset()
                < DynamicWindowUtils.getAppUsableScreenSize(getAnchor().getContext()).x) {
            mPopupWindow.setWidth(getMaxWidth());
        }

        if (getAnchor().getRootView() != null) {
            try {
                TransitionManager.endTransitions((ViewGroup) getAnchor().getRootView());
            } catch (Exception ignored) {
            }
        }

        final int[] screenPos = new int[2];
        getAnchor().getLocationInWindow(screenPos);

        int viewCenterX = screenPos[0];
        int OFFSET_X = getLocationOffsetX();
        final int OFFSET_Y = getLocationOffsetY();

        // Check for RTL language.
        if (DynamicViewUtils.isLayoutRtl(getAnchor())) {
            viewCenterX = viewCenterX + getAnchor().getWidth() - mPopupWindow.getWidth();
            OFFSET_X = -OFFSET_X;
        }

        // Do any further customisations for this popup.
        onCustomisePopup(mPopupWindow, view, backgroundColor);

        // Handle issues with popup not expanding.
        if (DynamicSdkUtils.is24(true)) {
            PopupWindowCompat.showAsDropDown(mPopupWindow, getAnchor(),
                    OFFSET_X, -OFFSET_Y, Gravity.NO_GRAVITY | Gravity.START);
        } else {
            mPopupWindow.showAtLocation(getAnchor(), Gravity.NO_GRAVITY,
                    viewCenterX + OFFSET_X, screenPos[1] - OFFSET_Y);
        }
    }

    /**
     * Try to dismiss the popup window.
     *
     * @see #getPopupWindow()
     * @see PopupWindow#dismiss()
     */
    public void dismiss() {
        if (getPopupWindow() != null) {
            getPopupWindow().dismiss();
        }
    }

    @Override
    public @DynamicFlavor String getProductFlavor() {
        return DynamicTheme.getInstance().getProductFlavor();
    }
}
