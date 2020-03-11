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
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;

/**
 * A {@link FrameLayout} to use as bottom sheet by applying the window insets and adjusting
 * the peek height.
 */
public class DynamicBottomSheet extends FrameLayout implements WindowInsetsWidget {

    public DynamicBottomSheet(@NonNull Context context) {
        this(context, null);
    }

    public DynamicBottomSheet(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicBottomSheet(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicBottomSheet);

        try {
            if (a.getBoolean(
                    R.styleable.DynamicBottomSheet_ads_windowInsets,
                    WidgetDefaults.ADS_WINDOW_INSETS)) {
                applyWindowInsets();
            }
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetBehavior.from(v).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void applyWindowInsets() {
        if (getParent() == null || !(getParent() instanceof CoordinatorLayout)) {
            return;
        }

        final int paddingBottom = getPaddingBottom();
        final int peekHeight = BottomSheetBehavior.from(this).getPeekHeight();
        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                        paddingBottom + insets.getSystemWindowInsetBottom());
                BottomSheetBehavior.from(v).setPeekHeight(
                        peekHeight + insets.getSystemWindowInsetBottom());
                insets.consumeSystemWindowInsets();

                return insets;
            }
        });
    }
}
