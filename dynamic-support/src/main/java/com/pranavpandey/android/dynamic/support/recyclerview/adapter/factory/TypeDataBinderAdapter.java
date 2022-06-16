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

package com.pranavpandey.android.dynamic.support.recyclerview.adapter.factory;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicTypeBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.Collection;

/**
 * A {@link DynamicTypeBinderAdapter} to handle the generic data that can be used
 * with the {@link com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicDataBinder}.
 */
public abstract class TypeDataBinderAdapter<T, E extends Enum<E>,
        VB extends DynamicRecyclerViewBinder<?>> extends DynamicTypeBinderAdapter<E, VB> {

    /**
     * Data used by this binder adapter.
     */
    private T mData;

    @Override
    public int getItemCount() {
        return getData() == null ? 0 : getData() instanceof Collection
                ? ((Collection<?>) getData()).size() : 1;
    }

    /**
     * Get the data used by this binder adapter.
     *
     * @return The data used by this binder adapter.
     */
    public @Nullable T getData() {
        return mData;
    }

    /**
     * Set the data for this binder adapter.
     *
     * @param data The data to be set.
     */
    public void setData(@Nullable T data) {
        this.mData = data;

        notifyBinderDataSetChanged();
    }

    /**
     * Set the data for this binder adapter.
     *
     * @param data The data to be set.
     * @param position The binder position to be notified.
     */
    public void setData(@Nullable T data, int position) {
        this.mData = data;

        notifyBinderItemChanged(getDataBinder(getItemViewType(position)), position);
    }
}
