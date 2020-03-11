/*
 * Copyright 2018-2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.splash;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * Interface to implement the splash screen and to get various callbacks while showing the splash.
 */
public interface DynamicSplashListener {

    /**
     * This method will be called to get the layout resource for the splash.
     *
     * @return The splash layout resource.
     */
    @LayoutRes int getLayoutRes();

    /**
     * This method will be called just after creating the splash fragment with the supplied
     * layout resource.
     * <p>Do any modifications in the splash layout here.
     *
     * @param view The view created from the layout resource.
     */
    void onViewCreated(@NonNull View view);

    /**
     * This method will be called to get the minimum time in milliseconds for which the splash
     * must be displayed.
     *
     * @return The minimum time in milliseconds for which the splash must be displayed.
     */
    long getMinSplashTime();

    /**
     * This method will be called before starting the splash background task.
     */
    void onPreSplash();

    /**
     * This method will be called after starting the splash background task.
     * <p>Do any time taking operation here. Do not perform any UI related operation here,
     * use {@link #onPostSplash()} here.
     */
    void doBehindSplash();

    /**
     * This method will be called after finishing the splash background task.
     */
    void onPostSplash();
}
