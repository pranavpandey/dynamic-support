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

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A simple recycler view adapter to implement the {@link DynamicBinderAdapter}
 * and {@link DynamicRecyclerViewBinder}.
 */
public class DynamicSimpleBinderAdapter extends DynamicBinderAdapter {

    /**
     * A list of data binders displayed by this adapter.
     */
    private List<DynamicRecyclerViewBinder> mDataBinders = new ArrayList<>();

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (int i = 0; i < mDataBinders.size(); i++) {
            DynamicRecyclerViewBinder binder = mDataBinders.get(i);
            itemCount += binder.getItemCount();
        }

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int itemCount = 0;
        for (int i = 0; i <  mDataBinders.size(); i++) {
            itemCount += mDataBinders.get(i).getItemCount();
            if (position < itemCount) {
                return i;
            }
        }

        throw new IllegalArgumentException("Position argument is invalid.");
    }

    @Override
    public <T extends DynamicRecyclerViewBinder> T getDataBinder(int viewType) {
        return (T) mDataBinders.get(viewType);
    }

    @Override
    public int getPosition(@NonNull DynamicRecyclerViewBinder binder, int binderPosition) {
        int viewType = mDataBinders.indexOf(binder);
        if (viewType < 0) {
            throw new IllegalStateException("Binder does not exists in the adapter.");
        }

        int position = binderPosition;
        for (int i = 0; i < viewType; i++) {
            position += mDataBinders.get(i).getItemCount();
        }

        return position;
    }

    @Override
    public int getBinderPosition(int position) {
        int binderItemCount;
        for (int i = 0; i < mDataBinders.size(); i++) {
            binderItemCount = mDataBinders.get(i).getItemCount();
            if (position - binderItemCount < 0) {
                break;
            }
            position -= binderItemCount;
        }

        return position;
    }

    @Override
    public void notifyBinderItemRangeChanged(@NonNull DynamicRecyclerViewBinder binder,
                                             int position, int itemCount) {
        notifyItemRangeChanged(getPosition(binder, position), itemCount);
    }

    @Override
    public void notifyBinderItemRangeInserted(@NonNull DynamicRecyclerViewBinder binder,
                                              int position, int itemCount) {
        notifyItemRangeInserted(getPosition(binder, position), itemCount);
    }

    @Override
    public void notifyBinderItemRangeRemoved(@NonNull DynamicRecyclerViewBinder binder,
                                             int position, int itemCount) {
        notifyItemRangeRemoved(getPosition(binder, position), itemCount);
    }

    /**
     * Getter for {@link #mDataBinders}.
     */
    public List<DynamicRecyclerViewBinder> getBinderList() {
        return mDataBinders;
    }

    /**
     * Add data binder to display in this adapter.
     *
     * @param binder DynamicTheme data binder to be added in this adapter.
     *
     * @see #mDataBinders
     */
    public void addDataBinder(@NonNull DynamicRecyclerViewBinder binder) {
        mDataBinders.add(binder);
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param binders A list of dynamic data binders to be added in this
     *                adapter.
     *
     * @see #mDataBinders
     */
    public void addDataBinders(@NonNull Collection<? extends DynamicRecyclerViewBinder> binders) {
        mDataBinders.addAll(binders);
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param binders An array of dynamic data binders to be added in this
     *                adapter.
     *
     * @see #mDataBinders
     */
    public void addDataBinders(@NonNull DynamicRecyclerViewBinder... binders) {
        mDataBinders.addAll(Arrays.asList(binders));
    }
}
