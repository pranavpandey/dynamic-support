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

package com.pranavpandey.android.dynamic.support.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;

/**
 * A RecyclerView inside a FrameLayout with some built-in functionality like
 * swipe refresh layout, progress bar, etc. which can be initialized quickly.
 */
public abstract class DynamicRecyclerViewFrame extends FrameLayout {

    /**
     * State key for the fragment super state.
     */
    private static final String ADS_RECYCLER_VIEW_SCROLL_OFFSET = "superState";

    /**
     * Animation duration to show or hide the progress.
     */
    private int mShortAnimationDuration;

    /**
     * Swipe refresh layout to provide pull to refresh functionality.
     *
     * @see #setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Use this listener to get callbacks from the SwipeRefreshLayout.
     *
     * @see #setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    /**
     * Recycler view to display the data.
     *
     * @see #onCreateRecyclerView(RecyclerView)
     */
    private RecyclerView mRecyclerView;

    /**
     * Layout manager used by the recycler view.
     *
     * @see #getRecyclerViewLayoutManager()
     */
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    /**
     * Progress bar which can be shown while the data is loading in the background.
     *
     * @see #showProgress()
     * @see #hideProgress()
     */
    private ContentLoadingProgressBar mProgressBar;

    public DynamicRecyclerViewFrame(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRecyclerViewFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public DynamicRecyclerViewFrame(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    /**
     * This method will be called to get the layout resource for this view.
     * <p>Supply the view layout resource here to do the inflation.
     *
     * @return The layout used by this view. Override this to supply a different layout.
     */
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_recycler_view;
    }

    /**
     * Initialize this view with default settings.
     */
    private void initialize() {
        inflate(getContext(), getLayoutRes(), this);
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mSwipeRefreshLayout = findViewById(R.id.ads_swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.ads_dynamic_recycler_view);
        mProgressBar = findViewById(R.id.ads_progress);

        setRecyclerViewLayoutManager(getRecyclerViewLayoutManager());
        onCreateRecyclerView(mRecyclerView);
        setOnRefreshListener(setOnRefreshListener());
    }

    /**
     * This method will be called to return the layout manager for the recycler view.
     * <p>Override this method to supply the layout manager during.
     *
     * @return The layout manager for the recycler view.
     */
    public abstract @Nullable RecyclerView.LayoutManager getRecyclerViewLayoutManager();


    /**
     * Set supplied LayoutManager to the RecyclerView.
     *
     * @param layoutManager Layout manager for the recycler view;
     */
    public void setRecyclerViewLayoutManager(
            @Nullable final RecyclerView.LayoutManager layoutManager) {
        this.mRecyclerViewLayoutManager = layoutManager;
        if (mRecyclerViewLayoutManager == null) {
            mRecyclerViewLayoutManager = DynamicLayoutUtils.getLinearLayoutManager(
                    getContext(), LinearLayoutManager.VERTICAL);
        }

        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        checkForStaggeredGridLayoutManager();

        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Set an adapter for the recycler view.
     *
     * @param adapter The recycler view adapter.
     */
    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);

        checkForStaggeredGridLayoutManager();
    }

    /**
     * Checks for the staggered grid layout manager to avoid the jumping of items by
     * scrolling the recycler view to top.
     */
    protected void checkForStaggeredGridLayoutManager() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    ((StaggeredGridLayoutManager) mRecyclerViewLayoutManager)
                            .scrollToPositionWithOffset(0, 0);
                }
            }
        });
    }

    /**
     * Returns the adapter used by the recycler view.
     *
     * @return The adapter used by the recycler view.
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    /**
     * Override this method to perform other operations on the recycler view.
     *
     * @param recyclerView The recycler view inside this view.
     */
    protected void onCreateRecyclerView(@NonNull RecyclerView recyclerView) { }

    /**
     * Override this method to set swipe refresh layout listener immediately after
     * initializing the view.
     *
     * @return The on refresh listener for the swipe refresh layout.
     *
     * @see #setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)
     */
    protected SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return null;
    }

    /**
     * Get the recycler view used by this view.
     *
     * @return The recycler view to display the data.
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Get the swipe refresh layout used by this view.
     *
     * @return The swipe refresh layout used by this view.
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * Get the swipe refresh layout listener used by this view.
     *
     * @return The swipe refresh layout listener used by this view.
     */
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    /**
     * Set the swipe refresh layout listener to provide pull to efresh functionality.
     *
     * @param onRefreshListener The listener to be set.
     */
    public void setOnRefreshListener(
            @Nullable SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;

        if (mSwipeRefreshLayout != null) {
            if (onRefreshListener != null) {
                mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }
    }

    /**
     * Get the progress bar which can be shown while the data is loading in the background.
     *
     * @return The progress bar which can be shown while the data is loading in the background.
     */
    public ContentLoadingProgressBar getProgressBar() {
        return mProgressBar;
    }

    /**
     * Show progress bar and hide the recycler view.
     */
    public void showProgress() {
        if (mProgressBar != null) {
            mRecyclerView.setAlpha(0f);
            mProgressBar.setAlpha(0f);

            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    /**
     * Hide progress bar and show the recycler view.
     */
    public void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressBar.setVisibility(View.GONE);
                            mRecyclerView.animate()
                                    .alpha(1f)
                                    .setDuration(mShortAnimationDuration)
                                    .setListener(null);
                        }
                    });
        }
    }

    /**
     * Returns the root scrollable view for this frame layout.
     *
     * @return The root scrollable view.
     */
    public View getViewRoot() {
        return getRecyclerView();
    }

    /**
     * Smooth scroll the root view according to the supplied values.
     *
     * @param x The x position to scroll to.
     * @param y The y position to scroll to.
     */
    public void smoothScrollTo(final int x, final int y) {
        post(new Runnable() {
            @Override
            public void run() {
                getViewRoot().scrollTo(x, y);
            }
        });
    }
}
