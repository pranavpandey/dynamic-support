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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An interface to get the value callbacks.
 */
public interface DynamicValueListener<T> {

    /**
     * This method will be called when a value is selected.
     *
     * @param tag The optional tag to differentiate between various callbacks.
     * @param value The selected value.
     * @param unit The optional unit for the selected value.
     */
    void onValueSelected(@Nullable String tag, @NonNull T value, @Nullable String unit);
}
