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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView;

/**
 * Binder to bind the {@link DynamicInfo} which can be used with the
 * {@link DynamicBinderAdapter}.
 */
public class DynamicInfoBinder extends DynamicRecyclerViewBinder {

    /**
     * Data used by this binder.
     */
    private DynamicInfo mData;

    public DynamicInfoBinder(@NonNull DynamicBinderAdapter dynamicBinderAdapter) {
        super(dynamicBinderAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_layout_info_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
                                 final int position) {
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
        viewHolderInfo.getDynamicInfoView().setLinksIconsId(dynamicInfo.getLinksIconsId());
        viewHolderInfo.getDynamicInfoView().setLinksDrawables(dynamicInfo.getLinksDrawables());
        viewHolderInfo.getDynamicInfoView().setLinksColorsId(dynamicInfo.getLinksColorsId());
        viewHolderInfo.getDynamicInfoView().setLinksColors(dynamicInfo.getLinksColors());

        viewHolderInfo.getDynamicInfoView().update();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : 1;
    }

    /**
     * Getter for {@link #mData}.
     */
    public DynamicInfo getData() {
        return mData;
    }

    /**
     * Setter for {@link #mData}.
     */
    public void setData(DynamicInfo data) {
        this.mData = data;

        if (!getRecyclerViewAdapter().isComputingLayout()) {
            notifyBinderDataSetChanged();
        }
    }

    /**
     * View holder to hold the dynamic info view layout.
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Dynamic view view for this view holder.
         */
        private final DynamicInfoView dynamicInfoView;

        public ViewHolder(View view) {
            super(view);

            dynamicInfoView = view.findViewById(R.id.ads_dynamic_info_view);
        }

        /**
         * Getter for {@link #dynamicInfoView}.
         */
        public DynamicInfoView getDynamicInfoView() {
            return dynamicInfoView;
        }
    }
}
