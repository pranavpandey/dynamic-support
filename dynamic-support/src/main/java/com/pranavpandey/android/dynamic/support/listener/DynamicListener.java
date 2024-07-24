/*
 * Copyright 2018-2024 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.listener;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.DynamicColors;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.product.DynamicProductFlavor;

/**
 * An interface to listen the dynamic change events.
 */
public interface DynamicListener extends DynamicProductFlavor {

    /**
     * Returns the context used by this listener.
     *
     * @return The context used by this listener.
     */
    @NonNull Context getContext();

    /**
     * This method will be called to get the intended theme styles version for this listener.
     *
     * @return The intended theme styles version for this listener.
     */
    @DynamicTheme.Version int getRequiredThemeVersion();

    /**
     * Checks whether the night mode is enabled for this listener.
     *
     * @param resolve {@code true} to resolve based on the theme data.
     *
     * @return {@code true} if the night mode is enabled for this listener.
     */
    boolean isNightMode(boolean resolve);

    /**
     * This method will be called to return the theme style resource for this listener.
     * <p>Override this method to supply your own customised style.
     *
     * @param theme The optional dynamic theme to resolve the style resource.
     *
     * @return The theme style resource for this listener.
     *
     * @see AppTheme#isDarkTheme()
     */
    @StyleRes int getThemeRes(@Nullable AppTheme<?> theme);

    /**
     * This method will be called to return the theme style resource for this listener.
     * <p>Override this method to supply your own customised style.
     *
     * @return The theme style resource for this listener.
     *
     * @see DynamicAppTheme#isDarkTheme()
     */
    @StyleRes int getThemeRes();

    /**
     * This method will be called to return the dynamic app theme for this listener.
     * <p>Override this method to supply your own customised theme.
     *
     * @return The dynamic app theme for this listener.
     */
    @Nullable AppTheme<?> getDynamicTheme();

    /**
     * Returns whether the dynamic colors are enabled for this listener.
     *
     * @return {@code true} if the dynamic colors are enabled for this listener.
     */
    boolean isDynamicColors();

    /**
     * Returns whether to apply the dynamic colors for this listener.
     *
     * @return {@code true} if the dynamic colors should be applied for this listener.
     */
    boolean isDynamicColor();

    /**
     * Returns whether the dynamic colors should be extracted from the system for this listener.
     *
     * @return {@code true} if the dynamic colors should be extracted from the system
     *         for this listener
     */
    boolean isSystemColor();

    /**
     * Returns whether the dynamic colors should be extracted from the wallpaper for this listener.
     *
     * @return {@code true} if the dynamic colors should be extracted from the wallpaper
     *         for this listener
     */
    boolean isWallpaperColor();

    /**
     * This method will be called to resolve the default color according to it's type.
     * <p>It is useful in resolving the color if the default theme also has auto values.
     *
     * @param colorType The color type to be resolved.
     *
     * @return The resolved default color according to the color type.
     *
     * @see Theme#AUTO
     */
    @ColorInt int getDefaultColor(@Theme.ColorType int colorType);

    /**
     * Returns whether to register a shared preferences listener for this listener.
     *
     * @return {@code true} to register a {@link SharedPreferences.OnSharedPreferenceChangeListener}
     *         to receive preference change callback.
     */
    boolean isOnSharedPreferenceChangeListener();

    /**
     * This method will be called when the dynamic change event occurs (like theme, locale, etc.).
     * <p>Recreate the activity or application here to adapt changes.
     *
     * @param context {@code true} if there is a context change and it must be reinitialized.
     * @param recreate {@code true} if listener must be recreated to adapt the changes.
     */
    void onDynamicChanged(boolean context, boolean recreate);

    /**
     * This method will be called when the dynamic configuration change event occurs
     * (like locale, font scale, orientation, ui mode, etc.).
     * <p>It will provide more control on {@link #onDynamicChanged(boolean, boolean)} method call.
     *
     * @param locale {@code true} if locale is changed.
     * @param fontScale {@code true} if font scale is changed.
     * @param orientation {@code true} if there is an orientation change.
     * @param uiMode {@code true} if ui mode is changed.
     * @param density {@code true} if configuration density is changed.
     *
     * @see ActivityInfo#CONFIG_LOCALE
     * @see ActivityInfo#CONFIG_FONT_SCALE
     * @see ActivityInfo#CONFIG_ORIENTATION
     * @see ActivityInfo#CONFIG_UI_MODE
     * @see ActivityInfo#CONFIG_DENSITY
     */
    void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density);

    /**
     * This method will be called when the dynamic color change event occurs according
     * to the wallpaper.
     * <p>Recreate the activity or application here to adapt changes.
     *
     * @param colors The new dynamic colors.
     * @param context {@code true} if there is a context change and it must be reinitialized.
     */
    void onDynamicColorsChanged(@Nullable DynamicColors colors, boolean context);

    /**
     * This method will be called when the auto theme change event occurs according to the time.
     * <p>Recreate the activity or application here to adapt changes.
     *
     * @param context {@code true} if there is a context change and it must be reinitialized.
     */
    void onAutoThemeChanged(boolean context);

    /**
     * This method will be called when the power save mode has been changed.
     *
     * <p>It will be called only on API 21 and above.
     *
     * @param powerSaveMode {@code true} if the device is in power save mode.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void onPowerSaveModeChanged(boolean powerSaveMode);

    /**
     * Returns whether the navigation bar theme should be applied for the activity.
     * <p>It will be applied only on the API 21 and above.
     *
     * @return {@code true} to apply navigation bar theme for the activity.
     */
    boolean setNavigationBarTheme();

    /**
     * This method will be called when the navigation bar theme has been changed.
     */
    void onNavigationBarThemeChanged();
}
