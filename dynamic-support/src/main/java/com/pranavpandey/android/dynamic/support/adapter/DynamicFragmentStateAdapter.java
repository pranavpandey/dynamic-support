/*
 * Copyright 2018-2021 Pranav Pandey
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A {@link FragmentStateAdapter} to be used with the {@link androidx.viewpager2.widget.ViewPager2}.
 * <p>The main purpose of this adapter is to tint the recycler view edge glow according to the
 * dynamic theme.
 */
public abstract class DynamicFragmentStateAdapter extends FragmentStateAdapter {

    /**
     * Recycler view attached to this adapter.
     */
    private RecyclerView mRecyclerView;

    public DynamicFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public DynamicFragmentStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public DynamicFragmentStateAdapter(@NonNull FragmentManager fragmentManager,
            @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.mRecyclerView = recyclerView;

        onTintRecyclerView();
    }

    /**
     * This method will be called to tint the recycler view.
     */
    protected void onTintRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                DynamicScrollUtils.setEdgeEffectColor(recyclerView,
                        !DynamicTheme.getInstance().get().isBackgroundAware()
                                ? DynamicTheme.getInstance().get().getPrimaryColor()
                                : DynamicColorUtils.getContrastColor(
                                DynamicTheme.getInstance().get().getPrimaryColor(),
                                DynamicTheme.getInstance().get().getBackgroundColor()));
            }
        });
    }

    /**
     * Returns the recycler view attached to this adapter.
     *
     * @return The recycler view attached to this adapter.
     */
    public @Nullable RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
