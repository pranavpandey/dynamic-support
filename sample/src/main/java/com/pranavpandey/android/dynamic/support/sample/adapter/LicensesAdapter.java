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

package com.pranavpandey.android.dynamic.support.sample.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicInfoBigBinder;
import com.pranavpandey.android.dynamic.support.sample.binder.LicenseBinder;

import java.util.ArrayList;

/**
 * A binder adapter to display a list of {@link DynamicInfo} by
 * using {@link DynamicSimpleBinderAdapter}.
 */
public class LicensesAdapter extends DynamicSimpleBinderAdapter {

    /**
     * Data set used by this adapter.
     */
    private ArrayList<DynamicInfo> mDataSet;

    public LicensesAdapter(@Nullable ArrayList<DynamicInfo> dataSet) {
        this.mDataSet = dataSet;

        // Add license binder for this adapter.
        addDataBinder(new LicenseBinder(this));
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((DynamicInfoBigBinder) getDataBinder(
                getItemViewType(position))).setData(getItem(position));

        super.onBindViewHolder(viewHolder, position);
    }

    /**
     * Getter for {@link #mDataSet}.
     */
    public ArrayList<DynamicInfo> getDataSet() {
        return mDataSet;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * @return The item according the position.
     *
     * @param position The position to retrieve the item.
     */
    public DynamicInfo getItem(int position) {
        return mDataSet.get(position);
    }
}
