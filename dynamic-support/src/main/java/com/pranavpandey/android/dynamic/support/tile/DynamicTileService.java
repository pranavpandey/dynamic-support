/*
 * Copyright 2018-2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.tile;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.service.quicksettings.TileService;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

/**
 * A {@link TileService} providing the helper methods to update it properly.
 */
@TargetApi(Build.VERSION_CODES.N)
public abstract class DynamicTileService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();

        onDynamicUpdate();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        onDynamicUpdate();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();

        onDynamicUpdate();
    }

    /**
     * Returns the class for this tile service.
     *
     * @return The class for this tile service.
     */
    protected abstract @NonNull Class<?> getTileServiceClass();

    /**
     * This method will be called when there is a change in the tile state.
     *
     * <p><p>Override this method to update the tile icon, title, etc.
     */
    @CallSuper
    protected void onDynamicUpdate() { }

    /**
     * Update the tile service for a given class.
     *
     * @param context The context to get the package name.
     * @param clazz The tile service class to be updated.
     */
    public static synchronized void update(@NonNull Context context, @NonNull Class<?> clazz) {
        try {
            TileService.requestListeningState(context,
                    new ComponentName(context.getPackageName(), clazz.getName()));
        } catch (Exception ignored) {
        }
    }
}
