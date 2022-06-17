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

package com.pranavpandey.android.dynamic.support.widget.base;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * Interface to create dynamic widgets acting as a bottom sheet.
 */
public interface DynamicBottomSheetWidget {

    /**
     * Returns the bottom sheet behavior used by this widget.
     *
     * @return The bottom sheet behavior used by this widget.
     */
    @Nullable BottomSheetBehavior<?> getBottomSheetBehavior();
}
