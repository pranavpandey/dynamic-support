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

package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.ItemBinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link DynamicSimpleBinderAdapter} to display the list of {@link DynamicItem}
 * inside a recycler view.
 */
public class DynamicItemsAdapter extends DynamicSimpleBinderAdapter<ItemBinder> {

    /**
     * The data used by this adapter.
     * <p>A list of {@link DynamicItem}.
     */
    private List<DynamicItem> mData;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param data The data for this adapter.
     */
    public DynamicItemsAdapter(@NonNull Collection<? extends DynamicItem> data) {
        this.mData = new ArrayList<>(data);

        addDataBinder(new ItemBinder(this));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        getDataBinder(getItemViewType(position)).setData(getItem(position));
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Get the data used by this adapter.
     *
     * @return The data used by this adapter.
     *         <p>A list of {@link DynamicItem}.
     */
    public @NonNull List<DynamicItem> getData() {
        return mData;
    }

    /**
     * Returns the item according to the supplied position.
     *
     * @param position The position to get the item.
     *
     * @return The item according to the supplied position.
     */
    public @Nullable DynamicItem getItem(int position) {
        return mData.get(position);
    }
}
