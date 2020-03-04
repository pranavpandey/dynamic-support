/*
 * Copyright 2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.permission.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.adapter.DynamicPermissionsAdapter;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link android.widget.FrameLayout} containing a {@link RecyclerView} to show the list of
 * required permissions.
 */
public class DynamicPermissionsView extends DynamicRecyclerViewFrame {

    /**
     * Interface to provide callback when a permission is selected.
     */
    public interface PermissionListener {

        /**
         * This method will be called when a permission is selected.
         *
         * @param view The selected permission view.
         * @param position The selected position.
         * @param dynamicPermission The selected permission.
         */
        void onPermissionSelected(@NonNull View view, int position,
                @NonNull DynamicPermission dynamicPermission);
    }

    /**
     * List to store all the required permissions.
     */
    private List<DynamicPermission> mPermissions = new ArrayList<>();

    /**
     * List to store all the required dangerous permissions.
     */
    private List<DynamicPermission> mDangerousPermissions = new ArrayList<>();;

    /**
     * List to store the unrequested or denied permissions.
     */
    private List<DynamicPermission> mPermissionsLeft = new ArrayList<>();;

    /**
     * List to store the unrequested or denied dangerous permissions.
     */
    private List<DynamicPermission> mDangerousPermissionsLeft = new ArrayList<>();;

    /**
     * List to store the unrequested or denied special permissions.
     */
    private List<DynamicPermission> mSpecialPermissionsLeft = new ArrayList<>();;

    /**
     * Permissions adapter to show the {@link DynamicPermission}.
     */
    private DynamicPermissionsAdapter mAdapter;

    public DynamicPermissionsView(@NonNull Context context) {
        super(context);
    }

    public DynamicPermissionsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicPermissionsView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public @Nullable RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return DynamicLayoutUtils.getLinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL);
    }

    /**
     * Set the permissions to be shown by this view.
     * <p>Generally, it will be the required permissions to perform a particular action.
     *
     * @param permissions The permissions to be shown by this view.
     * @param permissionListener The listener to receive callback when a permission is selected.
     *
     */
    public void setPermissions(@NonNull List<DynamicPermission> permissions,
            @Nullable PermissionListener permissionListener) {
        mPermissions = permissions;
        mDangerousPermissions = new ArrayList<>();
        mPermissionsLeft = new ArrayList<>();
        mSpecialPermissionsLeft = new ArrayList<>();
        mDangerousPermissionsLeft = new ArrayList<>();

        for (DynamicPermission dynamicPermission : mPermissions) {
            if (!dynamicPermission.isAllowed()) {
                mPermissionsLeft.add(dynamicPermission);

                if (dynamicPermission.isDangerous()) {
                    mDangerousPermissions.add(dynamicPermission);

                    dynamicPermission.setAskAgain(
                            ActivityCompat.shouldShowRequestPermissionRationale(
                            (Activity) getContext(), dynamicPermission.getPermission()));

                    if (dynamicPermission.isAskAgain()) {
                        mDangerousPermissionsLeft.add(dynamicPermission);
                    }
                } else {
                    dynamicPermission.setAskAgain(true);
                    mSpecialPermissionsLeft.add(dynamicPermission);
                }
            }
        }

        mAdapter = new DynamicPermissionsAdapter(permissions, permissionListener);
        getRecyclerView().setAdapter(mAdapter);
    }

    /**
     * Returns a list of all the required permissions.
     *
     * @return A list of all the required permissions.
     */
    public @NonNull List<DynamicPermission> getDynamicPermissions() {
        return mPermissions;
    }

    /**
     * Get an array of all the dangerous permissions.
     *
     * @return An array of all the dangerous permissions.
     */
    public @NonNull String[] getDangerousPermissions() {
        String[] permissions = new String[mDangerousPermissions.size()];

        for (int i = 0; i < mDangerousPermissions.size(); i ++) {
            permissions[i] = mDangerousPermissions.get(i).getPermission();
        }
        return permissions;
    }

    /**
     * Returns an array of unrequested or denied dangerous permissions.
     *
     * @return An array of unrequested or denied dangerous permissions.
     */
    public @NonNull String[] getDangerousPermissionsLeft() {
        String[] permissions = new String[mDangerousPermissionsLeft.size()];

        for (int i = 0; i < mDangerousPermissionsLeft.size(); i ++) {
            permissions[i] = mDangerousPermissionsLeft.get(i).getPermission();
        }
        return permissions;
    }

    /**
     * Returns a list of unrequested or denied special permissions.
     *
     * @return A list of unrequested or denied special permissions.
     */
    public @NonNull List<DynamicPermission> getSpecialPermissionsLeft() {
        return mSpecialPermissionsLeft;
    }

    /**
     * Checks whether all the permissions shown by this view are granted.
     *
     * @return {@code true} if all the permissions shown by this view have been granted.
     */
    public boolean isAllPermissionsGranted() {
        return !isSpecialPermissionsLeft() && !isDangerousPermissionsLeft();
    }

    /**
     * Checks whether there is any permission left.
     *
     * @return {@code true} if there is any permission shown by this view has not been
     *         requested or denied.
     */
    public boolean isPermissionsLeft() {
        return !mPermissionsLeft.isEmpty();
    }

    /**
     * Checks whether there is any dangerous permission left.
     *
     * @return {@code true} if there is any dangerous permission shown by this view has not been
     *         requested or denied.
     */
    public boolean isDangerousPermissionsLeft() {
        return !mDangerousPermissionsLeft.isEmpty();
    }

    /**
     * Checks whether there is any special permission left.
     *
     * @return {@code true} if there is any special permission shown by this view has not been
     *         requested or denied.
     */
    public boolean isSpecialPermissionsLeft() {
        return !mSpecialPermissionsLeft.isEmpty();
    }
}
