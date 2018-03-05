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

package com.pranavpandey.android.dynamic.support.sample.controller;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;

/**
 * Singleton class to perform common operations throughout
 * the app.
 */
public class SampleController {

    /**
     * Singleton instance of {@link SampleController}.
     */
    private static SampleController sInstance;

    /**
     * Application context used by this instance.
     */
    private Context mContext;

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use {@link #initializeInstance(Context)} instead.
     */
    private SampleController() { }

    /**
     * Default constructor to initialize this controller.
     */
    private SampleController(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Initialize this controller when application starts. Must be
     * initialize once.
     *
     * @param context Context to retrieve resources.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        if (sInstance == null) {
            sInstance = new SampleController(context);
        }
    }

    /**
     * Get instance to access public methods. Must be called before accessing
     * methods.
     *
     * @return {@link #sInstance} Singleton {@link SampleController} instance.
     */
    public static synchronized SampleController getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SampleController.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * Setter for {@link #mContext}.
     */
    public void setContext(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Getter for {@link #mContext}.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * @return The app theme primary color.
     */
    public @ColorInt int getColorPrimaryApp() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT);
    }

    /**
     * @return The app theme accent color.
     */
    public @ColorInt int getColorAccentApp() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT,
                Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT);
    }

    /**
     * @return {@code true} to apply the navigation bar theme.
     */
    public boolean isThemeNavigationBar() {
        return DynamicPreferences.getInstance().loadPrefs(
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME,
                Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT);
    }
}
