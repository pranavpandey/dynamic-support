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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.Nullable;

/**
 * A callback that notifies clients when the progress level has been changed.
 * <p>This includes changes that were initiated by the user through a touch gesture
 * or arrow key/trackball as well as changes that were initiated programmatically.
 */
public interface DynamicSliderChangeListener<S> {

    /**
     * This method will be called when the user has started a touch gesture.
     *
     * @param slider The slider in which the touch gesture began.
     */
    void onStartTrackingTouch(@Nullable S slider);

    /**
     * This method will be called when the slider value has changed.
     *
     * @param slider The slider whose value has changed.
     * @param progress The current progress level.
     * @param fromUser {@code true} if the value change was initiated by the user.
     */
    void onProgressChanged(@Nullable S slider, float progress, boolean fromUser);

    /**
     * This method will be called when the user has finished a touch gesture.
     *
     * @param slider The slider in which the touch gesture finished.
     */
    void onStopTrackingTouch(@Nullable S slider);
}
