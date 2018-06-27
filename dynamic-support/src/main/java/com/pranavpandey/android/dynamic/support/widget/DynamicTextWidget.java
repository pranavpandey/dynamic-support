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

package com.pranavpandey.android.dynamic.support.widget;

import android.support.annotation.ColorInt;

import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Interface to create dynamic widgets with a text view which
 * can be tinted according to the {@link DynamicTheme}.
 */
public interface DynamicTextWidget extends DynamicWidget {

    /**
     * @return The text color type applied to this view.
     *
     * @see DynamicColorType
     */
    @DynamicColorType int getTextColorType();

    /**
     * Set the text color type to this view.
     *
     * @param textColorType Text color type for this view.
     *
     * @see DynamicColorType
     */
    void setTextColorType(@DynamicColorType int textColorType);

    /**
     * @return The value of text color applied to this view.
     */
    @ColorInt int getTextColor();

    /**
     * Set the value of text color for this view.
     *
     * @param textColor Text color for this view.
     */
    void setTextColor(@ColorInt int textColor);

    /**
     * Set text color of this view according to the supplied values.
     * Generally, it should be a tint color so that text will be visible on
     * this view background.
     */
    void setTextColor();
}
