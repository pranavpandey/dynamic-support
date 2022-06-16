/*
 * Copyright 2018-2022 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.recyclerview.binder.factory;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicDataBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicQueryBinder;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicEmptyView;

/**
 * A {@link DynamicDataBinder} to bind the search or any other query results that can be
 * used with the {@link DynamicBinderAdapter}.
 */
public class EmptyBinder extends DynamicQueryBinder<String, String, EmptyBinder.ViewHolder> {

    /**
     * Drawable used by this binder.
     */
    private Drawable mDrawable;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public EmptyBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ads_layout_empty_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (getData() == null) {
            return;
        }

        viewHolder.getEmptyView().setIcon(getDrawable());
        viewHolder.getEmptyView().setTitle(getData());

        DynamicResourceUtils.highlightQueryTextColor(getQuery(),
                viewHolder.getEmptyView().getTitleView(), getHighlightColor());

        if (isFullSpanForStaggeredGrid()) {
            DynamicLayoutUtils.setFullSpanForView(viewHolder.itemView);
        }
    }

    /**
     * Returns whether to set the for span for
     * {@link androidx.recyclerview.widget.StaggeredGridLayoutManager}.
     *
     * @return {@code true} to set the full span.
     */
    public boolean isFullSpanForStaggeredGrid() {
        return true;
    }

    /**
     * Get the drawable used by this binder.
     *
     * @return The drawable used by this binder.
     */
    public @Nullable Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * Set the drawable for this binder.
     *
     * @param drawable The drawable to be set.
     */
    public void setDrawable(@Nullable Drawable drawable) {
        this.mDrawable = drawable;

        notifyBinderDataSetChanged();
    }

    /**
     * View holder to hold the empty view layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Empty view to show the icon and text for this view holder.
         */
        private final DynamicEmptyView emptyView;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(@NonNull View view) {
            super(view);

            this.emptyView = view.findViewById(R.id.ads_empty_view);
        }

        /**
         * Get the empty view for this view holder.
         *
         * @return The empty view for this view holder.
         */
        public @NonNull DynamicEmptyView getEmptyView() {
            return emptyView;
        }
    }
}
