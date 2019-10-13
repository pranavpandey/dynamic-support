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
import android.os.Handler
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.IconCompat
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.sample.activity.ActionActivity
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils
import java.util.*

/**
 * Sample application extending the [DynamicApplication], it must be done to initialize the
 * base components of dynamic support library.
 *
 * This must be registered in the manifest using `name` attribute of the `application` tag.
 */
class SampleApplication : DynamicApplication() {

    companion object {

        /**
         * Theme change delay to apply correct app shortcuts theme.
         */
        const val THEME_CHANGE_DELAY = 150
    }

    override fun onInitialize() {
        // Do any startup work here like initializing the other libraries, analytics, etc.
        AppController.initializeInstance(this)
    }

    @StyleRes override fun getThemeRes(): Int {
        // Return application theme to be applied.
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        // Customise application theme after applying the base style.
        ThemeController.setApplicationTheme()

        Handler().postDelayed({
            // Add dynamic app shortcuts after the delay.
            setShortcuts()
        }, THEME_CHANGE_DELAY.toLong())
    }

    override fun onNavigationBarThemeChange() {
        // TODO: Do any customisations on navigation bar theme change.
    }

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        // Update themes on shared preferences change.
        when (key) {
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR ->
                if (!DynamicTheme.getInstance().isNight && ThemeController.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChange(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR ->
                if (DynamicTheme.getInstance().isNight && ThemeController.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChange(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT ->
                DynamicTheme.getInstance().onDynamicChange(false, true)
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME ->
                DynamicTheme.getInstance().onNavigationBarThemeChange()
            Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME ->
                setShortcuts()
        }
    }

    /**
     * Set dynamic app shortcuts for this application.
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun setShortcuts() {
        // Set in API 25 and above devices.
        if (DynamicSdkUtils.is25()) {
            // Initialize ShortcutManager.
            val shortcutManager = getSystemService(ShortcutManager::class.java)
            // Initialize ShortcutInfo list.
            val shortcuts = ArrayList<ShortcutInfo>()

            // Sources app shortcut intent.
            val intent = Intent(context, ActionActivity::class.java)
            intent.action = Constants.ACTION_APP_SHORTCUT
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add Sources app shortcut to open GitHub page.
            shortcuts.add(ShortcutInfo.Builder(context,
                    Constants.APP_SHORTCUT_SOURCES)
                    .setShortLabel(context.getString(R.string.ads_license_sources))
                    .setLongLabel(context.getString(R.string.ads_license_sources))
                    .setIcon(getShortcutIcon(context, R.drawable.ic_app_shortcut_sources))
                    .setIntent(intent)
                    .build())

            // Add and update app shortcuts.
            if (shortcutManager != null) {
                shortcutManager.removeAllDynamicShortcuts()
                shortcutManager.addDynamicShortcuts(shortcuts)
                shortcutManager.updateShortcuts(shortcuts)
            }
        }
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

            // Use IconCompat to support adaptive icons on API 26 and above devices.
            return IconCompat.createWithAdaptiveBitmap(DynamicResourceUtils
                    .getBitmapFromVectorDrawable(drawable)).toIcon()
        }

        return null
    }
}
