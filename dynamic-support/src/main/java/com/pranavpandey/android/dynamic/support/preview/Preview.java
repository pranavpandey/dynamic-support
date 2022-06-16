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

package com.pranavpandey.android.dynamic.support.preview;

import androidx.annotation.Nullable;

/**
 * An abstract class to provide the data preview functionality.
 * 
 * @param <T> The type of the info.
 * @param <D> The type of the data.
 */
public abstract class Preview<T, D> {

    /**
     * Preview key to maintain its state.
     */
    public static final String KEY = "ads_preview";

    /**
     * The info used by this preview.
     */
    T info;

    /**
     * The data used by this preview.
     */
    D data;

    /**
     * The customized data used by this preview.
     */
    D dataCustom;

    /**
     * The title used by this preview.
     */
    String title;

    /**
     * The subtitle used by this preview.
     */
    String subtitle;

    /**
     * Constructor to initialize an object of this class..
     */
    public Preview() {
        this(null, null, null, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     */
    public Preview(@Nullable T info, @Nullable D data) {
        this(info, data, null, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     * @param title The title for this preview.
     * @param subtitle The subtitle for this preview.
     */
    public Preview(@Nullable T info, @Nullable D data,
            @Nullable String title, @Nullable String subtitle) {
        this(info, data, null, title, subtitle);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     * @param dataCustom The customized data for this preview.
     * @param title The title for this preview.
     * @param subtitle The subtitle for this preview.
     */
    public Preview(@Nullable T info, @Nullable D data, @Nullable D dataCustom,
            @Nullable String title, @Nullable String subtitle) {
        this.info = info;
        this.data = data;
        this.dataCustom = dataCustom;
        this.title = title;
        this.subtitle = subtitle;
    }

    /**
     * Get the info used by this preview.
     * 
     * @return The info used by this preview.
     */
    public @Nullable T getInfo() {
        return info;
    }

    /**
     * Set the info used by this preview.
     *
     * @param info The info to be set.
     */
    public void setInfo(@Nullable T info) {
        this.info = info;
    }

    /**
     * Get the data used by this preview.
     *
     * @param resolve {@code true} to resolve the custom data.
     *
     * @return The data used by this preview.
     */
    public @Nullable D getData(boolean resolve) {
        if (resolve && dataCustom != null) {
            return dataCustom;
        }

        return data;
    }

    /**
     * Get the data used by this preview.
     *
     * @return The data used by this preview.
     *
     * @see #getData(boolean)
     */
    public @Nullable D getData() {
        return getData(true);
    }

    /**
     * Set the data used by this preview.
     *
     * @param data The data to be set.
     */
    public void setData(@Nullable D data) {
        this.data = data;
    }

    /**
     * Get the customized data used by this preview.
     *
     * @return The customized data used by this preview.
     */
    public @Nullable D getDataCustom() {
        return dataCustom;
    }

    /**
     * Set the customized data used by this preview.
     *
     * @param dataCustom The customized data to be set.
     */
    public void setDataCustom(@Nullable D dataCustom) {
        this.dataCustom = dataCustom;
    }

    /**
     * Get the title used by this preview.
     *
     * @return The title used by this preview.
     */
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Set the title used by this preview.
     *
     * @param title The title used to be set.
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /**
     * Get the subtitle used by this preview.
     *
     * @return The subtitle used by this preview.
     */
    public @Nullable String getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle used by this preview.
     *
     * @param subtitle The subtitle used to be set.
     */
    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }
}
