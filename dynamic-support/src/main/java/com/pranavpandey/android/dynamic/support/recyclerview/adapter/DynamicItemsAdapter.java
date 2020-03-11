/*
 * Copyright 2018-2020 Pranav Pandey
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
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicItemBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link DynamicSimpleBinderAdapter} to display the list of {@link DynamicItem}
 * inside a recycler view.
 */
@SuppressWarnings({"rawtypes"})
public class DynamicItemsAdapter extends DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder> {

    /**
     * The data set used by this adapter.
     * <p>A list of {@link DynamicItem}.
     */
    private List<DynamicItem> mDataSet;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dataSet The data set for this adapter.
     */
    public DynamicItemsAdapter(@NonNull Collection<? extends DynamicItem> dataSet) {
        this.mDataSet = new ArrayList<>(dataSet);

        addDataBinder(new DynamicItemBinder(this));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((DynamicItemBinder) getDataBinder(getItemViewType(position))).setData(getItem(position));
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Get the data set used by this adapter.
     *
     * @return The data set used by this adapter.
     *         <p>A list of {@link DynamicItem}.
     */
    public @NonNull List<DynamicItem> getDataSet() {
        return mDataSet;
    }

    /**
     * Returns the item according to the supplied position.
     *
     * @param position The position to get the item.
     *
     * @return The item according to the supplied position.
     */
    public @Nullable DynamicItem getItem(int position) {
        return mDataSet.get(position);
    }
}
