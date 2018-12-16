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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.view.DynamicView;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;

/**
 * A DynamicView to show the theme preview according to the selected values.
 */
public abstract class ThemePreview extends DynamicView {

    /**
     * Dynamic app theme used by this preview.
     */
    private DynamicAppTheme mDynamicAppTheme;

    public ThemePreview(@NonNull Context context) {
        super(context);
    }

    public ThemePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemePreview(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        this.mDynamicAppTheme = DynamicTheme.getInstance().get();
    }

    @Override
    protected void onEnabled(boolean enabled) {
        setAlpha(enabled ? WidgetDefaults.ADS_ALPHA_ENABLED : WidgetDefaults.ADS_ALPHA_DISABLED);
    }

    /**
     * Get the dynamic app theme used by this preview.
     *
     * @return The dynamic app theme used by this preview.
     */
    public @NonNull DynamicAppTheme getDynamicAppTheme() {
        return mDynamicAppTheme;
    }

    /**
     * Set the dynamic app theme used by this preview.
     *
     * @param dynamicAppTheme The dynamic app theme to be set.
     */
    public void setDynamicAppTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        this.mDynamicAppTheme = dynamicAppTheme;

        onUpdate();
    }
}
