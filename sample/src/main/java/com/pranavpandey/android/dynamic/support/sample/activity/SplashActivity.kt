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

package com.pranavpandey.android.dynamic.support.sample.activity

import android.annotation.SuppressLint
import android.graphics.drawable.Animatable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.pranavpandey.android.dynamic.support.Dynamic
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.splash.activity.DynamicSplashActivity
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget
import com.pranavpandey.android.dynamic.util.DynamicIntentUtils

/**
 * Implementing a splash screen by using [DynamicSplashActivity].
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : DynamicSplashActivity() {

    /**
     * Splash image view to start animations.
     */
    private var mSplash: AppCompatImageView? = null

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

        if (mSplash is DynamicWidget) {
            // TODO: Tint views according to the background.
            Dynamic.setContrastWithColor(mSplash, backgroundColor)
            Dynamic.setContrastWithColor(
                view.findViewById(R.id.splash_title) as DynamicWidget,
                backgroundColor)
            Dynamic.setContrastWithColor(
                view.findViewById(R.id.splash_subtitle) as DynamicWidget,
                backgroundColor)
        }
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
        startMainActivity(DynamicIntentUtils.getActivityIntent(
                this, DrawerActivity::class.java), true)
        finish()
    }
}
