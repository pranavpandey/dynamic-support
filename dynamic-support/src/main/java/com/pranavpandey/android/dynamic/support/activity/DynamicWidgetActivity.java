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

package com.pranavpandey.android.dynamic.support.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;

/**
 * An activity to configure widgets having basic configuration methods.
 * <p>Extend this activity and add your own settings fragments according to the need.
 */
public abstract class DynamicWidgetActivity extends DynamicActivity {

    /**
     * App widget id for this configuration activity.
     */
    protected int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    /**
     * {@code true} if updating the widget.
     */
    protected boolean mUpdateWidget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mUpdateWidget = extras.getBoolean(
                    DynamicIntent.EXTRA_WIDGET_UPDATE, false);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finishActivity();
        }
    }

    /**
     * Add widget after saving the settings.
     *
     * @param finishActivity {@code true} to finish the activity.
     */
    public void addWidget(boolean finishActivity) {
        if (!mUpdateWidget) {
            // Make sure we pass back the original appWidgetId.
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
        }

        if (finishActivity) {
            finishActivity();
        }
    }

    /**
     * Add widget after saving the settings.
     *
     * @see #addWidget(boolean)
     */
    public void addWidget() {
        addWidget(true);
    }
}
