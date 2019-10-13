/*
 * Copyright 2019 Pranav Pandey
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

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckedTextView;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.lang.reflect.Field;

/**
 * Helper class to perform various tint operations on views.
 */
public class DynamicTintUtils {

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
    public static void setViewBackgroundTint(@NonNull View view, @ColorInt int background,
            @ColorInt int color, boolean borderless, boolean checkable) {
        @ColorInt int pressedColor = DynamicColorUtils.shiftColor(color,
                WidgetDefaults.ADS_SHIFT_LIGHT, WidgetDefaults.ADS_SHIFT_DARK);

        if (view instanceof MaterialButton) {
            if (borderless) {
                if (!DynamicSdkUtils.is21()) {
                    pressedColor = DynamicColorUtils.getStateColor(
                            DynamicColorUtils.adjustAlpha(
                                    pressedColor, WidgetDefaults.ADS_STATE_PRESSED),
                            WidgetDefaults.ADS_STATE_LIGHT, WidgetDefaults.ADS_STATE_DARK);

                    ViewCompat.setBackgroundTintList(view,
                            DynamicResourceUtils.getColorStateList(
                                    Color.TRANSPARENT, pressedColor, checkable));
                } else {
                    ((MaterialButton) view).setRippleColor(
                            DynamicResourceUtils.getColorStateList(
                                    Color.TRANSPARENT, pressedColor, checkable));
                }
            } else {
                ViewCompat.setBackgroundTintList(view,
                        DynamicResourceUtils.getColorStateList(
                                DynamicColorUtils.getTintColor(background),
                                color, pressedColor, checkable));
            }
        } else if (view instanceof TintableBackgroundView) {
            if (borderless) {
                ViewCompat.setBackgroundTintList(view,
                        DynamicResourceUtils.getColorStateList(
                                Color.TRANSPARENT, pressedColor, checkable));
            } else {
                ViewCompat.setBackgroundTintList(view,
                        DynamicResourceUtils.getColorStateList(
                                DynamicColorUtils.getTintColor(background),
                                color, pressedColor, checkable));
            }
        } else {
            if (!DynamicSdkUtils.is21()) {
                background = DynamicColorUtils.getStateColor(
                        DynamicColorUtils.adjustAlpha(DynamicColorUtils.getTintColor(background),
                                WidgetDefaults.ADS_STATE_PRESSED),
                        WidgetDefaults.ADS_STATE_LIGHT, WidgetDefaults.ADS_STATE_DARK);
                pressedColor = DynamicColorUtils.getStateColor(
                        DynamicColorUtils.adjustAlpha(color, WidgetDefaults.ADS_STATE_PRESSED),
                        WidgetDefaults.ADS_STATE_LIGHT, WidgetDefaults.ADS_STATE_DARK);

                if (borderless) {
                    DynamicDrawableUtils.setBackground(view,
                            DynamicResourceUtils.getStateListDrawable(Color.TRANSPARENT,
                                    background, pressedColor, checkable));
                } else {
                    // TODO:
                }
            }
        }

        if (DynamicSdkUtils.is21() && view.getBackground() instanceof RippleDrawable) {
            if (borderless) {
                pressedColor = DynamicColorUtils.adjustAlpha(
                        color, WidgetDefaults.ADS_STATE_PRESSED);
            }

            pressedColor = DynamicColorUtils.getStateColor(pressedColor,
                    WidgetDefaults.ADS_STATE_LIGHT, WidgetDefaults.ADS_STATE_DARK);

            RippleDrawable rippleDrawable = (RippleDrawable) view.getBackground();
            if (checkable && !(view instanceof DynamicCheckedTextView)) {
                background = DynamicColorUtils.getStateColor(
                        DynamicColorUtils.adjustAlpha(
                                DynamicColorUtils.getTintColor(background),
                                WidgetDefaults.ADS_STATE_PRESSED),
                        WidgetDefaults.ADS_STATE_LIGHT, WidgetDefaults.ADS_STATE_DARK);

                rippleDrawable.setColor(DynamicResourceUtils.getColorStateList(
                        Color.TRANSPARENT, background, pressedColor, true));
            } else {
                rippleDrawable.setColor(ColorStateList.valueOf(pressedColor));
            }
        }
    }

    /**
     * Set a view background tint according to the supplied color.
     *
     * @param view The view to set the background tint.
     * @param color The tint color to be used.
     */
    public static void setViewBackgroundTint(@NonNull View view,
            @ColorInt int color, boolean borderless) {
        setViewBackgroundTint(view, DynamicTheme.getInstance().get()
                .getTintBackgroundColor(), color, borderless, false);
    }

    /**
     * Try to set the inset foreground color of the view.
     *
     * @param color The scrim color to be set.
     */
    public static void setInsetForegroundColor(@NonNull View navigationView, @ColorInt int color) {
        try {
            final Field insetForeground =
                    ScrimInsetsFrameLayout.class.getDeclaredField("insetForeground");
            insetForeground.setAccessible(true);
            insetForeground.set(navigationView, new ColorDrawable(DynamicColorUtils
                    .adjustAlpha(color, WidgetDefaults.ADS_ALPHA_SCRIM)));
            navigationView.invalidate();
        } catch (Exception ignored) {
        }
    }
}
