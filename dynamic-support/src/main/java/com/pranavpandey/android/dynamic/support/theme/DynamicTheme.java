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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage theme for the whole application and its activities.
 * <p>It must be initialized before using any activity or widget as they are
 * heavily dependent on this class to generate colors dynamically.
 */
public class DynamicTheme implements DynamicListener {

    /**
     * Normal delay for updating the views.
     */
    public static final int ADS_DELAY_NORMAL = 250;

    /**
     * Theme change delay which will be useful in some situations like changing the app theme,
     * updating the widgets, etc.
     */
    public static final int ADS_DELAY_THEME_CHANGE = 100;

    /**
     * Dynamic theme shared preferences.
     */
    public static final String ADS_PREF_THEME = "ads_dynamic_theme";

    /**
     * Key for the theme preference.
     */
    public static final String ADS_PREF_THEME_KEY = "ads_theme_";

    /**
     * Default shift amount to generate the darker color.
     */
    public static final float ADS_COLOR_SHIFT_DARK_DEFAULT = 0.863f;

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
     * Default corner size for the theme.
     */
    private static final int ADS_CORNER_SIZE_DEFAULT = DynamicUnitUtils.convertDpToPixels(2);

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
     * Collection of dynamic listeners to send them event callback.
     */
    private List<DynamicListener> mDynamicListeners;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #initializeInstance(Context)} instead.
     */
    private DynamicTheme() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The application context to be attached with this theme.
     */
    private DynamicTheme(@NonNull Context context) {
        this.mContext = context;
        this.mDynamicListeners = new ArrayList<>();
        this.mDefaultApplicationTheme = new DynamicAppTheme(ADS_COLOR_PRIMARY_DEFAULT,
                ADS_COLOR_PRIMARY_DARK_DEFAULT, ADS_COLOR_ACCENT_DEFAULT,
                ADS_CORNER_SIZE_DEFAULT, Theme.BackgroundAware.ENABLE);
        this.mApplicationTheme = new DynamicAppTheme();
        this.mRemoteTheme = new DynamicWidgetTheme();
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
        this.mLocalContext = localContext;
        this.mDefaultLocalTheme = new DynamicAppTheme(ADS_COLOR_PRIMARY_DEFAULT,
                ADS_COLOR_PRIMARY_DARK_DEFAULT, ADS_COLOR_ACCENT_DEFAULT,
                ADS_CORNER_SIZE_DEFAULT, Theme.BackgroundAware.ENABLE);
        this.mLocalTheme = new DynamicAppTheme();

        if (localContext instanceof Activity && ((Activity) localContext)
                .getLayoutInflater().getFactory2() == null) {
            LayoutInflaterCompat.setFactory2(((Activity) localContext)
                    .getLayoutInflater(), new DynamicLayoutInflater());
        }

        return this;
    }

    /**
     * Initialize theme when application starts.
     * <p>Must be initialized once.
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
                    .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                            mContext, theme, R.attr.ads_cornerRadius,
                            mDefaultApplicationTheme.getCornerRadius()))
                    .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                            mContext, theme, R.attr.ads_backgroundAware,
                            mDefaultApplicationTheme.getBackgroundAware()));

            mApplicationTheme = mDefaultApplicationTheme;

            if (initializeRemoteColors) {
                initializeRemoteColors();
            }
        }

        return this;
    }

    /**
     * Initialize colors form the supplied dynamic app theme.
     *
     * @param dynamicTheme The dynamic app theme to initialize colors.
     * @param initializeRemoteColors {@code true} to initialize remote colors also.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme setTheme(@Nullable DynamicAppTheme dynamicTheme,
            boolean initializeRemoteColors) {
        if (dynamicTheme != null) {
            if (dynamicTheme.getThemeRes() == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
                throw new IllegalStateException("Dynamic app theme style resource " +
                        "id is not found for the application theme.");
            }

            setThemeRes(dynamicTheme.getThemeRes(), false);
            mApplicationTheme = dynamicTheme;

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
                    .setCornerRadius(DynamicResourceUtils.resolveDimensionPixelOffSet(
                            mContext, localTheme, R.attr.ads_cornerRadius,
                            mDefaultLocalTheme.getCornerRadius()))
                    .setBackgroundAware(DynamicResourceUtils.resolveInteger(
                            mContext, localTheme, R.attr.ads_backgroundAware,
                            mDefaultLocalTheme.getBackgroundAware()));

            mLocalTheme = mDefaultLocalTheme;
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
            mLocalTheme = dynamicLocalTheme;
        }

        return this;
    }

    /**
     * Initialize remote colors according to the base colors.
     * <p>They can be set individually by calling the appropriate functions.
     *
     * @return The {@link DynamicTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicTheme initializeRemoteColors() {
        mRemoteTheme = (DynamicWidgetTheme) new DynamicWidgetTheme(mApplicationTheme)
                .setBackgroundColor(ContextCompat.getColor(getResolvedContext(),
                !DynamicVersionUtils.isLollipop()
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

        mContext = null;
        mLocalContext = null;
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
     * Generates dark variant of the supplied color.
     *
     * @param color The color to generate the dark variant.
     *
     * @return The generated dark variant.
     */
    public @ColorInt int generateDarkColor(@ColorInt int color) {
        return DynamicColorUtils.shiftColor(color, DynamicTheme.ADS_COLOR_SHIFT_DARK_DEFAULT);
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
            DynamicPreferences.getInstance().savePrefs(ADS_PREF_THEME,
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
        return DynamicPreferences.getInstance().loadPrefs(ADS_PREF_THEME,
                ADS_PREF_THEME_KEY + context.getClass().getName(), null);
    }

    /**
     * Delete the supplied context theme from shared preferences.
     *
     * @param context The context to delete the theme.
     */
    public void deleteLocalTheme(@NonNull Context context) {
        try {
            DynamicPreferences.getInstance().deletePrefs(ADS_PREF_THEME,
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
}
