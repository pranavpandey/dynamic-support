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
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPager2Fragment
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment
import com.pranavpandey.android.dynamic.support.sample.R


/**
 * Settings fragment to show app settings and widgets by using [DynamicViewPagerFragment].
 */
class SettingsFragment : DynamicViewPager2Fragment() {

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @param page The default selected page.
         *
         * @return The new instance of [SettingsFragment].
         */
        fun newInstance(page: Int): androidx.fragment.app.Fragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putInt(DynamicViewPagerFragment.ADS_ARGS_VIEW_PAGER_PAGE, page)
            fragment.arguments = args

            return fragment
        }
    }

    override fun getSubtitle(): CharSequence? {
        // Set subtitle for the app compat activity.
        return getString(R.string.ads_nav_settings)
    }

    override fun getBottomNavigationViewId(): Int {
        // Return the bottom navigation view id.
        return R.id.bottom_navigation
    }

    override fun getCheckedMenuItemId(): Int {
        // Select navigation menu item.
        return R.id.nav_settings
    }

    override fun getTitle(position: Int): String? {
        // TODO: Return tab titles.
        return when (position) {
            1 -> getString(R.string.ads_widgets)
            else -> getString(R.string.ads_app)
        }
    }

    override fun createFragment(position: Int): Fragment {
        // TODO: Return view pager fragments.
        return when (position) {
            1 -> WidgetsFragment.newInstance()
            else -> AppSettingsFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        // TODO: Return item count.
        return 2
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Scroll toolbar for this fragment.
        dynamicActivity.setToolbarLayoutFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
    }
}
