/*
 * Copyright 2018-2021 Pranav Pandey
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
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A collection of useful functions to display hints throughout the app.
 */
public class DynamicHintUtils {

    /**
     * Make a themed snackbar with text and action. Background will be accent color from the
     * theme and it will automatically use its tint color for the text and icon to provide
     * best visibility.
     *
     * @param context The context to use.
     * @param text The text to show. Can be formatted text.
     * @param icon The toast icon to show.
     *
     * @return The toast with the supplied parameters.
     *         <p>Use {@link Toast#show()} to display the toast.
     */
    public static @NonNull Toast getToast(@NonNull Context context,
            @Nullable CharSequence text, @Nullable Drawable icon) {
        return DynamicToast.make(context, text, icon,
                DynamicTheme.getInstance().get().getTintAccentColor(),
                DynamicTheme.getInstance().get().getAccentColor(), Toast.LENGTH_SHORT);
    }

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be of primary color from the theme and it will
     * automatically use its tint color for the text and action to provide best visibility.
     *
     * @param view The view to show the snackbar.
     * @param text The text to show. Can be formatted text.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    @SuppressLint("Range")
    public static @NonNull Snackbar getSnackbar(@NonNull View view, @NonNull CharSequence text) {
        return getSnackbar(view, text, Snackbar.LENGTH_SHORT);
    }

    /**
     * Make a themed snackbar with text and action.
     * <p>The background of the snackbar will be of primary color from the theme and it will
     * automatically use its tint color for the text and action to provide best visibility.
     *
     * @param view The view to show the snackbar.
     * @param text The text to show. Can be formatted text.
     * @param duration The duration of the snackbar.
     *                 <p>Can be {@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    public static @NonNull Snackbar getSnackbar(@NonNull View view,
            @NonNull CharSequence text, @Snackbar.Duration int duration) {
        return getSnackbar(view, text, DynamicTheme.getInstance().get().getPrimaryColor(),
                DynamicTheme.getInstance().get().getTintPrimaryColor(), duration);
    }

    /**
     * Make a themed snackbar with text and action.
     *
     * @param view The view to show the snackbar.
     * @param text The text to show. Can be formatted text.
     * @param backgroundColor The snackbar background color.
     * @param tintColor The snackbar tint color based on the background. It will automatically
     *                  check for the contrast to provide bes visibility.
     * @param duration The duration of the snackbar.
     *                 <p>Can be {@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    public static @NonNull Snackbar getSnackbar(@NonNull View view,
            @NonNull CharSequence text, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Snackbar.Duration int duration) {
        return getSnackbar(view, text, backgroundColor, tintColor, duration, true);
    }

    /**
     * Make a themed snackbar with text and action.
     *
     * @param view The view to show the snackbar.
     * @param text The text to show. Can be formatted text.
     * @param backgroundColor The snackbar background color.
     * @param tintColor The snackbar tint color based on the background. It will automatically
     *                  check for the contrast to provide bes visibility.
     * @param duration The duration of the snackbar.
     *                 <p>Can be {@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     * @param backgroundAware {@code true} to consider background aware for the tint color.
     *
     * @return The snackbar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snackbar.
     */
    public static @NonNull Snackbar getSnackbar(@NonNull View view,
            @NonNull CharSequence text, @ColorInt int backgroundColor,
            @ColorInt int tintColor, @Snackbar.Duration int duration, boolean backgroundAware) {
        backgroundColor = DynamicColorUtils.removeAlpha(backgroundColor);
        tintColor = DynamicColorUtils.removeAlpha(tintColor);

        if (backgroundAware && DynamicTheme.getInstance().get().isBackgroundAware()) {
            backgroundColor = DynamicColorUtils.getContrastColor(backgroundColor,
                    DynamicTheme.getInstance().get().getBackgroundColor());
            tintColor = DynamicColorUtils.getContrastColor(tintColor, backgroundColor);
        }

        Snackbar snackbar = Snackbar.make(view, text, duration);
        DynamicDrawableUtils.setBackground(snackbar.getView(),
                DynamicDrawableUtils.getCornerDrawable(DynamicTheme.getInstance()
                        .get().getCornerSizeDp(), backgroundColor));
        ((TextView) snackbar.getView().findViewById(
                R.id.snackbar_text)).setTextColor(tintColor);
        ((TextView) snackbar.getView().findViewById(
                R.id.snackbar_text)).setMaxLines(Integer.MAX_VALUE);
        snackbar.setActionTextColor(tintColor);

        snackbar.dismiss();
        return snackbar;
    }
}
