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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A model class to hold the dynamic item information which can be used by the
 * {@link com.pranavpandey.android.dynamic.support.view.DynamicItemView}.
 */
public class DynamicItem {

    /**
     * Icon used by this item.
     */
    private Drawable icon;

    /**
     * Title used by this item.
     */
    private CharSequence title;

    /**
     * Subtitle used by this item.
     */
    private CharSequence subtitle;
    
    /**
     * Icon tint color used by this item.
     */
    private @ColorInt int color;

    /**
     * Icon tint color type used by this item.
     */
    private @Theme.ColorType int colorType;

    /**
     * {@code true} to show horizontal divider. 
     * <p>Useful to display in a list view.
     */
    private boolean showDivider;

    /**
     * Click listener used by this item.
     */
    private View.OnClickListener onClickListener;

    /**
     * Constructor to initialize an object of this class.
     * 
     * @param icon The icon for this item.
     * @param title The title for this item.
     * @param subtitle The subtitle for this item.
     * @param color The icon tint color for this item.
     * @param colorType The icon tint color type for this item.
     * @param showDivider {@code true} to show horizontal divider.
     */
    public DynamicItem(@Nullable Drawable icon, @Nullable CharSequence title,
            @Nullable CharSequence subtitle, int color,
            @Theme.ColorType int colorType, boolean showDivider) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.color = color;
        this.colorType = colorType;
        this.showDivider = showDivider;
    }

    /**
     * Get the icon used by this item.
     *
     * @return The icon used by this item.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Set the icon used by this item.
     * 
     * @param icon The icon to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Get the title used by this item.
     *
     * @return The title used by this item.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Set the title used by this item.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Get the subtitle used by this item.
     *
     * @return The subtitle used by this item.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle used by this item.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Get the icon tint color type used by this item.
     *
     * @return The icon tint color type used by this item.
     * 
     * @see Theme.ColorType
     */
    public @Theme.ColorType int getColorType() {
        return colorType;
    }

    /**
     * Set the icon tint color type used by this item.
     *
     * @param colorType The icon tint color type to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     *         
     * @see Theme.ColorType
     */
    public DynamicItem setColorType(@Theme.ColorType int colorType) {
        this.colorType = colorType;

        return this;
    }

    /**
     * Get the icon tint color used by this item.
     *
     * @return The icon tint color used by this item.
     */
    public @ColorInt int getColor() {
        return color;
    }

    /**
     * Set the icon tint color used by this item.
     *
     * @param color The icon tint color to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setColor(@ColorInt int color) {
        this.colorType = Theme.ColorType.CUSTOM;
        this.color = color;

        return this;
    }

    /**
     * Returns whether to show the horizontal divider for this item.
     *
     * @return {@code true} to show horizontal divider. 
     *         <p>Useful to display in a list view.
     */
    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * Set the horizontal divider fro this item.
     * <p>Useful to display in a list view.
     *
     * @param showDivider {@code true} to show horizontal divider.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;

        return this;
    }

    /**
     * Get the on click listener used by this item.
     *
     * @return The on click listener used by this item.
     */
    public @Nullable View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    /**
     * Set the on click listener used by this item.
     *
     * @param onClickListener The on click listener to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public DynamicItem setOnClickListener(@Nullable View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        return this;
    }
}
