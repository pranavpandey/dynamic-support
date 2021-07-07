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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A {@link TextInputEditText} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicTextInputEditText extends TextInputEditText implements DynamicWidget {

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
     * Internal text view used by this view.
     */
    private final DynamicTextView mTextView;

    public DynamicTextInputEditText(@NonNull Context context) {
        this(context, null);
    }

    public DynamicTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTextView = new DynamicTextView(context, attrs);
        loadFromAttributes(attrs);
    }

    public DynamicTextInputEditText(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextView = new DynamicTextView(context, attrs, defStyleAttr);
        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicTextInputEditText);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicTextInputEditText_adt_colorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTextInputEditText_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicTextInputEditText_adt_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicTextInputEditText_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicTextInputEditText_adt_backgroundAware,
                    Defaults.getBackgroundAware());
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

            post(new Runnable() {
                @Override
                public void run() {
                    TextInputLayout parent = getParentTextInputLayout();
                    if (parent != null) {
                        DynamicInputUtils.setColor(DynamicTextInputEditText.this,
                                parent.getBoxBackgroundColor(), mAppliedColor);
                    }
                }
            });
        }

        Dynamic.setColorType(mTextView, Theme.ColorType.NONE);
        Dynamic.setContrastWithColorTypeOrColor(mTextView,
                mContrastWithColorType, mContrastWithColor);
        Dynamic.setBackgroundAware(mTextView, mBackgroundAware);
        setTextColor(mTextView.getTextColors());
        setHintTextColor(mTextView.getHintTextColors());
        setLinkTextColor(mTextView.getLinkTextColors());
        setHighlightColor(DynamicColorUtils.getContrastColor(
                getCurrentTextColor(), getCurrentTextColor()));
    }

    /**
     * Returns the parent text input layout.
     *
     * @return The parent text input layout.
     */
    private @Nullable TextInputLayout getParentTextInputLayout() {
        ViewParent parent = getParent();
        while (parent instanceof View) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }
}