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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.activity.TutorialActivity;
import com.pranavpandey.android.dynamic.support.setting.DynamicScreenPreference;
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils;

/**
 * Home fragment to show some of the features of dynamic-support
 * library by using {@link DynamicFragment}.
 */
public class HomeFragment extends DynamicFragment {

    /**
     * @return The new instance of {@link HomeFragment}.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected CharSequence getSubtitle() {
        // Set subtitle for the app compat activity.
        return getString(R.string.ads_nav_home);
    }

    @Override
    protected int setNavigationViewCheckedItem() {
        // Select navigation menu item.
        return R.id.nav_home;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Do not scroll toolbar for this fragment.
        getDynamicActivity().setToolbarLayoutFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);

        // Set subtitle for the dynamic item view.
        ((DynamicItemView) view.findViewById(R.id.item_gradle)).setSubtitle(
                String.format(getString(R.string.format_version),
                DynamicPackageUtils.getAppVersion(getContext())));

        // Set on preference click listeners.
        ((DynamicScreenPreference) view.findViewById(R.id.pref_tutorial))
                .setOnPreferenceClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), TutorialActivity.class));
                    }
                });
    }
}
