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

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.listener.ThemeListener;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicCodeUtils;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicFileUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;
import com.pranavpandey.android.dynamic.util.concurrent.task.ContextTask;

/**
 * A {@link DynamicTask} to perform the theme related operations.
 *
 * @param <V> The type of the dynamic app theme.
 */
public abstract class ThemeExportTask<V extends DynamicAppTheme>
        extends ContextTask<Void, Void, Uri> {

    /**
     * Theme action to perform the operation accordingly.
     */
    private final @Theme.Action int mThemeAction;

    /**
     * Theme preview bitmap.
     */
    private Bitmap mThemeBitmap;

    /**
     * The listener to receive the theme events.
     */
    private final ThemeListener<V> mThemeListener;

    /**
     * The dynamic theme returned by the listener.
     */
    private final V mDynamicTheme;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param themeAction The task type to perform the operation accordingly.
     * @param themeListener The listener to receive the theme events.
     */
    public ThemeExportTask(@Theme.Action int themeAction,
            @Nullable ThemeListener<V> themeListener) {
        super(themeListener != null && themeListener.getThemePreview() != null
                ? themeListener.getThemePreview().getContext() : null);

        this.mThemeAction = themeAction;
        this.mThemeListener = themeListener;
        this.mDynamicTheme = getThemeListener() != null
                || getThemeListener().getThemePreview() != null
                ? themeListener.getThemePreview().getDynamicTheme() : null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (getThemeListener() == null) {
            return;
        }

        if (getThemeAction() == Theme.Action.SHARE) {
            getThemeListener().onSetAction(getThemeAction(),
                    getThemeListener().getThemePreview(), false);
            mThemeBitmap = ((ThemeListener.Export<V>) getThemeListener()).getThemeBitmap(
                    getThemeListener().getThemePreview(), getThemeAction());
        }
    }

    @Override
    protected @Nullable Uri doInBackground(@Nullable Void params) {
        if (getContext() == null || getThemeListener() == null || getDynamicTheme() == null) {
            return null;
        }

        if (getThemeAction() == Theme.Action.SHARE_CODE
                || getThemeAction() == Theme.Action.SAVE_CODE) {
            mThemeBitmap = DynamicCodeUtils.generateThemeCode(getDynamicTheme(),
                    DynamicResourceUtils.getDrawable(getContext(), getDynamicTheme()
                            .isDynamicColor() ? R.drawable.adt_ic_app : R.drawable.ads_ic_style));
        }

        if (getThemeAction() == Theme.Action.SHARE_FILE
                || getThemeAction() == Theme.Action.SAVE_FILE) {
            return DynamicFileUtils.getUriFromFile(getContext(),
                    DynamicThemeUtils.requestThemeFile(getContext(), Theme.NAME,
                            getDynamicTheme().toDynamicString()));
        } else {
            return DynamicFileUtils.getBitmapUri(getContext(),
                    getThemeBitmap(), Theme.Key.SHARE, Theme.EXTENSION_IMAGE);
        }
    }

    @Override
    protected void onPostExecute(@Nullable DynamicResult<Uri> result) {
        super.onPostExecute(result);

        if (getThemeListener() == null) {
            return;
        }

        if (getThemeAction() == Theme.Action.SHARE) {
            getThemeListener().onSetAction(getThemeAction(),
                    getThemeListener().getThemePreview(), true);
        }

        if (result instanceof DynamicResult.Error) {
            getThemeListener().onThemeError(getThemeAction(),
                    getThemeListener().getThemePreview(),
                    ((DynamicResult.Error<Uri>) result).getException());
        }
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
    public @Nullable ThemeListener<V> getThemeListener() {
        return mThemeListener;
    }

    /**
     * Get the dynamic theme returned by the listener.
     *
     * @return The dynamic theme returned by the listener.
     */
    public @Nullable V getDynamicTheme() {
        return mDynamicTheme;
    }

    /**
     * Get the theme bitmap used by this task.
     *
     * @return The theme bitmap used by this task.
     */
    public @Nullable Bitmap getThemeBitmap() {
        return mThemeBitmap;
    }
}
