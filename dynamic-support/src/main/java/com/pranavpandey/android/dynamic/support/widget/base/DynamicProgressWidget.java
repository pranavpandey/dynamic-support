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

/**
 * Interface to create dynamic widgets with a progress bar and thumb.
 */
public interface DynamicProgressWidget extends DynamicWidget {

    /**
     * Set progress bar color according to the supplied values.
     */
    void setProgressBarColor();

    /**
     * Set thumb color according to the supplied values.
     */
    void setThumbColor();
}
