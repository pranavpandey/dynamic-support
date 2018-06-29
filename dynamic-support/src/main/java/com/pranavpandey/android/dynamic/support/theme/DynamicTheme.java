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

package com.pranavpandey.android.dynamic.support.theme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage theme for the whole application and its activities.
 * It must be initialized before using any activity or widget as they are
 * heavily dependent on this class to generate colors dynamically.
 */
public class DynamicTheme implements DynamicListener {

    /**
     * Dynamic theme shared preferences.
     */
    public static final String ADS_PREF_THEME = "ads_dynamic_theme";

    /**
     * Key for the theme preference.
     */
    public static final String ADS_PREF_THEME_KEY = "ads_theme_";

    /**
     * Constant for the auto theme.
     */
    public static final int ADS_THEME_AUTO = 0;

    /**
     * Default shift amount to generate the darker color.
     */
    public static final float ADS_COLOR_SHIFT_DARK_DEFAULT = 0.9f;

    /**
     * Default primary color used by this theme if no color is supplied.
     */
    private static final @ColorInt int ADS_COLOR_PRIMARY_DEFAULT =
            Color.parseColor("#3F51B5");

    /**
     * Default dark primary color used by this theme if no color is supplied.
     */
    private static final @ColorInt int ADS_COLOR_PRIMARY_DARK_DEFAULT =
            Color.parseColor("#303F9F");

    /**
     * Default accent color used by this theme if no color is supplied.
     */
    private static final @ColorInt int ADS_COLOR_ACCENT_DEFAULT =
            Color.parseColor("#E91E63");

    /**
     * Background color used by the application.
     */
    private @ColorInt int mBackgroundColor;

    /**
     * Primary color used by the application.
     */
    private @ColorInt int mPrimaryColor;

    /**
     * Dark primary color used by the application.
     */
    private @ColorInt int mPrimaryColorDark;

    /**
     * Accent color used by the application.
     */
    private @ColorInt int mAccentColor;

    /**
     * Dark accent color used by the application.
     */
    private @ColorInt int mAccentColorDark;

    /**
     * Tint color according to the background color.
     */
    private @ColorInt int mTintBackgroundColor;

    /**
     * Tint color according to the primary color.
     */
    private @ColorInt int mTintPrimaryColor;

    /**
     * Tint color according to the dark primary color.
     */
    private @ColorInt int mTintPrimaryColorDark;

    /**
     * Tint color according to the accent color.
     */
    private @ColorInt int mTintAccentColor;

    /**
     * Tint color according to the dark accent color.
     */
    private @ColorInt int mTintAccentColorDark;

    /**
     * Background color for remote views used by the application
     */
    private @ColorInt int mRemoteBackgroundColor;

    /**
     * Primary color for remote views used by the application.
     */
    private @ColorInt int mRemotePrimaryColor;

    /**
     * Accent color for remote views used by the application.
     */
    private @ColorInt int mRemoteAccentColor;

    /**
     * Tint color according to the remote background color.
     */
    private @ColorInt int mTintRemoteBackgroundColor;

    /**
     * Tint color according to the remote primary color.
     */
    private @ColorInt int mTintRemotePrimaryColor;

    /**
     * Tint color according to the remote accent color.
     */
    private @ColorInt int mTintRemoteAccentColor;

    /**
     * Background color used by the notification.
     */
    private @ColorInt int mNotificationBackgroundColor;

    /**
     * Primary color used by the notification.
     */
    private @ColorInt int mNotificationPrimaryColor;

    /**
     * Accent color used by the notification.
     */
    private @ColorInt int mNotificationAccentColor;

    /**
     * Tint color according to the notification background color.
     */
    private @ColorInt int mTintNotificationBackgroundColor;

    /**
     * Tint color according to the notification primary color.
     */
    private @ColorInt int mTintNotificationPrimaryColor;

    /**
     * Tint color according to the notification accent color.
     */
    private @ColorInt int mTintNotificationAccentColor;

    /**
     * Background color used by the widget.
     */
    private @ColorInt int mWidgetBackgroundColor;

    /**
     * Primary color used by the widget.
     */
    private @ColorInt int mWidgetPrimaryColor;

    /**
     * Accent color used by the widget.
     */
    private @ColorInt int mWidgetAccentColor;

    /**
     * Tint color according to the widget background color.
     */
    private @ColorInt int mTintWidgetBackgroundColor;

    /**
     * Tint color according to the widget primary color.
     */
    private @ColorInt int mTintWidgetPrimaryColor;

    /**
     * Tint color according to the widget accent color.
     */
    private @ColorInt int mTintWidgetAccentColor;

    /**
     * Theme resource used by the local context.
     */
    private @ColorInt int mLocalThemeRes;

    /**
     * Background color used by the local context.
     */
    private @ColorInt int mLocalBackgroundColor;

    /**
     * Primary color used by the local context.
     */
    private @ColorInt int mLocalPrimaryColor;

    /**
     * Dark primary color used by the local context.
     */
    private @ColorInt int mLocalPrimaryColorDark;

    /**
     * Accent color used by the local context.
     */
    private @ColorInt int mLocalAccentColor;

    /**
     * Dark accent color used by the local context.
     */
    private @ColorInt int mLocalAccentColorDark;

    /**
     * Tint color according to the local background color.
     */
    private @ColorInt int mTintLocalBackgroundColor;

    /**
     * Tint color according to the local primary color.
     */
    private @ColorInt int mTintLocalPrimaryColor;

    /**
     * Tint color according to the local dark primary color.
     */
    private @ColorInt int mTintLocalPrimaryColorDark;

    /**
     * Tint color according to the local accent color.
     */
    private @ColorInt int mTintLocalAccentColor;

    /**
     * Tint color according to the local dark accent color.
     */
    private @ColorInt int mTintLocalAccentColorDark;

    /**
     * Singleton instance of {@link DynamicTheme}.
     */
    private static DynamicTheme sInstance;

    /**
     * Application context used by this theme instance.
     */
    private Context mContext;

    /**
     * Local activity context used by this theme instance.
     */
    private Context mLocalContext;

    /**
     * Collection of dynamic listeners to send them event callback.
     */
    private List<DynamicListener> mDynamicListeners;

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use {@link #initializeInstance(Context)} instead.
     */
    private DynamicTheme() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The application context to be attached
     *                with this theme.
     */
    private DynamicTheme(@NonNull Context context) {
        this.mContext = context;
        this.mPrimaryColor = ADS_COLOR_PRIMARY_DEFAULT;
        this.mPrimaryColorDark = ADS_COLOR_PRIMARY_DARK_DEFAULT;
        this.mAccentColor = ADS_COLOR_ACCENT_DEFAULT;
        this.mDynamicListeners = new ArrayList<>();
    }

    /**
     * Attach a local context to this theme. It can be an activity in
     * case different themes are required for different activities.
     *
     * @param localContext The context to be attached with this theme.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme attach(@NonNull Context localContext) {
        this.mLocalContext = localContext;
        this.mLocalPrimaryColor = ADS_COLOR_PRIMARY_DEFAULT;
        this.mLocalPrimaryColorDark = ADS_COLOR_PRIMARY_DARK_DEFAULT;
        this.mLocalAccentColor = ADS_COLOR_ACCENT_DEFAULT;

        if (localContext instanceof Activity && ((Activity) localContext)
                .getLayoutInflater().getFactory2() == null) {
            LayoutInflaterCompat.setFactory2(((Activity) localContext)
                    .getLayoutInflater(), new DynamicLayoutInflater());
        }
        return this;
    }

    /**
     * Initialize theme when application starts.
     * Must be initialized once.
     *
     * @param context The context to retrieve resources.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        if (sInstance == null) {
            sInstance = new DynamicTheme(context);
        }
    }

    /**
     * Get instance to access public methods. Must be called before
     * accessing methods.
     *
     * @return The singleton instance of this class.
     */
    public static synchronized DynamicTheme getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicTheme.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * Initialize colors form the supplied theme resource.
     *
     * @param theme The theme resource to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote
     *                               colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTheme(@StyleRes int theme, boolean initializeRemoteColors) {
        mContext.getTheme().applyStyle(theme, true);

        this.mBackgroundColor = DynamicResourceUtils.resolveColor(mContext,
                android.R.attr.windowBackground, DynamicColorType.NONE);
        this.mPrimaryColor = DynamicResourceUtils.resolveColor(mContext,
                android.support.v7.appcompat.R.attr.colorPrimary, mPrimaryColor);
        this.mPrimaryColorDark = DynamicResourceUtils.resolveColor(mContext,
                android.support.v7.appcompat.R.attr.colorPrimaryDark, mPrimaryColorDark);
        this.mAccentColor = DynamicResourceUtils.resolveColor(mContext,
                android.support.v7.appcompat.R.attr.colorAccent, mAccentColor);
        initializeColors();

        if (initializeRemoteColors) {
            initializeRemoteColors(true);
        }

        return this;
    }

    /**
     * Initialize colors form the supplied local theme resource.
     *
     * @param localTheme The local theme resource to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setLocalTheme(@StyleRes int localTheme) {
        if (mLocalContext == null) {
            throw new IllegalStateException("Not attached to a local mContext.");
        }

        this.mLocalThemeRes = localTheme;
        mLocalContext.getTheme().applyStyle(localTheme, true);
        this.mLocalBackgroundColor = DynamicResourceUtils.resolveColor(mLocalContext,
                android.R.attr.windowBackground, DynamicColorType.NONE);
        this.mLocalPrimaryColor = DynamicResourceUtils.resolveColor(mLocalContext,
                android.support.v7.appcompat.R.attr.colorPrimary, mLocalPrimaryColor);
        this.mLocalPrimaryColorDark = DynamicResourceUtils.resolveColor(mLocalContext,
                android.support.v7.appcompat.R.attr.colorPrimaryDark, mLocalPrimaryColorDark);
        this.mLocalAccentColor = DynamicResourceUtils.resolveColor(mLocalContext,
                android.support.v7.appcompat.R.attr.colorAccent, mLocalAccentColor);
        initializeLocalColors();

        addDynamicThemeListener(mLocalContext);
        return this;
    }

    /**
     * Initialize other colors according to the base colors. They can
     * be set individually by calling the appropriate functions.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme initializeColors() {
        this.mAccentColorDark = mAccentColor;
        setTintBackgroundColor(DynamicColorUtils.getTintColor(mBackgroundColor));
        setTintPrimaryColor(DynamicColorUtils.getTintColor(mPrimaryColor));
        setTintPrimaryColorDark(DynamicColorUtils.getTintColor(mPrimaryColorDark));
        setTintAccentColor(DynamicColorUtils.getTintColor(mAccentColor));
        setTintAccentColorDark(DynamicColorUtils.getTintColor(mAccentColorDark));

        return this;
    }

    /**
     * Initialize other local colors according to the base local colors.
     * They can be set individually by calling the appropriate functions.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme initializeLocalColors() {
        this.mLocalAccentColorDark = mLocalAccentColor;
        setTintLocalBackgroundColor(DynamicColorUtils.getTintColor(mLocalBackgroundColor));
        setTintLocalPrimaryColor(DynamicColorUtils.getTintColor(mLocalPrimaryColor));
        setTintLocalPrimaryColorDark(DynamicColorUtils.getTintColor(mLocalPrimaryColorDark));
        setTintLocalAccentColor(DynamicColorUtils.getTintColor(mLocalAccentColor));
        setTintLocalAccentColorDark(DynamicColorUtils.getTintColor(mLocalAccentColorDark));

        return this;
    }

    /**
     * Initialize remote colors according to the base colors. They can
     * be set individually by calling the appropriate functions.
     *
     * @param reset {@code true} to reset the previously set remote colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme initializeRemoteColors(boolean reset) {
        if (reset) {
            this.mRemoteBackgroundColor = ContextCompat.getColor(getResolvedContext(),
                    !DynamicVersionUtils.isLollipop() ? R.color.notification_background
                            : R.color.notification_background_light);
            this.mRemotePrimaryColor = mPrimaryColor;
            this.mRemoteAccentColor = mAccentColor;
            this.mNotificationBackgroundColor = mRemoteBackgroundColor;
            this.mNotificationPrimaryColor = mPrimaryColor;
            this.mNotificationAccentColor = mAccentColor;
            this.mWidgetBackgroundColor = mRemoteBackgroundColor;
            this.mWidgetPrimaryColor = mPrimaryColor;
            this.mWidgetAccentColor = mAccentColor;
        }

        setTintRemoteBackgroundColor(DynamicColorUtils.getTintColor(mRemoteBackgroundColor));
        setTintRemotePrimaryColor(DynamicColorUtils.getTintColor(mRemotePrimaryColor));
        setTintRemoteAccentColor(DynamicColorUtils.getTintColor(mRemoteAccentColor));
        setTintNotificationBackgroundColor(DynamicColorUtils.getTintColor(mNotificationBackgroundColor));
        setTintNotificationPrimaryColor(DynamicColorUtils.getTintColor(mNotificationPrimaryColor));
        setTintNotificationAccentColor(DynamicColorUtils.getTintColor(mNotificationAccentColor));
        setTintWidgetBackgroundColor(DynamicColorUtils.getTintColor(mWidgetBackgroundColor));
        setTintWidgetPrimaryColor(DynamicColorUtils.getTintColor(mWidgetPrimaryColor));
        setTintWidgetAccentColor(DynamicColorUtils.getTintColor(mWidgetAccentColor));

        return this;
    }

    /**
     * Get color according to the dynamic color type.
     *
     * @param colorType The color type to be retrieved.
     *
     * @return The color according to the type.
     *
     * @see DynamicColorType
     */
    public @ColorInt int getColorFromType(@DynamicColorType int colorType) {
        switch (colorType) {
            default: return DynamicColorType.NONE;
            case DynamicColorType.PRIMARY: return getPrimaryColor();
            case DynamicColorType.PRIMARY_DARK: return getPrimaryColorDark();
            case DynamicColorType.ACCENT: return getAccentColor();
            case DynamicColorType.ACCENT_DARK: return getAccentColorDark();
            case DynamicColorType.TINT_PRIMARY: return getTintPrimaryColor();
            case DynamicColorType.TINT_PRIMARY_DARK: return getTintPrimaryColorDark();
            case DynamicColorType.TINT_ACCENT: return getTintAccentColor();
            case DynamicColorType.TINT_ACCENT_DARK: return getTintAccentColorDark();
            case DynamicColorType.BACKGROUND: return getBackgroundColor();
            case DynamicColorType.TINT_BACKGROUND: return getTintBackgroundColor();
        }
    }

    /**
     * @return The application context.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Set the application context for this theme.
     *
     * @param context The application context to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setContext(@NonNull Context context) {
        this.mContext = context;

        return this;
    }

    /**
     * @return The background color used by the application.
     */
    public @ColorInt int getBackgroundColor() {
        return mLocalContext != null ? mLocalBackgroundColor : mBackgroundColor;
    }

    /**
     * Set the background color used by the application.
     *
     * @param backgroundColor The background color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintBackgroundColor(int)
     */
    public DynamicTheme setBackgroundColor(@ColorInt int backgroundColor,
                                           boolean generateTint) {
        this.mBackgroundColor = backgroundColor;
        if (generateTint) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(backgroundColor));
        }

        return this;
    }

    /**
     * Set the background color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param backgroundColor The background color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setBackgroundColor(int, boolean)
     * @see #setTintBackgroundColor(int)
     */
    public DynamicTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    /**
     * @return The primary color used by the application.
     */
    public @ColorInt int getPrimaryColor() {
        return mLocalContext != null ? mLocalPrimaryColor : mPrimaryColor;
    }

    /**
     * Set the primary color used by the application.
     *
     * @param primaryColor The primary color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintPrimaryColor(int)
     */
    public DynamicTheme setPrimaryColor(@ColorInt int primaryColor, boolean generateTint) {
        this.mPrimaryColor = primaryColor;
        if (generateTint) {
            setTintPrimaryColor(DynamicColorUtils.getTintColor(primaryColor));
        }

        return this;
    }

    /**
     * Set the primary color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param primaryColor The primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setPrimaryColor(int, boolean)
     * @see #setTintPrimaryColor(int)
     */
    public DynamicTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    /**
     * @return The dark primary color used by the application.
     */
    public @ColorInt int getPrimaryColorDark() {
        return mLocalContext != null ? mLocalPrimaryColorDark : mPrimaryColorDark;
    }

    /**
     * Set the dark primary color used by the application.
     *
     * @param primaryColorDark The dark primary color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintPrimaryColorDark(int)
     */
    public DynamicTheme setPrimaryColorDark(@ColorInt int primaryColorDark,
                                            boolean generateTint) {
        this.mPrimaryColorDark = primaryColorDark;
        if (generateTint) {
            setTintPrimaryColorDark(DynamicColorUtils.getTintColor(primaryColorDark));
        }

        return this;
    }

    /**
     * Set the dark primary color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param primaryColorDark The dark primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setPrimaryColorDark(int, boolean)
     * @see #setTintPrimaryColorDark(int)
     */
    public DynamicTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    /**
     * @return The accent color used by the application.
     */
    public @ColorInt int getAccentColor() {
        return mLocalContext != null ? mLocalAccentColor : mAccentColor;
    }

    /**
     * Set the accent color used by the application.
     *
     * @param accentColor The accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintAccentColor(int)
     */
    public DynamicTheme setAccentColor(@ColorInt int accentColor, boolean generateTint) {
        this.mAccentColor = accentColor;
        if (generateTint) {
            setTintAccentColor(DynamicColorUtils.getTintColor(accentColor));
        }

        return this;
    }

    /**
     * Set the accent color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param accentColor The accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setAccentColor(int, boolean)
     * @see #setTintAccentColor(int)
     */
    public DynamicTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    /**
     * @return The dark accent color used by the application.
     */
    public @ColorInt int getAccentColorDark() {
        return mLocalContext != null ? mLocalAccentColorDark : mAccentColorDark;
    }

    /**
     * Set the dark accent color used by the application.
     *
     * @param accentColorDark The dark accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintAccentColorDark(int)
     */
    public DynamicTheme setAccentColorDark(@ColorInt int accentColorDark,
                                           boolean generateTint) {
        this.mAccentColorDark = accentColorDark;
        if (generateTint) {
            setTintAccentColorDark(DynamicColorUtils.getTintColor(accentColorDark));
        }

        return this;
    }

    /**
     * Set the dark accent color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param accentColorDark The dark accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setAccentColorDark(int, boolean)
     * @see #setTintAccentColorDark(int)
     */
    public DynamicTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    /**
     * @return The background tint color used by the application.
     */
    public @ColorInt int getTintBackgroundColor() {
        return mLocalContext != null ? mTintLocalBackgroundColor : mTintBackgroundColor;
    }

    /**
     * Set the background tint color used by the application.
     *
     * @param tintBackgroundColor The background tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintBackgroundColor(@ColorInt int tintBackgroundColor) {
        this.mTintBackgroundColor = tintBackgroundColor;

        return this;
    }

    /**
     * @return The primary tint color used by the application.
     */
    public @ColorInt int getTintPrimaryColor() {
        return mLocalContext != null ? mTintLocalPrimaryColor : mTintPrimaryColor;
    }

    /**
     * Set the primary tint color used by the application.
     *
     * @param tintPrimaryColor The primary tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintPrimaryColor(@ColorInt int tintPrimaryColor) {
        this.mTintPrimaryColor = tintPrimaryColor;

        return this;
    }

    /**
     * @return The dark primary tint color used by the application.
     */
    public @ColorInt int getTintPrimaryColorDark() {
        return mLocalContext != null ? mTintLocalPrimaryColorDark : mTintPrimaryColorDark;
    }

    /**
     * Set the dark primary tint color used by the application.
     *
     * @param tintPrimaryColorDark The dark primary tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintPrimaryColorDark(@ColorInt int tintPrimaryColorDark) {
        this.mTintPrimaryColorDark = tintPrimaryColorDark;

        return this;
    }

    /**
     * @return The accent tint color used by the application.
     */
    public @ColorInt int getTintAccentColor() {
        return mLocalContext != null ? mTintLocalAccentColor : mTintAccentColor;
    }

    /**
     * Set the accent tint color used by the application.
     *
     * @param tintAccentColor The accent tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintAccentColor(@ColorInt int tintAccentColor) {
        this.mTintAccentColor = tintAccentColor;

        return this;
    }

    /**
     * @return The dark accent tint color used by the application.
     */
    public @ColorInt int getTintAccentColorDark() {
        return mLocalContext != null ? mTintLocalAccentColorDark : mTintAccentColorDark;
    }


    /**
     * Set the dark accent tint color used by the application.
     *
     * @param tintAccentColorDark The dark accent tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintAccentColorDark(@ColorInt int tintAccentColorDark) {
        this.mTintAccentColorDark = tintAccentColorDark;

        return this;
    }

    /**
     * @return The remote background color used by the application.
     */
    public @ColorInt int getRemoteBackgroundColor() {
        return mRemoteBackgroundColor;
    }

    /**
     * Set the remote background color used by the application.
     *
     * @param remoteBackgroundColor The remote background color
     *                              to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintRemoteBackgroundColor(int)
     */
    public DynamicTheme setRemoteBackgroundColor(@ColorInt int remoteBackgroundColor,
                                                 boolean generateTint) {
        this.mRemoteBackgroundColor = remoteBackgroundColor;
        if (generateTint) {
            setTintRemoteBackgroundColor(DynamicColorUtils.getTintColor(remoteBackgroundColor));
        }

        return this;
    }

    /**
     * Set the remote background color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param remoteBackgroundColor The remote background color to
     *                              be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setRemoteBackgroundColor(int, boolean)
     * @see #setTintRemoteBackgroundColor(int)
     */
    public DynamicTheme setRemoteBackgroundColor(@ColorInt int remoteBackgroundColor) {
        return setRemoteBackgroundColor(remoteBackgroundColor, true);
    }

    /**
     * @return The remote primary color used by the application.
     */
    public @ColorInt int getRemotePrimaryColor() {
        return mRemotePrimaryColor;
    }

    /**
     * Set the remote primary color used by the application.
     *
     * @param remotePrimaryColor The remote primary color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintRemotePrimaryColor(int)
     */
    public DynamicTheme setRemotePrimaryColor(
            @ColorInt int remotePrimaryColor, boolean generateTint) {
        this.mRemotePrimaryColor = remotePrimaryColor;
        if (generateTint) {
            setTintRemotePrimaryColor(DynamicColorUtils.getTintColor(remotePrimaryColor));
        }

        return this;
    }

    /**
     * Set the remote primary color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param remotePrimaryColor The remote primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setRemotePrimaryColor(int, boolean)
     * @see #setTintRemotePrimaryColor(int)
     */
    public DynamicTheme setRemotePrimaryColor(@ColorInt int remotePrimaryColor) {
        return setRemotePrimaryColor(remotePrimaryColor, true);
    }

    /**
     * @return The remote accent color used by the application.
     */
    public @ColorInt int getRemoteAccentColor() {
        return mRemoteAccentColor;
    }

    /**
     * Set the remote accent color used by the application.
     *
     * @param remoteAccentColor The remote accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintRemoteAccentColor(int)
     */
    public DynamicTheme setRemoteAccentColor(@ColorInt int remoteAccentColor,
                                             boolean generateTint) {
        this.mRemoteAccentColor = remoteAccentColor;
        if (generateTint) {
            setTintRemoteAccentColor(DynamicColorUtils.getTintColor(remoteAccentColor));
        }

        return this;
    }

    /**
     * Set the remote accent color used by the application.
     * It will automatically generate the tint color also.
     *
     * @param remoteAccentColor The remote accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setRemoteAccentColor(int, boolean)
     * @see #setTintRemoteAccentColor(int)
     */
    public DynamicTheme setRemoteAccentColor(@ColorInt int remoteAccentColor) {
        return setRemoteAccentColor(remoteAccentColor, true);
    }

    /**
     * @return The remote background tint color used by the application.
     */
    public @ColorInt int getTintRemoteBackgroundColor() {
        return mTintRemoteBackgroundColor;
    }

    /**
     * Set the remote background tint color used by the application.
     *
     * @param tintRemoteBackgroundColor The remote background tint
     *                                  color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintRemoteBackgroundColor(@ColorInt int tintRemoteBackgroundColor) {
        this.mTintRemoteBackgroundColor = tintRemoteBackgroundColor;

        return this;
    }

    /**
     * @return The remote primary tint color used by the application.
     */
    public @ColorInt int getTintRemotePrimaryColor() {
        return mTintRemotePrimaryColor;
    }

    /**
     * Set the remote primary tint color used by the application.
     *
     * @param tintRemotePrimaryColor The remote primary tint color
     *                               to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintRemotePrimaryColor(@ColorInt int tintRemotePrimaryColor) {
        this.mTintRemotePrimaryColor = tintRemotePrimaryColor;

        return this;
    }

    /**
     * @return The remote accent tint color used by the application.
     */
    public @ColorInt int getTintRemoteAccentColor() {
        return mTintRemoteAccentColor;
    }

    /**
     * Set the remote accent tint color used by the application.
     *
     * @param tintRemoteAccentColor The remote accent tint color
     *                              to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintRemoteAccentColor(@ColorInt int tintRemoteAccentColor) {
        this.mTintRemoteAccentColor = tintRemoteAccentColor;

        return this;
    }

    /**
     * @return The background color used by the notification.
     */
    public @ColorInt int getNotificationBackgroundColor() {
        return mNotificationBackgroundColor;
    }

    /**
     * Set the background color used by the notification.
     *
     * @param notificationBackgroundColor The notification background
     *                                    color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintNotificationBackgroundColor(int)
     */
    public DynamicTheme setNotificationBackgroundColor(
            @ColorInt int notificationBackgroundColor, boolean generateTint) {
        this.mNotificationBackgroundColor = notificationBackgroundColor;
        if (generateTint) {
            setTintNotificationBackgroundColor(
                    DynamicColorUtils.getTintColor(notificationBackgroundColor));
        }

        return this;
    }

    /**
     * Set the background color used by the notification.
     * It will automatically generate the tint color also.
     *
     * @param notificationBackgroundColor The notification background
     *                                    color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setNotificationBackgroundColor(int, boolean)
     * @see #setTintNotificationBackgroundColor(int)
     */
    public DynamicTheme setNotificationBackgroundColor(
            @ColorInt int notificationBackgroundColor) {
        return setNotificationBackgroundColor(
                notificationBackgroundColor, true);
    }

    /**
     * @return The primary color used by the notification.
     */
    public @ColorInt int getNotificationPrimaryColor() {
        return mNotificationPrimaryColor;
    }

    /**
     * Set the primary color used by the notification.
     *
     * @param notificationPrimaryColor The notification primary
     *                                 color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintNotificationPrimaryColor(int)
     */
    public DynamicTheme setNotificationPrimaryColor(@ColorInt int notificationPrimaryColor,
                                                    boolean generateTint) {
        this.mNotificationPrimaryColor = notificationPrimaryColor;
        if (generateTint) {
            this.mTintNotificationPrimaryColor =
                    DynamicColorUtils.getTintColor(notificationPrimaryColor);
        }

        return this;
    }

    /**
     * Set the primary color used by the notification.
     * It will automatically generate the tint color also.
     *
     * @param notificationPrimaryColor The notification primary
     *                                 color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setNotificationPrimaryColor(int, boolean)
     * @see #setTintNotificationPrimaryColor(int)
     */
    public DynamicTheme setNotificationPrimaryColor(@ColorInt int notificationPrimaryColor) {
        return setNotificationPrimaryColor(notificationPrimaryColor, true);
    }

    /**
     * @return The accent color used by the notification.
     */
    public @ColorInt int getNotificationAccentColor() {
        return mNotificationAccentColor;
    }

    /**
     * Set the accent color used by the notification.
     *
     * @param notificationAccentColor The notification accent
     *                                color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintNotificationAccentColor(int)
     */
    public DynamicTheme setNotificationAccentColor(@ColorInt int notificationAccentColor,
                                                   boolean generateTint) {
        this.mNotificationAccentColor = notificationAccentColor;
        if (generateTint) {
            this.mTintNotificationAccentColor =
                    DynamicColorUtils.getTintColor(notificationAccentColor);
        }

        return this;
    }

    /**
     * Set the accent color used by the notification.
     * It will automatically generate the tint color also.
     *
     * @param notificationAccentColor The notification accent
     *                                color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setNotificationAccentColor(int, boolean)
     * @see #setTintNotificationAccentColor(int)
     */
    public DynamicTheme setNotificationAccentColor(@ColorInt int notificationAccentColor) {
        return setNotificationAccentColor(notificationAccentColor, true);
    }

    /**
     * @return The background tint color used by the notification.
     */
    public @ColorInt int getTintNotificationBackgroundColor() {
        return mTintNotificationBackgroundColor;
    }

    /**
     * Sets background tint color used by the notification.
     *
     * @param tintNotificationBackgroundColor The background tint color
     *                                        to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintNotificationBackgroundColor(
            @ColorInt int tintNotificationBackgroundColor) {
        this.mTintNotificationBackgroundColor = tintNotificationBackgroundColor;

        return this;
    }

    /**
     * @return The primary tint color used by the notification.
     */
    public @ColorInt int getTintNotificationPrimaryColor() {
        return mTintNotificationPrimaryColor;
    }

    /**
     * Sets primary tint color used by the notification.
     *
     * @param tintNotificationPrimaryColor The primary tint color
     *                                     to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintNotificationPrimaryColor(
            @ColorInt int tintNotificationPrimaryColor) {
        this.mTintNotificationPrimaryColor = tintNotificationPrimaryColor;

        return this;
    }

    /**
     * @return The accent tint color used by the notification.
     */
    public @ColorInt int getTintNotificationAccentColor() {
        return mTintNotificationAccentColor;
    }

    /**
     * Sets accent tint color used by the notification.
     *
     * @param tintNotificationAccentColor The accent tint color
     *                                    to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintNotificationAccentColor(
            @ColorInt int tintNotificationAccentColor) {
        this.mTintNotificationAccentColor = tintNotificationAccentColor;

        return this;
    }

    /**
     * @return The background color used by the widget.
     */
    public @ColorInt int getWidgetBackgroundColor() {
        return mWidgetBackgroundColor;
    }

    /**
     * Set the background color used by the widget.
     *
     * @param widgetBackgroundColor The widget background color
     *                              to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintWidgetBackgroundColor(int)
     */
    public DynamicTheme setWidgetBackgroundColor(@ColorInt int widgetBackgroundColor,
                                                 boolean generateTint) {
        this.mWidgetBackgroundColor = widgetBackgroundColor;
        if (generateTint) {
            setTintWidgetBackgroundColor(DynamicColorUtils.getTintColor(widgetBackgroundColor));
        }

        return this;
    }

    /**
     * Set the background color used by the widget.
     * It will automatically generate the tint color also.
     *
     * @param widgetBackgroundColor The widget background color to
     *                              be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setWidgetBackgroundColor(int, boolean)
     * @see #setTintWidgetBackgroundColor(int)
     */
    public DynamicTheme setWidgetBackgroundColor(@ColorInt int widgetBackgroundColor) {
        return setWidgetBackgroundColor(widgetBackgroundColor, true);
    }

    /**
     * @return The primary color used by the widget.
     */
    public @ColorInt int getWidgetPrimaryColor() {
        return mWidgetPrimaryColor;
    }

    /**
     * Set the primary color used by the widget.
     *
     * @param widgetPrimaryColor The widget primary color
     *                           to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintWidgetPrimaryColor(int)
     */
    public DynamicTheme setWidgetPrimaryColor(@ColorInt int widgetPrimaryColor,
                                              boolean generateTint) {
        this.mWidgetPrimaryColor = widgetPrimaryColor;
        if (generateTint) {
            setTintWidgetPrimaryColor(DynamicColorUtils.getTintColor(widgetPrimaryColor));
        }

        return this;
    }

    /**
     * Set the primary color used by the widget.
     * It will automatically generate the tint color also.
     *
     * @param widgetPrimaryColor The widget primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setWidgetPrimaryColor(int, boolean)
     * @see #setTintWidgetPrimaryColor(int)
     */
    public DynamicTheme setWidgetPrimaryColor(@ColorInt int widgetPrimaryColor) {
        return setWidgetPrimaryColor(widgetPrimaryColor, true);
    }

    /**
     * @return The accent color used by the widget.
     */
    public @ColorInt int getWidgetAccentColor() {
        return mWidgetAccentColor;
    }

    /**
     * Set the accent color used by the widget.
     *
     * @param widgetAccentColor The widget accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintWidgetAccentColor(int)
     */
    public DynamicTheme setWidgetAccentColor(@ColorInt int widgetAccentColor,
                                             boolean generateTint) {
        this.mWidgetAccentColor = widgetAccentColor;
        if (generateTint) {
            setTintWidgetAccentColor(DynamicColorUtils.getTintColor(widgetAccentColor));
        }

        return this;
    }

    /**
     * Set the accent color used by the widget.
     * It will automatically generate the tint color also.
     *
     * @param widgetAccentColor The widget accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setWidgetAccentColor(int, boolean)
     * @see #setTintWidgetAccentColor(int)
     */
    public DynamicTheme setWidgetAccentColor(@ColorInt int widgetAccentColor) {
        return setWidgetAccentColor(widgetAccentColor, true);
    }

    /**
     * @return The background tint color used by the widget.
     */
    public @ColorInt int getTintWidgetBackgroundColor() {
        return mTintWidgetBackgroundColor;
    }

    /**
     * Sets background tint color used by the widget.
     *
     * @param tintWidgetBackgroundColor The background tint color
     *                                  to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintWidgetBackgroundColor(@ColorInt int tintWidgetBackgroundColor) {
        this.mTintWidgetBackgroundColor = tintWidgetBackgroundColor;

        return this;
    }

    /**
     * @return The primary tint color used by the widget.
     */
    public @ColorInt int getTintWidgetPrimaryColor() {
        return mTintWidgetPrimaryColor;
    }

    /**
     * Sets primary tint color used by the widget.
     *
     * @param tintWidgetPrimaryColor The primary tint color
     *                               to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintWidgetPrimaryColor(@ColorInt int tintWidgetPrimaryColor) {
        this.mTintWidgetPrimaryColor = tintWidgetPrimaryColor;

        return this;
    }

    /**
     * @return The accent tint color used by the widget.
     */
    public @ColorInt int getTintWidgetAccentColor() {
        return mTintWidgetAccentColor;
    }

    /**
     * Sets accent tint color used by the widget.
     *
     * @param tintWidgetAccentColor The accent tint color
     *                              to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintWidgetAccentColor(@ColorInt int tintWidgetAccentColor) {
        this.mTintWidgetAccentColor = tintWidgetAccentColor;

        return this;
    }

    /**
     * @return The background color used by the local context.
     */
    public @ColorInt int getLocalBackgroundColor() {
        return mLocalBackgroundColor;
    }

    /**
     * Set the background color used by the local context.
     *
     * @param localBackgroundColor The background color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintLocalBackgroundColor(int)
     */
    public DynamicTheme setLocalBackgroundColor(@ColorInt int localBackgroundColor,
                                                boolean generateTint) {
        this.mLocalBackgroundColor = localBackgroundColor;
        if (generateTint) {
            this.mTintLocalBackgroundColor =
                    DynamicColorUtils.getTintColor(localBackgroundColor);
        }

        return this;
    }

    /**
     * Set the background color used by the local context.
     * It will automatically generate the tint color also.
     *
     * @param localBackgroundColor The background color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setLocalBackgroundColor(int, boolean)
     * @see #setTintLocalBackgroundColor(int)
     */
    public DynamicTheme setLocalBackgroundColor(@ColorInt int localBackgroundColor) {
        return setLocalBackgroundColor(localBackgroundColor, true);
    }

    /**
     * @return The primary color used by the local context.
     */
    public @ColorInt int getLocalPrimaryColor() {
        return mLocalPrimaryColor;
    }

    /**
     * Set the primary color used by the local context.
     *
     * @param localPrimaryColor The primary color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintLocalPrimaryColor(int)
     */
    public DynamicTheme setLocalPrimaryColor(@ColorInt int localPrimaryColor,
                                             boolean generateTint) {
        this.mLocalPrimaryColor = localPrimaryColor;
        if (generateTint) {
            this.mTintLocalPrimaryColor = DynamicColorUtils.getTintColor(localPrimaryColor);
        }

        return this;
    }

    /**
     * Set the primary color used by the local context.
     * It will automatically generate the tint color also.
     *
     * @param localPrimaryColor The primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setLocalPrimaryColor(int, boolean)
     * @see #setTintLocalPrimaryColor(int)
     */
    public DynamicTheme setLocalPrimaryColor(@ColorInt int localPrimaryColor) {
        return setLocalPrimaryColor(localPrimaryColor, true);
    }

    /**
     * @return The dark primary color used by the local context.
     */
    public @ColorInt int getLocalPrimaryColorDark() {
        return mLocalPrimaryColorDark;
    }

    /**
     * Set the dark primary color used by the local context.
     *
     * @param localPrimaryColorDark The dark primary color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintLocalPrimaryColorDark(int)
     */
    public DynamicTheme setLocalPrimaryColorDark(@ColorInt int localPrimaryColorDark,
                                                 boolean generateTint) {
        this.mLocalPrimaryColorDark = localPrimaryColorDark;
        if (generateTint) {
            this.mTintLocalPrimaryColorDark =
                    DynamicColorUtils.getTintColor(localPrimaryColorDark);
        }

        return this;
    }

    /**
     * Set the dark primary color used by the local context.
     * It will automatically generate the tint color also.
     *
     * @param localPrimaryColorDark The dark primary color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setLocalPrimaryColorDark(int, boolean)
     * @see #setTintLocalPrimaryColorDark(int)
     */
    public DynamicTheme setLocalPrimaryColorDark(@ColorInt int localPrimaryColorDark) {
        return setLocalPrimaryColorDark(localPrimaryColorDark, true);
    }

    /**
     * @return The accent color used by the local context.
     */
    public @ColorInt int getLocalAccentColor() {
        return mLocalAccentColor;
    }

    /**
     * Set the accent color used by the local context.
     *
     * @param localAccentColor The accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintLocalAccentColor(int)
     */
    public DynamicTheme setLocalAccentColor(@ColorInt int localAccentColor,
                                            boolean generateTint) {
        this.mLocalAccentColor = localAccentColor;
        if (generateTint) {
            this.mTintLocalAccentColor = DynamicColorUtils.getTintColor(localAccentColor);
        }

        return this;
    }

    /**
     * Set the accent color used by the local context.
     * It will automatically generate the tint color also.
     *
     * @param localAccentColor The accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setLocalAccentColor(int, boolean)
     * @see #setTintLocalAccentColor(int)
     */
    public DynamicTheme setLocalAccentColor(@ColorInt int localAccentColor) {
        return setLocalAccentColor(localAccentColor, true);
    }

    /**
     * @return The dark accent color used by the local context.
     */
    public @ColorInt int getLocalAccentColorDark() {
        return mLocalAccentColorDark;
    }

    /**
     * Set the dark accent color used by the local context.
     *
     * @param localAccentColorDark The dark accent color to be set.
     * @param generateTint {@code true} to automatically generate
     *                     the tint color also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setTintLocalAccentColorDark(int)
     */
    public DynamicTheme setLocalAccentColorDark(@ColorInt int localAccentColorDark,
                                                boolean generateTint) {
        this.mLocalAccentColorDark = localAccentColorDark;
        if (generateTint) {
            this.mTintLocalAccentColorDark =
                    DynamicColorUtils.getTintColor(localAccentColorDark);
        }

        return this;
    }

    /**
     * Set the dark accent color used by the local context.
     * It will automatically generate the tint color also.
     *
     * @param localAccentColorDark The dark accent color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     *
     * @see #setLocalAccentColorDark(int, boolean)
     * @see #setTintLocalAccentColorDark(int)
     */
    public DynamicTheme setLocalAccentColorDark(@ColorInt int localAccentColorDark) {
        return setLocalAccentColorDark(localAccentColorDark, true);
    }

    /**
     * @return The background tint color used by the local context.
     */
    public @ColorInt int getTintLocalBackgroundColor() {
        return mTintLocalBackgroundColor;
    }

    /**
     * Set the background tint color used by the local context.
     *
     * @param tintLocalBackgroundColor The background tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintLocalBackgroundColor(@ColorInt int tintLocalBackgroundColor) {
        this.mTintLocalBackgroundColor = tintLocalBackgroundColor;

        return this;
    }

    /**
     * @return The primary tint color used by the local context.
     */
    public @ColorInt int getTintLocalPrimaryColor() {
        return mTintLocalPrimaryColor;
    }

    /**
     * Set the primary tint color used by the local context.
     *
     * @param tintLocalPrimaryColor The primary tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintLocalPrimaryColor(@ColorInt int tintLocalPrimaryColor) {
        this.mTintLocalPrimaryColor = tintLocalPrimaryColor;

        return this;
    }

    /**
     * @return The dark primary tint color used by the local context.
     */
    public @ColorInt int getTintLocalPrimaryColorDark() {
        return mTintLocalPrimaryColorDark;
    }

    /**
     * Set the dark primary tint color used by the local context.
     *
     * @param tintLocalPrimaryColorDark The dark primary tint color
     *                                  to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintLocalPrimaryColorDark(@ColorInt int tintLocalPrimaryColorDark) {
        this.mTintLocalPrimaryColorDark = tintLocalPrimaryColorDark;

        return this;
    }

    /**
     * @return The accent tint color used by the local context.
     */
    public @ColorInt int getTintLocalAccentColor() {
        return mTintLocalAccentColor;
    }

    /**
     * Set the accent tint color used by the local context.
     *
     * @param tintLocalAccentColor The accent tint color to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintLocalAccentColor(@ColorInt int tintLocalAccentColor) {
        this.mTintLocalAccentColor = tintLocalAccentColor;

        return this;
    }

    /**
     * @return The dark accent tint color used by the local context.
     */
    public @ColorInt int getTintLocalAccentColorDark() {
        return mTintLocalAccentColorDark;
    }

    /**
     * Set the dark accent tint color used by the local context.
     *
     * @param tintLocalAccentColorDark The dark accent tint color
     *                                 to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicTheme setTintLocalAccentColorDark(@ColorInt int tintLocalAccentColorDark) {
        this.mTintLocalAccentColorDark = tintLocalAccentColorDark;

        return this;
    }

    /**
     * Recreate local activity to update all the views with new theme.
     */
    public void recreateLocal() {
        if (mLocalContext == null) {
            throw new IllegalStateException("Not attached to a local context");
        }

        if (!(mLocalContext instanceof Activity)) {
            throw new IllegalStateException("Not an instance of Activity");
        }

        ((Activity) mLocalContext).recreate();
    }

    /**
     * Set the initialized instance to {@code null} when app
     * terminates for better theme results when theme is changed.
     */
    public void onDestroy() {
        if (sInstance == null) {
            return;
        }

        mContext = null;
        mLocalContext = null;
        sInstance.mContext = null;
        sInstance.mLocalContext = null;
        sInstance = null;

        clearDynamicThemeListener();
    }

    /**
     * Set the initialized instance to {@code null} when local
     * destroys for better theme results when theme is changed.
     */
    public void onLocalDestroy() {
        if (sInstance == null) {
            return;
        }

        saveLocalTheme();
        removeDynamicThemeListener(mLocalContext);
        mLocalContext = null;
    }

    /**
     * @return The currently used context. Generally either the
     *         application or an activity
     */
    private Context getResolvedContext() {
        return mLocalContext != null ? mLocalContext : mContext;
    }

    /**
     * @return The default contrast with color to tint the background
     *         aware views accordingly.
     */
    public @ColorInt int getDefaultContrastWith() {
        return mLocalContext != null ? mLocalBackgroundColor : mBackgroundColor;
    }

    /**
     * Add a dynamic listener to receive the various callbacks.
     *
     * @param dynamicThemeListener The dynamic listener to be
     *                             added.
     *
     * @see DynamicListener
     */
    public void addDynamicThemeListener(@NonNull Context dynamicThemeListener) {
        if (dynamicThemeListener instanceof DynamicListener
                && !mDynamicListeners.contains(dynamicThemeListener)) {
            mDynamicListeners.add((DynamicListener) dynamicThemeListener);
        }
    }

    /**
     * Remove a dynamic listener.
     *
     * @param dynamicThemeListener The dynamic listener to be
     *                             removed.
     *
     * @see DynamicListener
     */
    public void removeDynamicThemeListener(@NonNull Context dynamicThemeListener) {
        if (dynamicThemeListener instanceof DynamicListener) {
            mDynamicListeners.remove(dynamicThemeListener);
        }
    }

    /**
     * Remove all the dynamic listeners.
     */
    public void clearDynamicThemeListener() {
        if (!mDynamicListeners.isEmpty()) {
            mDynamicListeners.clear();
        }
    }

    @Override
    public void onNavigationBarThemeChange() {
        for (DynamicListener dynamicListener : mDynamicListeners) {
            dynamicListener.onNavigationBarThemeChange();
        }
    }

    @Override
    public void onDynamicChange(boolean context, boolean recreate) {
        for (DynamicListener dynamicListener : mDynamicListeners) {
            dynamicListener.onDynamicChange(context, recreate);
        }
    }

    @Override
    public String toString() {
        return "DynamicTheme{" +
                "mBackgroundColor=" + mBackgroundColor +
                ", mPrimaryColor=" + mPrimaryColor +
                ", mPrimaryColorDark=" + mPrimaryColorDark +
                ", mAccentColor=" + mAccentColor +
                ", mAccentColorDark=" + mAccentColorDark +
                ", mTintBackgroundColor=" + mTintBackgroundColor +
                ", mTintPrimaryColor=" + mTintPrimaryColor +
                ", mTintPrimaryColorDark=" + mTintPrimaryColorDark +
                ", mTintAccentColor=" + mTintAccentColor +
                ", mTintAccentColorDark=" + mTintAccentColorDark +
                ", mRemoteBackgroundColor=" + mRemoteBackgroundColor +
                ", mRemotePrimaryColor=" + mRemotePrimaryColor +
                ", mRemoteAccentColor=" + mRemoteAccentColor +
                ", mTintRemotePrimaryColor=" + mTintRemotePrimaryColor +
                ", mTintRemoteAccentColor=" + mTintRemoteAccentColor +
                ", mNotificationBackgroundColor=" + mNotificationBackgroundColor +
                ", mNotificationPrimaryColor=" + mNotificationPrimaryColor +
                ", mNotificationAccentColor=" + mNotificationAccentColor +
                ", mTintNotificationPrimaryColor=" + mTintNotificationPrimaryColor +
                ", mTintNotificationAccentColor=" + mTintNotificationAccentColor +
                ", mWidgetBackgroundColor=" + mWidgetBackgroundColor +
                ", mWidgetPrimaryColor=" + mWidgetPrimaryColor +
                ", mWidgetAccentColor=" + mWidgetAccentColor +
                ", mTintWidgetPrimaryColor=" + mTintWidgetPrimaryColor +
                ", mTintWidgetAccentColor=" + mTintWidgetAccentColor +
                ", mLocalThemeRes=" + mLocalThemeRes +
                ", mLocalBackgroundColor=" + mLocalBackgroundColor +
                ", mLocalPrimaryColor=" + mLocalPrimaryColor +
                ", mLocalPrimaryColorDark=" + mLocalPrimaryColorDark +
                ", mLocalAccentColor=" + mLocalAccentColor +
                ", mLocalAccentColorDark=" + mLocalAccentColorDark +
                ", mTintLocalBackgroundColor=" + mTintLocalBackgroundColor +
                ", mTintLocalPrimaryColor=" + mTintLocalPrimaryColor +
                ", mTintLocalPrimaryColorDark=" + mTintLocalPrimaryColorDark +
                ", mTintLocalAccentColor=" + mTintLocalAccentColor +
                ", mTintLocalAccentColorDark=" + mTintLocalAccentColorDark +
                '}';
    }

    /**
     * Save the local context theme in shared preferences.
     */
    public void saveLocalTheme() {
        if (mLocalContext != null) {
            DynamicPreferences.getInstance().savePrefs(ADS_PREF_THEME,
                    ADS_PREF_THEME_KEY + mLocalContext.getClass().getName(), toString());
        }
    }

    /**
     * @return The supplied context theme from shared preferences.
     *
     * @param context The context to retrieve the theme.
     */
    public @Nullable String getLocalTheme(@NonNull Context context) {
        return DynamicPreferences.getInstance().loadPrefs(ADS_PREF_THEME,
                ADS_PREF_THEME_KEY + context.getClass().getName(), null);
    }

    /**
     * Delete the supplied context theme from shared preferences.
     *
     * @param context The context to delete the theme.
     */
    public void deleteLocalTheme(@NonNull Context context) {
        DynamicPreferences.getInstance().deletePrefs(ADS_PREF_THEME,
                ADS_PREF_THEME_KEY + context.getClass().getName());
    }
}
