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
 * Interface to create dynamic widgets with floating surface options which can be used to adjust
 * their elevation, background, stroke, etc. according to the {@link DynamicTheme}.
 */
public interface DynamicFloatingWidget {

    /**
     * Returns whether this widget a floating view.
     *
     * @return {@code true} if this view is a floating view.
     */
    boolean isFloatingView();

    /**
     * Set the floating view option for this widget.
     *
     * @param floatingView {@code true} to enable floating view.
     */
    void setFloatingView(boolean floatingView);
}
