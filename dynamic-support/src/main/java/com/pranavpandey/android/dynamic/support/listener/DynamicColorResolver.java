/*
 * Copyright 2019 Pranav Pandey
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
import androidx.annotation.Nullable;

/**
 * An interface to get the various callbacks related to the automatic color generation.
 */
public interface DynamicColorResolver {

    /**
     * Get default color for this resolver.
     *
     * @param tag The optional tag to differentiate between various callbacks.
     *
     * @return The default color.
     */
    int getDefaultColor(@Nullable String tag);

    /**
     * Get automatically generated color for this resolver.
     *
     * @param tag The optional tag to differentiate between various callbacks.
     *
     * @return The automatically generated color.
     */
    @ColorInt int getAutoColor(@Nullable String tag);
}