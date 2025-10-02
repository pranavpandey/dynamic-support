/*
 * Copyright 2018-2024 Pranav Pandey
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
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.util.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

/**
 * A {@link DynamicSlider} to provide a hue bar for the color picker.
 */
public class DynamicHueSlider extends DynamicSlider {

    private GradientDrawable mHueDrawable;

    public DynamicHueSlider(@NonNull Context context) {
        super(context);
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

        final Rect bounds = new Rect(getTrackSidePadding(), 0,
                getTrackWidth() + getTrackSidePadding(), h);
        mHueDrawable = new GradientDrawable(
                DynamicViewUtils.isLayoutRtl(this)
                        ? GradientDrawable.Orientation.RIGHT_LEFT
                        : GradientDrawable.Orientation.LEFT_RIGHT,
                DynamicPickerUtils.getHueColors());

        if (DynamicSdkUtils.is18()) {
            bounds.inset((int) -(getTrackSidePadding() / 8f),
                    (int) ((bounds.height() - getTrackHeight()) / 2f));
            mHueDrawable.setCornerRadius(bounds.height() / 2f);
        } else {
            bounds.inset(0, (int) ((bounds.height() - getTrackHeight()) / 2f));
        }

        mHueDrawable.setShape(GradientDrawable.RECTANGLE);
        mHueDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mHueDrawable.setSize(bounds.width(), bounds.height());
        mHueDrawable.setBounds(bounds);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        mHueDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public void setProgressBarColor() {
        setTrackStopIndicatorSize(Theme.Corner.MIN);
        setTrackInsideCornerSize(Theme.Corner.MIN);
        setThumbTrackGapSize(Theme.Corner.MIN);

        setTrackActiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTrackInactiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTickActiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setTickInactiveTintList(ColorStateList.valueOf(Color.TRANSPARENT));
    }
}