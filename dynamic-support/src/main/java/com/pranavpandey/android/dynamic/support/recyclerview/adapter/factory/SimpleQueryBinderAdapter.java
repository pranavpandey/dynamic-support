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

import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

/**
 * A {@link SimpleDataBinderAdapter} to handle the generic data with query that can be used
 * with the {@link com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicQueryBinder}.
 *
 * @param <T> The type of the data this adapter will receive.
 * @param <Q> The type of the query this adapter will receive.
 * @param <VB> The type of the dynamic recycler view binder.
 */
public abstract class SimpleQueryBinderAdapter<T, Q, VB extends DynamicRecyclerViewBinder<?>>
        extends SimpleDataBinderAdapter<T, VB> implements Filterable {

    /**
     * Raw data used by this binder adapter.
     */
    private T mRawData;

    /**
     * Query used by this binder adapter.
     */
    private Q mQuery;

    /**
     * Get the raw data used by this binder adapter.
     *
     * @return The raw data used by this binder adapter.
     */
    public @Nullable T getRawData() {
        return mRawData;
    }

    /**
     * Checks whether this adapter has raw data.
     *
     * @return {@code true} if this adapter has raw data.
     */
    public boolean isRawData() {
        return mRawData != null;
    }

    /**
     * Set the data and query for this binder adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     * @param filtered {@code true} if the filtered data.
     */
    public void setData(@Nullable T data, @Nullable Q query, boolean filtered) {
        if (!filtered) {
            this.mRawData = data;
        }
        this.mQuery = query;

        super.setData(data);
    }

    /**
     * Set the data and query for this binder adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     *
     * @see #setData(Object, Object, boolean)
     */
    public void setData(@Nullable T data, @Nullable Q query) {
        setData(data, query, false);
    }

    @Override
    public void setData(@Nullable T data) {
        setData(data, getQuery());
    }

    @Override
    public void setData(@Nullable T data, int position) {
        super.setData(data, position);

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
        setData(getData(), query, true);
    }

    /**
     * Ths method will be called on building the query for the filter.
     *
     * @param constraint The constraint returned by the filter.
     *
     * @return The query according to the constraint returned by the filter.
     */
    protected abstract @NonNull Q onQuery(@NonNull CharSequence constraint);

    /**
     * Ths method will be called on filtering the data.
     *
     * @param data The data to be filtered.
     * @param query The query returned by the filter.
     *
     * @return The filtered data according to the supplied query,
     */
    protected abstract @Nullable T onFilter(@Nullable T data, @NonNull Q query);

    /**
     * Ths method will be called on publishing the results.
     *
     * @param data The data returned by the filter.
     * @param query The query returned by the filter.
     */
    protected void onPublishResults(@Nullable T data, @NonNull Q query) {
        setData(data, query, true);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = TextUtils.isEmpty(constraint) ? getRawData()
                        : onFilter(getRawData(), onQuery(constraint));

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                onPublishResults((T) results.values, onQuery(constraint));
            }
        };
    }
}
