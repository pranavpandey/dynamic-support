/*
 * Copyright 2019 Pranav Pandey
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

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A simple recycler view adapter to implement the {@link DynamicBinderAdapter}
 * and {@link DynamicRecyclerViewBinder}.
 */
public abstract class DynamicSimpleBinderAdapter<VB extends DynamicRecyclerViewBinder>
        extends DynamicBinderAdapter<VB> {

    /**
     * List of data binders displayed by this adapter.
     */
    private List<VB> mDataBinders = new ArrayList<>();

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
    public VB getDataBinder(int viewType) {
        return mDataBinders.get(viewType);
    }

    @Override
    public int getPosition(@NonNull VB binder, int position) {
        int viewType = mDataBinders.indexOf(binder);
        if (viewType < 0) {
            throw new IllegalStateException("Binder does not exists in the adapter.");
        }

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
    public void notifyBinderItemRangeChanged(@NonNull VB binder, int position, int itemCount) {
        notifyItemRangeChanged(getPosition(binder, position), itemCount);
    }

    @Override
    public void notifyBinderItemRangeInserted(@NonNull VB binder, int position, int itemCount) {
        notifyItemRangeInserted(getPosition(binder, position), itemCount);
    }

    @Override
    public void notifyBinderItemRangeRemoved(@NonNull VB binder, int position, int itemCount) {
        notifyItemRangeRemoved(getPosition(binder, position), itemCount);
    }

    /**
     * Get the list of data binders displayed by this adapter.
     *
     * @return The list of data binders displayed by this adapter.
     */
    public List<VB> getBinderList() {
        return mDataBinders;
    }

    /**
     * Add data binder to display in this adapter.
     *
     * @param binder The data binder to be added in this adapter
     */
    public void addDataBinder(@NonNull VB binder) {
        mDataBinders.add(binder);
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param binders The array of dynamic data binders to be added in this adapter.
     */
    public void addDataBinders(@NonNull Collection<VB> binders) {
        mDataBinders.addAll(binders);
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param binders The array of dynamic data binders to be added in this adapter.
     */
    @SafeVarargs
    public final void addDataBinders(@NonNull VB... binders) {
        mDataBinders.addAll(Arrays.asList(binders));
    }
}
