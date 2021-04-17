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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicQueryBinder;
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView;

/**
 * A {@link DynamicQueryBinder} to bind the {@link DynamicInfo} that can be used
 * with the {@link DynamicBinderAdapter}.
 */
public class InfoBinder<Query> extends DynamicQueryBinder<
        DynamicInfo, Query, InfoBinder.ViewHolder> {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public InfoBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ads_layout_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (getData() == null) {
            return;
        }

        viewHolder.getDynamicInfo().setIcon(getData().getIcon());
        viewHolder.getDynamicInfo().setIconBig(getData().getIconBig());
        viewHolder.getDynamicInfo().setTitle(getData().getTitle());
        viewHolder.getDynamicInfo().setSubtitle(getData().getSubtitle());
        viewHolder.getDynamicInfo().setDescription(getData().getDescription());
        viewHolder.getDynamicInfo().setLinks(getData().getLinks());
        viewHolder.getDynamicInfo().setLinksSubtitles(getData().getLinksSubtitles());
        viewHolder.getDynamicInfo().setLinksUrls(getData().getLinksUrls());
        viewHolder.getDynamicInfo().setLinksIconsId(getData().getLinksIconsResId());
        viewHolder.getDynamicInfo().setLinksDrawables(getData().getLinksDrawables());
        viewHolder.getDynamicInfo().setLinksColorsId(getData().getLinksColorsResId());
        viewHolder.getDynamicInfo().setLinksColors(getData().getLinksColors());
        viewHolder.getDynamicInfo().onUpdate();
    }

    /**
     * View holder to hold the dynamic info view layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Dynamic info view for this view holder.
         */
        private final DynamicInfoView dynamicInfo;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(@NonNull View view) {
            super(view);

            dynamicInfo = view.findViewById(R.id.ads_dynamic_info_view);
        }

        /**
         * Get the dynamic info view for this view holder.
         *
         * @return The dynamic info view for this view holder.
         */
        public @NonNull DynamicInfoView getDynamicInfo() {
            return dynamicInfo;
        }
    }
}
