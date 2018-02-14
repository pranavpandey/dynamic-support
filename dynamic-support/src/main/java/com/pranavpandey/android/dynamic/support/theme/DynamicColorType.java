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

package com.pranavpandey.android.dynamic.support.theme;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.ACCENT;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.ACCENT_DARK;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.BACKGROUND;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.CUSTOM;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.NONE;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.PRIMARY;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.PRIMARY_DARK;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.TINT_ACCENT;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.TINT_ACCENT_DARK;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.TINT_BACKGROUND;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.TINT_PRIMARY;
import static com.pranavpandey.android.dynamic.support.theme.DynamicColorType.TINT_PRIMARY_DARK;

/**
 * Interface to hold the color type constant values according to the
 * {@link com.pranavpandey.android.dynamic.support.R.attr#ads_colorType}.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { NONE, PRIMARY, PRIMARY_DARK, ACCENT, ACCENT_DARK, TINT_PRIMARY,
        TINT_PRIMARY_DARK, TINT_ACCENT, TINT_ACCENT_DARK, CUSTOM, BACKGROUND, TINT_BACKGROUND })
public @interface DynamicColorType {

    /**
     * Constant for unknown color.
     */
    int UNKNOWN = -2;

    /**
     * Constant for no color.
     */
    int NONE = 0;

    /**
     * Constant for {@link DynamicTheme#mPrimaryColor}.
     */
    int PRIMARY = 1;

    /**
     * Constant for {@link DynamicTheme#mPrimaryColorDark}.
     */
    int PRIMARY_DARK = 2;

    /**
     * Constant for {@link DynamicTheme#mAccentColor}.
     */
    int ACCENT = 3;

    /**
     * Constant for {@link DynamicTheme#mAccentColorDark}.
     */
    int ACCENT_DARK = 4;

    /**
     * Constant for {@link DynamicTheme#mTintPrimaryColor}.
     */
    int TINT_PRIMARY = 5;

    /**
     * Constant for {@link DynamicTheme#mTintPrimaryColorDark}.
     */
    int TINT_PRIMARY_DARK = 6;

    /**
     * Constant for {@link DynamicTheme#mTintAccentColor}.
     */
    int TINT_ACCENT = 7;

    /**
     * Constant for {@link DynamicTheme#mTintAccentColorDark}.
     */
    int TINT_ACCENT_DARK = 8;

    /**
     * Constant for custom color.
     */
    int CUSTOM = 9;

    /**
     * Constant for {@link DynamicTheme#mBackgroundColor}.
     */
    int BACKGROUND = 10;

    /**
     * Constant for {@link DynamicTheme#mTintBackgroundColor}.
     */
    int TINT_BACKGROUND = 11;
}
