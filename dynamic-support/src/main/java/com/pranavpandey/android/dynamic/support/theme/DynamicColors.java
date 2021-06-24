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

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.TargetApi;
import android.app.WallpaperColors;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to handle dynamic colors generation.
 */
public class DynamicColors implements Parcelable {

    /**
     * Factor to generate shades of a color.
     */
    private static final float FACTOR = 0.7f;

    /**
     * Map to store the original colors.
     */
    private final Map<Integer, Integer> mColors;

    /**
     * Map to store the mutated colors.
     */
    private final Map<Integer, Integer> mMutatedColors;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicColors() {
        this(new HashMap<>());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param colors The dynamic colors to be handled.
     */
    public DynamicColors(@NonNull Map<Integer, Integer> colors) {
        this.mColors = colors;
        this.mMutatedColors = colors;
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

        in.readMap(mColors, mColors.getClass().getClassLoader());
        in.readMap(mMutatedColors, mMutatedColors.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mColors);
        dest.writeMap(mMutatedColors);
    }

    /**
     * Get the map of original colors.
     *
     * @return The map of original colors.
     */
    public @NonNull Map<Integer, Integer> getColors() {
        return mColors;
    }

    /**
     * Get the map of mutated colors.
     *
     * @return The map of mutated colors.
     */
    public @NonNull Map<Integer, Integer> getMutatedColors() {
        return mMutatedColors;
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
    public @ColorInt int getColor(@Theme.ColorType int colorType, @ColorInt int fallback) {
        return get(getColors(), colorType, fallback);
    }

    /**
     * Get the mutated color according to its type.
     *
     * @param colorType The type of the color.
     * @param fallback The fallback color if the request color type is not found.
     */
    public @ColorInt int getMutatedColor(@Theme.ColorType int colorType, @ColorInt int fallback) {
        return get(getMutatedColors(), colorType, fallback);
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
    public void putColor(@Theme.ColorType int colorType, @ColorInt int color) {
        put(getColors(), colorType, color);
    }

    /**
     * Store the color according to its type in the mutated map.
     *
     * @param colorType The type of the color.
     * @param color The color to be stored.
     */
    public void putMutatedColor(@Theme.ColorType int colorType, @ColorInt int color) {
        put(getMutatedColors(), colorType, color);
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
    public void putColors(@Nullable Map<Integer, Integer> colors) {
        put(getColors(), colors);
    }

    /**
     * Store the colors in the mutated map.
     *
     * @param colors The colors to be stored.
     */
    public void putMutatedColors(@Nullable Map<Integer, Integer> colors) {
        put(getMutatedColors(), colors);
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
    public void putColors(@Nullable WallpaperColors colors) {
        put(getColors(), colors);
    }

    /**
     * Store the wallpaper colors in the mutated map.
     *
     * @param colors The wallpaper colors to be stored.
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void putMutatedColors(@Nullable WallpaperColors colors) {
        put(getMutatedColors(), colors);
    }

    /**
     * Mutate original colors for the supplied dynamic theme.
     *
     * @param dynamicTheme The dynamic theme to be used.
     */
    public void mutate(@NonNull DynamicTheme dynamicTheme) {
        @ColorInt int background = getColor(Theme.ColorType.BACKGROUND,
                dynamicTheme.get().getBackgroundColor());
        @ColorInt int backgroundMutated = background;
        @ColorInt int primary = getColor(Theme.ColorType.PRIMARY,
                dynamicTheme.get().getPrimaryColor());
        @ColorInt int accent = getColor(Theme.ColorType.ACCENT,
                dynamicTheme.get().getAccentColor());

        if (dynamicTheme.isNightMode()) {
            backgroundMutated = DynamicColorUtils.getDarkerColor(backgroundMutated, FACTOR);
            primary = DynamicColorUtils.getDarkerColor(primary, FACTOR);
        } else {
            backgroundMutated = DynamicColorUtils.getLighterColor(backgroundMutated, FACTOR);
            primary = DynamicColorUtils.getLighterColor(primary, FACTOR);
        }

        if (!getColors().containsKey(Theme.ColorType.PRIMARY)) {
            primary = backgroundMutated;
        }

        if (!getColors().containsKey(Theme.ColorType.ACCENT)) {
            accent = background;
        }

        putMutatedColor(Theme.ColorType.BACKGROUND, backgroundMutated);
        putMutatedColor(Theme.ColorType.SURFACE, Theme.AUTO);
        putMutatedColor(Theme.ColorType.PRIMARY, primary);
        putMutatedColor(Theme.ColorType.PRIMARY_DARK, Theme.AUTO);
        putMutatedColor(Theme.ColorType.ACCENT, accent);
        putMutatedColor(Theme.ColorType.ACCENT_DARK, Theme.AUTO);
    }

    /**
     * Clear original and mutated colors.
     */
    public void clear() {
        getColors().clear();
        getMutatedColors().clear();
    }
}
