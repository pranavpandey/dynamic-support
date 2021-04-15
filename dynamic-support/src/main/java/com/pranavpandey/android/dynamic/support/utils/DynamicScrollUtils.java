/*
 * Copyright 2018-2021 Pranav Pandey
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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.widget.EdgeEffectCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
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
     * {@link HorizontalScrollView} field constant for the left glow.
     */
    private static Field F_SCROLL_VIEW_EDGE_GLOW_LEFT;

    /**
     * {@link HorizontalScrollView} field constant for the right glow.
     */
    private static Field F_SCROLL_VIEW_EDGE_GLOW_RIGHT;

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
    private static Field F_SCROLL_BAR_FIELD_CACHE;

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
     *
     * @param clazz The class object to be used.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void initializeEdgeEffectFields(@Nullable Object clazz) {
        if (clazz instanceof EdgeEffectCompat) {
            try {
                F_EDGE_EFFECT_COMPAT_EDGE_EFFECT = EdgeEffectCompat.class
                        .getDeclaredField("mEdgeEffect");
                F_EDGE_EFFECT_COMPAT_EDGE_EFFECT.setAccessible(true);
            } catch (Exception ignored) {
            }
        } else if (clazz instanceof EdgeEffect) {
            try {
                F_EDGE_EFFECT_EDGE = EdgeEffect.class.getDeclaredField("mEdge");
                F_EDGE_EFFECT_EDGE.setAccessible(true);
                F_EDGE_EFFECT_GLOW = EdgeEffect.class.getDeclaredField("mGlow");;
                F_EDGE_EFFECT_GLOW.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Set the edge effect or glow color for the navigation view.
     *
     * @param view The navigation view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable NavigationView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeNavigationViewFields(view);

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    F_NAVIGATION_VIEW_PRESENTER.get(view);
            initializeNavigationViewFields(presenter);

            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    F_NAVIGATION_VIEW_RECYCLER_VIEW.get(presenter);
            setEdgeEffectColor(navigationMenuView, color, null);
        } catch (Exception ignored) {
        }
    }

    /**
     * Initialize abs list view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeListViewFields(@Nullable Object clazz) {
        if (clazz instanceof AbsListView) {
            try {
                F_LIST_VIEW_EDGE_GLOW_TOP = AbsListView.class
                        .getDeclaredField("mEdgeGlowTop");
                F_LIST_VIEW_EDGE_GLOW_TOP.setAccessible(true);
                F_LIST_VIEW_EDGE_GLOW_BOTTOM = AbsListView.class
                        .getDeclaredField("mEdgeGlowBottom");
                F_LIST_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize recycler view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeRecyclerViewFields(@Nullable Object clazz) {
        if (clazz instanceof RecyclerView) {
            try {
                F_RECYCLER_VIEW_EDGE_GLOW_TOP = RecyclerView.class
                        .getDeclaredField("mTopGlow");
                F_RECYCLER_VIEW_EDGE_GLOW_TOP.setAccessible(true);
                F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM = RecyclerView.class
                        .getDeclaredField("mBottomGlow");
                F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);
                F_RECYCLER_VIEW_EDGE_GLOW_LEFT = RecyclerView.class
                        .getDeclaredField("mLeftGlow");
                F_RECYCLER_VIEW_EDGE_GLOW_LEFT.setAccessible(true);
                F_RECYCLER_VIEW_EDGE_GLOW_RIGHT = RecyclerView.class
                        .getDeclaredField("mRightGlow");
                F_RECYCLER_VIEW_EDGE_GLOW_RIGHT.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize scroll view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeScrollViewFields(@Nullable Object clazz) {
        if (clazz instanceof ScrollView) {
            try {
                F_SCROLL_VIEW_EDGE_GLOW_TOP = ScrollView.class
                        .getDeclaredField("mEdgeGlowTop");
                F_SCROLL_VIEW_EDGE_GLOW_TOP.setAccessible(true);
                F_SCROLL_VIEW_EDGE_GLOW_BOTTOM = ScrollView.class
                        .getDeclaredField("mEdgeGlowBottom");
                F_SCROLL_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize horizontal scroll view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeHorizontalScrollViewFields(@Nullable Object clazz) {
        if (clazz instanceof HorizontalScrollView) {
            try {
                F_SCROLL_VIEW_EDGE_GLOW_LEFT = HorizontalScrollView.class
                        .getDeclaredField("mEdgeGlowLeft");
                F_SCROLL_VIEW_EDGE_GLOW_LEFT.setAccessible(true);
                F_SCROLL_VIEW_EDGE_GLOW_RIGHT = ScrollView.class
                        .getDeclaredField("mEdgeGlowRight");
                F_SCROLL_VIEW_EDGE_GLOW_RIGHT.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize nested scroll view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeNestedScrollViewFields(@Nullable Object clazz) {
        if (clazz instanceof NestedScrollView) {
            try {
                F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP = NestedScrollView.class
                        .getDeclaredField("mEdgeGlowTop");
                F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP.setAccessible(true);
                F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM = NestedScrollView.class
                        .getDeclaredField("mEdgeGlowBottom");
                F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize view pager fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeViewPagerFields(@Nullable Object clazz) {
        if (clazz instanceof ViewPager) {
            try {
                F_VIEW_PAGER_EDGE_GLOW_LEFT = ViewPager.class
                        .getDeclaredField("mLeftEdge");
                F_VIEW_PAGER_EDGE_GLOW_LEFT.setAccessible(true);
                F_VIEW_PAGER_EDGE_GLOW_RIGHT = ViewPager.class
                        .getDeclaredField("mRightEdge");
                F_VIEW_PAGER_EDGE_GLOW_RIGHT.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize navigation view fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeNavigationViewFields(@Nullable Object clazz) {
        if (clazz instanceof NavigationView) {
            try {
                F_NAVIGATION_VIEW_PRESENTER = NavigationView.class
                        .getDeclaredField("presenter");
                F_NAVIGATION_VIEW_PRESENTER.setAccessible(true);
            } catch (Exception ignored) {
            }
        }

        if (clazz instanceof NavigationMenuPresenter) {
            try {
                F_NAVIGATION_VIEW_RECYCLER_VIEW = NavigationMenuPresenter.class
                        .getDeclaredField("menuView");
                F_NAVIGATION_VIEW_RECYCLER_VIEW.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Initialize scroll bar fields so that we can access them via reflection.
     *
     * @param clazz The class object to be used.
     */
    private static void initializeScrollBarFields(@Nullable Object clazz) {
        if (clazz instanceof View) {
            try {
                F_SCROLL_BAR_FIELD_CACHE = View.class.getDeclaredField("mScrollCache");
                F_SCROLL_BAR_FIELD_CACHE.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Set the edge effect or glow color for the list view.
     *
     * @param view The list view to be used.
     * @param color The edge effect color to be set.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public static void setEdgeEffectColor(@Nullable AbsListView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        if (DynamicSdkUtils.is29()) {
            view.setEdgeEffectColor(color);
            return;
        }

        initializeListViewFields(view);

        try {
            setEdgeEffectColor(F_LIST_VIEW_EDGE_GLOW_TOP.get(view), color);
            setEdgeEffectColor(F_LIST_VIEW_EDGE_GLOW_BOTTOM.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the edge effect or glow color for the recycler view.
     *
     * @param view The recycler view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable RecyclerView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeRecyclerViewFields(view);

        try {
            setEdgeEffectColor(F_RECYCLER_VIEW_EDGE_GLOW_TOP.get(view), color);
            setEdgeEffectColor(F_RECYCLER_VIEW_EDGE_GLOW_BOTTOM.get(view), color);
            setEdgeEffectColor(F_RECYCLER_VIEW_EDGE_GLOW_LEFT.get(view), color);
            setEdgeEffectColor(F_RECYCLER_VIEW_EDGE_GLOW_RIGHT.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the edge effect or glow color for the recycler view.
     *
     * @param view The recycler view to be used.
     * @param color The edge effect color to be set.
     * @param scrollListener The scroll listener to set the color on over scroll.
     */
    public static void setEdgeEffectColor(@Nullable RecyclerView view, final @ColorInt int color,
            @Nullable RecyclerView.OnScrollListener scrollListener) {
        if (view == null) {
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

            view.removeOnScrollListener(scrollListener);
            view.addOnScrollListener(scrollListener);
        }

        setEdgeEffectColor(view, color);
    }

    /**
     * Set the edge effect or glow color for the scroll view.
     *
     * @param view The scroll view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable ScrollView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeScrollViewFields(view);

        try {
            setEdgeEffectColor(F_SCROLL_VIEW_EDGE_GLOW_TOP.get(view), color);
            setEdgeEffectColor(F_SCROLL_VIEW_EDGE_GLOW_BOTTOM.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the edge effect or glow color for the horizontal scroll view.
     *
     * @param view The horizontal scroll view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable HorizontalScrollView view,
            @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeHorizontalScrollViewFields(view);

        try {
            setEdgeEffectColor(F_SCROLL_VIEW_EDGE_GLOW_LEFT.get(view), color);
            setEdgeEffectColor(F_SCROLL_VIEW_EDGE_GLOW_RIGHT.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the edge effect or glow color for the nested scroll view.
     *
     * @param view The nested scroll view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable NestedScrollView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeNestedScrollViewFields(view);

        try {
            setEdgeEffectColor(F_NESTED_SCROLL_VIEW_EDGE_GLOW_TOP.get(view), color);
            setEdgeEffectColor(F_NESTED_SCROLL_VIEW_EDGE_GLOW_BOTTOM.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the edge effect or glow color for the view pager.
     *
     * @param view The view pager to be used.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@Nullable ViewPager view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeViewPagerFields(view);

        try {
            setEdgeEffectColor(F_VIEW_PAGER_EDGE_GLOW_LEFT.get(view), color);
            setEdgeEffectColor(F_VIEW_PAGER_EDGE_GLOW_RIGHT.get(view), color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set color of the supplied edge effect object.
     *
     * @param edgeEffect The edge effect object to be used.
     * @param color The edge effect color to be set.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeEffectColor(@Nullable Object edgeEffect, @ColorInt int color) {
        if (edgeEffect instanceof EdgeEffectCompat) {
            try {
                initializeEdgeEffectFields(edgeEffect);
                edgeEffect = F_EDGE_EFFECT_COMPAT_EDGE_EFFECT.get(edgeEffect);
            } catch (Exception ignored) {
                return;
            }
        }

        if (edgeEffect == null) {
            return;
        }

        if (DynamicSdkUtils.is21()) {
            ((EdgeEffect) edgeEffect).setColor(color);
        } else if (edgeEffect instanceof EdgeEffect){
            initializeEdgeEffectFields(edgeEffect);

            try {
                Drawable mEdge = (Drawable) F_EDGE_EFFECT_EDGE.get(edgeEffect);
                Drawable mGlow = (Drawable) F_EDGE_EFFECT_GLOW.get(edgeEffect);
                if (mGlow != null) {
                    mGlow.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mGlow.setCallback(null);
                }

                if (mEdge != null) {
                    mEdge.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mEdge.setCallback(null);
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Set the scroll bar color for the view.
     *
     * @param view The view to be used.
     * @param color The scroll bar color to be set.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public static void setScrollBarColor(@Nullable View view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        if (DynamicSdkUtils.is29()) {
            DynamicDrawableUtils.colorizeDrawable(
                    view.getVerticalScrollbarThumbDrawable(), color);
            DynamicDrawableUtils.colorizeDrawable(
                    view.getHorizontalScrollbarThumbDrawable(), color);

            return;
        }

        initializeScrollBarFields(view);

        try {
            Object mScrollCache = F_SCROLL_BAR_FIELD_CACHE.get(view);

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

    /**
     * Set the scroll bar color for the navigation view.
     *
     * @param view The navigation view to be used.
     * @param color The edge effect color to be set.
     */
    public static void setScrollBarColor(@Nullable NavigationView view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeNavigationViewFields(view);

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    F_NAVIGATION_VIEW_PRESENTER.get(view);
            initializeNavigationViewFields(presenter);

            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    F_NAVIGATION_VIEW_RECYCLER_VIEW.get(presenter);
            setScrollBarColor(navigationMenuView, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set the selector color for the list view.
     *
     * @param view The list view to be used.
     * @param color The selector color to be set.
     * @param background The background color to be used.
     */
    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setSelectorColor(@Nullable AbsListView view,
            @ColorInt int color, @ColorInt int background) {
        if (view == null) {
            return;
        }

        if (view.getSelector() instanceof DrawableWrapper) {
            DrawableWrapper drawable = (DrawableWrapper) view.getSelector();
            drawable.mutate();

            if (DynamicSdkUtils.is21()
                    && drawable.getWrappedDrawable() instanceof RippleDrawable) {
                color = DynamicColorUtils.getLighterColor(DynamicColorUtils.adjustAlpha(color,
                        Defaults.ADS_ALPHA_PRESSED_SELECTOR), Defaults.ADS_STATE_LIGHT);

                DynamicTintUtils.colorizeRippleDrawable(view, drawable.getWrappedDrawable(),
                        background, color, false, false);
            } else {
                DynamicDrawableUtils.colorizeDrawable(drawable, color);
            }
        } else {
            DynamicDrawableUtils.colorizeDrawable(view.getSelector(), color);
        }
    }

    /**
     * Tint view according to the supplied contrast with color.
     *
     * @param view The scrollable to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the view.
     *
     * @see #setEdgeEffectColor(AbsListView, int)
     * @see #setEdgeEffectColor(RecyclerView, int)
     * @see #setEdgeEffectColor(ScrollView, int)
     * @see #setEdgeEffectColor(HorizontalScrollView, int)
     * @see #setEdgeEffectColor(NestedScrollView, int)
     * @see #setEdgeEffectColor(ViewPager, int)
     * @see #setEdgeEffectColor(NavigationView, int)
     *
     * @see #setScrollBarColor(NavigationView, int)
     * @see #setScrollBarColor(View, int)
     *
     * @see #setSelectorColor(AbsListView, int, int)
     */
    public static <T> void tint(@Nullable T view, @ColorInt int contrastWithColor) {
        if (view == null) {
            return;
        }

        @ColorInt int edgeEffectColor = DynamicTheme.getInstance().resolveColorType(
                Defaults.ADS_COLOR_TYPE_EDGE_EFFECT);
        @ColorInt int scrollBarColor = DynamicTheme.getInstance().resolveColorType(
                Defaults.ADS_COLOR_TYPE_SCROLLABLE);
        @ColorInt int selectorColor = DynamicTheme.getInstance().get().getTintBackgroundColor();

        if (DynamicTheme.getInstance().get().isBackgroundAware()) {
            edgeEffectColor = DynamicColorUtils.getContrastColor(
                    edgeEffectColor, contrastWithColor);
            scrollBarColor = DynamicColorUtils.getContrastColor(scrollBarColor, contrastWithColor);
            selectorColor = DynamicColorUtils.getContrastColor(selectorColor, contrastWithColor);
        }

        if (view instanceof AbsListView) {
            setEdgeEffectColor((AbsListView) view, edgeEffectColor);
        } else if (view instanceof RecyclerView) {
            setEdgeEffectColor((RecyclerView) view, edgeEffectColor);
        } else if (view instanceof ScrollView) {
            setEdgeEffectColor((ScrollView) view, edgeEffectColor);
        } else if (view instanceof HorizontalScrollView) {
            setEdgeEffectColor((HorizontalScrollView) view, edgeEffectColor);
        } else if (view instanceof NestedScrollView) {
            setEdgeEffectColor((NestedScrollView) view, edgeEffectColor);
        } else if (view instanceof ViewPager) {
            setEdgeEffectColor((ViewPager) view, edgeEffectColor);
        } else if (view instanceof NavigationView) {
            setEdgeEffectColor((NavigationView) view, edgeEffectColor);
        }

        if (view instanceof NavigationView) {
            setScrollBarColor((NavigationView) view, scrollBarColor);
        } else if (view instanceof View) {
            setScrollBarColor((View) view, scrollBarColor);
        }

        if (view instanceof AbsListView) {
            setSelectorColor((AbsListView) view, selectorColor, contrastWithColor);
        }
    }
}
