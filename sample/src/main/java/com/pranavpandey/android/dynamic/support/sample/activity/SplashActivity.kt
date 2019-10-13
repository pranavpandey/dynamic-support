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

import android.content.Intent
import android.graphics.drawable.Animatable
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.splash.DynamicSplashActivity
import java.util.*

/**
 * Implementing a splash screen by using [DynamicSplashActivity].
 */
class SplashActivity : DynamicSplashActivity() {

    /**
     * Splash image view to start animations.
     */
    private var mSplash: AppCompatImageView? = null

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
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

    override fun setNavigationBarThemeInLandscape(): Boolean {
        return true
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun getMinSplashTime(): Long {
        // TODO: Return minimum splash duration according to the requirement.
        return 550
    }

    override fun onViewCreated(view: View) {
        // TODO: Get any view reference here.
        mSplash = view.findViewById(R.id.splash_image)
    }

    override fun onPreSplash() {
        // TODO: Do any operation on pre splash.
        if (mSplash != null) {
            (mSplash!!.drawable as Animatable).start()
        }
    }

    override fun doBehindSplash() {
        // TODO: Do any operation behind the splash.
    }

    override fun onPostSplash() {
        // TODO: Do any operation on post splash.
        startActivity(Intent(this, DrawerActivity::class.java))
        finish()
    }

    override fun finish() {
        super.finish()

        // TODO: Enter and exit animation according ot the need.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
