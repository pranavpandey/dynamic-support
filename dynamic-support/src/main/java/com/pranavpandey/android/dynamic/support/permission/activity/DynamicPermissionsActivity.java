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

package com.pranavpandey.android.dynamic.support.permission.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.permission.fragment.DynamicPermissionsFragment;
import com.pranavpandey.android.dynamic.support.permission.listener.DynamicPermissionsListener;
import com.pranavpandey.android.dynamic.util.DynamicPackageUtils;

import java.util.List;

/**
 * Base activity to request the {@link DynamicPermission}. It will be useful to request a
 * permission from anywhere, even from a service. Extend this activity in your app and add
 * it in the manifest to provide additional functionality.
 *
 * <p>Then, register that activity to request the permissions via
 * {@link DynamicPermissions#setPermissionActivity(Class)}. Rest of the things will be handled
 * by the {@link DynamicPermissionsFragment}.
 *
 * <p>To request permissions, just call
 * {@link DynamicPermissions#requestPermissions(String[], boolean, Intent, int)} method anywhere
 * within the app.
 */
public class DynamicPermissionsActivity extends DynamicActivity
        implements DynamicPermissionsListener {

    /**
     * No. of permission shown by this activity.
     */
    private int mPermissionsCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ads_permissions);
        setSubtitle(DynamicPackageUtils.getAppLabel(getContext()));
        setAppBarBackDrop(R.drawable.ads_ic_security);
    }

    @Override
    protected void onNewIntent(@Nullable Intent intent, boolean newIntent) {
        super.onNewIntent(intent, newIntent);

        if (intent == null || intent.getAction() == null) {
            return;
        }

        addHeader(R.layout.ads_header_appbar, true);

        if (getContentFragment() == null || newIntent) {
            switchFragment(DynamicPermissionsFragment.newInstance(getIntent()), false);
        }
    }

    @Override
    protected boolean setCollapsingToolbarLayout() {
        return true;
    }

    @Override
    public void onAddHeader(@Nullable View view) {
        super.onAddHeader(view);

        if (view == null) {
            return;
        }

        Dynamic.set(view.findViewById(R.id.ads_header_appbar_icon),
                DynamicPackageUtils.getAppIcon(this));
        Dynamic.set(view.findViewById(R.id.ads_header_appbar_title),
                DynamicPackageUtils.getAppLabel(this));

        if (mPermissionsCount > 0) {
            updateSubtitle(mPermissionsCount);
        }
    }

    /**
     * Update subtitle according to the permissions count.
     *
     * @param count The no. of permissions shown by this activity.
     */
    public void updateSubtitle(int count) {
        this.mPermissionsCount = count;

        if (findViewById(R.id.ads_header_appbar_subtitle) == null) {
            return;
        }

        Dynamic.set(findViewById(R.id.ads_header_appbar_subtitle), getString(count == 1
                ? R.string.ads_permissions_subtitle_single : R.string.ads_permissions_subtitle));
    }

    @Override
    public void onRequestDynamicPermissionsResult(@NonNull List<DynamicPermission> permissions,
            @NonNull String[] dangerousLeft, @NonNull List<DynamicPermission> specialLeft) { }
}
