/*
 * Copyright 2019 Pranav Pandey
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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicTintUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * Base drawer activity to handle everything related to the {@link DrawerLayout} and
 * {@link com.google.android.material.internal.NavigationMenu}.
 * <p>It also has many other useful functions to customise according to the need.
 */
public abstract class DynamicDrawerActivity extends DynamicActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Drawer layout used by this activity.
     */
    private DrawerLayout mDrawer;

    /**
     * Action bar drawer toggle used by this activity.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Navigation view used by this activity.
     */
    private NavigationView mNavigationView;

    /**
     * Header image view used by the navigation view.
     */
    private ImageView mNavHeaderIcon;

    /**
     * Title text view used by the navigation view.
     */
    private TextView mNavHeaderTitle;

    /**
     * Subtitle text view used by the navigation view.
     */
    private TextView mNavHeaderSubtitle;

    @Override
    protected int getLayoutRes() {
        return setCollapsingToolbarLayout()
                ? R.layout.ads_activity_drawer_collapsing
                : R.layout.ads_activity_drawer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawer = findViewById(R.id.ads_drawer_layout);
        mNavigationView = findViewById(R.id.ads_navigation_view);
        mNavHeaderIcon = mNavigationView.getHeaderView(0)
                .findViewById(R.id.ads_header_drawer_icon);
        mNavHeaderTitle = mNavigationView.getHeaderView(0)
                .findViewById(R.id.ads_header_drawer_title);
        mNavHeaderSubtitle = mNavigationView.getHeaderView(0)
                .findViewById(R.id.ads_header_drawer_subtitle);

        mDrawer.setDrawerElevation(DynamicUnitUtils.convertDpToPixels(8));

        setupDrawer();
        setStatusBarColor(getStatusBarColor());
        setNavigationBarColor(getNavigationBarColor());
    }

    @Override
    public void onResume() {
        super.onResume();

        configureDrawer();
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            if (!isPersistentDrawer() && (mDrawer.isDrawerOpen(GravityCompat.START)
                    || mDrawer.isDrawerOpen(GravityCompat.END))) {
                closeDrawers();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected boolean isDrawerActivity() {
        return true;
    }

    @Override
    public void setStatusBarColor(@ColorInt int color) {
        super.setStatusBarColor(color);

        if (isDrawerActivity()) {
            if (mDrawer != null) {
                mDrawer.setStatusBarBackgroundColor(getStatusBarColor());
                mDrawer.setDrawerShadow(R.drawable.ads_drawer_shadow_start, GravityCompat.START);
            }
        }
    }

    @Override
    public @Nullable View getEdgeToEdgeView() {
        return mDrawer;
    }

    /**
     * Sets the action bar drawer toggle icon according to the returned value.
     *
     * @return {@code true} to show the drawer toggle icon.
     */
    protected boolean setActionBarDrawerToggle() {
        return true;
    }

    /**
     * Initialize the navigation drawer.
     */
    private void setupDrawer() {
        if (mDrawer == null) {
            return;
        }

        if (setActionBarDrawerToggle()) {
            mDrawerToggle = new ActionBarDrawerToggle(
                    this, mDrawer, getToolbar(), R.string.ads_navigation_drawer_open,
                    R.string.ads_navigation_drawer_close);
            mDrawer.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        } else {
            showDrawerToggle(false);
        }

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                collapseSearchView();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) { }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) { }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });

        DynamicTintUtils.setInsetForegroundColor(mNavigationView,
                getStatusBarColor(), !isPersistentDrawer());
        mNavigationView.setNavigationItemSelectedListener(this);
        configureDrawer();
    }

    /**
     * Configure navigation drawer for the persistent mode.
     */
    private void configureDrawer() {
        if (isPersistentDrawer()) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawer.setScrimColor(Color.TRANSPARENT);
            mDrawerToggle.setDrawerIndicatorEnabled(false);

            ViewGroup frame = findViewById(R.id.ads_drawer_persistent_frame);

            if (DynamicLocaleUtils.isLayoutRtl()) {
                frame.setPadding(frame.getPaddingLeft(), frame.getPaddingTop(),
                        getResources().getDimensionPixelSize(R.dimen.ads_margin_content_start),
                        frame.getPaddingBottom());
            } else {
                frame.setPadding(getResources().getDimensionPixelSize(
                        R.dimen.ads_margin_content_start), frame.getPaddingTop(),
                        frame.getPaddingRight(), frame.getPaddingBottom());
            }
        } else {
            if (isDrawerLocked()) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getContentView().post(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawers();
                    }
                });
            }
        }
    }

    /**
     * Makes the drawer persistent according to the returned value.
     *
     * @return {@code true} to make the drawer persistent by locking it in the open mode.
     *
     * @see DrawerLayout#LOCK_MODE_LOCKED_OPEN
     */
    public boolean isPersistentDrawer() {
        return getResources().getBoolean(R.bool.ads_persistent_drawer);
    }

    /**
     * Get the drawer layout used by this activity.
     *
     * @return The drawer layout used by this activity.
     */
    public @NonNull DrawerLayout getDrawer() {
        return mDrawer;
    }

    /**
     * Checks whether the drawer is in the open state.
     *
     * @return {@code true} if the drawer is in the open mode.
     */
    public boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen(GravityCompat.START)
                || mDrawer.isDrawerOpen(GravityCompat.END);
    }

    /**
     * Close the drawer if it is not in the locked state.
     *
     * @param gravity The gravity of the drawer to close it.
     *
     * @see DrawerLayout#LOCK_MODE_LOCKED_OPEN
     */
    public void closeDrawer(int gravity) {
        if (mDrawer.isDrawerVisible(gravity) && mDrawer.getDrawerLockMode(gravity)
                != DrawerLayout.LOCK_MODE_LOCKED_OPEN) {
            mDrawer.closeDrawer(gravity);
        }
    }

    /**
     * Close all the opened drawers if they are not in the locked state.
     *
     * @see DrawerLayout#LOCK_MODE_LOCKED_OPEN
     */
    public void closeDrawers() {
        closeDrawer(GravityCompat.START);
        closeDrawer(GravityCompat.END);
    }

    /**
     * Checks whether the drawer is in the locked mode.
     *
     * @return {@code true} if the drawer is in the locked mode.
     */
    public boolean isDrawerLocked() {
        return mDrawer.getDrawerLockMode(GravityCompat.START)
                == DrawerLayout.LOCK_MODE_LOCKED_OPEN
                || mDrawer.getDrawerLockMode(GravityCompat.END)
                == DrawerLayout.LOCK_MODE_LOCKED_OPEN;
    }

    /**
     * Returns the action bar drawer toggle used by this activity.
     *
     * @return The action bar drawer toggle used by this activity.
     */
    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    /**
     * Set the drawer toggle icon to open or close the navigation drawer.
     *
     * @param showDrawerIndicator {@code true} to show the drawer toggle icon.
     */
    public void showDrawerToggle(boolean showDrawerIndicator) {
        if (mDrawerToggle != null && getSupportActionBar() != null) {
            if (!showDrawerIndicator) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (getToolbar() != null) {
                    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                setupDrawer();
            }
        }
    }

    /**
     * Animate the drawer toggle icon from one offset to another.
     *
     * @param startOffSet The start offset.
     * @param endOffSet The end offset.
     */
    public void animateDrawerToggle(final float startOffSet, final float endOffSet) {
        if (endOffSet == 0f && !isPersistentDrawer()) {
            showDrawerToggle(true);
        }

        if (!isPersistentDrawer()) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(startOffSet, endOffSet);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    mDrawerToggle.onDrawerSlide(mDrawer, slideOffset);
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) { }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (endOffSet == 1f) {
                        showDrawerToggle(false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) { }

                @Override
                public void onAnimationRepeat(Animator animator) { }
            });

            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(350);
            valueAnimator.start();
        } else {
            showDrawerToggle(false);
        }
    }

    /**
     * Get the navigation view used by this activity.
     *
     * @return The navigation view used by this activity.
     */
    public @NonNull NavigationView getNavigationView() {
        return mNavigationView;
    }

    /**
     * Set menu for the navigation view.
     *
     * @param menuRes The menu resource id for the navigation view.
     */
    public void setNavigationViewMenu(@MenuRes int menuRes) {
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(menuRes);
    }

    /**
     * Set header icon for the navigation view.
     *
     * @param drawable The drawable for the header image view.
     */
    public void setNavHeaderIcon(@Nullable Drawable drawable) {
        mNavHeaderIcon.setImageDrawable(drawable);
    }

    /**
     * Set header icon for the navigation view.
     *
     * @param drawableRes The drawable resource for the header image view.
     */
    public void setNavHeaderIcon(@DrawableRes int drawableRes) {
        setNavHeaderIcon(DynamicResourceUtils.getDrawable(this, drawableRes));
    }

    /**
     * Set header title for the navigation view.
     *
     * @param string The string for the title.
     */
    public void setNavHeaderTitle(@Nullable String string) {
        mNavHeaderTitle.setText(string);
    }

    /**
     * Set header title for the navigation view.
     *
     * @param stringRes The string resource for the title.
     */
    public void setNavHeaderTitle(@StringRes int stringRes) {
        mNavHeaderTitle.setText(stringRes);
    }

    /**
     * Set header subtitle for the navigation view.
     *
     * @param string The string for the subtitle.
     */
    public void setNavHeaderSubtitle(@Nullable String string) {
        mNavHeaderSubtitle.setText(string);
    }

    /**
     * Set header subtitle for the navigation view.
     *
     * @param stringRes The string resource for the subtitle.
     */
    public void setNavHeaderSubtitle(@StringRes int stringRes) {
        mNavHeaderSubtitle.setText(stringRes);
    }
}
