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

package com.pranavpandey.android.dynamic.support.permission.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.permission.binder.DynamicPermissionBinder;
import com.pranavpandey.android.dynamic.support.permission.view.DynamicPermissionsView;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicRecyclerViewAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicTypeBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.factory.TypeDataBinderAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.HeaderBackgroundBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.HeaderBinder;

import java.util.List;

/**
 * A {@link TypeDataBinderAdapter} to show the {@link DynamicPermissions}.
 */
public class DynamicPermissionsAdapter extends TypeDataBinderAdapter<List<DynamicPermission>,
        DynamicTypeBinderAdapter.ItemViewType, DynamicRecyclerViewBinder<?>> {

    /**
     * Callback when a permission is selected.
     */
    private final DynamicPermissionsView.PermissionListener mPermissionListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param data The list of permissions shown by this adapter.
     * @param permissionListener The listener to receiver the permission events.
     */
    public DynamicPermissionsAdapter(@Nullable List<DynamicPermission> data,
            @Nullable DynamicPermissionsView.PermissionListener permissionListener) {
        mPermissionListener = permissionListener;

        putDataBinder(ItemViewType.HEADER, new HeaderBackgroundBinder(this));
        putDataBinder(ItemViewType.ITEM, new DynamicPermissionBinder(this));

        setData(data);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getData() != null) {
            switch (holder.getItemViewType()) {
                case DynamicRecyclerViewAdapter.TYPE_SECTION_HEADER:
                    ((HeaderBinder) getDataBinder(DynamicRecyclerViewAdapter.TYPE_SECTION_HEADER))
                            .setData(new DynamicItem().setTitle(
                                    getData().get(position).getSectionTitle()));
                    break;
                case DynamicRecyclerViewAdapter.TYPE_ITEM:
                    ((DynamicPermissionBinder) getDataBinder(DynamicRecyclerViewAdapter.TYPE_ITEM))
                            .setData(getData().get(position));
                    break;
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getData() != null ? getData().get(position).getItemViewType()
                : DynamicRecyclerViewAdapter.TYPE_UNKNOWN;
    }

    @Override
    public @NonNull ItemViewType getEnumFromOrdinal(int viewType) {
        switch (viewType) {
            case DynamicRecyclerViewAdapter.TYPE_SECTION_HEADER:
                return ItemViewType.HEADER;
            case DynamicRecyclerViewAdapter.TYPE_ITEM:
                return ItemViewType.ITEM;
            default:
                return ItemViewType.UNKNOWN;
        }
    }

    /**
     * Returns the callback when a permission is selected.
     *
     * @return The callback when a permission is selected.
     */
    public @Nullable DynamicPermissionsView.PermissionListener getPermissionListener() {
        return mPermissionListener;
    }
}
