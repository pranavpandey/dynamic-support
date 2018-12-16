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

/**
 * Binder to bind the {@link DynamicInfo} which can be used with the {@link DynamicBinderAdapter}.
 */
public class DynamicInfoBinder extends DynamicRecyclerViewBinder {

    /**
     * Data used by this binder.
     */
    private DynamicInfo mData;

    public DynamicInfoBinder(@NonNull DynamicBinderAdapter binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_layout_info_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder viewHolderInfo = (ViewHolder) viewHolder;
        DynamicInfo dynamicInfo = getData();

        viewHolderInfo.getDynamicInfoView().setIcon(dynamicInfo.getIcon());
        viewHolderInfo.getDynamicInfoView().setIconBig(dynamicInfo.getIconBig());
        viewHolderInfo.getDynamicInfoView().setTitle(dynamicInfo.getTitle());
        viewHolderInfo.getDynamicInfoView().setSubtitle(dynamicInfo.getSubtitle());
        viewHolderInfo.getDynamicInfoView().setDescription(dynamicInfo.getDescription());
        viewHolderInfo.getDynamicInfoView().setLinks(dynamicInfo.getLinks());
        viewHolderInfo.getDynamicInfoView().setLinksSubtitles(dynamicInfo.getLinksSubtitles());
        viewHolderInfo.getDynamicInfoView().setLinksUrls(dynamicInfo.getLinksUrls());
        viewHolderInfo.getDynamicInfoView().setLinksIconsId(dynamicInfo.getLinksIconsResId());
        viewHolderInfo.getDynamicInfoView().setLinksDrawables(dynamicInfo.getLinksDrawables());
        viewHolderInfo.getDynamicInfoView().setLinksColorsId(dynamicInfo.getLinksColorsResId());
        viewHolderInfo.getDynamicInfoView().setLinksColors(dynamicInfo.getLinksColors());

        viewHolderInfo.getDynamicInfoView().onUpdate();
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
        private final DynamicInfoView dynamicInfoView;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(View view) {
            super(view);

            dynamicInfoView = view.findViewById(R.id.ads_dynamic_info_view);
        }

        /**
         * Get the dynamic info view for this view holder.
         *
         * @return The dynamic info view for this view holder.
         */
        public DynamicInfoView getDynamicInfoView() {
            return dynamicInfoView;
        }
    }
}
