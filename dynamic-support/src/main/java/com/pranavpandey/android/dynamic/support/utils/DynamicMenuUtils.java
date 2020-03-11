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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionMenuView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.pranavpandey.android.dynamic.support.widget.tooltip.DynamicTooltip;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.lang.reflect.Method;

/**
 * Helper class to tint and perform operations on toolbar menu.
 *
 * @see Menu
 * @see android.view.MenuItem
 * @see ActionMenuView
 */
public class DynamicMenuUtils {

    /**
     * Set the menu to show MenuItem icons in the overflow window.
     *
     * @param menu The menu to force icons to show.
     */
    public static void forceMenuIcons(@Nullable Menu menu) {
        if (menu == null) {
            return;
        }

        try {
            Class<?> MenuBuilder = menu.getClass();
            Method setOptionalIconsVisible = MenuBuilder.getDeclaredMethod(
                    "setOptionalIconsVisible", boolean.class);
            if (!setOptionalIconsVisible.isAccessible()) {
                setOptionalIconsVisible.setAccessible(true);
            }
            setOptionalIconsVisible.invoke(menu, true);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set other items color of this view according to the supplied values.
     * <p>Generally, it should be a tint color so that items will be visible on this view
     * background.
     *
     * @param view The view to set its items color.
     * @param color The tint color to be applied.
     * @param background The background color for the tooltip.
     * @param tint {@code true} to tint views according to the supplied parameters.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    public static void setViewItemsTint(@NonNull final View view,
            @ColorInt final int color, @ColorInt final int background, boolean tint) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);

        if (view instanceof ViewGroup){
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                setViewItemsTint(((ViewGroup) view).getChildAt(i), color, background, tint);
            }
        }

        if (tint && view instanceof ImageButton) {
            ((ImageButton) view).getDrawable().setAlpha(255);
            ((ImageButton) view).getDrawable().setColorFilter(colorFilter);
            DynamicTintUtils.setViewBackgroundTint(view, color, true);
        }

        if (view instanceof ImageView) {
            if (tint) {
                ((ImageView) view).getDrawable().setAlpha(255);
                ((ImageView) view).getDrawable().setColorFilter(colorFilter);
                DynamicTintUtils.setViewBackgroundTint(view, color, true);
            }

            if (!TextUtils.isEmpty(view.getContentDescription())) {
                DynamicTooltip.set(view, color, background, view.getContentDescription());
            }
        }

        if (tint && view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setTextColor(color);
        }

        if (tint && view instanceof TextView) {
            ((TextView) view).setTextColor(color);
            DynamicTintUtils.setViewBackgroundTint(view, color, true);
        }

        if (tint && view instanceof EditText) {
            ((EditText) view).setTextColor(color);
        }

        if (view instanceof ActionMenuView) {
            for (int j = 0; j < ((ActionMenuView) view).getChildCount(); j++) {
                final View innerView = ((ActionMenuView) view).getChildAt(j);

                if (tint && innerView instanceof ActionMenuItemView) {
                    final Drawable[] compoundDrawables =
                            ((ActionMenuItemView) innerView).getCompoundDrawables();
                    if (DynamicSdkUtils.is23()) {
                        ((ActionMenuItemView) innerView).setCompoundDrawableTintList
                                (DynamicResourceUtils.getColorStateList(color));
                    } else {
                        for (Drawable compoundDrawable : compoundDrawables) {
                            if (compoundDrawable != null) {
                                DynamicDrawableUtils.colorizeDrawable(compoundDrawable, color);
                            }
                        }
                    }
                }

                if (innerView instanceof MenuView.ItemView) {
                    if (tint) {
                        DynamicTintUtils.setViewBackgroundTint(view, color, true);
                    }

                    DynamicTooltip.set(innerView, color, background,
                            ((MenuView.ItemView) innerView).getItemData().getTitle());
                }
            }
        }

        if (view instanceof BottomNavigationMenuView) {
            for (int j = 0; j < ((BottomNavigationMenuView) view).getChildCount(); j++) {
                final View innerView = ((BottomNavigationMenuView) view).getChildAt(j);

                if (innerView instanceof BottomNavigationItemView) {
                    if (tint) {
                        DynamicTintUtils.setViewBackgroundTint(view, color, true);
                    }

                    DynamicTooltip.set(innerView, color, background,
                            ((MenuView.ItemView) innerView).getItemData().getTitle());
                }
            }
        }
    }

    /**
     * Set other items color of this view according to the supplied values.
     * <p>Generally, it should be a tint color so that items will be visible on this view
     * background.
     *
     * @param view The view to set its items color.
     * @param color The tint color to be applied.
     * @param background The background color for the tooltip.
     */
    public static void setViewItemsTint(@NonNull final View view,
            @ColorInt final int color, @ColorInt final int background) {
        setViewItemsTint(view, color, background, true);
    }
}
