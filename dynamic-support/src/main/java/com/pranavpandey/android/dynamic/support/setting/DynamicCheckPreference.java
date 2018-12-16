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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;

/**
 * A DynamicSimplePreference to provide the functionality of a
 * {@link android.preference.SwitchPreference}.
 */
public class DynamicCheckPreference extends DynamicSimplePreference {

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
     * Checked change listener to get the callback if switch state is changed.
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * Switch compat to show the checked state.
     */
    private SwitchCompat mSwitchCompat;

    public DynamicCheckPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicCheckPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicCheckPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicPreference);

        try {
            mChecked = a.getBoolean(R.styleable.DynamicPreference_ads_checked,
                    DEFAULT_CHECK_STATE);
            mSummaryUnchecked = a.getString(
                    R.styleable.DynamicPreference_ads_unchecked);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mSwitchCompat = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_check, this, false)
                .findViewById(R.id.ads_preference_check_switch);

        setViewFrame(mSwitchCompat, true);

        setOnPreferenceClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!mChecked);
            }
        }, false);


        mChecked = DynamicPreferences.getInstance()
                .loadPrefs(getPreferenceKey(), mChecked);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (mSwitchCompat != null) {
            if (!mChecked) {
                if (mSummaryUnchecked != null) {
                    setTextView(getSummaryView(), mSummaryUnchecked);
                }
            }

            mSwitchCompat.post(new Runnable() {
                @Override
                public void run() {
                    mSwitchCompat.setChecked(mChecked);
                }
            });
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        setViewEnabled(mSwitchCompat, enabled);
    }

    /**
     * Returns whether this preference is checked.
     *
     * @return {@code true} if this preference is checked.
     */
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * Set the state of the switch to be checked or unchecked.
     *
     * @param checked {@code true} if this preference is checked.
     */
    public void setChecked(boolean checked) {
        this.mChecked = checked;

        if (getPreferenceKey() != null) {
            DynamicPreferences.getInstance().savePrefs(getPreferenceKey(), checked);
        }
    }

    /**
     * Get the optional summary for the unchecked state.
     *
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
     * Returns the checked change listener.
     *
     * @return The checked change listener to get the callback if switch state is changed.
     */
    public @Nullable CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    /**
     * Set the checked change listener to get the callback if switch state is changed.
     *
     * @param onCheckedChangeListener The listener to be set.
     */
    public void setOnCheckedChangeListener(
            @Nullable CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getPreferenceKey())) {
            mChecked = DynamicPreferences.getInstance().loadPrefs(key, mChecked);

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(mSwitchCompat, mChecked);
            }

            onUpdate();
        }
    }
}
