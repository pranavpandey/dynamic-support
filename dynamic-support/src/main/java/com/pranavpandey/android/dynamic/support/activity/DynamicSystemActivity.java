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

package com.pranavpandey.android.dynamic.support.activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils;

import java.util.Locale;

/**
 * Base activity to perform all the system UI related tasks like status and navigation bar color,
 * theme, etc. It is heavily depends on the {@link DynamicTheme} which can be customised by
 * implementing the corresponding functions.
 *
 * <p><p>Just extend this activity and implement the various functions according to the need.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class DynamicSystemActivity extends AppCompatActivity implements
        DynamicLocale, DynamicListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Dynamic theme key to maintain its state.
     */
    protected static final String ADS_STATE_DYNAMIC_THEME = "ads_state_dynamic_theme";

    /**
     * Status bar color key to maintain its state.
     */
    protected static final String ADS_STATE_STATUS_BAR_COLOR = "ads_state_status_bar_color";

    /**
     * Navigation bar color key to maintain its state.
     */
    protected static final String ADS_STATE_NAVIGATION_BAR_COLOR = "ads_state_navigation_bar_color";

    /**
     * Default tint color for the system UI elements like snack bars, etc.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_UI_COLOR =
            Color.parseColor("#F5F5F5");

    /**
     * Default background color for the system UI elements like status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_BG_COLOR =
            Color.parseColor("#000000");

    /**
     * Default overlay color for the system UI elements like status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_OVERLAY_COLOR =
            Color.parseColor("#1A000000");

    /**
     * Dynamic context used by this activity.
     */
    protected Context mContext = this;

    /**
     * Current locale used by this activity.
     */
    private Locale mCurrentLocale;

    /**
     * Current status bar color.
     */
    protected @ColorInt int mStatusBarColor;

    /**
     * Current navigation bar color.
     */
    protected @ColorInt int mNavigationBarColor;

    /**
     * Applied navigation bar color.
     */
    protected @ColorInt int mAppliedNavigationBarColor;

    /**
     * {@code true} if navigation bar theme is applied.
     */
    protected boolean mNavigationBarTheme;

    /**
     * Handler to update the theme.
     */
    protected Handler mHandler;

    @Override
    public void attachBaseContext(@NonNull Context base) {
        super.attachBaseContext(setLocale(base));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDynamicTheme();
        onCustomiseTheme();
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        mStatusBarColor = DynamicTheme.getInstance().get().getPrimaryColorDark();
        mNavigationBarColor = DynamicTheme.getInstance().get().getPrimaryColorDark();

        updateTaskDescription(DynamicTheme.getInstance().get().getPrimaryColor());
        setNavigationBarColor(mNavigationBarColor);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADS_STATE_STATUS_BAR_COLOR, mStatusBarColor);
        outState.putInt(ADS_STATE_NAVIGATION_BAR_COLOR, mNavigationBarColor);
    }

    @Override
    public @Nullable String[] getSupportedLocales() {
        return null;
    }

    @Override
    public @NonNull Locale getDefaultLocale(@NonNull Context context) {
        return DynamicLocaleUtils.getDefaultLocale(context, getSupportedLocales());
    }

    @Override
    public @NonNull Context setLocale(@NonNull Context context) {
        this.mCurrentLocale = DynamicLocaleUtils.getLocale(
                getLocale(), getDefaultLocale(context));
        this.mContext = DynamicLocaleUtils.setLocale(context, mCurrentLocale);

        return mContext;
    }

    /**
     * Get the style resource to apply theme on this activity.
     * <p>Override this method to supply your own customised style.
     *
     * @return The style resource to be applied on this activity.
     */
    protected @StyleRes int getThemeRes() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Get the dynamic app theme to be applied on this activity.
     * <p>Override this method to supply your own customised theme.
     *
     * @return The dynamic app theme for this activity.
     */
    protected @Nullable DynamicAppTheme getDynamicTheme() {
        return null;
    }

    /**
     * This method will be called just before the {@link #onCreate(Bundle)} after applying
     * the theme.
     * <p>Override this method to customise the theme further.
     */
    protected void onCustomiseTheme() { }

    /**
     * Returns a layout inflater factory for this activity.
     * <p>It will be used to replace the app compat widgets with their dynamic counterparts
     * to provide the support for dynamic theme.
     *
     * <p><p>Override this method to provide a custom layout inflater.
     *
     * @return The layout inflater factory for this activity.
     */
    protected @Nullable LayoutInflater.Factory2 getDynamicLayoutInflater() {
        return new DynamicLayoutInflater();
    }

    /**
     * Get the dynamic theme context used by this activity.
     *
     * @return The dynamic context used by this activity.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Set the dynamic app theme and style resource for this activity.
     */
    private void setDynamicTheme() {
        if (getDynamicTheme() == null) {
            DynamicTheme.getInstance().attach(this,
                    getDynamicLayoutInflater()).setLocalThemeRes(getThemeRes());
        } else {
            DynamicTheme.getInstance().attach(this,
                    getDynamicLayoutInflater()).setLocalTheme(getDynamicTheme());
        }
    }

    /**
     * Sets whether the navigation bar theme should be applied for this activity.
     * <p>It will be applied only on the Android L and above devices.
     *
     * <p><p>By default it will use the {@link Theme.ColorType#PRIMARY_DARK} color, use
     * {@link #setNavigationBarColor(int)} to set a custom color.
     *
     * @return {@code true} to apply navigation bar theme for this activity.
     */
    protected boolean setNavigationBarTheme() {
        return false;
    }

    /**
     * Returns whether the navigation bar theme is applied for this activity.
     *
     * @return {@code true} if navigation bar theme is applied.
     */
    public boolean isNavigationBarTheme() {
        return mNavigationBarTheme;
    }

    /**
     * Sets whether the navigation bar theme should be applied for this activity in landscape mode.
     * <p>It will be applied only on the Android L and above devices.
     *
     * <p><p>By default it will use the {@link Theme.ColorType#PRIMARY_DARK} color, use
     * {@link #setNavigationBarColor(int)} to set a custom color.
     *
     * @return {@code true} to apply navigation bar theme for this activity in the landscape mode.
     */
    protected boolean setNavigationBarThemeInLandscape() {
        return getResources().getBoolean(R.bool.ads_navigation_bar_theme_landscape);
    }

    /**
     * Sets whether register a shared preferences listener for this activity.
     *
     * @return {@code true} to register a {@link SharedPreferences.OnSharedPreferenceChangeListener}
     *         to receive preference change callback.
     */
    protected boolean setOnSharedPreferenceChangeListener() {
        return true;
    }

    /**
     * This method will be called after the theme has been changed.
     * <p>Override this method to perform operations after the theme has been changed like
     * re-initialize the {@link DynamicTheme} with new colors, etc.
     */
    protected void onAppThemeChange() {
        recreate();
    }

    /**
     * This method will be called after the navigation bar theme has been changed.
     * <p>Override this method to perform operations after the navigation bar theme has been
     * changed like update it with new colors.
     */
    protected void navigationBarThemeChange() {
        setNavigationBarColor(getNavigationBarColor());
    }

    /**
     * Set the status bar color.
     * <p>It will be applied only on the Android L and above devices.
     *
     * @param color Color to be applied on the status bar.
     */
    protected void setWindowStatusBarColor(@ColorInt int color) {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * Set the status bar color.
     * <p>It will be applied only on the Android L and above devices.
     *
     * @param color The color to be applied.
     */
    public void setStatusBarColor(@ColorInt int color) {
        if (DynamicVersionUtils.isLollipop()) {
            mStatusBarColor = color;
            updateStatusBar();
        }
    }

    /**
     * Set the status bar color resource.
     * <p>It will be applied only on the Android L and above devices.
     *
     * @param colorRes The color resource to be applied.
     */
    public void setStatusBarColorRes(@ColorRes int colorRes) {
        setStatusBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent status bar flag, useful in case of {@link CollapsingToolbarLayout}.
     * <p>It will be applied only on the Android L and above devices.
     */
    public void setTranslucentStatusBar() {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Update status bar color according to the supplied parameters. It will automatically
     * check for the light or dark color and will change the status bar icons or background
     * accordingly so that the icons will always be visible.
     */
    protected void updateStatusBar() {
        boolean isLightColor = !DynamicColorUtils.isColorDark(mStatusBarColor);
        if (DynamicTheme.getInstance().get().isBackgroundAware() && isLightColor) {
            if (!DynamicVersionUtils.isMarshmallow()) {
                mStatusBarColor = DynamicColorUtils.getContrastColor(
                        mStatusBarColor, ADS_DEFAULT_SYSTEM_UI_COLOR);
            }
        }

        DynamicViewUtils.setLightStatusBar(getWindow().getDecorView(), isLightColor);
    }

    /**
     * Set the navigation bar color.
     * <p>It will be applied only on the Android L and above devices.
     *
     * @param color The color to be applied.
     */
    public void setNavigationBarColor(@ColorInt int color) {
        if (DynamicTheme.getInstance().get().isBackgroundAware()
                && !DynamicVersionUtils.isOreo()) {
            color = DynamicColorUtils.getContrastColor(color, ADS_DEFAULT_SYSTEM_UI_COLOR);
        }

        int orientation = DynamicWindowUtils.getScreenOrientation(this);
        if (DynamicWindowUtils.isNavigationBarThemeSupported(this)
                && (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)) {
            if (!setNavigationBarThemeInLandscape()) {
                color = ADS_DEFAULT_SYSTEM_BG_COLOR;
            }
        }

        this.mNavigationBarColor = color;
        if (DynamicVersionUtils.isLollipop()) {
            this.mNavigationBarTheme = setNavigationBarTheme();

            mAppliedNavigationBarColor = color = mNavigationBarTheme
                    ? color : ADS_DEFAULT_SYSTEM_BG_COLOR;
            getWindow().setNavigationBarColor(color);
        } else {
            mAppliedNavigationBarColor = mNavigationBarColor;
        }

        updateNavigationBar();
    }

    /**
     * Set the navigation bar color resource.
     * <p>It will be applied only on the Android L and above devices.
     *
     * @param colorRes The color resource to be applied.
     */
    public void setNavigationBarColorRes(@ColorRes int colorRes) {
        setNavigationBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent navigation bar flag, useful in case of to show the layout behind the
     * navigation bar.
     * <p>It will be applied only on the Android L and above devices.
     */
    public void setTranslucentNavigationBar() {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * Update navigation bar color according to the supplied parameters. It will automatically
     * check for the light or dark color and will background accordingly so that the buttons will
     * always be visible.
     */
    protected void updateNavigationBar() {
        DynamicViewUtils.setLightNavigationBar(getWindow().getDecorView(),
                !DynamicColorUtils.isColorDark(mAppliedNavigationBarColor));
    }

    /**
     * Get the current status bar color.
     *
     * @return The current status bar color.
     */
    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    /**
     * Get the current (original) navigation bar color.
     * <p>It may be different from the actually applied color. To get the user visible color,
     * use {@link #getAppliedNavigationBarColor()}.
     *
     * @return The current navigation bar color.
     *
     * @see #getAppliedNavigationBarColor()
     */
    public int getNavigationBarColor() {
        return mNavigationBarColor;
    }

    /**
     * Get the applied navigation bar color.
     *
     * @return The applied navigation bar color.
     */
    public int getAppliedNavigationBarColor() {
        return mAppliedNavigationBarColor;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (setOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .registerOnSharedPreferenceChangeListener(this);
        }

        if (!DynamicTheme.getInstance().isDynamicListener(this)) {
            setDynamicTheme();
            onCustomiseTheme();
            setNavigationBarColor(mNavigationBarColor);

            mHandler.postDelayed(mDynamicChange, DynamicTheme.DELAY_THEME_CHANGE);
        }
    }

    /**
     * Runnable to change dynamic theme on resume.
     */
    private Runnable mDynamicChange = new Runnable() {
        @Override
        public void run() {
            String theme = DynamicTheme.getInstance().getLocalTheme(
                    DynamicSystemActivity.this);
            if (theme != null && !theme.equals(DynamicTheme.getInstance().toString())) {
                DynamicTheme.getInstance().onDynamicChange(false, true);
            } else if (mCurrentLocale != null) {
                if (!mCurrentLocale.equals(DynamicLocaleUtils.getLocale(
                        getLocale(), getDefaultLocale(DynamicSystemActivity.this)))) {
                    DynamicTheme.getInstance().onDynamicChange(false, true);
                }
            }
        }
    };

    @Override
    public void onPause() {
        if (setOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
        DynamicTheme.getInstance().onLocalDestroy();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        DynamicTheme.getInstance().deleteLocalTheme(this);
        super.onDestroy();
    }

    @Override
    public void onNavigationBarThemeChange() {
        navigationBarThemeChange();
    }

    @Override
    public void onDynamicChange(boolean context, boolean recreate) {
        if (context) {
            setLocale(getContext());
        }

        if (recreate) {
            onAppThemeChange();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }

    /**
     * Update the task description on Android L and above devices to match it with the theme
     * color.
     *
     * @param color The color to be set.
     */
    protected void updateTaskDescription(@ColorInt int color) {
        if (DynamicVersionUtils.isLollipop()) {
            setTaskDescription(new ActivityManager.TaskDescription(null, null,
                    DynamicColorUtils.removeAlpha(color)));
        }
    }
}