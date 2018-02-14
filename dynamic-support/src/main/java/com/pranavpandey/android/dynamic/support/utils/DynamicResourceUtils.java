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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.util.Calendar;

/**
 * Helper class to perform resource operations. Context and App compat
 * is used to provide backward compatibility.
 *
 * @see ContextCompat
 * @see AppCompatResources
 */
public class DynamicResourceUtils {

    /**
     * Resource id constant for no resource.
     */
    public static final int ADS_DEFAULT_RESOURCE_ID = -1;

    /**
     * Resource value constant for no value.
     */
    public static final int ADS_DEFAULT_RESOURCE_VALUE = 0;

    /**
     * Extract the supplied color attribute value from the theme.
     *
     * @param attr Color attribute whose value should be extracted.
     * @param defValue Value to return if the attribute is not defined or
     *                 not a resource.
     *
     * @return Value of the supplied attribute.
     */
    public static @ColorInt int resolveColor(@NonNull Context context,
                                             @AttrRes int attr, int defValue) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);

        return ContextCompat.getColor(context, outValue.resourceId);
    }

    /**
     * Extract the supplied drawable attribute value from the theme.
     *
     * @param attr Drawable attribute whose value should be extracted.
     *
     * @return Value of the supplied attribute.
     */
    public static @Nullable Drawable resolveDrawable(
            @NonNull Context context, @AttrRes int attr) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);

        return ContextCompat.getDrawable(context, outValue.resourceId);
    }

    /**
     * Get the drawable from the supplied id.
     *
     * @param context Context to retrieve resources.
     * @param drawableRes Drawable id to get the drawable.
     *
     * @return Drawable retrieved from the id.
     */
    public static @Nullable Drawable getDrawable(@NonNull Context context,
                                       @DrawableRes int drawableRes) {
        try {
            return AppCompatResources.getDrawable(context, drawableRes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the bitmap from the vector drawable.
     *
     * @param drawable Instance of vector drawable to be converted
     *                 into bitmap.
     *
     * @return Bitmap converted from the vector drawable.
     */
    public static @Nullable Bitmap getBitmapFromVectorDrawable(@Nullable Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (DynamicVersionUtils.isLollipop()) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        } else {
            drawable.mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Get the bitmap drawable from the drawable.
     *
     * @param drawable Drawable to be converted into bitmap drawable.
     *
     * @return Bitmap drawable converted from the drawable.
     */
    public static @Nullable Bitmap getBitmap(@Nullable Drawable drawable) {
        return getBitmapFromVectorDrawable(drawable);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references
     * do not change.
     *
     * @param drawable The drawable to be colorized.
     * @param colorFilter Color filter to be applied on the drawable.
     *
     * @return Colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawable(
            @Nullable Drawable drawable, @NonNull ColorFilter colorFilter) {
        if (drawable != null) {
            return DynamicDrawableUtils.colorizeDrawable(drawable, colorFilter);
        }

        return null;
    }

    /**
     * Colorize and return the mutated drawable so that, all other references
     * do not change.
     *
     * @param context Context to retrieve drawable resource.
     * @param drawableId Id of the drawable to be colorized.
     * @param colorFilter Color filter to be applied on the drawable.
     *
     * @return Colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(
            @NonNull Context context, @DrawableRes int drawableId,
            @NonNull ColorFilter colorFilter) {
        return colorizeDrawable(getDrawable(context, drawableId), colorFilter);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references
     * do not change.
     *
     * @param drawable The drawable to be colorized.
     * @param color Color to colorize the drawable.
     * @param mode PorterDuff mode.
     *
     * @return Colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawable(
            @Nullable Drawable drawable, @ColorInt int color, @Nullable PorterDuff.Mode mode) {
        if (drawable != null) {
            return DynamicDrawableUtils.colorizeDrawable(drawable, color, mode);
        }

        return null;
    }

    /**
     * Colorize and return the mutated drawable so that, all other references
     * do not change.
     *
     * @param context Context to retrieve drawable resource.
     * @param drawableId Id of the drawable to be colorized.
     * @param color Color to colorize the drawable.
     * @param mode PorterDuff mode.
     *
     * @return Colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(
            @NonNull Context context, @DrawableRes int drawableId,
            @ColorInt int color, @Nullable PorterDuff.Mode mode) {
        return colorizeDrawable(getDrawable(context, drawableId), color, mode);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references
     * do not change.
     *
     * @param context Context to retrieve drawable resource.
     * @param drawableId Id of the drawable to be colorized.
     * @param color Color to colorize the drawable.
     *
     * @return Colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(
            @NonNull Context context, @DrawableRes int drawableId, @ColorInt int color) {
        return colorizeDrawable(getDrawable(context, drawableId), color, PorterDuff.Mode.SRC_IN);
    }

    /**
     * Highlight the query text within a TextView. Suitable for notifying
     * user about the searched query found in the adapter. TextView should
     * not be empty. Please set your default text first then, highlight the
     * query text by using this function.
     *
     * @param query String to be highlighted.
     * @param textView to set the highlighted text.
     * @param color of the highlighted text.
     *
     * @see Spannable
     */
    public static void highlightQueryTextColor(
            @NonNull String query, @NonNull TextView textView, @ColorInt int color) {
        final String stringText = textView.getText().toString().toLowerCase();
        if (!TextUtils.isEmpty(query) && stringText.contains(query)) {
            final int startPos = stringText.indexOf(query);
            final int endPos = startPos + query.length();

            final SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
            sb.setSpan(new ForegroundColorSpan(color),
                    startPos, endPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    startPos, endPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            textView.setText(sb, TextView.BufferType.SPANNABLE);
        }
    }

    /**
     * Highlight the query text within a TextView. Suitable for notifying
     * user about the searched query found in the adapter. TextView should
     * not be empty. Please set your default text first then, highlight the
     * query text by using this function.
     *
     * @param query String to be highlighted.
     * @param textView to set the highlighted text.
     * @param colorId Color id of the highlighted text.
     *
     * @see Spannable
     */
    public static void highlightQueryTextColorRes(
            @NonNull String query, @NonNull TextView textView, @ColorRes int colorId) {
        highlightQueryTextColor(query, textView,
                ContextCompat.getColor(textView.getContext(), colorId));
    }

    /**
     * Create a new color state list from the supplied one by
     * changing its tint color.
     * 
     * @param context Context to retrieve resources.
     * @param colorStateList Drawable to be converted.
     * @param color Color to be applied.
     *
     * @return New color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateList(
            @NonNull Context context, @NonNull ColorStateList colorStateList,
            @ColorInt int color) {

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { android.R.attr.state_enabled },
                new int[] { android.R.attr.state_pressed },
                new int[] { android.R.attr.state_focused },
                new int[] { android.R.attr.state_pressed }
        };

        int[] colors = new int[] {
                color,
                colorStateList.getColorForState
                        (new int[] { android.R.attr.state_enabled }, color),
                colorStateList.getColorForState(
                        new int[] { android.R.attr.state_pressed }, color),
                colorStateList.getColorForState
                        (new int[] { android.R.attr.state_focused }, color),
                colorStateList.getColorForState(
                        new int[] { android.R.attr.state_pressed }, color),
        };

        return new ColorStateList(states, colors);
    }

    /**
     * Create a new color state list from the supplied one by
     * changing its tint color.
     *
     * @param context Context to retrieve resources.
     * @param colorStateList Drawable to be converted.
     * @param color Color to be applied.
     * @param contrastWith Contrast color to make sure that it will
     *                     always be visible on this background.
     *
     * @return New color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateList(
            @NonNull Context context, @NonNull ColorStateList colorStateList,
            @ColorInt int color, @ColorInt int contrastWith) {
        return convertColorStateList(context, colorStateList,
                DynamicColorUtils.getContrastColor(color, contrastWith));
    }

    /**
     * Create a new color state list from the supplied tint color.
     * Tint color will be applied on all the states.
     *
     * @param color Tint color to be applied.
     *
     * @return New color state list with the applied tint color.
     */
    public static @NonNull ColorStateList getColorStateList(@ColorInt int color) {
        return new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_enabled },
                        new int[] { android.R.attr.state_enabled }
                },
                new int[] { color, color });
    }

    /**
     * Create a new color state list from the supplied normal and
     * tint colors. Tint color will be applied on the states like
     * checked, enabled, etc.
     *
     * @param normal Normal color to be applied.
     * @param color Tint color to be applied.
     *
     * @return New color state list with the applied normal
     *         and tint colors.
     */
    public static @NonNull ColorStateList getColorStateList(
            @ColorInt int normal, @ColorInt int color) {
        return new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_enabled,
                                -android.R.attr.state_activated,
                                -android.R.attr.state_checked },
                        new int[] { android.R.attr.state_enabled,
                                -android.R.attr.state_activated,
                                -android.R.attr.state_checked
                        },
                        new int[] { android.R.attr.state_activated },
                        new int[] { android.R.attr.state_checked }
                },
                new int[] { normal, normal, color, color });
    }

    /**
     * Create a new color state list buttons from the supplied normal
     * and tint colors. Tint color will be applied on the states like
     * pressed, focused, etc.
     *
     * @param normal Normal color to be applied.
     * @param pressed Pressed color to be applied.
     *
     * @return New color state list with the applied normal
     *         and tint colors.
     */
    public static @NonNull ColorStateList getColorStateListButton(
            @ColorInt int normal, @ColorInt int pressed) {
        return new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_pressed },
                        new int[] { android.R.attr.state_focused },
                        new int[] {}
                },
                new int[] { pressed, pressed, normal });
    }

    /**
     * Create a new color state list from the supplied tint color.
     * Tint color will be applied on all the states.
     *
     * @param color Tint color to be applied.
     *
     * @return New color state list with the applied tint color.
     */
    public static @NonNull ColorStateList getColorStateListWithStates(@ColorInt int color) {
        final @ColorInt int normalColor = DynamicTheme.getInstance().getTintBackgroundColor();

        return new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_enabled },
                        new int[] { android.R.attr.state_enabled,
                                -android.R.attr.state_pressed,
                                -android.R.attr.state_focused
                        },
                        new int[] {}
                },
                new int[] {
                        normalColor,
                        normalColor,
                        color
                });


    }

    /**
     * Get drawable array from the its resource id.
     *
     * @param context Context to retrieve resources.
     * @param arrayRes Resource id of the drawable array.
     *
     * @return Drawable array from its resource id.
     */
    public static @Nullable Drawable[] convertToDrawableArray(
            @NonNull Context context, @ArrayRes int arrayRes) {
        Drawable[] drawables = null;

        if (arrayRes != ADS_DEFAULT_RESOURCE_ID) {
            TypedArray drawableArray = context.getResources().obtainTypedArray(arrayRes);
            drawables = new Drawable[drawableArray.length()];

            for (int i = 0; i < drawableArray.length(); i++) {
                try {
                    drawables[i] = DynamicResourceUtils.getDrawable(context,
                            drawableArray.getResourceId(i, ADS_DEFAULT_RESOURCE_VALUE));
                } catch (Exception e) {
                    drawables[i] = null;
                }
            }

            drawableArray.recycle();
        }

        return drawables;
    }

    /**
     * Get color array from the its resource id.
     *
     * @param context Context to retrieve resources.
     * @param arrayRes Resource id of the color array.
     *
     * @return Color array from its resource id.
     */
    public static @Nullable @ColorInt Integer[] convertToColorArray(
            @NonNull Context context, @ArrayRes int arrayRes) {
        Integer[] colors = null;

        if (arrayRes != ADS_DEFAULT_RESOURCE_ID) {
            TypedArray colorArray = context.getResources().obtainTypedArray(arrayRes);
            colors = new Integer[colorArray.length()];

            for (int i = 0; i < colorArray.length(); i++) {
                try {
                    colors[i] = colorArray.getColor(i, ADS_DEFAULT_RESOURCE_VALUE);
                } catch (Exception e) {
                    colors[i] = ADS_DEFAULT_RESOURCE_VALUE;
                }
            }

            colorArray.recycle();
        }

        return colors;
    }

    /**
     * @return {@code true} if it is night. Useful to apply themes
     * based on the day and night.
     */
    @SuppressLint("WrongConstant")
    public static boolean isNight() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }
}
