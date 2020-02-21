/*
 * Copyright 2019 Pranav Pandey
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
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicWindowResolver;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicSimpleTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.adapter.DynamicTutorialsAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.widget.Dynamic;
import com.pranavpandey.android.dynamic.support.widget.DynamicPageIndicator2;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.support.widget.tooltip.DynamicTooltip;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.ArrayList;

/**
 * Base activity with a view pager to show the supplied tutorials.
 * <p>Extend this activity and supply tutorials or dataSet by using the provided methods.
 */
public abstract class DynamicTutorialActivity<T extends DynamicSimpleTutorial,
        V extends Fragment> extends DynamicSystemActivity {

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
    private DynamicTutorialsAdapter<T, V> mAdapter;

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

        setContentView(R.layout.ads_activity_tutorial);

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);
        mViewPager = findViewById(R.id.ads_tutorial_view_pager);
        mFooter = findViewById(R.id.ads_tutorial_footer);
        mPageIndicator = findViewById(R.id.ads_tutorial_page_indicator);
        mActionPrevious = findViewById(R.id.ads_tutorial_action_previous);
        mActionNext = findViewById(R.id.ads_tutorial_action_next_done);
        mActionCustom = findViewById(R.id.ads_tutorial_action_custom);

        mFooter.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
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

        mArgbEvaluator = new ArgbEvaluator();

        if (DynamicLocaleUtils.isLayoutRtl()) {
            ViewCompat.setLayoutDirection(mViewPager, ViewCompat.LAYOUT_DIRECTION_RTL);
        }

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                    float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                @ColorInt final int color;
                if (position < (mAdapter.getItemCount() - 1)) {
                    color = (Integer) mArgbEvaluator.evaluate(
                            positionOffset, mAdapter.getTutorial(position).getBackgroundColor(),
                            mAdapter.getTutorial(position + 1).getBackgroundColor());
                } else {
                    color = mAdapter.getTutorial(
                            mAdapter.getItemCount() - 1).getBackgroundColor();
                }

                setColor(position, color);
                mAdapter.getTutorial(position).onPageScrolled(
                        position, positionOffset, positionOffsetPixels);
                mAdapter.getTutorial(position).onSetPadding(0, 0, 0, mFooterHeight);
                mAdapter.getTutorial(Math.min(mAdapter.getItemCount() - 1, position + 1))
                        .onSetPadding(0, 0, 0, mFooterHeight);
                mAdapter.getTutorial(position).onBackgroundColorChanged(color);
                mAdapter.getTutorial(Math.min(mAdapter.getItemCount() - 1, position + 1))
                        .onBackgroundColorChanged(color);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (mAdapter != null && mAdapter.getTutorial(position) != null) {
                    mAdapter.getTutorial(position).onPageSelected(position);
                    mAdapter.getTutorial(position).onSetPadding(0, 0, 0, mFooterHeight);
                    setColor(position, mAdapter.getTutorial(position).getBackgroundColor());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (mAdapter != null
                        && mAdapter.getTutorial(mViewPager.getCurrentItem()) != null) {
                    mAdapter.getTutorial(mViewPager.getCurrentItem())
                            .onPageScrollStateChanged(state);
                }
            }
        });

        mActionPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasTutorialPrevious()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
            }
        });

        mActionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasTutorialNext()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                } else {
                    finishActivity();
                }
            }
        });

        setTutorials(false);

        // A hack to retain the status bar color.
        if (savedInstanceState == null) {
            setStatusBarColor(getStatusBarColor());
        } else {
            setStatusBarColor(savedInstanceState.getInt(ADS_STATE_STATUS_BAR_COLOR));
        }
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        setWindowStatusBarColor(getStatusBarColor());
        updateStatusBar();
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return mCoordinatorLayout;
    }

    @Override
    public boolean isApplyEdgeToEdgeInsets() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        setTutorials(false);
    }

    /**
     * Returns the list of {@link DynamicTutorial} to be shown by this activity.
     *
     * @return The list of {@link DynamicTutorial} to be shown by this activity.
     */
    protected @NonNull ArrayList<DynamicTutorial<T, V>> getTutorials() {
        return new ArrayList<>();
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
     * @param page The current page for to be set.
     */
    protected void setTutorials(int page, boolean smoothScroll) {
        if (mViewPager == null) {
            return;
        }

        mAdapter = new DynamicTutorialsAdapter<>(this);
        mAdapter.setTutorials(getTutorials());
        mViewPager.setOffscreenPageLimit(mAdapter.getItemCount());
        mViewPager.setAdapter(mAdapter);
        mPageIndicator.setViewPager(mViewPager);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(page < mAdapter.getItemCount() ? page : 0, smoothScroll);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Set view pager adapter according to the tutorials list.
     */
    protected void setTutorials(boolean smoothScroll) {
        if (mViewPager == null) {
            return;
        }

        setTutorials(mViewPager.getCurrentItem(), smoothScroll);
    }

    /**
     * Update activity background and system UI according to the supplied color.
     *
     * @param color The activity color to be applied.
     */
    private void setColor(int position, @ColorInt int color) {
        @ColorInt int systemUIColor = color;
        @ColorInt int tintColor = DynamicColorUtils.getTintColor(color);

        if (mDynamicWindowResolver != null) {
            systemUIColor = mDynamicWindowResolver.getSystemUIColor(color);
        } else {
            if (DynamicTheme.getInstance().get().getPrimaryColorDark(false) == Theme.AUTO) {
                systemUIColor = DynamicTheme.getInstance().generateDarkColor(systemUIColor);
            } else {
                systemUIColor = DynamicTheme.getInstance().get().getPrimaryColor()
                        != DynamicTheme.getInstance().get().getPrimaryColorDark()
                        ? DynamicTheme.getInstance().get().getPrimaryColorDark()
                        : systemUIColor;
            }
        }

        updateTaskDescription(color);
        setStatusBarColor(systemUIColor);
        setNavigationBarColor(systemUIColor);
        mCoordinatorLayout.setStatusBarBackgroundColor(getStatusBarColor());
        mCoordinatorLayout.setBackgroundColor(color);

        if (mAdapter.getRecyclerView() != null) {
            DynamicScrollUtils.setEdgeEffectColor(mAdapter.getRecyclerView(), tintColor);
        }

        Dynamic.setContrastWithColor(mActionPrevious, color);
        Dynamic.setContrastWithColor(mActionNext, color);
        Dynamic.setContrastWithColor(mActionCustom, color);
        Dynamic.setColor(mActionPrevious, tintColor);
        Dynamic.setColor(mActionNext, tintColor);
        Dynamic.setColor(mActionCustom, tintColor);
        mActionCustom.setTextColor(color);
        mPageIndicator.setSelectedColour(tintColor);
        mPageIndicator.setUnselectedColour(DynamicColorUtils.adjustAlpha(
                tintColor, WidgetDefaults.ADS_ALPHA_UNCHECKED));
        DynamicTintUtils.setViewBackgroundTint(mActionPrevious,
                color, tintColor, true, false);
        DynamicTintUtils.setViewBackgroundTint(mActionNext, color,
                tintColor, true, false);

        if (hasTutorialPrevious()) {
            mActionPrevious.setVisibility(View.VISIBLE);
            mActionPrevious.setContentDescription(getString(R.string.ads_previous));
        } else {
            mActionPrevious.setVisibility(View.INVISIBLE);
            mActionPrevious.setContentDescription(null);
        }

        if (hasTutorialNext()) {
            mActionNext.setImageDrawable(DynamicResourceUtils.getDrawable(
                    this, R.drawable.ads_ic_chevron_right));
            mActionNext.setContentDescription(getString(R.string.ads_next));
        } else {
            mActionNext.setImageDrawable(DynamicResourceUtils.getDrawable(
                    this, R.drawable.ads_ic_check));
            mActionNext.setContentDescription(getString(R.string.ads_finish));
        }

        DynamicTooltip.set(mActionPrevious, tintColor, color,
                mActionPrevious.getContentDescription());
        DynamicTooltip.set(mActionNext, tintColor, color,
                mActionNext.getContentDescription());
    }

    /**
     * Checks whether the view pager can be moved to the previous tutorial or item.
     *
     * @return {@code true} if view pager can be moved to the previous tutorial or item.
     */
    private boolean hasTutorialPrevious() {
        return mViewPager.getCurrentItem() != 0;
    }

    /**
     * Checks whether the view pager can be moved to the next tutorial or item.
     *
     * @return {@code true} if view pager can be moved to the next tutorial or item.
     */
    private boolean hasTutorialNext() {
        return mAdapter != null && mViewPager.getCurrentItem() < mAdapter.getItemCount() - 1;
    }

    /**
     * Set an action for the custom action button.
     *
     * @param text The text for the action button.
     * @param onClickListener The on click listener for the action button.
     */
    protected void setAction(final @Nullable String text,
            final @Nullable View.OnClickListener onClickListener) {
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                if (onClickListener != null) {
                    mActionCustom.setText(text);
                    mActionCustom.setOnClickListener(onClickListener);
                    mActionCustom.setVisibility(View.VISIBLE);
                } else {
                    mActionCustom.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Get the view pager used by this activity.
     *
     * @return The view pager used by this activity.
     */
    protected ViewPager2 getViewPager() {
        return mViewPager;
    }

    /**
     * Get the view pager adapter used by this activity.
     *
     * @return The view pager adapter used by this activity.
     */
    protected DynamicTutorialsAdapter<T, V> getViewPagerAdapter() {
        return mAdapter;
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        super.onDynamicChanged(context, recreate);

        setTutorials(true);
    }
}
