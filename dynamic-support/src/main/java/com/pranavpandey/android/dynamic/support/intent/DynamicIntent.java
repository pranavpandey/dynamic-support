/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.intent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.theme.fragment.DynamicThemeFragment;
import com.pranavpandey.android.dynamic.utils.DynamicIntentUtils;

/**
 * Helper class to manage the intent and extras.
 */
@TargetApi(Build.VERSION_CODES.M)
public class DynamicIntent {

    /**
     * Constant for the permissions action.
     */
    public static final String ACTION_PERMISSIONS =
            "com.pranavpandey.android.dynamic.support.intent.action.PERMISSIONS";

    /**
     * Constant for the dynamic theme action.
     */
    public static final String ACTION_THEME =
            "com.pranavpandey.android.dynamic.support.intent.action.THEME";

    /**
     * Constant for the dynamic remote theme action.
     */
    public static final String ACTION_THEME_REMOTE =
            "com.pranavpandey.android.dynamic.support.intent.action.THEME_REMOTE";

    /**
     * Constant for the dynamic theme share action.
     */
    public static final String ACTION_THEME_SHARE =
            "com.pranavpandey.android.dynamic.support.intent.action.THEME_SHARE";

    /**
     * Constant for dynamic permission request.
     */
    public static final int REQUEST_PERMISSIONS = -1;

    /**
     * Constant for dynamic theme request.
     */
    public static final int REQUEST_THEME = 0;

    /**
     * Constant for dynamic day theme request.
     */
    public static final int REQUEST_THEME_DAY = 1;

    /**
     * Constant for dynamic night theme request.
     */
    public static final int REQUEST_THEME_NIGHT = 2;

    /**
     * Constant for dynamic remote theme request.
     */
    public static final int REQUEST_THEME_REMOTE = 4;

    /**
     * Constant to edit the dynamic theme.
     */
    public static final int REQUEST_THEME_EDIT = 100;

    /**
     * Constant to save the dynamic theme.
     */
    public static final int REQUEST_THEME_SAVE = 101;

    /**
     * Settings action constant for the write system settings.
     *
     * @see Settings#ACTION_MANAGE_WRITE_SETTINGS
     */
    public static final String ACTION_WRITE_SYSTEM_SETTINGS =
            Settings.ACTION_MANAGE_WRITE_SETTINGS;

    /**
     * Settings action constant for the overlay settings.
     *
     * @see Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    public static final String ACTION_OVERLAY_SETTINGS =
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION;

    /**
     * Settings action constant for the usage access settings.
     *
     * @see Settings#ACTION_USAGE_ACCESS_SETTINGS
     */
    public static final String ACTION_USAGE_ACCESS_SETTINGS =
            Settings.ACTION_USAGE_ACCESS_SETTINGS;

    /**
     * Settings action constant for the accessibility settings.
     *
     * @see Settings#ACTION_ACCESSIBILITY_SETTINGS
     */
    public static final String ACTION_ACCESSIBILITY_SETTINGS =
            Settings.ACTION_ACCESSIBILITY_SETTINGS;

    /**
     * Settings action constant for the ignore battery optimization.
     *
     * @see Settings#ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
     */
    public static final String ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS =
            Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;

    /**
     * Settings action constant for request to ignore battery optimizations.
     *
     * @see Settings#ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
     */
    public static final String ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS =
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

    /**
     * Intent extra constant for the fragment argument key.
     */
    public static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";

    /**
     * Intent extra constant for the show fragment arguments.
     */
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";

    /**
     * Intent extra constant for the uri.
     */
    public static final String EXTRA_URI =
            "com.pranavpandey.android.dynamic.support.intent.extra.URI";

    /**
     * Intent extra constant for the text.
     */
    public static final String EXTRA_TEXT =
            "com.pranavpandey.android.dynamic.support.intent.extra.TEXT";

    /**
     * Intent extra constant for the dynamic theme.
     */
    public static final String EXTRA_THEME =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME";

    /**
     * Intent extra constant for the default dynamic theme.
     */
    public static final String EXTRA_THEME_DEFAULT =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME_DEFAULT";

    /**
     * Intent extra constant for the theme URL.
     */
    public static final String EXTRA_THEME_URL =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME_URL";

    /**
     * Intent extra constant for the theme bitmap uri.
     */
    public static final String EXTRA_THEME_BITMAP_URI =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME_BITMAP_URI";

    /**
     * Intent extra constant to show the theme presets.
     */
    public static final String EXTRA_THEME_SHOW_PRESETS =
            "com.pranavpandey.android.dynamic.support.intent.extra.THEME_SHOW_PRESETS";

    /**
     * Intent extra constant for the permissions.
     */
    public static final String EXTRA_PERMISSIONS =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS";

    /**
     * Intent extra constant for permissions to perform it when all the permissions are granted.
     */
    public static final String EXTRA_PERMISSIONS_INTENT =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS_INTENT";

    /**
     * Intent extra constant for permissions action to perform it when all the permissions
     * are granted.
     */
    public static final String EXTRA_PERMISSIONS_ACTION =
            "com.pranavpandey.android.dynamic.support.intent.extra.PERMISSIONS_ACTION";

    /**
     * Intent extra constant for updating the app widget.
     */
    public static final String EXTRA_WIDGET_UPDATE =
            "com.pranavpandey.android.dynamic.support.intent.extra.WIDGET_UPDATE";

    /**
     * Returns an intent to edit or show the dynamic theme.
     *
     * @param context The context to create the intent.
     * @param clazz The theme activity class to create the intent.
     * @param action The action for the intent.
     * @param theme The dynamic app theme extra for the intent.
     * @param defaultTheme The optional dynamic app theme default extra for the intent.
     * @param text The optional text extra for the intent.
     *
     * @return The intent to edit or show the dynamic theme.
     *
     * @see Intent#setComponent(ComponentName)
     * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
     * @see #EXTRA_THEME
     * @see #EXTRA_THEME_DEFAULT
     * @see #EXTRA_TEXT
     */
    public static @NonNull Intent getThemeIntent(@NonNull Context context,
            @NonNull Class<?> clazz, @NonNull String action, @Nullable String theme,
            @Nullable String defaultTheme, @Nullable String text) {
        Intent intent = DynamicIntentUtils.getActivityIntentForResult(context, clazz);
        intent.setAction(action);
        intent.putExtra(EXTRA_THEME, theme);
        intent.putExtra(EXTRA_THEME_DEFAULT, defaultTheme);
        intent.putExtra(EXTRA_TEXT, text);

        return intent;
    }

    /**
     * Returns an intent to share the dynamic theme with a bitmap (code) URI.
     *
     * @param context The context to create the intent.
     * @param clazz The theme activity class to create the intent.
     * @param action The action for the intent.
     * @param theme The dynamic app theme extra for the intent.
     * @param themeURL The optional dynamic app theme URL extra for the intent.
     * @param bitmapUri The optional bitmap URI extra for the intent.
     *
     * @return The intent to share the dynamic theme with a bitmap (code) URI.
     *
     * @see Intent#setComponent(ComponentName)
     * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
     * @see #EXTRA_THEME
     * @see #EXTRA_THEME_URL
     * @see #EXTRA_THEME_BITMAP_URI
     */
    public static @NonNull Intent getThemeShareIntent(@NonNull Context context,
            @NonNull Class<?> clazz, @NonNull String action, @Nullable String theme,
            @Nullable String themeURL, @Nullable Uri bitmapUri) {
        Intent intent = DynamicIntentUtils.getActivityIntent(context, clazz);
        intent.setAction(action);
        intent.putExtra(EXTRA_THEME, theme);
        intent.putExtra(EXTRA_THEME_URL, themeURL);
        intent.putExtra(EXTRA_THEME_BITMAP_URI, bitmapUri);

        return intent;
    }

    /**
     * Returns an intent to edit or show the dynamic theme.
     *
     * @param context The context to create the intent.
     * @param clazz The theme activity class to create the intent.
     * @param action The action for the intent.
     * @param requestCode The request code for the intent.
     * @param theme The dynamic app theme extra for the intent.
     * @param defaultTheme The optional dynamic app theme default extra for the intent.
     * @param text The optional text extra for the intent.
     * @param sharedElement The optional view for the shared element transition.
     *
     * @see #getThemeIntent(Context, Class, String, String, String, String)
     */
    public static void editTheme(@Nullable Context context, @NonNull Class<?> clazz,
            @NonNull String action, int requestCode, @Nullable String theme,
            @Nullable String defaultTheme, @Nullable String text, @Nullable View sharedElement) {
        if (context == null) {
            return;
        }

        Bundle bundle = null;
        if (DynamicMotion.getInstance().isMotion()
                && sharedElement != null && context instanceof Activity) {
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    sharedElement, DynamicThemeFragment.ADS_NAME_THEME_PREVIEW_ACTION).toBundle();
        }

        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(
                    DynamicIntent.getThemeIntent(context, clazz, action,
                            theme, defaultTheme, text), requestCode, bundle);
        } else {
            context.startActivity(DynamicIntent.getThemeIntent(context,
                    clazz, action, theme, defaultTheme, text));
        }

        Dynamic.setTransitionResultCode(context, requestCode);
    }

    /**
     * Returns an intent to edit or show the dynamic app theme.
     *
     * @param context The context to create the intent.
     * @param clazz The theme activity class to create the intent.
     * @param requestCode The request code for the intent.
     * @param theme The dynamic app theme extra for the intent.
     * @param defaultTheme The optional dynamic app theme default extra for the intent.
     * @param text The optional text extra for the intent.
     * @param sharedElement The optional view for the shared element transition.
     *
     * @see #ACTION_THEME
     * @see #editTheme(Context, Class, String, int, String, String, String, View)
     */
    public static void editAppTheme(@Nullable Context context, @NonNull Class<?> clazz,
            int requestCode, @Nullable String theme, @Nullable String defaultTheme,
            @Nullable String text, @Nullable View sharedElement) {
        editTheme(context, clazz, DynamicIntent.ACTION_THEME, requestCode,
                theme, defaultTheme, text, sharedElement);
    }

    /**
     * Returns an intent to edit or show the dynamic remote theme.
     *
     * @param context The context to create the intent.
     * @param clazz The theme activity class to create the intent.
     * @param requestCode The request code for the intent.
     * @param theme The dynamic app theme extra for the intent.
     * @param defaultTheme The optional dynamic app theme default extra for the intent.
     * @param text The optional text extra for the intent.
     * @param sharedElement The optional view for the shared element transition.
     *
     * @see #ACTION_THEME
     * @see #editTheme(Context, Class, String, int, String, String, String, View)
     */
    public static void editRemoteTheme(@Nullable Context context, @NonNull Class<?> clazz,
            int requestCode, @Nullable String theme, @Nullable String defaultTheme,
            @Nullable String text, @Nullable View sharedElement) {
        editTheme(context, clazz, DynamicIntent.ACTION_THEME_REMOTE, requestCode,
                theme, defaultTheme, text, sharedElement);
    }
}
