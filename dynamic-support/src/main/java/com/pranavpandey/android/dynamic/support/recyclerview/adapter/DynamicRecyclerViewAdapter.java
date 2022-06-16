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

import androidx.annotation.IntDef;
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
 */
public abstract class DynamicRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    @IntDef(value = { TYPE_UNKNOWN, TYPE_EMPTY_VIEW, TYPE_SECTION_HEADER,
            TYPE_ITEM, TYPE_SETTING, TYPE_SECTION_DIVIDER })
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
}
