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

package com.pranavpandey.android.dynamic.support.preview.listener;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.preview.Preview;

/**
 * An interface to implement a dynamic {@link Preview}.
 *
 * @param <T> The type of the preview.
 * @param <D> The type of the preview data.
 * @param <S> The type of the preview size.
 */
public interface DynamicPreview<T extends Preview<?, ?>, D, S> {

    /**
     * Get the object used by this preview.
     *
     * @return The object used by this preview.
     */
    @NonNull T getPreview();

    /**
     * Returns the data used by this preview.
     *
     * @return The data used by this preview.
     */
    @Nullable D getPreviewData();

    /**
     * Returns the size used by this preview.
     *
     * @return The size used by this preview.
     */
    @NonNull S getPreviewSize();

    /**
     * Returns the user visible hint used by this preview.
     *
     * @return The user visible hint used by this preview.
     */
    @Nullable String getPreviewHint();

    /**
     * Returns the placeholder for the preview.
     *
     * @return The placeholder for the preview.
     */
    @Nullable Drawable getPreviewPlaceholder();

    /**
     * Returns the optional fallback drawable for the activity.
     *
     * @return The optional fallback drawable for the activity.
     */
    @Nullable Drawable getFallbackDrawable();

    /**
     * This method will be called to initialize the preview.
     */
    void onPreview();

    /**
     * This method will be called when the preview or data is clicked.
     */
    void onPreviewClick();

    /**
     * This method will be called to get the share title.
     *
     * @return The title for the share menu.
     */
    @Nullable String getShareTitle();

    /**
     * This method will be called to get the default file name.
     *
     * @param requestCode The request code used by the intent.
     * @param legacy {@code true} if requesting for the legacy storage.
     *
     * @return The file name for the preview.
     */
    @NonNull String getFileName(int requestCode, boolean legacy);

    /**
     * This method will be called after exporting the preview.
     *
     * @param file The URI for the exported file.
     */
    void onExportComplete(@Nullable Uri file);

    /**
     * This method will be called when there is an error while exporting the preview.
     */
    void onExportError();
}
