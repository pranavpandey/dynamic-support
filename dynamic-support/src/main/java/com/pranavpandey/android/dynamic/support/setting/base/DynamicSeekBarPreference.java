/*
 * Copyright 2018-2022 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.setting.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicSeekBarResolver;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;

/**
 * A {@link DynamicSpinnerPreference} to provide the functionality of a seek bar preference with
 * control buttons to modify the value.
 */
public class DynamicSeekBarPreference extends DynamicSpinnerPreference {

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
    public static final boolean DEFAULT_SEEK_CONTROLS = true;

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
     * Seek bar change listener and value resolver to get the various callbacks.
     */
    private SeekBar.OnSeekBarChangeListener mDynamicSeekBarResolver;

    /**
     * Seek bar change listener to get the callback for control events.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarControlListener;

    /**
     * Text view to show the seek bar value.
     */
    private TextView mPreferenceValueView;

    /**
     * Image view to show the decrease button.
     */
    private ImageButton mControlLeftView;

    /**
     * Image view to show the increase button.
     */
    private ImageButton mControlRightView;

    /**
     * Seek bar to display and modify the preference value.
     */
    private AppCompatSeekBar mSeekBar;

    /**
     * {@code true} if seek bar and controls are enabled.
     */
    private boolean mSeekEnabled;

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
                    mMinValue);
            mSeekInterval = a.getInteger(
                    R.styleable.DynamicSeekBarPreference_ads_interval,
                    DEFAULT_SEEK_INTERVAL);
            mControls = a.getBoolean(
                    R.styleable.DynamicSeekBarPreference_ads_controls,
                    DEFAULT_SEEK_CONTROLS);
            mUnit = a.getString(
                    R.styleable.DynamicSeekBarPreference_ads_unit);
            mSeekEnabled = a.getBoolean(
                    R.styleable.DynamicSeekBarPreference_ads_seek_enabled,
                    DEFAULT_SEEK_CONTROLS);
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

        mPreferenceValueView = findViewById(R.id.ads_preference_seek_bar_value);
        mSeekBar = findViewById(R.id.ads_preference_seek_bar_seek);
        mControlLeftView = findViewById(R.id.ads_preference_seek_bar_left);
        mControlRightView = findViewById(R.id.ads_preference_seek_bar_right);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (getDynamicSeekBarResolver() != null) {
                    getDynamicSeekBarResolver().onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mProgress = progress;
                    updateSeekFunctions();
                }

                if (getDynamicSeekBarResolver() != null) {
                    getDynamicSeekBarResolver().onProgressChanged(
                            seekBar, getValueFromProgress(), fromUser);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setProgress(getProgress());

                if (getDynamicSeekBarResolver() != null) {
                    getDynamicSeekBarResolver().onStopTrackingTouch(seekBar);
                }
            }
        });

        mControlLeftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(getProgress() - 1);
            }
        });

        mControlRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(getProgress() + 1);
            }
        });

        setActionButton(getContext().getString(R.string.ads_default), new OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSeekBar(getDefaultValue());
            }
        });

        mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                .load(super.getPreferenceKey(), mDefaultValue));
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return getAltPreferenceKey();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        mProgress = getProgressFromValue(DynamicPreferences.getInstance()
                .load(super.getPreferenceKey(), getValueFromProgress()));

        if (getSeekBar() != null) {
            getSeekBar().setMax(getMax());

            if (isControls()) {
                Dynamic.setVisibility(getControlLeftView(), VISIBLE);
                Dynamic.setVisibility(getControlRightView(), VISIBLE);
            } else {
                Dynamic.setVisibility(getControlLeftView(), GONE);
                Dynamic.setVisibility(getControlRightView(), GONE);
            }

            if (getOnActionClickListener() != null) {
                setTextView(getActionView(), getActionString());
                Dynamic.setClickListener(getActionView(), getOnActionClickListener());
                Dynamic.setVisibility(getActionView(), VISIBLE);
            } else {
                Dynamic.setVisibility(getActionView(), GONE);
            }

            getSeekBar().post(mUpdateRunnable);
        }
    }

    /**
     * Runnable to post the update.
     */
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (getSeekBar() != null) {
                getSeekBar().setProgress(getProgress());
                updateSeekFunctions();
            }
        }
    };

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getSeekBar(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getPreferenceValueView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getControlLeftView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getControlRightView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getActionView(), enabled && isSeekEnabled());
    }

    @Override
    public void setColor(@ColorInt int color) {
        super.setColor(color);

        Dynamic.setColor(getSeekBar(), color);
        Dynamic.setColor(getPreferenceValueView(), color);
    }

    @Override
    public void setColor() {
        super.setColor();

        Dynamic.setContrastWithColorTypeOrColor(getSeekBar(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getPreferenceValueView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getControlLeftView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getControlRightView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getActionView(),
                getContrastWithColorType(), getContrastWithColor());

        Dynamic.setBackgroundAwareSafe(getSeekBar(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getPreferenceValueView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getControlLeftView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getControlRightView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getActionView(), getBackgroundAware());
    }

    /**
     * Enable or disable the seek bar.
     *
     * @param seekEnabled {@code true} to enable the seek bar.
     */
    public void setSeekEnabled(boolean seekEnabled) {
        this.mSeekEnabled = seekEnabled;

        onEnabled(isEnabled());
    }

    /**
     * Returns whether the seek bar is enabled.
     *
     * @return {@code true} if seek bar is enabled.
     */
    public boolean isSeekEnabled() {
        return mSeekEnabled;
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

        update();
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

        update();
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

        update();
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
            update();
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

        update();
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

        update();
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

        update();
    }

    /**
     * Returns the seek bar change listener to get the callback for seek events.
     *
     * @return The seek bar change listener to get the callback for seek events.
     */
    public @Nullable SeekBar.OnSeekBarChangeListener getDynamicSeekBarResolver() {
        return mDynamicSeekBarResolver;
    }

    /**
     * Set the seek bar change listener and value resolver to get the various callbacks.
     *
     * @param dynamicSeekBarResolver The resolver to be set.
     */
    public void setDynamicSeekBarResolver(
            @Nullable SeekBar.OnSeekBarChangeListener dynamicSeekBarResolver) {
        this.mDynamicSeekBarResolver = dynamicSeekBarResolver;
    }

    /**
     * Returns the seek bar change listener and value resolver to get the various callbacks.
     *
     * @return The seek bar change listener and value resolver to get the various callbacks.
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
        return (getMaxValue() - getMinValue()) / getSeekInterval();
    }

    /**
     * Returns the seek bar progress according to the supplied value.
     *
     * @param value The value to converted into seek bar progress.
     *
     * @return The seek bar progress according to the supplied value.
     */
    private int getProgressFromValue(int value) {
        return (Math.min(value, getMaxValue()) - getMinValue()) / getSeekInterval();
    }

    /**
     * Returns the preference value according to the seek bar progress.
     *
     * @return The preference value according to the seek bar progress.
     */
    public int getValueFromProgress() {
        return getMinValue() + (getProgress() * getSeekInterval());
    }

    /**
     * Get the seek bar to display and modify the preference value.
     *
     * @return The seek bar to display and modify the preference value.
     */
    public @Nullable AppCompatSeekBar getSeekBar() {
        return mSeekBar;
    }

    /**
     * Get the text view to show the value.
     *
     * @return The text view to show the value.
     */
    public @Nullable TextView getPreferenceValueView() {
        return mPreferenceValueView;
    }

    /**
     * Get the image view to show the decrease button used by this preference.
     *
     * @return The image view to show the decrease button used by this preference.
     */
    public @Nullable ImageButton getControlLeftView() {
        return mControlLeftView;
    }

    /**
     * Get the image view to show the increase button used by this preference.
     *
     * @return The image view to show the increase button used by this preference.
     */
    public @Nullable ImageButton getControlRightView() {
        return mControlRightView;
    }

    /**
     * Set the preference value after animating the seek bar.
     *
     * @param value The preference value to be set.
     */
    private void animateSeekBar(int value) {
        if (getSeekBar() == null) {
            return;
        }

        final int progress = getProgressFromValue(value);
        ObjectAnimator animation = ObjectAnimator.ofInt(getSeekBar(),
                "progress", getProgress(), progress);
        animation.setDuration(DynamicMotion.Duration.LONG);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
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

    /**
     * Update seek bar functions according to the current parameters.
     */
    private void updateSeekFunctions() {
        int actualValue = getValueFromProgress();

        if (getPreferenceValueView() != null) {
            if (getUnit() != null) {
                setTextView(getPreferenceValueView(), String.format(
                        getContext().getString(R.string.ads_format_blank_space),
                        String.valueOf(actualValue), getUnit()));
            } else {
                setTextView(getPreferenceValueView(), String.valueOf(actualValue));
            }

            if (getDynamicSeekBarResolver() instanceof DynamicSeekBarResolver) {
                setTextView(getPreferenceValueView(),
                        ((DynamicSeekBarResolver) getDynamicSeekBarResolver())
                                .getValueString(getPreferenceValueView().getText(),
                                        getProgress(), actualValue, getUnit()));
            }
        }

        if (isEnabled() && isSeekEnabled()) {
            Dynamic.setEnabled(getControlLeftView(), getProgress() > DEFAULT_MIN_VALUE);
            Dynamic.setEnabled(getControlRightView(), getProgress() < getMax());
            Dynamic.setEnabled(getActionView(), actualValue != getDefaultValue());
        }
    }

    /**
     * Update seek bar controls according to the current parameters.
     */
    private void setProgressFromControl(int progress) {
        if (getOnSeekBarControlListener() != null) {
            getOnSeekBarControlListener().onStartTrackingTouch(getSeekBar());
        }

        setProgress(progress);

        if (getOnSeekBarControlListener() != null) {
            getOnSeekBarControlListener().onProgressChanged(
                    getSeekBar(), getProgress(), true);
            getOnSeekBarControlListener().onStopTrackingTouch(getSeekBar());
        }
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
