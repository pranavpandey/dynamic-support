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

package com.pranavpandey.android.dynamic.support.sample.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPager2Fragment
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.util.DynamicPermissionUtils


/**
 * About fragment to show app info and licences by using [DynamicViewPagerFragment].
 */
class AboutFragment : DynamicViewPager2Fragment() {

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @param page The default selected page.
         *
         * @return The new instance of [AboutFragment].
         */
        fun newInstance(page: Int): androidx.fragment.app.Fragment {
            val fragment = AboutFragment()
            val args = Bundle()
            args.putInt(DynamicViewPagerFragment.ADS_ARGS_VIEW_PAGER_PAGE, page)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Scroll toolbar for this fragment.
        dynamicActivity.setToolbarLayoutFlags(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
    }

    override fun setHasOptionsMenu(): Boolean {
        return true
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateMenu(menu, inflater)
        inflater.inflate(R.menu.ads_menu_info, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ads_menu_info) {
            DynamicPermissionUtils.launchAppInfo(requireContext())
        }
        return super.onMenuItemSelected(item)
    }

    override fun getSubtitle(): CharSequence {
        // Set subtitle for the app compat activity.
        return getString(R.string.ads_about);
    }

    override fun getNavigationViewId(): Int {
        // Return the navigation bar view id.
        return R.id.ads_navigation_bar_view
    }

    override fun getCheckedMenuItemId(): Int {
        // Select navigation menu item.
        return R.id.nav_about
    }

    override fun getTitle(position: Int): String {
        // TODO: Return tab titles.
        return when (position) {
            1 -> getString(R.string.ads_notices)
            else -> getString(R.string.ads_menu_info)
        }
    }

    override fun createFragment(position: Int): Fragment {
        // TODO: Return view pager fragments.
        return when (position) {
            1 -> NoticeFragment.newInstance()
            else -> AppInfoFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        // TODO: Return item count.
        return 2
    }
}
