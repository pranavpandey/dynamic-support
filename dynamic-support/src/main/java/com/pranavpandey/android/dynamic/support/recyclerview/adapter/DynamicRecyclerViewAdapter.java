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

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A {@link RecyclerView.Adapter} to display items in different sections.
 * <p>Each section can have a section header also.
 *
 * <p>Extend this adapter and implement {@link DynamicRecyclerViewItem} interface
 * in the object class.
 *
 * @param <VH> The type of the recycler view holder.
 */
public abstract class DynamicRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    /**
     * Constant for the type unknown.
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * Constant for the type empty view.
     */
    public static final int TYPE_EMPTY_VIEW = 1;

    /**
     * Constant for the type section header.
     */
    public static final int TYPE_SECTION_HEADER = 2;

    /**
     * Constant for the type item.
     */
    public static final int TYPE_ITEM = 3;

    /**
     * Constant for the type item setting.
     */
    public static final int TYPE_SETTING = 4;

    /**
     * Constant for the type section divider.
     */
    public static final int TYPE_SECTION_DIVIDER = 5;

    /**
     * Valid item types for this adapter.
     *
     * <p>0. {@link #TYPE_EMPTY_VIEW}
     * <br>1. {@link #TYPE_SECTION_HEADER}
     * <br>2. {@link #TYPE_ITEM}
     * <br>2. {@link #TYPE_SETTING}
     * <br>3. {@link #TYPE_SECTION_DIVIDER}
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType { }

    /**
     * Implement this interface in the object class to get item type and section header text.
     */
    public interface DynamicRecyclerViewItem {

        /**
         * This method will be called to return the item type of this object.
         *
         * @return The item type of this object.
         *
         * @see ItemType
         */
        int getItemViewType();

        /**
         * This method will be called to return the section title for the item type
         * {@link #TYPE_SECTION_HEADER}.
         *
         * @return The section title for the item type {@link #TYPE_SECTION_HEADER}.
         */
        @Nullable String getSectionTitle();
    }

    /**
     * Recycler view displaying this adapter.
     */
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(@Nullable RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    /**
     * Checks whether the recycler view associated with this adapter is computing layout.
     *
     * @return {@code true} if the recycler view is computing layout.
     */
    public boolean isComputingLayout() {
        if (getRecyclerView() == null) {
            return false;
        }

        return getRecyclerView().isComputingLayout();
    }

    /**
     * Get the recycler view displaying this adapter.
     *
     * @return The recycler view displaying this adapter.
     */
    public @Nullable RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Returns the context associated with the recycler view.
     *
     * @return The context associated with the recycler view.
     */
    public @Nullable Context getContext() {
        if (getRecyclerView() == null) {
            return null;
        }

        return getRecyclerView().getContext();
    }

    /**
     * Returns the layout associated with the recycler view.
     *
     * @return The layout associated with the recycler view.
     */
    public @Nullable RecyclerView.LayoutManager getLayoutManager() {
        if (getRecyclerView() == null) {
            return null;
        }

        return getRecyclerView().getLayoutManager();
    }
}
