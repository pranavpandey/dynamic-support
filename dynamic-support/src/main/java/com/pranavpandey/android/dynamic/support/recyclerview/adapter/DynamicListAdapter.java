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

package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A {@link ListAdapter} to handle the generic data.
 *
 * @param <T> The type of the lists this adapter will receive.
 * @param <VH> The type of the recycler view holder.
 */
public abstract class DynamicListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends ListAdapter<T, VH> {

    /**
     * Recycler view displaying this adapter.
     */
    private RecyclerView mRecyclerView;

    /**
     * Constructor to initialize an object of this class.
     * 
     * @param diffCallback The diff callback to be used.
     */
    protected DynamicListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param config The config to be used.
     */
    protected DynamicListAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.mRecyclerView = recyclerView;
    }

    /**
     * Get the recycler view displaying this adapter.
     *
     * @return The recycler view displaying this adapter.
     */
    public @Nullable RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Returns the context associated with the recycler view.
     *
     * @return The context associated with the recycler view.
     */
    public @Nullable Context getContext() {
        if (getRecyclerView() == null) {
            return null;
        }

        return getRecyclerView().getContext();
    }

    /**
     * Returns the layout associated with the recycler view.
     *
     * @return The layout associated with the recycler view.
     */
    public @Nullable RecyclerView.LayoutManager getLayoutManager() {
        if (getRecyclerView() == null) {
            return null;
        }

        return getRecyclerView().getLayoutManager();
    }
}
