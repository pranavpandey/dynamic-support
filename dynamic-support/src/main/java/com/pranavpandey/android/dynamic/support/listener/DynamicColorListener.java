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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.Nullable;

/**
 * An interface to get the various color callbacks.
 */
public interface DynamicColorListener {

    /**
     * This method will be called when a color is selected.
     *
     * @param tag The optional tag to differentiate between various callbacks.
     * @param position The selected position.
     * @param color The selected color.
     */
    void onColorSelected(@Nullable String tag, int position, int color);
}