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
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderChangeListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderResolver;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;

/**
 * A {@link DynamicSpinnerPreference} to provide the functionality of a slider preference with
 * control buttons to modify the value.
 */
public class DynamicSliderPreference extends DynamicSpinnerPreference {

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
     * Default maximum threshold to enable tick.
     */
    public static final int DEFAULT_MAX_TICK_THRESHOLD = 25;

    /**
     * Minimum value for this preference.
     */
    private int mMinValue;

    /**
     * Maximum value for this preference.
     */
    private int mMaxValue;

    /**
     * Default value for this preference.
     */
    private int mDefaultValue;

    /**
     * The current slider progress.
     */
    private int mProgress;

    /**
     * Seek interval for the slider.
     */
    private int mSeekInterval;

    /**
     * Optional unit text for the preference value.
     */
    private CharSequence mUnit;

    /**
     * {@code true} to show slider buttons to increase or decrease the value.
     */
    private boolean mControls;

    /**
     * Slider change listener and value resolver to get the various callbacks.
     */
    private DynamicSliderChangeListener<Slider> mDynamicSliderResolver;

    /**
     * Slider change listener to get the callback for control events.
     */
    private DynamicSliderChangeListener<Slider> mOnSliderControlListener;

    /**
     * Text view to show the slider value.
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
     * Slider to display and modify the preference value.
     */
    private Slider mSlider;

    /**
     * {@code true} if slider and controls are enabled.
     */
    private boolean mSeekEnabled;

    /**
     * {@code true} to show the slider tick marks.
     */
    private boolean mTickVisible;

    public DynamicSliderPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSliderPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSliderPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicSliderPreference);

        try {
            mMinValue = a.getInt(
                    R.styleable.DynamicSliderPreference_ads_min,
                    DEFAULT_MIN_VALUE);
            mMaxValue = a.getInt(
                    R.styleable.DynamicSliderPreference_ads_max,
                    DEFAULT_MAX_VALUE);
            mDefaultValue = a.getInt(
                    R.styleable.DynamicSliderPreference_ads_progress,
                    mMinValue);
            mSeekInterval = a.getInt(
                    R.styleable.DynamicSliderPreference_ads_interval,
                    DEFAULT_SEEK_INTERVAL);
            mUnit = a.getString(
                    R.styleable.DynamicSliderPreference_ads_unit);
            mControls = a.getBoolean(
                    R.styleable.DynamicSliderPreference_ads_controls,
                    DEFAULT_SEEK_CONTROLS);
            mSeekEnabled = a.getBoolean(
                    R.styleable.DynamicSliderPreference_ads_seek_enabled,
                    DEFAULT_SEEK_CONTROLS);
            mTickVisible = a.getBoolean(
                    R.styleable.DynamicSliderPreference_ads_tick_visible,
                    DEFAULT_SEEK_CONTROLS);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_slider;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mPreferenceValueView = findViewById(R.id.ads_preference_slider_value);
        mSlider = findViewById(R.id.ads_preference_slider_seek);
        mControlLeftView = findViewById(R.id.ads_preference_slider_left);
        mControlRightView = findViewById(R.id.ads_preference_slider_right);

        mSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                if (getDynamicSliderResolver() != null) {
                    getDynamicSliderResolver().onStartTrackingTouch(slider);
                }
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                setProgress(getProgress());

                if (getDynamicSliderResolver() != null) {
                    getDynamicSliderResolver().onStopTrackingTouch(slider);
                }
            }
        });

        mSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (fromUser) {
                    mProgress = (int) value;
                    updateSeekFunctions();
                }

                if (getDynamicSliderResolver() != null) {
                    getDynamicSliderResolver().onProgressChanged(
                            slider, getValueFromProgress(), fromUser);
                }
            }
        });

        mControlLeftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(getProgress() - DEFAULT_SEEK_INTERVAL);
            }
        });

        mControlRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressFromControl(getProgress() + DEFAULT_SEEK_INTERVAL);
            }
        });

        setActionButton(getContext().getString(R.string.ads_default), new OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSlider(getDefaultValue());
            }
        });

        mProgress = getProgressFromValue(DynamicPreferences.getInstance().load(
                getAltPreferenceKey(), mDefaultValue));
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return super.getAltPreferenceKey();
    }

    @Override
    public @Nullable String getAltPreferenceKey() {
        return super.getPreferenceKey();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        mProgress = getProgressFromValue(DynamicPreferences.getInstance().load(
                getAltPreferenceKey(), getValueFromProgress()));

        if (getSlider() != null) {
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

            getSlider().post(mUpdateRunnable);
            updateSeekFunctions();
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getSlider(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getPreferenceValueView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getControlLeftView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getControlRightView(), enabled && isSeekEnabled());
        Dynamic.setEnabled(getActionView(), enabled && isSeekEnabled());
    }

    @Override
    public void setColor(@ColorInt int color) {
        super.setColor(color);

        Dynamic.setColor(getSlider(), color);
        Dynamic.setColor(getPreferenceValueView(), color);
    }

    @Override
    public void setColor() {
        super.setColor();

        Dynamic.setContrastWithColorTypeOrColor(getSlider(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getPreferenceValueView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getControlLeftView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getControlRightView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getActionView(),
                getContrastWithColorType(), getContrastWithColor());

        Dynamic.setBackgroundAwareSafe(getSlider(),
                getBackgroundAware(), getContrast(false));
        Dynamic.setBackgroundAwareSafe(getPreferenceValueView(),
                getBackgroundAware(), getContrast(false));
        Dynamic.setBackgroundAwareSafe(getControlLeftView(),
                getBackgroundAware(), getContrast(false));
        Dynamic.setBackgroundAwareSafe(getControlRightView(),
                getBackgroundAware(), getContrast(false));
        Dynamic.setBackgroundAwareSafe(getActionView(),
                getBackgroundAware(), getContrast(false));
    }

    /**
     * Returns whether the slider is enabled.
     *
     * @return {@code true} if slider is enabled.
     */
    public boolean isSeekEnabled() {
        return mSeekEnabled;
    }

    /**
     * Enable or disable the slider.
     *
     * @param seekEnabled {@code true} to enable the slider.
     */
    public void setSeekEnabled(boolean seekEnabled) {
        this.mSeekEnabled = seekEnabled;

        onEnabled(isEnabled());
    }

    /**
     * Enable or disable the slider tick marks.
     *
     * @param tickVisible {@code true} to enable the slider tick marks.
     */
    public void setTickVisible(boolean tickVisible) {
        this.mTickVisible = tickVisible;

        update();
    }

    /**
     * Returns whether the slider tick marks are enabled.
     *
     * @return {@code true} if slider tick mark are enabled.
     */
    public boolean isTickVisible() {
        return mTickVisible && (getSeekInterval() > DEFAULT_SEEK_INTERVAL
                || (getMax() < DEFAULT_MAX_TICK_THRESHOLD));
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
        this.mMinValue = Math.max(DEFAULT_MIN_VALUE, minValue);

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
        this.mMaxValue = Math.max(DEFAULT_MIN_VALUE, maxValue);

        update();
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
        this.mDefaultValue = Math.max(DEFAULT_MIN_VALUE, defaultValue);

        update();
    }

    /**
     * Returns the current slider progress.
     *
     * @return The current slider progress.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Get the current slider progress.
     *
     * @param progress The progress to be set.
     */
    public void setProgress(int progress) {
        this.mProgress = progress;

        if (getAltPreferenceKey() != null) {
            DynamicPreferences.getInstance().save(getAltPreferenceKey(), getValueFromProgress());
        } else {
            update();
        }
    }

    /**
     * Get the seek interval for the slider.
     *
     * @return The seek interval for the slider.
     */
    public int getSeekInterval() {
        return mSeekInterval;
    }

    /**
     * Set the seek interval for the slider.
     *
     * @param seekInterval The seek interval to be set.
     */
    public void setSeekInterval(int seekInterval) {
        this.mSeekInterval = Math.max(DEFAULT_SEEK_INTERVAL, seekInterval);

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
        setProgress(getProgressFromValue(getValidValue(value)));
    }

    /**
     * Returns the valid value after performing various checks.
     *
     * @param value The value to be checked.
     *
     * @return The valid value after performing various checks.
     */
    public int getValidValue(int value) {
        if (value < getMinValue()) {
            value = getMinValue();
        } else if (value > getMaxValue()) {
            value = getMaxValue();
        }

        return value;
    }

    /**
     * Returns whether the slider slider buttons to increase or decrease the value
     * are enabled.
     *
     * @return {@code true} to show slider buttons to increase or decrease the value.
     */
    public boolean isControls() {
        return mControls;
    }

    /**
     * Set the slider controls to be enabled or disabled.
     *
     * @param controls {@code true} to show slider buttons to increase or decrease the value.
     */
    public void setControls(boolean controls) {
        this.mControls = controls;

        update();
    }

    /**
     * Returns the slider change listener to get the callback for seek events.
     *
     * @return The slider change listener to get the callback for seek events.
     */
    public @Nullable DynamicSliderChangeListener<Slider> getDynamicSliderResolver() {
        return mDynamicSliderResolver;
    }

    /**
     * Set the slider change listener and value resolver to get the various callbacks.
     *
     * @param dynamicSliderResolver The resolver to be set.
     */
    public void setDynamicSliderResolver(
            @Nullable DynamicSliderChangeListener<Slider> dynamicSliderResolver) {
        this.mDynamicSliderResolver = dynamicSliderResolver;
    }

    /**
     * Returns the slider change listener and value resolver to get the various callbacks.
     *
     * @return The slider change listener and value resolver to get the various callbacks.
     */
    public @Nullable DynamicSliderChangeListener<Slider> getOnSliderControlListener() {
        return mOnSliderControlListener;
    }

    /**
     * Set the slider change listener to get the callback for control events.
     *
     * @param onSliderControlListener The listener to be set.
     */
    public void setOnSliderControlListener(
            @Nullable DynamicSliderChangeListener<Slider> onSliderControlListener) {
        this.mOnSliderControlListener = onSliderControlListener;
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
     * Returns the slider progress according to the supplied value.
     *
     * @param value The value to be converted into slider progress.
     *
     * @return The slider progress according to the supplied value.
     */
    public int getProgressFromValue(int value) {
        return (Math.min(value, getMaxValue()) - getMinValue()) / getSeekInterval();
    }

    /**
     * Returns the preference value according to the slider progress.
     *
     * @return The preference value according to the slider progress.
     */
    public int getValueFromProgress() {
        return getMinValue() + (getProgress() * getSeekInterval());
    }

    /**
     * Get the slider to display and modify the preference value.
     *
     * @return The slider to display and modify the preference value.
     */
    public @Nullable Slider getSlider() {
        return mSlider;
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
     * Set the preference value after animating the slider.
     *
     * @param value The preference value to be set.
     */
    public void animateSlider(int value) {
        if (getSlider() == null) {
            return;
        }

        final int progress = getProgressFromValue(getValidValue(value));
        ValueAnimator animation = ValueAnimator.ofInt(getProgress(), progress);
        animation.setDuration(DynamicMotion.Duration.LONG);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getSlider().setValue((int) animation.getAnimatedValue());
            }
        });
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
     * Update slider functions according to the current parameters.
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

            if (getDynamicSliderResolver() instanceof DynamicSliderResolver) {
                setTextView(getPreferenceValueView(),
                        ((DynamicSliderResolver<Slider>) getDynamicSliderResolver())
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
     * Update slider controls according to the current parameters.
     */
    private void setProgressFromControl(int progress) {
        if (getOnSliderControlListener() != null) {
            getOnSliderControlListener().onStartTrackingTouch(getSlider());
        }

        setProgress(progress);

        if (getOnSliderControlListener() != null) {
            getOnSliderControlListener().onProgressChanged(
                    getSlider(), getProgress(), true);
            getOnSliderControlListener().onStopTrackingTouch(getSlider());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            @Nullable String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(getAltPreferenceKey())) {
            update();
        }
    }

    /**
     * Runnable to post the update.
     */
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (getSlider() != null) {
                getSlider().setValue(getProgress());
                getSlider().setValueTo(getMax());
                getSlider().setStepSize(DEFAULT_SEEK_INTERVAL);
                getSlider().setTickVisible(isTickVisible());
                updateSeekFunctions();
            }
        }
    };
}
