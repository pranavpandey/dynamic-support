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

package com.pranavpandey.android.dynamic.support.behavior;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pranavpandey.android.dynamic.support.utils.DynamicFABUtils;

import java.util.List;

/**
 * A {@link FloatingActionButton} behavior to automatically show or hide it according to the
 * nested scroll. Set this behavior in the layout file having {@link CoordinatorLayout} as root
 * element to show or hide the FAB according to the scroll direction.
 *
 * <p><p>Scrolling in upwards direction will hide the FAB and scrolling in downwards direction
 * will make it visible.
 */
public class DynamicFABScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

    public DynamicFABScrollBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency)
                || dependency instanceof FloatingActionButton
                || dependency instanceof ExtendedFloatingActionButton;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child, @NonNull View directTargetChild,
            @NonNull View target, int nestedScrollAxes, final int type) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        if (dyConsumed > 0) {
            // User scrolled down -> hide the FAB
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for (View view : dependencies) {
                if (view instanceof FloatingActionButton
                        && ((FloatingActionButton) view).getDrawable() != null) {
                    DynamicFABUtils.hide((FloatingActionButton) view);
                } else if (view instanceof ExtendedFloatingActionButton
                        && (((ExtendedFloatingActionButton) view).getIcon() != null
                        || !TextUtils.isEmpty(((ExtendedFloatingActionButton) view).getText()))) {
                    DynamicFABUtils.hide((ExtendedFloatingActionButton) view, true);
                }
            }
        } else if (dyConsumed < 0) {
            // User scrolled up -> show the FAB
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for (View view : dependencies) {
                if (view instanceof FloatingActionButton
                        && ((FloatingActionButton) view).getDrawable() != null) {
                    DynamicFABUtils.show((FloatingActionButton) view);
                } else if (view instanceof ExtendedFloatingActionButton
                        && (((ExtendedFloatingActionButton) view).getIcon() != null
                        || !TextUtils.isEmpty(((ExtendedFloatingActionButton) view).getText()))) {
                    DynamicFABUtils.show((ExtendedFloatingActionButton) view, true);
                }
            }
        }
    }
}
