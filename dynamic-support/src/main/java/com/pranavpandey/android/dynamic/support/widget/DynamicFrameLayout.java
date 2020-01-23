/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A FrameLayout to apply background color according to the supplied parameters.
 */
public class DynamicFrameLayout extends FrameLayout implements DynamicWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    private @ColorInt int mColor;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The background aware functionality to change this view color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p><p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #mContrastWithColor
     */
    private @Theme.BackgroundAware int mBackgroundAware;

    /**
     * Scaled touch slop used by the view configuration.
     */
    private int mTouchSlop;

    /**
     * Initial horizontal position.
     */
    private float mInitialX;

    /**
     * Initial vertical position.
     */
    private float mInitialY;

    public DynamicFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicFrameLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicTheme);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_colorType,
                    Theme.ColorType.NONE);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicTheme_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicTheme_ads_contrastWithColor,
                    WidgetDefaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicTheme_ads_backgroundAware,
                    WidgetDefaults.getBackgroundAware());
        } finally {
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initialize();
    }

    @Override
    public void initialize() {
        if (mColorType != Theme.ColorType.NONE
                && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor();
    }

    @Override
    public @Theme.ColorType int getColorType() {
        return mColorType;
    }

    @Override
    public void setColorType(@Theme.ColorType int colorType) {
        this.mColorType = colorType;

        initialize();
    }

    @Override
    public @Theme.ColorType int getContrastWithColorType() {
        return mContrastWithColorType;
    }

    @Override
    public void setContrastWithColorType(@Theme.ColorType int contrastWithColorType) {
        this.mContrastWithColorType = contrastWithColorType;

        initialize();
    }

    @Override
    public @ColorInt int getColor() {
        return mColor;
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        setColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setColor();
    }

    @Override
    public void setBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        this.mBackgroundAware = backgroundAware;

        setColor();
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return mBackgroundAware;
    }

    @Override
    public boolean isBackgroundAware() {
        return DynamicTheme.getInstance().resolveBackgroundAware(
                mBackgroundAware) != Theme.BackgroundAware.DISABLE;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? WidgetDefaults.ADS_ALPHA_ENABLED : WidgetDefaults.ADS_ALPHA_DISABLED);
    }

    @Override
    public void setColor() {
        if (mColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (isBackgroundAware() && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicDrawableUtils.setBackground(this, new ColorDrawable(mColor));
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        handleInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Checks whether a child can be scrolled.
     *
     * @param orientation The current orientation of the view.
     * @param delta The delta to check the scrolling.
     *
     * @return Returns {@code true} if a child can be scrolled.
     */
    private boolean canChildScroll(int orientation, float delta) {
        if (getChildCount() <= 0) {
            return false;
        }

        int direction = -((int) Math.signum(delta));
        switch (orientation) {
            default:
                throw new IllegalArgumentException();
            case ViewPager2.ORIENTATION_HORIZONTAL:
                return getChildAt(0).canScrollHorizontally(direction);
            case ViewPager2.ORIENTATION_VERTICAL:
                return getChildAt(0).canScrollVertically(direction);
        }
    }

    /**
     * Returns the parent view pager for this view.
     */
    private @Nullable ViewPager2 getParentViewPager() {
        if (!(getParent() instanceof View)) {
            return null;
        }

        View view = (View) getParent();
        while (view != null && !(view instanceof ViewPager2)) {
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                view = null;
            }
        }

        return (view instanceof ViewPager2) ? (ViewPager2) view : null;
    }

    /**
     * Handle touch event to support nested {@link ViewPager2} scrolling.
     *
     * @param ev The motion event being dispatched down the hierarchy.
     */
    private void handleInterceptTouchEvent(@Nullable MotionEvent ev) {
        ViewPager2 viewPager2 = getParentViewPager();

        if (ev == null || viewPager2 == null) {
            return;
        }

        int orientation = viewPager2.getOrientation();

        // Early return if child can't scroll in same direction as parent.
        if (!canChildScroll(orientation, -1.0f) && !canChildScroll(orientation, 1.0f)) {
            return;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mInitialX = ev.getX();
            mInitialY = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = ev.getX() - mInitialX;
            float dy = ev.getY() - mInitialY;
            boolean isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL;

            // Assuming ViewPager2 touch-slop is 2x touch-slop of child.
            float scaledDx = Math.abs(dx) * (isVpHorizontal ? 0.5f : 1.0f);
            float scaledDy = Math.abs(dy) * (isVpHorizontal ? 1.0f : 0.5f);

            if (scaledDx > mTouchSlop || scaledDy > mTouchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept.
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (canChildScroll(orientation, isVpHorizontal ? dx : dy)) {
                        // Child can scroll, disallow all parents to intercept.
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        // Child cannot scroll, allow all parents to intercept.
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
    }
}
