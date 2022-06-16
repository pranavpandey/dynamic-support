/*
 * Copyright 2018-2022 Pranav Pandey
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
 * <p>Any child view or fragment must implement this interface to support any color
 * transitions.
 */
public interface Tutorial<T, V> extends ViewPager.OnPageChangeListener {

    /**
     * A {@link Tutorial} to support shared element(s) transition.
     */
    interface SharedElement<T, V> extends Tutorial<T, V> {

        /**
         * Returns whether this tutorial supports shared element(s) transition.
         *
         * @return {@code true} if this tutorial supports shared element(s) transition.
         */
        boolean isSharedElement();
    }

    /**
     *  Activity scene transition name for the tutorial.
     */
    String ADS_NAME_TUTORIAL = "ads_name:tutorial";

    /**
     *  Activity scene transition name for the tutorial image.
     */
    String ADS_NAME_TUTORIAL_IMAGE = ADS_NAME_TUTORIAL + ":image";

    /**
     *  Activity scene transition name for the tutorial title.
     */
    String ADS_NAME_TUTORIAL_TITLE = ADS_NAME_TUTORIAL + ":title";

    /**
     *  Activity scene transition name for the tutorial subtitle.
     */
    String ADS_NAME_TUTORIAL_SUBTITLE = ADS_NAME_TUTORIAL + ":subtitle";

    /**
     * Returns the id for this tutorial.
     *
     * @return The id of this tutorial.
     */
    int getTutorialId();

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
     * Returns the color used by this tutorial.
     *
     * @return The color used by this tutorial.
     */
    @ColorInt int getColor();

    /**
     * Returns the tint color used by this tutorial.
     *
     * @return The tint color used by this tutorial.
     */
    @ColorInt int getTintColor();

    /**
     * This method will be called when there is a change in the colors of the activity.
     * <p>Implement this method to update any views during the transition.
     *
     * @param color The color used by the tutorial.
     * @param tintColor The tint color used by this tutorial.
     */
    void onColorChanged(@ColorInt int color, @ColorInt int tintColor);

    /**
     * This method will be called on setting the padding of the tutorial.
     * <p>Implement this method to update any views according to the container.
     *
     * @param left The left padding supplied by the container.
     * @param top The top padding supplied by the container.
     * @param right The right padding supplied by the container.
     * @param bottom The bottom padding supplied by the container.
     */
    void onSetPadding(int left, int top, int right, int bottom);
}
