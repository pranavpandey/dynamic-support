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

package com.pranavpandey.android.dynamic.support.listener;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;

/**
 * An interface to listen the dynamic change events.
 */
public interface DynamicListener {

    /**
     * This method will be called when the navigation bar theme has been changed.
     */
    void onNavigationBarThemeChanged();

    /**
     * This method will be called when the dynamic change event occurs (like theme, locale, etc.).
     * <p>Recreate the activity or application here to adapt changes.
     *
     * @param context {@code true} if there is a context change and it must be reinitialized.
     *
     * @param recreate {@code true} if listener must be recreated to adapt the changes.
     */
    void onDynamicChanged(boolean context, boolean recreate);

    /**
     * This method will be called when the dynamic configuration change event occurs
     * (like locale, font scale, orientation, ui mode, etc.).
     * <p>It will provide more control on {@link #onDynamicChanged(boolean, boolean)} method call.
     *
     * @param locale {@code true} if locale is changed.
     * @param fontScale {@code true} if font scale is changed.
     * @param orientation {@code true} if there is an orientation change.
     * @param uiMode {@code true} if ui mode is changed.
     * @param density {@code true} if configuration density is changed.
     *
     * @see ActivityInfo#CONFIG_LOCALE
     * @see ActivityInfo#CONFIG_FONT_SCALE
     * @see ActivityInfo#CONFIG_ORIENTATION
     * @see ActivityInfo#CONFIG_UI_MODE
     * @see ActivityInfo#CONFIG_DENSITY
     */
    void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density);

    /**
     * This method will be called when the auto theme change event occurs according to the time.
     * <p>Recreate the activity or application here to adapt changes.
     */
    void onAutoThemeChanged();

    /**
     * This method will be called when the power save mode has been changed.
     *
     * <p><p>It will be called only on API 21 and above.
     *
     * @param powerSaveMode {@code true} if the device is in power save mode.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void onPowerSaveModeChanged(boolean powerSaveMode);
}
