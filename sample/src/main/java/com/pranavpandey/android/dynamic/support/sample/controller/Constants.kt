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

package com.pranavpandey.android.dynamic.support.sample.controller

import android.graphics.Color
import androidx.annotation.ColorInt
import com.pranavpandey.android.dynamic.theme.Theme

/**
 * Helper class to hold constants and shared preferences.
 */
object Constants {

    /**
     * Google Play publisher name.
     */
    const val ME = "Pranav Pandey"

    /**
     * Rotation package name.
     */
    const val PACKAGE_ROTATION = "com.pranavpandey.rotation"

    /**
     * Everyday package name.
     */
    const val PACKAGE_EVERYDAY = "com.pranavpandey.calendar"

    /**
     * Palettes package name.
     */
    const val PACKAGE_PALETTES = "com.pranavpandey.theme"

    /**
     * Zerocros package name.
     */
    const val PACKAGE_ZEROCROS = "com.pranavpandey.tictactoe"

    /**
     * Open source repository url.
     */
    const val URL_GITHUB = "https://github.com/pranavpandey/dynamic-support"

    /**
     * Action for app shortcut intent.
     */
    const val ACTION_APP_SHORTCUT =
            "com.pranavpandey.android.dynamic.support.sample.intent.action.APP_SHORTCUT"

    /**
     * Constant for Sources app shortcut.
     */
    const val APP_SHORTCUT_SOURCES = "app_shortcut_sources"

    /**
     * Default value for app theme color.
     *
     * `Auto` to use day and night themes.
     */
    @ColorInt const val APP_THEME_COLOR = Theme.AUTO

    /**
     * TODO: Default value for app theme day color.
     */
    @ColorInt val APP_THEME_DAY_COLOR = Color.parseColor("#EAEAEA")

    /**
     * TODO: Default value for app theme night color.
     */
    @ColorInt val APP_THEME_NIGHT_COLOR = Color.parseColor("#252525")

    /**
     * TODO: Default value for app theme surface color.
     */
    @ColorInt val APP_THEME_COLOR_SURFACE = Theme.AUTO

    /**
     * TODO: Default value for app theme primary color.
     */
    @ColorInt val APP_THEME_COLOR_PRIMARY = Color.parseColor("#3F51B5")

    /**
     * TODO: Default value for app theme accent color.
     */
    @ColorInt val APP_THEME_COLOR_ACCENT = Color.parseColor("#E91E63")

    /**
     * Shared preferences key for first launch of the app.
     */
    const val PREF_FIRST_LAUNCH = "pref_first_launch"

    /**
     * Shared preferences key for app theme color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR = "pref_settings_app_theme_color"

    /**
     * Shared preferences key for app theme day color.
     */
    const val PREF_SETTINGS_APP_THEME_DAY_COLOR = "pref_settings_app_theme_day_color"

    /**
     * Shared preferences key for app theme night color.
     */
    const val PREF_SETTINGS_APP_THEME_NIGHT_COLOR = "pref_settings_app_theme_night_color"

    /**
     * Shared preferences key for app theme surface color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_SURFACE = "pref_settings_app_theme_color_surface"

    /**
     * Shared preferences key for app theme primary color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_PRIMARY = "pref_settings_app_theme_color_primary"

    /**
     * Shared preferences key for app theme accent color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_ACCENT = "pref_settings_app_theme_color_accent"

    /**
     * Shared preferences key for navigation bar theme.
     */
    const val PREF_SETTINGS_NAVIGATION_BAR_THEME = "pref_settings_navigation_bar_theme"

    /**
     * Shared preferences key for app shortcuts theme.
     */
    const val PREF_SETTINGS_APP_SHORTCUTS_THEME = "pref_settings_app_shortcuts_theme"

    /**
     * Shared preferences default value for first launch of the app.
     */
    const val PREF_FIRST_LAUNCH_DEFAULT = true

    /**
     * Shared preferences default value for app theme color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_DEFAULT = APP_THEME_COLOR

    /**
     * Shared preferences default value for app theme day color.
     */
    @ColorInt val PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT = APP_THEME_DAY_COLOR

    /**
     * Shared preferences default value for app theme night color.
     */
    @ColorInt val PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT = APP_THEME_NIGHT_COLOR

    /**
     * Shared preferences default value for app theme surface color.
     */
    @ColorInt val PREF_SETTINGS_APP_THEME_COLOR_SURFACE_DEFAULT = APP_THEME_COLOR_SURFACE

    /**
     * Shared preferences default value for app theme primary color.
     */
    @ColorInt val PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT = APP_THEME_COLOR_PRIMARY

    /**
     * Shared preferences default value for app theme accent color.
     */
    @ColorInt val PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT = APP_THEME_COLOR_ACCENT

    /**
     * Shared preferences default value for navigation bar theme.
     */
    const val PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT = false

    /**
     * Shared preferences default value for app shortcuts theme.
     */
    const val PREF_SETTINGS_APP_SHORTCUTS_THEME_DEFAULT = true
}
