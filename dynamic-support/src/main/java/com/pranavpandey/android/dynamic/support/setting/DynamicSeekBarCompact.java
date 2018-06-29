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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPicker;

/**
 * A compact version of the DynamicSeekBarPreference for the
 * {@link DynamicColorPicker}. It can also be used in other 
 * scenarios.
 */
public class DynamicSeekBarCompact extends DynamicSeekBarPreference {

    public DynamicSeekBarCompact(@NonNull Context context) {
        super(context);
    }

    public DynamicSeekBarCompact(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSeekBarCompact(@NonNull Context context,
                                 @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_seek_bar_compact;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setActionButton(null, null);
    }
}
