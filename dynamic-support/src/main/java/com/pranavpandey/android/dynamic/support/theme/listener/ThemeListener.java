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

package com.pranavpandey.android.dynamic.support.theme.listener;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;

/**
 * Interface to listen various theme events.
 *
 * @param <T> The type of the dynamic app theme.
 */
public interface ThemeListener<T extends DynamicAppTheme> {

    /**
     *  Activity scene transition name for the theme preview.
     */
    String ADS_NAME_THEME_PREVIEW = "ads_name:theme_preview";

    /**
     *  Activity scene transition name for the theme preview action.
     */
    String ADS_NAME_THEME_PREVIEW_ACTION = ADS_NAME_THEME_PREVIEW + ":action";

    /**
     *  Activity scene transition name for the theme preview icon.
     */
    String ADS_NAME_THEME_PREVIEW_ICON = ADS_NAME_THEME_PREVIEW + ":icon";

    /**
     * Constant to request the theme file location.
     */
    int REQUEST_THEME_LOCATION = 0;

    /**
     * Constant to request the theme file (code) location.
     */
    int REQUEST_THEME_CODE_LOCATION = 1;

    /**
     * Constant to request the custom theme file (code) location.
     */
    int REQUEST_THEME_CODE_LOCATION_ALT = 2;

    /**
     * Constant to request the theme import from file.
     */
    int REQUEST_THEME_IMPORT = 5;

    /**
     * Constant to request the theme from other apps.
     */
    int REQUEST_THEME_CAPTURE = 8;

    /**
     * Key for the background color preference.
     */
    String ADS_PREF_THEME_COLOR_BACKGROUND = "ads_pref_settings_theme_color_background";

    /**
     * Key for the tint background color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_BACKGROUND = "ads_pref_settings_theme_color_tint_background";

    /**
     * Key for the surface color preference.
     */
    String ADS_PREF_THEME_COLOR_SURFACE = "ads_pref_settings_theme_color_surface";

    /**
     * Key for the tint surface color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_SURFACE = "ads_pref_settings_theme_color_tint_surface";

    /**
     * Key for the primary color preference.
     */
    String ADS_PREF_THEME_COLOR_PRIMARY = "ads_pref_settings_theme_color_primary";

    /**
     * Key for the tint primary color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_PRIMARY = "ads_pref_settings_theme_color_tint_primary";

    /**
     * Key for the dark primary color preference.
     */
    String ADS_PREF_THEME_COLOR_PRIMARY_DARK = "ads_pref_settings_theme_color_primary_dark";

    /**
     * Key for the tint dark primary color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_PRIMARY_DARK =
            "ads_pref_settings_theme_color_tint_primary_dark";

    /**
     * Key for the accent color preference.
     */
    String ADS_PREF_THEME_COLOR_ACCENT = "ads_pref_settings_theme_color_accent";

    /**
     * Key for the tint accent color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_ACCENT = "ads_pref_settings_theme_color_tint_accent";

    /**
     * Key for the dark accent color preference.
     */
    String ADS_PREF_THEME_COLOR_ACCENT_DARK = "ads_pref_settings_theme_color_accent_dark";

    /**
     * Key for the tint dark accent color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_ACCENT_DARK =
            "ads_pref_settings_theme_color_tint_accent_dark";

    /**
     * Key for the error color preference.
     */
    String ADS_PREF_THEME_COLOR_ERROR = "ads_pref_settings_theme_color_error";

    /**
     * Key for the tint error color preference.
     */
    String ADS_PREF_THEME_COLOR_TINT_ERROR = "ads_pref_settings_theme_color_tint_error";

    /**
     * Key for the primary text color preference.
     */
    String ADS_PREF_THEME_TEXT_PRIMARY = "ads_pref_settings_theme_text_primary";

    /**
     * Key for the inverse primary text color preference.
     */
    String ADS_PREF_THEME_TEXT_INVERSE_PRIMARY = "ads_pref_settings_theme_text_inverse_primary";

    /**
     * Key for the secondary text color preference.
     */
    String ADS_PREF_THEME_TEXT_SECONDARY = "ads_pref_settings_theme_text_secondary";

    /**
     * Key for the inverse secondary text color preference.
     */
    String ADS_PREF_THEME_TEXT_INVERSE_SECONDARY =
            "ads_pref_settings_theme_text_inverse_secondary";

    /**
     * Key for the font scale preference.
     */
    String ADS_PREF_THEME_FONT_SCALE = "ads_pref_settings_theme_font_scale";

    /**
     * Key for the font scale alternate preference.
     */
    String ADS_PREF_THEME_FONT_SCALE_ALT = "ads_pref_settings_theme_font_scale_alt";

    /**
     * Key for the corner size preference.
     */
    String ADS_PREF_THEME_CORNER_SIZE = "ads_pref_settings_theme_corner_size";

    /**
     * Key for the corner size alternate preference.
     */
    String ADS_PREF_THEME_CORNER_SIZE_ALT = "ads_pref_settings_theme_corner_size_alt";

    /**
     * Key for the background aware preference.
     */
    String ADS_PREF_THEME_BACKGROUND_AWARE = "ads_pref_settings_theme_background_aware";

    /**
     * Key for the contrast preference.
     */
    String ADS_PREF_THEME_CONTRAST = "ads_pref_settings_theme_contrast";

    /**
     * Key for the contrast alternate preference.
     */
    String ADS_PREF_THEME_CONTRAST_ALT = "ads_pref_settings_theme_contrast_alt";

    /**
     * Key for the opacity preference.
     */
    String ADS_PREF_THEME_OPACITY = "ads_pref_settings_theme_opacity";

    /**
     * Key for the opacity alternate preference.
     */
    String ADS_PREF_THEME_OPACITY_ALT = "ads_pref_settings_theme_opacity_alt";

    /**
     * Key for the elevation preference.
     */
    String ADS_PREF_THEME_ELEVATION = "ads_pref_settings_theme_elevation";

    /**
     * Key for the style preference.
     */
    String ADS_PREF_THEME_STYLE = "ads_pref_settings_theme_style";

    /**
     * Returns whether to provide theme menu for default operations.
     *
     * @return {@code true} to provide theme menu for default operations.
     */
    boolean isThemeMenu();

    /**
     * This method will called to load settings from the supplied theme.
     *
     * @param theme The dynamic app theme to be loaded.
     */
    void onLoadTheme(@NonNull T theme);

    /**
     * Get the theme preview associated with this listener.
     *
     * @return The theme preview associated with this listener.
     */
    @Nullable ThemePreview<T> getThemePreview();

    /**
     * This method will be called on setting the action icon.
     * <p>It can be used to change the action icon while sharing the theme.
     *
     * @param themeAction The theme action to be used.
     * @param themePreview The theme preview to set the share action.
     * @param enable {@code true} to enable the share action.
     */
    void onSetAction(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, boolean enable);

    /**
     * This method will be called on error while doing theme operations.

     * @param themeAction The theme action to be used.
     * @param themePreview The theme preview to set the share action.
     * @param e The optional exception.
     */
    void onThemeError(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, @Nullable Exception e);

    /**
     * Interface to load the theme values.
     */
    interface Value {

        /**
         * Returns the resolved font scale for the theme.
         *
         * @return The resolved font scale for the theme.
         */
        int getFontScale();

        /**
         * Returns the resolved corner size for the theme.
         *
         * @return The resolved corner size for the theme.
         */
        int getCornerSize();

        /**
         * Returns the resolved background aware for the theme.
         *
         * @return The resolved background aware for the theme.
         */
        @Theme.BackgroundAware int getBackgroundAware();

        /**
         * Returns the resolved contrast for the theme.
         *
         * @return The resolved contrast for the theme.
         */
        int getContrast();

        /**
         * Returns the resolved opacity for the theme.
         *
         * @return The resolved opacity for the theme.
         */
        int getOpacity();

        /**
         * Returns the resolved elevation for the theme.
         *
         * @return The resolved elevation for the theme.
         */
        @Theme.Elevation int getElevation();

        /**
         * Returns the resolved style for the theme.
         *
         * @return The resolved style for the theme.
         */
        @Theme.Style int getStyle();
    }

    /**
     * Interface to listen the theme import events.
     *
     * @param <T> The type of the dynamic app theme.
     */
    interface Import<T extends DynamicAppTheme> {

        /**
         * Interface to listen the theme import (from file) events.
         *
         * @param <T> The type of the theme source.
         */
        interface File<T> {

            /**
             * This method will be called to get the theme source.
             *
             * @return The source to retrieve the theme.
             */
            @Nullable T getThemeSource();

            /**
             * This method will be called on importing the theme.
             *
             * @param theme The theme string to be imported.
             */
            void onImportTheme(@Nullable String theme);
        }

        /**
         * This method will be called on importing the theme.
         *
         * @param themeAction The theme action to be used.
         */
        void importTheme(@Theme.Action int themeAction);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         */
        void importTheme(@Nullable String theme);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         * @param themeAction The theme action to be used.
         */
        void importTheme(@Nullable String theme, @Theme.Action int themeAction);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         *
         * @return The imported theme.
         */
        @NonNull T onImportTheme(@NonNull String theme);
    }

    /**
     * Interface to listen the theme export events.
     *
     * @param <T> The type of the dynamic app theme.
     */
    interface Export<T extends DynamicAppTheme> extends ThemeListener<T> {

        /**
         * This method will be called to return a bitmap.
         *
         * @param themePreview The theme preview associated with this listener.
         * @param themeAction The theme action associated with this listener.
         *
         * @return The theme bitmap.
         */
        @Nullable Bitmap getThemeBitmap(@Nullable ThemePreview<T> themePreview,
                @Theme.Action int themeAction);

        /**
         * This method will be called to copy the theme string or data.
         *
         * @param themePreview The theme preview associated with this listener.
         */
        void onCopyTheme(@Nullable ThemePreview<T> themePreview);

        /**
         * This method will be called to get the theme export task.
         *
         * @param dialog The dialog interface if applicable.
         * @param themeAction The theme action to be used.
         * @param themePreview The theme preview for the task.
         *
         * @return The {@link DynamicTask} to perform theme export operations.
         */
        @NonNull DynamicTask<?, ?, ?> getThemeExportTask(@Nullable DialogInterface dialog,
                @Theme.Action int themeAction, @Nullable ThemePreview<T> themePreview);
    }

    /**
     * Interface to listen the theme code events.
     */
    interface Code {

        /**
         * This method will be called to retrieve the theme data.
         *
         * @return The theme data.
         */
        @Nullable String getThemeData();

        /**
         * This method will be called to retrieve the theme code bitmap URI.
         *
         * @return The theme code bitmap URI.
         */
        @Nullable Uri getThemeCode();
    }

    /**
     * Interface to listen the theme selection events.
     */
    interface Select {

        /**
         * This method will be called on selecting the theme.
         *
         * @param theme The selected theme.
         */
        void onThemeSelect(@Theme int theme);

        /**
         * This method will be called on copying the theme.
         */
        void onThemeCopy();
    }
}
