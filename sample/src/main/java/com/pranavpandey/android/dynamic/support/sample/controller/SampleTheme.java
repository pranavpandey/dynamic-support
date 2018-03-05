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

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * Helper class to perform theme related operations.
 */
public class SampleTheme {

    /**
     * @return {@code true} if {@code auto} theme is selected.
     */
    public static boolean isAutoTheme() {
        return getAppThemeColor() == DynamicTheme.ADS_THEME_AUTO;
    }

    /**
     * Set the app theme color.
     *
     * @param color The color to be used for the app theme.
     */
    public static void setAppThemeColor(@ColorInt int color) {
        DynamicPreferences.getInstance().savePrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR, color);
    }

    /**
     * @return The app theme color.
     */
    public static int getAppThemeColor() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_DEFAULT);
    }

    /**
     * Set the app theme day color.
     *
     * @param color The color to be used for the app day theme.
     */
    public static void setAppThemeDayColor(@ColorInt int color) {
        DynamicPreferences.getInstance().savePrefs(
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR, color);
    }

    /**
     * @return The app theme day color.
     */
    public static @ColorInt int getAppThemeDayColor() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT);
    }

    /**
     * Set the app night theme color.
     *
     * @param color The color to be used for the app night theme.
     */
    public static void setAppThemeNightColor(@ColorInt int color) {
        DynamicPreferences.getInstance().savePrefs(
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR, color);
    }

    /**
     * @return The app theme night color.
     */
    public static @ColorInt int getAppThemeNightColor() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR,
                Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT);
    }

    /**
     * @return The app theme style according to the supplied color.
     *
     * @param color The color used for the background.
     */
    public static @StyleRes int getAppStyle(@ColorInt int color) {
        return DynamicColorUtils.isColorDark(color)
                ? R.style.Sample : R.style.Sample_Light;
    }

    /**
     * @return The app theme splash style according to the supplied
     *         color.
     *
     * @param color The color used for the background.
     */
    public static @StyleRes int getSplashStyle(@ColorInt int color) {
        return DynamicColorUtils.isColorDark(color)
                ? R.style.Sample_Splash : R.style.Sample_Light_Splash;
    }

    /**
     * @return The app theme style according to the current
     *         settings.
     */
    public static @StyleRes int getSplashStyle() {
        if (getAppThemeColor() == DynamicTheme.ADS_THEME_AUTO) {
            return getSplashStyle(DynamicResourceUtils.isNight()
                    ? getAppThemeNightColor() : getAppThemeDayColor());
        } else {
            return getSplashStyle(getAppThemeColor());
        }
    }

    /**
     * @return The app theme splash style according to the current
     *         settings.
     */
    public static @StyleRes int getAppStyle() {
        if (getAppThemeColor() == DynamicTheme.ADS_THEME_AUTO) {
            return getAppStyle(DynamicResourceUtils.isNight()
                    ? getAppThemeNightColor() : getAppThemeDayColor());
        } else {
            return getAppStyle(getAppThemeColor());
        }
    }

    /**
     * @return The background color according to the current
     *         settings.
     */
    public static @ColorInt int getBackgroundColor() {
        if (getAppThemeColor() == DynamicTheme.ADS_THEME_AUTO) {
            return DynamicResourceUtils.isNight()
                    ? getAppThemeNightColor() : getAppThemeDayColor();
        } else {
            return getAppThemeColor();
        }
    }

    /**
     * Set the application theme according to the current settings.
     *
     * @param applicationContext The context to set the theme.
     */
    public static void setApplicationTheme(@NonNull Context applicationContext) {
        final @ColorInt int colorPrimary = SampleController.getInstance().getColorPrimaryApp();
        DynamicTheme.getInstance().setPrimaryColor(colorPrimary)
                .setPrimaryColorDark(DynamicColorUtils.shiftColor(
                        colorPrimary, DynamicTheme.ADS_COLOR_SHIFT_DARK_DEFAULT))
                .setAccentColor(SampleController.getInstance().getColorAccentApp())
                .setBackgroundColor(getBackgroundColor())
                .initializeColors().initializeRemoteColors(true);
    }

    /**
     * Set the local theme according to the current settings.
     *
     * @param context The context to set the theme.
     */
    public static void setLocalTheme(@NonNull Context context) {
        final @ColorInt int colorPrimary = SampleController.getInstance().getColorPrimaryApp();
        DynamicTheme.getInstance().setLocalPrimaryColor(colorPrimary)
                .setLocalPrimaryColorDark(DynamicColorUtils.shiftColor(
                        colorPrimary, DynamicTheme.ADS_COLOR_SHIFT_DARK_DEFAULT))
                .setLocalAccentColor(SampleController.getInstance().getColorAccentApp())
                .setLocalBackgroundColor(getBackgroundColor())
                .initializeLocalColors();
    }
}
