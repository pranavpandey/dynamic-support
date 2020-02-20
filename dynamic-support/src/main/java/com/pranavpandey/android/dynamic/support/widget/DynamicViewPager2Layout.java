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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

/**
 * A {@link DynamicFrameLayout} to provide nested scrolling in the same direction inside
 * a {@link ViewPager2}.
 *
 * @see <a href="https://developer.android.com/training/animation/vp2-migration#nested-scrollables">More info</a>
 */
public class DynamicViewPager2Layout extends DynamicFrameLayout {

    /**
     * Scaled touch slop used by the view configuration.
     */
    private int mTouchSlop;

    /**
     * Initial horizontal position.
     */
    private float mInitialX;

    /**
     * Initial vertical position.
     */
    private float mInitialY;

    public DynamicViewPager2Layout(@NonNull Context context) {
        super(context);
    }

    public DynamicViewPager2Layout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicViewPager2Layout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        super.loadFromAttributes(attrs);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        handleInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Checks whether a child can be scrolled.
     *
     * @param orientation The current orientation of the view.
     * @param delta The delta to check the scrolling.
     *
     * @return Returns {@code true} if a child can be scrolled.
     */
    private boolean canChildScroll(int orientation, float delta) {
        if (getChildCount() <= 0) {
            return false;
        }

        int direction = -((int) Math.signum(delta));
        switch (orientation) {
            default:
                throw new IllegalArgumentException();
            case ViewPager2.ORIENTATION_HORIZONTAL:
                return getChildAt(0).canScrollHorizontally(direction);
            case ViewPager2.ORIENTATION_VERTICAL:
                return getChildAt(0).canScrollVertically(direction);
        }
    }

    /**
     * Returns the parent view pager for this view.
     */
    private @Nullable ViewPager2 getParentViewPager() {
        if (!(getParent() instanceof View)) {
            return null;
        }

        View view = (View) getParent();
        while (view != null && !(view instanceof ViewPager2)) {
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                view = null;
            }
        }

        return (view instanceof ViewPager2) ? (ViewPager2) view : null;
    }

    /**
     * Handle touch event to support nested {@link ViewPager2} scrolling.
     *
     * @param ev The motion event being dispatched down the hierarchy.
     */
    private void handleInterceptTouchEvent(@Nullable MotionEvent ev) {
        ViewPager2 viewPager2 = getParentViewPager();

        if (ev == null || viewPager2 == null) {
            return;
        }

        int orientation = viewPager2.getOrientation();

        // Early return if child can't scroll in same direction as parent.
        if (!canChildScroll(orientation, -1.0f) && !canChildScroll(orientation, 1.0f)) {
            return;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mInitialX = ev.getX();
            mInitialY = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = ev.getX() - mInitialX;
            float dy = ev.getY() - mInitialY;
            boolean isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL;

            // Assuming ViewPager2 touch-slop is 2x touch-slop of child.
            float scaledDx = Math.abs(dx) * (isVpHorizontal ? 0.5f : 1.0f);
            float scaledDy = Math.abs(dy) * (isVpHorizontal ? 1.0f : 0.5f);

            if (scaledDx > mTouchSlop || scaledDy > mTouchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept.
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (canChildScroll(orientation, isVpHorizontal ? dx : dy)) {
                        // Child can scroll, disallow all parents to intercept.
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        // Child cannot scroll, allow all parents to intercept.
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
    }
}
