/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.model;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.model.DynamicAction.NONE;
import static com.pranavpandey.android.dynamic.support.model.DynamicAction.START_ACTIVITY;
import static com.pranavpandey.android.dynamic.support.model.DynamicAction.START_FOREGROUND_SERVICE;
import static com.pranavpandey.android.dynamic.support.model.DynamicAction.START_SERVICE;

/**
 * Interface to hold the various dynamic actions.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = { NONE, START_SERVICE, START_FOREGROUND_SERVICE, START_ACTIVITY })
public @interface DynamicAction {

    /**
     * Constant for no action.
     */
    int NONE = -1;

    /**
     * Constant to start an activity.
     *
     * @see Context#startActivity(Intent)
     */
    int START_SERVICE = 0;

    /**
     * Constant to start a foreground service.
     *
     * @see androidx.core.content.ContextCompat#startForegroundService(Context, Intent)
     */
    int START_FOREGROUND_SERVICE = 1;

    /**
     * Constant to start an service.
     *
     * @see Context#startActivity(Intent)
     */
    int START_ACTIVITY = 2;
}
