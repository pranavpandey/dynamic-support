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

package com.pranavpandey.android.dynamic.support.permission;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A model class to hold the permission details that will be used
 * by the app. It will be used internally by the {@link DynamicPermissions}.
 */
public class DynamicPermission implements Parcelable {

    /**
     * The permission string.
     */
    private String permission;

    /**
     * Icon used by this permission.
     */
    private Drawable icon;

    /**
     * Title used by this permission.
     */
    private String title;

    /**
     * Subtitle used by this permission.
     */
    private String subtitle;

    /**
     * Description used by this permission.
     */
    private String description;

    /**
     * {@code true} if the permission is dangerous.
     *
     * @see <a href="https://developer.android.com/guide/topics/permissions/requesting.html#normal-dangerous">
     *      Normal and Dangerous Permissions</a>
     */
    private boolean dangerous;

    /**
     * {@code true} if the permission is granted.
     */
    private boolean granted;

    /**
     * {@code true} if request this permission again.
     *
     * @see android.support.v4.app.ActivityCompat#shouldShowRequestPermissionRationale(Activity, String)
     */
    private boolean askAgain;

    /**
     * Default constructor to initialize the dynamic permission.
     */
    public DynamicPermission() { }

    /**
     * Constructor to initialize dynamic permission with a description.
     */
    public DynamicPermission(@NonNull String permission, @Nullable String description) {
        this.permission = permission;
        this.description = description;
    }

    /**
     * Constructor to initialize dynamic permission with a icon, tile
     * and subtitle.
     */
    public DynamicPermission(@NonNull String permission, @Nullable Drawable icon,
                             @NonNull String title, @Nullable String subtitle) {
        this.permission = permission;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    /**
     * Getter for {@link #permission}.
     */
    public @NonNull String getPermission() {
        return permission;
    }

    /**
     * Setter for {@link #permission}.
     */
    public void setPermission(@NonNull String permission) {
        this.permission = permission;
    }

    /**
     * Getter for {@link #icon}.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Setter for {@link #icon}.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.icon = icon;
    }

    /**
     * Getter for {@link #title}.
     */
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Setter for {@link #title}.
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /**
     * Getter for {@link #subtitle}.
     */
    public @Nullable String getSubtitle() {
        return subtitle;
    }

    /**
     * Setter for {@link #subtitle}.
     */
    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Getter for {@link #description}.
     */
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * Setter for {@link #description}.
     */
    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    /**
     * Getter for {@link #dangerous}.
     */
    public boolean isDangerous() {
        return dangerous;
    }

    /**
     * Setter for {@link #dangerous}.
     */
    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    /**
     * Getter for {@link #granted}.
     */
    public boolean isGranted() {
        return granted;
    }

    /**
     * Setter for {@link #granted}.
     */
    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    /**
     * Getter for {@link #askAgain}.
     */
    public boolean isAskAgain() {
        return askAgain;
    }

    /**
     * Setter for {@link #askAgain}.
     */
    public void setAskAgain(boolean askAgain) {
        this.askAgain = askAgain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permission);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        dest.writeByte((byte) (dangerous ? 1 : 0));
        dest.writeByte((byte) (granted ? 1 : 0));
        dest.writeByte((byte) (askAgain ? 1 : 0));
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        dest.writeParcelable(bitmap, flags);
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DynamicPermission createFromParcel(Parcel in) {
            return new DynamicPermission(in);
        }

        public DynamicPermission[] newArray(int size) {
            return new DynamicPermission[size];
        }
    };

    /**
     * De-parcel {@link DynamicPermission} object.
     */
    public DynamicPermission(Parcel in) {
        this.permission = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.description = in.readString();
        this.dangerous = in.readByte() != 0;
        this.granted = in.readByte() != 0;
        this.askAgain = in.readByte() != 0;
        Bitmap bitmap = in.readParcelable(getClass().getClassLoader());
        this.icon = new BitmapDrawable(Resources.getSystem(), bitmap);
    }
}
