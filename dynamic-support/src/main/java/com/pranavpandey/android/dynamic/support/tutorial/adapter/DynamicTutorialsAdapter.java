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

package com.pranavpandey.android.dynamic.support.tutorial.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * View pager adapter to display the supplied dynamic tutorials.
 *
 * @see DynamicTutorial
 */
public class DynamicTutorialsAdapter extends FragmentPagerAdapter {

    /**
     * Fragment manager for this adapter.
     */
    private FragmentManager mFragmentManager;

    /**
     * Fragments list for this adapter.
     */
    private List<DynamicTutorial> mDataSet;

    public DynamicTutorialsAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);

        this.mFragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mDataSet.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof Fragment) {
            mFragmentManager.beginTransaction()
                    .detach((Fragment) object)
                    .attach((Fragment) object)
                    .commit();
        }
        return super.getItemPosition(object);
    }

    @Override
    public @NonNull Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (fragment.isAdded()) {
            return fragment;
        }

        Fragment instantiatedFragment = (Fragment) super.instantiateItem(container, position);
        mDataSet.set(position, (DynamicTutorial) instantiatedFragment);

        return instantiatedFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object == null) {
            return;
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    public void addTutorial(int location, DynamicTutorial dynamicTutorial) {
        if (!mDataSet.contains(dynamicTutorial)) {
            mDataSet.add(location, dynamicTutorial);
        }
    }

    public boolean addTutorial(DynamicTutorial dynamicTutorial) {
        if (mDataSet.contains(dynamicTutorial)) {
            return false;
        }

        boolean modified = mDataSet.add(dynamicTutorial);

        if (modified) {
            notifyDataSetChanged();
        }
        return modified;
    }

    public boolean addTutorials(
            int location, @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        int i = 0;
        for (DynamicTutorial dynamicTutorial : dynamicTutorials) {
            if (!mDataSet.contains(dynamicTutorial)) {
                mDataSet.add(location + i, dynamicTutorial);
                i++;
                modified = true;
            }
        }

        if (modified) {
            notifyDataSetChanged();
        }
        return modified;
    }

    public boolean addTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        for (DynamicTutorial dynamicTutorial : dynamicTutorials) {
            if (!mDataSet.contains(dynamicTutorial)) {
                mDataSet.add(dynamicTutorial);
                modified = true;
            }
        }

        if (modified) {
            notifyDataSetChanged();
        }
        return modified;
    }

    public boolean clearTutorials() {
        if (!mDataSet.isEmpty()) {
            mDataSet.clear();
            return true;
        }

        return false;
    }

    public boolean containsTutorial(Object object) {
        return object instanceof DynamicTutorial && mDataSet.contains(object);
    }

    public boolean containsTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        return mDataSet.containsAll(dynamicTutorials);
    }

    public List<DynamicTutorial> getTutorials() {
        return mDataSet;
    }

    public DynamicTutorial getTutorial(int position) {
        return mDataSet.get(position);
    }

    public boolean removeTutorial(DynamicTutorial dynamicTutorial) {
        int locationToRemove = mDataSet.indexOf(dynamicTutorial);
        if (locationToRemove >= 0) {
            mDataSet.remove(locationToRemove);
            return true;
        }

        return false;
    }

    public boolean removeTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        for (DynamicTutorial dynamicTutorial : dynamicTutorials) {
            int locationToRemove = mDataSet.indexOf(dynamicTutorial);
            if (locationToRemove >= 0) {
                mDataSet.remove(locationToRemove);
                modified = true;
            }
        }

        return modified;
    }

    public boolean retainTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        for (int i = mDataSet.size() - 1; i >= 0; i--) {
            if (!dynamicTutorials.contains(mDataSet.get(i))) {
                mDataSet.remove(i);
                modified = true;
                i--;
            }
        }

        return modified;
    }

    public DynamicTutorial setTutorial(int location, DynamicTutorial dynamicTutorial) {
        if (!mDataSet.contains(dynamicTutorial)) {
            return mDataSet.set(location, dynamicTutorial);
        }

        return mDataSet.set(location, dynamicTutorial);
    }

    public @NonNull List<DynamicTutorial> setTutorials(
            Collection<? extends DynamicTutorial> dynamicTutorials) {
        List<DynamicTutorial> oldList = new ArrayList<>();
        if (mDataSet != null) {
            oldList = new ArrayList<>(mDataSet);
            for (DynamicTutorial fragment : oldList) {
                mFragmentManager.beginTransaction().remove((Fragment) fragment).commit();
            }
        }

        mDataSet = new ArrayList<>(dynamicTutorials);
        notifyDataSetChanged();
        return oldList;
    }
}
