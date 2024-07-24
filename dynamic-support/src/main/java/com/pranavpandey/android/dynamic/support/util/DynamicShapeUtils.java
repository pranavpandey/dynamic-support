/*
 * Copyright 2018-2024 Pranav Pandey
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

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicUnitUtils;

/**
 * Helper class to perform shape operations.
 */
public class DynamicShapeUtils {

    /**
     * Returns the corner size for the slider according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The corner size for the slider according to the supplied corner size.
     */
    public static @DrawableRes int getSlideCornerSize(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return 0;
        } else {
            return DynamicUnitUtils.convertDpToPixels(Theme.Corner.DEFAULT);
        }
    }

    /**
     * Returns the overlay resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The overlay resource according to the supplied corner size.
     */
    public static @DrawableRes int getOverlayRes(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return R.drawable.ads_overlay;
        } else if (cornerSize < Theme.Corner.MIN_OVAL) {
            return R.drawable.ads_overlay_rect;
        } else {
            return R.drawable.ads_overlay_round;
        }
    }

    /**
     * Returns the overlay start resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The overlay start resource according to the supplied corner size.
     */
    public static @DrawableRes int getOverlayStartRes(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return R.drawable.ads_overlay;
        } else if (cornerSize < Theme.Corner.MIN_OVAL) {
            return R.drawable.ads_overlay_rect_start;
        } else {
            return R.drawable.ads_overlay_round_start;
        }
    }

    /**
     * Returns the overlay end resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The overlay end resource according to the supplied corner size.
     */
    public static @DrawableRes int getOverlayEndRes(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return R.drawable.ads_overlay;
        } else if (cornerSize < Theme.Corner.MIN_OVAL) {
            return R.drawable.ads_overlay_rect_end;
        } else {
            return R.drawable.ads_overlay_round_end;
        }
    }

    /**
     * Returns the dim overlay resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The dim overlay resource according to the supplied corner size.
     */
    public static @DrawableRes int getOverlayDimRes(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return R.drawable.ads_overlay_dim;
        } else if (cornerSize < Theme.Corner.MIN_OVAL) {
            return R.drawable.ads_overlay_dim_rect;
        } else {
            return R.drawable.ads_overlay_dim_round;
        }
    }

    /**
     * Returns the tab indicator resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The tab indicator resource according to the supplied corner size.
     */
    public static @DrawableRes int getTabIndicatorRes(float cornerSize) {
        if (cornerSize < Theme.Corner.MIN_ROUND) {
            return R.drawable.ads_tabs_indicator;
        } else if (cornerSize < Theme.Corner.MIN_OVAL) {
            return R.drawable.ads_tabs_indicator_rect;
        } else {
            return R.drawable.ads_tabs_indicator_round;
        }
    }

    /**
     * Returns the list selector resource according to the supplied corner size.
     *
     * @param cornerSize The corner size to be considered in dips.
     *
     * @return The list selector resource according to the supplied corner size.
     */
    public static @DrawableRes int getListSelectorRes(float cornerSize) {
        if (Dynamic.isLegacyVersion()) {
            if (cornerSize < Theme.Corner.MIN_ROUND) {
                return R.drawable.ads_list_selector;
            } else if (cornerSize < Theme.Corner.MIN_OVAL) {
                return R.drawable.ads_list_selector_rect;
            } else {
                return R.drawable.ads_list_selector_round;
            }
        } else {
            if (cornerSize < Theme.Corner.MIN_ROUND) {
                return R.drawable.ads_list_selector_v2;
            } else if (cornerSize < Theme.Corner.MIN_OVAL) {
                return R.drawable.ads_list_selector_rect_v2;
            } else {
                return R.drawable.ads_list_selector_round_v2;
            }
        }
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
    public static @NonNull Drawable getCornerDrawable(float cornerRadius,
            @ColorInt int color, boolean topOnly, boolean adjustCorner) {
        float cornerRadiusPixel = DynamicUnitUtils.convertDpToPixels(adjustCorner
                ? Math.max(0, cornerRadius - Theme.Corner.FACTOR_CORNER) : cornerRadius);
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
        return getCornerDrawableWithStroke(cornerRadius, color, topOnly,
                adjustCorner, Theme.Size.STROKE, DynamicColorUtils.setAlpha(
                        Dynamic.getTintColor(color), Theme.Opacity.STROKE_MIN));
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
                topOnly, Theme.Size.STROKE, strokeColor);
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
}
