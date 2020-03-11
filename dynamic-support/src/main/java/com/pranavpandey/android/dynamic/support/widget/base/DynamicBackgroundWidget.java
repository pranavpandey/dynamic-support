/*
 * Copyright 2018-2020 Pranav Pandey
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
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Interface to create dynamic widgets with a background which can be tinted according
 * to the {@link DynamicTheme}.
 */
public interface DynamicBackgroundWidget {

    /**
     * Returns the background color type applied to this widget.
     *
     * @return The background color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getBackgroundColorType();

    /**
     * Set the background color type to this widget.
     *
     * @param backgroundColorType Text color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setBackgroundColorType(@Theme.ColorType int backgroundColorType);

    /**
     * Returns the value of background color applied to this widget.
     *
     * @return The value of background color applied to this widget.
     */
    @ColorInt int getBackgroundColor();

    /**
     * Set the value of background color for this widget.
     *
     * @param backgroundColor Background color for this widget.
     */
    void setBackgroundColor(@ColorInt int backgroundColor);
}