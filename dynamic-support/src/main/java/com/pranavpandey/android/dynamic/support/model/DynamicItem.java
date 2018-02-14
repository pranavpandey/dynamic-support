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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.View;

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;

/**
 * A model class to hold the dynamic item information which can be used
 * by the {@link com.pranavpandey.android.dynamic.support.view.DynamicItemView}.
 */
public class DynamicItem {

    /**
     * Icon used by the item view.
     */
    private Drawable icon;

    /**
     * Title used by the item view.
     */
    private CharSequence title;

    /**
     * Subtitle used by the item view.
     */
    private CharSequence subtitle;
    
    /**
     * Icon tint color used by the item view.
     */
    private @ColorInt int color;

    /**
     * Icon tint color type used by the item view.
     */
    private @DynamicColorType int colorType;

    /**
     * {@code true} to show horizontal divider. Useful to display 
     * in a list view.
     */
    private boolean showDivider;

    private View.OnClickListener onClickListener;

    public DynamicItem(@Nullable Drawable icon, @Nullable CharSequence title,
                       @Nullable CharSequence subtitle, int color,
                       @DynamicColorType int colorType, boolean showDivider) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.color = color;
        this.colorType = colorType;
        this.showDivider = showDivider;
    }

    /**
     * Getter for {@link #icon}.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Setter for {@link #icon}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Getter for {@link #title}.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Setter for {@link #title}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Getter for {@link #subtitle}.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Setter for {@link #subtitle}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Getter for {@link #colorType}.
     */
    public @DynamicColorType int getColorType() {
        return colorType;
    }

    /**
     * Setter for {@link #colorType}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setColorType(@DynamicColorType int color) {
        this.colorType = DynamicColorType.CUSTOM;
        this.color = color;

        return this;
    }

    /**
     * Getter for {@link #color}.
     */
    public @ColorInt int getColor() {
        return color;
    }

    /**
     * Setter for {@link #color}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setColor(@ColorInt int color) {
        this.color = color;

        return this;
    }

    /**
     * Getter for {@link #showDivider}.
     */
    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * Setter for {@link #showDivider}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;

        return this;
    }

    /**
     * Getter for {@link #onClickListener}.
     */
    public @Nullable View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    /**
     * Setter for {@link #onClickListener}.
     *
     * @return {@link DynamicItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicItem setOnClickListener(@Nullable View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        return this;
    }
}
