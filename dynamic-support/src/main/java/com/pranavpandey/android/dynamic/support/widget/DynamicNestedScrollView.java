/*
 * Copyright 2018-2020 Pranav Pandey
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
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A {@link NestedScrollView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicNestedScrollView extends NestedScrollView
        implements WindowInsetsWidget, DynamicScrollableWidget {

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
     * Scroll bar color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mScrollBarColorType;

    /**
     * Scroll bar color applied to this view.
     */
    private @ColorInt int mScrollBarColor;

    public DynamicNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicNestedScrollView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicNestedScrollView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicNestedScrollView_ads_colorType,
                    WidgetDefaults.ADS_COLOR_EDGE_EFFECT);
            mScrollBarColorType = a.getInt(
                    R.styleable.DynamicNestedScrollView_ads_scrollBarColorType,
                    WidgetDefaults.ADS_COLOR_SCROLL_BAR);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicNestedScrollView_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicNestedScrollView_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mScrollBarColor = a.getColor(
                    R.styleable.DynamicNestedScrollView_ads_scrollBarColor,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicNestedScrollView_ads_contrastWithColor,
                    WidgetDefaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicNestedScrollView_ads_backgroundAware,
                    WidgetDefaults.getBackgroundAware());

            if (a.getBoolean(
                    R.styleable.DynamicNestedScrollView_ads_windowInsets,
                    WidgetDefaults.ADS_WINDOW_INSETS)) {
                applyWindowInsets();
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        if (mColorType != Theme.ColorType.NONE
                && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mScrollBarColorType != Theme.ColorType.NONE
                && mScrollBarColorType != Theme.ColorType.CUSTOM) {
            mScrollBarColor = DynamicTheme.getInstance()
                    .resolveColorType(mScrollBarColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor(true);
    }

    @Override
    public void applyWindowInsets() {
        DynamicViewUtils.applyWindowInsetsBottom(this);
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
    public @Theme.ColorType int getScrollBarColorType() {
        return mScrollBarColorType;
    }

    @Override
    public void setScrollBarColorType(@Theme.ColorType int scrollBarColorType) {
        this.mScrollBarColorType = scrollBarColorType;

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

        setColor(true);
    }

    @Override
    public @ColorInt int getScrollBarColor() {
        return mScrollBarColor;
    }

    @Override
    public void setScrollBarColor(@ColorInt int scrollBarColor) {
        this.mScrollBarColorType = Theme.ColorType.CUSTOM;
        this.mScrollBarColor = scrollBarColor;

        setScrollBarColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setColor(true);
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
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        setColor();
    }

    @Override
    public void setColor() {
        if (mColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (isBackgroundAware() && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicScrollUtils.setEdgeEffectColor(this, mColor);

        }
    }

    @Override
    public void setScrollBarColor() {
        if (mScrollBarColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (isBackgroundAware() && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mScrollBarColor = DynamicColorUtils.getContrastColor(
                        mScrollBarColor, mContrastWithColor);
            }

            DynamicScrollUtils.setScrollBarColor(this, mScrollBarColor);
        }
    }

    @Override
    public void setColor(boolean setScrollBarColor) {
        setColor();

        if (setScrollBarColor) {
            setScrollBarColor();
        }
    }
}
