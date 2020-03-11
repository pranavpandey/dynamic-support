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
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.base.BaseWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A {@link RelativeLayout} to apply window insets to act as a root view.
 */
public class DynamicRootLayout extends RelativeLayout
        implements BaseWidget, WindowInsetsWidget {

    public DynamicRootLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRootLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicRootLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicRootLayout);

        try {
            if (a.getBoolean(
                    R.styleable.DynamicRootLayout_ads_windowInsets,
                    WidgetDefaults.ADS_WINDOW_INSETS)) {
                applyWindowInsets();
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() { }

    @Override
    public void applyWindowInsets() {
        if (isRootLayout()) {
            DynamicViewUtils.applyWindowInsetsHorizontal(this, true);
        }
    }

    /**
     * Checks if this is the only root layout in the view heirarchy.
     *
     * @return {@code true} if this is the only root layout in the view heirarchy.
     */
    private boolean isRootLayout() {
        if (!(getParent() instanceof View)) {
            return true;
        }

        View view = (View) getParent();
        while (view != null && !(view instanceof DynamicRootLayout)) {
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                view = null;
            }
        }

        return view == null;
    }
}
