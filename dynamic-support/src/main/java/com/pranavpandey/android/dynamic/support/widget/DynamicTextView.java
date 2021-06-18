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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicLinkWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicRtlWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * A {@link MaterialTextView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DynamicTextView extends MaterialTextView implements DynamicWidget,
        DynamicLinkWidget, DynamicRtlWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

    /**
     * Link color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mLinkColorType;

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
     * Link color applied to this view.
     */
    private @ColorInt int mLinkColor;

    /**
     * Link color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedLinkColor;

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
     * Original text appearance attribute resource.
     */
    private @AttrRes int mAppearanceAttrRes;

    /**
     * Original text color attribute resource.
     */
    private @AttrRes int mColorAttrRes;

    /**
     * {@code true} if dynamic RTL support is enabled for this widget.
     */
    private boolean mRtlSupport;

    public DynamicTextView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicTextView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicTextView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicTextView_adt_colorType,
                    Theme.ColorType.NONE);
            mLinkColorType = a.getInt(
                    R.styleable.DynamicTextView_adt_linkColorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTextView_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicTextView_adt_color,
                    Theme.Color.UNKNOWN);
            mLinkColor = a.getColor(
                    R.styleable.DynamicTextView_adt_linkColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicTextView_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicTextView_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mRtlSupport = a.getBoolean(
                    R.styleable.DynamicTextView_adt_rtlSupport,
                    Defaults.ADS_RTL_SUPPORT);

            if (attrs != null) {
                mAppearanceAttrRes = DynamicResourceUtils.getResourceIdFromAttributes(
                        getContext(), attrs, android.R.attr.textAppearance);
                mColorAttrRes = DynamicResourceUtils.getResourceIdFromAttributes(
                        getContext(), attrs, android.R.attr.textColor);
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        if (mColorType == Theme.ColorType.NONE) {
            if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorPrimary)) {
                mColorType = Theme.ColorType.TEXT_PRIMARY;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorSecondary)) {
                mColorType = Theme.ColorType.TEXT_SECONDARY;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorPrimaryInverse)) {
                mColorType = Theme.ColorType.TEXT_PRIMARY_INVERSE;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorSecondaryInverse)) {
                mColorType = Theme.ColorType.TEXT_SECONDARY_INVERSE;
            } else {
                mColorType = Theme.ColorType.TEXT_PRIMARY;
            }

            if (mAppearanceAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textAppearancePopupMenuHeader)
                    || mAppearanceAttrRes == DynamicResourceUtils.getResourceId(getContext(),
                    androidx.appcompat.R.attr.textAppearancePopupMenuHeader)) {
                mColorType = Theme.ColorType.PRIMARY;
                mContrastWithColorType = Theme.ColorType.SURFACE;
            }
        }

        if (mLinkColorType == Theme.ColorType.NONE) {
            if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorPrimary)) {
                mLinkColorType = Theme.ColorType.TEXT_PRIMARY;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorSecondary)) {
                mLinkColorType = Theme.ColorType.TEXT_SECONDARY;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorPrimaryInverse)) {
                mLinkColorType = Theme.ColorType.TEXT_PRIMARY_INVERSE;
            } else if (mColorAttrRes == DynamicResourceUtils.getResourceId(
                    getContext(), android.R.attr.textColorSecondaryInverse)) {
                mLinkColorType = Theme.ColorType.TEXT_SECONDARY_INVERSE;
            } else {
                mLinkColorType = Theme.ColorType.TEXT_PRIMARY;
            }
        }

        if (mColorType != Theme.ColorType.NONE && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mLinkColorType != Theme.ColorType.NONE && mLinkColorType != Theme.ColorType.CUSTOM) {
            mLinkColor = DynamicTheme.getInstance().resolveColorType(mLinkColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor();
        setLinkColor();
        setRtlSupport(mRtlSupport);
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
    public @Theme.ColorType int getLinkColorType() {
        return mLinkColorType;
    }

    @Override
    public void setLinkColorType(@Theme.ColorType int linkColorType) {
        this.mLinkColorType = linkColorType;

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
    public @ColorInt int getLinkColor(boolean resolve) {
        return resolve ? mAppliedLinkColor : mLinkColor;
    }

    @Override
    public @ColorInt int getLinkColor() {
        return getLinkColor(true);
    }

    @Override
    public void setLinkColor(@ColorInt int linkColor) {
        this.mLinkColorType = Theme.ColorType.CUSTOM;
        this.mLinkColor = linkColor;

        setLinkColor();
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
        setLinkColor();
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
        setLinkColor();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (mColorType != Theme.ColorType.NONE) {
            setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
        } else {
            setAlpha(Defaults.ADS_ALPHA_ENABLED);
        }
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            setTextColor(mAppliedColor);
            setHintTextColor(DynamicColorUtils.adjustAlpha(
                    mAppliedColor, Defaults.ADS_ALPHA_HINT));
        }

        setHighlightColor(DynamicColorUtils.getContrastColor(
                getCurrentTextColor(), getCurrentTextColor()));
    }

    @Override
    public void setLinkColor() {
        if (mLinkColor != Theme.Color.UNKNOWN) {
            mAppliedLinkColor = mLinkColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedLinkColor = DynamicColorUtils.getContrastColor(
                        mLinkColor, mContrastWithColor);
            }

            setLinkTextColor(mAppliedLinkColor);
        }
    }

    @Override
    public boolean isRtlSupport() {
        return mRtlSupport;
    }

    @Override
    public void setRtlSupport(boolean rtlSupport) {
        this.mRtlSupport = rtlSupport;

        if (mRtlSupport) {
            if (DynamicSdkUtils.is17()) {
                setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            } else {
                setGravity(getGravity() | Gravity.START);
            }
        }
    }
}
