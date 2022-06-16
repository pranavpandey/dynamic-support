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

package com.pranavpandey.android.dynamic.support.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * Base navigation activity to handle everything related to the {@link BottomNavigationView}
 * and {@link NavigationRailView}.
 * <p>It also has many other useful functions to customise according to the requirements.
 */
public abstract class DynamicNavigationActivity extends DynamicActivity
        implements NavigationBarView.OnItemSelectedListener {

    /**
     * Navigation bar view used by this activity.
     */
    private NavigationBarView mNavigationBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationBarView = findViewById(R.id.ads_navigation_bar_view);

        if (mNavigationBarView instanceof BottomNavigationView) {
            setBottomBarShadowVisible(true);
        } else if (mNavigationBarView instanceof NavigationRailView) {
            setNavigationShadowVisible(true);
        }

        mNavigationBarView.setOnItemSelectedListener(this);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return setCollapsingToolbarLayout() ? R.layout.ads_activity_navigation_collapsing
                : R.layout.ads_activity_navigation;
    }

    @Override
    public boolean isApplyFooterInsets() {
        return !(findViewById(R.id.ads_navigation_bar_view) instanceof BottomNavigationView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setWindowStatusBarColor(int color) {
        super.setWindowStatusBarColor(color);

        if (DynamicSdkUtils.is21()) {
            getWindow().setStatusBarColor(Dynamic.withThemeOpacity(color));
        }
    }

    /**
     * Set menu for the navigation bar view.
     *
     * @param menuRes The menu resource id for the navigation bar view.
     */
    public void setNavigationBarViewMenu(@MenuRes int menuRes) {
        mNavigationBarView.getMenu().clear();
        mNavigationBarView.inflateMenu(menuRes);
    }

    /**
     * Set the select item for bottom navigation bar view.
     *
     * @param itemId The menu item id to be selected.
     */
    public void setSelectedItemId(@IdRes int itemId) {
        mNavigationBarView.setSelectedItemId(itemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavigationItemId = item.getItemId();

        onNavigationItemSelected(mNavigationItemId, false);
        mNavigationItemSelected = true;

        return true;
    }

    /**
     * Returns the navigation bar view used by this activity.
     *
     * @return The navigation bar view used by this activity.
     */
    public @Nullable NavigationBarView getNavigationBarView() {
        return mNavigationBarView;
    }
}
