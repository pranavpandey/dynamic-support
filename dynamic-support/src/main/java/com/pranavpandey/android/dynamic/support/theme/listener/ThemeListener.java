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

package com.pranavpandey.android.dynamic.support.theme.listener;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicTask;

/**
 * Interface to listen various theme events.
 */
public interface ThemeListener<T extends DynamicAppTheme> {

    /**
     * This method will called to load settings from the supplied theme.
     *
     * @param theme The dynamic app theme to be loaded.
     */
    void onLoadTheme(@NonNull T theme);

    /**
     * Get the theme preview associated with this listener.
     *
     * @return The theme preview associated with this listener.
     */
    @Nullable ThemePreview<T> getThemePreview();

    /**
     * This method will be called on setting the action icon.
     * <p>It can be used to change the action icon while sharing the theme.
     *
     * @param themeAction The theme action to be used.
     * @param themePreview The theme preview to set the share action.
     * @param enable {@code true} to enable the share action.
     */
    void onSetAction(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, boolean enable);

    /**
     * This method will be called on error while doing theme operations.

     * @param themeAction The theme action to be used.
     * @param themePreview The theme preview to set the share action.
     * @param e The optional exception.
     */
    void onThemeError(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, @Nullable Exception e);

    /**
     * Interface to listen the theme import events.
     */
    interface Import<T extends DynamicAppTheme> {

        /**
         * Interface to listen the theme import (from file) events.
         */
        interface File<T> {

            /**
             * This method will be called to get the theme source.
             *
             * @return The source to retrieve the theme.
             */
            @Nullable T getThemeSource();

            /**
             * This method will be called on importing the theme.
             *
             * @param theme The theme string to be imported.
             */
            void onImportTheme(@Nullable String theme);
        }

        /**
         * This method will be called on importing the theme.
         *
         * @param themeAction The theme action to be used.
         */
        void importTheme(@Theme.Action int themeAction);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         */
        void importTheme(@Nullable String theme);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         * @param themeAction The theme action to be used.
         */
        void importTheme(@Nullable String theme, @Theme.Action int themeAction);

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         *
         * @return The imported theme.
         */
        T onImportTheme(@NonNull String theme);
    }

    /**
     * Interface to listen the theme export events.
     */
    interface Export<T extends DynamicAppTheme> extends ThemeListener<T> {

        /**
         * This method will be called to return a bitmap.
         *
         * @param themePreview The theme preview associated with this listener.
         * @param themeAction The theme action associated with this listener.
         *
         * @return The theme bitmap.
         */
        @Nullable Bitmap getThemeBitmap(@Nullable ThemePreview<T> themePreview,
                @Theme.Action int themeAction);

        /**
         * This method will be called to get the theme export task.
         *
         * @param dialog The dialog interface if applicable.
         * @param themeAction The theme action to be used.
         * @param themePreview The theme preview for the task.
         *
         * @return The {@link DynamicTask} to perform theme export operations.
         */
        @NonNull DynamicTask<?, ?, ?> getThemeExportTask(@Nullable DialogInterface dialog,
                @Theme.Action int themeAction, @Nullable ThemePreview<T> themePreview);
    }

    /**
     * Interface to listen the theme code events.
     */
    interface Code {

        /**
         * This method will be called to retrieve the theme data.
         *
         * @return The theme data.
         */
        @Nullable String getThemeData();

        /**
         * This method will be called to retrieve the theme code bitmap uri.
         *
         * @return The theme code bitmap uri.
         */
        @Nullable Uri getThemeCode();
    }

    /**
     * Interface to listen the theme selection events.
     */
    interface Select {

        /**
         * This method will be called on selecting the theme.
         *
         * @param theme The selected theme.
         */
        void onThemeSelect(@Theme int theme);

        /**
         * This method will be called on copying the theme.
         */
        void onThemeCopy();
    }
}
