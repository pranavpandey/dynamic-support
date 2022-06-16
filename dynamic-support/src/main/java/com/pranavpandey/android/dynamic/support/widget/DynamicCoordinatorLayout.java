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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;

/**
 * A {@link CoordinatorLayout} to draw the status bar background.
 */
public class DynamicCoordinatorLayout extends CoordinatorLayout implements WindowInsetsWidget {

    /**
     * System window insets rect used by this view.
     */
    private Rect mInsets;

    public DynamicCoordinatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicCoordinatorLayout);

        try {
            if (a.getBoolean(
                    R.styleable.DynamicCoordinatorLayout_adt_windowInsets,
                    Defaults.ADS_WINDOW_INSETS)) {
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
        setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener(this,
                new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if (null == mInsets) {
                    mInsets = new Rect();
                }

                mInsets.set(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);

                setWillNotDraw(insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()).equals(Insets.NONE)
                        || getStatusBarBackground() == null);
                return insets;
            }
        });
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        if (mInsets != null && getStatusBarBackground() != null) {
            getStatusBarBackground().setBounds(0, 0, getWidth(), mInsets.top);
            getStatusBarBackground().draw(canvas);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (getStatusBarBackground() != null) {
            getStatusBarBackground().setCallback(this);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (getStatusBarBackground() != null) {
            getStatusBarBackground().setCallback(null);
        }
    }
}
