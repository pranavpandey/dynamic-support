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

package com.pranavpandey.android.dynamic.support.graphics;

import android.graphics.Paint;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * A {@link Paint} to apply the {@link Paint.Cap} according ot the {@link DynamicTheme}.
 *
 * @see Paint.Cap#BUTT
 * @see Paint.Cap#ROUND
 */
public class DynamicPaint extends Paint {

    public DynamicPaint() {
        super();

        initialize();
    }

    public DynamicPaint(int flags) {
        super(flags);

        initialize();
    }

    public DynamicPaint(Paint paint) {
        super(paint);

        initialize();
    }

    /**
     * Apply the {@link Paint.Cap} according ot the {@link DynamicTheme}.
     */
    private void initialize() {
        setStrokeCap(DynamicTheme.getInstance().get().getCornerSizeDp()
                >= Defaults.ADS_CORNER_MIN_THEME_ROUND ? Paint.Cap.ROUND : Paint.Cap.BUTT);
    }
}
