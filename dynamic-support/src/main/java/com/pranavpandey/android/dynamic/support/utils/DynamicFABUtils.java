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

package com.pranavpandey.android.dynamic.support.utils;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicExtendedFloatingActionButton;

/**
 * Helper class to perform various {@link FloatingActionButton} operations.
 */
public class DynamicFABUtils {

    /**
     * Same animation that FloatingActionButton.Behavior uses to hide the FAB when the
     * AppBarLayout exits.
     *
     * @param fab The FAB to set hide animation.
     */
    public static void hide(@NonNull FloatingActionButton fab) {
        fab.hide();
    }

    /**
     * Same animation that FloatingActionButton.Behavior uses to hide the extended FAB when the
     * AppBarLayout exits.
     *
     * @param extendedFab The extended FAB to set hide animation.
     */
    public static void hide(@NonNull ExtendedFloatingActionButton extendedFab,
            boolean shrinkBefore) {
        if (shrinkBefore && (extendedFab instanceof DynamicExtendedFloatingActionButton
                && ((DynamicExtendedFloatingActionButton) extendedFab).isFABExtended())) {
            extendedFab.shrink();
            return;
        }

        extendedFab.hide();
    }

    /**
     * Same animation that FloatingActionButton.Behavior uses to show the FAB when the
     * AppBarLayout enters.
     *
     * @param fab The FAB to set show animation.
     */
    public static void show(@NonNull FloatingActionButton fab) {
        fab.show();
    }

    /**
     * Same animation that FloatingActionButton.Behavior uses to show the extended FAB when the
     * AppBarLayout enters.
     *
     * @param extendedFab The FAB to set show animation.
     */
    public static void show(@NonNull ExtendedFloatingActionButton extendedFab,
            boolean extendAfter) {
        extendedFab.show();

        if (extendAfter && (extendedFab instanceof DynamicExtendedFloatingActionButton
                && ((DynamicExtendedFloatingActionButton) extendedFab).isAllowExtended()
                && !((DynamicExtendedFloatingActionButton) extendedFab).isFABExtended())) {
            extendedFab.extend();
        }
    }
}
