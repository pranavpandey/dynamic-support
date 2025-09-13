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

package com.pranavpandey.android.dynamic.support.fragment.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.listener.DynamicLifecycle;

/**
 * A {@link ViewPager2.OnPageChangeCallback} to dispatch the {@link DynamicLifecycle} events.
 */
public class DynamicOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

    /**
     * Fragment manager used by this callback.
     */
    private final FragmentManager mFragmentManager;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param fragmentManager The child fragment manager for this callback.
     */
    public DynamicOnPageChangeCallback(@NonNull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        if (position < 0 || getFragmentManager() == null) {
            return;
        }

        if (position < getFragmentManager().getFragments().size()) {
            Fragment fragment = getFragmentManager().getFragments().get(position);
            if (fragment instanceof DynamicLifecycle) {
                if (positionOffset != 0) {
                    ((DynamicLifecycle) fragment).onDynamicPause(true);
                } else {
                    ((DynamicLifecycle) fragment).onDynamicResume(true);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(@ViewPager2.ScrollState int state) {
        super.onPageScrollStateChanged(state);

        if (state != ViewPager2.SCROLL_STATE_IDLE && getFragmentManager() != null
                && !getFragmentManager().getFragments().isEmpty()) {
            Dynamic.collapseSearchView(getFragmentManager().getFragments().get(0).getActivity());
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        if (position < 0 || getFragmentManager() == null) {
            return;
        }

        if (position < getFragmentManager().getFragments().size()) {
            Fragment fragment = getFragmentManager().getFragments().get(position);
            if (fragment instanceof DynamicLifecycle) {
                ((DynamicLifecycle) fragment).onDynamicResume(true);
            }
        }
    }

    /**
     * Returns the fragment manager used by this callback.
     *
     * @return The fragment manager used by this callback.
     */
    public @Nullable FragmentManager getFragmentManager() {
        return mFragmentManager;
    }
}
