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

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

/**
 * A {@link TypeDataBinderAdapter} to handle the generic data with query that can be used
 * with the {@link com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicQueryBinder}.
 */
public abstract class TypeQueryBinderAdapter<T, Q, E extends Enum<E>,
        VB extends DynamicRecyclerViewBinder<?>> extends TypeDataBinderAdapter<T, E, VB> {

    /**
     * Query used by this binder adapter.
     */
    private Q mQuery;

    /**
     * Set the data and query for this binder adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     */
    public void setData(@Nullable T data, @Nullable Q query) {
        this.mQuery = query;

        setData(data);
    }

    /**
     * Get the query used by this binder adapter.
     *
     * @return The query used by this binder adapter.
     */
    public @Nullable Q getQuery() {
        return mQuery;
    }

    /**
     * Set the query for this binder adapter.
     *
     * @param query The query to be set.
     */
    public void setQuery(@Nullable Q query) {
        setData(getData(), query);
    }
}
