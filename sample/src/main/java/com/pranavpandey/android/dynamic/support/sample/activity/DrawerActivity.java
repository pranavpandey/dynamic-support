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
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;

import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.Constants;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.sample.fragment.HomeFragment;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

import java.util.Locale;

/**
 * Implementing a navigation drawer using {@link DynamicDrawerActivity}.
 */
public class DrawerActivity extends DynamicDrawerActivity {

    /**
     * Navigation drawer delay for smoother open and close events.
     */
    private static final int NAV_DRAWER_LAUNCH_DELAY = 250;

    @Override
    protected int getContentRes() {
        // Returning default dynamic support drawer layout.
        return ADS_DEFAULT_LAYOUT_RES;
    }

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    protected @StyleRes int getThemeRes() {
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
        // Return true to apply the navigation bar theme.
        return SampleController.getInstance().isThemeNavigationBar();
    }

    @Override
    public boolean isPersistentDrawer() {
        // Return true to make navigation drawer persistent or opened.
        return getResources().getBoolean(R.bool.ads_persistent_drawer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set app bar title.
        setTitle(R.string.app_name);

        // Set navigation drawer header and menu.
        setNavHeaderIcon(R.drawable.ads_ic_extension);
        setNavHeaderTitle(R.string.app_name);
        setNavHeaderSubtitle(R.string.app_subtitle);
        setNavigationViewMenu(R.menu.menu_drawer);

        // Set home fragment as default fragment.
        if (getContentFragment() == null) {
            switchFragment(HomeFragment.newInstance(), false);
        }

        // Set floating action button to view sources on GitHub.
        setFAB(R.drawable.ads_ic_social_github, getFABVisibility(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLinkUtils.viewUrl(DrawerActivity.this, Constants.URL_GITHUB);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Using handler for smoother open and close events.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectMenu(item.getItemId());
            }
        }, NAV_DRAWER_LAUNCH_DELAY);

        closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Perform operations according to the menu id.
     */
    private void selectMenu(@IdRes int menuId) {
        switch (menuId) {
            case R.id.nav_home:
                if (!(getContentFragment() instanceof HomeFragment)) {
                    switchFragment(HomeFragment.newInstance(), false);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_donate:
                DynamicLinkUtils.viewUrl(this, Constants.URL_DONATE);
                break;
            case R.id.nav_share:
                DynamicLinkUtils.shareApp(this, null,
                        String.format(getString(R.string.ads_format_next_line),
                                getString(R.string.app_share), Constants.URL_GITHUB));
                break;
        }
    }
}
