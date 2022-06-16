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

package com.pranavpandey.android.dynamic.support.sample

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.multidex.MultiDex
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.sample.activity.ActionActivity
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils
import com.pranavpandey.android.dynamic.theme.AppTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils
import java.util.*


/**
 * Sample application extending the [DynamicApplication], it must be done to initialize the
 * base components of dynamic support library.
 *
 * This must be registered in the manifest using `name` attribute of the `application` tag.
 */
class SampleApplication : DynamicApplication() {

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }

    override fun onInitialize() {
        // Do any startup work here like initializing the other libraries, analytics, etc.
        AppController.initializeInstance(this)
    }

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun isNightMode(resolve: Boolean): Boolean {
        // TODO: Using the default Night mode implementation.
        return DynamicTheme.getInstance().isSystemNightMode
    }

    @StyleRes
    override fun getThemeRes(theme: AppTheme<*>?): Int {
        return if (theme != null) {
            ThemeController.getAppStyle(theme.backgroundColor)
        } else ThemeController.appStyle
    }

    override fun getDynamicTheme(): AppTheme<*>? {
        return ThemeController.dynamicAppTheme
    }

    @ColorInt
    override fun getDefaultColor(@Theme.ColorType colorType: Int): Int {
        return when (colorType) {
            Theme.ColorType.BACKGROUND -> {
                return ThemeController.backgroundColor
            }
            Theme.ColorType.PRIMARY -> {
                return ThemeController.colorPrimaryApp
            }
            Theme.ColorType.ACCENT -> {
                ThemeController.colorAccentApp
            }
            else -> super.getDefaultColor(colorType)
        }
    }

    override fun onCustomiseTheme() {
        // Call method to do the delayed work.
        setDelayedTheme()
    }

    override fun onDynamicChanged(context: Boolean, recreate: Boolean) {
        super.onDynamicChanged(context, recreate)

        if (context) {
            AppController.instance.context = this
        }

        if (recreate) {
            setDelayedTheme()
        }
    }

    override fun onNavigationBarThemeChanged() {
        // TODO: Do any customisations on navigation bar theme change.
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        // Update themes on shared preferences change.
        when (key) {
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR ->
                if (ThemeController.getCurrentTheme() == Theme.DAY) {
                    onAutoThemeChanged(false)
                }
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR ->
                if (ThemeController.getCurrentTheme() == Theme.NIGHT) {
                    onAutoThemeChanged(false)
                }
            Constants.PREF_SETTINGS_APP_THEME_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_SURFACE,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT ->
                onAutoThemeChanged(false)
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME ->
                DynamicTheme.getInstance().onNavigationBarThemeChanged()
            Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME ->
                setShortcuts()
        }
    }

    /**
     * Method to do some delayed work.
     */
    private fun setDelayedTheme() {
        DynamicTheme.getInstance().handler.postDelayed({
            // Add dynamic app shortcuts after the delay.
            setShortcuts()
        }, DynamicTheme.DELAY_THEME_CHANGE)
    }

    /**
     * Set dynamic app shortcuts for this application.
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun setShortcuts() {
        // Do not set for API 24 and below.
        if (!DynamicSdkUtils.is25()) {
            return;
        }

        // Initialize ShortcutManager.
        val shortcutManager = ContextCompat.getSystemService(
            this, ShortcutManager::class.java)
        // Do not proceed if shortcut manager is null or rate limit is active.
        if (shortcutManager == null || shortcutManager.isRateLimitingActive) {
            return
        }

        // Sources app shortcut intent.
        val intent = Intent(context, ActionActivity::class.java)
        intent.action = Constants.ACTION_APP_SHORTCUT
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Initialize ShortcutInfo list.
        val shortcuts = ArrayList<ShortcutInfo>()
        // Add Sources app shortcut to open GitHub page.
        shortcuts.add(ShortcutInfo.Builder(context,
                Constants.APP_SHORTCUT_SOURCES)
                .setShortLabel(context.getString(R.string.ads_license_sources))
                .setLongLabel(context.getString(R.string.ads_license_sources))
                .setIcon(getShortcutIcon(context, R.drawable.ic_app_shortcut_sources))
                .setIntent(intent)
                .build())

        // Update dynamic app shortcuts.
        shortcutManager.removeAllDynamicShortcuts()
        shortcutManager.addDynamicShortcuts(shortcuts)
        shortcutManager.updateShortcuts(shortcuts)
    }

    /**
     * Generate a dynamic app shortcut icon from the supplied drawable resource and theme
     * it according the app colors. It must contain a background and foreground layers with
     * the appropriate ids.
     *
     * @param context The context to retrieve the resources.
     * @param drawableRes The drawable resource for the icon.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun getShortcutIcon(context: Context, @DrawableRes drawableRes: Int): Icon? {
        @ColorInt var primaryColor = DynamicTheme.getInstance().get().primaryColor
        @ColorInt var tintPrimaryColor = DynamicTheme.getInstance().get().tintPrimaryColor
        val drawable = DynamicResourceUtils.getDrawable(context, drawableRes)

        if (!AppController.instance.isThemeAppShortcuts) {
            primaryColor = DynamicTheme.getInstance().get().backgroundColor
            tintPrimaryColor = DynamicTheme.getInstance().get().tintBackgroundColor
        }

        if (drawable != null) {
            DynamicDrawableUtils.colorizeDrawable((drawable as LayerDrawable)
                    .findDrawableByLayerId(R.id.background), primaryColor)
            DynamicDrawableUtils.colorizeDrawable(drawable
                    .findDrawableByLayerId(R.id.foreground), tintPrimaryColor)

            // Use IconCompat to support adaptive icons on API 26 and above.
            return IconCompat.createWithAdaptiveBitmap(DynamicResourceUtils
                    .getBitmapFromVectorDrawable(drawable)).toIcon(getContext())
        }

        return null
    }
}
