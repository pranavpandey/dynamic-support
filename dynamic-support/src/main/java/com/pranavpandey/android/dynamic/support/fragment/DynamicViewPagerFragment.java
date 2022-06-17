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

package com.pranavpandey.android.dynamic.support.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.fragment.listener.DynamicOnPageChangeListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicViewPagerCallback;

/**
 * An abstract {@link ViewPager} fragment to display multiple fragments inside the view pager
 * along with the tabs.
 * <p>Extend this fragment and implement the necessary methods to use it inside an activity.
 */
public abstract class DynamicViewPagerFragment extends DynamicFragment
        implements DynamicViewPagerCallback {

    /**
     * Fragment argument key to set the initial view pager page.
     */
    public static String ADS_ARGS_VIEW_PAGER_PAGE = "ads_args_view_pager_page";

    /**
     * View pager used by this fragment.
     */
    private ViewPager mViewPager;

    /**
     * Tab layout used by this fragment.
     */
    private TabLayout mTabLayout;

    @Override
    public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.ads_view_pager);

        if (getActivity() == null) {
            return;
        }

        mViewPager.setOffscreenPageLimit(getItemCount());
        mViewPager.addOnPageChangeListener(
                new DynamicOnPageChangeListener(getChildFragmentManager()));
        mViewPager.setAdapter(new ViewPagerAdapter(
                getChildFragmentManager(), this));
        Dynamic.addHeader(getActivity(), R.layout.ads_tabs,
                true, getSavedInstanceState() == null);

        if (getSavedInstanceState() == null && getArguments() != null
                && requireArguments().containsKey(ADS_ARGS_VIEW_PAGER_PAGE)) {
            setPage(requireArguments().getInt(ADS_ARGS_VIEW_PAGER_PAGE));
        }
    }

    @Override
    public void onAddActivityHeader(@Nullable View view) {
        super.onAddActivityHeader(view);

        if (view == null) {
            return;
        }

        mTabLayout = view.findViewById(R.id.ads_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Get the tab layout used by this fragment.
     *
     * @return The tab layout used by this fragment.
     */
    public @Nullable TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * Get the view pager used by this fragment.
     *
     * @return The view pager used by this fragment.
     */
    public @Nullable ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * Returns the currently selected view pager page or position.
     *
     * @return The currently selected view pager page or position.
     */
    public int getCurrentPage() {
        if (mViewPager == null) {
            return ViewPager.NO_ID;
        }

        return mViewPager.getCurrentItem();
    }

    /**
     * Set the current page or position for the view pager.
     *
     * @param page The current position for the view pager.
     */
    public void setPage(final int page) {
        if (mViewPager == null) {
            return;
        }

        mViewPager.setCurrentItem(page);
    }

    /**
     * View pager adapter to display the supplied fragments with tab titles.
     */
    static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * Dynamic view pager callback to get titles and fragments.
         */
        private final DynamicViewPagerCallback viewPagerCallback;

        /**
         * Constructor to initialize an object of this class.
         *
         * @param fragmentManager The fragment manager for this adapter.
         * @param viewPagerCallback The view pager callback to return the data.
         */
        ViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                @NonNull DynamicViewPagerCallback viewPagerCallback) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            this.viewPagerCallback = viewPagerCallback;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return viewPagerCallback.getTitle(position);
        }

        @Override
        public @NonNull Fragment getItem(int position) {
            return viewPagerCallback.createFragment(position);
        }

        @Override
        public int getCount() {
            return viewPagerCallback.getItemCount();
        }
    }
}
