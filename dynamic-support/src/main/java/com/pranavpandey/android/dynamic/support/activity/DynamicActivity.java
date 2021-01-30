/*
 * Copyright 2018-2020 Pranav Pandey
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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicFABUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicHintUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomSheet;
import com.pranavpandey.android.dynamic.support.widget.DynamicExtendedFloatingActionButton;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * Base activity to handle everything related to design support and the app compat library.
 * It has a fragment container to add fragments dynamically with many other useful methods to
 * provide a good looking material design UI.
 *
 * <p>If {@link com.google.android.material.internal.NavigationMenu} is required then,
 * please check {@link DynamicDrawerActivity}.
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
     */
    protected ViewGroup mSearchViewRoot;

    /**
     * Button to clear the toolbar search view edit text.
     */
    protected ImageView mSearchViewClear;

    /**
     * Listener to listen search view expand and collapse callbacks.
     */
    protected DynamicSearchListener mDynamicSearchListener;

    /**
     * Floating action button used by this activity.
     * <p>Use the methods {@link #setFAB(Drawable, int, View.OnClickListener)} or
     * {@link #setFAB(int, int, View.OnClickListener)} to enable it.
     */
    protected FloatingActionButton mFAB;

    /**
     * Extended floating action button used by this activity.
     * <p>Use the methods {@link #setExtendedFAB(Drawable, String, int, View.OnClickListener)} or
     * {@link #setExtendedFAB(int, int, int, View.OnClickListener)} to enable it.
     */
    protected ExtendedFloatingActionButton mExtendedFAB;

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
     * Bottom bar shadow layout used by this activity.
     */
    protected View mBottomBarShadow;

    /**
     * Default menu reference to perform menu operations.
     */
    protected Menu mMenu;

    /**
     * Back drop frame for the collapsing toolbar layout.
     */
    protected ViewGroup mFrameBackDrop;

    /**
     * Header frame just below the app toolbar to add custom views like tabs, hints, etc.
     * <p>Use the methods {@link #addHeader(int, boolean)} or {@link #addHeader(View, boolean)}
     * to add the views.
     */
    protected ViewGroup mFrameHeader;

    /**
     * Frame layout to hold the content fragment.
     * <p>Use the methods {@link #switchFragment(Fragment, boolean, String)} or
     * {@link #switchFragment(Fragment, boolean)} or
     * {@link #switchFragment(FragmentTransaction, Fragment, boolean, String)} to add or
     * change the fragments.
     */
    protected FrameLayout mFrameContent;

    /**
     * Frame layout for the bottom sheet.
     * <p>Use the methods {@link #addBottomSheet(int, boolean)}
     * or {@link #addBottomSheet(View, boolean)} to add the views.
     */
    protected DynamicBottomSheet mBottomSheet;

    /**
     * Footer frame at the bottom of the screen to add custom views like
     * bottom navigation bar, ads, etc.
     * <p>Use the methods {@link #addFooter(int, boolean)} or {@link #addFooter(View, boolean)}
     * to add the views.
     */
    protected ViewGroup mFrameFooter;

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
        mBottomSheet = findViewById(R.id.ads_bottom_sheet);
        mFrameFooter = findViewById(R.id.ads_footer_frame);
        mToolbar = findViewById(R.id.ads_toolbar);
        mSearchViewEditText = findViewById(R.id.ads_search_view_edit);
        mSearchViewRoot = findViewById(R.id.ads_search_view_root);
        mSearchViewClear = findViewById(R.id.ads_search_view_clear);
        mFAB = findViewById(R.id.ads_fab);
        mExtendedFAB = findViewById(R.id.ads_fab_extended);

        mCoordinatorLayout = findViewById(R.id.ads_coordinator_layout);
        mAppBarLayout = findViewById(R.id.ads_app_bar_layout);
        mBottomBarShadow = findViewById(R.id.ads_bottom_bar_shadow);

        mFrameContent.setBackgroundColor(DynamicTheme.getInstance().get().getBackgroundColor());

        if (mAppBarLayout != null) {
            mAppBarLayout.addOnOffsetChangedListener(mAppBarStateListener);
        }

        if (getContentRes() != ADS_DEFAULT_LAYOUT_RES) {
            mFrameContent.addView(getLayoutInflater().inflate(getContentRes(),
                    new LinearLayout(this), false));
        }

        if (setCollapsingToolbarLayout()) {
            mCollapsingToolbarLayout = findViewById(R.id.ads_collapsing_toolbar_layout);
            mFrameBackDrop = findViewById(R.id.ads_backdrop_frame);
        }

        setSupportActionBar(mToolbar);
        setStatusBarColor(getStatusBarColor());
        setNavigationBarColor(getNavigationBarColor());
        setSearchView();

        if (savedInstanceState != null) {
            if (mAppBarLayout != null) {
                mAppBarLayout.setExpanded(savedInstanceState
                        .getBoolean(ADS_STATE_APP_BAR_COLLAPSED));
            }

            if (mFAB != null && savedInstanceState.getInt(
                    ADS_STATE_FAB_VISIBLE) != View.INVISIBLE) {
                DynamicFABUtils.show(mFAB);
            }

            if (mExtendedFAB != null && savedInstanceState.getInt(
                    ADS_STATE_EXTENDED_FAB_VISIBLE) != View.INVISIBLE) {
                DynamicFABUtils.show(mExtendedFAB, false);
            }

            if (savedInstanceState.getBoolean(ADS_STATE_SEARCH_VIEW_VISIBLE)) {
                restoreSearchViewState();
            }
        }

        DynamicViewUtils.applyWindowInsetsMarginHorizontalBottom(mFAB);
        DynamicViewUtils.applyWindowInsetsMarginHorizontalBottom(mExtendedFAB);
        DynamicViewUtils.applyWindowInsetsBottom(mFrameFooter, true);

        if (mBottomSheet != null) {
            mBottomSheet.applyWindowInsets();
        }

        setFrameVisibility(mBottomSheet);
        setFrameVisibility(mFrameFooter);
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        if (!isDrawerActivity()) {
            setWindowStatusBarColor(getStatusBarColor());
        }

        if (mCoordinatorLayout != null) {
            mCoordinatorLayout.setStatusBarBackgroundColor(getStatusBarColor());
        }

        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setStatusBarScrimColor(getStatusBarColor());
            mCollapsingToolbarLayout.setContentScrimColor(
                    DynamicTheme.getInstance().get().getPrimaryColor());
        }
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return mCoordinatorLayout;
    }

    @Override
    public boolean isApplyEdgeToEdgeInsets() {
        return false;
    }

    /**
     * Checks whether the navigation drawer is added.
     *
     * @return {@code true} if this activity is a drawer activity.
     */
    protected boolean isDrawerActivity() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ADS_STATE_APP_BAR_COLLAPSED, isAppBarCollapsed());

        if (mFAB != null) {
            outState.putInt(ADS_STATE_FAB_VISIBLE, mFAB.getVisibility());
        }

        if ((mExtendedFAB != null)) {
            outState.putInt(ADS_STATE_EXTENDED_FAB_VISIBLE, mExtendedFAB.getVisibility());
            if (mExtendedFAB instanceof DynamicExtendedFloatingActionButton) {
                outState.putBoolean(ADS_STATE_EXTENDED_FAB_STATE,
                        ((DynamicExtendedFloatingActionButton) mExtendedFAB).isFABExtended());
            }
        }

        if (mSearchViewRoot != null) {
            outState.putBoolean(ADS_STATE_SEARCH_VIEW_VISIBLE,
                    mSearchViewRoot.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActionModeStarted(@NonNull ActionMode mode) {
        super.onActionModeStarted(mode);

        if (mode.getCustomView() != null) {
            DynamicDrawableUtils.setBackground(mode.getCustomView(),
                    DynamicDrawableUtils.colorizeDrawable(mode.getCustomView().getBackground(),
                            DynamicTheme.getInstance().get().getBackgroundColor()));
        }
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setTitle(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleRes) {
        setTitle(getText(titleRes));
    }

    /**
     * Set subtitle for the support action bar.
     *
     * @param subtitle The subtitle to be set.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    /**
     * Set subtitle resource for the support action bar.
     *
     * @param subtitleRes The subtitle resource to be set.
     */
    public void setSubtitle(@StringRes int subtitleRes) {
        setSubtitle(getText(subtitleRes));
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
     * Get the custom layout resource for this activity.
     * <p>Return {@link #ADS_DEFAULT_LAYOUT_RES} to use the default inbuilt layouts.
     *
     * @return The custom layout resource.
     */
    protected @LayoutRes int getLayoutRes() {
        return setCollapsingToolbarLayout()
                ? R.layout.ads_activity_collapsing : R.layout.ads_activity;
    }

    /**
     * Returns the custom content resource if no fragment is required.
     * <p>It will automatically add this layout in the {@link #mFrameContent}.
     *
     * @return The custom content resource if no fragment is required.
     */
    protected int getContentRes() {
        return ADS_DEFAULT_LAYOUT_RES;
    }

    /**
     * Returns the parent content view used by this activity.
     *
     * @return The parent content view used by this activity.
     */
    public  @NonNull View getContentView() {
        return mFrameContent != null ? mFrameContent.getRootView() : getWindow().getDecorView();
    }

    /**
     * Set the icon and on click listener for the back or up button in the app bar.
     *
     * @param icon The drawable used for the back or up button.
     * @param onClickListener The click listener for the back or up button.
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
     * Set the icon and on click listener for the back or up button in the app bar.
     *
     * @param iconRes The drawable resource used for the back or up button.
     * @param onClickListener The click listener for the back or up button.
     */
    public void setNavigationClickListener(@DrawableRes int iconRes,
            @Nullable View.OnClickListener onClickListener) {
        setNavigationClickListener(DynamicResourceUtils.getDrawable(
                this, iconRes), onClickListener);
    }

    /**
     * Sets the on click listener for the back or up button in the app bar.
     * <p>If no listener is supplied then, it will automatically hide the button.
     *
     * @param onClickListener The click listener for the back or up button.
     */
    public void setNavigationClickListener(@Nullable View.OnClickListener onClickListener) {
        setNavigationClickListener(onClickListener != null
                ? DynamicResourceUtils.getDrawable(this, R.drawable.ads_ic_back)
                : null, onClickListener);
    }

    /**
     * Returns the app toolbar used by this activity.
     *
     * @return The app toolbar used by this activity.
     */
    public @Nullable Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Returns whether the app bar is visible.
     *
     * @return {@code true} if the app bar is visible. It will be used internally to maintain
     *         the app bar state.
     */
    public boolean isAppBarVisible() {
        return mAppBarVisible;
    }

    /**
     * Returns the coordinator layout used by this activity.
     *
     * @return The coordinator layout used by this activity.
     */
    public @Nullable CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    /**
     * Returns the collapsing toolbar layout used by this activity.
     *
     * @return The collapsing toolbar layout used by this activity.
     */
    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return mCollapsingToolbarLayout;
    }

    /**
     * Set layout scroll flags for the collapsing toolbar layout.
     * <p>Useful to change the collapse mode dynamically.
     *
     * @param flags The scroll flags for the collapsing toolbar layout.
     */
    public void setCollapsingToolbarLayoutFlags(@AppBarLayout.LayoutParams.ScrollFlags int flags) {
        if (mCollapsingToolbarLayout != null) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                    mCollapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(flags);
            mCollapsingToolbarLayout.setLayoutParams(params);
        }
    }

    /**
     * Set layout scroll flags for the toolbar.
     * <p>Useful to change the collapse mode dynamically.
     *
     * @param flags The scroll flags for the collapsing toolbar layout.
     */
    public void setToolbarLayoutFlags(@AppBarLayout.LayoutParams.ScrollFlags int flags) {
        if (mToolbar != null) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                    mToolbar.getLayoutParams();
            params.setScrollFlags(flags);
            mToolbar.setLayoutParams(params);
        }
    }

    /**
     * Set the toolbar or collapsing toolbar layout visibility (collectively known as app bar)
     * if available.
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
     * Add a backdrop view for the collapsing toolbar layout which will be shown when it is
     * expanded and will be hidden on collapsing the toolbar.
     *
     * @param view The view to be added as the backdrop frame.
     * @param expandedTitleColor The title color when the toolbar is expanded.
     */
    public void setAppBarBackDrop(@Nullable View view, @ColorInt int expandedTitleColor) {
        if (DynamicTheme.getInstance().get().isBackgroundAware()) {
            expandedTitleColor = DynamicColorUtils.getContrastColor(
                    expandedTitleColor, DynamicTheme.getInstance().get().getPrimaryColor());
        }

        if (mCollapsingToolbarLayout != null) {
            if (mFrameBackDrop.getChildCount() > 0) {
                mFrameBackDrop.removeAllViews();
            }

            if (view != null) {
                mFrameBackDrop.addView(view);
                setAppBarTransparent(true);
                mCollapsingToolbarLayout.setExpandedTitleColor(expandedTitleColor);
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(expandedTitleColor);
            }
        }
    }

    /**
     * Add a backdrop view for the collapsing toolbar layout which will be shown when it is
     * expanded and will be hidden on collapsing the toolbar.
     *
     * @param layoutRes The layout resource to be added as the backdrop frame.
     * @param expandedTitleColorRes The title color resource when the toolbar is expanded.
     */
    public void setAppBarBackDropRes(@LayoutRes int layoutRes,
            @ColorRes int expandedTitleColorRes) {
        setAppBarBackDrop(getLayoutInflater().inflate(layoutRes,
                new LinearLayout(this), false),
                ContextCompat.getColor(this, expandedTitleColorRes));
    }

    /**
     * Set the drawable for backdrop image used by the collapsing toolbar layout which will be
     * shown when it is expanded and will be hidden on collapsing the toolbar.
     *
     * @param drawable The drawable for the backdrop image.
     */
    public void setAppBarBackDrop(@Nullable Drawable drawable) {
        View view = getLayoutInflater().inflate(R.layout.ads_appbar_backdrop_image,
                new LinearLayout(this), false);
        ((ImageView) view.findViewById(R.id.ads_image_backdrop))
                .setImageDrawable(drawable);

        setAppBarBackDrop(view, DynamicTheme.getInstance().get().getTintPrimaryColor());
    }

    /**
     * Set the drawable id for backdrop image used by the collapsing toolbar layout which will be
     * shown when it is expanded and will be hidden on collapsing the toolbar.
     *
     * @param drawableRes The drawable resource for the backdrop image.
     */
    public void setAppBarBackDrop(@DrawableRes int drawableRes) {
        setAppBarBackDrop(DynamicResourceUtils.getDrawable(this, drawableRes));
    }

    /**
     * Make the app bar transparent or translucent.
     * <p>Useful to make it transparent if a backdrop view has been added.
     *
     * @param transparent {@code true} to make the app bar transparent.
     */
    public void setAppBarTransparent(boolean transparent) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(transparent ? Color.TRANSPARENT
                            : DynamicTheme.getInstance().get().getPrimaryColor()));
        }
    }

    /**
     * Set the visibility of app bar shadow.
     *
     * @param visible {@code true} to show the content shadow below the app bar.
     */
    public void setAppBarShadowVisible(boolean visible) {
        findViewById(R.id.ads_app_bar_shadow).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Add a view to the view group.
     *
     * @param viewGroup The view group to add the view.
     * @param view The view to be added.
     * @param removePrevious {@code true} to remove all the previous views of the view group.
     */
    public void addView(@Nullable ViewGroup viewGroup,
            @Nullable View view, boolean removePrevious) {
        DynamicViewUtils.addView(viewGroup, view, removePrevious);
        setFrameVisibility(viewGroup);
    }

    /**
     * Get the header frame just below the app toolbar to add custom views like tabs, hints, etc.
     * <p>Use the methods {@link #addHeader(int, boolean)} or {@link #addHeader(View, boolean)}
     * to add the views.
     *
     * @return The header frame just below the app toolbar to add custom views like tabs,
     *         hints, etc.
     */
    public @Nullable ViewGroup getFrameHeader() {
        return mFrameHeader;
    }

    /**
     * Add header view just below the app bar.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param view The view to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addHeader(@Nullable View view, boolean removePrevious) {
        addView(mFrameHeader, view, removePrevious);
    }

    /**
     * Add header view just below the app bar.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addHeader(@LayoutRes int layoutRes, boolean removePrevious) {
        addHeader(getLayoutInflater().inflate(layoutRes,
                new LinearLayout(this), false), removePrevious);
    }

    /**
     * Set the visibility of bottom bar shadow.
     *
     * @param visible {@code true} to show the content shadow above the bottom bar.
     */
    public void setBottomBarShadowVisible(boolean visible) {
        mBottomBarShadow.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Get the bottom sheet frame layout.
     * <p>Use the methods {@link #addBottomSheet(int, boolean)}
     * or {@link #addBottomSheet(View, boolean)} to add the views.
     *
     * @return The bottom sheet frame layout.
     */
    public @Nullable DynamicBottomSheet getBottomSheet() {
        return mBottomSheet;
    }

    /**
     * Add view in the bottom sheet frame layout.
     *
     * @param view The view to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addBottomSheet(@Nullable View view, boolean removePrevious) {
        addView(mBottomSheet, view, removePrevious);
    }

    /**
     * Returns the current bottom sheet behavior.
     *
     * @return The bottom sheet behavior.
     */
    public BottomSheetBehavior getBottomSheetBehavior() {
        return BottomSheetBehavior.from(mBottomSheet);
    }

    /**
     * Sets the bottom sheet state if present.
     *
     * @param bottomSheetState The state to be set.
     */
    public void setBottomSheetState(@BottomSheetBehavior.State int bottomSheetState) {
        getBottomSheetBehavior().setState(bottomSheetState);
    }

    /**
     * Add view in the bottom sheet frame layout.
     *
     * @param layoutRes The layout resource to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addBottomSheet(@LayoutRes int layoutRes, boolean removePrevious) {
        addBottomSheet(getLayoutInflater().inflate(layoutRes,
                new LinearLayout(this), false), removePrevious);
    }

    /**
     * Get the footer frame at the bottom of the screen to add custom views like
     * bottom navigation bar, ads, etc.
     * <p>Use the methods {@link #addFooter(int, boolean)} or {@link #addFooter(View, boolean)}
     * to add the views.
     *
     * @return The footer frame at the bottom of the screen to add custom views like
     *         bottom navigation bar, ads, etc.
     */
    public @Nullable ViewGroup getFrameFooter() {
        return mFrameFooter;
    }

    /**
     * Add footer view at the bottom of the screen.
     * <p>Useful to add bottom navigation bar, ads, etc. dynamically. Multiple views can be
     * added and the default background will be the app bar background (theme primary color).
     *
     * @param view The view to be added in the footer frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addFooter(@Nullable View view, boolean removePrevious) {
        addView(mFrameFooter, view, removePrevious);
    }

    /**
     * Add footer view at the bottom of the screen.
     * <p>Useful to add bottom navigation bar, ads, etc. dynamically. Multiple views can be
     * added and the default background will be the app bar background (theme primary color).
     *
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public void addFooter(@LayoutRes int layoutRes, boolean removePrevious) {
        addFooter(getLayoutInflater().inflate(layoutRes,
                new LinearLayout(this), false), removePrevious);
    }

    /**
     * Set the visibility of app bar menu item by supplying its id.
     *
     * @param id Menu item id to set its visibility.
     * @param visible {@code true} to make the menu item visible.
     */
    public void setMenuItemVisible(final int id, final boolean visible) {
        getContentView().post(new Runnable() {
            @Override
            public void run() {
                if (mMenu != null && mMenu.findItem(id) != null) {
                    mMenu.findItem(id).setVisible(visible);
                }
            }
        });
    }

    /**
     * Set view group visibility according to the child views.
     *
     * @param viewGroup The view group to set the visibility.
     */
    private void setFrameVisibility(@Nullable ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }

        viewGroup.setVisibility(viewGroup.getChildCount() > 0 ? View.VISIBLE : View.GONE);

        if (viewGroup.getId() == R.id.ads_footer_frame && mBottomBarShadow != null) {
            mBottomBarShadow.setVisibility(viewGroup.getVisibility());
        }
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
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setSearchViewClearButton();
                }

                @Override
                public void afterTextChanged(Editable s) { }
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
     * Restore the search view state after the configuration change.
     */
    public void restoreSearchViewState() {
        if (mSearchViewEditText == null) {
            return;
        }

        getContentView().post(new Runnable() {
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
     * @param requestSoftInput {@code true} to request the soft input keyboard.
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
     * @return The listener to listen search view expand and collapse callbacks.
     */
    public @Nullable DynamicSearchListener getSearchViewListener() {
        return mDynamicSearchListener;
    }

    /**
     * Sets the listener to listen search view expand and collapse callbacks.
     *
     * @param dynamicSearchListener The listener to be set.
     */
    public void setSearchViewListener(@Nullable DynamicSearchListener dynamicSearchListener) {
        this.mDynamicSearchListener = dynamicSearchListener;
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            if (isSearchViewExpanded()) {
                collapseSearchView();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Get the floating action button used by this activity.
     *
     * @return The floating action button used by this activity.
     */
    public @Nullable FloatingActionButton getFAB() {
        return mFAB;
    }

    /**
     * Set a floating action button (FAB) used by this activity by supplying an image drawable,
     * current visibility and a click listener.
     * <p>The FAB will be tinted automatically according to the accent color used by this activity.
     *
     * <p>Please use {@link #getFAB()} method to perform more operations dynamically.
     *
     * @param drawable The image drawable to be set.
     * @param visibility The visibility to be set.
     *                   <p>{@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is clicked.
     */
    public void setFAB(@Nullable Drawable drawable, int visibility,
            @Nullable View.OnClickListener onClickListener) {
        if (mFAB == null) {
            return;
        }

        removeExtendedFAB();

        setFABImageDrawable(drawable);
        mFAB.setOnClickListener(onClickListener);
        setFABVisibility(visibility);
    }

    /**
     * Set a floating action button (FAB) used by this activity by supplying an image drawable,
     * current visibility and a click listener.
     * <p>The FAB will be tinted automatically according to the accent color used by this activity.
     *
     * <p>Please use {@link #getFAB()} method to perform more operations dynamically.
     *
     * @param drawableRes The image drawable resource to be set.
     * @param visibility The visibility to be set.
     *                   <p>{@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is clicked.
     */
    public void setFAB(@DrawableRes int drawableRes, int visibility,
            @Nullable View.OnClickListener onClickListener) {
        setFAB(DynamicResourceUtils.getDrawable(
                this, drawableRes), visibility, onClickListener);
    }

    /**
     * Set the FAB image drawable resource.
     * <p>Image will be tinted automatically according to its background color to provide
     * best visibility.
     *
     * @param drawable The image drawable for the floating action button.
     */
    public void setFABImageDrawable(final @Nullable Drawable drawable) {
        if (mFAB != null) {
            mFAB.setImageDrawable(drawable);

            if (drawable == null) {
                hideFAB();
            }
        }
    }

    /**
     * Set the FAB image drawable resource.
     * <p>Image will be tinted automatically according to its background color to provide
     * best visibility.
     *
     * @param drawableRes The image drawable resource for the floating action button.
     */
    public void setFABImageDrawable(@DrawableRes int drawableRes) {
        setFABImageDrawable(DynamicResourceUtils.getDrawable(this, drawableRes));
    }

    /**
     * Set the FAB visibility to {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
     */
    public void setFABVisibility(int visibility) {
        if (mFAB != null && visibility != ADS_VISIBILITY_FAB_NO_CHANGE) {
            switch (visibility) {
                case View.VISIBLE:
                    DynamicFABUtils.show(mFAB);
                    break;
                case View.INVISIBLE:
                case View.GONE:
                    DynamicFABUtils.hide(mFAB);
                    break;
            }
        }
    }

    /**
     * Show the FAB by setting its visibility to {@link View#VISIBLE}.
     */
    public void showFAB() {
        setFABVisibility(View.VISIBLE);
    }

    /**
     * Hide the FAB by setting its visibility to {@link View#GONE}.
     */
    public void hideFAB() {
        setFABVisibility(View.GONE);
    }

    /**
     * Remove the FAB associated with this activity.
     * <p>Please call the methods {@link #setFAB(int, int, View.OnClickListener)} or
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
     * Get the extended floating action button used by this activity.
     *
     * @return The extended floating action button used by this activity.
     */
    public @Nullable ExtendedFloatingActionButton getExtendedFAB() {
        return mExtendedFAB;
    }

    /**
     * Set an extended floating action button (FAB) used by this activity by supplying an icon,
     * a text, current visibility and a click listener.
     * <p>The FAB will be tinted automatically according to the accent color used by this activity.
     *
     * <p>Please use {@link #getExtendedFAB()} method to perform more operations dynamically.
     *
     * @param icon The icon drawable to be set.
     * @param text The text to be set.
     * @param visibility The visibility to be set.
     *                   <p>{@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is clicked.
     */
    public void setExtendedFAB(@Nullable Drawable icon, @Nullable String text,
            int visibility, @Nullable View.OnClickListener onClickListener) {
        if (mExtendedFAB == null) {
            return;
        }

        removeFAB();

        updateExtendedFAB(icon, text);
        mExtendedFAB.setOnClickListener(onClickListener);
        setExtendedFABVisibility(visibility);
    }

    /**
     * Set an extended floating action button (FAB) used by this activity by supplying an icon,
     * a text, current visibility and a click listener.
     * <p>The FAB will be tinted automatically according to the accent color used by this activity.
     *
     * <p>Please use {@link #getExtendedFAB()} method to perform more operations dynamically.
     *
     * @param drawableRes The icon drawable resource to be set.
     * @param resId The string resource id to be set.
     * @param visibility The visibility to be set.
     *                   <p>{@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
     * @param onClickListener Callback that will run when this view is clicked.
     */
    public void setExtendedFAB(@DrawableRes int drawableRes, @StringRes int resId,
            int visibility, @Nullable View.OnClickListener onClickListener) {
        setExtendedFAB(DynamicResourceUtils.getDrawable(this, drawableRes),
                getString(resId), visibility, onClickListener);
    }

    /**
     * Set the extended FAB icon and text.
     * <p>Icon and text will be tinted automatically according to its background color to provide
     * best visibility.
     *
     * @param icon The icon drawable to be set.
     * @param text The text to be set.
     */
    public void updateExtendedFAB(@Nullable Drawable icon, @Nullable String text) {
        if (mExtendedFAB != null) {
            mExtendedFAB.setText(text);
            mExtendedFAB.setIcon(icon);
        }
    }

    /**
     * Set the extended FAB icon and text.
     * <p>Icon and text will be tinted automatically according to its background color to provide
     * best visibility.
     *
     * @param drawableRes The icon drawable resource to be set.
     * @param resId The string resource id to be set.
     */
    public void updateExtendedFAB(@DrawableRes int drawableRes, @StringRes int resId) {
        updateExtendedFAB(DynamicResourceUtils.getDrawable(this, drawableRes), getString(resId));
    }

    /**
     * Set the extended FAB visibility to {@link View#VISIBLE}, {@link View#INVISIBLE}
     * or {@link View#GONE}.
     */
    public void setExtendedFABVisibility(int visibility) {
        if (mExtendedFAB != null && visibility != ADS_VISIBILITY_EXTENDED_FAB_NO_CHANGE) {
            switch (visibility) {
                case View.VISIBLE:
                    DynamicFABUtils.show(mExtendedFAB, false);
                    break;
                case View.INVISIBLE:
                case View.GONE:
                    DynamicFABUtils.hide(mExtendedFAB, false);
                    break;
            }
        }
    }

    /**
     * Shrink the extended FAB.
     */
    public void shrinkFAB() {
        if (mExtendedFAB != null) {
            mExtendedFAB.shrink();
        }
    }

    /**
     * Shrink the extended FAB.
     *
     * @param allowExtended {@code true} if the FAB can be extended.
     */
    public void shrinkFAB(boolean allowExtended) {
        if (mExtendedFAB != null) {
            shrinkFAB();

            if (mExtendedFAB instanceof DynamicExtendedFloatingActionButton) {
                ((DynamicExtendedFloatingActionButton) mExtendedFAB)
                        .setAllowExtended(allowExtended);
            }
        }
    }

    /**
     * Extend the extended FAB.
     */
    public void extendFAB() {
        if (mExtendedFAB != null) {
            mExtendedFAB.extend();
        }
    }

    /**
     * Shrink the extended FAB.
     *
     * @param allowExtended {@code true} if the FAB can be extended.
     */
    public void extendFAB(boolean allowExtended) {
        if (mExtendedFAB != null) {
            extendFAB();

            if (mExtendedFAB instanceof DynamicExtendedFloatingActionButton) {
                ((DynamicExtendedFloatingActionButton) mExtendedFAB)
                        .setAllowExtended(allowExtended);
            }
        }
    }

    /**
     * Show the extended FAB by setting its visibility to {@link View#VISIBLE}.
     */
    public void showExtendedFAB() {
        setExtendedFABVisibility(View.VISIBLE);
    }

    /**
     * Hide the extended FAB by setting its visibility to {@link View#GONE}.
     */
    public void hideExtendedFAB() {
        setExtendedFABVisibility(View.GONE);
    }

    /**
     * Remove the extended FAB associated with this activity.
     * <p>Please call the methods {@link #setExtendedFAB(int, int, int, View.OnClickListener)}
     * or {@link #setExtendedFAB(Drawable, String, int, View.OnClickListener)} to set it again.
     */
    public void removeExtendedFAB() {
        if (mExtendedFAB != null) {
            updateExtendedFAB(null, null);
            mExtendedFAB.setOnClickListener(null);
            hideExtendedFAB();
        }
    }

    /**
     * Make a themed snack bar with text and action. Background will be the tint background color
     * to blend it smoothly and it will automatically use its tint color for the text and action
     * to provide best visibility.
     *
     * @param text The text to show. Can be formatted text.
     * @param duration The duration of the snack bar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snack bar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@NonNull CharSequence text,
            @Snackbar.Duration int duration) {
        return DynamicHintUtils.getSnackBar(mCoordinatorLayout, text,
                DynamicTheme.getInstance().get().getTintBackgroundColor(),
                DynamicTheme.getInstance().get().getBackgroundColor(), duration);
    }


    /**
     * Make a themed snack bar with text and action. Background will be the tint background color
     * to blend it smoothly and it will automatically use its tint color for the text and action
     * to provide best visibility.
     *
     * @param stringRes The string resource for the snack bar.
     * @param duration The duration of the snack bar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @return The snack bar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@StringRes int stringRes,
            @Snackbar.Duration int duration) {
        return getSnackBar(getString(stringRes), duration);
    }

    /**
     * Make a themed snack bar with text and action. Background will be the tint background color
     * to blend it smoothly and it will automatically use its tint color for the text and action
     * to provide best visibility.
     *
     * @param text The text to show. Can be formatted text.
     *
     * @return The snack bar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@NonNull CharSequence text) {
        return getSnackBar(text, Snackbar.LENGTH_SHORT);
    }

    /**
     * Make a themed snack bar with text and action. Background will be the tint background color
     * to blend it smoothly and it will automatically use its tint color for the text and action
     * to provide best visibility.
     *
     * @param stringRes The string resource for the snack bar.
     *
     * @return The snack bar with the supplied parameters.
     *         <p>Use {@link Snackbar#show()} to display the snack bar.
     */
    public @NonNull Snackbar getSnackBar(@StringRes int stringRes) {
        return getSnackBar(getString(stringRes));
    }
}
