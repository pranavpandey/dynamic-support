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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderResolver;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;

/**
 * A {@link DynamicSliderPreference} to display a {@code customized} value string.
 *
 * @see DynamicSliderFloat
 * @see DynamicSliderRatio
 */
public class DynamicSliderValue extends DynamicSliderPreference {

    public DynamicSliderValue(@NonNull Context context) {
        super(context);
    }

    public DynamicSliderValue(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSliderValue(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setDynamicSliderResolver(new DynamicSliderResolver<Slider>() {
            @Override
            public @Nullable CharSequence getValueString(@Nullable CharSequence valueString,
                    int progress, float value, @Nullable CharSequence unit) {
                return onCustomizeValueString(valueString, progress, value, unit);
            }

            @Override
            public void onStartTrackingTouch(@Nullable Slider slider) { }

            @Override
            public void onProgressChanged(@Nullable Slider slider,
                    float progress, boolean fromUser) { }

            @Override
            public void onStopTrackingTouch(@Nullable Slider slider) { }
        });
    }

    /**
     * This method will be called to customize the value string.
     *
     * @param valueString The current value string.
     * @param progress The current progress level.
     * @param value The resolved value from the progress.
     * @param unit The optional value unit.
     *
     * @return The customized value string.
     */
    protected @Nullable CharSequence onCustomizeValueString(@Nullable CharSequence valueString,
            int progress, float value, @Nullable CharSequence unit) {
        return getValueString();
    }
}
