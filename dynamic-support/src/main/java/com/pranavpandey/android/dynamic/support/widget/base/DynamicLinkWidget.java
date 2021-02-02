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

package com.pranavpandey.android.dynamic.support.widget.base;

import androidx.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Interface to create text widgets and tint their link color according to the
 * {@link DynamicTheme}.
 */
public interface DynamicLinkWidget extends DynamicWidget {

    /**
     * Returns the link color type applied to this widget.
     *
     * @return The link color type applied to this widget.
     *
     * @see Theme.ColorType
     */
    @Theme.ColorType int getLinkColorType();

    /**
     * Set the link color type to this widget.
     *
     * @param linkColorType Link color type for this widget.
     *
     * @see Theme.ColorType
     */
    void setLinkColorType(@Theme.ColorType int linkColorType);

    /**
     * Returns the value of link color applied to this widget.
     *
     * @param resolve {@code true} to resolve the link color applied to this widget.
     *
     * @return The value of link color applied to this widget.
     */
    @ColorInt int getLinkColor(boolean resolve);

    /**
     * Returns the value of link color applied to this widget.
     *
     * @return The value of link color applied to this widget.
     */
    @ColorInt int getLinkColor();

    /**
     * Set the value of link color for this widget.
     *
     * @param linkColor Link color for this widget.
     */
    void setLinkColor(@ColorInt int linkColor);

    /**
     * Set link color of this widget according to the supplied values.
     * <p>Generally, it should be a tint color so that link will be visible on this
     * widget background.
     */
    void setLinkColor();
}
