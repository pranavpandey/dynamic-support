/*
 * Copyright 2020 Pranav Pandey
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
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.Arrays;

/**
 * A {@link DynamicThemePreference} to display the night theme settings.
 *
 * <p><p>It will automatically adjust the values according to the available API.
 * <p>{@link Theme.Night#BATTERY} will be available for API 21 and above devices.
 * <p>{@link Theme.Night#SYSTEM} will be available for API 28 and above devices.
 */
public class DynamicNightThemePreference extends DynamicThemePreference {

    public DynamicNightThemePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicNightThemePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicNightThemePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setEntries(getResources().getStringArray(R.array.ads_theme_entries_night));
        setValues(getResources().getStringArray(R.array.ads_theme_values_night));

        if (getValues() != null && getPreferenceValue() != null) {
            setDefaultValue(Arrays.asList(getValues()).indexOf(DynamicSdkUtils.is28()
                    ? Theme.Night.ToString.SYSTEM : Theme.Night.ToString.AUTO));

            updateValueString(false);
        }
    }
}
