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

package com.pranavpandey.android.dynamic.support.utils;

import android.annotation.TargetApi;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

/**
 * Helper class to perform various {@link SeekBar} operations.
 */
public class DynamicSeekBarUtils {

    /**
     * Set a hue gradient progress drawable for a seek bar.
     *
     * @param seekBar The seek bar to set the hue gradient.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setHueDrawable(@NonNull SeekBar seekBar) {
        if (DynamicVersionUtils.isLollipop()) {
            seekBar.setProgressTintList(null);
        }

        LinearGradient gradient =
                new LinearGradient(0.0f, 0.0f, (float) seekBar.getWidth(), 0.0f,
                        new int[] { 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00,
                                0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000 },
                        null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(gradient);

        Rect bounds = seekBar.getProgressDrawable().getBounds();
        bounds.inset(0, (int) (bounds.height() * 0.45f));

        seekBar.setProgressDrawable(shape);
        seekBar.getProgressDrawable().setBounds(bounds);
    }
}
