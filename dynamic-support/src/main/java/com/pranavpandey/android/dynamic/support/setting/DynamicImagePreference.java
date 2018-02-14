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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

import static com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE;

/**
 * A DynamicSimplePreference with a secondary image view to
 * represent data according to the need.
 */
public class DynamicImagePreference extends DynamicSimplePreference {

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
                                  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicPreference);

        try {
            mImageDrawable = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicPreference_ads_dynamicPreference_image,
                    ADS_DEFAULT_RESOURCE_VALUE));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mImageView = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_image, this, false)
                .findViewById(R.id.ads_preference_image_big);
        setViewFrame(mImageView, true);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (getImageDrawable() != null) {
            mImageView.setImageDrawable(getImageDrawable());
            mImageView.setVisibility(VISIBLE);
        } else {
            mImageView.setVisibility(GONE);
        }
    }

    /**
     * Getter for {@link #mImageView}.
     */
    public ImageView getImageView() {
        return mImageView;
    }

    /**
     * Getter for {@link #mImageDrawable}.
     */
    public @Nullable Drawable getImageDrawable() {
        return mImageDrawable;
    }

    /**
     * Setter for {@link #mImageDrawable}.
     */
    public void setImageDrawable(@Nullable Drawable imageDrawable) {
        this.mImageDrawable = imageDrawable;

        onUpdate();
    }

    /**
     * Setter for {@link #mImageDrawable} via its resource id.
     */
    public void setImageResource(@DrawableRes int drawableRes) {
        setImageDrawable(DynamicResourceUtils.getDrawable(getContext(), drawableRes));
    }
}
