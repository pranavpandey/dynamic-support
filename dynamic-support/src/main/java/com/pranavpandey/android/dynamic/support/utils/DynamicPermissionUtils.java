/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;

/**
 * Helper class to work with permissions and {@link DynamicPermission}.
 */
public class DynamicPermissionUtils {

    /**
     * Default package scheme for the permission settings intent.
     */
    private static final String SCHEME = "package";

    /**
     * Open the settings activity according to the permission name.
     *
     * @param context The context to start the activity.
     * @param permission The permission name.
     *
     * @return {@code true} if permissions settings activity can be opened successfully.
     *         Otherwise, {@code false}.
     */
    public static boolean openPermissionSettings(@NonNull Context context,
            @NonNull String permission) {
        String action = getPermissionSettingsAction(permission);
        Intent intent = new Intent(action);

        if (action.equals(DynamicIntent.ACTION_OVERLAY_SETTINGS)
                || action.equals(DynamicIntent.ACTION_WRITE_SYSTEM_SETTINGS)) {
            Uri uri = Uri.fromParts(SCHEME, context.getPackageName(), null);
            intent.setData(uri);
        }

        try {
            context.startActivity(intent);
            return true;
        } catch (Exception ignored) {
        }

        return false;
    }

    /**
     * Launch app info by extracting the package name from the supplied context.
     *
     * @param context The context to start the activity.
     *
     * @return {@code true} if permissions settings activity can be opened successfully.
     *         Otherwise, {@code false}.
     */
    public static boolean launchAppInfo(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, context.getPackageName(), null);
        intent.setData(uri);

        try {
            context.startActivity(intent);
            return true;
        } catch (Exception ignored) {
        }

        return false;
    }

    /**
     * Get permission icon drawable resource according to the permission name.
     *
     * @param permission The permission name.
     *
     * @return The permission icon drawable resource.
     */
    public static @DrawableRes int getPermissionIcon(@NonNull String permission) {
        switch (permission) {
            default:
                return R.drawable.ads_ic_settings;
        }
    }

    /**
     * Get permission title string resource according to the permission name.
     *
     * @param permission The permission name.
     *
     * @return The permission title string resource.
     */
    public static @StringRes int getPermissionTitle(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return R.string.ads_perm_overlay;
            case Manifest.permission.PACKAGE_USAGE_STATS:
                return R.string.ads_perm_usage_access;
            case Manifest.permission.WRITE_SETTINGS:
                return R.string.ads_perm_write_system_settings;
            default:
                return R.string.ads_perm_default;
        }
    }

    /**
     * Get permission subtitle string resource according to the permission name.
     *
     * @param permission The permission name.
     *
     * @return The permission subtitle string resource.
     */
    public static @StringRes int getPermissionSubtitle(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return R.string.ads_perm_overlay_desc;
            case Manifest.permission.PACKAGE_USAGE_STATS:
                return R.string.ads_perm_usage_access_desc;
            case Manifest.permission.WRITE_SETTINGS:
                return R.string.ads_perm_write_system_settings_desc;
            default:
                return R.string.ads_perm_default_desc;
        }
    }

    /**
     * Get permission settings action according to the permission name.
     *
     * @param permission The permission name.
     *
     * @return The permission settings action.
     */
    public static @NonNull String getPermissionSettingsAction(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return DynamicIntent.ACTION_OVERLAY_SETTINGS;
            case Manifest.permission.PACKAGE_USAGE_STATS:
                return DynamicIntent.ACTION_USAGE_ACCESS_SETTINGS;
            case Manifest.permission.WRITE_SETTINGS:
                return DynamicIntent.ACTION_WRITE_SYSTEM_SETTINGS;
            case Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS:
                return DynamicIntent.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
            default:
                return Settings.ACTION_APPLICATION_DETAILS_SETTINGS;

        }
    }
}
