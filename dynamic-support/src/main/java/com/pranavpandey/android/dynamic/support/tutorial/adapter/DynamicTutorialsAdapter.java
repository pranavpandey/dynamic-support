/*
 * Copyright 2019 Pranav Pandey
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.adapter.DynamicFragmentStateAdapter;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicSimpleTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * View pager adapter to display the supplied dynamic tutorials.
 *
 * @see DynamicTutorial
 */
public class DynamicTutorialsAdapter<T extends DynamicSimpleTutorial, V extends Fragment>
        extends DynamicFragmentStateAdapter {

    /**
     * Fragments list for this adapter.
     */
    private List<DynamicTutorial<T, V>> mDataSet;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param fragmentActivity The fragment activity to do the transactions.
     */
    public DynamicTutorialsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        this.mDataSet = new ArrayList<>();
    }

    @Override
    public @NonNull Fragment createFragment(int position) {
        return mDataSet.get(position).createTutorial();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    protected void onTintRecyclerView() {
        // Disable default tinting.
    }

    /**
     * Set tutorials for this adapter.
     *
     * @param tutorials The collection of tutorials to be set.
     */
    public void setTutorials(@NonNull Collection<? extends DynamicTutorial<T, V>> tutorials) {
        mDataSet = new ArrayList<>(tutorials);

        notifyDataSetChanged();
    }

    /**
     * Add tutorial to this adapter.
     *
     * @param location The index at which to add.
     * @param tutorial The tutorial to be added.
     *
     * @return {@code true} if the tutorial added successfully.
     */
    public boolean addTutorial(int location, @NonNull DynamicTutorial<T, V> tutorial) {
        if (mDataSet.contains(tutorial)) {
            return false;
        }

        boolean modified = mDataSet.add(tutorial);

        if (modified) {
            notifyDataSetChanged();
        }

        return modified;
    }

    /**
     * Add tutorial to this adapter.
     *
     * @param tutorial The tutorial to be added.
     *
     * @return {@code true} if the tutorial added successfully.
     */
    public boolean addTutorial(@NonNull DynamicTutorial<T, V> tutorial) {
        return addTutorial(mDataSet.size(), tutorial);
    }

    /**
     * Add a collection of tutorials to this adapter.
     *
     * @param location The index at which to add.
     * @param tutorials The collection of tutorials to be added.
     *
     * @return {@code true} if the tutorials added successfully.
     */
    public boolean addTutorials(int location, 
            @NonNull Collection<? extends DynamicTutorial<T, V>> tutorials) {
        boolean modified = false;
        int i = 0;

        for (DynamicTutorial<T, V> dynamicTutorial : tutorials) {
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
     * @param tutorials The collection of tutorials to be added.
     *
     * @return {@code true} if the tutorials added successfully.
     */
    public boolean addTutorials(@NonNull Collection<? extends DynamicTutorial<T, V>> tutorials) {
        return addTutorials(mDataSet.size(), tutorials);
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
     * Get the tutorials shown by this adapter.
     */
    public @NonNull List<DynamicTutorial<T, V>> getTutorials() {
        return mDataSet;
    }

    /**
     * Returns the tutorial for a particular position.
     *
     * @param position The position to get the tutorial.
     *
     * @return The tutorial at the supplied position.
     */
    public DynamicTutorial<T, V> getTutorial(int position) {
        return mDataSet.get(position);
    }

    /**
     * Remove tutorial from this adapter.
     *
     * @param tutorial The tutorial to be removed.
     *
     * @return {@code true} if the tutorial removed successfully.
     */
    public boolean removeTutorial(@NonNull DynamicTutorial<T, V> tutorial) {
        int locationToRemove = mDataSet.indexOf(tutorial);

        if (locationToRemove >= 0) {
            mDataSet.remove(locationToRemove);

            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    /**
     * Remove a collection of tutorials from this adapter.
     *
     * @param tutorials The collection of tutorials to be removed.
     *
     * @return {@code true} if the tutorials removed successfully.
     */
    public boolean removeTutorials(
            @NonNull Collection<? extends DynamicTutorial<T, V>> tutorials) {
        boolean modified = false;

        for (DynamicTutorial<T, V> dynamicTutorial : tutorials) {
            int locationToRemove = mDataSet.indexOf(dynamicTutorial);
            if (locationToRemove >= 0) {
                mDataSet.remove(locationToRemove);
                modified = true;
            }
        }

        return modified;
    }

    /**
     * Retain a collection of tutorials to this adapter.
     *
     * @param tutorials The collection of tutorials to be retained.
     *
     * @return {@code true} if the tutorials retained successfully.
     */
    public boolean retainTutorials(
            @NonNull Collection<? extends DynamicTutorial<T, V>> tutorials) {
        boolean modified = false;

        for (int i = mDataSet.size() - 1; i >= 0; i--) {
            if (!tutorials.contains(mDataSet.get(i))) {
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
}
