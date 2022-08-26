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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicShapeUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

import java.lang.reflect.Field;

/**
 * A {@link NavigationView} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicNavigationView extends NavigationView
        implements WindowInsetsWidget, DynamicBackgroundWidget,
        DynamicScrollableWidget, DynamicStateSelectedWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mColorType;

    /**
     * Color type applied to the background of this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mBackgroundColorType;

    /**
     * Scroll bar color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mScrollBarColorType;

    /**
     * Normal item color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mStateNormalColorType;

    /**
     * Selected item color type applied to this view.
     *
     * @see Theme.ColorType
     */
    protected @Theme.ColorType int mStateSelectedColorType;

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
     * Background color applied to this view.
     */
    protected @ColorInt int mBackgroundColor;

    /**
     * Scroll bar color applied to this view.
     */
    protected @ColorInt int mScrollBarColor;

    /**
     * Scroll bar color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedScrollBarColor;

    /**
     * Normal item color applied to this view.
     */
    protected @ColorInt int mStateNormalColor;

    /**
     * Normal item color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedStateNormalColor;

    /**
     * Selected item color applied to this view.
     */
    protected @ColorInt int mStateSelectedColor;

    /**
     * Selected item color applied to this view after considering the background aware properties.
     */
    protected @ColorInt int mAppliedStateSelectedColor;

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

    public DynamicNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicNavigationView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicNavigationView);

        try {
            mBackgroundColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_backgroundColorType,
                    Theme.ColorType.BACKGROUND);
            mColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_colorType,
                    Defaults.ADS_COLOR_TYPE_EDGE_EFFECT);
            mScrollBarColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_scrollBarColorType,
                    Defaults.ADS_COLOR_TYPE_SCROLLABLE);
            mStateNormalColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_stateNormalColorType,
                    Theme.ColorType.TEXT_PRIMARY);
            mStateSelectedColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_stateSelectedColorType,
                    Theme.ColorType.ACCENT);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicNavigationView_adt_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mBackgroundColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_backgroundColor,
                    Theme.Color.UNKNOWN);
            mColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_color,
                    Theme.Color.UNKNOWN);
            mScrollBarColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_scrollBarColor,
                    Theme.Color.UNKNOWN);
            mStateNormalColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_stateNormalColor,
                    Theme.Color.UNKNOWN);
            mStateSelectedColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_stateSelectedColor,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicNavigationView_adt_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicNavigationView_adt_backgroundAware,
                    Defaults.getBackgroundAware());
            mContrast = a.getInteger(
                    R.styleable.DynamicNavigationView_adt_contrast,
                    Theme.Contrast.AUTO);

            if (a.getBoolean(R.styleable.DynamicNavigationView_adt_dynamicCornerSize,
                    Defaults.ADS_DYNAMIC_CORNER_SIZE)) {
                updateBackground();
            }

            if (a.getBoolean(
                    R.styleable.DynamicNavigationView_adt_windowInsets,
                    Defaults.ADS_WINDOW_INSETS)) {
                applyWindowInsets();
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    /**
     * Update background according to the corner size.
     */
    private void updateBackground() {
        if (getBackground() instanceof MaterialShapeDrawable) {
            MaterialShapeDrawable drawable = (MaterialShapeDrawable) getBackground();
            ShapeAppearanceModel.Builder builder =
                    drawable.getShapeAppearanceModel().toBuilder();
            float cornerSize = DynamicTheme.getInstance().get().getCornerRadius();

            builder.setTopLeftCornerSize(Theme.Corner.MIN);
            builder.setTopRightCornerSize(Theme.Corner.MIN);

            if (drawable.getBottomLeftCornerResolvedSize() > 0) {
                builder.setBottomLeftCornerSize(cornerSize);
            }

            if (drawable.getBottomRightCornerResolvedSize() > 0) {
                builder.setBottomRightCornerSize(cornerSize);
            }

            drawable.setShapeAppearanceModel(builder.build());
        }
    }

    @Override
    public void initialize() {
        if (mBackgroundColorType != Theme.ColorType.NONE
                && mBackgroundColorType != Theme.ColorType.CUSTOM) {
            mBackgroundColor = DynamicTheme.getInstance()
                    .resolveColorType(mBackgroundColorType);
        }

        if (mColorType != Theme.ColorType.NONE
                && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mScrollBarColorType != Theme.ColorType.NONE
                && mScrollBarColorType != Theme.ColorType.CUSTOM) {
            mScrollBarColor = DynamicTheme.getInstance()
                    .resolveColorType(mScrollBarColorType);
        }

        if (mStateNormalColorType != Theme.ColorType.NONE
                && mStateNormalColorType != Theme.ColorType.CUSTOM) {
            mStateNormalColor = DynamicTheme.getInstance()
                    .resolveColorType(mStateNormalColorType);
        }

        if (mStateSelectedColorType != Theme.ColorType.NONE
                && mStateSelectedColorType != Theme.ColorType.CUSTOM) {
            mStateSelectedColor = DynamicTheme.getInstance()
                    .resolveColorType(mStateSelectedColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setBackgroundColor(mBackgroundColor);
    }

    @Override
    public void applyWindowInsets() {
        final NavigationMenuView menuView = DynamicScrollUtils.getNavigationMenuView(this);
        final View header;
        final int left = getPaddingLeft();
        final int top = getPaddingTop();
        final int right = getPaddingRight();
        final int bottom = getPaddingBottom();
        final int menuLeft;
        final int menuTop;
        final int menuRight;
        final int menuBottom;
        final int headerLeft;
        final int headerTop;
        final int headerRight;
        final int headerBottom;

        if (menuView != null) {
            menuLeft = menuView.getPaddingLeft();
            menuTop = menuView.getPaddingTop();
            menuRight = menuView.getPaddingRight();
            menuBottom = menuView.getPaddingBottom();
        } else {
            menuLeft = 0;
            menuTop = 0;
            menuRight = 0;
            menuBottom = 0;
        }

        if (getHeaderCount() != 0) {
            header = getHeaderView(0);
            headerLeft = header.getPaddingLeft();
            headerTop = header.getPaddingTop();
            headerRight = header.getPaddingRight();
            headerBottom = header.getPaddingBottom();
        } else {
            header = null;
            headerLeft = 0;
            headerTop = 0;
            headerRight = 0;
            headerBottom = 0;
        }

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public @NonNull WindowInsetsCompat onApplyWindowInsets(
                    @NonNull View v, @NonNull WindowInsetsCompat insets) {
                final Rect rect = new Rect();
                rect.set(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);

                try {
                    final Field fieldInsets =
                            ScrimInsetsFrameLayout.class.getDeclaredField("insets");
                    fieldInsets.setAccessible(true);
                    fieldInsets.set(DynamicNavigationView.this, rect);
                } catch (Exception ignored) {
                }

                v.setPadding(left + rect.left, top,
                        right + rect.right, bottom + rect.bottom);

                if (menuView != null) {
                    menuView.setPadding(menuLeft, header != null ? menuTop : menuTop + rect.top,
                            menuRight, menuBottom + insets.getInsetsIgnoringVisibility(
                                    WindowInsetsCompat.Type.navigationBars()).bottom);
                }

                if (header != null) {
                    header.setPadding(headerLeft, headerTop + rect.top,
                            headerRight, headerBottom);
                }

                return insets;
            }
        });

        DynamicViewUtils.requestApplyWindowInsets(this);
    }

    @Override
    public @Theme.ColorType int getBackgroundColorType() {
        return mBackgroundColorType;
    }

    @Override
    public void setBackgroundColorType(@Theme.ColorType int backgroundColorType) {
        this.mBackgroundColorType = backgroundColorType;

        initialize();
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
    public @Theme.ColorType int getScrollBarColorType() {
        return mScrollBarColorType;
    }

    @Override
    public void setScrollBarColorType(@Theme.ColorType int scrollBarColorType) {
        this.mScrollBarColorType = scrollBarColorType;

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
    public @Theme.ColorType int getStateSelectedColorType() {
        return mStateSelectedColorType;
    }

    @Override
    public void setStateSelectedColorType(@Theme.ColorType int stateSelectedColorType) {
        this.mStateSelectedColorType = stateSelectedColorType;

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
    public @ColorInt int getBackgroundColor() {
        return mBackgroundColor ;
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

        setScrollableWidgetColor(false);
    }

    @Override
    public @ColorInt int getScrollBarColor(boolean resolve) {
        return resolve ? mAppliedScrollBarColor : mScrollBarColor;
    }

    @Override
    public @ColorInt int getScrollBarColor() {
        return getScrollBarColor(true);
    }

    @Override
    public void setScrollBarColor(@ColorInt int scrollBarColor) {
        this.mScrollBarColorType = Theme.ColorType.CUSTOM;
        this.mScrollBarColor = scrollBarColor;

        setScrollBarColor();
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

        setStatesColor();
    }

    @Override
    public @ColorInt int getStateSelectedColor(boolean resolve) {
        return resolve ? mAppliedStateSelectedColor : mStateSelectedColor;
    }

    @Override
    public @ColorInt int getStateSelectedColor() {
        return getStateSelectedColor(true);
    }

    @Override
    public void setStateSelectedColor(@ColorInt int stateSelectedColor) {
        this.mStateSelectedColorType = Theme.ColorType.CUSTOM;
        this.mStateSelectedColor = stateSelectedColor;

        setStatesColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setBackgroundColor(getBackgroundColor());
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

        setBackgroundColor(getBackgroundColor());
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

        updateBackground();
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        if (getBackground() instanceof MaterialShapeDrawable) {
            DynamicDrawableUtils.colorizeDrawable(getBackground(),
                    Dynamic.withThemeOpacity(backgroundColor, Theme.Opacity.WIDGET));
        } else {
            super.setBackgroundColor(Dynamic.withThemeOpacity(
                    backgroundColor, Theme.Opacity.WIDGET));
        }

        this.mBackgroundColor = backgroundColor;
        this.mBackgroundColorType = Theme.ColorType.CUSTOM;

        setScrollableWidgetColor(true);
        setStatesColor();
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = Dynamic.withContrastRatio(mColor, mContrastWithColor, this);
            }

            DynamicScrollUtils.setEdgeEffectColor(this, mAppliedColor);
            DynamicScrollUtils.setSubHeaderColor(this, mAppliedColor);
        }
    }

    @Override
    public void setScrollBarColor() {
        if (mScrollBarColor != Theme.Color.UNKNOWN) {
            mAppliedScrollBarColor = mScrollBarColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedScrollBarColor = Dynamic.withContrastRatio(
                        mScrollBarColor, mContrastWithColor, this);
            }

            DynamicScrollUtils.setScrollBarColor(this, mAppliedScrollBarColor);
        }
    }

    @Override
    public void setScrollableWidgetColor(boolean setScrollBarColor) {
        setColor();

        if (setScrollBarColor) {
            setScrollBarColor();
        }
    }

    /**
     * Set selected item color of this view according to the supplied values.
     */
    public void setStatesColor() {
        if (mStateSelectedColor != Theme.Color.UNKNOWN) {
            mAppliedStateNormalColor = mStateNormalColor;
            mAppliedStateSelectedColor = mStateSelectedColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedStateNormalColor = Dynamic.withContrastRatio(
                        mStateNormalColor, mContrastWithColor, this);
                mAppliedStateSelectedColor = Dynamic.withContrastRatio(
                        mStateSelectedColor, mContrastWithColor, this);
            }

            setItemBackgroundResource(DynamicShapeUtils.getListSelectorRes(
                    DynamicTheme.getInstance().get().getCornerSize()));

            DynamicDrawableUtils.colorizeDrawable(getItemBackground(),
                    DynamicColorUtils.getStateColor(mAppliedStateSelectedColor,
                            Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK));
            DynamicTintUtils.colorizeRippleDrawable(this, getItemBackground(),
                    mContrastWithColor, mAppliedStateSelectedColor, false, true);

            if (getItemIconTintList() != null) {
                setItemIconTintList(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemIconTintList(), mAppliedStateNormalColor,
                        mAppliedStateSelectedColor));
            }

            if (getItemTextColor() != null) {
                setItemTextColor(DynamicResourceUtils.convertColorStateListWithNormal(
                        getItemTextColor(), mAppliedStateNormalColor, mAppliedStateSelectedColor));
            }
        }
    }
}
