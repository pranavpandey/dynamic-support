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

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;

/**
 * A model class to hold the dynamic information which can be used by the {@link DynamicInfoView}.
 */
public class DynamicInfo implements Parcelable {

    /**
     * Icon used by this info.
     */
    private Drawable icon;

    /**
     * Big fallback icon used by this info.
     */
    private Drawable iconBig;

    /**
     * Title used by this info.
     */
    private CharSequence title;

    /**
     * Subtitle used by this info.
     */
    private CharSequence subtitle;

    /**
     * Description used by this info.
     */
    private CharSequence description;

    /**
     * Status used by this info.
     */
    private CharSequence status;

    /**
     * Title for the links used by this info.
     */
    private CharSequence[] links;

    /**
     * Subtitle for the links used by this info.
     */
    private CharSequence[] linksSubtitles;

    /**
     * Url for the links used by this info.
     */
    private CharSequence[] linksUrls;

    /**
     * Icon drawables array resource for the links used by this info.
     */
    private @ArrayRes int linksIconsResId;

    /**
     * Icon tint colors array resource for the links used by this info.
     */
    private @ArrayRes int linksColorsResId;

    /**
     * Icon drawable for the links used by this info.
     */
    private Drawable[] linksDrawables;

    /**
     * Icon tint color for the links used by this info.
     */
    private Integer[] linksColors;

    /**
     * Default constructor to initialize the dynamic permission.
     */
    public DynamicInfo() { }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicInfo(@NonNull Parcel in) {
        this.icon = DynamicResourceUtils.getDrawable(null,
                (Bitmap) in.readParcelable(getClass().getClassLoader()));
        this.iconBig = DynamicResourceUtils.getDrawable(null,
                (Bitmap) in.readParcelable(getClass().getClassLoader()));
        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.description = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.status = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.linksIconsResId = in.readInt();
        this.linksColorsResId = in.readInt();
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicInfo> CREATOR =
            new Parcelable.Creator<DynamicInfo>() {
        @Override
        public DynamicInfo createFromParcel(Parcel in) {
            return new DynamicInfo(in);
        }

        @Override
        public DynamicInfo[] newArray(int size) {
            return new DynamicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(DynamicBitmapUtils.getBitmap(icon), flags);
        TextUtils.writeToParcel(title, dest, flags);
        TextUtils.writeToParcel(subtitle, dest, flags);
        TextUtils.writeToParcel(description, dest, flags);
        TextUtils.writeToParcel(status, dest, flags);
        dest.writeInt(linksIconsResId);
        dest.writeInt(linksColorsResId);
    }

    /**
     * Get the icon used by this info.
     *
     * @return The icon used by this info.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Set the icon for this info.
     *
     * @param icon The icon to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Get the big fallback icon used by this info.
     *
     * @return The big fallback icon used by this info.
     */
    public @Nullable Drawable getIconBig() {
        return iconBig;
    }

    /**
     * Set the big fallback icon for this info.
     * 
     * @param iconBig The big fallback icon to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setIconBig(@Nullable Drawable iconBig) {
        this.iconBig = iconBig;

        return this;
    }

    /**
     * Get the title used by this info.
     *
     * @return The title used by this info.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Set the title for this info.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Get the subtitle used by this info.
     *
     * @return The subtitle used by this info.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle for this info.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Get the description used by this info.
     *
     * @return The description used by this info.
     */
    public @Nullable CharSequence getDescription() {
        return description;
    }

    /**
     * Set the description for this info.
     *
     * @param description The description to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setDescription(@Nullable CharSequence description) {
        this.description = description;

        return this;
    }

    /**
     * Get the status used by this info.
     *
     * @return The status used by this info.
     */
    public @Nullable CharSequence getStatus() {
        return status;
    }

    /**
     * Set the status for this info.
     *
     * @param status The status to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setStatus(@Nullable CharSequence status) {
        this.status = status;

        return this;
    }

    /**
     * Get the title for the links used by this info.
     *
     * @return The title for the links used by this info.
     */
    public @Nullable CharSequence[] getLinks() {
        return links;
    }

    /**
     * Set the title for the links used by this info.
     *
     * @param links The titles for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinks(@Nullable CharSequence[] links) {
        this.links = links;

        return this;
    }

    /**
     * Get the subtitle for the links used by this info.
     *
     * @return The subtitle for the links used by this info.
     */
    public @Nullable CharSequence[] getLinksSubtitles() {
        return linksSubtitles;
    }

    /**
     * Set the subtitle for the links used by this info.
     *
     * @param linksSubtitles The subtitles for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksSubtitles(@Nullable CharSequence[] linksSubtitles) {
        this.linksSubtitles = linksSubtitles;

        return this;
    }

    /**
     * Get the URL for the links used by this info.
     *
     * @return The URL for the links used by this info.
     */
    public @Nullable CharSequence[] getLinksUrls() {
        return linksUrls;
    }

    /**
     * Set the URL for the links used by this info.
     *
     * @param linksUrls The urls for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksUrls(@Nullable CharSequence[] linksUrls) {
        this.linksUrls = linksUrls;

        return this;
    }

    /**
     * Get the icons array resource for the links used by this info.
     *
     * @return The icons array resource for the links used by this info.
     */
    public @ArrayRes int getLinksIconsResId() {
        return linksIconsResId;
    }

    /**
     * Set the icons array resource for the links used by this info.
     *
     * @param linksIconsResId The icon drawables array resource for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksIconsResId(@ArrayRes int linksIconsResId) {
        this.linksIconsResId = linksIconsResId;

        return this;
    }

    /**
     * Get the icon tint colors array resource for the links used by this info.
     *
     * @return The icon tint colors array resource for the links used by this info.
     */
    public @ArrayRes int getLinksColorsResId() {
        return linksColorsResId;
    }

    /**
     * Set the icon tint colors array resource for the links used by this info.
     *
     * @param linksColorsResId The icon tint colors array resource for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksColorsResId(@ArrayRes int linksColorsResId) {
        this.linksColorsResId = linksColorsResId;

        return this;
    }

    /**
     * Get the icon for the links used by this info.
     *
     * @return The icon for the links used by this info.
     */
    public @Nullable Drawable[] getLinksDrawables() {
        return linksDrawables;
    }

    /**
     * Set the icon for the links used by this info.
     *
     * @param linksDrawables The icon drawables for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksDrawables(@Nullable Drawable[] linksDrawables) {
        this.linksDrawables = linksDrawables;

        return this;
    }

    /**
     * Get the icon tint color for the links used by this info.
     *
     * @return The icon tint color for the links used by this info.
     */
    public @Nullable Integer[] getLinksColors() {
        return linksColors;
    }

    /**
     * Set the icon tint color for the links used by this info.
     *
     * @param linksColors The icon tint color for the links to be set.
     *
     * @return The {@link DynamicInfo} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicInfo setLinksColors(@Nullable Integer[] linksColors) {
        this.linksColors = linksColors;

        return this;
    }
}
