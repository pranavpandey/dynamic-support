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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

/**
 * A model class to store {@link android.widget.Spinner} item information with an icon and a text.
 */
public class DynamicSpinnerItem {

    /**
     * Icon used by this item.
     */
    private Drawable icon;

    /**
     * Text used by this item.
     */
    private CharSequence text;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icon The icon for this item.
     * @param text The text for this item.
     */
    public DynamicSpinnerItem(@Nullable Drawable icon, @Nullable CharSequence text) {
        this.icon = icon;
        this.text = text;
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
     * @return The {@link DynamicSpinnerItem} object to allow for chaining of calls to set methods.
     */
    public DynamicSpinnerItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Get the text used by this item.
     *
     * @return The text used by this item.
     */
    public @Nullable CharSequence getText() {
        return text;
    }

    /**
     * Set the text used by this item.
     *
     * @param text The text to be set.
     *
     * @return The {@link DynamicSpinnerItem} object to allow for chaining of calls to set methods.
     */
    public DynamicSpinnerItem setText(@Nullable CharSequence text) {
        this.text = text;

        return this;
    }
}
