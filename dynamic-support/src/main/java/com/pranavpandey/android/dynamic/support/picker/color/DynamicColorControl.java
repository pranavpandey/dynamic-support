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

package com.pranavpandey.android.dynamic.support.picker.color;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorControl.ALL;
import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorControl.CMYK;
import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorControl.HSV;
import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorControl.RGB;

/**
 * Interface to hold the color control constant values to provide color sliders accordingly.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { ALL, HSV, RGB, CMYK })
public @interface DynamicColorControl {

    /**
     * Constant for All (HSV, RGB and CMYK) the controls.
     */
    int ALL = 0;

    /**
     * Constant for the HSV controls.
     */
    int HSV = 1;

    /**
     * Constant for the RGB controls.
     */
    int RGB = 2;

    /**
     * Constant for the CMYK controls.
     */
    int CMYK = 3;
}
