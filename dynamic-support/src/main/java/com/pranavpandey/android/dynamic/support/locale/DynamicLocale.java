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

package com.pranavpandey.android.dynamic.support.locale;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

/**
 * An interface to implement the dynamic locale changes throughout the app.
 */
public interface DynamicLocale {

    /**
     * @return The array of supported locales.
     *
     * @see androidx.core.os.ConfigurationCompat#getLocales(Configuration)
     */
    @Nullable String[] getSupportedLocales();

    /**
     * @return The default locale to be used if no dynamic locale support is provided.
     *
     * @param context The context to get configuration.
     *
     * @see #getLocale()
     */
    @NonNull Locale getDefaultLocale(@NonNull Context context);

    /**
     * @return The locale to be applied.
     *
     * @see Locale
     */
    @Nullable Locale getLocale();

    /**
     * Apply the locale according to the configuration.
     *
     * @param context The context to set the locale.
     *
     * @return The modified context after applying the locale.
     */
    @NonNull Context setLocale(@NonNull Context context);
}