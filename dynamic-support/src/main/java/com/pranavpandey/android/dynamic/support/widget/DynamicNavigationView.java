/*
 * Copyright 2018 Pranav Pandey
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

import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

/**
 * A NavigationView to change its selected item and edge effect color according to the
 * supplied parameters.
 */
public class DynamicNavigationView extends NavigationView implements
        DynamicBackgroundWidget, DynamicScrollableWidget, DynamicStateSelectedWidget {

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
     * Color type applied to the background of this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mBackgroundColorType;

    /**
     * Scroll bar color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mScrollBarColorType;

    /**
     * Normal item color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mStateNormalColorType;

    /**
     * Selected item color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mStateSelectedColorType;

    /**
     * Background color applied to this view.
     */
    private @ColorInt int mBackgroundColor;

    /**
     * Scroll bar color applied to this view.
     */
    private @ColorInt int mScrollBarColor;

    /**
     * Normal item color applied to this view.
     */
    private @ColorInt int mStateNormalColor;

    /**
     * Selected item color applied to this view.
     */
    private @ColorInt int mStateSelectedColor;

    public DynamicNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicNavigationView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicTheme);

        try {
            mBackgroundColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_backgroundColorType,
                    Theme.ColorType.BACKGROUND);
            mColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_colorType,
                    WidgetDefaults.ADS_COLOR_EDGE_EFFECT);
            mScrollBarColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_scrollBarColorType,
                    WidgetDefaults.ADS_COLOR_SCROLL_BAR);
            mStateNormalColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_stateNormalColorType,
                    Theme.ColorType.TEXT_PRIMARY);
            mStateSelectedColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_stateSelectedColorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mBackgroundColor = a.getColor(
                    R.styleable.DynamicTheme_ads_backgroundColor,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mColor = a.getColor(
                    R.styleable.DynamicTheme_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mScrollBarColor = a.getColor(
                    R.styleable.DynamicTheme_ads_scrollBarColor,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mStateNormalColor = a.getColor(
                    R.styleable.DynamicTheme_ads_stateNormalColor,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mStateSelectedColor = a.getColor(
                    R.styleable.DynamicTheme_ads_stateSelectedColor,
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

        initialize();
    }

    @Override
    public void initialize() {
        if (mBackgroundColorType != Theme.ColorType.NONE
                && mBackgroundColorType != Theme.ColorType.CUSTOM) {
            mBackgroundColor = DynamicTheme.getInstance()
                    .resolveColorType(mBackgroundColorType);
        }


        if (mColorType != Theme.ColorType.NONE
                && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mScrollBarColorType != Theme.ColorType.NONE
                && mScrollBarColorType != Theme.ColorType.CUSTOM) {
            mScrollBarColor = DynamicTheme.getInstance()
                    .resolveColorType(mScrollBarColorType);
        }

        if (mStateNormalColorType != Theme.ColorType.NONE
                && mStateNormalColorType != Theme.ColorType.CUSTOM) {
            mStateNormalColor = DynamicTheme.getInstance()
                    .resolveColorType(mStateNormalColorType);
        }

        if (mStateSelectedColorType != Theme.ColorType.NONE
                && mStateSelectedColorType != Theme.ColorType.CUSTOM) {
            mStateSelectedColor = DynamicTheme.getInstance()
                    .resolveColorType(mStateSelectedColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setBackgroundColor(mBackgroundColor);
        setColor(true);
        setStatesColor();
    }

    @Override
    public @Theme.ColorType int getBackgroundColorType() {
        return mBackgroundColorType;
    }

    @Override
    public void setBackgroundColorType(@Theme.ColorType int backgroundColorType) {
        this.mBackgroundColorType = backgroundColorType;

        initialize();
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
    public @Theme.ColorType int getStateNormalColorType() {
        return mStateNormalColorType;
    }

    @Override
    public void setStateNormalColorType(@Theme.ColorType int stateNormalColorType) {
        this.mStateNormalColorType = stateNormalColorType;

        initialize();
    }

    @Override
    public @Theme.ColorType int getStateSelectedColorType() {
        return mStateSelectedColorType;
    }

    @Override
    public void setStateSelectedColorType(@Theme.ColorType int stateSelectedColorType) {
        this.mStateSelectedColorType = stateSelectedColorType;

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
    public @ColorInt int getBackgroundColor() {
        return mBackgroundColor ;
    }

    @Override
    public @ColorInt int getColor() {
        return mColor;
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        setColor(false);
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
    public @ColorInt int getStateNormalColor() {
        return mStateNormalColor;
    }

    @Override
    public void setStateNormalColor(@ColorInt int stateNormalColor) {
        this.mStateNormalColorType = Theme.ColorType.CUSTOM;
        this.mStateNormalColor = stateNormalColor;

        setStatesColor();
    }

    @Override
    public @ColorInt int getStateSelectedColor() {
        return mStateSelectedColor;
    }

    @Override
    public void setStateSelectedColor(@ColorInt int stateSelectedColor) {
        this.mStateSelectedColorType = Theme.ColorType.CUSTOM;
        this.mStateSelectedColor = stateSelectedColor;

        setStatesColor();
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
        setStatesColor();
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
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        super.setBackgroundColor(backgroundColor);

        this.mBackgroundColorType = Theme.ColorType.CUSTOM;

        setColor(true);
        setStatesColor();
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

    /**
     * Set selected item color of this view according to the
     * supplied values.
     */
    public void setStatesColor() {
        if (mStateSelectedColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (isBackgroundAware() && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mStateNormalColor = DynamicColorUtils.getContrastColor(
                        mStateNormalColor, mContrastWithColor);
                mStateSelectedColor = DynamicColorUtils.getContrastColor(
                        mStateSelectedColor, mContrastWithColor);
            }

            if (DynamicVersionUtils.isLollipop()) {
                if (DynamicTheme.getInstance().get().getCornerSizeDp()
                        >= WidgetDefaults.ADS_CORNER_SELECTOR_ROUND) {
                    setItemBackgroundResource(R.drawable.ads_list_selector_round);
                } else if (DynamicTheme.getInstance().get().getCornerSizeDp()
                        >= WidgetDefaults.ADS_CORNER_SELECTOR_RECT) {
                    setItemBackgroundResource(R.drawable.ads_list_selector_rect);
                } else {
                    setItemBackgroundResource(R.drawable.ads_list_selector);
                }

                DynamicDrawableUtils.colorizeDrawable(getItemBackground(),
                        DynamicColorUtils.getLighterColor(DynamicColorUtils.adjustAlpha(
                                mStateSelectedColor, WidgetDefaults.ADS_ALPHA_SELECTED),
                                WidgetDefaults.ADS_STATE_LIGHT));
            }

            if (getItemIconTintList() != null) {
                setItemIconTintList(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemIconTintList(), mStateNormalColor, mStateSelectedColor));
            }

            if (getItemTextColor() != null) {
                setItemTextColor(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemTextColor(), mStateNormalColor, mStateSelectedColor));
            }
        }
    }
}