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

package com.pranavpandey.android.dynamic.support.tutorial;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.tutorial.fragment.DynamicTutorialFragment;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A {@link Tutorial} item to display a title, subtitle and description along with an image
 * which can be tinted according to the tutorial colors.
 */
public class DynamicTutorial implements Parcelable,
        Tutorial.SharedElement<DynamicTutorial, DynamicTutorialFragment> {

    /**
     * Id to uniquely identify this tutorial.
     */
    private @ColorInt int id;

    /**
     * Color used by this tutorial.
     */
    private @ColorInt int color;

    /**
     * Tint color used by this tutorial.
     */
    private @ColorInt int tintColor;

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
     * {@code true} to tint the image according to the tint color.
     */
    private boolean tintImage;

    /**
     * {@code true} to set the shared element name and make it available for the transition.
     */
    private boolean sharedElement;

    /**
     * Fragment to show this tutorial.
     */
    private DynamicTutorialFragment mFragment;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The id to uniquely identify this tutorial.
     * @param color The color for this tutorial.
     * @param tintColor The tint color for this tutorial.
     * @param title The title for this tutorial.
     * @param description The description for this tutorial.
     * @param imageRes The image resource for this tutorial.
     */
    public DynamicTutorial(int id, @ColorInt int color, @ColorInt int tintColor,
            @Nullable String title, @Nullable String description, @DrawableRes int imageRes) {
        this(id, color, tintColor, title, null, description, imageRes);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The id to uniquely identify this tutorial.
     * @param color The color for this tutorial.
     * @param tintColor The tint color for this tutorial.
     * @param title The title for this tutorial.
     * @param subtitle The subtitle for this tutorial.
     * @param description The description for this tutorial.
     * @param imageRes The image resource for this tutorial.
     */
    public DynamicTutorial(int id, @ColorInt int color,
            @ColorInt int tintColor, @Nullable String title, @Nullable String subtitle,
            @Nullable String description, @DrawableRes int imageRes) {
        this(id, color, tintColor, title, subtitle, description,
                imageRes, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The id to uniquely identify this tutorial.
     * @param color The color for this tutorial.
     * @param tintColor The tint color for this tutorial.
     * @param title The title for this tutorial.
     * @param subtitle The subtitle for this tutorial.
     * @param description The description for this tutorial.
     * @param imageRes The image resource for this tutorial.
     * @param tintImage {@code true} to tint the image according to the tint color.
     */
    public DynamicTutorial(int id, @ColorInt int color, @ColorInt int tintColor,
            @Nullable String title, @Nullable String subtitle, @Nullable String description,
            @DrawableRes int imageRes, boolean tintImage) {
        this(id, color, tintColor, title, subtitle, description,
                imageRes, tintImage, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The id to uniquely identify this tutorial.
     * @param color The color for this tutorial.
     * @param tintColor The tint color for this tutorial.
     * @param title The title for this tutorial.
     * @param subtitle The subtitle for this tutorial.
     * @param description The description for this tutorial.
     * @param imageRes The image resource for this tutorial.
     * @param tintImage {@code true} to tint the image according to the tint color.
     * @param sharedElement {@code true} to set the shared element.
     */
    public DynamicTutorial(int id, @ColorInt int color, @ColorInt int tintColor,
            @Nullable String title, @Nullable String subtitle, @Nullable String description,
            @DrawableRes int imageRes, boolean tintImage, boolean sharedElement) {
        this.id = id;
        this.color = color;
        this.tintColor = tintColor;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imageRes = imageRes;
        this.tintImage = tintImage;
        this.sharedElement = sharedElement;
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicTutorial> CREATOR =
            new Parcelable.Creator<DynamicTutorial>() {
        @Override
        public DynamicTutorial createFromParcel(Parcel in) {
            return new DynamicTutorial(in);
        }

        @Override
        public DynamicTutorial[] newArray(int size) {
            return new DynamicTutorial[size];
        }
    };

    /**
     * Read {@link DynamicTutorial} object from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicTutorial(Parcel in) {
        this.id = in.readInt();
        this.color = in.readInt();
        this.tintColor = in.readInt();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.description = in.readString();
        this.imageRes = in.readInt();
        this.tintImage = in.readByte() != 0;
        this.sharedElement = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(color);
        dest.writeInt(tintColor);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        dest.writeInt(imageRes);
        dest.writeByte((byte) (tintImage ? 1 : 0));
        dest.writeByte((byte) (sharedElement ? 1 : 0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mFragment != null) {
            mFragment.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mFragment != null) {
            mFragment.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mFragment != null) {
            mFragment.onPageScrollStateChanged(state);
        }
    }

    @Override
    public int getTutorialId() {
        return id;
    }

    @Override
    public @NonNull DynamicTutorial getTutorial() {
        return this;
    }

    @Override
    public @NonNull DynamicTutorialFragment createTutorial() {
        setTutorialFragment(DynamicTutorialFragment.newInstance(this));

        return mFragment;
    }

    @Override
    public @ColorInt int getColor() {
        return color;
    }

    @Override
    public @ColorInt int getTintColor() {
        return tintColor;
    }

    @Override
    public void onColorChanged(@ColorInt int color, @ColorInt int tintColor) {
        if (mFragment != null) {
            mFragment.onColorChanged(color, tintColor);
        }
    }

    @Override
    public void onSetPadding(int left, int top, int right, int bottom) {
        if (mFragment != null) {
            mFragment.onSetPadding(left, top, right, bottom);
        }
    }

    @Override
    public boolean isSharedElement() {
        return sharedElement;
    }

    /**
     * Set the id to uniquely identify this tutorial.
     *
     * @param id The id to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setTutorialId(int id) {
        this.id = id;

        return this;
    }

    /**
     * Set the colors used by this tutorial.
     *
     * @param color The tutorial color to be set.
     * @param tintColor The tint color to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setColor(@ColorInt int color, @ColorInt int tintColor) {
        this.color = color;
        this.tintColor = tintColor;

        onColorChanged(color, tintColor);
        return this;
    }

    /**
     * Set the color used by this tutorial.
     *
     * @param color The color to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setColor(@ColorInt int color) {
        setColor(color, DynamicColorUtils.getTintColor(getTintColor()));
        return this;
    }

    /**
     * Get the title used by this tutorial.
     *
     * @return The title used by this tutorial.
     */
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Set the title used by this tutorial.
     *
     * @param title The subtitle to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setTitle(@Nullable String title) {
        this.title = title;

        return this;
    }

    /**
     * Get the subtitle used by this tutorial.
     *
     * @return The subtitle used by this tutorial.
     */
    public @Nullable String getSubtitle() {
        return subtitle;
    }

    /**
     * Set the subtitle used by this tutorial.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Get the description used by this tutorial.
     *
     * @return The description used by this tutorial.
     */
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * Set the description used by this tutorial.
     *
     * @param description The description to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setDescription(@Nullable String description) {
        this.description = description;

        return this;
    }

    /**
     * Get the image resource used by this tutorial.
     *
     * @return The image resource used by this tutorial.
     */
    public @DrawableRes int getImageRes() {
        return imageRes;
    }

    /**
     * Set the image resource used by this tutorial.
     *
     * @param imageRes The image resource to be set.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;

        return this;
    }

    /**
     * Returns whether to tint the image according to the tint color.
     *
     * @return {@code true} to tint the image according to the tint color.
     */
    public boolean isTintImage() {
        return tintImage;
    }

    /**
     * Set the image to be tinted or not.
     *
     * @param tintImage {@code true} to tint the image.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setTintImage(boolean tintImage) {
        this.tintImage = tintImage;

        return this;
    }

    /**
     * Set whether to set the shared element.
     *
     * @param sharedElement {@code true} to set the shared element.
     *
     * @return The {@link DynamicTutorial} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicTutorial setSharedElement(boolean sharedElement) {
        this.sharedElement = sharedElement;

        return this;
    }

    public @Nullable DynamicTutorialFragment getTutorialFragment() {
        return mFragment;
    }

    public void setTutorialFragment(@Nullable DynamicTutorialFragment fragment) {
        this.mFragment = fragment;
    }
}
