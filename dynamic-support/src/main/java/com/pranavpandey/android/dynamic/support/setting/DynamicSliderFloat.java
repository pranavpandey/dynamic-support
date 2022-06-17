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
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderResolver;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;

/**
 * A {@link DynamicSliderPreference} to display value as {@code float} up to two decimal places.
 */
public class DynamicSliderFloat extends DynamicSliderPreference {

    public DynamicSliderFloat(@NonNull Context context) {
        super(context);
    }

    public DynamicSliderFloat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSliderFloat(@NonNull Context context,
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
                return getContext() == null ? valueString : String.format(
                        getContext().getString(R.string.ads_format_float_two),
                        value / getMaxValue());
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
}
