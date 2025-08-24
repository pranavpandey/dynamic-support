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

package com.pranavpandey.android.dynamic.support.theme.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicTaskViewModel;
import com.pranavpandey.android.dynamic.support.theme.listener.ThemeListener;
import com.pranavpandey.android.dynamic.support.theme.task.ThemeImportTask;
import com.pranavpandey.android.dynamic.support.util.DynamicInputUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;

import static com.pranavpandey.android.dynamic.theme.Theme.Action.IMPORT_FILE;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.INVALID;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SAVE_CODE;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SAVE_FILE;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SELECT;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SELECT_ALL;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SELECT_APP;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SHARE;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SHARE_CODE;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SHARE_DATA;
import static com.pranavpandey.android.dynamic.theme.Theme.Action.SHARE_FILE;

/**
 * A dialog fragment to import a dynamic theme.
 *
 * @param <T> The type of the dynamic app theme this dialog will handle.
 * @param <V> The type of the theme source.
 */
public class DynamicThemeDialog<T extends DynamicAppTheme, V> extends DynamicDialogFragment {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicThemeDialog";

    /**
     * Key for dialog type to maintain its state.
     */
    private static final String ADS_STATE_DIALOG_TYPE = "ads_state_dialog_type";

    /**
     * Key for edit text string to maintain its state.
     */
    private static final String ADS_STATE_EDIT_TEXT_STRING = "state_edit_text_string";

    /**
     * Theme action to show the layout accordingly.
     */
    private @Theme.Action int mThemeAction;

    /**
     * Custom title for the dialog.
     */
    private CharSequence mTitle;

    /**
     * Optional message for the dialog.
     */
    private CharSequence mMessage;

    /**
     * Text view to show the message.
     */
    private TextView mMessageView = null;

    /**
     * Interface to listen the dynamic theme events.
     */
    private ThemeListener<T> mThemeListener;

    /**
     * Interface to listen the theme code events.
     */
    private ThemeListener.Code mThemeCodeListener;

    /**
     * Interface to listen the theme import (from file) events.
     */
    private ThemeListener.Import.File<V> mThemeImportFileListener;

    /**
     * Interface to listen the theme selection events.
     */
    private ThemeListener.Select mThemeSelectListener;

    /**
     * Edit text to get the theme data.
     */
    private EditText mEditText;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public static @NonNull DynamicThemeDialog<DynamicAppTheme, Void> newInstance() {
        return new DynamicThemeDialog<>();
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param <T> The type of the dynamic app theme.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public static @NonNull <T extends DynamicAppTheme>
    DynamicThemeDialog<T, Void> newThemeInstance() {
        return new DynamicThemeDialog<>();
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param <T> The type of the dynamic app theme.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public static @NonNull <T extends DynamicAppTheme>
    DynamicThemeDialog<T, Intent> newImportIntentInstance() {
        return new DynamicThemeDialog<>();
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param <T> The type of the dynamic app theme.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public static @NonNull <T extends DynamicAppTheme>
    DynamicThemeDialog<T, Uri> newImportUriInstance() {
        return new DynamicThemeDialog<>();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder,
            final @Nullable Bundle savedInstanceState) {
        dialogBuilder.setTitle(R.string.ads_theme)
                .setNegativeButton(R.string.ads_cancel, null);

        View view;

        if (savedInstanceState != null) {
            mThemeAction = savedInstanceState.getInt(ADS_STATE_DIALOG_TYPE);
        }

        switch (mThemeAction) {
            case SELECT_ALL:
            case SELECT_APP:
            case SELECT:
                view = LayoutInflater.from(requireContext()).inflate(
                        R.layout.ads_dialog_theme_select,
                        new LinearLayout(requireContext()), false);
                dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_theme_select_root));
                mMessageView = view.findViewById(R.id.ads_dialog_theme_select_message);

                view.findViewById(R.id.ads_dialog_theme_select_app).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onThemeSelect(Theme.APP);
                            }
                        });
                view.findViewById(R.id.ads_dialog_theme_select_day).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onThemeSelect(Theme.DAY);
                            }
                        });
                view.findViewById(R.id.ads_dialog_theme_select_night).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onThemeSelect(Theme.NIGHT);
                            }
                        });
                view.findViewById(R.id.ads_dialog_theme_select_remote).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onThemeSelect(Theme.REMOTE);
                            }
                        });

                Dynamic.setVisibility(view.findViewById(R.id.ads_dialog_theme_select_remote),
                        mThemeAction == SELECT_ALL ? View.VISIBLE : View.GONE);
                Dynamic.setVisibility(view.findViewById(R.id.ads_dialog_theme_select_app),
                        mThemeAction == SELECT ? View.GONE : View.VISIBLE);

                dialogBuilder.setPositiveButton(R.string.ads_copy,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onThemeCopy();
                    }
                });
                break;
            case INVALID:
                view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_general,
                        new LinearLayout(requireContext()), false);
                dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_general_root));
                mMessageView = view.findViewById(R.id.ads_dialog_general_message);
                mMessage = getString(R.string.ads_theme_invalid);

                ((TextView) view.findViewById(R.id.ads_dialog_general_desc))
                        .setText(R.string.ads_theme_invalid_desc);
                break;
            case SHARE_CODE:
                if (mThemeCodeListener != null) {
                    view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_image,
                            new LinearLayout(requireContext()), false);
                    dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_image_root));
                    mMessageView = view.findViewById(R.id.ads_dialog_image_message);
                    mMessage = mThemeCodeListener.getThemeData();

                    Dynamic.set(view.findViewById(R.id.ads_dialog_image_view),
                            DynamicBitmapUtils.getBitmap(requireContext(),
                                    mThemeCodeListener.getThemeCode()));

                    dialogBuilder.setPositiveButton(R.string.ads_nav_share,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DynamicLinkUtils.share(requireActivity(),
                                            getString(R.string.ads_theme),
                                            mThemeCodeListener.getThemeData(),
                                            mThemeCodeListener.getThemeCode());
                                }
                            });
                    break;
                }
            case SHARE:
            case SHARE_DATA:
            case SHARE_FILE:
            case SAVE_FILE:
            case SAVE_CODE:
                view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_progress,
                        new LinearLayout(requireContext()), false);
                dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_progress_root));
                mMessageView = view.findViewById(R.id.ads_dialog_progress_message);

                Dynamic.setAppBarProgressVisible(getActivity(), true);

                setCancelable(false);
                dialogBuilder.setNegativeButton(null, null);

                setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (mThemeListener == null || mThemeListener.getThemePreview() == null) {
                            dismiss();
                            return;
                        }

                        mMessageView.setText(getString(mThemeAction == SHARE_CODE
                                || mThemeAction == SAVE_CODE ? R.string.ads_theme_code
                                : R.string.ads_nav_share));

                        if (mThemeListener instanceof ThemeListener.Export) {
                            if (mThemeAction == SAVE_FILE) {
                                mMessageView.setText(getString(R.string.ads_save));
                            }

                            new ViewModelProvider(DynamicThemeDialog.this).get(
                                    DynamicTaskViewModel.class).execute(
                                            ((ThemeListener.Export<T>) mThemeListener)
                                                    .getThemeExportTask(dialog, mThemeAction,
                                                            mThemeListener.getThemePreview()));
                        }
                    }
                });
                break;
            case IMPORT_FILE:
                view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_progress,
                        new LinearLayout(requireContext()), false);
                dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_progress_root));
                mMessageView = view.findViewById(R.id.ads_dialog_progress_message);
                mMessage = getString(R.string.ads_import);

                setCancelable(false);
                dialogBuilder.setNegativeButton(null, null);

                setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (mThemeImportFileListener == null) {
                            dismiss();
                            return;
                        }

                        new ViewModelProvider(DynamicThemeDialog.this).get(
                                DynamicTaskViewModel.class).execute(
                                        new ThemeImportTask<V>(getActivity(), mThemeAction,
                                                new ThemeListener.Import.File<V>() {
                                            @Override
                                            public @Nullable V getThemeSource() {
                                                return mThemeImportFileListener.getThemeSource();
                                            }

                                            @Override
                                            public void onImportTheme(@Nullable String theme) {
                                                dismiss();

                                                mThemeImportFileListener.onImportTheme(theme);
                                            }
                                        }) {
                        });
                    }
                });
                break;
            default:
                view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_theme,
                        new LinearLayout(requireContext()), false);
                dialogBuilder.setViewRoot(view.findViewById(R.id.ads_dialog_theme_root));
                mEditText = view.findViewById(R.id.ads_dialog_theme_edit_text);

                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s,
                            int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) { }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updatePositiveButton(s);
                    }
                });

                dialogBuilder.setPositiveButton(R.string.ads_import,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mThemeListener instanceof ThemeListener.Import
                                && mEditText.getText() != null) {
                            mEditText.getText().clearSpans();
                            ((ThemeListener.Import<?>) mThemeListener)
                                    .importTheme(mEditText.getText().toString());
                        }
                    }
                });

                setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (savedInstanceState != null) {
                            mEditText.setText(savedInstanceState
                                    .getString(ADS_STATE_EDIT_TEXT_STRING));
                        } else {
                            updatePositiveButton(mEditText.getText().toString());
                        }

                        DynamicInputUtils.showSoftInput(mEditText);
                    }
                });
                break;
        }

        if (mTitle != null) {
            dialogBuilder.setTitle(mTitle);
        }
        Dynamic.set(mMessageView, mMessage);

        return dialogBuilder.setView(view);
    }

    /**
     * This method will be called on selecting the theme.
     *
     * @param theme The selected theme.
     */
    private void onThemeSelect(@Theme int theme) {
        if (mThemeSelectListener != null) {
            mThemeSelectListener.onThemeSelect(theme);
        }

        dismiss();
    }

    /**
     * This method will be called on copying the theme.
     */
    private void onThemeCopy() {
        if (mThemeSelectListener != null) {
            mThemeSelectListener.onThemeCopy();
        }

        dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        new ViewModelProvider(DynamicThemeDialog.this).get(
                DynamicTaskViewModel.class).cancel(true);

        outState.putInt(ADS_STATE_DIALOG_TYPE, mThemeAction);

        if (mEditText != null) {
            outState.putString(ADS_STATE_EDIT_TEXT_STRING, mEditText.getText().toString());
        }
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        Dynamic.setAppBarProgressVisible(getActivity(), false);
    }

    private void updatePositiveButton(@Nullable CharSequence charSequence) {
        if (getDynamicDialog() != null) {
            getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE)
                    .setEnabled(charSequence != null
                            && DynamicThemeUtils.isValidTheme(charSequence.toString()));
        }
    }

    /**
     * Get the theme action used by this dialog.
     *
     * @return The theme action used by this dialog.
     */
    public @Theme.Action int getThemeAction() {
        return mThemeAction;
    }

    /**
     * Set the theme action for this dialog.
     *
     * @param themeAction The theme action to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setThemeAction(@Theme.Action int themeAction) {
        this.mThemeAction = themeAction;

        return this;
    }

    /**
     * Get the custom title used by this dialog.
     *
     * @return The custom title used by this dialog.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the custom title for this dialog.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        return this;
    }

    /**
     * Get the optional message used by this dialog.
     *
     * @return The optional message used by this dialog.
     */
    public @Nullable CharSequence getMessage() {
        return mMessage;
    }

    /**
     * Set the optional message for this dialog.
     *
     * @param message The message to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setMessage(@Nullable CharSequence message) {
        this.mMessage = message;

        return this;
    }

    /**
     * Get the dynamic theme listener used by this dialog.
     *
     * @return The dynamic theme listener used by this dialog.
     */
    public @Nullable ThemeListener<?> getThemeListener() {
        return mThemeListener;
    }

    /**
     * Set the dynamic theme listener for this dialog.
     *
     * @param themeListener The dynamic theme listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setThemeListener(
            @Nullable ThemeListener<T> themeListener) {
        this.mThemeListener = themeListener;

        return this;
    }

    /**
     * Get the theme code listener used by this dialog.
     *
     * @return The theme code listener used by this dialog.
     */
    public @Nullable ThemeListener.Code getThemeCodeListener() {
        return mThemeCodeListener;
    }

    /**
     * Set the theme code listener for this dialog.
     *
     * @param themeCodeListener The theme code listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setThemeCodeListener(
            @Nullable ThemeListener.Code themeCodeListener) {
        this.mThemeCodeListener = themeCodeListener;

        return this;
    }

    /**
     * Get the theme import (from file) listener used by this dialog.
     *
     * @return The theme import (from file) listener used by this dialog.
     */
    public @Nullable ThemeListener.Import.File<V> getThemeImportFileListener() {
        return mThemeImportFileListener;
    }

    /**
     * Set the theme import (from file) listener for this dialog.
     *
     * @param themeImportFileListener The theme import (from file) listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setThemeImportFileListener(
            @Nullable ThemeListener.Import.File<V> themeImportFileListener) {
        this.mThemeImportFileListener = themeImportFileListener;

        return this;
    }

    /**
     * Get the theme select listener used by this dialog.
     *
     * @return The theme select listener used by this dialog.
     */
    public @Nullable ThemeListener.Select getThemeSelectListener() {
        return mThemeSelectListener;
    }

    /**
     * Set the theme select listener for this dialog.
     *
     * @param themeSelectListener The theme select listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog<T, V> setThemeSelectListener(
            @Nullable ThemeListener.Select themeSelectListener) {
        this.mThemeSelectListener = themeSelectListener;

        return this;
    }
}
