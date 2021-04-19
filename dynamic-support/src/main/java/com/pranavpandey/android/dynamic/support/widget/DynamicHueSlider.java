/*
 * Copyright 2018-2021 Pranav Pandey
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
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.utils.DynamicPickerUtils;

/**
 * A {@link DynamicSlider} to provide a hue bar for the color picker.
 */
public class DynamicHueSlider extends DynamicSlider {

    private ShapeDrawable mHueDrawable;

    public DynamicHueSlider(@NonNull Context context) {
        this(context, null);
    }

    public DynamicHueSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHueSlider(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Rect bounds = new Rect(getTrackSidePadding(), 0,
                getTrackWidth() + getTrackSidePadding(), h);
        bounds.inset(0, (int) (bounds.height() * Defaults.ADS_INSET_HUE));

        LinearGradient gradient = DynamicPickerUtils.getHueGradient(
                bounds.width(), bounds.height());
        mHueDrawable = new ShapeDrawable(new RectShape());
        mHueDrawable.getPaint().setShader(gradient);
        mHueDrawable.setBounds(bounds);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        mHueDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public void setProgressBarColor() {
        setTrackActiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTrackInactiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTickActiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTickInactiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
    }
}