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

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicImageView} to show the drag indicator according to the {@link DynamicTheme}.
 */
public class DynamicDragIndicator extends DynamicImageView {

    public DynamicDragIndicator(@NonNull Context context) {
        this(context, null);
    }

    public DynamicDragIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicDragIndicator(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize() {
        super.initialize();

        if (DynamicTheme.getInstance().get().getCornerSize() < Theme.Corner.MIN_ROUND) {
            setImageResource(R.drawable.ads_overlay_dim);
        } else if (DynamicTheme.getInstance().get().getCornerSize() < Theme.Corner.MIN_OVAL) {
            setImageResource(R.drawable.ads_overlay_dim_rect);
        } else {
            setImageResource(R.drawable.ads_overlay_dim_round);
        }
    }
}
