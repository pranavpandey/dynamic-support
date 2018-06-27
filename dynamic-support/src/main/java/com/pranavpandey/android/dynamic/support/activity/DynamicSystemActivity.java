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
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils;

import java.util.Locale;

/**
 * Base activity to perform all the system UI related tasks like status
 * and navigation bar color, theme, etc. It is heavily depends on the
 * {@link DynamicTheme} which can be customised by implementing the
 * corresponding functions.
 *
 * <p>Just extend this activity and implement the various functions
 * according to the need.</p>
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class DynamicSystemActivity extends AppCompatActivity implements
        DynamicLocale, DynamicListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Dynamic context used by this activity.
     */
    protected Context mContext = this;

    /**
     * Dynamic theme key to maintain its state.
     */
    protected static final String ADS_STATE_DYNAMIC_THEME =
            "ads_state_dynamic_theme";

    /**
     * Status bar color key to maintain its state.
     */
    protected static final String ADS_STATE_STATUS_BAR_COLOR =
            "ads_state_status_bar_color";

    /**
     * Navigation bar color key to maintain its state.
     */
    protected static final String ADS_STATE_NAVIGATION_BAR_COLOR =
            "ads_state_navigation_bar_color";

    /**
     * Default tint color for the system UI elements like snack
     * bars, etc.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_UI_COLOR =
            Color.parseColor("#F5F5F5");

    /**
     * Default background color for the system UI elements like
     * status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_BG_COLOR =
            Color.parseColor("#000000");

    /**
     * Default overlay color for the system UI elements like
     * status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_OVERLAY_COLOR =
            Color.parseColor("#1A000000");

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

    @Override
    public void attachBaseContext(@NonNull Context base) {
        super.attachBaseContext(setLocale(base));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setThemeRes();
        onCustomiseTheme();
        super.onCreate(savedInstanceState);

        mStatusBarColor = DynamicTheme.getInstance().getPrimaryColorDark();
        mNavigationBarColor = DynamicTheme.getInstance().getPrimaryColorDark();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        updateTaskDescription();
        setNavigationBarColor(mNavigationBarColor);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(ADS_STATE_STATUS_BAR_COLOR, mStatusBarColor);
        savedInstanceState.putInt(ADS_STATE_NAVIGATION_BAR_COLOR, mNavigationBarColor);
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
     * Get the style resource file to apply theme on ths activity.
     * Override this method to supply your own customised style.
     *
     * @return The style resource to be applied on this activity.
     */
    protected @StyleRes int getThemeRes() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * This method will be called just before the {@link #onCreate(Bundle)}
     * after applying the theme. Override this method to customise the theme
     * further.
     */
    protected void onCustomiseTheme() { }

    /**
     * @return The dynamic context used by this activity.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Set the current theme resource for this activity.
     */
    private void setThemeRes() {
        if (getThemeRes() != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            DynamicTheme.getInstance().attach(this).setLocalTheme(getThemeRes());
        }
    }

    /**
     * @return {@code true} to apply navigation bar theme for this
     * activity. It will be applied only on the Android L and above
     * devices.
     *
     * <p>By default it will use the {@link DynamicColorType#PRIMARY}
     * color, please use {@link #setNavigationBarColor(int)} to set a
     * custom color.</p>
     */
    protected boolean setNavigationBarTheme() {
        return false;
    }

    /**
     * @return {@code true} if navigation bar theme is applied.
     */
    public boolean isNavigationBarTheme() {
        return mNavigationBarTheme;
    }

    /**
     * @return {@code true} to apply navigation bar theme for this
     * activity in the landscape mode. It will be applied only on
     * the Android L and above devices.
     *
     * <p>By default it will use the {@link DynamicColorType#PRIMARY}
     * color, please use {@link #setNavigationBarColor(int)} to set a
     * custom color.</p>
     */
    protected boolean setNavigationBarThemeInLandscape() {
        return getResources().getBoolean(R.bool.ads_navigation_bar_theme_landscape);
    }

    /**
     * @return {@code true} to register a
     * {@link SharedPreferences.OnSharedPreferenceChangeListener}
     * to receive preference change callback.
     */
    protected boolean setSharedPreferenceChangeListener() {
        return true;
    }

    /**
     * Called after the theme has been changed. Override this method
     * to perform operations after the theme has been changed like
     * re-initialize the {@link DynamicTheme} with new colors, etc.
     */
    protected void onAppThemeChange() {
        recreate();
    }

    /**
     * Called after the navigation bar theme has been changed.
     * Override this method to perform operations after the
     * navigation bar theme has been changed like update it with
     * new colors.
     */
    protected void navigationBarThemeChange() {
        setNavigationBarColor(getNavigationBarColor());
    }

    /**
     * Set the status bar color. It will be applied only on the
     * Android L and above devices.
     *
     * @param color Color to be applied on the status bar.
     */
    protected void setWindowStatusBarColor(@ColorInt int color) {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * Set the status bar color. It will be applied only on the
     * Android L and above devices.
     *
     * @param color Color to be applied on the status bar.
     */
    public void setStatusBarColor(@ColorInt int color) {
        if (DynamicVersionUtils.isLollipop()) {
            mStatusBarColor = color;
            updateStatusBar();
        }
    }

    /**
     * Set the status bar color resource. It will be applied only on the
     * Android L and above devices.
     *
     * @param colorRes Color resource to be applied on the status bar.
     */
    public void setStatusBarColorRes(@ColorRes int colorRes) {
        setStatusBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent status bar flag, useful in case of
     * {@link android.support.design.widget.CollapsingToolbarLayout}.
     * It will be applied only on the Android L and above devices.
     */
    public void setTranslucentStatusBar() {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Update status bar color according to the supplied parameters.
     * It will automatically check for the light or dark color and will
     * change the status bar icons or background accordingly so that the
     * icons will always be visible.
     */
    protected void updateStatusBar() {
        if (!DynamicColorUtils.isColorDark(mStatusBarColor)) {
            if (!DynamicVersionUtils.isMarshmallow()) {
                mStatusBarColor = DynamicColorUtils.getContrastColor(
                        mStatusBarColor, ADS_DEFAULT_SYSTEM_UI_COLOR);
            }
        }

        DynamicViewUtils.setLightStatusBar(getWindow().getDecorView(),
                !DynamicColorUtils.isColorDark(mStatusBarColor));
    }

    /**
     * Set the navigation bar color. It will be applied only on the
     * Android L and above devices.
     *
     * @param color Color to be applied on the navigation bar.
     */
    public void setNavigationBarColor(@ColorInt int color) {
        if (!DynamicVersionUtils.isOreo()) {
            color = DynamicColorUtils.getContrastColor(
                    color, ADS_DEFAULT_SYSTEM_UI_COLOR);
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
     * Set the navigation bar color resource. It will be applied only
     * on the Android L and above devices.
     *
     * @param colorRes Color resource to be applied on the navigation bar.
     */
    public void setNavigationBarColorRes(@ColorRes int colorRes) {
        setNavigationBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent navigation bar flag, useful in case of
     * to sow the layout behind the navigation bar. It will be applied
     * only on the Android L and above devices.
     */
    public void setTranslucentNavigationBar() {
        if (DynamicVersionUtils.isLollipop()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * Update navigation bar color according to the supplied parameters.
     * It will automatically check for the light or dark color and will
     * background accordingly so that the buttons will always be visible.
     */
    protected void updateNavigationBar() {
        DynamicViewUtils.setLightNavigationBar(getWindow().getDecorView(),
                !DynamicColorUtils.isColorDark(mAppliedNavigationBarColor));
    }

    /**
     * @return The current status bar color.
     */
    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    /**
     * @return The current navigation bar color.
     */
    public int getNavigationBarColor() {
        return mNavigationBarColor;
    }

    /**
     * @return The applied navigation bar color.
     */
    public int getAppliedNavigationBarColor() {
        return mAppliedNavigationBarColor;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (setSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .registerOnSharedPreferenceChangeListener(this);
        }

        setThemeRes();
        onCustomiseTheme();
        setNavigationBarColor(mNavigationBarColor);

        final String theme = DynamicTheme.getInstance().getLocalTheme(this);
        if (theme != null && !theme.equals(DynamicTheme.getInstance().toString())) {
            DynamicTheme.getInstance().onDynamicChange(false, true);
        } else if (mCurrentLocale != null) {
            if (!mCurrentLocale.equals(DynamicLocaleUtils.getLocale(
                    getLocale(), getDefaultLocale(this)))) {
                DynamicTheme.getInstance().onDynamicChange(false, true);
            }
        }
    }

    @Override
    public void onPause() {
        DynamicTheme.getInstance().onLocalDestroy();
        super.onPause();

        if (setSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
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
     * Update the task description on Android L and above devices to
     * match it with the theme color.
     */
    private void updateTaskDescription() {
        if (DynamicVersionUtils.isLollipop()) {
            @ColorInt int color = mStatusBarColor;
            color = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));

            setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
        }
    }
}