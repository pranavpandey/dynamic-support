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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;

/**
 * An interface to get the various callbacks related to the dynamic theme.
 *
 * @param <T> The type of the dynamic app theme this resolver will resolve.
 */
public interface DynamicThemeResolver<T extends DynamicAppTheme> {

    /**
     * Returns the default theme string.
     *
     * @param theme The current default dynamic theme string.
     *
     * @return The default theme string.
     */
    @NonNull String getDefaultTheme(@Nullable String theme);

    /**
     * Returns the dynamic theme according to the supplied string.
     *
     * @param theme The dynamic theme string to be converted.
     *
     * @return The dynamic theme according to the supplied string.
     */
    @Nullable T getDynamicTheme(@Nullable String theme);
}
