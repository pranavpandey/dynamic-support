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

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets with corner radius support which can be used to modify
 * it at runtime according to the {@link DynamicTheme}.
 */
public interface DynamicCornerWidget<T> {

    /**
     * Set the corner radius for this widget.
     *
     * @param cornerRadius The corner radius to be set.
     */
    void setCorner(T cornerRadius);

    /**
     * Returns the corner radius used by this widget.
     *
     * @return The corner radius used by this widget.
     */
    T getCorner();
}
