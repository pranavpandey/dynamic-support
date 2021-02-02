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

package com.pranavpandey.android.dynamic.support.widget.base;

import androidx.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Interface to create dynamic widgets with a scroll bar according to the {@link DynamicTheme}.
 */
public interface DynamicScrollableWidget extends DynamicWidget {

    /**
     * Returns the scroll bar color type applied to this widget.
     * 
     * @return The scroll bar color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getScrollBarColorType();

    /**
     * Set the scroll bar color type to this widget.
     *
     * @param scrollBarColorType Scroll bar color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setScrollBarColorType(@Theme.ColorType int scrollBarColorType);

    /**
     * Returns the value of scroll bar color applied to this widget.
     *
     * @param resolve {@code true} to resolve the applied scroll bar color.
     *
     * @return The value of scroll bar color applied to this widget.
     */
    @ColorInt int getScrollBarColor(boolean resolve);

    /**
     * Returns the value of scroll bar color applied to this widget.
     * 
     * @return The value of scroll bar color applied to this widget.
     */
    @ColorInt int getScrollBarColor();

    /**
     * Set the value of scroll bar color for this widget.
     *
     * @param scrollBarColor Scroll bar color for this widget.
     */
    void setScrollBarColor(@ColorInt int scrollBarColor);

    /**
     * Set scroll bar color of this widget according to the supplied values.
     */
    void setScrollBarColor();

    /**
     * Set color and scroll bar color of this widget at once.
     *
     * @param setScrollBarColor {@code true} to set the scroll bar color also.
     */
    void setColor(boolean setScrollBarColor);
}
