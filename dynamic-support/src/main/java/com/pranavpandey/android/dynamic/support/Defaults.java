/*
 * Copyright 2018-2021 Pranav Pandey
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

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * Default configurations used by the dynamic widgets.
 */
public class Defaults {

    /**
     * Constant for the invalid or no position.
     */
    public static final int ADS_NO_POSITION = RecyclerView.NO_POSITION;

    /**
     * Constant for the stroke width, 1 dip.
     */
    public static final int ADS_STROKE_WIDTH = 1;

    /**
     * Constant for the stroke width int pixels, equivalent to 1 dip.
     */
    public static final int ADS_STROKE_WIDTH_PIXEL =
            DynamicUnitUtils.convertDpToPixels(ADS_STROKE_WIDTH);

    /**
     * Constant for the stroke alpha.
     */
    public static final int ADS_STROKE_ALPHA = 100;

    /**
     * Constant to adjust the corner size for the header.
     */
    public static final float ADS_STROKE_CORNER_ADJUST = 1f;

    /**
     * Constant for the selected tab height.
     */
    public static final float ADS_HEIGHT_TAB_SELECTED = 3f;

    /**
     * Constant for the selected tab height int pixels, equivalent to 3 dip.
     */
    public static final int ADS_HEIGHT_TAB_SELECTED_PIXEL =
            DynamicUnitUtils.convertDpToPixels(ADS_HEIGHT_TAB_SELECTED);

    /**
     * Minimum corner size in dips to provide the rectangle theme overlay.
     */
    public static final float ADS_CORNER_MIN_THEME = 4f;

    /**
     * Minimum corner size in dips to provide the rounded theme overlay.
     */
    public static final float ADS_CORNER_MIN_THEME_ROUND = 8f;

    /**
     * Minimum corner size in dips to provide the rounded tab indicator.
     */
    public static final float ADS_CORNER_MIN_TABS = 6f;

    /**
     * Minimum corner size in dips for the box background.
     */
    public static final float ADS_CORNER_MIN_BOX = 8f;

    /**
     * Minimum corner size in dips to provide the rounded rectangle list selector.
     */
    public static final float ADS_CORNER_SELECTOR_RECT = 4f;

    /**
     * Minimum corner size in dips to provide the rounded list selector.
     */
    public static final float ADS_CORNER_SELECTOR_ROUND = 8f;

    /**
     * Default alpha for the surface color to do not show stroke on the same background.
     */
    public static final int ADS_ALPHA_SURFACE_STROKE = 255;

    /**
     * Default alpha for the toast.
     */
    public static final float ADS_ALPHA_DIVIDER = 0.5f;

    /**
     * Default alpha for the toast.
     */
    public static final float ADS_ALPHA_TOAST = 0.94f;

    /**
     * Default alpha when the widget is enabled.
     */
    public static final float ADS_ALPHA_ENABLED = 1f;

    /**
     * Default alpha when the widget is enabled integer.
     */
    public static final int ADS_ALPHA_ENABLED_INTEGER = 255;

    /**
     * Default alpha for the hint.
     */
    public static final float ADS_ALPHA_HINT = 0.8f;

    /**
     * Default alpha for the unselected state.
     */
    public static final float ADS_ALPHA_UNSELECTED = 0.8f;

    /**
     * Default alpha for the unselected state in integer.
     */
    public static final int ADS_ALPHA_UNSELECTED_INTEGER = 200;

    /**
     * Default alpha value for the unselected state to generate color state list dynamically.
     */
    public static final float ADS_ALPHA_UNCHECKED = 0.7f;

    /**
     * Default alpha value for the selected state to generate color state list dynamically.
     */
    public static final float ADS_ALPHA_SELECTED = 0.6f;

    /**
     * Default alpha when the widget is pressed.
     */
    public static final float ADS_ALPHA_PRESSED = 0.2f;

    /**
     * Default alpha when the selector is pressed.
     */
    public static final float ADS_ALPHA_PRESSED_SELECTOR = 0.4f;

    /**
     * Default alpha for the scrim insets.
     */
    public static final float ADS_ALPHA_SCRIM = 0.7f;

    /**
     * Default alpha for the backdrop view.
     */
    public static final float ADS_ALPHA_BACKDROP = 0.4f;

    /**
     * Default alpha when the widget is disabled.
     */
    public static final float ADS_ALPHA_DISABLED = 0.5f;

    /**
     * Default inset for the hue drawable.
     */
    public static final float ADS_INSET_HUE = 0.45f;

    /**
     * Default light state value for the box background.
     */
    public static final float ADS_STATE_BOX_LIGHT = 0.12f;

    /**
     * Default dark state value for the box background.
     */
    public static final float ADS_STATE_BOX_DARK = 0.1f;

    /**
     * Default value to lighten the color.
     */
    public static final float ADS_STATE_LIGHT = 0.3f;

    /**
     * Default value to darken the color.
     */
    public static final float ADS_STATE_DARK = 0.2f;

    /**
     * Default value for the pressed state.
     */
    public static final float ADS_STATE_PRESSED = 0.3f;

    /**
     * Default value to shift a light color.
     */
    public static final float ADS_SHIFT_LIGHT = 0.9f;

    /**
     * Default value to shift a dark color.
     */
    public static final float ADS_SHIFT_DARK = 1.1f;

    /**
     * Default factor to generate the surface color.
     */
    public static final float ADS_FACTOR_SURFACE = 0.04f;

    /**
     * Default color type used by the system secondary widgets.
     */
    public static final int ADS_COLOR_TYPE_SYSTEM_SECONDARY = Theme.ColorType.ACCENT_DARK;

    /**
     * Default value for the error color type.
     */
    public static final int ADS_COLOR_TYPE_ERROR = Theme.ColorType.ERROR;

    /**
     * Default edge effect or glow color type used by the scrollable widgets.
     */
    public static final int ADS_COLOR_TYPE_EDGE_EFFECT = Theme.ColorType.PRIMARY;

    /**
     * Default color type used by the scrollable widgets.
     */
    public static final int ADS_COLOR_TYPE_SCROLLABLE = Theme.ColorType.TINT_BACKGROUND;

    /**
     * Default icon color type used by the image widgets.
     */
    public static final int ADS_COLOR_TYPE_ICON = ADS_COLOR_TYPE_SCROLLABLE;

    /**
     * Default value to make widgets background aware so that they can change color according
     * to the theme background to provide best visibility.
     * <p>{@code true} to make widgets background aware.
     *
     * @deprecated
     */
    public static final boolean ADS_BACKGROUND_AWARE = true;

    /**
     * Default value to provide widget corner radius according to the dynamic theme.
     * <p>{@code true} to provide widget corner radius according to the dynamic theme.
     */
    public static final boolean ADS_DYNAMIC_CORNER_RADIUS = true;

    /**
     * Default value to provide widget elevation on the same background.
     * <p>{@code true} to enable elevation on the same background.
     */
    public static final boolean ADS_ELEVATION_ON_SAME_BACKGROUND = false;

    /**
     * Default value to for the floating view.
     * <p>{@code false} to disable the floating view.
     */
    public static final boolean ADS_FLOATING_VIEW = false;

    /**
     * Default borderless value for the widgets.
     * <p>{@code true} if the widget is borderless.
     */
    public static final boolean ADS_STYLE_BORDERLESS = false;

    /**
     * Default tint background value for the widgets.
     * <p>{@code true} to enable the background tinting.
     */
    public static final boolean ADS_TINT_BACKGROUND = true;

    /**
     * Default value to show a divider below the widgets.
     * <p>{@code true} to show the divider.
     */
    public static final boolean ADS_SHOW_DIVIDER = false;

    /**
     * Default value to fill the empty icon space.
     * <p>{@code true} to fill the space.
     */
    public static final boolean ADS_FILL_SPACE = false;

    /**
     * Default value to provide the dynamic RTL support.
     * <p>{@code true} to provide the dynamic RTL support.
     */
    public static final boolean ADS_RTL_SUPPORT = true;

    /**
     * {@code true} to make apply window insets.
     */
    public static final boolean ADS_WINDOW_INSETS = true;

    /**
     * Returns the default value for the background functionality used by the various widgets.
     *
     * @return The default value for the background functionality used by the various widgets.
     *
     * @see Theme.BackgroundAware
     */
    public static @Theme.BackgroundAware int getBackgroundAware() {
        return DynamicTheme.getInstance().get().getBackgroundAware(false);
    }

    /**
     * Get the default contrast with color from the theme.
     * <p>Generally, it should be a background color so that widgets can change their
     * color accordingly if they are background aware.
     *
     * @param context The context to retrieve theme and resources.
     *
     * @return The default contrast with color.
     *
     * @see com.pranavpandey.android.dynamic.support.R.attr#ads_contrastWithDefault
     */
    public static @ColorInt int getContrastWithColor(@NonNull Context context) {
        return DynamicTheme.getInstance().getDefaultContrastWith();
    }
}
