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

package com.pranavpandey.android.dynamic.support.theme.inflater;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicPopupBackground;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * A {@link Runnable} to apply theme to the {@link android.view.Menu}.
 */
public class MenuInflaterRunnable implements Runnable {

    /**
     * Menu item view used by this runnable.
     */
    private final View mMenu;

    /**
     * Attribute sets for the menu item view.
     */
    private final AttributeSet mAttrs;

    public MenuInflaterRunnable(@Nullable View menu, @Nullable AttributeSet attrs) {
        this.mAttrs = attrs;
        this.mMenu = menu;
    }

    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        if (mMenu == null || mAttrs == null) {
            return;
        }

        try {
            DynamicCardView cardView = new DynamicPopupBackground(mMenu.getContext(), mAttrs);
            @ColorInt int backgroundColor = DynamicColorUtils.removeAlpha(cardView.getColor());
            @ColorInt int tintColor = DynamicTheme.getInstance().get().getTintSurfaceColor();

            Dynamic.setColor(mMenu.findViewById(android.R.id.icon), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.icon), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.submenuarrow), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.group_divider), tintColor);
            Dynamic.setAlpha(mMenu.findViewById(androidx.appcompat.R.id.group_divider),
                    Defaults.ADS_ALPHA_DIVIDER);

            Dynamic.setContrastWithColor(mMenu.findViewById(android.R.id.icon), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(android.R.id.title), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    android.R.id.checkbox), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.icon), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.title), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.shortcut), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.radio), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.checkbox), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.submenuarrow), backgroundColor);
            Dynamic.setContrastWithColor(mMenu.findViewById(
                    androidx.appcompat.R.id.group_divider), backgroundColor);

            if (mMenu.getParent() != null) {
                ViewGroup menu = (ViewGroup) this.mMenu.getParent();
                ViewGroup group = (ViewGroup) menu.getParent();

                Dynamic.tintScrollable(menu, backgroundColor);

                if (group == null) {
                    return;
                }

                if (DynamicSdkUtils.is21()) {
                    if (!(group instanceof CardView)) {
                        if (group.getBackground() != null) {
                            if (group.getBackground() instanceof GradientDrawable) {
                                GradientDrawable background =
                                        (GradientDrawable) group.getBackground();
                                background.setCornerRadius(DynamicTheme
                                        .getInstance().get().getCornerRadius());
                            } else if (group.getBackground() instanceof LayerDrawable) {
                                GradientDrawable background = (GradientDrawable)
                                        ((LayerDrawable) group.getBackground()).getDrawable(0);
                                background.setCornerRadius(DynamicTheme
                                        .getInstance().get().getCornerRadius());
                                ViewCompat.setBackground(group, background);
                            }

                            DynamicDrawableUtils.colorizeDrawable(
                                    group.getBackground(), backgroundColor);
                        }

                        group.removeAllViews();
                        group.addView(cardView);
                        cardView.addView(menu);
                    } else {
                        group.setElevation(0);
                        group.removeAllViews();
                        group.addView(menu);
                    }
                } else {
                    ViewCompat.setBackground(group, cardView.getBackground());
                }
            }

        } catch (Exception ignored) {
        }
    }
}
