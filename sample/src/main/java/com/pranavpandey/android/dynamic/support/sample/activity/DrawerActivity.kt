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

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
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

    /**
     * Selected menu item id.
     */
    private var mDrawerItemId: Int = 0

    /**
     * true if the menu item action is pending.
     */
    private var mDrawerItemSelected: Boolean = false

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

        // Set floating action button to view the sources on GitHub.
//        setFAB(R.drawable.ads_ic_social_github, fabVisibility) {
//            DynamicLinkUtils.viewUrl(this@DrawerActivity, Constants.URL_GITHUB)
//        }

        // Set extended floating action button to view the sources on GitHub.
        setExtendedFAB(R.drawable.ads_ic_social_github,
                R.string.ads_license_sources, fabVisibility) {
            DynamicLinkUtils.viewUrl(this@DrawerActivity, Constants.URL_GITHUB)
        }

        if (!isPersistentDrawer) {
            drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

                override fun onDrawerOpened(drawerView: View) {}

                override fun onDrawerClosed(drawerView: View) {
                    if (mDrawerItemSelected) {
                        mDrawerItemSelected = false
                        selectMenu(mDrawerItemId)
                    }
                }

                override fun onDrawerStateChanged(newState: Int) {}
            })
        }

        // Show tutorial on first launch.
        if (AppController.instance.isFirstLaunch) {
            AppController.instance.isFirstLaunch = false
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDrawerItemId = item.itemId;

        if (isPersistentDrawer) {
            selectMenu(mDrawerItemId);
        } else {
            mDrawerItemSelected = true;
        }

        closeDrawers();
        return true;
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
            R.id.nav_rate -> DynamicLinkUtils.rateApp(this)
            R.id.nav_share -> DynamicLinkUtils.share(this, null,
                    String.format(getString(R.string.ads_format_next_line),
                            getString(R.string.app_share), Constants.URL_GITHUB))
        }
    }
}
