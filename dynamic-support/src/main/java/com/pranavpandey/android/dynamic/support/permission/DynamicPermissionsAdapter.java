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

package com.pranavpandey.android.dynamic.support.permission;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * A {@link RecyclerView.Adapter} to show the {@link DynamicPermissions}.
 */
public class DynamicPermissionsAdapter extends
        RecyclerView.Adapter<DynamicPermissionsAdapter.ViewHolder> {

    /**
     * List of permissions shown by this adapter.
     */
    private ArrayList<DynamicPermission> mPermissions;

    /**
     * Callback when a permission is selected.
     */
    private DynamicPermissionsView.PermissionListener mPermissionListener;

    /**
     * View holder to hold the permission layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Layout used by this holder.
         */
        private final ViewGroup layout;

        /**
         * Image view to show the permission status icon.
         */
        private final ImageView statusIcon;

        /**
         * Image view to show the permission icon.
         */
        private final ImageView icon;

        /**
         * Text view to show the permission title.
         */
        private final TextView title;

        /**
         * Text view to show the permission subtitle.
         */
        private final TextView subtitle;

        /**
         * Text view to show the permission info.
         */
        private final TextView info;

        /**
         * Text view to show the permission description.
         */
        private final TextView description;

        /**
         * Text view to show the permission status.
         */
        private final TextView status;

        public ViewHolder(@NonNull View view) {
            super(view);

            layout = view.findViewById(R.id.ads_info_layout);
            icon = view.findViewById(R.id.ads_info_icon_big);
            statusIcon = view.findViewById(R.id.ads_info_icon);
            title = view.findViewById(R.id.ads_info_title);
            subtitle = view.findViewById(R.id.ads_info_subtitle);
            info = view.findViewById(R.id.ads_info_status_description);
            description = view.findViewById(R.id.ads_info_description);
            status = view.findViewById(R.id.ads_info_status);
        }

        /**
         * Getter for {@link #layout}.
         */
        public ViewGroup getLayout() {
            return layout;
        }

        /**
         * Getter for {@link #statusIcon}.
         */
        public ImageView getStatusIcon() {
            return statusIcon;
        }

        /**
         * Getter for {@link #icon}.
         */
        public ImageView getIcon() {
            return icon;
        }

        /**
         * Getter for {@link #title}.
         */
        public TextView getTitle() {
            return title;
        }

        /**
         * Getter for {@link #subtitle}.
         */
        public TextView getSubtitle() {
            return subtitle;
        }

        /**
         * Getter for {@link #info}.
         */
        public TextView getInfo() {
            return info;
        }

        /**
         * Getter for {@link #description}.
         */
        public TextView getDescription() {
            return description;
        }

        /**
         * Getter for {@link #status}.
         */
        public TextView getStatus() {
            return status;
        }
    }

    public DynamicPermissionsAdapter(
            @NonNull ArrayList<DynamicPermission> dataSet,
            @Nullable DynamicPermissionsView.PermissionListener permissionListener) {
        mPermissions = dataSet;
        mPermissionListener = permissionListener;

        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ads_layout_info, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (mPermissionListener != null) {
            viewHolder.getLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPermissionListener.onPermissionSelected(v, viewHolder.getAdapterPosition(),
                            getItem(viewHolder.getAdapterPosition()));
                }
            });
        } else {
            viewHolder.getLayout().setClickable(false);
        }

        DynamicPermission dynamicPermission = getItem(position);

        if (dynamicPermission.getIcon() != null) {
            viewHolder.getIcon().setImageDrawable(dynamicPermission.getIcon());
            viewHolder.getIcon().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getIcon().setVisibility(GONE);
        }

        if (dynamicPermission.getTitle() != null) {
            viewHolder.getTitle().setText(dynamicPermission.getTitle());
            viewHolder.getTitle().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getTitle().setVisibility(GONE);
        }

        if (dynamicPermission.getSubtitle() != null) {
            viewHolder.getSubtitle().setText(dynamicPermission.getSubtitle());
            viewHolder.getSubtitle().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getSubtitle().setVisibility(GONE);
        }

        if (dynamicPermission.getDescription() != null) {
            viewHolder.getDescription().setText(dynamicPermission.getDescription());
            viewHolder.getDescription().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getDescription().setVisibility(GONE);
        }

        if (dynamicPermission.isGranted()) {
            viewHolder.getStatusIcon().setImageResource(R.drawable.ads_ic_check);
            viewHolder.getInfo().setText(R.string.ads_perm_granted_desc);
            viewHolder.getStatus().setText(R.string.ads_perm_granted);
            viewHolder.getLayout().setClickable(false);
        } else {
            viewHolder.getStatusIcon().setImageResource(R.drawable.ads_ic_close);
            viewHolder.getInfo().setText(R.string.ads_perm_request_desc);
            viewHolder.getStatus().setText(R.string.ads_perm_request);

            if (!dynamicPermission.isAskAgain()) {
                viewHolder.getStatusIcon().setImageResource(R.drawable.ads_ic_error);
                viewHolder.getInfo().setText(R.string.ads_perm_denied_desc);
                viewHolder.getStatus().setText(R.string.ads_perm_denied);
            }
            viewHolder.getLayout().setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return mPermissions.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    /**
     * Get dynamic permission for a given position.
     *
     * @param position The position of the adapter
     *
     * @return The dynamic permission according to the supplied
     *         position.
     */
    public DynamicPermission getItem(int position) {
        return mPermissions.get(position);
    }
}
