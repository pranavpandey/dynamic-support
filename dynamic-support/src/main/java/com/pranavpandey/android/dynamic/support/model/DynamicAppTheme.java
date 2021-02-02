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

package com.pranavpandey.android.dynamic.support.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
     * Error color used by this theme.
     */
    @SerializedName(Theme.Key.ERROR)
    private @ColorInt int errorColor;

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
     * Tint color according to the error color.
     */
    @SerializedName(Theme.Key.TINT_ERROR)
    private @ColorInt int tintErrorColor;

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
     * Style value used by this theme.
     */
    @SerializedName(Theme.Key.STYLE)
    private int style;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicAppTheme() {
        this(Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.BackgroundAware.AUTO, Theme.Style.AUTO);
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
     * @param dynamicTheme The dynamic theme to copy the theme.
     */
    public DynamicAppTheme(@NonNull AppTheme<?> dynamicTheme) {
        this.themeRes = dynamicTheme.getThemeRes();
        this.backgroundColor = dynamicTheme.getBackgroundColor(false);
        this.surfaceColor = dynamicTheme.getSurfaceColor(false);
        this.primaryColor = dynamicTheme.getPrimaryColor(false);
        this.primaryColorDark = dynamicTheme.getPrimaryColorDark(false);
        this.accentColor = dynamicTheme.getAccentColor(false);
        this.accentColorDark = dynamicTheme.getAccentColorDark(false);
        this.errorColor = dynamicTheme.getErrorColor(false);
        this.tintBackgroundColor = dynamicTheme.getTintBackgroundColor(false);
        this.tintSurfaceColor = dynamicTheme.getTintSurfaceColor(false);
        this.tintPrimaryColor = dynamicTheme.getTintPrimaryColor(false);
        this.tintPrimaryColorDark = dynamicTheme.getTintPrimaryColorDark(false);
        this.tintAccentColor = dynamicTheme.getTintAccentColor(false);
        this.tintAccentColorDark = dynamicTheme.getTintAccentColorDark(false);
        this.tintErrorColor = dynamicTheme.getTintErrorColor(false);
        this.textPrimaryColor = dynamicTheme.getTextPrimaryColor(false, false);
        this.textSecondaryColor = dynamicTheme.getTextSecondaryColor(false, false);
        this.textPrimaryColorInverse = dynamicTheme.getTextPrimaryColorInverse(false, false);
        this.textSecondaryColorInverse = dynamicTheme.getTextSecondaryColorInverse(false, false);
        this.fontScale = dynamicTheme.getFontScale(false);
        this.cornerRadius = dynamicTheme.getCornerRadius(false);
        this.backgroundAware = dynamicTheme.getBackgroundAware(false);
        this.style = dynamicTheme.getStyle();
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param primaryColor The primary color for this theme.
     * @param primaryColorDark The dark primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param fontScale The text scaling factor for this theme.
     * @param cornerRadius The corner size for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int primaryColor,
            @ColorInt int primaryColorDark, @ColorInt int accentColor, int fontScale,
            int cornerRadius, @Theme.BackgroundAware int backgroundAware) {
        this(primaryColor, primaryColorDark, accentColor,
                Theme.AUTO, fontScale, cornerRadius, backgroundAware);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param primaryColor The primary color for this theme.
     * @param primaryColorDark The dark primary color for this theme.
     * @param accentColor The accent color for this theme.
     * @param errorColor The error color for this theme.
     * @param fontScale The text scaling factor for this theme.
     * @param cornerRadius The corner size for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     */
    public DynamicAppTheme(@ColorInt int primaryColor, @ColorInt int primaryColorDark,
            @ColorInt int accentColor, @ColorInt int errorColor, int fontScale,
            int cornerRadius, @Theme.BackgroundAware int backgroundAware) {
        this(Theme.AUTO, Theme.AUTO, primaryColor, primaryColorDark, accentColor,
                accentColor, errorColor, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, fontScale, cornerRadius, backgroundAware, Theme.Style.AUTO);
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
        this(backgroundColor, Theme.AUTO, primaryColor, primaryColor, accentColor,
                accentColor, Theme.AUTO, tintBackgroundColor, Theme.AUTO, tintPrimaryColor,
                tintPrimaryColor, tintAccentColor, tintAccentColor, Theme.AUTO, textPrimaryColor,
                textSecondaryColor, textPrimaryColorInverse, textSecondaryColorInverse,
                Theme.AUTO, Theme.AUTO, backgroundAware, Theme.Style.AUTO);
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
     * @param errorColor The error color for this theme.
     * @param tintBackgroundColor Tint color according to the background color.
     * @param tintSurfaceColor Tint color according to the surface color.
     * @param tintPrimaryColor Tint color according to the primary color.
     * @param tintPrimaryColorDark Tint color according to the dark primary color.
     * @param tintAccentColor Tint color according to the accent color.
     * @param tintAccentColorDark Tint color according to the dark accent color.
     * @param tintErrorColor Tint color according to the error color.
     * @param textPrimaryColor The primary text color for this theme.
     * @param textSecondaryColor The secondary text color for this theme.
     * @param textPrimaryColorInverse Inverse color for the primary text color.
     * @param textSecondaryColorInverse Inverse color for the secondary text color.
     * @param fontScale The text scaling factor for this theme.
     * @param cornerRadius The corner size for this theme.
     * @param backgroundAware The background aware functionality for this theme.
     * @param style The style value for this theme.
     */
    public DynamicAppTheme(@ColorInt int backgroundColor, @ColorInt int surfaceColor,
            @ColorInt int primaryColor, @ColorInt int primaryColorDark,
            @ColorInt int accentColor, @ColorInt int accentColorDark, @ColorInt int errorColor,
            @ColorInt int tintBackgroundColor, @ColorInt int tintSurfaceColor,
            @ColorInt int tintPrimaryColor, @ColorInt int tintPrimaryColorDark,
            @ColorInt int tintAccentColor, @ColorInt int tintAccentColorDark,
            @ColorInt int tintErrorColor, @ColorInt int textPrimaryColor,
            @ColorInt int textSecondaryColor, @ColorInt int textPrimaryColorInverse,
            @ColorInt int textSecondaryColorInverse, int fontScale, int cornerRadius,
            @Theme.BackgroundAware int backgroundAware, @Theme.Style int style) {
        this.themeRes = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
        this.backgroundColor = backgroundColor;
        this.surfaceColor = surfaceColor;
        this.primaryColor = primaryColor;
        this.primaryColorDark = primaryColorDark;
        this.accentColor = accentColor;
        this.accentColorDark = accentColorDark;
        this.errorColor = errorColor;
        this.tintBackgroundColor = tintBackgroundColor;
        this.tintSurfaceColor = tintSurfaceColor;
        this.tintPrimaryColor = tintPrimaryColor;
        this.tintPrimaryColorDark = tintPrimaryColorDark;
        this.tintAccentColor = tintAccentColor;
        this.tintAccentColorDark = tintAccentColorDark;
        this.tintErrorColor = tintErrorColor;
        this.textPrimaryColor = textPrimaryColor;
        this.textSecondaryColor = textSecondaryColor;
        this.textPrimaryColorInverse = textPrimaryColorInverse;
        this.textSecondaryColorInverse = textSecondaryColorInverse;
        this.fontScale = fontScale;
        this.cornerRadius = cornerRadius;
        this.backgroundAware = backgroundAware;
        this.style = style;
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
        this.errorColor = in.readInt();
        this.tintBackgroundColor = in.readInt();
        this.tintSurfaceColor = in.readInt();
        this.tintPrimaryColor = in.readInt();
        this.tintPrimaryColorDark = in.readInt();
        this.tintAccentColor = in.readInt();
        this.tintAccentColorDark = in.readInt();
        this.tintErrorColor = in.readInt();
        this.textPrimaryColor = in.readInt();
        this.textSecondaryColor = in.readInt();
        this.textPrimaryColorInverse = in.readInt();
        this.textSecondaryColorInverse = in.readInt();
        this.fontScale = in.readInt();
        this.cornerRadius = in.readInt();
        this.backgroundAware = in.readInt();
        this.style = in.readInt();
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
        dest.writeInt(errorColor);
        dest.writeInt(tintBackgroundColor);
        dest.writeInt(tintSurfaceColor);
        dest.writeInt(tintPrimaryColor);
        dest.writeInt(tintPrimaryColorDark);
        dest.writeInt(tintAccentColor);
        dest.writeInt(tintAccentColorDark);
        dest.writeInt(tintErrorColor);
        dest.writeInt(textPrimaryColor);
        dest.writeInt(textSecondaryColor);
        dest.writeInt(textPrimaryColorInverse);
        dest.writeInt(textSecondaryColorInverse);
        dest.writeInt(fontScale);
        dest.writeInt(cornerRadius);
        dest.writeInt(backgroundAware);
        dest.writeInt(style);
    }

    @Override
    public @NonNull DynamicAppTheme getThemeFallback(boolean resolve) {
        if (resolve) {
            return DynamicTheme.getInstance().get();
        }

        return DynamicTheme.getInstance().getDefault();
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
    public @ColorInt int getBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && backgroundColor == Theme.AUTO) {
            if (getThemeFallback(false).getBackgroundColor(
                    false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Background color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.BACKGROUND);
            }

            return getThemeFallback(false).getBackgroundColor(true, inverse);
        }

        return backgroundColor;
    }

    @Override
    public @ColorInt int getBackgroundColor(boolean resolve) {
        return getBackgroundColor(resolve, false);
    }

    @Override
    public @ColorInt int getBackgroundColor() {
        return getBackgroundColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundColor(
            @ColorInt int backgroundColor, boolean generateTint) {
        this.backgroundColor = backgroundColor;
        if (generateTint) {
            setTintBackgroundColor(DynamicColorUtils.getTintColor(getBackgroundColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    @Override
    public @ColorInt int getStrokeColor() {
        return DynamicTheme.getInstance().generateStrokeColor(getBackgroundColor());
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve) {
        if (resolve && getSurfaceColor(false) == Theme.AUTO) {
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
        if (generateTint) {
            setTintSurfaceColor(DynamicColorUtils.getTintColor(getSurfaceColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setSurfaceColor(@ColorInt int surfaceColor) {
        return setSurfaceColor(surfaceColor, true);
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve) {
        if (resolve && getPrimaryColor(false) == Theme.AUTO) {
            if (getThemeFallback(false).getPrimaryColor(false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Primary color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.PRIMARY);
            }
            return getThemeFallback(false).getPrimaryColor();
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
        if (generateTint) {
            setTintPrimaryColor(DynamicColorUtils.getTintColor(getPrimaryColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        if (resolve && getPrimaryColorDark(false) == Theme.AUTO) {
            return DynamicTheme.getInstance().generateSystemColor(getPrimaryColor());
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
        if (generateTint) {
            setTintPrimaryColorDark(DynamicColorUtils.getTintColor(getPrimaryColorDark()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve) {
        if (resolve && getAccentColor(false) == Theme.AUTO) {
            if (getThemeFallback(false).getAccentColor(false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Accent color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.ACCENT);
            }
            return getThemeFallback(false).getAccentColor();
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
        if (generateTint) {
            setTintAccentColor(DynamicColorUtils.getTintColor(getAccentColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve) {
        if (resolve && getAccentColorDark(false) == Theme.AUTO) {
            return DynamicTheme.getInstance().generateSystemSecondaryColor(getBackgroundColor());
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
        if (generateTint) {
            setTintAccentColorDark(DynamicColorUtils.getTintColor(getAccentColorDark()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    @Override
    public int getErrorColor(boolean resolve) {
        if (resolve && getErrorColor(false) == Theme.AUTO) {
            return DynamicTheme.getInstance().generateErrorColor(
                    getPrimaryColor(), getAccentColor());
        }

        return errorColor;
    }

    @Override
    public int getErrorColor() {
        return getErrorColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setErrorColor(int errorColor, boolean generateTint) {
        this.errorColor = errorColor;
        if (generateTint) {
            setTintErrorColor(DynamicColorUtils.getTintColor(getErrorColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setErrorColor(int errorColor) {
        return setErrorColor(errorColor, true);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && getTintBackgroundColor(false, false) == Theme.AUTO) {
            return DynamicColorUtils.getTintColor(getBackgroundColor(true, inverse));
        }

        return tintBackgroundColor;
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve) {
        return getTintBackgroundColor(resolve, false);
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
        if (resolve && getTintSurfaceColor(false) == Theme.AUTO) {
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
        if (resolve && getTintPrimaryColor(false) == Theme.AUTO) {
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
        if (resolve && getTintPrimaryColorDark(false) == Theme.AUTO) {
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
        if (resolve && getTintAccentColor(false) == Theme.AUTO) {
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
        if (resolve && getTintAccentColorDark(false) == Theme.AUTO) {
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
    public @ColorInt int getTintErrorColor(boolean resolve) {
        if (resolve && getTintErrorColor(false) == Theme.AUTO) {
            return DynamicColorUtils.getTintColor(getErrorColor());
        }

        return tintErrorColor;
    }

    @Override
    public @ColorInt int getTintErrorColor() {
        return getTintErrorColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTintErrorColor(@ColorInt int tintErrorColor) {
        this.tintErrorColor = tintErrorColor;

        return this;
    }

    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && getTextPrimaryColor(false, false) == Theme.AUTO) {
            if (inverse && isInverseTheme()) {
                return getTextPrimaryColorInverse(true, false);
            }

            if (getThemeFallback(false).getTextPrimaryColor(
                    false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Text primary color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.TEXT_PRIMARY);
            }

            return getThemeFallback(false).getTextPrimaryColor(true, inverse);
        }

        if (inverse && isInverseTheme()) {
            return getTextPrimaryColorInverse(resolve, false);
        }

        return textPrimaryColor;
    }

    @Override
    public @ColorInt int getTextPrimaryColor(boolean resolve) {
        return getTextPrimaryColor(resolve, true);
    }

    @Override
    public @ColorInt int getTextPrimaryColor() {
        return getTextPrimaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextPrimaryColor(
            @ColorInt int textPrimaryColor, boolean generateInverse) {
        this.textPrimaryColor = textPrimaryColor;
        if (generateInverse) {
            setTextPrimaryColorInverse(DynamicColorUtils.getTintColor(getTextPrimaryColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setTextPrimaryColor(@ColorInt int textPrimaryColor) {
        return setTextPrimaryColor(textPrimaryColor, true);
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve, boolean inverse) {
        if (resolve && getTextSecondaryColor(false, false) == Theme.AUTO) {
            if (inverse && isInverseTheme()) {
                return getTextSecondaryColorInverse(true, false);
            }

            if (getThemeFallback(false).getTextSecondaryColor(
                    false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Text secondary color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.TEXT_SECONDARY);
            }

            return getThemeFallback(false).getTextSecondaryColor(true, inverse);
        }

        if (inverse && isInverseTheme()) {
            return getTextSecondaryColorInverse(resolve, false);
        }

        return textSecondaryColor;
    }

    @Override
    public @ColorInt int getTextSecondaryColor(boolean resolve) {
        return getTextSecondaryColor(resolve, true);
    }

    @Override
    public @ColorInt int getTextSecondaryColor() {
        return getTextSecondaryColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setTextSecondaryColor(
            @ColorInt int textSecondaryColor, boolean generateInverse) {
        this.textSecondaryColor = textSecondaryColor;
        if (generateInverse) {
            setTextSecondaryColorInverse(DynamicColorUtils.getTintColor(getTextSecondaryColor()));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setTextSecondaryColor(@ColorInt int textSecondaryColor) {
        return setTextSecondaryColor(textSecondaryColor, true);
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve, boolean inverse) {
        if (resolve && getTextPrimaryColorInverse(false, false) == Theme.AUTO) {
            if (inverse && isInverseTheme()) {
                return getTextPrimaryColor(true, false);
            }

            if (getThemeFallback(false).getTextPrimaryColorInverse(
                    false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Text primary color inverse cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(
                        Theme.ColorType.TEXT_PRIMARY_INVERSE);
            }
            
            return getThemeFallback(false).getTextPrimaryColorInverse(true, inverse);
        }

        if (inverse && isInverseTheme()) {
            return getTextPrimaryColor(resolve, false);
        }

        return textPrimaryColorInverse;
    }

    @Override
    public @ColorInt int getTextPrimaryColorInverse(boolean resolve) {
        return getTextPrimaryColorInverse(resolve, true);
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
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve, boolean inverse) {
        if (resolve && getTextSecondaryColorInverse(false, false) == Theme.AUTO) {
            if (inverse && isInverseTheme()) {
                return getTextSecondaryColor(true, false);
            }

            if (getThemeFallback(false).getTextSecondaryColorInverse(
                    false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Text secondary color inverse cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(
                        Theme.ColorType.TEXT_SECONDARY_INVERSE);
            }

            return getThemeFallback(false).getTextSecondaryColorInverse(true, inverse);
        }

        if (inverse && isInverseTheme()) {
            return getTextSecondaryColor(resolve, false);
        }

        return textSecondaryColorInverse;
    }

    @Override
    public @ColorInt int getTextSecondaryColorInverse(boolean resolve) {
        return getTextSecondaryColorInverse(resolve, true);
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
        if (resolve && getFontScale(false) == Theme.AUTO) {
            return getThemeFallback(false).getFontScale();
        }

        return fontScale;
    }

    @Override
    public int getFontScale() {
        return getFontScale(true);
    }

    @Override
    public float getFontScaleRelative() {
        // Rounding off to one decimal place to avoid inconsistency on configuration changes,
        // especially on multi-window mode.
        return Math.round(((getFontScale() / 100f)
                * Resources.getSystem().getConfiguration().fontScale) * 10f) / 10.0f;
    }

    @Override
    public @NonNull DynamicAppTheme setFontScale(int fontScale) {
        this.fontScale = fontScale;

        return this;
    }

    @Override
    public boolean isFontScale() {
        return getFontScale() != getThemeFallback(false).getFontScale();
    }

    @Override
    public int getCornerRadius(boolean resolve) {
        if (resolve && getCornerRadius(false) == Theme.AUTO) {
            return getThemeFallback(false).getCornerRadius();
        }

        return cornerRadius;
    }

    @Override
    public int getCornerRadius() {
        return getCornerRadius(true);
    }

    @Override
    public int getCornerSizeDp(boolean resolve) {
        if (!resolve && getCornerRadius(false) == Theme.AUTO) {
            return Theme.AUTO;
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
        return setCornerRadius(cornerSize == Theme.AUTO ? (int) cornerSize
                : DynamicUnitUtils.convertDpToPixels(cornerSize));
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware(boolean resolve) {
        if (resolve && getBackgroundAware(false) == Theme.BackgroundAware.AUTO) {
            return getThemeFallback(false).getBackgroundAware();
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
    public @Theme.Style int getStyle() {
        return style;
    }

    @Override
    public @NonNull DynamicAppTheme setStyle(@Theme.Style int style) {
        this.style = style;

        return this;
    }

    @Override
    public boolean isDarkTheme() {
        return DynamicColorUtils.isColorDark(getBackgroundColor());
    }

    @Override
    public boolean isInverseTheme() {
        return (isDarkTheme() && !getThemeFallback(true).isDarkTheme())
                || (!isDarkTheme() && getThemeFallback(true).isDarkTheme());
    }

    @Override
    public boolean isBackgroundSurface() {
        return DynamicColorUtils.removeAlpha(getBackgroundColor())
                == DynamicColorUtils.removeAlpha(getSurfaceColor());
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
            setTintErrorColor(DynamicColorUtils.getTintColor(getErrorColor()));
        }

        if (inverse) {
            setTextPrimaryColorInverse(DynamicColorUtils.getTintColor(
                    getTextPrimaryColor(true, false)));
            setTextSecondaryColorInverse(DynamicColorUtils.getTintColor(
                    getTextSecondaryColor(true, false)));
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
        return getClass().getSimpleName()
                + "{" + getThemeRes()
                + getBackgroundColor(false)
                + getSurfaceColor(false)
                + getPrimaryColor(false)
                + getPrimaryColorDark(false)
                + getAccentColor(false)
                + getAccentColorDark(false)
                + getErrorColor(false)
                + getTintBackgroundColor(false)
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
                + getStyle() + '}';
    }

    public @ColorInt int getHighlightColor() {
        return DynamicColorUtils.getContrastColor(getPrimaryColor(), getBackgroundColor());
    }
}
