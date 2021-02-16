/*
 * Copyright 2018-2021 Pranav Pandey
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

import com.pranavpandey.android.dynamic.support.setting.base.DynamicCheckPreference;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * A {@link DynamicCheckPreference} to display the app shortcuts theme setting.
 *
 * <p>It will be available only for API 25 and above devices.
 */
public class ThemeAppShortcutsPreference extends DynamicCheckPreference {

    public ThemeAppShortcutsPreference(@NonNull Context context) {
        super(context);
    }

    public ThemeAppShortcutsPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeAppShortcutsPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setVisibility(DynamicSdkUtils.is25() ? VISIBLE : GONE);
    }
}
