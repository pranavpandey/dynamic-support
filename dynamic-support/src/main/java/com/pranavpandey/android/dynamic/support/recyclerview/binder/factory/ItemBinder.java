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

package com.pranavpandey.android.dynamic.support.recyclerview.binder.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicDataBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;

/**
 * A {@link DynamicRecyclerViewBinder} to bind the {@link DynamicItem} that can be used
 * with the {@link DynamicBinderAdapter}.
 */
public class ItemBinder extends DynamicDataBinder<DynamicItem, ItemBinder.ViewHolder> {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public ItemBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ads_layout_item, parent, false), R.id.ads_dynamic_item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (getData() == null || viewHolder.getDynamicItem() == null) {
            return;
        }

        Dynamic.setColorType(viewHolder.getDynamicItem(), getData().getColorType());
        Dynamic.setColor(viewHolder.getDynamicItem(), getData().getColor());
        Dynamic.setContrastWithColorTypeOrColor(viewHolder.getDynamicItem(),
                getData().getContrastWithColorType(), getData().getContrastWithColor());
        Dynamic.setBackgroundAwareSafe(viewHolder.getDynamicItem(),
                getData().getBackgroundAware());
        viewHolder.getDynamicItem().setIcon(getData().getIcon());
        viewHolder.getDynamicItem().setTitle(getData().getTitle());
        viewHolder.getDynamicItem().setSubtitle(getData().getSubtitle());
        viewHolder.getDynamicItem().setShowDivider(getData().isShowDivider());

        if (getData().getOnClickListener() != null) {
            viewHolder.getDynamicItem().setOnClickListener(getData().getOnClickListener());
        } else {
            viewHolder.getDynamicItem().setClickable(false);
        }

        if (getBinderAdapter().getRecyclerView() != null
                && getBinderAdapter().getRecyclerView().getLayoutManager()
                instanceof FlexboxLayoutManager) {
            viewHolder.getDynamicItem().getLayoutParams().width
                    = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
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
         * @param itemViewId The item view id to be used.
         */
        public ViewHolder(@NonNull View view, @IdRes int itemViewId) {
            super(view);

            dynamicItem = view.findViewById(itemViewId);
        }

        /**
         * Get the dynamic item view for this view holder.
         *
         * @return The dynamic item view for this view holder.
         */
        public @Nullable DynamicItemView getDynamicItem() {
            return dynamicItem;
        }
    }
}
