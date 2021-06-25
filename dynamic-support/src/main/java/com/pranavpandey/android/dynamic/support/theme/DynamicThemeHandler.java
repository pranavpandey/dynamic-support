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

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

import java.util.List;

/**
 * A {@link Handler} to handle theme updates on the main thread.
 *
 * @see Looper#getMainLooper()
 */
public class DynamicThemeHandler extends Handler implements DynamicListener {

    /**
     * Message constant to post the dynamic theme changes.
     *
     * @see DynamicListener#onDynamicChanged(boolean, boolean)
     */
    public static final int MESSAGE_POST_DYNAMIC = 0x1;

    /**
     * Message constant to post the dynamic configuration changes.
     *
     * @see DynamicListener#onDynamicConfigurationChanged(
     * boolean, boolean, boolean, boolean, boolean)
     */
    public static final int MESSAGE_POST_DYNAMIC_CONFIGURATION = 0x2;

    /**
     * Message constant to post the dynamic color changes.
     *
     * @see DynamicListener#onDynamicColorsChanged(DynamicColors)
     */
    public static final int MESSAGE_POST_DYNAMIC_COLOR = 0x3;

    /**
     * Message constant to post the auto theme changes.
     *
     * @see DynamicListener#onAutoThemeChanged()
     */
    public static final int MESSAGE_POST_AUTO_THEME = 0x4;

    /**
     * Message constant to post the power save mode changes.
     *
     * @see DynamicListener#onPowerSaveModeChanged(boolean)
     */
    public static final int MESSAGE_POST_POWER_SAVE_MODE = 0x5;

    /**
     * Message constant to post the navigation bar theme changes.
     *
     * @see DynamicListener#onNavigationBarThemeChanged()
     */
    public static final int MESSAGE_POST_NAVIGATION_BAR_THEME = 0x6;

    /**
     * Bundle key to store the context changes.
     */
    public static final String DATA_BOOLEAN_CONTEXT = "ads_data_boolean_context";

    /**
     * Bundle key to store the recreate changes.
     */
    public static final String DATA_BOOLEAN_RECREATE = "ads_data_boolean_recreate";

    /**
     * Bundle key to store the locale changes.
     */
    public static final String DATA_BOOLEAN_LOCALE = "ads_data_boolean_locale";

    /**
     * Bundle key to store the font scale changes.
     */
    public static final String DATA_BOOLEAN_FONT_SCALE = "ads_data_boolean_font_scale";

    /**
     * Bundle key to store the orientation changes.
     */
    public static final String DATA_BOOLEAN_ORIENTATION = "ads_data_boolean_orientation";

    /**
     * Bundle key to store the UI mode changes.
     */
    public static final String DATA_BOOLEAN_UI_MODE = "ads_data_boolean_ui_mode";

    /**
     * Bundle key to store the density changes.
     */
    public static final String DATA_BOOLEAN_DENSITY = "ads_data_boolean_density";

    /**
     * Bundle key to store the power save mode changes.
     */
    public static final String DATA_BOOLEAN_POWER_SAVE_MODE = "ads_data_boolean_power_save_mode";

    /**
     * Bundle key to store the dynamic color changes.
     */
    public static final String DATA_PARCELABLE_COLORS = "ads_data_parcelable_colors";

    /**
     * List of listeners to receive the callbacks.
     */
    private final List<DynamicListener> mListeners;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param looper The looper to be used.
     * @param listeners The list of listeners to receive the callbacks.
     */
    public DynamicThemeHandler(@NonNull Looper looper, @NonNull List<DynamicListener> listeners) {
        super(looper);

        this.mListeners = listeners;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case MESSAGE_POST_DYNAMIC:
                if (msg.getData() != null) {
                    onDynamicChanged(msg.getData().getBoolean(DATA_BOOLEAN_CONTEXT),
                            msg.getData().getBoolean(DATA_BOOLEAN_RECREATE));
                }
                break;
            case MESSAGE_POST_DYNAMIC_CONFIGURATION:
                if (msg.getData() != null) {
                    onDynamicConfigurationChanged(
                            msg.getData().getBoolean(DATA_BOOLEAN_LOCALE),
                            msg.getData().getBoolean(DATA_BOOLEAN_FONT_SCALE),
                            msg.getData().getBoolean(DATA_BOOLEAN_ORIENTATION),
                            msg.getData().getBoolean(DATA_BOOLEAN_UI_MODE),
                            msg.getData().getBoolean(DATA_BOOLEAN_DENSITY));
                }
                break;
            case MESSAGE_POST_DYNAMIC_COLOR:
                onDynamicColorsChanged(msg.getData().getParcelable(DATA_PARCELABLE_COLORS));
                break;
            case MESSAGE_POST_AUTO_THEME:
                onAutoThemeChanged();
                break;
            case MESSAGE_POST_POWER_SAVE_MODE:
                if (msg.getData() != null) {
                    onPowerSaveModeChanged(msg.getData().getBoolean(DATA_BOOLEAN_POWER_SAVE_MODE));
                }
                break;
            case MESSAGE_POST_NAVIGATION_BAR_THEME:
                onNavigationBarThemeChanged();
                break;
        }
    }

    /**
     * Returns the list of listeners handled by this handler.
     *
     * @return The list of listeners handled by this handler.
     */
    public @Nullable List<DynamicListener> getListeners() {
        return mListeners;
    }

    /**
     * Add a dynamic listener to receive the various callbacks.
     *
     * @param listener The dynamic listener to be added.
     *
     * @see DynamicListener
     */
    public void addListener(@Nullable DynamicListener listener) {
        if (getListeners() != null && !getListeners().contains(listener)) {
            getListeners().add((DynamicListener) listener);
        }
    }

    /**
     * Remove a dynamic listener.
     *
     * @param listener The dynamic listener to be removed.
     *
     * @see DynamicListener
     */
    public void removeListener(@Nullable DynamicListener listener) {
        if (getListeners() != null) {
            getListeners().remove(listener);
        }
    }

    /**
     * Checks whether a dynamic listener is already registered.
     *
     * @param listener The dynamic listener to be checked.
     *
     * @return {@code true} if the listener is already registered.
     *
     * @see DynamicListener
     */
    public boolean isListener(@Nullable DynamicListener listener) {
        if (getListeners() == null) {
            return false;
        }

        return getListeners().contains(listener);
    }

    /**
     * Returns the latest listener added to the theme.
     *
     * @param latest {@code true} to resolve the latest listener.
     *
     * @return The latest listener added to the theme.
     */
    public @Nullable DynamicListener resolveListener(boolean latest) {
        if (getListeners() == null || getListeners().isEmpty()) {
            return null;
        }

        return getListeners().get(latest && getListeners().size() > 0
                ? getListeners().size() - 1 : 0);
    }

    /**
     * Remove all the dynamic listener.
     *
     * @see DynamicListener
     */
    public void clearListeners() {
        if (getListeners() != null) {
            getListeners().clear();
        }
    }

    @Override
    public @NonNull Context getContext() {
        DynamicListener listener;
        if ((listener = resolveListener(false)) == null) {
            return DynamicTheme.getInstance().getListener().getContext();
        }

        return listener.getContext();
    }

    @Override
    public boolean isNightMode(boolean resolve) {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return DynamicTheme.getInstance().getListener().isNightMode(resolve);
        }

        return listener.isNightMode(resolve);
    }

    @Override
    public @StyleRes int getThemeRes(@Nullable AppTheme<?> theme) {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return DynamicTheme.getInstance().getListener().getThemeRes(theme);
        }

        return listener.getThemeRes(theme);
    }

    @Override
    public @StyleRes int getThemeRes() {
        return getThemeRes(null);
    }

    @Override
    public @Nullable AppTheme<?> getDynamicTheme() {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return DynamicTheme.getInstance().getListener().getDynamicTheme();
        }

        return listener.getDynamicTheme();
    }

    @Override
    public boolean isDynamicColor() {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return DynamicTheme.getInstance().getListener().isDynamicColor();
        }

        return listener.isDynamicColor();
    }

    @Override
    public @ColorInt int getDefaultColor(@Theme.ColorType int colorType) {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return DynamicTheme.getInstance().getListener().getDefaultColor(colorType);
        }

        return listener.getDefaultColor(colorType);
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onDynamicChanged(context, recreate);
        }
    }

    @Override
    public void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density) {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onDynamicConfigurationChanged(locale,
                    fontScale, orientation, uiMode, density);
        }
    }

    @Override
    public void onDynamicColorsChanged(@Nullable DynamicColors colors) {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onDynamicColorsChanged(colors);
        }
    }

    @Override
    public void onAutoThemeChanged() {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onAutoThemeChanged();
        }
    }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onPowerSaveModeChanged(powerSaveMode);
        }
    }

    @Override
    public boolean setNavigationBarTheme() {
        DynamicListener listener;
        if ((listener = resolveListener(true)) == null) {
            return false;
        }

        return listener.setNavigationBarTheme();
    }

    @Override
    public void onNavigationBarThemeChanged() {
        if (getListeners() == null) {
            return;
        }

        for (DynamicListener listener : getListeners()) {
            listener.onNavigationBarThemeChanged();
        }
    }
}
