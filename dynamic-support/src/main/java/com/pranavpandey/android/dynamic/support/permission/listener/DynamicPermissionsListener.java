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

package com.pranavpandey.android.dynamic.support.permission.listener;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.model.DynamicPermission;

import java.util.List;

/**
 * An interface to get the various permissions callbacks.
 */
public interface DynamicPermissionsListener {

    /**
     * This method will be called after requesting a set of permissions.
     *
     * @param permissions A list of all the required permissions.
     * @param dangerousLeft An array of dangerous permissions not yet granted or requested.
     * @param specialLeft A list of special permissions not yet granted or requested.
     */
    void onRequestDynamicPermissionsResult(@NonNull List<DynamicPermission> permissions,
            @NonNull String[] dangerousLeft, @NonNull List<DynamicPermission> specialLeft);
}
