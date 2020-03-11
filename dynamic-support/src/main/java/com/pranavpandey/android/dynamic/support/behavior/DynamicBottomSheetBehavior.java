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

package com.pranavpandey.android.dynamic.support.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.R;

/**
 * A {@link BottomSheetBehavior} behavior to automatically hide it according to the nested scroll.
 * Set this behavior in the layout file having {@link CoordinatorLayout} as root element to hide
 * the bottom sheet according to the scroll direction.
 *
 * <p><p>Scrolling in downwards direction will hide the bottom sheet.
 */
public class DynamicBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    public DynamicBottomSheetBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent,
            @NonNull V child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency)
                || (dependency.getId() == R.id.ads_bottom_sheet
                && dependency.getMeasuredHeight() > 0);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child, @NonNull View directTargetChild,
            @NonNull View target, int nestedScrollAxes, final int type) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        if (dyConsumed > 0) {
            if (getState() != STATE_COLLAPSED && getState() != STATE_SETTLING) {
                setState(STATE_COLLAPSED);
            }
        }
    }
}
