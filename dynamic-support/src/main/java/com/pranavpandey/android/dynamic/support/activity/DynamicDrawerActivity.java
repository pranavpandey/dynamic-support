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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * Base drawer activity to handle everything related to the {@link DrawerLayout}
 * and {@link android.support.design.internal.NavigationMenu}. It has many other
 * useful functions to customise according to the need.
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
     * Header image view used by the {@link #mNavigationView}.
     */
    private ImageView mNavHeaderIcon;

    /**
     * Title text view used by the {@link #mNavigationView}.
     */
    private TextView mNavHeaderTitle;

    /**
     * Subtitle text view used by the {@link #mNavigationView}.
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
        mNavigationView.setBackgroundColor(DynamicTheme.getInstance().getBackgroundColor());

        setStatusBarColor(getStatusBarColor());
        setupDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();

        configureDrawer();
    }

    @Override
    public void onBackPressed() {
        if (!isPersistentDrawer() && (mDrawer.isDrawerOpen(GravityCompat.START)
                || mDrawer.isDrawerOpen(GravityCompat.END))) {
            closeDrawers();
        } else {
            super.onBackPressed();
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
                mDrawer.setStatusBarBackgroundColor(color);
                mDrawer.setDrawerShadow(R.drawable.ads_drawer_shadow_start, GravityCompat.START);
            }
        }
    }

    /**
     * @return {@code true} to show the drawer toggle icon.
     */
    protected boolean setActionBarDrawerToggle() {
        return true;
    }

    /**
     * Initialize the navigation drawer.
     */
    private void setupDrawer() {
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
            frame.setPadding(getResources().getDimensionPixelSize(
                    R.dimen.ads_margin_content_start), frame.getPaddingTop(),
                    frame.getPaddingRight(), frame.getPaddingBottom());
        } else {
            if (isDrawerLocked()) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawer.post(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawers();
                    }
                });
            }
        }
    }

    /**
     * @return {@code true} to make the drawer persistent by locking
     * it in the open mode.
     *
     * @see DrawerLayout#LOCK_MODE_LOCKED_OPEN
     */
    public boolean isPersistentDrawer() {
        return false;
    }

    /**
     * Getter for {@link #mDrawer}.
     */
    public @NonNull DrawerLayout getDrawer() {
        return mDrawer;
    }

    /**
     * Close the drawer if it is not in the locked state.
     *
     * @param gravity Gravity of the drawer to close it.
     *
     * @see DrawerLayout#LOCK_MODE_LOCKED_OPEN
     */
    public void closeDrawer(int gravity) {
        if (mDrawer.isDrawerVisible(gravity)
                && mDrawer.getDrawerLockMode(gravity)
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
     * @return {@code true} if the {@link #mDrawer} is in the locked
     * mode.
     */
    public boolean isDrawerLocked() {
        return mDrawer.getDrawerLockMode(GravityCompat.START)
                == DrawerLayout.LOCK_MODE_LOCKED_OPEN
                || mDrawer.getDrawerLockMode(GravityCompat.END)
                == DrawerLayout.LOCK_MODE_LOCKED_OPEN;
    }

    /**
     * Getter for {@link #mDrawerToggle}.
     */
    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    /**
     * Set the drawer toggle icon to open or close the navigation
     * drawer.
     *
     * @param showDrawerIndicator {@code true} to show the drawer toggle
     *                            icon.
     */
    public void showDrawerToggle(boolean showDrawerIndicator) {
        if (mDrawerToggle != null && getSupportActionBar() != null) {
            if (!showDrawerIndicator) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                setupDrawer();
            }
        }
    }

    /**
     * Animate the {@link #mDrawerToggle} from one offset to another.
     *
     * @param startOffSet The start offset.
     * @param endOffSet The end offset.
     */
    public void animateDrawerToggle(final float startOffSet, final float endOffSet) {
        if (endOffSet == 0f && !isPersistentDrawer()) {
            showDrawerToggle(true);
        }

        if (!isPersistentDrawer()) {
            ValueAnimator anim = ValueAnimator.ofFloat(startOffSet, endOffSet);

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    mDrawerToggle.onDrawerSlide(mDrawer, slideOffset);
                }
            });

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (endOffSet == 1f) {
                        showDrawerToggle(false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });

            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(350);
            anim.start();
        } else {
            showDrawerToggle(false);
        }
    }

    /**
     * Getter for {@link #mNavigationView}.
     */
    public @NonNull NavigationView getNavigationView() {
        return mNavigationView;
    }

    /**
     * Set the menu for {@link #mNavigationView}.
     *
     * @param menuId Menu resource id for the navigation view.
     */
    public void setNavigationViewMenu(@MenuRes int menuId) {
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(menuId);
    }

    /**
     * Set the header icon for {@link #mNavigationView}.
     *
     * @param drawable Drawable for the header image view.
     */
    public void setNavHeaderIcon(@Nullable Drawable drawable) {
        mNavHeaderIcon.setImageDrawable(drawable);
    }

    /**
     * Set the header icon for {@link #mNavigationView}.
     *
     * @param drawableId Drawable resource id for the header image view.
     */
    public void setNavHeaderIcon(@DrawableRes int drawableId) {
        setNavHeaderIcon(DynamicResourceUtils.getDrawable(this, drawableId));
    }

    /**
     * Set the header title for {@link #mNavigationView}.
     *
     * @param string Text for the title text view.
     */
    public void setNavHeaderTitle(@Nullable String string) {
        mNavHeaderTitle.setText(string);
    }

    /**
     * Set the header title for {@link #mNavigationView}.
     *
     * @param stringId String resource id for the title text view.
     */
    public void setNavHeaderTitle(@StringRes int stringId) {
        mNavHeaderTitle.setText(stringId);
    }

    /**
     * Set the header subtitle for {@link #mNavigationView}.
     *
     * @param string Text for the subtitle text view.
     */
    public void setNavHeaderSubtitle(@Nullable String string) {
        mNavHeaderSubtitle.setText(string);
    }

    /**
     * Set the header subtitle for {@link #mNavigationView}.
     *
     * @param stringId String resource id for the subtitle text view.
     */
    public void setNavHeaderSubtitle(@StringRes int stringId) {
        mNavHeaderSubtitle.setText(stringId);
    }
}
