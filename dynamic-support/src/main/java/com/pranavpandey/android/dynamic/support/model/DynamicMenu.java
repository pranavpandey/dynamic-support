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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A model class to store {@link android.view.Menu} menu information with an icon and a text.
 */
public class DynamicMenu {

    /**
     * Icon used by this menu.
     */
    private Drawable icon;

    /**
     * Title used by this menu.
     */
    private CharSequence title;

    /**
     * Subtitle used by this menu.
     */
    private CharSequence subtitle;

    /**
     * {@code true} if this menu has a submenu.
     */
    private boolean hasSubmenu;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icon The icon for this menu.
     * @param title The title for this menu.
     */
    public DynamicMenu(@Nullable Drawable icon, @Nullable CharSequence title) {
        this(icon, title, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icon The icon for this menu.
     * @param title The title for this menu.
     * @param subtitle The subtitle for this menu.
     */
    public DynamicMenu(@Nullable Drawable icon, @Nullable CharSequence title, 
            @Nullable CharSequence subtitle) {
        this(icon, title, subtitle, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icon The icon for this menu.
     * @param title The title for this menu.
     * @param subtitle The subtitle for this menu.
     * @param hasSubmenu {@code true} to enable submenu.
     */
    public DynamicMenu(@Nullable Drawable icon, @Nullable CharSequence title, 
            @Nullable CharSequence subtitle, boolean hasSubmenu) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.hasSubmenu = hasSubmenu;
    }

    /**
     * Get the icon used by this menu.
     *
     * @return The icon used by this menu.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Set the icon used by this menu.
     *
     * @param icon The icon to be set.
     *
     * @return The {@link DynamicMenu} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMenu setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Get the title used by this menu.
     *
     * @return The title used by this menu.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Set the title used by this menu.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicMenu} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMenu setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Get the subtitle used by this menu.
     *
     * @return The subtitle used by this menu.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle used by this menu.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicMenu} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMenu setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Returns whether this menu has a submenu.
     *
     * @return {@code true} if this menu has a submenu.
     */
    public boolean isHasSubmenu() {
        return hasSubmenu;
    }

    /**
     * Sets if this menu has a submenu.
     *
     * @param hasSubmenu {@code true} to enable submenu.
     *
     * @return The {@link DynamicMenu} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMenu setHasSubmenu(boolean hasSubmenu) {
        this.hasSubmenu = hasSubmenu;

        return this;
    }
}
