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

package com.pranavpandey.android.dynamic.support.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils

/**
 * App Settings fragment to control theme settings by using
 * [DynamicFragment].
 */
class AppSettingsFragment : DynamicFragment() {

    /**
     * Dynamic color preference for day theme.
     */
    private var mAppThemeDay: DynamicColorPreference? = null

    /**
     * Dynamic color preference for night theme.
     */
    private var mAppThemeNight: DynamicColorPreference? = null

    companion object {

        /**
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

        // Hide navigation bar theme if not supported by the device.
        if (!DynamicWindowUtils.isNavigationBarThemeSupported(context!!)) {
            view.findViewById<View>(R.id.pref_navigation_bar_theme).visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        // Update preferences on resume.
        updatePreferences()
    }

    /**
     * Enable or disable day and night theme according to
     * the app theme.
     */
    private fun updatePreferences() {
        if (SampleTheme.isAutoTheme) {
            mAppThemeDay!!.isEnabled = true
            mAppThemeNight!!.isEnabled = true
        } else {
            mAppThemeDay!!.isEnabled = false
            mAppThemeNight!!.isEnabled = false
        }
    }
}
