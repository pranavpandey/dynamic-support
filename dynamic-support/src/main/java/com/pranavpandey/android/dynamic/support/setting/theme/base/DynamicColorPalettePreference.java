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

import com.google.android.material.color.DynamicColors;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

import java.util.Arrays;

/**
 * A {@link DynamicSpinnerPreference} to display and edit the settings for the dynamic
 * color palette.
 */
public class DynamicColorPalettePreference extends DynamicSpinnerPreference {

    public DynamicColorPalettePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicColorPalettePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorPalettePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        if (getValues() != null && DynamicColors.isDynamicColorAvailable()) {
            setDefaultValue(Arrays.asList(getValues()).indexOf(Theme.Color.ToString.SYSTEM));
            updateValueString(false);
        }
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        setVisibility(DynamicTheme.getInstance().getListener().isDynamicColors() ? VISIBLE : GONE);
    }
}
