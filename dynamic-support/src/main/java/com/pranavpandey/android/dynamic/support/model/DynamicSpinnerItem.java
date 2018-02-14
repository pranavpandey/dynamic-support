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
import android.support.annotation.Nullable;

/**
 * A model class to store {@link android.widget.Spinner} item information
 * with a icon and a text.
 */
public class DynamicSpinnerItem {

    /**
     * Icon used by this item view.
     */
    private Drawable icon;

    /**
     * Text used by this item view.
     */
    private CharSequence text;

    /**
     * Constructor to initialize an object of this class by supplying
     * an icon and a text.
     */
    public DynamicSpinnerItem(@Nullable Drawable icon, @Nullable CharSequence text) {
        this.icon = icon;
        this.text = text;
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
     * @return {@link DynamicSpinnerItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicSpinnerItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Getter for {@link #text}.
     */
    public @Nullable CharSequence getText() {
        return text;
    }

    /**
     * Setter for {@link #text}.
     *
     * @return {@link DynamicSpinnerItem} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicSpinnerItem setText(@Nullable CharSequence text) {
        this.text = text;

        return this;
    }
}
