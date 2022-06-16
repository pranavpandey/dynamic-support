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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckedTextView;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

import java.lang.reflect.Field;

/**
 * Helper class to perform various tint operations on views.
 */
public class DynamicTintUtils {

    /**
     * Colorize a ripple drawable tint according to the supplied view and color.
     *
     * @param view The view to check the background tint.
     * @param drawable The ripple drawable to be colorized.
     * @param background The background color to calculate the default color.
     * @param color The tint color to be used.
     * @param borderless {@code true} if the view is borderless.
     * @param checkable {@code true} if the view is checkable.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void colorizeRippleDrawable(@Nullable View view, @Nullable Drawable drawable,
            @ColorInt int background, @ColorInt int color, boolean borderless, boolean checkable) {
        if (view == null || !DynamicSdkUtils.is21() || !(drawable instanceof RippleDrawable)) {
            return;
        }

        if (Dynamic.isBackgroundAware(view)) {
            color = Dynamic.withContrastRatio(color, background, view);
        }

        @ColorInt int pressedColor;
        if (borderless) {
            pressedColor = DynamicColorUtils.adjustAlpha(color, Defaults.ADS_STATE_PRESSED);
        } else if (view instanceof MaterialButton) {
            pressedColor = DynamicColorUtils.adjustAlpha(Dynamic.getTintColor(
                    color, view), Defaults.ADS_STATE_PRESSED);
        } else {
            pressedColor = color;
        }

        pressedColor = DynamicColorUtils.getStateColor(pressedColor,
                Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK);

        try {
            if (checkable && !(view instanceof DynamicCheckedTextView)) {
                background = DynamicColorUtils.getStateColor(DynamicColorUtils.adjustAlpha(
                        Dynamic.getTintColor(background, view), Defaults.ADS_STATE_PRESSED),
                        Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK);

                ((RippleDrawable) drawable.mutate()).setColor(
                        DynamicResourceUtils.getColorStateList(Color.TRANSPARENT,
                                background, pressedColor, true));
            } else {
                ((RippleDrawable) drawable.mutate()).setColor(
                        ColorStateList.valueOf(pressedColor));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Set a view background tint according to the supplied color.
     *
     * @param view The view to set the background tint.
     * @param background The background color to calculate the default color.
     * @param color The tint color to be used.
     * @param borderless {@code true} if the view is borderless.
     * @param checkable {@code true} if the view is checkable.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setViewBackgroundTint(@Nullable View view, @ColorInt int background,
            @ColorInt int color, boolean borderless, boolean checkable) {
        if (view == null) {
            return;
        }

        if (Dynamic.isBackgroundAware(view)) {
            color = Dynamic.withContrastRatio(color, background, view);
        }

        @ColorInt int pressedColor = DynamicColorUtils.shiftColor(color,
                Defaults.ADS_SHIFT_LIGHT, Defaults.ADS_SHIFT_DARK);

        if (view instanceof MaterialButton) {
            if (borderless) {
                if (DynamicSdkUtils.is21()) {
                    ((MaterialButton) view).setRippleColor(
                            DynamicResourceUtils.getColorStateList(
                                    Color.TRANSPARENT, pressedColor, checkable));
                } else {
                    pressedColor = DynamicColorUtils.getStateColor(
                            DynamicColorUtils.adjustAlpha(
                                    pressedColor, Defaults.ADS_STATE_PRESSED),
                            Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK);

                    ViewCompat.setBackgroundTintList(view,
                            DynamicResourceUtils.getColorStateList(
                                    Color.TRANSPARENT, pressedColor, checkable));
                }
            } else {
                ViewCompat.setBackgroundTintList(view, DynamicResourceUtils.getColorStateList(
                        Dynamic.getTintColor(background, view), color, pressedColor, checkable));
            }
        } else if (view instanceof TintableBackgroundView) {
            if (borderless) {
                ViewCompat.setBackgroundTintList(view,
                        DynamicResourceUtils.getColorStateList(
                                Color.TRANSPARENT, pressedColor, checkable));
            } else {
                ViewCompat.setBackgroundTintList(view, DynamicResourceUtils.getColorStateList(
                        Dynamic.getTintColor(background, view), color, pressedColor, checkable));
            }
        } else if (!DynamicSdkUtils.is21()) {
            background = DynamicColorUtils.getStateColor(DynamicColorUtils.adjustAlpha(
                    Dynamic.getTintColor(background, view), Defaults.ADS_STATE_PRESSED),
                    Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK);
            pressedColor = DynamicColorUtils.getStateColor(
                    DynamicColorUtils.adjustAlpha(color, Defaults.ADS_STATE_PRESSED),
                    Defaults.ADS_STATE_LIGHT, Defaults.ADS_STATE_DARK);

            if (borderless) {
                Dynamic.setBackground(view, DynamicResourceUtils.getStateListDrawable(
                        Color.TRANSPARENT, background, pressedColor, checkable));
            } else {
                Dynamic.setBackground(view, DynamicResourceUtils.colorizeDrawable(
                        view.getBackground(), pressedColor, PorterDuff.Mode.SRC_ATOP));
            }
        }

        // Fix floating action button background tint on API level 21 and above.
        if (view instanceof FloatingActionButton) {
            return;
        }

        if (DynamicSdkUtils.is21() && view.getBackground() instanceof RippleDrawable) {
            colorizeRippleDrawable(view, view.getBackground(), background, color,
                    borderless, checkable);
        }
    }

    /**
     * Set a view background tint according to the supplied color.
     *
     * @param view The view to set the background tint.
     * @param background The background color to consider the background aware.
     * @param borderless {@code true} if the view is borderless.
     */
    public static void setViewBackgroundTint(@Nullable View view,
            @ColorInt int background, boolean borderless) {
        setViewBackgroundTint(view, background, DynamicTheme.getInstance().resolveColorType(
                Defaults.ADS_COLOR_TYPE_SYSTEM_SECONDARY), borderless, false);
    }

    /**
     * Set a view foreground tint according to the supplied color.
     *
     * @param view The view to set the foreground tint.
     * @param background The background color to consider the background aware.
     * @param color The tint color to be used.
     * @param borderless {@code true} if the view is borderless.
     * @param checkable {@code true} if the view is checkable.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setViewForegroundTint(@Nullable View view, @ColorInt int background,
            @ColorInt int color, boolean borderless, boolean checkable) {
        if (view == null) {
            return;
        }

        if (!DynamicSdkUtils.is23()) {
            setViewBackgroundTint(view, background, color, borderless, checkable);
        } else if (view instanceof CardView || view.getForeground() instanceof RippleDrawable) {
            colorizeRippleDrawable(view, view.getForeground(), background,
                    color, borderless, checkable);
        }
    }

    /**
     * Set a view background tint according to the supplied color.
     *
     * @param view The view to set the foreground tint.
     * @param background The background color to consider the background aware.
     * @param borderless {@code true} if the view is borderless.
     */
    public static void setViewForegroundTint(@Nullable View view,
            @ColorInt int background, boolean borderless) {
        setViewForegroundTint(view, background, DynamicTheme.getInstance().get()
                .getTintBackgroundColor(), borderless, false);
    }

    /**
     * Try to set the inset foreground color for the view.
     *
     * @param view The view set the inset foreground color.
     * @param color The scrim color to be set.
     * @param drawBottomInset {@code true} to draw the bottom inset.
     */
    @SuppressLint("RestrictedApi")
    public static void setInsetForegroundColor(@Nullable ScrimInsetsFrameLayout view,
            @ColorInt int color, boolean drawBottomInset) {
        if (view == null) {
            return;
        }

        if (view instanceof NavigationView) {
            ((NavigationView) view).setTopInsetScrimEnabled(true);
            ((NavigationView) view).setBottomInsetScrimEnabled(drawBottomInset);
        } else {
            view.setDrawTopInsetForeground(true);
            view.setDrawBottomInsetForeground(drawBottomInset);
        }

        try {
            final Field insetForeground =
                    ScrimInsetsFrameLayout.class.getDeclaredField("insetForeground");
            insetForeground.setAccessible(true);
            insetForeground.set(view, new ColorDrawable(DynamicColorUtils
                    .adjustAlpha(color, Defaults.ADS_ALPHA_SCRIM)));

            view.invalidate();
        } catch (Exception ignored) {
        }
    }
}
