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

package com.pranavpandey.android.dynamic.support;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.ColorInt;
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
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.DynamicColors;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.loader.DynamicLoader;

import java.util.Locale;

/**
 * Base {@link Application} class which can be extended to initialize the {@link DynamicTheme}
 * and to perform theme change operations.
 */
public abstract class DynamicApplication extends Application
        implements androidx.work.Configuration.Provider, DynamicLocale,
        DynamicListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Dynamic context used by this application.
     */
    protected Context mContext;

    /**
     * Base context used by this application.
     */
    private Context mBaseContext;

    /**
     * Current application configuration.
     */
    private Configuration mConfiguration;

    @Override
    public void attachBaseContext(@NonNull Context base) {
        this.mBaseContext = base;

        DynamicPreferences.initializeInstance(base);
        DynamicTheme.initializeInstance(this, getDynamicResolver());
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

        if (isDynamicColor()) {
            DynamicTheme.getInstance().setWallpaperColors(true, false);
        }
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        DynamicLoader.getInstance().clearDrawables();
    }

    /**
     * This method will be called inside the {@link #onCreate()} method before applying the theme.
     * <p>Do any initializations in this method.
     */
    protected abstract void onInitialize();

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
     *
     * @return {@code true} to setup the dynamic work.
     */
    protected boolean onSetupDynamicWork() {
        return true;
    }

    /**
     * Set the dynamic app theme and style resource for this application.
     */
    protected void setDynamicTheme() {
        DynamicTheme.getInstance().setTheme(getThemeRes(),
                getDynamicTheme(), false);
        onCustomiseTheme();

        if (isOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .registerOnSharedPreferenceChangeListener(this);
        }
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
                false, DynamicLocaleUtils.getLocale(getLocale(),
                        getDefaultLocale(context)), getFontScale());
    }

    @Override
    public float getFontScale() {
        return getDynamicTheme() != null ? getDynamicTheme().getFontScaleRelative()
                : DynamicTheme.getInstance().getDefault(false).getFontScaleRelative();
    }

    @Override
    public @NonNull Context getContext() {
        return mContext != null ? mContext : getBaseContext() != null
                ? getBaseContext() : mBaseContext;
    }

    @Override
    public @DynamicTheme.Version int getRequiredThemeVersion() {
        return DynamicTheme.Version.DEFAULT_AUTO;

    }

    @Override
    public @StyleRes int getThemeRes(@Nullable AppTheme<?> theme) {
        if (Dynamic.isLegacyVersion(getRequiredThemeVersion())) {
            if (theme == null) {
                return R.style.Theme_Dynamic;
            }

            return theme.isDarkTheme() ? R.style.Theme_Dynamic : R.style.Theme_Dynamic_Light;
        } else {
            if (theme == null) {
                return R.style.Theme_Dynamic2;
            }

            return theme.isDarkTheme() ? R.style.Theme_Dynamic2 : R.style.Theme_Dynamic2_Light;
        }
    }

    @Override
    public @StyleRes int getThemeRes() {
        return getThemeRes(null);
    }

    @Override
    public @Nullable AppTheme<?> getDynamicTheme() {
        return new DynamicAppTheme();
    }

    @Override
    public boolean isDynamicColors() {
        return true;
    }

    @Override
    public boolean isDynamicColor() {
        return (com.google.android.material.color.DynamicColors.isDynamicColorAvailable()
                && isSystemColor()) || isWallpaperColor();
    }

    @Override
    public boolean isSystemColor() {
        return false;
    }

    @Override
    public boolean isWallpaperColor() {
        return false;
    }

    @Override
    public boolean isOnSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public @ColorInt int getDefaultColor(@Theme.ColorType int colorType) {
        if (colorType == Theme.ColorType.BACKGROUND) {
            return DynamicTheme.COLOR_BACKGROUND_DEFAULT;
        } if (colorType == Theme.ColorType.PRIMARY) {
            return DynamicTheme.COLOR_PRIMARY_DEFAULT;
        } else if (colorType == Theme.ColorType.ACCENT) {
            return DynamicTheme.COLOR_ACCENT_DEFAULT;
        } else if (colorType == Theme.ColorType.TEXT_PRIMARY) {
            return DynamicTheme.COLOR_TEXT_PRIMARY_DEFAULT;
        } else if (colorType == Theme.ColorType.TEXT_SECONDARY) {
            return DynamicTheme.COLOR_TEXT_SECONDARY_DEFAULT;
        }

        return Color.TRANSPARENT;
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        if (isOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        if (context) {
            setLocale(mBaseContext);
            setLocale(getContext());
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
    public void onDynamicColorsChanged(@Nullable DynamicColors colors, boolean context) {
        if (isDynamicColors()) {
            onDynamicChanged(context, true);
        }
    }

    @Override
    public void onAutoThemeChanged(boolean context) {
        if (isDynamicColors()) {
            DynamicTheme.getInstance().setWallpaperColors(isDynamicColor(), context);
        } else {
            DynamicTheme.getInstance().onDynamicChanged(context, true);
        }
    }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) {
        onAutoThemeChanged(false);
    }

    @Override
    public boolean setNavigationBarTheme() {
        return false;
    }

    @Override
    public void onNavigationBarThemeChanged() { }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            @Nullable String key) {
        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (DynamicTheme.Version.KEY.equals(key)) {
            DynamicTheme.getInstance().onDynamicChanged(true, true);
        }
    }
}
