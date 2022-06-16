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
import androidx.appcompat.widget.AppCompatSpinner;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * A {@link AppCompatSpinner} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicSpinner extends AppCompatSpinner
        implements DynamicWidget, DynamicSurfaceWidget {

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
     * {@code true} to enable elevation on the same background.
     * <p>It will be useful to provide the true dark theme by disabling the card view elevation.
     *
     * <p>When disabled, widget elevation will be disabled (or 0) if the color of this widget
     * (surface color) is exactly same as dynamic theme background color.
     */
    protected boolean mElevationOnSameBackground;

    /**
     * Popup background used by this view.
     */
    private final DynamicPopupBackground mPopupBackground;

    public DynamicSpinner(@NonNull Context context) {
        this(context, null);
    }

    public DynamicSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPopupBackground = new DynamicPopupBackground(getContext(), attrs);
        loadFromAttributes(attrs);
    }

    public DynamicSpinner(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPopupBackground = new DynamicPopupBackground(getContext(), attrs);
        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicSpinner);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicSpinner_adt_colorType,
                    Defaults.ADS_COLOR_TYPE_SYSTEM_SECONDARY);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicSpinner_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicSpinner_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicSpinner_adt_contrastWithColor,
                    Theme.Color.UNKNOWN);
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicSpinner_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mElevationOnSameBackground = a.getBoolean(
                    R.styleable.DynamicSpinner_adt_elevationOnSameBackground,
                    Defaults.ADS_ELEVATION_ON_SAME_BACKGROUND);
            mPopupBackground.setCorner(0f);
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicDrawableUtils.colorizeDrawable(getBackground(), mAppliedColor);
        }

        setSurface();
    }

    @Override
    public boolean isElevationOnSameBackground() {
        return mElevationOnSameBackground;
    }

    @Override
    public void setElevationOnSameBackground(boolean elevationOnSameBackground) {
        this.mElevationOnSameBackground = elevationOnSameBackground;

        setSurface();
    }

    @Override
    public void setSurface() {
        Dynamic.setColorTypeOrColor(mPopupBackground, mContrastWithColorType, mContrastWithColor);
        setPopupBackgroundDrawable(mPopupBackground.getBackground());
    }

    @Override
    public boolean isBackgroundSurface() {
        return mColorType != Theme.ColorType.BACKGROUND
                && mColor != Theme.Color.UNKNOWN
                && DynamicColorUtils.removeAlpha(mContrastWithColor)
                == DynamicColorUtils.removeAlpha(
                        DynamicTheme.getInstance().get().getSurfaceColor());
    }

    @Override
    public boolean isStrokeRequired() {
        return DynamicSdkUtils.is16() && !mElevationOnSameBackground && isBackgroundSurface()
                && Color.alpha(mColor) < Defaults.ADS_ALPHA_SURFACE_STROKE;
    }
}
