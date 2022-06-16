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

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.TargetApi;
import android.app.WallpaperColors;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to handle dynamic colors generation.
 */
public class DynamicColors implements Parcelable {

    /**
     * Factor to generate shades of a color.
     */
    private static final float FACTOR = 0.8f;

    /**
     * Map to store the original colors.
     */
    private final Map<Integer, Integer> mOriginal;

    /**
     * Map to store the mutated colors.
     */
    private final Map<Integer, Integer> mMutated;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicColors() {
        this(new HashMap<>());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param original The original colors to be handled.
     */
    public DynamicColors(@NonNull Map<Integer, Integer> original) {
        this.mOriginal = new HashMap<>(original);
        this.mMutated = new HashMap<>(original);
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicColors> CREATOR =
            new Parcelable.Creator<DynamicColors>() {
        @Override
        public DynamicColors createFromParcel(Parcel in) {
            return new DynamicColors(in);
        }

        @Override
        public DynamicColors[] newArray(int size) {
            return new DynamicColors[size];
        }
    };

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicColors(Parcel in) {
        this();

        in.readMap(mOriginal, mOriginal.getClass().getClassLoader());
        in.readMap(mMutated, mMutated.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mOriginal);
        dest.writeMap(mMutated);
    }

    /**
     * Get the map of original colors.
     *
     * @return The map of original colors.
     */
    public @NonNull Map<Integer, Integer> getOriginal() {
        return mOriginal;
    }

    /**
     * Get the map of mutated colors.
     *
     * @return The map of mutated colors.
     */
    public @NonNull Map<Integer, Integer> getMutated() {
        return mMutated;
    }

    /**
     * Get the color according to its type from the supplied map.
     *
     * @param colors The color map to be used.
     * @param colorType The type of the color.
     * @param fallback The fallback color if the request color type is not found.
     */
    public @ColorInt int get(@NonNull Map<Integer, Integer> colors,
            @Theme.ColorType int colorType, @ColorInt int fallback) {
        final @ColorInt Integer color;
        if (colors.containsKey(colorType) && (color = colors.get(colorType)) != null) {
            return color;
        }

        return fallback;
    }

    /**
     * Get the original color according to its type.
     *
     * @param colorType The type of the color.
     * @param fallback The fallback color if the request color type is not found.
     */
    public @ColorInt int getOriginal(@Theme.ColorType int colorType, @ColorInt int fallback) {
        return get(getOriginal(), colorType, fallback);
    }

    /**
     * Get the mutated color according to its type.
     *
     * @param colorType The type of the color.
     * @param fallback The fallback color if the request color type is not found.
     */
    public @ColorInt int getMutated(@Theme.ColorType int colorType, @ColorInt int fallback) {
        return get(getMutated(), colorType, fallback);
    }

    /**
     * Store the color according to its type in the supplied map.
     *
     * @param colors The map to store the color.
     * @param colorType The type of the color.
     * @param color The color to be stored.
     */
    public void put(@NonNull Map<Integer, Integer> colors,
            @Theme.ColorType int colorType, @ColorInt int color) {
        colors.put(colorType, color);
    }

    /**
     * Store the color according to its type in the original map.
     *
     * @param colorType The type of the color.
     * @param color The color to be stored.
     */
    public void putOriginal(@Theme.ColorType int colorType, @ColorInt int color) {
        put(getOriginal(), colorType, color);
    }

    /**
     * Store the color according to its type in the mutated map.
     *
     * @param colorType The type of the color.
     * @param color The color to be stored.
     */
    public void putMutated(@Theme.ColorType int colorType, @ColorInt int color) {
        put(getMutated(), colorType, color);
    }

    /**
     * Store the colors in the supplied map.
     *
     * @param colors The map to store the colors.
     * @param newColors The colors to be stored.
     */
    public void put(@NonNull Map<Integer, Integer> colors,
            @Nullable Map<Integer, Integer> newColors) {
        if (newColors == null) {
            return;
        }

        clear();
        colors.putAll(newColors);
    }

    /**
     * Store the colors in the original map.
     *
     * @param colors The colors to be stored.
     */
    public void putOriginal(@Nullable Map<Integer, Integer> colors) {
        put(getOriginal(), colors);
    }

    /**
     * Store the colors in the mutated map.
     *
     * @param colors The colors to be stored.
     */
    public void putMutated(@Nullable Map<Integer, Integer> colors) {
        put(getMutated(), colors);
    }

    /**
     * Store the wallpaper colors in the supplied map.
     *
     * @param colors The map to store the colors.
     * @param newColors The wallpaper colors to be stored.
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void put(@NonNull Map<Integer, Integer> colors, @Nullable WallpaperColors newColors) {
        if (newColors == null) {
            return;
        }

        clear();

        if (newColors.getTertiaryColor() != null) {
            colors.put(Theme.ColorType.BACKGROUND, newColors.getTertiaryColor().toArgb());
            colors.put(Theme.ColorType.SURFACE, Theme.AUTO);
        }

        colors.put(Theme.ColorType.PRIMARY, newColors.getPrimaryColor().toArgb());
        colors.put(Theme.ColorType.PRIMARY_DARK, Theme.AUTO);

        if (newColors.getSecondaryColor() != null) {
            colors.put(Theme.ColorType.ACCENT, newColors.getSecondaryColor().toArgb());
            colors.put(Theme.ColorType.ACCENT_DARK, Theme.AUTO);
        }
    }

    /**
     * Store the wallpaper colors in the original map.
     *
     * @param colors The wallpaper colors to be stored.
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void putOriginal(@Nullable WallpaperColors colors) {
        put(getOriginal(), colors);
    }

    /**
     * Store the wallpaper colors in the mutated map.
     *
     * @param colors The wallpaper colors to be stored.
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void putMutated(@Nullable WallpaperColors colors) {
        put(getMutated(), colors);
    }

    /**
     * Mutate original colors for the supplied app theme.
     *
     * @param colors The map to store the mutated colors.
     * @param theme The app theme to be used.
     */
    public void mutate(@NonNull Map<Integer, Integer> colors, @NonNull AppTheme<?> theme) {
        colors.clear();

        @ColorInt int background = getOriginal(Theme.ColorType.BACKGROUND,
                theme.getBackgroundColor());
        @ColorInt int backgroundMutated = background;
        @ColorInt int primary = getOriginal(Theme.ColorType.PRIMARY, theme.getPrimaryColor());
        @ColorInt int accent = getOriginal(Theme.ColorType.ACCENT, theme.getAccentColor());

        if (theme.isDarkTheme()) {
            backgroundMutated = DynamicColorUtils.getDarkerColor(backgroundMutated, FACTOR);
            primary = DynamicColorUtils.getDarkerColor(primary, FACTOR);
        } else {
            backgroundMutated = DynamicColorUtils.getLighterColor(backgroundMutated, FACTOR);
            primary = DynamicColorUtils.getLighterColor(primary, FACTOR);
        }

        if (!getOriginal().containsKey(Theme.ColorType.PRIMARY)) {
            primary = backgroundMutated;
        }

        if (!getOriginal().containsKey(Theme.ColorType.ACCENT)) {
            accent = background;
        }

        put(colors, Theme.ColorType.BACKGROUND, backgroundMutated);
        put(colors, Theme.ColorType.SURFACE, Theme.AUTO);
        put(colors, Theme.ColorType.PRIMARY, primary);
        put(colors, Theme.ColorType.PRIMARY_DARK, Theme.AUTO);
        put(colors, Theme.ColorType.ACCENT, accent);
        put(colors, Theme.ColorType.ACCENT_DARK, Theme.AUTO);
    }

    /**
     * Mutate original colors for the supplied app theme.
     *
     * @param theme The app theme to be used.
     */
    public void mutate(@NonNull AppTheme<?> theme) {
        mutate(getMutated(), theme);
    }

    /**
     * Clear original and mutated colors.
     */
    public void clear() {
        getOriginal().clear();
        getMutated().clear();
    }
}
