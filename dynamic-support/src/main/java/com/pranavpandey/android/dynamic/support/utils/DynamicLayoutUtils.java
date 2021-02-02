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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicRecyclerViewAdapter;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.Arrays;

/**
 * Helper class to perform layout operations like detecting the column count at runtime.
 */
public class DynamicLayoutUtils {

    /**
     * Get the column count according to the current configuration.
     * <p>It will also consider multi-window mode on API 24 and above devices.
     *
     * <p>It is not recommended to do this calculation at runtime. So, please define all the
     * span counts in xml.
     *
     * @param context The context to get configuration.
     * @param defaultCount The default column count.
     * @param maxCount The maximum column count up to which we can scale.
     * @param compact {@code true} if the layout is compact and disable the auto increase of
     *                columns in multi-window mode.
     *
     * @return The column count according to the current device configurations.
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static int getLayoutColumns(@NonNull Context context,
            int defaultCount, int maxCount, boolean compact) {
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
                == Configuration.ORIENTATION_LANDSCAPE) {
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
     * Checks whether the app is in multi-window mode.
     *
     * @param context The context to get configuration.
     *
     * @return {@code true} if the app is in multi-window mode.
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static boolean isInMultiWindowMode(@NonNull Context context) {
        try {
            return (DynamicSdkUtils.is24()
                    && ((Activity) context).isInMultiWindowMode());
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     *
     * Get the screen size category for the supplied context.
     *
     * @param context The context to get configuration.
     *
     * @return The screen size category for the supplied context.
     */
    public static int getScreenSizeCategory(@NonNull Context context) {
        return context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    /**
     * Get the grid count for the supplied context.
     *
     * @param context The context to get the span count.
     *
     * @return The grid count for the supplied context with default count 1 and max count 2.
     */
    public static int getGridCount(@NonNull Context context) {
        return context.getResources().getInteger(R.integer.ads_span_normal);
    }

    /**
     * Get the grid count for the supplied context suitable for no drawer activity.
     *
     * @param context The context to get the span count.
     *
     * @return The grid count for the supplied context with default count 1 and max count 3.
     */
    public static int getGridCountNoDrawer(@NonNull Context context) {
        return context.getResources().getInteger(R.integer.ads_span_no_drawer);
    }

    /**
     * Get the grid count for the supplied context suitable for a compact layout.
     *
     * @param context The context to get the span count.
     *
     * @return The grid count for the supplied context with default count 1 or 2 and max count 3.
     */
    public static int getGridCountCompact(@NonNull Context context) {
        return context.getResources().getInteger(R.integer.ads_span_compact);
    }

    /**
     * Get the grid count for the supplied context suitable for the dialog.
     *
     * @param context The context to get the span count.
     *
     * @return The grid count for the supplied context with default count 1 or 2 and max count 3.
     */
    public static int getGridCountCompactDialog(@NonNull Context context) {
        return context.getResources().getInteger(R.integer.ads_span_compact_dialog);
    }

    /**
     * Returns the {@link LinearLayoutManager} object for the given context.
     *
     * @param context The context to instantiate the layout manager.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     *
     * @return The {@link LinearLayoutManager} object for the given context.
     */
    public static @NonNull LinearLayoutManager getLinearLayoutManager(
            @NonNull Context context, int orientation) {
        return new LinearLayoutManager(context, orientation, false);
    }

    /**
     * Returns the {@link GridLayoutManager} object for the given context.
     * 
     * @param context The context to instantiate the layout manager.
     * @param count The column count for the grid layout.
     *
     * @return The {@link GridLayoutManager} object for the given context.
     */
    public static @NonNull GridLayoutManager getGridLayoutManager(
            @NonNull Context context, int count) {
        return new GridLayoutManager(context, count);
    }

    /**
     * Returns the {@link StaggeredGridLayoutManager} object for the given context.
     * 
     * @param count The no. of rows or columns count according to the orientation.
     * @param orientation The orientation of the layout manager.
     *                    {@link StaggeredGridLayoutManager#VERTICAL} or
     *                    {@link StaggeredGridLayoutManager#HORIZONTAL}
     *                    
     * @return The {@link StaggeredGridLayoutManager} object for the given context.
     */
    public static @NonNull StaggeredGridLayoutManager getStaggeredGridLayoutManager(
            int count, int orientation) {
        return new StaggeredGridLayoutManager(count, orientation);
    }

    /**
     * Returns the {@link FlexboxLayoutManager} object for the given context.
     *
     * @param context The context to instantiate the layout manager.
     * @param flexDirection The flex direction attribute to the flex container.
     * @param justifyContent The justify content attribute to the flex container.
     * @param alignItems The align items attribute to the flex container.
     *
     * @return The {@link FlexboxLayoutManager} object for the given context.
     */
    public static @NonNull FlexboxLayoutManager getFlexboxLayoutManager(
            @NonNull Context context, @FlexDirection int flexDirection,
            @JustifyContent  int justifyContent, @AlignItems int alignItems) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(flexDirection);
        layoutManager.setJustifyContent(justifyContent);
        layoutManager.setAlignItems(alignItems);

        return layoutManager;
    }

    /**
     * Returns the {@link FlexboxLayoutManager} object for the given context.
     *
     * @param context The context to instantiate the layout manager.
     * @param flexDirection The flex direction attribute to the flex container.
     *
     * @return The {@link FlexboxLayoutManager} object for the given context.
     */
    public static @NonNull FlexboxLayoutManager getFlexboxLayoutManager(
            @NonNull Context context, @FlexDirection int flexDirection) {
        return getFlexboxLayoutManager(context, flexDirection,
                JustifyContent.FLEX_START, AlignItems.STRETCH);
    }

    /**
     * Sets full span for the item view types in case of a {@link GridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     * @param itemTypes The item types supported by the recycler view.
     * @param spanCount The maximum span supported by the layout manager.
     *
     * @see DynamicRecyclerViewAdapter.ItemType
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     */
    public static void setFullSpanForType(final @Nullable RecyclerView recyclerView,
            final @NonNull Integer[] itemTypes, int spanCount) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager =
                    (GridLayoutManager) recyclerView.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (Arrays.asList(itemTypes).contains(
                            recyclerView.getAdapter().getItemViewType(position))) {
                        return spanCount;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    /**
     * Sets full span for the header and empty view in case of a {@link GridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     *
     * @see DynamicRecyclerViewAdapter.ItemType
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     * @see #setFullSpanForType(RecyclerView, Integer[], int)
     *
     * @see DynamicRecyclerViewAdapter#TYPE_SECTION_HEADER
     * @see DynamicRecyclerViewAdapter#TYPE_EMPTY_VIEW
     */
    public static void setFullSpanForType(final @Nullable RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            setFullSpanForType(recyclerView, new Integer[] {
                    DynamicRecyclerViewAdapter.TYPE_SECTION_HEADER,
                            DynamicRecyclerViewAdapter.TYPE_EMPTY_VIEW },
                    ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount());
        }
    }

    /**
     * Sets full span for the positions in case of a {@link GridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     * @param positions The positions supported by the recycler view.
     * @param spanCount The maximum span supported by the layout manager.
     *
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     */
    public static void setFullSpanForPosition(final @Nullable RecyclerView recyclerView,
            final @NonNull Integer[] positions, int spanCount) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager =
                    (GridLayoutManager) recyclerView.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (Arrays.asList(positions).contains(position)) {
                        int previousSpans = 0;
                        int previousIndex = Arrays.asList(positions).indexOf(position);
                        for (int i = 0; i < previousIndex; i++) {
                            previousSpans += Math.min(Math.abs(spanCount
                                    - ((positions[i] + previousSpans) % spanCount)), spanCount);

                            if (previousSpans > 0) {
                                previousSpans--;
                            }
                        }

                        return Math.min(Math.abs(spanCount -
                                ((position + previousSpans) % spanCount)), spanCount);
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    /**
     * Sets full span for the positions in case of a {@link GridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     * @param positions The positions supported by the recycler view.
     *
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     */
    public static void setFullSpanForPosition(final @Nullable RecyclerView recyclerView,
            final @NonNull Integer[] positions) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            setFullSpanForPosition(recyclerView, positions,
                    ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount());
        }
    }

    /**
     * Sets full span for the single last position in case of a {@link GridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param recyclerView The recycler view to set the span size.
     *
     * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
     * @see #setFullSpanForPosition(RecyclerView, Integer[], int)
     */
    public static void setFullSpanForPosition(final @Nullable RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                && recyclerView.getAdapter().getItemCount() %
                ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount() <
                ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount()) {
            setFullSpanForPosition(recyclerView, new Integer[] {
                            recyclerView.getAdapter().getItemCount() - 1 });
        }
    }

    /**
     * Sets full span for the view in case of a {@link StaggeredGridLayoutManager}.
     * This method must be called after setting an adapter for the recycler view.
     *
     * @param view The recycler view to set the span size.
     *
     * @see StaggeredGridLayoutManager.LayoutParams#setFullSpan(boolean)
     */
    public static void setFullSpanForView(final @Nullable View view) {
        if (view == null) {
            return;
        }

        if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).setFullSpan(true);
        }
    }
}
