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

package com.pranavpandey.android.dynamic.support.behavior;

/**
 * Interface to listen movement of an item inside the recycler view adapter.
 * <p>Implement this interface in the adapter class to receive item movement callbacks.
 */
public interface DynamicTouchAdapter {

    /**
     * This method will be called when item starts moving from one position to other.
     *
     * @param fromPosition The current position of the item.
     * @param toPosition The new position of the item.
     *
     * @return {@code true} if item starts moving.
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * This method will be called when item is dismissed or removed from a particular position.
     *
     * @param position The position of the dismissed item.
     */
    void onItemDismiss(int position);
}
