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
 * Interface to create dynamic widgets with a normal and selected state according to the
 * {@link DynamicTheme}.
 */
public interface DynamicStateSelectedWidget extends DynamicStateWidget {

    /**
     * Returns the selected state color type applied to this widget.
     * 
     * @return The selected state color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getStateSelectedColorType();

    /**
     * Set the selected state color type to this widget.
     *
     * @param stateSelectedColorType Normal state color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setStateSelectedColorType(@Theme.ColorType int stateSelectedColorType);

    /**
     * Returns the value of selected state color applied to this widget.
     *
     * @param resolve {@code true} to resolve the selected state color.
     *
     * @return The value of selected state color applied to this widget.
     */
    @ColorInt int getStateSelectedColor(boolean resolve);

    /**
     * Returns the value of selected state color applied to this widget.
     * 
     * @return The value of selected state color applied to this widget.
     */
    @ColorInt int getStateSelectedColor();

    /**
     * Set the value of selected state color for this widget.
     *
     * @param stateSelectedColorColor Normal state color for this widget.
     */
    void setStateSelectedColor(@ColorInt int stateSelectedColorColor);
}
