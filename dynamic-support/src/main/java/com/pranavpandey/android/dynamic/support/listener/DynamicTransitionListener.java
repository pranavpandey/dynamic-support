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

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An interface to listen the transition events.
 */
public interface DynamicTransitionListener {

    /**
     * This method will be called to postpone the transition until the returned view is laid.
     *
     * <p>It will be called only on API 21 and above.
     *
     * @return The view to postpone the transition.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable View getPostponeTransitionView();

    /**
     * This method will be called to find the view according to the transition name
     * or resource id.
     *
     * <p>It will be called only on API 21 and above.
     *
     * @param resultCode The transition result code.
     * @param position The position of the shared element.
     * @param name The transition name of the view.
     * @param viewId The id resource to find the view by id.
     *
     * @return The view according to the view id.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable View onFindView(int resultCode, int position,
            @NonNull String name, @IdRes int viewId);
}
