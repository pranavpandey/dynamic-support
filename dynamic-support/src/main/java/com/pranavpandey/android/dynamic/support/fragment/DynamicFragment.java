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

package com.pranavpandey.android.dynamic.support.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.android.material.transition.MaterialSharedAxis;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity;
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicLifecycle;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicTransitionListener;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.util.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * Base fragment class to provide basic functionality and to work with the {@link DynamicActivity}.
 * <p>Extend this fragment to add more functionality according to the requirements.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DynamicFragment extends Fragment implements DynamicLifecycle,
        DynamicTransitionListener, DynamicSearchListener, MenuProvider,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Saved instance state for this fragment.
     */
    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mSavedInstanceState = savedInstanceState;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onApplyTransitions(false);

        mSavedInstanceState = savedInstanceState;

        if (getActivity() == null) {
            return;
        }

        requireActivity().addMenuProvider(this,
                getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        if (isSupportActionBar()) {
            requireActivity().setTitle(getTitle());

            if (getActivity() instanceof DynamicActivity) {
                ((DynamicActivity) requireActivity()).setSubtitle(getSubtitle());
            } else {
                ((AppCompatActivity) requireActivity())
                        .getSupportActionBar().setSubtitle(getSubtitle());
            }
        }

        if (getCheckedMenuItemId() != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            if (requireActivity().findViewById(getNavigationViewId())
                    instanceof NavigationBarView) {
                ((NavigationBarView) requireActivity().findViewById(
                        getNavigationViewId())).setSelectedItemId(getCheckedMenuItemId());
            }

            if (requireActivity() instanceof DynamicDrawerActivity) {
                ((DynamicDrawerActivity) requireActivity()).getNavigationView()
                        .setCheckedItem(getCheckedMenuItemId());
            }
        }
    }

    @Override
    public void startActivity(@SuppressLint("UnknownNullness") Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            onStartActivityException(e);
        }
    }

    @Override
    public void startActivity(@SuppressLint("UnknownNullness") Intent intent,
            @Nullable Bundle options) {
        try {
            super.startActivity(intent, options);
        } catch (Exception e) {
            onStartActivityException(e);
        }
    }

    @Override
    public void startActivityForResult(@SuppressLint("UnknownNullness") Intent intent,
            int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            onStartActivityException(e);
        }
    }

    @Override
    public void startActivityForResult(@SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options) {
        try {
            super.startActivityForResult(intent, requestCode, options);
        } catch (Exception e) {
            onStartActivityException(e);
        }
    }

    /**
     * This method will be called when there is an exception on starting the activity
     * from this fragment.
     *
     * @param exception The exception occurred.
     *
     * @see #startActivity(Intent)
     * @see #startActivity(Intent, Bundle)
     * @see #startActivityForResult(Intent, int)
     * @see #startActivityForResult(Intent, int, Bundle)
     */
    protected void onStartActivityException(@Nullable Exception exception) {
        if (exception instanceof ActivityNotFoundException) {
            throw new ActivityNotFoundException();
        } else {
            Dynamic.showSnackbar(getActivity(), R.string.ads_error);

            if (exception != null) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void postponeEnterTransition() {
        super.postponeEnterTransition();

        if (getActivity() instanceof AppCompatActivity) {
            requireActivity().supportPostponeEnterTransition();
        }
    }

    @Override
    public void startPostponedEnterTransition() {
        super.startPostponedEnterTransition();

        if (getActivity() instanceof AppCompatActivity) {
            requireActivity().supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void onApplyTransitions(boolean exit) {
        if (getActivity() != null) {
            setAllowEnterTransitionOverlap(true);
            setAllowReturnTransitionOverlap(true);

            setEnterTransition(onAdjustEnterReturnTransition(
                    getDynamicEnterTransition(), true));
            setReturnTransition(onAdjustEnterReturnTransition(
                    getDynamicReturnTransition(), false));
            setExitTransition(onAdjustExitReenterTransition(
                    getDynamicExitTransition(), true));
            setReenterTransition(onAdjustExitReenterTransition(
                    getDynamicReenterTransition(), false));
        }

        if (!DynamicSdkUtils.is21() || getActivity() == null) {
            return;
        }

        if (getActivity() instanceof DynamicSystemActivity) {
            ((DynamicSystemActivity) requireActivity()).setDynamicTransitionListener(this);
        }

        final View transitionView = getPostponeTransitionView();
        if (transitionView != null) {
            transitionView.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            transitionView.getViewTreeObserver().removeOnPreDrawListener(this);
                            startPostponedEnterTransition();

                            return true;
                        }
                    });
        } else {
            startPostponedEnterTransition();
        }
    }

    @Override
    public @Nullable Object getDynamicEnterTransition() {
        if (getView() != null) {
            return DynamicMotion.getInstance().withDuration(new MaterialSharedAxis(
                    MaterialSharedAxis.Y, true).addTarget(getView()));
        } else {
            return DynamicMotion.getInstance().withDuration(
                    new MaterialSharedAxis(MaterialSharedAxis.Y, true));
        }
    }

    @Override
    public @Nullable Object getDynamicReturnTransition() {
        return getReturnTransition();
    }

    @Override
    public @Nullable Object getDynamicExitTransition() {
        if (getView() != null) {
            return DynamicMotion.getInstance().withDuration(
                    new MaterialFadeThrough().addTarget(getView()));
        } else {
            return DynamicMotion.getInstance().withDuration(new MaterialFadeThrough());
        }
    }

    @Override
    public @Nullable Object getDynamicReenterTransition() {
        return getReenterTransition();
    }

    @Override
    public @Nullable Object onAdjustEnterReturnTransition(
            @Nullable Object transition, boolean enter) {
        return transition;
    }

    @Override
    public @Nullable Object onAdjustExitReenterTransition(
            @Nullable Object transition, boolean exit) {
        return transition;
    }

    @Override
    public @Nullable View getPostponeTransitionView() {
        return getView();
    }

    @Override
    public @Nullable View onFindView(int resultCode, int position,
            @NonNull String transition, @IdRes int viewId) {
        return getView() != null ? getView().findViewById(viewId) : null;
    }

    @Override
    public void onSearchViewExpanded() {
        setMenuVisibility(false);

        Dynamic.removeSearchViewTextChangedListener(getActivity(), getTextWatcher());
        Dynamic.addSearchViewTextChangedListener(getActivity(), getTextWatcher());
    }

    @Override
    public void onSearchViewCollapsed() {
        setMenuVisibility(true);

        Dynamic.removeSearchViewTextChangedListener(getActivity(), getTextWatcher());
    }

    @Override
    public @Nullable TextWatcher getTextWatcher() {
        return null;
    }

    /**
     * Call {@link Activity#startActivity(Intent, Bundle)} from the fragment's
     * containing Activity.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param intent The intent to be used.
     * @param options The intent to be set.
     */
    public void startMotionActivity(@SuppressLint("UnknownNullness") Intent intent,
            @Nullable Bundle options) {
        if (DynamicMotion.getInstance().isMotion()) {
            startActivity(intent, options);
        } else {
            startActivity(intent);
        }
    }

    /**
     * Call {@link Activity#startActivityForResult(Intent, int, Bundle)} from the fragment's
     * containing Activity.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param intent The intent to be used.
     * @param requestCode The request code to be set.
     * @param options The intent to be set.
     */
    public void startMotionActivityForResult(@SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options) {
        if (DynamicMotion.getInstance().isMotion()) {
            startActivityForResult(intent, requestCode, options);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * This method will be called after adding the activity header.
     *
     * @param view The view added to the header.
     */
    public void onAddActivityHeader(@Nullable View view) { }

    /**
     * Returns whether to register a listener to listen the search view expand and collapse
     * callbacks for the {@link DynamicActivity}.
     *
     * @return {@code true} to register a {@link DynamicSearchListener} to listen the search view
     * expand and collapse callbacks for the {@link DynamicActivity}.
     */
    public boolean isSearchViewListenerListener() {
        return false;
    }

    /**
     * Returns whether to set the options menu for this fragment.
     *
     * @return {@code true} if set the options menu for this fragment.
     *
     * @see #setHasOptionsMenu(boolean)
     * @see #onDynamicResume(boolean)
     */
    public boolean setHasOptionsMenu() {
        return false;
    }

    /**
     * Returns whether to force menu icons for this fragment.
     *
     * @return {@code true} if force menu icons for this fragment.
     *
     * @see #onPrepareMenu(Menu)
     * @see DynamicMenuUtils#forceMenuIcons(Menu)
     */
    public boolean isForceMenuIcons() {
        return true;
    }

    /**
     * Returns whether to register a shared preferences listener for this fragment.
     *
     * @return {@code true} to register a {@link SharedPreferences.OnSharedPreferenceChangeListener}
     *         to receive preference change callback.
     */
    public boolean isOnSharedPreferenceChangeListener() {
        return false;
    }

    /**
     * Returns whether to use a menu provider for this fragment.
     *
     * @return {@code true} if a menu provider is required for this fragment.
     *
     * @see androidx.fragment.app.FragmentActivity#addMenuProvider(MenuProvider)
     */
    protected boolean isHasMenuProvider() {
        return false;
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        if (isForceMenuIcons()) {
            DynamicMenuUtils.forceMenuIcons(menu);
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) { }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) { }

    @Override
    public void onStart() {
        super.onStart();

        onDynamicResume(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        onDynamicResume(false);
    }

    @Override
    public void onPause() {
        onDynamicPause(false);
        super.onPause();
    }

    @Override
    public void onStop() {
        onDynamicPause(true);
        super.onStop();
    }

    @CallSuper
    @Override
    public void onDynamicResume(boolean forced) {
        if (isOnSharedPreferenceChangeListener() && getContext() != null) {
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .registerOnSharedPreferenceChangeListener(this);
        }

        if (forced) {
            onApplyTransitions(true);
        }

        if (isSearchViewListenerListener()) {
            Dynamic.setSearchViewListener(getActivity(), this);
        }
    }

    @CallSuper
    @Override
    public void onDynamicPause(boolean forced) {
        if (forced) {
            Dynamic.collapseSearchView(getActivity());
            Dynamic.setSearchViewListener(getActivity(), null);

            if (isOnSharedPreferenceChangeListener() && getContext() != null) {
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                        .unregisterOnSharedPreferenceChangeListener(this);
            }
        }
    }

    /**
     * Get the current saved instance state for this fragment.
     *
     * @return The current saved instance state for this fragment.
     */
    public @Nullable Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    /**
     * Returns the title used by the parent activity.
     *
     * @return The title used by the parent activity.
     */
    protected @Nullable CharSequence getTitle() {
        if (getActivity() != null) {
            return requireActivity().getTitle();
        }

        return null;
    }

    /**
     * Returns the subtitle used by the parent activity.
     *
     * @return The subtitle used by the parent activity.
     */
    @SuppressWarnings("ConstantConditions")
    protected @Nullable CharSequence getSubtitle() {
        if (isSupportActionBar()) {
            return ((AppCompatActivity) requireActivity()).getSupportActionBar().getSubtitle();
        }

        return null;
    }

    /**
     * Returns the bottom navigation view id to make its menu item checked.
     *
     * @return The bottom navigation view id to make its menu item checked.
     *
     * @see #getCheckedMenuItemId()
     */
    protected @IdRes int getNavigationViewId() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Returns the menu item id to make it checked.
     *
     * @return The menu item id to make it checked.
     */
    protected @IdRes int getCheckedMenuItemId() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Checks whether the parent activity is a app compat activity.
     *
     * @return {@code true} if the parent activity is an {@link AppCompatActivity}.
     */
    public boolean isAppCompatActivity() {
        return getActivity() != null && requireActivity() instanceof AppCompatActivity;
    }

    /**
     * Checks whether the support action bar is applied to the parent activity.
     *
     * @return {@code true} if the support action bar is not {@code null}.
     */
    public boolean isSupportActionBar() {
        return isAppCompatActivity() &&
                ((AppCompatActivity) requireActivity()).getSupportActionBar() != null;
    }

    /**
     * Retrieves a parcelable from the fragment arguments associated with the supplied key.
     *
     * @param key The key to be retrieved.
     * @param <T> The type of the parcelable.
     *
     * @return The parcelable from the fragment arguments.
     *
     * @see Fragment#getArguments()
     */
    public @Nullable <T extends Parcelable> T getParcelableFromArguments(@Nullable String key) {
        if (getArguments() == null) {
            return null;
        }

        try {
            return requireArguments().getParcelable(key);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Retrieves a string from the fragment arguments associated with the supplied key.
     *
     * @param key The key to be retrieved.
     *
     * @return The string from the fragment arguments.
     *
     * @see Fragment#getArguments()
     */
    public @Nullable String getStringFromArguments(@Nullable String key) {
        if (getArguments() == null) {
            return null;
        }

        try {
            return requireArguments().getString(key);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Retrieves a boolean from the fragment arguments associated with the supplied key.
     *
     * @param key The key to be retrieved.
     * @param defaultValue The default value.
     *
     * @return The boolean from the fragment arguments.
     *
     * @see Fragment#getArguments()
     */
    public boolean getBooleanFromArguments(@Nullable String key, boolean defaultValue) {
        if (getArguments() == null) {
            return defaultValue;
        }

        return requireArguments().getBoolean(key, defaultValue);
    }

    /**
     * Set result for the parent activity to notify the requester about the action.
     *
     * @param resultCode The result code for the activity.
     * @param intent The result intent to provide any data as an extra.
     * @param finish {@code true} to finish the activity.
     */
    protected void setResult(int resultCode, @Nullable Intent intent, boolean finish) {
        if (getActivity() != null) {
            if (intent != null) {
                requireActivity().setResult(resultCode, intent);
            } else {
                requireActivity().setResult(resultCode);
            }

            if (finish) {
                finishActivity();
            }
        }
    }

    /**
     * Set result for the parent activity to notify the requester about the action.
     *
     * @param resultCode The result code for the activity.
     * @param intent The result intent to provide any data as an extra.
     */
    protected void setResult(int resultCode, @Nullable Intent intent) {
        setResult(resultCode, intent, true);
    }

    /**
     * Set result for the parent activity to notify the requester about the action.
     *
     * @param resultCode The result code for the activity.
     * @param finish {@code true} to finish the activity.
     */
    protected void setResult(int resultCode, boolean finish) {
        setResult(resultCode, null, finish);
    }

    /**
     * Set result for the parent activity to notify the requester about the action.
     *
     * @param resultCode The result code for the activity.
     */
    protected void setResult(int resultCode) {
        setResult(resultCode, null, true);
    }

    /**
     * Finish the parent activity by calling {@link Activity#finish()}.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void finishActivity() {
        if (getActivity() instanceof DynamicSystemActivity) {
            ((DynamicSystemActivity) requireActivity()).finishActivity();
        } else if (getActivity() != null && !requireActivity().isFinishing()) {
            if (DynamicSdkUtils.is21()
                    && (requireActivity().getWindow().getSharedElementEnterTransition() != null
                    || requireActivity().getWindow().getSharedElementReturnTransition() != null)) {
                requireActivity().supportFinishAfterTransition();
            } else {
                requireActivity().finish();
            }
        }
    }

    /**
     * Get the parent activity for this fragment.
     *
     * @return The parent activity as the instance of {@link DynamicSystemActivity}.
     */
    public @NonNull DynamicSystemActivity getSystemActivity() {
        return (DynamicSystemActivity) requireActivity();
    }

    /**
     * Get the parent activity for this fragment.
     *
     * @return The parent activity as the instance of {@link DynamicActivity}.
     */
    public @NonNull DynamicActivity getDynamicActivity() {
        return (DynamicActivity) requireActivity();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }
}
