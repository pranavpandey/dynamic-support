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

package com.pranavpandey.android.dynamic.support.tutorial.activity;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicSnackbar;
import com.pranavpandey.android.dynamic.support.listener.DynamicTransitionListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicWindowResolver;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.tutorial.Tutorial;
import com.pranavpandey.android.dynamic.support.tutorial.adapter.DynamicTutorialsAdapter;
import com.pranavpandey.android.dynamic.support.util.DynamicHintUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicPageIndicator2;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base activity with a view pager to show the supplied tutorials.
 * <p>Extend this activity and supply tutorials or dataSet by using the provided methods.
 */
public abstract class DynamicTutorialActivity<V extends Fragment, T extends Tutorial<T, V>>
        extends DynamicSystemActivity implements DynamicTransitionListener, DynamicSnackbar {

    /**
     * Coordinator layout used by this activity.
     */
    private CoordinatorLayout mCoordinatorLayout;

    /**
     * View pager used by this activity.
     */
    private ViewPager2 mViewPager;

    /**
     * View pager adapter used by this activity.
     */
    private DynamicTutorialsAdapter<V, T> mAdapter;

    /**
     * View group to show the tutorial actions.
     */
    private ViewGroup mFooter;

    /**
     * View pager indicator used by this activity.
     */
    private DynamicPageIndicator2 mPageIndicator;

    /**
     * Previous action button used by this activity.
     */
    private ImageButton mActionPrevious;

    /**
     * Next and done action button used by this activity.
     */
    private ImageButton mActionNext;

    /**
     * Custom action button used by this activity.
     */
    private Button mActionCustom;

    /**
     * Argb evaluator to manage color transition.
     */
    private ArgbEvaluator mArgbEvaluator;

    /**
     * Resolver to resolve the status and navigation bar color.
     */
    private DynamicWindowResolver mDynamicWindowResolver;

    /**
     * Height of the footer in pixels.
     */
    private int mFooterHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);
        mViewPager = findViewById(R.id.ads_tutorial_view_pager);
        mFooter = findViewById(R.id.ads_tutorial_footer);
        mPageIndicator = findViewById(R.id.ads_tutorial_page_indicator);
        mActionPrevious = findViewById(R.id.ads_tutorial_action_previous);
        mActionNext = findViewById(R.id.ads_tutorial_action_next_done);
        mActionCustom = findViewById(R.id.ads_tutorial_action_custom);

        mArgbEvaluator = new ArgbEvaluator();

        if (DynamicViewUtils.isLayoutRtl(getContentView())) {
            ViewCompat.setLayoutDirection(mViewPager, ViewCompat.LAYOUT_DIRECTION_RTL);
        }

        mFooter.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (DynamicSdkUtils.is16()) {
                            mFooter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mFooter.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        mFooterHeight = mFooter.getHeight();

                        setTutorials(false);
                    }
                });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                    float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                final @ColorInt int color;
                final @ColorInt int tintColor;
                if (position < (getTutorialsCount() - 1)) {
                    color = (Integer) mArgbEvaluator.evaluate(positionOffset,
                            Dynamic.getColor(getTutorial(position), getBackgroundColor()),
                            Dynamic.getColor(getTutorial(position + 1),
                                    getBackgroundColor()));
                    tintColor = (Integer) mArgbEvaluator.evaluate(positionOffset,
                            Dynamic.getTintColor(getTutorial(position), getTintColor()),
                            Dynamic.getTintColor(getTutorial(position + 1),
                                    getTintColor()));
                } else {
                    color = Dynamic.getColor(getTutorial(
                            getTutorialsCount() - 1), getBackgroundColor());
                    tintColor = Dynamic.getTintColor(getTutorial(
                            getTutorialsCount() - 1), getTintColor());
                }

                onSetColor(position, color, tintColor);
                onSetTooltip(position, color, tintColor);

                Dynamic.onPageScrolled(getTutorial(position), position,
                        positionOffset, positionOffsetPixels);
                Dynamic.onSetPadding(getTutorial(position), 0, 0, 0, mFooterHeight);
                Dynamic.onSetPadding(getTutorial(Math.min(getTutorialsCount() - 1, position + 1)),
                        0, 0, 0, mFooterHeight);
                Dynamic.onColorChanged(getTutorial(position), color, tintColor);
                Dynamic.onColorChanged(getTutorial(Math.min(
                        getTutorialsCount() - 1, position + 1)), color, tintColor);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                setFooter(true);

                onSetColor(position, Dynamic.getColor(
                        getTutorial(position), getBackgroundColor()),
                        Dynamic.getTintColor(getTutorial(position), getTintColor()));
                onSetTooltip(position, Dynamic.getColor(
                        getTutorial(position), getBackgroundColor()),
                        Dynamic.getTintColor(getTutorial(position), getTintColor()));

                Dynamic.onPageSelected(getTutorial(position), position);
                Dynamic.onSetPadding(getTutorial(position),
                        0, 0, 0, mFooterHeight);
                Dynamic.onColorChanged(getTutorial(position),
                        Dynamic.getColor(getTutorial(position), getBackgroundColor()),
                        Dynamic.getTintColor(getTutorial(position), getTintColor()));

                onUpdate(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    onSetColor(getCurrentPosition(), Dynamic.getColor(getTutorial(
                            getCurrentPosition()), getBackgroundColor()), Dynamic.getTintColor(
                                    getTutorial(getCurrentPosition()), getTintColor()));
                    onSetTooltip(getCurrentPosition(), Dynamic.getColor(getTutorial(
                            getCurrentPosition()), getBackgroundColor()), Dynamic.getTintColor(
                            getTutorial(getCurrentPosition()), getTintColor()));

                    Dynamic.onColorChanged(getTutorial(getCurrentPosition()),
                            Dynamic.getColor(getTutorial(getCurrentPosition()),
                                    getBackgroundColor()), Dynamic.getTintColor(getTutorial(
                                            getCurrentPosition()), getTintColor()));
                }

                Dynamic.onPageScrollStateChanged(getTutorial(getCurrentPosition()), state);
            }
        });

        mActionPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorialPrevious(v);
            }
        });

        mActionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorialNext(v);
            }
        });

        setTutorials(getDefaultPosition(), false);

        // A hack to retain the status bar color.
        if (getSavedInstanceState() == null) {
            setStatusBarColor(getStatusBarColor());
        } else {
            setStatusBarColor(getSavedInstanceState().getInt(ADS_STATE_STATUS_BAR_COLOR));
        }
    }

    @Override
    protected void onManageSharedElementTransition() {
        super.onManageSharedElementTransition();

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames,
                    List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames,
                        sharedElements, sharedElementSnapshots);

                if (sharedElementNames == null || sharedElementNames.isEmpty()) {
                    return;
                }

                if (DynamicMotion.getInstance().isMotion()
                        && isTutorialSharedElementTransition()
                        && getSavedInstanceState() == null) {
                    Dynamic.setVisibility(mFooter, View.INVISIBLE);
                }
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                    List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames,
                        sharedElements, sharedElementSnapshots);

                setFooter(true);
            }
        });
    }

    @Override
    protected void onAdjustElevation() {
        super.onAdjustElevation();

        Dynamic.setVisibility(findViewById(R.id.ads_bottom_bar_shadow),
                !DynamicTheme.getInstance().get().isElevation() ? View.GONE : View.VISIBLE);
    }

    /**
     * Returns the layout resource for this activity.
     *
     * @return The layout resource for this activity.
     */
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_activity_tutorial;
    }

    @Override
    public @Nullable View getContentView() {
        return getCoordinatorLayout() != null ? getCoordinatorLayout().getRootView()
                : getWindow().getDecorView();
    }

    @Override
    public @Nullable View getPostponeTransitionView() {
        return null;
    }

    @Override
    public @ColorInt int getBackgroundColor() {
        if (DynamicTheme.getInstance().get().isBackgroundAware()
                && DynamicColorUtils.isColorDark(
                        DynamicTheme.getInstance().get().getBackgroundColor())
                != DynamicColorUtils.isColorDark(
                        DynamicTheme.getInstance().get().getPrimaryColor())) {
            return DynamicTheme.getInstance().get().getTintPrimaryColor();
        }

        return DynamicTheme.getInstance().get().getPrimaryColor();
    }

    /**
     * Returns the tint color used by this activity.
     *
     * @return The tint color used by this activity.
     */
    public @ColorInt int getTintColor() {
        if (DynamicTheme.getInstance().get().isBackgroundAware()
                && DynamicColorUtils.isColorDark(
                        DynamicTheme.getInstance().get().getBackgroundColor())
                == DynamicColorUtils.isColorDark(
                        DynamicTheme.getInstance().get().getTintPrimaryColor())) {
            return DynamicTheme.getInstance().get().getPrimaryColor();
        }

        return DynamicTheme.getInstance().get().getTintPrimaryColor();
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        setWindowStatusBarColor(getStatusBarColor());
        updateStatusBar();
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return getCoordinatorLayout();
    }

    @Override
    public boolean isApplyEdgeToEdgeInsets() {
        return false;
    }

    @Override
    public @Nullable CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isPaused() && !isLaunchedFromHistory()) {
            // A hack to properly run shared element transition.
            setTutorials(false);
        } else if (mViewPager != null) {
            // Fix lag on resuming the activity.
            mViewPager.postDelayed(mTutorialsRunnable, DynamicMotion.Duration.TINIER);
        }

        onUpdate(getCurrentPosition());
    }

    /**
     * Runnable to set the tutorials.
     */
    private final Runnable mTutorialsRunnable = new Runnable() {
        @Override
        public void run() {
            setTutorials(false);
        }
    };

    /**
     * Runnable to update the tutorials adapter.
     */
    private final Runnable mAdapterRunnable = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * Returns the list of {@link Tutorial} to be shown by this activity.
     *
     * @return The list of {@link Tutorial} to be shown by this activity.
     */
    protected @NonNull List<Tutorial<T, V>> getTutorials() {
        return new ArrayList<>();
    }

    /**
     * Get the view pager adapter used by this activity.
     *
     * @return The view pager adapter used by this activity.
     */
    public @Nullable DynamicTutorialsAdapter<V, T> getViewPagerAdapter() {
        return mAdapter;
    }

    /**
     * Returns the no. of tutorials handled by this activity.
     *
     * @return The no. of tutorials handled by this activity.
     */
    public int getTutorialsCount() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    /**
     * Returns the default position for the tutorials.
     *
     * @return The default position for the tutorials.
     */
    protected int getDefaultPosition() {
        return 0;
    }

    /**
     * Returns the {@link Tutorial} for the supplied position.
     *
     * @param position The position to get the tutorial.
     *
     * @return The {@link Tutorial} for the supplied position.
     */
    public @Nullable Tutorial<T, V> getTutorial(int position) {
        if (getTutorialsCount() <= 0) {
            return null;
        }

        return mAdapter.getTutorial(position);
    }

    /**
     * Returns the listener to resolve the status and navigation bar color.
     *
     * @return The listener to resolve the status and navigation bar color.
     */
    public @Nullable DynamicWindowResolver getDynamicWindowResolver() {
        return mDynamicWindowResolver;
    }

    /**
     * Set resolver to resolve the status and navigation bar.
     *
     * @param dynamicWindowResolver The resolver to be set
     */
    public void setDynamicWindowResolver(@Nullable DynamicWindowResolver dynamicWindowResolver) {
        this.mDynamicWindowResolver = dynamicWindowResolver;

        setTutorials(true);
    }

    /**
     * Set view pager adapter according to the tutorials list.
     *
     * @param page The current page to be set.
     * @param smoothScroll {@code true} to smoothly scroll the page.
     * @param reload {@code true} to reload the tutorials state.
     */
    protected void setTutorials(int page, boolean smoothScroll, boolean reload) {
        final List<Tutorial<T, V>> tutorials;
        if (mViewPager == null || (tutorials = getTutorials()).isEmpty()) {
            return;
        }

        if (reload || mAdapter == null) {
            mAdapter = new DynamicTutorialsAdapter<>(this);
        }

        mAdapter.setTutorials(tutorials);
        mViewPager.setOffscreenPageLimit(getTutorialsCount());
        mViewPager.setAdapter(mAdapter);
        mPageIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(page < getTutorialsCount() ? page : 0, smoothScroll);

        // A hack to update the tutorials properly on lower API levels.
        mViewPager.post(mAdapterRunnable);
    }

    /**
     * Set view pager adapter according to the tutorials list.
     *
     * @param page The current page to be set.
     * @param smoothScroll {@code true} to smoothly scroll the page.
     *
     * @see #setTutorials(int, boolean, boolean)
     */
    protected void setTutorials(int page, boolean smoothScroll) {
        setTutorials(page, smoothScroll, true);
    }

    /**
     * Set view pager adapter according to the tutorials list.
     *
     * @param smoothScroll {@code true} to smoothly scroll the page.
     */
    protected void setTutorials(boolean smoothScroll) {
        if (mViewPager == null) {
            return;
        }

        setTutorials(getCurrentPosition(), smoothScroll);
    }

    /**
     * Set the current position for the tutorial list.
     *
     * @param page The current page to be set.
     * @param smoothScroll {@code true} to smoothly scroll the page.
     */
    protected void setTutorial(int page, boolean smoothScroll) {
        if (mViewPager == null || getTutorialsCount() <= 0) {
            return;
        }

        if (page < getTutorialsCount()) {
            mViewPager.setCurrentItem(page, smoothScroll);
        }
    }

    /**
     * Set the current position for the tutorial list.
     *
     * @param page The current page to be set.
     */
    protected void setTutorial(int page) {
        setTutorial(page, DynamicMotion.getInstance().isMotion());
    }

    /**
     * Update activity background and system UI according to the supplied color.
     *
     * @param position The current position of the tutorial.
     * @param color The background color to be applied.
     * @param tintColor The tint color to be applied.
     */
    protected void onSetColor(int position, @ColorInt int color, @ColorInt int tintColor) {
        @ColorInt int systemUIColor = color;

        if (mDynamicWindowResolver != null) {
            systemUIColor = mDynamicWindowResolver.getSystemUIColor(color);
        } else {
            if (DynamicTheme.getInstance().get().getPrimaryColorDark(false) == Theme.AUTO) {
                systemUIColor = DynamicTheme.getInstance().generateSystemColor(systemUIColor);
            } else {
                systemUIColor = DynamicTheme.getInstance().get().getPrimaryColor()
                        != DynamicTheme.getInstance().get().getPrimaryColorDark()
                        ? DynamicTheme.getInstance().get().getPrimaryColorDark()
                        : systemUIColor;
            }
        }

        setWindowBackground(color);
        setStatusBarColor(systemUIColor);
        setNavigationBarColor(systemUIColor);
        updateTaskDescription(color);

        onAdjustElevation();

        Dynamic.setContrastWithColor(findViewById(R.id.ads_bottom_bar_shadow), color);
        Dynamic.setContrastWithColor(findViewById(R.id.ads_tutorial_backdrop), color);
        Dynamic.tint(mActionPrevious, tintColor, color);
        Dynamic.tint(mActionNext, tintColor, color);
        Dynamic.tint(mActionCustom, tintColor, color);

        mAdapter.setContrastWithColor(color);
        mPageIndicator.setSelectedColour(DynamicTheme.getInstance().get().isBackgroundAware()
                ? Dynamic.withContrastRatio(tintColor, color) : tintColor);
        mPageIndicator.setUnselectedColour(DynamicColorUtils.adjustAlpha(
                mPageIndicator.getSelectedColour(), Defaults.ADS_ALPHA_UNCHECKED));

        if (hasTutorialPrevious()) {
            Dynamic.setVisibility(mActionPrevious, View.VISIBLE);
            Dynamic.setContentDescription(mActionPrevious, getString(R.string.ads_previous));
        } else {
            Dynamic.setVisibility(mActionPrevious, View.INVISIBLE);
            Dynamic.setContentDescription(mActionPrevious, null);
        }

        if (hasTutorialNext()) {
            Dynamic.set(mActionNext, DynamicResourceUtils.getDrawable(
                    this, R.drawable.ads_ic_chevron_right));
            Dynamic.setContentDescription(mActionNext, getString(R.string.ads_next));
        } else {
            Dynamic.set(mActionNext, DynamicResourceUtils.getDrawable(
                    this, R.drawable.ads_ic_check));
            Dynamic.setContentDescription(mActionNext, getString(R.string.ads_finish));
        }
    }

    /**
     * This method will be called to set the tooltip for the tutorial actions.
     *
     * @param position The current position of the tutorial.
     * @param color The background color to be applied.
     * @param tintColor The tint color to be applied.
     */
    protected void onSetTooltip(int position, @ColorInt int color, @ColorInt int tintColor) {
        Dynamic.setTooltip(getActionPrevious(), color, tintColor);
        Dynamic.setTooltip(getActionNext(), color, tintColor);
    }

    /**
     * This method will be called to update the activity according to the tutorial position.
     *
     * @param position The current tutorial position.
     */
    protected void onUpdate(int position) { }

    /**
     * Set the tutorial footer and make it visible.
     *
     * @param animate {@code true} to animate the footer.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setFooter(boolean animate) {
        if (mFooter == null) {
            return;
        }

        if (animate && mFooter.getVisibility() != View.VISIBLE) {
            postFooter();
        } else {
            Dynamic.setVisibility(mFooter, View.VISIBLE);
        }
    }

    /**
     * Checks whether the shared element transition is enable for the current tutorial.
     *
     * @return {@code true} if the shared element transition is enable for the current tutorial.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean isTutorialSharedElementTransition() {
        return DynamicSdkUtils.is21() && isSharedElement(getDefaultPosition());
    }

    /**
     * Runnable to animate the footer.
     * <p>Animation fix for lower API levels.
     */
    private final Runnable mFooterRunnable = new Runnable() {
        @Override
        public void run() {
            if (mFooter == null) {
                return;
            }

            if (mFooter.getVisibility() == View.VISIBLE) {
                // Fix random invisible footer on emulators.
                Dynamic.setAlpha(mFooter, DynamicMotion.Value.FLOAT_MAX);
                return;
            }

            Dynamic.setVisibility(mFooter, View.VISIBLE);

            if (mFooterHeight > 0) {
                Dynamic.setAlpha(mFooter, DynamicMotion.Value.FLOAT_MIN);
                DynamicMotion.getInstance().floatWithAlpha(mFooter, DynamicMotion.Duration.TINIER,
                        View.TRANSLATION_Y, mFooterHeight, DynamicMotion.Value.FLOAT_MIN,
                        DynamicMotion.Value.FLOAT_MIN, DynamicMotion.Value.FLOAT_MAX).start();
            }
        }
    };

    /**
     * Post footer animation on main thread.
     */
    public void postFooter() {
        DynamicTheme.getInstance().getHandler().post(mFooterRunnable);
    }

    /**
     * Returns the current position of the view pager.
     *
     * @return The current position of the view pager.
     */
    public int getCurrentPosition() {
        return mViewPager == null || mAdapter == null
                ? Defaults.ADS_NO_POSITION : mViewPager.getCurrentItem();
    }

    /**
     * Checks whether the view pager can be moved to the previous tutorial or item.
     *
     * @return {@code true} if view pager can be moved to the previous tutorial or item.
     */
    public boolean hasTutorialPrevious() {
        return getCurrentPosition() != Defaults.ADS_NO_POSITION && getCurrentPosition() != 0;
    }

    /**
     * Checks whether the view pager can be moved to the next tutorial or item.
     *
     * @return {@code true} if view pager can be moved to the next tutorial or item.
     */
    public boolean hasTutorialNext() {
        return getCurrentPosition() != Defaults.ADS_NO_POSITION
                && getCurrentPosition() < getTutorialsCount() - 1;
    }

    /**
     * This method will be called to go to the previous tutorial.
     *
     * @param view The optional view triggering the action.
     */
    public void onTutorialPrevious(@Nullable View view) {
        if (hasTutorialPrevious()) {
            setTutorial(getCurrentPosition() - 1);
        }
    }

    /**
     * This method will be called to go to the next tutorial.
     *
     * @param view The optional view triggering the action.
     */
    public void onTutorialNext(@Nullable View view) {
        if (hasTutorialNext()) {
            setTutorial(getCurrentPosition() + 1);
        } else {
            finishActivity();
        }
    }

    /**
     * Set an action for the custom action button.
     *
     * @param text The text for the action button.
     * @param onClickListener The on click listener for the action button.
     */
    public void setAction(final @Nullable CharSequence text,
            final @Nullable View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            mActionCustom.setText(text);
            mActionCustom.setOnClickListener(onClickListener);
            Dynamic.setVisibility(mActionCustom, View.VISIBLE);
        } else {
            Dynamic.setVisibility(mActionCustom, View.GONE);
        }
    }

    /**
     * Get the view pager used by this activity.
     *
     * @return The view pager used by this activity.
     */
    public @NonNull ViewPager2 getViewPager() {
        return mViewPager;
    }

    /**
     * Get the previous action button used by this activity.
     *
     * @return The previous action button used by this activity.
     */
    public @Nullable ImageButton getActionPrevious() {
        return mActionPrevious;
    }

    /**
     * Get the next action button used by this activity.
     *
     * @return The next action button used by this activity.
     */
    public @Nullable ImageButton getActionNext() {
        return mActionNext;
    }

    /**
     * Returns whether the shared element is enabled for the supplied tutorial position.
     *
     * @param position The position to be used.
     *
     * @return The shared element is enabled for the supplied tutorial position.
     */
    public boolean isSharedElement(int position) {
        final Tutorial<T, V> tutorial = getTutorial(position);

        return tutorial instanceof Tutorial.SharedElement
                && ((Tutorial.SharedElement<T, V>) tutorial).isSharedElement();
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        super.onDynamicChanged(context, recreate);

        setTutorials(true);
    }

    @Override
    public void finishActivity() {
        if (isSharedElement(getCurrentPosition())) {
            super.finishActivity();
        } else if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public @Nullable Snackbar getSnackbar(@NonNull CharSequence text,
            @Snackbar.Duration int duration) {
        final Tutorial<T, V> tutorial = getTutorial(getCurrentPosition());
        if (tutorial == null || getCoordinatorLayout() == null) {
            return null;
        }

        return DynamicHintUtils.getSnackbar(getCoordinatorLayout(), text, Dynamic.getTintColor(
                tutorial.getColor()), tutorial.getColor(), duration, false);
    }

    @Override
    public @Nullable Snackbar getSnackbar(@StringRes int stringRes,
            @Snackbar.Duration int duration) {
        return getSnackbar(getString(stringRes), duration);
    }

    @Override
    public @Nullable Snackbar getSnackbar(@NonNull CharSequence text) {
        return getSnackbar(text, Snackbar.LENGTH_SHORT);
    }

    @Override
    public @Nullable Snackbar getSnackbar(@StringRes int stringRes) {
        return getSnackbar(getString(stringRes));
    }

    @Override
    public void onSnackbarShow(@Nullable Snackbar snackbar) {
        if (snackbar == null) {
            return;
        }

        snackbar.show();
    }
}
