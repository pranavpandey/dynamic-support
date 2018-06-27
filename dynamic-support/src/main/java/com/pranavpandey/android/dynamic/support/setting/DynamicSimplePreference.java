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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A DynamicPreference to implement the basic functionality
 * It can be extended to modify according to the need.
 */
public class DynamicSimplePreference extends DynamicPreference {

    /**
     * The preference root view.
     */
    private ViewGroup mPreferenceView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the summary.
     */
    private TextView mSummaryView;

    /**
     * Text view to show the description.
     */
    private TextView mDescriptionView;

    /**
     * Text view to show the value.
     */
    private TextView mValueView;

    /**
     * Frame to add a secondary view like another image,
     * etc.
     */
    private ViewGroup mViewFrame;

    /**
     * Button to provide a secondary action like permission
     * request, etc.
     */
    private Button mActionView;

    public DynamicSimplePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSimplePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSimplePreference(@NonNull Context context,
                                   @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) { }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_simple;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mPreferenceView = findViewById(R.id.ads_preference_simple);
        mIconView = findViewById(R.id.ads_preference_simple_icon);
        mTitleView = findViewById(R.id.ads_preference_simple_title);
        mSummaryView = findViewById(R.id.ads_preference_simple_summary);
        mDescriptionView = findViewById(R.id.ads_preference_simple_description);
        mValueView = findViewById(R.id.ads_preference_simple_value);
        mViewFrame = findViewById(R.id.ads_preference_simple_view);
        mActionView = findViewById(R.id.ads_preference_action_button);

        setViewFrame(null, true);
    }

    @Override
    protected void onUpdate() {
        if (getOnPreferenceClickListener() != null) {
            mPreferenceView.setOnClickListener(getOnPreferenceClickListener());
        }

        mIconView.setImageDrawable(getIcon());

        if (getTitle() != null) {
            mTitleView.setText(getTitle());
            mTitleView.setVisibility(View.VISIBLE);
        } else {
            mTitleView.setVisibility(View.GONE);
        }

        if (getSummary() != null) {
            mSummaryView.setText(getSummary());
            mSummaryView.setVisibility(View.VISIBLE);
        } else {
            mSummaryView.setVisibility(View.GONE);
        }

        if (getDescription() != null) {
            mDescriptionView.setText(getDescription());
            mDescriptionView.setVisibility(View.VISIBLE);
        } else {
            mDescriptionView.setVisibility(View.GONE);
        }

        if (getValueString() != null) {
            mValueView.setText(getValueString());
            mValueView.setVisibility(View.VISIBLE);
        } else {
            mValueView.setVisibility(View.GONE);
        }

        if (getOnActionClickListener() != null) {
            mActionView.setText(getActionString());
            mActionView.setOnClickListener(getOnActionClickListener());
            mActionView.setVisibility(View.VISIBLE);
        } else {
            mActionView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        mPreferenceView.setEnabled(enabled);
        mIconView.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mSummaryView.setEnabled(enabled);
        mDescriptionView.setEnabled(enabled);
        mValueView.setEnabled(enabled);
        mActionView.setEnabled(enabled);

        if (mViewFrame.getChildCount() > 0) {
            mViewFrame.getChildAt(0).setEnabled(enabled);
        }

        onUpdate();
    }

    /**
     * @return The preference root view.
     */
    public ViewGroup getPreferenceView() {
        return mPreferenceView;
    }

    /**
     * @return The image view to show the icon.
     */
    public TextView getValueView() {
        return mValueView;
    }

    /**
     * @return The text view to show the title.
     */
    public ViewGroup getViewFrame() {
        return mViewFrame;
    }

    /**
     * Add a view in the view frame.
     *
     * @param view The view to be added.
     * @param removePrevious {@code true} to remove all the previous
     *                       views of the view group.
     */
    public void setViewFrame(@Nullable View view, boolean removePrevious) {
        if (view != null) {
            mViewFrame.setVisibility(View.VISIBLE);
            DynamicViewUtils.addView(mViewFrame, view, removePrevious);
        } else {
            mViewFrame.setVisibility(View.GONE);
        }
    }
}
