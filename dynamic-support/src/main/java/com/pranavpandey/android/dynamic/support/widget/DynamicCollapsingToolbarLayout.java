/*
 * Copyright 2018-2020 Pranav Pandey
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
import android.view.Gravity;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicRtlWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;

/**
 * A {@link CollapsingToolbarLayout} to provide support for RTL (right-to-left) layouts.
 */
public class DynamicCollapsingToolbarLayout extends CollapsingToolbarLayout
        implements WindowInsetsWidget, DynamicRtlWidget {

    /**
     * {@code true} if dynamic RTL support is enabled for this widget.
     */
    private boolean mRtlSupport;

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
                        R.styleable.DynamicCollapsingToolbarLayout_ads_rtlSupport,
                        WidgetDefaults.ADS_RTL_SUPPORT);

                if (a.getBoolean(
                        R.styleable.DynamicCollapsingToolbarLayout_ads_windowInsets,
                        WidgetDefaults.ADS_WINDOW_INSETS)) {
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
        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(),
                        v.getPaddingRight(), v.getPaddingBottom());

                return insets;
            }
        });
    }

    @Override
    public boolean isRtlSupport() {
        return mRtlSupport;
    }

    @Override
    public void setRtlSupport(boolean rtlSupport) {
        this.mRtlSupport = rtlSupport;

        if (mRtlSupport && DynamicLocaleUtils.isLayoutRtl()) {
            setExpandedTitleGravity(Gravity.END | Gravity.BOTTOM);
            setCollapsedTitleGravity(Gravity.END);
        } else {
            setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);
            setCollapsedTitleGravity(Gravity.START);
        }
    }
}
