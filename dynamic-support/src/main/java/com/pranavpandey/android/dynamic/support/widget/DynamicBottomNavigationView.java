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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTextWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A {@link BottomNavigationView} to apply {@link DynamicTheme} according to the
 * supplied parameters.
 */
public class DynamicBottomNavigationView extends BottomNavigationView
        implements WindowInsetsWidget, DynamicTextWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

    /**
     * Text color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mTextColorType;

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
     * Text color applied to this view.
     */
    private @ColorInt int mTextColor;

    /**
     * Text color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedTextColor;

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

    public DynamicBottomNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicBottomNavigationView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicBottomNavigationView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicBottomNavigationView_ads_colorType,
                    Theme.ColorType.PRIMARY);
            mTextColorType = a.getInt(
                    R.styleable.DynamicBottomNavigationView_ads_textColorType,
                    Theme.ColorType.TINT_PRIMARY);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicBottomNavigationView_ads_contrastWithColorType,
                    Theme.ColorType.PRIMARY);
            mColor = a.getColor(
                    R.styleable.DynamicBottomNavigationView_ads_color,
                    Theme.Color.UNKNOWN);
            mTextColor = a.getColor(
                    R.styleable.DynamicBottomNavigationView_ads_textColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicBottomNavigationView_ads_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicBottomNavigationView_ads_backgroundAware,
                    Defaults.getBackgroundAware());

            if (a.getBoolean(
                    R.styleable.DynamicBottomNavigationView_ads_windowInsets,
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

        setColor();
        setTextColor();
    }

    @Override
    public void applyWindowInsets() {
        DynamicViewUtils.applyWindowInsetsBottom(this);
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
        setTextColor();
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
        this.mTextColorType  = Theme.ColorType.CUSTOM;
        this.mTextColor = textColor;

        setColor();
        setTextColor();
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
        setTextColor();
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
        setTextColor();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        setColor();
        setTextColor();
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            setBackgroundColor(mColor);
        }
    }

    @Override
    public void setTextColor() {
        if (mTextColor != Theme.Color.UNKNOWN) {
            mAppliedTextColor = mTextColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedTextColor = DynamicColorUtils.getContrastColor(
                        mTextColor, mContrastWithColor);
            }

            @ColorInt int normalColor = DynamicColorUtils.adjustAlpha(
                    DynamicColorUtils.getTintColor(mAppliedColor), Defaults.ADS_ALPHA_UNSELECTED);
            setItemTextColor(DynamicResourceUtils.getColorStateList(
                    normalColor, mAppliedTextColor, true));
            setItemIconTintList(DynamicResourceUtils.getColorStateList(
                    normalColor, mAppliedTextColor, true));
            setItemRippleColor(DynamicResourceUtils.getColorStateList(
                    Color.TRANSPARENT, DynamicColorUtils.adjustAlpha(
                            mAppliedTextColor, Defaults.ADS_ALPHA_PRESSED), false));

            DynamicMenuUtils.setViewItemsTint(this,
                    mAppliedTextColor, mAppliedColor, false);
        }
    }
}
