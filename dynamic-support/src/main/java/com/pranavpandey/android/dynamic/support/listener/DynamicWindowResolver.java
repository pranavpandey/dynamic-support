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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.ColorInt;

/**
 * An interface to get the various callbacks related to the window color generation like
 * status bar or navigation bar color.
 */
public interface DynamicWindowResolver {

    /**
     * Returns color for the status and navigation bars.
     *
     * @param optionalColor The optional color to provide some additional information like
     *                      the background color.
     *
     * @return The color for the status and navigation bars.
     */
    @ColorInt int getSystemUIColor(@ColorInt int optionalColor);
}
