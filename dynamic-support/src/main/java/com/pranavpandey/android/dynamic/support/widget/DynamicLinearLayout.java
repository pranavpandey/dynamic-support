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
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTintWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;

/**
 * A {@link LinearLayout} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicLinearLayout extends LinearLayout implements DynamicWidget, DynamicTintWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mColorType;

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    protected @Theme.ColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    protected @ColorInt int mColor;

    /**
     * Color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedColor;

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
     * {@code true} to tint background according to the widget color.
     */
    protected boolean mTintBackground;

    /**
     * {@code true} if the style applied to this view is borderless.
     */
    protected boolean mStyleBorderless;

    public DynamicLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicLinearLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicLinearLayout);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicLinearLayout_adt_colorType,
                    Theme.ColorType.NONE);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicLinearLayout_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicLinearLayout_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicLinearLayout_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicLinearLayout_adt_backgroundAware,
                    Theme.BackgroundAware.DISABLE);
            mContrast = a.getInteger(
                    R.styleable.DynamicLinearLayout_adt_contrast,
                    Theme.Contrast.AUTO);
            mTintBackground = a.getBoolean(
                    R.styleable.DynamicLinearLayout_adt_tintBackground,
                    Defaults.ADS_TINT_BACKGROUND);
            mStyleBorderless = a.getBoolean(
                    R.styleable.DynamicLinearLayout_adt_styleBorderless,
                    Defaults.ADS_STYLE_BORDERLESS_GROUP);
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

        setColor();
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
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);

        setColor();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);

        setColor();
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(longClickable);

        setColor();
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        super.setOnLongClickListener(l);

        setColor();
    }

    @Override
    public boolean isTintBackground() {
        return mTintBackground;
    }

    @Override
    public void setTintBackground(boolean tintBackground) {
        this.mTintBackground = tintBackground;

        setColor();
    }

    @Override
    public boolean isStyleBorderless() {
        return mStyleBorderless;
    }

    @Override
    public void setStyleBorderless(boolean styleBorderless) {
        this.mStyleBorderless = styleBorderless;

        setColor();
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundColor(isBackgroundAware()
                ? Dynamic.withThemeOpacity(color, Theme.Opacity.WIDGET)
                : Dynamic.withThemeOpacity(color));
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor, this);
            }

            setBackgroundColor(mAppliedColor);
        }

        if (getBackground() != null) {
            getBackground().clearColorFilter();
        }

        if (getDividerDrawable() != null) {
            getDividerDrawable().clearColorFilter();
        }

        if (isTintBackground()) {
            if (!(getBackground() instanceof ColorDrawable)) {
                Dynamic.tintBackground(this, mContrastWithColor, isStyleBorderless());
            }

            setDividerDrawable(DynamicDrawableUtils.colorizeDrawable(getDividerDrawable(),
                    DynamicColorUtils.getContrastColor(DynamicTheme.getInstance().resolveColorType(
                            Defaults.ADS_COLOR_TYPE_DIVIDER), mContrastWithColor)));
        }
    }
}
