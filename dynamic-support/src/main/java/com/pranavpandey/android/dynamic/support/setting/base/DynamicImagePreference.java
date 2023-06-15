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

package com.pranavpandey.android.dynamic.support.setting.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;

/**
 * A {@link DynamicSpinnerPreference} with a secondary image view to represent data according
 * to the need.
 */
public class DynamicImagePreference extends DynamicSpinnerPreference {

    /**
     * Secondary image view to show the drawable.
     */
    private ImageView mImageView;

    /**
     * Drawable for the image view.
     */
    private Drawable mImageDrawable;

    public DynamicImagePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicImagePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImagePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicImagePreference);

        try {
            mImageDrawable = DynamicResourceUtils.getDrawable(getContext(),
                    a.getResourceId(
                            R.styleable.DynamicImagePreference_ads_image,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mImageView = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_image, getViewFrame(), true)
                .findViewById(R.id.ads_preference_image_value);
        setViewFrame(mImageView, true);
        Dynamic.setColorType(getValueView(), Theme.ColorType.NONE);
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return super.getAltPreferenceKey();
    }

    @Override
    public @Nullable String getAltPreferenceKey() {
        return super.getPreferenceKey();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        Dynamic.set(getImageView(), getImageDrawable());
        Dynamic.setClickable(getPreferenceView(), getOnPreferenceClickListener() != null);
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getImageView(), enabled);
    }

    @Override
    public void setColor() {
        super.setColor();

        Dynamic.setContrastWithColorTypeOrColor(getImageView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setBackgroundAwareSafe(getImageView(),
                getBackgroundAware(), getContrast(false));
    }

    /**
     * Get the secondary image view to show the drawable.
     *
     * @return The secondary image view to show the drawable.
     */
    public @Nullable ImageView getImageView() {
        return mImageView;
    }

    /**
     * Get the drawable for the image view.
     *
     * @return The drawable for the image view.
     */
    public @Nullable Drawable getImageDrawable() {
        return mImageDrawable;
    }

    /**
     * Set the drawable for the image view.
     *
     * @param imageDrawable The image drawable to be set.
     * @param update {@code true} to call {@link #update()} method after setting the
     *               image drawable.
     */
    public void setImageDrawable(@Nullable Drawable imageDrawable, boolean update) {
        this.mImageDrawable = imageDrawable;

        if (update) {
            update();
        } else {
            Dynamic.set(getImageView(), getImageDrawable());
        }
    }

    /**
     * Set the drawable for the image view.
     *
     * @param imageDrawable The image drawable to be set.
     */
    public void setImageDrawable(@Nullable Drawable imageDrawable) {
        setImageDrawable(imageDrawable, true);
    }

    /**
     * Set the drawable for the image view.
     *
     * @param drawableRes The drawable resource to be set.
     * @param update {@code true} to call {@link #update()} method after setting the
     *               image resource.
     */
    public void setImageResource(@DrawableRes int drawableRes, boolean update) {
        setImageDrawable(DynamicResourceUtils.getDrawable(getContext(), drawableRes), update);
    }

    /**
     * Set the drawable for the image view.
     *
     * @param drawableRes The drawable resource to be set.
     */
    public void setImageResource(@DrawableRes int drawableRes) {
        setImageResource(drawableRes, true);
    }

    /**
     * Set the bitmap for the image view.
     *
     * @param imageBitmap The image bitmap to be set.
     * @param update {@code true} to call {@link #update()} method after setting the
     *               image bitmap.
     */
    public void setImageBitmap(@Nullable Bitmap imageBitmap, boolean update) {
        try {
            setImageDrawable(new BitmapDrawable(DynamicBitmapUtils.resizeBitmap(
                    imageBitmap, Theme.Size.SMALL, Theme.Size.SMALL)), update);
        } catch (Exception e) {
            e.getStackTrace();

            setImageDrawable(null);
        }
    }

    /**
     * Set the bitmap for the image view.
     *
     * @param imageBitmap The image bitmap to be set.
     */
    public void setImageBitmap(@Nullable Bitmap imageBitmap) {
        setImageBitmap(imageBitmap, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            @Nullable String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(getAltPreferenceKey())) {
            update();
        }
    }
}
