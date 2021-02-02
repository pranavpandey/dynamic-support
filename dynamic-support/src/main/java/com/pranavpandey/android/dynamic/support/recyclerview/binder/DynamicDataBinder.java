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

package com.pranavpandey.android.dynamic.support.recyclerview.binder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;

import java.util.Collection;

/**
 * A {@link DynamicRecyclerViewBinder} to bind the generic data that can be used
 * with the {@link DynamicBinderAdapter}.
 */
public abstract class DynamicDataBinder<T, VH extends RecyclerView.ViewHolder>
        extends DynamicRecyclerViewBinder<VH> {

    /**
     * Data used by this binder.
     */
    private T mData;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public DynamicDataBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public int getItemCount() {
        return getData() == null ? 0 : getData() instanceof Collection
                ? ((Collection<?>) getData()).size() : 1;
    }

    /**
     * Get the data used by this binder.
     *
     * @return The data used by this binder.
     */
    public @Nullable T getData() {
        return mData;
    }

    /**
     * Set the data for this binder.
     *
     * @param data The data to be set.
     */
    public void setData(@Nullable T data) {
        this.mData = data;

        notifyBinderDataSetChanged();
    }

    /**
     * Set the data for this binder.
     *
     * @param data The data to be set.
     * @param position The binder position to be notified.
     */
    public void setData(@Nullable T data, int position) {
        this.mData = data;

        notifyBinderItemChanged(position);
    }
}
