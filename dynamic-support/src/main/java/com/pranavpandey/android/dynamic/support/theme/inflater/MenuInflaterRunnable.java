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

package com.pranavpandey.android.dynamic.support.theme.inflater;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
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
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

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

    /**
     * Constructor to initialize an object of this class.
     *
     * @param menu The menu item view for this runnable.
     * @param attrs The attribute set to be used.
     */
    public MenuInflaterRunnable(@Nullable View menu, @Nullable AttributeSet attrs) {
        this.mMenu = menu;
        this.mAttrs = attrs;
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
            @ColorInt int backgroundColor = cardView.getColor();
            @ColorInt int tintColor = DynamicTheme.getInstance().get().getTintSurfaceColor();

            Dynamic.setColor(mMenu.findViewById(android.R.id.icon), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.icon), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.submenuarrow), tintColor);
            Dynamic.setColor(mMenu.findViewById(androidx.appcompat.R.id.group_divider), tintColor);
            Dynamic.setBackgroundColor(mMenu.findViewById(androidx.appcompat.R.id.group_divider),
                    tintColor);
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

            ViewGroup menu = (ViewGroup) this.mMenu.getParent();
            ViewGroup parent = (ViewGroup) menu.getParent();

            Dynamic.tintScrollable(menu, backgroundColor);

            if (parent != null) {
                if (!(parent instanceof CardView)) {
                    ViewCompat.setBackground(menu, null);
                    ViewCompat.setBackground(parent, cardView.getBackground());

                    final CardView menuParent = new DynamicPopupBackground(
                            mMenu.getContext(), mAttrs);
                    menuParent.setCardElevation(Theme.Elevation.DISABLE);

                    if (DynamicSdkUtils.is21()) {
                        menuParent.setCardBackgroundColor(Color.TRANSPARENT);
                    } else {
                        menuParent.setRadius(Theme.Corner.MIN);
                        ViewCompat.setBackground(menuParent, null);
                    }

                    parent.removeAllViews();
                    parent.addView(menuParent);
                    menuParent.addView(menu);
                }
            } else {
                ViewCompat.setBackground(menu, cardView.getBackground());
            }
        } catch (Exception ignored) {
        }
    }
}
