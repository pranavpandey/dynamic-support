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

package com.pranavpandey.android.dynamic.support.provider;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.pranavpandey.android.dynamic.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicAppWidgetUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.util.DynamicCodeUtils;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

import java.util.Locale;

/**
 * A customisable {@link AppWidgetProvider} to provide basic configuration functionality.
 * <p>Extend it and modify according to the requirements.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class DynamicAppWidgetProvider<T extends AppTheme<?>>
        extends AppWidgetProvider implements DynamicLocale {

    /**
     * Intent extra constant for width of the app widget.
     */
    public static final String EXTRA_APP_WIDGET_WIDTH = "app:widget:width";

    /**
     * Intent extra constant for height of the app widget.
     */
    public static final String EXTRA_APP_WIDGET_HEIGHT = "app:widget:height";

    /**
     * Intent extra constant for adjust position of the app widget.
     */
    public static final String EXTRA_APP_WIDGET_ADJUST_POSITION = "app:widget:adjust_position";

    /**
     * Intent extra constant for the app widget type.
     */
    public static final String EXTRA_APP_WIDGET_TYPE = "app:widget:type";

    /**
     * Constant for the default app widget id.
     */
    public static final int NO_ID = -1;

    /**
     * Constant size in dips for one cell.
     */
    public static final int WIDGET_CELL_SIZE_ONE = 60;

    /**
     * Constant size in dips for two cells.
     */
    public static final int WIDGET_CELL_SIZE_TWO = WIDGET_CELL_SIZE_ONE * 2;

    /**
     * Constant size in dips for three cells.
     */
    public static final int WIDGET_CELL_SIZE_THREE = WIDGET_CELL_SIZE_ONE * 3;

    /**
     * Constant size in dips for four cells.
     */
    public static final int WIDGET_CELL_SIZE_FOUR = WIDGET_CELL_SIZE_ONE * 4;

    /**
     * Constant size in dips for five cells.
     */
    public static final int WIDGET_CELL_SIZE_FIVE = WIDGET_CELL_SIZE_ONE * 5;

    /**
     * Constant size in dips for six cells.
     */
    public static final int WIDGET_CELL_SIZE_SIX = WIDGET_CELL_SIZE_ONE * 6;

    /**
     * Constant size in dips for seven cells.
     */
    public static final int WIDGET_CELL_SIZE_SEVEN = WIDGET_CELL_SIZE_ONE * 7;

    /**
     * Constant size in dips for eight cells.
     */
    public static final int WIDGET_CELL_SIZE_EIGHT = WIDGET_CELL_SIZE_ONE * 8;

    /**
     * Bitmap quality of the widget background.
     */
    public static final int WIDGET_BACKGROUND_QUALITY = 0;

    /**
     * Default header size in dips.
     */
    public static final int WIDGET_HEADER_SIZE = 56;

    /**
     * Dynamic context used by this provider.
     */
    protected Context mContext;

    /**
     * Current locale used by this provider.
     */
    private Locale mCurrentLocale;

    /**
     * Current width of this widget provider in dips.
     */
    private int mWidth;

    /**
     * Current height of this widget provider dips.
     */
    private int mHeight;

    /**
     * {@code true} if position should be adjusted.
     */
    private boolean mAdjustPosition = true;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent)  {
        super.onReceive(setLocale(context), intent);

        mAdjustPosition = true;

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, NO_ID);

            int[] appWidgetIds;
            if (appWidgetId != NO_ID) {
                appWidgetIds =  new int[] { appWidgetId };
            } else {
                appWidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, getClass().getName()));
            }

            if (appWidgetIds != null) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }

    @Override
    public void onUpdate(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, @NonNull int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(@NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager, int appWidgetId,
            @NonNull Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        mAdjustPosition = false;

        updateAppWidget(getContext(), appWidgetManager, appWidgetId);
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

    @Override
    public @Nullable String[] getSupportedLocales() {
        if (DynamicTheme.getInstance().getListener() instanceof DynamicLocale) {
            return ((DynamicLocale) DynamicTheme.getInstance().getListener()).getSupportedLocales();
        }

        return null;
    }

    @Override
    public @NonNull Locale getDefaultLocale(@NonNull Context context) {
        return DynamicLocaleUtils.getDefaultLocale(context, getSupportedLocales());
    }

    @Override
    public @Nullable Locale getLocale() {
        if (DynamicTheme.getInstance().getListener() instanceof DynamicLocale) {
            return ((DynamicLocale) DynamicTheme.getInstance().getListener()).getLocale();
        }

        return DynamicLocaleUtils.getCurrentLocale(DynamicTheme.getInstance().getContext());
    }

    @Override
    public @NonNull Context setLocale(@NonNull Context context) {
        mCurrentLocale = DynamicLocaleUtils.getLocale(
                getLocale(), getDefaultLocale(context));
        return mContext = DynamicLocaleUtils.setLocale(context,
                false, mCurrentLocale, getFontScale());
    }

    /**
     * This method will be called to get the theme style resource for this provider.
     * <p>Override this method to supply your own customised style.
     *
     * @param appWidgetId The app widget id to be used.
     *
     * @return The theme style resource for this provider.
     *
     * @see DynamicAppTheme#isDarkTheme()
     */
    public @StyleRes int getThemeRes(int appWidgetId) {
        return DynamicTheme.getInstance().getListener().getThemeRes(getDynamicTheme(appWidgetId));
    }

    /**
     * This method will be called to get the dynamic app theme for this provideZ.
     * <p>Override this method to supply your own customised theme.
     *
     * @param appWidgetId The app widget id to be used.
     *
     * @return The dynamic app theme for this provider.
     */
    public @Nullable T getDynamicTheme(int appWidgetId) {
        return null;
    }

    @Override
    public float getFontScale() {
        if (DynamicTheme.getInstance().getListener() instanceof DynamicLocale) {
            return ((DynamicLocale) DynamicTheme.getInstance().getListener()).getFontScale();
        }

        return DynamicTheme.getInstance().get(false).getFontScaleRelative();
    }

    /**
     * Override this method to get a {@link RemoteViews} fo this widget provider.
     * <p>It can be according to the widget height and width to provide dynamic layouts.
     *
     * @param context The context associated with this widget provider.
     *
     * @return The remote views for this provider.
     */
    protected abstract @NonNull RemoteViews getRemoteViews(@NonNull Context context);

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
        context.getTheme().applyStyle(getThemeRes(appWidgetId), true);
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
        if (DynamicSdkUtils.is16()) {
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
    }

    /**
     * Get the dynamic theme context used by this provider.
     *
     * @return The dynamic context used by this provider.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Get the current locale used by this provider.
     *
     * @return The current locale used by this provider.
     */
    public @NonNull Locale getCurrentLocale() {
        return mCurrentLocale;
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
     * Sets whether the position should be adjusted.
     *
     * @param adjustPosition {@code true} if the position should be adjusted.
     */
    public void setAdjustPosition(boolean adjustPosition) {
         this.mAdjustPosition = adjustPosition;
    }

    /**
     * Checks whether the position should be adjusted.
     *
     * @return {@code true} if the position should be adjusted.
     */
    public boolean isAdjustPosition() {
        return mAdjustPosition;
    }

    /**
     * Converts the widget size into cells.
     *
     * @return The number of cells according to the supplied size.
     *
     * @param size The size to be converted into cells.
     */
    public static int getCellsForSize(int size) {
        return (int) (Math.ceil(size + 30d) / WIDGET_CELL_SIZE_ONE);
    }
    
    /**
     * Get a bitmap for the widget background according to the corner radius.
     *
     * @param width The width in dip for the bitmap.
     * @param height The height in dip for the bitmap.
     * @param cornerRadius The corner size in dip for the bitmap.
     *
     * @return The bitmap for the widget background according to the supplied parameters.
     */
    public static @Nullable Bitmap getWidgetFrameBitmap(int width,
            int height, float cornerRadius) {
        return DynamicBitmapUtils.getBitmap(DynamicCodeUtils.getCornerDrawableWithStroke(
                width, height, cornerRadius, Color.WHITE, false));
    }

    /**
     * Get a bitmap for the widget background according to the corner radius.
     *
     * @param width The width in dip for the bitmap.
     * @param height The height in dip for the bitmap.
     * @param cornerRadius The corner size in dip for the bitmap.
     * @param color The color to be used.
     * @param strokeColor The stroke color to be used.
     *
     * @return The bitmap for the widget background according to the supplied parameters.
     */
    public static @Nullable Bitmap getWidgetFrameBitmapWithStroke(int width, int height,
            float cornerRadius, @ColorInt int color, @ColorInt int strokeColor) {
        return DynamicBitmapUtils.getBitmap(DynamicCodeUtils.getCornerDrawableWithStroke(
                width, height, cornerRadius, color, false, strokeColor));
    }

    /**
     * Get a bitmap for the widget background according to the corner radius.
     *
     * @param width The width in dip for the bitmap.
     * @param height The height in dip for the bitmap.
     * @param cornerRadius The corner size in dip for the bitmap.
     * @param color The color to be used.
     *
     * @return The bitmap for the widget background according to the supplied parameters.
     */
    public static @Nullable Bitmap getWidgetFrameBitmapWithStroke(int width,
            int height, float cornerRadius, @ColorInt int color) {
        return DynamicBitmapUtils.getBitmap(DynamicCodeUtils.getCornerDrawableWithStroke(
                width, height, cornerRadius, color, false));
    }

    /**
     * Returns a bitmap for widget header according to the corner radius.
     *
     * @param width The width in dip for the bitmap.
     * @param height The height in dip for the bitmap.
     * @param cornerRadius The corner size in dip for the bitmap.
     * @param color The color to be used.
     *
     * @return The bitmap for the widget background according to the supplied parameters.
     */
    public static @Nullable Bitmap getWidgetHeaderBitmap(int width,
            int height, float cornerRadius, @ColorInt int color) {
        return DynamicBitmapUtils.getBitmap(DynamicCodeUtils
                .getCornerDrawable(width, height, cornerRadius, color, true));
    }
}
