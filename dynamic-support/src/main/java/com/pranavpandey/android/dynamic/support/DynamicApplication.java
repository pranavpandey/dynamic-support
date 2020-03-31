/*
 * Copyright 2018-2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.pranavpandey.android.dynamic.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.Locale;

/**
 * Base application class which can be extended to initialize the {@link DynamicTheme} and to
 * perform theme change operations.
 */
public abstract class DynamicApplication extends Application
        implements androidx.work.Configuration.Provider, DynamicLocale,
        DynamicListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Dynamic context used by this application.
     */
    protected Context mContext;

    /**
     * Current application configuration.
     */
    private Configuration mConfiguration;

    @Override
    public void attachBaseContext(@NonNull Context base) {
        DynamicPreferences.initializeInstance(base);
        DynamicTheme.initializeInstance(base, getDynamicResolver());
        DynamicTheme.getInstance().addDynamicListener(this);
        PreferenceManager.getDefaultSharedPreferences(base)
                .registerOnSharedPreferenceChangeListener(this);
        super.attachBaseContext(setLocale(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        DynamicTheme.getInstance().setDynamicThemeWork(onSetupDynamicWork());
        mConfiguration = new Configuration(getResources().getConfiguration());

        onInitialize();
        setDynamicTheme();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int diff = mConfiguration.diff(new Configuration(newConfig));
        DynamicTheme.getInstance().onDynamicConfigurationChanged(
                (diff & ActivityInfo.CONFIG_LOCALE) != 0,
                (diff & ActivityInfo.CONFIG_FONT_SCALE) != 0,
                (diff & ActivityInfo.CONFIG_ORIENTATION) != 0,
                (diff & ActivityInfo.CONFIG_UI_MODE) != 0,
                DynamicSdkUtils.is17() && (diff & ActivityInfo.CONFIG_DENSITY) != 0);

        mConfiguration = new Configuration(newConfig);
    }

    /**
     * This method will be called inside the {@link #onCreate()} method before applying the theme.
     * <p>Do any initializations in this method.
     */
    protected abstract void onInitialize();

    /**
     * Get the style resource to apply theme on this application.
     * <p>Override this method to supply your own customised style.
     *
     * @return Style resource to be applied on this activity.
     */
    protected @StyleRes int getThemeRes() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Get the dynamic app theme to be applied on this application.
     * <p>Override this method to supply your own customised theme.
     *
     * @return The dynamic app theme for this application.
     */
    protected @Nullable DynamicAppTheme getDynamicTheme() {
        return null;
    }

    /**
     * Returns the resolver for the dynamic theme to provide implementation for conditions like
     * auto and night themes.
     * <p>Override this method to supply your own resolver implementation.
     *
     * @return The resolver for the dynamic theme.
     */
    protected @Nullable DynamicResolver getDynamicResolver() {
        return null;
    }

    /**
     * This method will be called inside the {@link #onCreate()} method after applying the theme.
     * <p>Override this method to customise the theme further.
     */
    protected void onCustomiseTheme() { }

    /**
     * This method will be called before setting up the dynamic work to listen changes based on
     * time. It will be useful in updating the {@code auto} theme while the app is in background.
     * <p>Return {@code false} to skip the dynamic work initialization.
     */
    protected boolean onSetupDynamicWork() {
        return true;
    }

    /**
     * Returns the dynamic context used by this application.
     *
     * @return The dynamic context used by this application.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Set the dynamic app theme and style resource for this application.
     */
    protected void setDynamicTheme() {
        DynamicTheme.getInstance().setTheme(getThemeRes(), getDynamicTheme(), true);

        onCustomiseTheme();
    }

    @Override
    public @NonNull androidx.work.Configuration getWorkManagerConfiguration() {
        return new androidx.work.Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
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
        return mContext = DynamicLocaleUtils.setLocale(context,
                DynamicLocaleUtils.getLocale(getLocale(),
                        getDefaultLocale(context)), getFontScale());
    }

    @Override
    public float getFontScale() {
        return getDynamicTheme() != null ? getDynamicTheme().getFontScaleRelative()
                : DynamicTheme.getInstance().getDefault().getFontScaleRelative();
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        if (context) {
            setLocale(getContext());
            DynamicTheme.getInstance().setContext(getContext());
        }

        setDynamicTheme();
    }

    @Override
    public void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density) {
        onDynamicChanged(locale || fontScale || orientation
                || uiMode || density, locale || uiMode);
    }

    @Override
    public void onNavigationBarThemeChanged() { }

    @Override
    public void onAutoThemeChanged() { }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) { }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }
}
