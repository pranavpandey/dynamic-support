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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.permission.view.DynamicPermissionsView;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView;
import com.pranavpandey.android.dynamic.util.DynamicTextUtils;

import java.util.List;
import java.util.Locale;

/**
 * A {@link RecyclerView.Adapter} to show the {@link DynamicPermissions}.
 */
public class DynamicPermissionsAdapter extends
        RecyclerView.Adapter<DynamicPermissionsAdapter.ViewHolder> {

    /**
     * List of permissions shown by this adapter.
     */
    private final List<DynamicPermission> mData;

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
        mData = data;
        mPermissionListener = permissionListener;
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.ads_layout_info_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        final DynamicPermission dynamicPermission;
        if ((dynamicPermission = getItem(position)) == null) {
            return;
        }

        if (mPermissionListener != null) {
            Dynamic.setOnClickListener(holder.getInfoView(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPermissionListener.onPermissionSelected(v,
                            holder.getAdapterPosition(), dynamicPermission);
                }
            });
        } else {
            Dynamic.setClickable(holder.getInfoView(), false);
        }

        holder.getInfoView().setIconBig(dynamicPermission.getIcon());
        holder.getInfoView().setTitle(DynamicTextUtils.capitalize(
                dynamicPermission.getTitle(), Locale.getDefault()));
        holder.getInfoView().setSubtitle(dynamicPermission.getSubtitle());
        holder.getInfoView().setDescription(dynamicPermission.getDescription());

        if (dynamicPermission.isAllowed()) {
            holder.getInfoView().setIcon(DynamicResourceUtils.getDrawable(
                    holder.getInfoView().getContext(), R.drawable.ads_ic_check));
            holder.getInfoView().setDescription(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_granted_desc));
            holder.getInfoView().setStatus(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_granted));
            Dynamic.setClickable(holder.getRoot(), false);
        } else {
            holder.getInfoView().setIcon(DynamicResourceUtils.getDrawable(
                    holder.getInfoView().getContext(), R.drawable.ads_ic_close));
            holder.getInfoView().setDescription(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_request_desc));
            holder.getInfoView().setStatus(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_request));

            if (!dynamicPermission.isAskAgain()) {
                holder.getInfoView().setIcon(DynamicResourceUtils.getDrawable(
                        holder.getInfoView().getContext(), R.drawable.ads_ic_error_outline));
                holder.getInfoView().setDescription(holder.getInfoView()
                        .getContext().getString(R.string.ads_perm_denied_desc));
                holder.getInfoView().setStatus(holder.getInfoView()
                        .getContext().getString(R.string.ads_perm_denied));
            }

            Dynamic.setClickable(holder.getRoot(), true);
        }

        if (dynamicPermission.isReinstall()) {
            holder.getInfoView().setDescription(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_reinstall_desc));
            holder.getInfoView().setStatus(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_reinstall));
            Dynamic.setClickable(holder.getRoot(), true);
        }

        if (dynamicPermission.isUnknown()) {
            holder.getInfoView().setDescription(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_unknown_desc));
            holder.getInfoView().setStatus(holder.getInfoView()
                    .getContext().getString(R.string.ads_perm_unknown));
            Dynamic.setClickable(holder.getRoot(), false);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * Get the dynamic permission for a given position.
     *
     * @param position The position of the adapter.
     *
     * @return The dynamic permission according to the supplied position.
     */
    public @Nullable DynamicPermission getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    /**
     * View holder to hold the permission root.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Root view used by this view holder.
         */
        private final ViewGroup root;

        /**
         * Dynamic info view used by this view holder.
         */
        private final DynamicInfoView dynamicInfo;

        /**
         * Constructor to initialize views from the supplied root.
         *
         * @param view The view for this view holder.
         */
        public ViewHolder(@NonNull View view) {
            super(view);

            root = view.findViewById(R.id.ads_info_card);
            dynamicInfo = view.findViewById(R.id.ads_dynamic_info_view);

            Dynamic.setColorType(dynamicInfo.getIconView(), Defaults.ADS_COLOR_TYPE_ICON);
        }

        /**
         * Get the root view used by this view holder.
         *
         * @return The root view used by this view holder.
         */
        public @NonNull ViewGroup getRoot() {
            return root;
        }

        /**
         * Get the dynamic info view used by this view holder.
         *
         * @return The dynamic info view used by this view holder.
         */
        public @NonNull DynamicInfoView getInfoView() {
            return dynamicInfo;
        }
    }
}
