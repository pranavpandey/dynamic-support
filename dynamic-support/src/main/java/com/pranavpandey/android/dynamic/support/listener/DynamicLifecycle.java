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

package com.pranavpandey.android.dynamic.support.listener;

/**
 * An interface to listen the dynamic lifecycle events.
 */
public interface DynamicLifecycle {

    /**
     * This method will be called when the dynamic resume event occurs like resuming a
     * {@link androidx.fragment.app.Fragment} when it is visible to the user inside a
     * {@link androidx.viewpager.widget.ViewPager}.
     */
    void onDynamicResume();

    /**
     * This method will be called when the dynamic pause event occurs like pausing a
     * {@link androidx.fragment.app.Fragment} when it goes off screen inside a
     * {@link androidx.viewpager.widget.ViewPager}.
     */
    void onDynamicPause();
}
