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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * A model class to hold the permission details that will be used by the app.
 * <p>It will be used internally by the {@link DynamicPermissions}.
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
    private boolean allowed;

    /**
     * {@code true} if request this permission again.
     */
    private boolean askAgain;

    /**
     * {@code true} if this permission is not preset in the system.
     */
    private boolean unknown;

    /**
     * Default constructor to initialize the dynamic permission.
     */
    public DynamicPermission() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param permission The permission string.
     */
    public DynamicPermission(@NonNull String permission) {
        this(permission, null, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param permission The permission string.
     * @param icon The icon used for this permission.
     * @param title The title for this permission.
     */
    public DynamicPermission(@NonNull String permission,
            @Nullable Drawable icon, @Nullable String title) {
        this(permission, icon, title, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param permission The permission string.
     * @param icon The icon used for this permission.
     * @param title The title for this permission.
     * @param subtitle The subtitle for this permission.
     */
    public DynamicPermission(@NonNull String permission, @Nullable Drawable icon,
            @Nullable String title, @Nullable String subtitle) {
        this(permission, icon, title, subtitle, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param permission The permission string.
     * @param icon The icon used for this permission.
     * @param title The title for this permission.
     * @param subtitle The subtitle for this permission.
     * @param description The description for this permission.
     */
    public DynamicPermission(@NonNull String permission, @Nullable Drawable icon,
            @Nullable String title, @Nullable String subtitle, @Nullable String description) {
        this.permission = permission;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicPermission(@NonNull Parcel in) {
        this.permission = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.description = in.readString();
        this.dangerous = in.readByte() != 0;
        this.allowed = in.readByte() != 0;
        this.askAgain = in.readByte() != 0;
        this.unknown = in.readByte() != 0;
        this.icon = new BitmapDrawable(Resources.getSystem(),
                (Bitmap) in.readParcelable(getClass().getClassLoader()));
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicPermission> CREATOR =
            new Parcelable.Creator<DynamicPermission>() {
        @Override
        public DynamicPermission createFromParcel(Parcel in) {
            return new DynamicPermission(in);
        }

        @Override
        public DynamicPermission[] newArray(int size) {
            return new DynamicPermission[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permission);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        dest.writeByte((byte) (dangerous ? 1 : 0));
        dest.writeByte((byte) (allowed ? 1 : 0));
        dest.writeByte((byte) (askAgain ? 1 : 0));
        dest.writeByte((byte) (unknown ? 1 : 0));
        dest.writeParcelable(DynamicBitmapUtils.getBitmap(icon), flags);
    }

    /**
     * Get the permission string.
     *
     * @return The permission string.
     */
    public @NonNull String getPermission() {
        return permission;
    }

    /**
     * Sets the permission string.
     *
     * @param permission The permission to be set.
     */
    public void setPermission(@NonNull String permission) {
        this.permission = permission;
    }

    /**
     * Get the icon used by this permission.
     *
     * @return The icon used by this permission.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Sets the icon for this permission.
     *
     * @param icon The icon to be set.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.icon = icon;
    }

    /**
     * Get the title used by this permission.
     *
     * @return The title used by this permission.
     */
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Sets the title for this permission.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /**
     * Get the subtitle used by this permission.
     *
     * @return The subtitle used by this permission.
     */
    public @Nullable String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the subtitle for this permission.
     *
     * @param subtitle The subtitle to be set.
     */
    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Get the description used by this permission.
     *
     * @return The description used by this permission.
     */
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * Sets the description for this permission.
     *
     * @param description The description to be set.
     */
    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    /**
     * Returns whether this permission is dangerous.
     *
     * @return {@code true} if the permission is dangerous.
     */
    public boolean isDangerous() {
        return dangerous;
    }

    /**
     * Sets this permission as dangerous or normal.
     *
     * @param dangerous {@code true} if the permission is dangerous.
     */
    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    /**
     * Returns whether this permission is granted.
     *
     * @return {@code true} if the permission is granted.
     */
    public boolean isAllowed() {
        return allowed;
    }

    /**
     * Sets this permission as granted or denied.
     *
     * @param allowed {@code true} if the permission is granted.
     */
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Returns whether to request this permission again.
     *
     * @return {@code true} if request this permission again.
     */
    public boolean isAskAgain() {
        return askAgain;
    }

    /**
     * Sets this permission to request again or not.
     *
     * @param askAgain {@code true} if request this permission again.
     */
    public void setAskAgain(boolean askAgain) {
        this.askAgain = askAgain;
    }

    /**
     * Returns whether this permission is unknown or not.
     *
     * @return {@code true} this permission is present in the system.
     */
    public boolean isUnknown() {
        return unknown;
    }

    /**
     * Sets whether this permission is unknown or not.
     *
     * @param unknown {@code true} if this permission is present in the system.
     */
    public void setUnknown(boolean unknown) {
        this.unknown = unknown;
    }

    /**
     * Checks whether this permission must be granted after reinstalling the app.
     *
     * @return {@code true} if this permission must be granted after reinstalling the app.
     */
    public boolean isReinstall() {
        return isDangerous() && !isAskAgain() && !DynamicSdkUtils.is23();
    }
}
