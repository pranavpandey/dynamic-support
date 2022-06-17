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
import com.pranavpandey.android.dynamic.support.activity.DynamicNavigationActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.fragment.AboutFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.SettingsFragment
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils

/**
 * Implementing a responsive navigation bar by using [DynamicNavigationActivity].
 */
class NavigationBarActivity : DynamicNavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set app bar title.
        setTitle(R.string.app_name)

        // TODO: Inflate menu for the navigation bar view.
        setNavigationBarViewMenu(R.menu.menu_navigation_bar)

        // Set floating action button to view sources on GitHub.
        setFAB(R.drawable.ads_ic_social_github, fabVisibility) {
            DynamicLinkUtils.viewUrl(this@NavigationBarActivity, Constants.URL_GITHUB)
        }

        // Select home fragment.
        if (contentFragment == null) {
            onNavigationItemSelected(R.id.nav_home, false)
        }
    }

    // TODO: Set a navigation item selected listener for the navigation bar view.
    override fun onNavigationItemSelected(itemId: Int, restore: Boolean) {
        super.onNavigationItemSelected(itemId, restore)

        when (itemId) {
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
    }
}
