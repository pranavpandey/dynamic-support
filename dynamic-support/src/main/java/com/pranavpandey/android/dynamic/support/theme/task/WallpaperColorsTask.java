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

package com.pranavpandey.android.dynamic.support.theme.task;

import android.annotation.TargetApi;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.utils.concurrent.task.ContextTask;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link ContextTask} to extract the wallpaper colors.
 *
 * @see Palette
 * @see WallpaperManager#getDrawable()
 * @see WallpaperManager#getWallpaperColors(int)
 */
@TargetApi(Build.VERSION_CODES.O_MR1)
public abstract class WallpaperColorsTask extends ContextTask<Void, Void, Map<Integer, Integer>> {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to get the wallpaper manager.
     *
     * @see WallpaperManager
     */
    public WallpaperColorsTask(@Nullable Context context) {
        super(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected @Nullable Map<Integer, Integer> doInBackground(@Nullable Void params) {
        if (getContext() == null) {
            return null;
        }

        final Map<Integer, Integer> colors = new HashMap<>();

        if (DynamicSdkUtils.is27()) {
            final WallpaperColors wallpaperColors = WallpaperManager.getInstance(
                    getContext()).getWallpaperColors(WallpaperManager.FLAG_SYSTEM);

            if (wallpaperColors != null) {
                if (wallpaperColors.getTertiaryColor() != null) {
                    colors.put(Theme.ColorType.BACKGROUND,
                            wallpaperColors.getTertiaryColor().toArgb());
                }

                colors.put(Theme.ColorType.PRIMARY,
                        wallpaperColors.getPrimaryColor().toArgb());

                if (wallpaperColors.getSecondaryColor() != null) {
                    colors.put(Theme.ColorType.ACCENT,
                            wallpaperColors.getSecondaryColor().toArgb());
                }
            }
        } else {
            final Bitmap wallpaper = DynamicBitmapUtils.getBitmap(
                    WallpaperManager.getInstance(getContext()).getDrawable());

            if (wallpaper != null) {
                final Palette palette = new Palette.Builder(wallpaper).generate();

                if (palette.getLightMutedSwatch() != null) {
                    colors.put(Theme.ColorType.BACKGROUND, palette.getLightMutedSwatch().getRgb());
                } else if (palette.getDarkMutedSwatch() != null) {
                    colors.put(Theme.ColorType.BACKGROUND, palette.getDarkMutedSwatch().getRgb());
                }

                if (palette.getDominantSwatch() != null) {
                    colors.put(Theme.ColorType.PRIMARY, palette.getDominantSwatch().getRgb());
                } else if (palette.getVibrantSwatch() != null) {
                    colors.put(Theme.ColorType.PRIMARY, palette.getVibrantSwatch().getRgb());
                }

                if (palette.getLightVibrantSwatch() != null) {
                    colors.put(Theme.ColorType.ACCENT, palette.getLightVibrantSwatch().getRgb());
                } else if (palette.getDarkVibrantSwatch() != null) {
                    colors.put(Theme.ColorType.ACCENT, palette.getDarkVibrantSwatch().getRgb());
                }
            }
        }

        return colors;
    }
}
