/*
 * Copyright 2018-2020 Pranav Pandey
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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.DynamicSeekBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;

/**
 * A {@link DynamicSpinnerPreference} to provide the functionality of a seek bar preference with
 * control buttons to modify the value.
 */
public class DynamicSeekBarPreference extends DynamicSpinnerPreference {

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
     * {@code true} to show seek bar buttons to increase or decrease the value.
     */
    private boolean mControls;

    /**
     * Seek bar change listener to get the callback for seek events.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

    /**
     * Seek bar change listener to get the callback for control events.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarControlListener;

    /**
     * Text view to show the seek bar value.
     */
    private TextView mSeekBarView;

    /**
     * Image view to show the decrease button.
     */
    private ImageButton mSeekBarLeftView;

    /**
     * Image view to show the increase button.
     */
    private ImageButton mSeekBarRightView;

    /**
     * Seek bar to display and modify the preference value.
     */
    private AppCompatSeekBar mSeekBar;

    /**
     * Button to provide a secondary action like permission request, etc.
     */
    private Button mActionView;

    /**
     * {@code true} if seek bar is controls are enabled.
     */
    private boolean mSeekBarEnabled;

    public DynamicSeekBarPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSeekBarPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSeekBarPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicSeekBarPreference);

        try {
            mMaxValue = a.getInteger(
                    R.styleable.DynamicSeekBarPreference_ads_max,
                    DEFAULT_MAX_VALUE);
            mMinValue = a.getInteger(
                    R.styleable.DynamicSeekBarPreference_ads_min,
                    DEFAULT_MIN_VALUE);
            mDefaultValue = a.getInteger(
                    R.styleable.DynamicSeekBarPreference_ads_progress,
                    DEFAULT_SEEK_VALUE);
            mSeekInterval = a.getInteger(
                    R.styleable.DynamicSeekBarPreference_ads_interval,
                    DEFAULT_SEEK_INTERVAL);
            mControls = a.getBoolean(
                    R.styleable.DynamicSeekBarPreference_ads_controls,
                    DEFAULT_SEEK_CONTROLS);
            mUnit = a.getString(
                    R.styleable.DynamicSeekBarPreference_ads_unit);
            mSeekBarEnabled = a.getBoolean(
                    R.styleable.DynamicSeekBarPreference_ads_seek_bar,
                    true);
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
        super.onInflate();

        mSeekBarView = findViewById(R.id.ads_preference_seek_bar_value);
        mSeekBar = findViewById(R.id.ads_preference_seek_bar_seek);
        mSeekBarLeftView = findViewById(R.id.ads_preference_seek_bar_left);
        mSeekBarRightView = findViewById(R.id.ads_preference_seek_bar_right);
        mActionView = findViewById(R.id.ads_preference_action_button);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mProgress = progress;
                    updateSeekFunctions();
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

        mSeekBarLeftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(mProgress - 1);
            }
        });

        mSeekBarRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(mProgress + 1);
            }
        });

        setActionButton(getContext().getString(R.string.ads_default), new OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSeekBar(mDefaultValue);
            }
        });

        if (super.getPreferenceKey() != null) {
            mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                    .load(super.getPreferenceKey(), mDefaultValue));
            mSeekBar.setProgress(mProgress);
            updateSeekFunctions();
        }
    }

    private void animateSeekBar(int value) {
        final int progress = getProgressFromValue(value);
        ObjectAnimator animation = ObjectAnimator.ofInt(mSeekBar,
                "progress", getProgress(), progress);
        animation.setDuration(ANIMATION_DURATION);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setProgress(progress);
            }

            @Override
            public void onAnimationEnd(Animator animation) { }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });

        animation.start();
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return getAltPreferenceKey();
    }

    /**
     * Update seek bar functions according to the current parameters.
     */
    private void updateSeekFunctions() {
        int actualValue = getValueFromProgress();

        if (mUnit != null) {
            mSeekBarView.setText(String.format(
                    getContext().getString(R.string.ads_format_blank_space),
                    String.valueOf(actualValue), mUnit));
        } else {
            mSeekBarView.setText(String.valueOf(actualValue));
        }

        if (isEnabled() && mSeekBarEnabled) {
            mSeekBarLeftView.setEnabled(mProgress > DEFAULT_MIN_VALUE);
            mSeekBarRightView.setEnabled(mProgress < getMax());
            mActionView.setEnabled(actualValue != mDefaultValue);
        }
    }

    /**
     * Update seek bar controls according to the current parameters.
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
        super.onUpdate();

        if (mSeekBar != null) {
            mSeekBar.setMax(getMax());

            if (mControls) {
                mSeekBarLeftView.setVisibility(VISIBLE);
                mSeekBarRightView.setVisibility(VISIBLE);
            } else {
                mSeekBarLeftView.setVisibility(GONE);
                mSeekBarRightView.setVisibility(GONE);
            }

            if (getOnActionClickListener() != null) {
                mActionView.setText(getActionString());
                mActionView.setOnClickListener(getOnActionClickListener());
                mActionView.setVisibility(VISIBLE);
            } else {
                mActionView.setVisibility(GONE);
            }

            mSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    mSeekBar.setProgress(mProgress);
                    updateSeekFunctions();
                }
            });
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        if (mSeekBar != null) {
            mSeekBar.setEnabled(enabled && mSeekBarEnabled);
            mSeekBarView.setEnabled(enabled && mSeekBarEnabled);
            mSeekBarLeftView.setEnabled(enabled && mSeekBarEnabled);
            mSeekBarRightView.setEnabled(enabled && mSeekBarEnabled);
            mActionView.setEnabled(enabled && mSeekBarEnabled);
        }
    }

    /**
     * Enable or disable the seek bar.
     *
     * @param seekBarEnabled {@code true} to enable the seek bar.
     */
    public void setSeekBarEnabled(boolean seekBarEnabled) {
        this.mSeekBarEnabled = seekBarEnabled;

        onEnabled(isEnabled());
    }

    /**
     * Returns whether the seek bar is enabled.
     *
     * {@code true} if seek bar is enabled.
     */
    public boolean isSeekBarEnabled() {
        return mSeekBarEnabled;
    }

    /**
     * Get the default value for this preference.
     *
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
     * Get the maximum value for this preference.
     *
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
     * Get the minimum value for this preference.
     *
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
     * Returns the current seek bar progress.
     *
     * @return The current seek bar progress.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Get the current seek bar progress.
     *
     * @param progress The progress to be set.
     */
    public void setProgress(int progress) {
        this.mProgress = progress;

        if (super.getPreferenceKey() != null) {
            DynamicPreferences.getInstance().save(
                    super.getPreferenceKey(), getValueFromProgress());
        } else {
            onUpdate();
        }
    }

    /**
     * Get the seek interval for the seek bar.
     *
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
     * Get the optional unit text for the preference value.
     *
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
     * Returns whether the seek bar seek bar buttons to increase or decrease the value
     * are enabled.
     *
     * @return {@code true} to show seek bar buttons to increase or decrease the value.
     */
    public boolean isControls() {
        return mControls;
    }

    /**
     * Set the seek bar controls to be enabled or disabled.
     *
     * @param controls {@code true} to show seek bar buttons to increase or decrease the value.
     */
    public void setControls(boolean controls) {
        this.mControls = controls;

        onUpdate();
    }

    /**
     * Returns the seek bar change listener to get the callback for seek events.
     *
     * @return The seek bar change listener to get the callback for seek events.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return mOnSeekBarChangeListener;
    }

    /**
     * Set the seek bar change listener to get the callback for seek events.
     *
     * @param onSeekBarChangeListener The listener to be set.
     */
    public void setOnSeekBarChangeListener(
            @Nullable SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    /**
     * Returns the seek bar change listener to get the callback for control events.
     *
     * @return The seek bar change listener to get the callback for control events.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getOnSeekBarControlListener() {
        return mOnSeekBarControlListener;
    }

    /**
     * Set the seek bar change listener to get the callback for control events.
     *
     * @param onSeekBarControlListener The listener to be set.
     */
    public void setOnSeekBarControlListener(
            @Nullable SeekBar.OnSeekBarChangeListener onSeekBarControlListener) {
        this.mOnSeekBarControlListener= onSeekBarControlListener;
    }

    /**
     * Returns the maximum preference value according to the seek interval.
     *
     * @return The maximum preference value according to the seek interval.
     */
    private int getMax() {
        return (mMaxValue - mMinValue) / mSeekInterval;
    }

    /**
     * Returns the seek bar progress according to the supplied value.
     *
     * @param value The value to converted into seek bar progress.
     *
     * @return The seek bar progress according to the supplied value.
     */
    private int getProgressFromValue(int value) {
        return (Math.min(value, mMaxValue) - mMinValue) / mSeekInterval;
    }

    /**
     * Returns the preference value according to the seek bar progress.
     *
     * @return The preference value according to the seek bar progress.
     */
    public int getValueFromProgress() {
        return mMinValue + (mProgress * mSeekInterval);
    }

    /**
     * Get the seek bar to display and modify the preference value.
     *
     * @return The seek bar to display and modify the preference value.
     */
    public AppCompatSeekBar getSeekBar() {
        return mSeekBar;
    }

    /**
     * Get the text view to show the value.
     *
     * @return The text view to show the value.
     */
    public TextView getSeekBarValueView() {
        return mSeekBarView;
    }

    /**
     * Set the color for seek bar and value view.
     *
     * @param color The color to be set.
     */
    public void setColor(@ColorInt int color) {
        ((DynamicSeekBar) mSeekBar).setColor(color);
        ((DynamicTextView) mSeekBarView).setColor(color);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(super.getPreferenceKey())) {
            mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                    .load(super.getPreferenceKey(), mProgress));

            onUpdate();
        }
    }
}
