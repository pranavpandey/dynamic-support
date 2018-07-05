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

package com.pranavpandey.android.dynamic.support;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.support.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

import java.util.Locale;

/**
 * Base application class which can be extended to initialize the
 * {@link DynamicTheme} and to perform theme change operations.
 */
public abstract class DynamicApplication extends Application implements
        DynamicLocale, DynamicListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

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
        super.attachBaseContext(setLocale(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mConfiguration = new Configuration(getResources().getConfiguration());
        DynamicTheme.initializeInstance(this);
        DynamicTheme.getInstance().addDynamicListener(this);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        onInitialize();
        setThemeRes();
        onCustomiseTheme();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int diff = mConfiguration.diff(new Configuration(newConfig));
        if ((diff & ActivityInfo.CONFIG_LOCALE) != 0) {
            DynamicTheme.getInstance().onDynamicChange(true, false);
            mConfiguration = new Configuration(newConfig);
        }
    }

    /**
     * Set the current theme resource for this application.
     */
    protected void setThemeRes() {
        if (getThemeRes() != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            DynamicTheme.getInstance().setTheme(getThemeRes(), true);
        }
    }

    /**
     * This method will be Called inside the {@link #onCreate()} method
     * before applying the theme. Do any initializations in this method.
     */
    protected abstract void onInitialize();

    /**
     * Get the style resource file to apply theme on ths application.
     * Override this method to supply your own customised style.
     *
     * @return Style resource to be applied on this activity.
     */
    protected @StyleRes int getThemeRes() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * This method will be called inside the {@link #onCreate()} method
     * after applying the theme. Override this method to customise the theme
     * further.
     */
    protected void onCustomiseTheme() { }

    /**
     * @return The dynamic context used by this application.
     */
    public @NonNull Context getContext() {
        return mContext;
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
        this.mContext = DynamicLocaleUtils.setLocale(context,
                DynamicLocaleUtils.getLocale(getLocale(), getDefaultLocale(context)));

        return mContext;
    }

    @Override
    public void onDynamicChange(boolean context, boolean recreate) {
        if (context) {
            setLocale(getContext());
            DynamicTheme.getInstance().setContext(getContext());
        }

        setThemeRes();
        onCustomiseTheme();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }
}
