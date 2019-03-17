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
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * Dynamic app theme class to store various colors and attributes which can be modified at runtime.
 */
public class DynamicAppTheme implements Parcelable {

    /**
     * Constant for the auto color value.
     */
    public static final int AUTO = Theme.AUTO;

    /**
     * DynamicAppTheme resource used by this theme.
     */
    @Exclude
    @SerializedName(DynamicThemeUtils.NAME_THEME_RES)
    private @StyleRes int themeRes;

    /**
     * Background color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_BACKGROUND_COLOR)
    private @ColorInt int backgroundColor;

    /**
     * Primary color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_PRIMARY_COLOR)
    private @ColorInt int primaryColor;

    /**
     * Dark primary color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_PRIMARY_COLOR_DARK)
    private @ColorInt int primaryColorDark;

    /**
     * Accent color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_ACCENT_COLOR)
    private @ColorInt int accentColor;

    /**
     * Dark accent color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_ACCENT_COLOR_DARK)
    private @ColorInt int accentColorDark;

    /**
     * Tint color according to the background color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TINT_BACKGROUND_COLOR)
    private @ColorInt int tintBackgroundColor;

    /**
     * Tint color according to the primary color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TINT_PRIMARY_COLOR)
    private @ColorInt int tintPrimaryColor;

    /**
     * Tint color according to the dark primary color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TINT_PRIMARY_COLOR_DARK)
    private @ColorInt int tintPrimaryColorDark;

    /**
     * Tint color according to the accent color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TINT_ACCENT_COLOR)
    private @ColorInt int tintAccentColor;

    /**
     * Tint color according to the dark accent color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TINT_ACCENT_COLOR_DARK)
    private @ColorInt int tintAccentColorDark;

    /**
     * Primary text color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_TEXT_PRIMARY_COLOR)
    private @ColorInt int textPrimaryColor;

    /**
     * Secondary text color used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_TEXT_SECONDARY_COLOR)
    private @ColorInt int textSecondaryColor;

    /**
     * Inverse color for the primary text color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TEXT_PRIMARY_COLOR_INVERSE)
    private @ColorInt int textPrimaryColorInverse;

    /**
     * Inverse color for the secondary text color.
     */
    @SerializedName(DynamicThemeUtils.NAME_TEXT_SECONDARY_COLOR_INVERSE)
    private @ColorInt int textSecondaryColorInverse;

    /**
     * Corner radius used by this theme in pixels.
     */
    @SerializedName(DynamicThemeUtils.NAME_CORNER_RADIUS)
    private int cornerRadius;

    /**
     * Background aware functionality used by this theme.
     */
    @SerializedName(DynamicThemeUtils.NAME_BACKGROUND_AWARE)
    private @Theme.BackgroundAware int backgroundAware;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicAppTheme() {
        this.themeRes = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
        this.backgroundColor = AUTO;
        this.primaryColor = AUTO;
        this.primaryColorDark = AUTO;
        this.accentColor = AUTO;
        this.accentColorDark = AUTO;
        this.tintBackgroundColor = AUTO;
        this.tintPrimaryColor = AUTO;
        this.tintPrimaryColorDark = AUTO;
        this.tintAccentColor = AUTO;
        this.tintAccentColorDark = AUTO;
        this.textPrimaryColor = AUTO;
        this.textSecondaryColor = AUTO;
        this.textPrimaryColorInverse = AUTO;
        this.textSecondaryColorInverse = AUTO;
        this.cornerRadius = AUTO;
        this.backgroundAware = Theme.BackgroundAware.AUTO;
    }

    /**
     * Constructor to initialize an object of this class from the dynamic string.
     *
     * @param theme The dynamic string to initialize the instance.
     */
    public DynamicAppTheme(@NonNull String theme) throws Exception {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicAppTheme.class, new DynamicThemeTypeAdapter<>())
                .create().fromJson(DynamicThemeUtils
                        .formatDynamicTheme(theme), DynamicAppTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicAppTheme The dynamic app theme to copy the theme.
     */
    public DynamicAppTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        this.themeRes = dynamicAppTheme.getThemeRes();
        this.backgroundColor = dynamicAppTheme.getBackgroundColor(false);
        this.primaryColor = dynamicAppTheme.getPrimaryColor(false);
        this.primaryColorDark = dynamicAppTheme.getPrimaryColorDark(false);
        this.accentColor = dynamicAppTheme.getAccentColor(false);
        this.accentColorDark = dynamicAppTheme.getAccentColorDark(false);
        this.tintBackgroundColor = dynamicAppTheme.getTintBackgroundColor(false);
        this.tintPrimaryColor = dynamicAppTheme.getTintPrimaryColor(false);
        this.tintPrimaryColorDark = dynamicAppTheme.getTintPrimaryColorDark(false);
        this.tintAccentColor = dynamicAppTheme.getTintAccentColor(false);
        this.tintAccentColorDark = dynamicAppTheme.getTintAccentColorDark(false);
        this.textPrimaryColor = dynamicAppTheme.getTextPrimaryColor(false);
        this.textSecondaryColor = dynamicAppTheme.getTextSecondaryColor(false);
        this.textPrimaryColorInverse = dynamicAppTheme.getTextPrimaryColorInverse(false);
        this.textSecondaryColorInverse = dynamicAppTheme.getTextSecondaryColorInverse(false);
        this.cornerRadius = dynamicAppTheme.getCornerRadius(false);
        this.backgroundAware = dynamicAppTheme.getBackgroundAware(false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param primaryColor The primary color for this theme.
     * @param primaryColorDark The dark primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param cornerRadius The corner size for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int primaryColor,
            @ColorInt int primaryColorDark, @ColorInt int accentColor, int cornerRadius,
            @Theme.BackgroundAware int backgroundAware) {
        this(AUTO, primaryColor, primaryColorDark, accentColor, AUTO, AUTO, AUTO,
                AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, cornerRadius, backgroundAware);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param backgroundColor The background color for this theme.
     * @param primaryColor The primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param textPrimaryColor The primary text color for this theme.
     * @param textSecondaryColor The secondary text color for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int backgroundColor, @ColorInt int primaryColor,
            @ColorInt int accentColor, @ColorInt int textPrimaryColor,
            @ColorInt int textSecondaryColor, @Theme.BackgroundAware int backgroundAware) {
        this(backgroundColor, primaryColor, accentColor,
                DynamicColorUtils.getTintColor(backgroundColor),
                DynamicColorUtils.getTintColor(primaryColor),
                DynamicColorUtils.getTintColor(accentColor),
                textPrimaryColor, textSecondaryColor,
                DynamicColorUtils.getTintColor(textPrimaryColor),
                DynamicColorUtils.getTintColor(textSecondaryColor), backgroundAware);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param backgroundColor The background color for this theme.
     * @param primaryColor The primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param tintBackgroundColor Tint color according to the background color.
     * @param tintPrimaryColor Tint color according to the primary color.
     * @param tintAccentColor Tint color according to the accent color.
     * @param textPrimaryColor The primary text color for this theme.
     * @param textSecondaryColor The secondary text color for this theme.
     * @param textPrimaryColorInverse Inverse color for the primary text color.
     * @param textSecondaryColorInverse Inverse color for the secondary text color.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int backgroundColor, @ColorInt int primaryColor,
            @ColorInt int accentColor, @ColorInt int tintBackgroundColor,
            @ColorInt int tintPrimaryColor, @ColorInt int tintAccentColor,
            @ColorInt int textPrimaryColor, @ColorInt int textSecondaryColor,
            @ColorInt int textPrimaryColorInverse, @ColorInt int textSecondaryColorInverse,
            @Theme.BackgroundAware int backgroundAware) {
        this(backgroundColor, primaryColor, primaryColor, accentColor, accentColor, 
                tintBackgroundColor, tintPrimaryColor, tintPrimaryColor, tintAccentColor,
                tintAccentColor, textPrimaryColor, textSecondaryColor, textPrimaryColorInverse,
                textSecondaryColorInverse, AUTO, backgroundAware);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param backgroundColor The background color for this theme.
     * @param primaryColor The primary color for this theme.
     * @param primaryColorDark The dark primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param accentColorDark The dark accent color for this theme.
     * @param tintBackgroundColor Tint color according to the background color.
     * @param tintPrimaryColor Tint color according to the primary color.
     * @param tintPrimaryColorDark Tint color according to the dark primary color.
     * @param tintAccentColor Tint color according to the accent color.
     * @param tintAccentColorDark Tint color according to the dark accent color.
     * @param textPrimaryColor The primary text color for this theme.
     * @param textSecondaryColor The secondary text color for this theme.
     * @param textPrimaryColorInverse Inverse color for the primary text color.
     * @param textSecondaryColorInverse Inverse color for the secondary text color.
     * @param cornerRadius The corner size for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int backgroundColor, @ColorInt int primaryColor,
            @ColorInt int primaryColorDark, @ColorInt int accentColor,
            @ColorInt int accentColorDark, @ColorInt int tintBackgroundColor,
            @ColorInt int tintPrimaryColor, @ColorInt int tintPrimaryColorDark,
            @ColorInt int tintAccentColor, @ColorInt int tintAccentColorDark,
            @ColorInt int textPrimaryColor, @ColorInt int textSecondaryColor,
            @ColorInt int textPrimaryColorInverse, @ColorInt int textSecondaryColorInverse,
            int cornerRadius, @Theme.BackgroundAware int backgroundAware) {
        this.themeRes = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
        this.backgroundColor = backgroundColor;
        this.primaryColor = primaryColor;
        this.primaryColorDark = primaryColorDark;
        this.accentColor = accentColor;
        this.accentColorDark = accentColorDark;
        this.tintBackgroundColor = tintBackgroundColor;
        this.tintPrimaryColor = tintPrimaryColor;
        this.tintPrimaryColorDark = tintPrimaryColorDark;
        this.tintAccentColor = tintAccentColor;
        this.tintAccentColorDark = tintAccentColorDark;
        this.textPrimaryColor = textPrimaryColor;
        this.textSecondaryColor = textSecondaryColor;
        this.textPrimaryColorInverse = textPrimaryColorInverse;
        this.textSecondaryColorInverse = textSecondaryColorInverse;
        this.cornerRadius = cornerRadius;
        this.backgroundAware = backgroundAware;
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicAppTheme> CREATOR =
            new Parcelable.Creator<DynamicAppTheme>() {
        @Override
        public DynamicAppTheme createFromParcel(Parcel in) {
            return new DynamicAppTheme(in);
        }

        @Override
        public DynamicAppTheme[] newArray(int size) {
            return new DynamicAppTheme[size];
        }
    };

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicAppTheme(Parcel in) {
        this.themeRes = in.readInt();
        this.backgroundColor = in.readInt();
        this.primaryColor = in.readInt();
        this.primaryColorDark = in.readInt();
        this.accentColor = in.readInt();
        this.accentColorDark = in.readInt();
        this.tintBackgroundColor = in.readInt();
        this.tintPrimaryColor = in.readInt();
        this.tintPrimaryColorDark = in.readInt();
        this.tintAccentColor = in.readInt();
        this.tintAccentColorDark = in.readInt();
        this.textPrimaryColor = in.readInt();
        this.textSecondaryColor = in.readInt();
        this.textPrimaryColorInverse = in.readInt();
        this.textSecondaryColorInverse = in.readInt();
        this.cornerRadius = in.readInt();
        this.backgroundAware = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(themeRes);
        dest.writeInt(backgroundColor);
        dest.writeInt(primaryColor);
        dest.writeInt(primaryColorDark);
        dest.writeInt(accentColor);
        dest.writeInt(accentColorDark);
        dest.writeInt(tintBackgroundColor);
        dest.writeInt(tintPrimaryColor);
        dest.writeInt(tintPrimaryColorDark);
        dest.writeInt(tintAccentColor);
        dest.writeInt(tintAccentColorDark);
        dest.writeInt(textPrimaryColor);
        dest.writeInt(textSecondaryColor);
        dest.writeInt(textPrimaryColorInverse);
        dest.writeInt(textSecondaryColorInverse);
        dest.writeInt(cornerRadius);
        dest.writeInt(backgroundAware);
    }

    /**
     * @return The theme resource used by this theme.
     */
    public @StyleRes int getThemeRes() {
        return themeRes;
    }

    /**
     * Set the theme resource used by this theme.
     *
     * @param themeRes The theme resource to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setThemeRes(@StyleRes int themeRes) {
        this.themeRes = themeRes;

        return this;
    }

    /**
     * @return The background color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     */
    public @ColorInt int getBackgroundColor(boolean resolve) {
        if (resolve && backgroundColor == AUTO) {
            if (DynamicTheme.getInstance().getDefault().getBackgroundColor() == AUTO) {
                throw new IllegalArgumentException(
                        "Background color cannot be auto for the default theme.");
            }

            return DynamicTheme.getInstance().getDefault().getBackgroundColor();
        }

        return backgroundColor;
    }

    /**
     * @return The background color used by this theme.
     */
    public @ColorInt int getBackgroundColor() {
        return getBackgroundColor(true);
    }

    /**
     * Set the background color used by this theme.
     *
     * @param backgroundColor The background color to be set.
     * @param generateTint {@code true} to automatically generate the tint color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTintBackgroundColor(int)
     */
    public @NonNull DynamicAppTheme setBackgroundColor(
            @ColorInt int backgroundColor, boolean generateTint) {
        this.backgroundColor = backgroundColor;
        if (generateTint && backgroundColor != AUTO) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(backgroundColor));
        }

        return this;
    }

    /**
     * Set the background color used by this theme.
     * <p>It will automatically generate the tint color also.
     *
     * @param backgroundColor The background color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setBackgroundColor(int, boolean)
     * @see #setTintBackgroundColor(int)
     */
    public @NonNull DynamicAppTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    /**
     * Returns the primary color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The primary color used by this theme.
     */
    public @ColorInt int getPrimaryColor(boolean resolve) {
        if (resolve && primaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getPrimaryColor();
        }

        return primaryColor;
    }

    /**
     * Returns the primary color used by this theme.
     *
     * @return The primary color used by this theme.
     */
    public @ColorInt int getPrimaryColor() {
        return getPrimaryColor(true);
    }

    /**
     * Set the primary color used by this theme.
     *
     * @param primaryColor The primary color to be set.
     * @param generateTint {@code true} to automatically generate the tint color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTintPrimaryColor(int)
     */
    public @NonNull DynamicAppTheme setPrimaryColor(
            @ColorInt int primaryColor, boolean generateTint) {
        this.primaryColor = primaryColor;
        if (generateTint && primaryColor != AUTO) {
            setTintPrimaryColor(DynamicColorUtils.getTintColor(primaryColor));
        }

        return this;
    }

    /**
     * Set the primary color used by this theme.
     * <p>It will automatically generate the tint color also.
     *
     * @param primaryColor The primary color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setPrimaryColor(int, boolean)
     * @see #setTintPrimaryColor(int)
     */
    public @NonNull DynamicAppTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    /**
     * Returns the dark primary color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The dark primary color used by this theme.
     */
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        if (resolve && primaryColorDark == AUTO) {
            return DynamicTheme.getInstance().generateDarkColor(getPrimaryColor());
        }

        return primaryColorDark;
    }

    /**
     * Returns the dark primary color used by this theme.
     *
     * @return The dark primary color used by this theme.
     */
    public @ColorInt int getPrimaryColorDark() {
        return getPrimaryColorDark(true);
    }

    /**
     * Set the dark primary color used by this theme.
     *
     * @param primaryColorDark The dark primary color to be set.
     * @param generateTint {@code true} to automatically generate the tint color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTintPrimaryColorDark(int)
     */
    public @NonNull DynamicAppTheme setPrimaryColorDark(
            @ColorInt int primaryColorDark, boolean generateTint) {
        this.primaryColorDark = primaryColorDark;
        if (generateTint && primaryColorDark != AUTO) {
            setTintPrimaryColorDark(DynamicColorUtils.getTintColor(primaryColorDark));
        }

        return this;
    }

    /**
     * Set the dark primary color used by this theme.
     * <p>It will automatically generate the tint color also.
     *
     * @param primaryColorDark The dark primary color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setPrimaryColorDark(int, boolean)
     * @see #setTintPrimaryColorDark(int)
     */
    public @NonNull DynamicAppTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    /**
     * Returns the accent color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The accent color used by this theme.
     */
    public @ColorInt int getAccentColor(boolean resolve) {
        if (resolve && accentColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getAccentColor();
        }

        return accentColor;
    }

    /**
     * Returns the accent color used by this theme.
     *
     * @return The accent color used by this theme.
     */
    public @ColorInt int getAccentColor() {
        return getAccentColor(true);
    }

    /**
     * Set the accent color used by this theme.
     *
     * @param accentColor The accent color to be set.
     * @param generateTint {@code true} to automatically generate the tint color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTintAccentColor(int)
     */
    public @NonNull DynamicAppTheme setAccentColor(
            @ColorInt int accentColor, boolean generateTint) {
        this.accentColor = accentColor;
        if (generateTint && accentColor != AUTO) {
            setTintAccentColor(DynamicColorUtils.getTintColor(accentColor));
        }

        return this;
    }

    /**
     * Set the accent color used by this theme.
     * <p>It will automatically generate the tint color also.
     *
     * @param accentColor The accent color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setAccentColor(int, boolean)
     * @see #setTintAccentColor(int)
     */
    public @NonNull DynamicAppTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    /**
     * Returns the dark accent color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The dark accent color used by this theme.
     */
    public @ColorInt int getAccentColorDark(boolean resolve) {
        if (resolve && accentColorDark == AUTO) {
            return DynamicTheme.getInstance().generateDarkColor(getAccentColor());
        }

        return accentColorDark;
    }

    /**
     * Returns the dark accent color used by this theme.
     *
     * @return The dark accent color used by this theme.
     */
    public @ColorInt int getAccentColorDark() {
        return getAccentColorDark(true);
    }

    /**
     * Set the dark accent color used by this theme.
     *
     * @param accentColorDark The dark accent color to be set.
     * @param generateTint {@code true} to automatically generate the tint color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTintAccentColorDark(int)
     */
    public @NonNull DynamicAppTheme setAccentColorDark(
            @ColorInt int accentColorDark, boolean generateTint) {
        this.accentColorDark = accentColorDark;
        if (generateTint && accentColorDark != AUTO) {
            setTintAccentColorDark(DynamicColorUtils.getTintColor(accentColorDark));
        }

        return this;
    }

    /**
     * Set the dark accent color used by this theme.
     * <p>It will automatically generate the tint color also.
     *
     * @param accentColorDark The dark accent color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setAccentColorDark(int, boolean)
     * @see #setTintAccentColorDark(int)
     */
    public @NonNull DynamicAppTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    /**
     * Returns the background tint color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The background tint color used by this theme.
     */
    public @ColorInt int getTintBackgroundColor(boolean resolve) {
        if (resolve && tintBackgroundColor == AUTO) {
            return DynamicColorUtils.getTintColor(getBackgroundColor());
        }

        return tintBackgroundColor;
    }

    /**
     * Returns the background tint color used by this theme.
     *
     * @return The background tint color used by this theme.
     */
    public @ColorInt int getTintBackgroundColor() {
        return getTintBackgroundColor(true);
    }

    /**
     * Set the background tint color used by this theme.
     *
     * @param tintBackgroundColor The background tint color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTintBackgroundColor(@ColorInt int tintBackgroundColor) {
        this.tintBackgroundColor = tintBackgroundColor;

        return this;
    }

    /**
     * Returns the primary tint color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The primary tint color used by this theme.
     */
    public @ColorInt int getTintPrimaryColor(boolean resolve) {
        if (resolve && tintPrimaryColor == AUTO) {
            return DynamicColorUtils.getTintColor(getPrimaryColor());
        }

        return tintPrimaryColor;
    }

    /**
     * Returns the primary tint color used by this theme.
     *
     * @return The primary tint color used by this theme.
     */
    public @ColorInt int getTintPrimaryColor() {
        return getTintPrimaryColor(true);
    }

    /**
     * Set the primary tint color used by this theme.
     *
     * @param tintPrimaryColor The primary tint color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTintPrimaryColor(@ColorInt int tintPrimaryColor) {
        this.tintPrimaryColor = tintPrimaryColor;

        return this;
    }

    /**
     * Returns the dark primary tint color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The dark primary tint color used by this theme.
     */
    public @ColorInt int getTintPrimaryColorDark(boolean resolve) {
        if (resolve && tintPrimaryColorDark == AUTO) {
            return DynamicColorUtils.getTintColor(getPrimaryColorDark());
        }

        return tintPrimaryColorDark;
    }

    /**
     * Returns the dark primary tint color used by this theme.
     *
     * @return The dark primary tint color used by this theme.
     */
    public @ColorInt int getTintPrimaryColorDark() {
        return getTintPrimaryColorDark(true);
    }

    /**
     * Set the dark primary tint color used by this theme.
     *
     * @param tintPrimaryColorDark The dark primary tint color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTintPrimaryColorDark(@ColorInt int tintPrimaryColorDark) {
        this.tintPrimaryColorDark = tintPrimaryColorDark;

        return this;
    }

    /**
     * Returns the accent tint color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The accent tint color used by this theme.
     */
    public @ColorInt int getTintAccentColor(boolean resolve) {
        if (resolve && tintAccentColor == AUTO) {
            return DynamicColorUtils.getTintColor(getAccentColor());
        }

        return tintAccentColor;
    }

    /**
     * Returns the accent tint color used by this theme.
     *
     * @return The accent tint color used by this theme.
     */
    public @ColorInt int getTintAccentColor() {
        return getTintAccentColor(true);
    }

    /**
     * Set the accent tint color used by this theme.
     *
     * @param tintAccentColor The accent tint color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTintAccentColor(@ColorInt int tintAccentColor) {
        this.tintAccentColor = tintAccentColor;

        return this;
    }

    /**
     * Returns the dark accent tint color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The dark accent tint color used by this theme.
     */
    public @ColorInt int getTintAccentColorDark(boolean resolve) {
        if (resolve && tintAccentColorDark == AUTO) {
            return DynamicColorUtils.getTintColor(getAccentColorDark());
        }

        return tintAccentColorDark;
    }

    /**
     * Returns the dark accent tint color used by this theme.
     *
     * @return The dark accent tint color used by this theme.
     */
    public @ColorInt int getTintAccentColorDark() {
        return getTintAccentColorDark(true);
    }

    /**
     * Set the dark accent tint color used by this theme.
     *
     * @param tintAccentColorDark The dark accent tint color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTintAccentColorDark(@ColorInt int tintAccentColorDark) {
        this.tintAccentColorDark = tintAccentColorDark;

        return this;
    }


    /**
     * Returns the primary text color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The primary text color used by this theme.
     */
    public @ColorInt int getTextPrimaryColor(boolean resolve) {
        if (resolve && textPrimaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getTextPrimaryColor();
        }

        return textPrimaryColor;
    }

    /**
     * Returns the primary text color used by this theme.
     *
     * @return The primary text color used by this theme.
     */
    public @ColorInt int getTextPrimaryColor() {
        return getTextPrimaryColor(true);
    }

    /**
     * Set the primary text color used by this theme.
     *
     * @param textPrimaryColor The primary text color to be set.
     * @param generateInverse {@code true} to automatically generate the inverse color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTextPrimaryColorInverse(int)
     */
    public @NonNull DynamicAppTheme setTextPrimaryColor(
            @ColorInt int textPrimaryColor, boolean generateInverse) {
        this.textPrimaryColor = textPrimaryColor;
        if (generateInverse && textPrimaryColor != AUTO) {
            setTextPrimaryColorInverse(DynamicColorUtils.getTintColor(textPrimaryColor));
        }

        return this;
    }

    /**
     * Set the primary text color used by this theme.
     * <p>It will automatically generate the inverse color also.
     *
     * @param textPrimaryColor The primary text color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTextPrimaryColor(int, boolean)
     * @see #setTextPrimaryColorInverse(int)
     */
    public @NonNull DynamicAppTheme setTextPrimaryColor(@ColorInt int textPrimaryColor) {
        return setTextPrimaryColor(textPrimaryColor, true);
    }

    /**
     * Returns the secondary text color used by this theme.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The secondary text color used by this theme.
     */
    public @ColorInt int getTextSecondaryColor(boolean resolve) {
        if (resolve && textSecondaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getTextSecondaryColor();
        }

        return textSecondaryColor;
    }

    /**
     * Returns the secondary text color used by this theme.
     *
     * @return The secondary text color used by this theme.
     */
    public @ColorInt int getTextSecondaryColor() {
        return getTextSecondaryColor(true);
    }

    /**
     * Set the secondary text color used by this theme.
     *
     * @param textSecondaryColor The secondary text color to be set.
     * @param generateInverse {@code true} to automatically generate the inverse color also.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTextSecondaryColorInverse(int)
     */
    public @NonNull DynamicAppTheme setTextSecondaryColor(
            @ColorInt int textSecondaryColor, boolean generateInverse) {
        this.textSecondaryColor = textSecondaryColor;
        if (generateInverse && textSecondaryColor != AUTO) {
            setTextSecondaryColorInverse(DynamicColorUtils.getTintColor(textSecondaryColor));
        }

        return this;
    }

    /**
     * Set the secondary text color used by this theme.
     * <p>It will automatically generate the inverse color also.
     *
     * @param textSecondaryColor The secondary text color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #setTextSecondaryColor(int, boolean)
     * @see #setTextSecondaryColorInverse(int)
     */
    public @NonNull DynamicAppTheme setTextSecondaryColor(@ColorInt int textSecondaryColor) {
        return setTextSecondaryColor(textSecondaryColor, true);
    }

    /**
     * Returns the inverse color for the primary text color.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The inverse color for the primary text color.
     */
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve) {
        if (resolve && textPrimaryColorInverse == AUTO) {
            return DynamicColorUtils.getTintColor(getTextPrimaryColor());
        }

        return textPrimaryColorInverse;
    }

    /**
     * Returns the inverse color for the primary text color.
     *
     * @return The inverse color for the primary text color.
     */
    public @ColorInt int getTextPrimaryColorInverse() {
        return getTextPrimaryColorInverse(true);
    }

    /**
     * Set the primary text inverse color used by this theme.
     *
     * @param textPrimaryColorInverse The primary text inverse color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTextPrimaryColorInverse(int textPrimaryColorInverse) {
        this.textPrimaryColorInverse = textPrimaryColorInverse;

        return this;
    }

    /**
     * Returns the inverse color for the secondary text color.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The inverse color for the secondary text color.
     */
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve) {
        if (resolve && textSecondaryColorInverse == AUTO) {
            return DynamicColorUtils.getTintColor(getTextSecondaryColor());
        }

        return textSecondaryColorInverse;
    }

    /**
     * Returns the inverse color for the secondary text color.
     *
     * @return The inverse color for the secondary text color.
     */
    public @ColorInt int getTextSecondaryColorInverse() {
        return getTextSecondaryColorInverse(true);
    }

    /**
     * Set the secondary text inverse color used by this theme.
     *
     * @param textSecondaryColorInverse The secondary text inverse color to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setTextSecondaryColorInverse(int textSecondaryColorInverse) {
        this.textSecondaryColorInverse = textSecondaryColorInverse;

        return this;
    }

    /**
     * Returns the corner size used by this theme in pixels.
     *
     * @param resolve {@code true} to resolve auto corner size.
     *
     * @return The corner size used by this theme in pixels.
     */
    public int getCornerRadius(boolean resolve) {
        if (resolve && cornerRadius == AUTO) {
            return DynamicTheme.getInstance().getDefault().getCornerRadius();
        }

        return cornerRadius;
    }

    /**
     * Returns the corner size used by this theme in pixels.
     *
     * @return The corner size used by this theme in pixels.
     */
    public int getCornerRadius() {
        return getCornerRadius(true);
    }

    /**
     * Returns the corner size used by this theme in dips.
     *
     * @return The corner size used by this theme in dips.
     */
    public int getCornerSizeDp() {
        return DynamicUnitUtils.convertPixelsToDp(getCornerRadius());
    }

    /**
     * Set the corner size used by this theme.
     *
     * @param cornerRadius The corner size to be set in pixels.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;

        return this;
    }

    /**
     * Set the corner size used by this theme.
     *
     * @param cornerSize The corner size to be set in dips.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setCornerRadiusDp(float cornerSize) {
        return setCornerRadius((int) cornerSize == AUTO ? (int) cornerSize
                : DynamicUnitUtils.convertDpToPixels(cornerSize));
    }

    /**
     * Returns the background aware functionality used by this theme.
     *
     * @param resolve {@code true} to resolve auto background aware.
     *
     * @return The background aware functionality used by this theme.
     */
    public @Theme.BackgroundAware int getBackgroundAware(boolean resolve) {
        if (resolve && backgroundAware == Theme.BackgroundAware.AUTO) {
            return DynamicTheme.getInstance().getDefault().getBackgroundAware();
        }

        return backgroundAware;
    }

    /**
     * Returns the background aware functionality used by this theme.
     *
     * @return The background aware functionality used by this theme.
     */
    public @Theme.BackgroundAware int getBackgroundAware() {
        return getBackgroundAware(true);
    }

    /**
     * Checks whether the background functionality is enabled.
     *
     * @return {@code true} if the background aware functionality is enabled.
     */
    public boolean isBackgroundAware() {
        return getBackgroundAware(true) != Theme.BackgroundAware.DISABLE;
    }

    /**
     * Set the background aware functionality used by this theme.
     *
     * @param backgroundAware The background aware functionality to be set.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme setBackgroundAware(
            @Theme.BackgroundAware int backgroundAware) {
        this.backgroundAware = backgroundAware;

        return this;
    }

    /**
     * Returns {@code true} if this theme is dark.
     *
     * @return {@code true} if this theme is dark.
     */
    public boolean isDarkTheme() {
        return DynamicColorUtils.isColorDark(getBackgroundColor());
    }

    /**
     * Auto generate tint or inverse colors according to the base colors. They can be set
     * individually by calling the appropriate methods.
     *
     * @param tint {@code true} to generate tint colors.
     * @param inverse {@code true} to generate inverse colors.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicAppTheme autoGenerateColors(boolean tint, boolean inverse) {
        if (tint) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(getBackgroundColor()));
            setTintPrimaryColor(DynamicColorUtils.getTintColor(getPrimaryColor()));
            setTintPrimaryColorDark(DynamicColorUtils.getTintColor(getAccentColorDark()));
            setTintAccentColor(DynamicColorUtils.getTintColor(getAccentColor()));
            setTintAccentColorDark(DynamicColorUtils.getTintColor(getAccentColorDark()));
        }

        if (inverse) {
            setTextPrimaryColorInverse(DynamicColorUtils.getTintColor(getTextPrimaryColor()));
            setTextSecondaryColorInverse(DynamicColorUtils.getTintColor(getTextSecondaryColor()));
        }

        return this;
    }

    /**
     * Auto generate tint or inverse colors according to the base colors. They can be set
     * individually by calling the appropriate methods.
     *
     * @return The {@link DynamicAppTheme} object to allow for chaining of calls to set methods.
     *
     * @see #autoGenerateColors(boolean, boolean)
     */
    public @NonNull DynamicAppTheme autoGenerateColors() {
        return autoGenerateColors(true, true);
    }

    /**
     * Converts this theme into its Json equivalent.
     *
     * @return The Json equivalent of this theme.
     */
    public @NonNull String toJsonString() {
        return new Gson().toJson(new DynamicAppTheme(this));
    }

    /**
     * Convert this theme into a pretty json string.
     *
     * @return The converted json string.
     */
    public @NonNull String toDynamicString() {
        return new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicAppTheme.class, new DynamicThemeTypeAdapter<>())
                .setPrettyPrinting().create().toJson(new DynamicAppTheme(this));
    }

    @Override
    public @NonNull String toString() {
        return "DynamicAppTheme{"
                + themeRes + backgroundColor + primaryColor + primaryColorDark
                + accentColor + accentColorDark + tintBackgroundColor + tintPrimaryColor
                + tintPrimaryColorDark + tintAccentColor + tintAccentColorDark + textPrimaryColor
                + textSecondaryColor + textPrimaryColorInverse + textSecondaryColorInverse
                + cornerRadius + backgroundAware +
                '}';
    }
}
