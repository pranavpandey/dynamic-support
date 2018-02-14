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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.popup.DynamicArrayPopup;
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;

import java.util.Arrays;

/**
 * A DynamicPreference to provide the functionality of a
 * {@link android.preference.ListPreference} with single choice.
 */
public class DynamicSpinnerPreference extends DynamicSimplePreference {

    /**
     * Popup type for this preference.
     * Either {@link DynamicPopup.DynamicPopupType#LIST}
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
     * Default value for the preference.
     */
    private int mDefaultValue;

    public DynamicSpinnerPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSpinnerPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSpinnerPreference(@NonNull Context context,
                                    @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicPreference);

        try {
            mEntries = a.getTextArray(R.styleable.DynamicPreference_ads_dynamicPreference_entries);
            mValues = a.getTextArray(R.styleable.DynamicPreference_ads_dynamicPreference_values);
            mDefaultValue = a.getInt(R.styleable.DynamicPreference_ads_dynamicPreference_value, 0);
            mPopupType = a.getInt(R.styleable.DynamicPreference_ads_dynamicPreference_popupType,
                    DynamicPopup.DynamicPopupType.NONE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        ((DynamicTextView) getValueView()).setColorType(DynamicColorType.ACCENT);

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
        });

        updateValueString();
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
     * Update value string according to the current preference
     * value.
     */
    public void updateValueString() {
        if (mEntries != null) {
            setValueString(mEntries[Arrays.asList(mValues).indexOf(getPreferenceValue())]);
        }
    }

    /**
     * Set the value for this preference and save it in the
     * shared preferences.
     *
     * @param value The preference value.
     */
    public void setPreferenceValue(@NonNull String value) {
        if (getPreferenceKey() != null) {
            DynamicPreferences.getInstance().savePrefs(getPreferenceKey(), value);
            updateValueString();
        }
    }

    /**
     * @return The current value of this preference.
     */
    public @Nullable String getPreferenceValue() {
        if (getPreferenceKey() == null) {
            return null;
        }

        return DynamicPreferences.getInstance().loadPrefs(
                getPreferenceKey(), mValues[mDefaultValue].toString());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getPreferenceKey())) {
            updateValueString();
        }
    }
}
