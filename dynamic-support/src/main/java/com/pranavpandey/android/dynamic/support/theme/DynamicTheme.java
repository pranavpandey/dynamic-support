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

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.TargetApi;
import android.app.Activity;
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
import com.pranavpandey.android.dynamic.support.theme.inflater.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.theme.work.DynamicThemeWork;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

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
public class DynamicTheme implements DynamicListener, DynamicResolver {

    /**
     * Tag for the dynamic theme.
     */
    private static final String TAG = "Dynamic Theme";

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
    private static final int CORNER_SIZE_DEFAULT = DynamicUnitUtils.convertDpToPixels(2);

    /**
     * Singleton instance of {@link DynamicTheme}.
     */
    private static DynamicTheme sInstance;

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
    private Map<String, String> mDynamicThemes;

    /**
     * Resolver used by the dynamic theme.
     */
    private DynamicResolver mDynamicResolver;

    /**
     * Main thread handler to publish results.
     */
    private final DynamicThemeHandler mMainThreadHandler;

    /**
     * Making default constructor private so that it cannot be initialized without a listener.
     * <p>Use {@link #initializeInstance(DynamicListener, DynamicResolver)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private DynamicTheme() {
        this.mMainThreadHandler = new DynamicThemeHandler(
                Looper.getMainLooper(), new ArrayList<>());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param listener The application dynamic listener to be attached with this theme.
     * @param dynamicResolver The resolver for the dynamic theme.
     *                        <p>Pass {@code null} to use the default implementation.
     */
    private DynamicTheme(@NonNull DynamicListener listener,
            @Nullable DynamicResolver dynamicResolver) {
        this();

        DynamicPermissions.initializeInstance(listener.getContext());

        this.mListener = listener;
        this.mPowerManager = (PowerManager) mListener.getContext()
                .getSystemService(Context.POWER_SERVICE);
        this.mDynamicResolver = dynamicResolver;
        this.mDynamicThemes = new HashMap<>();
        this.mDefaultApplicationTheme = new DynamicAppTheme()
                .setFontScale(FONT_SCALE_DEFAULT).setCornerRadius(CORNER_SIZE_DEFAULT)
                .setBackgroundAware(Theme.BackgroundAware.ENABLE);
        this.mApplicationTheme = new DynamicAppTheme();

        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    if (PowerManager.ACTION_POWER_SAVE_MODE_CHANGED.equals(intent.getAction())) {
                        mPowerSaveMode = getPowerManager().isPowerSaveMode();
                        onPowerSaveModeChanged(mPowerSaveMode);
                    } else {
                        setDynamicThemeWork(!WorkManager.getInstance(context)
                                .getWorkInfosForUniqueWork(DynamicThemeWork.TAG).isDone());
                        onAutoThemeChanged();
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
        mListener.getContext().registerReceiver(mBroadcastReceiver, intentFilter);

        setRemoteTheme(null);
        addDynamicListener(listener);
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

            WorkManager.getInstance(getContext()).enqueueUniqueWork(
                    DynamicThemeWork.TAG, ExistingWorkPolicy.REPLACE,
                    new OneTimeWorkRequest.Builder(DynamicThemeWork.class)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .build());
        } else {
            WorkManager.getInstance(getContext()).cancelUniqueWork(DynamicThemeWork.TAG);
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
        this.mDefaultLocalTheme = new DynamicAppTheme()
                .setFontScale(FONT_SCALE_DEFAULT).setCornerRadius(CORNER_SIZE_DEFAULT)
                .setBackgroundAware(Theme.BackgroundAware.ENABLE);
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
    public static synchronized void initializeInstance(@NonNull DynamicListener listener,
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
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * Initialize colors from the supplied theme resource.
     *
     * @param theme The theme resource to initialize colors.
     * @param dynamicTheme The dynamic theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setThemeRes(@StyleRes int theme,
            @Nullable AppTheme<?> dynamicTheme, boolean initializeRemoteColors) {
        if (theme == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            Log.w(TAG, "Dynamic theme style resource id is not found for the " +
                    "application theme. Trying to use the default style.");
            theme = getThemeRes(dynamicTheme);

            if (dynamicTheme != null) {
                dynamicTheme.setThemeRes(theme);
            }
        }

        getContext().getTheme().applyStyle(theme, true);
        getDefaultApplication().setThemeRes(theme);

        getDefaultApplication().setBackgroundColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, android.R.attr.windowBackground,
                        getDefaultApplication().getBackgroundColor()), false)
                .setSurfaceColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorSurface,
                        getDefaultApplication().getSurfaceColor()), false)
                .setPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorPrimary,
                        getDefaultApplication().getPrimaryColor()), false)
                .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorPrimaryDark,
                        getDefaultApplication().getPrimaryColorDark()), false)
                .setAccentColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorAccent,
                        getDefaultApplication().getAccentColor()), false)
                .setAccentColorDark(getDefaultApplication().getAccentColor(), false)
                .setErrorColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorError,
                        getDefaultApplication().getErrorColor()), false)
                .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorOnSurface,
                        getDefaultApplication().getTintSurfaceColor()))
                .setTintPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorOnPrimary,
                        getDefaultApplication().getTintPrimaryColor()))
                .setTintAccentColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorOnSecondary,
                        getDefaultApplication().getTintAccentColor()))
                .setTintErrorColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, R.attr.colorOnError,
                        getDefaultApplication().getTintErrorColor()))
                .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, android.R.attr.textColorPrimary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                        getContext(), theme, android.R.attr.textColorSecondary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                        getContext(), theme, android.R.attr.textColorPrimaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                        getContext(), theme, android.R.attr.textColorSecondaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setFontScale(DynamicResourceUtils.resolveInteger(
                        getContext(), theme, R.attr.ads_fontScale,
                        getDefaultApplication().getFontScale()))
                .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                        getContext(), theme, R.attr.ads_cornerRadius,
                        getDefaultApplication().getCornerRadius()))
                .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                        getContext(), theme, R.attr.ads_backgroundAware,
                        getDefaultApplication().getBackgroundAware()));

        mApplicationTheme = new DynamicAppTheme(dynamicTheme == null
                ? getDefaultApplication() : dynamicTheme);

        if (initializeRemoteColors) {
            initializeRemoteColors();
        }

        return this;
    }

    /**
     * Initialize colors from the supplied dynamic app theme.
     *
     * @param theme The theme resource to initialize colors.
     * @param dynamicTheme The dynamic theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setTheme(@StyleRes int theme,
            @Nullable AppTheme<?> dynamicTheme, boolean initializeRemoteColors) {
        if (dynamicTheme != null) {
            setThemeRes(dynamicTheme.getThemeRes(), dynamicTheme, false);
        } else {
            setThemeRes(theme, null, false);
        }

        return this;
    }

    /**
     * Initialize colors from the supplied local theme resource.
     *
     * @param theme The local theme resource to initialize colors.
     * @param dynamicTheme The local dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setLocalThemeRes(@StyleRes int theme,
            @Nullable AppTheme<?> dynamicTheme) {
        if (getLocalContext() == null) {
            throw new IllegalStateException("Not attached to a local context.");
        }

        if (theme == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            Log.w(TAG, "Dynamic theme style resource id is not found for the " +
                    "local theme. Trying to use the default style.");
            theme = getThemeRes(dynamicTheme);

            if (dynamicTheme != null) {
                dynamicTheme.setThemeRes(theme);
            }
        }

        getLocalContext().getTheme().applyStyle(theme, true);
        getDefaultLocal().setThemeRes(theme);

        getDefaultLocal().setBackgroundColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, android.R.attr.windowBackground,
                        getDefaultLocal().getBackgroundColor()), false)
                .setSurfaceColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorSurface,
                        getDefaultLocal().getSurfaceColor()), false)
                .setPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorPrimary,
                        getDefaultLocal().getPrimaryColor()))
                .setPrimaryColorDark(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorPrimaryDark,
                        getDefaultLocal().getPrimaryColorDark()), false)
                .setAccentColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorAccent,
                        getDefaultLocal().getAccentColor()), false)
                .setAccentColorDark(getDefaultLocal().getAccentColor(), false)
                .setErrorColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorError,
                        getDefaultLocal().getErrorColor()), false)
                .setTintSurfaceColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorOnSurface,
                        getDefaultLocal().getTintSurfaceColor()))
                .setTintPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorOnPrimary,
                        getDefaultLocal().getTintPrimaryColor()))
                .setTintAccentColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorOnSecondary,
                        getDefaultLocal().getTintAccentColor()))
                .setTintErrorColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, R.attr.colorOnError,
                        getDefaultLocal().getTintErrorColor()))
                .setTextPrimaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, android.R.attr.textColorPrimary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextSecondaryColor(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, android.R.attr.textColorSecondary,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE), false)
                .setTextPrimaryColorInverse(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, android.R.attr.textColorPrimaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setTextSecondaryColorInverse(DynamicResourceUtils.resolveColor(
                        getLocalContext(), theme, android.R.attr.textColorSecondaryInverse,
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE))
                .setFontScale(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), theme, R.attr.ads_fontScale,
                        getDefaultLocal().getFontScale()))
                .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                        getLocalContext(), theme, R.attr.ads_cornerRadius,
                        getDefaultLocal().getCornerRadius()))
                .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                        getLocalContext(), theme, R.attr.ads_backgroundAware,
                        getDefaultLocal().getBackgroundAware()));

        mLocalTheme = new DynamicAppTheme(dynamicTheme == null
                ? getDefaultLocal() : dynamicTheme);

        return this;
    }

    /**
     * Initialize colors from the supplied local dynamic app theme.
     *
     * @param theme The local theme resource to initialize colors.
     * @param dynamicTheme The local dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setLocalTheme(@StyleRes int theme,
            @Nullable AppTheme<?> dynamicTheme) {
        if (dynamicTheme != null) {
            setLocalThemeRes(dynamicTheme.getThemeRes(), dynamicTheme);
        } else {
            setLocalThemeRes(theme, null);
        }

        return this;
    }

    /**
     * Initialize colors from the supplied remote dynamic app theme.
     *
     * @param dynamicTheme The remote dynamic theme to initialize colors.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setRemoteTheme(@Nullable AppTheme<?> dynamicTheme) {
        if (dynamicTheme != null) {
            this.mRemoteTheme = new DynamicRemoteTheme(dynamicTheme);
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
            default:
                return Theme.Color.UNKNOWN;
        }
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
        return mDefaultLocalTheme != null ? mDefaultLocalTheme : getDefaultApplication();
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
        mListener = null;
        mLocalListener.clear();
        mLocalListener = null;
        mBroadcastReceiver = null;
        mDefaultApplicationTheme = null;
        mApplicationTheme = null;
        mDefaultLocalTheme = null;
        mLocalTheme = null;
        mRemoteTheme = null;
        sInstance.mListener = null;
        sInstance.mApplicationTheme = null;
        sInstance.mDefaultApplicationTheme = null;
        sInstance.mDefaultLocalTheme = null;
        sInstance.mLocalListener.clear();
        sInstance.mLocalListener = null;
        sInstance.mLocalTheme = null;
        sInstance.mRemoteTheme = null;
        sInstance = null;

        clearDynamicListeners();
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
        }
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
     * Generates stroke color according to the supplied color.
     *
     * @param color The color to generate the stroke color.
     *
     * @return The generated stroke color.
     */
    public @ColorInt int generateStrokeColor(@ColorInt int color) {
        return DynamicColorUtils.setAlpha(DynamicColorUtils.getTintColor(color),
                Defaults.ADS_STROKE_ALPHA);
    }

    /**
     * Generates surface color according to the supplied color.
     *
     * @param color The color to generate the surface color.
     *
     * @return The generated surface color.
     */
    public @ColorInt int generateSurfaceColor(@ColorInt int color) {
        return DynamicColorUtils.isColorDark(color) ? DynamicColorUtils.getLighterColor(
                color, Defaults.ADS_FACTOR_SURFACE) : DynamicColorUtils.getLighterColor(
                        color, Defaults.ADS_FACTOR_SURFACE * 2);
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
        return DynamicColorUtils.getTintColor(color);
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
        if (mDynamicResolver == null) {
            mDynamicResolver = new DynamicThemeResolver(getInstance());
        }

        return mDynamicResolver;
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
     * Get the main thread handler.
     *
     * @return The main thread handler.
     */
    public @NonNull DynamicThemeHandler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * Add a dynamic listener to receive the various callbacks.
     *
     * @param listener The dynamic listener to be added.
     *
     * @see DynamicListener
     */
    public void addDynamicListener(@Nullable DynamicListener listener) {
        synchronized (getMainThreadHandler()) {
            getMainThreadHandler().addListener(listener);
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
        synchronized (getMainThreadHandler()) {
            getMainThreadHandler().removeListener(listener);
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
        return getMainThreadHandler().isListener(listener);
    }

    /**
     * Remove all the dynamic listeners and themes.
     */
    public void clearDynamicListeners() {
        getMainThreadHandler().clearListeners();
        getDynamicThemes().clear();
    }

    @Override
    public @NonNull Context getContext() {
        return getMainThreadHandler().getContext();
    }

    @Override
    public @StyleRes int getThemeRes(@Nullable AppTheme<?> theme) {
        return getMainThreadHandler().getThemeRes(theme);
    }

    @Override
    public @StyleRes int getThemeRes() {
        return getMainThreadHandler().getThemeRes();
    }

    @Override
    public @Nullable AppTheme<?> getDynamicTheme() {
        return getMainThreadHandler().getDynamicTheme();
    }

    @Override
    public @ColorInt int getDefaultColor(@Theme.ColorType int colorType) {
        return getMainThreadHandler().getDefaultColor(colorType);
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_CONTEXT, context);
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_RECREATE, recreate);

        Message message = getMainThreadHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_DYNAMIC);
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

        Message message = getMainThreadHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_DYNAMIC_CONFIGURATION);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onAutoThemeChanged() {
        getMainThreadHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_AUTO_THEME).sendToTarget();
    }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DynamicThemeHandler.DATA_BOOLEAN_POWER_SAVE_MODE, powerSaveMode);

        Message message = getMainThreadHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_POWER_SAVE_MODE);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public boolean setNavigationBarTheme() {
        return getMainThreadHandler().setNavigationBarTheme();
    }

    @Override
    public void onNavigationBarThemeChanged() {
        getMainThreadHandler().obtainMessage(
                DynamicThemeHandler.MESSAGE_POST_NAVIGATION_BAR_THEME).sendToTarget();
    }

    @Override
    public @NonNull String toString() {
        StringBuilder theme = new StringBuilder();
        theme.append(getApplication().toString());
        theme.append(getRemote().toString());

        if (getLocal() != null) {
            theme.append(getLocal().toString());
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
        try {
            return new Gson().fromJson(theme, DynamicAppTheme.class);
        } catch (Exception e) {
            return null;
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
        try {
            return new Gson().fromJson(theme, DynamicWidgetTheme.class);
        } catch (Exception e) {
            return null;
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
        try {
            return new Gson().fromJson(theme, DynamicRemoteTheme.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isHideDividers() {
        return getDynamicResolver().isHideDividers();
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
    public boolean resolveNightTheme(@Theme int appTheme, @Theme.Night int implementation) {
        return getDynamicResolver().resolveNightTheme(appTheme, implementation);
    }

    @Override
    public boolean resolveNightTheme(@Theme.ToString String appTheme,
            @Theme.Night.ToString String implementation) {
        return getDynamicResolver().resolveNightTheme(appTheme, implementation);
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
