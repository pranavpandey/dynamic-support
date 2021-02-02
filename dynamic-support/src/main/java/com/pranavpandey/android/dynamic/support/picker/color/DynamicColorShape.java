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

import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape.CIRCLE;
import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape.RECTANGLE;
import static com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape.SQUARE;

/**
 * Interface to hold the color shape constant values to generate color swatches accordingly.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { CIRCLE, SQUARE, RECTANGLE })
public @interface DynamicColorShape {

    /**
     * Constant for the circle color shape.
     */
    int CIRCLE = 0;

    /**
     * Constant for the square color shape.
     */
    int SQUARE = 1;

    /**
     * Constant for the rectangle color shape.
     */
    int RECTANGLE = 2;
}
