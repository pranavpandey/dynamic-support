/*
 * Copyright 2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.factory;

import android.content.Context;
import android.widget.RemoteViewsService;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

/**
 * A customisable {@link RemoteViewsService.RemoteViewsFactory} to provide basic functionality.
 * <p>Extend it and implement necessary methods according to the need.
 */
public abstract class DynamicRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /**
     * Dynamic context used by this factory.
     */
    protected final Context mContext;

    /**
     * App widget is used by this factory.
     */
    private final int mAppWidgetId;

    /**
     * Current width of the widget provider used for this factory in dips.
     */
    private int mWidth;

    /**
     * Current height of the widget provider used for this factory in dips.
     */
    private int mHeight;

    /**
     * {@code true} to adjust the scrollable view position.
     */
    private boolean mAdjustPosition;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The dynamic context for this factory.
     * @param appWidgetId The app widget id for this factory.
     * @param width The current width of the widget provider.
     * @param height The current height of the widget provider.
     */
    public DynamicRemoteViewsFactory(@NonNull Context context,
            int appWidgetId, int width, int height) {
        this(context, appWidgetId, width, height, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The dynamic context for this factory.
     * @param appWidgetId The app widget id for this factory.
     * @param width The current width of the widget provider.
     * @param height The current height of the widget provider.
     * @param adjustPosition {@code true} to adjust the scrollable view position.
     */
    public DynamicRemoteViewsFactory(@NonNull Context context,
            int appWidgetId, int width, int height, boolean adjustPosition) {
        this.mContext = context;
        this.mAppWidgetId = appWidgetId;
        this.mWidth = width;
        this.mHeight = height;
        this.mAdjustPosition = adjustPosition;

        onInitialize();
    }

    /**
     * This method will be called on initializing the factory.
     */
    public abstract void onInitialize();

    /**
     * Get the dynamic context used by this factory.
     *
     * @return The dynamic context used by this factory.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Get the app widget id used by this factory.
     *
     * @return The app widget id used by this factory.
     */
    public int getAppWidgetId() {
        return mAppWidgetId;
    }

    /**
     * Returns the current width of the widget provider used for this factory.
     *
     * @return The current width of the widget provider used for this factory.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Sets the current width of the widget provider used for this factory.
     *
     * @param width The width to be set.
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }

    /**
     * Returns the current height of the widget provider used for this factory.
     *
     * @return The current height of the widget provider used for this factory.
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Sets the current height of the widget provider used for this factory.
     *
     * @param height The height to be set.
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }

    /**
     * Checks whether to adjust position of the scrollable view.
     *
     * @return {@code true} to adjust position of the scrollable view.
     */
    public boolean isAdjustPosition() {
        return mAdjustPosition;
    }

    /**
     * Sets the adjust position of the scrollable view for this factory.
     *
     * @param scrollToPosition {@code true} to adjust position of the scrollable view.
     */
    public void setScrollToPosition(boolean scrollToPosition) {
        this.mAdjustPosition = scrollToPosition;
    }

    @CallSuper
    @Override
    public void onDataSetChanged() {
        onInitialize();
    }
}
