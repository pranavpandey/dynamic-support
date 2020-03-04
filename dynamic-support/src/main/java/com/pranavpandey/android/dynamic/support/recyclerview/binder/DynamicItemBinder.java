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

package com.pranavpandey.android.dynamic.support.recyclerview.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicItemsAdapter;
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;

/**
 * Binder to bind the {@link DynamicItem} which can be used with the {@link DynamicBinderAdapter}.
 */
public class DynamicItemBinder extends DynamicRecyclerViewBinder<DynamicItemBinder.ViewHolder> {

    /**
     * Data used by this binder.
     */
    private DynamicItem mData;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public DynamicItemBinder(@NonNull DynamicItemsAdapter binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_layout_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DynamicItem dynamicItem = getData();

        if (dynamicItem != null) {
            viewHolder.getDynamicItem().setIcon(dynamicItem.getIcon());
            viewHolder.getDynamicItem().setTitle(dynamicItem.getTitle());
            viewHolder.getDynamicItem().setSubtitle(dynamicItem.getSubtitle());
            viewHolder.getDynamicItem().setColorType(dynamicItem.getColorType());
            viewHolder.getDynamicItem().setColor(dynamicItem.getColor());
            viewHolder.getDynamicItem().setShowDivider(dynamicItem.isShowDivider());

            if (dynamicItem.getOnClickListener() != null) {
                viewHolder.getDynamicItem().setOnClickListener(
                        dynamicItem.getOnClickListener());
            } else {
                viewHolder.getDynamicItem().setClickable(false);
            }

            viewHolder.getDynamicItem().onUpdate();
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : 1;
    }

    /**
     * Get the data used by this binder.
     *
     * @return The data used by this binder.
     */
    public @Nullable DynamicItem getData() {
        return mData;
    }

    /**
     * Set the data for this binder.
     *
     * @param data The data to be set.
     */
    public void setData(@Nullable DynamicItem data) {
        this.mData = data;

        notifyBinderDataSetChanged();
    }

    /**
     * View holder to hold the dynamic item view layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Dynamic item view for this view holder.
         */
        private final DynamicItemView dynamicItem;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(View view) {
            super(view);

            dynamicItem = view.findViewById(R.id.ads_dynamic_item_view);
        }

        /**
         * Get the dynamic info view for this view holder.
         *
         * @return The dynamic item view for this view holder.
         */
        public DynamicItemView getDynamicItem() {
            return dynamicItem;
        }
    }
}
