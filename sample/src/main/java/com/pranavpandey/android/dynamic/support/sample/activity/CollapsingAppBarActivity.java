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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.sample.fragment.AppSettingsFragment;

import java.util.Locale;

/**
 * Implementing a collapsing app bar layout by using
 * {@link DynamicActivity}.
 */
public class CollapsingAppBarActivity extends DynamicActivity {

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    protected int getContentRes() {
        // Returning default dynamic support drawer layout.
        return ADS_DEFAULT_LAYOUT_RES;
    }

    @Override
    protected @StyleRes
    int getThemeRes() {
        // Return activity theme to be applied.
        return SampleTheme.getAppStyle();
    }

    @Override
    protected void onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        SampleTheme.setLocalTheme(getContext());
    }

    @Override
    protected boolean setNavigationBarTheme() {
        // TODO: Return true to apply the navigation bar theme.
        return SampleController.getInstance().isThemeNavigationBar();
    }

    @Override
    protected boolean setCollapsingToolbarLayout() {
        // Return true to enable collapsing toolbar layout.
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Set app bar title.
        setTitle(R.string.app_name);

        // Finish this activity on clicking the back navigation button.
        setNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollapsingAppBarActivity.this.finish();
            }
        });

        // Set a drawable to use as app bar backdrop when in expanded state.
        setAppBarBackDrop(R.drawable.ads_ic_extension);

        // Add an optional header with an icon, title and subtitle.
        addHeader(com.pranavpandey.android.dynamic.support.R.layout.ads_layout_header_toolbar, true);
        ((ImageView) findViewById(com.pranavpandey.android.dynamic.support.R.id.ads_header_toolbar_icon))
                .setImageDrawable(getApplicationInfo().loadIcon(getPackageManager()));
        ((TextView) findViewById(com.pranavpandey.android.dynamic.support.R.id.ads_header_toolbar_title))
                .setText(R.string.collapsing_app_bar);
        ((TextView) findViewById(com.pranavpandey.android.dynamic.support.R.id.ads_header_toolbar_subtitle))
                .setText(R.string.collapsing_app_bar_subtitle);

        // Set app settings fragment.
        if (getContentFragment() == null) {
            switchFragment(AppSettingsFragment.newInstance(), false);
        }
    }
}
