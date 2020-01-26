/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.sample.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StyleRes
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.sample.fragment.AppSettingsFragment
import java.util.*

/**
 * Implementing a collapsing app bar layout by using [DynamicActivity].
 */
class CollapsingAppBarActivity : DynamicActivity() {

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun getContentRes(): Int {
        // Returning default dynamic support drawer layout.
        return DynamicActivity.ADS_DEFAULT_LAYOUT_RES
    }

    @StyleRes
    override fun getThemeRes(): Int {
        // Return activity theme to be applied.
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        ThemeController.setLocalTheme()
    }

    override fun setNavigationBarTheme(): Boolean {
        // TODO: Return true to apply the navigation bar theme.
        return AppController.instance.isThemeNavigationBar
    }

    override fun setCollapsingToolbarLayout(): Boolean {
        // Return true to enable collapsing toolbar layout.
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set app bar title.
        setTitle(R.string.app_name)

        // Finish this activity on clicking the back navigation button.
        setNavigationClickListener { this@CollapsingAppBarActivity.finish() }

        // Set a drawable to use as app bar backdrop when in expanded state.
        setAppBarBackDrop(R.drawable.ads_ic_extension)

        // Add an optional header with an icon, title and subtitle.
        addHeader(R.layout.ads_header_appbar, true)
        (findViewById<View>(R.id.ads_header_appbar_icon) as ImageView)
                .setImageDrawable(applicationInfo.loadIcon(packageManager))
        (findViewById<View>(R.id.ads_header_appbar_title) as TextView)
                .setText(R.string.collapsing_app_bar)
        (findViewById<View>(R.id.ads_header_appbar_subtitle) as TextView)
                .setText(R.string.collapsing_app_bar_subtitle)

        // Set app settings fragment.
        if (contentFragment == null) {
            switchFragment(AppSettingsFragment.newInstance(), false)
        }
    }
}
