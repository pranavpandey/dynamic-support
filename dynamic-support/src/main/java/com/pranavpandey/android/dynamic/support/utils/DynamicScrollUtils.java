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

package com.pranavpandey.android.dynamic.support.utils;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.widget.EdgeEffectCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.lang.reflect.Field;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Helper class to set {@link EdgeEffect} or glow color and scroll bar color for the supported 
 * views dynamically by using reflection. It will be used to match the color with the app's theme.
 */
@RestrictTo(LIBRARY_GROUP)
public final class DynamicScrollUtils {

    /**
     * {@link EdgeEffect} field constant for the edge.
     */
    private static Field F_EDGE_EFFECT_EDGE;
    /**
     * {@link EdgeEffect} field constant for the glow.
     */
    private static Field F_EDGE_EFFECT_GLOW;
    /**
     * {@link EdgeEffectCompat} field constant for the edge effect.
     */
    private static Field F_EDGE_EFFECT_COMPAT_EDGE_EFFECT;

    /**
     * {@link AbsListView} field constant for the top glow.
     */
    private static Field F_LIST_VIEW_EDGE_GLOW_TOP;
    /**
     * {@link AbsListView} field constant for the bottom glow.
     */
    private static Field F_LIST_VIEW_EDGE_GLOW_BOTTOM;
    /**
     * {@link RecyclerView} field constant for the top glow.
     */
    private static Field F_RECYCLER_VIEW_EDGE_GLOW_TOP;
    /**
     * {@link RecyclerView} field constant for the left glow.
     */
    private static Field F_RECYCLER_VIEW_EDGE_GLOW_LEFT;
    /**
     * {@link RecyclerView} field constant for the right glow.
     */
    private static Field F_RECYCLER_VIEW_EDGE_GLOW_RIGHT;
    /**
     * {@link RecyclerView} field constant for the bottom glow.
     */
    private static Field F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM;

    /**
     * {@link ScrollView} field constant for the top glow.
     */
    private static Field F_SCROLL_VIEW_EDGE_GLOW_TOP;
    /**
     * {@link ScrollView} field constant for the bottom glow.
     */
    private static Field F_SCROLL_VIEW_EDGE_GLOW_BOTTOM;
    /**
     * {@link NestedScrollView} field constant for the top glow.
     */
    private static Field F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP;
    /**
     * {@link NestedScrollView} field constant for the bottom glow.
     */
    private static Field F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM;

    /**
     * {@link ViewPager} field constant for the left glow.
     */
    private static Field F_VIEW_PAGER_EDGE_GLOW_LEFT;
    /**
     * {@link ViewPager} field constant for the right glow.
     */
    private static Field F_VIEW_PAGER_EDGE_GLOW_RIGHT;

    /**
     * {@link NavigationView} field constant for the presenter.
     */
    private static Field F_NAVIGATION_VIEW_PRESENTER;
    /**
     * {@link NavigationView} field constant for the recycler view.
     */
    private static Field F_NAVIGATION_VIEW_RECYCLER_VIEW;

    /**
     * Scroll bar field constant for the view.
     */
    private static Field F_VIEW_SCROLL_BAR;

    /**
     * Scroll bar cache constant for the view.
     */
    private static Field V_SCROLL_BAR_FIELD_CACHE;

    /**
     * Scroll bar vertical thumb constant for the view.
     */
    private static Field V_SCROLL_BAR_VERTICAL_THUMB;

    /**
     * Scroll bar horizontal thumb constant for the view.
     */
    private static Field V_SCROLL_BAR_HORIZONTAL_THUMB;

    /**
     * Initialize edge effect or glow fields so that we can access them via reflection.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void initializeEdgeEffectFields() {
        if (F_EDGE_EFFECT_EDGE != null
                && F_EDGE_EFFECT_GLOW != null
                && F_EDGE_EFFECT_COMPAT_EDGE_EFFECT != null) {
            F_EDGE_EFFECT_EDGE.setAccessible(true);
            F_EDGE_EFFECT_GLOW.setAccessible(true);
            F_EDGE_EFFECT_COMPAT_EDGE_EFFECT.setAccessible(true);

            return;
        }

        if (DynamicSdkUtils.is21()) {
            F_EDGE_EFFECT_EDGE = null;
            F_EDGE_EFFECT_GLOW = null;
        } else if (DynamicSdkUtils.is14()) {
            Field edge = null;
            Field glow = null;
            for (Field field : EdgeEffect.class.getDeclaredFields()) {
                switch (field.getName()) {
                    case "mEdge":
                        field.setAccessible(true);
                        edge = field;
                        break;
                    case "mGlow":
                        field.setAccessible(true);
                        glow = field;
                        break;
                }
            }

            F_EDGE_EFFECT_EDGE = edge;
            F_EDGE_EFFECT_GLOW = glow;
        }

        Field edgeEffectCompat = null;
        try {
            edgeEffectCompat = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
        } catch (NoSuchFieldException ignored) {
        }
        F_EDGE_EFFECT_COMPAT_EDGE_EFFECT = edgeEffectCompat;
    }

    /**
     * Initialize recycler view fields so that we can access them via reflection.
     */
    private static void initializeRecyclerViewFields() {
        if (F_RECYCLER_VIEW_EDGE_GLOW_TOP != null
                && F_RECYCLER_VIEW_EDGE_GLOW_LEFT != null
                && F_RECYCLER_VIEW_EDGE_GLOW_RIGHT != null
                && F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM != null) {
            F_RECYCLER_VIEW_EDGE_GLOW_TOP.setAccessible(true);
            F_RECYCLER_VIEW_EDGE_GLOW_LEFT.setAccessible(true);
            F_RECYCLER_VIEW_EDGE_GLOW_RIGHT.setAccessible(true);
            F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = RecyclerView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mTopGlow":
                    field.setAccessible(true);
                    F_RECYCLER_VIEW_EDGE_GLOW_TOP = field;
                    break;
                case "mBottomGlow":
                    field.setAccessible(true);
                    F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM = field;
                    break;
                case "mLeftGlow":
                    field.setAccessible(true);
                    F_RECYCLER_VIEW_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightGlow":
                    field.setAccessible(true);
                    F_RECYCLER_VIEW_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize abs list view fields so that we can access them via reflection.
     */
    private static void initializeListViewFields() {
        if (F_LIST_VIEW_EDGE_GLOW_TOP != null
                && F_LIST_VIEW_EDGE_GLOW_BOTTOM != null) {
            F_LIST_VIEW_EDGE_GLOW_TOP.setAccessible(true);
            F_LIST_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = AbsListView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    F_LIST_VIEW_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    F_LIST_VIEW_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize scroll view fields so that we can access them via reflection.
     */
    private static void initializeScrollViewFields() {
        if (F_SCROLL_VIEW_EDGE_GLOW_TOP != null
                && F_SCROLL_VIEW_EDGE_GLOW_BOTTOM != null) {
            F_SCROLL_VIEW_EDGE_GLOW_TOP.setAccessible(true);
            F_SCROLL_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = ScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    F_SCROLL_VIEW_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    F_SCROLL_VIEW_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize nested scroll view fields so that we can access them via reflection.
     */
    private static void initializeNestedScrollViewFields() {
        if (F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP != null
                && F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM != null) {
            F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP.setAccessible(true);
            F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = NestedScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize view pager fields so that we can access them via reflection.
     */
    private static void initializeViewPagerFields() {
        if (F_VIEW_PAGER_EDGE_GLOW_LEFT != null
                && F_VIEW_PAGER_EDGE_GLOW_RIGHT != null) {
            F_VIEW_PAGER_EDGE_GLOW_LEFT.setAccessible(true);
            F_VIEW_PAGER_EDGE_GLOW_RIGHT.setAccessible(true);

            return;
        }

        Class<?> clazz = ViewPager.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mLeftEdge":
                    field.setAccessible(true);
                    F_VIEW_PAGER_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightEdge":
                    field.setAccessible(true);
                    F_VIEW_PAGER_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize navigation view fields so that we can access them via reflection.
     */
    private static void initializeNavigationViewFields() {
        if (F_NAVIGATION_VIEW_PRESENTER != null
                && F_NAVIGATION_VIEW_RECYCLER_VIEW != null) {
            F_NAVIGATION_VIEW_PRESENTER.setAccessible(true);
            F_NAVIGATION_VIEW_RECYCLER_VIEW.setAccessible(true);

            return;
        }

        Class<?> clazz = NavigationView.class;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("presenter")) {
                field.setAccessible(true);
                F_NAVIGATION_VIEW_PRESENTER = field;

                break;
            }
        }

        Class<?> clazz1 = NavigationMenuPresenter.class;
        for (Field field : clazz1.getDeclaredFields()) {
            if (field.getName().equals("menuView")) {
                field.setAccessible(true);
                F_NAVIGATION_VIEW_RECYCLER_VIEW = field;

                break;
            }
        }
    }

    /**
     * Initialize scroll bar fields so that we can access them via reflection.
     */
    private static void initializeScrollBarFields(@NonNull View view) {
        try {
            if (V_SCROLL_BAR_FIELD_CACHE == null) {
                V_SCROLL_BAR_FIELD_CACHE = View.class.getDeclaredField("mScrollCache");
                V_SCROLL_BAR_FIELD_CACHE.setAccessible(true);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for list view.
     *
     * @param listView The list view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull AbsListView listView, @ColorInt int color) {
        initializeListViewFields();

        try {
            Object edgeEffect = F_LIST_VIEW_EDGE_GLOW_TOP.get(listView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_LIST_VIEW_EDGE_GLOW_BOTTOM.get(listView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for recycler view.
     *
     * @param recyclerView The recycler view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull RecyclerView recyclerView, @ColorInt int color) {
        initializeRecyclerViewFields();

        try {
            Object edgeEffect = F_RECYCLER_VIEW_EDGE_GLOW_TOP.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_RECYCLER_VIEW_EDGE_GLOW_LEFT.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_RECYCLER_VIEW_EDGE_GLOW_RIGHT.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for recycler view.
     *
     * @param recyclerView The recycler view to set the edge effect color.
     * @param color The edge effect color to be set.
     * @param scrollListener Scroll listener to set color on over scroll.
     */
    public static void setEdgeEffectColor(@Nullable RecyclerView recyclerView,
            final @ColorInt int color, @Nullable RecyclerView.OnScrollListener scrollListener) {
        if (recyclerView == null) {
            return;
        }

        if (scrollListener == null) {
            scrollListener =
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView,
                                int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            setEdgeEffectColor(recyclerView, color);
                        }
                    };

            recyclerView.removeOnScrollListener(scrollListener);
            recyclerView.addOnScrollListener(scrollListener);
        }

        setEdgeEffectColor(recyclerView, color);
    }

    /**
     * Set edge effect or glow color for scroll view.
     *
     * @param scrollView The scroll view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull ScrollView scrollView, @ColorInt int color) {
        initializeScrollViewFields();

        try {
            Object edgeEffect = F_SCROLL_VIEW_EDGE_GLOW_TOP.get(scrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_SCROLL_VIEW_EDGE_GLOW_BOTTOM.get(scrollView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for nested scroll view.
     *
     * @param nestedScrollView The nested scroll view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(
            @NonNull NestedScrollView nestedScrollView, @ColorInt int color) {
        initializeNestedScrollViewFields();

        try {
            Object edgeEffect =
                    F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP.get(nestedScrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect =
                    F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM.get(nestedScrollView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for view pager.
     *
     * @param viewPager The view pager to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull ViewPager viewPager, @ColorInt int color) {
        initializeViewPagerFields();

        try {
            Object edgeEffect = F_VIEW_PAGER_EDGE_GLOW_LEFT.get(viewPager);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = F_VIEW_PAGER_EDGE_GLOW_RIGHT.get(viewPager);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for navigation view.
     *
     * @param navigationView The navigation view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(
            @NonNull NavigationView navigationView, @ColorInt int color) {
        initializeNavigationViewFields();

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    F_NAVIGATION_VIEW_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    F_NAVIGATION_VIEW_RECYCLER_VIEW.get(presenter);
            setEdgeEffectColor(navigationMenuView, color, null);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set color of the supplied edge effect object.
     *
     * @param edgeEffect The edge effect object to set the color.
     * @param color The edge effect color to be set.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeEffectColor(@Nullable Object edgeEffect, @ColorInt int color) {
        initializeEdgeEffectFields();

        if (edgeEffect instanceof EdgeEffectCompat) {
            try {
                F_EDGE_EFFECT_COMPAT_EDGE_EFFECT.setAccessible(true);
                edgeEffect = F_EDGE_EFFECT_COMPAT_EDGE_EFFECT.get(edgeEffect);
            } catch (IllegalAccessException illegalAccessException) {
                return;
            }
        }

        if (edgeEffect == null) {
            return;
        }

        if (DynamicSdkUtils.is21()) {
            ((EdgeEffect) edgeEffect).setColor(color);
        } else {
            try {
                F_EDGE_EFFECT_EDGE.setAccessible(true);
                final Drawable mEdge = (Drawable) F_EDGE_EFFECT_EDGE.get(edgeEffect);
                F_EDGE_EFFECT_GLOW.setAccessible(true);
                final Drawable mGlow = (Drawable) F_EDGE_EFFECT_GLOW.get(edgeEffect);
                if (mGlow != null) {
                    mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mGlow.setCallback(null);
                }

                if (mEdge != null) {
                    mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mEdge.setCallback(null);
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Set scroll bar color for navigation view.
     *
     * @param navigationView The navigation view to set the scroll bar color.
     * @param color The edge effect color to be set.
     */
    public static void setScrollBarColor(
            @NonNull NavigationView navigationView, @ColorInt int color) {
        initializeNavigationViewFields();

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    F_NAVIGATION_VIEW_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    F_NAVIGATION_VIEW_RECYCLER_VIEW.get(presenter);
            setScrollBarColor(navigationMenuView, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set scroll bar color for view.
     *
     * @param view The view to set the scroll bar color.
     * @param color The scroll bar color.
     */
    public static void setScrollBarColor(@Nullable View view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeScrollBarFields(view);
        color = DynamicColorUtils.getLessVisibleColor(color);

        if (V_SCROLL_BAR_FIELD_CACHE == null) {
            return;
        }

        try {
            Object mScrollCache = V_SCROLL_BAR_FIELD_CACHE.get(view);

            if (mScrollCache != null) {
                F_VIEW_SCROLL_BAR =
                        mScrollCache.getClass().getDeclaredField("scrollBar");
                F_VIEW_SCROLL_BAR.setAccessible(true);
                Object scrollBar = F_VIEW_SCROLL_BAR.get(mScrollCache);

                if (scrollBar != null) {
                    V_SCROLL_BAR_VERTICAL_THUMB =
                            scrollBar.getClass().getDeclaredField("mVerticalThumb");
                    V_SCROLL_BAR_VERTICAL_THUMB.setAccessible(true);

                    if (V_SCROLL_BAR_VERTICAL_THUMB != null) {
                        DynamicDrawableUtils.colorizeDrawable((Drawable)
                                V_SCROLL_BAR_VERTICAL_THUMB.get(scrollBar), color);
                    }
                }
            }

            // Fix for Android 9 developer preview. For more info, please
            // visit g.co/dev/appcompat.
            if (!DynamicSdkUtils.is28()) {
                V_SCROLL_BAR_HORIZONTAL_THUMB =
                        view.getClass().getDeclaredField("mHorizontalThumb");
                V_SCROLL_BAR_HORIZONTAL_THUMB.setAccessible(true);
                if (V_SCROLL_BAR_HORIZONTAL_THUMB != null) {
                    DynamicDrawableUtils.colorizeDrawable((Drawable)
                            V_SCROLL_BAR_HORIZONTAL_THUMB.get(view), color);
                }
            }
        } catch(Exception ignored) {
        }
    }
}
