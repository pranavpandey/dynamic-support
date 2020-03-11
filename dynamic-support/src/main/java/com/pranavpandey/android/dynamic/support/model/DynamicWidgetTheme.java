/*
 * Copyright 2018-2020 Pranav Pandey
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.model.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.AppWidgetTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.annotation.Exclude;
import com.pranavpandey.android.dynamic.theme.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * An app widget theme to store various colors and attributes for app widget which can be
 * modified at runtime.
 */
public class DynamicWidgetTheme extends DynamicAppTheme
        implements AppWidgetTheme<DynamicAppTheme> {

    /**
     * App widget id used by this theme.
     */
    @Exclude
    @SerializedName(Theme.Key.WIDGET_ID)
    private int widgetId;

    /**
     * Header state used by this theme.
     */
    @SerializedName(Theme.Key.HEADER)
    private @Theme.Visibility int header;
    
    /**
     * Opacity value used by this theme.
     */
    @SerializedName(Theme.Key.OPACITY)
    private int opacity;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicWidgetTheme() {
        super();

        this.header = Theme.Visibility.AUTO;
        this.opacity = AppWidgetTheme.OPACITY_DEFAULT;
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
     * Constructor to initialize an object of this class from the theme string.
     *
     * @param theme The theme string to initialize the instance.
     */
    public DynamicWidgetTheme(@NonNull String theme) throws JsonSyntaxException {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicWidgetTheme.class,
                        new DynamicThemeTypeAdapter<DynamicWidgetTheme>()).create()
                .fromJson(DynamicThemeUtils.formatTheme(theme), DynamicWidgetTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicAppTheme The dynamic app theme to copy the theme.
     */
    public DynamicWidgetTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        super(dynamicAppTheme);

        this.header = Theme.Visibility.AUTO;
        this.opacity = AppWidgetTheme.OPACITY_DEFAULT;
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

    @Override
    public int getWidgetId() {
        return widgetId;
    }

    @Override
    public @NonNull DynamicWidgetTheme setWidgetId(int widgetId) {
        this.widgetId = widgetId;
        
        return this;
    }

    @Override
    public @Theme.Visibility int getHeader() {
        return header;
    }

    @Override
    public @NonNull @Theme.Visibility.ToString String getHeaderString() {
        return String.valueOf(header);
    }

    @Override
    public @NonNull DynamicWidgetTheme setHeader(@Theme.Visibility int header) {
        this.header = header;

        return this;
    }

    @Override
    public @NonNull DynamicWidgetTheme setHeaderString(
            @NonNull @Theme.Visibility.ToString String header) {
        this.header = Integer.parseInt(header);

        return this;
    }
    
    @Override
    public int getOpacity() {
        return Math.min(AppWidgetTheme.OPACITY_MAX, opacity);
    }

    @Override
    public @NonNull DynamicWidgetTheme setOpacity(
            @IntRange(from = 0, to = AppWidgetTheme.OPACITY_MAX) int opacity) {
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

    /**
     * Returns background color after considering the opacity value of this theme.
     *
     * @return The background color with alpha according to the opacity value.
     */
    public @ColorInt int getBackgroundColorWithOpacity() {
        return DynamicColorUtils.setAlpha(getBackgroundColor(), getOpacity());
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve) {
        if (resolve && super.getSurfaceColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getSurfaceColor();
        }

        return super.getSurfaceColor(resolve);
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
    public @ColorInt int getTintSurfaceColor(boolean resolve) {
        if (resolve && super.getTintSurfaceColor(false) == AUTO) {
            return DynamicTheme.getInstance().getApplication().getTintSurfaceColor();
        }

        return super.getTintSurfaceColor(resolve);
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
    public int getFontScale(boolean resolve) {
        if (resolve && (super.getFontScale(false) == AUTO
                || super.getFontScale(true) <= 0)) {
            return DynamicTheme.getInstance().getApplication().getFontScale();
        }

        return super.getFontScale(resolve);
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
                + getFontScale(false) + getCornerRadius(false)
                + getBackgroundAware(false) + widgetId + header + opacity +
                '}';
    }
}
