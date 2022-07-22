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
import android.view.Gravity;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicRtlWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

/**
 * A {@link CollapsingToolbarLayout} to provide support for RTL (right-to-left) layouts.
 */
public class DynamicCollapsingToolbarLayout extends CollapsingToolbarLayout
        implements WindowInsetsWidget, DynamicRtlWidget {

    /**
     * {@code true} if dynamic RTL support is enabled for this widget.
     */
    protected boolean mRtlSupport;

    public DynamicCollapsingToolbarLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicCollapsingToolbarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicCollapsingToolbarLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicCollapsingToolbarLayout);

        try {
            if (attrs != null) {
                mRtlSupport = a.getBoolean(
                        R.styleable.DynamicCollapsingToolbarLayout_adt_rtlSupport,
                        Defaults.ADS_RTL_SUPPORT);

                if (a.getBoolean(
                        R.styleable.DynamicCollapsingToolbarLayout_adt_windowInsets,
                        Defaults.ADS_WINDOW_INSETS)) {
                    applyWindowInsets();
                }
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        setRtlSupport(mRtlSupport);
    }

    @Override
    public void applyWindowInsets() {
        DynamicViewUtils.applyWindowInsetsHorizontal(this);
    }

    @Override
    public void setStatusBarScrimColor(@ColorInt int color) {
        super.setStatusBarScrimColor(Dynamic.withThemeOpacity(color));
    }

    @Override
    public void setContentScrimColor(@ColorInt int color) {
        super.setContentScrimColor(DynamicTheme.getInstance().get().isTranslucent()
                ? Color.TRANSPARENT : Dynamic.withThemeOpacity(color));
    }

    @Override
    public boolean isRtlSupport() {
        return mRtlSupport;
    }

    @Override
    public void setRtlSupport(boolean rtlSupport) {
        this.mRtlSupport = rtlSupport;

        if (mRtlSupport && DynamicViewUtils.isLayoutRtl(this)) {
            setExpandedTitleGravity(Gravity.END | Gravity.BOTTOM);
            setCollapsedTitleGravity(Gravity.END);
        } else {
            setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);
            setCollapsedTitleGravity(Gravity.START);
        }
    }
}
