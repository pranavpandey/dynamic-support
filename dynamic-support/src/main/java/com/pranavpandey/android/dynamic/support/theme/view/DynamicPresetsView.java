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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewNested;
import com.pranavpandey.android.dynamic.support.theme.adapter.DynamicPresetsAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;
import com.pranavpandey.android.dynamic.support.widget.Dynamic;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.ThemeContract;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils;

/**
 * A recycler view frame layout to show the theme presets.
 */
public class DynamicPresetsView<T extends DynamicAppTheme>
        extends DynamicRecyclerViewNested implements LifecycleObserver {

    /**
     * Listener to get the preset theme and click events.
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
    private ViewGroup mHeader;

    /**
     * View parent to show the info.
     */
    private View mViewInfo;

    /**
     * View to show the info.
     */
    private DynamicItemView mInfo;

    /**
     * Recycler view adapter to show a list of theme previews.
     */
    private DynamicPresetsAdapter<T> mPresetsAdapter;

    /**
     * lifecycle owner for this view.
     */
    private Fragment mLifecycleOwner;

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

        mHeader = findViewById(R.id.ads_theme_presets_header_card);
        mViewInfo = findViewById(R.id.ads_theme_presets_info_view);
        mInfo = findViewById(R.id.ads_theme_presets_info);

        mViewInfo.setOnClickListener(new OnClickListener() {
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
                    DynamicLinkUtils.viewInGooglePlay(getContext(),
                            ThemeContract.Preset.AUTHORITY);
                }
            }
        });

        Dynamic.setColorType(((DynamicHeader) findViewById(
                R.id.ads_theme_presets_header)).getIconView(), Theme.ColorType.NONE);
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

        if (owner != null) {
            owner.getLifecycle().addObserver(this);
        }

        loadPresets();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void loadPresets() {
        if (isPackageExists()) {
            if (isPermissionGranted()) {
                setPresetsVisible(true);
            } else {
                mInfo.setSubtitle(getContext().getString(
                        R.string.ads_permissions_subtitle_single));
                setPresetsVisible(false);            }
        } else {
            mInfo.setSubtitle(getContext().getString(R.string.ads_theme_presets_desc));
            setPresetsVisible(false);
        }

        if (mLifecycleOwner != null && isPermissionGranted()) {
            LoaderManager.getInstance(mLifecycleOwner).initLoader(
                    ADS_LOADER_PRESETS, null, mLoaderCallbacks).forceLoad();
        }
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
    public DynamicPresetsAdapter<T> getPresetsAdapter() {
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
     * @return {@code true} if the the presets manager is available.
     */
    public boolean isPackageExists() {
        return DynamicPackageUtils.isPackageExists(getContext(), ThemeContract.Preset.AUTHORITY);
    }

    /**
     * Checks whether the required permissions are granted to read the theme presets.
     *
     * @return {@code true} the required permissions are granted to read the theme presets.
     */
    public boolean isPermissionGranted() {
        return DynamicPermissions.getInstance().isGranted(
                ThemeContract.Preset.READ_PERMISSIONS, false);
    }

    /**
     * Sets the visibility of the presets view.
     *
     * @param visible {@code true} to make the header and recycler view visible.
     */
    private void setPresetsVisible(boolean visible) {
        if (visible) {
            mHeader.setVisibility(VISIBLE);
            mViewInfo.setVisibility(GONE);
            getRecyclerView().setVisibility(VISIBLE);
        } else {
            mHeader.setVisibility(GONE);
            getRecyclerView().setVisibility(GONE);
        }
    }

    /**
     * Loader manager to callbacks to query presets from the theme provider.
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    if (id == ADS_LOADER_PRESETS) {
                        if (isPermissionGranted()) {
                            return new CursorLoader(getContext().getApplicationContext(),
                                    ThemeContract.Preset.CONTENT_URI,
                                    new String[] { ThemeContract.Preset.Column.THEME },
                                    null, null, null);
                        } else {
                            return new Loader<>(getContext().getApplicationContext());
                        }
                    }

                    throw new IllegalArgumentException();
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    if (loader.getId() == ADS_LOADER_PRESETS) {
                        if (data != null) {
                            mPresetsAdapter.setPresets(data);
                        }

                        setPresetsVisible(data != null && data.getCount() > 0);
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    if (loader.getId() == ADS_LOADER_PRESETS) {
                        mPresetsAdapter.setPresets(null);
                        setPresetsVisible(false);
                    }
                }
            };
}
