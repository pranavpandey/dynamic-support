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

package com.pranavpandey.android.dynamic.support.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.annotation.Exclude;
import com.pranavpandey.android.dynamic.support.model.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.support.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;

/**
 * DynamicAppTheme class to store various colors and attributes for app widget which can be
 * modified at runtime.
 */
public class DynamicWidgetTheme extends DynamicAppTheme {

    /**
     * Default value for the opacity.
     */
    public static final int ADS_OPACITY_DEFAULT = 255;
    
    /**
     * App widget id used by this theme.
     */
    @Exclude
    @SerializedName(DynamicThemeUtils.ADS_NAME_WIDGET_ID)
    private int widgetId;

    /**
     * Header state used by this theme.
     */
    @SerializedName(DynamicThemeUtils.ADS_NAME_HEADER)
    private @Theme.Visibility int header;
    
    /**
     * Opacity value used by this theme.
     */
    @SerializedName(DynamicThemeUtils.ADS_NAME_OPACITY)
    private int opacity;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicWidgetTheme() {
        super();

        this.header = Theme.Visibility.AUTO;
        this.opacity = ADS_OPACITY_DEFAULT;
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param widgetId The widget id to be used.
     */
    public DynamicWidgetTheme(int widgetId) {
        this();

        this.widgetId = widgetId;
    }

    /**
     * Constructor to initialize an object of this class from the dynamic string.
     *
     * @param theme The dynamic string to initialize the instance.
     */
    public DynamicWidgetTheme(@NonNull String theme) throws Exception {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicWidgetTheme.class,
                        new DynamicThemeTypeAdapter<DynamicWidgetTheme>()).create()
                .fromJson(DynamicThemeUtils.formatDynamicTheme(theme), DynamicWidgetTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicAppTheme The dynamic app theme to copy the theme.
     */
    public DynamicWidgetTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        super(dynamicAppTheme);

        this.header = Theme.Visibility.AUTO;
        this.opacity = ADS_OPACITY_DEFAULT;
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicWidgetTheme The dynamic widget theme to copy the theme.
     */
    public DynamicWidgetTheme(@NonNull DynamicWidgetTheme dynamicWidgetTheme) {
        super(dynamicWidgetTheme);

        this.header = dynamicWidgetTheme.getHeader();
        this.opacity = dynamicWidgetTheme.getOpacity();
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicWidgetTheme> CREATOR =
            new Parcelable.Creator<DynamicWidgetTheme>() {
        @Override
        public DynamicWidgetTheme createFromParcel(Parcel in) {
            return new DynamicWidgetTheme(in);
        }

        @Override
        public DynamicWidgetTheme[] newArray(int size) {
            return new DynamicWidgetTheme[size];
        }
    };

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicWidgetTheme(Parcel in) {
        super(in);

        this.widgetId = in.readInt();
        this.header = in.readInt();
        this.opacity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(widgetId);
        dest.writeInt(header);
        dest.writeInt(opacity);
    }

    /**
     * Get the app widget id used by this theme.
     *
     * @return The app widget id used by this theme.
     */
    public int getWidgetId() {
        return widgetId;
    }

    /**
     * Set the widget id used by this theme.
     * 
     * @param widgetId The widget id to be set.
     *
     * @return The {@link DynamicWidgetTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicWidgetTheme setWidgetId(int widgetId) {
        this.widgetId = widgetId;
        
        return this;
    }

    /**
     * Get the header state used by this theme.
     *
     * @return The header state used by this theme.
     */
    public @Theme.Visibility int getHeader() {
        return header;
    }

    /**
     * Returns the header state string used by this theme.
     *
     * @return The header state string used by this theme.
     */
    public @NonNull @Theme.Visibility.ToString String getHeaderString() {
        return String.valueOf(header);
    }

    /**
     * Set the header state used by this theme.
     *
     * @param header The header state to be set.
     *
     * @return The {@link DynamicWidgetTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicWidgetTheme setHeader(@Theme.Visibility int header) {
        this.header = header;

        return this;
    }

    /**
     * Set the header state used by this theme.
     *
     * @param header The header state to be set.
     *
     * @return The {@link DynamicWidgetTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicWidgetTheme setHeaderString(
            @NonNull@Theme.Visibility.ToString String header) {
        this.header = Integer.valueOf(header);

        return this;
    }
    
    /**
     * Get the opacity value used by this theme.
     *
     * @return The opacity value used by this theme.
     */
    public int getOpacity() {
        return opacity;
    }

    /**
     * Set the opacity value used by this theme.
     *
     * @param opacity The opacity value to be set.
     *
     * @return The {@link DynamicWidgetTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicWidgetTheme setOpacity(@IntRange(from = 0, to = 255) int opacity) {
        this.opacity = opacity;

        return this;
    }

    @Override
    public @StyleRes int getThemeRes() {
        return DynamicTheme.getInstance().getApplication().getThemeRes();
    }

    @Override
    public @ColorInt int getBackgroundColor(boolean resolve) {
        if (resolve && super.getBackgroundColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getBackgroundColor();
        }

        return super.getBackgroundColor(resolve);
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve) {
        if (resolve && super.getPrimaryColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getPrimaryColor();
        }

        return super.getPrimaryColor(resolve);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        if (resolve && super.getPrimaryColorDark(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getPrimaryColorDark();
        }

        return super.getPrimaryColorDark(resolve);
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve) {
        if (resolve && super.getAccentColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getAccentColor();
        }

        return super.getAccentColor(resolve);
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve) {
        if (resolve && super.getAccentColorDark(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getAccentColorDark();
        }

        return super.getAccentColorDark(resolve);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve) {
        if (resolve && super.getTintBackgroundColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintBackgroundColor();
        }

        return super.getTintBackgroundColor(resolve);
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve) {
        if (resolve && super.getTintPrimaryColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintPrimaryColor();
        }

        return super.getTintPrimaryColor(resolve);
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark(boolean resolve) {
        if (resolve && super.getTintPrimaryColorDark(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintPrimaryColorDark();
        }

        return super.getTintPrimaryColorDark(resolve);
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve) {
        if (resolve && super.getTintAccentColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintAccentColor();
        }

        return super.getTintAccentColor(resolve);
    }

    @Override
    public @ColorInt int getTintAccentColorDark(boolean resolve) {
        if (resolve && super.getTintAccentColorDark(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintAccentColorDark();
        }

        return super.getTintAccentColorDark(resolve);
    }

    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve) {
        if (resolve && super.getTextPrimaryColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTextPrimaryColor();
        }

        return super.getTextPrimaryColor(resolve);
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve) {
        if (resolve && super.getTextSecondaryColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTextSecondaryColor();
        }

        return super.getTextSecondaryColor(resolve);
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve) {
        if (resolve && super.getTextPrimaryColorInverse(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTextPrimaryColorInverse();
        }

        return super.getTextPrimaryColorInverse(resolve);
    }

    @Override
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve) {
        if (resolve && super.getTextSecondaryColorInverse(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTextSecondaryColorInverse();
        }

        return super.getTextSecondaryColorInverse(resolve);
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware(boolean resolve) {
        if (resolve && super.getBackgroundAware(false) == Theme.BackgroundAware.AUTO) {
            return DynamicTheme.getInstance().getApplication().getBackgroundAware();
        }

        return super.getBackgroundAware(resolve);
    }

    @Override
    public int getCornerRadius(boolean resolve) {
        if (resolve && super.getCornerRadius(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getCornerRadius();
        }

        return super.getCornerRadius(resolve);
    }

    @Override
    public @NonNull String toJsonString() {
        return new Gson().toJson(new DynamicWidgetTheme(this));
    }

    @Override
    public @NonNull String toDynamicString() {
        return new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicWidgetTheme.class,
                        new DynamicThemeTypeAdapter<DynamicWidgetTheme>())
                .setPrettyPrinting().create().toJson(new DynamicWidgetTheme(this));
    }

    @Override
    public @NonNull String toString() {
        return "DynamicWidgetTheme{"
                + getThemeRes() + getBackgroundColor(false) + getPrimaryColor(false)
                + getPrimaryColorDark(false) + getAccentColor(false)
                + getAccentColorDark(false) + getTintBackgroundColor(false)
                + getTintPrimaryColor(false) + getTintPrimaryColorDark(false)
                + getTintAccentColor(false) + getTintAccentColorDark(false)
                + getTextPrimaryColor(false) + getTextSecondaryColor(false)
                + getTextPrimaryColorInverse(false) + getTextSecondaryColorInverse(false)
                + getCornerRadius(false) + getBackgroundAware(false)
                + widgetId + header + opacity +
                '}';
    }
}
