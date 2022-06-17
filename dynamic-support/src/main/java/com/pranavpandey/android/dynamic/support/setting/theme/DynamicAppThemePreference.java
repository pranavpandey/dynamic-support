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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicThemePreference} to display the app theme setting.
 * <p>It will automatically set the theme type to custom.
 *
 * @see Theme#CUSTOM
 * @see com.pranavpandey.android.dynamic.theme.AppTheme#setType(int)
 */
public class DynamicAppThemePreference extends DynamicThemePreference {

    public DynamicAppThemePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicAppThemePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicAppThemePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public @Nullable DynamicAppTheme getDynamicTheme(@Nullable String theme) {
        return Dynamic.setThemeType(DynamicTheme.getInstance().getTheme(theme), Theme.APP);
    }
}
