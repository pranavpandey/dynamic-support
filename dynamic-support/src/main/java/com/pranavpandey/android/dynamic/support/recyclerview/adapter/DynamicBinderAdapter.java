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

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

/**
 * A {@link RecyclerView.Adapter} to display different types of items or views in a recycler view.
 * <p>Each section can have a header or a completely different item.
 *
 * <p>Extend this adapter and use {@link DynamicRecyclerViewBinder} to create binding logic
 * for the each type of views.
 *
 * @param <VB> The type of the dynamic recycler view binder.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DynamicBinderAdapter<VB extends DynamicRecyclerViewBinder>
        extends DynamicRecyclerViewAdapter<RecyclerView.ViewHolder> {

    @Override
    public @NonNull RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return getDataBinder(viewType).onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getDataBinder(holder.getItemViewType()).getRecyclerViewAdapter() != null) {
            getDataBinder(holder.getItemViewType()).onBindViewHolder(
                    holder, getBinderPosition(position));
        }
    }

    @Override
    public abstract int getItemCount();

    @Override
    public abstract int getItemViewType(int position);

    /**
     * Get data binder according to the view type.
     *
     * @param viewType The view type constant to get the data binder.
     *
     * @return The data binder associated with this view type.
     *
     * @see DynamicTypeBinderAdapter
     */
    public abstract VB getDataBinder(int viewType);

    /**
     * Get the position of a data binder item inside the recycler view.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position of the data binder item.
     *
     * @return The position of the data binder item inside the recycler view.
     */
    public abstract int getPosition(@NonNull VB binder, int position);

    /**
     * Get the position of a data binder inside the recycler view.
     *
     * @param position The position of the data binder.
     *
     * @return The position of the data binder inside the recycler view.
     */
    public abstract int getBinderPosition(int position);

    /**
     * This method will be called when the data has been changed.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void notifyBinderDataSetChanged() {
        if (!isComputingLayout()) {
            notifyDataSetChanged();
        }
    }

    /**
     * This method will be called when an item has been changed in the data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the item has been changed.
     */
    public void notifyBinderItemChanged(@NonNull VB binder, int position) {
        notifyItemChanged(getPosition(binder, position));
    }

    /**
     * This method will be called when an item has been inserted in the data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the item is inserted.
     */
    public void notifyBinderItemInserted(@NonNull VB binder, int position) {
        notifyItemInserted(getPosition(binder, position));
    }

    /**
     * This method will be called when an item has been removed in the data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the item has been removed.
     */
    public void notifyBinderItemRemoved(@NonNull VB binder, int position) {
        notifyItemRemoved(getPosition(binder, position));
    }

    /**
     * This method will be called when an item has been moved in the data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param fromPosition Initial position of the moved item.
     * @param toPosition Final position of the moved item.
     */
    public void notifyBinderItemMoved(@NonNull VB binder, int fromPosition, int toPosition) {
        notifyItemMoved(getPosition(binder, fromPosition), getPosition(binder, toPosition));
    }

    /**
     * This method will be called when the item range of a data binder has been changed.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the first item has been changed.
     * @param itemCount Total no. of items have been changed.
     */
    public abstract void notifyBinderItemRangeChanged(
            @NonNull VB binder, int position, int itemCount);

    /**
     * This method will be called when a set of items have been inserted in a data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the first item has been inserted.
     * @param itemCount Total no. of items have been inserted.
     */
    public abstract void notifyBinderItemRangeInserted(
            @NonNull VB binder, int position, int itemCount);

    /**
     * This method will be called when a set of items have been removed in a data binder.
     *
     * @param binder The data binder inside the recycler view.
     * @param position The position at which the first item has been removed.
     * @param itemCount Total no. of items have been removed.
     */
    public abstract void notifyBinderItemRangeRemoved(
            @NonNull VB binder, int position, int itemCount);
}
