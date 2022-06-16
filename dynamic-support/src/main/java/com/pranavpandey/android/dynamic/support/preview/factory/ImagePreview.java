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

package com.pranavpandey.android.dynamic.support.preview.factory;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.preview.Preview;

/**
 * A {@link Preview} to provide the image preview functionality.
 */
public class ImagePreview extends Preview<String, Uri> implements Parcelable {

    /**
     * Constructor to initialize an object of this class.
     */
    public ImagePreview() {
        super();
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     */
    public ImagePreview(@Nullable String info, @Nullable Uri data) {
        super(info, data);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     * @param title The title for this preview.
     * @param subtitle The subtitle for this preview.
     */
    public ImagePreview(@Nullable String info, @Nullable Uri data,
            @Nullable String title, @Nullable String subtitle) {
        super(info, data, title, subtitle);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param info The info for this preview.
     * @param data The data for this preview.
     * @param dataCustom The customized data for this preview.
     * @param title The title for this preview.
     * @param subtitle The subtitle for this preview.
     */
    public ImagePreview(@Nullable String info, @Nullable Uri data, @Nullable Uri dataCustom,
            @Nullable String title, @Nullable String subtitle) {
        super(info, data, dataCustom, title, subtitle);
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public ImagePreview(@NonNull Parcel in) {
        super(in.readString(), in.readParcelable(Uri.class.getClassLoader()),
                in.readParcelable(Uri.class.getClassLoader()), in.readString(), in.readString());
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<ImagePreview> CREATOR =
            new Parcelable.Creator<ImagePreview>() {
                @Override
                public ImagePreview createFromParcel(Parcel in) {
                    return new ImagePreview(in);
                }

                @Override
                public ImagePreview[] newArray(int size) {
                    return new ImagePreview[size];
                }
            };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getInfo());
        dest.writeParcelable(getData(false), flags);
        dest.writeParcelable(getDataCustom(), flags);
        dest.writeString(getTitle());
        dest.writeString(getSubtitle());
    }
}
