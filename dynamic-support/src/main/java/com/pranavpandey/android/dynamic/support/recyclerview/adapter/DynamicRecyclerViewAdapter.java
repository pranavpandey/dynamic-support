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

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Simple recycler view adapter to display items in different
 * sections. Each section can have a section header also. Extend this
 * adapter and implement {@link DynamicRecyclerViewItem} interface
 * in the object class.
 */
public abstract class DynamicRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Constant for the type empty view.
     */
    public static final int TYPE_EMPTY_VIEW = 0;

    /**
     * Constant for the type section header.
     */
    public static final int TYPE_SECTION_HEADER = 1;

    /**
     * Constant for the type item.
     */
    public static final int TYPE_ITEM = 2;

    /**
     * Constant for the type section divider.
     */
    public static final int TYPE_SECTION_DIVIDER = 3;

    /**
     * Valid item types for this adapter.
     *
     * <p>0. {@link #TYPE_EMPTY_VIEW}
     * <br />1. {@link #TYPE_SECTION_HEADER}
     * <br />2. {@link #TYPE_ITEM}
     * <br />3. {@link #TYPE_SECTION_DIVIDER}</p>
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { TYPE_EMPTY_VIEW, TYPE_SECTION_HEADER, TYPE_ITEM, TYPE_SECTION_DIVIDER })
    public @interface ItemType { }

    /**
     * Implement this interface in the object class to get item
     * type and section header text.
     */
    public interface DynamicRecyclerViewItem {

        /**
         * @return Item type of this object.
         *
         * @see ItemType
         */
        @ItemType int getItemViewType();

        /**
         * @return Section title for the item type {@link #TYPE_SECTION_HEADER}.
         */
        String getSectionTitle();
    }
}
