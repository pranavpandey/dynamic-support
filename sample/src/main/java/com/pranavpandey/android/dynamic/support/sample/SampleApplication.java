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

package com.pranavpandey.android.dynamic.support.sample;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.DynamicApplication;
import com.pranavpandey.android.dynamic.support.sample.controller.Constants;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

import java.util.Locale;

/**
 * Sample application extending the {@link DynamicApplication},
 * it must be done to initialize the base components of dynamic
 * support library.
 * <p>This must be registered in the manifest using {@code name}
 * attribute of the {@code application} tag.</p>
 */
public class SampleApplication extends DynamicApplication {

    @Override
    protected void onInitialize() {
        // Do any startup work here like initializing the other
        // libraries, analytics, etc.
        SampleController.initializeInstance(getContext());
    }

    @Override
    protected @StyleRes int getThemeRes() {
        // Return application theme to be applied.
        return SampleTheme.getAppStyle();
    }

    @Override
    protected void onCustomiseTheme() {
        // Customise application theme after applying the base style.
        SampleTheme.setApplicationTheme(getContext());
    }

    @Override
    public void onNavigationBarThemeChange() {
        // TODO: Do any customisations on navigation bar theme change.
    }

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        // Update themes on shared preferences change.
        switch (key) {
            case Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR:
                if (!DynamicResourceUtils.isNight() && SampleTheme.isAutoTheme()) {
                    DynamicTheme.getInstance().onDynamicChange(false, true);
                }
                break;
            case Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR:
                if (DynamicResourceUtils.isNight() && SampleTheme.isAutoTheme()) {
                    DynamicTheme.getInstance().onDynamicChange(false, true);
                }
                break;
            case Constants.PREF_SETTINGS_APP_THEME_COLOR:
            case Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY:
            case Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT:
                DynamicTheme.getInstance().onDynamicChange(false, true);
                break;
            case Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME:
                DynamicTheme.getInstance().onNavigationBarThemeChange();
                break;
        }
    }
}
