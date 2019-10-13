/*
 * Copyright 2019 Pranav Pandey
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
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.popup.DynamicArrayPopup;
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;
import com.pranavpandey.android.dynamic.theme.Theme;

import java.util.Arrays;

/**
 * A DynamicPreference to provide the functionality of a
 * {@link android.preference.ListPreference} with single choice.
 */
public class DynamicSpinnerPreference extends DynamicSimplePreference {

    /**
     * Popup type for this preference.
     * <p>Either {@link DynamicPopup.DynamicPopupType#LIST}
     * or {@link DynamicPopup.DynamicPopupType#GRID}.
     */
    private @DynamicPopup.DynamicPopupType int mPopupType;

    /**
     * Array to store list entries.
     */
    private CharSequence[] mEntries;

    /**
     * Array to store values corresponding to the list entries.
     */
    private CharSequence[] mValues;

    /**
     * Default value index for this preference.
     */
    private int mDefaultValue;

    public DynamicSpinnerPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSpinnerPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSpinnerPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicPreference);

        try {
            mEntries = a.getTextArray(R.styleable.DynamicPreference_ads_entries);
            mValues = a.getTextArray(R.styleable.DynamicPreference_ads_values);
            mDefaultValue = a.getInt(R.styleable.DynamicPreference_ads_value,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE);
            mPopupType = a.getInt(R.styleable.DynamicPreference_ads_popupType,
                    DynamicPopup.DynamicPopupType.NONE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        if (getValueView() != null) {
            ((DynamicTextView) getValueView()).setColorType(Theme.ColorType.ACCENT);
        }

        setOnPreferenceClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnPromptListener() != null) {
                    if (getOnPromptListener().onPopup()) {
                        showPopup(v);
                    }
                } else {
                    showPopup(v);
                }
            }
        }, false);

        updateValueString(false);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (getPreferenceView() != null) {
            getPreferenceView().setClickable(
                    getOnPreferenceClickListener() != null && mEntries != null);
        }
    }

    /**
     * Show popup to select a preference value.
     *
     * @param anchor The anchor view for the popup.
     */
    private void showPopup(@NonNull View anchor) {
        DynamicArrayPopup popup = new DynamicArrayPopup(anchor, mEntries,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (!getPreferenceValue().equals(mValues[position].toString())) {
                            setPreferenceValue(mValues[position].toString());
                        }

                        if (getOnPromptListener() != null) {
                            getOnPromptListener().onPopupItemClick(parent, view, position, id);
                        }
                    }
                });

        popup.setTitle(getTitle());
        popup.setSelectedPosition(Arrays.asList(mValues).indexOf(getPreferenceValue()));
        popup.setViewType(mPopupType);
        popup.build().show();
    }

    /**
     * Get the list entries for this preference.
     */
    public @Nullable CharSequence[] getEntries() {
        return mEntries;
    }

    /**
     * Sets the list entries for this preference.
     *
     * @param entries The list entries to be used.
     */
    public void setEntries(@NonNull CharSequence[] entries) {
        this.mEntries = entries;

        updateValueString(true);
    }

    /**
     * Get the list values for this preference.
     */
    public @Nullable CharSequence[] getValues() {
        return mValues;
    }

    /**
     * Sets the list values for this preference.
     *
     * @param values The list values to be used.
     */
    public void setValues(@NonNull CharSequence[] values) {
        this.mValues = values;

        updateValueString(true);
    }

    /**
     * Update value string according to the current preference value.
     *
     * @param update {@code true} to call {@link #onUpdate()} method after setting the
     *               value string.
     */
    public void updateValueString(boolean update) {
        if (mEntries != null && mValues != null) {
            setValueString(mEntries[Arrays.asList(mValues)
                    .indexOf(getPreferenceValue())], update);
        }
    }

    public int getDefaultValue() {
        return mDefaultValue;
    }

    /**
     * Set the default value index for this preference.
     *
     * @param defaultValue The default value index to be set.
     */
    public void setDefaultValue(int defaultValue) {
        this.mDefaultValue = defaultValue;
    }

    /**
     * Returns the default value of this preference.
     *
     * @return The default value of this preference.
     */
    public @Nullable String getPreferenceValue() {
        if (getPreferenceKey() == null) {
            return null;
        }

        return DynamicPreferences.getInstance().load(
                getPreferenceKey(), mValues[mDefaultValue].toString());
    }

    /**
     * Set the value for this preference and save it in the shared preferences.
     *
     * @param value The preference value to be set.
     */
    public void setPreferenceValue(@NonNull String value) {
        if (getPreferenceKey() != null) {
            DynamicPreferences.getInstance().save(getPreferenceKey(), value);
        }

        updateValueString(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getPreferenceKey())) {
            updateValueString(true);
        }
    }
}
