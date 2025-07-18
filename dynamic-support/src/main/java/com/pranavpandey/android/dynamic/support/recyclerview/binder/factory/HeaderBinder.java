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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;

/**
 * An {@link ItemBinder} to bind the
 * {@link com.pranavpandey.android.dynamic.support.view.DynamicHeader} that can be used
 * with the {@link DynamicBinderAdapter}.
 */
public class HeaderBinder extends ItemBinder {

    /**
     * {@code true} to set the full span.
     */
    private boolean mIsFullSpanForStaggeredGrid;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public HeaderBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);

        this.mIsFullSpanForStaggeredGrid = true;
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ads_layout_header, parent, false), R.id.ads_header);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (isFullSpanForStaggeredGrid()) {
            DynamicLayoutUtils.setFullSpanForView(holder.itemView);
        }
    }

    /**
     * Returns whether to set the full span for
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
     * @return The {@link HeaderBinder} object to allow for chaining of calls to set methods.
     */
    public @NonNull HeaderBinder setFullSpanForStaggeredGrid(boolean fullSpanForStaggeredGrid) {
        this.mIsFullSpanForStaggeredGrid = fullSpanForStaggeredGrid;

        notifyBinderDataSetChanged();
        return this;
    }
}
