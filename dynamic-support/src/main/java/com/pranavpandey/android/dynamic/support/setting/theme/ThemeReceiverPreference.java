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

package com.pranavpandey.android.dynamic.support.setting.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.theme.ThemeContract;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.DynamicPackageUtils;

/**
 * A {@link DynamicSpinnerPreference} to display and edit the settings for the
 * {@link com.pranavpandey.android.dynamic.theme.receiver.DynamicThemeReceiver}.
 */
public class ThemeReceiverPreference extends DynamicSpinnerPreference {

    public ThemeReceiverPreference(@NonNull Context context) {
        super(context);
    }

    public ThemeReceiverPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeReceiverPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setActionButton(getContext().getString(R.string.ads_perm_info_required),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DynamicPackageUtils.isPackageExists(getContext(),
                        ThemeContract.Preset.AUTHORITY)) {
                    DynamicPermissions.getInstance().isGranted(
                            ThemeContract.Preset.RECEIVER_PERMISSIONS, true);
                } else {
                    DynamicLinkUtils.viewInGooglePlay(getContext(),
                            ThemeContract.Preset.AUTHORITY);
                }
            }
        });
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (DynamicPackageUtils.isPackageExists(getContext(), ThemeContract.Preset.AUTHORITY)) {
            Dynamic.setText(getActionView(), R.string.ads_perm_info_required);
            Dynamic.setVisibility(getDescriptionView(), GONE);
            Dynamic.setVisibility(getActionView(), DynamicPermissions.getInstance().isGranted(
                    ThemeContract.Preset.RECEIVER_PERMISSIONS, false) ? GONE :VISIBLE);
        } else {
            Dynamic.setText(getActionView(), R.string.ads_info_google_play);
            Dynamic.setVisibility(getDescriptionView(), VISIBLE);
            Dynamic.setVisibility(getActionView(), VISIBLE);
        }
    }
}
