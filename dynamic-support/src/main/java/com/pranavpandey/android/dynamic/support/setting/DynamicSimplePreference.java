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
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.DynamicButton;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A DynamicPreference to implement the basic functionality
 *
 * <p><p>It can be extended to modify according to the need.
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
     * Frame to add a secondary view like another image, etc.
     */
    private ViewGroup mViewFrame;

    /**
     * Button to provide a secondary action like permission request, etc.
     */
    private DynamicButton mActionView;

    public DynamicSimplePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSimplePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSimplePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) { }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_simple;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mPreferenceView = findViewById(R.id.ads_preference);
        mIconView = findViewById(R.id.ads_preference_icon);
        mTitleView = findViewById(R.id.ads_preference_title);
        mSummaryView = findViewById(R.id.ads_preference_summary);
        mDescriptionView = findViewById(R.id.ads_preference_description);
        mValueView = findViewById(R.id.ads_preference_value);
        mViewFrame = findViewById(R.id.ads_preference_view);
        mActionView = findViewById(R.id.ads_preference_action_button);

        setViewFrame(null, true);
    }

    @Override
    protected void onUpdate() {
        if (mPreferenceView != null) {
            mPreferenceView.setOnClickListener(getOnPreferenceClickListener());
        }

        setImageView(mIconView, getIcon());
        setTextView(mTitleView, getTitle());
        setTextView(mSummaryView, getSummary());
        setTextView(mValueView, getValueString());
        setTextView(mDescriptionView, getDescription());

        if (mActionView != null) {
            if (getOnActionClickListener() != null) {
                mActionView.setText(getActionString());
                mActionView.setOnClickListener(getOnActionClickListener());
                mActionView.setVisibility(View.VISIBLE);
            } else {
                mActionView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        setViewEnabled(mPreferenceView, enabled);
        setViewEnabled(mIconView, enabled);
        setViewEnabled(mTitleView, enabled);
        setViewEnabled(mSummaryView, enabled);
        setViewEnabled(mDescriptionView, enabled);
        setViewEnabled(mValueView, enabled);
        setViewEnabled(mActionView, enabled);

        if (mViewFrame != null && mViewFrame.getChildCount() > 0) {
            setViewEnabled(mViewFrame.getChildAt(0), enabled);
        }

        onUpdate();
    }

    /**
     * Get the preference root view.
     *
     * @return The preference root view.
     */
    public ViewGroup getPreferenceView() {
        return mPreferenceView;
    }

    /**
     * Get the text view to show the title.
     *
     * @return The text view to show the title.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the text view to show the summary.
     *
     * @return The text view to show the summary.
     */
    public TextView getSummaryView() {
        return mSummaryView;
    }

    /**
     * Get the text view to show the value.
     *
     * @return The text view to show the value.
     */
    public TextView getValueView() {
        return mValueView;
    }

    /**
     * Get the text view to show the description.
     *
     * @return The text view to show the description.
     */
    public TextView getDescriptionView() {
        return mDescriptionView;
    }

    /**
     * Get the text view to show the title.
     *
     * @return The text view to show the title.
     */
    public ViewGroup getViewFrame() {
        return mViewFrame;
    }

    /**
     * Add a view in the view frame.
     *
     * @param view The view to be added.
     * @param removePrevious {@code true} to remove all the previous views of the view group.
     */
    public void setViewFrame(@Nullable View view, boolean removePrevious) {
        if (mViewFrame != null) {
            if (view != null) {
                mViewFrame.setVisibility(View.VISIBLE);
                DynamicViewUtils.addView(mViewFrame, view, removePrevious);
            } else {
                mViewFrame.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Get the button to provide a secondary action like permission request, etc.
     *
     * @return The button to provide a secondary action like permission request, etc.
     */
    public DynamicButton getActionView() {
        return mActionView;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key != null && key.equals(getPreferenceKey())) {
            onUpdate();
        }
    }
}
