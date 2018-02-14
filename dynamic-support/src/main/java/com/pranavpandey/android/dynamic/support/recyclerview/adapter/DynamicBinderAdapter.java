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

package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

/**
 * Simple recycler view adapter to display different types of items or views
 * in a recycler view. Each section can have a header or a completely different
 * item. Extend this adapter and use {@link DynamicRecyclerViewBinder} to create
 * binding logic for the each type of views.
 */
public abstract class DynamicBinderAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Recycler view displaying this adapter.
     */
    private RecyclerView mRecyclerView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getDataBinder(viewType).onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int binderPosition = getBinderPosition(position);
        getDataBinder(viewHolder.getItemViewType())
                .onBindViewHolder(viewHolder, binderPosition);
    }

    @Override
    public void onAttachedToRecyclerView(@Nullable RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public boolean isComputingLayout() {
        return mRecyclerView != null && mRecyclerView.isComputingLayout();
    }

    /**
     * Getter for {@link #mRecyclerView}.
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public abstract int getItemCount();

    @Override
    public abstract int getItemViewType(int position);

    /**
     * Get data binder according to the view type.
     *
     * @param viewType View type constant to get the data binder.
     *
     * @return Data binder associated with this view type.
     *
     * @see DynamicTypeBinderAdapter
     */
    public abstract <T extends DynamicRecyclerViewBinder> T getDataBinder(int viewType);

    /**
     * Get the position of a data binder item inside the recycler view.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position of the data binder item.
     *
     * @return Position of the data binder item inside the recycler view.
     */
    public abstract int getPosition(@NonNull DynamicRecyclerViewBinder binder, int position);

    /**
     * Get the position of a data binder inside the recycler view.
     *
     * @param position Position of the data binder.
     *
     * @return Position of the data binder inside the recycler view.
     */
    public abstract int getBinderPosition(int position);

    /**
     * Called when an item has been changed in the data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the item has been changed.
     */
    public void notifyBinderItemChanged(@NonNull DynamicRecyclerViewBinder binder,
                                        int position) {
        notifyItemChanged(getPosition(binder, position));
    }

    /**
     * Called when an item has been inserted in the data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the item is inserted.
     */
    public void notifyBinderItemInserted(@NonNull DynamicRecyclerViewBinder binder,
                                         int position) {
        notifyItemInserted(getPosition(binder, position));
    }

    /**
     * Called when an item has been removed in the data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the item has been removed.
     */
    public void notifyBinderItemRemoved(@NonNull DynamicRecyclerViewBinder binder,
                                        int position) {
        notifyItemRemoved(getPosition(binder, position));
    }

    /**
     * Called when an item has been moved in the data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param fromPosition Initial position of the moved item.
     * @param toPosition Final position of the moved item.
     */
    public void notifyBinderItemMoved(@NonNull DynamicRecyclerViewBinder binder,
                                      int fromPosition, int toPosition) {
        notifyItemMoved(getPosition(binder, fromPosition),
                getPosition(binder, toPosition));
    }

    /**
     * Called when the item range of a data binder has been changed.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the first item has been changed.
     * @param itemCount Total no. of items has been changed.
     */
    public abstract void notifyBinderItemRangeChanged(@NonNull DynamicRecyclerViewBinder binder,
                                                      int position, int itemCount);

    /**
     * Called when a set of items has been inserted in a data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the first item has been inserted.
     * @param itemCount Total no. of items has been inserted.
     */
    public abstract void notifyBinderItemRangeInserted(@NonNull DynamicRecyclerViewBinder binder,
                                                       int position, int itemCount);

    /**
     * Called when a set of items has been removed in a data binder.
     *
     * @param binder DynamicTheme data binder inside the recycler view.
     * @param position Position at which the first item has been removed.
     * @param itemCount Total no. of items has been removed.
     */
    public abstract void notifyBinderItemRangeRemoved(@NonNull DynamicRecyclerViewBinder binder,
                                                      int position, int itemCount);
}
