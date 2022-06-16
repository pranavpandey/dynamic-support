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

package com.pranavpandey.android.dynamic.support.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.pranavpandey.android.dynamic.utils.DynamicTaskUtils;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicConcurrent;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicExecutor;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicStatus;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicTask;

import java.util.concurrent.ExecutorService;

/**
 * An {@link AndroidViewModel} to manage the {@link DynamicTask} state.
 */
public class DynamicTaskViewModel extends AndroidViewModel implements DynamicExecutor {

    /**
     * Executor service to execute the task.
     */
    private final ExecutorService mExecutorService;

    /**
     * The task managed by this view model.
     */
    private DynamicTask<?, ?, ?> mTask;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param application The application associated with this view model.
     */
    public DynamicTaskViewModel(@NonNull Application application) {
        super(application);

        mExecutorService = DynamicConcurrent.getDefaultExecutor();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        cancel(true);
    }

    @Override
    public @NonNull ExecutorService getDefaultExecutor() {
        return mExecutorService;
    }

    /**
     * Get the task managed by this view model.
     *
     * @return The task managed by this view model.
     */
    public @Nullable DynamicTask<?, ?, ?> getTask() {
        return mTask;
    }

    /**
     * Execute the supplied task and manage its state.
     *
     * @param task The task to be executed.
     */
    public void execute(@Nullable DynamicTask<?, ?, ?> task) {
        cancel(true);

        this.mTask = task;
        DynamicConcurrent.getInstance().execute(getDefaultExecutor(), getTask());
    }

    /**
     * Check whether the task is already running.
     *
     * @return {@code true} if the task is already running.
     */
    public boolean isRunning() {
        return getTask() != null && getTask().getStatus() == DynamicStatus.RUNNING;
    }

    /**
     * Try to cancel the task managed by this view model.
     *
     * @param mayInterruptIfRunning {@code true} if the thread executing the
     *        task should be interrupted; otherwise, in-progress tasks are allowed
     *        to complete.
     *
     * @see DynamicTaskUtils#cancelTask(DynamicTask, boolean)
     */
    public void cancel(boolean mayInterruptIfRunning) {
        DynamicTaskUtils.cancelTask(getTask(), mayInterruptIfRunning);
    }
}
