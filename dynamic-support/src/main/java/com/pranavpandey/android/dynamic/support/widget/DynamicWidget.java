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

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets which can be tinted
 * according to the {@link DynamicTheme}.
 */
public interface DynamicWidget {

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    void loadFromAttributes(@Nullable AttributeSet attrs);

    /**
     * Initialize this view by setting color type. If it is background
     * aware then, background color will also taken into account while
     * setting the color filter.
     *
     * @see DynamicColorType
     */
    void initialize();

    /**
     * @return The color type applied to this view.
     *
     * @see DynamicColorType
     */
    @DynamicColorType int getColorType();

    /**
     * Set the color type to this view.
     *
     * @param colorType Color type for this view.
     *
     * @see DynamicColorType
     */
    void setColorType(@DynamicColorType int colorType);

    /**
     * @return The contrast with color type applied to this view.
     *
     * @see DynamicColorType
     */
    @DynamicColorType int getContrastWithColorType();

    /**
     * Set the contrast with color type to this view.
     *
     * @param contrastWithColorType Contrast with Color type for
     *                              this view.
     *
     * @see DynamicColorType
     */
    void setContrastWithColorType(@DynamicColorType int contrastWithColorType);

    /**
     * @return The value of color applied to this view.
     */
    @ColorInt int getColor();

    /**
     * Set the value of color for this view.
     *
     * @param color Color for this view.
     */
    void setColor(@ColorInt int color);

    /**
     * @return The value of contrast with color applied to this view.
     */
    @ColorInt int getContrastWithColor();

    /**
     * Set the value of contrast with color for this view.
     *
     * @param contrastWithColor Contrast with color for this view.
     */
    void setContrastWithColor(@ColorInt int contrastWithColor);

    /**
     * @return {@code true} if this view changes color according
     * to the background.
     */
    boolean isBackgroundAware();

    /**
     * Set the value to make this view background aware or not.
     *
     * @param backgroundAware {@code true} to make this view
     *                        background aware.
     */
    void setBackgroundAware(boolean backgroundAware);

    /**
     * Override this method to handle state changes for this view.
     * For example, change alpha according to the enabled or disabled
     * state of this view.
     *
     * @param enabled {@code true} if this widget is enabled and can
     *                receive click events.
     */
    void setEnabled(boolean enabled);

    /**
     * Set color of this view according to the supplied values.
     */
    void setColor();
}
