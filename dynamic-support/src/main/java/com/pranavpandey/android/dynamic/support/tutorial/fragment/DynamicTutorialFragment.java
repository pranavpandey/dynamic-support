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

package com.pranavpandey.android.dynamic.support.tutorial.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.Tutorial;
import com.pranavpandey.android.dynamic.support.tutorial.activity.DynamicTutorialActivity;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A {@link DynamicTutorialFragment} with an image, title, subtitle and description that
 * will be used with the {@link DynamicTutorialActivity}.
 */
public class DynamicTutorialFragment extends DynamicFragment
        implements Tutorial<DynamicTutorial, DynamicTutorialFragment> {

    /**
     * Fragment argument key to set the dynamic tutorial.
     */
    public static final String ADS_ARGS_TUTORIAL = "ads_args_tutorial";

    /**
     * Tutorial key to maintain its state.
     */
    public static final String ADS_STATE_TUTORIAL = "ads_state_tutorial";

    /**
     * Dynamic tutorial used by this fragment.
     */
    private DynamicTutorial mDynamicTutorial;

    /**
     * Root view of this fragment.
     */
    private ViewGroup mRootView;

    /**
     * Image view to show the tutorial image.
     */
    private ImageView mImageView;

    /**
     * Scroll view to show the scrolling content.
     */
    private NestedScrollView mScrollView;

    /**
     * Card view of this fragment.
     */
    private CardView mCardView;

    /**
     * Text view to show the tutorial title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the tutorial subtitle.
     */
    private TextView mSubtitleView;

    /**
     * Text view to show the tutorial description.
     */
    private TextView mDescriptionView;

    /**
     * Function to initialize this fragment.
     *
     * @param dynamicTutorial The dynamic tutorial for this fragment.
     *
     * @return An instance of {@link DynamicTutorialFragment}.
     */
    public static @NonNull DynamicTutorialFragment newInstance(
            @NonNull DynamicTutorial dynamicTutorial) {
        DynamicTutorialFragment fragment = new DynamicTutorialFragment();
        Bundle args = new Bundle();
        args.putParcelable(ADS_ARGS_TUTORIAL, dynamicTutorial);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (requireArguments().containsKey(ADS_ARGS_TUTORIAL)) {
                mDynamicTutorial = requireArguments().getParcelable(ADS_ARGS_TUTORIAL);
            }
        }

        if (savedInstanceState != null) {
            mDynamicTutorial = savedInstanceState.getParcelable(ADS_STATE_TUTORIAL);
        }
    }

    @Override
    public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_fragment_tutorial_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootView = view.findViewById(R.id.ads_tutorial_simple);
        mImageView = view.findViewById(R.id.ads_tutorial_simple_image);
        mScrollView = view.findViewById(R.id.ads_tutorial_simple_scroller);
        mCardView = view.findViewById(R.id.ads_tutorial_simple_card);
        mTitleView = view.findViewById(R.id.ads_tutorial_simple_title);
        mSubtitleView = view.findViewById(R.id.ads_tutorial_simple_subtitle);
        mDescriptionView = view.findViewById(R.id.ads_tutorial_simple_description);

        if (mDynamicTutorial.isSharedElement()) {
            ViewCompat.setTransitionName(mRootView, Tutorial.ADS_NAME_TUTORIAL);
            ViewCompat.setTransitionName(mImageView, Tutorial.ADS_NAME_TUTORIAL_IMAGE);
            ViewCompat.setTransitionName(mTitleView, Tutorial.ADS_NAME_TUTORIAL_TITLE);
            ViewCompat.setTransitionName(mSubtitleView, Tutorial.ADS_NAME_TUTORIAL_SUBTITLE);
        } else {
            ViewCompat.setTransitionName(mRootView, null);
            ViewCompat.setTransitionName(mImageView, null);
            ViewCompat.setTransitionName(mTitleView, null);
            ViewCompat.setTransitionName(mSubtitleView, null);
        }

        if (mDynamicTutorial != null) {
            if (mImageView != null) {
                Dynamic.set(mImageView, DynamicResourceUtils.getDrawable(
                        requireContext(), mDynamicTutorial.getImageRes()));
            }

            Dynamic.set(mTitleView, mDynamicTutorial.getTitle());
            Dynamic.set(mSubtitleView, mDynamicTutorial.getSubtitle());
            Dynamic.set(mDescriptionView, mDynamicTutorial.getDescription());
        }

        tintWidgets(getBackgroundColor());
    }

    @Override
    public @Nullable Object getDynamicEnterTransition() {
        return null;
    }

    @Override
    public @Nullable Object getDynamicExitTransition() {
        return null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ADS_STATE_TUTORIAL, mDynamicTutorial);
    }

    /**
     * Tint the widgets according to the supplied background color.
     *
     * @param color The color to generate the tint.
     */
    private void tintWidgets(@ColorInt int color) {
        final @ColorInt int tintColor = DynamicColorUtils.getTintColor(color);

        if (mDynamicTutorial != null && mDynamicTutorial.isTintImage()) {
            Dynamic.tint(mImageView, tintColor, color);
        }

        Dynamic.setContrastWithColor(mCardView, color);
        Dynamic.setElevationOnSameBackground(mCardView,
                !DynamicTheme.getInstance().get().isBackgroundSurface());
        if (Dynamic.isStrokeRequired()) {
            Dynamic.setColor(mCardView, DynamicColorUtils.setAlpha(color,
                    Color.alpha(DynamicTheme.getInstance().get().getSurfaceColor())));
            Dynamic.setStrokeColor(mCardView, DynamicColorUtils.getTintColor(color));
        } else {
            Dynamic.setColor(mCardView, DynamicTheme.getInstance().get().isBackgroundSurface()
                    ? color : DynamicTheme.getInstance().generateSurfaceColor(color));
        }

        Dynamic.tint(mTitleView, tintColor, Dynamic.getColor(mCardView, color));
        Dynamic.tint(mSubtitleView, tintColor, Dynamic.getColor(mCardView, color));
        Dynamic.tint(mDescriptionView, tintColor, Dynamic.getColor(mCardView, color));
        Dynamic.tint(mScrollView, tintColor, Dynamic.getColor(mCardView, color));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        onBackgroundColorChanged(getBackgroundColor());
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public @NonNull DynamicTutorial getTutorial() {
        return mDynamicTutorial;
    }

    @Override
    public @NonNull DynamicTutorialFragment createTutorial() {
        return this;
    }

    @Override
    public int getTutorialId() {
        return mDynamicTutorial.getId();
    }

    @Override
    public int getBackgroundColor() {
        return mDynamicTutorial != null ? mDynamicTutorial.getBackgroundColor()
                : DynamicTheme.getInstance().get().getPrimaryColor();
    }

    @Override
    public void onBackgroundColorChanged(int color) {
        tintWidgets(color);
    }

    @Override
    public void onSetPadding(int left, int top, int right, int bottom) {
        if (mRootView == null || bottom <= 0 || mRootView.getPaddingBottom() >= bottom) {
            return;
        }

        mRootView.setPadding(mRootView.getPaddingLeft() + left,
                mRootView.getPaddingTop() + top,
                mRootView.getPaddingRight() + right,
                mRootView.getPaddingBottom() + bottom);
    }

    /**
     * Returns the root view of this fragment.
     *
     * @return The root view of this fragment.
     */
    public @NonNull ViewGroup getRootView() {
        return mRootView;
    }

    /**
     * Returns the image view to show the tutorial image.
     *
     * @return The image view to show the tutorial image.
     */
    public @NonNull ImageView getImageView() {
        return mImageView;
    }

    /**
     * Returns the scroll view to show the scrolling content.
     *
     * @return The scroll view to show the scrolling content.
     */
    public @NonNull NestedScrollView getScrollView() {
        return mScrollView;
    }

    /**
     * Returns the card view of this fragment.
     *
     * @return The card view of this fragment.
     */
    public @NonNull CardView getCardView() {
        return mCardView;
    }

    /**
     * Returns the text view to show the tutorial title.
     *
     * @return The text view to show the tutorial title.
     */
    public @NonNull TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Returns the text view to show the tutorial subtitle.
     *
     * @return The text view to show the tutorial subtitle.
     */
    public @NonNull TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Returns the text view to show the tutorial description.
     *
     * @return The text view to show the tutorial description.
     */
    public @NonNull TextView getDescriptionView() {
        return mDescriptionView;
    }
}
