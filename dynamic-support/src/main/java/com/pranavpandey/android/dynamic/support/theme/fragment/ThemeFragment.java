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
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog;
import com.pranavpandey.android.dynamic.support.theme.listener.ThemeListener;
import com.pranavpandey.android.dynamic.support.theme.task.ThemeExportTask;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.support.util.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicFileUtils;
import com.pranavpandey.android.dynamic.util.DynamicIntentUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;
import com.pranavpandey.android.dynamic.util.concurrent.task.FileWriteTask;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the requirements.
 */
public abstract class ThemeFragment<T extends DynamicAppTheme> extends DynamicFragment
        implements ThemeListener.Value, ThemeListener.Import<T>, ThemeListener.Export<T> {

    /**
     * Dynamic app theme used by this fragment.
     */
    protected T mDynamicTheme;

    /**
     * Default dynamic app theme used by this fragment.
     */
    protected T mDynamicThemeDefault;

    /**
     * Temporary file URI to store the exported theme.
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

    /**
     * This method will be called to get the share title.
     *
     * @return The title for the share menu.
     */
    public @Nullable String getShareTitle() {
        return getString(R.string.ads_theme);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateMenu(menu, inflater);

        inflater.inflate(R.menu.ads_menu_theme, menu);
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        super.onPrepareMenu(menu);

        if (DynamicFileUtils.getTempDir(requireContext()) == null) {
            menu.findItem(R.id.ads_menu_theme_file).setVisible(false);
        } else if (!DynamicIntentUtils.isFilePicker(requireContext(), true)) {
            menu.findItem(R.id.ads_menu_theme_file_save).setVisible(false);
            menu.findItem(R.id.ads_menu_theme_file_code).setVisible(false);
        } else if (!DynamicIntentUtils.isFilePicker(requireContext())) {
            menu.findItem(R.id.ads_menu_theme_file_import).setVisible(false);
        }

        menu.findItem(R.id.ads_menu_theme_capture).setVisible(
                DynamicIntentUtils.isMatrixCaptureResult(requireContext()));
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.ads_menu_theme_data_copy) {
            DynamicTheme.getInstance().copyThemeString(getDynamicActivity(),
                    mThemePreview.getDynamicTheme().toDynamicString());
        } else if (itemId == R.id.ads_menu_theme_share) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE)
                    .setThemeListener(this)
                    .setMessage(getShareTitle())
                    .showDialog(requireActivity());
        } else if (itemId == R.id.ads_menu_theme_code) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE_CODE)
                    .setThemeListener(this)
                    .setMessage(getShareTitle())
                    .showDialog(requireActivity());
        } else if (itemId == R.id.ads_menu_theme_import) {
            importTheme(Theme.Action.IMPORT);
        } else if (itemId == R.id.ads_menu_theme_capture) {
            importTheme(Theme.Action.CAPTURE);
        } else if (itemId == R.id.ads_menu_theme_file_save) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SAVE_FILE)
                    .setThemeListener(this)
                    .setMessage(getShareTitle())
                    .showDialog(requireActivity());
        } else if (itemId == R.id.ads_menu_theme_file_code) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SAVE_CODE)
                    .setThemeListener(this)
                    .setMessage(getShareTitle())
                    .showDialog(requireActivity());
        } else if (itemId == R.id.ads_menu_theme_file_share) {
            DynamicThemeDialog.<T>newThemeInstance()
                    .setThemeAction(Theme.Action.SHARE_FILE)
                    .setThemeListener(this)
                    .setMessage(getShareTitle())
                    .showDialog(requireActivity());
        } else if (itemId == R.id.ads_menu_theme_file_import) {
            importTheme(Theme.Action.IMPORT_FILE);
        } else if (itemId == R.id.ads_menu_refresh) {
            mSettingsChanged = false;

            onLoadTheme(mDynamicTheme);
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
        } else if (itemId == R.id.ads_menu_default) {
            mSettingsChanged = false;

            onLoadTheme(mDynamicThemeDefault);
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
            Dynamic.showSnackbar(getActivity(), R.string.ads_theme_reset_desc);

            return true;
        }

        return super.onMenuItemSelected(item);
    }

    /**
     * Save theme according to the supplied parameters.
     *
     * @param requestCode The request code to be used.
     * @param file The file URI to be used.
     */
    protected void saveTheme(int requestCode, @Nullable Uri file) {
        new ViewModelProvider(this).get(DynamicTaskViewModel.class).execute(
                new FileWriteTask(requireContext(), mThemeExported, file) {
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
                                    DynamicFileUtils.getFileNameFromUri(requireContext(), file)));
                        } else {
                            onThemeError(Theme.Action.SAVE_FILE, null, null);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        final Uri uri = data != null ? data.getData() : null;

        switch (requestCode) {
            case REQUEST_THEME_LOCATION:
            case REQUEST_THEME_CODE_LOCATION:
                saveTheme(requestCode, uri);
                break;
            case REQUEST_THEME_IMPORT:
                DynamicThemeDialog.newImportUriInstance()
                        .setThemeAction(Theme.Action.IMPORT_FILE)
                        .setThemeImportFileListener(new File<Uri>() {
                            @Override
                            public @Nullable Uri getThemeSource() {
                                return uri;
                            }

                            @Override
                            public void onImportTheme(@Nullable String theme) {
                                importTheme(theme, Theme.Action.IMPORT_FILE);
                            }
                        })
                        .setMessage(getShareTitle())
                        .showDialog(requireActivity());
                break;
            case REQUEST_THEME_CAPTURE:
                importTheme(data != null ? data.getStringExtra(Theme.Intent.EXTRA_DATA_CAPTURE)
                        : null, Theme.Action.CAPTURE);
        }
    }

    /**
     * Set progress for the theme tasks.
     *
     * @param requestCode The request code to be used.
     * @param visible {@code true} to make the progress visible.
     */
    protected void setProgress(int requestCode, boolean visible) {
        if (mProgressDialog != null && mProgressDialog.isAdded()) {
            mProgressDialog.dismiss();
        }

        if (visible) {
            switch (requestCode) {
                case DynamicIntent.REQUEST_PREVIEW_LOCATION:
                case DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT:
                    Dynamic.setAppBarProgressVisible(getActivity(), true);

                    mProgressDialog = DynamicProgressDialog.newInstance()
                            .setName(getString(R.string.ads_file))
                            .setBuilder(new DynamicDialog.Builder(requireContext())
                                    .setTitle(getString(R.string.ads_save)));
                    mProgressDialog.showDialog(requireActivity());
                    break;
            }
        } else {
            Dynamic.setAppBarProgressVisible(getActivity(), false);
            mProgressDialog = null;
        }
    }

    /**
     * Try to resolve theme value from the slider preference.
     *
     * @param preference The slider preference to be used.
     * @param fallback The fallback value to be used in case of any issues.
     *
     * @return The resolved theme value from the slider preference.
     */
    public int getPreferenceValue(@Nullable DynamicSliderPreference preference, int fallback) {
        if (preference == null || preference.getPreferenceValue() == null) {
            return fallback;
        }

        return Theme.ToString.CUSTOM.equals(preference.getPreferenceValue())
                ? preference.getValueFromProgress()
                : Integer.parseInt(preference.getPreferenceValue());
    }

    /**
     * Try to resolve theme value from the spinner preference.
     *
     * @param preference The spinner preference to be used.
     * @param fallback The fallback value to be used in case of any issues.
     *
     * @return The resolved theme value from the spinner preference.
     */
    public int getPreferenceValue(@Nullable DynamicSpinnerPreference preference, int fallback) {
        if (preference == null || preference.getPreferenceValue() == null) {
            return fallback;
        }

        return Integer.parseInt(preference.getPreferenceValue());
    }

    @Override
    public int getFontScale() {
        return mDynamicTheme.getFontScale();
    }

    @Override
    public int getCornerSize() {
        return mDynamicTheme.getCornerSize();
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return mDynamicTheme.getBackgroundAware(false);
    }

    @Override
    public int getContrast() {
        return mDynamicTheme.getContrast();
    }

    @Override
    public int getOpacity() {
        return mDynamicTheme.getOpacity();
    }

    @Override
    public @Theme.Elevation int getElevation() {
        return mDynamicTheme.getElevation(false);
    }

    @Override
    public @Theme.Style int getStyle() {
        return mDynamicTheme.getStyle();
    }

    @Override
    public @NonNull ThemePreview<T> getThemePreview() {
        return mThemePreview;
    }

    @Override
    public void onThemeError(@Theme.Action int themeAction,
            @Nullable ThemePreview<T> themePreview, @Nullable Exception e) {
        switch (themeAction) {
            case Theme.Action.CAPTURE:
            case Theme.Action.IMPORT_FILE:
                DynamicThemeDialog.<T>newThemeInstance()
                        .setThemeAction(Theme.Action.INVALID)
                        .setBuilder(new DynamicDialog.Builder(requireContext())
                                .setPositiveButton(themeAction == Theme.Action.CAPTURE
                                                ? R.string.ads_theme_code_capture
                                                : R.string.ads_import,
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
        switch (themeAction) {
            case Theme.Action.CAPTURE:
                DynamicIntent.captureTheme(requireContext(), this, REQUEST_THEME_CAPTURE,
                        DynamicTheme.getInstance().get().toJsonString(true, true));
                break;
            case Theme.Action.IMPORT_FILE:
                DynamicPickerUtils.selectFile(requireContext(), this,
                        Theme.MIME_PICK, REQUEST_THEME_IMPORT);
                break;
            default:
                DynamicThemeDialog.<T>newThemeInstance()
                        .setThemeAction(Theme.Action.IMPORT)
                        .setThemeListener(this).showDialog(requireActivity());
                break;
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
                    onThemeError(Theme.Action.SAVE_FILE,
                            getThemeListener().getThemePreview(), null);
                    return;
                }

                if (getThemeAction() == Theme.Action.SHARE_FILE) {
                    DynamicLinkUtils.share(requireActivity(),
                            getShareTitle() != null ? getShareTitle() : null,
                            DynamicThemeUtils.getThemeUrl(getThemeListener().getThemePreview()
                                    .getDynamicTheme()), result.getData(), Theme.MIME);
                } else if (getThemeAction() == Theme.Action.SHARE_CODE) {
                    startActivity(DynamicIntent.getThemeShareIntent(requireActivity(),
                            DynamicTheme.getInstance().getPreviewActivity(),
                            DynamicIntent.ACTION_THEME_SHARE,
                            getThemeListener().getThemePreview().getDynamicTheme().toJsonString(),
                            getThemeListener().getThemePreview().getDynamicThemeType(),
                            getThemeListener().getThemePreview().getDynamicTheme().getThemeData(),
                            result.getData()));
                } else if (getThemeAction() == Theme.Action.SAVE_FILE) {
                    mThemeExported = result.getData();

                    final Uri file;
                    if ((file = DynamicPickerUtils.saveToFile(
                            requireContext(), ThemeFragment.this, mThemeExported,
                            Theme.MIME, REQUEST_THEME_LOCATION, true,
                            DynamicThemeUtils.getFileName(null, Theme.EXTENSION))) != null) {
                        saveTheme(ThemeListener.REQUEST_THEME_LOCATION, file);
                    } else if (!DynamicIntentUtils.isFilePicker(requireContext(), Theme.MIME)) {
                        onThemeError(Theme.Action.SAVE_FILE,
                                getThemeListener().getThemePreview(), null);
                    }
                } else if (getThemeAction() == Theme.Action.SAVE_CODE) {
                    mThemeExported = result.getData();

                    final Uri file;
                    if ((file = DynamicPickerUtils.saveToFile(
                            requireContext(), ThemeFragment.this, mThemeExported,
                            Theme.MIME_IMAGE, REQUEST_THEME_CODE_LOCATION, true,
                            DynamicThemeUtils.getFileName(Theme.EXTENSION_IMAGE))) != null) {
                        saveTheme(ThemeListener.REQUEST_THEME_CODE_LOCATION, file);
                    } else if (!DynamicIntentUtils.isFilePicker(
                            requireContext(), Theme.MIME_IMAGE)) {
                        onThemeError(Theme.Action.SAVE_CODE,
                                getThemeListener().getThemePreview(), null);
                    }
                } else {
                    DynamicLinkUtils.share(requireActivity(),
                            getShareTitle() != null ? getShareTitle() : null,
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
