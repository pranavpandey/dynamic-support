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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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

    /**
     * Constructor to initialize an object of this class.
     *
     * @param fragmentManager The fragment manager to do the transactions.
     */
    public DynamicTutorialsAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);

        this.mFragmentManager = fragmentManager;
        this.mDataSet = new ArrayList<>();
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
    public @NonNull Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (fragment.isAdded()) {
            return fragment;
        }

        Fragment instantiatedFragment = (Fragment) super.instantiateItem(container, position);
        mDataSet.set(position, (DynamicTutorial) instantiatedFragment);

        return instantiatedFragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @Nullable Object object) {
        if (object == null) {
            return;
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    /**
     * Set tutorials for this adapter.
     *
     * @param dynamicTutorials The collection of tutorials to be set.
     */
    public void setTutorials(@NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        removeTutorialFragments(mDataSet);
        mDataSet = new ArrayList<>(dynamicTutorials);

        notifyDataSetChanged();
    }

    /**
     * Add tutorial to this adapter.
     *
     * @param location The index at which to add.
     * @param dynamicTutorial The tutorial to be added.
     *
     * @return {@code true} if the tutorial added successfully.
     */
    public boolean addTutorial(int location, @NonNull DynamicTutorial dynamicTutorial) {
        if (mDataSet.contains(dynamicTutorial)) {
            return false;
        }

        boolean modified = mDataSet.add(dynamicTutorial);

        if (modified) {
            notifyDataSetChanged();
        }
        return modified;
    }

    /**
     * Add tutorial to this adapter.
     *
     * @param dynamicTutorial The tutorial to be added.
     *
     * @return {@code true} if the tutorial added successfully.
     */
    public boolean addTutorial(@NonNull DynamicTutorial dynamicTutorial) {
        return addTutorial(mDataSet.size(), dynamicTutorial);
    }

    /**
     * Add a collection of tutorials to this adapter.
     *
     * @param location The index at which to add.
     * @param dynamicTutorials The collection of tutorials to be added.
     *
     * @return {@code true} if the tutorials added successfully.
     */
    public boolean addTutorials(int location,
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
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

    /**
     * Add a collection of tutorials to this adapter.
     *
     * @param dynamicTutorials The collection of tutorials to be added.
     *
     * @return {@code true} if the tutorials added successfully.
     */
    public boolean addTutorials(@NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        return addTutorials(mDataSet.size(), dynamicTutorials);
    }

    /**
     * Remove all the tutorials from this adapter.
     *
     * @return {@code true} if the tutorials removed successfully.
     */
    public boolean clearTutorials() {
        if (!mDataSet.isEmpty()) {
            mDataSet.clear();

            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    /**
     * Checks whether this adapter contains the supplied object.
     *
     * @return {@code true} if this adapter contains the supplied object.
     */
    public boolean containsTutorial(Object object) {
        return object instanceof DynamicTutorial && mDataSet.contains(object);
    }

    /**
     * Checks whether this adapter contains the supplied collection.
     *
     * @return {@code true} if this adapter contains the supplied collection.
     */
    public boolean containsTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        return mDataSet.containsAll(dynamicTutorials);
    }

    /**
     * Get the tutorials shown by this adapter.
     */
    public @NonNull List<DynamicTutorial> getTutorials() {
        return mDataSet;
    }

    /**
     * Returns the tutorial for a particular position.
     *
     * @param position The position to get the tutorial.
     *
     * @return The tutorial at the supplied position.
     */
    public DynamicTutorial getTutorial(int position) {
        return mDataSet.get(position);
    }

    /**
     * Remove tutorial from this adapter.
     *
     * @param dynamicTutorial The tutorial to be removed.
     *
     * @return {@code true} if the tutorial removed successfully.
     */
    public boolean removeTutorial(@NonNull DynamicTutorial dynamicTutorial) {
        int locationToRemove = mDataSet.indexOf(dynamicTutorial);
        if (locationToRemove >= 0) {
            mDataSet.remove(locationToRemove);
            removeTutorialFragment(dynamicTutorial);

            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    /**
     * Remove a collection of tutorials from this adapter.
     *
     * @param dynamicTutorials The collection of tutorials to be removed.
     *
     * @return {@code true} if the tutorials removed successfully.
     */
    public boolean removeTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        for (DynamicTutorial dynamicTutorial : dynamicTutorials) {
            int locationToRemove = mDataSet.indexOf(dynamicTutorial);
            if (locationToRemove >= 0) {
                removeTutorialFragment(dynamicTutorial);
                mDataSet.remove(locationToRemove);
                modified = true;
            }
        }

        return modified;
    }

    /**
     * Retain a collection of tutorials to this adapter.
     *
     * @param dynamicTutorials The collection of tutorials to be retained.
     *
     * @return {@code true} if the tutorials retained successfully.
     */
    public boolean retainTutorials(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        boolean modified = false;
        for (int i = mDataSet.size() - 1; i >= 0; i--) {
            if (!dynamicTutorials.contains(mDataSet.get(i))) {
                removeTutorialFragment(mDataSet.get(i));
                mDataSet.remove(i);
                modified = true;
                i--;
            }
        }

        if (modified) {
            notifyDataSetChanged();
        }

        return modified;
    }

    /**
     * Remove a tutorial fragments from the activity.
     *
     * @param dynamicTutorial The tutorial to be removed.
     */
    public void removeTutorialFragment(@NonNull DynamicTutorial dynamicTutorial) {
        if (((Fragment) dynamicTutorial).isAdded()) {
            mFragmentManager.beginTransaction().remove((Fragment) dynamicTutorial).commit();
        }
    }

    /**
     * Remove a collection of tutorial fragments from the activity.
     *
     * @param dynamicTutorials The collection of tutorials to be removed.
     */
    public void removeTutorialFragments(
            @NonNull Collection<? extends DynamicTutorial> dynamicTutorials) {
        for (DynamicTutorial dynamicTutorial : dynamicTutorials) {
            removeTutorialFragment(dynamicTutorial);
        }
    }
}
