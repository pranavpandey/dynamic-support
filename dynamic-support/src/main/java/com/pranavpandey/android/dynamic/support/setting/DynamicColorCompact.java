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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference;

/**
 * A compact version of the {@link DynamicColorPreference}.
 */
public class DynamicColorCompact extends DynamicColorPreference {

    public DynamicColorCompact(@NonNull Context context) {
        super(context);
    }

    public DynamicColorCompact(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorCompact(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        Dynamic.setVisibility(getTitleView(), GONE);
        Dynamic.setVisibility(getSummaryView(), GONE);
        Dynamic.setVisibility(getValueView(), GONE);
        Dynamic.setVisibility(getDescriptionView(), GONE);
        Dynamic.setVisibility(getActionView(), GONE);
        Dynamic.setVisibility(getIconFooterView(), GONE);

        Dynamic.setVisibility(getIconView(), VISIBLE);
        Dynamic.setVisibility(getValueView(), VISIBLE);
    }
}
