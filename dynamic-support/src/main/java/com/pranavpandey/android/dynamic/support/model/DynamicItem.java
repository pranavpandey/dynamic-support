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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;

/**
 * A model class to hold the dynamic item information which can be used by the
 * {@link DynamicItemView}.
 */
public class DynamicItem implements Parcelable {

    /**
     * Icon tint color type used by this item.
     */
    private @Theme.ColorType int colorType;

    /**
     * Background color type for this item so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int contrastWithColorType;

    /**
     * Icon tint color used by this item.
     */
    private @ColorInt int color;

    /**
     * Background color for this item so that it will remain in contrast with this color.
     */
    private @ColorInt int contrastWithColor;

    /**
     * The background aware functionality to change this item color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #contrastWithColor
     */
    private @Theme.BackgroundAware int backgroundAware;

    /**
     * Minimum contrast value to generate contrast color for the background aware functionality.
     */
    protected int contrast;
    
    /**
     * Icon used by this item.
     */
    private Drawable icon;

    /**
     * Drawable for the image view.
     */
    private Drawable imageDrawable;

    /**
     * Title used by this item.
     */
    private CharSequence title;

    /**
     * Subtitle used by this item.
     */
    private CharSequence subtitle;

    /**
     * {@code true} to show horizontal divider. 
     * <p>Useful to display in a list view.
     */
    private boolean showDivider;

    /**
     * Click listener used by this item.
     */
    private View.OnClickListener onClickListener;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicItem() {
        this(null, null, null, Theme.Color.UNKNOWN,
                Theme.ColorType.CUSTOM, false);
    }

    /**
     * Constructor to initialize an object of this class.
     * 
     * @param icon The icon for this item.
     * @param title The title for this item.
     * @param subtitle The subtitle for this item.
     * @param color The icon tint color for this item.
     * @param colorType The icon tint color type for this item.
     * @param showDivider {@code true} to show horizontal divider.
     */
    public DynamicItem(@Nullable Drawable icon, @Nullable CharSequence title,
            @Nullable CharSequence subtitle, int color,
            @Theme.ColorType int colorType, boolean showDivider) {
        this(icon, null, title, subtitle, color, colorType, showDivider);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icon The icon for this item.
     * @param imageDrawable The drawable for the image view.
     * @param title The title for this item.
     * @param subtitle The subtitle for this item.
     * @param color The icon tint color for this item.
     * @param colorType The icon tint color type for this item.
     * @param showDivider {@code true} to show horizontal divider.
     */
    public DynamicItem(@Nullable Drawable icon, @Nullable Drawable imageDrawable,
            @Nullable CharSequence title, @Nullable CharSequence subtitle, int color,
            @Theme.ColorType int colorType, boolean showDivider) {
        this.icon = icon;
        this.imageDrawable = imageDrawable;
        this.title = title;
        this.subtitle = subtitle;
        this.color = color;
        this.colorType = colorType;
        this.showDivider = showDivider;

        this.contrastWithColorType = Theme.ColorType.NONE;
        this.contrastWithColor = Theme.Color.UNKNOWN;
        this.backgroundAware = Theme.BackgroundAware.UNKNOWN;
        this.contrast = Theme.Contrast.UNKNOWN;
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicItem(@NonNull Parcel in) {
        this.colorType = in.readInt();
        this.contrastWithColorType = in.readInt();
        this.color = in.readInt();
        this.contrastWithColor = in.readInt();
        this.backgroundAware = in.readInt();
        this.contrast = in.readInt();
        this.icon = DynamicResourceUtils.getDrawable(null,
                (Bitmap) in.readParcelable(getClass().getClassLoader()));
        this.imageDrawable = DynamicResourceUtils.getDrawable(null,
                (Bitmap) in.readParcelable(getClass().getClassLoader()));
        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.showDivider = in.readByte() != 0;
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicItem> CREATOR =
            new Parcelable.Creator<DynamicItem>() {
        @Override
        public DynamicItem createFromParcel(Parcel in) {
            return new DynamicItem(in);
        }

        @Override
        public DynamicItem[] newArray(int size) {
            return new DynamicItem[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(colorType);
        dest.writeInt(contrastWithColorType);
        dest.writeInt(color);
        dest.writeInt(contrastWithColor);
        dest.writeInt(backgroundAware);
        dest.writeInt(contrast);
        dest.writeParcelable(DynamicBitmapUtils.getBitmap(icon), flags);
        dest.writeParcelable(DynamicBitmapUtils.getBitmap(imageDrawable), flags);
        TextUtils.writeToParcel(title, dest, flags);
        TextUtils.writeToParcel(subtitle, dest, flags);
        dest.writeByte((byte) (showDivider ? 1 : 0));
    }

    /**
     * Get the icon tint color type used by this item.
     *
     * @return The icon tint color type used by this item.
     *
     * @see Theme.ColorType
     */
    public @Theme.ColorType int getColorType() {
        return colorType;
    }

    /**
     * Set the icon tint color type used by this item.
     *
     * @param colorType The icon tint color type to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     *
     * @see Theme.ColorType
     */
    public @NonNull DynamicItem setColorType(@Theme.ColorType int colorType) {
        this.colorType = colorType;

        return this;
    }

    /**
     * Get the contrast with color type used by this item.
     *
     * @return The contrast with color type used by this item.
     *
     * @see Theme.ColorType
     */
    public @Theme.ColorType int getContrastWithColorType() {
        return contrastWithColorType;
    }

    /**
     * Set the contrast with color type used by this item.
     *
     * @param contrastWithColorType The contrast with color type to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     *
     * @see Theme.ColorType
     */
    public @NonNull DynamicItem setContrastWithColorType(
            @Theme.ColorType int contrastWithColorType) {
        this.contrastWithColorType = contrastWithColorType;

        return this;
    }

    /**
     * Get the icon tint color used by this item.
     *
     * @return The icon tint color used by this item.
     */
    public @ColorInt int getColor() {
        return color;
    }

    /**
     * Set the icon tint color used by this item.
     *
     * @param color The icon tint color to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setColor(@ColorInt int color) {
        this.colorType = Theme.ColorType.CUSTOM;
        this.color = color;

        return this;
    }

    /**
     * Get the contrast with color used by this item.
     *
     * @return The contrast with color used by this item.
     */
    public @ColorInt int getContrastWithColor() {
        return contrastWithColor;
    }

    /**
     * Set the contrast with color used by this item.
     *
     * @param contrastWithColor The contrast with color to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setContrastWithColor(@ColorInt int contrastWithColor) {
        this.contrastWithColorType = Theme.ColorType.CUSTOM;
        this.contrastWithColor = contrastWithColor;

        return this;
    }

    /**
     * Returns the background aware functionality used by this item.
     *
     * @return The background aware functionality used by this item.
     */
    public @Theme.BackgroundAware int getBackgroundAware() {
        return backgroundAware;
    }

    /**
     * Checks whether the background aware functionality is enabled.
     *
     * @return {@code true} if this item changes color according to the background.
     */
    public boolean isBackgroundAware() {
        return Dynamic.isBackgroundAware(this);
    }

    /**
     * Set the value to make this item background aware or not.
     *
     * @param backgroundAware The background aware functionality to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        this.backgroundAware = backgroundAware;

        return this;
    }

    /**
     * Get the contrast value used by this item.
     *
     * @param resolve {@code true} to resolve auto contrast.
     *
     * @return The contrast value used by this item.
     */
    public int getContrast(boolean resolve) {
        if (resolve) {
            return Dynamic.getContrast(this);
        }

        return contrast;
    }

    /**
     * Get the contrast value used by this item.
     *
     * @return The contrast value used by this item.
     */
    public int getContrast() {
        return getContrast(true);
    }

    /**
     * Returns the contrast ratio for by this item.
     *
     * @return The contrast ratio for by this item.
     */
    public float getContrastRatio() {
        return getContrast() / (float) Theme.Contrast.MAX;
    }

    /**
     * Set the contrast value used for this item.
     *
     * @param contrast The contrast value to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setContrast(int contrast) {
        this.contrast = contrast;

        return this;
    }

    /**
     * Get the icon used by this item.
     *
     * @return The icon used by this item.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Set the icon used by this item.
     * 
     * @param icon The icon to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Get the image drawable used by this item.
     *
     * @return The image drawable used by this item.
     */
    public @Nullable Drawable getImageDrawable() {
        return imageDrawable;
    }

    /**
     * Set the image drawable used by this item.
     *
     * @param imageDrawable The image drawable to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setImageDrawable(@Nullable Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;

        return this;
    }

    /**
     * Get the title used by this item.
     *
     * @return The title used by this item.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Set the title used by this item.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Get the subtitle used by this item.
     *
     * @return The subtitle used by this item.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle used by this item.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Returns whether to show the horizontal divider for this item.
     *
     * @return {@code true} to show horizontal divider. 
     *         <p>Useful to display in a list view.
     */
    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * Set the horizontal divider fro this item.
     * <p>Useful to display in a list view.
     *
     * @param showDivider {@code true} to show horizontal divider.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;

        return this;
    }

    /**
     * Get the on click listener used by this item.
     *
     * @return The on click listener used by this item.
     */
    public @Nullable View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    /**
     * Set the on click listener used by this item.
     *
     * @param onClickListener The on click listener to be set.
     *
     * @return The {@link DynamicItem} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicItem setOnClickListener(
            @Nullable View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        return this;
    }
}
