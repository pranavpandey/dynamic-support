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

package com.pranavpandey.android.dynamic.support.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity;

/**
 * Base fragment class to provide basic functionality and to work
 * with the {@link DynamicActivity}. Extend this fragment to add
 * more functionality according to the need
 */
public abstract class DynamicFragment extends Fragment {

    /**
     * @return The title used by the parent activity.
     */
    protected @Nullable CharSequence getTitle() {
        if (isSupportActionBar()) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle();
        }

        return null;
    }

    /**
     * @return The subtitle used by the parent activity.
     */
    protected @Nullable CharSequence getSubtitle() {
        if (isSupportActionBar()) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar().getSubtitle();
        }

        return null;
    }


    /**
     * @return The menu item id to make it checked if the parent activity
     * is a {@link DynamicDrawerActivity}.
     */
    protected @IdRes int setNavigationViewCheckedItem() {
        return -1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isSupportActionBar()) {
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar().setTitle(getTitle());
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar().setSubtitle(getSubtitle());
        }

        if (setNavigationViewCheckedItem() != -1
                && getActivity() instanceof DynamicDrawerActivity) {
            ((DynamicDrawerActivity) getActivity()).getNavigationView()
                    .setCheckedItem(setNavigationViewCheckedItem());
        }
    }

    /**
     * @return {@code true} if the parent activity is an
     * {@link AppCompatActivity}.
     */
    protected boolean isAppCompatActivity() {
        return getActivity() != null && getActivity() instanceof AppCompatActivity;
    }

    /**
     * @return {@code true} if the support action bar is not {@code null}.
     */
    protected boolean isSupportActionBar() {
        return isAppCompatActivity() &&
                ((AppCompatActivity) getActivity()).getSupportActionBar() != null;
    }

    /**
     * Set result for the parent activity to notify the requester about
     * the action.
     *
     * @param resultCode The result code for the activity.
     * @param intent The result intent to provide any data as an extra.
     * @param finish {@code true} to finish the activity.
     */
    protected void setResult(int resultCode, @Nullable Intent intent, boolean finish) {
        if (getActivity() != null) {
            if (intent != null) {
                getActivity().setResult(resultCode, intent);
            } else {
                getActivity().setResult(resultCode);
            }

            if (finish) {
                finishActivity();
            }
        }
    }

    /**
     * Set result for the parent activity to notify the requester about
     * the action.
     *
     * @param resultCode The result code for the activity.
     * @param intent The result intent to provide any data as an extra.
     */
    protected void setResult(int resultCode, @Nullable Intent intent) {
        setResult(resultCode, intent, true);
    }

    /**
     * Set result for the parent activity to notify the requester about
     * the action.
     *
     * @param resultCode The result code for the activity.
     * @param finish {@code true} to finish the activity.
     */
    protected void setResult(int resultCode, boolean finish) {
        setResult(resultCode, null, finish);
    }

    /**
     * Set result for the parent activity to notify the requester about
     * the action.
     *
     * @param resultCode The result code for the activity.
     */
    protected void setResult(int resultCode) {
        setResult(resultCode, null, true);
    }

    /**
     * Finish the parent activity by calling {@link Activity#finish()}.
     */
    protected void finishActivity() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    /**
     * @return The parent activity as the instance of {@link DynamicActivity}.
     */
    public DynamicActivity getDynamicActivity() {
        return (DynamicActivity) getActivity();
    }
}
