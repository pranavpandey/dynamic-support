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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.SeekBar;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.color.DynamicColors;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.graphic.DynamicPaint;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPicker;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicFileUtils;
import com.pranavpandey.android.dynamic.util.DynamicIntentUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

import java.io.File;

/**
 * Helper class to perform various {@link SeekBar} operations.
 */
public class DynamicPickerUtils {

    /**
     * Returns the default value for the night theme according to the available functionality.
     *
     * @return The default value for the night theme according to the available functionality.
     *
     * @see DynamicSdkUtils#is28()
     * @see Theme.Night.ToString#SYSTEM
     * @see Theme.Night.ToString#AUTO
     */
    public static @Theme.ToString String getDefaultNightThemeAlt() {
        return DynamicSdkUtils.is28() ? Theme.Night.ToString.SYSTEM : Theme.Night.ToString.AUTO;
    }

    /**
     * Returns the default value for the color palette according to the available functionality.
     *
     * @return The default value for the color palette according to the available functionality.
     *
     * @see DynamicColors#isDynamicColorAvailable()
     * @see Theme.Color.ToString#SYSTEM
     * @see Theme.Color.ToString#AUTO
     */
    public static @Theme.Color.ToString String getDefaultColorPalette() {
        return DynamicColors.isDynamicColorAvailable()
                ? Theme.Color.ToString.SYSTEM : Theme.Color.ToString.APP;
    }

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
        Paint fill = new DynamicPaint();
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

        Paint paint = new DynamicPaint();
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

    /**
     * Try to request a storage location for the supplied file.
     *
     * @param context The context to get the file URI.
     * @param owner The requesting owner for the intent.
     * @param file The file URI to request the storage location.
     * @param mimeType The mime type of the file.
     * @param requestCode The request code for the intent.
     * @param downloads {@code true} to return the download location on unsupported API levels.
     * @param fileName The default file name.
     *
     * @return {@code null} if the request for storage location for the supplied file was
     *         successful. Otherwise, the {@code downloads} location URI based on the
     *         supplied parameter.
     *
     * @see DynamicFileUtils#getSaveToFileIntent(Context, Uri, String)
     * @see Activity#startActivityForResult(Intent, int)
     */
    public static @Nullable Uri saveToFile(@Nullable Context context,
            @Nullable LifecycleOwner owner, @Nullable Uri file, @NonNull String mimeType,
            int requestCode, boolean downloads, @Nullable String fileName) {
        if (context == null) {
            return null;
        }

        if (owner != null && DynamicIntentUtils.isFilePicker(context, mimeType)) {
            if (owner instanceof Activity) {
                ((Activity) owner).startActivityForResult(DynamicFileUtils
                        .getSaveToFileIntent((Activity) owner, file, mimeType), requestCode);

                return null;
            } else if (owner instanceof Fragment) {
                ((Fragment) owner).startActivityForResult(DynamicFileUtils
                        .getSaveToFileIntent(((Fragment) owner).requireContext(),
                                file, mimeType), requestCode);

                return null;
            }
        }

        return downloads ? DynamicFileUtils.getUriFromFile(context,
                DynamicFileUtils.getPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)) : null;
    }

    /**
     * Try to request a storage location for the supplied file.
     *
     * @param context The context to get the file URI.
     * @param owner The requesting owner for the intent.
     * @param file The file URI to request the storage location.
     * @param mimeType The mime type of the file.
     * @param requestCode The request code for the intent.
     *
     * @return {@code null} if the request for storage location for the supplied file was
     *         successful. Otherwise, the {@code downloads} location URI.
     *
     * @see #saveToFile(Context, LifecycleOwner, Uri, String, int, boolean, String)
     */
    public static @Nullable Uri saveToFile(@Nullable Context context,
            @Nullable LifecycleOwner owner, @Nullable Uri file,
            @NonNull String mimeType, int requestCode) {
        return saveToFile(context, owner, file, mimeType, requestCode, true, null);
    }

    /**
     * Try to request a storage location for the supplied file.
     *
     * @param context The context to get the file URI.
     * @param owner The requesting owner for the intent.
     * @param file The file to request the storage location.
     * @param mimeType The mime type of the file.
     * @param requestCode The request code for the intent.
     * @param downloads {@code true} to return the download location on unsupported API levels.
     * @param fileName The default file name.
     *
     * @return {@code null} if the request for storage location for the supplied file was
     *         successful. Otherwise, the {@code downloads} location URI based on the
     *         supplied parameter.
     *
     * @see DynamicFileUtils#getSaveToFileIntent(Context, File, String)
     * @see Activity#startActivityForResult(Intent, int)
     */
    public static @Nullable File saveToFile(@Nullable Context context,
            @Nullable LifecycleOwner owner, @Nullable File file, @NonNull String mimeType,
            int requestCode, boolean downloads, @Nullable String fileName) {
        if (context == null) {
            return null;
        }

        if (owner != null && DynamicIntentUtils.isFilePicker(context, mimeType)) {
            if (owner instanceof Activity) {
                ((Activity) owner).startActivityForResult(DynamicFileUtils
                        .getSaveToFileIntent((Activity) owner, file, mimeType), requestCode);

                return null;
            } else if (owner instanceof Fragment) {
                ((Fragment) owner).startActivityForResult(DynamicFileUtils
                        .getSaveToFileIntent(((Fragment) owner).requireContext(),
                                file, mimeType), requestCode);

                return null;
            }
        }

        return downloads ? DynamicFileUtils.getPublicDir(
                Environment.DIRECTORY_DOWNLOADS, fileName) : null;
    }

    /**
     * Try to request a storage location for the supplied file.
     *
     * @param context The context to get the file URI.
     * @param owner The requesting owner for the intent.
     * @param file The file to request the storage location.
     * @param mimeType The mime type of the file.
     * @param requestCode The request code for the intent.
     *
     * @return {@code null} if the request for storage location for the supplied file was
     *         successful. Otherwise, the {@code downloads} location URI.
     *
     * @see #saveToFile(Context, LifecycleOwner, File, String, int, boolean, String)
     */
    public static @Nullable File saveToFile(@Nullable Context context,
            @Nullable LifecycleOwner owner, @Nullable File file,
            @NonNull String mimeType, int requestCode) {
        return saveToFile(context, owner, file, mimeType, requestCode, true, null);
    }

    /**
     * Try to request an intent to select a file according to the supplied mime type.
     *
     * @param context The context to get the file URI.
     * @param owner The requesting owner for the intent.
     * @param mimeType The mime type of the file.
     * @param requestCode The request code for the intent.
     *
     * @return {@code null} if the request for storage location for the supplied file was
     *         successful. Otherwise, the {@code downloads} location URI.
     *
     * @see DynamicFileUtils#getFileSelectIntent(String)
     * @see Activity#startActivityForResult(Intent, int)
     */
    public static boolean selectFile(@Nullable Context context,
            @Nullable LifecycleOwner owner, @NonNull String mimeType, int requestCode) {
        if (owner != null && DynamicIntentUtils.isFilePicker(context, mimeType)) {
            if (owner instanceof Activity) {
                ((Activity) owner).startActivityForResult(DynamicFileUtils
                        .getFileSelectIntent(mimeType), requestCode);

                return true;
            } else if (owner instanceof Fragment) {
                ((Fragment) owner).startActivityForResult(DynamicFileUtils
                        .getFileSelectIntent(mimeType), requestCode);

                return true;
            }
        }

        return false;
    }
}
