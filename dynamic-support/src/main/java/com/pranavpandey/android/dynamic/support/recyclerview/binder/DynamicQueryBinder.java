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

package com.pranavpandey.android.dynamic.support.recyclerview.binder;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * A {@link DynamicDataBinder} to bind the generic data with query that can be used
 * with the {@link DynamicBinderAdapter}.
 */
public abstract class DynamicQueryBinder<T, Q, VH extends RecyclerView.ViewHolder>
        extends DynamicDataBinder<T, VH> {

    /**
     * Query used by this binder.
     */
    private Q mQuery;

    /**
     * Highlight color used by this binder.
     *
     * @see DynamicAppTheme#getHighlightColor()
     */
    private @ColorInt int mHighlightColor;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public DynamicQueryBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);

        initHighlightColor();
    }

    /**
     * Set the data and query for this binder.
     *
     * @param data The data to be set.
     * @param query The query to be set.
     */
    public void setData(@Nullable T data, @Nullable Q query) {
        this.mQuery = query;

        setData(data);
    }

    /**
     * Get the query used by this binder.
     *
     * @return The query used by this binder.
     */
    public @Nullable Q getQuery() {
        return mQuery;
    }

    /**
     * Set the query for this binder.
     *
     * @param query The query to be set.
     */
    public void setQuery(@Nullable Q query) {
        setData(getData(), query);
    }

    /**
     * Initialize the highlight color for this binder.
     */
    public void initHighlightColor() {
        this.mHighlightColor = DynamicTheme.getInstance().get().getHighlightColor(
                DynamicTheme.getInstance().get().getSurfaceColor());
    }

    /**
     * Get the highlight color used by this binder.
     *
     * @return The highlight color used by this binder.
     */
    public @ColorInt int getHighlightColor() {
        return mHighlightColor;
    }

    /**
     * Set the highlight color for this binder.
     *
     * @param highlightColor The highlight color to be set.
     */
    public void setHighlightColor(@ColorInt int highlightColor) {
        this.mHighlightColor = highlightColor;

        notifyBinderDataSetChanged();
    }
}
