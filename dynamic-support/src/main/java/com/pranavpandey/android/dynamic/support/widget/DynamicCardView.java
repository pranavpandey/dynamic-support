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
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicFloatingWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * A {@link CardView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicCardView extends CardView implements DynamicWidget,
        DynamicCornerWidget<Float>, DynamicSurfaceWidget, DynamicFloatingWidget {

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
     * Color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedColor;

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

    /**
     * {@code true} to enable elevation on the same background.
     * <p>It will be useful to provide the true dark theme by disabling the card view elevation.
     *
     * <p>When disabled, widget elevation will be disabled (or 0) if the color of this widget
     * (surface color) is exactly same as dynamic theme background color.
     */
    private boolean mElevationOnSameBackground;

    /**
     * {@code true} if this view is floating.
     * <p>It will be useful to provide the stroke for popup and dialogs.
     */
    private boolean mFloatingView;

    /**
     * Intended elevation for this view without considering the background.
     */
    private float mElevation;

    public DynamicCardView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicCardView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicCardView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicCardView_ads_colorType,
                    Theme.ColorType.SURFACE);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicCardView_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicCardView_ads_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicCardView_ads_contrastWithColor,
                    Theme.Color.UNKNOWN);
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicCardView_ads_backgroundAware,
                    Theme.BackgroundAware.DISABLE);
            mElevationOnSameBackground = a.getBoolean(
                    R.styleable.DynamicCardView_ads_elevationOnSameBackground,
                    Defaults.ADS_ELEVATION_ON_SAME_BACKGROUND);
            mFloatingView = a.getBoolean(
                    R.styleable.DynamicCardView_ads_floatingView,
                    Defaults.ADS_FLOATING_VIEW);
            mElevation = getCardElevation();

            if (a.getBoolean(R.styleable.DynamicCardView_ads_dynamicCornerRadius,
                    Defaults.ADS_DYNAMIC_CORNER_RADIUS)) {
                setCorner((float) DynamicTheme.getInstance().get().getCornerRadius());
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
    public void setBackgroundColor(@ColorInt int color) {
        super.setCardBackgroundColor(color);

        setColor(color);
    }

    @Override
    public void setCorner(Float cornerRadius) {
        setRadius(cornerRadius);
    }

    @Override
    public Float getCorner() {
        return getRadius();
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            if (mElevationOnSameBackground && isBackgroundSurface()) {
                mAppliedColor = DynamicTheme.getInstance().generateSurfaceColor(mAppliedColor);
            }

            setCardBackgroundColor(DynamicColorUtils.removeAlpha(mAppliedColor));
            setSurface();
        }
    }

    @Override
    public void setCardElevation(float elevation)  {
        super.setCardElevation(elevation);

        if (elevation > 0) {
            this.mElevation = getCardElevation();
        }
    }

    @Override
    public boolean isElevationOnSameBackground() {
        return mElevationOnSameBackground;
    }

    @Override
    public void setElevationOnSameBackground(boolean elevationOnSameBackground) {
        this.mElevationOnSameBackground = elevationOnSameBackground;

        setColor();
    }

    @Override
    public boolean isFloatingView() {
        return mFloatingView;
    }

    @Override
    public void setFloatingView(boolean floatingView) {
        this.mFloatingView = floatingView;

        setColor();
    }

    @Override
    public void setSurface() {
        if (!mElevationOnSameBackground && isBackgroundSurface()) {
            setCardElevation(0);
        } else {
            setCardElevation(mElevation);
        }
    }

    @Override
    public boolean isBackgroundSurface() {
        return mColorType != Theme.ColorType.BACKGROUND
                && mColor != Theme.Color.UNKNOWN
                && DynamicColorUtils.removeAlpha(mColor)
                == DynamicColorUtils.removeAlpha(mContrastWithColor);
    }

    @Override
    public boolean isStrokeRequired() {
        return DynamicSdkUtils.is16() && !mElevationOnSameBackground && isBackgroundSurface()
                && Color.alpha(mColor) < Defaults.ADS_ALPHA_SURFACE_STROKE;
    }
}
