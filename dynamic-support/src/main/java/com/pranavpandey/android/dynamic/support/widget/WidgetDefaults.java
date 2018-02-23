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

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

/**
 * Default configurations used by the dynamic widgets.
 */
public class WidgetDefaults {

    /**
     * Default alpha when the widget is enabled.
     */
    public static final float ADS_ALPHA_ENABLED = 1.0f;

    /**
     * Default alpha value for the unselected state to generate
     * color state list dynamically.
     */
    public static final float ADS_ALPHA_UNCHECKED = 0.65f;

    /**
     * Default alpha when the widget is disabled.
     */
    public static final float ADS_ALPHA_DISABLED = 0.4f;

    /**
     * Default state value to lighten the color.
     */
    public static final float ADS_STATE_LIGHT = 0.3f;

    /**
     * Default state value to darken the color.
     */
    public static final float ADS_STATE_DARK = 0.2f;

    /**
     * Default pressed state value.
     */
    public static final float ADS_STATE_PRESSED = 0.15f;

    /**
     * Constant for the unknown color.
     */
    public static final int ADS_COLOR_UNKNOWN =
            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE ;

    /**
     * Default edge effect or glow color used by the scrollable widgets.
     */
    public static final int ADS_COLOR_EDGE_EFFECT = DynamicColorType.PRIMARY;

    /**
     * Default scroll bar used by the scrollable widgets.
     */
    public static final int ADS_COLOR_SCROLL_BAR = DynamicColorType.TINT_BACKGROUND;

    /**
     * Default value to make widgets background aware so that they can
     * change color according to the theme background to provide best
     * visibility.
     * <p>
     * {@code true} to make widgets background aware.</p>
     */
    public static final boolean ADS_BACKGROUND_AWARE = true;

    /**
     * Default value to make cards background aware so that they can
     * change color according to the theme background to provide best
     * visibility.
     * <p>
     * {@code true} to make widgets background aware.</p>
     */
    public static final boolean ADS_BACKGROUND_AWARE_CARD = false;

    /**
     * Default value to show a divider below the widgets.
     */
    public static final boolean ADS_SHOW_DIVIDER = false;

    /**
     * Get default contrast with color from the theme. Generally, it should
     * be a background color so that widgets can change their color accordingly
     * if they are background aware.
     *
     * @param context Context to retrieve theme and resources.
     *
     * @return Default contrast with color.
     *
     * @see com.pranavpandey.android.dynamic.support.R.attr#ads_contrastWithDefault
     */
    public static @ColorInt int getDefaultContrastWithColor(@NonNull Context context) {
        return DynamicTheme.getInstance().getDefaultContrastWith();
    }
}
