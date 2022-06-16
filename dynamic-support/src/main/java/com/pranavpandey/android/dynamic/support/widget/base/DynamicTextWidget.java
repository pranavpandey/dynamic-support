/*
 * Copyright 2018-2022 Pranav Pandey
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
 * Interface to create dynamic widgets with a text view which can be tinted according to the
 * {@link DynamicTheme}.
 */
public interface DynamicTextWidget extends DynamicWidget {

    /**
     * Returns the text color type applied to this widget.
     * 
     * @return The text color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getTextColorType();

    /**
     * Set the text color type to this widget.
     *
     * @param textColorType Text color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setTextColorType(@Theme.ColorType int textColorType);

    /**
     * Returns the value of text color applied to this widget.
     *
     * @param resolve {@code true} to resolve the text color applied to this widget.
     *
     * @return The value of text color applied to this widget.
     */
    @ColorInt int getTextColor(boolean resolve);

    /**
     * Returns the value of text color applied to this widget.
     * 
     * @return The value of text color applied to this widget.
     */
    @ColorInt int getTextColor();

    /**
     * Set the value of text color for this widget.
     *
     * @param textColor Text color for this widget.
     */
    void setTextColor(@ColorInt int textColor);

    /**
     * Set text color of this widget according to the supplied values.
     * <p>Generally, it should be a tint color so that text will be visible on this
     * widget background.
     */
    void setTextColor();
}
