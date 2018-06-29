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

package com.pranavpandey.android.dynamic.support.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.splash.DynamicSplashFragment;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicFABUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicHintUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * Base activity to handle everything related to design support and
 * the app compat library. It has a fragment container to add fragments
 * dynamically with many other useful methods to provide a good looking
 * material design UI.
 *
 * <p>If {@link android.support.design.internal.NavigationMenu} is required
 * then, please check {@link DynamicDrawerActivity}.</p>
 */
public abstract class DynamicActivity extends DynamicStateActivity {

    /**
     * Constant to use the default layout resource..
     */
    protected static final int ADS_DEFAULT_LAYOUT_RES = -1;

    /**
     * App toolbar used by this activity.
     */
    protected Toolbar mToolbar;

    /**
     * Toolbar edit text used by this activity.
     */
    protected EditText mSearchViewEditText;

    /**
     * Root view of the toolbar search view.
     *
     * @see #mSearchViewEditText
     */
    protected ViewGroup mSearchViewRoot;

    /**
     * Button to clear the toolbar search view edit text.
     *
     * @see #mSearchViewEditText
     */
    protected ImageView mSearchViewClear;

    /**
     * Listener to listen search view expand and collapse callbacks.
     */
    protected DynamicSearchListener mDynamicSearchListener;

    /**
     * Title used by the app toolbar.
     */
    protected TextView mTitle;

    /**
     * Floating action button used by this activity. Use the methods
     * {@link #setFAB(Drawable, int, View.OnClickListener)} or
     * {@link #setFAB(int, int, View.OnClickListener)} to enable it.
     */
    protected FloatingActionButton mFAB;

    /**
     * Coordinator layout used by this activity.
     */
    protected CoordinatorLayout mCoordinatorLayout;

    /**
     * Collapsing toolbar layout used by this activity.
     */
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;

    /**
     * App bar layout used by this activity.
     */
    protected AppBarLayout mAppBarLayout;

    /**
     * Default menu reference to perform menu operations.
     */
    protected Menu mMenu;

    /**
     * Back drop frame for the {@link CollapsingToolbarLayout}.
     */
    protected ViewGroup mFrameBackDrop;

    /**
     * Header frame just below the app toolbar to add custom views
     * like tabs, hints, etc. Use the methods {@link #addHeader(int, boolean)}
     * or {@link #addHeader(View, boolean)} to add the views.
      */
    protected ViewGroup mFrameHeader;

    /**
     * Footer frame at the bottom of the screen to add custom views
     * like bottom navigation bar, ads, etc. Use the methods
     * {@link #addFooter(int, boolean)} or {@link #addFooter(View, boolean)}
     * to add the views.
     */
    protected ViewGroup mFrameFooter;

    /**
     * Frame layout to hold the content fragment. Use the methods
     * {@link #switchFragment(Fragment, boolean, String)} or
     * {@link #switchFragment(Fragment, boolean)} ot
     * {@link #switchFragment(FragmentTransaction, Fragment, boolean, String)}
     * to add or change the fragments.
     */
    protected FrameLayout mFrameContent;

    /**
     * Boolean to maintain the app bar state.
     */
    protected boolean mAppBarVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        mFrameContent = findViewById(R.id.ads_container);
        mFrameHeader = findViewById(R.id.ads_header_frame);
        mFrameFooter = findViewById(R.id.ads_footer_frame);
        mToolbar = findViewById(R.id.ads_toolbar);
        mSearchViewEditText = findViewById(R.id.ads_search_view_edit);
        mSearchViewRoot = findViewById(R.id.ads_search_view_root);
        mSearchViewClear = findViewById(R.id.ads_search_view_clear);
        mFAB = findViewById(R.id.ads_fab);

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);
        mAppBarLayout = findViewById(R.id.ads_app_bar_layout);

        mFrameContent.setBackgroundColor(DynamicTheme.getInstance().getBackgroundColor());
        mAppBarLayout.addOnOffsetChangedListener(mAppBarStateListener);

        if (getContentRes() != ADS_DEFAULT_LAYOUT_RES) {
            mFrameContent.addView(LayoutInflater.from(this).inflate(
                    getContentRes(), new LinearLayout(this), false));
        }

        if (setCollapsingToolbarLayout()) {
            mCollapsingToolbarLayout = findViewById(R.id.ads_collapsing_toolbar_layout);
            mFrameBackDrop = findViewById(R.id.ads_backdrop_frame);
            //mTitle = findViewById(R.id.ads_text_app_title);
        }

        setSupportActionBar(mToolbar);
        setStatusBarColor(getStatusBarColor());
        setSearchView();

        if (savedInstanceState != null) {
            mAppBarLayout.setExpanded(
                    savedInstanceState.getBoolean(ADS_STATE_APP_BAR_COLLAPSED));

            if (savedInstanceState.getInt(ADS_STATE_FAB_VISIBLE) != View.INVISIBLE) {
                DynamicFABUtils.show(mFAB);
            }

            if (savedInstanceState.getBoolean(ADS_STATE_SEARCH_VIEW_VISIBLE)) {
                restoreSearchViewState();
            }
        }
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        if (!isDrawerActivity()) {
            if (mCoordinatorLayout != null) {
                mCoordinatorLayout.setStatusBarBackgroundColor(color);
            }

            if (mCollapsingToolbarLayout != null) {
                mCollapsingToolbarLayout.setStatusBarScrimColor(color);
                mCollapsingToolbarLayout.setContentScrimColor(
                        DynamicTheme.getInstance().getPrimaryColor());
            } else {
                setWindowStatusBarColor(color);
            }
        }
    }

    /**
     * @return {@code true} if this activity is a drawer activity.
     */
    protected boolean isDrawerActivity() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(ADS_STATE_APP_BAR_COLLAPSED, isAppBarCollapsed());
        savedInstanceState.putInt(ADS_STATE_FAB_VISIBLE, mFAB.getVisibility());

        if (mSearchViewRoot != null) {
            savedInstanceState.putBoolean(ADS_STATE_SEARCH_VIEW_VISIBLE,
                    mSearchViewRoot.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setTitle(title);
            //mTitle.setText(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        setTitle(getText(titleId));
    }

    @Override
    protected @IdRes int getFragmentContainerId() {
        return R.id.ads_container;
    }

    /**
     * @return {@code true} to enable the collapsing toolbar layout.
     */
    protected boolean setCollapsingToolbarLayout() {
        return false;
    }

    /**
     * @return The custom layout resource id. Just return
     * {@link #ADS_DEFAULT_LAYOUT_RES} to use the default inbuilt layouts.
     */
    protected @LayoutRes int getLayoutRes() {
        return setCollapsingToolbarLayout()
                ? R.layout.ads_activity_collapsing : R.layout.ads_activity;
    }

    /**
     * @return The custom content resource id if no fragment is required.
     * It will automatically add this layout in the {@link #mFrameContent}.
     */
    protected abstract @LayoutRes int getContentRes();

    /**
     * Set the icon and on click listener for the back or up button in the
     * app bar.
     *
     * @param icon Drawable used for the back or up button.
     * @param onClickListener On click listener for the back or up button.
     */
    public void setNavigationClickListener(@Nullable Drawable icon,
                                           @Nullable View.OnClickListener onClickListener) {
        mToolbar.setNavigationIcon(icon);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(onClickListener != null);
        }

        mToolbar.setNavigationOnClickListener(onClickListener);
    }

    /**
     * Set the icon and on click listener for the back or up button in the
     * app bar.
     *
     * @param iconRes Drawable resource id used for the back or up button.
     * @param onClickListener On click listener for the back or up button.
     */
    public void setNavigationClickListener(@DrawableRes int iconRes,
                                           @Nullable View.OnClickListener onClickListener) {
        setNavigationClickListener(DynamicResourceUtils.getDrawable(
                this, iconRes), onClickListener);
    }

    /**
     * Sets the on click listener for the back or up button in the app bar.
     * If no listener is supplied then, it will automatically hide the button.
     *
     * @param onClickListener On click listener for the back or up button.
     */
    public void setNavigationClickListener(@Nullable View.OnClickListener onClickListener) {
        setNavigationClickListener(onClickListener != null
                ? DynamicResourceUtils.getDrawable(this, R.drawable.ads_ic_back)
                : null, onClickListener);
    }

    /**
     * @return The app toolbar used by this activity.
     */
    public @Nullable Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * @return {@code true} if the app bar is visible. It will be used
     * internally to maintain the app bar state.
     */
    public boolean isAppBarVisible() {
        return mAppBarVisible;
    }

    /**
     * @return The coordinator layout used by this activity.
     */
    public @Nullable CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    /**
     * @return The collapsing toolbar layout used by this activity.
     */
    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return mCollapsingToolbarLayout;
    }

    /**
     * Set layout scroll flags for the {@link #mCollapsingToolbarLayout}.
     * Useful to change the collapse mode dynamically.
     *
     * @param flags Scroll flags for the collapsing toolbar layout.
     */
    public void setCollapsingToolbarLayoutFlags(
            @AppBarLayout.LayoutParams.ScrollFlags int flags) {
        if (mCollapsingToolbarLayout != null) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                    mCollapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(flags);
            mCollapsingToolbarLayout.setLayoutParams(params);
        }
    }

    /**
     * Set layout scroll flags for the {@link #mToolbar}. Useful to
     * change the collapse mode dynamically.
     *
     * @param flags Scroll flags for the collapsing toolbar layout.
     */
    public void setToolbarLayoutFlags(
            @AppBarLayout.LayoutParams.ScrollFlags int flags) {
        if (mToolbar != null) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                    mToolbar.getLayoutParams();
            params.setScrollFlags(flags);
            mToolbar.setLayoutParams(params);
        }
    }

    /**
     * Set the {@link #mToolbar} or {@link #mCollapsingToolbarLayout}
     * visibility (collectively known as app bar) if available.
     *
     * @param appBarVisible {@code true} to make the app bar visible.
     */
    public void setAppBarVisible(boolean appBarVisible) {
        mAppBarVisible = appBarVisible;
        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setVisibility(
                    appBarVisible ? View.VISIBLE : View.GONE);
        } else {
            mToolbar.setVisibility(appBarVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Add a backdrop view for the {@link #mCollapsingToolbarLayout} which
     * will be shown when it is expanded and will be hidden on collapsing
     * the toolbar.
     *
     * @param view View to be added as the backdrop frame.
     * @param expandedTitleColor Title color when the toolbar is expanded.
     */
    public void setAppBarBackDrop(@Nullable View view, @ColorInt int expandedTitleColor) {
        if (mCollapsingToolbarLayout != null) {
            if (mFrameBackDrop.getChildCount() > 0) {
                mFrameBackDrop.removeAllViews();
            }

            if (view != null) {
                mFrameBackDrop.addView(view);
                setAppBarTransparent(true);
                mCollapsingToolbarLayout
                        .setExpandedTitleColor(expandedTitleColor);
                mCollapsingToolbarLayout
                        .setCollapsedTitleTextColor(expandedTitleColor);
                //mTitle.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Add a backdrop view for the {@link #mCollapsingToolbarLayout} which
     * will be shown when it is expanded and will be hidden on collapsing
     * the toolbar.
     *
     * @param layoutId Layout resource id to be added as the backdrop frame.
     * @param expandedTitleColorRes Title color resource id when the toolbar
     *                              is expanded.
     */
    public void setAppBarBackDropRes(@LayoutRes int layoutId,
                                     @ColorRes int expandedTitleColorRes) {
        setAppBarBackDrop(LayoutInflater.from(this).inflate(layoutId,
                new LinearLayout(this), false),
                ContextCompat.getColor(this, expandedTitleColorRes));
    }

    /**
     * Set the drawable for backdrop image used by the
     * {@link #mCollapsingToolbarLayout} which will be shown when it is
     * expanded and will be hidden on collapsing the toolbar.
     *
     * @param drawable Drawable for the backdrop image.
     */
    public void setAppBarBackDrop(@Nullable Drawable drawable) {
        View view = LayoutInflater.from(this).inflate(R.layout.ads_appbar_backdrop_image,
                new LinearLayout(this), false);
        ((ImageView) view.findViewById(R.id.ads_image_backdrop))
                .setImageDrawable(drawable);

        setAppBarBackDrop(view, DynamicTheme.getInstance().getTintPrimaryColor());
    }

    /**
     * Set the drawable id for backdrop image used by the
     * {@link #mCollapsingToolbarLayout} hich will be shown when it is expanded
     * and will be hidden on collapsing the toolbar.
     *
     * @param drawableRes Drawable resource id for the backdrop image.
     */
    public void setAppBarBackDrop(@DrawableRes int drawableRes) {
        setAppBarBackDrop(DynamicResourceUtils.getDrawable(this, drawableRes));
    }

    /**
     * Make the app bar transparent or translucent. Useful to make it
     * transparent if a backdrop view has been added.
     *
     * @param transparent {@code true} to make the app bar transparent.
     */
    public void setAppBarTransparent(boolean transparent) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(transparent ? Color.TRANSPARENT
                            : DynamicTheme.getInstance().getPrimaryColor()));
        }
    }

    /**
     * Set the visibility of app bar shadow.
     *
     * @param visible {@code true} to show the content shadow below the app bar.
     */
    public void showAppBarShadow(boolean visible) {
        findViewById(R.id.ads_app_bar_shadow).setVisibility(
                visible ? View.VISIBLE : View.GONE);
    }

    /**
     * @return The header frame just below the app toolbar to add custom views
     *         like tabs, hints, etc. Use the methods {@link #addHeader(int, boolean)}
     *         or {@link #addHeader(View, boolean)} to add the views.
     */
    public @Nullable ViewGroup getFrameHeader() {
        return mFrameHeader;
    }

    /**
     * Add header view just below the app bar. Useful to add tabs or hints
     * dynamically. Multiple views can be added and the default background
     * will be the app bar background (theme primary color). Please check
     * {@link DynamicSplashFragment}
     * to add the tabs automatically.
     *
     * @param view View to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addHeader(@Nullable View view, boolean removePrevious) {
        if (removePrevious && mFrameHeader.getChildCount() > 0) {
            mFrameHeader.removeAllViews();
        }

        if (view != null) {
            mFrameHeader.addView(view);
        }
    }

    /**
     * Add header view just below the app bar. Useful to add tabs or hints
     * dynamically. Multiple views can be added and the default background
     * will be the app bar background (theme primary color). Please check
     * {@link DynamicSplashFragment}
     * to add the tabs automatically.
     *
     * @param layoutId Layout resource id to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addHeader(@LayoutRes int layoutId, boolean removePrevious) {
        addHeader(LayoutInflater.from(this).inflate(layoutId,
                new LinearLayout(this), false), removePrevious);
    }

    /**
     * @return The footer frame at the bottom of the screen to add custom views
     *         like bottom navigation bar, ads, etc. Use the methods
     *         {@link #addFooter(int, boolean)} or {@link #addFooter(View, boolean)}
     *         to add the views.
     */
    public @Nullable ViewGroup getFrameFooter() {
        return mFrameFooter;
    }

    /**
     * Add footer view at the bottom of the screen. Useful to add bottom navigation
     * bar, ads, etc. dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please
     * check {@link DynamicSplashFragment}
     * to add the tabs automatically.
     *
     * @param view View to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addFooter(@Nullable View view, boolean removePrevious) {
        if (removePrevious && mFrameHeader.getChildCount() > 0) {
            mFrameFooter.removeAllViews();
        }

        if (view != null) {
            mFrameFooter.addView(view);
        }
    }

    /**
     * Add footer view at the bottom of the screen. Useful to add bottom navigation
     * bar, ads, etc. dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please
     * check {@link DynamicSplashFragment}
     * to add the tabs automatically.
     *
     * @param layoutId Layout resource id to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addFooter(@LayoutRes int layoutId, boolean removePrevious) {
        addFooter(LayoutInflater.from(this).inflate(layoutId,
                new LinearLayout(this), false), removePrevious);
    }

    /**
     * Set the visibility of app bar menu item by supplying its id.
     *
     * @param id Menu item id to set its visibility.
     * @param visible {@code true} to make the menu item visible.
     */
    public void setMenuItemVisible(final int id, final boolean visible) {
        (new Handler()).post(new Runnable() {
            @Override
            public void run() {
                if (mMenu != null) {
                    mMenu.findItem(id).setVisible(visible);
                }
            }
        });
    }

    /**
     * Setup search view edit text and clear button listeners.
     */
    protected void setSearchView() {
        if (mSearchViewClear != null) {
            mSearchViewClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSearchViewEditText.setText("");
                }
            });
        }

        if (mSearchViewEditText != null) {
            mSearchViewEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence,
                                              int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    setSearchViewClearButton();
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });
        }

        setSearchViewClearButton();
    }

    /**
     * Set search view clear button state.
     */
    private void setSearchViewClearButton() {
        if (mSearchViewEditText != null) {
            if (mSearchViewEditText.getText() != null
                    && mSearchViewEditText.getText().length() != 0) {
                mSearchViewClear.setVisibility(View.VISIBLE);
            } else {
                mSearchViewClear.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Restore the search view state after the configuration
     * change.
     */
    public void restoreSearchViewState() {
        mSearchViewEditText.post(new Runnable() {
            @Override
            public void run() {
                expandSearchView(false);
                mSearchViewEditText.setText(mSearchViewEditText.getText());

                if (mSearchViewEditText.getText() != null) {
                    mSearchViewEditText.setSelection(mSearchViewEditText.getText().length());
                }
            }
        });
    }

    /**
     * Expand search view to start searching.
     *
     * @param requestSoftInput {@code true} to request the soft input
     *                         keyboard.
     */
    public void expandSearchView(boolean requestSoftInput) {
        if (mSearchViewRoot != null && mSearchViewRoot.getVisibility() == View.GONE) {
            mSearchViewRoot.setVisibility(View.VISIBLE);

            if (mDynamicSearchListener != null) {
                mDynamicSearchListener.onSearchViewExpanded();
            }

            if (requestSoftInput) {
                DynamicInputUtils.showSoftInput(mSearchViewEditText);
            }
        }
    }

    /**
     * Collapse search view to stop searching.
     */
    public void collapseSearchView() {
        if (mSearchViewRoot != null && mSearchViewRoot.getVisibility() == View.VISIBLE) {
            mSearchViewEditText.setText("");
            DynamicInputUtils.hideSoftInput(mSearchViewEditText);
            mSearchViewRoot.setVisibility(View.GONE);

            if (mDynamicSearchListener != null) {
                mDynamicSearchListener.onSearchViewCollapsed();
            }
        }
    }

    /**
     * @return The toolbar edit text used by this activity.
     */
    public @Nullable EditText getSearchViewEditText() {
        return mSearchViewEditText;
    }

    /**
     * @return {@code true} if search view is expanded.
     */
    public boolean isSearchViewExpanded() {
        return mSearchViewRoot != null && mSearchViewRoot.getVisibility() == View.VISIBLE;
    }

    /**
     * @return The listener to listen search view expand and collapse
     *         callbacks.
     */
    public @Nullable DynamicSearchListener getSearchViewListener() {
        return mDynamicSearchListener;
    }

    /**
     * Sets the listener to listen search view expand and collapse
     * callbacks.
     *
     * @param dynamicSearchListener The listener to be set.
     */
    public void setSearchViewListener(@Nullable DynamicSearchListener dynamicSearchListener) {
        this.mDynamicSearchListener = dynamicSearchListener;
    }

    @Override
    public void onBackPressed() {
        if (isSearchViewExpanded()) {
            collapseSearchView();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @return The floating action button used by this activity.
     */
    public @Nullable FloatingActionButton getFAB() {
        return mFAB;
    }

    /**
     * Set a floating action button ({@link #mFAB}) used by this activity
     * by supplying an image drawable, current visibility and a click
     * listener. FAB will be tinted automatically according to the accent
     * color used by this activity.
     *
     * <p>Please use {@link #getFAB()} method to perform more operations
     * dynamically.</p>
     *
     * @param drawable Image drawable for the floating action button.
     * @param visibility Current FAB visibility. {@link View#VISIBLE},
     *                   {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is
     *                        clicked.
     */
    public void setFAB(@Nullable Drawable drawable, int visibility,
                       @Nullable View.OnClickListener onClickListener) {
        if (mFAB != null) {
            setFABImageDrawable(drawable);
            mFAB.setOnClickListener(onClickListener);
            setFABVisibility(visibility);
        }
    }

    /**
     * Set a floating action button ({@link #mFAB}) used by this activity
     * by supplying an image drawable resource, current visibility and a
     * click listener. FAB will be tinted automatically according to the
     * accent color used by this activity.
     *
     * <p>Please use {@link #getFAB()} method to perform more operations
     * dynamically.</p>
     *
     * @param drawableRes Image drawable resource for the floating action
     *                    button.
     * @param visibility Current FAB visibility. {@link View#VISIBLE},
     *                   {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is
     *                        clicked.
     */
    public void setFAB(@DrawableRes int drawableRes, int visibility,
                       @Nullable View.OnClickListener onClickListener) {
        setFAB(DynamicResourceUtils.getDrawable(
                this, drawableRes), visibility, onClickListener);
    }

    /**
     * Set the {@link #mFAB} image drawable. Image will be tinted automatically
     * according to its background color to provide best visibility.
     *
     * @param drawable Image drawable for the floating action button.
     */
    public void setFABImageDrawable(@Nullable Drawable drawable) {
        if (mFAB != null) {
            if (drawable != null) {
                mFAB.setImageDrawable(drawable);
            }
        }
    }

    /**
     * Set the {@link #mFAB} image drawable resource. Image will be tinted
     * automatically according to its background color to provide best
     * visibility.
     *
     * @param drawableRes Image drawable resource for the floating action
     *                    button.
     */
    public void setFABImageDrawable(@DrawableRes int drawableRes) {
        setFABImageDrawable(DynamicResourceUtils.getDrawable(this, drawableRes));
    }

    /**
     * Set the {@link #mFAB} visibility to {@link View#VISIBLE},
     * {@link View#INVISIBLE} or {@link View#GONE}
     */
    public void setFABVisibility(int visibility) {
        if (mFAB != null && visibility != ADS_VISIBILITY_FAB_NO_CHANGE) {
            mFAB.setVisibility(visibility);
        }
    }

    /**
     * Show the {@link #mFAB} by setting its visibility to
     * {@link View#VISIBLE}.
     */
    public void showFAB() {
        setFABVisibility(View.VISIBLE);
    }

    /**
     * Hide the {@link #mFAB} by setting its visibility to
     * {@link View#GONE}.
     */
    public void hideFAB() {
        setFABVisibility(View.INVISIBLE);
    }

    /**
     * Remove the {@link #mFAB} associated with this activity. Please call
     * the methods {@link #setFAB(int, int, View.OnClickListener)} or
     * {@link #setFAB(Drawable, int, View.OnClickListener)} to set it again.
     */
    public void removeFAB() {
        if (mFAB != null) {
            setFABImageDrawable(null);
            mFAB.setOnClickListener(null);
            hideFAB();
        }
    }

    /**
     * Make a themed snack bar with text and action. Background will be
     * the current navigation bar color to blend it smoothly and it will
     * automatically use its tint color for the text and action to provide
     * best visibility.
     *
     * @param text The text to show. Can be formatted text.
     * @param duration The duration of the snack bar. Can be
     *                 {@link Snackbar#LENGTH_SHORT},
     *                 {@link Snackbar#LENGTH_LONG} or
     *                 {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return Snack bar with the supplied parameters.
     *         Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@NonNull CharSequence text,
                                @BaseTransientBottomBar.Duration int duration) {
        return DynamicHintUtils.getSnackBar(mCoordinatorLayout, text,
                getAppliedNavigationBarColor(), DynamicColorUtils.getTintColor(
                        getAppliedNavigationBarColor()), duration);
    }


    /**
     * Make a themed snack bar with text and action. Background will be
     * the current navigation bar color to blend it smoothly and it will
     * automatically use its tint color for the text and action to provide
     * best visibility.
     *
     * @param stringId The string resource id.
     * @param duration The duration of the snack bar. Can be
     *                 {@link Snackbar#LENGTH_SHORT},
     *                 {@link Snackbar#LENGTH_LONG} or
     *                 {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return Snack bar with the supplied parameters.
     *         Use {@link Snackbar#show()} to display the snack bar.
     */
    public @Nullable Snackbar getSnackBar(@StringRes int stringId,
                                @BaseTransientBottomBar.Duration  int duration) {
        return getSnackBar(getString(stringId), duration);
    }

    /**
     * Make a themed snack bar with text and action. Background will be
     * the current navigation bar color to blend it smoothly and it will
     * automatically use its tint color for the text and action to provide
     * best visibility.
     *
     * @param text The text to show. Can be formatted text.
     *
     * @return Snack bar with the supplied parameters.
     *         Use {@link Snackbar#show()} to display the snack bar.
     */
    @SuppressLint("Range")
    public @NonNull Snackbar getSnackBar(@NonNull CharSequence text) {
        return getSnackBar(text, Snackbar.LENGTH_LONG);
    }

    /**
     * Make a themed snack bar with text and action. Background will be
     * the current navigation bar color to blend it smoothly and it will
     * automatically use its tint color for the text and action to provide
     * best visibility.
     *
     * @param stringId The string resource id.
     *
     * @return Snack bar with the supplied parameters.
     *         Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@StringRes int stringId) {
        return getSnackBar(getString(stringId));
    }
}
