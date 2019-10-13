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

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.view.LayoutInflater;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.theme.work.DynamicThemeWork;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Helper class to manage theme for the whole application and its activities.
 * <p>It must be initialized before using any activity or widget as they are
 * heavily dependent on this class to generate colors dynamically.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DynamicTheme implements DynamicListener, DynamicResolver {

    /**
     * Dynamic theme shared preferences.
     */
    public static final String ADS_PREF_THEME = "ads_dynamic_theme";

    /**
     * Key for the theme preference.
     */
    public static final String ADS_PREF_THEME_KEY = "ads_theme_";

    /**
     * Normal delay in milliseconds for updating the views.
     */
    public static final long DELAY_NORMAL = 250;

    /**
     * Theme change delay in milliseconds which will be useful in some situations like changing
     * the app theme, updating the widgets, etc.
     */
    public static final long DELAY_THEME_CHANGE = 150;

    /**
     * Default shift amount to generate the darker color.
     */
    public static final float COLOR_SHIFT_DARK_DEFAULT = 0.863f;

    /**
     * Default primary color used by this theme if no color is supplied.
     */
    private static final @ColorInt int COLOR_PRIMARY_DEFAULT =
            Color.parseColor("#3F51B5");

    /**
     * Default dark primary color used by this theme if no color is supplied.
     */
    private static final @ColorInt int COLOR_PRIMARY_DARK_DEFAULT =
            Color.parseColor("#303F9F");

    /**
     * Default accent color used by this theme if no color is supplied.
     */
    private static final @ColorInt int COLOR_ACCENT_DEFAULT =
            Color.parseColor("#E91E63");

    /**
     * Default font scale for the theme.
     */
    public static final int FONT_SCALE_DEFAULT = 100;

    /**
     * Default corner size for the theme.
     */
    private static final int CORNER_SIZE_DEFAULT = DynamicUnitUtils.convertDpToPixels(2);

    /**
     * {@code true} if power save mode is enabled.
     */
    private boolean mPowerSaveMode;

    /**
     * Default theme used by the application.
     */
    private DynamicAppTheme mDefaultApplicationTheme;

    /**
     * Default theme used by the local context.
     */
    private DynamicAppTheme mDefaultLocalTheme;

    /**
     * Theme used by the application.
     */
    private DynamicAppTheme mApplicationTheme;

    /**
     * Theme used by the local context.
     */
    private DynamicAppTheme mLocalTheme;

    /**
     * Theme used by the remote elements.
     */
    private DynamicWidgetTheme mRemoteTheme;

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
     * Broadcast receiver to listen various events.
     */
    private BroadcastReceiver mBroadcastReceiver;

    /**
     * Power manager to perform battery and screen related events.
     */
    private PowerManager mPowerManager;

    /**
     * Collection of dynamic listeners to send them event callback.
     */
    private List<DynamicListener> mDynamicListeners;

    /**
     * Resolver used by the dynamic theme.
     */
    private DynamicResolver mDynamicResolver;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #initializeInstance(Context, DynamicResolver)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    protected DynamicTheme() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The application context to be attached with the dynamic theme.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    private DynamicTheme(@NonNull Context context,
            @Nullable DynamicResolver dynamicResolver) {
        this.mContext = context;
        this.mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        this.mDynamicListeners = new ArrayList<>();
        this.mDynamicResolver = dynamicResolver != null ? dynamicResolver : this;
        this.mDefaultApplicationTheme = new DynamicAppTheme(COLOR_PRIMARY_DEFAULT,
                COLOR_PRIMARY_DARK_DEFAULT, COLOR_ACCENT_DEFAULT, FONT_SCALE_DEFAULT,
                CORNER_SIZE_DEFAULT, Theme.BackgroundAware.ENABLE);
        this.mApplicationTheme = new DynamicAppTheme();
        this.mRemoteTheme = new DynamicWidgetTheme();

        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    if (intent.getAction().equals(
                            PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)) {
                        mPowerSaveMode = mPowerManager.isPowerSaveMode();
                        onPowerSaveModeChange(mPowerSaveMode);
                    } else {
                        setDynamicThemeWork(!WorkManager.getInstance(context)
                                .getWorkInfosForUniqueWork(DynamicThemeWork.TAG).isDone());
                        onAutoThemeChange();
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        if (DynamicSdkUtils.is21()) {
            intentFilter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
            this.mPowerSaveMode = mPowerManager.isPowerSaveMode();
        } else {
            this.mPowerSaveMode = false;
        }
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * Sets the dynamic theme work to schedule auto theme event according to the time.
     *
     * @param enqueue {@code true} to enqueue the dynamic theme work.
     */
    public void setDynamicThemeWork(boolean enqueue) {
        if (enqueue) {
            long delay;
            Date date = new Date();
            if (isNight()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getDynamicResolver().getNightTimeEnd());
                if (date.after(calendar.getTime())) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                delay = calendar.getTimeInMillis() - date.getTime();
            } else {
                delay = getDynamicResolver().getNightTimeStart().getTime() - date.getTime();
            }

            WorkManager.getInstance(mContext).enqueueUniqueWork(
                    DynamicThemeWork.TAG, ExistingWorkPolicy.REPLACE,
                    new OneTimeWorkRequest.Builder(DynamicThemeWork.class)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .build());
        } else {
            WorkManager.getInstance(mContext).cancelUniqueWork(DynamicThemeWork.TAG);
        }
    }

    /**
     * Attach a local context to this theme.
     * <p>It can be an activity in case different themes are required for different activities.
     *
     * @param localContext The context to be attached with this theme.
     * @param layoutInflater The layout inflater factory for the local context.
     *                       <p>{@code null} to use no custom layout inflater.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public DynamicTheme attach(@NonNull Context localContext,
            @Nullable LayoutInflater.Factory2 layoutInflater) {
        this.mLocalContext = localContext;
        this.mDefaultLocalTheme = new DynamicAppTheme(COLOR_PRIMARY_DEFAULT,
                COLOR_PRIMARY_DARK_DEFAULT, COLOR_ACCENT_DEFAULT, FONT_SCALE_DEFAULT,
                CORNER_SIZE_DEFAULT, Theme.BackgroundAware.ENABLE);
        this.mLocalTheme = new DynamicAppTheme();

        if (localContext instanceof Activity && layoutInflater != null
                && ((Activity) localContext).getLayoutInflater().getFactory2() == null) {
            LayoutInflaterCompat.setFactory2(((Activity) localContext)
                    .getLayoutInflater(), layoutInflater);
        }

        return this;
    }

    /**
     * Attach a local context to this theme.
     * <p>It can be an activity in case different themes are required for different activities.
     *
     * @param localContext The context to be attached with this theme.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public DynamicTheme attach(@NonNull Context localContext) {
        return attach(localContext, new DynamicLayoutInflater());
    }

    /**
     * Initialize theme when application starts.
     * <p>Must be initialized once.
     *
     * @param context The context to retrieve resources.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    public static synchronized void initializeInstance(@Nullable Context context,
            @Nullable DynamicResolver dynamicResolver) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        if (sInstance == null) {
            sInstance = new DynamicTheme(context, dynamicResolver);
        }
    }

    /**
     * Get instance to access public methods.
     * <p>Must be called before accessing methods.
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
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setThemeRes(@StyleRes int theme, boolean initializeRemoteColors) {
        if (theme != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mContext.getTheme().applyStyle(theme, true);

            mDefaultApplicationTheme.setThemeRes(theme)
                    .setBackgroundColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, android.R.attr.windowBackground,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setSurfaceColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, R.attr.colorSurface, DynamicAppTheme.AUTO))
                    .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, R.attr.colorOnSurface, DynamicAppTheme.AUTO))
                    .setPrimaryColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, R.attr.colorPrimary,
                            mDefaultApplicationTheme.getPrimaryColor()))
                    .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                            mContext, theme, R.attr.colorPrimaryDark,
                            mDefaultApplicationTheme.getPrimaryColorDark()))
                    .setAccentColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, R.attr.colorAccent,
                            mDefaultApplicationTheme.getAccentColor()))
                    .setAccentColorDark(mApplicationTheme.getAccentColor())
                    .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, android.R.attr.textColorPrimary,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                            mContext, theme, android.R.attr.textColorSecondary,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                            mContext, theme, android.R.attr.textColorPrimaryInverse,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                            mContext, theme, android.R.attr.textColorSecondaryInverse,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setFontScale(DynamicResourceUtils.resolveInteger(
                            mContext, theme, R.attr.ads_fontScale,
                            mDefaultApplicationTheme.getFontScale()))
                    .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                            mContext, theme, R.attr.ads_cornerRadius,
                            mDefaultApplicationTheme.getCornerRadius()))
                    .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                            mContext, theme, R.attr.ads_backgroundAware,
                            mDefaultApplicationTheme.getBackgroundAware()));

            mApplicationTheme = new DynamicAppTheme(mDefaultApplicationTheme);

            if (initializeRemoteColors) {
                initializeRemoteColors();
            }
        }

        return this;
    }

    /**
     * Initialize colors form the supplied dynamic app theme.
     *
     * @param theme The dynamic app theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setTheme(@Nullable DynamicAppTheme theme,
            boolean initializeRemoteColors) {
        if (theme != null) {
            if (theme.getThemeRes() == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
                throw new IllegalStateException("Dynamic app theme style resource " +
                        "id is not found for the application theme.");
            }

            setThemeRes(theme.getThemeRes(), false);
            mApplicationTheme = new DynamicAppTheme(theme);

            if (initializeRemoteColors) {
                initializeRemoteColors();
            }
        }

        return this;
    }

    /**
     * Initialize colors form the supplied local theme resource.
     *
     * @param localTheme The local theme resource to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setLocalThemeRes(@StyleRes int localTheme) {
        if (mLocalContext == null) {
            throw new IllegalStateException("Not attached to a local context.");
        }

        if (localTheme != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mLocalContext.getTheme().applyStyle(localTheme, true);

            mDefaultLocalTheme.setThemeRes(localTheme)
                    .setBackgroundColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, android.R.attr.windowBackground,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setSurfaceColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, R.attr.colorSurface, DynamicAppTheme.AUTO))
                    .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, R.attr.colorOnSurface, DynamicAppTheme.AUTO))
                    .setPrimaryColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, R.attr.colorPrimary,
                            mDefaultLocalTheme.getPrimaryColor()))
                    .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, R.attr.colorPrimaryDark,
                            mDefaultLocalTheme.getPrimaryColorDark()))
                    .setAccentColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, R.attr.colorAccent,
                            mDefaultLocalTheme.getAccentColor()))
                    .setAccentColorDark(mLocalTheme.getAccentColor())
                    .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, android.R.attr.textColorPrimary,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, android.R.attr.textColorSecondary,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, android.R.attr.textColorPrimaryInverse,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                            mContext, localTheme, android.R.attr.textColorSecondaryInverse,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                    .setFontScale(DynamicResourceUtils.resolveInteger(
                            mContext, localTheme, R.attr.ads_fontScale,
                            mDefaultLocalTheme.getFontScale()))
                    .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                            mContext, localTheme, R.attr.ads_cornerRadius,
                            mDefaultLocalTheme.getCornerRadius()))
                    .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                            mContext, localTheme, R.attr.ads_backgroundAware,
                            mDefaultLocalTheme.getBackgroundAware()));

            mLocalTheme = new DynamicAppTheme(mDefaultLocalTheme);
            addDynamicListener(mLocalContext);
        }

        return this;
    }

    /**
     * Initialize colors form the supplied local dynamic app theme.
     *
     * @param dynamicLocalTheme The local dynamic app theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setLocalTheme(@Nullable DynamicAppTheme dynamicLocalTheme) {
        if (dynamicLocalTheme != null) {
            if (dynamicLocalTheme.getThemeRes() == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
                throw new IllegalStateException("Dynamic app theme style resource " +
                        "id is not found for the application theme.");
            }

            setLocalThemeRes(dynamicLocalTheme.getThemeRes());
            mLocalTheme = new DynamicAppTheme(dynamicLocalTheme);
        }

        return this;
    }

    /**
     * Initialize remote colors according to the base colors.
     * <p>They can be set individually by calling the appropriate methods.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme initializeRemoteColors() {
        mRemoteTheme = (DynamicWidgetTheme) new DynamicWidgetTheme(mApplicationTheme)
                .setBackgroundColor(ContextCompat.getColor(getResolvedContext(),
                !DynamicSdkUtils.is21()
                        ? R.color.notification_background
                        : R.color.notification_background_light));

        return this;
    }

    /**
     * Resolve background aware according to the constant value.
     *
     * @param backgroundAware The background aware to be resolved.
     *
     * @return The resolved background aware value.
     *         <p>Either {@link Theme.BackgroundAware#ENABLE}
     *         or {@link Theme.BackgroundAware#DISABLE}.
     *
     * @see Theme.BackgroundAware
     */
    public @Theme.BackgroundAware int resolveBackgroundAware(
            @Theme.BackgroundAware int backgroundAware) {
        if (backgroundAware == Theme.BackgroundAware.AUTO) {
            return DynamicTheme.getInstance().get().getBackgroundAware();
        }

        return backgroundAware;
    }

    /**
     * Resolve color according to the color type.
     *
     * @param colorType The color type to be resolved.
     *
     * @return The resolved color value.
     *
     * @see Theme.ColorType
     */
    public @ColorInt int resolveColorType(@Theme.ColorType int colorType) {
        switch (colorType) {
            default: return Theme.ColorType.NONE;
            case Theme.ColorType.PRIMARY: return get().getPrimaryColor();
            case Theme.ColorType.PRIMARY_DARK: return get().getPrimaryColorDark();
            case Theme.ColorType.ACCENT: return get().getAccentColor();
            case Theme.ColorType.ACCENT_DARK: return get().getAccentColorDark();
            case Theme.ColorType.TINT_PRIMARY: return get().getTintPrimaryColor();
            case Theme.ColorType.TINT_PRIMARY_DARK: return get().getTintPrimaryColorDark();
            case Theme.ColorType.TINT_ACCENT: return get().getTintAccentColor();
            case Theme.ColorType.TINT_ACCENT_DARK: return get().getTintAccentColorDark();
            case Theme.ColorType.BACKGROUND: return get().getBackgroundColor();
            case Theme.ColorType.TINT_BACKGROUND: return get().getTintBackgroundColor();
            case Theme.ColorType.TEXT_PRIMARY: return get().getTextPrimaryColor();
            case Theme.ColorType.TEXT_SECONDARY: return get().getTextSecondaryColor();
            case Theme.ColorType.TEXT_PRIMARY_INVERSE: return get().getTextPrimaryColorInverse();
            case Theme.ColorType.TEXT_SECONDARY_INVERSE: return get().getTextSecondaryColorInverse();
            case Theme.ColorType.SURFACE: return get().getSurfaceColor();
            case Theme.ColorType.TINT_SURFACE: return get().getTintSurfaceColor();
        }
    }

    /**
     * Get the application context.
     *
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
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setContext(@NonNull Context context) {
        this.mContext = context;

        return this;
    }

    /**
     * Get the local context.
     *
     * @return The local context.
     */
    public @Nullable Context getLocalContext() {
        return mLocalContext;
    }

    /**
     * Get the power manager used by the application.
     *
     * @return The power manager used by the application.
     */
    public PowerManager getPowerManager() {
        return mPowerManager;
    }

    /**
     * Get the theme used by the application.
     *
     * @return The theme used by the application.
     */
    public DynamicAppTheme getApplication() {
        return mApplicationTheme;
    }

    /**
     * Get the theme used by the local context.
     *
     * @return The theme used by the local context.
     */
    public DynamicAppTheme getLocal() {
        return mLocalTheme;
    }

    /**
     * @return The theme used by the remote views.
     */
    public DynamicWidgetTheme getRemote() {
        return mRemoteTheme;
    }

    /**
     * Get the theme according to the current state.
     * <p>Either application theme or local theme.
     *
     * @return The theme according to the current state.
     */
    public DynamicAppTheme get() {
        return mLocalContext != null ? mLocalTheme : mApplicationTheme;
    }

    /**
     * Get the default theme according to the current state.
     * <p>Either default application theme or default local theme.
     *
     * @return The default theme according to the current state.
     */
    public DynamicAppTheme getDefault() {
        return mLocalContext != null ? mDefaultLocalTheme : mDefaultApplicationTheme;
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
     * Set the initialized instance to {@code null} when app terminates for better theme
     * results when theme is changed.
     */
    public void onDestroy() {
        if (sInstance == null) {
            return;
        }

        mContext.unregisterReceiver(mBroadcastReceiver);
        mContext = null;
        mLocalContext = null;
        mBroadcastReceiver = null;
        mDefaultApplicationTheme = null;
        mApplicationTheme = null;
        mDefaultLocalTheme = null;
        mLocalTheme = null;
        mRemoteTheme = null;
        sInstance.mContext = null;
        sInstance.mApplicationTheme = null;
        sInstance.mDefaultApplicationTheme = null;
        sInstance.mDefaultLocalTheme = null;
        sInstance.mLocalContext = null;
        sInstance.mLocalTheme = null;
        sInstance.mRemoteTheme = null;
        sInstance = null;

        clearDynamicListeners();
    }

    /**
     * Set the initialized instance to {@code null} when local destroys for better theme
     * results when theme is changed.
     */
    public void onLocalDestroy() {
        if (sInstance == null) {
            return;
        }

        removeDynamicListener(mLocalContext);
        saveLocalTheme();

        mLocalContext = null;
        mLocalTheme = null;
    }

    /**
     * Generates default theme according to the current settings.
     *
     * @return The generated default theme.
     */
    public @NonNull DynamicAppTheme generateDefaultTheme() {
        return new DynamicAppTheme().setBackgroundColor(
                getDefault().getBackgroundColor(), false);
    }

    /**
     * Generates surface color according to the supplied color.
     *
     * @param color The color to generate the surface color.
     *
     * @return The generated surface color.
     */
    public @ColorInt int generateSurfaceColor(@ColorInt int color) {
        return DynamicColorUtils.isColorDark(color)
                ? DynamicColorUtils.getLighterColor(color, 0.09f)
                : DynamicColorUtils.getLighterColor(color, 0.2f);
    }

    /**
     * Generates dark variant of the supplied color.
     *
     * @param color The color to generate the dark variant.
     *
     * @return The generated dark variant of the color.
     */
    public @ColorInt int generateDarkColor(@ColorInt int color) {
        return DynamicColorUtils.shiftColor(color, DynamicTheme.COLOR_SHIFT_DARK_DEFAULT);
    }

    /**
     * Returns the currently used context.
     * <p>Generally, either application or an activity.
     *
     * @return The currently used context.
     */
    private Context getResolvedContext() {
        return mLocalContext != null ? mLocalContext : mContext;
    }

    /**
     * Returns the default contrast with color to tint the background aware views accordingly.
     *
     * @return The default contrast with color.
     */
    public @ColorInt int getDefaultContrastWith() {
        return get().getBackgroundColor();
    }

    /**
     * Returns the resolver used by the dynamic theme.
     *
     * @return The resolver used by the dynamic theme.
     */
    public @NonNull DynamicResolver getDynamicResolver() {
        return mDynamicResolver != null ? mDynamicResolver : this;
    }

    /**
     * Sets the resolver used by the dynamic theme.
     *
     * @param dynamicResolver The resolver to be set.
     */
    public void setDynamicResolver(@Nullable DynamicResolver dynamicResolver) {
        this.mDynamicResolver = dynamicResolver;
    }

    /**
     * Add a dynamic listener to receive the various callbacks.
     *
     * @param dynamicListener The dynamic listener to be added.
     *
     * @see DynamicListener
     */
    public void addDynamicListener(@Nullable Context dynamicListener) {
        if (dynamicListener instanceof DynamicListener
                && !mDynamicListeners.contains(dynamicListener)) {
            mDynamicListeners.add((DynamicListener) dynamicListener);
        }
    }

    /**
     * Remove a dynamic listener.
     *
     * @param dynamicListener The dynamic listener to be removed.
     *
     * @see DynamicListener
     */
    public void removeDynamicListener(@Nullable Context dynamicListener) {
        if (dynamicListener instanceof DynamicListener) {
            mDynamicListeners.remove(dynamicListener);
        }
    }

    /**
     * Checks whether a dynamic listener is already registered.
     *
     * @param dynamicListener The dynamic listener to be checked.
     *
     * @see DynamicListener
     */
    public boolean isDynamicListener(@Nullable Context dynamicListener) {
        if (!(dynamicListener instanceof DynamicListener)) {
            return false;
        }

        return mDynamicListeners.contains(dynamicListener);
    }

    /**
     * Remove all the dynamic listeners.
     */
    public void clearDynamicListeners() {
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
    public void onAutoThemeChange() {
        for (DynamicListener dynamicListener : mDynamicListeners) {
            dynamicListener.onAutoThemeChange();
        }
    }

    @Override
    public void onPowerSaveModeChange(boolean powerSaveMode) {
        for (DynamicListener dynamicListener : mDynamicListeners) {
            dynamicListener.onPowerSaveModeChange(powerSaveMode);
        }
    }

    @Override
    public @NonNull String toString() {
        StringBuilder theme = new StringBuilder();

        if (mApplicationTheme != null) {
            theme.append(mApplicationTheme.toString());
        }
        if (mLocalTheme != null) {
            theme.append(mLocalTheme.toString());
        }
        if (mRemoteTheme != null) {
            theme.append(mRemoteTheme.toString());
        }

        return theme.toString();
    }

    /**
     * Save the local context theme in shared preferences.
     */
    public void saveLocalTheme() {
        if (mLocalContext != null) {
            DynamicPreferences.getInstance().save(ADS_PREF_THEME,
                    ADS_PREF_THEME_KEY + mLocalContext.getClass().getName(), toString());
        }
    }

    /**
     * Returns the supplied context theme from the shared preferences.
     *
     * @param context The context to retrieve the theme.
     *
     * @return The supplied context theme from shared preferences.
     */
    public @Nullable String getLocalTheme(@NonNull Context context) {
        return DynamicPreferences.getInstance().load(ADS_PREF_THEME,
                ADS_PREF_THEME_KEY + context.getClass().getName(), null);
    }

    /**
     * Delete the supplied context theme from shared preferences.
     *
     * @param context The context to delete the theme.
     */
    public void deleteLocalTheme(@NonNull Context context) {
        try {
            DynamicPreferences.getInstance().delete(ADS_PREF_THEME,
                    ADS_PREF_THEME_KEY + context.getClass().getName());
        } catch (Exception ignored) {
        }
    }

    /**
     * Returns the dynamic app theme from the Json string.
     *
     * @param theme The dynamic app theme Json string to be converted.
     *
     * @return The dynamic app theme from the Json string.
     */
    public @Nullable DynamicAppTheme getTheme(@Nullable String theme) {
        return new Gson().fromJson(theme, DynamicAppTheme.class);
    }

    @Override
    public boolean isSystemNightMode() {
        return (getContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public boolean isNight() {
        Date date = new Date();
        return date.getTime() >= getNightTimeStart().getTime()
                || date.getTime() < getNightTimeEnd().getTime();
    }

    @Override
    public boolean isNight(@Theme int theme) {
        return theme == Theme.NIGHT || (theme == Theme.AUTO && isNight());
    }

    @Override
    public boolean isNight(@Theme.ToString String theme) {
        return getDynamicResolver().isNight(Integer.valueOf(theme));
    }

    @Override
    public @NonNull Date getNightTimeStart() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    @Override
    public @NonNull Date getNightTimeEnd() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    @Override
    public boolean resolveNightTheme(@Theme int appTheme, @Theme.Night int implementation) {
        if (appTheme == Theme.AUTO) {
            switch (implementation) {
                default:
                case Theme.Night.SYSTEM:
                    return getDynamicResolver().isSystemNightMode();
                case Theme.Night.CUSTOM:
                    return false;
                case Theme.Night.AUTO:
                    return getDynamicResolver().isNight(appTheme);
                case Theme.Night.BATTERY:
                    return mPowerSaveMode;
            }
        }

        return appTheme == Theme.NIGHT;
    }

    @Override
    public boolean resolveNightTheme(@Theme.ToString String appTheme,
            @Theme.Night.ToString String implementation) {
        return getDynamicResolver().resolveNightTheme(
                Integer.valueOf(appTheme), Integer.valueOf(implementation));
    }
}
