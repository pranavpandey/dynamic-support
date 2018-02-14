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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.widget.DynamicSeekBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;

/**
 * A DynamicPreference to provide the functionality of a seek bar
 * preference with control buttons to modify the value.
 */
public class DynamicSeekBarPreference extends DynamicPreference {

    /**
     * Constant for the seek bar animation duration.
     */
    public static final int ANIMATION_DURATION = 500;

    /**
     * Default value for the {@link #mDefaultValue}.
     */
    public static final int DEFAULT_SEEK_VALUE = 0;

    /**
     * Default value for the {@link #mMinValue}.
     */
    public static final int DEFAULT_MIN_VALUE = 0;

    /**
     * Default value for the {@link #mMaxValue}.
     */
    public static final int DEFAULT_MAX_VALUE = 100;

    /**
     * Default value for the {@link #mSeekInterval}.
     */
    public static final int DEFAULT_SEEK_INTERVAL = 1;

    /**
     * Default value for the {@link #mControls}.
     */
    public static final boolean DEFAULT_SEEK_CONTROLS = false;

    /**
     * Default value for this preference.
     */
    private int mDefaultValue;

    /**
     * The current {@link #mSeekBar} progress.
     */
    private int mProgress;

    /**
     * Maximum value for this preference.
     */
    private int mMaxValue;

    /**
     * Minimum value for this preference.
     */
    private int mMinValue;

    /**
     * Seek interval for the {@link #mSeekBar}.
     */
    private int mSeekInterval;

    /**
     * Optional unit text for the preference value.
     */
    private CharSequence mUnit;

    /**
     * {@code true} to show seek bar buttons to increase or
     * decrease the value.
     */
    private boolean mControls;

    /**
     * Seek bar change listener to get the callback for seek
     * events.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

    /**
     * Seek bar change listener to get the callback for control
     * events.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarControlListener;

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
     * Image view to show the decrease button.
     */
    private ImageView mSeekLeftView;

    /**
     * Image view to show the increase button.
     */
    private ImageView mSeekRightView;

    /**
     * Seek bar to display and modify the preference value.
     */
    private AppCompatSeekBar mSeekBar;

    /**
     * Button to provide a secondary action like permission
     * request, etc.
     */
    private Button mActionView;

    public DynamicSeekBarPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSeekBarPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSeekBarPreference(@NonNull Context context,
                                    @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicPreference);

        try {
            mMaxValue = a.getInteger(R.styleable.DynamicPreference_ads_dynamicPreference_max,
                    DEFAULT_MAX_VALUE);
            mMinValue = a.getInteger(R.styleable.DynamicPreference_ads_dynamicPreference_min,
                    DEFAULT_MIN_VALUE);
            mDefaultValue = a.getInteger(R.styleable.DynamicPreference_ads_dynamicPreference_progress,
                    DEFAULT_SEEK_VALUE);
            mSeekInterval = a.getInteger(R.styleable.DynamicPreference_ads_dynamicPreference_interval,
                    DEFAULT_SEEK_INTERVAL);
            mControls = a.getBoolean(R.styleable.DynamicPreference_ads_dynamicPreference_controls,
                    DEFAULT_SEEK_CONTROLS);
            mUnit = a.getString(R.styleable.DynamicPreference_ads_dynamicPreference_unit);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_seek_bar;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mPreferenceView = findViewById(R.id.ads_preference_seek_bar);
        mIconView = findViewById(R.id.ads_preference_seek_bar_icon);
        mTitleView = findViewById(R.id.ads_preference_seek_bar_title);
        mSummaryView = findViewById(R.id.ads_preference_seek_bar_summary);
        mDescriptionView = findViewById(R.id.ads_preference_seek_bar_description);
        mValueView = findViewById(R.id.ads_preference_seek_bar_value);
        mSeekBar = findViewById(R.id.ads_preference_seek_bar_seek);
        mSeekLeftView = findViewById(R.id.ads_preference_seek_bar_left);
        mSeekRightView = findViewById(R.id.ads_preference_seek_bar_right);
        mActionView = findViewById(R.id.ads_preference_action_button);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mProgress = progress;
                    updateSeekFunctions();
                } else {
                    mSeekBar.setProgress(mProgress);
                }

                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onProgressChanged(
                            seekBar, getValueFromProgress(), fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setProgress(mProgress);

                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }
        });

        mSeekLeftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(mProgress - 1);
            }
        });

        mSeekRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(mProgress + 1);
            }
        });

        setActionButton(getContext().getString(R.string.ads_default), new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int defaultValue = getProgressFromValue(mDefaultValue);
                ObjectAnimator animation = ObjectAnimator.ofInt(mSeekBar,
                        "progress", defaultValue);
                animation.setDuration(ANIMATION_DURATION);
                animation.start();
                setProgress(defaultValue);
            }
        });

        if (getPreferenceKey() != null) {
            mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                    .loadPrefs(getPreferenceKey(), mDefaultValue));
        }
    }

    /**
     * Update seek bar functions according to the current
     * parameters.
     */
    private void updateSeekFunctions() {
        int actualValue = getValueFromProgress();

        if (mUnit != null) {
            mValueView.setText(String.format(
                    getContext().getString(R.string.ads_format_blank_space),
                    String.valueOf(actualValue), mUnit));
        } else {
            mValueView.setText(String.valueOf(actualValue));
        }

        if (isEnabled()) {
            mSeekLeftView.setEnabled(mProgress > DEFAULT_MIN_VALUE);
            mSeekRightView.setEnabled(mProgress < getMax());
            mActionView.setEnabled(actualValue != mDefaultValue);
        }
    }

    /**
     * Update seek bar controls according to the current
     * parameters.
     */
    private void setProgressFromControl(int progress) {
        if (mOnSeekBarControlListener != null) {
            mOnSeekBarControlListener.onStartTrackingTouch(mSeekBar);
        }

        setProgress(progress);

        if (mOnSeekBarControlListener != null) {
            mOnSeekBarControlListener.onProgressChanged(mSeekBar, mProgress, true);
            mOnSeekBarControlListener.onStopTrackingTouch(mSeekBar);
        }
    }

    @Override
    protected void onUpdate() {
        mIconView.setImageDrawable(getIcon());
        mSeekBar.setMax(getMax());

        if (mControls) {
            mSeekLeftView.setVisibility(VISIBLE);
            mSeekRightView.setVisibility(VISIBLE);
        } else {
            mSeekLeftView.setVisibility(GONE);
            mSeekRightView.setVisibility(GONE);
        }

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

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(mProgress);
                updateSeekFunctions();
            }
        });
    }

    @Override
    protected void onEnabled(boolean enabled) {
        mPreferenceView.setEnabled(enabled);
        mIconView.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mSummaryView.setEnabled(enabled);
        mDescriptionView.setEnabled(enabled);
        mSeekBar.setEnabled(enabled);
        mValueView.setEnabled(enabled);
        mSeekLeftView.setEnabled(enabled);
        mSeekRightView.setEnabled(enabled);
        mActionView.setEnabled(enabled);

        onUpdate();
    }

    /**
     * Getter for {@link #mMaxValue}.
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    /**
     * Setter for {@link #mMaxValue}.
     */
    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;

        onUpdate();
    }

    /**
     * Getter for {@link #mMinValue}.
     */
    public int getMinValue() {
        return mMinValue;
    }

    /**
     * Setter for {@link #mMinValue}.
     */
    public void setMinValue(int minValue) {
        this.mMinValue = minValue;

        onUpdate();
    }

    /**
     * Getter for {@link #mProgress}.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Setter for {@link #mProgress}.
     */
    public void setProgress(int progress) {
        this.mProgress = progress;

        if (getPreferenceKey() != null) {
            DynamicPreferences.getInstance().savePrefs(
                    getPreferenceKey(), getValueFromProgress());
        } else {
            onUpdate();
        }
    }

    /**
     * Getter for {@link #mUnit}.
     */
    public @Nullable CharSequence getUnit() {
        return mUnit;
    }

    /**
     * Setter for {@link #mUnit}.
     */
    public void setUnit(@Nullable CharSequence unit) {
        this.mUnit = unit;

        onUpdate();
    }

    /**
     * Getter for {@link #mSeekInterval}.
     */
    public int getSeekInterval() {
        return mSeekInterval;
    }

    /**
     * Setter for {@link #mSeekInterval}.
     */
    public void setSeekInterval(int seekInterval) {
        this.mSeekInterval = seekInterval;

        onUpdate();
    }


    public void setValue(int value) {
        setProgress(getProgressFromValue(value));
    }

    /**
     * Getter for {@link #mDefaultValue}.
     */
    public int getDefaultValue() {
        return mDefaultValue;
    }

    /**
     * Setter for {@link #mDefaultValue}.
     */
    public void setDefaultValue(int defaultValue) {
        this.mDefaultValue = defaultValue;

        onUpdate();
    }

    /**
     * Getter for {@link #mControls}.
     */
    public boolean isControls() {
        return mControls;
    }

    /**
     * Setter for {@link #mControls}.
     */
    public void setControls(boolean controls) {
        this.mControls = controls;

        onUpdate();
    }

    /**
     * Getter for {@link #mOnSeekBarChangeListener}.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return mOnSeekBarChangeListener;
    }

    /**
     * Setter for {@link #mOnSeekBarChangeListener}.
     */
    public void setOnSeekBarChangeListener(
            @Nullable SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    /**
     * Getter for {@link #mOnSeekBarControlListener}.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarControlListener() {
        return mOnSeekBarControlListener;
    }

    /**
     * Setter for {@link #mOnSeekBarControlListener}.
     */
    public void setOnSeekBarControlListener(
            @Nullable SeekBar.OnSeekBarChangeListener onSeekBarControlListener) {
        this.mOnSeekBarControlListener= onSeekBarControlListener;
    }

    /**
     * @return The maximum preference value according to the
     *         {@link #mSeekInterval}.
     */
    private int getMax() {
        return (mMaxValue - mMinValue) / mSeekInterval;
    }

    /**
     * @return The {@link #mSeekBar} progress according to the
     *         supplied value.
     *
     * @param value The value to converted into seek bar progress.
     */
    private int getProgressFromValue(int value) {
        return (value - mMinValue) / mSeekInterval;
    }

    /**
     * @return The preference value according to the {@link #mSeekBar}
     *         progress.
     */
    public int getValueFromProgress() {
        return mMinValue + (mProgress * mSeekInterval);
    }

    /**
     * Getter for {@link #mSeekBar}.
     */
    public AppCompatSeekBar getSeekBar() {
        return mSeekBar;
    }

    /**
     * Set color for {@link #mSeekBar} and {@link #mValueView}.
     */
    public void setColor(@ColorInt int color) {
        ((DynamicSeekBar) mSeekBar).setColor(color);
        ((DynamicTextView) mValueView).setColor(color);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getPreferenceKey())) {
            mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                    .loadPrefs(getPreferenceKey(), mProgress));

            onUpdate();
        }
    }
}
