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

package com.pranavpandey.android.dynamic.support.motion;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper class to perform various animations.
 */
public class DynamicMotion {

    /**
     * Constant values for the animation duration.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {

        /**
         * Constant for the no duration.
         */
        long NO = 0L;

        /**
         * Constant for the tiniest duration.
         */
        long TINIEST = 25L;

        /**
         * Constant for the tinier duration.
         */
        long TINIER = 40L;

        /**
         * Constant for the tiny duration.
         */
        long TINY = 60L;

        /**
         * Constant for the shorter duration.
         */
        long SHORTER = 150L;

        /**
         * Constant for the short duration.
         */
        long SHORT = 220L;

        /**
         * Constant for the medium duration.
         */
        long MEDIUM = 400L;

        /**
         * Constant for the long duration.
         */
        long LONG = 500L;

        /**
         * Constant for the longer duration.
         */
        long LONGER = 650L;

        /**
         * Constant for the splash duration.
         */
        long SPLASH = 1000L;

        /**
         * Constant for the color animation duration.
         */
        long COLOR = 8000L;

        /**
         * Constant for the default duration.
         */
        long DEFAULT = MEDIUM;
    }

    /**
     * Constant values for the view properties.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Value {

        /**
         * Minimum value for the {@link Float} property.
         */
        float FLOAT_MIN = 0f;

        /**
         * Half value for the {@link Float} property.
         */
        float FLOAT_HALF = 0.5f;

        /**
         * Maximum value for the {@link Float} property.
         */
        float FLOAT_MAX = 1f;
    }

    /**
     * Singleton instance of {@link DynamicMotion}.
     */
    @SuppressLint("StaticFieldLeak")
    private static DynamicMotion sInstance;

    /**
     * Duration for the animations.
     */
    private long mDuration;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #getInstance()} instead.
     */
    private DynamicMotion() {
        this.mDuration = Duration.DEFAULT;
    }

    /**
     * Retrieves the singleton instance of {@link DynamicMotion}.
     * <p>Must be called before accessing the public methods.
     *
     * @return The singleton instance of {@link DynamicMotion}.
     */
    public static synchronized @NonNull DynamicMotion getInstance() {
        if (sInstance == null) {
            sInstance = new DynamicMotion();
        }

        return sInstance;
    }

    /**
     * Checks whether the motion is enabled.
     *
     * @return {@code true} if the motion is enabled.
     */
    public boolean isMotion() {
        return mDuration > Duration.NO && !DynamicTheme.getInstance().isPowerSaveMode();
    }

    /**
     * Sets the motion, either enable or disable.
     *
     * @param motion {@code true} to enable the motion.
     *
     * @return The {@link DynamicMotion} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMotion setMotion(boolean motion) {
        mDuration = motion ? Duration.DEFAULT : Duration.NO;

        return this;
    }

    /**
     * Get the current motion duration.
     *
     * @return The current motion duration.
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * Sets the motion duration.
     *
     * @param duration The motion duration to be set.
     *        <p>Use {@code 0} or {@link Duration#NO} to disable all the motion.
     *
     * @return The {@link DynamicMotion} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicMotion setDuration(long duration) {
        this.mDuration = duration;

        return this;
    }

    /**
     * Sets the default motion duration.
     *
     * @return The {@link DynamicMotion} object to allow for chaining of calls to set methods.
     *
     * @see #setDuration(long)
     * @see Duration#DEFAULT
     */
    public @NonNull DynamicMotion setDuration() {
        return setDuration(Duration.DEFAULT);
    }

    /**
     * Returns an animator set with view property and alpha animators.
     *
     * @param target The target view to be animated.
     * @param startDelay The start delay for the animation.
     * @param property The view property to be animated.
     * @param from The initial value fo the view property.
     * @param to The final value of the view property.
     * @param alphaFrom The initial value of the view alpha.
     * @param alphaTo The final value of the view alpha.
     *
     * @return The animator set with view property and alpha animators.
     *
     * @see ObjectAnimator
     * @see View#ROTATION
     * @see View#ROTATION_X
     * @see View#ROTATION_Y
     * @see View#SCALE_X
     * @see View#SCALE_Y
     * @see View#TRANSLATION_X
     * @see View#TRANSLATION_Y
     * @see View#TRANSLATION_Z
     * @see View#X
     * @see View#Y
     * @see View#Z
     * @see View#Z
     * @see View#ALPHA
     */
    public @NonNull AnimatorSet floatWithAlpha(@Nullable View target,
            long startDelay, @NonNull Property<View, Float> property, float from,
            float to, @FloatRange(from = 0f, to = 1f) float alphaFrom,
            @FloatRange(from = 0f, to = 1f) float alphaTo) {
        if (!isMotion()) {
            startDelay = Duration.NO;
        }

        AnimatorSet animators = new AnimatorSet();
        if (target != null) {
            animators.playTogether(ObjectAnimator.ofFloat(target, property, from, to),
                    ObjectAnimator.ofFloat(target, View.ALPHA, alphaFrom, alphaTo));
            animators.setInterpolator(new AccelerateDecelerateInterpolator());
            animators.setDuration(mDuration);
            animators.setStartDelay(startDelay);
        }

        return animators;
    }

    /**
     * Returns an animator set with view property and alpha animators.
     *
     * @param target The target view to be animated.
     * @param property The view property to be animated.
     * @param from The initial value fo the view property.
     * @param to The final value of the view property.
     * @param alphaFrom The initial value of the view alpha.
     * @param alphaTo The final value of the view alpha.
     *
     * @return The animator set with view property and alpha animators.
     *
     * @see #floatWithAlpha(View, long, Property, float, float, float, float)
     * @see ObjectAnimator
     * @see View#ROTATION
     * @see View#ROTATION_X
     * @see View#ROTATION_Y
     * @see View#SCALE_X
     * @see View#SCALE_Y
     * @see View#TRANSLATION_X
     * @see View#TRANSLATION_Y
     * @see View#TRANSLATION_Z
     * @see View#X
     * @see View#Y
     * @see View#Z
     * @see View#Z
     * @see View#ALPHA
     */
    public @NonNull AnimatorSet floatWithAlpha(@Nullable View target,
            @NonNull Property<View, Float> property, float from, float to,
            @FloatRange(from = 0f, to = 1f) float alphaFrom,
            @FloatRange(from = 0f, to = 1f) float alphaTo) {
        return floatWithAlpha(target, Duration.NO, property, from, to, alphaFrom, alphaTo);
    }

    /**
     * Returns the transition with the specified duration.
     *
     * @param transition The transition to used.
     * @param duration The duration to be set.
     * @param <T> The type of the transition.
     *
     * @return The transition with the specified duration.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public @Nullable <T> T withDuration(@Nullable T transition, long duration) {
        if (!isMotion()) {
            duration = Duration.NO;
        }

        if (transition != null) {
            if (DynamicSdkUtils.is19() && transition instanceof Transition) {
                ((Transition) transition).setDuration(duration);
            } else if (transition instanceof androidx.transition.Transition) {
                ((androidx.transition.Transition) transition).setDuration(duration);
            } else if (transition instanceof Animation) {
                ((Animation) transition).setDuration(duration);
            } else if (transition instanceof Animator) {
                ((Animator) transition).setDuration(duration);
            }
        }

        return transition;
    }

    /**
     * Returns the transition with the default duration.
     *
     * @param transition The transition to used.
     * @param <T> The type of the transition.
     *
     * @return The transition with the default duration.
     *
     * @see #withDuration(Object, long)
     * @see Duration
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public @Nullable <T> T withDuration(@Nullable T transition) {
        return withDuration(transition, mDuration);
    }

    /**
     * Returns the transition with the specified start delay.
     *
     * @param transition The transition to used.
     * @param startDelay The start delay to be set.
     * @param <T> The type of the transition.
     *
     * @return The transition with the specified start delay.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public @Nullable <T> T withStartDelay(@Nullable T transition, long startDelay) {
        if (!isMotion()) {
            startDelay = Duration.NO;
        }

        if (transition != null) {
            if (DynamicSdkUtils.is19() && transition instanceof Transition) {
                ((Transition) transition).setStartDelay(startDelay);
            } else if (transition instanceof androidx.transition.Transition) {
                ((androidx.transition.Transition) transition).setStartDelay(startDelay);
            } else if (transition instanceof Animation) {
                ((Animation) transition).setStartTime(System.currentTimeMillis() + startDelay);
            } else if (transition instanceof Animator) {
                ((Animator) transition).setStartDelay(startDelay);
            }
        }

        return transition;
    }

    /**
     * Returns the transition with the default start delay.
     *
     * @param transition The transition to used.
     * @param <T> The type of the transition.
     *
     * @return The transition with the {@link Duration#SHORTER} delay.
     *
     * @see #withStartDelay(Object, long)
     * @see Duration#SHORTER
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public @Nullable <T> T withStartDelay(@Nullable T transition) {
        return withStartDelay(transition, Duration.SHORTER);
    }

    /**
     * Returns the transition with the specified interpolator.
     *
     * @param transition The transition to used.
     * @param interpolator The interpolator to be set.
     * @param <T> The type of the transition.
     *
     * @return The transition with the specified interpolator.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public @Nullable <T> T withInterpolator(@Nullable T transition,
            @Nullable Interpolator interpolator) {
        if (transition != null) {
            if (DynamicSdkUtils.is19() && transition instanceof Transition) {
                ((Transition) transition).setInterpolator(interpolator);
            } else if (transition instanceof androidx.transition.Transition) {
                ((androidx.transition.Transition) transition).setInterpolator(interpolator);
            } else if (transition instanceof Animation) {
                ((Animation) transition).setInterpolator(interpolator);
            } else if (transition instanceof Animator) {
                ((Animator) transition).setInterpolator(interpolator);
            }
        }

        return transition;
    }

    /**
     * Use {@link TransitionManager} to begin delayed transition according to the motion duration.
     *
     * @param sceneRoot The scene root to be used.
     * @param transition The transition to be used.
     *
     * @see TransitionManager#beginDelayedTransition(ViewGroup, androidx.transition.Transition)
     */
    public void beginDelayedTransition(@Nullable ViewGroup sceneRoot,
            @Nullable androidx.transition.Transition transition) {
        if (sceneRoot == null) {
            return;
        }

        if (transition == null) {
            transition = new AutoTransition();
        }

        TransitionManager.beginDelayedTransition(sceneRoot, withDuration(transition));
    }

    /**
     * Use {@link TransitionManager} to begin delayed transition according to the motion duration.
     *
     * @param sceneRoot The scene root to be used.
     *
     * @see AutoTransition
     * @see #beginDelayedTransition(ViewGroup, androidx.transition.Transition)
     */
    public void beginDelayedTransition(@Nullable ViewGroup sceneRoot) {
        beginDelayedTransition(sceneRoot, null);
    }
}
