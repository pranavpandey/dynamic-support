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

package com.pranavpandey.android.dynamic.support.permission.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.permission.view.DynamicPermissionsView;

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

    public DynamicPermissionsAdapter(@NonNull ArrayList<DynamicPermission> dataSet,
            @Nullable DynamicPermissionsView.PermissionListener permissionListener) {
        mPermissions = dataSet;
        mPermissionListener = permissionListener;

        setHasStableIds(true);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ads_layout_info, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        if (mPermissionListener != null) {
            viewHolder.getLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPermissionListener.onPermissionSelected(v, position, getItem(position));
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

        if (dynamicPermission.isAllowed()) {
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
     * Get the dynamic permission for a given position.
     *
     * @param position The position of the adapter.
     *
     * @return The dynamic permission according to the supplied position.
     */
    public DynamicPermission getItem(int position) {
        return mPermissions.get(position);
    }

    /**
     * View holder to hold the permission layout.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Layout used by this view holder.
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

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        ViewHolder(@NonNull View view) {
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
         * Get the layout used by this view holder.
         *
         * @return The layout used by this view holder.
         */
        ViewGroup getLayout() {
            return layout;
        }

        /**
         * Get the image view to show the permission status icon.
         *
         * @return The image view to show the permission status icon.
         */
        ImageView getStatusIcon() {
            return statusIcon;
        }

        /**
         * Get the image view to show the permission icon.
         *
         * @return The image view to show the permission icon.
         */
        ImageView getIcon() {
            return icon;
        }

        /**
         * Get the text view to show the permission title.
         *
         * @return The text view to show the permission title.
         */
        TextView getTitle() {
            return title;
        }

        /**
         * Get the text view to show the permission subtitle.
         *
         * @return The text view to show the permission subtitle.
         */
        TextView getSubtitle() {
            return subtitle;
        }

        /**
         * Get the text view to show the permission info.
         *
         * @return The text view to show the permission info.
         */
        TextView getInfo() {
            return info;
        }

        /**
         * Get the text view to show the permission description.
         *
         * @return The text view to show the permission description.
         */
        TextView getDescription() {
            return description;
        }

        /**
         * Get the text view to show the permission status.
         *
         * @return The text view to show the permission status.
         */
        TextView getStatus() {
            return status;
        }
    }
}
