/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicSplashListener;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.splash.fragment.DynamicSplashFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * An activity to show a splash screen before the actual app launch. Its layout can be fully
 * customised and it also provides multiple methods to do any background work before launching
 * the main activity by running the
 * {@link com.pranavpandey.android.dynamic.utils.concurrent.DynamicTask}.
 *
 * <p>Extend this activity and implement the required methods to show a splash screen.
 * It should be declared as the main activity in the project's manifest for best performance.
 */
public abstract class DynamicSplashActivity extends DynamicSystemActivity
        implements DynamicSplashListener {

    /**
     * Splash fragment TAG key which will be used to find it during the configuration changes.
     */
    protected static final String ADS_STATE_SPLASH_FRAGMENT_TAG = "ads_state_splash_fragment_tag";

    /**
     * Boolean to save the fragment state.
     */
    private static boolean ADS_SPLASH_MAGIC;

    /**
     * Intent for the next activity
     */
    protected Intent mNextActivityIntent;

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

        if (mContentFragment instanceof DynamicSplashFragment) {
            getWindow().setBackgroundDrawable(new ColorDrawable(
                    ((DynamicSplashFragment) mContentFragment).getBackgroundColor()));
            mCoordinatorLayout.setBackgroundColor(
                    ((DynamicSplashFragment) mContentFragment).getBackgroundColor());
        }

        setStatusBarColor(getStatusBarColor());
        setNavigationBarColor(getNavigationBarColor());

        ((DynamicSplashFragment) mContentFragment).setOnSplashListener(this);
        commitFragmentTransaction(getSupportFragmentManager().beginTransaction()
                .replace(R.id.ads_container, mContentFragment, ADS_STATE_SPLASH_FRAGMENT_TAG));
    }

    @Override
    protected void onNewIntent(@Nullable Intent intent, boolean newIntent) {
        super.onNewIntent(intent, newIntent);

        onUpdateIntent(intent, newIntent);

        if (!(mContentFragment instanceof DynamicSplashFragment)) {
            return;
        }

        ((DynamicSplashFragment) mContentFragment).show(getSavedInstanceState() != null);
    }

    @Override
    public @NonNull View getContentView() {
        return findViewById(R.id.ads_container);
    }

    @Override
    public @ColorInt int getBackgroundColor() {
        return DynamicTheme.getInstance().get().getPrimaryColor();
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        setWindowStatusBarColor(getStatusBarColor());

        if (mCoordinatorLayout != null) {
            mCoordinatorLayout.setStatusBarBackgroundColor(getStatusBarColor());
        }
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return ADS_SPLASH_MAGIC ? null : getContentView();
    }

    @Override
    protected void onAppThemeChange() {
        // Skip activity recreation on app theme change.
    }

    @Override
    public long getMinSplashTime() {
        return DynamicMotion.Duration.SPLASH;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isChangingConfigurations() && ADS_SPLASH_MAGIC
                && mContentFragment instanceof DynamicSplashFragment) {
            ((DynamicSplashFragment) mContentFragment).setOnSplashListener(this);
            ((DynamicSplashFragment) mContentFragment).show();
        }
    }

    @Override
    public void onPause() {
        if (mContentFragment instanceof DynamicSplashFragment) {
            if (!isChangingConfigurations()) {
                ((DynamicSplashFragment) mContentFragment).stop();
                ADS_SPLASH_MAGIC = true;
            }
            ((DynamicSplashFragment) mContentFragment).setOnSplashListener(null);
        }
        super.onPause();
    }

    @Override
    protected void onSetFallbackActivityOptions() {
        super.onSetFallbackActivityOptions();

        if (!DynamicSdkUtils.is16()) {
            overridePendingTransition(R.anim.ads_fade_in, R.anim.ads_fade_out);
        }
    }

    @Override
    public void onPreSplash() {
        onUpdateIntent(getIntent(), false);
    }

    /**
     * Returns the next activity intent.
     *
     * @return The next activity intent.
     */
    public @Nullable Intent getNextActivityIntent() {
        return mNextActivityIntent;
    }

    /**
     * Set the next activity intent.
     *
     * @param intent The next activity intent to be set.
     */
    public void setNextActivityIntent(@Nullable Intent intent) {
        this.mNextActivityIntent = intent;
    }

    /**
     * Start the activity intent with a fade animation.
     *
     * @param intent The activity intent to be started.
     * @param finish {@code true} to finish the current activity before starting
     *               the main activity.
     *
     * @see #finishActivity()
     * @see ActivityCompat#startActivity(Context, Intent, Bundle)
     */
    public void startMainActivity(@Nullable Intent intent, boolean finish) {
       startMotionActivity(intent, ActivityOptionsCompat.makeCustomAnimation(
               this, R.anim.ads_fade_in, R.anim.ads_fade_out).toBundle(),
               true, finish, false);
    }
}
