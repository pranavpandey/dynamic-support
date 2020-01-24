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

package com.pranavpandey.android.dynamic.support.theme.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicFileUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.io.File;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the need.
 */
public abstract class ThemeFragment<T extends DynamicAppTheme> extends DynamicFragment {

    /**
     * Constant to request the theme file location.
     */
    protected static final int REQUEST_THEME_LOCATION = 0;

    /**
     * Constant to request the theme import from file.
     */
    protected static final int REQUEST_THEME_IMPORT = 1;

    /**
     * Dynamic app theme used by this fragment.
     */
    protected T mDynamicAppTheme;

    /**
     * Default dynamic app theme used by this fragment.
     */
    protected T mDynamicAppThemeDefault;

    /**
     * Temporary file to store the exported theme.
     */
    protected File mThemeExported;

    /**
     * {@code true} if the settings has been changed.
     */
    protected boolean mSettingsChanged;

    /**
     * Theme preview used by this fragment.
     */
    protected ThemePreview<T> mThemePreview;

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        DynamicMenuUtils.forceMenuIcons(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.ads_menu_theme, menu);

        if (DynamicFileUtils.getTempDir(getContext()) == null) {
            menu.findItem(R.id.ads_menu_theme_file).setVisible(false);
        } else if (!DynamicSdkUtils.is19()) {
            menu.findItem(R.id.ads_menu_theme_file_save).setVisible(false);
            menu.findItem(R.id.ads_menu_theme_file_import).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.ads_menu_theme_copy) {
            DynamicLinkUtils.copyToClipboard(getContext(), getString(R.string.ads_theme),
                    mThemePreview.getDynamicTheme().toDynamicString());

            getDynamicActivity().getSnackBar(R.string.ads_theme_copy_done).show();
        } else if (i == R.id.ads_menu_theme_share) {
            onSetActionIcon(false);
            DynamicLinkUtils.share(getContext(),
                    getSubtitle() != null ? getSubtitle().toString() : null,
                    DynamicThemeUtils.getThemeUrl(mThemePreview.getDynamicTheme()),
                    DynamicFileUtils.getBitmapUri(getContext(),
                            DynamicThemeUtils.createThemeBitmap(mThemePreview),
                            Theme.Key.SHARE));
            onSetActionIcon(true);
        } else if (i == R.id.ads_menu_theme_import) {
            importTheme(false);
        } else if (i == R.id.ads_menu_theme_file_save) {
            mThemeExported = DynamicThemeUtils.requestThemeFile(getContext(),
                    Theme.NAME, mThemePreview.getDynamicTheme().toDynamicString());
            startActivityForResult(DynamicFileUtils.getSaveToFileIntent(getContext(),
                    mThemeExported, Theme.MIME), REQUEST_THEME_LOCATION);
        } else if (i == R.id.ads_menu_theme_file_share) {
            onSetActionIcon(false);
            DynamicLinkUtils.share(getContext(),
                    getSubtitle() != null ? getSubtitle().toString() : null,
                    DynamicThemeUtils.getThemeUrl(mThemePreview.getDynamicTheme()),
                    DynamicFileUtils.getUriFromFile(getContext(),
                            DynamicThemeUtils.requestThemeFile(getContext(), Theme.NAME,
                                    mThemePreview.getDynamicTheme().toDynamicString())),
                    Theme.MIME);
            onSetActionIcon(true);
        } else if (i == R.id.ads_menu_theme_file_import) {
            importTheme(true);
        } else if (i == R.id.ads_menu_refresh) {
            mSettingsChanged = false;
            loadTheme(mDynamicAppTheme);

            getDynamicActivity().setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }

            switch (requestCode) {
                case REQUEST_THEME_LOCATION:
                    if (uri != null) {
                        if (DynamicFileUtils.writeToFile(getContext(),
                                DynamicFileUtils.getUriFromFile(
                                        getContext(), mThemeExported), uri)) {
                            getDynamicActivity().getSnackBar(String.format(getString(
                                    R.string.ads_theme_format_saved), DynamicFileUtils
                                    .getFileNameFromUri(getContext(), uri))).show();
                        } else {
                            getDynamicActivity().getSnackBar(
                                    R.string.ads_theme_export_error).show();
                        }
                    }
                    break;
                case REQUEST_THEME_IMPORT:
                    if (uri != null) {
                        importTheme(DynamicFileUtils.readStringFromFile(
                                getContext(), uri), true);
                    }
                    break;
            }
        }
    }

    /**
     * This method will be called when importing the theme from a string or file.
     *
     * @param theme The theme string to be imported.
     */
    protected abstract @NonNull T onImportTheme(@NonNull String theme);

    /**
     * Update settings according to the supplied theme.
     *
     * @param theme The dynamic app theme to be loaded.
     */
    protected abstract void loadTheme(@NonNull T theme);

    /**
     * This method will be called on setting the action icon.
     * <p>It can be used to change the action icon while sharing the theme.
     *
     * @param enable {@code true} to enable the action icon;
     */
    public abstract void onSetActionIcon(boolean enable);

    /**
     * Get the theme preview used by this fragment.
     *
     * The theme preview used by this fragment.
     */
    public @NonNull ThemePreview<T> getThemePreview() {
        return mThemePreview;
    }

    /**
     * Show dialog to import the theme.
     *
     * @param file {@code true} to import from the file.
     *
     * @see DynamicThemeDialog
     * @see DynamicThemeDialog.Type#THEME_IMPORT
     */
    protected void importTheme(final boolean file) {
        if (file) {
            startActivityForResult(DynamicFileUtils.getFileSelectIntent(
                    Theme.MIME), REQUEST_THEME_IMPORT);
        } else {
            DynamicThemeDialog.newInstance()
                    .setType(DynamicThemeDialog.Type.THEME_IMPORT)
                    .setOnImportThemeListener(
                            new DynamicThemeDialog.OnThemeImportListener() {
                                @Override
                                public void onImportTheme(@NonNull String theme) {
                                    importTheme(theme, file);
                                }
                            }).showDialog(getActivity());
        }
    }

    /**
     * Try to import the supplied theme string.
     *
     * @param theme The theme string to be imported.
     * @param file {@code true} if importing from the file.
     **/
    protected void importTheme(@Nullable String theme, boolean file) {
        if (theme == null || !DynamicThemeUtils.isValidTheme(theme)) {
            invalidTheme(file);
        } else {
            try {
                mSettingsChanged = false;
                loadTheme(onImportTheme(theme));

                getDynamicActivity().getSnackBar(
                        R.string.ads_theme_import_done).show();
                getDynamicActivity().setBottomSheetState(
                        BottomSheetBehavior.STATE_EXPANDED);
            } catch (Exception ignored) {
                invalidTheme(file);
            }
        }
    }

    /**
     * Show dialog for the invalid theme.
     *
     * @param file {@code true} if imported from the file.
     *
     * @see DynamicThemeDialog
     * @see DynamicThemeDialog.Type#THEME_INVALID
     */
    protected void invalidTheme(final boolean file) {
        DynamicThemeDialog.newInstance()
                .setType(DynamicThemeDialog.Type.THEME_INVALID)
                .setBuilder(new DynamicDialog.Builder(getContext())
                        .setPositiveButton(R.string.ads_backup_import,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        importTheme(file);
                                    }
                                })).showDialog(getActivity());
    }

    /**
     * Set the theme and finish this activity.
     */
    public void saveThemeSettings() {
        Intent intent = new Intent();
        intent.putExtra(DynamicIntent.EXTRA_THEME,
                mThemePreview.getDynamicTheme().toJsonString());
        setResult(Activity.RESULT_OK, intent);

        finishActivity();
    }
}
