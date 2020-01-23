/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.tutorial;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;

/**
 * Helper class to do the dynamic tutorial operations.
 */
public class DynamicTutorialUtils {

    /**
     * Set drawable for the tutorial image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable for the image view.
     */
    public static void setImage(@Nullable ImageView imageView, @Nullable Drawable drawable) {
        if (imageView != null) {
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Set text for the tutorial text view and mange its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text for the text view.
     */
    public static void setText(@Nullable TextView textView, @Nullable String text) {
        if (textView != null) {
            if (text != null) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Tint dynamic widgets used by the tutorial according to the supplied color.
     *
     * @param dynamicWidget The dynamic widget to be tinted.
     * @param color The color to tint the widget.
     */
    public static void tintDynamicView(@Nullable DynamicWidget dynamicWidget, @ColorInt int color) {
        if (dynamicWidget != null) {
            dynamicWidget.setColor(color);

            if (dynamicWidget instanceof DynamicScrollableWidget) {
                ((DynamicScrollableWidget) dynamicWidget).setScrollBarColor(color);
            }
        }
    }
}
