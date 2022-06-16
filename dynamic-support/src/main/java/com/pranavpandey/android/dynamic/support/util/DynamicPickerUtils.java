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

package com.pranavpandey.android.dynamic.support.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.widget.SeekBar;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPicker;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * Helper class to perform various {@link SeekBar} operations.
 */
public class DynamicPickerUtils {

    /**
     * Returns the hue colors.
     *
     * @return The hue colors.
     */
    public static @NonNull int[] getHueColors() {
        return new int[] { 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00,
                0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000 };
    }

    /**
     * Returns the hue gradient.
     *
     * @param width The gradient width.
     * @param height The gradient height.
     *
     * @return The hue gradient.
     */
    public static @NonNull LinearGradient getHueGradient(float width, float height) {
        return new LinearGradient(0.0f, 0.0f, width, height,
                getHueColors(), null, Shader.TileMode.CLAMP);
    }

    /**
     * Returns an alpha pattern paint.
     *
     * @param pixelSize The size of one pixel.
     *
     * @return The alpha pattern.
     */
    public static @NonNull Paint getAlphaPatternPaint(int pixelSize) {
        Bitmap bitmap = Bitmap.createBitmap(pixelSize * 2,
                pixelSize * 2, Bitmap.Config.ARGB_8888);
        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);

        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, pixelSize, pixelSize);
        fill.setColor(0xFFEAEAEA);
        canvas.drawRect(rect, fill);
        rect.offset(pixelSize, pixelSize);
        canvas.drawRect(rect, fill);
        fill.setColor(0xFFA9A9A9);
        rect.offset(-pixelSize, 0);
        canvas.drawRect(rect, fill);
        rect.offset(pixelSize, -pixelSize);
        canvas.drawRect(rect, fill);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.REPEAT,
                BitmapShader.TileMode.REPEAT));

        return paint;
    }

    /**
     * Set a hue gradient progress drawable for a seek bar.
     *
     * @param seekBar The seek bar to set the hue gradient.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setHueDrawable(@Nullable SeekBar seekBar) {
        if (seekBar == null) {
            return;
        }

        if (DynamicSdkUtils.is21()) {
            seekBar.setProgressTintList(null);
        }

        LinearGradient gradient = getHueGradient(seekBar.getWidth(), 0);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(gradient);

        Rect bounds = seekBar.getProgressDrawable().getBounds();
        bounds.inset(0, (int) (bounds.height() * Defaults.ADS_INSET_HUE_SMALL));

        seekBar.setProgressDrawable(shape);
        seekBar.getProgressDrawable().setBounds(bounds);
    }

    /**
     * Save the recently copied color to shared preferences.
     *
     * @param color The color to be saved.
     */
    public static void setRecentColor(@ColorInt int color) {
        DynamicPreferences.getInstance().save(
                DynamicColorPicker.ADS_PREF_COLOR_PICKER_RECENT, color);
    }

    /**
     * Returns the recently copied color from the shared preferences.
     *
     * @return The recently copied color from the shared preferences.
     */
    public static @ColorInt int getRecentColor() {
        return DynamicPreferences.getInstance().load(
                DynamicColorPicker.ADS_PREF_COLOR_PICKER_RECENT,
                Theme.Color.UNKNOWN);
    }
}
