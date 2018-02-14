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

package com.pranavpandey.android.dynamic.support.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.utils.DynamicAppWidgetUtils.Theme.THEME_AUTO;
import static com.pranavpandey.android.dynamic.support.utils.DynamicAppWidgetUtils.Theme.THEME_CUSTOM;

/**
 * Helper class to perform various app widget operations. It will be used internally
 * by the {@link com.pranavpandey.android.dynamic.support.provider.DynamicAppWidgetProvider}
 * but can also be used by the other components.
 */
public class DynamicAppWidgetUtils {

    /**
     * Intent extra if updating the widget.
     */
    public static final String ADS_EXTRA_WIDGET_UPDATE = "ads_extra_widget_update";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef(value = { THEME_AUTO, THEME_CUSTOM })
    public @interface Theme {
        /**
         * Constant for automatic widget theme.
         */
        String THEME_AUTO = "0";

        /**
         * Constant for custom widget theme.
         */
        String THEME_CUSTOM = "1";
    }

    /**
     * Default alpha value for enabled or active views.
     */
    public static final int ALPHA_ACTION_ENABLED = 255;

    /**
     * Default alpha value for disabled or inactive views.
     */
    public static final int ALPHA_ACTION_DISABLED = 125;

    /**
     * Save a string preference for an app widget widget provider according
     * to the widget id.
     *
     * @param preferences The preference name to store the key.
     * @param appWidgetId The app widget id to create or find the
     *                    preference key.
     * @param value The value for the preference.
     */
    public static void saveWidgetSettings(@NonNull String preferences,
                                          int appWidgetId, @Nullable String value) {
        DynamicPreferences.getInstance().savePrefs(
                preferences, String.valueOf(appWidgetId), value);
    }

    /**
     * Load a string preference for an app widget widget provider according
     * to the widget id.
     *
     * @param preferences The preference name to store the key.
     * @param appWidgetId The app widget id to find the preference key.
     * @param value The default value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a string.
     */
    public static @Nullable String loadWidgetSettings(@NonNull String preferences,
                                                      int appWidgetId, @Nullable String value) {
        return DynamicPreferences.getInstance().loadPrefs(
                preferences, String.valueOf(appWidgetId), value);
    }

    /**
     * Remove a preference for an app widget widget provider according
     * to the widget id.
     *
     * @param preferences The preference name to remove the key.
     * @param appWidgetId The app widget id to find the preference key.
     */
    public static void deleteWidgetSettings(@NonNull String preferences, int appWidgetId) {
        DynamicPreferences.getInstance().deletePrefs(preferences, String.valueOf(appWidgetId));
    }

    /**
     * Cleanup preferences for an app widget provider when all of its widget
     * instances has been deleted.
     *
     * @param preferences The preference name to cleanup the preferences.
     */
    public static void cleanupPreferences(@NonNull String preferences) {
        DynamicPreferences.getInstance().deleteSharedPreferences(preferences);
    }
}
