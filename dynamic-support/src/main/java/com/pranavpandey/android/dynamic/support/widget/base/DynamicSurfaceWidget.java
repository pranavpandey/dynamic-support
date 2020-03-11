/*
 * Copyright 2018-2020 Pranav Pandey
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

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Interface to create dynamic widgets with surface options which can be used to adjust their
 * elevation, background, etc. according to the {@link DynamicTheme}.
 */
public interface DynamicSurfaceWidget {

    /**
     * Returns whether to enable elevation on the same background.
     *
     * @return {@code true} to enable elevation on the same background.
     */
    boolean isElevationOnSameBackground();

    /**
     * Set the elevation ons ame background option for this widget.
     *
     * @param elevationOnSameBackground {@code true} to enable elevation on the same background.
     */
    void setElevationOnSameBackground(boolean elevationOnSameBackground);

    /**
     * Set surface for this widget according to the supplied values.
     */
    void setSurface();

    boolean isBackgroundSurface();

    /**
     * Checks whether the stroke is required for the surface card view.
     *
     * @return {@code true} if the stroke is required for this surface card view.
     *
     * @see Theme.ColorType#SURFACE
     */
    boolean isStrokeRequired();
}
