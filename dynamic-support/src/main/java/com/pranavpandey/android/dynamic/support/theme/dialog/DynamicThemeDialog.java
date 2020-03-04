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

package com.pranavpandey.android.dynamic.support.theme.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.utils.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.widget.Dynamic;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_IMPORT;
import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_INVALID;
import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_SELECT;
import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_SELECT_ALL;

/**
 * A dialog fragment to import a dynamic theme.
 */
public class DynamicThemeDialog extends DynamicDialogFragment {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicThemeDialog";

    /**
     * Interface to listen theme selection events.
     */
    public interface OnThemeSelectListener {

        /**
         * This method will be called on selecting the theme.
         *
         * @param theme The selected theme.
         */
        void onThemeSelect(@Theme int theme);

        /**
         * This method will be called on copying the theme.
         */
        void onThemeCopy();
    }

    /**
     * Interface to listen theme import events.
     */
    public interface OnThemeImportListener {

        /**
         * This method will be called on importing the theme.
         *
         * @param theme The theme string to be imported.
         */
        void onImportTheme(@NonNull String theme);
    }

    /**
     * Dialog type to show the layout accordingly.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { THEME_SELECT_ALL, THEME_SELECT, THEME_IMPORT, THEME_INVALID })
    public @interface Type {

        /**
         * Constant for the import theme selection dialog with all the themes.
         */
        int THEME_SELECT_ALL = -2;

        /**
         * Constant for the import theme selection dialog with day and night themes.
         */
        int THEME_SELECT = -1;

        /**
         * Constant for the import theme dialog.
         */
        int THEME_IMPORT = 0;

        /**
         * Constant for the invalid theme dialog.
         */
        int THEME_INVALID = 1;
    }

    /**
     * Key for dialog type to maintain its state.
     */
    private static final String ADS_STATE_DIALOG_TYPE = "ads_state_dialog_type";

    /**
     * Key for edit text string to maintain its state.
     */
    private static final String ADS_STATE_EDIT_TEXT_STRING = "state_edit_text_string";

    /**
     * Dialog type to show the layout accordingly.
     */
    private @Type int mType;

    /**
     * Optional message for the dialog.
     */
    private CharSequence mMessage;

    /**
     * Interface to listen theme selection events.
     */
    private OnThemeSelectListener mOnThemeSelectListener;

    /**
     * Interface to listen theme import events.
     */
    private OnThemeImportListener mOnThemeImportListener;

    /**
     * Edit text to get the theme data.
     */
    private EditText mEditText;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public @NonNull static DynamicThemeDialog newInstance() {
        return new DynamicThemeDialog();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder,
            @Nullable final Bundle savedInstanceState) {
        View view;
        TextView messageView = null;

        if (savedInstanceState != null) {
            mType = savedInstanceState.getInt(ADS_STATE_DIALOG_TYPE);
        }

        if (mType == THEME_SELECT_ALL || mType == THEME_SELECT) {
            view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_theme_select,
                    new LinearLayout(requireContext()), false);
            messageView = view.findViewById(R.id.ads_dialog_theme_select_message);

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

            if (mType == THEME_SELECT) {
                view.findViewById(R.id.ads_dialog_theme_select_app).setVisibility(View.GONE);
            }

            dialogBuilder.setPositiveButton(R.string.ads_copy,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onThemeCopy();
                }
            });
        } else if (mType == THEME_INVALID) {
            view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_general,
                    new LinearLayout(requireContext()), false);
            messageView = view.findViewById(R.id.ads_dialog_general_message);
            mMessage = getString(R.string.ads_theme_invalid);

            ((TextView) view.findViewById(R.id.ads_dialog_general_desc))
                    .setText(R.string.ads_theme_invalid_desc);
        } else {
            view = LayoutInflater.from(requireContext()).inflate(R.layout.ads_dialog_theme,
                    new LinearLayout(requireContext()), false);
            mEditText = view.findViewById(R.id.ads_dialog_theme_edit_text);

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updatePositiveButton(s);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            dialogBuilder.setPositiveButton(
                    R.string.ads_backup_import, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mOnThemeImportListener != null && mEditText.getText() != null) {
                                mEditText.getText().clearSpans();
                                mOnThemeImportListener.onImportTheme(
                                        mEditText.getText().toString());
                            }
                        }
                    });


            setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    if (savedInstanceState != null) {
                        mEditText.setText(savedInstanceState.getString(ADS_STATE_EDIT_TEXT_STRING));
                    } else {
                        updatePositiveButton(mEditText.getText().toString());
                    }

                    DynamicInputUtils.showSoftInput(mEditText);
                }
            });
        }

        Dynamic.set(messageView, mMessage);

        return dialogBuilder.setTitle(R.string.ads_theme)
                .setNegativeButton(R.string.ads_cancel, null)
                .setView(view).setViewRoot(view.findViewById(R.id.ads_dialog_theme));
    }

    /**
     * This method will be called on selecting the theme.
     *
     * @param theme The selected theme.
     */
    private void onThemeSelect(@Theme int theme) {
        if (mOnThemeSelectListener != null) {
            mOnThemeSelectListener.onThemeSelect(theme);
        }

        dismiss();
    }

    /**
     * This method will be called on copying the theme.
     */
    private void onThemeCopy() {
        if (mOnThemeSelectListener != null) {
            mOnThemeSelectListener.onThemeCopy();
        }

        dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADS_STATE_DIALOG_TYPE, mType);

        if (mEditText != null) {
            outState.putString(ADS_STATE_EDIT_TEXT_STRING, mEditText.getText().toString());
        }
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    private void updatePositiveButton(@Nullable CharSequence charSequence) {
        if (getDynamicDialog() != null) {
            getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE)
                    .setEnabled(charSequence != null
                            && DynamicThemeUtils.isValidTheme(charSequence.toString()));
        }
    }

    /**
     * Get the dialog type.
     *
     * @return The current dialog type.
     */
    public @Type int getType() {
        return mType;
    }

    /**
     * Set the dialog type.
     *
     * @param type The dialog type to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog setType(@Type int type) {
        this.mType = type;

        return this;
    }

    /**
     * Get the message used by this dialog.
     *
     * @return The message used by this dialog.
     */
    public @Nullable CharSequence getMessage() {
        return mMessage;
    }

    /**
     * Get the optional message for this dialog.
     *
     * @param message The message to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog setMessage(@Nullable CharSequence message) {
        this.mMessage = message;

        return this;
    }

    /**
     * Get the theme select listener.
     *
     * @return The theme select listener.
     */
    public @Nullable OnThemeSelectListener getOnSelectThemeListener() {
        return mOnThemeSelectListener;
    }

    /**
     * Set the theme select listener.
     *
     * @param onThemeSelectListener The theme select listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog setOnSelectThemeListener(
            @Nullable OnThemeSelectListener onThemeSelectListener) {
        this.mOnThemeSelectListener = onThemeSelectListener;

        return this;
    }

    /**
     * Get the theme import listener.
     *
     * @return The theme import listener.
     */
    public @Nullable OnThemeImportListener getOnImportThemeListener() {
        return mOnThemeImportListener;
    }

    /**
     * Set the theme import listener.
     *
     * @param onThemeImportListener The theme import listener to be set.
     *
     * @return The {@link DynamicThemeDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicThemeDialog setOnImportThemeListener(
            @Nullable OnThemeImportListener onThemeImportListener) {
        this.mOnThemeImportListener = onThemeImportListener;

        return this;
    }
}
