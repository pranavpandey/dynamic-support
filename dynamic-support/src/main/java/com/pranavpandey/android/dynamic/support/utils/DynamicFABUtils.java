/*
 * Copyright 2018 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.utils;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

/**
 * Helper class to perform various {@link FloatingActionButton} operations.
 */
public class DynamicFABUtils {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    /**
     * Same animation that FloatingActionButton.Behavior uses to hide the
     * FAB when the AppBarLayout exits.
     *
     * @param fab FAB to set hide animation.
     */
    public static void hide(final FloatingActionButton fab) {
        if (DynamicVersionUtils.isIceCreamSandwich()) {
            ViewCompat.animate(fab).scaleX(0.0F).scaleY(0.0F)
                    .alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) { }

                        @Override
                        public void onAnimationCancel(View view) { }

                        @Override
                        public void onAnimationEnd(View view) {
                            view.setVisibility(View.GONE);
                        }
                    }).start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(
                    fab.getContext(), android.support.design.R.anim.abc_fade_out);
            anim.setInterpolator(INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(final Animation animation) { }
            });
            fab.startAnimation(anim);
        }
    }

    /**
     * Same animation that FloatingActionButton.Behavior uses to show the
     * FAB when the AppBarLayout enters.
     *
     * @param fab FAB to set show animation.
     */
    public static void show(FloatingActionButton fab) {
        fab.setVisibility(View.VISIBLE);
        if (DynamicVersionUtils.isIceCreamSandwich()) {
            ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(
                    fab.getContext(), android.support.design.R.anim.abc_fade_in);
            anim.setDuration(200L);
            anim.setInterpolator(INTERPOLATOR);
            fab.startAnimation(anim);
        }
    }
}
