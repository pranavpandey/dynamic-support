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

package com.pranavpandey.android.dynamic.support.sample.activity

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.fragment.AboutFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.SettingsFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils
import java.util.*

/**
 * Implementing a bottom navigation view by using [DynamicActivity].
 */
class BottomNavigationActivity : DynamicActivity() {

    /**
     * Bottom navigation view used by this activity.
     */
    private var mBottomNavigationView: DynamicBottomNavigationView? = null

    override fun onNavigationBarThemeChanged() {
        super.onNavigationBarThemeChanged()

        // Update bottom navigation view theme on navigation bar theme change.
        themeBottomNavigationView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set app bar title.
        setTitle(R.string.app_name)

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

            // Show floating action button after switching the fragment.
            showFAB()
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
        mBottomNavigationView!!.color = DynamicTheme.getInstance().get().primaryColor
        mBottomNavigationView!!.textColor = DynamicTheme.getInstance().get().accentColor
        frameFooter!!.setBackgroundColor(mBottomNavigationView!!.color)
    }

    override fun onResume() {
        super.onResume()

        // Update bottom navigation view theme on resume.
        themeBottomNavigationView()
    }
}
