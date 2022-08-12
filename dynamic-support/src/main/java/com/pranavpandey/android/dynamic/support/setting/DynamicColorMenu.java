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
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;

/**
 * An {@link android.view.MenuItem} version of the {@link DynamicColorCompact}.
 */
public class DynamicColorMenu extends DynamicColorCompact {

    public DynamicColorMenu(@NonNull Context context) {
        super(context);
    }

    public DynamicColorMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorMenu(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_simple_compact;
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        Dynamic.setVisibility(getValueView(), GONE);

        if (getIcon() != null) {
            Dynamic.setVisibility(getIconView(), VISIBLE);
            Dynamic.setVisibility(getViewFrame(), GONE);
        } else {
            Dynamic.setVisibility(getIconView(), GONE);
            Dynamic.setVisibility(getViewFrame(), VISIBLE);
        }
    }
}
