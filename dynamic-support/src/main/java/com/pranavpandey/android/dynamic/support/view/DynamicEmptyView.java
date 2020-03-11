/*
 * Copyright 2018-2020 Pranav Pandey
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

/*
 * Copyright 2018-2020 Pranav Pandey
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

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicHeader} to provide the empty view with an icon, title and subtitle.
 */
public class DynamicEmptyView extends DynamicHeader {

    public DynamicEmptyView(@NonNull Context context) {
        super(context);
    }

    public DynamicEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicEmptyView(@NonNull Context context,
            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_empty_view;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        setColorType(Theme.ColorType.PRIMARY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (getIconView() != null) {
            ((DynamicWidget) getIconView()).setColorType(getColorType());

            if (getColor() != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                ((DynamicWidget) getIconView()).setColor(getColor());
            }
        }

        if (getTitleView() != null) {
            ((DynamicWidget) getTitleView()).setColorType(Theme.ColorType.TINT_BACKGROUND);
        }

        if (getSubtitleView() != null) {
            ((DynamicWidget) getSubtitleView()).setColorType(Theme.ColorType.TINT_BACKGROUND);
        }
    }
}
