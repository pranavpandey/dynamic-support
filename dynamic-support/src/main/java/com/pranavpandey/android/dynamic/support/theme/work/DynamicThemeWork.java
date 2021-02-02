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

package com.pranavpandey.android.dynamic.support.theme.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Worker class to change dynamic theme according to the time.
 * <p>It will be useful in updating the {@code auto} theme while the app is in background.
 */
public class DynamicThemeWork extends Worker {

    /**
     * Tag for this worker.
     */
    public static final String TAG = "DynamicThemeWork";

    public DynamicThemeWork(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public @NonNull Result doWork() {
        DynamicTheme.getInstance().setDynamicThemeWork(true);
        DynamicTheme.getInstance().onAutoThemeChanged();

        return Result.success();
    }
}
