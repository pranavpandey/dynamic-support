/*
 * Copyright 2018-2024 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewNested;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.adapter.DynamicPresetsAdapter;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView;
import com.pranavpandey.android.dynamic.theme.ThemeContract;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.DynamicPackageUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * A recycler view frame layout to show the theme presets.
 *
 * @param <T> The type of the dynamic app theme this view will handle.
 */
public class DynamicPresetsView<T extends DynamicAppTheme>
        extends DynamicRecyclerViewNested implements DefaultLifecycleObserver {

    /**
     * Listener to get the preset theme and click events.
     *
     * @param <T> The type of the dynamic app theme.
     */
    public interface DynamicPresetsListener<T extends DynamicAppTheme> {

        /**
         * This method will be called to request the required permissions.
         *
         * @param permissions The permissions to be requested.
         */
        void onRequestPermissions(@NonNull String[] permissions);

        /**
         * This method will be called while creating the dynamic theme.
         *
         * @param theme The dynamic theme string associated with the clicked preset.
         *
         * @return The created dynamic theme.
         *
         * @see DynamicThemeUtils#decodeTheme(String)
         */
        @Nullable T getDynamicTheme(@NonNull String theme);

        /**
         * This method will be called when a preset is clicked.
         *
         * @param anchor The anchor or the clicked view.
         * @param theme The dynamic theme associated with the clicked preset.
         * @param themePreview The theme preview associated with the preset.
         */
        void onPresetClick(@NonNull View anchor, @NonNull String theme,
                @NonNull ThemePreview<T> themePreview);
    }

    /**
     * Loader id to show the presets.
     */
    private static final int ADS_LOADER_PRESETS = 1;

    /**
     * View group to show the header.
     */
    private ViewGroup mHeaderCard;

    /**
     * View to show the info.
     */
    private DynamicItemView mHeader;

    /**
     * Recycler view adapter to show a list of theme previews.
     */
    private DynamicPresetsAdapter<T> mPresetsAdapter;

    /**
     * lifecycle owner for this view.
     */
    private Fragment mLifecycleOwner;

    /**
     * Loader used by this view.
     */
    private Loader<Cursor> mLoader;

    /**
     * Listener to handle the preset events.
     */
    private DynamicPresetsView.DynamicPresetsListener<T> mDynamicPresetsListener;

    public DynamicPresetsView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicPresetsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicPresetsView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialize() {
        super.initialize();

        mHeader = findViewById(R.id.ads_theme_presets_header);
        mHeaderCard = findViewById(R.id.ads_theme_presets_header_card);

        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (DynamicSdkUtils.is16()) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        loadPresets();
                    }
                });
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);

        loadPresets();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mLifecycleOwner != null) {
            mLifecycleOwner.getLifecycle().removeObserver(this);
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_theme_presets;
    }

    @Override
    public @Nullable RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return DynamicLayoutUtils.getLinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public void showProgress(boolean animate) {
        super.showProgress(false);
    }

    @Override
    public void hideProgress(boolean animate) {
        super.hideProgress(false);
    }

    /**
     * Returns the type for the presets adapter.
     *
     * @return The type for the presets adapter.
     */
    protected @DynamicPresetsAdapter.Type int getType() {
        return DynamicPresetsAdapter.Type.HORIZONTAL;
    }

    /**
     * Set a presets adapter for the recycler view.
     *
     * @param owner The life cycle owner of this view.
     * @param layout The layout resource containing the theme preview.
     * @param dynamicPresetsListener The listener to receive the preset events.
     *
     * @see ThemePreview
     * @see androidx.lifecycle.LifecycleOwner
     * @see ViewModelStoreOwner
     */
    public void setPresetsAdapter(@Nullable Fragment owner, @LayoutRes int layout,
            @Nullable DynamicPresetsView.DynamicPresetsListener<T> dynamicPresetsListener) {
        this.mLifecycleOwner = owner;
        this.mDynamicPresetsListener = dynamicPresetsListener;

        mPresetsAdapter = new DynamicPresetsAdapter<>(getContext(), getType(), layout);
        mPresetsAdapter.setDynamicPresetsListener(dynamicPresetsListener);
        setAdapter(mPresetsAdapter);

        if (mLifecycleOwner != null) {
            mLifecycleOwner.getLifecycle().addObserver(this);
        }

        loadPresets();
    }

    /**
     * Runnable to load the theme presets.
     */
    private final Runnable mLoadPresets = new Runnable() {
        @Override
        public void run() {
            setPresetsVisible(isPackageExists() && isPermissionGranted());

            if (mLifecycleOwner == null || !isPermissionGranted()) {
                return;
            }

            if (mLoader == null) {
                mLoader = LoaderManager.getInstance(mLifecycleOwner).initLoader(
                        ADS_LOADER_PRESETS, null, mLoaderCallbacks);
            }

            if (mLoader.isStarted()) {
                mLoader.stopLoading();
                mLoader.forceLoad();
            } else {
                mLoader.startLoading();
            }
        }
    };

    /**
     * Try to load the theme presets.
     */
    public void loadPresets() {
        if (DynamicMotion.getInstance().isMotion()) {
            DynamicMotion.getInstance().beginDelayedTransition((ViewGroup) getParent());
        }

        post(mLoadPresets);
        postDelayed(mLoadPresets, DynamicMotion.Duration.SHORT);
    }

    /**
     * Set a presets adapter for the recycler view.
     *
     * @param owner The life cycle owner of this view.
     * @param dynamicPresetsListener The listener to receive the preset events.
     *
     * @see ThemePreview
     * @see androidx.lifecycle.LifecycleOwner
     * @see ViewModelStoreOwner
     */
    public void setPresetsAdapter(@Nullable Fragment owner,
            @Nullable DynamicPresetsView.DynamicPresetsListener<T> dynamicPresetsListener) {
        mPresetsAdapter = new DynamicPresetsAdapter<>(getContext(), getType());
        setPresetsAdapter(owner, mPresetsAdapter.getLayoutRes(), dynamicPresetsListener);
    }

    /**
     * Returns the presets adapter used by the recycler view.
     *
     * @return The presets adapter used by the recycler view.
     */
    @SuppressWarnings("unchecked")
    public @Nullable DynamicPresetsAdapter<T> getPresetsAdapter() {
        return (DynamicPresetsAdapter<T>) getAdapter();
    }

    /**
     * Get the dynamic preset listener used by this adapter.
     *
     * @return The dynamic preset listener used by this adapter.
     */
    public @Nullable DynamicPresetsView.DynamicPresetsListener<T> getDynamicPresetsListener() {
        return mDynamicPresetsListener;
    }

    /**
     * Sets the dynamic preset listener for this adapter.
     *
     * @param dynamicPresetsListener The listener to be set.
     */
    public void setDynamicPresetsListener(
            @Nullable DynamicPresetsView.DynamicPresetsListener<T> dynamicPresetsListener) {
        this.mDynamicPresetsListener = dynamicPresetsListener;

        if (mPresetsAdapter != null) {
            mPresetsAdapter.setDynamicPresetsListener(dynamicPresetsListener);
        }
    }

    /**
     * Checks whether the presets manager is available.
     *
     * @return {@code true} if the presets manager is available.
     */
    public boolean isPackageExists() {
        return DynamicPackageUtils.isPackageExists(getContext(), ThemeContract.Preset.AUTHORITY);
    }

    /**
     * Checks whether the required permissions are granted to read the theme presets.
     *
     * @return {@code true} if the required permissions are granted to read the theme presets.
     */
    public boolean isPermissionGranted() {
        return DynamicPermissions.getInstance().isGranted(
                ThemeContract.Preset.READ_PERMISSIONS, false);
    }

    /**
     * Sets the visibility of the presets view.
     *
     * @param visible {@code true} to make the header and recycler view visible.
     * @param data The presets data to be set.
     */
    private void setPresetsVisible(final boolean visible, @Nullable Cursor data) {
        if (DynamicMotion.getInstance().isMotion()) {
            DynamicMotion.getInstance().beginDelayedTransition(
                    (ViewGroup) DynamicPresetsView.this.getParent());
        }

        if (visible) {
            Dynamic.setVisibility(getRecyclerView(), VISIBLE);
            mHeader.setTitle(getContext().getString(R.string.ads_picker_presets));
            mHeader.setSubtitle(getContext().getString(R.string.ads_theme_presets_import));
            mHeader.setImageDrawable(DynamicResourceUtils.getDrawable(
                    getContext(), R.drawable.ads_ic_refresh), true);

            Dynamic.setOnClickListener(mHeader, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPresets();
                }
            });
        } else {
            Dynamic.setVisibility(getRecyclerView(), GONE);
            mHeader.setTitle(getContext().getString(R.string.ads_theme_presets));
            mHeader.setImageDrawable(null, true);

            if (!isPackageExists()) {
                mHeader.setSubtitle(String.format(
                        getContext().getString(R.string.ads_theme_presets_desc_app),
                        getContext().getString(R.string.ads_theme_presets_app)));
                mHeader.setImageDrawable(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_download), true);
            } else if (!isPermissionGranted()) {
                mHeader.setSubtitle(getContext().getString(
                        R.string.ads_permissions_subtitle_single));
                mHeader.setImageDrawable(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_security), true);
            }

            Dynamic.setOnClickListener(mHeader, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPackageExists()) {
                        if (mDynamicPresetsListener != null && !isPermissionGranted()) {
                            mDynamicPresetsListener.onRequestPermissions(
                                    ThemeContract.Preset.READ_PERMISSIONS);
                        } else {
                            loadPresets();
                        }
                    } else {
                        DynamicLinkUtils.viewApp(getContext(), ThemeContract.Preset.AUTHORITY,
                                DynamicTheme.getInstance().getProductFlavor());
                    }
                }
            });
        }

        if (getPresetsAdapter() != null) {
            getPresetsAdapter().setPresets(data);
        }
    }

    /**
     * Sets the visibility of the presets view.
     *
     * @param visible {@code true} to make the header and recycler view visible.
     *
     * @see #setPresetsVisible(boolean, Cursor)
     */
    private void setPresetsVisible(boolean visible) {
        setPresetsVisible(visible, null);
    }

    /**
     * Loader manager callbacks to query presets from the theme provider.
     */
    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public @NonNull Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (id != ADS_LOADER_PRESETS) {
                throw new IllegalArgumentException();
            }

            if (isPermissionGranted()) {
                try {
                    setPresetsVisible(false);

                    return new CursorLoader(getContext().getApplicationContext(),
                            ThemeContract.Preset.CONTENT_URI,
                            new String[] { ThemeContract.Preset.Column.THEME },
                            null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new Loader<>(getContext().getApplicationContext());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (loader.getId() != ADS_LOADER_PRESETS) {
                return;
            }

            setPresetsVisible(data != null && data.getCount() > 0, data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (loader.getId() != ADS_LOADER_PRESETS) {
                return;
            }

            setPresetsVisible(false, null);
        }
    };
}
