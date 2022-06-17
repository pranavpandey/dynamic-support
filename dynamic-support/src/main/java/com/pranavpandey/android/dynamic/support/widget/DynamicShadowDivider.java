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

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * An {@link DynamicImageView} to implement shadow divider.
 */
public class DynamicShadowDivider extends DynamicImageView {

    public DynamicShadowDivider(@NonNull Context context) {
        super(context);
    }

    public DynamicShadowDivider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicShadowDivider(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        super.loadFromAttributes(attrs);

        if (getColorType() == Theme.ColorType.NONE
                && getColor(false) == Theme.Color.UNKNOWN) {
            setColorType(Defaults.ADS_COLOR_TYPE_SYSTEM_SECONDARY);
        }
    }

    @Override
    public void setColor() {
        super.setColor();

        if (!DynamicTheme.getInstance().get().isShowDividers()
                && getContrastWithColor() != Theme.Color.UNKNOWN) {
            setColorFilter(getContrastWithColor(), getFilterMode());
            setVisibility(INVISIBLE);
        } else if (getVisibility() != GONE) {
            setVisibility(VISIBLE);
        }
    }
}
