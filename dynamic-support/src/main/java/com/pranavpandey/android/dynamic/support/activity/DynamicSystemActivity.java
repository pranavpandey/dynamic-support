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

package com.pranavpandey.android.dynamic.support.activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.transition.MaterialContainerTransform;
import com.pranavpandey.android.dynamic.locale.DynamicLocale;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicTransitionListener;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Base activity to perform all the system UI related tasks like setting the status and
 * navigation bar colors, theme, etc. It heavily depends on the {@link DynamicTheme} which can be
 * customised by implementing the corresponding methods.
 *
 * <p><p>Just extend this activity and implement the various methods according to the need.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class DynamicSystemActivity extends AppCompatActivity implements
        DynamicLocale, DynamicListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Dynamic theme key to maintain its state.
     */
    protected static final String ADS_STATE_DYNAMIC_THEME = "ads_state_dynamic_theme";

    /**
     * Background color key to maintain its state.
     */
    protected static final String ADS_STATE_BACKGROUND_COLOR = "ads_state_background_color";

    /**
     * Status bar color key to maintain its state.
     */
    protected static final String ADS_STATE_STATUS_BAR_COLOR = "ads_state_status_bar_color";

    /**
     * Navigation bar color key to maintain its state.
     */
    protected static final String ADS_STATE_NAVIGATION_BAR_COLOR =
            "ads_state_navigation_bar_color";

    /**
     * Shared element map key to maintain its state.
     */
    protected static final String ADS_STATE_SHARED_ELEMENT_MAP = "ads_state_shared_element_map";

    /**
     * Transition result code key to maintain its state.
     */
    protected static final String ADS_STATE_TRANSITION_RESULT_CODE =
            "ads_state_transition_result_code";

    /**
     * Transition position key to maintain its state.
     */
    protected static final String ADS_STATE_TRANSITION_POSITION = "ads_state_transition_position";

    /**
     * Default tint color for the system UI elements like snack bars, etc.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_UI_COLOR =
            Color.parseColor("#F5F5F5");

    /**
     * Default background color for the system UI elements like status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_BG_COLOR =
            Color.parseColor("#000000");

    /**
     * Default overlay color for the system UI elements like status and navigation bar.
     */
    protected static final @ColorInt int ADS_DEFAULT_SYSTEM_OVERLAY_COLOR =
            Color.parseColor("#1A000000");

    /**
     * Dynamic context used by this activity.
     */
    protected Context mContext = this;

    /**
     * Current locale used by this activity.
     */
    private Locale mCurrentLocale;

    /**
     * Saved instance state for this activity.
     */
    private Bundle mSavedInstanceState;

    /**
     * Background color used by the activity window.
     */
    protected @ColorInt int mBackgroundColor;

    /**
     * Current status bar color.
     */
    protected @ColorInt int mStatusBarColor;

    /**
     * Current navigation bar color.
     */
    protected @ColorInt int mNavigationBarColor;

    /**
     * Applied navigation bar color.
     */
    protected @ColorInt int mAppliedNavigationBarColor;

    /**
     * {@code true} if navigation bar theme is applied.
     */
    protected boolean mNavigationBarTheme;

    /**
     * Hash map to store the shared elements map.
     */
    private Map<String, Integer> mSharedElementMap;

    /**
     * Result code for the shared element transition.
     */
    private int mTransitionResultCode;

    /**
     * Transition position of the shared element.
     */
    private int mTransitionPosition;

    /**
     * Listener to listen the transition events.
     */
    private DynamicTransitionListener mDynamicTransitionListener;

    /**
     * {@code true} if activity is finishing after running the support transition.
     */
    private boolean mFinishAfterTransition;

    @Override
    public void attachBaseContext(@NonNull Context base) {
        super.attachBaseContext(setLocale(base));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDynamicTheme();
        onSetSharedElementTransition();
        super.onCreate(savedInstanceState);

        mSavedInstanceState = savedInstanceState;
        mBackgroundColor = DynamicTheme.getInstance().get().getBackgroundColor();
        mStatusBarColor = DynamicTheme.getInstance().get().getPrimaryColorDark();
        mNavigationBarColor = DynamicTheme.getInstance().get().getPrimaryColorDark();

        updateTaskDescription(DynamicTheme.getInstance().get().getPrimaryColor());
        setNavigationBarColor(mNavigationBarColor);

        onManageSharedElementTransition();
    }

    /**
     * Setup the shared element transition fot this activity.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onSetSharedElementTransition() {
        if (!DynamicSdkUtils.is21()) {
            return;
        }

        MaterialContainerTransform transform = new MaterialContainerTransform(this);
        transform.setScrimColor(Color.TRANSPARENT);
        SharedElementCallback mSharedElementCallback =
                new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(
                            @NonNull List<String> sharedElementNames,
                            @NonNull List<View> sharedElements,
                            @NonNull List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames,
                                sharedElements, sharedElementSnapshots);

                        resetSharedElementTransition();
                    }

                    @Override
                    public void onMapSharedElements(@NonNull List<String> names,
                            @NonNull Map<String, View> sharedElements) {
                        if (mSharedElementMap == null) {
                            mSharedElementMap = new HashMap<>();
                            for (Map.Entry<String, View> entry : sharedElements.entrySet()) {
                                mSharedElementMap.put(entry.getKey(), entry.getValue().getId());
                            }
                        } else if (!mFinishAfterTransition) {
                            if (mDynamicTransitionListener != null) {
                                for (Map.Entry<String, Integer> entry
                                        : mSharedElementMap.entrySet()) {
                                    if (!names.contains(entry.getKey())) {
                                        names.add(entry.getKey());
                                    }

                                    if (sharedElements.size() < names.size()) {
                                        sharedElements.put(entry.getKey(), getSharedElement(
                                                mTransitionResultCode, mTransitionPosition,
                                                entry.getKey(), entry.getValue()));
                                    }
                                }
                            }
                            super.onMapSharedElements(names, sharedElements);

                            resetSharedElementTransition();
                        } else {
                            resetSharedElementTransition();
                        }
                    }
                };

        if (getWindow().getSharedElementReturnTransition() == null) {
            setExitSharedElementCallback(mSharedElementCallback);
        } else {
            setEnterSharedElementCallback(mSharedElementCallback);
            getWindow().setSharedElementEnterTransition(transform);
            getWindow().setSharedElementReturnTransition(transform);
        }
    }

    /**
     * Setup the shared element transition callbacks to manage it's position on
     * configuration change.
     */
    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onManageSharedElementTransition() {
        if (DynamicSdkUtils.is21()) {
            if (mSavedInstanceState != null) {
                if (DynamicSdkUtils.is29()) {
                    resetSharedElementTransition();
                    return;
                }

                mBackgroundColor = mSavedInstanceState.getInt(ADS_STATE_BACKGROUND_COLOR);

                if (mSavedInstanceState.getSerializable(ADS_STATE_SHARED_ELEMENT_MAP) != null) {
                    mSharedElementMap = (HashMap<String, Integer>)
                            mSavedInstanceState.getSerializable(ADS_STATE_SHARED_ELEMENT_MAP);
                    mTransitionResultCode = mSavedInstanceState.getInt(
                            ADS_STATE_TRANSITION_RESULT_CODE);
                    mTransitionPosition = mSavedInstanceState.getInt(
                            ADS_STATE_TRANSITION_POSITION);
                }
            }

            adjustSharedElementTransition(mSavedInstanceState != null);
        }
    }

    /**
     * Adjust the activity transition and background color for the smoother shared
     * element transition.
     *
     * @param resume {@code true} if the activity is in the resumed state.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void adjustSharedElementTransition(boolean resume) {
        if (!DynamicSdkUtils.is21()){
            return;
        }

        if (getWindow().getSharedElementEnterTransition() != null
                || mBackgroundColor == Color.TRANSPARENT) {
            mBackgroundColor = Color.TRANSPARENT;
            getWindow().setBackgroundDrawable(new ColorDrawable(mBackgroundColor));
        }

        if (resume && DynamicSdkUtils.is29()
                && getWindow().getSharedElementEnterTransition() != null) {
            resetSharedElementTransition();
        }
    }

    /**
     * Resets the shared element transition.
     */
    protected void resetSharedElementTransition() {
        mBackgroundColor = DynamicTheme.getInstance().get().getBackgroundColor();
        mSharedElementMap = null;
        mDynamicTransitionListener = null;
        mFinishAfterTransition = false;
    }

    /**
     * Returns the shared element according to the given id.
     *
     * @param resultCode The transition result code.
     * @param position The position of the shared element.
     * @param viewId The id resource to find the view by id.
     *
     * @return The view according to the view id.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected @Nullable View getSharedElement(int resultCode, int position,
            @NonNull String transition, @IdRes int viewId) {
        View view = mDynamicTransitionListener == null ? findViewById(viewId)
                : mDynamicTransitionListener.onFindView(resultCode, position, transition, viewId);

        if (view != null) {
            view.setTag(null);
        }

        return view;
    }

    /**
     * Commit the fragment transaction.
     *
     * @param fragmentTransaction The fragment transaction to be committed.
     */
    public void commitFragmentTransaction(@NonNull FragmentTransaction fragmentTransaction) {
        try {
            fragmentTransaction.commit();
        } catch (Exception e) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADS_STATE_BACKGROUND_COLOR, mBackgroundColor);
        outState.putInt(ADS_STATE_STATUS_BAR_COLOR, mStatusBarColor);
        outState.putInt(ADS_STATE_NAVIGATION_BAR_COLOR, mNavigationBarColor);
        outState.putInt(ADS_STATE_TRANSITION_RESULT_CODE, mTransitionResultCode);
        outState.putInt(ADS_STATE_TRANSITION_POSITION, mTransitionPosition);
        outState.putSerializable(ADS_STATE_SHARED_ELEMENT_MAP, (Serializable) mSharedElementMap);
    }

    @Override
    public @Nullable String[] getSupportedLocales() {
        return null;
    }

    @Override
    public @NonNull Locale getDefaultLocale(@NonNull Context context) {
        return DynamicLocaleUtils.getDefaultLocale(context, getSupportedLocales());
    }

    @Override
    public @NonNull Context setLocale(@NonNull Context context) {
        this.mCurrentLocale = DynamicLocaleUtils.getLocale(getLocale(), getDefaultLocale(context));

        return mContext = DynamicLocaleUtils.setLocale(context, mCurrentLocale, getFontScale());
    }

    @Override
    public float getFontScale() {
        return getDynamicTheme() != null ? getDynamicTheme().getFontScaleRelative()
                : DynamicTheme.getInstance().getDefault().getFontScaleRelative();
    }

    /**
     * Get the style resource to apply theme on this activity.
     * <p>Override this method to supply your own customised style.
     *
     * @return The style resource to be applied on this activity.
     */
    protected @StyleRes int getThemeRes() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Get the dynamic app theme to be applied on this activity.
     * <p>Override this method to supply your own customised theme.
     *
     * @return The dynamic app theme for this activity.
     */
    protected @Nullable DynamicAppTheme getDynamicTheme() {
        return DynamicTheme.getInstance().getApplication();
    }

    /**
     * This method will be called just before the {@link #onCreate(Bundle)} after applying
     * the theme.
     * <p>Override this method to customise the theme further.
     */
    protected void onCustomiseTheme() { }

    /**
     * Returns a layout inflater factory for this activity.
     * <p>It will be used to replace the app compat widgets with their dynamic counterparts
     * to provide the support for dynamic theme.
     *
     * <p><p>Override this method to provide a custom layout inflater.
     *
     * @return The layout inflater factory for this activity.
     */
    protected @Nullable LayoutInflater.Factory2 getDynamicLayoutInflater() {
        return new DynamicLayoutInflater();
    }

    /**
     * Get the dynamic theme context used by this activity.
     *
     * @return The dynamic context used by this activity.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Get the current locale used by this activity.
     *
     * @return The current locale used by this activity.
     */
    public @NonNull Locale getCurrentLocale() {
        return mCurrentLocale;
    }

    /**
     * Get the current saved instance state for this activity.
     */
    public @Nullable Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    /**
     * Returns the transition result code for this activity.
     *
     * @return The transition result code for this activity.
     */
    public int getTransitionResultCode() {
        return mTransitionResultCode;
    }

    /**
     * Sets the transition result code for this activity.
     *
     * @param transitionResultCode The transition result code to be set.
     */
    public void setTransitionResultCode(int transitionResultCode) {
        this.mTransitionResultCode = transitionResultCode;
    }

    /**
     * Returns the transition position of the shared element.
     *
     * @return The transition position of the shared element.
     */
    public int getTransitionPosition() {
        return mTransitionPosition;
    }

    /**
     * Sets the transition position of the shared element.
     *
     * @param transitionPosition The transition position to be set.
     */
    public void setTransitionPosition(int transitionPosition) {
        this.mTransitionPosition = transitionPosition;
    }

    /**
     * Returns the dynamic transition listener used by this activity.
     *
     * @return The dynamic transition listener used by this activity.
     */
    public @Nullable DynamicTransitionListener getDynamicTransitionListener() {
        return mDynamicTransitionListener;
    }

    /**
     * Sets the dynamic transition listener for this activity.
     *
     * @param dynamicTransitionListener The dynamic transition listener to be set.
     */
    public void setDynamicTransitionListener(
            @Nullable DynamicTransitionListener dynamicTransitionListener) {
        this.mDynamicTransitionListener = dynamicTransitionListener;
    }

    /**
     * Checks whether this activity is launched from the history.
     *
     * @return {@code true} if this activity is launched from the history.
     */
    public boolean isLaunchedFromHistory() {
        return getIntent() != null && (getIntent().getFlags()
                & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0;
    }

    /**
     * Set the dynamic app theme and style resource for this activity.
     */
    private void setDynamicTheme() {
        if (getDynamicTheme() == null) {
            DynamicTheme.getInstance().attach(this,
                    getDynamicLayoutInflater()).setLocalThemeRes(getThemeRes());
        } else {
            DynamicTheme.getInstance().attach(this,
                    getDynamicLayoutInflater()).setLocalTheme(getDynamicTheme());
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(
                DynamicTheme.getInstance().get().getBackgroundColor()));

        onCustomiseTheme();
    }

    /**
     * Sets whether the navigation bar theme should be applied for this activity.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * <p><p>By default it will use the {@link Theme.ColorType#PRIMARY_DARK} color, use
     * {@link #setNavigationBarColor(int)} to set a custom color.
     *
     * @return {@code true} to apply navigation bar theme for this activity.
     */
    protected boolean setNavigationBarTheme() {
        return false;
    }

    /**
     * Returns whether the navigation bar theme is applied for this activity.
     *
     * @return {@code true} if navigation bar theme is applied.
     */
    public boolean isNavigationBarTheme() {
        return mNavigationBarTheme;
    }

    /**
     * Sets whether the navigation bar theme should be applied for this activity in landscape mode.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * <p><p>By default it will use the {@link Theme.ColorType#PRIMARY_DARK} color, use
     * {@link #setNavigationBarColor(int)} to set a custom color.
     *
     * @return {@code true} to apply navigation bar theme for this activity in the landscape mode.
     */
    protected boolean setNavigationBarThemeInLandscape() {
        return getResources().getBoolean(R.bool.ads_navigation_bar_theme_landscape);
    }

    /**
     * Sets whether register a shared preferences listener for this activity.
     *
     * @return {@code true} to register a {@link SharedPreferences.OnSharedPreferenceChangeListener}
     *         to receive preference change callback.
     */
    protected boolean setOnSharedPreferenceChangeListener() {
        return true;
    }

    /**
     * This method will be called after the theme has been changed.
     * <p>Override this method to perform operations after the theme has been changed like
     * re-initialize the {@link DynamicTheme} with new colors, etc.
     */
    protected void onAppThemeChange() {
        ActivityCompat.recreate(this);
    }

    /**
     * This method will be called after the navigation bar theme has been changed.
     * <p>Override this method to perform operations after the navigation bar theme has been
     * changed like update it with new colors.
     */
    protected void navigationBarThemeChange() {
        onAppThemeChange();
    }

    /**
     * Set the status bar color.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * @param color Color to be applied on the status bar.
     */
    protected void setWindowStatusBarColor(@ColorInt int color) {
        if (DynamicSdkUtils.is21()) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * Set the status bar color.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * @param color The color to be applied.
     */
    public void setStatusBarColor(@ColorInt int color) {
        if (DynamicSdkUtils.is21()) {
            this.mStatusBarColor = color;
            updateStatusBar();
        }
    }

    /**
     * Set the status bar color resource.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * @param colorRes The color resource to be applied.
     */
    public void setStatusBarColorRes(@ColorRes int colorRes) {
        setStatusBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent status bar flag, useful in case of {@link CollapsingToolbarLayout}.
     * <p>It will be applied only on the API 21 and above devices.
     */
    public void setTranslucentStatusBar() {
        if (DynamicSdkUtils.is21()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Update status bar color according to the supplied parameters. It will automatically
     * check for the light or dark color and will change the status bar icons or background
     * accordingly so that the icons will always be visible.
     */
    protected void updateStatusBar() {
        boolean isLightColor = !DynamicColorUtils.isColorDark(mStatusBarColor);
        if (DynamicTheme.getInstance().get().isBackgroundAware() && isLightColor) {
            if (!DynamicSdkUtils.is23()) {
                mStatusBarColor = DynamicColorUtils.getContrastColor(
                        mStatusBarColor, ADS_DEFAULT_SYSTEM_UI_COLOR);
            }
        }

        DynamicViewUtils.setLightStatusBar(getWindow().getDecorView(), isLightColor);
    }

    /**
     * Checks whether to enable edge-to-edge content.
     * <p>Override this method to provide your own implementation.
     *
     * @return {@code true} to enable edge-to-edge content.
     */
    public boolean isEdgeToEdgeContent() {
        return !mNavigationBarTheme && DynamicWindowUtils.isGestureNavigation(this);
    }

    /**
     * Returns the view to apply edge-to-edge window insets.
     *
     * @return The view to apply edge-to-edge window insets.
     *
     * @see #isApplyEdgeToEdgeInsets()
     */
    public @Nullable View getEdgeToEdgeView() {
        return null;
    }

    /**
     * Returns whether to apply edge-to-edge window insets.
     *
     * @return {@code true} to apply edge-to-edge window insets.
     *
     * @see #getEdgeToEdgeView()
     */
    public boolean isApplyEdgeToEdgeInsets() {
        return true;
    }

    /**
     * Returns the bottom view to apply edge-to-edge window insets.
     *
     * @return The bottom view to apply edge-to-edge window insets.
     *
     * @see #isApplyEdgeToEdgeInsets()
     */
    public @Nullable View getEdgeToEdgeViewBottom() {
        return getEdgeToEdgeView();
    }

    /**
     * Set the navigation bar color.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * @param color The color to be applied.
     */
    public void setNavigationBarColor(@ColorInt int color) {
        if (DynamicTheme.getInstance().get().isBackgroundAware()
                && !DynamicSdkUtils.is26()) {
            color = DynamicColorUtils.getContrastColor(color, ADS_DEFAULT_SYSTEM_UI_COLOR);
        }

        int orientation = DynamicWindowUtils.getScreenOrientation(this);
        if (DynamicWindowUtils.isNavigationBarThemeSupported(this)
                && (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)) {
            if (!setNavigationBarThemeInLandscape()) {
                color = ADS_DEFAULT_SYSTEM_BG_COLOR;
            }
        }

        this.mNavigationBarColor = color;
        if (DynamicSdkUtils.is21()) {
            this.mNavigationBarTheme = setNavigationBarTheme();

            if (isEdgeToEdgeContent()) {
                if ((getWindow().getDecorView().getSystemUiVisibility()
                        & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                        != View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) {
                    DynamicViewUtils.setEdgeToEdge(getWindow().getDecorView(), true);
                }

                if (isApplyEdgeToEdgeInsets() && getEdgeToEdgeView() != null) {
                    ViewCompat.setOnApplyWindowInsetsListener(getEdgeToEdgeView(),
                            new OnApplyWindowInsetsListener() {
                                @Override
                                public WindowInsetsCompat onApplyWindowInsets(
                                        View v, WindowInsetsCompat insets) {
                                    final ViewGroup.MarginLayoutParams lp =
                                            (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                                    lp.topMargin = insets.getSystemWindowInsetTop();

                                    v.setLayoutParams(lp);
                                    DynamicViewUtils.applyWindowInsetsBottom(
                                            getEdgeToEdgeViewBottom());

                                    return insets.consumeSystemWindowInsets();
                                }
                            });
                }

                mAppliedNavigationBarColor = Color.TRANSPARENT;
                getWindow().setNavigationBarColor(mAppliedNavigationBarColor);
            } else {
                mAppliedNavigationBarColor = color = mNavigationBarTheme
                        ? color : ADS_DEFAULT_SYSTEM_BG_COLOR;
                getWindow().setNavigationBarColor(color);
            }
        } else {
            mAppliedNavigationBarColor = mNavigationBarColor;
        }

        updateNavigationBar();
    }

    /**
     * Set the navigation bar color resource.
     * <p>It will be applied only on the API 21 and above devices.
     *
     * @param colorRes The color resource to be applied.
     */
    public void setNavigationBarColorRes(@ColorRes int colorRes) {
        setNavigationBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * Set the translucent navigation bar flag, useful in case of to show the layout behind the
     * navigation bar.
     * <p>It will be applied only on the API 21 and above devices.
     */
    public void setTranslucentNavigationBar() {
        if (DynamicSdkUtils.is21()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * Update navigation bar color according to the supplied parameters. It will automatically
     * check for the light or dark color and will background accordingly so that the buttons will
     * always be visible.
     */
    protected void updateNavigationBar() {
        DynamicViewUtils.setLightNavigationBar(getWindow().getDecorView(),
                !DynamicColorUtils.isColorDark(mAppliedNavigationBarColor));
    }

    /**
     * Get the current status bar color.
     *
     * @return The current status bar color.
     */
    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    /**
     * Get the current (original) navigation bar color.
     * <p>It may be different from the actually applied color. To get the user visible color,
     * use {@link #getAppliedNavigationBarColor()}.
     *
     * @return The current navigation bar color.
     *
     * @see #getAppliedNavigationBarColor()
     */
    public int getNavigationBarColor() {
        return mNavigationBarColor;
    }

    /**
     * Get the applied navigation bar color.
     *
     * @return The applied navigation bar color.
     */
    public int getAppliedNavigationBarColor() {
        return mAppliedNavigationBarColor;
    }

    /**
     * Fix for AppCompat 1.1.0.
     * <p>https://issuetracker.google.com/issues/140602653
     */
    @Override
    public void applyOverrideConfiguration(@Nullable Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(getResources().getConfiguration());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);

        mTransitionResultCode = requestCode;
    }

    @Override
    public void startActivityFromFragment(@NonNull Fragment fragment,
            Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityFromFragment(fragment, intent, requestCode, options);

        mTransitionResultCode = requestCode;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (setOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .registerOnSharedPreferenceChangeListener(this);
        }

        if (!DynamicTheme.getInstance().isDynamicListener(this)) {
            setDynamicTheme();
            setNavigationBarColor(mNavigationBarColor);
            adjustSharedElementTransition(true);

            getWindow().getDecorView().postDelayed(
                    mDynamicChange, DynamicTheme.DELAY_THEME_CHANGE);
        }
    }

    /**
     * Runnable to change the dynamic theme on resume.
     */
    private final Runnable mDynamicChange = new Runnable() {
        @Override
        public void run() {
            String theme = DynamicTheme.getInstance().getLocalTheme(
                    DynamicSystemActivity.this);
            if (theme != null && !theme.equals(DynamicTheme.getInstance().toString())) {
                DynamicTheme.getInstance().onDynamicChanged(false, true);
            } else if (mCurrentLocale != null) {
                if (!mCurrentLocale.equals(DynamicLocaleUtils.getLocale(
                        getLocale(), getDefaultLocale(DynamicSystemActivity.this)))) {
                    DynamicTheme.getInstance().onDynamicChanged(false, true);
                }
            }

            updateTaskDescription(DynamicTheme.getInstance().get().getPrimaryColor());
        }
    };

    @Override
    public void onPause() {
        if (setOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
        DynamicTheme.getInstance().onLocalDestroy();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        DynamicTheme.getInstance().deleteLocalTheme(this);
        super.onDestroy();
    }

    @Override
    public void onNavigationBarThemeChanged() {
        navigationBarThemeChange();
    }

    @Override
    public void onDynamicChanged(boolean context, boolean recreate) {
        if (context) {
            setLocale(getContext());
        }

        if (recreate) {
            onAppThemeChange();
        }
    }

    @Override
    public void onDynamicConfigurationChanged(boolean locale, boolean fontScale,
            boolean orientation, boolean uiMode, boolean density) {
        onDynamicChanged(locale || fontScale || orientation
                || uiMode || density, locale || uiMode);
    }

    @Override
    public void onAutoThemeChanged() { }

    @Override
    public void onPowerSaveModeChanged(boolean powerSaveMode) { }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }

    /**
     * Update the task description on API 21 and above devices to match it with the theme
     * color.
     *
     * @param color The color to be set.
     */
    protected void updateTaskDescription(@ColorInt int color) {
        if (DynamicSdkUtils.is21()) {
            setTaskDescription(new ActivityManager.TaskDescription(null, null,
                    DynamicColorUtils.removeAlpha(color)));
        }
    }

    /**
     * Finish the activity properly after checking the shared element transition.
     */
    protected void finishActivity() {
        if (!isFinishing()) {
            if (DynamicSdkUtils.is21()
                    && (getWindow().getSharedElementEnterTransition() != null
                    || getWindow().getSharedElementReturnTransition() != null)) {
                mFinishAfterTransition = true;
                supportFinishAfterTransition();
            } else {
                finish();
            }
        }
    }
}