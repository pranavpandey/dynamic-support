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

package com.pranavpandey.android.dynamic.support.tutorial.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorialActivity;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;

/**
 * A tutorial fragment which will be used with the {@link DynamicTutorialActivity} to display
 * a list of fragments in the view pager.
 * <p>Extend this to provide any extra functionality according to the need.
 */
public abstract class DynamicTutorialFragment extends DynamicFragment implements DynamicTutorial {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        onBackgroundColorChanged(getBackgroundColor());
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public int getTutorialId() {
        return -1;
    }

    @Override
    public int getBackgroundColor() {
        return DynamicTheme.getInstance().get().getPrimaryColor();
    }

    @Override
    public void onBackgroundColorChanged(int color) { }

    /**
     * Set drawable for the tutorial image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable for the image view.
     */
    protected void setTutorialImage(@Nullable ImageView imageView, @Nullable Drawable drawable) {
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
    protected void setTutorialText(@Nullable TextView textView, @Nullable String text) {
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
    protected void tintDynamicView(@Nullable DynamicWidget dynamicWidget, @ColorInt int color) {
        if (dynamicWidget != null) {
            dynamicWidget.setColor(color);
        }
    }
}