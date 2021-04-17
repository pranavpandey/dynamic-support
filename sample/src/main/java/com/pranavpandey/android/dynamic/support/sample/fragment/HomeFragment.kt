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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.activity.BottomNavigationActivity
import com.pranavpandey.android.dynamic.support.sample.activity.CollapsingAppBarActivity
import com.pranavpandey.android.dynamic.support.sample.activity.TutorialActivity
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.setting.base.DynamicScreenPreference
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils

/**
 * Home fragment to show some of the features of dynamic-support library by
 * using [DynamicFragment].
 */
class HomeFragment : DynamicFragment() {

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @return The new instance of [HomeFragment].
         */
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getSubtitle(): CharSequence? {
        // Set subtitle for the app compat activity.
        return getString(R.string.ads_nav_home)
    }

    override fun getBottomNavigationViewId(): Int {
        // Return the bottom navigation view id.
        return R.id.bottom_navigation
    }

    override fun getCheckedMenuItemId(): Int {
        // Select navigation menu item.
        return R.id.nav_home
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Do not scroll toolbar for this fragment.
        dynamicActivity.setToolbarLayoutFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP)

        // Remove tab layout from the header.
        dynamicActivity.addHeader(null, true)

        // Set subtitle for the dynamic item view.
        (view.findViewById<View>(R.id.item_gradle) as DynamicItemView).subtitle =
                String.format(getString(R.string.format_version),
                DynamicPackageUtils.getAppVersion(requireContext()))

        // Set on click listener for the dynamic item view.
        (view.findViewById<View>(R.id.item_rotation) as DynamicItemView)
                .setOnClickListener {
                    // View an app in Google Play by supplying the package name.
                    DynamicLinkUtils.viewInGooglePlay(requireContext(), Constants.PACKAGE_ROTATION)
                }

        // Set on click listener for the dynamic item view.
        (view.findViewById<View>(R.id.item_everyday) as DynamicItemView)
                .setOnClickListener {
                    // View an app in Google Play by supplying the package name.
                    DynamicLinkUtils.viewInGooglePlay(requireContext(), Constants.PACKAGE_EVERYDAY)
                }

        // Set on click listener for the dynamic item view.
        (view.findViewById<View>(R.id.item_palettes) as DynamicItemView)
                .setOnClickListener {
                    // View an app in Google Play by supplying the package name.
                    DynamicLinkUtils.viewInGooglePlay(requireContext(), Constants.PACKAGE_PALETTES)
                }

        // Set on click listener for the dynamic item view.
        (view.findViewById<View>(R.id.item_zerocors) as DynamicItemView)
                .setOnClickListener {
                    // View an app in Google Play by supplying the package name.
                    DynamicLinkUtils.viewInGooglePlay(requireContext(), Constants.PACKAGE_ZEROCROS)
                }

        // Set on preference click listeners.
        // Start tutorial activity.
        (view.findViewById<View>(R.id.pref_tutorial)
                as DynamicScreenPreference).onPreferenceClickListener =
                View.OnClickListener {
                    startActivity(Intent(activity, TutorialActivity::class.java))
                }

        // Start collapsing app bar activity.
        (view.findViewById<View>(R.id.pref_collapsing_app_bar)
                as DynamicScreenPreference).onPreferenceClickListener =
                View.OnClickListener {
                    startActivity(Intent(activity, CollapsingAppBarActivity::class.java))
                }

        if (activity is DynamicDrawerActivity ) {
            // Start bottom navigation activity.
            (view.findViewById<View>(R.id.pref_bottom_navigation)
                    as DynamicScreenPreference).onPreferenceClickListener =
                    View.OnClickListener {
                        startActivity(Intent(activity, BottomNavigationActivity::class.java))
                    }
        } else {
            // Hide bottom navigation activity.
            view.findViewById<View>(R.id.pref_bottom_navigation).visibility = View.GONE
        }

    }
}
