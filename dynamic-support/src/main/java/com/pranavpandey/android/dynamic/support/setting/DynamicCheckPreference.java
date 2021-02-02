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

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicSimplePreference} to provide the functionality of a
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
     * Checked change listener to get the callback when compound button state is changed.
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * Compound button to show the checked state.
     */
    private CompoundButton mCompoundButton;

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
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicCheckPreference);

        try {
            mChecked = a.getBoolean(
                    R.styleable.DynamicCheckPreference_ads_checked,
                    DEFAULT_CHECK_STATE);
            mSummaryUnchecked = a.getString(
                    R.styleable.DynamicCheckPreference_ads_unchecked);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mCompoundButton = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_check, this, false)
                .findViewById(R.id.ads_preference_check_switch);

        setViewFrame(mCompoundButton, true);

        setOnPreferenceClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked());
            }
        }, false);

        mCompoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (getOnCheckedChangeListener() != null) {
                    getOnCheckedChangeListener().onCheckedChanged(compoundButton, isChecked());
                }
            }
        });
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        mChecked = DynamicPreferences.getInstance().load(getPreferenceKey(), isChecked());

        if (getCompoundButton() != null) {
            if (!isChecked() && getSummaryUnchecked() != null) {
                setTextView(getSummaryView(), getSummaryUnchecked());
            }

            getCompoundButton().post(mUpdateRunnable);
        }
    }

    /**
     * Runnable to post the update.
     */
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (getCompoundButton() != null) {
                getCompoundButton().setChecked(isChecked());
            }
        }
    };

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getCompoundButton(), enabled);
    }

    @Override
    public void setColor() {
        super.setColor();

        if (getBackgroundAware() != Theme.BackgroundAware.UNKNOWN) {
            Dynamic.setBackgroundAware(getCompoundButton(), getBackgroundAware());
        }

        if (getContrastWithColorType() != Theme.ColorType.NONE
                && getContrastWithColorType() != Theme.ColorType.CUSTOM) {
            Dynamic.setContrastWithColorType(getCompoundButton(), getContrastWithColorType());
        } else if (getContrastWithColorType() == Theme.ColorType.CUSTOM
                && getContrastWithColor() != Theme.Color.UNKNOWN) {
            Dynamic.setContrastWithColor(getCompoundButton(), getContrastWithColor());
        }
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

        DynamicPreferences.getInstance().save(getPreferenceKey(), checked);
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

        update();
    }

    /**
     * Get the compound button used by this preference.
     *
     * @return The compound button used by this preference.
     */
    public @Nullable CompoundButton getCompoundButton() {
        return mCompoundButton;
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

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(super.getPreferenceKey())) {
            update();
        }
    }
}
