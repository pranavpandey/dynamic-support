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

package com.pranavpandey.android.dynamic.support.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;

/**
 * Helper class to perform various app widget operations. It will be used internally
 * by the {@link com.pranavpandey.android.dynamic.support.provider.DynamicAppWidgetProvider}
 * but can also be used by the other components.
 */
public class DynamicAppWidgetUtils {

    /**
     * Default alpha value for enabled or active views.
     */
    public static final int ALPHA_ACTION_ENABLED = 255;

    /**
     * Default alpha value for disabled or inactive views.
     */
    public static final int ALPHA_ACTION_DISABLED = 125;

    /**
     * Save a string preference for an app widget widget provider according to the widget id.
     *
     * @param preferences The preference name to store the key.
     * @param appWidgetId The app widget id to create or find the preference key.
     * @param value The value for the preference.
     */
    public static void saveWidgetSettings(@NonNull String preferences,
            int appWidgetId, @Nullable String value) {
        DynamicPreferences.getInstance().save(preferences, String.valueOf(appWidgetId), value);
    }

    /**
     * Load a string preference for an app widget widget provider according to the widget id.
     *
     * @param preferences The preference name to store the key.
     * @param appWidgetId The app widget id to find the preference key.
     * @param value The default value for the preference.
     *
     * @return Returns the preference value if it exists, or the default value.
     *         <p>Throws {@link ClassCastException} if there is a preference with this name
     *         that is not a string.
     */
    public static @Nullable String loadWidgetSettings(@NonNull String preferences,
            int appWidgetId, @Nullable String value) {
        return DynamicPreferences.getInstance().load(
                preferences, String.valueOf(appWidgetId), value);
    }

    /**
     * Remove a preference for an app widget widget provider according to the widget id.
     *
     * @param preferences The preference name to remove the key.
     * @param appWidgetId The app widget id to find the preference key.
     */
    public static void deleteWidgetSettings(@NonNull String preferences, int appWidgetId) {
        DynamicPreferences.getInstance().delete(preferences, String.valueOf(appWidgetId));
    }

    /**
     * Cleanup preferences for an app widget provider when all of its widget instances
     * have been deleted.
     *
     * @param preferences The preference name to cleanup the preferences.
     */
    public static void cleanupPreferences(@NonNull String preferences) {
        DynamicPreferences.getInstance().deleteSharedPreferences(preferences);
    }
}
