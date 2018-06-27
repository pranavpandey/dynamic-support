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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;

/**
 * Base activity to request the {@link DynamicPermission}. It will be useful
 * to request a permission from anywhere, even from a service. Just extend
 * this activity in your app and add it in the manifest. Rest of the things
 *
 * <p>Then, register that activity to request the permissions via
 * {@link DynamicPermissions#setPermissionActivity(Class)}. Rest of the things
 * will be handled by the {@link DynamicPermissionsFragment}.</p>
 *
 * <p>To request permissions, just call
 * {@link DynamicPermissions#requestPermissions(String[], boolean, Intent, int)}
 * method anywhere within the app.</p>
 */
public abstract class DynamicPermissionsActivity extends DynamicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicPermissionsActivity.this.finish();
            }
        });

        setAppBarBackDrop(R.drawable.ads_ic_security);

        addHeader(R.layout.ads_layout_header_appbar, true);
        ((ImageView) findViewById(R.id.ads_header_toolbar_icon))
                .setImageDrawable(getApplicationInfo().loadIcon(getPackageManager()));
        ((TextView) findViewById(R.id.ads_header_toolbar_title))
                .setText(getApplicationInfo().loadLabel(getPackageManager()));

        if (getContentFragment() == null) {
            switchFragment(DynamicPermissionsFragment.newInstance(getIntent()), false);
        }
    }

    @Override
    protected int getContentRes() {
        return -1;
    }

    @Override
    protected boolean setCollapsingToolbarLayout() {
        return true;
    }

    /**
     * Update subtitle according to the permissions count.
     *
     * @param count The no. of permissions shown by this
     *              activity.
     */
    public void updateSubtitle(int count) {
        ((TextView) findViewById(R.id.ads_header_toolbar_subtitle))
                .setText(count == 1
                        ? R.string.ads_permissions_subtitle_single
                        : R.string.ads_permissions_subtitle);
    }
}
