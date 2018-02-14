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

package com.pranavpandey.android.dynamic.support.splash;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;

/**
 * An activity to show a splash screen before the actual app launch.
 * Its layout can be fully customised and it also provides multiple
 * methods to do any background work before launching the main activity
 * by running an {@link android.os.AsyncTask}.
 *
 * <p>Just extend this activity and implement the required functions to
 * show a splash screen. It should be declared as the main activity in
 * the projects's manifest for best performance.</p>
 */
public abstract class DynamicSplashActivity extends DynamicSystemActivity
        implements DynamicSplashListener {

    /**
     * Splash fragment TAG key which will be used to find it
     * during the configuration changes.
     */
    protected static final String ADS_STATE_SPLASH_FRAGMENT_TAG =
            "ads_state_splash_fragment_tag";

    /**
     * Default minimum time to show the splash.
     */
    private static final long ADS_MIN_SPLASH_TIME = 1000;

    /**
     * Boolean to save the fragment state.
     */
    private static boolean ADS_SPLASH_MAGIC;

    /**
     * Content fragment used by this activity.
     */
    private Fragment mContentFragment;

    /**
     * Coordinator layout used by this activity.
     */
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ADS_SPLASH_MAGIC = false;
        setContentView(R.layout.ads_layout_container);

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);

        if (savedInstanceState != null) {
            mContentFragment = getSupportFragmentManager()
                    .findFragmentByTag(ADS_STATE_SPLASH_FRAGMENT_TAG);
        }

        if (mContentFragment == null) {
            mContentFragment = DynamicSplashFragment.newInstance(getLayoutRes());
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.ads_container, mContentFragment, ADS_STATE_SPLASH_FRAGMENT_TAG).commit();

        if (savedInstanceState == null) {
            ((DynamicSplashFragment) mContentFragment).setOnSplashListener(this);
        }

        setStatusBarColor(getStatusBarColor());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ((DynamicSplashFragment) mContentFragment).show();
        }
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        if (mCoordinatorLayout != null) {
            mCoordinatorLayout.setStatusBarBackgroundColor(getStatusBarColor());
        }
    }

    @Override
    public long getMinSplashTime() {
        return ADS_MIN_SPLASH_TIME;
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        ADS_SPLASH_MAGIC = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!ADS_SPLASH_MAGIC && mContentFragment != null) {
            ((DynamicSplashFragment) mContentFragment).stop();
        }
    }
}
