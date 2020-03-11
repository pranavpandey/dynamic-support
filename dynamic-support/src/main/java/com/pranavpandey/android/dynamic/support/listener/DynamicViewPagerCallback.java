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

package com.pranavpandey.android.dynamic.support.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * An interface to return the data for {@link androidx.viewpager2.widget.ViewPager2} using
 * {@link com.pranavpandey.android.dynamic.support.adapter.DynamicFragmentStateAdapter}.
 */
public interface DynamicViewPagerCallback {

    /**
     * This method will be called to return the tab title for the supplied position.
     *
     * @param position The current position of the adapter.
     *
     * @return The tab title for the supplied position.
     */
    @Nullable String getTitle(int position);

    /**
     * This method will be called on creating the fragment for the supplied position.
     *
     * @param position The current position of the adapter.
     *
     * @return The fragment for the supplied position.
     */
    @NonNull Fragment createFragment(int position);

    /**
     * This method will be called to get the item count for the adapter.
     *
     * @return The item count for the adapter.
     */
    int getItemCount();
}