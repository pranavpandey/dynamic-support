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

package com.pranavpandey.android.dynamic.support.sample.controller

import android.app.Application
import android.support.annotation.ColorInt
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences

/**
 * Singleton class to perform common operations throughout
 * the app.
 */
class SampleController {

    /**
     * Application context used by this instance.
     */
    var context: Application? = null

    /**
     * Getter and Setter for the [Constants.PREF_FIRST_LAUNCH]
     * shared preference.
     */
    var isFirstLaunch: Boolean
        get() = DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_FIRST_LAUNCH,
                Constants.PREF_FIRST_LAUNCH_DEFAULT)
        set(firstLaunch) = DynamicPreferences.getInstance().savePrefs(
                Constants.PREF_FIRST_LAUNCH, firstLaunch)

    /**
     * @return The app theme primary color.
     */
    val colorPrimaryApp: Int
        @ColorInt get() = DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT)

    /**
     * @return The app theme accent color.
     */
    val colorAccentApp: Int
        @ColorInt get() = DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT)

    /**
     * @return `true` to apply the navigation bar theme.
     */
    val isThemeNavigationBar: Boolean
        get() = DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME,
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT)

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use [.initializeInstance] instead.
     */
    private constructor() {}

    /**
     * Default constructor to initialize this controller.
     */
    private constructor(context: Application) {
        this.context = context
    }

    companion object {

        /**
         * Singleton instance of [SampleController].
         */
        private var sInstance: SampleController? = null

        /**
         * Initialize this controller when application starts. Must be
         * initialize once.
         *
         * @param context The context to retrieve resources.
         */
        @Synchronized
        fun initializeInstance(context: Application?) {
            if (context == null) {
                throw NullPointerException("Context should not be null")
            }

            if (sInstance == null) {
                sInstance = SampleController(context)
            }
        }

        /**
         * Get instance to access public methods. Must be called before accessing
         * methods.
         *
         * @return [.sInstance] Singleton [SampleController] instance.
         */
        val instance: SampleController
            @Synchronized get() {
                if (sInstance == null) {
                    throw IllegalStateException(SampleController::class.java.simpleName
                            + " is not initialized, call initializeInstance(..) method first.")
                }

                return sInstance as SampleController
            }
    }
}
