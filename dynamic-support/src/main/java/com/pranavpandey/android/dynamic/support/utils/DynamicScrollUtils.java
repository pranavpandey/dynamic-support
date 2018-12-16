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
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

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
    private static Field ADS_EDGE_EFFECT_FIELD_EDGE;
    /**
     * {@link EdgeEffect} field constant for the glow.
     */
    private static Field ADS_EDGE_EFFECT_FIELD_GLOW;
    /**
     * {@link EdgeEffectCompat} field constant for the edge effect.
     */
    private static Field ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT;

    /**
     * {@link AbsListView} field constant for the top glow.
     */
    private static Field ADS_LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link AbsListView} field constant for the bottom glow.
     */
    private static Field ADS_LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;
    /**
     * {@link RecyclerView} field constant for the top glow.
     */
    private static Field ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link RecyclerView} field constant for the left glow.
     */
    private static Field ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT;
    /**
     * {@link RecyclerView} field constant for the right glow.
     */
    private static Field ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT;
    /**
     * {@link RecyclerView} field constant for the bottom glow.
     */
    private static Field ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    /**
     * {@link ScrollView} field constant for the top glow.
     */
    private static Field ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link ScrollView} field constant for the bottom glow.
     */
    private static Field ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;
    /**
     * {@link NestedScrollView} field constant for the top glow.
     */
    private static Field ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link NestedScrollView} field constant for the bottom glow.
     */
    private static Field ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    /**
     * {@link ViewPager} field constant for the left glow.
     */
    private static Field ADS_VIEW_PAGER_FIELD_EDGE_GLOW_LEFT;
    /**
     * {@link ViewPager} field constant for the right glow.
     */
    private static Field ADS_VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT;

    /**
     * {@link NavigationView} field constant for the presenter.
     */
    private static Field ADS_NAVIGATION_VIEW_FIELD_PRESENTER;
    /**
     * {@link NavigationView} field constant for the recycler view.
     */
    private static Field ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW;

    /**
     * Scroll bar field constant for the view.
     */
    private static Field ADS_VIEW_SCROLL_BAR_FIELD;

    /**
     * Scroll bar cache constant for the view.
     */
    private static Field ADS_VIEW_SCROLL_BAR_FIELD_CACHE;

    /**
     * Scroll bar vertical thumb constant for the view.
     */
    private static Field ADS_VIEW_SCROLL_BAR_VERTICAL_THUMB;

    /**
     * Scroll bar horizontal thumb constant for the view.
     */
    private static Field ADS_VIEW_SCROLL_BAR_HORIZONTAL_THUMB;

    /**
     * Initialize edge effect or glow fields so that we can access them via reflection.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void initializeEdgeEffectFields() {
        if (ADS_EDGE_EFFECT_FIELD_EDGE != null
                && ADS_EDGE_EFFECT_FIELD_GLOW != null
                && ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT != null) {
            ADS_EDGE_EFFECT_FIELD_EDGE.setAccessible(true);
            ADS_EDGE_EFFECT_FIELD_GLOW.setAccessible(true);
            ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);

            return;
        }

        if (DynamicVersionUtils.isLollipop()) {
            ADS_EDGE_EFFECT_FIELD_EDGE = null;
            ADS_EDGE_EFFECT_FIELD_GLOW = null;
        } else if (DynamicVersionUtils.isIceCreamSandwich()) {
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

            ADS_EDGE_EFFECT_FIELD_EDGE = edge;
            ADS_EDGE_EFFECT_FIELD_GLOW = glow;
        }

        Field edgeEffectCompat = null;
        try {
            edgeEffectCompat = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
        } catch (NoSuchFieldException ignored) {
        }
        ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT = edgeEffectCompat;
    }

    /**
     * Initialize recycler view fields so that we can access them via reflection.
     */
    private static void initializeRecyclerViewFields() {
        if (ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP != null
                && ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT != null
                && ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT != null
                && ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);
            ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = RecyclerView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mTopGlow":
                    field.setAccessible(true);
                    ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mBottomGlow":
                    field.setAccessible(true);
                    ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
                case "mLeftGlow":
                    field.setAccessible(true);
                    ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightGlow":
                    field.setAccessible(true);
                    ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize abs list view fields so that we can access them via reflection.
     */
    private static void initializeListViewFields() {
        if (ADS_LIST_VIEW_FIELD_EDGE_GLOW_TOP != null
                && ADS_LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            ADS_LIST_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            ADS_LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = AbsListView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    ADS_LIST_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    ADS_LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize scroll view fields so that we can access them via reflection.
     */
    private static void initializeScrollViewFields() {
        if (ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null
                && ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = ScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize nested scroll view fields so that we can access them via reflection.
     */
    private static void initializeNestedScrollViewFields() {
        if (ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null
                && ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = NestedScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize view pager fields so that we can access them via reflection.
     */
    private static void initializeViewPagerFields() {
        if (ADS_VIEW_PAGER_FIELD_EDGE_GLOW_LEFT != null
                && ADS_VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT != null) {
            ADS_VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            ADS_VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);

            return;
        }

        Class<?> clazz = ViewPager.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mLeftEdge":
                    field.setAccessible(true);
                    ADS_VIEW_PAGER_FIELD_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightEdge":
                    field.setAccessible(true);
                    ADS_VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize navigation view fields so that we can access them via reflection.
     */
    private static void initializeNavigationViewFields() {
        if (ADS_NAVIGATION_VIEW_FIELD_PRESENTER != null
                && ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW != null) {
            ADS_NAVIGATION_VIEW_FIELD_PRESENTER.setAccessible(true);
            ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.setAccessible(true);

            return;
        }

        Class<?> clazz = NavigationView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "presenter":
                    field.setAccessible(true);
                    ADS_NAVIGATION_VIEW_FIELD_PRESENTER = field;
                    break;
            }
        }

        Class<?> clazz1 = NavigationMenuPresenter.class;
        for (Field field : clazz1.getDeclaredFields()) {
            switch (field.getName()) {
                case "menuView":
                    field.setAccessible(true);
                    ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW = field;
                    break;
            }
        }
    }

    /**
     * Initialize scroll bar fields so that we can access them via reflection.
     */
    private static void initializeScrollBarFields(@NonNull View view) {
        try {
            if (ADS_VIEW_SCROLL_BAR_FIELD_CACHE == null) {
                ADS_VIEW_SCROLL_BAR_FIELD_CACHE = View.class.getDeclaredField("mScrollCache");
                ADS_VIEW_SCROLL_BAR_FIELD_CACHE.setAccessible(true);
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
            Object edgeEffect = ADS_LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
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
            Object edgeEffect = ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.get(recyclerView);
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
    private static void setEdgeEffectColor(@NonNull RecyclerView recyclerView, 
            final @ColorInt int color, @Nullable RecyclerView.OnScrollListener scrollListener) {
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
            Object edgeEffect = ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
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
                    ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(nestedScrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect =
                    ADS_NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(nestedScrollView);
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
            Object edgeEffect = ADS_VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.get(viewPager);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = ADS_VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.get(viewPager);
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
                    ADS_NAVIGATION_VIEW_FIELD_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.get(presenter);
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
                ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);
                edgeEffect = ADS_EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.get(edgeEffect);
            } catch (IllegalAccessException illegalAccessException) {
                return;
            }
        }

        if (edgeEffect == null) {
            return;
        }

        if (DynamicVersionUtils.isLollipop()) {
            ((EdgeEffect) edgeEffect).setColor(color);
        } else {
            try {
                ADS_EDGE_EFFECT_FIELD_EDGE.setAccessible(true);
                final Drawable mEdge = (Drawable) ADS_EDGE_EFFECT_FIELD_EDGE.get(edgeEffect);
                ADS_EDGE_EFFECT_FIELD_GLOW.setAccessible(true);
                final Drawable mGlow = (Drawable) ADS_EDGE_EFFECT_FIELD_GLOW.get(edgeEffect);
                mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mEdge.setCallback(null);
                mGlow.setCallback(null);
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
                    ADS_NAVIGATION_VIEW_FIELD_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    ADS_NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.get(presenter);
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
    public static void setScrollBarColor(@NonNull View view, @ColorInt int color) {
        initializeScrollBarFields(view);
        color = DynamicColorUtils.getLessVisibleColor(color);

        if (ADS_VIEW_SCROLL_BAR_FIELD_CACHE == null) {
            return;
        }

        try {
            Object mScrollCache = ADS_VIEW_SCROLL_BAR_FIELD_CACHE.get(view);
            ADS_VIEW_SCROLL_BAR_FIELD =
                    mScrollCache.getClass().getDeclaredField("scrollBar");
            ADS_VIEW_SCROLL_BAR_FIELD.setAccessible(true);
            Object scrollBar = ADS_VIEW_SCROLL_BAR_FIELD.get(mScrollCache);
            ADS_VIEW_SCROLL_BAR_VERTICAL_THUMB =
                    scrollBar.getClass().getDeclaredField("mVerticalThumb");
            ADS_VIEW_SCROLL_BAR_VERTICAL_THUMB.setAccessible(true);

            if (ADS_VIEW_SCROLL_BAR_VERTICAL_THUMB != null) {
                DynamicDrawableUtils.colorizeDrawable((Drawable)
                        ADS_VIEW_SCROLL_BAR_VERTICAL_THUMB.get(scrollBar), color);
            }

            // Fix for Android P developer preview. For more info, please
            // visit g.co/dev/appcompat.
//            ADS_VIEW_SCROLL_BAR_HORIZONTAL_THUMB =
//                    scrollBar.getClass().getDeclaredField("mHorizontalThumb");
//            ADS_VIEW_SCROLL_BAR_HORIZONTAL_THUMB.setAccessible(true);
//            if (ADS_VIEW_SCROLL_BAR_HORIZONTAL_THUMB != null) {
//                DynamicDrawableUtils.colorizeDrawable((Drawable)
//                        ADS_VIEW_SCROLL_BAR_HORIZONTAL_THUMB.get(scrollBar), color);
//            }
        } catch(Exception ignored) {
        }
    }
}
