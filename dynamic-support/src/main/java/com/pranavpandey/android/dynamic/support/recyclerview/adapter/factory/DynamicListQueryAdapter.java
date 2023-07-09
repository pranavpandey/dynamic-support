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

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicListAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

import java.util.List;

/**
 * A {@link DynamicListAdapter} to handle the generic data with query.
 *
 * @param <T> The type of the lists this adapter will receive.
 * @param <Q> The type of the query this adapter will receive.
 * @param <VH> The type of the recycler view holder.
 */
public abstract class DynamicListQueryAdapter<T, Q, VH extends RecyclerView.ViewHolder>
        extends DynamicListAdapter<T, VH> implements Filterable {

    /**
     * Raw data used by this list adapter.
     */
    private List<T> mRawData;

    /**
     * Query used by this list adapter.
     */
    private Q mQuery;

    /**
     * Highlight color used by this adapter.
     *
     * @see DynamicAppTheme#getHighlightColor()
     */
    private @ColorInt int mHighlightColor;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param diffCallback The diff callback to be used.
     */
    protected DynamicListQueryAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);

        initHighlightColor();
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param config The config to be used.
     */
    protected DynamicListQueryAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);

        initHighlightColor();
    }

    /**
     * Get the raw data used by this list adapter.
     *
     * @return The raw data used by this list adapter.
     */
    public @Nullable List<T> getRawData() {
        return mRawData;
    }

    /**
     * Checks whether this adapter has raw data.
     *
     * @return {@code true} if this adapter has raw data.
     */
    public boolean isRawData() {
        return mRawData != null && !mRawData.isEmpty();
    }

    /**
     * Set the data and query for this list adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     * @param commitCallback Optional runnable that is executed when the List is committed,
     *                       if it is committed.
     * @param filtered {@code true} if the filtered data.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(@Nullable List<T> data, @Nullable Q query,
            @Nullable Runnable commitCallback, boolean filtered) {
        if (!filtered) {
            this.mRawData = data;
        }
        this.mQuery = query;

        super.submitList(data, commitCallback);

        if (filtered) {
            notifyDataSetChanged();
        }
    }

    /**
     * Set the data and query for this list adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     * @param commitCallback Optional runnable that is executed when the List is committed,
     *                       if it is committed.
     *
     * @see #setData(List, Object, Runnable, boolean)
     */
    public void setData(@Nullable List<T> data, @Nullable Q query,
            @Nullable Runnable commitCallback) {
        setData(data, query, commitCallback, false);
    }

    /**
     * Set the data and query for this list adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     * @param filtered {@code true} if the filtered data.
     *
     * @see #setData(List, Object, Runnable, boolean)
     */
    public void setData(@Nullable List<T> data, @Nullable Q query, boolean filtered) {
        setData(data, query, null, filtered);
    }

    /**
     * Set the data and query for this list adapter.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     *
     * @see #setData(List, Object, Runnable, boolean)
     */
    public void setData(@Nullable List<T> data, @Nullable Q query) {
        setData(data, query, null, false);
    }

    /**
     * Set the data and query for this list adapter.
     *
     * @param data The data to be set.
     * @param commitCallback Optional runnable that is executed when the List is committed,
     *                       if it is committed.
     *
     * @see #setData(List, Object, Runnable)
     */
    public void setData(@Nullable List<T> data, @Nullable Runnable commitCallback) {
        setData(data, getQuery(), commitCallback);
    }

    /**
     * Set the data for this list adapter.
     *
     * @param data The data to be set.
     *
     * @see #setData(List, Object)
     */
    public void setData(@Nullable List<T> data) {
        setData(data, getQuery());
    }

    /**
     * Get the query used by this list adapter.
     *
     * @return The query used by this list adapter.
     */
    public @Nullable Q getQuery() {
        return mQuery;
    }

    /**
     * Set the query for this list adapter.
     *
     * @param query The query to be set.
     */
    public void setQuery(@Nullable Q query) {
        setData(getCurrentList(), query, true);
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
     * Initialize the highlight color for this adapter.
     */
    public void initHighlightColor() {
        this.mHighlightColor = DynamicTheme.getInstance().get().getHighlightColor(
                DynamicTheme.getInstance().get().getSurfaceColor());
    }

    /**
     * Get the highlight color used by this adapter.
     *
     * @return The highlight color used by this adapter.
     */
    public @ColorInt int getHighlightColor() {
        return mHighlightColor;
    }

    /**
     * Set the highlight color for this adapter.
     *
     * @param highlightColor The highlight color to be set.
     */
    public void setHighlightColor(@ColorInt int highlightColor) {
        this.mHighlightColor = highlightColor;

        submitList(getCurrentList());
    }

    /**
     * Ths method will be called on filtering the data.
     *
     * @param data The data to be filtered.
     * @param query The query returned by the filter.
     *
     * @return The filtered data according to the supplied query,
     */
    protected abstract @Nullable List<T> onFilter(@Nullable List<T> data, @NonNull Q query);

    /**
     * Ths method will be called on publishing the results.
     *
     * @param data The data returned by the filter.
     * @param query The query returned by the filter.
     */
    protected void onPublishResults(@Nullable List<T> data, @NonNull Q query) {
        setData(data, query, true);
    }

    @Override
    public void submitList(@Nullable List<T> list) {
        setData(list);
    }

    @Override
    public void submitList(@Nullable List<T> list, @Nullable Runnable commitCallback) {
        setData(list, commitCallback);
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
                onPublishResults((List<T>) results.values, onQuery(constraint));
            }
        };
    }
}
