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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.Nullable;

/**
 * A callback that notifies clients when the progress level has been changed.
 * <p>This includes changes that were initiated by the user through a touch gesture
 * or arrow key/trackball as well as changes that were initiated programmatically.
 *
 * @param <S> The type of the slider attached to this resolver.
 */
public interface DynamicSliderResolver<S> extends DynamicSliderChangeListener<S> {

    /**
     * Returns user visible value string according to the slider value and
     * the optional value unit.
     *
     * @param valueString The current value string.
     * @param progress The current progress level.
     * @param value The resolved value from the progress.
     * @param unit The optional value unit.
     *
     * @return The user visible value string.
     */
    @Nullable CharSequence getValueString(@Nullable CharSequence valueString,
            int progress, float value, @Nullable CharSequence unit);
}
