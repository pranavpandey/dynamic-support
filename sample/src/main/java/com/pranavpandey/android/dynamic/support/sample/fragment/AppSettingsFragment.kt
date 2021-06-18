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

package com.pranavpandey.android.dynamic.support.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

/**
 * App Settings fragment to control theme settings by using [DynamicFragment].
 */
class AppSettingsFragment : DynamicFragment() {

    /**
     * Dynamic color preference for the day theme.
     */
    private var mAppThemeDay: DynamicColorPreference? = null

    /**
     * Dynamic color preference for the night theme.
     */
    private var mAppThemeNight: DynamicColorPreference? = null

    /**
     * Dynamic color preference for the surface color.
     */
    private var mAppThemeColorSurface: DynamicColorPreference? = null

    /**
     * Dynamic color preference for the primary color.
     */
    private var mAppThemeColorPrimary: DynamicColorPreference? = null

    /**
     * Dynamic color preference for the accent color.
     */
    private var mAppThemeColorAccent: DynamicColorPreference? = null

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @return The new instance of [AppSettingsFragment].
         */
        fun newInstance(): AppSettingsFragment {
            return AppSettingsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAppThemeDay = view.findViewById(R.id.pref_app_theme_day)
        mAppThemeNight = view.findViewById(R.id.pref_app_theme_night)
        mAppThemeColorSurface = view.findViewById(R.id.pref_app_theme_color_surface)
        mAppThemeColorPrimary = view.findViewById(R.id.pref_app_theme_color_primary)
        mAppThemeColorAccent = view.findViewById(R.id.pref_app_theme_color_accent)

        // Set the dynamic color resolvers to resolve the default and auto color.

        mAppThemeDay!!.dynamicColorResolver = object : DynamicColorResolver {
            override fun getDefaultColor(@Nullable tag: String?): Int {
                return Constants.APP_THEME_DAY_COLOR
            }

            override fun getAutoColor(@Nullable tag: String?): Int {
                return DynamicTheme.getInstance().get().backgroundColor
            }
        }

        mAppThemeNight!!.dynamicColorResolver = object : DynamicColorResolver {
            override fun getDefaultColor(@Nullable tag: String?): Int {
                return Constants.APP_THEME_NIGHT_COLOR
            }

            override fun getAutoColor(@Nullable tag: String?): Int {
                return DynamicTheme.getInstance().get().backgroundColor
            }
        }

        mAppThemeColorSurface!!.dynamicColorResolver = object : DynamicColorResolver {
            override fun getDefaultColor(@Nullable tag: String?): Int {
                return Constants.APP_THEME_COLOR_SURFACE
            }

            override fun getAutoColor(@Nullable tag: String?): Int {
                return DynamicTheme.getInstance().get().surfaceColor
            }
        }

        mAppThemeColorPrimary!!.dynamicColorResolver = object : DynamicColorResolver {
            override fun getDefaultColor(@Nullable tag: String?): Int {
                return Constants.APP_THEME_COLOR_PRIMARY
            }

            override fun getAutoColor(@Nullable tag: String?): Int {
                return DynamicTheme.getInstance().get().primaryColor
            }
        }

        mAppThemeColorAccent!!.dynamicColorResolver = object : DynamicColorResolver {
            override fun getDefaultColor(@Nullable tag: String?): Int {
                return Constants.APP_THEME_COLOR_ACCENT
            }

            override fun getAutoColor(@Nullable tag: String?): Int {
                return DynamicTheme.getInstance().get().accentColor
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Update preferences on resume.
        updatePreferences()
    }

    /**
     * Enable or disable day and night theme according to the app theme.
     */
    private fun updatePreferences() {
        if (ThemeController.isAutoTheme) {
            mAppThemeDay!!.isEnabled = true
            mAppThemeNight!!.isEnabled = true
        } else {
            mAppThemeDay!!.isEnabled = false
            mAppThemeNight!!.isEnabled = false
        }
    }
}
