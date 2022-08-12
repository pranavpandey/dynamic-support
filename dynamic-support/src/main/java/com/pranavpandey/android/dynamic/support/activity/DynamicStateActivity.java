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

package com.pranavpandey.android.dynamic.support.activity;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.pranavpandey.android.dynamic.support.R;

import java.util.Locale;

/**
 * A {@link DynamicSystemActivity} to maintain state of the widgets and fragments.
 * <p>It will be useful while handling orientation changes. It saves the current fragment state
 * and reuses it while a configuration change takes place.
 */
public abstract class DynamicStateActivity extends DynamicSystemActivity {

    /**
     * Minimum delay to restore the activity state.
     */
    public static final int STATE_DELAY = 400;

    /**
     * Locale key to maintain its state during configuration changes.
     */
    public static final String ADS_STATE_LOCALE = "ads_state_locale";

    /**
     * Content fragment TAG key which will be used to find it during the configuration changes.
     */
    public static final String ADS_STATE_CONTENT_FRAGMENT_TAG = "ads_state_content_fragment_tag";

    /**
     * Status bar key to maintain its state.
     */
    public static final String ADS_STATE_APP_BAR_COLLAPSED = "ads_state_app_bar_collapsed";

    /**
     * FAB key to maintain its state.
     */
    public static final String ADS_STATE_FAB_VISIBLE = "ads_state_fab_visible";

    /**
     * Extended FAB key to maintain its state.
     */
    public static final String ADS_STATE_EXTENDED_FAB_VISIBLE = "ads_state_extended_fab_visible";

    /**
     * Extended FAB state key to maintain its state.
     */
    public static final String ADS_STATE_EXTENDED_FAB_STATE = "ads_state_extended_fab_state";

    /**
     * Search key to maintain its state.
     */
    public static final String ADS_STATE_SEARCH_VIEW_VISIBLE = "ads_state_search_view_visible";

    /**
     * FAB visibility constant for no change.
     */
    public static final int ADS_VISIBILITY_FAB_NO_CHANGE = -1;

    /**
     * Extended FAB visibility constant for no change.
     */
    public static final int ADS_VISIBILITY_EXTENDED_FAB_NO_CHANGE = -1;

    /**
     * Extended FAB state constant for extended.
     */
    public static final boolean ADS_STATE_EXTENDED_FAB_NO_CHANGE = true;

    /**
     * Content fragment used by this activity.
     */
    private Fragment mContentFragment;

    /**
     * Content fragment TAG which will be used to find it during configuration changes.
     */
    private String mContentFragmentTag;

    /**
     * Current FAB visibility.
     */
    private int mFABVisibility;

    /**
     * Current extended FAB visibility.
     */
    private int mExtendedFABVisibility;

    /**
     * Current extended FAB state.
     */
    private boolean mExtendedFABState;

    /**
     * {@code true} if the app bar is in collapsed state.
     */
    private boolean mAppBarCollapsed;

    /**
     * The selected navigation item id.
     */
    protected @IdRes int mNavigationItemId;

    /**
     * {@code true} if the navigation item is selected.
     * <p>It will be useful in restoring state on configuration changes.
     */
    protected boolean mNavigationItemSelected;

    /**
     * App bar off set change listener to identify whether it is in the collapsed state.
     */
    protected AppBarLayout.OnOffsetChangedListener mAppBarStateListener =
            new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            mAppBarCollapsed = verticalOffset == 0;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                final int fragmentsCount =
                        getSupportFragmentManager().getBackStackEntryCount();
                if (fragmentsCount > 0) {
                    mContentFragmentTag = getSupportFragmentManager()
                            .getBackStackEntryAt(fragmentsCount - 1).getName();

                    if (mContentFragmentTag != null) {
                        mContentFragment = getSupportFragmentManager()
                                .findFragmentByTag(mContentFragmentTag);
                    }
                } else {
                    mContentFragment = getSupportFragmentManager()
                            .findFragmentById(R.id.ads_container);
                }
            }
        });

        if (getSavedInstanceState() != null) {
            mCurrentLocale = (Locale) getSavedInstanceState().getSerializable(ADS_STATE_LOCALE);
            mFABVisibility = ADS_VISIBILITY_FAB_NO_CHANGE;
            mExtendedFABVisibility = ADS_VISIBILITY_EXTENDED_FAB_NO_CHANGE;
            mExtendedFABState = ADS_STATE_EXTENDED_FAB_NO_CHANGE;
            mContentFragmentTag = getSavedInstanceState().getString(ADS_STATE_CONTENT_FRAGMENT_TAG);
            mContentFragment = getSupportFragmentManager().findFragmentByTag(mContentFragmentTag);
        }
    }

    /**
     * Retrieves the fragment container id.
     * <p>This method must be implemented in the extended activity to show fragments inside
     * this container.
     *
     * @return The fragment container id so that the fragment can be injected into this view.
     */
    protected abstract @IdRes int getFragmentContainerId();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(ADS_STATE_LOCALE, mCurrentLocale);
        outState.putString(ADS_STATE_CONTENT_FRAGMENT_TAG, mContentFragmentTag);
    }

    /**
     * Switch the content fragment used by this activity by using the supplied fragment
     * transaction.
     *
     * @param fragmentTransaction The customised fragment transaction to support animations
     *                            and more.
     * @param fragment The fragment to be used by this activity.
     * @param addToBackStack {@code true} to put previous fragment to back stack.
     * @param tag The fragment tag to maintain the back stack.
     * @param restoreByTag {@code true} to restore the fragment by tag.
     */
    public void switchFragment(@NonNull FragmentTransaction fragmentTransaction,
            @NonNull Fragment fragment, boolean addToBackStack, @Nullable String tag,
            boolean restoreByTag) {
        tag = tag != null ? tag : fragment.getClass().getSimpleName();

        final Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentByTag != null) {
            if (restoreByTag) {
                fragment = fragmentByTag;
            } else {
                fragmentTransaction.remove(fragmentByTag);
            }
        }

        fragmentTransaction.setReorderingAllowed(true)
                .replace(getFragmentContainerId(), fragment, tag);
        if (addToBackStack && getContentFragment() != null) {
            fragmentTransaction.addToBackStack(tag);
        } else {
            getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        commitFragmentTransaction(fragmentTransaction);
        setContentFragment(fragment, tag);
    }

    /**
     * Switch the content fragment used by this activity.
     *
     * @param fragment The fragment to be used by this activity.
     * @param addToBackStack {@code true} to put previous fragment to back stack.
     * @param tag The fragment tag to maintain the back stack.
     * @param restoreByTag {@code true} to restore the fragment by tag.
     */
    public void switchFragment(@NonNull Fragment fragment, boolean addToBackStack,
            @Nullable String tag, boolean restoreByTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switchFragment(fragmentTransaction, fragment, addToBackStack, tag, restoreByTag);
    }

    /**
     * Switch the content fragment used by this activity.
     *
     * @param fragment The fragment to be used by this activity.
     * @param addToBackStack {@code true} to put previous fragment to back stack.
     * @param restoreByTag {@code true} to restore the fragment by tag.
     *
     * @see #switchFragment(FragmentTransaction, Fragment, boolean, String, boolean)
     */
    public void switchFragment(@NonNull Fragment fragment,
            boolean addToBackStack, boolean restoreByTag) {
        switchFragment(fragment, addToBackStack, null, restoreByTag);
    }

    /**
     * Switch the content fragment used by this activity.
     *
     * @param fragment The fragment to be used by this activity.
     * @param addToBackStack {@code true} to put previous fragment to back stack.
     *
     * @see #switchFragment(Fragment, boolean, boolean)
     */
    public void switchFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        switchFragment(fragment, addToBackStack, false);
    }

    /**
     * Switch the content fragment used by this activity.
     *
     * @param fragment The fragment to be used by this activity.
     * @param addToBackStack {@code true} to put previous fragment to back stack.
     *
     * @see #switchFragment(Fragment, boolean, boolean)
     */
    public void switchNewFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        switchFragment(fragment, addToBackStack, false);
    }

    /**
     * Returns the current displayed fragment.
     *
     * @return The content fragment used by this activity.
     */
    public @Nullable Fragment getContentFragment() {
        return mContentFragment;
    }

    /**
     * Set the current content fragment.
     *
     * @param fragment The fragment to be set.
     * @param tag The content fragment tag.
     */
    public void setContentFragment(@Nullable Fragment fragment, @NonNull String tag) {
        this.mContentFragment = fragment;
        this.mContentFragmentTag = tag;
    }

    /**
     * Returns the current FAB visibility.
     *
     * @return The current FAB visibility.
     */
    public int getFABVisibility() {
        return mFABVisibility;
    }

    /**
     * Set the current FAB visibility.
     *
     * @param visibility The FAB visibility to be set.
     */
    public void setFABVisibility(int visibility) {
        this.mFABVisibility = visibility;
    }

    /**
     * Returns the current extended FAB visibility.
     *
     * @return The current extended FAB visibility.
     */
    public int getExtendedFABVisibility() {
        return mExtendedFABVisibility;
    }

    /**
     * Set the current extended FAB visibility.
     *
     * @param visibility The extended FAB visibility to be set.
     */
    public void setExtendedFABVisibility(int visibility) {
        this.mExtendedFABVisibility = visibility;
    }

    /**
     * Returns whether the extend FAB is in the extended state.
     *
     * @return {@code true} if the extend FAB is in the extended state.
     */
    public boolean getExtendedFABState() {
        return mExtendedFABState;
    }

    /**
     * Set the current extended FAB state.
     *
     * @param extended {@code true} if the extend FAB is in the extended state.
     */
    public void setExtendedFABState(boolean extended) {
        this.mExtendedFABState = extended;
    }

    /**
     * Returns whether the app bar is in collapsed state.
     *
     * @return {@code true} if the app bar is in collapsed state.
     */
    public boolean isAppBarCollapsed() {
        return mAppBarCollapsed;
    }

    /**
     * This method will be called on selecting the navigation item.
     *
     * @param itemId The item id to be selected.
     * @param restore {@code true} if restoring the state of item.
     */
    public void onNavigationItemSelected(@IdRes int itemId, boolean restore) { }
}
