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

package com.pranavpandey.android.dynamic.support.graphic;

import android.graphics.Paint;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link Paint} to apply the {@link Paint.Cap} according to the {@link DynamicTheme}.
 *
 * @see Paint.Cap#BUTT
 * @see Paint.Cap#ROUND
 */
public class DynamicPaint extends Paint {

    public DynamicPaint() {
        this(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

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
     * Apply the {@link Paint.Cap} according to the {@link DynamicTheme}.
     */
    private void initialize() {
        setStrokeCap(DynamicTheme.getInstance().get().getCornerSize()
                >= Theme.Corner.MIN_OVAL ? Paint.Cap.ROUND : Paint.Cap.BUTT);
    }
}
