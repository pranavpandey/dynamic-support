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

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

/**
 * Helper class to perform layout operations like detecting the
 * column count at runtime.
 */
public class DynamicLayoutUtils {

    /**
     * Get the column count according to the current configuration.
     * It will also consider multi-window mode on {@link Build.VERSION_CODES#N}
     * or above devices.
     *
     * @param context The context to get configuration.
     * @param defaultCount The default column count.
     * @param maxCount The maximum column count up to which we can
     *                 scale.
     *
     * @return The column count according to the current device
     *         configurations.
     */
    public static int getLayoutColumns(@NonNull Context context, int defaultCount,
                                       int maxCount) {
        int columns = defaultCount;
        int screenCategory = getScreenSizeCategory(context);

        switch (screenCategory) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                if (columns != 1) {
                    columns = columns - 1;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                columns += 1;
                break;
        }

        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE){
            try {
                if (!(DynamicVersionUtils.isNougat() &&
                        ((Activity) context).isInMultiWindowMode())) {
                    columns *= 2;
                }
            } catch (Exception ignored) {
            }
        }

        return Math.min(columns, maxCount);
    }

    /**
     * @return The screen size category for the supplied context.
     *
     * @param context The context to get configuration.
     */
    public static int getScreenSizeCategory(@NonNull Context context){
        return context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    /**
     * @return The grid count for the supplied context with default
     *         count 1 and max count 2.
     *
     * @param context The context to get configuration.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static int getGridCount(@NonNull Context context) {
        return getLayoutColumns(context, 1, 2);
    }

    /**
     * @return The grid count for the supplied context with default
     *         count 2 and max count 3.
     *
     * @param context The context to get configuration.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static int getGridCountCompact(@NonNull Context context) {
        return getLayoutColumns(context, 2, 3);
    }

    /**
     * @return The grid count for the supplied context with default
     *         count 1 and max count 3.
     *
     * @param context The context to get configuration.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static int getGridCountNoDrawer(@NonNull Context context) {
        return getLayoutColumns(context, 1, 3);
    }

    /**
     * @return The {@link LinearLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     */
    public static LinearLayoutManager getLinearLayoutManager(
            @NonNull Context context, int orientation) {
        return new LinearLayoutManager(context, orientation, false);
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param count The default and max column count.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManager(@NonNull Context context, int count) {
        return new GridLayoutManager(context, getLayoutColumns(context, count, count));
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     *
     * @see #getGridCount(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManager(@NonNull Context context) {
        return getGridLayoutManager(context, getGridCount(context));
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     *
     * @see #getGridCountCompact(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManagerCompact(@NonNull Context context) {
        return getGridLayoutManager(context, getGridCountCompact(context));
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     *
     * @see #getGridCountNoDrawer(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManagerNoDrawer(@NonNull Context context) {
        return getGridLayoutManager(context, getGridCountNoDrawer(context));
    }

    /**
     * @return The {@link StaggeredGridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param count The no. of rows or columns count according to the
     *              orientation.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     */
    public static StaggeredGridLayoutManager getStaggeredGridLayoutManager(
            @NonNull Context context, int count, int orientation) {
        return new StaggeredGridLayoutManager(count, orientation);
    }

    /**
     * @return The {@link StaggeredGridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     *
     * @see #getGridCount(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static StaggeredGridLayoutManager getStaggeredGridLayoutManager(
            @NonNull Context context, int orientation) {
        return getStaggeredGridLayoutManager(
                context, getGridCount(context), orientation);
    }

    /**
     * @return The {@link StaggeredGridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     *
     * @see #getGridCountCompact(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static StaggeredGridLayoutManager getStaggeredGridLayoutManagerCompact(
            @NonNull Context context, int orientation) {
        return getStaggeredGridLayoutManager(
                context, getGridCountCompact(context), orientation);
    }

    /**
     * @return The {@link StaggeredGridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     *
     * @see #getGridCountNoDrawer(Context)
     * @see #getLayoutColumns(Context, int, int)
     */
    public static StaggeredGridLayoutManager getStaggeredGridLayoutManagerNoDrawer(
            @NonNull Context context, int orientation) {
        return getStaggeredGridLayoutManager(
                context, getGridCountNoDrawer(context), orientation);
    }
}
