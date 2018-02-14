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

package com.pranavpandey.android.dynamic.support.tutorial;

import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;

/**
 * Interface for the dynamic tutorial having useful functions which will be
 * called by the {@link DynamicTutorialActivity} to perform color transitions.
 * Any child view or fragment must implement this interface to support any
 * color transitions.
 */
public interface DynamicTutorial extends ViewPager.OnPageChangeListener {

    /**
     * @return The id of this tutorial.
     */
    int getTutorialId();

    /**
     * @return The background color used by this tutorial.
     */
    @ColorInt int getBackgroundColor();

    /**
     * This method will be called when there is a change in the background
     * color of the activity. Implement this function to update any views
     * during the transition.
     */
    void onBackgroundColorChanged(@ColorInt int color);
}
