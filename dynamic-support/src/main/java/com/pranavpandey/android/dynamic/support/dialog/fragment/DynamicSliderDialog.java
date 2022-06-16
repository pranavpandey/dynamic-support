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

package com.pranavpandey.android.dynamic.support.dialog.fragment;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.slider.Slider;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderChangeListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicValueListener;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;

/**
 * A {@link DynamicDialogFragment} to provide value picker functionality
 * via {@link DynamicSliderPreference}.
 */
public class DynamicSliderDialog extends DynamicDialogFragment {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicSliderDialog";

    /**
     * Key for slider preference value to maintain its state.
     */
    private static final String STATE_PREFERENCE_VALUE = "ads_state_preference_value";

    /**
     * Icon used by the slider preference.
     */
    private Drawable mIcon;

    /**
     * Title used by the slider preference.
     */
    private String mTitle;

    /**
     * Summary used by the slider preference.
     */
    private String mSummary;

    /**
     * Unit used by the slider preference.
     */
    private String mUnit;

    /**
     * Minimum value used by the slider.
     */
    private int mMin;

    /**
     * Maximum value used by the slider.
     */
    private int mMax;

    /**
     * Interval used by the slider.
     */
    private int mInterval;

    /**
     * Value of the slider preference.
     */
    private int mValue;

    /**
     * Slider change listener used by this dialog.
     */
    private DynamicSliderChangeListener<Slider> mOnSliderChangeListener;

    /**
     * Value change listener used by this dialog.
     */
    private DynamicValueListener<Integer> mValueListener;

    /**
     * Slider preference used by this dialog.
     */
    private DynamicSliderPreference mSliderPreference;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return An instance of {@link DynamicSliderDialog}.
     */
    public static @NonNull DynamicSliderDialog newInstance() {
        return new DynamicSliderDialog();
    }

    @Override
    public @LayoutRes int getLayoutRes() {
        return R.layout.ads_dialog_slider;
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(getLayoutRes(),
                new LinearLayout(requireContext()), false);
        mSliderPreference = view.findViewById(R.id.ads_dialog_slider_preference);

        mSliderPreference.setIcon(mIcon);
        mSliderPreference.setTitle(mTitle);
        mSliderPreference.setSummary(mSummary);
        mSliderPreference.setMinValue(mMin);
        mSliderPreference.setMaxValue(mMax);
        mSliderPreference.setSeekInterval(mInterval);
        mSliderPreference.setUnit(mUnit);
        mSliderPreference.setValue(mValue);
        mSliderPreference.setActionButton(null, null);
        mSliderPreference.setControls(true);
        mSliderPreference.setDynamicSliderResolver(mOnSliderChangeListener);

        if (savedInstanceState != null) {
            mSliderPreference.setValue(savedInstanceState.getInt(STATE_PREFERENCE_VALUE));
        }

        dialogBuilder.setNegativeButton(R.string.ads_cancel, null)
                .setPositiveButton(R.string.ads_select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getValueListener() != null) {
                            getValueListener().onValueSelected(TAG,
                                    getValueFromProgress(), getUnit());
                        }
                    }
                });

        return dialogBuilder.setView(view).setViewRoot(
                view.findViewById(R.id.ads_dialog_slider_root));
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_PREFERENCE_VALUE, getValueFromProgress());
    }

    /**
     * Get the icon used by the slider preference.
     *
     * @return The icon used by the slider preference.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon used by the slider preference.
     *
     * @param icon The icon to be set.
     */
    public @NonNull DynamicSliderDialog setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        return this;
    }

    /**
     * Get the title used by the slider preference.
     *
     * @return The title used by the slider preference.
     */
    public @Nullable String getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by the slider preference.
     *
     * @param title The title to be set.
     */
    public @NonNull DynamicSliderDialog setTitle(@Nullable String title) {
        this.mTitle = title;

        return this;
    }

    /**
     * Get the summary used by the slider preference.
     *
     * @return The summary used by the slider preference.
     */
    public @Nullable String getSummary() {
        return mSummary;
    }

    /**
     * Set the summary used by the slider preference.
     *
     * @param summary The summary to be set.
     */
    public @NonNull DynamicSliderDialog setSummary(@Nullable String summary) {
        this.mSummary = summary;

        return this;
    }

    /**
     * Get the minimum value used by the slider preference.
     *
     * @return The minimum value used by the slider preference.
     */
    public int getMin() {
        return mMin;
    }

    /**
     * Set the minimum value used by the slider preference.
     *
     * @param min The minimum value to be set.
     */
    public @NonNull DynamicSliderDialog setMin(int min) {
        this.mMin = min;

        return this;
    }

    /**
     * Get the maximum value used by the slider preference.
     *
     * @return The maximum value used by the slider preference.
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Set the maximum value used by the slider preference.
     *
     * @param max The maximum value to be set.
     */
    public @NonNull DynamicSliderDialog setMax(int max) {
        this.mMax = max;

        return this;
    }

    /**
     * Get the interval used by the slider preference.
     *
     * @return The interval used by the slider preference.
     */
    public int getInterval() {
        return mInterval;
    }

    /**
     * Set the interval used by the slider preference.
     *
     * @param interval The interval to be set.
     */
    public @NonNull DynamicSliderDialog setInterval(int interval) {
        this.mInterval = interval;

        return this;
    }

    /**
     * Get the unit used by the slider preference.
     *
     * @return The unit used by the slider preference.
     */
    public @Nullable String getUnit() {
        return mUnit;
    }

    /**
     * Set the unit used by the slider preference.
     *
     * @param unit  The unit to be set.
     */
    public @NonNull DynamicSliderDialog setUnit(@Nullable String unit) {
        this.mUnit = unit;

        return this;
    }

    /**
     * Get the slider change listener used by this dialog.
     *
     * @return The slider change listener used by this dialog.
     */
    public @Nullable DynamicSliderChangeListener<Slider> getOnSliderChangeListener() {
        return mOnSliderChangeListener;
    }

    /**
     * Set the slider change listener used by this dialog.
     *
     * @param onSliderChangeListener The slider change listener to be set.
     */
    public @NonNull DynamicSliderDialog setOnSliderChangeListener(
            @Nullable DynamicSliderChangeListener<Slider> onSliderChangeListener) {
        this.mOnSliderChangeListener = onSliderChangeListener;

        return this;
    }

    /**
     * Get the value listener used by this dialog.
     *
     * @return The value listener used by this dialog.
     */
    public @Nullable DynamicValueListener<Integer> getValueListener() {
        return mValueListener;
    }

    /**
     * Set the value listener used by this dialog.
     *
     * @param valueListener The slider change listener to be set.
     */
    public @NonNull DynamicSliderDialog setValueListener(
            @Nullable DynamicValueListener<Integer> valueListener) {
        this.mValueListener = valueListener;

        return this;
    }

    /**
     * Get the current value of the slider preference.
     *
     * @return The current value of the slider preference.
     */
    public int getValue() {
        return mValue;
    }

    /**
     * Set the current value of the slider preference.
     *
     * @param value The current value to be set.
     */
    public @NonNull DynamicSliderDialog setValue(int value) {
        this.mValue = value;

        return this;
    }

    /**
     * Set the current value of the slider preference.
     *
     * @param value The current value to be set.
     */
    public @NonNull DynamicSliderDialog setValue(@NonNull Point value) {
        return setValue(Math.max(value.x, value.y));
    }

    /**
     * Get the slider preference used by this dialog.
     *
     * @return The slider preference used by this dialog.
     */
    public @Nullable DynamicSliderPreference getSliderPreference() {
        return mSliderPreference;
    }

    /**
     * Returns the slider progress according to the supplied value.
     *
     * @param value The value to be converted into slider progress.
     *
     * @return The slider progress according to the supplied value.
     */
    public int getProgressFromValue(int value) {
        if (getSliderPreference() != null) {
            return getSliderPreference().getProgressFromValue(value);
        }

        return value;
    }

    /**
     * Returns the preference value according to the slider progress.
     *
     * @return The preference value according to the slider progress.
     */
    public int getValueFromProgress() {
        if (getSliderPreference() != null) {
            return getSliderPreference().getValueFromProgress();
        }

        return getValue();
    }
}
