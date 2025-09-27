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

package com.pranavpandey.android.dynamic.support.setting.theme.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * A {@link DynamicSpinnerPreference} to display the theme styles version.
 *
 * @see DynamicTheme.Version
 */
public class DynamicThemeVersionPreference extends DynamicSpinnerPreference {

    public DynamicThemeVersionPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicThemeVersionPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicThemeVersionPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setPreferenceKey(DynamicTheme.Version.KEY);
        setEntries(getResources().getStringArray(R.array.ads_dynamic_theme_version_entries));
        setValues(getResources().getStringArray(R.array.ads_dynamic_theme_version_values));
    }
}
