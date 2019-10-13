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

package com.pranavpandey.android.dynamic.support.sample.controller

import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils

/**
 * Helper class to perform theme related operations.
 */
object ThemeController {

    /**
     * `true` if `auto` theme is selected.
     */
    val isAutoTheme: Boolean get() = appThemeColor == Theme.AUTO


    /**
     * Getter and Setter for the app theme color.
     */
    private var appThemeColor: Int
        get() = DynamicPreferences.getInstance().load(
                Constants.PREF_SETTINGS_APP_THEME_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_DEFAULT)
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
                Constants.PREF_SETTINGS_APP_THEME_COLOR, color)

    /**
     * Getter and Setter for the app theme day color.
     */
    private var appThemeDayColor: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT)
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR, color)

    /**
     * Getter and Setter for the app theme night color.
     */
    private var appThemeNightColor: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT)
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR, color)

    /**
     * The app theme splash style according to the current settings.
     */
    val appStyle: Int
        @StyleRes get() = if (appThemeColor == Theme.AUTO) {
            getAppStyle(if (DynamicTheme.getInstance().isNight)
                appThemeNightColor
            else
                appThemeDayColor)
        } else {
            getAppStyle(appThemeColor)
        }

    /**
     * The background color according to the current settings.
     */
    private val backgroundColor: Int
        @ColorInt get() = if (appThemeColor == Theme.AUTO) {
            if (DynamicTheme.getInstance().isNight)
                appThemeNightColor
            else
                appThemeDayColor
        } else {
            appThemeColor
        }

    /**
     * Returns the app theme style according to the supplied color.
     *
     * @param color The color used for the background.
     *
     * @return The app theme style according to the supplied color.
     */
    @StyleRes private fun getAppStyle(@ColorInt color: Int): Int {
        return if (DynamicColorUtils.isColorDark(color))
            R.style.Sample
        else
            R.style.Sample_Light
    }

    /**
     * Set the application theme according to the current settings.
     */
    fun setApplicationTheme() {
        DynamicTheme.getInstance().application
                .setPrimaryColor(AppController.instance.colorPrimaryApp)
                .setPrimaryColorDark(Theme.AUTO)
                .setAccentColor(AppController.instance.colorAccentApp)
                .setBackgroundColor(backgroundColor)
                .setSurfaceColor(Theme.AUTO).autoGenerateColors();
    }

    /**
     * Set the local theme according to the current settings.
     */
    fun setLocalTheme() {
        DynamicTheme.getInstance().get()
                .setPrimaryColor(AppController.instance.colorPrimaryApp)
                .setPrimaryColorDark(Theme.AUTO)
                .setAccentColor(AppController.instance.colorAccentApp)
                .setBackgroundColor(backgroundColor)
                .setSurfaceColor(Theme.AUTO).autoGenerateColors()
    }
}
