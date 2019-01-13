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
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;

/**
 * Binder to bind the {@link DynamicItem} which can be used with the {@link DynamicBinderAdapter}.
 */
public class DynamicItemBinder extends DynamicRecyclerViewBinder {

    /**
     * Data used by this binder.
     */
    private DynamicItem mData;

    public DynamicItemBinder(@NonNull DynamicBinderAdapter binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_layout_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder viewHolderItem = (ViewHolder) viewHolder;
        DynamicItem dynamicItem = getData();

        if (dynamicItem != null) {
            viewHolderItem.getDynamicItemView().setIcon(dynamicItem.getIcon());
            viewHolderItem.getDynamicItemView().setTitle(dynamicItem.getTitle());
            viewHolderItem.getDynamicItemView().setSubtitle(dynamicItem.getSubtitle());
            viewHolderItem.getDynamicItemView().setColorType(dynamicItem.getColorType());
            viewHolderItem.getDynamicItemView().setColor(dynamicItem.getColor());
            viewHolderItem.getDynamicItemView().setShowDivider(dynamicItem.isShowDivider());

            if (dynamicItem.getOnClickListener() != null) {
                viewHolderItem.getDynamicItemView()
                        .setOnClickListener(dynamicItem.getOnClickListener());
            } else {
                viewHolderItem.getDynamicItemView().setClickable(false);
            }

            viewHolderItem.getDynamicItemView().onUpdate();
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

        if (!getRecyclerViewAdapter().isComputingLayout()) {
            notifyBinderDataSetChanged();
        }
    }

    /**
     * View holder to hold the dynamic item view layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Dynamic item view for this view holder.
         */
        private final DynamicItemView dynamicItemView;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(View view) {
            super(view);

            dynamicItemView = view.findViewById(R.id.ads_dynamic_item_view);
        }

        /**
         * Get the dynamic info view for this view holder.
         *
         * @return The dynamic item view for this view holder.
         */
        public DynamicItemView getDynamicItemView() {
            return dynamicItemView;
        }
    }
}
