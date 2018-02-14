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

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets with a normal and checked state
 * according to the {@link DynamicTheme}.
 */
public interface DynamicStateWidget extends DynamicWidget {

    /**
     * @return The normal state color type applied to this view.
     *
     * @see DynamicColorType
     */
    @DynamicColorType int getStateNormalColorType();

    /**
     * Set the normal state color type to this view.
     *
     * @param stateNormalColorType Normal state color type for this view.
     *
     * @see DynamicColorType
     */
    void setStateNormalColorType(@DynamicColorType int stateNormalColorType);

    /**
     * @return The value of normal state color applied to this view.
     */
    @ColorInt int getStateNormalColor();

    /**
     * Set the value of normal state color for this view.
     *
     * @param stateNormalColorColor Normal state color for this view.
     */
    void setStateNormalColor(@ColorInt int stateNormalColorColor);
}
