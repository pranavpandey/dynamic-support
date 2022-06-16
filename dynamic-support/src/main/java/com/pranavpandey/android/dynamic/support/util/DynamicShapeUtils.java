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

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicUnitUtils;

/**
 * Helper class to perform shape operations.
 */
public class DynamicShapeUtils {

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param adjustCorner {@code true} to automatically adjust the corner radius.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly, boolean adjustCorner) {
        float cornerRadiusPixel = DynamicUnitUtils.convertDpToPixels(adjustCorner
                ? Math.max(0, cornerRadius - Defaults.ADS_STROKE_CORNER_ADJUST)
                : cornerRadius);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        MaterialShapeDrawable drawable;

        if (!topOnly) {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(cornerRadiusPixel).build();
        } else {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(cornerRadiusPixel)
                    .setTopRightCornerSize(cornerRadiusPixel).build();
        }

        drawable = new MaterialShapeDrawable(shapeAppearanceModel);
        drawable.setTint(color);

        return drawable;
    }

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawable(cornerRadius, color, topOnly, topOnly);
    }

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param adjustCorner {@code true} to automatically adjust the corner radius.
     * @param strokeSize The size {@code greater than 0} in dip to enable the stroke.
     * @param strokeColor The color for the stroke.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableWithStroke(float cornerRadius,
            @ColorInt int color, boolean topOnly, boolean adjustCorner,
            float strokeSize, @ColorInt int strokeColor) {
        Drawable drawable = getCornerDrawable(cornerRadius, color, topOnly, adjustCorner);

        if (strokeSize > 0 && Color.alpha(strokeColor) > 0
                && drawable instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable) drawable).setStroke(
                    DynamicUnitUtils.convertDpToPixels(strokeSize), strokeColor);
        }

        return drawable;
    }

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param adjustCorner {@code true} to automatically adjust the corner radius.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableWithStroke(float cornerRadius,
            @ColorInt int color, boolean topOnly, boolean adjustCorner) {
        return getCornerDrawableWithStroke(cornerRadius, color, topOnly, adjustCorner,
                Defaults.ADS_STROKE_WIDTH, DynamicColorUtils.setAlpha(
                        DynamicColorUtils.getTintColor(color), Defaults.ADS_STROKE_ALPHA));
    }

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param strokeColor The color for the stroke.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableWithStroke(float cornerRadius,
            @ColorInt int color, boolean topOnly, @ColorInt int strokeColor) {
        return getCornerDrawableWithStroke(cornerRadius, color, topOnly,
                topOnly, Defaults.ADS_STROKE_WIDTH, strokeColor);
    }

    /**
     * Returns a {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The {@link MaterialShapeDrawable} drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableWithStroke(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawableWithStroke(cornerRadius, color, topOnly, topOnly);
    }

    /**
     * Returns a corner drawable according to the supplied parameters.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacy(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly) {
        float adjustedCornerRadius = cornerRadius;

        if (!topOnly) {
            return DynamicDrawableUtils.getCornerDrawable(
                    width, height, adjustedCornerRadius, color);
        } else {
            adjustedCornerRadius = Math.max(0,
                    cornerRadius - Defaults.ADS_STROKE_CORNER_ADJUST);
            adjustedCornerRadius = DynamicUnitUtils.convertDpToPixels(adjustedCornerRadius);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadii(new float[] {
                    adjustedCornerRadius, adjustedCornerRadius,
                    adjustedCornerRadius, adjustedCornerRadius,
                    0, 0, 0, 0 });

            return DynamicDrawableUtils.getCornerDrawable(width, height, drawable, color);
        }
    }

    /**
     * Returns a corner drawable according to the supplied parameters.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacy(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawableLegacy(0, 0, cornerRadius, color, topOnly);
    }

    /**
     * Returns a corner drawable according to the supplied parameters.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param strokeSize The size {@code greater than 0} in dip to enable the stroke.
     * @param strokeColor The color for the stroke.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacyWithStroke(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly,
            float strokeSize, @ColorInt int strokeColor) {
        Drawable drawable = getCornerDrawableLegacy(width, height, cornerRadius, color, topOnly);

        if (strokeSize > 0 && Color.alpha(strokeColor) > 0
                && drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setStroke(
                    DynamicUnitUtils.convertDpToPixels(strokeSize), strokeColor);
        }

        return drawable;
    }

    /**
     * Returns a corner drawable according to the supplied parameters.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param strokeColor The color for the stroke.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacyWithStroke(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly, @ColorInt int strokeColor) {
        return getCornerDrawableLegacyWithStroke(width, height, cornerRadius, color,
                topOnly, Defaults.ADS_STROKE_WIDTH, strokeColor);
    }

    /**
     * Returns a corner drawable according to the supplied parameters.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacyWithStroke(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly) {
        return getCornerDrawableLegacyWithStroke(width, height, cornerRadius, color,
                topOnly, Defaults.ADS_STROKE_WIDTH, DynamicColorUtils.setAlpha(
                        DynamicColorUtils.getTintColor(color), Defaults.ADS_STROKE_ALPHA));
    }

    /**
     * Returns a corner drawable according to the supplied parameters.

     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param strokeSize The size {@code greater than 0} in dip to enable the stroke.
     * @param strokeColor The color for the stroke.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacyWithStroke(
            float cornerRadius, @ColorInt int color, boolean topOnly,
            float strokeSize, @ColorInt int strokeColor) {
        return getCornerDrawableLegacyWithStroke(0, 0,
                cornerRadius, color, topOnly, strokeSize, strokeColor);
    }

    /**
     * Returns a corner drawable according to the supplied parameters.

     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     *
     * @return The corner drawable according to the supplied parameters.
     */
    public static @NonNull Drawable getCornerDrawableLegacyWithStroke(
            float cornerRadius, @ColorInt int color, boolean topOnly) {
        return getCornerDrawableLegacyWithStroke(0, 0, cornerRadius, color, topOnly);
    }
}
