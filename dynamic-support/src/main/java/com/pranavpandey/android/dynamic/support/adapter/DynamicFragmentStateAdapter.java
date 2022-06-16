/*
 * Copyright 2018-2022 Pranav Pandey
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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicScrollUtils;

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

    /**
     * The contrast with color used by this adapter.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The recycler view on scroll listener used by this adapter.
     */
    private RecyclerView.OnScrollListener mOnScrollListener;

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

        setContrastWithColor(Defaults.getContrastWithColor(recyclerView.getContext()));
        onSetupRecyclerView();
    }

    /**
     * This method will be called to setup the recycler view.
     */
    protected void onSetupRecyclerView() {
        if (mOnScrollListener == null) {
            mOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(
                        @NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        return;
                    }

                    tintRecyclerView();
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    tintRecyclerView();
                }
            };
        }

        if (getRecyclerView() != null) {
            getRecyclerView().removeOnScrollListener(mOnScrollListener);
            getRecyclerView().addOnScrollListener(mOnScrollListener);
        }
    }

    /**
     * Tint the recycler view attached to this adapter.
     */
    public void tintRecyclerView() {
        DynamicScrollUtils.setEdgeEffectColor(getRecyclerView(),
                DynamicTheme.getInstance().get().isBackgroundAware()
                        ? Dynamic.withContrastRatio(DynamicTheme.getInstance().resolveColorType(
                                Defaults.ADS_COLOR_TYPE_EDGE_EFFECT), getContrastWithColor())
                        : DynamicTheme.getInstance().resolveColorType(
                                Defaults.ADS_COLOR_TYPE_EDGE_EFFECT));

        DynamicScrollUtils.setScrollBarColor(getRecyclerView(),
                DynamicTheme.getInstance().get().isBackgroundAware()
                        ? Dynamic.withContrastRatio(DynamicTheme.getInstance().resolveColorType(
                                Defaults.ADS_COLOR_TYPE_SCROLLABLE), getContrastWithColor())
                        : DynamicTheme.getInstance().resolveColorType(
                                Defaults.ADS_COLOR_TYPE_SCROLLABLE));
    }

    /**
     * Returns the recycler view attached to this adapter.
     *
     * @return The recycler view attached to this adapter.
     */
    public @Nullable RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Returns the contrast with color used by this adapter.
     *
     * @return The contrast with color used by this adapter.
     */
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    /**
     * Set the contrast with color used by this adapter.
     *
     * @param contrastWithColor The contrast with color to be set.
     */
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColor = contrastWithColor;

        tintRecyclerView();
    }
}
