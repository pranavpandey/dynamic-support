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

package com.pranavpandey.android.dynamic.support.setting.theme;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * A {@link ThemePreference} to display and edit the {@link DynamicAppTheme}.
 */
public class DynamicThemePreference extends ThemePreference<DynamicAppTheme> {
    public DynamicThemePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicThemePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicThemePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_theme;
    }

    @Override
    public @NonNull String getDefaultTheme(@Nullable String theme) {
        if (theme == null) {
            return DynamicTheme.getInstance().get().toJsonString();
        }

        return theme;
    }

    @Override
    public @Nullable DynamicAppTheme getDynamicTheme(@Nullable String theme) {
        return DynamicTheme.getInstance().getTheme(theme);
    }
}
