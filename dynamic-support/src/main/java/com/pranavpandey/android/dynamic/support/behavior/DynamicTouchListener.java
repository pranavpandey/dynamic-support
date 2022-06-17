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

package com.pranavpandey.android.dynamic.support.behavior;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Simple item touch helper extending {@link ItemTouchHelper.Callback} which can be used with
 * {@link DynamicTouchAdapter} to provide basic touch callbacks depending on your use case.
 */
public class DynamicTouchListener extends ItemTouchHelper.Callback {

    /**
     * Adapter to receive callbacks on touch.
     */
    private final DynamicTouchAdapter mAdapter;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param adapter The adapter to receive callbacks on touch.
     */
    public DynamicTouchListener(@NonNull DynamicTouchAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder holder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder holder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(holder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
        mAdapter.onItemDismiss(holder.getAdapterPosition());
    }
}