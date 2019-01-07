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

import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.theme.Theme;

/**
 * Interface to create base widget and to do the initialization work.
 */
public interface BaseWidget {

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    void loadFromAttributes(@Nullable AttributeSet attrs);

    /**
     * Initialize this widget by setting color type. If it is background aware then, background
     * color will also taken into account while setting the color filter.
     *
     * @see Theme.ColorType
     */
    void initialize();
}
