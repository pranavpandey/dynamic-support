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

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constant values for the dynamic theme.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { Theme.AUTO, Theme.CUSTOM, Theme.SYSTEM, Theme.DAY, Theme.NIGHT })
public @interface Theme {

    /**
     * Constant for the automatic theme.
     */
    int AUTO = -3;

    /**
     * Constant for the custom theme.
     */
    int CUSTOM = -2;

    /**
     * Constant for the system theme.
     */
    int SYSTEM = 1;

    /**
     * Constant for the day theme.
     */
    int DAY = 2;

    /**
     * Constant for the night theme.
     */
    int NIGHT = 3;

    /**
     * String constant values for the dynamic theme.
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef(value = { ToString.AUTO, ToString.CUSTOM,
            ToString.SYSTEM, ToString.DAY, ToString.NIGHT })
    @interface ToString {

        /**
         * String constant for the automatic theme.
         */
        String AUTO = "-3";

        /**
         * String constant for the custom theme.
         */
        String CUSTOM = "-2";

        /**
         * String constant for the system theme.
         */
        String SYSTEM = "1";

        /**
         * String constant for the day theme.
         */
        String DAY = "2";

        /**
         * String constant for the night theme.
         */
        String NIGHT = "3";
    }

    /**
     * Constant values for the night theme.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { Night.AUTO, Night.CUSTOM, Night.SYSTEM, Night.BATTERY })
    public @interface Night {

        /**
         * Constant for the night theme according to the time.
         */
        int AUTO = Theme.AUTO;

        /**
         * Constant for the custom night theme implementation.
         */
        int CUSTOM = Theme.CUSTOM;

        /**
         * Constant for the night theme according to the system.
         */
        int SYSTEM = Theme.SYSTEM;

        /**
         * Constant for the night theme when battery saver is active.
         */
        int BATTERY = 2;

        @Retention(RetentionPolicy.SOURCE)
        @StringDef(value = { Night.ToString.AUTO, Night.ToString.CUSTOM,
                Night.ToString.SYSTEM, Night.ToString.BATTERY })
        @interface ToString {

            /**
             * String constant for the night theme according to the time.
             */
            String AUTO = Theme.ToString.AUTO;

            /**
             * String constant for the custom night theme implementation.
             */
            String CUSTOM = Theme.ToString.CUSTOM;

            /**
             * String constant for the night theme according to the system.
             */
            String SYSTEM = Theme.ToString.SYSTEM;

            /**
             * String constant for the night theme when battery saver is active.
             */
            String BATTERY = "2";
        }
    }

    /**
     * Constant values for the visibility.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { Visibility.AUTO, Visibility.HIDE, Visibility.SHOW })
    @interface Visibility {

        /**
         * Constant for the automatic visibility.
         */
        int AUTO = Theme.AUTO;

        /**
         * Constant for the always hide visibility.
         */
        int HIDE = 0;

        /**
         * Constant for the always show visibility.
         */
        int SHOW = 1;

        /**
         * String constant values for the visibility.
         */
        @Retention(RetentionPolicy.SOURCE)
        @StringDef(value = { Theme.Visibility.ToString.AUTO,
                Theme.Visibility.ToString.HIDE, Theme.Visibility.ToString.SHOW })
        @interface ToString {

            /**
             * String constant for the automatic visibility.
             */
            String AUTO = Theme.ToString.AUTO;

            /**
             * String constant for the always hide visibility.
             */
            String HIDE = "0";

            /**
             * String constant for the always show visibility.
             */
            String SHOW = "1";
        }
    }

    /**
     * Constant values for the background aware functionality.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { BackgroundAware.AUTO, BackgroundAware.DISABLE, BackgroundAware.ENABLE})
    @interface BackgroundAware {

        /**
         * Constant for the automatic background aware.
         */
        int AUTO = Theme.AUTO;

        /**
         * Constant to disable the background aware.
         */
        int DISABLE = 0;

        /**
         * Constant to enable the background aware.
         */
        int ENABLE = 1;

        /**
         * String constant values for the background aware functionality.
         */
        @Retention(RetentionPolicy.SOURCE)
        @StringDef(value = { Theme.BackgroundAware.ToString.AUTO,
                Theme.BackgroundAware.ToString.DISABLE, Theme.BackgroundAware.ToString.ENABLE})
        @interface ToString {

            /**
             * String constant for the automatic background aware.
             */
            String AUTO = Theme.ToString.AUTO;

            /**
             * String constant to disable the background aware.
             */
            String DISABLE = "0";

            /**
             * String constant to enable the background aware.
             */
            String ENABLE = "1";
        }
    }

    /**
     * Interface to hold the color type constant values according to the
     * {@link com.pranavpandey.android.dynamic.support.R.attr#ads_colorType}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { ColorType.NONE, ColorType.PRIMARY, ColorType.PRIMARY_DARK,
            ColorType.ACCENT, ColorType.ACCENT_DARK, ColorType.TINT_PRIMARY,
            ColorType.TINT_PRIMARY_DARK, ColorType.TINT_ACCENT, ColorType.TINT_ACCENT_DARK,
            ColorType.CUSTOM, ColorType.BACKGROUND, ColorType.TINT_BACKGROUND,
            ColorType.TEXT_PRIMARY, ColorType.TEXT_SECONDARY, ColorType.TEXT_PRIMARY_INVERSE,
            ColorType.TEXT_SECONDARY_INVERSE })
    @interface ColorType {

        /**
         * Constant for the unknown color.
         */
        int UNKNOWN = -2;

        /**
         * Constant for the no color.
         */
        int NONE = 0;

        /**
         * Constant for the primary color.
         */
        int PRIMARY = 1;

        /**
         * Constant for the dark primary color.
         */
        int PRIMARY_DARK = 2;

        /**
         * Constant for the accent color.
         */
        int ACCENT = 3;

        /**
         * Constant for the dark accent color.
         */
        int ACCENT_DARK = 4;

        /**
         * Constant for the tint primary color.
         */
        int TINT_PRIMARY = 5;

        /**
         * Constant for the tint dark primary color.
         */
        int TINT_PRIMARY_DARK = 6;

        /**
         * Constant for the tint accent color.
         */
        int TINT_ACCENT = 7;

        /**
         * Constant for the tint dark accent color.
         */
        int TINT_ACCENT_DARK = 8;

        /**
         * Constant for the custom color.
         */
        int CUSTOM = 9;

        /**
         * Constant for the background color.
         */
        int BACKGROUND = 10;

        /**
         * Constant for the tint background color.
         */
        int TINT_BACKGROUND = 11;

        /**
         * Constant for the text primary color.
         */
        int TEXT_PRIMARY = 12;

        /**
         * Constant for the text secondary color.
         */
        int TEXT_SECONDARY = 13;

        /**
         * Constant for the inverse text primary color.
         */
        int TEXT_PRIMARY_INVERSE = 14;

        /**
         * Constant for the inverse text secondary color.
         */
        int TEXT_SECONDARY_INVERSE = 15;
    }
}