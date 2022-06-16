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

package com.pranavpandey.android.dynamic.support.sample.controller

import android.app.Application
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences

/**
 * Singleton class to perform common operations throughout the app.
 */
class AppController {

    /**
     * Application context used by this instance.
     */
    var context: Application? = null

    /**
     * Getter and setter for the [Constants.PREF_FIRST_LAUNCH] shared preference.
     */
    var isFirstLaunch: Boolean
        get() = DynamicPreferences.getInstance().load(
                Constants.PREF_FIRST_LAUNCH,
                Constants.PREF_FIRST_LAUNCH_DEFAULT)
        set(firstLaunch) = DynamicPreferences.getInstance().save(
                Constants.PREF_FIRST_LAUNCH, firstLaunch)

    /**
     * true` to apply the navigation bar theme.
     */
    val isThemeNavigationBar: Boolean
        get() = DynamicPreferences.getInstance().load(
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME,
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT)

    /**
     * true` to apply the app shortcuts theme.
     */
    val isThemeAppShortcuts: Boolean
        get() = DynamicPreferences.getInstance().load(
                Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME,
                Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME_DEFAULT)

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * Use [.initializeInstance] instead.
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
         * Singleton instance of [AppController].
         */
        private var sInstance: AppController? = null

        /**
         * Initialize this controller when application starts.
         * Must be initialize once.
         *
         * @param context The context to retrieve the resources.
         */
        @Synchronized
        fun initializeInstance(context: Application?) {
            if (context == null) {
                throw NullPointerException("Context should not be null")
            }

            if (sInstance == null) {
                sInstance = AppController(context)
            }
        }

        /**
         * Get instance to access public methods.
         * Must be called before accessing methods.
         *
         * @return [.sInstance] Singleton [AppController] instance.
         */
        val instance: AppController
            @Synchronized get() {
                if (sInstance == null) {
                    throw IllegalStateException(AppController::class.java.simpleName
                            + " is not initialized, call initializeInstance(..) method first.")
                }

                return sInstance as AppController
            }
    }
}
