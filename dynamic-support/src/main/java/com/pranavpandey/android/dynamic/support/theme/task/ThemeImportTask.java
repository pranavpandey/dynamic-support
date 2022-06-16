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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.listener.ThemeListener;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;
import com.pranavpandey.android.dynamic.util.concurrent.task.ContextTask;

/**
 * A {@link DynamicTask} to perform the theme import operations.
 */
public abstract class ThemeImportTask<T> extends ContextTask<Void, Void, String> {

    /**
     * Theme action to perform the operation accordingly.
     */
    private final @Theme.Action int mThemeAction;

    /**
     * The listener to receive the theme events.
     */
    private final ThemeListener.Import.File<T> mThemeListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to get the wallpaper manager.
     * @param themeAction The task type to perform the operation accordingly.
     * @param themeListener The listener to receive the theme events.
     */
    public ThemeImportTask(@Nullable Context context, @Theme.Action int themeAction,
            @Nullable ThemeListener.Import.File<T> themeListener) {
        super(context);

        this.mThemeAction = themeAction;
        this.mThemeListener = themeListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected @Nullable String doInBackground(@Nullable Void params) {
        if (getContext() == null || getThemeListener() == null) {
            return null;
        }

        if (getThemeListener().getThemeSource() instanceof Intent) {
            return DynamicThemeUtils.getThemeData(getContext(),
                    (Intent) getThemeListener().getThemeSource());
        } else if (getThemeListener().getThemeSource() instanceof Uri) {
            String data;
            if ((data = DynamicThemeUtils.getThemeData(getContext(),
                    (Uri) getThemeListener().getThemeSource())) != null) {
                return data;
            } else {
                return DynamicThemeUtils.getThemeUrl(DynamicThemeUtils.mapTheme(
                        new DynamicAppTheme(), DynamicThemeUtils.getBitmapColors(
                                DynamicBitmapUtils.getBitmap(getContext(),
                                        (Uri) getThemeListener().getThemeSource()))));
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(@Nullable DynamicResult<String> result) {
        super.onPostExecute(result);

        if (getThemeListener() == null) {
            return;
        }

        getThemeListener().onImportTheme(result != null ? result.getData() : null);
    }

    /**
     * Get the theme action used by this task.
     *
     * @return The theme action used by this task.
     */
    public @Theme.Action int getThemeAction() {
        return mThemeAction;
    }

    /**
     * Get the listener to receive the theme events.
     *
     * @return The listener to receive the theme events.
     */
    public @Nullable ThemeListener.Import.File<T> getThemeListener() {
        return mThemeListener;
    }
}
