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

package com.pranavpandey.android.dynamic.support.tutorial;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.tutorial.fragment.DynamicSimpleTutorialFragment;

/**
 * A simple tutorial item to display a title, subtitle and description
 * along with an image which can be tinted according to the background
 * color.
 */
public class DynamicSimpleTutorial implements Parcelable {

    /**
     * Id to uniquely identify this tutorial.
     */
    private @ColorInt int id;

    /**
     * Background color used by this tutorial.
     */
    private @ColorInt int backgroundColor;

    /**
     * Title used by this tutorial.
     */
    private String title;

    /**
     * Subtitle used by this tutorial.
     */
    private String subtitle;

    /**
     * Description used by this tutorial.
     */
    private String description;

    /**
     * Image resource used by this tutorial.
     */
    private @DrawableRes int imageRes;

    /**
     * {@code true} to tint the image according to the background
     * color.
     */
    private boolean tintImage;

    /**
     * Default constructor to initialize the tutorial.
     */
    public DynamicSimpleTutorial() { }

    /**
     * Constructor to initialize the tutorial with id, title,
     * description and a image resource.
     */
    public DynamicSimpleTutorial(int id, @ColorInt int backgroundColor, @Nullable String title,
                                 @Nullable String description, @DrawableRes int imageRes) {
        this(id, backgroundColor, title, null, description, imageRes);
    }

    /**
     * Constructor to initialize the tutorial with id, title, subtitle,
     * description and a image resource.
     */
    public DynamicSimpleTutorial(int id, @ColorInt int backgroundColor, @Nullable String title,
                                 @Nullable String subtitle, @Nullable String description,
                                 @DrawableRes int imageRes) {
        this(id, backgroundColor, title, subtitle, description, imageRes, false);
    }

    /**
     * Constructor to initialize the tutorial with id, title, subtitle,
     * description, a image resource and whether it should be tinted
     * or not.
     */
    public DynamicSimpleTutorial(int id, @ColorInt int backgroundColor, @Nullable String title,
                                 @Nullable String subtitle, @Nullable String description,
                                 @DrawableRes int imageRes, boolean tintImage) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imageRes = imageRes;
        this.tintImage = tintImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(backgroundColor);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        dest.writeInt(imageRes);
        dest.writeByte((byte) (tintImage ? 1 : 0));
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DynamicSimpleTutorial createFromParcel(Parcel in) {
            return new DynamicSimpleTutorial(in);
        }

        public DynamicSimpleTutorial[] newArray(int size) {
            return new DynamicSimpleTutorial[size];
        }
    };

    /**
     * De-parcel {@link DynamicSimpleTutorial} object.
     */
    public DynamicSimpleTutorial(Parcel in) {
        this.id = in.readInt();
        this.backgroundColor = in.readInt();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.description = in.readString();
        this.imageRes = in.readInt();
        this.tintImage = in.readByte() != 0;
    }

    /**
     * Getter for {@link #id}.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for {@link #id}.
     */
    public DynamicSimpleTutorial setId(int id) {
        this.id = id;

        return this;
    }

    /**
     * Getter for {@link #backgroundColor}.
     */
    public @ColorInt int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Setter for {@link #backgroundColor}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;

        return this;
    }

    /**
     * Getter for {@link #title}.
     */
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Setter for {@link #title}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setTitle(@Nullable String title) {
        this.title = title;

        return this;
    }

    /**
     * Getter for {@link #subtitle}.
     */
    public @Nullable String getSubtitle() {
        return subtitle;
    }

    /**
     * Setter for {@link #subtitle}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Getter for {@link #description}.
     */
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * Setter for {@link #description}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setDescription(@Nullable String description) {
        this.description = description;

        return this;
    }

    /**
     * Getter for {@link #imageRes}.
     */
    public @DrawableRes int getImageRes() {
        return imageRes;
    }

    /**
     * Setter for {@link #imageRes}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;

        return this;
    }

    /**
     * Getter for {@link #tintImage}.
     */
    public boolean isTintImage() {
        return tintImage;
    }

    /**
     * Setter for {@link #tintImage}.
     *
     * @return {@link DynamicSimpleTutorial} object to allow for chaining
     *         of calls to set methods.
     */
    public DynamicSimpleTutorial setTintImage(boolean tintImage) {
        this.tintImage = tintImage;

        return this;
    }

    /**
     * @return Build this simple tutorial and return a dynamic tutorial
     * to show it in the view pager.
     *
     * @see DynamicSimpleTutorialFragment
     */
    public @NonNull DynamicTutorial build() {
        return DynamicSimpleTutorialFragment.newInstance(this);
    }
}
