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

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * An {@link DynamicBackgroundView} to implement divider.
 */
public class DynamicDivider extends DynamicBackgroundView {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    private @ColorInt int mColor;

    /**
     * Color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedColor;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The background aware functionality to change this view color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #mContrastWithColor
     */
    private @Theme.BackgroundAware int mBackgroundAware;

    public DynamicDivider(@NonNull Context context) {
        super(context, null);
    }

    public DynamicDivider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicDivider(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Returns the filter mode to be used to tint this view.
     *
     * @return The filter mode to be used to tint this view.
     */
    public PorterDuff.Mode getFilterMode() {
        return PorterDuff.Mode.SRC_IN;
    }

    @Override
    public void initialize() {
        super.initialize();

        if (getColorType() == Theme.ColorType.NONE && getColor() == Theme.Color.UNKNOWN) {
            setColorType(Theme.ColorType.TINT_BACKGROUND);
        }
    }
}
