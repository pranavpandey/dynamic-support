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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateWidget;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link MaterialRadioButton} to apply {@link DynamicTheme} according to the supplied
 * parameters.
 */
public class DynamicRadioButton extends MaterialRadioButton implements DynamicStateWidget {

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
     * Normal state color type for this view.
     */
    protected @Theme.ColorType int mStateNormalColorType;

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
     * Normal state color applied to this view.
     */
    protected @ColorInt int mStateNormalColor;

    /**
     * Normal state color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedStateNormalColor;

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

    public DynamicRadioButton(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRadioButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicRadioButton(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicRadioButton);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicRadioButton_adt_colorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicRadioButton_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mStateNormalColorType = a.getInt(
                    R.styleable.DynamicRadioButton_adt_stateNormalColorType,
                    Defaults.ADS_COLOR_TYPE_ICON);
            mColor = a.getColor(
                    R.styleable.DynamicRadioButton_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicRadioButton_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mStateNormalColor = a.getColor(
                    R.styleable.DynamicRadioButton_adt_stateNormalColor,
                    Theme.Color.UNKNOWN);
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicRadioButton_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mContrast = a.getInteger(
                    R.styleable.DynamicRadioButton_adt_contrast,
                    Theme.Contrast.AUTO);
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

        if (mStateNormalColorType != Theme.ColorType.NONE
                && mStateNormalColorType != Theme.ColorType.CUSTOM) {
            mStateNormalColor = DynamicTheme.getInstance()
                    .resolveColorType(mStateNormalColorType);
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
    public @Theme.ColorType int getStateNormalColorType() {
        return mStateNormalColorType;
    }

    @Override
    public void setStateNormalColorType(@Theme.ColorType int stateNormalColorType) {
        this.mStateNormalColorType = stateNormalColorType;

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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            if (mContrastWithColor != Theme.Color.UNKNOWN) {
                if (mStateNormalColor == Theme.Color.UNKNOWN) {
                    mStateNormalColor = Dynamic.getTintColor(mContrastWithColor, this);
                }

                mAppliedColor = mColor;
                mAppliedStateNormalColor = mStateNormalColor;
                if (isBackgroundAware()) {
                    mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor);
                    mAppliedStateNormalColor = Dynamic.withContrastRatio(
                            mStateNormalColor, mContrastWithColor, this);
                }
            }

            DynamicTintUtils.setViewBackgroundTint(this,
                    mContrastWithColor, mAppliedColor, true, true);
            CompoundButtonCompat.setButtonTintList(this,
                    DynamicResourceUtils.getColorStateList(mAppliedStateNormalColor,
                            mAppliedColor, true));
        }

        setTextColor(CompoundButtonCompat.getButtonTintList(this));
    }
}
