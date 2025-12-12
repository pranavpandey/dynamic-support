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

package com.pranavpandey.android.dynamic.support.factory;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * A customisable {@link RemoteViewsService.RemoteViewsFactory} to provide basic functionality.
 * <p>Extend it and implement necessary methods according to the requirements.
 */
@TargetApi(Build.VERSION_CODES.S)
public abstract class DynamicRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /**
     * Dynamic context used by this factory.
     */
    protected final Context mContext;

    /**
     * App widget id used by this factory.
     */
    private final int mAppWidgetId;

    /**
     * App widget type used by this factory.
     */
    private final int mAppWidgetType;

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
     * @param appWidgetType The app widget type for this factory.
     * @param width The current width of the widget provider.
     * @param height The current height of the widget provider.
     */
    public DynamicRemoteViewsFactory(@NonNull Context context,
            int appWidgetId, int appWidgetType, int width, int height) {
        this(context, appWidgetId, appWidgetType, width, height, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The dynamic context for this factory.
     * @param appWidgetId The app widget id for this factory.
     * @param appWidgetType The app widget type for this factory.
     * @param width The current width of the widget provider.
     * @param height The current height of the widget provider.
     * @param adjustPosition {@code true} to adjust the scrollable view position.
     */
    public DynamicRemoteViewsFactory(@NonNull Context context, int appWidgetId,
            int appWidgetType, int width, int height, boolean adjustPosition) {
        this.mContext = context;
        this.mAppWidgetId = appWidgetId;
        this.mAppWidgetType = appWidgetType;
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
     * Get the app widget type used by this factory.
     *
     * @return The app widget type used by this factory.
     */
    public int getAppWidgetType() {
        return mAppWidgetType;
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
     * @param adjustPosition {@code true} to adjust position of the scrollable view.
     */
    public void setAdjustPosition(boolean adjustPosition) {
        this.mAdjustPosition = adjustPosition;
    }

    @CallSuper
    @Override
    public void onDataSetChanged() {
        onInitialize();
    }

    /**
     * Try to build {@link RemoteViews.RemoteCollectionItems} from this
     * {@link RemoteViewsService.RemoteViewsFactory} factory.
     *
     * @return A collection of {@link RemoteViews.RemoteCollectionItems}.
     *
     * @see #getCount()
     * @see #getItemId(int)
     * @see #getViewAt(int)
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public @NonNull RemoteViews.RemoteCollectionItems buildRemoteCollectionItems() {
        onDataSetChanged();

        RemoteViews.RemoteCollectionItems.Builder itemsBuilder
                = new RemoteViews.RemoteCollectionItems.Builder();
        for (int i = 0; i < getCount(); i++) {
            itemsBuilder.addItem(getItemId(i), getViewAt(i));
        }

        return itemsBuilder.build();
    }
}
