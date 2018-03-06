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

package com.pranavpandey.android.dynamic.support.sample.binder;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;

/**
 * A recycler view binder to display translators info by using
 * {@link DynamicRecyclerViewBinder}.
 */
public class TranslatorsBinder extends DynamicRecyclerViewBinder {

    public TranslatorsBinder(@NonNull DynamicBinderAdapter dynamicBinderAdapter) {
        super(dynamicBinderAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_info_translators, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
                                 final int position) {
    }

    @Override
    public int getItemCount() {
        // Return item count.
        return 1;
    }

    /**
     * Holder class to hold view holder item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final DynamicInfoView dynamicInfoView;

        public ViewHolder(View view) {
            super(view);

            dynamicInfoView = view.findViewById(R.id.info_translators);

            RecyclerView.LayoutManager layoutManager = DynamicLayoutUtils
                    .getGridLayoutManager(dynamicInfoView.getContext());
            dynamicInfoView.getLinksView().setRecyclerViewLayoutManager(layoutManager);

            ((DynamicImageView) dynamicInfoView.getIconView())
                    .setColor(DynamicTheme.getInstance().getPrimaryColor());
            if (dynamicInfoView.getLinksColors() != null) {
                final @ColorInt Integer colors[] = dynamicInfoView.getLinksColors();
                colors[colors.length - 1] = DynamicTheme.getInstance().getAccentColor();
                dynamicInfoView.setLinksColors(colors);
                dynamicInfoView.update();
            }
        }

        public DynamicInfoView getDynamicInfoView() {
            return dynamicInfoView;
        }
    }
}