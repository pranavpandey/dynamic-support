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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.model.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.AppTheme;
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
     * @param dynamicTheme The dynamic theme to copy the theme.
     */
    public DynamicWidgetTheme(@NonNull AppTheme<?> dynamicTheme) {
        super(dynamicTheme);

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

    /**
     * Returns background color after considering the opacity value of this theme.
     *
     * @return The background color with alpha according to the opacity value.
     */
    public @ColorInt int getBackgroundColorWithOpacity() {
        return DynamicColorUtils.setAlpha(getBackgroundColor(), getOpacity());
    }

    /**
     * Returns stroke color after considering the opacity value of this theme.
     *
     * @return The stroke color with alpha according to the opacity value.
     */
    public @ColorInt int getStrokeColorWithOpacity() {
        return DynamicColorUtils.setAlpha(getStrokeColor(), getOpacity());
    }

    /**
     * Returns opacity after converting it into the float range.
     *
     * @return The opacity after converting it into the float range.
     */
    public @FloatRange(from = 0f, to = 1f) float getAlpha() {
        return getOpacity() / 255f;
    }

    @Override
    public @NonNull DynamicAppTheme getThemeFallback(boolean resolve) {
        return DynamicTheme.getInstance().get(false);
    }

    @Override
    public @ColorInt int getBackgroundColor(boolean resolve) {
        if (resolve && super.getBackgroundColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getBackgroundColor();
        }

        return super.getBackgroundColor(resolve);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintBackgroundColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintBackgroundColor(true, inverse);
        }

        return super.getTintBackgroundColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getStrokeColor() {
        if (getPrimaryColorDark(false) == Theme.AUTO) {
            return super.getStrokeColor();
        }

        return getPrimaryColorDark();
    }

    @Override
    public @ColorInt int getTintSurfaceColor(boolean resolve) {
        if (resolve && super.getTintSurfaceColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintSurfaceColor();
        }

        return super.getTintSurfaceColor(resolve);
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve) {
        if (resolve && super.getPrimaryColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getPrimaryColor();
        }

        return super.getPrimaryColor(resolve);
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve) {
        if (resolve && super.getTintPrimaryColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintPrimaryColor();
        }

        return super.getTintPrimaryColor(resolve);
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark(boolean resolve) {
        if (resolve && super.getTintPrimaryColorDark(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintPrimaryColorDark();
        }

        return super.getTintPrimaryColorDark(resolve);
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve) {
        if (resolve && super.getAccentColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getAccentColor();
        }

        return super.getAccentColor(resolve);
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve) {
        if (resolve && super.getTintAccentColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintAccentColor();
        }

        return super.getTintAccentColor(resolve);
    }

    @Override
    public @ColorInt int getTintAccentColorDark(boolean resolve) {
        if (resolve && super.getTintAccentColorDark(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintAccentColorDark();
        }

        return super.getTintAccentColorDark(resolve);
    }

    @Override
    public @ColorInt int getTintErrorColor(boolean resolve) {
        if (resolve && super.getTintErrorColor(false) == Theme.AUTO) {
            return getThemeFallback(false).getTintErrorColor();
        }

        return super.getTintErrorColor(resolve);
    }

    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTextPrimaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextPrimaryColor();
        }

        return super.getTextPrimaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve, boolean inverse) {
        if (resolve && super.getTextPrimaryColorInverse(
                false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextPrimaryColorInverse();
        }

        return super.getTextPrimaryColorInverse(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTextSecondaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextSecondaryColor();
        }

        return super.getTextSecondaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve, boolean inverse) {
        if (resolve && super.getTextSecondaryColorInverse(
                false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextSecondaryColorInverse();
        }

        return super.getTextSecondaryColorInverse(resolve, inverse);
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
        return String.valueOf(getHeader());
    }

    @Override
    public @NonNull DynamicWidgetTheme setHeader(@Theme.Visibility int header) {
        this.header = header;

        return this;
    }

    @Override
    public @NonNull DynamicWidgetTheme setHeaderString(
            @NonNull @Theme.Visibility.ToString String header) {
        setHeader(Integer.parseInt(header));

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
        return getClass().getSimpleName()
                + "{" + getThemeRes()
                + getBackgroundColor(false, false)
                + getSurfaceColor(false)
                + getPrimaryColor(false)
                + getPrimaryColorDark(false)
                + getAccentColor(false)
                + getAccentColorDark(false)
                + getErrorColor(false)
                + getTintBackgroundColor(false, false)
                + getTintSurfaceColor(false)
                + getTintPrimaryColor(false)
                + getTintPrimaryColorDark(false)
                + getTintAccentColor(false)
                + getTintAccentColorDark(false)
                + getTintErrorColor(false)
                + getTextPrimaryColor(false, false)
                + getTextSecondaryColor(false, false)
                + getTextPrimaryColorInverse(false, false)
                + getTextSecondaryColorInverse(false, false)
                + getFontScale(false)
                + getCornerRadius(false)
                + getBackgroundAware(false)
                + getStyle()
                + getWidgetId() + getHeader() + getOpacity() + '}';
    }
}
