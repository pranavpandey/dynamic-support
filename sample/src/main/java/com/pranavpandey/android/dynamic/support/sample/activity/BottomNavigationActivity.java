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

package com.pranavpandey.android.dynamic.support.sample.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.Constants;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.sample.fragment.AboutFragment;
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment;
import com.pranavpandey.android.dynamic.support.sample.fragment.SettingsFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

import java.util.Locale;

/**
 * Implementing a bottom navigation view by using
 * {@link DynamicDrawerActivity}.
 */
public class BottomNavigationActivity extends DynamicActivity {

    /**
     * Bottom navigation view used by this activity.
     */
    private DynamicBottomNavigationView mBottomNavigationView;

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    protected int getContentRes() {
        // Returning default dynamic support drawer layout.
        return ADS_DEFAULT_LAYOUT_RES;
    }

    @Override
    protected @StyleRes
    int getThemeRes() {
        // Return activity theme to be applied.
        return SampleTheme.getAppStyle();
    }

    @Override
    protected void onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        SampleTheme.setLocalTheme(getContext());
    }

    @Override
    protected boolean setNavigationBarTheme() {
        // TODO: Return true to apply the navigation bar theme.
        return SampleController.getInstance().isThemeNavigationBar();
    }

    @Override
    public void onNavigationBarThemeChange() {
        super.onNavigationBarThemeChange();

        // Update bottom navigation view theme on navigation bar
        // theme change.
        themeBottomNavigationView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Set app bar title.
        setTitle(R.string.app_name);

        // Finish this activity on clicking the back navigation button.
        setNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationActivity.this.finish();
            }
        });

        // Add bottom navigation view in footer.
        addFooter(R.layout.layout_bottom_navigation, true);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        // Apply theme for the bottom navigation view.
        themeBottomNavigationView();

        // TODO: Inflate menu for the bottom navigation view.
        mBottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation);

        // TODO: Set a navigation item selected listener for the bottom navigation view.
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!(getContentFragment() instanceof HomeFragment)) {
                            switchFragment(HomeFragment.newInstance(), false);
                        }
                        break;
                    case R.id.nav_settings:
                        if (!(getContentFragment() instanceof SettingsFragment)) {
                            switchFragment(SettingsFragment.newInstance(0), false);
                        }
                        break;
                    case R.id.nav_about:
                        if (!(getContentFragment() instanceof AboutFragment)) {
                            switchFragment(AboutFragment.newInstance(0), false);
                        }
                        break;
                }

                return true;
            }
        });

        // Set floating action button to view sources on GitHub.
        setFAB(R.drawable.ads_ic_social_github, getFABVisibility(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLinkUtils.viewUrl(BottomNavigationActivity.this, Constants.URL_GITHUB);
            }
        });

        // Select home fragment.
        if (getContentFragment() == null) {
            mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    /**
     * Apply theme for the bottom navigation view.
     */
    private void themeBottomNavigationView() {
        if (mBottomNavigationView != null) {
            if (isNavigationBarTheme()
                    && mAppliedNavigationBarColor != ADS_DEFAULT_SYSTEM_BG_COLOR) {
                mBottomNavigationView.setColor(mNavigationBarColor);
                mBottomNavigationView.setTextColor(
                        DynamicColorUtils.getTintColor(mNavigationBarColor));
            } else {
                mBottomNavigationView.setColor(DynamicTheme.getInstance().getPrimaryColor());
                mBottomNavigationView.setTextColor(DynamicColorUtils.getTintColor(
                        DynamicTheme.getInstance().getPrimaryColor()));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update bottom navigation view theme on resume.
        themeBottomNavigationView();
    }
}
