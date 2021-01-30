/*
 * Copyright 2018-2020 Pranav Pandey
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.permission.fragment.DynamicPermissionsFragment;
import com.pranavpandey.android.dynamic.support.permission.listener.DynamicPermissionsListener;

import java.util.List;

/**
 * Base activity to request the {@link DynamicPermission}. It will be useful to request a
 * permission from anywhere, even from a service. Extend this activity in your app and add
 * it in the manifest.
 *
 * <p>Then, register that activity to request the permissions via
 * {@link DynamicPermissions#setPermissionActivity(Class)}. Rest of the things will be handled
 * by the {@link DynamicPermissionsFragment}.
 *
 * <p>To request permissions, just call
 * {@link DynamicPermissions#requestPermissions(String[], boolean, Intent, int)} method anywhere
 * within the app.
 */
public abstract class DynamicPermissionsActivity extends DynamicActivity
        implements DynamicPermissionsListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        setAppBarBackDrop(R.drawable.ads_ic_security);

        addHeader(R.layout.ads_header_appbar, true);
        ((ImageView) findViewById(R.id.ads_header_appbar_icon))
                .setImageDrawable(getApplicationInfo().loadIcon(getPackageManager()));
        ((TextView) findViewById(R.id.ads_header_appbar_title))
                .setText(getApplicationInfo().loadLabel(getPackageManager()));

        setupFragment(getIntent(), false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setupFragment(intent, true);
    }

    @Override
    protected boolean setCollapsingToolbarLayout() {
        return true;
    }

    /**
     * Setup fragment according to the intent.
     *
     * @param intent The received intent.
     * @param newIntent {@code true} if updating from the new intent.
     */
    private void setupFragment(@Nullable Intent intent, boolean newIntent) {
        if (intent != null && intent.getAction() != null) {
            if (getContentFragment() == null || newIntent) {
                switchFragment(DynamicPermissionsFragment.newInstance(getIntent()), false);
            }
        }
    }

    /**
     * Update subtitle according to the permissions count.
     *
     * @param count The no. of permissions shown by this activity.
     */
    public void updateSubtitle(int count) {
        ((TextView) findViewById(R.id.ads_header_appbar_subtitle))
                .setText(count == 1
                        ? R.string.ads_permissions_subtitle_single
                        : R.string.ads_permissions_subtitle);
    }

    @Override
    public void onRequestDynamicPermissionsResult(@NonNull List<DynamicPermission> permissions,
            @NonNull String[] dangerousLeft, @NonNull List<DynamicPermission> specialLeft) { }
}
