/*
 * Copyright 2018 Pranav Pandey
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

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.widget.base.BaseWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicRtlWidget;

/**
 * A CollapsingToolbarLayout to provide support for Right to Left (RTL) layouts.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DynamicCollapsingToolbarLayout extends CollapsingToolbarLayout
        implements BaseWidget, DynamicRtlWidget {

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
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, new int[]{R.attr.ads_rtlSupport});

        try {
            if (attrs != null) {
                mRtlSupport = a.getBoolean(0, WidgetDefaults.ADS_RTL_SUPPORT);
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
    public boolean isRtlSupport() {
        return mRtlSupport;
    }

    @Override
    public void setRtlSupport(boolean rtlSupport) {
        this.mRtlSupport = rtlSupport;

        if (mRtlSupport) {
            if (DynamicLocaleUtils.isLayoutRtl()) {
                setExpandedTitleGravity(Gravity.END | Gravity.BOTTOM);
                setCollapsedTitleGravity(Gravity.END);
            } else {
                setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);
                setCollapsedTitleGravity(Gravity.START);
            }
        } else {
            setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);
            setCollapsedTitleGravity(Gravity.START);
        }
    }
}
