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
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

/**
 * A collection of useful functions to display hints throughout the
 * app by using {@link DynamicToast} and {@link Snackbar}.
 */
public class DynamicHintUtils {

    /**
     * Make a themed toast with text and icon. Background will be primary
     * color from the theme and it will automatically use its tint color for
     * the text and icon to provide best visibility.
     *
     * @param context The context to use. Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @param text The text to show. Can be formatted text.
     * @param icon The toast icon to show.
     *
     * @return Toast with the supplied parameters. Use {@link Toast#show()}
     *         to display the toast.
     */
    public static @NonNull Toast getToast(@NonNull Context context, @Nullable CharSequence text,
                                          @Nullable Drawable icon) {
        return DynamicToast.make(context, text, icon,
                DynamicTheme.getInstance().getTintAccentColor(),
                DynamicTheme.getInstance().getAccentColor(), Toast.LENGTH_SHORT);
    }

    /**
     * Make a themed snack bar with text and action. Background will be primary
     * color from the theme and it will automatically use its tint color for
     * the text and action to provide best visibility.
     *
     * @param view View to show the snack bar.
     * @param text The text to show. Can be formatted text.
     *
     * @return Snack bar with the supplied parameters. Use {@link Snackbar#show()}
     *         to display the snack bar.
     */
    @SuppressLint("Range")
    public static @NonNull Snackbar getSnackBar(@NonNull View view, @NonNull CharSequence text) {
        return getSnackBar(view, text, DynamicTheme.getInstance().getPrimaryColor(),
                DynamicTheme.getInstance().getTintPrimaryColor(), Snackbar.LENGTH_LONG);
    }

    /**
     * Make a themed snack bar with text and action. Background will be primary
     * color from the theme and it will automatically use its tint color for
     * the text and action to provide best visibility.
     *
     * @param view View to show the snack bar.
     * @param text The text to show. Can be formatted text.
     * @param duration How long to display the message. Can be {@link Snackbar#LENGTH_SHORT},
     *                 {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return Snack bar with the supplied parameters. Use {@link Snackbar#show()}
     *         to display the snack bar.
     */
    public static @NonNull Snackbar getSnackBar(@NonNull View view, @NonNull CharSequence text,
                                                @Snackbar.Duration int duration) {
        return getSnackBar(view, text, DynamicTheme.getInstance().getPrimaryColor(),
                DynamicTheme.getInstance().getTintPrimaryColor(), duration);
    }

    /**
     * Make a themed snack bar with text and action.
     *
     * @param view View to show the snack bar.
     * @param text The text to show. Can be formatted text.
     * @param backgroundColor The snack bar background color.
     * @param tintColor The snack bar tint color based on the background. It will
     *                  automatically check for the contrast to provide best
     *                  visibility.
     * @param duration How long to display the message. Can be {@link Snackbar#LENGTH_SHORT},
     *                 {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return Snack bar with the supplied parameters. Use {@link Snackbar#show()}
     *         to display the snack bar.
     */
    public static @NonNull Snackbar getSnackBar(@NonNull View view, @NonNull CharSequence text,
                                                @ColorInt int backgroundColor,
                                                @ColorInt int tintColor,
                                                @Snackbar.Duration int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.getView().setBackgroundColor(backgroundColor);
        ((TextView) snackbar.getView().findViewById(
                android.support.design.R.id.snackbar_text)).setTextColor(tintColor);
        snackbar.setActionTextColor(tintColor);

        return snackbar;
    }
}
