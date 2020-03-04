/*
 * Copyright 2020 Pranav Pandey
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;

/**
 * A fragment to display splash before launching the main activity.
 * <p>It will be used internally by the {@link DynamicSplashActivity}.
 */
public class DynamicSplashFragment extends Fragment {

    /**
     * Fragment argument key to set the splash layout.
     */
    private static final String ADS_ARGS_SPLASH_LAYOUT_RES = "ads_args_splash_layout_res";

    /**
     * Async task to perform any background operation whiling showing the splash.
     */
    private SplashTask mSplashTask;

    /**
     * Listener to implement the splash screen and to get various callbacks while showing
     * the splash.
     */
    private DynamicSplashListener mDynamicSplashListener;

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

        if (mDynamicSplashListener != null) {
            mDynamicSplashListener.onViewCreated(mView);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && requireArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES) != -1) {
            mView = inflater.inflate(requireArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES),
                    container, false);
        }

        return mView;
    }

    /**
     * Returns the background color for this fragment.
     *
     * @return The background color for this fragment.
     */
    public @ColorInt int getBackgroundColor() {
        return DynamicTheme.getInstance().get().getPrimaryColor();
    }

    /**
     * Start the splash background task.
     */
    public void show() {
        mSplashTask = new SplashTask(mDynamicSplashListener);
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
     * Returns listener to implement the splash screen and to get various callbacks while
     * showing the splash.
     *
     * @return The listener to implement the splash screen.
     */
    public @Nullable DynamicSplashListener getOnSplashListener() {
        return mDynamicSplashListener;
    }

    /**
     * Set the listener to implement the splash screen and to get various callbacks while
     * showing the splash.
     *
     * @param dynamicSplashListener The listener to be set.
     */
    public void setOnSplashListener(@Nullable DynamicSplashListener dynamicSplashListener) {
        mDynamicSplashListener = dynamicSplashListener;
    }

    /**
     * Async task to perform any background operation while showing the splash.
     */
    static class SplashTask extends AsyncTask<Void, String, Void> {

        /**
         * Start time for this task.
         */
        long taskStartTime;

        /**
         * Time elapsed while executing this task.
         */
        long taskTimeElapsed;

        /**
         * Listener to implement the splash screen and to get various callbacks while
         * showing the splash.
         */
        private DynamicSplashListener dynamicSplashListener;

        /**
         * Constructor to initialize an object of this class.
         *
         * @param dynamicSplashListener The splash listener to get the various callbacks.
         */
        SplashTask(@Nullable DynamicSplashListener dynamicSplashListener) {
            this.dynamicSplashListener = dynamicSplashListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            taskStartTime = System.currentTimeMillis();
            if (dynamicSplashListener != null) {
                dynamicSplashListener.onPreSplash();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (dynamicSplashListener != null) {
                dynamicSplashListener.doBehindSplash();

                taskTimeElapsed = System.currentTimeMillis() - taskStartTime;
                if (taskTimeElapsed < dynamicSplashListener.getMinSplashTime()) {
                    try {
                        Thread.sleep(dynamicSplashListener.getMinSplashTime() - taskTimeElapsed);
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

            if (dynamicSplashListener != null) {
                dynamicSplashListener.onPostSplash();
            }
        }
    }
}
