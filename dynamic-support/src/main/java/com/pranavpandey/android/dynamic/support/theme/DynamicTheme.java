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

import java.util.HashSet;

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
     * Tint color according to the {@link #mBackgroundColor}.
     */
    private @ColorInt int mTintBackgroundColor;

    /**
     * Tint color according to the {@link #mPrimaryColor}.
     */
    private @ColorInt int mTintPrimaryColor;

    /**
     * Tint color according to the {@link #mPrimaryColorDark}.
     */
    private @ColorInt int mTintPrimaryColorDark;

    /**
     * Tint color according to the {@link #mAccentColor}.
     */
    private @ColorInt int mTintAccentColor;

    /**
     * Tint color according to the {@link #mAccentColorDark}.
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
     * Tint color according to the {@link #mRemotePrimaryColor}.
     */
    private @ColorInt int mTintRemotePrimaryColor;

    /**
     * Tint color according to the {@link #mRemoteAccentColor}.
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
     * Tint color according to the {@link #mNotificationPrimaryColor}.
     */
    private @ColorInt int mTintNotificationPrimaryColor;

    /**
     * Tint color according to the {@link #mNotificationAccentColor}.
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
     * Tint color according to the {@link #mWidgetPrimaryColor}.
     */
    private @ColorInt int mTintWidgetPrimaryColor;

    /**
     * Tint color according to the {@link #mWidgetAccentColor}.
     */
    private @ColorInt int mTintWidgetAccentColor;

    /**
     * Theme resource used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalThemeRes;

    /**
     * Background color used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalBackgroundColor;

    /**
     * Primary color used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalPrimaryColor;

    /**
     * Dark primary color used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalPrimaryColorDark;

    /**
     * Accent color used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalAccentColor;

    /**
     * Dark accent color used by the {@link #mLocalContext}, generally an
     * activity.
     */
    private @ColorInt int mLocalAccentColorDark;

    /**
     * Tint color according to the {@link #mLocalBackgroundColor}.
     */
    private @ColorInt int mTintLocalBackgroundColor;

    /**
     * Tint color according to the {@link #mLocalPrimaryColor}.
     */
    private @ColorInt int mTintLocalPrimaryColor;

    /**
     * Tint color according to the {@link #mLocalPrimaryColorDark}.
     */
    private @ColorInt int mTintLocalPrimaryColorDark;

    /**
     * Tint color according to the {@link #mLocalAccentColor}.
     */
    private @ColorInt int mTintLocalAccentColor;

    /**
     * Tint color according to the {@link #mLocalAccentColorDark}.
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
     * Collection of dynamic theme listeners to send them event callback.
     */
    private HashSet<DynamicListener> mDynamicListeners;

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use {@link #initializeInstance(Context)} instead.
     */
    private DynamicTheme() { }

    /**
     * Default constructor to initialize base colors.
     */
    private DynamicTheme(@NonNull Context context) {
        this.mContext = context;
        this.mPrimaryColor = ADS_COLOR_PRIMARY_DEFAULT;
        this.mPrimaryColorDark = ADS_COLOR_PRIMARY_DARK_DEFAULT;
        this.mAccentColor = ADS_COLOR_ACCENT_DEFAULT;
        this.mDynamicListeners = new HashSet<>();
    }

    /**
     * Attach a local context to this theme. It can be an activity in
     * case different themes are required for different activities.
     *
     * @param localContext Context to be attached with this theme.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme attach(@NonNull Context localContext) {
        this.mLocalContext = localContext;
        this.mLocalPrimaryColor = ADS_COLOR_PRIMARY_DEFAULT;
        this.mLocalPrimaryColorDark = ADS_COLOR_PRIMARY_DARK_DEFAULT;
        this.mLocalAccentColor = ADS_COLOR_ACCENT_DEFAULT;

        if (localContext instanceof Activity
                && ((Activity) localContext).getLayoutInflater().getFactory2() == null) {
            LayoutInflaterCompat.setFactory2(
                    ((Activity) localContext).getLayoutInflater(), new DynamicLayoutInflater());
        }
        return this;
    }

    /**
     * Initialize theme when application starts. Must be initialize once.
     *
     * @param context Context to retrieve resources.
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
     * Get instance to access public methods. Must be called before accessing
     * methods.
     *
     * @return {@link #sInstance} Singleton {@link DynamicTheme} instance.
     */
    public static synchronized DynamicTheme getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicTheme.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * Setter for {@link #mContext}.
     */
    public void setContext(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Getter for {@link #mContext}.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Initialize colors form the supplied theme resource.
     *
     * @param theme Theme resource to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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

        if (mLocalContext == null) {
            attach(mContext).setLocalTheme(theme);
        }

        return this;
    }

    /**
     * Initialize colors form the supplied local theme resource.
     *
     * @param localTheme Local theme resource to initialize colors.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme initializeColors() {
        this.mAccentColorDark = mAccentColor;
        this.mTintBackgroundColor = DynamicColorUtils.getTintColor(mBackgroundColor);
        this.mTintPrimaryColor = DynamicColorUtils.getTintColor(mPrimaryColor);
        this.mTintPrimaryColorDark = DynamicColorUtils.getTintColor(mPrimaryColorDark);
        this.mTintAccentColor = DynamicColorUtils.getTintColor(mAccentColor);
        this.mTintAccentColorDark = DynamicColorUtils.getTintColor(mAccentColorDark);

        return this;
    }

    /**
     * Initialize other local colors according to the base local colors.
     * They can be set individually by calling the appropriate functions.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme initializeLocalColors() {
        this.mLocalAccentColorDark = mLocalAccentColor;
        this.mTintLocalBackgroundColor = DynamicColorUtils.getTintColor(mLocalBackgroundColor);
        this.mTintLocalPrimaryColor = DynamicColorUtils.getTintColor(mLocalPrimaryColor);
        this.mTintLocalPrimaryColorDark = DynamicColorUtils.getTintColor(mLocalPrimaryColorDark);
        this.mTintLocalAccentColor = DynamicColorUtils.getTintColor(mLocalAccentColor);
        this.mTintLocalAccentColorDark = DynamicColorUtils.getTintColor(mLocalAccentColorDark);

        return this;
    }

    /**
     * Initialize remote colors according to the base colors. They can
     * be set individually by calling the appropriate functions.
     *
     * @param reset {@code true} to reset the previously set remote colors.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme initializeRemoteColors(boolean reset) {
        if (reset) {
            this.mRemoteBackgroundColor = ContextCompat.getColor(getResolvedContext(),
                    !DynamicVersionUtils.isLollipop() ? R.color.notification_background
                            : R.color.notification_background_light);
            this.mRemotePrimaryColor = mPrimaryColor;
            this.mRemoteAccentColor = mAccentColor;
            this.mNotificationPrimaryColor = mPrimaryColor;
            this.mNotificationAccentColor = mAccentColor;
            this.mWidgetPrimaryColor = mPrimaryColor;
            this.mWidgetAccentColor = mAccentColor;
        }

        this.mNotificationBackgroundColor = mRemoteBackgroundColor;
        this.mWidgetBackgroundColor = mRemoteBackgroundColor;
        this.mTintRemotePrimaryColor = DynamicColorUtils.getTintColor(mRemotePrimaryColor);
        this.mTintRemoteAccentColor = DynamicColorUtils.getTintColor(mRemoteAccentColor);
        this.mTintNotificationPrimaryColor = DynamicColorUtils.getTintColor(mNotificationPrimaryColor);
        this.mTintNotificationAccentColor = DynamicColorUtils.getTintColor(mNotificationAccentColor);
        this.mTintWidgetPrimaryColor = DynamicColorUtils.getTintColor(mWidgetPrimaryColor);
        this.mTintWidgetAccentColor = DynamicColorUtils.getTintColor(mWidgetAccentColor);

        return this;
    }

    /**
     * Get color according to the {@link DynamicColorType}.
     *
     * @param colorType Color attribute from
     * {@link com.pranavpandey.android.dynamic.support.R.attr#ads_colorType}.
     *
     * @return Color based on the attribute.
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
     * Getter for {@link #mBackgroundColor}.
     */
    public @ColorInt int getBackgroundColor() {
        return mLocalContext != null ? mLocalBackgroundColor : mBackgroundColor;
    }

    /**
     * Setter for {@link #mBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setBackgroundColor(@ColorInt int backgroundColor,
                                           boolean generateTint) {
        this.mBackgroundColor = backgroundColor;
        if (generateTint) {
            this.mTintBackgroundColor = DynamicColorUtils.getTintColor(backgroundColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mBackgroundColor}. It will automatically generate
     * tint color also.
     *
     * @see #setBackgroundColor(int)
     */
    public DynamicTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    /**
     * Getter for {@link #mPrimaryColor}.
     */
    public @ColorInt int getPrimaryColor() {
        return mLocalContext != null ? mLocalPrimaryColor : mPrimaryColor;
    }

    /**
     * Setter for {@link #mPrimaryColor}.
     *
     * @param primaryColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setPrimaryColor(@ColorInt int primaryColor, boolean generateTint) {
        this.mPrimaryColor = primaryColor;
        if (generateTint) {
            this.mTintPrimaryColor = DynamicColorUtils.getTintColor(primaryColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mPrimaryColor}. It will automatically generate
     * tint color also.
     *
     * @see #setPrimaryColor(int)
     */
    public DynamicTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    /**
     * Getter for {@link #mPrimaryColorDark}.
     */
    public @ColorInt int getPrimaryColorDark() {
        return mLocalContext != null ? mLocalPrimaryColorDark : mPrimaryColorDark;
    }

    /**
     * Setter for {@link #mPrimaryColorDark}.
     *
     * @param primaryColorDark Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setPrimaryColorDark(@ColorInt int primaryColorDark,
                                            boolean generateTint) {
        this.mPrimaryColorDark = primaryColorDark;
        if (generateTint) {
            this.mTintPrimaryColorDark = DynamicColorUtils.getTintColor(primaryColorDark);
        }

        return this;
    }

    /**
     * Setter for {@link #mPrimaryColorDark}. It will automatically generate
     * tint color also.
     *
     * @see #setPrimaryColorDark(int, boolean)
     */
    public DynamicTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    /**
     * Getter for {@link #mAccentColor}.
     */
    public @ColorInt int getAccentColor() {
        return mLocalContext != null ? mLocalAccentColor : mAccentColor;
    }

    /**
     * Setter for {@link #mAccentColor}.
     *
     * @param accentColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setAccentColor(@ColorInt int accentColor, boolean generateTint) {
        this.mAccentColor = accentColor;
        if (generateTint) {
            this.mTintAccentColor = DynamicColorUtils.getTintColor(accentColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setAccentColor(int, boolean)
     */
    public DynamicTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    /**
     * Getter for {@link #mAccentColorDark}.
     */
    public @ColorInt int getAccentColorDark() {
        return mLocalContext != null ? mLocalAccentColorDark : mAccentColorDark;
    }

    /**
     * Setter for {@link #mAccentColorDark}.
     *
     * @param accentColorDark Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setAccentColorDark(@ColorInt int accentColorDark,
                                           boolean generateTint) {
        this.mAccentColorDark = accentColorDark;
        if (generateTint) {
            this.mTintAccentColorDark = DynamicColorUtils.getTintColor(accentColorDark);
        }

        return this;
    }

    /**
     * Setter for {@link #mTintRemoteAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setAccentColorDark(int, boolean)
     */
    public DynamicTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    /**
     * Getter for {@link #mTintBackgroundColor}.
     */
    public @ColorInt int getTintBackgroundColor() {
        return mLocalContext != null ? mTintLocalBackgroundColor : mTintBackgroundColor;
    }

    /**
     * Setter for {@link #mTintBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintBackgroundColor(@ColorInt int tintBackgroundColor) {
        this.mTintBackgroundColor = tintBackgroundColor;

        return this;
    }

    /**
     * Getter for {@link #mTintPrimaryColor}.
     */
    public @ColorInt int getTintPrimaryColor() {
        return mLocalContext != null ? mTintLocalPrimaryColor : mTintPrimaryColor;
    }

    /**
     * Setter for {@link #mTintPrimaryColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintPrimaryColor(@ColorInt int tintPrimaryColor) {
        this.mTintPrimaryColor = tintPrimaryColor;

        return this;
    }

    /**
     * Getter for {@link #mTintPrimaryColorDark}.
     */
    public @ColorInt int getTintPrimaryColorDark() {
        return mLocalContext != null ? mTintLocalPrimaryColorDark : mTintPrimaryColorDark;
    }

    /**
     * Setter for {@link #mTintPrimaryColorDark}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintPrimaryColorDark(@ColorInt int tintPrimaryColorDark) {
        this.mTintPrimaryColorDark = tintPrimaryColorDark;

        return this;
    }

    /**
     * Getter for {@link #mTintAccentColor}.
     */
    public @ColorInt int getTintAccentColor() {
        return mLocalContext != null ? mTintLocalAccentColor : mTintAccentColor;
    }

    /**
     * Setter for {@link #mTintAccentColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintAccentColor(@ColorInt int tintAccentColor) {
        this.mTintAccentColor = tintAccentColor;

        return this;
    }

    /**
     * Getter for {@link #mTintAccentColorDark}.
     */
    public @ColorInt int getTintAccentColorDark() {
        return mLocalContext != null ? mTintLocalAccentColorDark : mTintAccentColorDark;
    }


    /**
     * Setter for {@link #mLocalAccentColorDark}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintAccentColorDark(@ColorInt int tintAccentColorDark) {
        this.mTintAccentColorDark = tintAccentColorDark;

        return this;
    }

    /**
     * Getter for {@link #mRemoteBackgroundColor}.
     */
    public @ColorInt int getRemoteBackgroundColor() {
        return mRemoteBackgroundColor;
    }

    /**
     * Setter for {@link #mRemoteBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setRemoteBackgroundColor(int remoteBackgroundColor) {
        this.mRemoteBackgroundColor = remoteBackgroundColor;

        return this;
    }

    /**
     * Getter for {@link #mRemotePrimaryColor}.
     */
    public @ColorInt int getRemotePrimaryColor() {
        return mRemotePrimaryColor;
    }

    /**
     * Setter for {@link #mRemotePrimaryColor}.
     *
     * @param remotePrimaryColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setRemotePrimaryColor(
            @ColorInt int remotePrimaryColor, boolean generateTint) {
        this.mRemotePrimaryColor = remotePrimaryColor;
        if (generateTint) {
            this.mTintRemotePrimaryColor = DynamicColorUtils.getTintColor(remotePrimaryColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mRemotePrimaryColor}. It will automatically generate
     * tint color also.
     *
     * @see #setRemotePrimaryColor(int, boolean)
     */
    public DynamicTheme setRemotePrimaryColor(@ColorInt int remotePrimaryColor) {
        return setRemotePrimaryColor(remotePrimaryColor, true);
    }

    /**
     * Getter for {@link #mRemoteAccentColor}.
     */
    public @ColorInt int getRemoteAccentColor() {
        return mRemoteAccentColor;
    }

    /**
     * Setter for {@link #mRemoteAccentColor}.
     *
     * @param remoteAccentColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setRemoteAccentColor(@ColorInt int remoteAccentColor,
                                             boolean generateTint) {
        this.mRemoteAccentColor = remoteAccentColor;
        if (generateTint) {
            this.mTintRemoteAccentColor = DynamicColorUtils.getTintColor(remoteAccentColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mRemoteAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setRemoteAccentColor(int, boolean)
     */
    public DynamicTheme setRemoteAccentColor(@ColorInt int remoteAccentColor) {
        return setRemoteAccentColor(remoteAccentColor, true);
    }

    /**
     * Getter for {@link #mTintRemotePrimaryColor}.
     */
    public @ColorInt int getTintRemotePrimaryColor() {
        return mTintRemotePrimaryColor;
    }

    /**
     * Setter for {@link #mTintRemotePrimaryColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintRemotePrimaryColor(@ColorInt int tintRemotePrimaryColor) {
        this.mTintRemotePrimaryColor = tintRemotePrimaryColor;

        return this;
    }

    /**
     * Getter for {@link #mTintRemoteAccentColor}.
     */
    public @ColorInt int getTintRemoteAccentColor() {
        return mTintRemoteAccentColor;
    }

    /**
     * Setter for {@link #mTintRemoteAccentColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintRemoteAccentColor(@ColorInt int tintRemoteAccentColor) {
        this.mTintRemoteAccentColor = tintRemoteAccentColor;

        return this;
    }

    /**
     * Getter for {@link #mNotificationBackgroundColor}.
     */
    public @ColorInt int getNotificationBackgroundColor() {
        return mNotificationBackgroundColor;
    }

    /**
     * Setter for {@link #mNotificationBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setNotificationBackgroundColor(int notificationBackgroundColor) {
        this.mNotificationBackgroundColor = notificationBackgroundColor;

        return this;
    }

    /**
     * Getter for {@link #mNotificationPrimaryColor}.
     */
    public @ColorInt int getNotificationPrimaryColor() {
        return mNotificationPrimaryColor;
    }

    /**
     * Setter for {@link #mNotificationPrimaryColor}.
     *
     * @param notificationPrimaryColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mNotificationPrimaryColor}. It will automatically generate
     * tint color also.
     *
     * @see #setNotificationPrimaryColor(int, boolean)
     */
    public DynamicTheme setNotificationPrimaryColor(@ColorInt int notificationPrimaryColor) {
        return setNotificationPrimaryColor(notificationPrimaryColor, true);
    }

    /**
     * Getter for {@link #mNotificationAccentColor}.
     */
    public @ColorInt int getNotificationAccentColor() {
        return mNotificationAccentColor;
    }

    /**
     * Setter for {@link #mNotificationAccentColor}.
     *
     * @param notificationAccentColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mNotificationAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setNotificationAccentColor(int, boolean)
     */
    public DynamicTheme setNotificationAccentColor(@ColorInt int notificationAccentColor) {
        return setNotificationAccentColor(notificationAccentColor, true);
    }

    /**
     * Getter for {@link #mTintNotificationPrimaryColor}.
     */
    public @ColorInt int getTintNotificationPrimaryColor() {
        return mTintNotificationPrimaryColor;
    }

    /**
     * Setter for {@link #mTintNotificationPrimaryColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintNotificationPrimaryColor(
            @ColorInt int tintNotificationPrimaryColor) {
        this.mTintNotificationPrimaryColor = tintNotificationPrimaryColor;

        return this;
    }

    /**
     * Getter for {@link #mTintNotificationAccentColor}.
     */
    public @ColorInt int getTintNotificationAccentColor() {
        return mTintNotificationAccentColor;
    }

    /**
     * Setter for {@link #mTintNotificationAccentColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintNotificationAccentColor(
            @ColorInt int tintNotificationAccentColor) {
        this.mTintNotificationAccentColor = tintNotificationAccentColor;

        return this;
    }

    /**
     * Getter for {@link #mWidgetBackgroundColor}.
     */
    public @ColorInt int getWidgetBackgroundColor() {
        return mWidgetBackgroundColor;
    }

    /**
     * Setter for {@link #mWidgetBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setWidgetBackgroundColor(int widgetBackgroundColor) {
        this.mWidgetBackgroundColor = widgetBackgroundColor;

        return this;
    }

    /**
     * Getter for {@link #mWidgetPrimaryColor}.
     */
    public @ColorInt int getWidgetPrimaryColor() {
        return mWidgetPrimaryColor;
    }

    /**
     * Setter for {@link #mWidgetPrimaryColor}.
     *
     * @param widgetPrimaryColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setWidgetPrimaryColor(@ColorInt int widgetPrimaryColor,
                                              boolean generateTint) {
        this.mWidgetPrimaryColor = widgetPrimaryColor;
        if (generateTint) {
            this.mTintWidgetPrimaryColor =
                    DynamicColorUtils.getTintColor(widgetPrimaryColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mWidgetPrimaryColor}. It will automatically generate
     * tint color also.
     *
     * @see #setWidgetPrimaryColor(int, boolean)
     */
    public DynamicTheme setWidgetPrimaryColor(@ColorInt int widgetPrimaryColor) {
        return setWidgetPrimaryColor(widgetPrimaryColor, true);
    }

    /**
     * Getter for {@link #mWidgetAccentColor}.
     */
    public @ColorInt int getWidgetAccentColor() {
        return mWidgetAccentColor;
    }

    /**
     * Setter for {@link #mWidgetAccentColor}.
     *
     * @param widgetAccentColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setWidgetAccentColor(@ColorInt int widgetAccentColor,
                                             boolean generateTint) {
        this.mWidgetAccentColor = widgetAccentColor;
        if (generateTint) {
            this.mTintWidgetAccentColor =
                    DynamicColorUtils.getTintColor(widgetAccentColor);
        }

        return this;
    }

    /**
     * Setter for {@link #mWidgetAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setWidgetAccentColor(int, boolean)
     */
    public DynamicTheme setWidgetAccentColor(@ColorInt int widgetAccentColor) {
        return setWidgetAccentColor(widgetAccentColor, true);
    }

    /**
     * Getter for {@link #mTintWidgetPrimaryColor}.
     */
    public @ColorInt int getTintWidgetPrimaryColor() {
        return mTintWidgetPrimaryColor;
    }

    /**
     * Setter for {@link #mTintWidgetPrimaryColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintWidgetPrimaryColor(@ColorInt int tintWidgetPrimaryColor) {
        this.mTintWidgetPrimaryColor = tintWidgetPrimaryColor;

        return this;
    }

    /**
     * Getter for {@link #mTintWidgetAccentColor}.
     */
    public @ColorInt int getTintWidgetAccentColor() {
        return mTintWidgetAccentColor;
    }

    /**
     * Setter for {@link #mTintWidgetAccentColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintWidgetAccentColor(@ColorInt int tintWidgetAccentColor) {
        this.mTintWidgetAccentColor = tintWidgetAccentColor;

        return this;
    }

    /**
     * Getter for {@link #mLocalBackgroundColor}.
     */
    public @ColorInt int getLocalBackgroundColor() {
        return mLocalBackgroundColor;
    }

    /**
     * Setter for {@link #mLocalBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mLocalBackgroundColor}. It will automatically generate
     * tint color also.
     *
     * @see #setLocalBackgroundColor(int)
     */
    public DynamicTheme setLocalBackgroundColor(@ColorInt int localBackgroundColor) {
        return setLocalBackgroundColor(localBackgroundColor, true);
    }

    /**
     * Getter for {@link #mTintLocalBackgroundColor}.
     */
    public @ColorInt int getTintLocalBackgroundColor() {
        return mTintLocalBackgroundColor;
    }

    /**
     * Setter for {@link #mTintLocalBackgroundColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintLocalBackgroundColor(@ColorInt int tintLocalBackgroundColor) {
        this.mTintLocalBackgroundColor = tintLocalBackgroundColor;

        return this;
    }

    /**
     * Getter for {@link #mLocalPrimaryColor}.
     */
    public @ColorInt int getLocalPrimaryColor() {
        return mLocalPrimaryColor;
    }

    /**
     * Setter for {@link #mLocalPrimaryColor}.
     *
     * @param localPrimaryColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mLocalPrimaryColor}. It will automatically generate
     * tint color also.
     *
     * @see #setLocalAccentColor(int, boolean)
     */
    public DynamicTheme setLocalPrimaryColor(@ColorInt int localPrimaryColor) {
        return setLocalPrimaryColor(localPrimaryColor, true);
    }

    /**
     * Getter for {@link #mLocalPrimaryColorDark}.
     */
    public @ColorInt int getLocalPrimaryColorDark() {
        return mLocalPrimaryColorDark;
    }

    /**
     * Setter for {@link #mLocalPrimaryColorDark}.
     *
     * @param localPrimaryColorDark Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mLocalPrimaryColorDark}. It will automatically generate
     * tint color also.
     *
     * @see #setLocalAccentColor(int, boolean)
     */
    public DynamicTheme setLocalPrimaryColorDark(@ColorInt int localPrimaryColorDark) {
        return setLocalPrimaryColorDark(localPrimaryColorDark, true);
    }

    /**
     * Getter for {@link #mLocalAccentColor}.
     */
    public @ColorInt int getLocalAccentColor() {
        return mLocalAccentColor;
    }

    /**
     * Setter for {@link #mLocalAccentColor}.
     *
     * @param localAccentColor Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mLocalAccentColor}. It will automatically generate
     * tint color also.
     *
     * @see #setLocalAccentColor(int, boolean)
     */
    public DynamicTheme setLocalAccentColor(@ColorInt int localAccentColor) {
        return setLocalAccentColor(localAccentColor, true);
    }

    /**
     * Getter for {@link #mLocalAccentColorDark}.
     */
    public @ColorInt int getLocalAccentColorDark() {
        return mLocalAccentColorDark;
    }

    /**
     * Setter for {@link #mLocalAccentColorDark}.
     *
     * @param localAccentColorDark Color to set.
     * @param generateTint {@code true} to generate tint color automatically.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
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
     * Setter for {@link #mLocalAccentColorDark}. It will automatically generate
     * tint color also.
     *
     * @see #setLocalAccentColorDark(int, boolean)
     */
    public DynamicTheme setLocalAccentColorDark(@ColorInt int localAccentColorDark) {
        return setLocalAccentColorDark(localAccentColorDark, true);
    }

    /**
     * Getter for {@link #mTintLocalPrimaryColor}.
     */
    public @ColorInt int getTintLocalPrimaryColor() {
        return mTintLocalPrimaryColor;
    }

    /**
     * Setter for {@link #mTintLocalPrimaryColor}.
     *
     * @return {@link DynamicTheme} object to allow for chaining of calls
     *         to set methods.
     */
    public DynamicTheme setTintLocalPrimaryColor(@ColorInt int tintLocalPrimaryColor) {
        this.mTintLocalPrimaryColor = tintLocalPrimaryColor;

        return this;
    }

    /**
     * Getter for {@link #mTintLocalPrimaryColorDark}.
     */
    public @ColorInt int getTintLocalPrimaryColorDark() {
        return mTintLocalPrimaryColorDark;
    }

    /**
     * Setter for {@link #mTintLocalPrimaryColorDark}.
     */
    public DynamicTheme setTintLocalPrimaryColorDark(@ColorInt int tintLocalPrimaryColorDark) {
        this.mTintLocalPrimaryColorDark = tintLocalPrimaryColorDark;

        return this;
    }

    /**
     * Getter for {@link #mTintLocalAccentColor}.
     */
    public @ColorInt int getTintLocalAccentColor() {
        return mTintLocalAccentColor;
    }

    /**
     * Setter for {@link #mTintLocalAccentColor}.
     */
    public DynamicTheme setTintLocalAccentColor(@ColorInt int tintLocalAccentColor) {
        this.mTintLocalAccentColor = tintLocalAccentColor;

        return this;
    }

    /**
     * Getter for {@link #mTintLocalAccentColorDark}.
     */
    public @ColorInt int getTintLocalAccentColorDark() {
        return mTintLocalAccentColorDark;
    }

    /**
     * Setter for {@link #mLocalPrimaryColor}.
     */
    public DynamicTheme setTintLocalAccentColorDark(@ColorInt int tintLocalAccentColorDark) {
        this.mTintLocalAccentColorDark = tintLocalAccentColorDark;

        return this;
    }

    /**
     * Re-create local activity to update all the views with new theme.
     */
    public void recreateLocal() {
        if (mLocalContext == null) {
            throw new IllegalStateException("Not attached to a local mContext");
        }

        if (!(mLocalContext instanceof Activity)) {
            throw new IllegalStateException("Not an instance of Activity");
        }

        ((Activity) mLocalContext).recreate();
    }

    /**
     * Set {@link #sInstance} to null when app exits for better theme
     * results when theme is changed.
     */
    public void onDestroy() {
        if (sInstance == null) {
            return;
        }

        sInstance.mContext = null;
        sInstance.mLocalContext = null;
        sInstance = null;
    }

    /**
     * Set {@link #sInstance} to null when app exits for better theme
     * results when theme is changed.
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
     * Get the currently used context. Generally either the application or
     * an activity
     */
    private Context getResolvedContext() {
        return mLocalContext != null ? mLocalContext : mContext;
    }

    /**
     * Get the default contrast with color to tint the background aware views
     * accordingly.
     */
    public @ColorInt int getDefaultContrastWith() {
        return mLocalContext != null ? mLocalBackgroundColor : mBackgroundColor;
    }

    /**
     * Add DynamicTutorialListener to {@link #mDynamicListeners}.
     */
    public void addDynamicThemeListener(@NonNull Context dynamicThemeListener) {
        if (dynamicThemeListener instanceof DynamicListener) {
            mDynamicListeners.add((DynamicListener) dynamicThemeListener);
        }
    }

    /**
     * Remove DynamicTutorialListener from {@link #mDynamicListeners}.
     */
    public void removeDynamicThemeListener(@NonNull Context dynamicThemeListener) {
        if (dynamicThemeListener instanceof DynamicListener) {
            mDynamicListeners.remove(dynamicThemeListener);
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
     * Save the {@link #mLocalContext} in shared preferences.
     */
    public void saveLocalTheme() {
        if (mLocalContext != null) {
            DynamicPreferences.getInstance().savePrefs(ADS_PREF_THEME,
                    ADS_PREF_THEME_KEY + mLocalContext.getClass().getName(), toString());
        }
    }

    /**
     * Get the supplied context theme from shared preferences.
     *
     * @param context The context to retrieve the theme.
     *
     * @return The supplied context theme from the shared preferences.
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
