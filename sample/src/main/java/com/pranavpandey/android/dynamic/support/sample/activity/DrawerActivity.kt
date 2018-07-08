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

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.IdRes
import android.support.annotation.StyleRes
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme
import com.pranavpandey.android.dynamic.support.sample.fragment.AboutFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment
import com.pranavpandey.android.dynamic.support.sample.fragment.SettingsFragment
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils
import java.util.*

/**
 * Implementing a navigation drawer by using [DynamicDrawerActivity].
 */
class DrawerActivity : DynamicDrawerActivity() {

    companion object {

        /**
         * Navigation drawer delay for smoother open and close events.
         */
        private const val NAV_DRAWER_LAUNCH_DELAY = 250
    }

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun getContentRes(): Int {
        // Returning default dynamic support drawer layout.
        return ADS_DEFAULT_LAYOUT_RES
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set app bar title.
        setTitle(R.string.app_name)

        // Set navigation drawer header and menu.
        setNavHeaderIcon(R.drawable.ads_ic_extension)
        setNavHeaderTitle(R.string.app_name)
        setNavHeaderSubtitle(R.string.app_subtitle)
        setNavigationViewMenu(R.menu.menu_drawer)

        // Set home fragment as default fragment.
        if (contentFragment == null) {
            switchFragment(HomeFragment.newInstance(), false)
        }

        // Set floating action button to view sources on GitHub.
        setFAB(R.drawable.ads_ic_social_github, fabVisibility) {
            DynamicLinkUtils.viewUrl(this@DrawerActivity, Constants.URL_GITHUB)
        }

        // Show tutorial on first launch.
        if (SampleController.instance.isFirstLaunch) {
            SampleController.instance.isFirstLaunch = false
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Using handler for smoother open and close events.
        Handler().postDelayed({ selectMenu(item.itemId) }, NAV_DRAWER_LAUNCH_DELAY.toLong())

        closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Perform operations according to the menu id.
     */
    private fun selectMenu(@IdRes menuId: Int) {
        when (menuId) {
            R.id.nav_home -> if (contentFragment !is HomeFragment) {
                switchFragment(HomeFragment.newInstance(), false)
            }
            R.id.nav_settings -> if (contentFragment !is SettingsFragment) {
                switchFragment(SettingsFragment.newInstance(0), false)
            }
            R.id.nav_about -> if (contentFragment !is AboutFragment) {
                switchFragment(AboutFragment.newInstance(0), false)
            }
            R.id.nav_donate -> DynamicLinkUtils.viewUrl(this, Constants.URL_DONATE)
            R.id.nav_share -> DynamicLinkUtils.shareApp(this, null,
                    String.format(getString(R.string.ads_format_next_line),
                            getString(R.string.app_share), Constants.URL_GITHUB))
        }
    }
}
