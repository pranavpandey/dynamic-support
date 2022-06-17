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
import androidx.viewpager.widget.ViewPager;

import com.pranavpandey.android.dynamic.support.listener.DynamicLifecycle;

/**
 * A {@link ViewPager.OnPageChangeListener} to dispatch the {@link DynamicLifecycle} events.
 */
public class DynamicOnPageChangeListener implements ViewPager.OnPageChangeListener {

    /**
     * Fragment manager used by this listener.
     */
    private final FragmentManager mFragmentManager;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param fragmentManager The child fragment manager for this listener.
     */
    public DynamicOnPageChangeListener(@NonNull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (getFragmentManager() == null || positionOffset == 0) {
            return;
        }

        if (getFragmentManager().getFragments().size() > position) {
            Fragment paused = getFragmentManager().getFragments().get(position);
            if (paused instanceof DynamicLifecycle) {
                ((DynamicLifecycle) paused).onDynamicPause(true);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (getFragmentManager() == null) {
            return;
        }

        if (getFragmentManager().getFragments().size() > position) {
            Fragment resumed = getFragmentManager().getFragments().get(position);
            if (resumed instanceof DynamicLifecycle) {
                ((DynamicLifecycle) resumed).onDynamicResume(true);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    /**
     * Returns the fragment manager used by this listener.
     *
     * @return The fragment manager used by this listener.
     */
    public @Nullable FragmentManager getFragmentManager() {
        return mFragmentManager;
    }
}
