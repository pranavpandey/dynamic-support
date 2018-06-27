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

package com.pranavpandey.android.dynamic.support.splash;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment to display splash before launching the main activity.
 * It will be used internally by the {@link DynamicSplashActivity}.
 */
public class DynamicSplashFragment extends Fragment {

    /**
     * Fragment argument key to set the splash layout.
     */
    private static final String ADS_ARGS_SPLASH_LAYOUT_RES = "ads_args_splash_layout_res";

    /**
     * Async task to perform any background operation whiling showing the
     * splash.
     */
    private SplashTask mSplashTask;

    /**
     * Listener to implement the splash screen and to get various
     * callbacks while showing the splash.
     */
    private static DynamicSplashListener mDynamicSplashListener;

    /**
     * View used by this fragment.
     */
    protected View mView;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param layoutRes The layout resource for this fragment.
     *
     * @return A instance of {@link DynamicSplashFragment}.
     */
    public static Fragment newInstance(@LayoutRes int layoutRes) {
        DynamicSplashFragment fragment = new DynamicSplashFragment();
        Bundle args = new Bundle();
        args.putInt(ADS_ARGS_SPLASH_LAYOUT_RES, layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDynamicSplashListener.onViewCreated(mView);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES) != -1) {
            mView = inflater.inflate(getArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES),
                    container, false);
        }

        return mView;
    }

    /**
     * Start the splash background task.
     */
    public void show() {
        mSplashTask = new SplashTask();
        mSplashTask.execute();
    }

    /**
     * Stop the splash background task.
     */
    public void stop() {
        if (mSplashTask != null && !mSplashTask.isCancelled()) {
            mSplashTask.cancel(true);
        }
    }

    /**
     * @return The listener to implement the splash screen and to get
     *         various callbacks while showing the splash.
     */
    public DynamicSplashListener getOnSplashListener() {
        return mDynamicSplashListener;
    }

    /**
     * Set the listener to implement the splash screen and to get
     * various callbacks while showing the splash.
     *
     * @param dynamicSplashListener The listener to be set.
     */
    public void setOnSplashListener(DynamicSplashListener dynamicSplashListener) {
        mDynamicSplashListener = dynamicSplashListener;
    }

    /**
     * Async task to perform any background operation while showing
     * the splash.
     */
    static class SplashTask extends AsyncTask<Void, String, Void> {

        long taskStartTime;
        long taskTimeElapsed;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            taskStartTime = System.currentTimeMillis();
            mDynamicSplashListener.onPreSplash();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mDynamicSplashListener.doBehindSplash();

            taskTimeElapsed = System.currentTimeMillis() - taskStartTime;
            if (taskTimeElapsed < mDynamicSplashListener.getMinSplashTime()) {
                try {
                    Thread.sleep(mDynamicSplashListener.getMinSplashTime() - taskTimeElapsed);
                } catch (InterruptedException ignored) {
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

            mDynamicSplashListener.onPostSplash();
        }
    }
}
