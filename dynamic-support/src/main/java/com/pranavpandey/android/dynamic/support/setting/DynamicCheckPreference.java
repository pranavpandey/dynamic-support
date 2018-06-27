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
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;

/**
 * A DynamicPreference to provide the functionality of a
 * {@link android.preference.SwitchPreference} with an action
 * button.
 */
public class DynamicCheckPreference extends DynamicPreference {

    /**
     * Default value for the checked state.
     */
    public static final boolean DEFAULT_CHECK_STATE = false;

    /**
     * {@code true} if this preference is checked.
     */
    private boolean mChecked;

    /**
     * Optional summary for the unchecked state.
     */
    private CharSequence mSummaryUnchecked;

    /**
     * Checked change listener to get the callback if switch
     * state is changed.
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

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
     * Switch compat to show the checked state.
     */
    private SwitchCompat mSwitchCompat;

    /**
     * Button to provide a secondary action like permission
     * request, etc.
     */
    private Button mActionView;

    public DynamicCheckPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicCheckPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicCheckPreference(@NonNull Context context,
                                  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicPreference);

        try {
            mChecked = a.getBoolean(R.styleable.DynamicPreference_ads_dynamicPreference_checked,
                    DEFAULT_CHECK_STATE);
            mSummaryUnchecked = a.getString(
                    R.styleable.DynamicPreference_ads_dynamicPreference_unchecked);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_check;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mPreferenceView = findViewById(R.id.ads_preference_checked);
        mIconView = findViewById(R.id.ads_preference_checked_icon);
        mTitleView = findViewById(R.id.ads_preference_checked_title);
        mSummaryView = findViewById(R.id.ads_preference_checked_summary);
        mDescriptionView = findViewById(R.id.ads_preference_checked_description);
        mSwitchCompat = findViewById(R.id.ads_preference_checked_switch);
        mActionView = findViewById(R.id.ads_preference_action_button);

        mPreferenceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!mChecked);

                if (getOnPreferenceClickListener() != null) {
                    getOnPreferenceClickListener().onClick(v);
                }
            }
        });

        mChecked = DynamicPreferences.getInstance()
                .loadPrefs(getPreferenceKey(), mChecked);
    }

    @Override
    protected void onUpdate() {
        mIconView.setImageDrawable(getIcon());

        if (getTitle() != null) {
            mTitleView.setText(getTitle());
            mTitleView.setVisibility(VISIBLE);
        } else {
            mTitleView.setVisibility(GONE);
        }

        if (getSummary() != null) {
            mSummaryView.setText(getSummary());
            mSummaryView.setVisibility(VISIBLE);
        } else {
            mSummaryView.setVisibility(GONE);
        }

        if (getDescription() != null) {
            mDescriptionView.setText(getDescription());
            mDescriptionView.setVisibility(VISIBLE);
        } else {
            mDescriptionView.setVisibility(GONE);
        }

        if (getOnActionClickListener() != null) {
            mActionView.setText(getActionString());
            mActionView.setOnClickListener(getOnActionClickListener());
            mActionView.setVisibility(VISIBLE);
        } else {
            mActionView.setVisibility(GONE);
        }

        mSwitchCompat.post(new Runnable() {
            @Override
            public void run() {
                mSwitchCompat.setChecked(mChecked);
            }
        });

        if (!mChecked) {
            if (mSummaryUnchecked != null) {
                mSummaryView.setText(mSummaryUnchecked);
                mSummaryView.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        mPreferenceView.setEnabled(enabled);
        mIconView.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mSummaryView.setEnabled(enabled);
        mDescriptionView.setEnabled(enabled);
        mSwitchCompat.setEnabled(enabled);
        mActionView.setEnabled(enabled);

        onUpdate();
    }

    /**
     * @return {@code true} if this preference is checked.
     */
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * Set the state of the switch to be checked or
     * unchecked.
     *
     * @param checked {@code true} if this preference
     *                is checked.
     */
    public void setChecked(boolean checked) {
        this.mChecked = checked;

        if (getPreferenceKey() != null) {
            DynamicPreferences.getInstance().savePrefs(getPreferenceKey(), checked);
        }
    }

    /**
     * @return The optional summary for the unchecked state.
     */
    public @Nullable CharSequence getSummaryUnchecked() {
        return mSummaryUnchecked;
    }

    /**
     * Set the optional summary for the unchecked state.
     *
     * @param summaryUnchecked The unchecked summary to be set.
     */
    public void setSummaryUnchecked(@Nullable String summaryUnchecked) {
        this.mSummaryUnchecked = summaryUnchecked;

        onUpdate();
    }

    /**
     * @return The checked change listener to get the callback if
     *         switch state is changed.
     */
    public @Nullable CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    /**
     * Set the checked change listener to get the callback if
     * switch state is changed.
     *
     * @param onCheckedChangeListener The listener to be set.
     */
    public void setOnCheckedChangeListener(
            @Nullable CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * @return The button to provide a secondary action like
     *         permission request, etc.
     */
    public Button getActionView() {
        return mActionView;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getPreferenceKey())) {
            boolean checked = DynamicPreferences.getInstance()
                    .loadPrefs(key, mChecked);
            mChecked = checked;

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(mSwitchCompat, checked);
            }

            onUpdate();
        }
    }
}
