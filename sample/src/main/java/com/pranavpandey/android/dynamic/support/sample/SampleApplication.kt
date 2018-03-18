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

package com.pranavpandey.android.dynamic.support.sample

import android.content.SharedPreferences
import android.support.annotation.StyleRes
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import java.util.*

/**
 * Sample application extending the [DynamicApplication],
 * it must be done to initialize the base components of dynamic
 * support library.
 *
 * This must be registered in the manifest using `name`
 * attribute of the `application` tag.
 */
class SampleApplication : DynamicApplication() {

    override fun onInitialize() {
        // Do any startup work here like initializing the other
        // libraries, analytics, etc.
        SampleController.initializeInstance(this)
    }

    @StyleRes override fun getThemeRes(): Int {
        // Return application theme to be applied.
        return SampleTheme.appStyle
    }

    override fun onCustomiseTheme() {
        // Customise application theme after applying the base style.
        SampleTheme.setApplicationTheme(context)
    }

    override fun onNavigationBarThemeChange() {
        // TODO: Do any customisations on navigation bar theme change.
    }

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        // Update themes on shared preferences change.
        when (key) {
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR ->
                if (!DynamicResourceUtils.isNight() && SampleTheme.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChange(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR ->
                if (DynamicResourceUtils.isNight() && SampleTheme.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChange(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT ->
                DynamicTheme.getInstance().onDynamicChange(false, true)
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME ->
                DynamicTheme.getInstance().onNavigationBarThemeChange()
        }
    }
}
