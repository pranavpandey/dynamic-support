/*
 * Copyright 2018 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.provider;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.utils.DynamicAppWidgetUtils;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A customisable app widget provider to provide basic configuration functionality.
 * <p>Extend it and modify according to the need.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class DynamicAppWidgetProvider extends AppWidgetProvider {

    /**
     * Constant size in dips for one cell.
     */
    public static final int ADS_WIDGET_CELL_SIZE_ONE = 60;

    /**
     * Constant size in dips for two cells.
     */
    public static final int ADS_WIDGET_CELL_SIZE_TWO = ADS_WIDGET_CELL_SIZE_ONE * 2;

    /**
     * Constant size in dips for three cells.
     */
    public static final int ADS_WIDGET_CELL_SIZE_THREE = ADS_WIDGET_CELL_SIZE_ONE * 3;

    /**
     * Constant size in dips for four cells.
     */
    public static final int ADS_WIDGET_CELL_SIZE_FOUR = ADS_WIDGET_CELL_SIZE_ONE * 4;

    /**
     * Constant size in dips for five cells.
     */
    public static final int ADS_WIDGET_CELL_SIZE_FIVE = ADS_WIDGET_CELL_SIZE_ONE * 5;

    /**
     * Constant size in dips for six cells.
     */
    public static final int ADS_WIDGET_CELL_SIZE_SIX = ADS_WIDGET_CELL_SIZE_ONE * 6;

    /**
     * Current width of this widget provider.
     */
    private int mWidth;

    /**
     * Current height of this widget provider.
     */
    private int mHeight;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent)  {
        super.onReceive(context, intent);

        if (intent.getAction() != null && intent.getAction()
                .equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass().getName()));
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, @NonNull int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            onAppWidgetOptionsChanged(context, appWidgetManager,
                    appWidgetId, appWidgetManager.getAppWidgetOptions(appWidgetId));
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, int appWidgetId,
            @NonNull Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context));
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onDeleted(@NonNull Context context, @NonNull int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            DynamicAppWidgetUtils.deleteWidgetSettings(getPreferences(), appWidgetId);
        }
    }

    @Override
    public void onDisabled(@NonNull Context context) {
        super.onDisabled(context);

        DynamicAppWidgetUtils.cleanupPreferences(getPreferences());
    }

    /**
     * Override this method to get a {@link RemoteViews} fo this widget provider.
     * <p>It can be according to the widget height and width to provide dynamic layouts.
     *
     * @param context The context associated with this widget provider.
     *
     * @return The remote views for this provider.
     */
    protected abstract RemoteViews getRemoteViews(@NonNull Context context);

    /**
     * Override this method to provide a shared preferences name for this app widget provider.
     *
     * @return The shared preferences for this widget provider.
     */
    protected abstract @NonNull String getPreferences();

    /**
     * Override this method to update a widget instance according to the id.
     * <p>It will be useful while implementing a configuration activity via
     * {@link com.pranavpandey.android.dynamic.support.activity.DynamicWidgetActivity}.
     *
     * @param context The context associated with this widget provider.
     * @param appWidgetManager The app widget manager.
     * @param appWidgetId The app widget id to be updated.
     */
    @CallSuper
    public void updateAppWidget(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, int appWidgetId) {
        updateWidgetDimensions(context, appWidgetManager, appWidgetId);
    }

    /**
     * Update widget width and height according to the current orientation.
     *
     * @param context The context associated with this widget provider.
     * @param appWidgetManager The app widget manager.
     * @param appWidgetId The app widget id to update the dimensions.
     */
    protected void updateWidgetDimensions(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            mWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            mHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        } else {
            mWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            mHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        }
    }

    /**
     * Returns the current width of this widget provider.
     *
     * @return The current width of this widget provider.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Sets the current width of this widget provider.
     *
     * @param width The width to be set.
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }

    /**
     * Returns the current height of this widget provider.
     *
     * @return The current height of this widget provider.
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Sets the current height of this widget provider.
     *
     * @param height The height to be set.
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }

    /**
     * Converts the widget size into cells.
     *
     * @return The number of cells according to the supplied size.
     *
     * @param size The size to be converted into cells.
     */
    public static int getCellsForSize(int size) {
        return (int) (Math.ceil(size + 30d) / ADS_WIDGET_CELL_SIZE_ONE);
    }

    /**
     * Get a bitmap for widget background according to the corner radius.
     *
     * @param width The width in dip for the bitmap.
     * @param height The height in dip for the bitmap.
     * @param cornerRadius The corner size in dip for the bitmap.
     *
     * @return The bitmap for widget background according to the supplied parameters.
     */
    public static Bitmap getWidgetFrameBitmap(int width, int height, float cornerRadius) {
        return DynamicBitmapUtils.getBitmapFormDrawable(DynamicDrawableUtils
                .getCornerDrawable(width, height, cornerRadius));
    }
}
