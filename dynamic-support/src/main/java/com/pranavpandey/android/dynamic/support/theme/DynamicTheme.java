/*
 * Copyright 2018-2024 Pranav Pandey
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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.UiModeManager;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicRemoteTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.preview.activity.DynamicPreviewActivity;
import com.pranavpandey.android.dynamic.support.theme.inflater.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.theme.task.WallpaperColorsTask;
import com.pranavpandey.android.dynamic.support.theme.work.DynamicThemeWork;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.DynamicColors;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicTaskUtils;
import com.pranavpandey.android.dynamic.util.DynamicUnitUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.product.DynamicFlavor;
import com.pranavpandey.android.dynamic.util.product.DynamicProductFlavor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Helper class to manage theme for the whole application and its activities.
 * <p>It must be initialized before using any activity or widget as they are
 * heavily dependent on this class to generate colors dynamically.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DynamicTheme implements DynamicProductFlavor, DynamicListener, DynamicResolver {

    /**
     * Constant values for the theme styles version.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {

        /**
         * Key constant for the theme version.
         */
        String KEY = "ads_theme_version";

        /**
         * Constant for the auto version.
         */
        int AUTO = -3;

        /**
         * Constant for the version 1.
         */
        int INT_1 = 1;

        /**
         * Constant for the version 2.
         */
        int INT_2 = 2;

        /**
         * Constant for the version 3.
         */
        int INT_3 = 3;

        /**
         * Constant for the default version.
         */
        int DEFAULT = AUTO;

        /**
         * Constant for the default auto version.
         */
        int DEFAULT_AUTO = com.google.android.material.color.DynamicColors.isDynamicColorAvailable()
                ? (DynamicSdkUtils.is36() ? Version.INT_3 : Version.INT_2) : Version.INT_1;

        /**
         * String constant values for the theme styles version.
         */
        @Retention(RetentionPolicy.SOURCE)
        @interface ToString {

            /**
             * String constant for the unknown version.
             */
            String AUTO = "-3";

            /**
             * String constant for the version 1.
             */
            String INT_1 = "1";

            /**
             * String constant for the version 2.
             */
            String INT_2 = "2";

            /**
             * String constant for the version 3.
             */
            String INT_3 = "3";

            /**
             * String constant for the default version.
             */
            String DEFAULT = AUTO;

            /**
             * String constant for the default auto version.
             */
            String DEFAULT_AUTO = Integer.toString(Version.DEFAULT_AUTO);
        }
    }

    /**
     * Tag for the dynamic theme.
     */
    private static final String TAG = "Dynamic Theme";

    /**
     * Key for the theme preference.
     */
    public static final String ADS_PREF_THEME_KEY = "ads_theme_";

    /**
     * Normal delay in milliseconds for updating the views.
     */
    public static final long DELAY_NORMAL = DynamicMotion.Duration.SHORT;

    /**
     * Theme change delay in milliseconds which will be useful in some situations like changing
     * the app theme, updating the widgets, etc.
     */
    public static final long DELAY_THEME_CHANGE = DynamicMotion.Duration.SHORTER;

    /**
     * Default shift amount to generate the darker color.
     */
    public static final float COLOR_SHIFT_DARK_DEFAULT = 0.863f;

    /**
     * Default primary color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_BACKGROUND_DEFAULT =
            Color.parseColor("#EAEAEA");

    /**
     * Default primary color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_PRIMARY_DEFAULT =
            Color.parseColor("#3F51B5");

    /**
     * Default dark primary color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_PRIMARY_DARK_DEFAULT =
            Color.parseColor("#303F9F");

    /**
     * Default accent color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_ACCENT_DEFAULT =
            Color.parseColor("#E91E63");

    /**
     * Default text primary color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_TEXT_PRIMARY_DEFAULT = Color.BLACK;

    /**
     * Default text secondary color used by this theme if no color is supplied.
     */
    public static final @ColorInt int COLOR_TEXT_SECONDARY_DEFAULT = Color.GRAY;

    /**
     * Default font scale for the theme.
     */
    public static final int FONT_SCALE_DEFAULT = 100;

    /**
     * Default corner size for the theme.
     */
    private static final int CORNER_SIZE_DEFAULT =
            DynamicUnitUtils.convertDpToPixels(Theme.Corner.DEFAULT);

    /**
     * Singleton instance of {@link DynamicTheme}.
     */
    @SuppressLint("StaticFieldLeak")
    private static DynamicTheme sInstance;

    /**
     * Application theme styles version.
     */
    private @Version int mVersion;

    /**
     * Locale theme styles version.
     */
    private @Version int mLocalVersion;

    /**
     * Main thread handler to publish results.
     */
    private final DynamicThemeHandler mHandler;

    /**
     * Dynamic application listener used by this theme instance.
     */
    private DynamicListener mListener;

    /**
     * Dynamic local listener used by this theme instance.
     */
    private WeakReference<DynamicListener> mLocalListener;

    /**
     * Broadcast receiver to listen various events.
     */
    private BroadcastReceiver mBroadcastReceiver;

    /**
     * UI mode manager to perform system related operations.
     */
    private UiModeManager mUiModeManager;

    /**
     * Power manager to perform battery and screen related events.
     */
    private PowerManager mPowerManager;

    /**
     * {@code true} if power save mode is enabled.
     */
    private boolean mPowerSaveMode;

    /**
     * Default theme used by the application.
     */
    private DynamicAppTheme mDefaultApplicationTheme;

    /**
     * Theme used by the application.
     */
    private DynamicAppTheme mApplicationTheme;

    /**
     * Default theme used by the local context.
     */
    private DynamicAppTheme mDefaultLocalTheme;

    /**
     * Theme used by the local context.
     */
    private DynamicAppTheme mLocalTheme;

    /**
     * Theme used by the remote elements.
     */
    private DynamicRemoteTheme mRemoteTheme;

    /**
     * Collection of dynamic themes to properly resume them.
     */
    private final Map<String, String> mDynamicThemes;

    /**
     * Resolver used by the dynamic theme.
     */
    private DynamicResolver mDynamicResolver;

    /**
     * Dynamic colors task used by the dynamic theme.
     */
    private WallpaperColorsTask mWallpaperColorsTask;

    /**
     * Listener to update theme on wallpaper colors change.
     */
    private WallpaperManager.OnColorsChangedListener mOnColorsChangedListener;

    /**
     * Preview activity used by this manager.
     */
    private Class<?> mPreviewActivity;

    /**
     * Making default constructor private so that it cannot be initialized without a listener.
     * <p>Use {@link #initializeInstance(DynamicListener, DynamicResolver)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private DynamicTheme() {
        this.mVersion = Version.DEFAULT_AUTO;
        this.mLocalVersion = Version.DEFAULT_AUTO;
        this.mHandler = new DynamicThemeHandler(Looper.getMainLooper(), new ArrayList<>());
        this.mDynamicThemes = new HashMap<>();
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param listener The application dynamic listener to be attached with this theme.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    private DynamicTheme(@Nullable DynamicListener listener,
            @Nullable DynamicResolver dynamicResolver) {
        this();
        
        if (listener != null) {
            initialize(listener, dynamicResolver);
        }
    }

    /**
     * Initialize this class with default values.
     *
     * @param listener The application dynamic listener to be attached with this theme.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    public void initialize(@NonNull DynamicListener listener,
            @Nullable DynamicResolver dynamicResolver) {
        DynamicPermissions.initializeInstance(listener.getContext());
        
        this.mListener = listener;
        this.mUiModeManager = ContextCompat.getSystemService(
                mListener.getContext(), UiModeManager.class);
        this.mPowerManager = ContextCompat.getSystemService(
                mListener.getContext(), PowerManager.class);
        this.mDynamicResolver = dynamicResolver;
        this.mDefaultApplicationTheme = new DynamicAppTheme()
                .setHost(true)
                .setFontScale(FONT_SCALE_DEFAULT)
                .setCornerRadius(CORNER_SIZE_DEFAULT)
                .setBackgroundAware(Theme.BackgroundAware.ENABLE)
                .setContrast(Theme.Contrast.DEFAULT)
                .setOpacity(Theme.Opacity.DEFAULT)
                .setElevation(Theme.Elevation.ENABLE);
        this.mApplicationTheme = new DynamicAppTheme().setHost(true);

        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    if (PowerManager.ACTION_POWER_SAVE_MODE_CHANGED.equals(intent.getAction())) {
                        mPowerSaveMode = getPowerManager().isPowerSaveMode();
                        onPowerSaveModeChanged(mPowerSaveMode);
                    } else if (Intent.ACTION_WALLPAPER_CHANGED.equals(intent.getAction())) {
                        setWallpaperColors(isDynamicColor(), false);
                    } else {
                        setDynamicThemeWork(!WorkManager.getInstance(context)
                                .getWorkInfosForUniqueWork(DynamicThemeWork.TAG).isDone());
                        onAutoThemeChanged(false);
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
            this.mPowerSaveMode = getPowerManager().isPowerSaveMode();
        } else {
            this.mPowerSaveMode = false;
        }
        ContextCompat.registerReceiver(mListener.getContext(), mBroadcastReceiver,
                intentFilter, ContextCompat.RECEIVER_EXPORTED);

        setRemoteTheme(null);
        addDynamicListener(listener);
    }

    /**
     * Sets the dynamic theme work to schedule auto theme event according to the time.
     *
     * @param enqueue {@code true} to enqueue the dynamic theme work.
     */
    public void setDynamicThemeWork(boolean enqueue) {
        try {
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

                WorkManager.getInstance(getContext()).enqueueUniqueWork(
                        DynamicThemeWork.TAG, ExistingWorkPolicy.REPLACE,
                        new OneTimeWorkRequest.Builder(DynamicThemeWork.class)
                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                .build());
            } else {
                WorkManager.getInstance(getContext()).cancelUniqueWork(DynamicThemeWork.TAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the {@link WallpaperManager.OnColorsChangedListener} to enable dynamic colors from
     * the wallpaper on API 27 and above. Otherwise, use the custom implementation for older
     * API levels.
     *
     * @param enable {@code true} to enable wallpaper colors listener.
     * @param context {@code true} if there is a context change and it must be reinitialized.
     *
     * @see DynamicListener#onDynamicColorsChanged(DynamicColors, boolean)
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void setWallpaperColors(boolean enable, final boolean context) {
        if (!isDynamicColors()) {
            return;
        }

        if (DynamicSdkUtils.is27()) {
            if (mOnColorsChangedListener == null) {
                mOnColorsChangedListener = new WallpaperManager.OnColorsChangedListener() {
                    @Override
                    public void onColorsChanged(WallpaperColors colors, int which) {
                        getColors(false).putOriginal(colors);
                        getColors().putOriginal(colors);
                        onDynamicColorsChanged(getColors(), context);
                    }
                };
            }

            WallpaperManager.getInstance(getListener().getContext())
                    .removeOnColorsChangedListener(mOnColorsChangedListener);

            if (enable) {
                WallpaperManager.getInstance(getListener().getContext())
                        .addOnColorsChangedListener(mOnColorsChangedListener, getHandler());
            }
        }

        DynamicTaskUtils.cancelTask(mWallpaperColorsTask, true);

        if (enable) {
            mWallpaperColorsTask = new WallpaperColorsTask(getContext()) {
                @Override
                protected void onPostExecute(
                        @Nullable DynamicResult<Map<Integer, Integer>> result) {
                    super.onPostExecute(result);

                    if (result != null && result.getData() != null) {
                        getColors(false).putOriginal(result.getData());
                        getColors().putOriginal(result.getData());
                        onDynamicColorsChanged(getColors(), context);
                    }
                }
            };

            mWallpaperColorsTask.execute();
        } else {
            getColors(false).clear();
            getColors().clear();
            onDynamicColorsChanged(getColors(), context);
        }
    }

    /**
     * Attach a local dynamic listener to this theme.
     * <p>It can be an activity in case different themes are required for different activities.
     *
     * @param localListener The local dynamic listener to be attached with this theme.
     * @param layoutInflater The layout inflater factory for the local context.
     *                       <p>{@code null} to use no custom layout inflater.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme attach(@NonNull DynamicListener localListener,
            @Nullable LayoutInflater.Factory2 layoutInflater) {
        this.mLocalListener = new WeakReference<>(localListener);
        this.mDefaultLocalTheme = new DynamicAppTheme(getDefaultApplication());
        this.mLocalTheme = new DynamicAppTheme();

        if (localListener instanceof Activity && layoutInflater != null
                && ((Activity) localListener).getLayoutInflater().getFactory2() == null) {
            LayoutInflaterCompat.setFactory2(((Activity) localListener)
                    .getLayoutInflater(), layoutInflater);
        }

        addDynamicListener(getLocalListener());
        return this;
    }

    /**
     * Attach a local dynamic listener to this theme.
     * <p>It can be an activity in case different themes are required for different activities.
     *
     * @param localListener The local dynamic listener to be attached with this theme.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme attach(@NonNull DynamicListener localListener) {
        return attach(localListener, new DynamicLayoutInflater());
    }

    /**
     * Initialize theme when application starts.
     * <p>Must be initialized once.
     *
     * @param listener The dynamic listener to retrieve resources.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    public static synchronized void initializeInstance(@Nullable DynamicListener listener,
            @Nullable DynamicResolver dynamicResolver) {
        if (listener == null) {
            throw new NullPointerException("Context should not be null.");
        }

        if (sInstance == null) {
            sInstance = new DynamicTheme(listener, dynamicResolver);
        }
    }

    /**
     * Retrieves the singleton instance of {@link DynamicTheme}.
     * <p>Must be called before accessing the public methods.
     *
     * @return The singleton instance of {@link DynamicTheme}.
     */
    public static synchronized @NonNull DynamicTheme getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicTheme.class.getSimpleName() +
                    " is not initialized, call initializeInstance(...) method first.");
        }

        return sInstance;
    }

    /**
     * Get the main thread handler.
     *
     * @return The main thread handler.
     */
    public @NonNull DynamicThemeHandler getHandler() {
        return mHandler;
    }

    /**
     * Returns the style resource used by the system.
     *
     * @param listener The dynamic listener to be used.
     * @param theme The dynamic theme to be used.
     *
     * @return The style resource used by the system.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public @StyleRes int getSystemThemeRes(@Nullable DynamicListener listener,
            @NonNull AppTheme<?> theme) {
        if (listener == null) {
            return theme.getThemeRes() != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID
                    ? theme.getThemeRes() : getDefault().getThemeRes();
        }

        return DynamicSdkUtils.is29() ? android.R.style.Theme_DeviceDefault_DayNight
                : listener.isNightMode(true) || theme.isDarkTheme()
                ? android.R.style.Theme_DeviceDefault : android.R.style.Theme_DeviceDefault_Light;
    }

    /**
     * Try to apply the dynamic colors according to the supplied parameters.
     *
     * @param colors The dynamic colors to be used.
     * @param listener The dynamic listener to be used.
     * @param theme The default dynamic theme to be used.
     * @param themeColors The dynamic theme to mutate the colors.
     */
    @TargetApi(Build.VERSION_CODES.S)
    private void setWallpaperColors(@NonNull DynamicColors colors,
            @Nullable DynamicListener listener, @NonNull AppTheme<?> theme,
            @Nullable AppTheme<?> themeColors) {
        if (listener == null) {
            return;
        }

        final @StyleRes int systemTheme = getSystemThemeRes(listener, theme);

        if (DynamicSdkUtils.is28()) {
            theme.setCornerSize(Math.min(DynamicResourceUtils.resolveDimension(
                    listener.getContext(), systemTheme, android.R.attr.dialogCornerRadius,
                    theme.getCornerSize()), Theme.Corner.MAX));
        } else {
            theme.setCornerSize(Math.min(DynamicResourceUtils.resolveDimension(
                    listener.getContext(), theme.getThemeRes(), R.attr.adt_cornerRadius,
                    theme.getCornerSize()), Theme.Corner.MAX));
        }

        if (!isDynamicColors() || (!isSystemColor() && !isWallpaperColor())) {
            return;
        }

        if (DynamicSdkUtils.is21()) {
            theme.setBackgroundColor(DynamicResourceUtils.resolveColor(
                    listener.getContext(), systemTheme, android.R.attr.colorBackground,
                    theme.getBackgroundColor()), false)
                    .setPrimaryColor(DynamicResourceUtils.resolveColor(
                            listener.getContext(), systemTheme, android.R.attr.colorPrimary,
                            theme.getPrimaryColor()), false)
                    .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                            listener.getContext(), systemTheme, android.R.attr.colorPrimaryDark,
                            theme.getPrimaryColorDark()), false)
                    .setAccentColor(DynamicResourceUtils.resolveColor(
                            listener.getContext(), systemTheme, android.R.attr.colorAccent,
                            theme.getAccentColor()), false);

            if (DynamicSdkUtils.is23()
                    && theme.getBackgroundColor(false, false) == Theme.AUTO) {
                theme.setSurfaceColor(DynamicResourceUtils.resolveColor(
                        listener.getContext(), systemTheme, android.R.attr.colorBackgroundFloating,
                        theme.getSurfaceColor()), false);
            } else {
                theme.setSurfaceColor(DynamicResourceUtils.resolveColor(
                        listener.getContext(), theme.getThemeRes(), R.attr.colorSurface,
                        theme.getSurfaceColor()), false);
            }

            if (DynamicSdkUtils.is26()
                    && theme.getPrimaryColor(false, false) == Theme.AUTO
                    && theme.getAccentColor(false, false) == Theme.AUTO) {
                theme.setErrorColor(DynamicResourceUtils.resolveColor(
                        listener.getContext(), systemTheme, android.R.attr.colorError,
                        theme.getErrorColor()), false);
            } else {
                theme.setErrorColor(DynamicResourceUtils.resolveColor(
                        listener.getContext(), theme.getThemeRes(), R.attr.colorError,
                        theme.getErrorColor()), false);
            }

            if (com.google.android.material.color.DynamicColors.isDynamicColorAvailable()) {
                if (Dynamic.isExpressiveVersion(Dynamic.loadThemeVersion())) {
                    theme.setBackgroundColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_neutral2_600), false)
                            .setSurfaceColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_neutral2_700), false)
                            .setPrimaryColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent1_300), false)
                            .setPrimaryColorDark(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent1_300), false)
                            .setAccentColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent2_600), false)
                            .setErrorColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent3_300), false);
                } else {
                    theme.setBackgroundColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_neutral1_500), false)
                            .setSurfaceColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_neutral1_600), false)
                            .setPrimaryColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent2_500), false)
                            .setPrimaryColorDark(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent2_500), false)
                            .setAccentColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent1_500), false)
                            .setErrorColor(ContextCompat.getColor(listener.getContext(),
                                    android.R.color.system_accent3_500), false);
                }
            }
        }

        if (themeColors != null) {
            if (isWallpaperColor() && !colors.getOriginal().isEmpty()) {
                colors.mutate(themeColors);

                theme.setBackgroundColor(colors.getMutated(Theme.ColorType.BACKGROUND,
                        theme.getBackgroundColor(), themeColors), false)
                        .setSurfaceColor(colors.getMutated(Theme.ColorType.SURFACE,
                                theme.getSurfaceColor(), themeColors), false)
                        .setPrimaryColor(colors.getMutated(Theme.ColorType.PRIMARY,
                                theme.getPrimaryColor(), themeColors), false)
                        .setPrimaryColorDark(colors.getMutated(Theme.ColorType.PRIMARY_DARK,
                                theme.getPrimaryColorDark(), themeColors), false)
                        .setAccentColor(colors.getMutated(Theme.ColorType.ACCENT,
                                theme.getAccentColor(), themeColors), false)
                        .setAccentColorDark(colors.getMutated(Theme.ColorType.ACCENT_DARK,
                                theme.getAccentColorDark(), themeColors), false)
                        .setErrorColor(colors.getMutated(Theme.ColorType.ERROR,
                                theme.getErrorColor(), themeColors), false);
            } else {
                colors.clear();

                colors.putOriginal(Theme.ColorType.BACKGROUND, theme.getBackgroundColor());
                colors.putOriginal(Theme.ColorType.SURFACE, Theme.AUTO);
                colors.putOriginal(Theme.ColorType.PRIMARY, theme.getPrimaryColor());
                colors.putOriginal(Theme.ColorType.PRIMARY_DARK, Theme.AUTO);
                colors.putOriginal(Theme.ColorType.ACCENT, theme.getAccentColor());
                colors.putOriginal(Theme.ColorType.ACCENT_DARK, Theme.AUTO);
                colors.putOriginal(Theme.ColorType.ERROR, Theme.AUTO);

                colors.mutate(themeColors);
            }
        }
    }

    /**
     * Initialize colors from the supplied theme resource.
     *
     * @param themeRes The theme resource to initialize colors.
     * @param theme The dynamic theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setThemeRes(@StyleRes int themeRes,
            @Nullable AppTheme<?> theme, boolean initializeRemoteColors) {
        if (themeRes == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            Log.w(TAG, "Dynamic theme style resource id is not found for the " +
                    "application theme. Trying to use the default style.");
            themeRes = getThemeRes(theme);
        }

        setVersion(DynamicResourceUtils.resolveInteger(getContext(), themeRes,
                R.attr.ads_theme_version, Version.DEFAULT_AUTO));

        if (theme != null) {
            theme.setThemeRes(themeRes);
            getDefaultApplication().setType(theme.getType());
        }

        getContext().getTheme().applyStyle(themeRes, true);
        getDefaultApplication().setThemeRes(themeRes);

        getDefaultApplication().setBackgroundColor(DynamicResourceUtils.resolveColor(
                getContext(), themeRes, android.R.attr.windowBackground,
                getDefaultApplication().getBackgroundColor()), false)
                .setSurfaceColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorSurface,
                        getDefaultApplication().getSurfaceColor()), false)
                .setPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorPrimary,
                        getDefaultApplication().getPrimaryColor()), false)
                .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorPrimaryDark,
                        getDefaultApplication().getPrimaryColorDark()), false)
                .setAccentColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorAccent,
                        getDefaultApplication().getAccentColor()), false)
                .setErrorColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorError,
                        getDefaultApplication().getErrorColor()), false)
                .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, android.R.attr.textColorPrimary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, android.R.attr.textColorSecondary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, android.R.attr.textColorPrimaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, android.R.attr.textColorSecondaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setAccentColorDark(getDefaultApplication().getAccentColorDark(), false)
                .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorOnSurface,
                        getDefaultApplication().getTintSurfaceColor()))
                .setTintPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorOnPrimary,
                        getDefaultApplication().getTintPrimaryColor()))
                .setTintAccentColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorOnSecondary,
                        getDefaultApplication().getTintAccentColor()))
                .setTintErrorColor(DynamicResourceUtils.resolveColor(
                        getContext(), themeRes, R.attr.colorOnError,
                        getDefaultApplication().getTintErrorColor()))
                .setFontScale(DynamicResourceUtils.resolveInteger(
                        getContext(), themeRes, R.attr.adt_fontScale,
                        getDefaultApplication().getFontScale()))
                .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffset(
                        getContext(), themeRes, R.attr.adt_cornerRadius,
                        getDefaultApplication().getCornerRadius()))
                .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                        getContext(), themeRes, R.attr.adt_backgroundAware,
                        getDefaultApplication().getBackgroundAware()))
                .setContrast(DynamicResourceUtils.resolveInteger(
                        getContext(), themeRes, R.attr.adt_contrast,
                        getDefaultApplication().getContrast()))
                .setOpacity(DynamicResourceUtils.resolveInteger(
                        getContext(), themeRes, R.attr.adt_opacity,
                        getDefaultApplication().getOpacity()))
                .setElevation(DynamicResourceUtils.resolveInteger(
                        getContext(), themeRes, R.attr.adt_elevation,
                        getDefaultApplication().getElevation()));

        if (theme != null) {
            mApplicationTheme = new DynamicAppTheme(theme);
        } else {
            mApplicationTheme = new DynamicAppTheme(getDefaultApplication());
        }

        getApplication().setHost(true);
        setWallpaperColors(getColors(false), getListener(),
                getDefaultApplication(), getApplication());

        if (initializeRemoteColors) {
            initializeRemoteColors();
        }

        return this;
    }

    /**
     * Initialize colors from the supplied dynamic app theme.
     *
     * @param themeRes The theme resource to initialize colors.
     * @param theme The dynamic theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setTheme(@StyleRes int themeRes,
            @Nullable AppTheme<?> theme, boolean initializeRemoteColors) {
        if (theme != null) {
            setThemeRes(theme.getThemeRes(), theme, initializeRemoteColors);
        } else {
            setThemeRes(themeRes, null, initializeRemoteColors);
        }

        return this;
    }

    /**
     * Initialize colors from the supplied local theme resource.
     *
     * @param themeRes The local theme resource to initialize colors.
     * @param theme The local dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    @TargetApi(Build.VERSION_CODES.P)
    public @NonNull DynamicTheme setLocalThemeRes(
            @StyleRes int themeRes, @Nullable AppTheme<?> theme) {
        if (getLocalContext() == null) {
            throw new IllegalStateException("Not attached to a local context.");
        }

        if (themeRes == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            Log.w(TAG, "Dynamic theme style resource id is not found for the " +
                    "local theme. Trying to use the default style.");
            themeRes = getThemeRes(theme);
        }

        setLocalVersion(DynamicResourceUtils.resolveInteger(getLocalContext(), themeRes,
                R.attr.ads_theme_version, Version.DEFAULT_AUTO));

        if (theme != null) {
            theme.setThemeRes(themeRes);
            getDefaultLocal().setType(theme.getType());
        }

        getLocalContext().getTheme().applyStyle(themeRes, true);
        getDefaultLocal().setThemeRes(themeRes);

        getDefaultLocal().setBackgroundColor(DynamicResourceUtils.resolveColor(
                getLocalContext(), themeRes, android.R.attr.windowBackground,
                getDefaultLocal().getBackgroundColor()), false)
                .setSurfaceColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorSurface,
                        getDefaultLocal().getSurfaceColor()), false)
                .setPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorPrimary,
                        getDefaultLocal().getPrimaryColor()))
                .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorPrimaryDark,
                        getDefaultLocal().getPrimaryColorDark()), false)
                .setAccentColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorAccent,
                        getDefaultLocal().getAccentColor()), false)
                .setErrorColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorError,
                        getDefaultLocal().getErrorColor()), false)
                .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, android.R.attr.textColorPrimary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, android.R.attr.textColorSecondary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, android.R.attr.textColorPrimaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, android.R.attr.textColorSecondaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setAccentColorDark(getDefaultLocal().getAccentColorDark(), false)
                .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorOnSurface,
                        getDefaultLocal().getTintSurfaceColor()))
                .setTintPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorOnPrimary,
                        getDefaultLocal().getTintPrimaryColor()))
                .setTintAccentColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorOnSecondary,
                        getDefaultLocal().getTintAccentColor()))
                .setTintErrorColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), themeRes, R.attr.colorOnError,
                        getDefaultLocal().getTintErrorColor()))
                .setFontScale(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), themeRes, R.attr.adt_fontScale,
                        getDefaultLocal().getFontScale()))
                .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffset(
                        getLocalContext(), themeRes, R.attr.adt_cornerRadius,
                        getDefaultLocal().getCornerRadius()))
                .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), themeRes, R.attr.adt_backgroundAware,
                        getDefaultLocal().getBackgroundAware()))
                .setContrast(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), themeRes, R.attr.adt_contrast,
                        getDefaultLocal().getContrast()))
                .setOpacity(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), themeRes, R.attr.adt_opacity,
                        getDefaultLocal().getOpacity()))
                .setElevation(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), themeRes, R.attr.adt_elevation,
                        getDefaultLocal().getElevation()));

        if (theme != null) {
            mLocalTheme = new DynamicAppTheme(theme);
        } else {
            mLocalTheme = new DynamicAppTheme(getDefaultLocal());
        }

        setWallpaperColors(getColors(), getLocalListener(), getDefaultLocal(), getLocal());

        return this;
    }

    /**
     * Initialize colors from the supplied local dynamic app theme.
     *
     * @param themeRes The local theme resource to initialize colors.
     * @param theme The local dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setLocalTheme(
            @StyleRes int themeRes, @Nullable AppTheme<?> theme) {
        if (theme != null) {
            setLocalThemeRes(theme.getThemeRes(), theme);
        } else {
            setLocalThemeRes(themeRes, null);
        }

        return this;
    }

    /**
     * Initialize colors from the supplied remote dynamic app theme.
     *
     * @param theme The remote dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setRemoteTheme(@Nullable AppTheme<?> theme) {
        if (theme != null) {
            this.mRemoteTheme = new DynamicRemoteTheme(theme);
        }

        if (mRemoteTheme == null) {
            this.mRemoteTheme = new DynamicRemoteTheme();
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
        if (mRemoteTheme == null) {
            mRemoteTheme = new DynamicRemoteTheme();
        }

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
            return get().getBackgroundAware();
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
            case Theme.ColorType.BACKGROUND:
                return get().getBackgroundColor();
            case Theme.ColorType.SURFACE:
                return get().getSurfaceColor();
            case Theme.ColorType.PRIMARY:
                return get().getPrimaryColor();
            case Theme.ColorType.PRIMARY_DARK:
                return get().getPrimaryColorDark();
            case Theme.ColorType.ACCENT:
                return get().getAccentColor();
            case Theme.ColorType.ACCENT_DARK:
                return get().getAccentColorDark();
            case Theme.ColorType.ERROR:
                return get().getErrorColor();
            case Theme.ColorType.TINT_BACKGROUND:
                return get().getTintBackgroundColor();
            case Theme.ColorType.TINT_SURFACE:
                return get().getTintSurfaceColor();
            case Theme.ColorType.TINT_PRIMARY:
                return get().getTintPrimaryColor();
            case Theme.ColorType.TINT_PRIMARY_DARK:
                return get().getTintPrimaryColorDark();
            case Theme.ColorType.TINT_ACCENT:
                return get().getTintAccentColor();
            case Theme.ColorType.TINT_ACCENT_DARK:
                return get().getTintAccentColorDark();
            case Theme.ColorType.TINT_ERROR:
                return get().getTintErrorColor();
            case Theme.ColorType.TEXT_PRIMARY:
                return get().getTextPrimaryColor();
            case Theme.ColorType.TEXT_SECONDARY:
                return get().getTextSecondaryColor();
            case Theme.ColorType.TEXT_PRIMARY_INVERSE:
                return get().getTextPrimaryColorInverse();
            case Theme.ColorType.TEXT_SECONDARY_INVERSE:
                return get().getTextSecondaryColorInverse();
            case Theme.ColorType.UNKNOWN:
            case Theme.ColorType.NONE:
            case Theme.ColorType.CUSTOM:
            default:
                return Theme.Color.UNKNOWN;
        }
    }

    /**
     * Get the application theme styles version.
     *
     * @return The application theme styles version.
     */
    public @Version int getVersion() {
        return mVersion;
    }

    /**
     * Set the application theme styles version.
     *
     * @param version The version to be set.
     */
    public void setVersion(@Version int version) {
        this.mVersion = version;
    }

    /**
     * Get the local theme styles version.
     *
     * @return The local theme styles version.
     */
    public @Version int getLocalVersion() {
        return mLocalVersion;
    }

    /**
     * Set the local theme styles version.
     *
     * @param version The version to be set.
     */
    public void setLocalVersion(@Version int version) {
        this.mLocalVersion = version;
    }

    /**
     * Resolves the theme version according to the attached local.
     *
     * @return The theme version according to the attached local.
     *
     * @see #getVersion()
     * @see #getLocalVersion()
     */
    public @Version int resolveVersion() {
        if (getLocalListener() != null) {
            return getLocalVersion();
        }

        return getVersion();
    }

    /**
     * Get the application listener used by this theme.
     *
     * @return The application context used by this theme.
     */
    public @NonNull DynamicListener getListener() {
        return mListener;
    }

    /**
     * Set the application listener for this theme.
     *
     * @param listener The application listener to be set.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setListener(@NonNull DynamicListener listener) {
        this.mListener = listener;

        return this;
    }

    /**
     * Get the local listener used by this theme.
     *
     * @return The local listener used by this theme.
     */
    public @Nullable DynamicListener getLocalListener() {
        if (mLocalListener == null) {
            return null;
        }

        return mLocalListener.get();
    }

    /**
     * Get the local listener used by this theme.
     *
     * @return The local listener used by this theme.
     */
    public @Nullable Context getLocalContext() {
        if (getLocalListener() == null) {
            return null;
        }

        return getLocalListener() instanceof Context
                ? (Context) getLocalListener() : getLocalListener().getContext();
    }

    /**
     * Returns the currently used context.
     * <p>Generally, either application or an activity.
     *
     * @return The currently used context.
     */
    private @NonNull Context getResolvedContext() {
        return getLocalContext() != null ? getLocalContext() : getContext();
    }

    /**
     * Get the UI mode manager used by the application.
     *
     * @return The UI mode manager used by the application.
     */
    public @NonNull UiModeManager getUiModeManager() {
        return mUiModeManager;
    }

    /**
     * Get the power manager used by the application.
     *
     * @return The power manager used by the application.
     */
    public @NonNull PowerManager getPowerManager() {
        return mPowerManager;
    }

    /**
     * Returns whether the power save mode is enabled.
     *
     * @return {@code true} if the power save mode is enabled.
     */
    public boolean isPowerSaveMode() {
        return mPowerSaveMode;
    }

    /**
     * Get the default theme used by the application.
     *
     * @return The default theme used by the application.
     */
    public @NonNull DynamicAppTheme getDefaultApplication() {
        return mDefaultApplicationTheme;
    }

    /**
     * Get the theme used by the application.
     *
     * @return The theme used by the application.
     */
    public @NonNull DynamicAppTheme getApplication() {
        return mApplicationTheme;
    }

    /**
     * Get the default theme used by the local context.
     *
     * @return The default theme used by the local context.
     */
    public @NonNull DynamicAppTheme getDefaultLocal() {
        if (mDefaultLocalTheme == null) {
            mDefaultLocalTheme = new DynamicAppTheme(getDefaultApplication());
        }

        return mDefaultLocalTheme;
    }

    /**
     * Get the theme used by the local context.
     *
     * @return The theme used by the local context.
     */
    public @Nullable DynamicAppTheme getLocal() {
        return mLocalTheme;
    }

    /**
     * Get the theme used by the remote elements.
     *
     * @return The theme used by the remote elements.
     */
    public @NonNull DynamicRemoteTheme getRemote() {
        return mRemoteTheme;
    }

    /**
     * Returns the saved dynamic themes currently in use or to be resumed later.
     *
     * @return The saved dynamic themes currently in use or to be resumed later.
     */
    public @NonNull Map<String, String> getDynamicThemes() {
        return mDynamicThemes;
    }

    /**
     * Get the preview activity used by this manager.
     *
     * @return The preview activity used by this manager.
     */
    public @NonNull Class<?> getPreviewActivity() {
        return mPreviewActivity != null ? mPreviewActivity : DynamicPreviewActivity.class;
    }

    /**
     * Sets the preview activity for this instance.
     *
     * @param previewActivity The preview activity class to be set.
     */
    public void setPreviewActivity(@NonNull Class<?> previewActivity) {
        this.mPreviewActivity = previewActivity;
    }

    /**
     * Get the theme according to the supplied parameter.
     * <p>Either application theme or local theme.
     *
     * @param resolve {@code true} to resolve the local theme if present.
     *
     * @return The theme according to the supplied parameter.
     */
    public @NonNull DynamicAppTheme get(boolean resolve) {
        if (resolve) {
            return getLocalContext() != null && getLocal() != null
                    ? getLocal() : getApplication();
        }

        return getApplication();
    }

    /**
     * Get the theme according to the current state.
     * <p>Either application theme or local theme.
     *
     * @return The theme according to the current state.
     */
    public @NonNull DynamicAppTheme get() {
        return get(true);
    }

    /**
     * Get the default theme according to the supplied parameter.
     * <p>Either default application theme or default local theme.
     *
     * @param resolve {@code true} to resolve the default local theme if present.
     *
     * @return The default theme according to the supplied parameter.
     */
    public @NonNull DynamicAppTheme getDefault(boolean resolve) {
        if (resolve) {
            return getLocalContext() != null ? getDefaultLocal() : getDefaultApplication();
        }

        return getDefaultApplication();
    }

    /**
     * Get the default theme according to the current state.
     * <p>Either default application theme or default local theme.
     *
     * @return The default theme according to the current state.
     *
     * @see #getDefault(boolean)
     */
    public @NonNull DynamicAppTheme getDefault() {
        return getDefault(true);
    }

    /**
     * Recreate local activity to update all the views with new theme.
     */
    public void recreateLocal() {
        if (getLocalContext() instanceof Activity) {
            ActivityCompat.recreate(((Activity) getLocalContext()));
        }
    }

    /**
     * Set the initialized instance to {@code null} when app terminates for better theme
     * results when theme is changed.
     */
    public void onDestroy() {
        if (sInstance == null) {
            return;
        }

        getContext().unregisterReceiver(mBroadcastReceiver);
        setWallpaperColors(false, false);
        clearDynamicListeners();

        mLocalListener.clear();
        mLocalListener = null;
        mLocalTheme = null;
        mDefaultLocalTheme = null;
        mRemoteTheme = null;
        mListener = null;
        mApplicationTheme = null;
        mDefaultApplicationTheme = null;
        mBroadcastReceiver = null;
        sInstance.mLocalListener.clear();
        sInstance.mLocalListener = null;
        sInstance.mLocalTheme = null;
        sInstance.mDefaultLocalTheme = null;
        sInstance.mRemoteTheme = null;
        sInstance.mListener = null;
        sInstance.mApplicationTheme = null;
        sInstance.mDefaultApplicationTheme = null;
        sInstance.mBroadcastReceiver = null;
        sInstance = null;
    }

    /**
     * Set the initialized instance to {@code null} when local destroys for better theme
     * results when theme is changed.
     *
     * @param listener The listener to be destroyed.
     */
    public void onLocalDestroy(@Nullable DynamicListener listener) {
        if (sInstance == null) {
            return;
        }

        removeDynamicListener(getLocalListener());
        removeDynamicListener(listener);
        saveLocalTheme();

        if (mLocalListener != null) {
            mLocalListener.clear();
            mLocalListener = null;
        }
        mLocalTheme = null;
        mDefaultLocalTheme = null;
    }

    /**
     * Generates default background color according to the application theme.
     *
     * @param night {@code true} if the night mode is enabled.
     *
     * @return The generated background color.
     */
    public @ColorInt int getDefaultBackgroundColor(boolean night) {
        return ContextCompat.getColor(getContext(), night
                ? R.color.ads_window_background : R.color.ads_window_background_light);
    }

    /**
     * Try to get the contrast from the system.
     *
     * @param fallback The fallback contrast to be used in case of any issues.
     *
     * @return The contrast from the system.
     */
    @TargetApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public int getSystemContrast(int fallback) {
        if (DynamicSdkUtils.is34()) {
            int contrast = (int) (mUiModeManager.getContrast() * Theme.Contrast.MAX) / 2;
            contrast = getDefault().getContrast() + contrast;

            return Math.min(Math.abs(contrast), Theme.Contrast.MAX);
        }

        return fallback;
    }

    /**
     * Try to get the corner radius for the widget background from the system.
     *
     * @param fallback The fallback radius to be used in case of any issues.
     *
     * @return The corner radius for the widget background from the system.
     */
    @TargetApi(Build.VERSION_CODES.S)
    public @Px int getWidgetCornerRadius(int fallback) {
        if (DynamicSdkUtils.is31()) {
            return Math.min(getContext().getResources().getDimensionPixelOffset(
                    android.R.dimen.system_app_widget_background_radius),
                    DynamicUnitUtils.convertDpToPixels(Theme.Corner.MAX));
        }

        return fallback;
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
     * Generates default theme according to the current settings.
     *
     * @return The generated default theme.
     */
    public @NonNull DynamicAppTheme generateDefaultTheme() {
        return new DynamicAppTheme().setBackgroundColor(
                getDefault().getBackgroundColor(), false);
    }

    /**
     * Generates stroke color according to the supplied color.
     *
     * @param color The color to generate the stroke color.
     *
     * @return The generated stroke color.
     */
    public @ColorInt int generateStrokeColor(@ColorInt int color) {
        return DynamicColorUtils.setAlpha(Dynamic.getTintColor(color), Theme.Opacity.STROKE_MIN);
    }

    /**
     * Generates surface color according to the supplied color.
     *
     * @param color The color to generate the surface color.
     *
     * @return The generated surface color.
     */
    public @ColorInt int generateSurfaceColor(@ColorInt int color) {
        return DynamicColorUtils.isColorDark(color) ? DynamicColorUtils.getLighterColor(color,
                Defaults.ADS_FACTOR_SURFACE, false) : DynamicColorUtils.getLighterColor(
                        color, Defaults.ADS_FACTOR_SURFACE * 2, false);
    }

    /**
     * Generates error color according to the supplied colors.
     *
     * @param primary The primary color to generate the error color.
     * @param secondary The secondary color to generate the error color.
     *
     * @return The generated error color.
     */
    public @ColorInt int generateErrorColor(@ColorInt int primary, @ColorInt int secondary) {
        return DynamicColorUtils.getAccentColor(Color.argb(
                Math.max(Color.alpha(primary), Color.alpha(secondary)),
                Math.max(Color.red(primary), Color.red(secondary)),
                Math.min(Color.green(primary), Color.blue(primary)),
                Math.min(Color.blue(secondary), Color.green(secondary))));
    }

    /**
     * Generates dark variant of the supplied color.
     *
     * @param color The color to generate the dark variant.
     *
     * @return The generated dark variant of the color.
     */
    public @ColorInt int generateSystemColor(@ColorInt int color) {
        return DynamicColorUtils.shiftColor(color, DynamicTheme.COLOR_SHIFT_DARK_DEFAULT);
    }

    /**
     * Generates tint of the supplied color.
     *
     * @param color The color to generate the tint.
     *
     * @return The generated tint of the color.
     */
    public @ColorInt int generateSystemSecondaryColor(@ColorInt int color) {
        return Dynamic.getTintColor(color);
    }

    /**
     * Returns the resolver used by the dynamic theme.
     *
     * @return The resolver used by the dynamic theme.
     */
    public @NonNull DynamicResolver getDynamicResolver() {
        if (mDynamicResolver == null) {
            mDynamicResolver = new DynamicThemeResolver(getInstance());
        }

        return mDynamicResolver;
    }

    /**
     * Sets the resolver used by the dynamic theme.
     *
     * @param resolver The resolver to be set.
     */
    public void setDynamicResolver(@Nullable DynamicResolver resolver) {
        this.mDynamicResolver = resolver;
    }
    
    /**
     * Add a dynamic listener to receive the various callbacks.
     *
     * @param listener The dynamic listener to be added.
     *
     * @see DynamicListener
     */
    public void addDynamicListener(@Nullable DynamicListener listener) {
        synchronized (getHandler()) {
            getHandler().addListener(listener);
        }
    }

    /**
     * Remove a dynamic listener.
     *
     * @param listener The dynamic listener to be removed.
     *
     * @see DynamicListener
     */
    public void removeDynamicListener(@Nullable DynamicListener listener) {
        synchronized (getHandler()) {
            getHandler().removeListener(listener);
        }
    }

    /**
     * Checks whether a dynamic listener is already registered.
     *
     * @param listener The dynamic listener to be checked.
     *
     * @return {@code true} if dynamic listener is already registered.
     *
     * @see DynamicListener
     */
    public boolean isDynamicListener(@Nullable DynamicListener listener) {
        return getHandler().isListener(listener);
    }

    /**
     * Remove all the dynamic listeners and themes.
     */
    public void clearDynamicListeners() {
        getHandler().clearListeners();
        getDynamicThemes().clear();
    }

    @Override
    public @DynamicFlavor String getProductFlavor() {
        return getHandler().getProductFlavor();
    }

    @Override
    public @NonNull Context getContext() {
        return getHandler().getContext();
    }

    @Override
    public @Version int getRequiredThemeVersion() {
        return getHandler().getRequiredThemeVersion();
    }

    @Override
    public boolean isNightMode(boolean resolve) {
        return getHandler().isNightMode(resolve);
    }

    @Override
    public @StyleRes int getThemeRes(@Nullable AppTheme<?> theme) {
        return getHandler().getThemeRes(theme);
    }

    @Override
    public @StyleRes int getThemeRes() {
        return getHandler().getThemeRes();
    }

    @Override
    public @Nullable AppTheme<?> getDynamicTheme() {
        return getHandler().getDynamicTheme();
    }

    @Override
    public boolean isDynamicColors() {
        return getHandler().isDynamicColors();
    }

    @Override
    public boolean isDynamicColor() {
        return getHandler().isDynamicColor();
    }

    @Override
    public boolean isSystemColor() {
        return getHandler().isSystemColor();
    }

    @Override
    public boolean isWallpaperColor() {
        return getHandler().isWallpaperColor();
    }

    @Override
    public boolean isOnSharedPreferenceChangeListener() {
        return getHandler().isOnSharedPreferenceChangeListener();
    }

    @Override
    public @ColorInt int getDefaultColor(@Theme.ColorType int colorType) {
        return getHandler().getDefaultColor(colorType);
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_CONTEXT, context);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_RECREATE, recreate);

        Message message = getHandler().obtainMessage(DynamicThemeHandler.MESSAGE_POST_DYNAMIC);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_LOCALE, locale);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_FONT_SCALE, fontScale);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_ORIENTATION, orientation);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_UI_MODE, uiMode);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_DENSITY, density);

        Message message = getHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_DYNAMIC_CONFIGURATION);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onDynamicColorsChanged(@Nullable DynamicColors colors, boolean context) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DynamicThemeHandler.DATA_PARCELABLE_COLORS, colors);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_CONTEXT, context);

        Message message = getHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_DYNAMIC_COLOR);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onAutoThemeChanged(boolean context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_CONTEXT, context);

        Message message = getHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_AUTO_THEME);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_POWER_SAVE_MODE, powerSaveMode);

        Message message = getHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_POWER_SAVE_MODE);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public boolean setNavigationBarTheme() {
        return getHandler().setNavigationBarTheme();
    }

    @Override
    public void onNavigationBarThemeChanged() {
        getHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_NAVIGATION_BAR_THEME).sendToTarget();
    }

    @Override
    public @NonNull String toString() {
        StringBuilder theme = new StringBuilder();
        theme.append(getApplication().toString());
        theme.append(getRemote().toString());

        if (getLocal() != null) {
            theme.append(getLocal().toString());
            theme.append(getColors().toString());
        }

        return theme.toString();
    }

    /**
     * Save the local context theme in shared preferences.
     */
    public void saveLocalTheme() {
        if (getLocalListener() != null) {
            getDynamicThemes().put(ADS_PREF_THEME_KEY
                    + getLocalListener().getClass().getName(), toString());
        }
    }

    /**
     * Returns the supplied listener theme from the shared preferences.
     *
     * @param listener The listener to retrieve the theme.
     *
     * @return The supplied listener theme from the shared preferences.
     */
    public @Nullable String getLocalTheme(@NonNull DynamicListener listener) {
        return getDynamicThemes().get(ADS_PREF_THEME_KEY + listener.getClass().getName());
    }

    /**
     * Delete the supplied listener theme from the shared preferences.
     *
     * @param listener The listener to delete the theme.
     */
    public void deleteLocalTheme(@NonNull DynamicListener listener) {
        getDynamicThemes().remove(ADS_PREF_THEME_KEY + listener.getClass().getName());
    }

    /**
     * Returns the dynamic app theme from the JSON string.
     *
     * @param theme The dynamic app theme JSON string to be converted.
     *
     * @return The dynamic app theme from the JSON string.
     */
    public @Nullable DynamicAppTheme getTheme(@Nullable String theme) {
        if (theme == null) {
            return null;
        }

        try {
            return new Gson().fromJson(theme, DynamicAppTheme.class);
        } catch (Exception ignored) {
            try {
                return new DynamicAppTheme(theme);
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Returns the dynamic widget theme from the JSON string.
     *
     * @param theme The dynamic widget theme JSON string to be converted.
     *
     * @return The dynamic widget theme from the JSON string.
     */
    public @Nullable DynamicWidgetTheme getWidgetTheme(@Nullable String theme) {
        if (theme == null) {
            return null;
        }

        try {
            return new Gson().fromJson(theme, DynamicWidgetTheme.class);
        } catch (Exception ignored) {
            try {
                return new DynamicWidgetTheme(theme);
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Returns the dynamic remote theme from the JSON string.
     *
     * @param theme The dynamic remote theme JSON string to be converted.
     *
     * @return The dynamic remote theme from the JSON string.
     */
    public @Nullable DynamicRemoteTheme getRemoteTheme(@Nullable String theme) {
        if (theme == null) {
            return null;
        }

        try {
            return new Gson().fromJson(theme, DynamicRemoteTheme.class);
        } catch (Exception ignored) {
            try {
                return new DynamicRemoteTheme(theme);
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }

    @Override
    public @NonNull DynamicTheme getTheme() {
        return getDynamicResolver().getTheme();
    }

    @Override
    public @NonNull DynamicColors getColors(boolean resolve) {
        return getDynamicResolver().getColors(resolve);
    }

    @Override
    public @NonNull DynamicColors getColors() {
        return getDynamicResolver().getColors();
    }

    @Override
    public boolean isSystemNightMode() {
        return getDynamicResolver().isSystemNightMode();
    }

    @Override
    public int resolveSystemColor(boolean isNight) {
        return getDynamicResolver().resolveSystemColor(isNight);
    }

    @Override
    public boolean isNight() {
        return getDynamicResolver().isNight();
    }

    @Override
    public boolean isNight(@Theme int theme) {
        return getDynamicResolver().isNight(theme);
    }

    @Override
    public boolean isNight(@Theme.ToString String theme) {
        return getDynamicResolver().isNight(theme);
    }

    @Override
    public @NonNull Date getNightTimeStart() {
        return getDynamicResolver().getNightTimeStart();
    }

    @Override
    public @NonNull Date getNightTimeEnd() {
        return getDynamicResolver().getNightTimeEnd();
    }

    @Override
    public boolean resolveNightTheme(@Theme int theme,
            @Theme.Night int implementation, boolean data) {
        return getDynamicResolver().resolveNightTheme(theme, implementation, data);
    }

    @Override
    public boolean resolveNightTheme(@Theme.ToString String theme,
            @Theme.Night.ToString String implementation, boolean data) {
        return getDynamicResolver().resolveNightTheme(theme, implementation, data);
    }

    @Override
    public @Theme int resolveAppTheme(@Theme int theme, @Theme.Night int night, boolean data) {
        return getDynamicResolver().resolveAppTheme(theme, night, data);
    }

    @Override
    public @Theme int resolveAppTheme(@Theme.ToString String theme,
            @Theme.Night.ToString String night, boolean data) {
        return getDynamicResolver().resolveAppTheme(theme, night, data);
    }

    /**
     * Copy the supplied theme string to the clipboard and show a {@link Snackbar}.
     *
     * @param context The context to get the {@link android.content.ClipboardManager}.
     * @param theme The theme string to be copied.
     */
    public void copyThemeString(@NonNull Context context, @Nullable String theme) {
        if (theme == null) {
            invalidTheme(context);

            return;
        }

        try {
            DynamicLinkUtils.copyToClipboard(context,
                    context.getString(R.string.ads_theme), theme);
            Dynamic.showSnackbar(context, R.string.ads_theme_copy_done);
        } catch (Exception ignored) {
            invalidTheme(context);
        }
    }

    /**
     * Copy the supplied theme data to the clipboard and show a {@link Snackbar}.
     *
     * @param context The context to get the {@link android.content.ClipboardManager}.
     * @param theme The theme data to be copied.
     */
    public void copyTheme(@NonNull Context context, @Nullable String theme) {
        if (theme == null) {
            invalidTheme(context);

            return;
        }

        try {
            copyThemeString(context, new DynamicAppTheme(theme).toDynamicString());
        } catch (Exception ignored) {
            invalidTheme(context);
        }
    }

    /**
     * Show a theme saved {@link Snackbar}.
     *
     * @param context The context to get the {@link Snackbar}.
     */
    public void saveTheme(@NonNull Context context) {
        Dynamic.showSnackbar(context, R.string.ads_theme_save_done);
    }

    /**
     * Show a invalid theme {@link Snackbar}.
     *
     * @param context The context to get the {@link Snackbar}.
     */
    public void invalidTheme(@NonNull Context context) {
        Dynamic.showSnackbar(context, R.string.ads_theme_invalid_desc);
    }
}
