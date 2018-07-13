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
     * Default value for the seek bar.
     */
    public static final int DEFAULT_SEEK_VALUE = 0;

    /**
     * Default value for the minimum seek value.
     */
    public static final int DEFAULT_MIN_VALUE = 0;

    /**
     * Default value for the maximum seek value.
     */
    public static final int DEFAULT_MAX_VALUE = 100;

    /**
     * Default value for the seek interval.
     */
    public static final int DEFAULT_SEEK_INTERVAL = 1;

    /**
     * Default value for the seek controls.
     */
    public static final boolean DEFAULT_SEEK_CONTROLS = false;

    /**
     * Default value for this preference.
     */
    private int mDefaultValue;

    /**
     * The current seek bar progress.
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
     * Seek interval for the seek bar.
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

        if (!enabled) {
            mValueView.setEnabled(enabled);
            mSeekLeftView.setEnabled(enabled);
            mSeekRightView.setEnabled(enabled);
            mActionView.setEnabled(enabled);
        }

        onUpdate();
    }

    /**
     * @return The default value for this preference.
     */
    public int getDefaultValue() {
        return mDefaultValue;
    }

    /**
     * Set the default value for this preference.
     *
     * @param defaultValue The default value to be set.
     */
    public void setDefaultValue(int defaultValue) {
        this.mDefaultValue = defaultValue;

        onUpdate();
    }

    /**
     * @return The maximum value for this preference.
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    /**
     * Set the maximum value for this preference.
     *
     * @param maxValue The maximum value to be set.
     */
    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;

        onUpdate();
    }

    /**
     * @return The minimum value for this preference.
     */
    public int getMinValue() {
        return mMinValue;
    }

    /**
     * Set the minimum value for this preference.
     *
     * @param minValue The minimum value to be set.
     */
    public void setMinValue(int minValue) {
        this.mMinValue = minValue;

        onUpdate();
    }

    /**
     * @return The current seek bar progress.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Set the current seek bar progress.
     *
     * @param progress The progress to be set.
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
     * @return The seek interval for the seek bar.
     */
    public int getSeekInterval() {
        return mSeekInterval;
    }

    /**
     * Set the seek interval for the seek bar.
     *
     * @param seekInterval The seek interval to be set.
     */
    public void setSeekInterval(int seekInterval) {
        this.mSeekInterval = seekInterval;

        onUpdate();
    }

    /**
     * @return The optional unit text for the preference value.
     */
    public @Nullable CharSequence getUnit() {
        return mUnit;
    }

    /**
     * Set the optional unit text for the preference value.
     *
     * @param unit The unit to be set.
     */
    public void setUnit(@Nullable CharSequence unit) {
        this.mUnit = unit;

        onUpdate();
    }

    /**
     * Set the value for this preference.
     *
     * @param value The value to be set.
     */
    public void setValue(int value) {
        setProgress(getProgressFromValue(value));
    }

    /**
     * @return {@code true} to show seek bar buttons to increase
     *         or decrease the value.
     */
    public boolean isControls() {
        return mControls;
    }

    /**
     * Set the seek bar controls to be enabled or disabled.
     *
     * @param controls {@code true} to show seek bar buttons
     *                 to increase or decrease the value.
     */
    public void setControls(boolean controls) {
        this.mControls = controls;

        onUpdate();
    }

    /**
     * @return The seek bar change listener to get the callback for
     *         seek events.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return mOnSeekBarChangeListener;
    }

    /**
     * Set the seek bar change listener to get the callback for
     * seek events.
     *
     * @param onSeekBarChangeListener The listener to be set.
     */
    public void setOnSeekBarChangeListener(
            @Nullable SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    /**
     * @return The seek bar change listener to get the callback for
     *         control events.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarControlListener() {
        return mOnSeekBarControlListener;
    }

    /**
     * Set the seek bar change listener to get the callback for
     * control events.
     *
     * @param onSeekBarControlListener The listener to be set.
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
     * @return The seek bar progress according to the supplied
     *         value.
     *
     * @param value The value to converted into seek bar progress.
     */
    private int getProgressFromValue(int value) {
        return (value - mMinValue) / mSeekInterval;
    }

    /**
     * @return The preference value according to the seek bar
     *         progress.
     */
    public int getValueFromProgress() {
        return mMinValue + (mProgress * mSeekInterval);
    }

    /**
     * @return The seek bar to display and modify the preference
     *         value.
     */
    public AppCompatSeekBar getSeekBar() {
        return mSeekBar;
    }

    /**
     * @return The text view to show the value.
     */
    public TextView getValueView() {
        return mValueView;
    }

    /**
     * Set the color for seek bar and value view.
     *
     * @param color The color to be set.
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
