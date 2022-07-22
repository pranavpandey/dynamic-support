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

package com.pranavpandey.android.dynamic.support.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * A {@link FlexboxLayoutManager} to fix {@link androidx.recyclerview.widget.RecyclerView} issues.
 */
public class DynamicFlexboxLayoutManager extends FlexboxLayoutManager {

    public DynamicFlexboxLayoutManager(Context context) {
        super(context);
    }

    public DynamicFlexboxLayoutManager(Context context, int flexDirection) {
        super(context, flexDirection);
    }

    public DynamicFlexboxLayoutManager(Context context, int flexDirection, int flexWrap) {
        super(context, flexDirection, flexWrap);
    }

    public DynamicFlexboxLayoutManager(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
