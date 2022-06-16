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

package com.pranavpandey.android.dynamic.support.theme.task;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.concurrent.task.ContextTask;

import java.util.Map;

/**
 * A {@link ContextTask} to extract the wallpaper colors.
 *
 * <p>It requires {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} permission on
 * API 26 and below.
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

        return DynamicThemeUtils.getWallpaperColors(getContext());
    }
}
