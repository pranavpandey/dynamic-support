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

import android.support.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets with a scroll bar
 * according to the {@link DynamicTheme}.
 */
public interface DynamicScrollableWidget extends DynamicWidget {

    /**
     * @return The scroll bar color type applied to this view.
     *
     * @see DynamicColorType
     */
    @DynamicColorType int getScrollBarColorType();

    /**
     * Set the scroll bar color type to this view.
     *
     * @param scrollBarColorType Scroll bar color type for this view.
     *
     * @see DynamicColorType
     */
    void setScrollBarColorType(@DynamicColorType int scrollBarColorType);

    /**
     * @return The value of scroll bar color applied to this view.
     */
    @ColorInt int getScrollBarColor();

    /**
     * Set the value of scroll bar color for this view.
     *
     * @param scrollBarColor Scroll bar color for this view.
     */
    void setScrollBarColor(@ColorInt int scrollBarColor);

    /**
     * Set scroll bar color of this view according to the supplied
     * values.
     */
    void setScrollBarColor();

    /**
     * Set color and scroll bar color of this view at once.
     *
     * @param setScrollBarColor {@code true} to set the scroll bar color
     *                          also.
     */
    void setColor(boolean setScrollBarColor);
}
