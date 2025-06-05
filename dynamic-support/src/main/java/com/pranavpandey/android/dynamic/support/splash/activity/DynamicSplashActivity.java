/*
 * Copyright 2018-2024 Pranav Pandey
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
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicSplashListener;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.splash.fragment.DynamicSplashFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * An activity to show a splash screen before the actual app launch. Its layout can be fully
 * customised and it also provides multiple methods to do any background work before launching
 * the main activity by running the
 * {@link com.pranavpandey.android.dynamic.util.concurrent.DynamicTask}.
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
     * Post splash key to maintain its state.
     */
    protected static final String ADS_STATE_SPLASH_POST = "ads_state_splash_post";

    /**
     * Boolean to save the fragment state.
     */
    private static boolean ADS_SPLASH_MAGIC;

    /**
     * Intent for the next activity
     */
    protected Intent mNextActivityIntent;

    /**
     * {@code true} if {@link #onPostSplash()} has been called at least once.
     */
    private boolean mPostSplash;

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
        setRootBackground(getBackgroundColor());

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);

        if (savedInstanceState != null) {
            mContentFragment = getSupportFragmentManager()
                    .findFragmentByTag(ADS_STATE_SPLASH_FRAGMENT_TAG);
        }

        if (mContentFragment == null) {
            mContentFragment = getContentFragment(getLayoutRes());
        }
        if (mContentFragment instanceof DynamicSplashFragment) {
            ((DynamicSplashFragment) mContentFragment).setOnSplashListener(this);
        }
        if (mContentFragment != null) {
            commitFragmentTransaction(getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ads_container, mContentFragment, ADS_STATE_SPLASH_FRAGMENT_TAG));
        }

        if (DynamicTheme.getInstance().get().getPrimaryColorDark(
                false, false) == Theme.AUTO) {
            setStatusBarColor(DynamicTheme.getInstance()
                    .generateSystemColor(getBackgroundColor()));
            setNavigationBarColor(getStatusBarColor());
        } else {
            setStatusBarColor(getStatusBarColor());
            setNavigationBarColor(getNavigationBarColor());
        }
    }

    @Override
    protected void onNewIntent(@Nullable Intent intent, boolean newIntent) {
        super.onNewIntent(intent, newIntent);

        if (newIntent) {
            mPostSplash = false;
        }
        
        onUpdateIntent(intent, newIntent);

        if (!(mContentFragment instanceof DynamicSplashFragment)) {
            return;
        }

        ((DynamicSplashFragment) mContentFragment).show(getSavedInstanceState() != null);
    }

    @Override
    public @Nullable Fragment getContentFragment(@StyleRes int layoutRes) {
        return DynamicSplashFragment.newInstance(layoutRes);
    }

    @Override
    public @Nullable View getContentView() {
        return findViewById(R.id.ads_container);
    }

    @Override
    public @ColorInt int getBackgroundColor() {
        return Dynamic.resolveColor(
                DynamicTheme.getInstance().get().getBackgroundColor(),
                DynamicTheme.getInstance().get().getPrimaryColor(),
                DynamicTheme.getInstance().get().getTintPrimaryColor(),
                DynamicTheme.getInstance().get().isBackgroundAware()
                        && !Dynamic.isExpressiveVersion());
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        setWindowStatusBarColor(getStatusBarColor());
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return ADS_SPLASH_MAGIC ? null : getCoordinatorLayout();
    }

    @Override
    public @Nullable CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    @Override
    public boolean isApplyEdgeToEdgeInsets() {
        return false;
    }

    @Override
    protected void onAppThemeChange() {
        // Skip theme change event for splash.
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ADS_STATE_SPLASH_POST, mPostSplash);
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

    @Override
    public void onPostSplash() {
        mPostSplash = true;
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
     * Returns whether the {@link #onPostSplash()} has been called at least once.
     *
      * @return {@code true} if the {@link #onPostSplash()} has been called at least once.
     */
    public boolean isPostSplash() {
        return mPostSplash;
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
