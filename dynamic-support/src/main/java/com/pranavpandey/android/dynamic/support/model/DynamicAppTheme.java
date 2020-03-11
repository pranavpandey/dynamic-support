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
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.model.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.annotation.Exclude;
import com.pranavpandey.android.dynamic.theme.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * An app theme to store various colors and attributes which can be modified at runtime.
 */
public class DynamicAppTheme implements AppTheme<DynamicAppTheme>, Parcelable {

    /**
     * Constant for the auto color value.
     */
    public static final int AUTO = Theme.AUTO;

    /**
     * DynamicAppTheme resource used by this theme.
     */
    @Exclude
    @SerializedName(Theme.Key.RES)
    private @StyleRes int themeRes;

    /**
     * Background color used by this theme.
     */
    @SerializedName(Theme.Key.BACKGROUND)
    private @ColorInt int backgroundColor;

    /**
     * Surface color used by this theme.
     */
    @SerializedName(Theme.Key.SURFACE)
    private @ColorInt int surfaceColor;

    /**
     * Primary color used by this theme.
     */
    @SerializedName(Theme.Key.PRIMARY)
    private @ColorInt int primaryColor;

    /**
     * Dark primary color used by this theme.
     */
    @SerializedName(Theme.Key.PRIMARY_DARK)
    private @ColorInt int primaryColorDark;

    /**
     * Accent color used by this theme.
     */
    @SerializedName(Theme.Key.ACCENT)
    private @ColorInt int accentColor;

    /**
     * Dark accent color used by this theme.
     */
    @SerializedName(Theme.Key.ACCENT_DARK)
    private @ColorInt int accentColorDark;

    /**
     * Tint color according to the background color.
     */
    @SerializedName(Theme.Key.TINT_BACKGROUND)
    private @ColorInt int tintBackgroundColor;

    /**
     * Tint color according to the surface color.
     */
    @SerializedName(Theme.Key.TINT_SURFACE)
    private @ColorInt int tintSurfaceColor;

    /**
     * Tint color according to the primary color.
     */
    @SerializedName(Theme.Key.TINT_PRIMARY)
    private @ColorInt int tintPrimaryColor;

    /**
     * Tint color according to the dark primary color.
     */
    @SerializedName(Theme.Key.TINT_PRIMARY_DARK)
    private @ColorInt int tintPrimaryColorDark;

    /**
     * Tint color according to the accent color.
     */
    @SerializedName(Theme.Key.TINT_ACCENT)
    private @ColorInt int tintAccentColor;

    /**
     * Tint color according to the dark accent color.
     */
    @SerializedName(Theme.Key.TINT_ACCENT_DARK)
    private @ColorInt int tintAccentColorDark;

    /**
     * Primary text color used by this theme.
     */
    @SerializedName(Theme.Key.TEXT_PRIMARY)
    private @ColorInt int textPrimaryColor;

    /**
     * Secondary text color used by this theme.
     */
    @SerializedName(Theme.Key.TEXT_SECONDARY)
    private @ColorInt int textSecondaryColor;

    /**
     * Inverse color for the primary text color.
     */
    @SerializedName(Theme.Key.TEXT_PRIMARY_INVERSE)
    private @ColorInt int textPrimaryColorInverse;

    /**
     * Inverse color for the secondary text color.
     */
    @SerializedName(Theme.Key.TEXT_SECONDARY_INVERSE)
    private @ColorInt int textSecondaryColorInverse;

    /**
     * Scaling factor for the text used by this theme.
     */
    @SerializedName(Theme.Key.FONT_SCALE)
    private int fontScale;

    /**
     * Corner radius used by this theme in pixels.
     */
    @SerializedName(Theme.Key.CORNER_RADIUS)
    private int cornerRadius;

    /**
     * Background aware functionality used by this theme.
     */
    @SerializedName(Theme.Key.BACKGROUND_AWARE)
    private @Theme.BackgroundAware int backgroundAware;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicAppTheme() {
        this.themeRes = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
        this.backgroundColor = AUTO;
        this.surfaceColor = AUTO;
        this.primaryColor = AUTO;
        this.primaryColorDark = AUTO;
        this.accentColor = AUTO;
        this.accentColorDark = AUTO;
        this.tintBackgroundColor = AUTO;
        this.tintSurfaceColor = AUTO;
        this.tintPrimaryColor = AUTO;
        this.tintPrimaryColorDark = AUTO;
        this.tintAccentColor = AUTO;
        this.tintAccentColorDark = AUTO;
        this.textPrimaryColor = AUTO;
        this.textSecondaryColor = AUTO;
        this.textPrimaryColorInverse = AUTO;
        this.textSecondaryColorInverse = AUTO;
        this.fontScale = AUTO;
        this.cornerRadius = AUTO;
        this.backgroundAware = Theme.BackgroundAware.AUTO;
    }

    /**
     * Constructor to initialize an object of this class from the theme string.
     *
     * @param theme The theme string to initialize the instance.
     */
    public DynamicAppTheme(@NonNull String theme) throws JsonSyntaxException {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicAppTheme.class, new DynamicThemeTypeAdapter<>())
                .create().fromJson(DynamicThemeUtils.formatTheme(theme), DynamicAppTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicAppTheme The dynamic app theme to copy the theme.
     */
    public DynamicAppTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        this.themeRes = dynamicAppTheme.getThemeRes();
        this.backgroundColor = dynamicAppTheme.getBackgroundColor(false);
        this.surfaceColor = dynamicAppTheme.getSurfaceColor(false);
        this.primaryColor = dynamicAppTheme.getPrimaryColor(false);
        this.primaryColorDark = dynamicAppTheme.getPrimaryColorDark(false);
        this.accentColor = dynamicAppTheme.getAccentColor(false);
        this.accentColorDark = dynamicAppTheme.getAccentColorDark(false);
        this.tintBackgroundColor = dynamicAppTheme.getTintBackgroundColor(false);
        this.tintSurfaceColor = dynamicAppTheme.getTintSurfaceColor(false);
        this.tintPrimaryColor = dynamicAppTheme.getTintPrimaryColor(false);
        this.tintPrimaryColorDark = dynamicAppTheme.getTintPrimaryColorDark(false);
        this.tintAccentColor = dynamicAppTheme.getTintAccentColor(false);
        this.tintAccentColorDark = dynamicAppTheme.getTintAccentColorDark(false);
        this.textPrimaryColor = dynamicAppTheme.getTextPrimaryColor(false);
        this.textSecondaryColor = dynamicAppTheme.getTextSecondaryColor(false);
        this.textPrimaryColorInverse = dynamicAppTheme.getTextPrimaryColorInverse(false);
        this.textSecondaryColorInverse = dynamicAppTheme.getTextSecondaryColorInverse(false);
        this.fontScale = dynamicAppTheme.getFontScale(false);
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
            @ColorInt int primaryColorDark, @ColorInt int accentColor, int fontScale,
            int cornerRadius, @Theme.BackgroundAware int backgroundAware) {
        this(AUTO, AUTO, primaryColor, primaryColorDark, accentColor, AUTO, AUTO,
                AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, AUTO, fontScale,
                cornerRadius, backgroundAware);
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
        this(backgroundColor, AUTO, primaryColor, primaryColor, accentColor, accentColor,
                tintBackgroundColor, AUTO, tintPrimaryColor, tintPrimaryColor, tintAccentColor,
                tintAccentColor, textPrimaryColor, textSecondaryColor, textPrimaryColorInverse,
                textSecondaryColorInverse, AUTO, AUTO, backgroundAware);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param backgroundColor The background color for this theme.
     * @param surfaceColor The surface color for this theme.
     * @param primaryColor The primary color for this theme.
     * @param primaryColorDark The dark primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param accentColorDark The dark accent color for this theme.
     * @param tintBackgroundColor Tint color according to the background color.
     * @param tintSurfaceColor Tint color according to the surface color.
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
    public DynamicAppTheme(@ColorInt int backgroundColor, @ColorInt int surfaceColor,
            @ColorInt int primaryColor, @ColorInt int primaryColorDark,
            @ColorInt int accentColor, @ColorInt int accentColorDark,
            @ColorInt int tintBackgroundColor, @ColorInt int tintSurfaceColor,
            @ColorInt int tintPrimaryColor, @ColorInt int tintPrimaryColorDark,
            @ColorInt int tintAccentColor, @ColorInt int tintAccentColorDark,
            @ColorInt int textPrimaryColor, @ColorInt int textSecondaryColor,
            @ColorInt int textPrimaryColorInverse, @ColorInt int textSecondaryColorInverse,
            int fontScale, int cornerRadius, @Theme.BackgroundAware int backgroundAware) {
        this.themeRes = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
        this.backgroundColor = backgroundColor;
        this.surfaceColor = surfaceColor;
        this.primaryColor = primaryColor;
        this.primaryColorDark = primaryColorDark;
        this.accentColor = accentColor;
        this.accentColorDark = accentColorDark;
        this.tintBackgroundColor = tintBackgroundColor;
        this.tintSurfaceColor = tintSurfaceColor;
        this.tintPrimaryColor = tintPrimaryColor;
        this.tintPrimaryColorDark = tintPrimaryColorDark;
        this.tintAccentColor = tintAccentColor;
        this.tintAccentColorDark = tintAccentColorDark;
        this.textPrimaryColor = textPrimaryColor;
        this.textSecondaryColor = textSecondaryColor;
        this.textPrimaryColorInverse = textPrimaryColorInverse;
        this.textSecondaryColorInverse = textSecondaryColorInverse;
        this.fontScale = fontScale;
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
        this.surfaceColor = in.readInt();
        this.primaryColor = in.readInt();
        this.primaryColorDark = in.readInt();
        this.accentColor = in.readInt();
        this.accentColorDark = in.readInt();
        this.tintBackgroundColor = in.readInt();
        this.tintSurfaceColor = in.readInt();
        this.tintPrimaryColor = in.readInt();
        this.tintPrimaryColorDark = in.readInt();
        this.tintAccentColor = in.readInt();
        this.tintAccentColorDark = in.readInt();
        this.textPrimaryColor = in.readInt();
        this.textSecondaryColor = in.readInt();
        this.textPrimaryColorInverse = in.readInt();
        this.textSecondaryColorInverse = in.readInt();
        this.fontScale = in.readInt();
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
        dest.writeInt(surfaceColor);
        dest.writeInt(primaryColor);
        dest.writeInt(primaryColorDark);
        dest.writeInt(accentColor);
        dest.writeInt(accentColorDark);
        dest.writeInt(tintBackgroundColor);
        dest.writeInt(tintSurfaceColor);
        dest.writeInt(tintPrimaryColor);
        dest.writeInt(tintPrimaryColorDark);
        dest.writeInt(tintAccentColor);
        dest.writeInt(tintAccentColorDark);
        dest.writeInt(textPrimaryColor);
        dest.writeInt(textSecondaryColor);
        dest.writeInt(textPrimaryColorInverse);
        dest.writeInt(textSecondaryColorInverse);
        dest.writeInt(fontScale);
        dest.writeInt(cornerRadius);
        dest.writeInt(backgroundAware);
    }

    @Override
    public @StyleRes int getThemeRes() {
        return themeRes;
    }

    @Override
    public @NonNull DynamicAppTheme setThemeRes(@StyleRes int themeRes) {
        this.themeRes = themeRes;

        return this;
    }

    @Override
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

    @Override
    public @ColorInt int getBackgroundColor() {
        return getBackgroundColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundColor(
            @ColorInt int backgroundColor, boolean generateTint) {
        this.backgroundColor = backgroundColor;
        if (generateTint && backgroundColor != AUTO) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(backgroundColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve) {
        if (resolve && surfaceColor == AUTO) {
            return DynamicTheme.getInstance().generateSurfaceColor(getBackgroundColor());
        }

        return surfaceColor;
    }

    @Override
    public @ColorInt int getSurfaceColor() {
        return getSurfaceColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setSurfaceColor(
            @ColorInt int surfaceColor, boolean generateTint) {
        this.surfaceColor = surfaceColor;
        if (generateTint && surfaceColor != AUTO) {
            setTintSurfaceColor(DynamicColorUtils.getTintColor(surfaceColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setSurfaceColor(@ColorInt int surfaceColor) {
        return setSurfaceColor(surfaceColor, true);
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve) {
        if (resolve && primaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getPrimaryColor();
        }

        return primaryColor;
    }

    @Override
    public @ColorInt int getPrimaryColor() {
        return getPrimaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColor(
            @ColorInt int primaryColor, boolean generateTint) {
        this.primaryColor = primaryColor;
        if (generateTint && primaryColor != AUTO) {
            setTintPrimaryColor(DynamicColorUtils.getTintColor(primaryColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        if (resolve && primaryColorDark == AUTO) {
            return DynamicTheme.getInstance().generateDarkColor(getPrimaryColor());
        }

        return primaryColorDark;
    }

    @Override
    public @ColorInt int getPrimaryColorDark() {
        return getPrimaryColorDark(true);
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColorDark(
            @ColorInt int primaryColorDark, boolean generateTint) {
        this.primaryColorDark = primaryColorDark;
        if (generateTint && primaryColorDark != AUTO) {
            setTintPrimaryColorDark(DynamicColorUtils.getTintColor(primaryColorDark));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve) {
        if (resolve && accentColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getAccentColor();
        }

        return accentColor;
    }

    @Override
    public @ColorInt int getAccentColor() {
        return getAccentColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColor(
            @ColorInt int accentColor, boolean generateTint) {
        this.accentColor = accentColor;
        if (generateTint && accentColor != AUTO) {
            setTintAccentColor(DynamicColorUtils.getTintColor(accentColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve) {
        if (resolve && accentColorDark == AUTO) {
            return DynamicTheme.getInstance().generateDarkColor(getAccentColor());
        }

        return accentColorDark;
    }

    @Override
    public @ColorInt int getAccentColorDark() {
        return getAccentColorDark(true);
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColorDark(
            @ColorInt int accentColorDark, boolean generateTint) {
        this.accentColorDark = accentColorDark;
        if (generateTint && accentColorDark != AUTO) {
            setTintAccentColorDark(DynamicColorUtils.getTintColor(accentColorDark));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve) {
        if (resolve && tintBackgroundColor == AUTO) {
            return DynamicColorUtils.getTintColor(getBackgroundColor());
        }

        return tintBackgroundColor;
    }

    @Override
    public @ColorInt int getTintBackgroundColor() {
        return getTintBackgroundColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintBackgroundColor(@ColorInt int tintBackgroundColor) {
        this.tintBackgroundColor = tintBackgroundColor;

        return this;
    }

    @Override
    public @ColorInt int getTintSurfaceColor(boolean resolve) {
        if (resolve && tintSurfaceColor == AUTO) {
            return DynamicColorUtils.getTintColor(getSurfaceColor());
        }

        return tintSurfaceColor;
    }

    @Override
    public @ColorInt int getTintSurfaceColor() {
        return getTintSurfaceColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintSurfaceColor(@ColorInt int tintSurfaceColor) {
        this.tintSurfaceColor = tintSurfaceColor;

        return this;
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve) {
        if (resolve && tintPrimaryColor == AUTO) {
            return DynamicColorUtils.getTintColor(getPrimaryColor());
        }

        return tintPrimaryColor;
    }

    @Override
    public @ColorInt int getTintPrimaryColor() {
        return getTintPrimaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintPrimaryColor(@ColorInt int tintPrimaryColor) {
        this.tintPrimaryColor = tintPrimaryColor;

        return this;
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark(boolean resolve) {
        if (resolve && tintPrimaryColorDark == AUTO) {
            return DynamicColorUtils.getTintColor(getPrimaryColorDark());
        }

        return tintPrimaryColorDark;
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark() {
        return getTintPrimaryColorDark(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintPrimaryColorDark(@ColorInt int tintPrimaryColorDark) {
        this.tintPrimaryColorDark = tintPrimaryColorDark;

        return this;
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve) {
        if (resolve && tintAccentColor == AUTO) {
            return DynamicColorUtils.getTintColor(getAccentColor());
        }

        return tintAccentColor;
    }

    @Override
    public @ColorInt int getTintAccentColor() {
        return getTintAccentColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintAccentColor(@ColorInt int tintAccentColor) {
        this.tintAccentColor = tintAccentColor;

        return this;
    }

    @Override
    public @ColorInt int getTintAccentColorDark(boolean resolve) {
        if (resolve && tintAccentColorDark == AUTO) {
            return DynamicColorUtils.getTintColor(getAccentColorDark());
        }

        return tintAccentColorDark;
    }

    @Override
    public @ColorInt int getTintAccentColorDark() {
        return getTintAccentColorDark(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintAccentColorDark(@ColorInt int tintAccentColorDark) {
        this.tintAccentColorDark = tintAccentColorDark;

        return this;
    }


    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve) {
        if (resolve && textPrimaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getTextPrimaryColor();
        }

        return textPrimaryColor;
    }

    @Override
    public @ColorInt int getTextPrimaryColor() {
        return getTextPrimaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextPrimaryColor(
            @ColorInt int textPrimaryColor, boolean generateInverse) {
        this.textPrimaryColor = textPrimaryColor;
        if (generateInverse && textPrimaryColor != AUTO) {
            setTextPrimaryColorInverse(DynamicColorUtils.getTintColor(textPrimaryColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setTextPrimaryColor(@ColorInt int textPrimaryColor) {
        return setTextPrimaryColor(textPrimaryColor, true);
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve) {
        if (resolve && textSecondaryColor == AUTO) {
            return DynamicTheme.getInstance().getDefault().getTextSecondaryColor();
        }

        return textSecondaryColor;
    }

    @Override
    public @ColorInt int getTextSecondaryColor() {
        return getTextSecondaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextSecondaryColor(
            @ColorInt int textSecondaryColor, boolean generateInverse) {
        this.textSecondaryColor = textSecondaryColor;
        if (generateInverse && textSecondaryColor != AUTO) {
            setTextSecondaryColorInverse(DynamicColorUtils.getTintColor(textSecondaryColor));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setTextSecondaryColor(@ColorInt int textSecondaryColor) {
        return setTextSecondaryColor(textSecondaryColor, true);
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve) {
        if (resolve && textPrimaryColorInverse == AUTO) {
            return DynamicColorUtils.getTintColor(getTextPrimaryColor());
        }

        return textPrimaryColorInverse;
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse() {
        return getTextPrimaryColorInverse(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextPrimaryColorInverse(int textPrimaryColorInverse) {
        this.textPrimaryColorInverse = textPrimaryColorInverse;

        return this;
    }

    @Override
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve) {
        if (resolve && textSecondaryColorInverse == AUTO) {
            return DynamicColorUtils.getTintColor(getTextSecondaryColor());
        }

        return textSecondaryColorInverse;
    }

    @Override
    public @ColorInt int getTextSecondaryColorInverse() {
        return getTextSecondaryColorInverse(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextSecondaryColorInverse(int textSecondaryColorInverse) {
        this.textSecondaryColorInverse = textSecondaryColorInverse;

        return this;
    }

    @Override
    public int getFontScale(boolean resolve) {
        if (resolve && fontScale == AUTO) {
            return DynamicTheme.getInstance().getDefault().getFontScale();
        }

        return fontScale;
    }

    @Override
    public int getFontScale() {
        return getFontScale(true);
    }

    @Override
    public float getFontScaleRelative() {
        return getFontScale() / 100f;
    }

    @Override
    public @NonNull DynamicAppTheme setFontScale(int fontScale) {
        this.fontScale = fontScale;

        return this;
    }

    @Override
    public int getCornerRadius(boolean resolve) {
        if (resolve && cornerRadius == AUTO) {
            return DynamicTheme.getInstance().getDefault().getCornerRadius();
        }

        return cornerRadius;
    }

    @Override
    public int getCornerRadius() {
        return getCornerRadius(true);
    }

    @Override
    public int getCornerSizeDp(boolean resolve) {
        if (!resolve && cornerRadius == AUTO) {
            return AUTO;
        }

        return DynamicUnitUtils.convertPixelsToDp(getCornerRadius());
    }

    @Override
    public int getCornerSizeDp() {
        return getCornerSizeDp(true);
    }

    @Override
    public @NonNull DynamicAppTheme setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setCornerRadiusDp(float cornerSize) {
        return setCornerRadius(cornerSize == AUTO ? (int) cornerSize
                : DynamicUnitUtils.convertDpToPixels(cornerSize));
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware(boolean resolve) {
        if (resolve && backgroundAware == Theme.BackgroundAware.AUTO) {
            return DynamicTheme.getInstance().getDefault().getBackgroundAware();
        }

        return backgroundAware;
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return getBackgroundAware(true);
    }

    @Override
    public boolean isBackgroundAware() {
        return getBackgroundAware(true) != Theme.BackgroundAware.DISABLE;
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundAware(
            @Theme.BackgroundAware int backgroundAware) {
        this.backgroundAware = backgroundAware;

        return this;
    }

    @Override
    public boolean isDarkTheme() {
        return DynamicColorUtils.isColorDark(getBackgroundColor());
    }

    @Override
    public @NonNull DynamicAppTheme autoGenerateColors(boolean tint, boolean inverse) {
        if (tint) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(getBackgroundColor()));
            setTintSurfaceColor(DynamicColorUtils.getTintColor(getSurfaceColor()));
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

    @Override
    public @NonNull DynamicAppTheme autoGenerateColors() {
        return autoGenerateColors(true, true);
    }

    @Override
    public @NonNull String toJsonString() {
        return new Gson().toJson(new DynamicAppTheme(this));
    }

    @Override
    public @NonNull String toDynamicString() {
        return new GsonBuilder().setExclusionStrategies(new ExcludeStrategy())
                .registerTypeAdapter(DynamicAppTheme.class, new DynamicThemeTypeAdapter<>())
                .setPrettyPrinting().create().toJson(new DynamicAppTheme(this));
    }

    @Override
    public @NonNull String toString() {
        return "DynamicAppTheme{"
                + themeRes + backgroundColor + surfaceColor + primaryColor + primaryColorDark
                + accentColor + accentColorDark + tintBackgroundColor + tintSurfaceColor
                + tintPrimaryColor + tintPrimaryColorDark + tintAccentColor + tintAccentColorDark
                + textPrimaryColor + textSecondaryColor + textPrimaryColorInverse
                + textSecondaryColorInverse + cornerRadius + backgroundAware +
                '}';
    }
}
