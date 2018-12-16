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

package com.pranavpandey.android.dynamic.support.intent;

import android.annotation.TargetApi;
import android.os.Build;
import android.provider.Settings;

/**
 * Helper class to manage the intent and extras.
 */
@TargetApi(Build.VERSION_CODES.M)
public class DynamicIntent {

    /**
     * Constant for dynamic permission request.
     */
    public static final int REQUEST_PERMISSIONS = 0;

    /**
     * Constant for dynamic theme request.
     */
    public static final int REQUEST_THEME = 0;

    /**
     * Constant for dynamic day theme request.
     */
    public static final int REQUEST_THEME_DAY = 1;

    /**
     * Constant for dynamic night theme request.
     */
    public static final int REQUEST_THEME_NIGHT = 2;

    /**
     * Constant for dynamic theme action.
     */
    public static final String ACTION_THEME =
            "com.pranavpandey.android.dynamic.support.intent.action.THEME";

    /**
     * Constant for permissions action.
     */
    public static final String ACTION_PERMISSIONS =
            "com.pranavpandey.android.dynamic.support.intent.action.PERMISSIONS";

    /**
     * Settings action constant for write system settings.
     *
     * @see Settings#ACTION_MANAGE_WRITE_SETTINGS
     */
    public static final String ACTION_WRITE_SYSTEM_SETTINGS = Settings.ACTION_MANAGE_WRITE_SETTINGS;
    /**
     * Settings action constant for overlay settings.
     *
     * @see Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    public static final String ACTION_OVERLAY_SETTINGS = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
    /**
     * Settings action constant for usage access settings.
     *
     * @see Settings#ACTION_USAGE_ACCESS_SETTINGS
     */
    public static final String ACTION_USAGE_ACCESS_SETTINGS = Settings.ACTION_USAGE_ACCESS_SETTINGS;

    /**
     * Constant for text extra.
     */
    public static final String EXTRA_TEXT =
            "com.pranavpandey.android.dynamic.support.intent.extra.TEXT";

    /**
     * Constant for dynamic theme extra.
     */
    public static final String EXTRA_THEME =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME";

    /**
     * Constant for default dynamic theme extra.
     */
    public static final String EXTRA_THEME_DEFAULT =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME_DEFAULT";

    /**
     * Constant for permissions extra.
     */
    public static final String EXTRA_PERMISSIONS =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS";
    /**
     * Constant for permissions extra intent to perform it when all the permissions are granted.
     */
    public static final String EXTRA_PERMISSIONS_INTENT =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS_INTENT";
    /**
     * Constant for permissions extra action to perform it when all the permissions are granted.
     */
    public static final String EXTRA_PERMISSIONS_ACTION =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS_ACTION";
    /**
     * Constant for intent extra if updating the widget.
     */
    public static final String EXTRA_WIDGET_UPDATE =
            "com.pranavpandey.android.dynamic.support.intent.extra.WIDGET_UPDATE";
}
