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

package com.pranavpandey.android.dynamic.support.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicEmptyView;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

/**
 * A {@link RecyclerView} inside a {@link FrameLayout} with some built-in functionality like
 * swipe refresh layout, progress bar, etc. that can be initialized quickly.
 */
public abstract class DynamicRecyclerViewFrame extends FrameLayout {

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
     * Empty view that can be shown when the data is empty.
     *
     * @see #showEmptyView()
     * @see #hideEmptyView()
     */
    private DynamicEmptyView mEmptyView;

    /**
     * Progress bar that can be shown while the data is loading in the background.
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

        onLoadAttributes(attrs);
    }

    public DynamicRecyclerViewFrame(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onLoadAttributes(attrs);
    }

    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        inflate(getContext(), getLayoutRes(), this);

        mSwipeRefreshLayout = findViewById(R.id.ads_swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.ads_recycler_view);
        mEmptyView = findViewById(R.id.ads_empty_view);
        mProgressBar = findViewById(R.id.ads_progress);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicRecyclerViewFrame);

        try {
            if (mEmptyView != null) {
                mEmptyView.setIcon(DynamicResourceUtils.getDrawable(getContext(),
                        a.getResourceId(
                                R.styleable.DynamicRecyclerViewFrame_ads_empty_view_icon,
                                DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE)));
                mEmptyView.setTitle(a.getString(
                        R.styleable.DynamicRecyclerViewFrame_ads_empty_view_title));
                mEmptyView.setSubtitle(a.getString(
                        R.styleable.DynamicRecyclerViewFrame_ads_empty_view_subtitle));
            }
        } finally {
            a.recycle();
        }

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
    protected void initialize() {
        setRecyclerViewLayoutManager(getRecyclerViewLayoutManager());
        onCreateRecyclerView(mRecyclerView);
        setSwipeRefreshLayout(mSwipeRefreshLayout);
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
     * @param notify {@code true} to notify data set changed.
     */
    public void setRecyclerViewLayoutManager(
            final @Nullable RecyclerView.LayoutManager layoutManager, boolean notify) {
        this.mRecyclerViewLayoutManager = layoutManager;
        if (mRecyclerViewLayoutManager == null) {
            mRecyclerViewLayoutManager = DynamicLayoutUtils.getLinearLayoutManager(
                    getContext(), LinearLayoutManager.VERTICAL);
        }

        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        checkForStaggeredGridLayoutManager();

        if (notify) {
            notifyDataSetChanged();
        }
    }

    /**
     * Set supplied LayoutManager to the RecyclerView.
     *
     * @param layoutManager Layout manager for the recycler view;
     *
     * @see #setRecyclerViewLayoutManager(RecyclerView.LayoutManager, boolean)
     */
    public void setRecyclerViewLayoutManager(
            final @Nullable RecyclerView.LayoutManager layoutManager) {
        setRecyclerViewLayoutManager(layoutManager, true);
    }

    /**
     * Set an adapter for the recycler view.
     *
     * @param adapter The recycler view adapter.
     */
    public void setAdapter(
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mRecyclerView.setAdapter(adapter);

        checkForStaggeredGridLayoutManager();
    }

    /**
     * Checks for the {@link StaggeredGridLayoutManager} to avoid the jumping of items.
     */
    protected void checkForStaggeredGridLayoutManager() {
        post(mStaggeredGridRunnable);
    }

    /**
     * Returns the adapter used by the recycler view.
     *
     * @return The adapter used by the recycler view.
     */
    public @Nullable RecyclerView.Adapter<?> getAdapter() {
        return mRecyclerView.getAdapter();
    }

    /**
     * Notify data set changed for the recycler view adapter.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged() {
        if (getAdapter() != null && !getRecyclerView().isComputingLayout()) {
            getAdapter().notifyDataSetChanged();
            checkForStaggeredGridLayoutManager();
        }
    }

    /**
     * Notify item changed for the recycler view adapter.
     *
     * @param position The changed position.
     */
    public void notifyItemChanged(int position) {
        if (getAdapter() != null && !getRecyclerView().isComputingLayout()) {
            getAdapter().notifyItemChanged(position);
        }
    }

    /**
     * Override this method to perform other operations on the recycler view.
     *
     * @param recyclerView The recycler view inside this view.
     */
    @CallSuper
    protected void onCreateRecyclerView(@NonNull RecyclerView recyclerView) { }

    /**
     * Override this method to set swipe refresh layout listener immediately after
     * initializing the view.
     *
     * @return The on refresh listener for the swipe refresh layout.
     *
     * @see #setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)
     */
    protected @Nullable SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return null;
    }

    /**
     * Get the recycler view used by this view.
     *
     * @return The recycler view to display the data.
     */
    public @NonNull RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Get the layout manager used by recycler view.
     *
     * @return The layout manager used by recycler view.
     */
    public @Nullable RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView == null ? null : mRecyclerView.getLayoutManager();
    }

    /**
     * Get the swipe refresh layout used by this view.
     *
     * @return The swipe refresh layout used by this view.
     */
    public @Nullable SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * Set the swipe refresh layout for this view.
     *
     * @param swipeRefreshLayout The swipe refresh layout to be set.
     */
    public void setSwipeRefreshLayout(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;

        setOnRefreshListener(setOnRefreshListener());
    }

    /**
     * Get the swipe refresh layout listener used by this view.
     *
     * @return The swipe refresh layout listener used by this view.
     */
    public @Nullable SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
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
     * Get the empty view that can be shown when the data is empty.
     *
     * @return The empty view that can be shown when the data is empty.
     */
    public @Nullable DynamicEmptyView getEmptyView() {
        return mEmptyView;
    }

    /**
     * Show empty view on top of the recycler view.
     *
     * @param animate {@code true} to animate the changes.
     */
    public void showEmptyView(final boolean animate) {
        if (mEmptyView == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (animate && DynamicMotion.getInstance().isMotion()) {
                    DynamicMotion.getInstance().beginDelayedTransition(
                            DynamicRecyclerViewFrame.this);
                }

                Dynamic.setVisibility(mRecyclerView, GONE);
                Dynamic.setVisibility(findViewById(R.id.ads_empty_view_scroll), VISIBLE);
            }
        });
    }

    /**
     * Show empty view on top of the recycler view.
     *
     * @see #showEmptyView(boolean)
     */
    public void showEmptyView() {
        showEmptyView(true);
    }

    /**
     * Hide empty view from the top of the recycler view.
     *
     * @param animate {@code true} to animate the changes.
     */
    public void hideEmptyView(final boolean animate) {
        if (mEmptyView == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (animate && DynamicMotion.getInstance().isMotion()) {
                    DynamicMotion.getInstance().beginDelayedTransition(
                            DynamicRecyclerViewFrame.this);
                }

                Dynamic.setVisibility(mRecyclerView, VISIBLE);
                Dynamic.setVisibility(findViewById(R.id.ads_empty_view_scroll), GONE);
            }
        });
    }

    /**
     * Hide empty view from the top of the recycler view.
     *
     * @see #hideEmptyView(boolean)
     */
    public void hideEmptyView() {
        hideEmptyView(true);
    }

    /**
     * Get the progress bar that can be shown while the data is loading in the background.
     *
     * @return The progress bar that can be shown while the data is loading in the background.
     */
    public @Nullable ContentLoadingProgressBar getProgressBar() {
        return mProgressBar;
    }

    /**
     * Set the refreshing state for the swipe refresh layout.
     *
     * @param refreshing {@code true} if the the data is refreshing.
     */
    public void setRefreshing(boolean refreshing) {
        if (getSwipeRefreshLayout() != null) {
            getSwipeRefreshLayout().setRefreshing(refreshing);
        }
    }

    /**
     * Show progress bar and hide the recycler view.
     *
     * @param animate {@code true} to animate the changes.
     */
    public void showProgress(final boolean animate) {
        if (mProgressBar == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (animate && DynamicMotion.getInstance().isMotion()) {
                    DynamicMotion.getInstance().beginDelayedTransition(
                            DynamicRecyclerViewFrame.this);
                }

                Dynamic.setVisibility(mProgressBar, VISIBLE);
                Dynamic.setVisibility(mRecyclerView, GONE);
            }
        });
    }

    /**
     * Show progress bar and hide the recycler view.
     *
     * @see #showProgress(boolean)
     */
    public void showProgress() {
        showProgress(true);
    }

    /**
     * Hide progress bar and show the recycler view.
     *
     * @param animate {@code true} to animate the changes.
     */
    public void hideProgress(final boolean animate) {
        if (mProgressBar == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (animate && DynamicMotion.getInstance().isMotion()) {
                    DynamicMotion.getInstance().beginDelayedTransition(
                            DynamicRecyclerViewFrame.this);
                }

                Dynamic.setVisibility(mProgressBar, GONE);
                Dynamic.setVisibility(mRecyclerView, VISIBLE);
            }
        });
    }

    /**
     * Hide progress bar and show the recycler view.
     *
     * @see #hideProgress(boolean)
     */
    public void hideProgress() {
        hideProgress(true);
    }

    /**
     * Returns the root scrollable view for this frame layout.
     *
     * @return The root scrollable view.
     */
    public @Nullable View getViewRoot() {
        return getRecyclerView();
    }

    /**
     * Smooth scroll the root view according to the supplied values.
     *
     * @param x The x position to scroll to.
     * @param y The y position to scroll to.
     */
    public void smoothScrollTo(final int x, final int y) {
        if (getViewRoot() == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                getViewRoot().scrollTo(x, y);
            }
        });
    }

    /**
     * Scroll the recycler view view layout manager to the supplied position.
     *
     * @param position The position to scroll to.
     */
    public void scrollToPosition(final int position) {
        if (getLayoutManager() == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                getLayoutManager().scrollToPosition(position);
            }
        });
    }

    /**
     * Handler to update the {@link StaggeredGridLayoutManager} o avoid the jumping of items.
     */
    protected final Runnable mStaggeredGridRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRecyclerView == null || mRecyclerViewLayoutManager == null
                    || !(mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager)
                    || mRecyclerViewLayoutManager.getChildCount() <= 0) {
                return;
            }

            ((StaggeredGridLayoutManager) mRecyclerViewLayoutManager).setGapStrategy(
                    ((StaggeredGridLayoutManager) mRecyclerViewLayoutManager)
                            .getGapStrategy() | GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            ((StaggeredGridLayoutManager) mRecyclerViewLayoutManager)
                    .invalidateSpanAssignments();

            if (((StaggeredGridLayoutManager)
                    mRecyclerViewLayoutManager).getSpanCount() > 1) {
                ((StaggeredGridLayoutManager) mRecyclerViewLayoutManager)
                        .scrollToPositionWithOffset(0, 0);
            }
        }
    };
}
