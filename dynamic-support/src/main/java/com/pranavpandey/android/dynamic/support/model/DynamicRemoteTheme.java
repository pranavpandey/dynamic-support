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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.pranavpandey.android.dynamic.support.model.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;

/**
 * A remote theme to resolve system colors according to the API level.
 */
public class DynamicRemoteTheme extends DynamicWidgetTheme {

    /**
     * Constant for the default system color.
     */
    public static final int SYSTEM_COLOR = Color.WHITE;

    /**
     * Constant for the default system color at night.
     */
    public static final int SYSTEM_COLOR_NIGHT = Color.BLACK;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicRemoteTheme() {
        super();
    }

    /**
     * Constructor to initialize an object of this class from the theme string.
     *
     * @param theme The theme string to initialize the instance.
     */
    public DynamicRemoteTheme(@NonNull String theme) throws JsonSyntaxException {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicRemoteTheme.class,
                        new DynamicThemeTypeAdapter<DynamicRemoteTheme>()).create()
                .fromJson(DynamicThemeUtils.formatTheme(theme), DynamicRemoteTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicAppTheme The dynamic app theme to copy the theme.
     */
    public DynamicRemoteTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        super(dynamicAppTheme);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        if (resolve && super.getPrimaryColorDark(false) == AUTO) {
            return DynamicTheme.getInstance().resolveSystemColor(
                    DynamicTheme.getInstance().isSystemNightMode());
        }

        return super.getPrimaryColorDark(resolve);
    }
}
