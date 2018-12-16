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
import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView;
import com.pranavpandey.android.dynamic.support.view.DynamicInfoViewBig;

/**
 * Binder to bind the {@link DynamicInfo} which can be used with the {@link DynamicBinderAdapter}.
 */
public class DynamicInfoBigBinder extends DynamicRecyclerViewBinder {

    /**
     * Data used by this binder.
     */
    private DynamicInfo mData;

    public DynamicInfoBigBinder(@NonNull DynamicBinderAdapter binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_layout_info_view_big, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder viewHolderInfo = (ViewHolder) viewHolder;
        DynamicInfo dynamicInfo = getData();

        viewHolderInfo.getDynamicInfoViewBig().setIcon(dynamicInfo.getIcon());
        viewHolderInfo.getDynamicInfoViewBig().setIconBig(dynamicInfo.getIconBig());
        viewHolderInfo.getDynamicInfoViewBig().setTitle(dynamicInfo.getTitle());
        viewHolderInfo.getDynamicInfoViewBig().setSubtitle(dynamicInfo.getSubtitle());
        viewHolderInfo.getDynamicInfoViewBig().setDescription(dynamicInfo.getDescription());
        viewHolderInfo.getDynamicInfoViewBig().setLinks(dynamicInfo.getLinks());
        viewHolderInfo.getDynamicInfoViewBig().setLinksSubtitles(dynamicInfo.getLinksSubtitles());
        viewHolderInfo.getDynamicInfoViewBig().setLinksUrls(dynamicInfo.getLinksUrls());
        viewHolderInfo.getDynamicInfoViewBig().setLinksIconsId(dynamicInfo.getLinksIconsResId());
        viewHolderInfo.getDynamicInfoViewBig().setLinksDrawables(dynamicInfo.getLinksDrawables());
        viewHolderInfo.getDynamicInfoViewBig().setLinksColorsId(dynamicInfo.getLinksColorsResId());
        viewHolderInfo.getDynamicInfoViewBig().setLinksColors(dynamicInfo.getLinksColors());

        viewHolderInfo.getDynamicInfoViewBig().onUpdate();
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
    public @Nullable DynamicInfo getData() {
        return mData;
    }

    /**
     * Set the data for this binder.
     *
     * @param data The data to be set.
     */
    public void setData(@Nullable DynamicInfo data) {
        this.mData = data;

        if (!getRecyclerViewAdapter().isComputingLayout()) {
            notifyBinderDataSetChanged();
        }
    }

    /**
     * View holder to hold the dynamic info view layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Dynamic info view for this view holder.
         */
        private final DynamicInfoViewBig dynamicInfoViewBig;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(View view) {
            super(view);

            dynamicInfoViewBig = view.findViewById(R.id.ads_dynamic_info_view_big);
        }

        /**
         * Get the dynamic info view for this view holder.
         *
         * @return The dynamic info view for this view holder.
         */
        public DynamicInfoView getDynamicInfoViewBig() {
            return dynamicInfoViewBig;
        }
    }
}
