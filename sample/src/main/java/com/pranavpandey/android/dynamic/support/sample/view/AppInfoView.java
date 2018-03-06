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

package com.pranavpandey.android.dynamic.support.sample.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
import com.pranavpandey.android.dynamic.support.sample.adapter.AppInfoAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;

/**
 * App info view to display app and developer info by using
 * {@link AppInfoAdapter} and {@link DynamicRecyclerViewFrame}.
 */
public class AppInfoView extends DynamicRecyclerViewFrame {

    public AppInfoView(@NonNull Context context) {
        this(context, null);
    }

    public AppInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setAdapter();
    }

    public AppInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setAdapter();
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return DynamicLayoutUtils.getLinearLayoutManager(
                getContext(), StaggeredGridLayoutManager.VERTICAL);
    }

    public AppInfoView setAdapter() {
        // Set adapter for the recycler view.
        setAdapter(new AppInfoAdapter());

        return this;
    }
}
