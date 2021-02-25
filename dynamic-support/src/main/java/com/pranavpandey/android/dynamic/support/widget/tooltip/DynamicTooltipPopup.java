/*
 * Copyright 2018-2021 Pranav Pandey
 * Copyright 2017 The Android Open Source Project
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

package com.pranavpandey.android.dynamic.support.widget.tooltip;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils;

/**
 * A popup window displaying a text message aligned to a specified view.
 */
public class DynamicTooltipPopup {

    /**
     * Tag for this popup.
     */
    private static final String TAG = "DynamicTooltipPopup";

    /**
     * Context used by this popup.
     */
    private final Context mContext;

    /**
     * Content view used by this popup.
     */
    private final View mContentView;

    /**
     * Card view used by this popup to provide background.
     */
    private final ViewGroup mCardView;

    /**
     * Image view used by this popup to show icon.
     */
    private final ImageView mIconView;

    /**
     * Text view used by this popup to show text.
     */
    private final TextView mTextView;

    /**
     * Window manager layout params for the popup.
     */
    private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();

    /**
     * Display frame rect for the popup.
     */
    private final Rect mTmpDisplayFrame = new Rect();

    /**
     * Anchor position for this popup.
     */
    private final int[] mTmpAnchorPos = new int[2];

    /**
     * App position for this popup.
     */
    private final int[] mTmpAppPos = new int[2];

    public DynamicTooltipPopup(@NonNull Context context) {
        this(context, DynamicTheme.getInstance().get().getTintBackgroundColor(),
                DynamicTheme.getInstance().get().getBackgroundColor());
    }

    public DynamicTooltipPopup(@NonNull Context context,
            @ColorInt int backgroundColor, @ColorInt int tintColor) {
        mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.ads_hint,
                new LinearLayout(context), false);

        mCardView = mContentView.findViewById(R.id.ads_hint_card);
        mIconView = mContentView.findViewById(R.id.ads_hint_icon);
        mTextView = mContentView.findViewById(R.id.ads_hint_text);

        mCardView.setAlpha(Defaults.ADS_ALPHA_TOAST);
        mIconView.setColorFilter(tintColor);
        mTextView.setTextColor(tintColor);
        Dynamic.setPreventCornerOverlap(mCardView, false);
        Dynamic.setColor(mCardView, backgroundColor);
        Dynamic.setCornerMin(mCardView, mContext.getResources().getDimension(
                R.dimen.ads_tooltip_corner_radius_max));

        mLayoutParams.setTitle(getClass().getSimpleName());
        mLayoutParams.packageName = mContext.getPackageName();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.windowAnimations = R.style.Animation_AppCompat_Tooltip;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    }

    /**
     * Set the popup icon and text.
     *
     * @param icon The icon drawable for the popup.
     * @param text The text for the popup.
     */
    public void set(@Nullable Drawable icon, @Nullable CharSequence text) {
        Dynamic.set(mIconView, icon);
        Dynamic.set(mTextView, text);
    }

    /**
     * Show the popup according to the supplied values.
     *
     * @param anchorView The anchor view to attach the popup.
     * @param anchorX The horizontal offset for the popup.
     * @param anchorY The vertical offset for the popup.
     * @param fromTouch {@code true} if showing the popup from touch.
     * @param icon The icon drawable for the popup.
     * @param text The text for the popup.
     */
    public void show(@NonNull View anchorView, int anchorX, int anchorY,
            boolean fromTouch, @Nullable Drawable icon, @Nullable CharSequence text) {
        if (isShowing()) {
            hide();
        }

        set(icon, text);
        computePosition(anchorView, anchorX, anchorY, fromTouch, mLayoutParams);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            try {
                wm.addView(mContentView, mLayoutParams);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Show the popup according to the supplied values.
     *
     * @param anchorView The anchor view to attach the popup.
     * @param anchorX The horizontal offset for the popup.
     * @param anchorY The vertical offset for the popup.
     * @param fromTouch {@code true} if showing the popup from touch.
     * @param text The text for the popup.
     */
    public void show(@NonNull View anchorView, int anchorX, int anchorY,
            boolean fromTouch, @Nullable CharSequence text) {
        show(anchorView, anchorX, anchorY, fromTouch, null, text);
    }

    /**
     * Hide the popup if it is showing.
     */
    public void hide() {
        if (!isShowing()) {
            return;
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.removeView(mContentView);
        }
    }

    /**
     * Checks whether the popup is showing.
     *
     * @return {@code true} if the popup is showing.
     */
    private boolean isShowing() {
        return mContentView.getParent() != null;
    }

    /**
     * Compute the popup position according to the anchor view.
     *
     * @param anchorView The anchor view to compute the position.
     * @param anchorX The horizontal offset for the popup.
     * @param anchorY The vertical offset for the popup.
     * @param fromTouch {@code true} if showing the popup from touch.
     * @param outParams The current layout params of the popup.
     */
    private void computePosition(@NonNull View anchorView, int anchorX, int anchorY,
            boolean fromTouch, @NonNull WindowManager.LayoutParams outParams) {
        outParams.token = anchorView.getApplicationWindowToken();
        final int tooltipPreciseAnchorThreshold = mContext.getResources().getDimensionPixelOffset(
                R.dimen.ads_tooltip_precise_anchor_threshold);
        final int tooltipPreciseAnchorThresholdVertical =
                mContext.getResources().getDimensionPixelOffset(
                R.dimen.ads_tooltip_precise_anchor_threshold_vertical);

        final int offsetX;
        if (anchorView.getWidth() >= tooltipPreciseAnchorThreshold) {
            // Wide view. Align the tooltip horizontally to the precise X position.
            offsetX = anchorX;
        } else {
            // Otherwise anchor the tooltip to the view center.
            offsetX = anchorView.getWidth() / 2;  // Center on the view horizontally.
        }

        final int offsetBelow;
        final int offsetAbove;
        if (anchorView.getHeight() >= tooltipPreciseAnchorThresholdVertical) {
            // Tall view. Align the tooltip vertically to the precise Y position.
            final int offsetExtra = mContext.getResources().getDimensionPixelOffset(
                    R.dimen.ads_tooltip_precise_anchor_extra_offset);
            offsetBelow = anchorY + offsetExtra;
            offsetAbove = anchorY - offsetExtra;
        } else {
            // Otherwise anchor the tooltip to the view center.
            offsetBelow = anchorView.getHeight();  // Place below the view in most cases.
            offsetAbove = 0;  // Place above the view if the tooltip does not fit below.
        }

        outParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        final int tooltipOffset = mContext.getResources().getDimensionPixelOffset(fromTouch
                ? R.dimen.ads_tooltip_y_offset_touch : R.dimen.ads_tooltip_y_offset_non_touch);

        final View appView = getAppRootView(anchorView);
        if (appView == null) {
            Log.e(TAG, "Cannot find app view");
            return;
        }
        appView.getWindowVisibleDisplayFrame(mTmpDisplayFrame);
        if (mTmpDisplayFrame.left < 0 && mTmpDisplayFrame.top < 0) {
            // No meaningful display frame, the anchor view is probably in a subpanel
            // (such as a popup window). Use the screen frame as a reasonable approximation.
            final Resources res = mContext.getResources();
            final DisplayMetrics metrics = res.getDisplayMetrics();
            mTmpDisplayFrame.set(0, DynamicWindowUtils.getStatusBarSize(mContext),
                    metrics.widthPixels, metrics.heightPixels);
        }
        appView.getLocationOnScreen(mTmpAppPos);

        anchorView.getLocationOnScreen(mTmpAnchorPos);
        mTmpAnchorPos[0] -= mTmpAppPos[0];
        mTmpAnchorPos[1] -= mTmpAppPos[1];
        // mTmpAnchorPos is now relative to the main app window.

        outParams.x = mTmpAnchorPos[0] + offsetX - appView.getWidth() / 2;

        final int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mContentView.measure(spec, spec);
        final int tooltipHeight = mContentView.getMeasuredHeight();

        final int yAbove = mTmpAnchorPos[1] + offsetAbove - tooltipOffset - tooltipHeight;
        final int yBelow = mTmpAnchorPos[1] + offsetBelow + tooltipOffset;
        if (fromTouch) {
            if (yAbove >= 0) {
                outParams.y = yAbove;
            } else {
                outParams.y = yBelow;
            }
        } else {
            if (yBelow + tooltipHeight <= mTmpDisplayFrame.height()) {
                outParams.y = yBelow;
            } else {
                outParams.y = yAbove;
            }
        }
    }

    /**
     * Finds the root view for the anchor view.
     *
     * @param anchorView The anchor view to find the root view.
     *
     * @return The root view for the anchor view.
     */
    private static View getAppRootView(@NonNull View anchorView) {
        View rootView = anchorView.getRootView();
        ViewGroup.LayoutParams lp = rootView.getLayoutParams();

        if (lp instanceof WindowManager.LayoutParams
                && (((WindowManager.LayoutParams) lp).type
                    == WindowManager.LayoutParams.TYPE_APPLICATION)) {
            // This covers regular app windows and Dialog windows.
            return rootView;
        }

        // For non-application window types (such as popup windows) try to find the main app window
        // through the context.
        Context context = anchorView.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context).getWindow().getDecorView();
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }

        // Main app window not found, fall back to the anchor's root view. There is no guarantee
        // that the tooltip position will be computed correctly.
        return rootView;
    }
}
