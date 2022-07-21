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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicCallback;
import com.pranavpandey.android.dynamic.util.loader.DynamicLoader;

/**
 * Helper class to perform resource operations. Context and App compat is used to provide
 * backward compatibility.
 *
 * @see ContextCompat
 * @see AppCompatResources
 */
public class DynamicResourceUtils {

    /**
     * Resource id constant for the no resource.
     */
    public static final int ADS_DEFAULT_RESOURCE_ID = -1;

    /**
     * Resource value constant for the no value.
     */
    public static final int ADS_DEFAULT_RESOURCE_VALUE = 0;

    /**
     * Extract the supplied attribute value resource id from the theme.
     *
     * @param context The context to be used.
     * @param attr The attribute whose value resource id to be extracted.
     *
     * @return The value resource id of the supplied attribute.
     */
    public static int getAttributeResId(@NonNull Context context, @AttrRes int attr) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);

        return outValue.resourceId;
    }

    /**
     * Extract the supplied integer attribute value from the theme.
     *
     * @param context The context to be used.
     * @param theme The theme to get the styled attributes.
     * @param attr The integer attribute whose value should be extracted.
     * @param defaultValue The value to return if the attribute is not defined or not a resource.
     *
     * @return The value of the supplied attribute.
     */
    public static int resolveInteger(@NonNull Context context,
            @StyleRes int theme, @AttrRes int attr, int defaultValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(theme, new int[] { attr });

        try {
            return a.getInteger(0, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } finally {
            a.recycle();
        }
    }

    /**
     * Extract the supplied color attribute value from the theme.
     *
     * @param theme The theme to get the styled attributes.
     * @param context The context to be used.
     * @param attr The color attribute whose value to be extracted.
     * @param defaultValue The value to return if the attribute is not defined or not a resource.
     *
     * @return The value of the supplied attribute.
     */
    public static @ColorInt int resolveColor(@NonNull Context context,
            @StyleRes int theme, @AttrRes int attr, int defaultValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(theme, new int[] { attr });

        try {
            return a.getColor(0, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } finally {
            a.recycle();
        }
    }

    /**
     * Extract the supplied drawable attribute value from the theme.
     *
     * @param theme The theme to get the styled attributes.
     * @param context The context to be used.
     * @param attr The drawable attribute whose value to be extracted.
     *
     * @return The value of the supplied attribute.
     */
    public static @Nullable Drawable resolveDrawable(@NonNull Context context,
            @StyleRes int theme, @AttrRes int attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(theme, new int[] { attr });

        try {
            return a.getDrawable(0);
        } finally {
            a.recycle();
        }
    }

    /**
     * Extract the supplied dimension attribute value from the theme.
     *
     * @param context The context to be used.
     * @param theme The theme to get the styled attributes.
     * @param attr The dimension attribute whose value should be extracted.
     * @param defaultValue The value to return if the attribute is not defined or not a resource.
     *
     * @return The value of the supplied attribute.
     */
    public static float resolveDimension(@NonNull Context context,
            @StyleRes int theme, @AttrRes int attr, float defaultValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(theme, new int[] { attr });

        try {
            return a.getDimension(0, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } finally {
            a.recycle();
        }
    }

    /**
     * Extract the supplied dimension attribute value from the theme.
     * <p>The extracted value will be converted into the integer pixels.
     *
     * @param context The context to be used.
     * @param theme The theme to get the styled attributes.
     * @param attr The dimension attribute whose value to be extracted.
     * @param defaultValue The value to return if the attribute is not defined or not a resource.
     *
     * @return The value of the supplied attribute.
     */
    public static int resolveDimensionPixelOffset(@NonNull Context context,
            @StyleRes int theme, @AttrRes int attr, int defaultValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(theme, new int[] { attr });

        try {
            return a.getDimensionPixelOffset(0, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } finally {
            a.recycle();
        }
    }

    /**
     * Get the drawable from the supplied resource.
     *
     * @param context The context to be used.
     * @param drawableRes The drawable resource to get the drawable.
     *
     * @return The drawable retrieved from the resource.
     */
    public static @Nullable Drawable getDrawable(@Nullable Context context,
            @DrawableRes int drawableRes) {
        if (context == null) {
            return null;
        }

        try {
            return AppCompatResources.getDrawable(context, drawableRes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the bitmap from the vector drawable.
     *
     * @param drawable The instance of vector drawable to be converted into bitmap.
     *
     * @return The bitmap converted from the vector drawable.
     */
    public static @NonNull Bitmap getBitmapFromVectorDrawable(@NonNull Drawable drawable) {
        if (DynamicSdkUtils.is21()) {
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
     * @param drawable The drawable to be converted into bitmap drawable.
     *
     * @return The bitmap drawable converted from the drawable.
     */
    public static @Nullable Bitmap getBitmap(@Nullable Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        return getBitmapFromVectorDrawable(drawable);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references do not change.
     *
     * @param drawable The drawable to be colorized.
     * @param colorFilter The color filter to be applied on the drawable.
     *
     * @return The colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawable(@Nullable Drawable drawable,
            @NonNull ColorFilter colorFilter) {
        if (drawable != null) {
            return DynamicDrawableUtils.colorizeDrawable(drawable, colorFilter);
        }

        return null;
    }

    /**
     * Colorize and return the mutated drawable so that, all other references do not change.
     *
     * @param drawable The drawable to be colorized.
     * @param color The color to colorize the drawable.
     * @param mode The porter duff mode.
     *
     * @return The colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawable(@Nullable Drawable drawable,
            @ColorInt int color, @Nullable PorterDuff.Mode mode) {
        if (drawable != null) {
            return DynamicDrawableUtils.colorizeDrawable(drawable, color, mode);
        }

        return null;
    }

    /**
     * Colorize and return the mutated drawable so that, all other references do not change.
     *
     * @param context The context to retrieve drawable resource.
     * @param drawableRes The drawable resource to be colorized.
     * @param colorFilter The color filter to be applied on the drawable.
     *
     * @return The colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(@NonNull Context context,
            @DrawableRes int drawableRes, @NonNull ColorFilter colorFilter) {
        return colorizeDrawable(getDrawable(context, drawableRes), colorFilter);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references do not change.
     *
     * @param context The context to retrieve drawable resource.
     * @param drawableRes The drawable resource to be colorized.
     * @param color The color to colorize the drawable.
     * @param mode The porter duff mode.
     *
     * @return The colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(@NonNull Context context,
            @DrawableRes int drawableRes, @ColorInt int color, @Nullable PorterDuff.Mode mode) {
        return colorizeDrawable(getDrawable(context, drawableRes), color, mode);
    }

    /**
     * Colorize and return the mutated drawable so that, all other references do not change.
     *
     * @param context The context to retrieve drawable resource.
     * @param drawableId Id of the drawable to be colorized.
     * @param color The color to colorize the drawable.
     *
     * @return The colorized drawable.
     */
    public static @Nullable Drawable colorizeDrawableRes(@NonNull Context context,
            @DrawableRes int drawableId, @ColorInt int color) {
        return colorizeDrawable(getDrawable(context, drawableId), color, PorterDuff.Mode.SRC_IN);
    }

    /**
     * Highlight the query text within a text view. Suitable for notifying user about the
     * searched query found in the adapter. TextView should not be empty. Please set your default
     * text first then, highlight the query text by using this method.
     *
     * @param query The string to be highlighted.
     * @param textView The text view to set the highlighted text.
     * @param color The color of the highlighted text.
     *
     * @see Spannable
     */
    public static void highlightQueryTextColor(@Nullable String query,
            @Nullable TextView textView, @ColorInt int color) {
        if (query == null || TextUtils.isEmpty(query) || textView == null
                || textView.getText() == null || TextUtils.isEmpty(textView.getText())) {
            return;
        }

        DynamicLoader.getInstance().setAsync(new DynamicCallback<TextView,
                CharSequence, CharSequence>(textView) {
            @Override
            public @Nullable CharSequence onPlaceholder(@NonNull TextView view) {
                return view.getText();
            }

            @Override
            public @Nullable CharSequence onResult(@NonNull TextView view) {
                final String text = view.getText().toString().toLowerCase();
                final String textQuery = query.toLowerCase();
                if (TextUtils.isEmpty(query) || TextUtils.isEmpty(text)
                        || !text.contains(textQuery)) {
                    return null;
                }

                final SpannableStringBuilder stringBuilder =
                        new SpannableStringBuilder(view.getText());

                int i = 0;
                while (i < text.length()) {
                    int startPos = text.indexOf(textQuery, i);

                    if (startPos != -1) {
                        i = startPos + query.length();

                        stringBuilder.setSpan(new ForegroundColorSpan(color),
                                startPos, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                                startPos, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }

                    i++;
                }

                return stringBuilder;
            }
        });
    }

    /**
     * Highlight the query text within a text view. Suitable for notifying user about the
     * searched query found in the adapter. TextView should not be empty. Please set your default
     * text first then, highlight the query text by using this method.
     *
     * @param query The string to be highlighted.
     * @param textView The text view to set the highlighted text.
     * @param colorRes The color resource of the highlighted text.
     *
     * @see Spannable
     */
    public static void highlightQueryTextColorRes(@NonNull String query,
            @NonNull TextView textView, @ColorRes int colorRes) {
        highlightQueryTextColor(query, textView,
                ContextCompat.getColor(textView.getContext(), colorRes));
    }

    /**
     * Create a new color state list from the supplied one by changing its normal and tint colors.
     *
     * @param colorStateList The state list drawable to be converted.
     * @param normalColor The normal color to be applied.
     * @param tintColor The color to be applied.
     *
     * @return The new color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateListWithNormal(
            @NonNull ColorStateList colorStateList,
            @ColorInt int normalColor, @ColorInt int tintColor) {

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { android.R.attr.state_enabled },
                new int[] { android.R.attr.state_pressed },
                new int[] { android.R.attr.state_focused },
                new int[] { android.R.attr.state_pressed }
        };

        int[] colors = new int[] {
                tintColor,
                normalColor,
                normalColor,
                normalColor,
                normalColor,
        };

        return new ColorStateList(states, colors);
    }

    /**
     * Create a new color state list from the supplied one by changing its normal and tint colors.
     *
     * @param colorStateList The state list drawable to be converted.
     * @param normalColor The normal color to be applied.
     * @param tintColor The color to be applied.
     * @param contrastWith The contrast color to make sure that it will always be visible on
     *                     this background.
     *
     * @return The new color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateListWithNormal(
            @NonNull ColorStateList colorStateList, @ColorInt int normalColor,
            @ColorInt int tintColor, @ColorInt int contrastWith) {
        return convertColorStateList(colorStateList,
                Dynamic.withContrastRatio(normalColor, contrastWith),
                Dynamic.withContrastRatio(tintColor, contrastWith));
    }

    /**
     * Create a new color state list from the supplied one by changing its tint color.
     *
     * @param colorStateList The state list drawable to be converted.
     * @param color The color to be applied.
     *
     * @return The new color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateList(
            @NonNull ColorStateList colorStateList, @ColorInt int color) {

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
     * Create a new color state list from the supplied one by changing its tint color.
     *
     * @param colorStateList The state list drawable to be converted.
     * @param color The color to be applied.
     * @param contrastWith The contrast color to make sure that it will always be visible on
     *                     this background.
     *
     * @return The new color state list with the applied color.
     */
    public static @NonNull ColorStateList convertColorStateList(
            @NonNull ColorStateList colorStateList,
            @ColorInt int color, @ColorInt int contrastWith) {
        return convertColorStateList(colorStateList,
                Dynamic.withContrastRatio(color, contrastWith));
    }

    /**
     * Create a new color state list from the supplied tint color.
     * <p>Tint color will be applied on all the states.
     *
     * @param color The tint color to be applied.
     *
     * @return The color state list with the applied tint color.
     */
    public static @NonNull ColorStateList getColorStateList(@ColorInt int color) {
        return getColorStateList(color, color, false);
    }

    /**
     * Create a new color state list from the supplied disabled, normal and tint colors.
     * <p>Tint color will be applied on the states like checked, enabled, etc.
     *
     * @param disabled The color for the disabled state.
     * @param normal The color for the normal state.
     * @param pressed The color for the pressed state.
     * @param color The tint color to be applied.
     * @param checkable {@code true} if the view is checkable.
     *
     * @return The color state list with the applied normal and tint colors.
     */
    public static @NonNull ColorStateList getColorStateList(@ColorInt int disabled,
            @ColorInt int normal, @ColorInt int pressed, @ColorInt int color, boolean checkable) {
        if (checkable) {
            return new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled,
                                    android.R.attr.state_focused },
                            new int[] { -android.R.attr.state_enabled,
                                    -android.R.attr.state_activated,
                                    -android.R.attr.state_checked,
                                    -android.R.attr.state_pressed },
                            new int[] { android.R.attr.state_enabled,
                                    -android.R.attr.state_activated,
                                    -android.R.attr.state_checked,
                                    -android.R.attr.state_pressed },
                            new int[] { android.R.attr.state_enabled,
                                    -android.R.attr.state_activated,
                                    -android.R.attr.state_checked,
                                    android.R.attr.state_pressed },
                            new int[] { android.R.attr.state_activated },
                            new int[] { android.R.attr.state_checked },
                            new int[] { }
                    },
                    new int[] { pressed, disabled, normal, pressed, color, color, normal });
        } else {
            return new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled,
                                    android.R.attr.state_focused },
                            new int[] { -android.R.attr.state_enabled,
                                    -android.R.attr.state_pressed },
                            new int[] { android.R.attr.state_enabled,
                                    -android.R.attr.state_pressed },
                            new int[] { android.R.attr.state_pressed },
                            new int[] { }
                    },
                    new int[] { color, disabled, normal, color, normal });
        }
    }

    /**
     * Create a new color state list from the supplied normal and tint colors.
     * <p>Tint color will be applied on the states like checked, enabled, etc.
     *
     * @param disabled The color for the disabled state.
     * @param normal The color for the normal state.
     * @param color The tint color to be applied.
     * @param checkable {@code true} if the view is checkable.
     *
     * @return The color state list with the applied normal and tint colors.
     */
    public static @NonNull ColorStateList getColorStateList(@ColorInt int disabled,
            @ColorInt int normal, @ColorInt int color, boolean checkable) {
        return getColorStateList(disabled, normal, normal, color, checkable);
    }

    /**
     * Create a new color state list from the supplied normal and tint colors.
     * <p>Tint color will be applied on the states like checked, enabled, etc.
     *
     * @param normal The normal color to be applied.
     * @param color The tint color to be applied.
     * @param checkable {@code true} if the view is checkable.
     *
     * @return The color state list with the applied normal and tint colors.
     */
    public static @NonNull ColorStateList getColorStateList(@ColorInt int normal,
            @ColorInt int color, boolean checkable) {
        return getColorStateList(normal, normal, color, checkable);
    }

    /**
     * Create a new state list drawable from the supplied disabled, normal and tint colors.
     * <p>Tint color will be applied on the states like checked, enabled, etc.
     *
     * @param disabled The color for the disabled state.
     * @param normal The color for the normal state.
     * @param pressed The color for the pressed state.
     * @param color The tint color to be applied.
     * @param checkable {@code true} if the view is checkable.
     *
     * @return The state list drawable with the applied normal and tint colors.
     */
    public static @NonNull StateListDrawable getStateListDrawable(@ColorInt int disabled,
            @ColorInt int normal, @ColorInt int pressed, @ColorInt int color, boolean checkable) {
        StateListDrawable drawable = new StateListDrawable();

        if (checkable) {
            drawable.addState(
                    new int[] { -android.R.attr.state_enabled,
                            -android.R.attr.state_activated,
                            -android.R.attr.state_checked,
                            -android.R.attr.state_pressed },
                    new ColorDrawable(disabled));
            drawable.addState(
                    new int[] { android.R.attr.state_enabled,
                            -android.R.attr.state_activated,
                            -android.R.attr.state_checked,
                            -android.R.attr.state_pressed },
                    new ColorDrawable(normal));
            drawable.addState(
                    new int[] { android.R.attr.state_enabled,
                            -android.R.attr.state_activated,
                            -android.R.attr.state_checked,
                            android.R.attr.state_pressed },
                    new ColorDrawable(pressed));
            drawable.addState(
                    new int[] { android.R.attr.state_checked,
                            android.R.attr.state_pressed },
                    new ColorDrawable(color));
            drawable.addState(
                    new int[] { android.R.attr.state_checked,
                            android.R.attr.state_pressed },
                    new ColorDrawable(color));
            drawable.addState(new int[] { },
                    new ColorDrawable(normal));
        } else {
            drawable.addState(
                    new int[] { -android.R.attr.state_enabled,
                            -android.R.attr.state_pressed },
                    new ColorDrawable(disabled));
            drawable.addState(
                    new int[] { android.R.attr.state_enabled,
                            -android.R.attr.state_pressed },
                    new ColorDrawable(normal));
            drawable.addState(
                    new int[] { android.R.attr.state_pressed },
                    new ColorDrawable(color));
            drawable.addState(
                    new int[] { },
                    new ColorDrawable(normal));
        }

        return drawable;
    }

    /**
     * Create a new state list drawable from the supplied disabled, normal and tint colors.
     * <p>Tint color will be applied on the states like checked, enabled, etc.
     *
     * @param normal The color for the normal state.
     * @param pressed The color for the pressed state.
     * @param color The tint color to be applied.
     * @param checkable {@code true} if the view is checkable.
     *
     * @return The state list drawable with the applied normal and tint colors.
     */
    public static @NonNull StateListDrawable getStateListDrawable(@ColorInt int normal,
            @ColorInt int pressed, @ColorInt int color, boolean checkable) {
      return getStateListDrawable(normal, normal, pressed, color, checkable);
    }

    /**
     * Get the value resource id of a given attribute.
     *
     * @param context The context to be used.
     * @param attrRes The resource id of the attribute.
     *
     * @return The value resource id of the supplied attribute.
     */
    public static int getResourceId(@NonNull Context context, @AttrRes int attrRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[] { attrRes });

        try {
            return a.getResourceId(0, ADS_DEFAULT_RESOURCE_VALUE);
        } finally {
            a.recycle();
        }
    }

    /**
     * Get the value resource id of a given attribute.
     *
     * @param context The context to be used.
     * @param attrs The supplied attribute set to load the values.
     * @param attrRes The resource id of the attribute.
     *
     * @return The value resource id of the supplied attribute.
     */
    public static int getResourceIdFromAttributes(@NonNull Context context,
            @NonNull AttributeSet attrs, @AttrRes int attrRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, new int[] { attrRes });

        try {
            return a.getResourceId(0, ADS_DEFAULT_RESOURCE_VALUE);
        } finally {
            a.recycle();
        }
    }

    /**
     * Get drawable resource array from its resource id.
     *
     * @param context The context to be used.
     * @param arrayRes The resource id of the drawable array.
     *
     * @return The drawable resource array from its resource id.
     */
    public static @Nullable int[] convertToDrawableResArray(
            @NonNull Context context, @ArrayRes int arrayRes) {
        int[] resources = null;

        if (arrayRes != ADS_DEFAULT_RESOURCE_ID) {
            TypedArray drawableArray = context.getResources().obtainTypedArray(arrayRes);
            resources = new int[drawableArray.length()];

            for (int i = 0; i < drawableArray.length(); i++) {
                try {
                    resources[i] = drawableArray.getResourceId(i, ADS_DEFAULT_RESOURCE_VALUE);
                } catch (Exception e) {
                    resources[i] = ADS_DEFAULT_RESOURCE_VALUE;
                }
            }

            drawableArray.recycle();
        }

        return resources;
    }

    /**
     * Get drawable array from ts resource id.
     *
     * @param context The context to be used.
     * @param arrayRes The resource id of the drawable array.
     *
     * @return The drawable array from its resource id.
     */
    public static @Nullable Drawable[] convertToDrawableArray(
            @NonNull Context context, @ArrayRes int arrayRes) {
        Drawable[] drawables = null;

        if (arrayRes != ADS_DEFAULT_RESOURCE_ID) {
            TypedArray drawableArray = context.getResources().obtainTypedArray(arrayRes);
            drawables = new Drawable[drawableArray.length()];

            for (int i = 0; i < drawableArray.length(); i++) {
                try {
                    drawables[i] = getDrawable(context,
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
     * Get color array from its resource id.
     *
     * @param context The context to be used.
     * @param arrayRes The resource id of the color array.
     *
     * @return The color array from its resource id.
     */
    public static @Nullable Integer[] convertToColorArray(
            @NonNull Context context, @ArrayRes int arrayRes) {
        Integer[] colors = null;

        if (arrayRes != ADS_DEFAULT_RESOURCE_ID) {
            TypedArray colorArray = context.getResources().obtainTypedArray(arrayRes);
            colors = new Integer[colorArray.length()];

            for (int i = 0; i < colorArray.length(); i++) {
                try {
                    if (colorArray.getInteger(i, Theme.AUTO) != Theme.AUTO) {
                        colors[i] = colorArray.getColor(i, ADS_DEFAULT_RESOURCE_VALUE);
                    } else {
                        colors[i] = Theme.AUTO;
                    }
                } catch (Exception e) {
                    colors[i] = ADS_DEFAULT_RESOURCE_VALUE;
                }
            }

            colorArray.recycle();
        }

        return colors;
    }
}
