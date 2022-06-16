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

package com.pranavpandey.android.dynamic.support.theme.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicProgressDialog;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicTaskViewModel;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.activity.DynamicThemeActivity;
import com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog;
import com.pranavpandey.android.dynamic.support.theme.listener.ThemeListener;
import com.pranavpandey.android.dynamic.support.theme.task.ThemeExportTask;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.support.util.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicFileUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;
import com.pranavpandey.android.dynamic.util.concurrent.task.FileWriteTask;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the need.
 */
public abstract class ThemeFragment<T extends DynamicAppTheme> extends DynamicFragment
        implements ThemeListener.Import<T>, ThemeListener.Export<T> {

    /**
     *  Activity scene transition name for the theme preview.
     */
    public static final String ADS_NAME_THEME_PREVIEW = "ads_name:theme_preview";

    /**
     *  Activity scene transition name for the theme preview icon.
     */
    public static final String ADS_NAME_THEME_PREVIEW_ICON = ADS_NAME_THEME_PREVIEW + ":icon";

    /**
     *  Activity scene transition name for the theme preview action.
     */
    public static final String ADS_NAME_THEME_PREVIEW_ACTION = ADS_NAME_THEME_PREVIEW + ":action";

    /**
     * Constant to request the theme file location.
     */
    protected static final int REQUEST_THEME_LOCATION = 0;

    /**
     * Constant to request the theme file (code) location.
     */
    protected static final int REQUEST_THEME_CODE_LOCATION = 1;

    /**
     * Constant to request the theme import from file.
     */
    protected static final int REQUEST_THEME_IMPORT = 5;

    /**
     * Key for the background color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_BACKGROUND =
            "ads_pref_settings_theme_color_background";

    /**
     * Key for the tint background color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_BACKGROUND =
            "ads_pref_settings_theme_color_tint_background";

    /**
     * Key for the surface color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_SURFACE =
            "ads_pref_settings_theme_color_surface";

    /**
     * Key for the tint surface color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_SURFACE =
            "ads_pref_settings_theme_color_tint_surface";

    /**
     * Key for the primary color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_PRIMARY =
            "ads_pref_settings_theme_color_primary";

    /**
     * Key for the tint primary color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_PRIMARY =
            "ads_pref_settings_theme_color_tint_primary";

    /**
     * Key for the dark primary color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_PRIMARY_DARK =
            "ads_pref_settings_theme_color_primary_dark";

    /**
     * Key for the tint dark primary color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_PRIMARY_DARK =
            "ads_pref_settings_theme_color_tint_primary_dark";

    /**
     * Key for the accent color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_ACCENT =
            "ads_pref_settings_theme_color_accent";

    /**
     * Key for the tint accent color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_ACCENT =
            "ads_pref_settings_theme_color_tint_accent";

    /**
     * Key for the dark accent color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_ACCENT_DARK =
            "ads_pref_settings_theme_color_accent_dark";

    /**
     * Key for the tint dark accent color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_ACCENT_DARK =
            "ads_pref_settings_theme_color_tint_accent_dark";

    /**
     * Key for the error color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_ERROR =
            "ads_pref_settings_theme_color_error";

    /**
     * Key for the tint error color preference.
     */
    public static final String ADS_PREF_THEME_COLOR_TINT_ERROR =
            "ads_pref_settings_theme_color_tint_error";

    /**
     * Key for the primary text color preference.
     */
    public static final String ADS_PREF_THEME_TEXT_PRIMARY =
            "ads_pref_settings_theme_text_primary";

    /**
     * Key for the inverse primary text color preference.
     */
    public static final String ADS_PREF_THEME_TEXT_INVERSE_PRIMARY =
            "ads_pref_settings_theme_text_inverse_primary";

    /**
     * Key for the secondary text color preference.
     */
    public static final String ADS_PREF_THEME_TEXT_SECONDARY =
            "ads_pref_settings_theme_text_secondary";

    /**
     * Key for the inverse secondary text color preference.
     */
    public static final String ADS_PREF_THEME_TEXT_INVERSE_SECONDARY =
            "ads_pref_settings_theme_text_inverse_secondary";

    /**
     * Key for the font scale preference.
     */
    public static final String ADS_PREF_THEME_FONT_SCALE =
            "ads_pref_settings_theme_font_scale";

    /**
     * Key for the font scale alternate preference.
     */
    public static final String ADS_PREF_THEME_FONT_SCALE_ALT =
            "ads_pref_settings_theme_font_scale_alt";

    /**
     * Key for the corner size preference.
     */
    public static final String ADS_PREF_THEME_CORNER_SIZE =
            "ads_pref_settings_theme_corner_size";

    /**
     * Key for the corner size alternate preference.
     */
    public static final String ADS_PREF_THEME_CORNER_SIZE_ALT =
            "ads_pref_settings_theme_corner_size_alt";

    /**
     * Key for the background aware preference.
     */
    public static final String ADS_PREF_THEME_BACKGROUND_AWARE =
            "ads_pref_settings_theme_background_aware";

    /**
     * Key for the style preference.
     */
    public static final String ADS_PREF_THEME_STYLE =
            "ads_pref_settings_theme_style";

    /**
     * Dynamic app theme used by this fragment.
     */
    protected T mDynamicTheme;

    /**
     * Default dynamic app theme used by this fragment.
     */
    protected T mDynamicThemeDefault;

    /**
     * Temporary file uri to store the exported theme.
     */
    protected Uri mThemeExported;

    /**
     * {@code true} if the settings have been changed.
     */
    protected boolean mSettingsChanged;

    /**
     * Theme preview used by this fragment.
     */
    protected ThemePreview<T> mThemePreview;

    /**
     * Dialog fragment to show the progress.
     */
    protected DynamicDialogFragment mProgressDialog;

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        DynamicMenuUtils.forceMenuIcons(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.ads_menu_theme, menu);

        if (DynamicFileUtils.getTempDir(requireContext()) == null) {
            menu.findItem(R.id.ads_menu_theme_file).setVisible(false);
        } else if (!DynamicSdkUtils.is19()) {
            menu.findItem(R.id.ads_menu_theme_file_save).setVisible(false);
            menu.findItem(R.id.ads_menu_theme_file_code).setVisible(false);
            menu.findItem(R.id.ads_menu_theme_file_import).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.ads_menu_theme_data_copy) {
            DynamicTheme.getInstance().copyThemeString(getDynamicActivity(),
                    mThemePreview.getDynamicTheme().toDynamicString());
        } else if (i == R.id.ads_menu_theme_share) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE)
                    .setThemeListener(this)
                    .setMessage(getSubtitle())
                    .showDialog(requireActivity());
        } else if (i == R.id.ads_menu_theme_code) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE_CODE)
                    .setThemeListener(this)
                    .setMessage(getSubtitle())
                    .showDialog(requireActivity());
        } else if (i == R.id.ads_menu_theme_import) {
            importTheme(Theme.Action.IMPORT);
        } else if (i == R.id.ads_menu_theme_file_save) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SAVE_FILE)
                    .setThemeListener(this)
                    .setMessage(getSubtitle())
                    .showDialog(requireActivity());
        } else if (i == R.id.ads_menu_theme_file_code) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SAVE_CODE)
                    .setThemeListener(this)
                    .setMessage(getSubtitle())
                    .showDialog(requireActivity());
        } else if (i == R.id.ads_menu_theme_file_share) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE_FILE)
                    .setThemeListener(this)
                    .setMessage(getSubtitle())
                    .showDialog(requireActivity());
        } else if (i == R.id.ads_menu_theme_file_import) {
            importTheme(Theme.Action.IMPORT_FILE);
        } else if (i == R.id.ads_menu_refresh) {
            mSettingsChanged = false;

            onLoadTheme(mDynamicTheme);
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
        } else if (i == R.id.ads_menu_default) {
            mSettingsChanged = false;

            onLoadTheme(mDynamicThemeDefault);
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
            Dynamic.showSnackbar(getActivity(), R.string.ads_theme_reset_desc);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        final Uri uri = data != null ? data.getData() : null;

        switch (requestCode) {
            case REQUEST_THEME_LOCATION:
            case REQUEST_THEME_CODE_LOCATION:
                new ViewModelProvider(this).get(DynamicTaskViewModel.class).execute(
                        new FileWriteTask(requireContext(), mThemeExported, uri) {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                                setProgress(requestCode, true);
                            }

                            @Override
                            protected void onPostExecute(@Nullable DynamicResult<Boolean> result) {
                                super.onPostExecute(result);

                                setProgress(requestCode, false);

                                if (getBooleanResult(result)) {
                                    Dynamic.showSnackbar(getActivity(), String.format(
                                            getString(R.string.ads_theme_format_saved),
                                            DynamicFileUtils.getFileNameFromUri(
                                                    requireContext(), uri)));
                                } else {
                                    onThemeError(Theme.Action.SAVE_FILE, null, null);
                                }
                            }
                        });
                break;
            case REQUEST_THEME_IMPORT:
                DynamicThemeDialog.newImportUriInstance()
                        .setThemeAction(Theme.Action.IMPORT_FILE)
                        .setThemeImportFileListener(
                                new File<Uri>() {
                                    @Override
                                    public @Nullable Uri getThemeSource() {
                                        return uri;
                                    }

                                    @Override
                                    public void onImportTheme(@Nullable String theme) {
                                        importTheme(theme, Theme.Action.IMPORT_FILE);
                                    }
                                })
                        .setMessage(getSubtitle())
                        .showDialog(requireActivity());
                break;
        }
    }

    /**
     * Set progress for the theme tasks.
     *
     * @param requestCode The request code to be used.
     * @param visible {@code true} to make the progress visible.
     */
    protected void setProgress(int requestCode, boolean visible) {
        if (!visible && mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        } else if (visible) {
            switch (requestCode) {
                case REQUEST_THEME_LOCATION:
                case REQUEST_THEME_CODE_LOCATION:
                    mProgressDialog = DynamicProgressDialog.newInstance()
                            .setName(getString(R.string.ads_file))
                            .setBuilder(new DynamicDialog.Builder(requireContext())
                                    .setTitle(getString(R.string.ads_theme)));
                    mProgressDialog.showDialog(requireActivity());
                    break;
            }
        }
    }

    @Override
    public @NonNull ThemePreview<T> getThemePreview() {
        return mThemePreview;
    }

    @Override
    public void onThemeError(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, @Nullable Exception e) {
        switch (themeAction) {
            case Theme.Action.IMPORT_FILE:
                DynamicThemeDialog.<T>newThemeInstance()
                        .setThemeAction(Theme.Action.INVALID)
                        .setBuilder(new DynamicDialog.Builder(requireContext())
                                .setPositiveButton(R.string.ads_backup_import,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        importTheme(themeAction);
                                    }
                                })).showDialog(requireActivity());
                break;
            case Theme.Action.SHARE:
            case Theme.Action.SHARE_DATA:
            case Theme.Action.SHARE_FILE:
            case Theme.Action.SHARE_CODE:
                Dynamic.showSnackbar(getActivity(), R.string.ads_theme_share_error);
                break;
            case Theme.Action.SAVE_FILE:
            case Theme.Action.SAVE_CODE:
                Dynamic.showSnackbar(getActivity(), themePreview != null
                        ? R.string.ads_theme_share_error : R.string.ads_theme_export_error);
                break;
        }
    }

    @Override
    public void importTheme(@Theme.Action int themeAction) {
        if (themeAction == Theme.Action.IMPORT_FILE) {
            startActivityForResult(DynamicFileUtils.getFileSelectIntent(
                    Theme.MIME), REQUEST_THEME_IMPORT);
        } else {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.IMPORT)
                    .setThemeListener(this).showDialog(requireActivity());
        }
    }

    @Override
    public void importTheme(@Nullable String theme) {
        importTheme(theme, Theme.Action.IMPORT);
    }

    @Override
    public void importTheme(@Nullable String theme, @Theme.Action int themeAction) {
        if (theme == null || !DynamicThemeUtils.isValidTheme(theme)) {
            onThemeError(themeAction, getThemePreview(), null);
        } else {
            try {
                mSettingsChanged = false;

                onLoadTheme(onImportTheme(theme));
                Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
                Dynamic.showSnackbar(getActivity(), R.string.ads_theme_import_done);
            } catch (Exception e) {
                onThemeError(themeAction, getThemePreview(), e);
            }
        }
    }

    @Override
    public @Nullable Bitmap getThemeBitmap(
            @Nullable ThemePreview<T> themePreview, int themeAction) {
        if (themePreview == null) {
            return null;
        }

        return DynamicThemeUtils.createThemeBitmap(themePreview);
    }

    @Override
    public @NonNull DynamicTask<?, ?, ?> getThemeExportTask(
            @Nullable DialogInterface dialog, @Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview) {
        return new ThemeExportTask<T>(themeAction, this) {
            @Override
            protected void onPostExecute(@Nullable DynamicResult<Uri> result) {
                super.onPostExecute(result);

                if (dialog != null) {
                    dialog.dismiss();
                }

                if (getThemeListener() == null || getThemeListener().getThemePreview() == null) {
                    return;
                }

                if (!(result instanceof DynamicResult.Success)) {
                    return;
                }

                if (getThemeAction() == Theme.Action.SHARE_FILE) {
                    DynamicLinkUtils.share(requireActivity(),
                            getSubtitle() != null ? getSubtitle().toString() : null,
                            DynamicThemeUtils.getThemeUrl(getThemeListener().getThemePreview()
                                    .getDynamicTheme()), result.getData(), Theme.MIME);
                } else if (getThemeAction() == Theme.Action.SHARE_CODE) {
                    startActivity(DynamicIntent.getThemeShareIntent(requireActivity(),
                            DynamicThemeActivity.class, DynamicIntent.ACTION_THEME_SHARE,
                            getThemeListener().getThemePreview().getDynamicTheme().toJsonString(),
                            DynamicThemeUtils.getThemeUrl(getThemeListener()
                                    .getThemePreview().getDynamicTheme()), result.getData()));
                } else if (getThemeAction() == Theme.Action.SAVE_FILE) {
                    mThemeExported = result.getData();
                    startActivityForResult(DynamicFileUtils.getSaveToFileIntent(requireContext(),
                            result.getData(), Theme.MIME), REQUEST_THEME_LOCATION);
                } else if (getThemeAction() == Theme.Action.SAVE_CODE) {
                    mThemeExported = result.getData();
                    startActivityForResult(DynamicFileUtils.getSaveToFileIntent(requireContext(),
                            result.getData(), Theme.MIME_IMAGE), REQUEST_THEME_CODE_LOCATION);
                } else {
                    DynamicLinkUtils.share(requireActivity(),
                            getSubtitle() != null ? getSubtitle().toString() : null,
                            DynamicThemeUtils.getThemeUrl(getThemeListener().getThemePreview()
                                    .getDynamicTheme()), result.getData());
                }
            }
        };
    }

    /**
     * Set the theme and finish this activity.
     */
    public void saveThemeSettings() {
        Intent intent = new Intent();
        intent.putExtra(DynamicIntent.EXTRA_THEME, mThemePreview.getDynamicTheme().toJsonString());
        setResult(Activity.RESULT_OK, intent);
    }
}
