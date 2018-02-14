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

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;

import java.util.ArrayList;

/**
 * A {@link android.widget.FrameLayout} containing a {@link RecyclerView}
 * to show the list of required permissions.
 */
public class DynamicPermissionsView extends DynamicRecyclerViewFrame {

    /**
     * Interface to provide callback when a permission is selected.
     */
    public interface PermissionListener {

        /**
         * This method will be called when a permission is selected.
         *
         * @param view The permission view.
         * @param dynamicPermission The selected permission.
         */
        void onPermissionSelected(@NonNull View view, int position,
                                  @NonNull DynamicPermission dynamicPermission);
    }

    /**
     * List to store all the required permissions.
     */
    private ArrayList<DynamicPermission> mPermissions;

    /**
     * List to store all the required dangerous permissions.
     */
    private ArrayList<DynamicPermission> mDangerousPermissions;

    /**
     * List to store the unrequested or denied permissions.
     */
    private ArrayList<DynamicPermission> mPermissionsLeft;

    /**
     * List to store the unrequested or denied dangerous permissions.
     */
    private ArrayList<DynamicPermission> mDangerousPermissionsLeft;

    /**
     * List to store the unrequested or denied special permissions.
     */
    private ArrayList<DynamicPermission> mSpecialPermissionsLeft;

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

    public DynamicPermissionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
    }

    /**
     * Set the permissions to be shown by this view. Generally, it will
     * be the required permissions to perform a particular action.
     *
     * @param permissions The permissions to be shown by this view.
     * @param permissionListener The DynamicTutorialListener to receive callback when a
     *                           permission is selected.
     *
     */
    public void setPermissions(@NonNull ArrayList<DynamicPermission> permissions,
                               @Nullable PermissionListener permissionListener) {
        mPermissions = permissions;
        mDangerousPermissions = new ArrayList<>();
        mPermissionsLeft = new ArrayList<>();
        mSpecialPermissionsLeft = new ArrayList<>();
        mDangerousPermissionsLeft = new ArrayList<>();

        for (DynamicPermission dynamicPermission : mPermissions) {
            if (!dynamicPermission.isGranted()) {
                mPermissionsLeft.add(dynamicPermission);

                if (dynamicPermission.isDangerous()) {
                    mDangerousPermissions.add(dynamicPermission);

                    dynamicPermission.setAskAgain(ActivityCompat.shouldShowRequestPermissionRationale(
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
     * @return An array of all the dangerous permissions.
     */
    public String[] getDangerousPermissions() {
        String[] permissions = new String[mDangerousPermissions.size()];

        for (int i = 0; i < mDangerousPermissions.size(); i ++) {
            permissions[i] = mDangerousPermissions.get(i).getPermission();
        }
        return permissions;
    }

    /**
     * @return An array of unrequested or denied dangerous permissions.
     */
    public String[] getDangerousPermissionsLeft() {
        String[] permissions = new String[mDangerousPermissionsLeft.size()];

        for (int i = 0; i < mDangerousPermissionsLeft.size(); i ++) {
            permissions[i] = mDangerousPermissionsLeft.get(i).getPermission();
        }
        return permissions;
    }

    /**
     * Getter for {@link #mSpecialPermissionsLeft}.
     */
    public ArrayList<DynamicPermission> getSpecialPermissionsLeft() {
        return mSpecialPermissionsLeft;
    }

    /**
     * @return {@code true} if all the permissions shown by this view has
     *         been granted.
     */
    public boolean isAllPermissionsGranted() {
        return !isSpecialPermissionsLeft() && !isDangerousPermissionsLeft();
    }

    /**
     * @return {@code true} if there is any permission shown by this view
     *         has not been requested or denied.
     */
    public boolean isPermissionsLeft() {
        return !mPermissionsLeft.isEmpty();
    }

    /**
     * @return {@code true} if there is any dangerous permission shown by this
     *         view has not been requested or denied.
     */
    public boolean isDangerousPermissionsLeft() {
        return !mDangerousPermissionsLeft.isEmpty();
    }

    /**
     * @return {@code true} if there is any special permission shown by this
     *         view has not been requested or denied.
     */
    public boolean isSpecialPermissionsLeft() {
        return !mSpecialPermissionsLeft.isEmpty();
    }
}
