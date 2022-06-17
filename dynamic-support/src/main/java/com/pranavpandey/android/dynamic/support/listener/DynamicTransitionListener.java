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

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An interface to listen the transition events.
 */
public interface DynamicTransitionListener {

    /**
     * This method will be called on applying transitions.
     * <p>It is recommended to do any shared element adjustments here like postponing
     * the transition.
     *
     * @param exit {@code true} if the activity is exiting.
     */
    void onApplyTransitions(boolean exit);

    /**
     * Returns the enter transition used by his listener.
     *
     * @return The enter transition used by his listener.
     *
     * @see Window#setEnterTransition(android.transition.Transition)
     * @see androidx.fragment.app.Fragment#setEnterTransition(Object)
     * @see #onAdjustEnterReturnTransition(Object, boolean)
     */
    @Nullable Object getDynamicEnterTransition();

    /**
     * Returns the return transition used by his listener.
     *
     * @return The return transition transition used by his listener.
     *
     * @see Window#setExitTransition(android.transition.Transition)
     * @see androidx.fragment.app.Fragment#setExitTransition(Object)
     * @see #onAdjustEnterReturnTransition(Object, boolean)
     */
    @Nullable Object getDynamicReturnTransition();

    /**
     * Returns the exit transition used by his listener.
     *
     * @return The exit transition used by his listener.
     *
     * @see Window#setExitTransition(android.transition.Transition)
     * @see androidx.fragment.app.Fragment#setEnterTransition(Object)
     * @see #onAdjustExitReenterTransition(Object, boolean)
     */
    @Nullable Object getDynamicExitTransition();

    /**
     * Returns the reenter transition used by his listener.
     *
     * @return The reenter transition used by his listener.
     *
     * @see Window#setReenterTransition(android.transition.Transition)
     * @see androidx.fragment.app.Fragment#setReenterTransition(Object)
     * @see #onAdjustExitReenterTransition(Object, boolean)
     */
    @Nullable Object getDynamicReenterTransition();

    /**
     * This method will be called to optionally to adjust the enter and return transitions
     * before applying.
     *
     * @param transition The transition to be adjusted.
     * @param enter {@code true} if enter transition, otherwise return transition.
     *
     * @return The adjusted enter or return transition before applying.
     *
     * @see #getDynamicEnterTransition()
     * @see #getDynamicReturnTransition()
     */
    @Nullable Object onAdjustEnterReturnTransition(@Nullable Object transition, boolean enter);

    /**
     * This method will be called to optionally to adjust the exit and reenter transitions
     * before applying.
     *
     * @param transition The transition to be adjusted.
     * @param exit {@code true} if exit transition, otherwise reenter transition.
     *
     * @return The adjusted exit or reenter transition before applying.
     *
     * @see #getDynamicExitTransition()
     * @see #getDynamicReenterTransition()
     */
    @Nullable Object onAdjustExitReenterTransition(@Nullable Object transition, boolean exit);

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
