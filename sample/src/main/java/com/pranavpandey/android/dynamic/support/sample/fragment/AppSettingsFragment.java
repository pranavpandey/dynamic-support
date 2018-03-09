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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference;
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils;

/**
 * App Settings fragment to control theme settings by using
 * {@link DynamicFragment}.
 */
public class AppSettingsFragment extends DynamicFragment {

    /**
     * Dynamic color preference for day theme.
     */
    private DynamicColorPreference mAppThemeDay;

    /**
     * Dynamic color preference for night theme.
     */
    private DynamicColorPreference mAppThemeNight;

    /**
     * @return The new instance of {@link AppSettingsFragment}.
     */
    public static AppSettingsFragment newInstance() {
        return new AppSettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAppThemeDay = view.findViewById(R.id.pref_app_theme_day);
        mAppThemeNight = view.findViewById(R.id.pref_app_theme_night);

        // Hide navigation bar theme if not supported by the device.
        if (!DynamicWindowUtils.isNavigationBarThemeSupported(getContext())) {
            view.findViewById(R.id.pref_navigation_bar_theme).setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();

        // Update preferences on resume.
        updatePreferences();
    }

    /**
     * Enable or disable day and night theme according to
     * the app theme.
     */
    private void updatePreferences() {
        if (SampleTheme.isAutoTheme()) {
            mAppThemeDay.setEnabled(true);
            mAppThemeNight.setEnabled(true);
        } else {
            mAppThemeDay.setEnabled(false);
            mAppThemeNight.setEnabled(false);
        }
    }
}
