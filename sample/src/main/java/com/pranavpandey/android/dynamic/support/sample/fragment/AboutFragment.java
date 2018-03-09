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

package com.pranavpandey.android.dynamic.support.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.Constants;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * About fragment to show app info and licences by using
 * {@link DynamicViewPagerFragment}.
 */
public class AboutFragment extends DynamicViewPagerFragment {

    /**
     * @return The new instance of {@link AboutFragment}.
     *
     * @param page The default selected page.
     */
    public static Fragment newInstance(int page) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putInt(ADS_ARGS_VIEW_PAGER_PAGE, page);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected CharSequence getSubtitle() {
        // Set subtitle for the app compat activity.
        return String.format(getString(R.string.ads_format_version),
                DynamicPackageUtils.getAppVersion(getContext()));
    }

    @Override
    protected int setNavigationViewCheckedItem() {
        // Select navigation menu item.
        return R.id.nav_about;
    }

    @Override
    protected List<String> getTitles() {
        // Initialize an empty string array for tab titles.
        List<String> titles = new ArrayList<>();

        // TODO: Add tab titles.
        titles.add(getString(R.string.ads_menu_info));
        titles.add(getString(R.string.ads_licenses));

        // Return all the added tab titles.
        return titles;
    }

    @Override
    protected List<Fragment> getPages() {
        // Initialize an empty fragment array for view pages pages.
        List<Fragment> pages = new ArrayList<>();

        // TODO: Add view pager fragments.
        pages.add(AppInfoFragment.newInstance());
        pages.add(LicensesFragment.newInstance());

        // Return all the added fragments.
        return pages;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Enable app bar options menu.
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Scroll toolbar for this fragment.
        getDynamicActivity().setToolbarLayoutFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        // Select current page from the bundle arguments.
        if (getArguments() != null && getArguments().containsKey(ADS_ARGS_VIEW_PAGER_PAGE)) {
            setPage(getArguments().getInt(ADS_ARGS_VIEW_PAGER_PAGE));
        }
    }

    @Override
    public void onCreateOptionsMenu(@Nullable Menu menu, @Nullable MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // TODO: Inflate menu for this fragment.
        inflater.inflate(R.menu.ads_menu_about, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case R.id.ads_menu_apps:
                    DynamicLinkUtils.moreApps(getContext(), Constants.ME);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Disable options menu on destroy.
        setHasOptionsMenu(false);

        // Remove tab layout from the header.
        getDynamicActivity().addHeader(null, true);
    }
}
