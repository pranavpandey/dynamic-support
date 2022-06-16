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

import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTintWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * An {@link MaterialButton} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicButton extends MaterialButton implements DynamicWidget,
        DynamicCornerWidget<Integer>, DynamicTintWidget, DynamicSurfaceWidget {

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

    /**
     * {@code true} to force elevation for this widget.
     * <p>When disabled, widget will follow the dynamic theme elevation.
     */
    protected boolean mForceElevation;

    /**
     * Intended elevation for this view without considering the background.
     */
    protected float mElevation;

    /**
     * State list animator used by this button.
     */
    protected StateListAnimator mStateListAnimator;

    public DynamicButton(@NonNull Context context) {
        this(context, null);
    }

    public DynamicButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicButton(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicButton);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicButton_adt_colorType,
                    Theme.ColorType.TINT_BACKGROUND);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicButton_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicButton_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicButton_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicButton_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mContrast = a.getInteger(
                    R.styleable.DynamicButton_adt_contrast,
                    Theme.Contrast.AUTO);
            mTintBackground = a.getBoolean(
                    R.styleable.DynamicButton_adt_tintBackground,
                    Defaults.ADS_TINT_BACKGROUND);
            mStyleBorderless = a.getBoolean(
                    R.styleable.DynamicButton_adt_styleBorderless,
                    Defaults.ADS_STYLE_BORDERLESS);
            mForceElevation = a.getBoolean(
                    R.styleable.DynamicButton_adt_forceElevation,
                    Defaults.ADS_FORCE_ELEVATION);
            mElevation = Dynamic.getElevation(this, Theme.Elevation.DISABLE);

            if (DynamicSdkUtils.is21()) {
                mStateListAnimator = getStateListAnimator();
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

        setCorner(DynamicTheme.getInstance().get().getCornerRadius());
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Dynamic.setCornerMin(this, Math.min(
                getWidth() / Theme.Corner.FACTOR_MAX,
                getHeight() / Theme.Corner.FACTOR_MAX));

        setSurface();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setElevation(float elevation) {
        super.setElevation(elevation);

        if (elevation > 0) {
            mElevation = elevation;
        }
    }

    @Override
    public void setBackground(@NonNull Drawable background) {
        super.setBackground(background);

        if (getBackground() instanceof MaterialShapeDrawable
                && ((MaterialShapeDrawable) getBackground()).getElevation() > 0) {
            mElevation = ((MaterialShapeDrawable) getBackground()).getElevation();
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        super.setStateListAnimator(stateListAnimator);

        if (stateListAnimator != null) {
            mStateListAnimator = stateListAnimator;
        }
    }

    @Override
    public @NonNull Integer getCorner() {
        return getCornerRadius();
    }

    @Override
    public void setCorner(@NonNull Integer cornerSize) {
        setCornerRadius(cornerSize);
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            @ColorInt int textColor = Dynamic.getTintColor(mColor, this);
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor, this);
                textColor = Dynamic.withContrastRatio(isStyleBorderless()
                                ? mAppliedColor : mContrastWithColor, isStyleBorderless()
                        ? mContrastWithColor : mAppliedColor, this);
            }

            if (getBackground() != null) {
                getBackground().clearColorFilter();

                if (isTintBackground()) {
                    Dynamic.tintBackground(this, mContrastWithColor,
                            mAppliedColor, isStyleBorderless());
                }
            }

            if (isStyleBorderless()) {
                setTextColor(DynamicResourceUtils.getColorStateList(textColor,
                        mAppliedColor, mAppliedColor, false));
            } else {
                setTextColor(DynamicResourceUtils.getColorStateList(
                        textColor, textColor, false));
            }
        }

        setSurface();
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setSurface() {
        if (!mForceElevation && isBackgroundSurface()) {
            Dynamic.setElevation(this, Theme.Elevation.DISABLE);

            if (DynamicSdkUtils.is21()) {
                setStateListAnimator(null);
            }
        } else {
            Dynamic.setElevation(this, mElevation);

            if (DynamicSdkUtils.is21()) {
                setStateListAnimator(mStateListAnimator);
            }
        }
    }

    @Override
    public boolean isBackgroundSurface() {
        return !DynamicTheme.getInstance().get().isElevation();
    }

    @Override
    public boolean isStrokeRequired() {
        return false;
    }
}
