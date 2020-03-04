/*
 * Copyright 2020 Pranav Pandey
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
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A fixed {@link DrawerLayout} when in persistent or {@link #LOCK_MODE_LOCKED_OPEN} state.
 */
public class DynamicDrawerLayout extends DrawerLayout implements WindowInsetsWidget {

    /**
     * Boolean to disallow the intercept if the drawer is in locked state.
     */
    private boolean mDisallowIntercept;

    public DynamicDrawerLayout(@NonNull Context context) {
        this(context, null);
    }

    public DynamicDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicDrawerLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicDrawerLayout);

        try {
            if (a.getBoolean(
                    R.styleable.DynamicDrawerLayout_ads_windowInsets,
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
        DynamicViewUtils.applyWindowInsetsHorizontal(this, false);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        return !mDisallowIntercept && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setDrawerLockMode(int lockMode) {
        super.setDrawerLockMode(lockMode);

        mDisallowIntercept = (lockMode == LOCK_MODE_LOCKED_OPEN);
    }
}
