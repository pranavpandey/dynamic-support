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

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets with tint options which can be used to tint their
 * background, etc. according to the {@link DynamicTheme}.
 */
public interface DynamicTintWidget {

    /**
     * Returns whether to tint the background.
     *
     * @return {@code true} to tint background according to the widget color.
     */
    boolean isTintBackground();

    /**
     * Set the background tinting option for this widget.
     *
     * @param tintBackground {@code true} to tint background according to the widget color.
     */
    void setTintBackground(boolean tintBackground);

    /**
     * Returns whether the borderless style is applied to this widget.
     *
     * @return {@code true} if the borderless style is applied to this widget.
     */
    boolean isStyleBorderless();

    /**
     * Sets whether the borderless style is applied to this widget.
     *
     * @param styleBorderless {@code true} if the borderless style is applied to this widget.
     */
    void setStyleBorderless(boolean styleBorderless);
}
