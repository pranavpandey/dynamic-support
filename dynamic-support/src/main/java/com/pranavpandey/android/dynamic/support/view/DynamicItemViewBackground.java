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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView;

/**
 * A {@link DynamicItemView} to provide the background header view with an icon,
 * title and subtitle.
 * <p>It will be used at various locations to show the banners.
 */
public class DynamicItemViewBackground extends DynamicItemView {

    public DynamicItemViewBackground(@NonNull Context context) {
        super(context);
    }

    public DynamicItemViewBackground(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicItemViewBackground(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_item_view_background;
    }

    @Override
    public void setColor() {
        super.setColor();

        final ViewGroup cardView = findViewById(R.id.ads_item_view_card);
        Dynamic.setColorTypeOrColor(cardView, getContrastWithColorType(), getContrastWithColor());

        if (getContrastWithColor() != Dynamic.getColor(cardView, getContrastWithColor())) {
            setContrastWithColor(Dynamic.getColor(cardView, getContrastWithColor()));
        }
    }
}
