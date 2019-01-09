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

package com.pranavpandey.android.dynamic.support.widget.base;

import androidx.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;

/**
 * Interface to create dynamic widgets which can be tinted according to the {@link DynamicTheme}.
 */
public interface DynamicWidget extends BaseWidget {

    /**
     * Returns the color type applied to this widget.
     * 
     * @return The color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getColorType();

    /**
     * Set the color type to this widget.
     *
     * @param colorType The color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setColorType(@Theme.ColorType int colorType);

    /**
     * Returns the contrast with color type applied to this widget.
     * 
     * @return The contrast with color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getContrastWithColorType();

    /**
     * Set the contrast with color type to this widget.
     *
     * @param contrastWithColorType The contrast with color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setContrastWithColorType(@Theme.ColorType int contrastWithColorType);

    /**
     * Returns the value of color applied to this widget.
     * 
     * @return The value of color applied to this widget.
     */
    @ColorInt int getColor();

    /**
     * Set the value of color for this widget.
     *
     * @param color Color for this widget.
     */
    void setColor(@ColorInt int color);

    /**
     * Returns the value of contrast with color applied to this widget.
     * 
     * @return The value of contrast with color applied to this widget.
     */
    @ColorInt int getContrastWithColor();

    /**
     * Set the value of contrast with color for this widget.
     *
     * @param contrastWithColor Contrast with color for this widget.
     */
    void setContrastWithColor(@ColorInt int contrastWithColor);

    /**
     * Returns the background aware functionality used by this widget.
     * 
     * @return The background aware functionality used by this widget.
     */
    @Theme.BackgroundAware int getBackgroundAware();

    /**
     * Set the value to make this widget background aware or not.
     *
     * @param backgroundAware The background aware functionality to be set.
     */
    void setBackgroundAware(@Theme.BackgroundAware int backgroundAware);

    /**
     * Checks whether the background aware functionality is enabled.
     * 
     * @return {@code true} if this widget changes color according to the background.
     */
    boolean isBackgroundAware();

    /**
     * Override this method to handle state changes for this widget.
     * <p>For example, change alpha according to the enabled or disabled state of this widget.
     *
     * @param enabled {@code true} if this widget is enabled and can receive click events.
     */
    void setEnabled(boolean enabled);

    /**
     * Set color of this widget according to the supplied values.
     */
    void setColor();
}
