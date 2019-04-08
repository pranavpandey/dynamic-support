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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

import org.json.JSONObject;

/**
 * Helper class to perform theme operations.
 */
public class DynamicThemeUtils {

    public static final String PATTERN_QUOTES = "[\"\\\"][\\s+][\\\"\"]";
    public static final String PATTERN_SPACE = "[\\s+]";

    /**
     * Constant name for sharing the theme.
     */
    public static final String NAME_THEME_SHARE = "dynamic-theme";

    /**
     * Serialized name for the theme resource.
     */
    public static final String NAME_THEME_RES = "themeRes";

    /**
     * Serialized name for the background color.
     */
    public static final String NAME_BACKGROUND_COLOR = "backgroundColor";

    /**
     * Serialized name for the tint background color.
     */
    public static final String NAME_TINT_BACKGROUND_COLOR = "tintBackgroundColor";

    /**
     * Serialized name for the primary color.
     */
    public static final String NAME_PRIMARY_COLOR = "primaryColor";

    /**
     * Serialized name for the tint primary color.
     */
    public static final String NAME_TINT_PRIMARY_COLOR = "tintPrimaryColor";

    /**
     * Serialized name for the primary color dark.
     */
    public static final String NAME_PRIMARY_COLOR_DARK = "primaryColorDark";

    /**
     * Serialized name for the tint primary color dark.
     */
    public static final String NAME_TINT_PRIMARY_COLOR_DARK = "tintPrimaryColorDark";

    /**
     * Serialized name for the accent color.
     */
    public static final String NAME_ACCENT_COLOR = "accentColor";

    /**
     * Serialized name for the tint accent color.
     */
    public static final String NAME_TINT_ACCENT_COLOR = "tintAccentColor";

    /**
     * Serialized name for the accent color dark.
     */
    public static final String NAME_ACCENT_COLOR_DARK = "accentColorDark";

    /**
     * Serialized name for the tint accent color dark.
     */
    public static final String NAME_TINT_ACCENT_COLOR_DARK = "tintAccentColorDark";

    /**
     * Serialized name for the text primary color.
     */
    public static final String NAME_TEXT_PRIMARY_COLOR = "textPrimaryColor";

    /**
     * Serialized name for the text primary color inverse.
     */
    public static final String NAME_TEXT_PRIMARY_COLOR_INVERSE = "textPrimaryColorInverse";

    /**
     * Serialized name for the text secondary color.
     */
    public static final String NAME_TEXT_SECONDARY_COLOR = "textSecondaryColor";

    /**
     * Serialized name for the text secondary color inverse.
     */
    public static final String NAME_TEXT_SECONDARY_COLOR_INVERSE = "textSecondaryColorInverse";

    /**
     * Serialized name for the corner radius.
     */
    public static final String NAME_CORNER_RADIUS = "cornerRadius";

    /**
     * Serialized name for the background aware.
     */
    public static final String NAME_BACKGROUND_AWARE = "backgroundAware";

    /**
     * Serialized name for the widget id.
     */
    public static final String NAME_WIDGET_ID = "widgetId";

    /**
     * Serialized name for the header.
     */
    public static final String NAME_HEADER = "header";

    /**
     * Serialized name for the opacity.
     */
    public static final String NAME_OPACITY = "opacity";

    /**
     * Constant for the auto value.
     */
    public static final String VALUE_AUTO = "auto";

    /**
     * Constant for the disable value.
     */
    public static final String VALUE_DISABLE = "disable";

    /**
     * Constant for the enable value.
     */
    public static final String VALUE_ENABLE = "enable";

    /**
     * Constant for the hide value.
     */
    public static final String VALUE_HIDE = "hide";

    /**
     * Constant for the show value.
     */
    public static final String VALUE_SHOW = "show";

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
        return string.trim().replaceAll(PATTERN_SPACE, "")
                .replaceAll(PATTERN_QUOTES, "\"");
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
                jsonObject.getString(NAME_BACKGROUND_COLOR)), false);
        dynamicAppTheme.setTintBackgroundColor(getValueFromColor(
                jsonObject.getString(NAME_TINT_BACKGROUND_COLOR)));
        dynamicAppTheme.setPrimaryColor(getValueFromColor(
                jsonObject.getString(NAME_PRIMARY_COLOR)), false);
        dynamicAppTheme.setTintPrimaryColor(getValueFromColor(
                jsonObject.getString(NAME_TINT_PRIMARY_COLOR)));
        dynamicAppTheme.setPrimaryColorDark(getValueFromColor(
                jsonObject.getString(NAME_PRIMARY_COLOR_DARK)), false);
        dynamicAppTheme.setTintPrimaryColorDark(getValueFromColor(
                jsonObject.getString(NAME_TINT_PRIMARY_COLOR_DARK)));
        dynamicAppTheme.setAccentColor(getValueFromColor(
                jsonObject.getString(NAME_ACCENT_COLOR)), false);
        dynamicAppTheme.setTintAccentColor(getValueFromColor(
                jsonObject.getString(NAME_TINT_ACCENT_COLOR)));
        dynamicAppTheme.setAccentColorDark(getValueFromColor(
                jsonObject.getString(NAME_ACCENT_COLOR_DARK)), false);
        dynamicAppTheme.setTintAccentColorDark(getValueFromColor(
                jsonObject.getString(NAME_TINT_ACCENT_COLOR_DARK)));
        dynamicAppTheme.setTextPrimaryColor(getValueFromColor(
                jsonObject.getString(NAME_TEXT_PRIMARY_COLOR)), false);
        dynamicAppTheme.setTextPrimaryColorInverse(getValueFromColor(
                jsonObject.getString(NAME_TEXT_PRIMARY_COLOR_INVERSE)));
        dynamicAppTheme.setTextSecondaryColor(getValueFromColor(
                jsonObject.getString(NAME_TEXT_SECONDARY_COLOR)), false);
        dynamicAppTheme.setTextSecondaryColorInverse(getValueFromColor(
                jsonObject.getString(NAME_TEXT_SECONDARY_COLOR_INVERSE)));
        dynamicAppTheme.setCornerRadius(getValueFromCornerRadius(
                jsonObject.getString(NAME_CORNER_RADIUS)));
        dynamicAppTheme.setBackgroundAware(getValueFromBackgroundAware(
                jsonObject.getString(NAME_BACKGROUND_AWARE)));

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
                jsonObject.getString(NAME_HEADER)));
        dynamicWidgetTheme.setCornerRadius(jsonObject.getInt(NAME_CORNER_RADIUS));

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
            return VALUE_AUTO;
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
        if (color.equals(VALUE_AUTO)) {
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
            return VALUE_AUTO;
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
        if (cornerRadius.equals(VALUE_AUTO)) {
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
                return VALUE_AUTO;
            case Theme.BackgroundAware.DISABLE:
                return VALUE_DISABLE;
            case Theme.BackgroundAware.ENABLE:
                return VALUE_ENABLE;
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
            case VALUE_AUTO:
                return Theme.BackgroundAware.AUTO;
            case VALUE_DISABLE:
                return Theme.BackgroundAware.DISABLE;
            case VALUE_ENABLE:
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
                return VALUE_AUTO;
            case Theme.Visibility.HIDE:
                return VALUE_HIDE;
            case Theme.Visibility.SHOW:
                return VALUE_SHOW;
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
            case VALUE_AUTO:
                return Theme.Visibility.AUTO;
            case VALUE_HIDE:
                return Theme.Visibility.HIDE;
            case VALUE_SHOW:
                return Theme.Visibility.SHOW;
        }
    }

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        float cornerRadiusPixel = DynamicUnitUtils.convertDpToPixels(
                topOnly ? Math.max(0, cornerRadius - 1f) : cornerRadius);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        MaterialShapeDrawable materialShapeDrawable;

        if (!topOnly) {
            shapeAppearanceModel.setAllCorners(new RoundedCornerTreatment(cornerRadiusPixel));
        } else {
            shapeAppearanceModel.setTopLeftCorner(new RoundedCornerTreatment(cornerRadiusPixel));
            shapeAppearanceModel.setTopRightCorner(new RoundedCornerTreatment(cornerRadiusPixel));
        }

        materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setTint(color);

        return materialShapeDrawable;
    }

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawableLegacy(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly) {
        float adjustedCornerRadius = cornerRadius;

        if (!topOnly) {
            return DynamicDrawableUtils.getCornerDrawable(
                    width, height, adjustedCornerRadius, color);
        } else {
            adjustedCornerRadius = Math.max(0, cornerRadius - 1f);
            adjustedCornerRadius = DynamicUnitUtils.convertDpToPixels(adjustedCornerRadius);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setSize(width, height);
            drawable.setCornerRadii(new float[] {
                    adjustedCornerRadius, adjustedCornerRadius,
                    adjustedCornerRadius, adjustedCornerRadius,
                    0, 0, 0, 0 });

            return DynamicDrawableUtils.getCornerDrawable(width, height, drawable, color);
        }
    }

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawableLegacy(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawableLegacy(0, 0, cornerRadius, color, topOnly);
    }
}
