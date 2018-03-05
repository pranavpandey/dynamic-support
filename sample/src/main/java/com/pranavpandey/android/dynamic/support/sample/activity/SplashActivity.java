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

package com.pranavpandey.android.dynamic.support.sample.activity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.splash.DynamicSplashActivity;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

import java.util.Locale;

/**
 * Implementing a splash screen using {@link DynamicSplashActivity}.
 */
public class SplashActivity extends DynamicSplashActivity {

    private AppCompatImageView mSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    protected @StyleRes int getThemeRes() {
        // Return activity theme to be applied.
        return SampleTheme.getSplashStyle();
    }

    @Override
    protected void onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        SampleTheme.setLocalTheme(this);
        DynamicTheme.getInstance().setLocalPrimaryColorDark(
                DynamicTheme.getInstance().getLocalPrimaryColor(), true);
    }

    @Override
    protected boolean setNavigationBarTheme() {
        return SampleController.getInstance().isThemeNavigationBar();
    }

    @Override
    protected boolean setNavigationBarThemeInLandscape() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public long getMinSplashTime() {
        // TODO: Return minimum splash duration according to the requirement.
        return 750;
    }

    @Override
    public void onViewCreated(@NonNull View view) {
        mSplash = view.findViewById(R.id.splash_image);
    }

    @Override
    public void onPreSplash() {
        // TODO: Do any operation on pre splash.
        if (mSplash != null) {
            ((Animatable) mSplash.getDrawable()).start();
        }
    }

    @Override
    public void doBehindSplash() {
        // TODO: Do any operation behind the splash.
    }

    @Override
    public void onPostSplash() {
        // TODO: Do any operation on post splash.
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
