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

package com.pranavpandey.android.dynamic.support.picker;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.picker.DynamicPickerType.CUSTOM;
import static com.pranavpandey.android.dynamic.support.picker.DynamicPickerType.PRESETS;

/**
 * Interface to hold the color type constant values to provide show presets or custom view
 * accordingly.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { PRESETS, CUSTOM })
public @interface DynamicPickerType {

    /**
     * Constant to show the presets view.
     */
    int PRESETS = 0;

    /**
     * Constant to show the custom.
     */
    int CUSTOM = 1;
}
