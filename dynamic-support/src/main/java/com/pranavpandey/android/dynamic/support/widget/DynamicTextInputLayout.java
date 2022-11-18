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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicErrorWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;

/**
 * A {@link TextInputLayout} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicTextInputLayout extends TextInputLayout implements DynamicWidget,
        DynamicCornerWidget<Float>, DynamicErrorWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mColorType;

    /**
     * Error color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mErrorColorType;

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
     * Error color applied to this view.
     */
    protected @ColorInt int mErrorColor;

    /**
     * Error color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedErrorColor;

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
     * Corner radius used by this view.
     */
    protected @Nullable Float mCornerRadius;

    /**
     * Internal text view used by this view.
     */
    private final DynamicTextView mTextView;

    public DynamicTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTextView = new DynamicTextView(context, attrs);
        loadFromAttributes(attrs);
    }

    public DynamicTextInputLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextView = new DynamicTextView(context, attrs, defStyleAttr);
        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicTextInputLayout);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicTextInputLayout_adt_colorType,
                    Theme.ColorType.ACCENT);
            mErrorColorType = a.getInt(
                    R.styleable.DynamicTextInputLayout_adt_errorColorType,
                    Defaults.ADS_COLOR_TYPE_ERROR);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTextInputLayout_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicTextInputLayout_adt_color,
                    Theme.Color.UNKNOWN);
            mErrorColor = a.getColor(
                    R.styleable.DynamicTextInputLayout_adt_errorColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicTextInputLayout_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicTextInputLayout_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mContrast = a.getInteger(
                    R.styleable.DynamicTextInputLayout_adt_contrast,
                    Theme.Contrast.AUTO);

            if (a.getBoolean(R.styleable.DynamicTextInputLayout_adt_dynamicCornerSize,
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

        if (mErrorColorType != Theme.ColorType.NONE
                && mErrorColorType != Theme.ColorType.CUSTOM) {
            mErrorColor = DynamicTheme.getInstance().resolveColorType(mErrorColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor();
        setErrorColor();
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
    public @Theme.ColorType int getErrorColorType() {
        return mErrorColorType;
    }

    @Override
    public void setErrorColorType(@Theme.ColorType int errorColorType) {
        this.mErrorColorType = errorColorType;

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
    public @ColorInt int getErrorColor(boolean resolve) {
        return resolve ? mAppliedErrorColor : mErrorColor;
    }

    @Override
    public @ColorInt int getErrorColor() {
        return getErrorColor(true);
    }

    @Override
    public void setErrorColor(@ColorInt int errorColor) {
        this.mErrorColorType = Theme.ColorType.CUSTOM;
        this.mErrorColor = errorColor;

        setErrorColor();
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
        setErrorColor();
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Dynamic.setCornerMin(this, Math.min(
                getWidth() / Theme.Corner.FACTOR_MAX_BOX,
                getHeight() / Theme.Corner.FACTOR_MAX_BOX));
    }

    @Override
    public void refreshStartIconDrawableState() {
        super.refreshStartIconDrawableState();

        DynamicInputUtils.setCursorColor(getEditText(), isErrorEnabled()
                ? getErrorColor(true) : getColor(true));
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);

        DynamicInputUtils.setCursorColor(getEditText(), isErrorEnabled()
                ? getErrorColor(true) : getColor(true));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @Override
    public @NonNull Float getCorner() {
        return mCornerRadius != null ? mCornerRadius : getBoxCornerRadiusTopStart();
    }

    @Override
    public void setCorner(@NonNull Float cornerSize) {
        this.mCornerRadius = cornerSize;

        // Fix null pointer exception while setting the corner radii
        // in MDC-Android 1.1.0-beta01.
        try {
            post(new Runnable() {
                @Override
                public void run() {
                    setBoxCornerRadii(getBoxCornerRadiusTopStart() > 0 ? cornerSize : 0,
                            getBoxCornerRadiusTopEnd() > 0 ? cornerSize : 0,
                            getBoxCornerRadiusBottomStart() > 0 ? cornerSize : 0,
                            getBoxCornerRadiusBottomEnd() > 0 ? cornerSize : 0);
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor, this);
            }

            // Remove alpha as box background color does not supports alpha component.
            @ColorInt int boxColor = DynamicColorUtils.removeAlpha(
                    DynamicColorUtils.getStateColor(mContrastWithColor,
                    Defaults.ADS_STATE_BOX_LIGHT, Defaults.ADS_STATE_BOX_DARK));

            post(new Runnable() {
                @Override
                public void run() {
                    setBoxStrokeColorStateList(DynamicResourceUtils.getColorStateList(
                            boxColor, mAppliedColor, false));
                    if (getBoxBackgroundMode() == BOX_BACKGROUND_FILLED) {
                        setBoxBackgroundColor(boxColor);
                    }

                    DynamicInputUtils.setColor(
                            DynamicTextInputLayout.this, mAppliedColor);
                }
            });
        }

        Dynamic.setColorType(mTextView, Theme.ColorType.NONE);
        Dynamic.setContrastWithColorTypeOrColor(mTextView,
                mContrastWithColorType, mContrastWithColor);
        Dynamic.setBackgroundAware(mTextView, mBackgroundAware, getContrast(false));

        setStartIconTintList(mTextView.getHintTextColors());
        setEndIconTintList(mTextView.getHintTextColors());
        setHelperTextColor(mTextView.getHintTextColors());
        setDefaultHintTextColor(mTextView.getHintTextColors());
        setHintTextColor(mTextView.getHintTextColors());
    }

    @Override
    public void setErrorColor() {
        if (mErrorColor != Theme.Color.UNKNOWN) {
            mAppliedErrorColor = mErrorColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedErrorColor = Dynamic.withContrastRatio(
                        mErrorColor, mContrastWithColor, this);
            }

            ColorStateList errorStateList = ColorStateList.valueOf(mAppliedErrorColor);

            setBoxStrokeErrorColor(errorStateList);
            setErrorTextColor(errorStateList);
            setErrorIconTintList(errorStateList);
        }
    }
}
