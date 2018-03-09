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

import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment;
import com.pranavpandey.android.dynamic.support.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings fragment to show app settings and widgets by using
 * {@link DynamicViewPagerFragment}.
 */
public class SettingsFragment extends DynamicViewPagerFragment {

    /**
     * @return The new instance of {@link SettingsFragment}.
     *
     * @param page The default selected page.
     */
    public static Fragment newInstance(int page) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ADS_ARGS_VIEW_PAGER_PAGE, page);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected CharSequence getSubtitle() {
        // Set subtitle for the app compat activity.
        return getString(R.string.ads_nav_settings);
    }

    @Override
    protected int setNavigationViewCheckedItem() {
        // Select navigation menu item.
        return R.id.nav_settings;
    }

    @Override
    protected List<String> getTitles() {
        // Initialize an empty string array for tab titles.
        List<String> titles = new ArrayList<>();

        // Add tab titles.
        titles.add(getString(R.string.ads_app));
        titles.add(getString(R.string.ads_widgets));

        // Return all the added tab titles.
        return titles;
    }

    @Override
    protected List<Fragment> getPages() {
        // Initialize an empty fragment array for view pages pages.
        List<Fragment> pages = new ArrayList<>();

        // TODO: Add view pager fragments.
        pages.add(AppSettingsFragment.newInstance());
        pages.add(WidgetsFragment.newInstance());

        // Return all the added fragments.
        return pages;
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
    public void onDestroy() {
        super.onDestroy();

        // Remove tab layout from the header.
        getDynamicActivity().addHeader(null, true);
    }
}
