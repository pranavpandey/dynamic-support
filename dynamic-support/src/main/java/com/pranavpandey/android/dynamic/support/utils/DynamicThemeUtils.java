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

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

import org.json.JSONObject;

/**
 * Helper class to perform theme operations.
 */
public class DynamicThemeUtils {

    public static final String ADS_PATTERN_QUOTES = "[\"\\\"][\\s+][\\\"\"]";
    public static final String ADS_PATTERN_SPACE = "[\\s+]";

    /**
     * Serialized name for the theme resource.
     */
    public static final String ADS_NAME_THEME_RES = "themeRes";
    
    /**
     * Serialized name for the background color.
     */
    public static final String ADS_NAME_BACKGROUND_COLOR = "backgroundColor";

    /**
     * Serialized name for the tint background color.
     */
    public static final String ADS_NAME_TINT_BACKGROUND_COLOR = "tintBackgroundColor";

    /**
     * Serialized name for the primary color.
     */
    public static final String ADS_NAME_PRIMARY_COLOR = "primaryColor";

    /**
     * Serialized name for the tint primary color.
     */
    public static final String ADS_NAME_TINT_PRIMARY_COLOR = "tintPrimaryColor";

    /**
     * Serialized name for the primary color dark.
     */
    public static final String ADS_NAME_PRIMARY_COLOR_DARK = "primaryColorDark";

    /**
     * Serialized name for the tint primary color dark.
     */
    public static final String ADS_NAME_TINT_PRIMARY_COLOR_DARK = "tintPrimaryColorDark";

    /**
     * Serialized name for the accent color.
     */
    public static final String ADS_NAME_ACCENT_COLOR = "accentColor";

    /**
     * Serialized name for the tint accent color.
     */
    public static final String ADS_NAME_TINT_ACCENT_COLOR = "tintAccentColor";

    /**
     * Serialized name for the accent color dark.
     */
    public static final String ADS_NAME_ACCENT_COLOR_DARK = "accentColorDark";

    /**
     * Serialized name for the tint accent color dark.
     */
    public static final String ADS_NAME_TINT_ACCENT_COLOR_DARK = "tintAccentColorDark";

    /**
     * Serialized name for the text primary color.
     */
    public static final String ADS_NAME_TEXT_PRIMARY_COLOR = "textPrimaryColor";

    /**
     * Serialized name for the text primary color inverse.
     */
    public static final String ADS_NAME_TEXT_PRIMARY_COLOR_INVERSE = "textPrimaryColorInverse";

    /**
     * Serialized name for the text secondary color.
     */
    public static final String ADS_NAME_TEXT_SECONDARY_COLOR = "textSecondaryColor";

    /**
     * Serialized name for the text secondary color inverse.
     */
    public static final String ADS_NAME_TEXT_SECONDARY_COLOR_INVERSE = "textSecondaryColorInverse";

    /**
     * Serialized name for the corner radius.
     */
    public static final String ADS_NAME_CORNER_RADIUS = "cornerRadius";

    /**
     * Serialized name for the background aware.
     */
    public static final String ADS_NAME_BACKGROUND_AWARE = "backgroundAware";

    /**
     * Serialized name for the widget id.
     */
    public static final String ADS_NAME_WIDGET_ID = "widgetId";

    /**
     * Serialized name for the header.
     */
    public static final String ADS_NAME_HEADER = "header";

    /**
     * Serialized name for the opacity.
     */
    public static final String ADS_NAME_OPACITY = "opacity";

    /**
     * Constant for the auto value.
     */
    public static final String ADS_VALUE_AUTO = "auto";

    /**
     * Constant for the disable value.
     */
    public static final String ADS_VALUE_DISABLE = "disable";

    /**
     * Constant for the enable value.
     */
    public static final String ADS_VALUE_ENABLE = "enable";

    /**
     * Constant for the hide value.
     */
    public static final String ADS_VALUE_HIDE = "hide";

    /**
     * Constant for the show value.
     */
    public static final String ADS_VALUE_SHOW = "show";

    /**
     * Checks whether the string is a valid JSON.
     *
     * @param string The string to be checked.
     *
     * @return {@code true} if the supplied string is a valid JSON.
     */
    public static boolean isValidJson(@Nullable String string) {
        boolean isValidJson = false;

        try {
            if (string != null) {
                new JSONObject(string);
                isValidJson = true;
            }
        } catch (Exception ignored) {
        }

        return isValidJson;
    }

    /**
     * Format the dynamic theme string and remove extra double quotes and white spaces.
     *
     * @param string The dynamic theme string to be formatted.
     *
     * @return The formatted dynamic theme string.
     */
    public static @NonNull String formatDynamicTheme(@NonNull String string) {
        return string.trim().replaceAll(ADS_PATTERN_SPACE, "")
                .replaceAll(ADS_PATTERN_QUOTES, "\"");
    }

    /**
     * Generates an app theme from the dynamic string.
     *
     * @param dynamicTheme The dynamic string to generate the theme.
     *
     * @return The generated dynamic app theme.
     */
    public static @NonNull DynamicAppTheme getAppTheme(@NonNull String dynamicTheme)
            throws Exception {
        DynamicAppTheme dynamicAppTheme = new DynamicAppTheme();
        JSONObject jsonObject = new JSONObject(formatDynamicTheme(dynamicTheme));

        dynamicAppTheme.setBackgroundColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_BACKGROUND_COLOR)), false);
        dynamicAppTheme.setTintBackgroundColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_TINT_BACKGROUND_COLOR)));
        dynamicAppTheme.setPrimaryColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_PRIMARY_COLOR)), false);
        dynamicAppTheme.setTintPrimaryColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_TINT_PRIMARY_COLOR)));
        dynamicAppTheme.setPrimaryColorDark(getValueFromColor(
                jsonObject.getString(ADS_NAME_PRIMARY_COLOR_DARK)), false);
        dynamicAppTheme.setTintPrimaryColorDark(getValueFromColor(
                jsonObject.getString(ADS_NAME_TINT_PRIMARY_COLOR_DARK)));
        dynamicAppTheme.setAccentColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_ACCENT_COLOR)), false);
        dynamicAppTheme.setTintAccentColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_TINT_ACCENT_COLOR)));
        dynamicAppTheme.setAccentColorDark(getValueFromColor(
                jsonObject.getString(ADS_NAME_ACCENT_COLOR_DARK)), false);
        dynamicAppTheme.setTintAccentColorDark(getValueFromColor(
                jsonObject.getString(ADS_NAME_TINT_ACCENT_COLOR_DARK)));
        dynamicAppTheme.setTextPrimaryColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_TEXT_PRIMARY_COLOR)), false);
        dynamicAppTheme.setTextPrimaryColorInverse(getValueFromColor(
                jsonObject.getString(ADS_NAME_TEXT_PRIMARY_COLOR_INVERSE)));
        dynamicAppTheme.setTextSecondaryColor(getValueFromColor(
                jsonObject.getString(ADS_NAME_TEXT_SECONDARY_COLOR)), false);
        dynamicAppTheme.setTextSecondaryColorInverse(getValueFromColor(
                jsonObject.getString(ADS_NAME_TEXT_SECONDARY_COLOR_INVERSE)));
        dynamicAppTheme.setCornerRadius(getValueFromCornerRadius(
                jsonObject.getString(ADS_NAME_CORNER_RADIUS)));
        dynamicAppTheme.setBackgroundAware(getValueFromBackgroundAware(
                jsonObject.getString(ADS_NAME_BACKGROUND_AWARE)));

        return dynamicAppTheme;
    }

    /**
     * Generates an widget theme from the dynamic string.
     *
     * @param dynamicTheme The dynamic string to generate the theme.
     *
     * @return The generated dynamic widget theme.
     */
    public static @NonNull DynamicWidgetTheme getWidgetTheme(@NonNull String dynamicTheme)
            throws Exception {
        DynamicWidgetTheme dynamicWidgetTheme = new DynamicWidgetTheme(getAppTheme(dynamicTheme));
        JSONObject jsonObject = new JSONObject(formatDynamicTheme(dynamicTheme));

        dynamicWidgetTheme.setHeader(getValueFromVisibility(
                jsonObject.getString(ADS_NAME_HEADER)));
        dynamicWidgetTheme.setCornerRadius(jsonObject.getInt(ADS_NAME_CORNER_RADIUS));

        return dynamicWidgetTheme;
    }

    /**
     * Converts color integer into its string equivalent.
     *
     * @param color The color to be converted.
     *
     * @return The string equivalent of the color.
     */
    public static @NonNull String getValueFromColor(int color) {
        if (color == Theme.AUTO) {
            return ADS_VALUE_AUTO;
        } else {
            return DynamicColorUtils.getColorString(color,
                    DynamicColorUtils.isAlpha(color), true);
        }
    }

    /**
     * Converts color string into its integer equivalent.
     *
     * @param color The color to be converted.
     *
     * @return The integer equivalent of the color.
     */
    public static int getValueFromColor(@NonNull String color) {
        if (color.equals(ADS_VALUE_AUTO)) {
            return Theme.AUTO;
        } else {
            return Color.parseColor(color);
        }
    }

    /**
     * Converts corner radius into its string equivalent.
     *
     * @param cornerRadius The corner radius to be converted.
     *
     * @return The string equivalent of the corner radius.
     */
    public static @NonNull String getValueFromCornerRadius(int cornerRadius) {
        if (cornerRadius == Theme.AUTO) {
            return ADS_VALUE_AUTO;
        } else {
            return String.valueOf(DynamicUnitUtils.convertPixelsToDp(cornerRadius));
        }
    }

    /**
     * Converts corner radius string into its integer equivalent.
     *
     * @param cornerRadius The corner radius to be converted.
     *
     * @return The integer equivalent of the corner radius.
     */
    public static int getValueFromCornerRadius(@NonNull String cornerRadius) {
        if (cornerRadius.equals(ADS_VALUE_AUTO)) {
            return Theme.AUTO;
        } else {
            return Integer.valueOf(cornerRadius);
        }
    }

    /**
     * Converts background aware into its string equivalent.
     *
     * @param backgroundAware The background aware to be converted.
     *
     * @return The string equivalent of the background aware.
     */
    public static @NonNull String getValueFromBackgroundAware(
            @Theme.BackgroundAware int backgroundAware) {
        switch (backgroundAware) {
            default:
            case Theme.BackgroundAware.AUTO:
                return ADS_VALUE_AUTO;
            case Theme.BackgroundAware.DISABLE:
                return ADS_VALUE_DISABLE;
            case Theme.BackgroundAware.ENABLE:
                return ADS_VALUE_ENABLE;
        }
    }

    /**
     * Converts background aware string into its integer equivalent.
     *
     * @param backgroundAware The background aware to be converted.
     *
     * @return The integer equivalent of the background aware.
     */
    public static @Theme.BackgroundAware int getValueFromBackgroundAware(
            @NonNull String backgroundAware) {
        switch (backgroundAware) {
            default:
            case ADS_VALUE_AUTO:
                return Theme.BackgroundAware.AUTO;
            case ADS_VALUE_DISABLE:
                return Theme.BackgroundAware.DISABLE;
            case ADS_VALUE_ENABLE:
                return Theme.BackgroundAware.ENABLE;
        }
    }

    /**
     * Converts visibility into its string equivalent.
     *
     * @param visibility The visibility to be converted.
     *
     * @return The string equivalent of the visibility.
     */
    public static @NonNull String getValueFromVisibility(@Theme.Visibility int visibility) {
        switch (visibility) {
            default:
            case Theme.Visibility.AUTO:
                return ADS_VALUE_AUTO;
            case Theme.Visibility.HIDE:
                return ADS_VALUE_HIDE;
            case Theme.Visibility.SHOW:
                return ADS_VALUE_SHOW;
        }
    }

    /**
     * Converts visibility string into its integer equivalent.
     *
     * @param visibility The visibility to be converted.
     *
     * @return The integer equivalent of the visibility.
     */
    public static @Theme.Visibility int getValueFromVisibility(@NonNull String visibility) {
        switch (visibility) {
            default:
            case ADS_VALUE_AUTO:
                return Theme.Visibility.AUTO;
            case ADS_VALUE_HIDE:
                return Theme.Visibility.HIDE;
            case ADS_VALUE_SHOW:
                return Theme.Visibility.SHOW;
        }
    }
}
