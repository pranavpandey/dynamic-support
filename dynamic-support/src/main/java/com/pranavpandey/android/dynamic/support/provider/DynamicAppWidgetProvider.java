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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.pranavpandey.android.dynamic.support.utils.DynamicAppWidgetUtils;

/**
 * A customisable app widget provider to provide basic configuration
 * functionality. Extend it and modify according to the need.
 */
public abstract class DynamicAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent)  {
        super.onReceive(context, intent);

        if (intent.getAction() != null &&
                (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                || intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED))) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass().getName()));
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager,
                         @NonNull int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onAppWidgetOptionsChanged(@NonNull Context context,
                                          @NonNull AppWidgetManager appWidgetManager,
                                          int appWidgetId, @NonNull Bundle newOptions) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context,
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH),
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)));

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            DynamicAppWidgetUtils.deleteWidgetSettings(getPreferences(), appWidgetIds[i]);
        }
    }

    @Override
    public void onDisabled(@NonNull Context context) {
        DynamicAppWidgetUtils.cleanupPreferences(getPreferences());
    }

    /**
     * Override this method to get a {@link RemoteViews} fo this widget
     * provider. It can be according to the widget height and width to
     * provide dynamic layouts.
     *
     * @param context The context associated with this widget provider.
     * @param minWidth The minimum width allowed for this provider.
     * @param minHeight The minimum height allowed for this provider.
     *
     * @return The remote views for this provider.
     */
    protected abstract RemoteViews getRemoteViews(
            @NonNull Context context, int minWidth, int minHeight);

    /**
     * Override this method to provide a shared preferences name for
     * this app widget provider.
     *
     * @return The shared preferences for this widget provider.
     */
    protected abstract @NonNull String getPreferences();

    /**
     * Override this method to update a widget instance according to
     * the id. It will be useful while implementing a configuration activity
     * via {@link com.pranavpandey.android.dynamic.support.activity.DynamicWidgetActivity}.
     *
     * @param context The context associated with this widget provider.
     * @param appWidgetManager The app widget manager.
     * @param appWidgetId The app widget id to be updated.
     */
    public abstract void updateAppWidget(@NonNull Context context,
                                         @NonNull AppWidgetManager appWidgetManager,
                                         int appWidgetId);

    /**
     * @return The number of cells according to the supplied size.
     *
     * @param size The size to be converted into cells.
     */
    public static int getCellsForSize(int size) {
        return (int) (Math.ceil(size + 30d) / 70d);
    }
}
