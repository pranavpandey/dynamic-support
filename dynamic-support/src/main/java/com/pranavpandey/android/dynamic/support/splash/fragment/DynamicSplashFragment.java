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

package com.pranavpandey.android.dynamic.support.splash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.listener.DynamicSplashListener;
import com.pranavpandey.android.dynamic.support.model.DynamicTaskViewModel;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.splash.activity.DynamicSplashActivity;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicStatus;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;

/**
 * A fragment to display splash before launching the main activity.
 * <p>It will be used internally by the {@link DynamicSplashActivity}.
 */
public class DynamicSplashFragment extends DynamicFragment {

    /**
     * Fragment argument key to set the splash layout.
     */
    private static final String ADS_ARGS_SPLASH_LAYOUT_RES = "ads_args_splash_layout_res";

    /**
     * Dynamic task view model to perform any background operation while showing the splash.
     */
    private DynamicTaskViewModel mTaskViewModel;

    /**
     * Listener to implement the splash screen and to get various callbacks while showing
     * the splash.
     */
    private DynamicSplashListener mDynamicSplashListener;

    /**
     * View used by this fragment.
     */
    private View mView;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param layoutRes The layout resource for this fragment.
     *
     * @return A instance of {@link DynamicSplashFragment}.
     */
    public static @NonNull Fragment newInstance(@LayoutRes int layoutRes) {
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
    public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && requireArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES) != -1) {
            mView = inflater.inflate(requireArguments().getInt(ADS_ARGS_SPLASH_LAYOUT_RES),
                    container, false);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mDynamicSplashListener != null) {
            mDynamicSplashListener.onViewCreated(mView);
        }
    }

    @Override
    public @Nullable Object getDynamicEnterTransition() {
        return null;
    }

    @Override
    public @Nullable Object getDynamicExitTransition() {
        return null;
    }

    /**
     * Returns the background color for this fragment.
     *
     * @return The background color for this fragment.
     */
    public @ColorInt int getBackgroundColor() {
        if (getActivity() instanceof DynamicSplashActivity) {
            return ((DynamicSplashActivity) requireActivity()).getBackgroundColor();
        }

        return DynamicTheme.getInstance().get().getPrimaryColor();
    }

    /**
     * Start the splash background task.
     *
     * @param stop {@code true} to stop and restart the splash task.
     */
    public void show(boolean stop) {
        if (stop) {
            stop();
        }

        mTaskViewModel = new ViewModelProvider(this).get(DynamicTaskViewModel.class);
        mTaskViewModel.execute(new SplashTask(mDynamicSplashListener));
    }

    /**
     * Start the splash background task.
     *
     * @see #show(boolean)
     */
    public void show() {
        show(true);
    }

    /**
     * Stop the splash background task.
     */
    public void stop() {
        if (mTaskViewModel != null) {
            mTaskViewModel.cancel(true);
        }
    }

    /**
     * Returns the dynamic task to perform any background operation while showing the splash.
     *
     * @return The dynamic task to perform any background operation while showing the splash.
     */
    public @Nullable DynamicTask<?, ?, ?> getSplashTask() {
        if (mTaskViewModel == null) {
            return null;
        }

        return mTaskViewModel.getTask();
    }

    /**
     * Returns the view used by this fragment.
     *
     * @return The view used by this fragment.
     */
    public @Nullable View getView() {
        return mView;
    }

    /**
     * Checks whether the splash task is running or not
     *
     * @return {@code true} if the splash task is running.
     */
    public boolean isSplashTaskRunning() {
        if (getSplashTask() == null) {
            return false;
        }

        return getSplashTask().getStatus() == DynamicStatus.RUNNING;
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
     * Dynamic task to perform any background operation while showing the splash.
     */
    static final class SplashTask extends DynamicTask<Void, Void, Void> {

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
        private final DynamicSplashListener dynamicSplashListener;

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

            try {
                Thread.sleep(DynamicMotion.Duration.TINY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception ignored) {
            }

            taskStartTime = System.currentTimeMillis();

            if (dynamicSplashListener != null) {
                dynamicSplashListener.onPreSplash();
            }
        }

        @Override
        protected Void doInBackground(@Nullable Void params) {
            taskTimeElapsed = System.currentTimeMillis() - taskStartTime;

            if (dynamicSplashListener != null) {
                dynamicSplashListener.doBehindSplash();

                if (taskTimeElapsed < dynamicSplashListener.getMinSplashTime()) {
                    try {
                        Thread.sleep(dynamicSplashListener.getMinSplashTime() - taskTimeElapsed);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception ignored) {
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(@Nullable DynamicResult<Void> result) {
            super.onPostExecute(result);

            if (dynamicSplashListener != null) {
                dynamicSplashListener.onPostSplash();
            }
        }

        @Override
        protected void onCancelled(DynamicResult<Void> result) {
            super.onCancelled(result);

            if (dynamicSplashListener != null) {
                dynamicSplashListener.onPreSplash();
            }
        }
    }
}
