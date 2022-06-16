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
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.behavior.DynamicBottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBottomSheetWidget;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;
import com.pranavpandey.android.dynamic.util.DynamicWindowUtils;

/**
 * A {@link FrameLayout} to use as bottom sheet by applying the window insets and adjusting
 * the peek height.
 */
public class DynamicBottomSheet extends FrameLayout implements
        DynamicBottomSheetWidget, WindowInsetsWidget {

    /**
     * Bottom sheet behaviour used by this bottom sheet.
     */
    private BottomSheetBehavior<DynamicBottomSheet> mBottomSheetBehavior;

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
                    R.styleable.DynamicBottomSheet_adt_windowInsets,
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
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBottomSheetBehavior() != null) {
                    getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    @Override
    public void applyWindowInsets() {
        if (getBottomSheetBehavior() == null) {
            return;
        }

        final int left = getPaddingLeft();
        final int top = getPaddingTop();
        final int right = getPaddingRight();
        final int bottom = getPaddingBottom();
        final int peek = getBottomSheetBehavior().getPeekHeight();

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public @NonNull WindowInsetsCompat onApplyWindowInsets(
                    @NonNull View v, @NonNull WindowInsetsCompat insets) {
                final boolean isRtl = DynamicViewUtils.isLayoutRtl(v);
                v.setPadding(isRtl ? right : left
                                + insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                        top - insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                        isRtl ? left : right
                                + insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                        bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
                /*
                 * Fix extra peek height when using top inset for coordinator layout and
                 * dynamic bottom sheet behavior.
                 */
                getBottomSheetBehavior().setPeekHeight(peek + (
                        !DynamicWindowUtils.isNavigationBarPresent(getContext())
                                && getBottomSheetBehavior() instanceof DynamicBottomSheetBehavior
                                ? -insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                                : insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom));

                return insets;
            }
        });

        DynamicViewUtils.requestApplyWindowInsets(this);
    }

    @Override
    public @Nullable BottomSheetBehavior<?> getBottomSheetBehavior() {
        if (!(getParent() instanceof CoordinatorLayout)) {
            return null;
        } else if (mBottomSheetBehavior == null) {
            mBottomSheetBehavior = BottomSheetBehavior.from(this);
            applyWindowInsets();
        }

        return mBottomSheetBehavior;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mBottomSheetBehavior = null;
    }
}
