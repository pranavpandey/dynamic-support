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

package com.pranavpandey.android.dynamic.support.tutorial;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.pranavpandey.android.dynamic.support.tutorial.activity.DynamicTutorialActivity;

/**
 * Interface for the dynamic tutorial having useful methods which will be called by the
 * {@link DynamicTutorialActivity} to perform color transitions.
 *
 * <p><p>Any child view or fragment must implement this interface to support any color
 * transitions.
 */
public interface DynamicTutorial<T, V> extends ViewPager.OnPageChangeListener {

    /**
     * Returns the tutorial object.
     *
     * @return The tutorial object.
     */
    @NonNull T getTutorial();

    /**
     * Returns the tutorial view or fragment.
     *
     * @return The tutorial view or fragment.
     */
    @NonNull V createTutorial();

    /**
     * Returns the id for this tutorial.
     *
     * @return The id of this tutorial.
     */
    int getTutorialId();

    /**
     * Returns the background color for this tutorial.
     *
     * @return The background color used by this tutorial.
     */
    @ColorInt int getBackgroundColor();

    /**
     * This method will be called when there is a change in the background color of the activity.
     * <p>Implement this method to update any views during the transition.
     *
     * @param color The color of the background.
     */
    void onBackgroundColorChanged(@ColorInt int color);

    void onSetPadding(int left, int top, int right, int bottom);
}
