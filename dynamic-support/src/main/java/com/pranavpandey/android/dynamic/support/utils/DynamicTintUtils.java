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

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.TintableBackgroundView;
import android.view.View;

import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

/**
 * Helper class to perform various tint operations on views.
 */
public class DynamicTintUtils {

    /**
     * Set a view background tint according to the supplied color.
     *
     * @param view The view to set the background tint.
     * @param color The tint color to be used.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setViewBackgroundTint(@NonNull View view, @ColorInt int color) {
        if (view instanceof FloatingActionButton) {
            ((FloatingActionButton) view).setBackgroundTintList(
                    DynamicResourceUtils.getColorStateListButton(color,
                            DynamicColorUtils.getStateColor(
                                    color, WidgetDefaults.ADS_STATE_PRESSED,
                                    WidgetDefaults.ADS_STATE_PRESSED)));
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintList(
                    DynamicResourceUtils.getColorStateListButton(color,
                            DynamicColorUtils.getStateColor(
                                    color, WidgetDefaults.ADS_STATE_PRESSED,
                                    WidgetDefaults.ADS_STATE_PRESSED)));
        }

        if (DynamicVersionUtils.isLollipop()
                && view.getBackground() instanceof RippleDrawable) {
            DynamicDrawableUtils.colorizeDrawable(view.getBackground(), color);
            RippleDrawable rippleDrawable = (RippleDrawable) view.getBackground();
            rippleDrawable.setColor(ColorStateList.valueOf(
                    DynamicColorUtils.getStateColor(
                            color, WidgetDefaults.ADS_STATE_LIGHT,
                            WidgetDefaults.ADS_STATE_DARK)));
        }
    }
}
