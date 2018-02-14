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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.ConfigurationCompat;

import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.util.Locale;

/**
 * Helper class to perform various locale operations.
 */
public class DynamicLocaleUtils {

    public static final String ADS_LOCALE_SYSTEM = "ads_locale_system";

    /**
     * Dynamic locale splitter to separate language, country, etc.
     */
    public static final String ADE_LOCALE_SPLIT = ",";

    /**
     * Get locale from the locale string in the format:
     * {@code language,region}
     *
     * @param locale The locale string to be converted.
     *
     * @return The converted locale from the locale string. Return
     * null for the default locale value.
     *
     * @see #ADS_LOCALE_SYSTEM
     */
    public static @Nullable Locale toLocale(@Nullable String locale) {
        Locale localeWithRegion;
        if (locale == null || locale.equals(DynamicLocaleUtils.ADS_LOCALE_SYSTEM)) {
            localeWithRegion = null;
        } else {
            String localeFormat[] = locale.split(ADE_LOCALE_SPLIT);
            localeWithRegion = new Locale(localeFormat[0]);
            if (localeFormat.length > 1) {
                localeWithRegion = new Locale(localeFormat[0], localeFormat[1]);
            }
        }

        return localeWithRegion;
    }

    /**
     * Get default locale language from the supported locales.
     *
     * @param context The context to get configuration.
     * @param supportedLocales The supported locales.
     *
     * @return The default locale according to the supported locales.
     */
    public static @NonNull Locale getDefaultLocale(@NonNull Context context,
                                                   @Nullable String[] supportedLocales) {
        if (supportedLocales == null) {
            return ConfigurationCompat.getLocales(
                    Resources.getSystem().getConfiguration()).get(0);
        } else {
            return ConfigurationCompat.getLocales(
                    Resources.getSystem().getConfiguration())
                    .getFirstMatch(supportedLocales);
        }
    }

    /**
     * @return The locale after performing safety checks.
     *
     * @param locale The locale to be checked.
     * @param defaultLocale The default locale if current locale does
     *                      not passes checks.
     */
    public static @NonNull Locale getLocale(@Nullable Locale locale,
                                            @NonNull Locale defaultLocale) {
        return locale == null ? defaultLocale : locale;
    }

    /**
     * Set the locale for a given context.
     *
     * @param context The context to set locale
     * @param locale The locale to be used for the context.
     */
    public static @NonNull Context setLocale(
            @NonNull Context context, @Nullable Locale locale) {
        if (locale == null) {
            return context;
        }

        if (DynamicVersionUtils.isJellyBeanMR1()) {
            return updateResources(context, locale);
        }

        return updateResourcesLegacy(context, locale);
    }

    /**
     * Update resources for a given context after setting the
     * locale on {@link Build.VERSION_CODES#JELLY_BEAN_MR1} or
     * above devices.
     *
     * @param context The context to set update resources.
     * @param locale The locale to be used for the context
     *               resources.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static @NonNull Context updateResources(
            @NonNull Context context, @NonNull Locale locale) {
        Locale.setDefault(locale);

        Configuration configuration = new Configuration(
                context.getResources().getConfiguration());
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        context.createConfigurationContext(configuration);

        // Hack to fix the dialog fragment layout issue on configuration
        // change.
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());

        return context;
    }

    /**
     * Update resources for a given context after setting the
     * locale on {@link Build.VERSION_CODES#JELLY_BEAN} or
     * below devices.
     *
     * @param context The context to set update resources.
     * @param locale The locale to be used for the context
     *               resources.
     */
    @SuppressWarnings("deprecation")
    private static @NonNull Context updateResourcesLegacy(
            @NonNull Context context, @NonNull Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        if (DynamicVersionUtils.isJellyBeanMR1()) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}