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
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A RecyclerView inside a FrameLayout with some built-in functionality
 * like swipe refresh layout, progress bar, etc. which can be initialized
 * quickly.
 */
public abstract class DynamicRecyclerViewFrame extends FrameLayout {

    /**
     * Constant for the type empty view.
     */
    public static final int TYPE_EMPTY_VIEW = 0;

    /**
     * Constant for the type section header.
     */
    public static final int TYPE_SECTION_HEADER = 1;

    /**
     * Constant for the type item.
     */
    public static final int TYPE_ITEM = 2;

    /**
     * Constant for the type section divider.
     */
    public static final int TYPE_SECTION_DIVIDER = 3;

    /**
     * Valid item types for this adapter.
     *
     * <p>0. {@link #TYPE_EMPTY_VIEW}
     * <br>1. {@link #TYPE_SECTION_HEADER}
     * <br>2. {@link #TYPE_ITEM}
     * <br>3. {@link #TYPE_SECTION_DIVIDER}</p>
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { TYPE_EMPTY_VIEW, TYPE_SECTION_HEADER, TYPE_ITEM, TYPE_SECTION_DIVIDER })
    public @interface ItemType { }

    /**
     * Implement this interface in the object class to get item
     * type and section header text.
     */
    public interface DynamicRecyclerViewItem {

        /**
         * @return Item type of this object.
         * @see ItemType
         */
        @ItemType
        int getItemViewType();

        /**
         * @return Section title for the item type {@link #TYPE_SECTION_HEADER}.
         */
        String getSectionTitle();
    }

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
     * Progress bar which can be shown while the data is loading in
     * the background.
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
                                    @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    /**
     * @return The layout used by this view. Override this to supply a
     *         different layout.
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
     *
     * @see #mRecyclerView
     */
    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);

        checkForStaggeredGridLayoutManager();
    }

    /**
     * Check for the staggered grid layout manager to avoid the jumping
     * of items by scrolling the recycler view to top.
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
     * @return Adapter used by the recycler view.
     *
     * @see #mRecyclerView
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    /**
     * Override this method to perform other operations on the
     * recycler view.
     *
     * @param recyclerView The recycler view inside this view.
     */
    protected void onCreateRecyclerView(@NonNull RecyclerView recyclerView) { }

    /**
     * Override this method to set swipe refresh layout listener
     * immediately after initializing the view.
     *
     * @return The on refresh listener for the swipe refresh layout.
     *
     * @see #setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)
     */
    protected SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return null;
    }

    /**
     * @return The recycler view to display the data.
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * @return The swipe refresh layout to provide pull to refresh
     *         functionality.
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * @return The swipe refresh layout to provide pull ro refresh
     *         functionality.
     */
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    /**
     * Set the swipe refresh layout listener to provide pull to
     * refresh functionality.
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
     * @return The progress bar which can be shown while the data
     *         is loading in the background.
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
}
