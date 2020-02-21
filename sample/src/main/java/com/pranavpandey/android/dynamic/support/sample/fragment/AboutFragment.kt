/*
 * Copyright 2020 Pranav Pandey
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
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import java.util.*

/**
 * About fragment to show app info and licences by using [DynamicViewPagerFragment].
 */
class AboutFragment : DynamicViewPagerFragment() {

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

    override fun getSubtitle(): CharSequence? {
        // Set subtitle for the app compat activity.
        return DynamicPackageUtils.getAppVersion(requireContext())
    }

    override fun getBottomNavigationViewId(): Int {
        // Return the bottom navigation view id.
        return R.id.bottom_navigation
    }

    override fun getCheckedMenuItemId(): Int {
        // Select navigation menu item.
        return R.id.nav_about
    }

    override fun getTitles(): List<String> {
        // Initialize an empty string array for tab titles.
        val titles = ArrayList<String>()

        // TODO: Add tab titles.
        titles.add(getString(R.string.ads_menu_info))
        titles.add(getString(R.string.ads_licenses))

        // Return all the added tab titles.
        return titles
    }

    override fun getPages(): List<androidx.fragment.app.Fragment> {
        // Initialize an empty fragment array for view pages pages.
        val pages = ArrayList<androidx.fragment.app.Fragment>()

        // TODO: Add view pager fragments.
        pages.add(AppInfoFragment.newInstance())
        pages.add(LicensesFragment.newInstance())

        // Return all the added fragments.
        return pages
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Scroll toolbar for this fragment.
        dynamicActivity.setToolbarLayoutFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)

        // Select current page from the bundle arguments.
        if (arguments != null && requireArguments().containsKey(
                        DynamicViewPagerFragment.ADS_ARGS_VIEW_PAGER_PAGE)) {
            setPage(requireArguments().getInt(DynamicViewPagerFragment.ADS_ARGS_VIEW_PAGER_PAGE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove tab layout from the header.
        dynamicActivity.addHeader(null, true)
    }
}
