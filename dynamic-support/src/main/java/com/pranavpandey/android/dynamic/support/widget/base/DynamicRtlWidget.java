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
 * Interface to create widgets with support for RTL (right-to-left) layouts.
 * <p>When enabled, widgets will adjust gravity or text alignment automatically to support the
 * RTL (right-to-left) layouts.
 */
public interface DynamicRtlWidget {

    /**
     * Returns whether to enable dynamic RTL support for this widget.
     *
     * @return {@code true} if dynamic RTL support is enabled for this widget.
     */
    boolean isRtlSupport();

    /**
     * Sets the dynamic RTL support for this widget.
     *
     * @param rtlSupport {@code true} to enable the RTL support for this widget.
     */
    void setRtlSupport(boolean rtlSupport);
}
