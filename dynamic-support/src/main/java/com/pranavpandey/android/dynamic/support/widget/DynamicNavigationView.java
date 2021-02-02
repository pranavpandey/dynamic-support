/*
 * Copyright 2018-2021 Pranav Pandey
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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

import java.lang.reflect.Field;

/**
 * A {@link NavigationView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicNavigationView extends NavigationView
        implements WindowInsetsWidget, DynamicBackgroundWidget,
        DynamicScrollableWidget, DynamicStateSelectedWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

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
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    private @ColorInt int mColor;

    /**
     * Color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedColor;

    /**
     * Background color applied to this view.
     */
    private @ColorInt int mBackgroundColor;

    /**
     * Scroll bar color applied to this view.
     */
    private @ColorInt int mScrollBarColor;

    /**
     * Scroll bar color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedScrollBarColor;

    /**
     * Normal item color applied to this view.
     */
    private @ColorInt int mStateNormalColor;

    /**
     * Normal item color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedStateNormalColor;

    /**
     * Selected item color applied to this view.
     */
    private @ColorInt int mStateSelectedColor;

    /**
     * Selected item color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedStateSelectedColor;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The background aware functionality to change this view color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #mContrastWithColor
     */
    private @Theme.BackgroundAware int mBackgroundAware;

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
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicNavigationView);

        try {
            mBackgroundColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_backgroundColorType,
                    Theme.ColorType.BACKGROUND);
            mColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_colorType,
                    Defaults.ADS_COLOR_TYPE_EDGE_EFFECT);
            mScrollBarColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_scrollBarColorType,
                    Defaults.ADS_COLOR_TYPE_SCROLLABLE);
            mStateNormalColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_stateNormalColorType,
                    Theme.ColorType.TEXT_PRIMARY);
            mStateSelectedColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_stateSelectedColorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicNavigationView_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mBackgroundColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_backgroundColor,
                    Theme.Color.UNKNOWN);
            mColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_color,
                    Theme.Color.UNKNOWN);
            mScrollBarColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_scrollBarColor,
                    Theme.Color.UNKNOWN);
            mStateNormalColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_stateNormalColor,
                    Theme.Color.UNKNOWN);
            mStateSelectedColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_stateSelectedColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicNavigationView_ads_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicNavigationView_ads_backgroundAware,
                    Defaults.getBackgroundAware());

            if (a.getBoolean(
                    R.styleable.DynamicNavigationView_ads_windowInsets,
                    Defaults.ADS_WINDOW_INSETS)) {
                applyWindowInsets();
            }
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
    public void applyWindowInsets() {
        final View header;
        final int left = getPaddingLeft();
        final int bottom = getPaddingBottom();
        final int headerTop;

        if (getHeaderCount() != 0) {
            header = getHeaderView(0);
            headerTop = header.getPaddingTop();
        } else {
            header = null;
            headerTop = 0;
        }

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                v.setPadding(left + insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()).left, v.getPaddingTop(),
                        v.getPaddingRight(), bottom + insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()).bottom);

                try {
                    final Rect rect = new Rect();
                    rect.set(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
                    final Field fieldInsets =
                            ScrimInsetsFrameLayout.class.getDeclaredField("insets");
                    fieldInsets.setAccessible(true);
                    fieldInsets.set(DynamicNavigationView.this, rect);

                    if (header != null) {
                        header.setPadding(header.getPaddingLeft(),
                                headerTop + insets.getInsets(
                                        WindowInsetsCompat.Type.systemBars()).top,
                                header.getPaddingRight(), header.getPaddingBottom());
                    }
                } catch (Exception ignored) {
                }

                return WindowInsetsCompat.CONSUMED.inset(
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()));
            }
        });
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
    public @ColorInt int getColor(boolean resolve) {
        return resolve ? mAppliedColor : mColor;
    }

    @Override
    public @ColorInt int getColor() {
        return getColor(true);
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        setColor(false);
    }

    @Override
    public @ColorInt int getScrollBarColor(boolean resolve) {
        return resolve ? mAppliedScrollBarColor : mScrollBarColor;
    }

    @Override
    public @ColorInt int getScrollBarColor() {
        return getScrollBarColor(true);
    }

    @Override
    public void setScrollBarColor(@ColorInt int scrollBarColor) {
        this.mScrollBarColorType = Theme.ColorType.CUSTOM;
        this.mScrollBarColor = scrollBarColor;

        setScrollBarColor();
    }

    @Override
    public @ColorInt int getStateNormalColor(boolean resolve) {
        return resolve ? mAppliedStateNormalColor : mStateNormalColor;
    }

    @Override
    public @ColorInt int getStateNormalColor() {
        return getStateNormalColor(true);
    }

    @Override
    public void setStateNormalColor(@ColorInt int stateNormalColor) {
        this.mStateNormalColorType = Theme.ColorType.CUSTOM;
        this.mStateNormalColor = stateNormalColor;

        setStatesColor();
    }

    @Override
    public @ColorInt int getStateSelectedColor(boolean resolve) {
        return resolve ? mAppliedStateSelectedColor : mStateSelectedColor;
    }

    @Override
    public @ColorInt int getStateSelectedColor() {
        return getStateSelectedColor(true);
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
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicScrollUtils.setEdgeEffectColor(this, mAppliedColor);
        }
    }

    @Override
    public void setScrollBarColor() {
        if (mScrollBarColor != Theme.Color.UNKNOWN) {
            mAppliedScrollBarColor = mScrollBarColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedScrollBarColor = DynamicColorUtils.getContrastColor(
                        mScrollBarColor, mContrastWithColor);
            }

            DynamicScrollUtils.setScrollBarColor(this, mAppliedScrollBarColor);
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
     * Set selected item color of this view according to the supplied values.
     */
    public void setStatesColor() {
        if (mStateSelectedColor != Theme.Color.UNKNOWN) {
            mAppliedStateNormalColor = mStateNormalColor;
            mAppliedStateSelectedColor = mStateSelectedColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedStateNormalColor = DynamicColorUtils.getContrastColor(
                        mStateNormalColor, mContrastWithColor);
                mAppliedStateSelectedColor = DynamicColorUtils.getContrastColor(
                        mStateSelectedColor, mContrastWithColor);
            }

            if (DynamicTheme.getInstance().get().getCornerSizeDp()
                    >= Defaults.ADS_CORNER_SELECTOR_ROUND) {
                setItemBackgroundResource(R.drawable.ads_list_selector_round);
            } else if (DynamicTheme.getInstance().get().getCornerSizeDp()
                    >= Defaults.ADS_CORNER_SELECTOR_RECT) {
                setItemBackgroundResource(R.drawable.ads_list_selector_rect);
            } else {
                setItemBackgroundResource(R.drawable.ads_list_selector);
            }

            DynamicDrawableUtils.colorizeDrawable(getItemBackground(),
                    DynamicColorUtils.getLighterColor(DynamicColorUtils.adjustAlpha(
                            mAppliedStateSelectedColor, Defaults.ADS_ALPHA_SELECTED),
                            Defaults.ADS_STATE_LIGHT));

            if (getItemIconTintList() != null) {
                setItemIconTintList(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemIconTintList(), mAppliedStateNormalColor,
                        mAppliedStateSelectedColor));
            }

            if (getItemTextColor() != null) {
                setItemTextColor(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemTextColor(), mAppliedStateNormalColor, mAppliedStateSelectedColor));
            }
        }
    }
}
