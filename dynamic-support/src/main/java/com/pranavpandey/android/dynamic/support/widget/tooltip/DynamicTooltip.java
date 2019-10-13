/*
 * Copyright 2019 Pranav Pandey
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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

/**
 * Event handler used to emulate the behavior of {@link View#setTooltipText(CharSequence)}
 * with icon and custom colors.
 */
@RestrictTo(LIBRARY_GROUP_PREFIX)
public class DynamicTooltip implements View.OnLongClickListener, View.OnHoverListener,
        View.OnAttachStateChangeListener {

    /**
     * Tag for this tooltip handler.
     */
    private static final String TAG = "DynamicTooltip";

    /**
     * Constant for long click hide timeout.
     */
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500;

    /**
     * Constant for hover hide timeout.
     */
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000;

    /**
     * Constant for short hover hide timeout.
     */
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000;

    /**
     * Anchor view for the tooltip popup.
     */
    private final View mAnchor;

    /**
     * Background color for the tooltip popup.
     */
    private final @ColorInt int mBackgroundColor;

    /**
     * Tint color for the tooltip popup.
     */
    private final @ColorInt int mTintColor;

    /**
     * Icon for the tooltip popup.
     */
    private final Drawable mTooltipIcon;

    /**
     * Text for the tooltip popup.
     */
    private final CharSequence mTooltipText;

    /**
     * Hover slop for the tooltip.
     */
    private final int mHoverSlop;

    /**
     * Runnable to show the tooltip popup.
     */
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            show(false /* not from touch*/);
        }
    };

    /**
     * Runnable to hide the tooltip popup.
     */
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Horizontal offset for the tooltip popup.
     */
    private int mAnchorX;

    /**
     * Vertical offset for the tooltip popup.
     */
    private int mAnchorY;

    /**
     * The tooltip popup.
     */
    private DynamicTooltipPopup mPopup;

    /**
     * {@code true} if showing from touch.
     */
    private boolean mFromTouch;

    /**
     * The handler currently scheduled to show a tooltip, triggered by a hover
     * (there can be only one).
     */
    private static DynamicTooltip sPendingHandler;

    /**
     * The handler currently showing a tooltip (there can be only one).
     */
    private static DynamicTooltip sActiveHandler;

    /**
     * Set the tooltip for the view.
     *
     * @param view The view to set the tooltip on.
     * @param backgroundColor The background color for the tooltip.
     * @param tintColor The tint color for the tooltip.
     * @param icon The icon drawable for the tooltip.
     * @param text The text for the tooltip.
     */
    public static void set(@NonNull View view, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Nullable Drawable icon, @Nullable CharSequence text) {
        // The code below is not attempting to update the tooltip text
        // for a pending or currently active tooltip, because it may lead
        // to updating the wrong tooltip in in some rare cases (e.g. when
        // action menu item views are recycled). Instead, the tooltip is
        // canceled/hidden. This might still be the wrong tooltip,
        // but hiding a wrong tooltip is less disruptive UX.
        if (sPendingHandler != null && sPendingHandler.mAnchor == view) {
            setPendingHandler(null);
        }
        if (TextUtils.isEmpty(text)) {
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener(null);
            view.setLongClickable(false);
            view.setOnHoverListener(null);
        } else {
            new DynamicTooltip(view, backgroundColor, tintColor, icon, text);
        }
    }

    /**
     * Set the tooltip for the view.
     *
     * @param view The view to set the tooltip on.
     * @param backgroundColor The background color for the tooltip.
     * @param tintColor The tint color for the tooltip.
     * @param text The text for the tooltip.
     */
    public static void set(@NonNull View view, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Nullable CharSequence text) {
        set(view, backgroundColor, tintColor, null, text);
    }

    private DynamicTooltip(@NonNull View anchor, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Nullable Drawable icon, @Nullable CharSequence text) {
        mAnchor = anchor;
        mBackgroundColor = backgroundColor;
        mTintColor = tintColor;
        mTooltipIcon = icon;
        mTooltipText = text;
        mHoverSlop = ViewConfigurationCompat.getScaledHoverSlop(
                ViewConfiguration.get(mAnchor.getContext()));
        clearAnchorPos();

        mAnchor.setOnLongClickListener(this);
        mAnchor.setOnHoverListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        mAnchorX = v.getWidth() / 2;
        mAnchorY = v.getHeight() / 2;
        show(true /* from touch */);
        return true;
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        if (mPopup != null && mFromTouch) {
            return false;
        }

        AccessibilityManager manager = (AccessibilityManager)
                mAnchor.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (manager != null && manager.isEnabled() && manager.isTouchExplorationEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_MOVE:
                if (mAnchor.isEnabled() && mPopup == null && updateAnchorPos(event)) {
                    setPendingHandler(this);
                }
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                clearAnchorPos();
                hide();
                break;
        }

        return false;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        // no-op.
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        hide();
    }

    /**
     * Show the tooltip.
     *
     * @param fromTouch {@code true} to show from the touch.
     */
    private void show(boolean fromTouch) {
        if (!ViewCompat.isAttachedToWindow(mAnchor)) {
            return;
        }

        setPendingHandler(null);
        if (sActiveHandler != null) {
            sActiveHandler.hide();
        }
        sActiveHandler = this;

        mFromTouch = fromTouch;
        mPopup = new DynamicTooltipPopup(mAnchor.getContext(), mBackgroundColor, mTintColor);
        mPopup.show(mAnchor, mAnchorX, mAnchorY, mFromTouch, mTooltipIcon, mTooltipText);
        // Only listen for attach state change while the popup is being shown.
        mAnchor.addOnAttachStateChangeListener(this);

        final long timeout;
        if (mFromTouch) {
            timeout = LONG_CLICK_HIDE_TIMEOUT_MS;
        } else if ((ViewCompat.getWindowSystemUiVisibility(mAnchor)
                & SYSTEM_UI_FLAG_LOW_PROFILE) == SYSTEM_UI_FLAG_LOW_PROFILE) {
            timeout = HOVER_HIDE_TIMEOUT_SHORT_MS - ViewConfiguration.getLongPressTimeout();
        } else {
            timeout = HOVER_HIDE_TIMEOUT_MS - ViewConfiguration.getLongPressTimeout();
        }

        mAnchor.removeCallbacks(mHideRunnable);
        mAnchor.postDelayed(mHideRunnable, timeout);
    }

    /**
     * Hide the tooltip.
     */
    private void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            if (mPopup != null) {
                mPopup.hide();
                mPopup = null;
                clearAnchorPos();
                mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                Log.e(TAG, "sActiveHandler.mPopup == null");
            }
        }

        if (sPendingHandler == this) {
            setPendingHandler(null);
        }
        mAnchor.removeCallbacks(mHideRunnable);
    }

    /**
     * Set the tooltip pending handler.
     *
     * @param handler The handler to be set.
     */
    private static void setPendingHandler(@Nullable DynamicTooltip handler) {
        if (sPendingHandler != null) {
            sPendingHandler.cancelPendingShow();
        }

        sPendingHandler = handler;
        if (sPendingHandler != null) {
            sPendingHandler.scheduleShow();
        }
    }

    /**
     * Schedule the tooltip.
     */
    private void scheduleShow() {
        mAnchor.postDelayed(mShowRunnable, ViewConfiguration.getLongPressTimeout());
    }

    /**
     * Cancel the pending tooltip.
     */
    private void cancelPendingShow() {
        mAnchor.removeCallbacks(mShowRunnable);
    }

    /**
     * Update the anchor position if it significantly (that is by at least mHoverSlope)
     * different from the previously stored position. Ignoring insignificant changes
     * filters out the jitter which is typical for such input sources as stylus.
     *
     * @return True if the position has been updated.
     */
    private boolean updateAnchorPos(MotionEvent event) {
        final int newAnchorX = (int) event.getX();
        final int newAnchorY = (int) event.getY();
        if (Math.abs(newAnchorX - mAnchorX) <= mHoverSlop
                && Math.abs(newAnchorY - mAnchorY) <= mHoverSlop) {
            return false;
        }

        mAnchorX = newAnchorX;
        mAnchorY = newAnchorY;
        return true;
    }

    /**
     *  Clear the anchor position to ensure that the next change is considered significant.
     */
    private void clearAnchorPos() {
        mAnchorX = Integer.MAX_VALUE;
        mAnchorY = Integer.MAX_VALUE;
    }
}
