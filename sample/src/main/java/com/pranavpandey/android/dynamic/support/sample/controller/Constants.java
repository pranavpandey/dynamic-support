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

package com.pranavpandey.android.dynamic.support.sample.controller;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Helper class to hold constants and shared preferences.
 */
public class Constants {

    /**
     * Google Play publisher name.
     */
    public static final String ME = "Pranav Pandey";

    /**
     * Open source repository url.
     */
    public static final String URL_GITHUB =
            "https://github.com/pranavpandey/dynamic-support";

    /**
     * Url to donate via PayPal.
     */
    public static final String URL_DONATE = "https://www.paypal.me/pranavpandeydev";

    /**
     * Default value for app theme color.
     * <p>{@code Auto} to use day and night themes.</p>
     */
    public static final @ColorInt int APP_THEME_COLOR =
            DynamicTheme.ADS_THEME_AUTO;

    /**
     * TODO: Default value for app theme day color.
     */
    public static final @ColorInt int APP_THEME_DAY_COLOR =
            Color.parseColor("#EAEAEA");

    /**
     * TODO: Default value for app theme night color.
     */
    public static final @ColorInt int APP_THEME_NIGHT_COLOR =
            Color.parseColor("#252525");

    /**
     * TODO: Default value for app theme primary color.
     */
    public static final @ColorInt int APP_THEME_COLOR_PRIMARY =
            Color.parseColor("#3F51B5");

    /**
     * TODO: Default value for app theme accent color.
     */
    public static final @ColorInt int APP_THEME_COLOR_ACCENT =
            Color.parseColor("#E91E63");

    /**
     * Shared preferences key for app theme color.
     */
    public static final String PREF_SETTINGS_APP_THEME_COLOR =
            "pref_settings_app_theme_color";

    /**
     * Shared preferences key for app theme day color.
     */
    public static final String PREF_SETTINGS_APP_THEME_DAY_COLOR =
            "pref_settings_app_theme_day_color";

    /**
     * Shared preferences key for app theme night color.
     */
    public static final String PREF_SETTINGS_APP_THEME_NIGHT_COLOR =
            "pref_settings_app_theme_night_color";

    /**
     * Shared preferences key for app theme primary color.
     */
    public static final String PREF_SETTINGS_APP_THEME_COLOR_PRIMARY =
            "pref_settings_app_theme_color_primary";

    /**
     * Shared preferences key for app theme accent color.
     */
    public static final String PREF_SETTINGS_APP_THEME_COLOR_ACCENT =
            "pref_settings_app_theme_color_accent";

    /**
     * Shared preferences key for navigation bar theme.
     */
    public static final String PREF_SETTINGS_NAVIGATION_BAR_THEME =
            "pref_settings_navigation_bar_theme";

    /**
     * Shared preferences default value for app theme color.
     */
    public static final int PREF_SETTINGS_APP_THEME_COLOR_DEFAULT =
            APP_THEME_COLOR;

    /**
     * Shared preferences default value for app theme day color.
     */
    public static final int PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT =
            APP_THEME_DAY_COLOR;

    /**
     * Shared preferences default value for app theme night color.
     */
    public static final int PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT =
            APP_THEME_NIGHT_COLOR;

    /**
     * Shared preferences default value for app theme primary color.
     */
    public static final @ColorInt int PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT =
            APP_THEME_COLOR_PRIMARY;

    /**
     * Shared preferences default value for app theme accent color.
     */
    public static final @ColorInt int PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT =
            APP_THEME_COLOR_ACCENT;

    /**
     * Shared preferences default value for navigation bar theme.
     */
    public static final boolean PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT = false;
}
