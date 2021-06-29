/*
 * Copyright 2018-2021 Pranav Pandey
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.adapter.DynamicFragmentStateAdapter;
import com.pranavpandey.android.dynamic.support.tutorial.Tutorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * View pager adapter to display the supplied dynamic tutorials.
 *
 * @see Tutorial
 */
public class DynamicTutorialsAdapter<V extends Fragment, T extends Tutorial<T, V>>
        extends DynamicFragmentStateAdapter {

    /**
     * Fragments list for this adapter.
     */
    private final List<Tutorial<T, V>> mData;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param fragmentActivity The fragment activity to do the transactions.
     */
    public DynamicTutorialsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        this.mData = new ArrayList<>();
    }

    @Override
    public @NonNull Fragment createFragment(int position) {
        return mData.get(position).createTutorial();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerView.setItemAnimator(null);
    }

    @Override
    protected void onSetupRecyclerView() {
        tintRecyclerView();
    }

    /**
     * Set tutorials for this adapter.
     *
     * @param tutorials The collection of tutorials to be set.
     */
    public void setTutorials(@NonNull Collection<? extends Tutorial<T, V>> tutorials) {
        mData.clear();
        mData.addAll(tutorials);

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
    public boolean addTutorial(int location, @NonNull Tutorial<T, V> tutorial) {
        if (mData.contains(tutorial)) {
            return false;
        }

        boolean modified = mData.add(tutorial);

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
    public boolean addTutorial(@NonNull Tutorial<T, V> tutorial) {
        return addTutorial(mData.size(), tutorial);
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
            @NonNull Collection<? extends Tutorial<T, V>> tutorials) {
        boolean modified = false;
        int i = 0;

        for (Tutorial<T, V> dynamicTutorial : tutorials) {
            if (!mData.contains(dynamicTutorial)) {
                mData.add(location + i, dynamicTutorial);
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
    public boolean addTutorials(@NonNull Collection<? extends Tutorial<T, V>> tutorials) {
        return addTutorials(mData.size(), tutorials);
    }

    /**
     * Remove all the tutorials from this adapter.
     *
     * @return {@code true} if the tutorials removed successfully.
     */
    public boolean clearTutorials() {
        if (!mData.isEmpty()) {
            mData.clear();

            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    /**
     * Get the tutorials shown by this adapter.
     *
     * @return The tutorials shown by this adapter.
     */
    public @NonNull List<Tutorial<T, V>> getTutorials() {
        return mData;
    }

    /**
     * Returns the tutorial for a particular position.
     *
     * @param position The position to get the tutorial.
     *
     * @return The tutorial at the supplied position.
     */
    public @Nullable Tutorial<T, V> getTutorial(int position) {
        if (position < 0) {
            return null;
        }

        return mData.get(position);
    }

    /**
     * Remove tutorial from this adapter.
     *
     * @param tutorial The tutorial to be removed.
     *
     * @return {@code true} if the tutorial removed successfully.
     */
    public boolean removeTutorial(@NonNull Tutorial<T, V> tutorial) {
        int locationToRemove = mData.indexOf(tutorial);

        if (locationToRemove >= 0) {
            mData.remove(locationToRemove);

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
            @NonNull Collection<? extends Tutorial<T, V>> tutorials) {
        boolean modified = false;

        for (Tutorial<T, V> dynamicTutorial : tutorials) {
            int locationToRemove = mData.indexOf(dynamicTutorial);
            if (locationToRemove >= 0) {
                mData.remove(locationToRemove);
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
            @NonNull Collection<? extends Tutorial<T, V>> tutorials) {
        boolean modified = false;

        for (int i = mData.size() - 1; i >= 0; i--) {
            if (!tutorials.contains(mData.get(i))) {
                mData.remove(i);
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
