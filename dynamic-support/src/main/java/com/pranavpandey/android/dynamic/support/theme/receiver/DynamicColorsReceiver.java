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

package com.pranavpandey.android.dynamic.support.theme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * Broadcast receiver to listen color events. It has been already added in the manifest and
 * should be registered dynamically at the runtime.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class DynamicColorsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, @Nullable Intent intent) {
        if (intent != null && Intent.ACTION_WALLPAPER_CHANGED.equals(intent.getAction())) {
            try {
                DynamicTheme.getInstance().onAutoThemeChanged();
            } catch (Exception ignored) {
            }
        }
    }
}
