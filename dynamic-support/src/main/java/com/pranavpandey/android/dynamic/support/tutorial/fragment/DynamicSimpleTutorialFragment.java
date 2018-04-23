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

package com.pranavpandey.android.dynamic.support.tutorial.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicSimpleTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorialActivity;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNestedScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A intro fragment which will be used with the {@link DynamicTutorialActivity}
 * to display a list of fragments in the view pager. Extend this provide any
 * extra functionality according to the need.
 */
public class DynamicSimpleTutorialFragment extends DynamicTutorialFragment {

    /**
     * Fragment argument key to set the dynamic tutorial.
     */
    private static final String ADS_ARGS_TUTORIAL = "ads_args_tutorial";

    /**
     * Tutorial key to maintain its state.
     */
    private static final String ADS_STATE_TUTORIAL = "ads_state_tutorial";

    /**
     * Dynamic simple tutorial used by this fragment.
     */
    private DynamicSimpleTutorial mDynamicSimpleTutorial;

    /**
     * Image view to show the tutorial image.
     */
    private DynamicImageView mImageView;

    /**
     * Scroll view to show the scrolling content.
     */
    private DynamicNestedScrollView mScrollView;

    /**
     * Text view to show the tutorial title.
     */
    private DynamicTextView mTitleView;

    /**
     * Text view to show the tutorial subtitle.
     */
    private DynamicTextView mSubtitleView;

    /**
     * Text view to show the tutorial description.
     */
    private DynamicTextView mDescriptionView;

    /**
     * Function to initialize this fragment.
     *
     * @param dynamicSimpleTutorial The dynamic simple tutorial for this
     *                              fragment.
     *
     * @return An instance of {@link DynamicSimpleTutorialFragment}.
     */
    public static DynamicTutorial newInstance(
            @NonNull DynamicSimpleTutorial dynamicSimpleTutorial) {
        DynamicSimpleTutorialFragment fragment = new DynamicSimpleTutorialFragment();
        Bundle args = new Bundle();
        args.putParcelable(ADS_ARGS_TUTORIAL, dynamicSimpleTutorial);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public DynamicSimpleTutorial getTutorial() {
        return mDynamicSimpleTutorial;
    }

    @Override
    public int getTutorialId() {
        return mDynamicSimpleTutorial.getId();
    }

    @Override
    public int getBackgroundColor() {
        return mDynamicSimpleTutorial != null
                ? mDynamicSimpleTutorial.getBackgroundColor()
                : super.getBackgroundColor();
    }

    @Override
    public void onBackgroundColorChanged(int color) {
        tintWidgets(color);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ADS_ARGS_TUTORIAL)) {
                mDynamicSimpleTutorial = getArguments().getParcelable(ADS_ARGS_TUTORIAL);
            }
        }

        if (savedInstanceState != null) {
            mDynamicSimpleTutorial = savedInstanceState.getParcelable(ADS_STATE_TUTORIAL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_fragment_tutorial_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageView = view.findViewById(R.id.ads_tutorial_simple_image);
        mScrollView = view.findViewById(R.id.ads_tutorial_simple_scroll);
        mTitleView = view.findViewById(R.id.ads_tutorial_simple_title);
        mSubtitleView = view.findViewById(R.id.ads_tutorial_simple_subtitle);
        mDescriptionView = view.findViewById(R.id.ads_tutorial_simple_description);

        if (mDynamicSimpleTutorial != null) {
            if (mImageView != null) {
                setTutorialImage(mImageView, DynamicResourceUtils.getDrawable(
                        getContext(), mDynamicSimpleTutorial.getImageRes()));
            }

            setTutorialText(mTitleView, mDynamicSimpleTutorial.getTitle());
            setTutorialText(mSubtitleView, mDynamicSimpleTutorial.getSubtitle());
            setTutorialText(mDescriptionView, mDynamicSimpleTutorial.getDescription());
        }

        tintWidgets(getBackgroundColor());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ADS_STATE_TUTORIAL, mDynamicSimpleTutorial);
    }

    /**
     * Tint the widgets according to the supplied background
     * color.
     *
     * @param color Color to generate the tint.
     */
    private void tintWidgets(@ColorInt int color) {
        final @ColorInt int tintColor = DynamicColorUtils.getTintColor(color);

        if (mDynamicSimpleTutorial != null && mDynamicSimpleTutorial.isTintImage()) {
            tintDynamicView(mImageView, tintColor);
        }

        tintDynamicView(mTitleView, tintColor);
        tintDynamicView(mScrollView, tintColor);
        tintDynamicView(mSubtitleView, tintColor);
        tintDynamicView(mDescriptionView, tintColor);
    }
}
