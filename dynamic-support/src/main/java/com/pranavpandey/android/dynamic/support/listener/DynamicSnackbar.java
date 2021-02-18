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

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

/**
 * An interface to show themed {@link Snackbar} dynamically.
 */
public interface DynamicSnackbar {

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be the tint background color to blend it smoothly
     * and it will automatically use its tint color for the text and action to provide best
     * visibility.
     *
     * @param text The text to show. Can be formatted text.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    @NonNull Snackbar getSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration);

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be the tint background color to blend it smoothly
     * and it will automatically use its tint color for the text and action to provide best
     * visibility.
     *
     * @param stringRes The string resource for the snackbar.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    @NonNull Snackbar getSnackbar(@StringRes int stringRes, @Snackbar.Duration int duration);

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be the tint background color to blend it smoothly
     * and it will automatically use its tint color for the text and action to provide best
     * visibility.
     *
     * @param text The text to show. Can be formatted text.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    @NonNull Snackbar getSnackbar(@NonNull CharSequence text);

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be the tint background color to blend it smoothly
     * and it will automatically use its tint color for the text and action to provide best
     * visibility.
     *
     * @param stringRes The string resource for the snackbar.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    @NonNull Snackbar getSnackbar(@StringRes int stringRes);
}
