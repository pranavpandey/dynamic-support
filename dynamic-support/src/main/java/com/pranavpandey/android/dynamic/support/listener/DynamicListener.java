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

package com.pranavpandey.android.dynamic.support.listener;

/**
 * An interface to listen the dynamic change events.
 */
public interface DynamicListener {

    /**
     * This method will be called when the navigation bar theme has been changed.
     */
    void onNavigationBarThemeChange();

    /**
     * This method will be called when the dynamic change event occurs (like theme, locale, etc.).
     * <p>Re-create the activity or application here to adapt changes.
     *
     * @param context {@code true} if there is a context change and it must be reinitialized.
     *
     * @param recreate {@code true} if listener must be recreated to adapt the changes.
     */
    void onDynamicChange(boolean context, boolean recreate);
}
