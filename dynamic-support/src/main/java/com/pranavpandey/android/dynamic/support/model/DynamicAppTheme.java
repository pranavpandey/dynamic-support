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

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.DynamicColors;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.adapter.DynamicThemeTypeAdapter;
import com.pranavpandey.android.dynamic.theme.annotation.Exclude;
import com.pranavpandey.android.dynamic.theme.strategy.ExcludeStrategy;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicUnitUtils;

/**
 * An app theme to store various colors and attributes which can be modified at runtime.
 */
public class DynamicAppTheme extends AppTheme<DynamicAppTheme> implements Parcelable {

    /**
     * {@code true} if this theme is host.
     */
    @Exclude
    @SerializedName(Theme.Key.HOST)
    private boolean host;

    /**
     * Style resource used by this theme.
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
     * Contrast value used by this theme.
     */
    @SerializedName(Theme.Key.CONTRAST)
    private int contrast;

    /**
     * Opacity value used by this theme.
     */
    @SerializedName(Theme.Key.OPACITY)
    private int opacity;

    /**
     * Elevation functionality used by this theme.
     */
    @SerializedName(Theme.Key.ELEVATION)
    private @Theme.Elevation int elevation;

    /**
     * Style value used by this theme.
     */
    @SerializedName(Theme.Key.STYLE)
    private @Theme.Style int style;

    /**
     * Type value used by this theme.
     */
    @SerializedName(Theme.Key.TYPE)
    private @Theme int type;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicAppTheme() {
        this(Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO, Theme.AUTO,
                Theme.AUTO, Theme.Corner.AUTO, Theme.BackgroundAware.AUTO, Theme.Contrast.AUTO,
                Theme.Opacity.AUTO, Theme.Elevation.AUTO, Theme.Style.AUTO, Theme.APP);
    }

    /**
     * Constructor to initialize an object of this class from the theme string.
     *
     * @param theme The theme string to initialize the instance.
     */
    public DynamicAppTheme(@NonNull String theme) throws JsonSyntaxException {
        this(new GsonBuilder().setExclusionStrategies(new ExcludeStrategy()).registerTypeAdapter(
                DynamicAppTheme.class, new DynamicThemeTypeAdapter<>(new DynamicAppTheme()))
                .create().fromJson(DynamicThemeUtils.format(theme), DynamicAppTheme.class));
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param theme The dynamic theme to copy the theme.
     */
    public DynamicAppTheme(@NonNull AppTheme<?> theme) {
        this.themeRes = theme.getThemeRes();
        this.backgroundColor = theme.getBackgroundColor(false, false);
        this.surfaceColor = theme.getSurfaceColor(false, false);
        this.primaryColor = theme.getPrimaryColor(false, false);
        this.primaryColorDark = theme.getPrimaryColorDark(false, false);
        this.accentColor = theme.getAccentColor(false, false);
        this.accentColorDark = theme.getAccentColorDark(false, false);
        this.errorColor = theme.getErrorColor(false, false);
        this.tintBackgroundColor = theme.getTintBackgroundColor(false, false);
        this.tintSurfaceColor = theme.getTintSurfaceColor(false, false);
        this.tintPrimaryColor = theme.getTintPrimaryColor(false, false);
        this.tintPrimaryColorDark = theme.getTintPrimaryColorDark(false, false);
        this.tintAccentColor = theme.getTintAccentColor(false, false);
        this.tintAccentColorDark = theme.getTintAccentColorDark(false, false);
        this.tintErrorColor = theme.getTintErrorColor(false, false);
        this.textPrimaryColor = theme.getTextPrimaryColor(false, false);
        this.textSecondaryColor = theme.getTextSecondaryColor(false, false);
        this.textPrimaryColorInverse = theme.getTextPrimaryColorInverse(false, false);
        this.textSecondaryColorInverse = theme.getTextSecondaryColorInverse(false, false);
        this.fontScale = theme.getFontScale(false);
        this.cornerRadius = theme.getCornerRadius(false);
        this.backgroundAware = theme.getBackgroundAware(false);
        this.contrast = theme.getContrast(false);
        this.opacity = theme.getOpacity(false);
        this.elevation = theme.getElevation(false);
        this.style = theme.getStyle();
        this.type = theme.getType(false);
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
                Theme.AUTO, fontScale, cornerRadius, backgroundAware, Theme.Contrast.AUTO,
                Theme.Opacity.AUTO, Theme.Elevation.AUTO, Theme.Style.AUTO, Theme.APP);
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
                Theme.AUTO, Theme.Corner.AUTO, backgroundAware, Theme.Contrast.AUTO,
                Theme.Opacity.AUTO, Theme.Elevation.AUTO, Theme.Style.AUTO, Theme.APP);
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
     * @param contrast The contrast value for this theme.
     * @param opacity The opacity value for this theme.
     * @param elevation The elevation functionality for this theme.
     * @param style The style value for this theme.
     * @param type The type value for this theme.
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
            @Theme.BackgroundAware int backgroundAware, int contrast, int opacity,
            @Theme.Elevation int elevation, @Theme.Style int style, @Theme int type) {
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
        this.contrast = contrast;
        this.opacity = opacity;
        this.elevation = elevation;
        this.style = style;
        this.type = type;
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicAppTheme(@NonNull Parcel in) {
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
        this.contrast = in.readInt();
        this.opacity = in.readInt();
        this.elevation = in.readInt();
        this.style = in.readInt();
        this.type = in.readInt();
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

    @Override
    public int describeContents() {
        return hashCode();
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
        dest.writeInt(contrast);
        dest.writeInt(opacity);
        dest.writeInt(elevation);
        dest.writeInt(style);
        dest.writeInt(type);
    }

    @Override
    public @NonNull DynamicAppTheme getThemeFallback(boolean resolve) {
        if (resolve) {
            return DynamicTheme.getInstance().get(!isHost());
        }

        return DynamicTheme.getInstance().getDefault(!isHost());
    }

    @Override
    public boolean isHost() {
        return host;
    }

    @Override
    public @NonNull DynamicAppTheme setHost(boolean host) {
        this.host = host;

        return this;
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
            if (getThemeFallback(false).getBackgroundColor(false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Background color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.BACKGROUND);
            }

            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.BACKGROUND,
                        getBackgroundColor(true, false), this);
            }

            return getThemeFallback(false).getBackgroundColor(true, inverse);
        }

        return backgroundColor;
    }

    @Override
    public @ColorInt int getBackgroundColor(boolean resolve) {
        return getBackgroundColor(resolve, true);
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
            setTintBackgroundColor(Dynamic.getTintColor(getBackgroundColor(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setBackgroundColor(@ColorInt int backgroundColor) {
        return setBackgroundColor(backgroundColor, true);
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve, boolean inverse) {
        if (resolve && getTintBackgroundColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_BACKGROUND,
                        getTintBackgroundColor(true, false), this);
            }

            return Dynamic.getTintColor(getBackgroundColor(true, inverse), this);
        }

        return tintBackgroundColor;
    }

    @Override
    public @ColorInt int getTintBackgroundColor(boolean resolve) {
        return getTintBackgroundColor(resolve, true);
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
    public boolean isDarkTheme() {
        if (DynamicTheme.getInstance().isDynamicColors()) {
            return getType() == Theme.NIGHT || (getType() != Theme.DAY
                    && ((getBackgroundColor(false, false) == Theme.AUTO
                    && DynamicColorUtils.isColorDark(getDynamicColors().getOriginal(
                    Theme.ColorType.BACKGROUND, getBackgroundColor(true, false))))
                    || (getBackgroundColor(false, false) != Theme.AUTO
                    && DynamicColorUtils.isColorDark(getBackgroundColor(true, false)))));
        }

        return DynamicColorUtils.isColorDark(getBackgroundColor(true, false));
    }

    @Override
    public boolean isInverseTheme() {
        if (DynamicTheme.getInstance().isDynamicColors()
                && (DynamicTheme.getInstance().isSystemColor()
                || DynamicTheme.getInstance().isWallpaperColor())) {
            return false;
        }

        return isDarkTheme() != getThemeFallback(true).isDarkTheme();
    }

    @Override
    public boolean isShowDividers() {
        return getAccentColorDark() != getPrimaryColor();
    }

    @Override
    public @Theme.Elevation int getElevation(boolean resolve) {
        if (resolve && getElevation(false) == Theme.Elevation.AUTO) {
            return getThemeFallback(false).getElevation();
        }

        return elevation;
    }

    @Override
    public @Theme.Elevation int getElevation() {
        return getElevation(true);
    }

    @Override
    public boolean isElevation() {
        return getElevation(true) != Theme.Elevation.DISABLE;
    }

    @Override
    public @NonNull DynamicAppTheme setElevation(@Theme.Elevation int elevation) {
        this.elevation = elevation;

        return this;
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve, boolean inverse) {
        if (resolve && getSurfaceColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.SURFACE,
                        getSurfaceColor(true, false), this);
            }

            return DynamicTheme.getInstance().generateSurfaceColor(getBackgroundColor());
        }

        return surfaceColor;
    }

    @Override
    public @ColorInt int getSurfaceColor(boolean resolve) {
        return getSurfaceColor(resolve, true);
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
            setTintSurfaceColor(Dynamic.getTintColor(getSurfaceColor(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setSurfaceColor(@ColorInt int surfaceColor) {
        return setSurfaceColor(surfaceColor, true);
    }

    @Override
    public @ColorInt int getTintSurfaceColor(boolean resolve, boolean inverse) {
        if (resolve && getTintSurfaceColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_SURFACE,
                        getTintSurfaceColor(true, false), this);
            }

            return Dynamic.getTintColor(getSurfaceColor(), this);
        }

        return tintSurfaceColor;
    }

    @Override
    public @ColorInt int getTintSurfaceColor(boolean resolve) {
        return getTintSurfaceColor(resolve, true);
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
    public boolean isBackgroundSurface() {
        return !isElevation() || DynamicColorUtils.removeAlpha(getBackgroundColor())
                == DynamicColorUtils.removeAlpha(getSurfaceColor());
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && getPrimaryColor(false, false) == Theme.AUTO) {
            if (getThemeFallback(false).getPrimaryColor(false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Primary color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.PRIMARY);
            }

            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.PRIMARY,
                        getPrimaryColor(true, false), this);
            }

            return getThemeFallback(false).getPrimaryColor();
        }

        return primaryColor;
    }

    @Override
    public @ColorInt int getPrimaryColor(boolean resolve) {
        return getPrimaryColor(resolve, true);
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
            setTintPrimaryColor(Dynamic.getTintColor(getPrimaryColor(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColor(@ColorInt int primaryColor) {
        return setPrimaryColor(primaryColor, true);
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve, boolean inverse) {
        if (resolve && getPrimaryColorDark(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.PRIMARY_DARK,
                        getPrimaryColorDark(true, false), this);
            }

            return DynamicTheme.getInstance().generateSystemColor(getPrimaryColor());
        }

        return primaryColorDark;
    }

    @Override
    public @ColorInt int getPrimaryColorDark(boolean resolve) {
        return getPrimaryColorDark(resolve, true);
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
            setTintPrimaryColorDark(Dynamic.getTintColor(getPrimaryColorDark(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setPrimaryColorDark(@ColorInt int primaryColorDark) {
        return setPrimaryColorDark(primaryColorDark, true);
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve, boolean inverse) {
        if (resolve && getTintPrimaryColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_PRIMARY,
                        getTintPrimaryColor(true, false), this);
            }

            return Dynamic.getTintColor(getPrimaryColor(), this);
        }

        return tintPrimaryColor;
    }

    @Override
    public @ColorInt int getTintPrimaryColor(boolean resolve) {
        return getTintPrimaryColor(resolve, true);
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
    public @ColorInt int getTintPrimaryColorDark(boolean resolve, boolean inverse) {
        if (resolve && getTintPrimaryColorDark(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_PRIMARY_DARK,
                        getTintPrimaryColorDark(true, false), this);
            }

            return Dynamic.getTintColor(getPrimaryColorDark(), this);
        }

        return tintPrimaryColorDark;
    }

    @Override
    public @ColorInt int getTintPrimaryColorDark(boolean resolve) {
        return getTintPrimaryColorDark(resolve, true);
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
    public @ColorInt int getAccentColor(boolean resolve, boolean inverse) {
        if (resolve && getAccentColor(false, false) == Theme.AUTO) {
            if (getThemeFallback(false).getAccentColor(false, false) == Theme.AUTO) {
                Log.w(getClass().getSimpleName(), "Accent color cannot " +
                        "be auto for the default theme, trying to use the default color.");
                return DynamicTheme.getInstance().getDefaultColor(Theme.ColorType.ACCENT);
            }

            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.ACCENT,
                        getAccentColor(true, false), this);
            }

            return getThemeFallback(false).getAccentColor();
        }

        return accentColor;
    }

    @Override
    public @ColorInt int getAccentColor(boolean resolve) {
        return getAccentColor(resolve, true);
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
            setTintAccentColor(Dynamic.getTintColor(getAccentColor(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColor(@ColorInt int accentColor) {
        return setAccentColor(accentColor, true);
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve, boolean inverse) {
        if (resolve && getAccentColorDark(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.ACCENT_DARK,
                        getAccentColorDark(true, false), this);
            }

            return DynamicTheme.getInstance().generateSystemSecondaryColor(getBackgroundColor());
        }

        return accentColorDark;
    }

    @Override
    public @ColorInt int getAccentColorDark(boolean resolve) {
        return getAccentColorDark(resolve, true);
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
            setTintAccentColorDark(Dynamic.getTintColor(getAccentColorDark(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setAccentColorDark(@ColorInt int accentColorDark) {
        return setAccentColorDark(accentColorDark, true);
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve, boolean inverse) {
        if (resolve && getTintAccentColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_ACCENT,
                        getTintAccentColor(true, false), this);
            }

            return Dynamic.getTintColor(getAccentColor(), this);
        }

        return tintAccentColor;
    }

    @Override
    public @ColorInt int getTintAccentColor(boolean resolve) {
        return getTintAccentColor(resolve, true);
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
    public @ColorInt int getTintAccentColorDark(boolean resolve, boolean inverse) {
        if (resolve && getTintAccentColorDark(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_ACCENT_DARK,
                        getTintAccentColorDark(true, false), this);
            }

            return Dynamic.getTintColor(getAccentColorDark(), this);
        }

        return tintAccentColorDark;
    }

    @Override
    public @ColorInt int getTintAccentColorDark(boolean resolve) {
        return getTintAccentColorDark(resolve, true);
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
    public int getErrorColor(boolean resolve, boolean inverse) {
        if (resolve && getErrorColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.ERROR,
                        getErrorColor(true, false), this);
            }

            return DynamicTheme.getInstance().generateErrorColor(
                    getPrimaryColor(), getAccentColor());
        }

        return errorColor;
    }

    @Override
    public @ColorInt int getErrorColor(boolean resolve) {
        return getErrorColor(resolve, true);
    }

    @Override
    public int getErrorColor() {
        return getErrorColor(true);
    }

    @Override
    public @NonNull DynamicAppTheme setErrorColor(int errorColor, boolean generateTint) {
        this.errorColor = errorColor;
        if (generateTint) {
            setTintErrorColor(Dynamic.getTintColor(getErrorColor(), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme setErrorColor(int errorColor) {
        return setErrorColor(errorColor, true);
    }

    @Override
    public @ColorInt int getTintErrorColor(boolean resolve, boolean inverse) {
        if (resolve && getTintErrorColor(false, false) == Theme.AUTO) {
            if (inverse && DynamicTheme.getInstance().isDynamicColors()) {
                return getDynamicColors().getMutated(Theme.ColorType.TINT_ERROR,
                        getTintErrorColor(true, false), this);
            }

            return Dynamic.getTintColor(getErrorColor(), this);
        }

        return tintErrorColor;
    }

    @Override
    public @ColorInt int getTintErrorColor(boolean resolve) {
        return getTintErrorColor(resolve, true);
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
            setTextPrimaryColorInverse(Dynamic.getTintColor(getTextPrimaryColor(), this));
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
            setTextSecondaryColorInverse(Dynamic.getTintColor(getTextSecondaryColor(), this));
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
    public @ColorInt int getHighlightColor(@ColorInt int contrastWithColor) {
        if (isBackgroundAware() && contrastWithColor != Theme.Color.UNKNOWN) {
            return Dynamic.withContrastRatio(getPrimaryColor(),
                    contrastWithColor, this);
        }

        return getPrimaryColor();
    }

    @Override
    public @ColorInt int getHighlightColor() {
        return getHighlightColor(getBackgroundColor());
    }

    @Override
    public @NonNull DynamicAppTheme autoGenerateColors(boolean tint, boolean inverse) {
        if (tint) {
            setTintBackgroundColor(Dynamic.getTintColor(getBackgroundColor(), this));
            setTintSurfaceColor(Dynamic.getTintColor(getSurfaceColor(), this));
            setTintPrimaryColor(Dynamic.getTintColor(getPrimaryColor(), this));
            setTintPrimaryColorDark(Dynamic.getTintColor(getAccentColorDark(), this));
            setTintAccentColor(Dynamic.getTintColor(getAccentColor(), this));
            setTintAccentColorDark(Dynamic.getTintColor(getAccentColorDark(), this));
            setTintErrorColor(Dynamic.getTintColor(getErrorColor(), this));
        }

        if (inverse) {
            setTextPrimaryColorInverse(Dynamic.getTintColor(
                    getTextPrimaryColor(true, false), this));
            setTextSecondaryColorInverse(Dynamic.getTintColor(
                    getTextSecondaryColor(true, false), this));
        }

        return this;
    }

    @Override
    public @NonNull DynamicAppTheme autoGenerateColors() {
        return autoGenerateColors(true, true);
    }

    @Override
    public int getFontScale(boolean resolve) {
        if (resolve && getFontScale(false) == Theme.Font.AUTO) {
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
        return Math.round(((getFontScale() / (float) Theme.Font.DEFAULT)
                * Resources.getSystem().getConfiguration().fontScale)
                * Theme.Font.FACTOR) / Theme.Font.FACTOR;
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
        if (resolve && getCornerRadius(false) == Theme.Corner.AUTO) {
            return getThemeFallback(false).getCornerRadius();
        }

        return cornerRadius;
    }

    @Override
    public int getCornerRadius() {
        return getCornerRadius(true);
    }

    @Override
    public @NonNull DynamicAppTheme setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;

        return this;
    }

    @Override
    public int getCornerSize(boolean resolve) {
        if (!resolve && getCornerRadius(false) == Theme.Corner.AUTO) {
            return Theme.Corner.AUTO;
        }

        return DynamicUnitUtils.convertPixelsToDp(getCornerRadius());
    }

    @Override
    public int getCornerSize() {
        return getCornerSize(true);
    }

    @Override
    public @NonNull DynamicAppTheme setCornerSize(float cornerSize) {
        return setCornerRadius(cornerSize == Theme.Corner.AUTO ? (int) cornerSize
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
    public int getContrast(boolean resolve) {
        if (resolve && getContrast(false) == Theme.Contrast.AUTO) {
            return getThemeFallback(false).getContrast();
        }

        return Math.min(Theme.Contrast.MAX, contrast);
    }

    @Override
    public int getContrast() {
        return getContrast(true);
    }

    @Override
    public float getContrastRatio() {
        return getContrast() / (float) Theme.Contrast.MAX;
    }

    @Override
    public @NonNull DynamicAppTheme setContrast(int contrast) {
        this.contrast = contrast;

        return this;
    }

    @Override
    public @ColorInt int getStrokeColor() {
        return getTintBackgroundColor();
    }

    @Override
    public int getOpacity(boolean resolve) {
        if (resolve && getOpacity(false) == Theme.Opacity.AUTO) {
            return getThemeFallback(false).getOpacity();
        }

        return Math.min(Theme.Opacity.MAX, opacity);
    }

    @Override
    public int getOpacity() {
        return getOpacity(true);
    }

    @Override
    public @NonNull DynamicAppTheme setOpacity(int opacity) {
        this.opacity = opacity;

        return this;
    }

    @Override
    public float getAlpha() {
        return getOpacity() / (float) Theme.Opacity.MAX;
    }

    @Override
    public boolean isTranslucent() {
        return getOpacity() < Theme.Opacity.MAX;
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
    public int getType(boolean resolve) {
        return type;
    }

    @Override
    public @Theme int getType() {
        return getType(true);
    }

    @Override
    public @NonNull DynamicAppTheme setType(@Theme int type) {
        this.type = type;

        return this;
    }

    @Override
    public @NonNull DynamicColors getDynamicColors() {
        return DynamicTheme.getInstance().getColors();
    }

    @Override
    public @NonNull String toJsonString(boolean resolve, boolean inverse) {
        return new GsonBuilder().registerTypeAdapter(DynamicAppTheme.class,
                new DynamicThemeTypeAdapter<>(new DynamicAppTheme(), resolve, inverse))
                .create().toJson(new DynamicAppTheme(this));
    }

    @Override
    public @NonNull String toDynamicString() {
        return new GsonBuilder().setExclusionStrategies(new ExcludeStrategy()).registerTypeAdapter(
                DynamicAppTheme.class, new DynamicThemeTypeAdapter<>(new DynamicAppTheme()))
                .setPrettyPrinting().create().toJson(new DynamicAppTheme(this));
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
                + getStyle() + getType() + '}';
    }
}
