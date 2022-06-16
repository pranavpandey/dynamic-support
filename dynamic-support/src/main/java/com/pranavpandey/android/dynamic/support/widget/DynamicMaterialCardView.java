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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicFloatingWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * A {@link MaterialCardView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicMaterialCardView extends MaterialCardView implements DynamicWidget,
        DynamicCornerWidget<Float>, DynamicSurfaceWidget, DynamicFloatingWidget {

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
     * {@code true} to force elevation for this widget.
     * <p>When disabled, widget will follow the dynamic theme elevation.
     */
    protected boolean mForceElevation;

    /**
     * {@code true} if this view is floating.
     * <p>It will be useful to provide the stroke for popup and dialogs.
     */
    protected boolean mFloatingView;

    /**
     * Intended elevation for this view without considering the background.
     */
    protected float mElevation;

    public DynamicMaterialCardView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicMaterialCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicMaterialCardView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicMaterialCardView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicMaterialCardView_adt_colorType,
                    Theme.ColorType.SURFACE);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicMaterialCardView_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicMaterialCardView_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicMaterialCardView_adt_contrastWithColor,
                    Theme.Color.UNKNOWN);
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicMaterialCardView_adt_backgroundAware,
                    Theme.BackgroundAware.DISABLE);
            mContrast = a.getInteger(
                    R.styleable.DynamicMaterialCardView_adt_contrast,
                    Theme.Contrast.AUTO);
            mForceElevation = a.getBoolean(
                    R.styleable.DynamicMaterialCardView_adt_forceElevation,
                    Defaults.ADS_FORCE_ELEVATION);
            mFloatingView = a.getBoolean(
                    R.styleable.DynamicMaterialCardView_adt_floatingView,
                    Defaults.ADS_FLOATING_VIEW);
            mElevation = getCardElevation();

            if (a.getBoolean(R.styleable.DynamicMaterialCardView_adt_dynamicCornerSize,
                    Defaults.ADS_DYNAMIC_CORNER_SIZE)) {
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
    public void setBackgroundColor(@ColorInt int color) {
        super.setCardBackgroundColor(color);

        setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Dynamic.setCornerMin(this, Math.min(
                getWidth() / Theme.Corner.FACTOR_MAX,
                getHeight() / Theme.Corner.FACTOR_MAX));
    }

    @Override
    public void setCardElevation(float elevation)  {
        super.setCardElevation(elevation);

        if (elevation > 0) {
            this.mElevation = getCardElevation();
        }
    }

    @Override
    public @NonNull Float getCorner() {
        return getRadius();
    }

    @Override
    public void setCorner(@NonNull Float cornerSize) {
        setRadius(cornerSize);
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public void setCardBackgroundColor(@ColorInt int color) {
        super.setCardBackgroundColor(isFloatingView()
                ? Dynamic.withThemeOpacity(color, Theme.Opacity.FLOATING)
                : isBackgroundAware() ? Dynamic.withThemeOpacity(color, Theme.Opacity.WIDGET)
                : Dynamic.withThemeOpacity(color));

        if (DynamicSdkUtils.is28()
                && DynamicTheme.getInstance().get().getElevation(false) == Theme.Elevation.AUTO
                && DynamicTheme.getInstance().get().getOpacity() < Theme.Opacity.FLOATING) {
            setOutlineAmbientShadowColor(getCardBackgroundColor().getDefaultColor());

            if (!isFloatingView() && !isForceElevation()) {
                setOutlineSpotShadowColor(getCardBackgroundColor().getDefaultColor());
            }
        }
    }

    @Override
    public void setStrokeColor(@ColorInt int strokeColor) {
        super.setStrokeColor(isFloatingView()
                ? Dynamic.withThemeOpacity(strokeColor, Theme.Opacity.FLOATING)
                : isBackgroundAware() ? Dynamic.withThemeOpacity(strokeColor, Theme.Opacity.WIDGET)
                : Dynamic.withThemeOpacity(strokeColor));
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor, this);
            }

            if (mForceElevation && isBackgroundSurface()) {
                mAppliedColor = DynamicTheme.getInstance().generateSurfaceColor(mAppliedColor);
            }

            mAppliedColor = DynamicColorUtils.removeAlpha(mAppliedColor);

            setCardBackgroundColor(mAppliedColor);
            setSurface();
        }
    }

    @Override
    public boolean isForceElevation() {
        return mForceElevation;
    }

    @Override
    public void setForceElevation(boolean forceElevation) {
        this.mForceElevation = forceElevation;

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
        setStrokeColor(Color.TRANSPARENT);

        @ColorInt int strokeColor = DynamicTheme.getInstance().get().getStrokeColor();
        if (DynamicTheme.getInstance().get().isBackgroundAware()
                && mContrastWithColor != Theme.Color.UNKNOWN) {
            strokeColor = Dynamic.withContrastRatio(strokeColor, mContrastWithColor, this);
        }

         if (mFloatingView) {
            if (Color.alpha(mColor) < Theme.Opacity.STROKE
                    || Color.alpha(mContrastWithColor) < Theme.Opacity.STROKE) {
                setStrokeColor(strokeColor);
            }
        } else if (isBackgroundSurface()) {
             if (!mForceElevation) {
                 setCardElevation(Theme.Elevation.DISABLE);
             }

             if (Color.alpha(mColor) < Theme.Opacity.STROKE) {
                 setStrokeColor(strokeColor);
             }
         } else {
            setCardElevation(mElevation);
        }
    }

    @Override
    public boolean isBackgroundSurface() {
        return !DynamicTheme.getInstance().get().isElevation()
                || (mColorType != Theme.ColorType.BACKGROUND
                && mColor != Theme.Color.UNKNOWN
                && DynamicColorUtils.removeAlpha(mColor)
                == DynamicColorUtils.removeAlpha(mContrastWithColor));
    }

    @Override
    public boolean isStrokeRequired() {
        return DynamicSdkUtils.is16() && !mForceElevation && isBackgroundSurface()
                && Color.alpha(mColor) < Theme.Opacity.STROKE;
    }
}
