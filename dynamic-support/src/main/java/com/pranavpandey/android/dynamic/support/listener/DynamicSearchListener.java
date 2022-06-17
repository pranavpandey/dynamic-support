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

import android.text.TextWatcher;

import androidx.annotation.Nullable;

/**
 * Interface to listen search view expand and collapse callbacks.
 */
public interface DynamicSearchListener {

    /**
     * This method will be called when the search view is expanded.
     */
    void onSearchViewExpanded();

    /**
     * This method will be called when the search view is collapsed.
     */
    void onSearchViewCollapsed();

    /**
     * This method will be called to get the text watcher for the search view.
     *
     * @return The text watcher for the search view.
     */
    @Nullable TextWatcher getTextWatcher();
}
