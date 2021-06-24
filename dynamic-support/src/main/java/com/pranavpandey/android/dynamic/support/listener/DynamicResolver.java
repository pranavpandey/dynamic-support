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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.theme.DynamicColors;
import com.pranavpandey.android.dynamic.theme.Theme;

import java.util.Date;

/**
 * An interface to resolve dynamic theme according to the various conditions.
 */
public interface DynamicResolver {

    /**
     * Checks whether the system night mode is enabled.
     *
     * @return {@code true} if the system night mode is enabled.
     */
    boolean isSystemNightMode();

    /**
     * Resolves system color according to the API level and night mode.
     *
     * @param isNight {@code true} to resolve the night color.
     *
     * @return The system UI color according to the Android version and night mode.
     */
    @ColorInt int resolveSystemColor(boolean isNight);

    /**
     * Checks for the night time.
     * <p>Useful to apply themes based on the day and night.
     *
     * @return {@code true} if it is night.
     */
    boolean isNight();

    /**
     * Checks for the night time according to the supplied value.
     * <p>Useful to apply themes based on the day and night.
     *
     * @param theme The integer value of the theme.
     *
     * @return {@code true} if it is night.
     */
    boolean isNight(@Theme int theme);

    /**
     * Checks for the night time according to the supplied value.
     * <p>Useful to apply themes based on the day and night.
     *
     * @param theme The string value of the theme.
     *
     * @return {@code true} if it is night.
     */
    boolean isNight(@Theme.ToString String theme);

    /**
     * Returns start time for the night theme.
     *
     * @return The start time for the night theme.
     */
    @NonNull Date getNightTimeStart();

    /**
     * Returns end time for the night theme.
     *
     * @return The end time for the night theme.
     */
    @NonNull Date getNightTimeEnd();

    /**
     * Resolves night theme according to the selected implementation.
     *
     * @param appTheme The app theme to resolve the auto night theme.
     * @param implementation The implementation for the night theme.
     *
     * @return {@code true} if the night theme is enabled according to the selected
     *         implementation.
     */
    boolean resolveNightTheme(@Theme int appTheme, @Theme.Night int implementation);

    /**
     * Resolves night theme according to the selected implementation.
     *
     * @param appTheme The app theme to resolve the auto night theme.
     * @param implementation The implementation for the night theme.
     *
     * @return {@code true} if the night theme is enabled according to the selected
     *         implementation.
     */
    boolean resolveNightTheme(@Theme.ToString String appTheme,
            @Theme.Night.ToString String implementation);

    /**
     * Get the dynamic colors used by this resolver.
     *
     * @return The dynamic colors used by this resolver.
     */
    @NonNull DynamicColors getDynamicColors();
}
