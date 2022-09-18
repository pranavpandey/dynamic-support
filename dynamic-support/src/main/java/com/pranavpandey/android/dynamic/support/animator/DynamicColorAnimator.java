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

package com.pranavpandey.android.dynamic.support.animator;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;

/**
 * A {@link DynamicTask} to animate color and its tint at regular interval.
 */
public class DynamicColorAnimator extends DynamicTask<Void, int[], Void> {

    /**
     * The raw color used by this animator.
     */
    private final @ColorInt int mColorRaw;

    /**
     * The raw tint color used by this animator.
     */
    private final @ColorInt int mTintRaw;

    /**
     * The color used by this animator.
     */
    private @ColorInt int mColor;

    /**
     * The tint color used by this animator.
     */
    private @ColorInt int mTint;

    /**
     * The temporary color used by this animator.
     */
    private @ColorInt int mColorTemp;

    /**
     * The temporary tint color used by this animator.
     */
    private @ColorInt int mTintTemp;

    /**
     * The animation duration used by this animator.
     */
    private final long mDuration;

    /**
     * The interval duration used by this animator.
     */
    private final long mInterval;

    /**
     * Value animator used by this task.
     */
    private ValueAnimator mValueAnimator;

    /**
     * Argb evaluator to animate color values.
     */
    private final ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    /**
     * Constructor to initialize an object of this class.
     *
     * @param color The color to be animated.
     * @param tint The tint color to be animated.
     * @param duration The animation duration for the animation.
     * @param interval The interval duration for the repeat animation.
     */
    public DynamicColorAnimator(@ColorInt int color, @ColorInt int tint,
            long duration, long interval) {
        this.mColorRaw = color;
        this.mColor = color;
        this.mTintRaw = tint;
        this.mTint = tint;
        this.mDuration = duration;
        this.mInterval = interval;
    }

    /**
     * Initialize colors before the animation.
     */
    public void initialize() {
        mColorTemp = mColor;
        mTintTemp = mTint;

        if (!isCancelled()) {
            onComputeColors(mColor, mTint);
        } else {
            mColor = mColorRaw;
            mTint = mColorRaw;
        }
    }

    /**
     * This method will be called on computing the new colors.
     *
     * @param color The base color to be animated.
     * @param tint The base tint color to be animated.
     *
     * @see #set(int, int)
     */
    protected void onComputeColors(@ColorInt int color, @ColorInt int tint) {
        set(DynamicColorUtils.replaceColor(color, DynamicColorUtils.getRandomColor(color)),
                DynamicColorUtils.replaceColor(tint, DynamicColorUtils.getRandomColor(tint)));
    }

    /**
     * Set initial colors for this animator.
     *
     * @param color The color to be animated.
     * @param tint The tint color to be animated.
     */
    public void set(@ColorInt int color, @ColorInt int tint) {
        this.mColor = color;
        this.mTint = tint;
    }

    /**
     * Start the value animator.
     */
    public void start() {
        if (mValueAnimator != null) {
            mValueAnimator.addUpdateListener(mAnimationUpdateListener);
            mValueAnimator.addListener(mAnimationListener);
            mValueAnimator.start();
        }
    }

    /**
     * Stop the value animator.
     */
    public void stop() {
        if (mValueAnimator != null) {
            mValueAnimator.removeUpdateListener(mAnimationUpdateListener);
            mValueAnimator.removeListener(mAnimationListener);
            mValueAnimator.cancel();
        }

        publishProgress(new DynamicResult.Progress<>(new int[] { mColorRaw, mTintRaw }));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        stop();

        mValueAnimator = ValueAnimator.ofFloat(DynamicMotion.Value.FLOAT_MIN,
                DynamicMotion.Value.FLOAT_MAX);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setStartDelay(mInterval);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);

        start();
    }

    @Override
    protected @Nullable Void doInBackground(@Nullable Void params) {
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        stop();
    }

    /**
     * The animation update listener to evaluate colors at particular intervals.
     */
    private final ValueAnimator.AnimatorUpdateListener mAnimationUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
            if (!isCancelled()) {
                publishProgress(new DynamicResult.Progress<>(new int[] {
                        (Integer) mArgbEvaluator.evaluate(
                                (float) valueAnimator.getAnimatedValue(), mColorTemp, mColor),
                        (Integer) mArgbEvaluator.evaluate(
                                (float) valueAnimator.getAnimatedValue(), mTintTemp, mTint)
                }));
            }
        }
    };

    /**
     * The animation listener to update colors at particular intervals.
     */
    private final Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animator) {
            initialize();
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animator) {
            stop();
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animator) {
            stop();
        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animator) {
            initialize();
        }
    };
}
