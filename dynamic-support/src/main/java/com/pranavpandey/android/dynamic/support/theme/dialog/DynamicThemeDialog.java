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

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.utils.DynamicInputUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;

import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_IMPORT;
import static com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog.Type.THEME_INVALID;

/**
 * A dialog fragment to import a dynamic theme.
 */
public class DynamicThemeDialog extends DynamicDialogFragment {

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
    @IntDef(value = { THEME_IMPORT, THEME_INVALID })
    public @interface Type {

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
     * Interface to listen theme import events.
     */
    private OnThemeImportListener mOnThemeImportListener;


    private EditText mEditText;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return An instance of {@link DynamicThemeDialog}.
     */
    public static DynamicThemeDialog newInstance() {
        return new DynamicThemeDialog();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder,
            @Nullable final Bundle savedInstanceState) {
        View view;

        if (savedInstanceState != null) {
            mType = savedInstanceState.getInt(ADS_STATE_DIALOG_TYPE);
        }

        if (mType == THEME_INVALID) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.ads_dialog_general, new LinearLayout(getContext()), false);

            ((TextView) view.findViewById(R.id.ads_dialog_general_message))
                    .setText(R.string.ads_theme_invalid);
            ((TextView) view.findViewById(R.id.ads_dialog_general_desc))
                    .setText(R.string.ads_theme_invalid_desc);
        } else {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.ads_dialog_theme, new LinearLayout(getContext()), false);
            mEditText = view.findViewById(R.id.ads_dialog_theme_edit_text);

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updatePositiveButton(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) { }
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

        dialogBuilder.setTitle(R.string.ads_theme)
                .setNegativeButton(R.string.ads_cancel, null)
                .setView(view).setViewRoot(view.findViewById(R.id.ads_dialog_theme));

        return dialogBuilder;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADS_STATE_DIALOG_TYPE, mType);

        if (mEditText != null) {
            outState.putString(ADS_STATE_EDIT_TEXT_STRING, mEditText.getText().toString());
        }
    }

    private void updatePositiveButton(@Nullable CharSequence charSequence) {
        if (getDynamicDialog() != null) {
            getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE)
                    .setEnabled(charSequence != null
                            && DynamicThemeUtils.isValidJson(charSequence.toString()));
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
    public DynamicThemeDialog setType(@Type int type) {
        this.mType = type;

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
    public DynamicThemeDialog setOnImportThemeListener(
            @Nullable OnThemeImportListener onThemeImportListener) {
        this.mOnThemeImportListener = onThemeImportListener;

        return this;
    }
}
