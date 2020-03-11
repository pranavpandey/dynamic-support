/*
 * Copyright 2018-2020 Pranav Pandey
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

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * Helper class to perform shape operations.
 */
public class DynamicShapeUtils {

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     * @param adjustCorner {@code true} to automatically adjust the corner radius.
     */
    public static Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly, boolean adjustCorner) {
        float cornerRadiusPixel = DynamicUnitUtils.convertDpToPixels(
                adjustCorner ? Math.max(0, cornerRadius - 1f) : cornerRadius);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        MaterialShapeDrawable materialShapeDrawable;

        if (!topOnly) {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(cornerRadiusPixel).build();
        } else {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(cornerRadiusPixel)
                    .setTopRightCornerSize(cornerRadiusPixel).build();
        }

        materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setTint(color);

        return materialShapeDrawable;
    }

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawable(cornerRadius, color, topOnly, topOnly);
    }

    /**
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param width The width in dip for the drawable.
     * @param height The height in dip for the drawable.
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawableLegacy(int width, int height,
            float cornerRadius, @ColorInt int color, boolean topOnly) {
        float adjustedCornerRadius = cornerRadius;

        if (!topOnly) {
            return DynamicDrawableUtils.getCornerDrawable(
                    width, height, adjustedCornerRadius, color);
        } else {
            adjustedCornerRadius = Math.max(0, cornerRadius - 1f);
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
     * Returns a corner drawable which can be used for the theme preview header.
     *
     * @param cornerRadius The corner size in dip for the drawable.
     * @param color The color for the drawable.
     * @param topOnly {@code true} to round the top corners only.
     */
    public static Drawable getCornerDrawableLegacy(float cornerRadius,
            @ColorInt int color, boolean topOnly) {
        return getCornerDrawableLegacy(0, 0, cornerRadius, color, topOnly);
    }
}