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
     * {@code true} to set the full span.
     */
    private boolean mIsFullSpanForStaggeredGrid;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getData() == null) {
            return;
        }

        holder.getEmptyView().setIcon(getDrawable() != null ? getDrawable() : holder.getIcon());
        holder.getEmptyView().setTitle(getData());

        if (isFullSpanForStaggeredGrid()) {
            DynamicLayoutUtils.setFullSpanForView(holder.itemView);
        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onHighlightQuery(@NonNull ViewHolder holder,
            int position, @Nullable String query) {
        DynamicResourceUtils.highlightQueryTextColor(query,
                holder.getEmptyView().getTitleView(), getHighlightColor());
    }

    /**
     * Returns whether to set the for span for
     * {@link androidx.recyclerview.widget.StaggeredGridLayoutManager}.
     *
     * @return {@code true} to set the full span.
     */
    public boolean isFullSpanForStaggeredGrid() {
        return mIsFullSpanForStaggeredGrid;
    }

    /**
     * Sets whether to set the full span for
     * {@link androidx.recyclerview.widget.StaggeredGridLayoutManager}.
     *
     * @param fullSpanForStaggeredGrid  {@code true} to set the full span.
     *
     * @return The {@link EmptyBinder} object to allow for chaining of calls to set methods.
     */
    public @NonNull EmptyBinder setFullSpanForStaggeredGrid(boolean fullSpanForStaggeredGrid) {
        this.mIsFullSpanForStaggeredGrid = fullSpanForStaggeredGrid;

        notifyBinderDataSetChanged();
        return this;
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
     *
     * @return The {@link EmptyBinder} object to allow for chaining of calls to set methods.
     */
    public @NonNull EmptyBinder setDrawable(@Nullable Drawable drawable) {
        this.mDrawable = drawable;

        notifyBinderDataSetChanged();
        return this;
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

        /**
         * Get the icon drawable used by the empty view.
         *
         * @return The icon drawable used by the empty view.
         */
        public @Nullable Drawable getIcon() {
            return emptyView != null ? emptyView.getIcon() : null;
        }
    }
}
