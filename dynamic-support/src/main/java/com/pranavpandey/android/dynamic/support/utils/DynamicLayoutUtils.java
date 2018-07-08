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
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
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
     * @param compact {@code true} if the layout is compact and
     *                disable the auto increase of columns in
     *                multi-window mode.
     *
     * @return The column count according to the current device
     *         configurations.
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static int getLayoutColumns(@NonNull Context context, int defaultCount,
                                       int maxCount, boolean compact) {
        int columns = defaultCount;
        int screenCategory = getScreenSizeCategory(context);

        switch (screenCategory) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                if (columns > 1) {
                    columns = columns - 1;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                if (context.getResources().getConfiguration().orientation
                        != Configuration.ORIENTATION_LANDSCAPE) {
                    break;
                }
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                columns += 1;
                break;
        }

        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE){
            try {
                if (compact || !isInMultiWindowMode(context)) {
                    columns *= 2;
                }
            } catch (Exception ignored) {
            }
        }

        return Math.min(columns, maxCount);
    }

    /**
     * @return {@code true} if the app is in multi-window mode.
     *
     * @param context The context to get configuration.
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static boolean isInMultiWindowMode(@NonNull Context context) {
        try {
            return (DynamicVersionUtils.isNougat()
                    && ((Activity) context).isInMultiWindowMode());
        } catch (Exception ignored) {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static int getLayoutColumns(@NonNull Context context,
                                       int defaultCount, int maxCount) {
        return getLayoutColumns(context, defaultCount, maxCount, false);
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
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
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
     * @param defaultCount The default column count.
     * @param maxCount The maximum column count up to which we can
     *                 scale.
     * @param compact {@code true} if the layout is compact and
     *                disable the auto increase of columns in
     *                multi-window mode.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManager(
            @NonNull Context context, int defaultCount, int maxCount, boolean compact) {
        return new GridLayoutManager(context, getLayoutColumns(
                context, defaultCount, maxCount, compact));
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param defaultCount The default column count.
     * @param maxCount The maximum column count up to which we can
     *                 scale.
     *
     * @see #getLayoutColumns(Context, int, int, boolean)
     */
    public static GridLayoutManager getGridLayoutManager(
            @NonNull Context context, int defaultCount, int maxCount) {
        return new GridLayoutManager(context, getLayoutColumns(
                context, defaultCount, maxCount, true));
    }

    /**
     * @return The {@link GridLayoutManager} object for a given
     *         context.
     *
     * @param context The context to instantiate layout manager.
     * @param count The default and max column count.
     * @param compact {@code true} if the layout is compact and
     *                disable the auto increase of columns in
     *                multi-window mode.
     *
     * @see #getLayoutColumns(Context, int, int)
     */
    public static GridLayoutManager getGridLayoutManager(
            @NonNull Context context, int count, boolean compact) {
        return getGridLayoutManager(context, count, count, compact);
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
        return getGridLayoutManager(context, count, false);
    }

    /**
     * @return The {@link StaggeredGridLayoutManager} object for a given
     *         context.
     *
     * @param count The no. of rows or columns count according to the
     *              orientation.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     */
    public static StaggeredGridLayoutManager getStaggeredGridLayoutManager(
            int count, int orientation) {
        return new StaggeredGridLayoutManager(count, orientation);
    }

    /**
     * Set full span for the header and empty view in case of a
     * {@link GridLayoutManager}. This method must be called
     * after setting a adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     *
     * @see DynamicRecyclerViewFrame.ItemType
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     */
    public static void setFullSpanForHeader(final RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                ((GridLayoutManager) recyclerView.getLayoutManager())
                        .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                switch (recyclerView.getAdapter().getItemViewType(position)) {
                                    case DynamicRecyclerViewFrame.TYPE_EMPTY_VIEW:
                                    case DynamicRecyclerViewFrame.TYPE_SECTION_HEADER:
                                        return ((GridLayoutManager)
                                                recyclerView.getLayoutManager()).getSpanCount();
                                    case DynamicRecyclerViewFrame.TYPE_ITEM:
                                        return 1;
                                    default:
                                        return -1;
                                }
                            }
                        });
            }
        }
    }
}
