/*
 * Copyright 2018-2022 Pranav Pandey
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
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTextWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

/**
 * A {@link BottomNavigationView} to apply {@link DynamicTheme} according to the
 * supplied parameters.
 */
public class DynamicNavigationRailView extends NavigationRailView
        implements WindowInsetsWidget, DynamicBackgroundWidget, DynamicTextWidget,
        DynamicCornerWidget<Float> {

    /**
     * Color type applied to the background of this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mBackgroundColorType;

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mColorType;

    /**
     * Text color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mTextColorType;

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    protected @Theme.ColorType int mContrastWithColorType;

    /**
     * Background color applied to this view.
     */
    protected @ColorInt int mBackgroundColor;

    /**
     * Color applied to this view.
     */
    protected @ColorInt int mColor;

    /**
     * Color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedColor;

    /**
     * Text color applied to this view.
     */
    protected @ColorInt int mTextColor;

    /**
     * Text color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedTextColor;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    protected @ColorInt int mContrastWithColor;

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
    protected @Theme.BackgroundAware int mBackgroundAware;

    /**
     * Minimum contrast value to generate contrast color for the background aware functionality.
     */
    protected int mContrast;

    /**
     * Corner size used by this view.
     */
    protected float mCornerSize;

    public DynamicNavigationRailView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicNavigationRailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicNavigationRailView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicNavigationRailView);

        try {
            mBackgroundColorType = a.getInt(
                    R.styleable.DynamicNavigationRailView_adt_backgroundColorType,
                    Theme.ColorType.PRIMARY);
            mColorType = a.getInt(
                    R.styleable.DynamicNavigationRailView_adt_colorType,
                    Theme.ColorType.PRIMARY);
            mTextColorType = a.getInt(
                    R.styleable.DynamicNavigationRailView_adt_textColorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicNavigationRailView_adt_contrastWithColorType,
                    Theme.ColorType.PRIMARY);
            mBackgroundColor = a.getColor(
                    R.styleable.DynamicNavigationRailView_adt_backgroundColor,
                    Theme.Color.UNKNOWN);
            mColor = a.getColor(
                    R.styleable.DynamicNavigationRailView_adt_color,
                    Theme.Color.UNKNOWN);
            mTextColor = a.getColor(
                    R.styleable.DynamicNavigationRailView_adt_textColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicNavigationRailView_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicNavigationRailView_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mContrast = a.getInteger(
                    R.styleable.DynamicNavigationRailView_adt_contrast,
                    Theme.Contrast.AUTO);

            if (a.getBoolean(
                    R.styleable.DynamicNavigationRailView_adt_dynamicCornerSize,
                    Defaults.ADS_DYNAMIC_CORNER_SIZE)) {
                setCorner((float) DynamicTheme.getInstance().get().getCornerRadius());
            }

            if (a.getBoolean(
                    R.styleable.DynamicNavigationRailView_adt_windowInsets,
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

        if (mTextColorType != Theme.ColorType.NONE
                && mTextColorType != Theme.ColorType.CUSTOM) {
            mTextColor = DynamicTheme.getInstance().resolveColorType(mTextColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setBackgroundColor(mBackgroundColor);
    }

    @Override
    public void applyWindowInsets() {
        DynamicViewUtils.applyWindowInsetsVertical(this);
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
    public @Theme.ColorType int getTextColorType() {
        return mTextColorType;
    }

    @Override
    public void setTextColorType(@Theme.ColorType int textColorType) {
        this.mTextColorType = textColorType;

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

        setTextWidgetColor(true);
    }

    @Override
    public @ColorInt int getTextColor(boolean resolve) {
        return resolve ? mAppliedTextColor : mTextColor;
    }

    @Override
    public @ColorInt int getTextColor() {
        return getTextColor(true);
    }

    @Override
    public void setTextColor(@ColorInt int textColor) {
        this.mTextColorType = Theme.ColorType.CUSTOM;
        this.mTextColor = textColor;

        setBackgroundColor(getBackgroundColor());
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setBackgroundColor(getBackgroundColor());
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return mBackgroundAware;
    }

    @Override
    public boolean isBackgroundAware() {
        return Dynamic.isBackgroundAware(this);
    }

    @Override
    public void setBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        this.mBackgroundAware = backgroundAware;

        setTextWidgetColor(true);
    }

    @Override
    public int getContrast(boolean resolve) {
        if (resolve) {
            return Dynamic.getContrast(this);
        }

        return mContrast;
    }

    @Override
    public int getContrast() {
        return getContrast(true);
    }

    @Override
    public float getContrastRatio() {
        return getContrast() / (float) Theme.Contrast.MAX;
    }

    @Override
    public void setContrast(int contrast) {
        this.mContrast = contrast;

        setBackgroundAware(getBackgroundAware());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        setBackgroundColor(getBackgroundColor());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Dynamic.setCornerMin(this, Math.min(
                getWidth() / Theme.Corner.FACTOR_MAX,
                getHeight() / Theme.Corner.FACTOR_MAX));
    }

    @Override
    public @NonNull Float getCorner() {
        return mCornerSize;
    }

    @Override
    public void setCorner(@NonNull Float cornerSize) {
        this.mCornerSize = cornerSize;

        if (getItemActiveIndicatorShapeAppearance() != null) {
            setItemActiveIndicatorShapeAppearance(
                    getItemActiveIndicatorShapeAppearance().withCornerSize(cornerSize));
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundColor(color);

        this.mBackgroundColor = color;
        this.mBackgroundColorType = Theme.ColorType.CUSTOM;

        setTextWidgetColor(true);
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
        }

        if (getBackground() != null) {
            DynamicDrawableUtils.setBackground(this, DynamicDrawableUtils.colorizeDrawable(
                    getBackground(), Dynamic.withThemeOpacity(getBackgroundColor())));
        } else {
            DynamicDrawableUtils.setBackground(this, null);
            super.setBackgroundColor(Dynamic.withThemeOpacity(getBackgroundColor()));
        }
    }

    @Override
    public void setTextColor() {
        if (mTextColor != Theme.Color.UNKNOWN) {
            @ColorInt int normalColor = DynamicColorUtils.adjustAlpha(
                    mContrastWithColor, Defaults.ADS_ALPHA_UNSELECTED);
            @ColorInt int activeColor = DynamicColorUtils.adjustAlpha(
                    mAppliedTextColor, Defaults.ADS_STATE_PRESSED);
            mAppliedTextColor = mTextColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                normalColor = Dynamic.withContrastRatio(normalColor, mContrastWithColor, this);
                mAppliedTextColor = Dynamic.withContrastRatio(
                        mTextColor, mContrastWithColor, this);
            }

            setItemTextColor(DynamicResourceUtils.getColorStateList(
                    normalColor, mAppliedTextColor, true));
            setItemIconTintList(DynamicResourceUtils.getColorStateList(
                    normalColor, mAppliedTextColor, true));
            setItemRippleColor(DynamicResourceUtils.getColorStateList(
                    Color.TRANSPARENT, activeColor, false));
            setItemActiveIndicatorColor(DynamicResourceUtils.getColorStateList(activeColor));

            DynamicMenuUtils.setViewItemsTint(this,
                    mAppliedTextColor, mAppliedColor, false);
        }
    }

    @Override
    public void setTextWidgetColor(boolean setTextColor) {
        setColor();

        if (setTextColor) {
            setTextColor();
        }
    }
}
