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

package com.pranavpandey.android.dynamic.support;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicSnackbar;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.picker.color.view.DynamicColorView;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.inflater.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.tutorial.Tutorial;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView;
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingToolbar;
import com.pranavpandey.android.dynamic.support.widget.DynamicMaterialCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationRailView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicLinkWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTextWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.support.widget.tooltip.DynamicTooltip;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.base.BackgroundAware;
import com.pranavpandey.android.dynamic.theme.base.DynamicColor;
import com.pranavpandey.android.dynamic.theme.base.StrokeTheme;
import com.pranavpandey.android.dynamic.theme.base.TranslucentTheme;
import com.pranavpandey.android.dynamic.theme.base.TypeTheme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper class to manipulate {@link DynamicActivity} and inflated views at runtime according
 * to their implemented types.
 *
 * <p>These methods are provided to avoid any exceptions when views are replaced by the
 * {@link DynamicLayoutInflater} or not using the {@link DynamicActivity}.
 * So, this is the recommended way to modify the activity or any of the following view types
 * at runtime.
 *
 * @see DynamicWidget
 * @see DynamicBackgroundWidget
 * @see com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget
 * @see DynamicLinkWidget
 * @see DynamicScrollableWidget
 * @see DynamicStateWidget
 * @see DynamicStateSelectedWidget
 * @see DynamicTextWidget
 * @see DynamicColorView
 * @see DynamicItem
 */
public class Dynamic {

    /**
     * Interface to hold the vibration values.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Vibration {

        /**
         * {@code true} to enable the vibration.
         */
        boolean DEFAULT = true;

        /**
         * Interface to hold the vibration intensity values.
         */
        @Retention(RetentionPolicy.SOURCE)
        @interface Intensity {

            /**
             * The minimum vibration intensity.
             */
            int MIN = 10;

            /**
             * The maximum vibration intensity.
             */
            int MAX = 500;

            /**
             * The vibration intensity interval.
             */
            int INTERVAL = 5;

            /**
             * The default vibration intensity.
             */
            int DEFAULT = 25;
        }
    }

    /**
     * Load the theme styles version from the shared preferences.
     *
     * @param resolve {@code true} to resolve the auto theme version.
     *
     * @return The theme styles version from the shared preferences.
     */
    public static @DynamicTheme.Version.ToString String loadThemeVersion(boolean resolve) {
        @DynamicTheme.Version.ToString String version = DynamicPreferences.getInstance().load(
                DynamicTheme.Version.KEY, DynamicTheme.Version.ToString.DEFAULT);

        return resolve && DynamicTheme.Version.ToString.AUTO.equals(version)
                ? DynamicTheme.Version.ToString.DEFAULT_AUTO : version;

    }

    /**
     * Load the theme styles version from the shared preferences.
     *
     * @return The theme styles version from the shared preferences.
     *
     * @see #loadThemeVersion(boolean)
     */
    public static @DynamicTheme.Version.ToString String loadThemeVersion() {
        return loadThemeVersion(true);
    }

    /**
     * Save the theme styles version into the shared preferences.
     *
     * @param version The version to be saved.
     */
    public static void saveThemeVersion(@DynamicTheme.Version.ToString String version) {
        DynamicPreferences.getInstance().save(DynamicTheme.Version.KEY, version);
    }

    /**
     * Checks whether the supplied theme styles version is legacy.
     *
     * @param version The version to be checked.
     *
     * @return {@code true} if the supplied theme styles version is legacy.
     */
    public static boolean isLegacyVersion(@DynamicTheme.Version int version) {
        return version < DynamicTheme.Version.INT_2;
    }

    /**
     * Checks whether the supplied theme styles version is legacy.
     *
     * @param version The version to be checked.
     *
     * @return {@code true} if the supplied theme styles version is legacy.
     */
    public static boolean isLegacyVersion(@DynamicTheme.Version.ToString String version) {
        return isLegacyVersion(Integer.parseInt(version));
    }

    /**
     * Checks whether the supplied theme styles version is expressive.
     *
     * @param version The version to be checked.
     *
     * @return {@code true} if the supplied theme styles version is expressive.
     */
    public static boolean isExpressiveVersion(@DynamicTheme.Version int version) {
        return version >= DynamicTheme.Version.INT_3;
    }

    /**
     * Checks whether the supplied theme styles version is expressive.
     *
     * @param version The version to be checked.
     *
     * @return {@code true} if the supplied theme styles version is expressive.
     */
    public static boolean isExpressiveVersion(@DynamicTheme.Version.ToString String version) {
        return isExpressiveVersion(Integer.parseInt(version));
    }

    /**
     * Checks whether the application theme styles version is expressive.
     *
     * @return {@code true} if the application theme styles version is expressive.
     *
     * @see #isExpressiveVersion(int)
     * @see DynamicTheme#getVersion()
     */
    public static boolean isExpressiveVersionRemote() {
        return isExpressiveVersion(DynamicTheme.getInstance().getVersion());
    }

    /**
     * Checks whether the theme styles version is legacy.
     *
     * @return {@code true} if the theme styles version is legacy.
     *
     * @see #isLegacyVersion(int)
     * @see DynamicTheme#resolveVersion()
     */
    public static boolean isLegacyVersion() {
        return isLegacyVersion(DynamicTheme.getInstance().resolveVersion());
    }

    /**
     * Checks whether the application theme styles version is legacy.
     *
     * @return {@code true} if the application theme styles version is legacy.
     *
     * @see #isLegacyVersion(int)
     * @see DynamicTheme#getVersion()
     */
    public static boolean isLegacyVersionRemote() {
        return isLegacyVersion(DynamicTheme.getInstance().getVersion());
    }

    /**
     * Set the text view all caps according to the theme styles version.
     *
     * @param textView The text view to be used.
     * @param allCaps {@code true} to set the all caps.
     *
     * @see #isLegacyVersion()
     */
    public static void setAllCapsIfRequired(@Nullable TextView textView, boolean allCaps) {
        DynamicViewUtils.setTextViewAllCaps(textView, allCaps && isLegacyVersion());
    }

    /**
     * Resolves a color based on the supplied parameters.
     *
     * @param background The background color to be considered.
     * @param color The color to be resolved.
     * @param tint The tint color to be resolved.
     * @param backgroundAware {@code true} if the background aware is enabled.
     *
     * @return The resolved color based on the supplied parameters.
     */
    public static @ColorInt int resolveColor(@ColorInt int background,
            @ColorInt int color, @ColorInt int tint, boolean backgroundAware) {
        final boolean dark = DynamicColorUtils.isColorDark(background);
        if (backgroundAware && dark != DynamicColorUtils.isColorDark(color)) {
            return dark == DynamicColorUtils.isColorDark(tint) ? tint : getTintColor(color);
        }

        return color;
    }

    /**
     * Resolves a color based on the supplied parameters.
     *
     * @param background The background color to be considered.
     * @param color The color to be resolved.
     * @param tint The tint color to be resolved.
     * @param backgroundAware {@code true} if the background aware is enabled.
     *
     * @return The resolved color based on the supplied parameters.
     */
    public static @ColorInt int resolveTintColor(@ColorInt int background,
            @ColorInt int color, @ColorInt int tint, boolean backgroundAware) {
        final boolean dark = DynamicColorUtils.isColorDark(background);
        if (backgroundAware && dark == DynamicColorUtils.isColorDark(tint)) {
            return dark != DynamicColorUtils.isColorDark(color) ? color : getTintColor(tint);
        }

        return tint;
    }

    /**
     * Sets the color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setColorType(int)
     * @see DynamicItem#setColorType(int)
     */
    public static <T> void setColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setColorType(colorType);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setColorType(colorType);
        }
    }

    /**
     * Sets the background color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicBackgroundWidget#setBackgroundColorType(int)
     */
    public static <T> void setBackgroundColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) dynamic).setBackgroundColorType(colorType);
        }
    }

    /**
     * Sets the contrast with color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setContrastWithColorType(int)
     * @see DynamicItem#setContrastWithColorType(int)
     */
    public static <T> void setContrastWithColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setContrastWithColorType(colorType);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setContrastWithColorType(colorType);
        }
    }

    /**
     * Sets the text color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTextWidget#setTextColorType(int)
     */
    public static <T> void setTextColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicTextWidget) {
            ((DynamicTextWidget) dynamic).setTextColorType(colorType);
        }
    }

    /**
     * Sets the link color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicLinkWidget#setLinkColorType(int)
     */
    public static <T> void setLinkColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) dynamic).setLinkColorType(colorType);
        }
    }

    /**
     * Sets the normal state color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateWidget#setStateNormalColorType(int)
     */
    public static <T> void setStateNormalColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicStateWidget) {
            ((DynamicStateWidget) dynamic).setStateNormalColorType(colorType);
        }
    }

    /**
     * Sets the selected state color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static <T> void setStateSelectedColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) dynamic).setStateSelectedColorType(colorType);
        }
    }

    /**
     * Sets the scroll bar color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollableWidget#setScrollBarColorType(int)
     */
    public static <T> void setScrollBarColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) dynamic).setScrollBarColorType(colorType);
        }
    }

    /**
     * Returns the color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param defaultColor The default color to be used.
     * @param <T> The type of the dynamic object.
     *
     * @return The color for the supplied dynamic object.
     *
     * @see DynamicWidget#getColor()
     * @see DynamicColorView#setColor(int)
     */
    public static @ColorInt <T> int getColor(@Nullable T dynamic, @ColorInt int defaultColor) {
        if (dynamic instanceof DynamicColorView) {
            return ((DynamicColorView) dynamic).getColor();
        } else if (dynamic instanceof DynamicWidget) {
            return ((DynamicWidget) dynamic).getColor();
        }

        return defaultColor;
    }

    /**
     * Sets the color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setColor(int)
     * @see DynamicColorView#setColor(int)
     * @see DynamicItem#setColor(int)
     */
    public static <T> void setColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicColorView) {
            ((DynamicColorView) dynamic).setColor(color);
        } else if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setColor(color);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setColor(color);
        }
    }

    /**
     * Refresh the color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setColor()
     * @see DynamicColorView#setColor()
     */
    public static <T> void setColor(@Nullable T dynamic) {
        if (dynamic instanceof DynamicColorView) {
            ((DynamicColorView) dynamic).setColor();
        } else if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setColor();
        }
    }

    /**
     * Sets the background color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicBackgroundWidget#setBackgroundColor(int)
     */
    public static <T> void setBackgroundColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) dynamic).setBackgroundColor(color);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setBackgroundColor(color);
        }
    }

    /**
     * Sets the stroke color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The stroke color to be set.
     * @param <T> The type of the dynamic object.
     */
    public static <T> void setStrokeColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof MaterialCardView) {
            ((MaterialCardView) dynamic).setStrokeColor(color);
        }
    }

    /**
     * Returns the contrast with color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param defaultColor The default color to be used.
     * @param <T> The type of the dynamic object.
     *
     * @return The contrast color for the supplied dynamic object.
     *
     * @see DynamicWidget#getContrastWithColor()
     */
    public static @ColorInt <T> int getContrastWithColor(
            @Nullable T dynamic, @ColorInt int defaultColor) {
        if (dynamic instanceof DynamicWidget) {
            return ((DynamicWidget) dynamic).getContrastWithColor();
        }

        return defaultColor;
    }

    /**
     * Sets the contrast with color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setContrastWithColor(int)
     * @see DynamicItem#setContrastWithColor(int)
     */
    public static <T> void setContrastWithColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setContrastWithColor(color);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setContrastWithColor(color);
        }
    }

    /**
     * Sets the text color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTextWidget#setTextColor(int)
     */
    public static <T> void setTextColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicTextWidget) {
            ((DynamicTextWidget) dynamic).setTextColor(color);
        }
    }

    /**
     * Refresh the text color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTextWidget#setTextColor()
     */
    public static <T> void setTextColor(@Nullable T dynamic) {
        if (dynamic instanceof DynamicTextWidget) {
            ((DynamicTextWidget) dynamic).setTextColor();
        }
    }

    /**
     * Sets the link color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicLinkWidget#setLinkColor(int)
     */
    public static <T> void setLinkColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) dynamic).setLinkColor(color);
        }
    }

    /**
     * Refresh the link color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicLinkWidget#setLinkColor()
     */
    public static <T> void setLinkColor(@Nullable T dynamic) {
        if (dynamic instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) dynamic).setLinkColor();
        }
    }

    /**
     * Sets the normal state color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateWidget#setStateNormalColor(int)
     */
    public static <T> void setStateNormalColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicStateWidget) {
            ((DynamicStateWidget) dynamic).setStateNormalColor(color);
        }
    }

    /**
     * Sets the selected state color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static <T> void setStateSelectedColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) dynamic).setStateSelectedColor(color);
        }
    }

    /**
     * Sets the scroll bar color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollableWidget#setScrollBarColor(int)
     */
    public static <T> void setScrollBarColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) dynamic).setScrollBarColor(color);
        }
    }

    /**
     * Refresh the scroll bar color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollableWidget#setScrollBarColor()
     */
    public static <T> void setScrollBarColor(@Nullable T dynamic) {
        if (dynamic instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) dynamic).setScrollBarColor();
        }
    }

    /**
     * Checks whether the stroke is required for the supplied theme.
     *
     * @param theme The theme object to be used.
     * @param <T> The type of the dynamic theme.
     *
     * @return {@code true} if the stroke is required for the supplied theme.
     */
    public static <T extends StrokeTheme<?>> boolean isStroke(@Nullable T theme) {
        if (theme == null) {
            return false;
        }

        return DynamicSdkUtils.is16() && theme.isStroke();
    }

    /**
     * Checks whether the stroke is required for the current theme.
     *
     * @return {@code true} if the stroke is required for the current theme.
     */
    public static boolean isStroke() {
        return isStroke(DynamicTheme.getInstance().get());
    }

    /**
     * Checks whether the background aware functionality is enabled for the supplied value.
     *
     * @param backgroundAware The value to be checked.
     *
     * @return {@code true} if the supplied value changes color according to
     *         the background.
     *
     * @see DynamicTheme#resolveBackgroundAware(int)
     */
    public static boolean isBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        return DynamicTheme.getInstance().resolveBackgroundAware(backgroundAware)
                != Theme.BackgroundAware.DISABLE;
    }

    /**
     * Checks whether the background aware functionality is enabled for the supplied
     * dynamic object.
     *
     * @param dynamic The dynamic object to be checked.
     * @param <T> The type of the dynamic object.
     *
     * @return {@code true} if the supplied dynamic object changes color according to
     *         the background.
     *
     * @see #isBackgroundAware(int)
     */
    public static <T> boolean isBackgroundAware(@Nullable T dynamic) {
        if (dynamic instanceof DynamicWidget) {
            return isBackgroundAware(((DynamicWidget) dynamic).getBackgroundAware());
        } else if (dynamic instanceof DynamicItem) {
            return isBackgroundAware(((DynamicItem) dynamic).getBackgroundAware());
        }

        return false;
    }

    /**
     * Sets the background aware and contrast for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set.
     * @param contrast The contrast to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setBackgroundAware(int)
     * @see DynamicWidget#setContrast(int)
     * @see DynamicItem#setBackgroundAware(int)
     * @see DynamicItem#setContrast(int)
     */
    public static <T> void setBackgroundAware(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware, int contrast) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setBackgroundAware(backgroundAware);
            ((DynamicWidget) dynamic).setContrast(contrast);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setBackgroundAware(backgroundAware);
            ((DynamicItem) dynamic).setContrast(contrast);
        }
    }

    /**
     * Sets the background aware and contrast for the dynamic object according
     * to the supplied theme.
     *
     * @param dynamic The dynamic object to be used.
     * @param theme The theme object to be used.
     * @param <V> The type of the dynamic object.
     * @param <T> The type of the dynamic theme.
     *
     * @see DynamicWidget#setBackgroundAware(int)
     * @see DynamicWidget#setContrast(int)
     * @see DynamicItem#setBackgroundAware(int)
     * @see DynamicItem#setContrast(int)
     */
    public static <V, T extends BackgroundAware<?>> void setBackgroundAware(
            @Nullable V dynamic, @Nullable T theme) {
        if (theme == null) {
            return;
        }

        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setBackgroundAware(theme.getBackgroundAware());
            ((DynamicWidget) dynamic).setContrast(theme.getContrast());
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setBackgroundAware(theme.getBackgroundAware());
            ((DynamicItem) dynamic).setContrast(theme.getContrast());
        }
    }

    /**
     * Sets the background aware for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setBackgroundAware(int)
     * @see DynamicItem#setBackgroundAware(int)
     */
    public static <T> void setBackgroundAware(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setBackgroundAware(backgroundAware);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setBackgroundAware(backgroundAware);
        }
    }

    /**
     * Returns the resolved contrast for the supplied value.
     *
     * @param contrast The contrast to be resolved.
     *
     * @return The resolved contrast for the supplied value.
     *
     * @see Theme.Contrast#AUTO
     * @see Theme.Contrast#UNKNOWN
     */
    public static int getContrast(int contrast) {
        if (contrast == Theme.Contrast.AUTO || contrast == Theme.Contrast.UNKNOWN) {
            return DynamicTheme.getInstance().get().getContrast();
        }

        return contrast;
    }

    /**
     * Returns the contrast for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param <T> The type of the dynamic object.
     *
     * @return The contrast for the supplied dynamic object.
     *
     * @see #getContrast(int)
     */
    public static <T> int getContrast(@Nullable T dynamic) {
        if (dynamic instanceof DynamicWidget) {
            return getContrast(((DynamicWidget) dynamic).getContrast(false));
        } else if (dynamic instanceof DynamicItem) {
            return getContrast(((DynamicItem) dynamic).getContrast(false));
        }

        return DynamicTheme.getInstance().get().getContrast();
    }

    /**
     * Returns the contrast color according to the supplied visible contrast.
     *
     * @param color The color to be used.
     * @param contrastWith The contrast with color to be used.
     * @param visibleContrast The minimum ratio for the visible contrast.
     *
     * @return The contrast color according to the supplied visible contrast.
     *
     * @see DynamicColorUtils#getContrastColor(int, int, float)
     */
    public static @ColorInt int withContrastRatio(@ColorInt int color,
            @ColorInt int contrastWith, float visibleContrast) {
        return DynamicColorUtils.getContrastColor(color, contrastWith, visibleContrast);
    }

    /**
     * Returns the contrast color according to the supplied theme.
     *
     * @param color The color to be used.
     * @param contrastWith The contrast with color to be used.
     * @param theme The theme object to be used.
     * @param <T> The type of the theme.
     *
     * @return The contrast color according to the supplied theme.
     *
     * @see BackgroundAware#getContrastRatio()
     * @see #withContrastRatio(int, int, float)
     * @see DynamicColorUtils#getContrastColor(int, int)
     */
    public static @ColorInt <T extends BackgroundAware<?>> int withContrastRatio(
            @ColorInt int color, @ColorInt int contrastWith, @Nullable T theme) {
        if (theme != null) {
            return withContrastRatio(color, contrastWith, theme.getContrastRatio());
        }

        return DynamicColorUtils.getContrastColor(color, contrastWith);
    }

    /**
     * Returns the contrast color according to the applied theme.
     *
     * @param color The color to be used.
     * @param contrastWith The contrast with color to be used.
     *
     * @return The contrast color according to the applied theme.
     *
     * @see DynamicTheme#get()
     * @see #withContrastRatio(int, int, BackgroundAware)
     */
    public static @ColorInt int withContrastRatio(@ColorInt int color,
            @ColorInt int contrastWith) {
        return withContrastRatio(color, contrastWith, DynamicTheme.getInstance().get());
    }

    /**
     * Returns the contrast color according to the supplied dynamic widget.
     *
     * @param color The color to be used.
     * @param contrastWith The contrast with color to be used.
     * @param view The view object to be used.
     * @param <T> The type of the dynamic widget.
     *
     * @return The contrast color according to the supplied dynamic widget.
     *
     * @see DynamicWidget#getContrastRatio()
     * @see DynamicItem#getContrastRatio()
     * @see #withContrastRatio(int, int, float)
     * @see #withContrastRatio(int, int)
     */
    public static @ColorInt <T> int withContrastRatio(
            @ColorInt int color, @ColorInt int contrastWith, @Nullable T view) {
        if (view instanceof DynamicWidget) {
            return withContrastRatio(color, contrastWith,
                    ((DynamicWidget) view).getContrastRatio());
        } else if (view instanceof DynamicItem) {
            return withContrastRatio(color, contrastWith,
                    ((DynamicItem) view).getContrastRatio());
        }

        return withContrastRatio(color, contrastWith);
    }

    /**
     * Returns the tint color according to the supplied visible contrast.
     *
     * @param color The color to be used.
     * @param visibleContrast The minimum ratio for the visible contrast.
     *
     * @return The tint color according to the supplied visible contrast.
     *
     * @see DynamicColorUtils#getTintColor(int, float)
     */
    public static @ColorInt int getTintColor(@ColorInt int color, float visibleContrast) {
        return DynamicColorUtils.getTintColor(color, visibleContrast);
    }

    /**
     * Returns the tint color according to the supplied theme.
     *
     * @param color The color to be used.
     * @param theme The theme object to be used.
     * @param <T> The type of the theme.
     *
     * @return The tint color according to the supplied theme.
     *
     * @see BackgroundAware#getContrastRatio()
     * @see DynamicColorUtils#getTintColor(int)
     * @see #getTintColor(int, float)
     * @see DynamicColorUtils#getTintColor(int)
     */
    public static @ColorInt <T extends BackgroundAware<?>> int getTintColor(
            @ColorInt int color, @Nullable T theme) {
        if (theme != null) {
            return getTintColor(color, theme.getContrastRatio());
        }

        return DynamicColorUtils.getTintColor(color);
    }

    /**
     * Returns the tint color according to the applied theme.
     *
     * @param color The color to be used.
     *
     * @return The tint color according to the applied theme.
     *
     * @see DynamicTheme#get()
     * @see #getTintColor(int, BackgroundAware)
     */
    public static @ColorInt int getTintColor(@ColorInt int color) {
        return getTintColor(color, DynamicTheme.getInstance().get());
    }

    /**
     * Returns the tint color according to the supplied dynamic widget.
     *
     * @param color The color to be used.
     * @param view The view object to be used.
     * @param <T> The type of the dynamic widget.
     *
     * @return The tint color according to the supplied dynamic widget.
     *
     * @see DynamicWidget#getContrastRatio()
     * @see DynamicItem#getContrastRatio()
     * @see #getTintColor(int, float)
     * @see #getTintColor(int)
     */
    public static @ColorInt <T> int getTintColor(@ColorInt int color, @Nullable T view) {
        if (view instanceof DynamicWidget) {
            return getTintColor(color, ((DynamicWidget) view).getContrastRatio());
        } else if (view instanceof DynamicItem) {
            return getTintColor(color, ((DynamicItem) view).getContrastRatio());
        }

        return getTintColor(color);
    }

    /**
     * Sets the translucent theme opacity for the supplied color.
     *
     * @param color The color to be used.
     * @param theme The theme object to be used.
     * @param min The minimum opacity to be used.
     * @param <T> The type of the dynamic theme.
     *
     * @return The color after applying the theme opacity.
     *
     * @see TranslucentTheme#isTranslucent()
     * @see TranslucentTheme#getOpacity()
     * @see DynamicColorUtils#setAlpha(int, int)
     */
    public static @ColorInt <T extends TranslucentTheme<?>> int withThemeOpacity(
            @ColorInt int color, @Nullable T theme,
            @IntRange(from = Theme.Opacity.MIN, to = Theme.Opacity.MAX) int min) {
        if (color == Theme.Color.UNKNOWN) {
            return Color.TRANSPARENT;
        } else if (color != Color.TRANSPARENT && theme != null && theme.isTranslucent()) {
            return DynamicColorUtils.setAlpha(color, Math.max(theme.getOpacity(), min));
        }

        return color;
    }

    /**
     * Sets the translucent theme opacity for the supplied color.
     *
     * @param color The color to be used.
     * @param theme The theme object to be used.
     * @param <T> The type of the dynamic theme.
     *
     * @return The color after applying the theme opacity.
     *
     * @see TranslucentTheme#getOpacity()
     * @see #withThemeOpacity(int, TranslucentTheme, int)
     */
    public static @ColorInt <T extends TranslucentTheme<?>> int withThemeOpacity(
            @ColorInt int color, @Nullable T theme) {
        if (theme != null) {
            return withThemeOpacity(color, theme, theme.getOpacity());
        }

        return color;
    }

    /**
     * Sets the translucent theme opacity for the supplied color.
     *
     * @param color The color to be used.
     * @param min The minimum opacity to be used.
     *
     * @return The color after applying the theme opacity.
     *
     * @see #withThemeOpacity(int, TranslucentTheme, int)
     */
    public static @ColorInt int withThemeOpacity(@ColorInt int color,
            @IntRange(from = Theme.Opacity.MIN, to = Theme.Opacity.MAX) int min) {
        return withThemeOpacity(color, DynamicTheme.getInstance().get(), min);
    }

    /**
     * Sets the translucent theme opacity for the supplied color.
     *
     * @param color The color to be used.
     *
     * @return The color after applying the theme opacity.
     *
     * @see #withThemeOpacity(int, TranslucentTheme)
     */
    public static @ColorInt int withThemeOpacity(@ColorInt int color) {
        return withThemeOpacity(color, DynamicTheme.getInstance().get());
    }

    /**
     * Sets the translucent theme opacity for the supplied drawable.
     *
     * @param drawable The drawable to be used.
     * @param theme The theme object to be used.
     * @param min The minimum opacity to be used.
     * @param <T> The type of the dynamic theme.
     *
     * @return The drawable after applying the theme opacity.
     *
     * @see TranslucentTheme#isTranslucent()
     * @see TranslucentTheme#getOpacity()
     * @see Drawable#setAlpha(int)
     */
    public static @Nullable <T extends TranslucentTheme<?>> Drawable withThemeOpacity(
            @Nullable Drawable drawable, @Nullable T theme,
            @IntRange(from = Theme.Opacity.MIN, to = Theme.Opacity.MAX) int min) {
        if (drawable != null && theme != null && theme.isTranslucent()) {
            drawable.setAlpha(Math.max(theme.getOpacity(), min));
        }

        return drawable;
    }

    /**
     * Sets the translucent theme opacity for the supplied drawable.
     *
     * @param drawable The drawable to be used.
     * @param theme The theme object to be used.
     * @param <T> The type of the dynamic theme.
     *
     * @return The drawable after applying the theme opacity.
     *
     * @see TranslucentTheme#getOpacity()
     * @see #withThemeOpacity(Drawable, TranslucentTheme, int)
     */
    public static @Nullable <T extends TranslucentTheme<?>> Drawable withThemeOpacity(
            @Nullable Drawable drawable, @Nullable T theme) {
        if (theme != null) {
            return withThemeOpacity(drawable, theme, theme.getOpacity());
        }

        return drawable;
    }

    /**
     * Sets the translucent theme opacity for the supplied drawable.
     *
     * @param drawable The drawable to be used.
     * @param min The minimum opacity to be used.
     *
     * @return The drawable after applying the theme opacity.
     *
     * @see #withThemeOpacity(Drawable, TranslucentTheme, int)
     */
    public static @Nullable Drawable withThemeOpacity(@Nullable Drawable drawable,
            @IntRange(from = Theme.Opacity.MIN, to = Theme.Opacity.MAX) int min) {
        return withThemeOpacity(drawable, DynamicTheme.getInstance().get(), min);
    }

    /**
     * Sets the translucent theme opacity for the supplied drawable.
     *
     * @param drawable The drawable to be used.
     *
     * @return The drawable after applying the theme opacity.
     *
     * @see #withThemeOpacity(Drawable, TranslucentTheme)
     */
    public static @Nullable Drawable withThemeOpacity(@Nullable Drawable drawable) {
        return withThemeOpacity(drawable, DynamicTheme.getInstance().get());
    }

    /**
     * Sets the type for the supplied dynamic theme object.
     *
     * @param theme The theme object to be used.
     * @param type The theme type to be set.
     * @param <T> The type of the theme object.
     *
     * @return The theme object after setting the type.
     *
     * @see TypeTheme#setType(int)
     */
    public static <T extends TypeTheme<?>> T setThemeType(@Nullable T theme, @Theme int type) {
        if (theme != null) {
            theme.setType(type);
        }

        return theme;
    }

    /**
     * Sets the type for the supplied dynamic theme object.
     *
     * @param theme The theme object to be used.
     * @param parent The parent theme to be used.
     * @param <T> The type of the theme object.
     *
     * @return The theme object after setting the type.
     *
     * @see #setThemeType(TypeTheme, int)
     */
    public static <T extends TypeTheme<?>> T setThemeType(@Nullable T theme, @Nullable T parent) {
        return setThemeType(theme, parent != null ? parent.getType()
                : DynamicTheme.getInstance().get().getType());
    }

    /**
     * Sets the color type or color for the supplied dynamic object after doing
     * the appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setColorType(Object, int)
     * @see #setColor(Object, int)
     */
    public static <T> void setColorTypeOrColor(@Nullable T dynamic,
            @Theme.ColorType int colorType, @ColorInt int color) {
        if (colorType != Theme.ColorType.NONE && colorType != Theme.ColorType.CUSTOM) {
            setColorType(dynamic, colorType);
        } else if (colorType == Theme.ColorType.CUSTOM && color != Theme.Color.UNKNOWN) {
            setColor(dynamic, color);
        }
    }

    /**
     * Sets the contrast with color type or color for the supplied dynamic object after doing
     * the appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setContrastWithColorType(Object, int)
     * @see #setContrastWithColor(Object, int)
     */
    public static <T> void setContrastWithColorTypeOrColor(@Nullable T dynamic,
            @Theme.ColorType int colorType, @ColorInt int color) {
        if (colorType != Theme.ColorType.NONE && colorType != Theme.ColorType.CUSTOM) {
            setContrastWithColorType(dynamic, colorType);
        } else if (colorType == Theme.ColorType.CUSTOM && color != Theme.Color.UNKNOWN) {
            setContrastWithColor(dynamic, color);
        }
    }

    /**
     * Sets the background aware and contrast for the supplied dynamic object after
     * doing appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set.
     * @param contrast The contrast to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setBackgroundAware(Object, int, int)
     * @see #setBackgroundAware(Object, int)
     */
    public static <T> void setBackgroundAwareSafe(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware, int contrast) {
        if (backgroundAware != Theme.BackgroundAware.UNKNOWN) {
            if (contrast != Theme.Contrast.UNKNOWN) {
                setBackgroundAware(dynamic, backgroundAware, contrast);
            } else {
                setBackgroundAware(dynamic, backgroundAware);
            }
        }
    }

    /**
     * Sets the background aware for the supplied dynamic object after doing appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setBackgroundAware(Object, int)
     */
    public static <T> void setBackgroundAwareSafe(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware) {
        if (backgroundAware != Theme.BackgroundAware.UNKNOWN) {
            setBackgroundAware(dynamic, backgroundAware);
        }
    }

    /**
     * Checks whether the dynamic theme object has dynamic colors enabled.
     *
     * @param theme The theme object to be used.
     * @param <T> The type of the theme object.
     *
     * @return {@code true} if the dynamic theme object has dynamic colors enabled.
     *
     * @see DynamicColor#isDynamicColor()
     */
    public static <T extends DynamicColor<T>> boolean isDynamicColor(@Nullable T theme) {
        if (theme != null) {
            return theme.isDynamicColor();
        }

        return false;
    }

    /**
     * Tint dynamic object according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param color The color to be set.
     * @param contrastWithColor The contrast with color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setColor(Object, int)
     * @see #setContrastWithColor(Object, int)
     * @see #setScrollBarColor(Object, int)
     */
    public static <T> void tint(@Nullable T dynamic,
            @ColorInt int color, @ColorInt int contrastWithColor) {
        if (dynamic != null) {
            setColor(dynamic, color);
            setContrastWithColor(dynamic, contrastWithColor);

            if (dynamic instanceof DynamicScrollableWidget) {
                setScrollBarColor(dynamic, color);
            }
        }
    }

    /**
     * Tint dynamic object according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #tint(Object, int, int)
     */
    public static <T> void tint(@Nullable T dynamic, @ColorInt int color) {
        tint(dynamic, color, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Tint background according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param color The tint color to be applied.
     * @param borderless {@code true} if the view is borderless.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewBackgroundTint(View, int, boolean)
     */
    public static <T> void tintBackground(@Nullable T dynamic,
            @ColorInt int contrastWithColor, @ColorInt int color, boolean borderless) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewBackgroundTint((View) dynamic,
                    contrastWithColor, color, borderless, false);
        }
    }

    /**
     * Tint background according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param borderless {@code true} if the view is borderless.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewBackgroundTint(View, int, boolean)
     */
    public static <T> void tintBackground(@Nullable T dynamic,
            @ColorInt int contrastWithColor, boolean borderless) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewBackgroundTint((View) dynamic,
                    contrastWithColor, borderless);
        }
    }

    /**
     * Tint background according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewBackgroundTint(View, int, boolean)
     */
    public static <T> void tintBackground(@Nullable T dynamic, @ColorInt int contrastWithColor) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewBackgroundTint((View) dynamic,
                    contrastWithColor, true);
        }
    }

    /**
     * Tint background according to the default contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param <T> The type of the dynamic object.
     *
     * @see #tintBackground(Object, int)
     */
    public static <T> void tintBackground(@Nullable T dynamic) {
        tintBackground(dynamic, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Tint foreground according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param color The tint color to be applied.
     * @param borderless {@code true} if the view is borderless.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewForegroundTint(View, int, boolean)
     */
    public static <T> void tintForeground(@Nullable T dynamic,
            @ColorInt int contrastWithColor, @ColorInt int color, boolean borderless) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewForegroundTint((View) dynamic,
                    contrastWithColor, color, borderless, false);
        }
    }

    /**
     * Tint foreground according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param borderless {@code true} if the view is borderless.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewForegroundTint(View, int, boolean)
     */
    public static <T> void tintForeground(@Nullable T dynamic,
            @ColorInt int contrastWithColor, boolean borderless) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewForegroundTint((View) dynamic,
                    contrastWithColor, borderless);
        }
    }

    /**
     * Tint foreground according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewForegroundTint(View, int, boolean)
     */
    public static <T> void tintForeground(@Nullable T dynamic, @ColorInt int contrastWithColor) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (dynamic instanceof Button
                || ((View) dynamic).isClickable() || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewForegroundTint((View) dynamic,
                    contrastWithColor, true);
        }
    }

    /**
     * Tint scrollable according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollUtils#tint(Object, int)
     */
    public static <T> void tintScrollable(@Nullable T dynamic, @ColorInt int contrastWithColor) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        DynamicScrollUtils.tint(dynamic, contrastWithColor);
    }

    /**
     * Tint scrollable according to the default contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param <T> The type of the dynamic object.
     *
     * @see #tintScrollable(Object, int)
     */
    public static <T> void tintScrollable(@Nullable T dynamic) {
        tintScrollable(dynamic, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Set on click listener for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param onClickListener The click listener to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicItemView#getItemView()
     * @see DynamicInfoView#getInfoView()
     * @see DynamicPreference#getPreferenceView()
     * @see View#setOnClickListener(View.OnClickListener)
     */
    public static <T> void setOnClickListener(@Nullable T dynamic,
            @Nullable View.OnClickListener onClickListener) {
        if (dynamic instanceof DynamicItemView) {
            ((DynamicItemView) dynamic).setOnClickListener(onClickListener);
        } else if (dynamic instanceof DynamicInfoView) {
            ((DynamicInfoView) dynamic).setOnClickListener(onClickListener);
        } else if (dynamic instanceof DynamicPreference) {
            ((DynamicPreference) dynamic).setOnPreferenceClickListener(onClickListener);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setOnClickListener(onClickListener);
        }
    }

    /**
     * Set on long click listener for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param onLongClickListener The long click listener to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicItemView#getItemView()
     * @see DynamicPreference#getPreferenceView()
     * @see View#setOnLongClickListener(View.OnLongClickListener)
     */
    public static <T> void setOnLongClickListener(@Nullable T dynamic,
            @Nullable View.OnLongClickListener onLongClickListener) {
        if (dynamic instanceof DynamicItemView) {
            ((DynamicItemView) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof DynamicInfoView) {
            ((DynamicInfoView) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof DynamicPreference) {
            ((DynamicPreference) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setOnLongClickListener(onLongClickListener);
        }
    }

    /**
     * Set the clickable property for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param clickable {@code true} to make it clickable.
     * @param <T> The type of the dynamic object.
     *
     * @see View#setClickable(boolean)
     */
    public static <T> void setClickable(@Nullable T dynamic, boolean clickable) {
        if (dynamic instanceof View) {
            ((View) dynamic).setClickable(clickable);
        }
    }

    /**
     * Set the long clickable property for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param longClickable {@code true} to make it long clickable.
     * @param <T> The type of the dynamic object.
     *
     * @see View#setLongClickable(boolean)
     */
    public static <T> void setLongClickable(@Nullable T dynamic, boolean longClickable) {
        if (dynamic instanceof View) {
            ((View) dynamic).setLongClickable(longClickable);
        }
    }

    /**
     * Sets the prevent corner overlap for the supplied view.
     *
     * @param view The view to be used.
     * @param preventCornerOverlap The value to be set.
     *
     * @see CardView#setPreventCornerOverlap(boolean)
     */
    public static void setPreventCornerOverlap(@Nullable View view, boolean preventCornerOverlap) {
        if (view instanceof CardView) {
            ((CardView) view).setPreventCornerOverlap(preventCornerOverlap);
        }
    }

    /**
     * Sets the corner for the supplied view.
     *
     * @param view The view to be used.
     * @param corner The corner to be set.
     *
     * @see DynamicButton#setCorner(Integer)
     * @see DynamicNavigationView#setCorner(Float)
     * @see DynamicBottomNavigationView#setCorner(Float)
     * @see DynamicNavigationRailView#setCorner(Float)
     * @see DynamicFloatingActionButton#setCorner(Float)
     * @see DynamicCardView#setCorner(Float)
     * @see DynamicMaterialCardView#setCorner(Float)
     * @see DynamicTextInputLayout#setCorner(Float)
     * @see DynamicColorView#setCornerRadius(float)
     */
    public static void setCorner(@Nullable View view, float corner) {
        if (view instanceof DynamicButton) {
            ((DynamicButton) view).setCorner((int) corner);
        } else if (view instanceof DynamicNavigationView) {
            ((DynamicNavigationView) view).setCorner((float) corner);
        } else if (view instanceof DynamicBottomNavigationView) {
            ((DynamicBottomNavigationView) view).setCorner((float) corner);
        } else if (view instanceof DynamicNavigationRailView) {
            ((DynamicNavigationRailView) view).setCorner((float) corner);
        } else if (view instanceof DynamicFloatingActionButton) {
            ((DynamicFloatingActionButton) view).setCorner((float) corner);
        } else if (view instanceof DynamicCardView) {
            ((DynamicCardView) view).setCorner(corner);
        } else if (view instanceof DynamicMaterialCardView) {
            ((DynamicMaterialCardView) view).setCorner(corner);
        } else if (view instanceof DynamicTextInputLayout) {
            ((DynamicTextInputLayout) view).setCorner(corner);
        } else if (view instanceof DynamicColorView) {
            ((DynamicColorView) view).setCornerRadius(corner);
        }
    }

    /**
     * Sets the minimum corner value for the supplied view.
     *
     * @param view The view to be used.
     * @param cornerMax The maximum corner value.
     *
     * @see #setCorner(View, float)
     */
    public static void setCornerMin(@Nullable View view, float cornerMax) {
        if (view instanceof DynamicButton) {
            setCorner(view, Math.min(((DynamicButton) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicNavigationView) {
            setCorner(view, Math.min(((DynamicNavigationView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicNavigationRailView) {
            setCorner(view, Math.min(((DynamicNavigationRailView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicBottomNavigationView) {
            setCorner(view, Math.min(((DynamicBottomNavigationView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicFloatingActionButton) {
            setCorner(view, Math.min(((DynamicFloatingActionButton) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicFloatingToolbar) {
            setCorner(view, Math.min(((DynamicFloatingToolbar) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicCardView) {
            setCorner(view, Math.min(((DynamicCardView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicMaterialCardView) {
            setCorner(view, Math.min(((DynamicMaterialCardView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicTextInputLayout) {
            setCorner(view, Math.min(((DynamicTextInputLayout) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicColorView) {
            setCorner(view, Math.min(((DynamicColorView) view).getCornerRadius(), cornerMax));
        }
    }

    /**
     * Sets whether to force elevation for the supplied view.
     *
     * @param view The view to be used.
     * @param forceElevation {@code true} to force elevation.
     */
    public static void setForceElevation(@Nullable View view, boolean forceElevation) {
        if (view instanceof DynamicSurfaceWidget) {
            ((DynamicSurfaceWidget) view).setForceElevation(forceElevation);
        }
    }

    /**
     * Returns the elevation for the supplied view.
     *
     * @param view The view to be used.
     * @param defaultElevation The default elevation to be used.
     *
     * @return The elevation for the supplied view.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static float getElevation(@Nullable View view, float defaultElevation) {
        if (view == null) {
            return defaultElevation;
        }

        if (view.getBackground() instanceof MaterialShapeDrawable) {
            return ((MaterialShapeDrawable) view.getBackground()).getElevation();
        } else if (DynamicSdkUtils.is21()) {
            return view.getElevation();
        } else {
            return defaultElevation;
        }
    }

    /**
     * Sets elevation for the supplied view.
     *
     * @param view The view to be used.
     * @param elevation The elevation to be set.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setElevation(@Nullable View view, float elevation) {
        if (view == null) {
            return;
        }

        if (view.getBackground() instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable) view.getBackground()).setElevation(elevation);
        } else if (DynamicSdkUtils.is21()) {
            view.setElevation(elevation);
        }
    }

    /**
     * Sets the background for the window or root view.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see Window#setBackgroundDrawable(Drawable)
     * @see View#setBackgroundDrawable(Drawable)
     * @see #withThemeOpacity(Drawable)
     */
    public static <T> void setRootBackground(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof Window) {
            ((Window) dynamic).setBackgroundDrawable(withThemeOpacity(new ColorDrawable(color)));
        } else if (dynamic instanceof View) {
            DynamicDrawableUtils.setBackground((View) dynamic,
                    withThemeOpacity(new ColorDrawable(color)));
        }
    }

    /**
     * Set background drawable for the view.
     *
     * @param view The view to set the background.
     * @param drawable The drawable to be set.
     *
     * @see DynamicDrawableUtils#setBackground(View, Drawable)
     */
    public static void setBackground(@Nullable View view, @Nullable Drawable drawable) {
        DynamicDrawableUtils.setBackground(view, drawable);
    }

    /**
     * Set background drawable resource for the view.
     *
     * @param view The view to set the background.
     * @param drawableRes The drawable resource to be set.
     *
     * @see #setBackground(View, Drawable)
     * @see DynamicResourceUtils#getDrawable(Context, int)
     */
    public static void setBackground(@Nullable View view, @DrawableRes int drawableRes) {
        if (view != null) {
            setBackground(view, DynamicResourceUtils.getDrawable(view.getContext(), drawableRes));
        }
    }

    /**
     * Set drawable for the image view and manage its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable to be set.
     */
    public static void set(@Nullable ImageView imageView, @Nullable Drawable drawable) {
        if (imageView == null) {
            return;
        }

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * Set bitmap for the image view and manage its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param bitmap The bitmap to be set.
     */
    public static void set(@Nullable ImageView imageView, @Nullable Bitmap bitmap) {
        if (imageView == null) {
            return;
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * Set content description for the image view.
     *
     * @param imageView The image view to set the content description.
     * @param text The content description to be set.
     */
    public static void setContentDescription(
            @Nullable ImageView imageView, @Nullable String text) {
        if (imageView == null) {
            return;
        }

        imageView.setContentDescription(text);
    }

    /**
     * Set text for the text view and manage its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void set(@Nullable TextView textView, @Nullable String text) {
        if (textView == null) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set text for the text view and manage its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void set(@Nullable TextView textView, @Nullable CharSequence text) {
        if (textView == null) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set rating value for the rating bar.
     *
     * @param ratingBar The rating bar to set the rating.
     * @param rating The rating value to be set.
     */
    public static void set(@Nullable RatingBar ratingBar, float rating) {
        if (ratingBar != null) {
            ratingBar.setRating(rating);
        }
    }

    /**
     * Set {@code HTML} text for the text view and manage its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void setHtml(@Nullable TextView textView, @Nullable CharSequence text) {
        if (textView == null) {
            return;
        }

        if (text != null && !TextUtils.isEmpty(text)) {
            textView.setText(HtmlCompat.fromHtml(text.toString(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT));
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set drawable for the image view and manage its visibility according to the data.
     *
     * @param view The view to set the drawable.
     * @param drawableRes The drawable resource id to be set.
     */
    public static void setResource(@Nullable View view, @DrawableRes int drawableRes) {
        if (view instanceof FloatingActionButton) {
            ((ImageView) view).setImageResource(drawableRes);
        } else if (view instanceof ImageView) {
            set((ImageView) view, DynamicResourceUtils.getDrawable(
                    view.getContext(), drawableRes));
        }
    }

    /**
     * Set text for the text view and manage its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param stringRes The string resource id to be set.
     */
    public static void setText(@Nullable TextView textView, @StringRes int stringRes) {
        if (textView != null) {
            set(textView, textView.getContext().getString(stringRes));
        }
    }

    /**
     * Set tooltip for the supplied view.
     *
     * @param view The view to be used.
     * @param backgroundColor The background color to be set.
     * @param tintColor The tint color to be set.
     * @param text The tooltip text to be set.
     *
     * @see DynamicTooltip#set(View, int, int, CharSequence)
     */
    public static void setTooltip(@Nullable View view, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Nullable CharSequence text) {
        if (view instanceof DynamicWidget) {
            backgroundColor = getColor(view, backgroundColor);
            tintColor = getContrastWithColor(view, tintColor);
        }

        if (isBackgroundAware(view)) {
            tintColor = withContrastRatio(tintColor, backgroundColor, view);
        }

        DynamicTooltip.set(view, backgroundColor, tintColor, text);
    }

    /**
     * Set tooltip for the supplied view.
     *
     * @param view The view to be used.
     * @param backgroundColor The background color to be set.
     * @param tintColor The tint color to be set.
     *
     * @see #setTooltip(View, int, int, CharSequence)
     */
    public static void setTooltip(@Nullable View view,
            @ColorInt int backgroundColor, @ColorInt int tintColor) {
        if (view != null) {
            setTooltip(view, backgroundColor, tintColor, view.getContentDescription());
        }
    }

    /**
     * Set tooltip for the supplied view.
     *
     * @param view The view to be used.
     * @param text The tooltip text to be set.
     *
     * @see #setTooltip(View, int, int, CharSequence)
     */
    public static void setTooltip(@Nullable View view, @Nullable CharSequence text) {
        setTooltip(view, DynamicTheme.getInstance().resolveColorType(
                Defaults.ADS_COLOR_TYPE_TOOLTIP_BACKGROUND),
                DynamicTheme.getInstance().resolveColorType(
                        Defaults.ADS_COLOR_TYPE_TOOLTIP_TINT), text);
    }

    /**
     * Set tooltip for the supplied view.
     *
     * @param view The view to be used.
     *
     * @see #setTooltip(View, CharSequence)
     */
    public static void setTooltip(@Nullable View view) {
        if (view != null) {
            setTooltip(view, view.getContentDescription());
        }
    }

    /**
     * Set click listener for the view and manage its visibility according to the data.
     *
     * @param view The view to set the click listener.
     * @param clickListener The click listener to be set.
     * @param visibility {@code true} to manage the visibility.
     */
    public static void setClickListener(@Nullable View view,
            @Nullable View.OnClickListener clickListener, boolean visibility) {
        if (view == null) {
            return;
        }

        view.setOnClickListener(clickListener);

        if (clickListener != null) {
            if (visibility) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            view.setClickable(false);

            if (visibility) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Set click listener for the view and manage its visibility according to the data.
     *
     * @param view The view to set the click listener.
     * @param clickListener The click listener to be set.
     */
    public static void setClickListener(@Nullable View view,
            @Nullable View.OnClickListener clickListener) {
        setClickListener(view, clickListener, false);
    }

    /**
     * Set visibility for the view.
     *
     * @param view The view to set the visibility.
     * @param visibility The visibility to be set.
     */
    public static void setVisibility(@Nullable View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * Set visibility for the view according to the base view.
     *
     * @param view The view to set the visibility.
     * @param base The view to get the visibility.
     */
    public static void setVisibility(@Nullable View view, @Nullable View base) {
        if (base != null) {
            setVisibility(view, base.getVisibility());
        }
    }

    /**
     * Set a view enabled or disabled.
     *
     * @param view The view to be enabled or disabled.
     * @param enabled {@code true} to enable the view.
     */
    public static void setEnabled(@Nullable View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    /**
     * Set alpha for the view.
     *
     * @param view The view to set the alpha.
     * @param alpha The alpha to be set.
     */
    public static void setAlpha(@Nullable View view,
            @FloatRange(from = 0f, to = 1f) float alpha) {
        if (view != null) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Set the visibility of app bar progress for the {@link DynamicActivity}.
     *
     * @param activity The activity to be used.
     * @param visible {@code true} to show the progress bar below the app bar.
     */
    public static void setAppBarProgressVisible(@Nullable Context activity, boolean visible) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setAppBarProgressVisible(visible);
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param text The text for the snackbar.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     */
    public static void showSnackbar(@Nullable Context activity,
            @Nullable CharSequence text, @Snackbar.Duration int duration) {
        final Snackbar snackbar;
        if (activity instanceof DynamicSnackbar && text != null && (snackbar
                = ((DynamicSnackbar) activity).getSnackbar(text, duration)) != null) {
            ((DynamicSnackbar) activity).onSnackbarShow(snackbar);
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param text The text for the snackbar.D
     */
    public static void showSnackbar(@Nullable Context activity, @Nullable CharSequence text) {
        final Snackbar snackbar;
        if (activity instanceof DynamicSnackbar && text != null && (snackbar
                = ((DynamicSnackbar) activity).getSnackbar(text)) != null) {
            ((DynamicSnackbar) activity).onSnackbarShow(snackbar);
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param stringRes The string resource for the snackbar.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     */
    public static void showSnackbar(@Nullable Context activity,
            @StringRes int stringRes, @Snackbar.Duration int duration) {
        final Snackbar snackbar;
        if (activity instanceof DynamicSnackbar && (snackbar
                = ((DynamicSnackbar) activity).getSnackbar(stringRes, duration)) != null) {
            ((DynamicSnackbar) activity).onSnackbarShow(snackbar);
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param stringRes The string resource for the snackbar.
     */
    public static void showSnackbar(@Nullable Context activity, @StringRes int stringRes) {
        final Snackbar snackbar;
        if (activity instanceof DynamicSnackbar && (snackbar
                = ((DynamicSnackbar) activity).getSnackbar(stringRes)) != null) {
            ((DynamicSnackbar) activity).onSnackbarShow(snackbar);
        }
    }

    /**
     * Sets the bottom sheet state if present for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param bottomSheetState The state to be set.
     */
    public static void setBottomSheetState(@Nullable Context activity,
            @BottomSheetBehavior.State int bottomSheetState) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setBottomSheetState(bottomSheetState);
        }
    }

    /**
     * Try to find view for the supplied view id.
     *
     * @param view The view to be used.
     * @param viewId The view id to be found.
     *
     * @return The view for the supplied view id.
     */
    public static @Nullable View findViewById(@Nullable View view, @IdRes int viewId) {
        if (view == null || viewId == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            return null;
        }

        return view.findViewById(viewId);
    }

    /**
     * Sets the transition name for the supplied view.
     *
     * @param view The view to be used.
     * @param name The transition name to be set.
     */
    public static void setTransitionName(@Nullable View view, @Nullable String name) {
        if (view != null) {
            ViewCompat.setTransitionName(view, name);
        }
    }

    /**
     * Sets the transition result code for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param transitionResultCode The transition result code to be set.
     */
    public static void setTransitionResultCode(@Nullable Context activity,
            int transitionResultCode) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setTransitionResultCode(transitionResultCode);
        }
    }

    /**
     * Sets the transition position for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param transitionPosition The transition position to be set.
     */
    public static void setTransitionPosition(@Nullable Context activity, int transitionPosition) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setTransitionPosition(transitionPosition);
        }
    }

    /**
     * Sets the listener to listen the search view expand and collapse callbacks
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param searchListener The search listener to be set.
     */
    public static void setSearchViewListener(@Nullable Context activity,
            @Nullable DynamicSearchListener searchListener) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setSearchViewListener(searchListener);
        }
    }

    /**
     * Adds the text watcher to listen search view text changes for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param textWatcher The search listener to be added.
     */
    public static void addSearchViewTextChangedListener(@Nullable Context activity,
            @Nullable TextWatcher textWatcher) {
        EditText editText;
        if (activity instanceof DynamicActivity && textWatcher != null
                && (editText = ((DynamicActivity) activity).getSearchViewEditText()) != null) {
            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Removes the text watcher from the search view to stop listening text changes
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param textWatcher The search listener to be removed.
     */
    public static void removeSearchViewTextChangedListener(@Nullable Context activity,
            @Nullable TextWatcher textWatcher) {
        EditText editText;
        if (activity instanceof DynamicActivity && textWatcher != null
                && (editText = ((DynamicActivity) activity).getSearchViewEditText()) != null) {
            editText.removeTextChangedListener(textWatcher);
        }
    }

    /**
     * Expand search view to start searching for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param requestSoftInput {@code true} to request the soft input keyboard.
     */
    public static void expandSearchView(@Nullable Context activity, boolean requestSoftInput) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).expandSearchView(requestSoftInput);
        }
    }

    /**
     * Collapse search view to stop searching for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     */
    public static void collapseSearchView(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).collapseSearchView();
        }
    }

    /**
     * Checks whether the search view is expanded for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     *
     * @return {@code true} if search view is expanded.
     */
    public static boolean isSearchViewExpanded(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            return ((DynamicActivity) activity).isSearchViewExpanded();
        }

        return false;
    }

    /**
     * Restore the search view state after the configuration change
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     */
    public static void restoreSearchViewState(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).restoreSearchViewState();
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     * @param animate {@code true} to animate the changes.
     */
    public static void addHeader(@Nullable Context activity, @Nullable View view,
            boolean removePrevious, boolean animate) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(view, removePrevious, animate);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addHeader(@Nullable Context activity,
            @Nullable View view, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(view, removePrevious);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     * @param animate {@code true} to animate the changes.
     */
    public static void addHeader(@Nullable Context activity, @LayoutRes int layoutRes,
            boolean removePrevious, boolean animate) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(layoutRes, removePrevious, animate);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addHeader(@Nullable Context activity,
            @LayoutRes int layoutRes, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(layoutRes, removePrevious);
        }
    }

    /**
     * Add view in the bottom sheet frame layout for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addBottomSheet(@Nullable Context activity,
            @Nullable View view, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addBottomSheet(view, removePrevious);
        }
    }

    /**
     * Add view in the bottom sheet frame layout for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addBottomSheet(@Nullable Context activity,
            @LayoutRes int layoutRes, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addBottomSheet(layoutRes, removePrevious);
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior
     * for the {@link DynamicActivity}.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param activity The activity context to be used.
     * @param fragment The calling fragment.
     * @param intent The intent to be used.
     * @param requestCode The request code to be set.
     * @param options The intent to be set.
     * @param finish {@code true} to finish calling activity.
     * @param afterTransition {@code true} to finish the calling activity after transition.
     */
    public static void startMotionActivityFromFragment(@Nullable Context activity,
            @NonNull Fragment fragment, @SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options, boolean finish, boolean afterTransition) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).startMotionActivityFromFragment(
                    fragment, intent, requestCode, options, finish, afterTransition);
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior
     * for the {@link DynamicActivity}.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param activity The activity context to be used.
     * @param fragment The calling fragment.
     * @param intent The intent to be used.
     * @param requestCode The request code to be set.
     * @param options The intent to be set.
     */
    public static void startMotionActivityFromFragment(@Nullable Context activity,
            @NonNull Fragment fragment, @SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).startMotionActivityFromFragment(
                    fragment, intent, requestCode, options);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param position The position index of the first page currently being displayed.
     *                 <p>Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset The value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels The value in pixels indicating the offset from position.
     */
    public static void onPageScrolled(@Nullable ViewPager.OnPageChangeListener pager,
            int position, float positionOffset, @Px int positionOffsetPixels) {
        if (pager != null) {
            pager.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onPageSelected(int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param position Position index of the new selected page.
     */
    public static void onPageSelected(@Nullable ViewPager.OnPageChangeListener pager,
            int position) {
        if (pager != null) {
            pager.onPageSelected(position);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onScreenStateChanged(int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param state The new scroll state.
     *
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    public static void onPageScrollStateChanged(@Nullable ViewPager.OnPageChangeListener pager,
            int state) {
        if (pager != null) {
            pager.onPageScrollStateChanged(state);
        }
    }

    /**
     * Call {@link Tutorial#getColor()} method for the {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param defaultColor The default color for the background.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     *
     * @return The background color for the supplied tutorial.
     */
    public static @ColorInt <T, V> int getColor(@Nullable Tutorial<T, V> tutorial,
            @ColorInt int defaultColor) {
        if (tutorial != null) {
            return tutorial.getColor();
        }

        return defaultColor;
    }

    /**
     * Call {@link Tutorial#getTintColor()} method for the {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param defaultColor The default tint color.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     *
     * @return The tint color for the supplied tutorial.
     */
    public static @ColorInt <T, V> int getTintColor(@Nullable Tutorial<T, V> tutorial,
            @ColorInt int defaultColor) {
        if (tutorial != null) {
            return tutorial.getTintColor();
        }

        return defaultColor;
    }

    /**
     * Call {@link Tutorial#onColorChanged(int, int)} method for the
     * {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param color The color of the background.
     * @param tint The tint color used by the tutorial.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     */
    public static <T, V> void onColorChanged(@Nullable Tutorial<T, V> tutorial,
            @ColorInt int color, @ColorInt int tint) {
        if (tutorial != null) {
            tutorial.onColorChanged(color, tint);
        }
    }

    /**
     * Call {@link Tutorial#onSetPadding(int, int, int, int)} method for the
     * {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param left The left padding supplied by the container.
     * @param top The top padding supplied by the container.
     * @param right The right padding supplied by the container.
     * @param bottom The bottom padding supplied by the container.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     */
    public static <T, V> void onSetPadding(@Nullable Tutorial<T, V> tutorial,
            int left, int top, int right, int bottom) {
        if (tutorial != null) {
            tutorial.onSetPadding(left, top, right, bottom);
        }
    }

    /**
     * Returns the app shortcut icon according to the dynamic theme.
     *
     * @param context The context to be used.
     * @param drawableRes The drawable resource to be used.
     * @param backgroundId The background id to be used.
     * @param foregroundId The foreground id to be used.
     * @param theme {@code true} to use {@link Theme.ColorType#PRIMARY} as background.
     *
     * @return The app shortcut icon according to the dynamic theme.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static @Nullable Icon getAppShortcutIcon(@NonNull Context context,
            @DrawableRes int drawableRes, @IdRes int backgroundId,
            @IdRes int foregroundId, boolean theme) {
        @ColorInt int background = DynamicTheme.getInstance().get(false).getPrimaryColor();
        @ColorInt int tint = DynamicTheme.getInstance().get(false).getTintPrimaryColor();
        Drawable drawable = DynamicResourceUtils.getDrawable(context, drawableRes);

        if (!theme) {
            background = DynamicTheme.getInstance().get(false).getBackgroundColor();
            tint = DynamicTheme.getInstance().get(false).getTintBackgroundColor();
        }

        if (drawable != null) {
            DynamicDrawableUtils.colorizeDrawable(((LayerDrawable) drawable)
                    .findDrawableByLayerId(backgroundId), background);
            DynamicDrawableUtils.colorizeDrawable(((LayerDrawable) drawable)
                    .findDrawableByLayerId(foregroundId), tint);

            return IconCompat.createWithAdaptiveBitmap(DynamicResourceUtils
                    .getBitmapFromVectorDrawable(drawable)).toIcon(context);
        }

        return null;
    }
}
