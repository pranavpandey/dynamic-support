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

package com.pranavpandey.android.dynamic.support.sample.activity

import android.os.Bundle
import android.support.annotation.StyleRes
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme
import com.pranavpandey.android.dynamic.support.sample.fragment.AboutFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.SettingsFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils
import java.util.*

/**
 * Implementing a bottom navigation view by using
 * [DynamicDrawerActivity].
 */
class BottomNavigationActivity : DynamicActivity() {

    /**
     * Bottom navigation view used by this activity.
     */
    private var mBottomNavigationView: DynamicBottomNavigationView? = null

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun getContentRes(): Int {
        // Returning default dynamic support drawer layout.
        return DynamicActivity.ADS_DEFAULT_LAYOUT_RES
    }

    @StyleRes
    override fun getThemeRes(): Int {
        // Return activity theme to be applied.
        return SampleTheme.appStyle
    }

    override fun onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        SampleTheme.setLocalTheme(context)
    }

    override fun setNavigationBarTheme(): Boolean {
        // TODO: Return true to apply the navigation bar theme.
        return SampleController.instance.isThemeNavigationBar
    }

    override fun onNavigationBarThemeChange() {
        super.onNavigationBarThemeChange()

        // Update bottom navigation view theme on navigation bar
        // theme change.
        themeBottomNavigationView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set app bar title.
        setTitle(R.string.app_name)

        // Finish this activity on clicking the back navigation button.
        setNavigationClickListener { this@BottomNavigationActivity.finish() }

        // Add bottom navigation view in footer.
        addFooter(R.layout.layout_bottom_navigation, true)
        mBottomNavigationView = findViewById(R.id.bottom_navigation)

        // Apply theme for the bottom navigation view.
        themeBottomNavigationView()

        // TODO: Inflate menu for the bottom navigation view.
        mBottomNavigationView!!.inflateMenu(R.menu.menu_bottom_navigation)

        // TODO: Set a navigation item selected listener for the bottom navigation view.
        mBottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> if (contentFragment !is HomeFragment) {
                    switchFragment(HomeFragment.newInstance(), false)
                }
                R.id.nav_settings -> if (contentFragment !is SettingsFragment) {
                    switchFragment(SettingsFragment.newInstance(0), false)
                }
                R.id.nav_about -> if (contentFragment !is AboutFragment) {
                    switchFragment(AboutFragment.newInstance(0), false)
                }
            }

            true
        }

        // Set floating action button to view sources on GitHub.
        setFAB(R.drawable.ads_ic_social_github, fabVisibility) {
            DynamicLinkUtils.viewUrl(this@BottomNavigationActivity, Constants.URL_GITHUB)
        }

        // Select home fragment.
        if (contentFragment == null) {
            mBottomNavigationView!!.selectedItemId = R.id.nav_home
        }
    }

    /**
     * Apply theme for the bottom navigation view.
     */
    private fun themeBottomNavigationView() {
        if (mBottomNavigationView != null) {
            mBottomNavigationView!!.color = DynamicTheme.getInstance().primaryColor
            mBottomNavigationView!!.textColor = DynamicColorUtils.getTintColor(
                    DynamicTheme.getInstance().primaryColor)
        }
    }

    override fun onResume() {
        super.onResume()

        // Update bottom navigation view theme on resume.
        themeBottomNavigationView()
    }
}
