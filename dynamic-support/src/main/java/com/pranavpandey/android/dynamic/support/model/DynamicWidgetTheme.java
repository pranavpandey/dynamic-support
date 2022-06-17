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
import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.provider.DynamicAppWidgetProvider;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.theme.annotation.Exclude;
import com.pranavpandey.android.dynamic.theme.base.WidgetTheme;
import com.pranavpandey.android.dynamic.theme.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;

/**
 * An app widget theme to store various colors and attributes for app widget which can be
 * modified at runtime.
 */
public class DynamicWidgetTheme extends DynamicAppTheme
        implements WidgetTheme<DynamicWidgetTheme, DynamicAppTheme> {

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
     * Constructor to initialize an object of this class.
     */
    public DynamicWidgetTheme() {
        this(DynamicAppWidgetProvider.NO_ID);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param widgetId The widget id to be used.
     */
    public DynamicWidgetTheme(int widgetId) {
        this(widgetId, new DynamicAppTheme());
    }

    /**
     * Constructor to initialize an object of this class from the theme string.
     *
     * @param theme The theme string to initialize the instance.
     */
    public DynamicWidgetTheme(@NonNull String theme) throws JsonSyntaxException {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy()).registerTypeAdapter(
                DynamicWidgetTheme.class, new DynamicThemeTypeAdapter<>(new DynamicWidgetTheme()))
                .create().fromJson(DynamicThemeUtils.format(theme), DynamicWidgetTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param theme The dynamic theme to copy the theme.
     */
    public DynamicWidgetTheme(@NonNull AppTheme<?> theme) {
        this(DynamicAppWidgetProvider.NO_ID, theme);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param widgetId The widget id to be used.
     * @param theme The dynamic theme to copy the theme.
     */
    public DynamicWidgetTheme(int widgetId, @NonNull AppTheme<?> theme) {
        super(theme);

        this.widgetId = widgetId;
        if (theme instanceof WidgetTheme) {
            this.header = ((WidgetTheme<?, ?>) theme).getHeader();
        } else {
            this.header = Theme.Visibility.AUTO;
        }

        setType(Theme.WIDGET);
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicWidgetTheme(@NonNull Parcel in) {
        super(in);

        this.widgetId = in.readInt();
        this.header = in.readInt();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(widgetId);
        dest.writeInt(header);
    }

    @Override
    public @NonNull DynamicAppTheme getThemeFallback(boolean resolve) {
        if (resolve) {
            return DynamicTheme.getInstance().get();
        }

        return DynamicTheme.getInstance().getApplication();
    }

    @Override
    public @ColorInt int getBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && super.getBackgroundColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getBackgroundColor(true, inverse);
        }

        return super.getBackgroundColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintBackgroundColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintBackgroundColor(true, inverse);
        }

        return super.getTintBackgroundColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve, boolean inverse) {
        if (resolve && super.getSurfaceColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getSurfaceColor(true, inverse);
        }

        return super.getSurfaceColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintSurfaceColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintSurfaceColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintSurfaceColor(true, inverse);
        }

        return super.getTintSurfaceColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getPrimaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getPrimaryColor(true, inverse);
        }

        return super.getPrimaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve, boolean inverse) {
        if (resolve && super.getPrimaryColorDark(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getPrimaryColorDark(true, inverse);
        }

        return super.getPrimaryColorDark(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintPrimaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintPrimaryColor(true, inverse);
        }

        return super.getTintPrimaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark(boolean resolve, boolean inverse) {
        if (resolve && super.getTintPrimaryColorDark(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintPrimaryColorDark(true, inverse);
        }

        return super.getTintPrimaryColorDark(resolve, inverse);
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve, boolean inverse) {
        if (resolve && super.getAccentColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getAccentColor(true, inverse);
        }

        return super.getAccentColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve, boolean inverse) {
        if (resolve && super.getAccentColorDark(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getAccentColorDark(true, inverse);
        }

        return super.getAccentColorDark(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintAccentColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintAccentColor(true, inverse);
        }

        return super.getTintAccentColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintAccentColorDark(boolean resolve, boolean inverse) {
        if (resolve && super.getTintAccentColorDark(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintAccentColorDark(true, inverse);
        }

        return super.getTintAccentColorDark(resolve, inverse);
    }

    @Override
    public @ColorInt int getErrorColor(boolean resolve, boolean inverse) {
        if (resolve && super.getErrorColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getErrorColor(true, inverse);
        }

        return super.getErrorColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTintErrorColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTintErrorColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTintErrorColor(true, inverse);
        }

        return super.getTintErrorColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTextPrimaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextPrimaryColor();
        }

        return super.getTextPrimaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve, boolean inverse) {
        if (resolve && super.getTextSecondaryColor(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextSecondaryColor();
        }

        return super.getTextSecondaryColor(resolve, inverse);
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve, boolean inverse) {
        if (resolve && super.getTextPrimaryColorInverse(false, false) == Theme.AUTO) {
            return getThemeFallback(false).getTextPrimaryColorInverse();
        }

        return super.getTextPrimaryColorInverse(resolve, inverse);
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
    public @ColorInt int getStrokeColor() {
        if (getPrimaryColorDark(false) == Theme.AUTO) {
            return super.getStrokeColor();
        }

        return getPrimaryColorDark();
    }

    @Override
    public @Theme int getType(boolean resolve) {
        if (resolve && super.getType(false) == Theme.WIDGET) {
            return Theme.APP;
        }

        return super.getType(resolve);
    }

    @Override
    public @NonNull String toJsonString(boolean resolve, boolean inverse) {
        return new GsonBuilder().registerTypeAdapter(DynamicWidgetTheme.class,
                new DynamicThemeTypeAdapter<>(new DynamicWidgetTheme(), resolve, inverse))
                .create().toJson(new DynamicWidgetTheme(this));
    }

    @Override
    public @NonNull String toDynamicString() {
        return new GsonBuilder().setExclusionStrategies(new ExcludeStrategy()).registerTypeAdapter(
                DynamicWidgetTheme.class, new DynamicThemeTypeAdapter<>(new DynamicWidgetTheme()))
                .setPrettyPrinting().create().toJson(new DynamicWidgetTheme(this));
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
    public @NonNull String toString() {
        return getClass().getSimpleName()
                + "{" + isHost() + getThemeRes()
                + getBackgroundColor(false, false)
                + getSurfaceColor(false, false)
                + getPrimaryColor(false, false)
                + getPrimaryColorDark(false, false)
                + getAccentColor(false, false)
                + getAccentColorDark(false, false)
                + getErrorColor(false, false)
                + getTintBackgroundColor(false, false)
                + getTintSurfaceColor(false, false)
                + getTintPrimaryColor(false, false)
                + getTintPrimaryColorDark(false, false)
                + getTintAccentColor(false, false)
                + getTintAccentColorDark(false, false)
                + getTintErrorColor(false, false)
                + getTextPrimaryColor(false, false)
                + getTextSecondaryColor(false, false)
                + getTextPrimaryColorInverse(false, false)
                + getTextSecondaryColorInverse(false, false)
                + getFontScale(false) + getCornerRadius(false)
                + getBackgroundAware(false) + getContrast(false)
                + getOpacity(false) + getElevation(false)
                + getStyle()+ getType() + getWidgetId() + getHeader() + '}';
    }
}
