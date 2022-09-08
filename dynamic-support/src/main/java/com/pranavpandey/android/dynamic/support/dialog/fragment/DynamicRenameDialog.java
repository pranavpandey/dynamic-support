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

package com.pranavpandey.android.dynamic.support.dialog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.util.DynamicInputUtils;

/**
 * A dialog fragment to provide the basic rename functionality.
 */
public class DynamicRenameDialog extends DynamicDialogFragment {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicRenameDialog";

    /**
     * State key to save the allow empty.
     */
    private static final String STATE_ALLOW_EMPTY = "state_allow_empty";

    /**
     * State key to save the edit text string.
     */
    private static final String STATE_EDIT_TEXT_STRING = "state_edit_text_string";

    /**
     * Interface to listen the rename event.
     */
    public interface RenameListener {

        /**
         * This method will be called when the positive button is clicked after entering
         * the new name.
         *
         * @param newName The entered new name.
         */
        void onRename(@NonNull String newName);
    }

    /**
     * Interface to listen the rename event.
     */
    private RenameListener mRenameListener;

    /**
     * {@code true} to allow the empty string.
     */
    private boolean mAllowEmpty;

    /**
     * The current name used by this dialog.
     */
    private String mName;

    /**
     * The name helper text used by this dialog.
     */
    private String mHelperText;

    /**
     * Text view to show the optional message.
     */
    private TextView mMessage;

    /**
     * Edit text to get the new name.
     */
    private EditText mEditText;

    /**
     * Initialize the new instance of this dialog fragment.
     *
     * @return An instance of {@link DynamicRenameDialog}.
     */
    public static @NonNull DynamicRenameDialog newInstance() {
        return new DynamicRenameDialog();
    }

    @Override
    public @LayoutRes int getLayoutRes() {
        return R.layout.ads_dialog_rename;
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder,
            final @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(getLayoutRes(),
                new LinearLayout(requireContext()), false);

        mMessage = view.findViewById(R.id.ads_dialog_rename_message);
        mEditText = view.findViewById(R.id.ads_dialog_rename_edit_text);

        if (!TextUtils.isEmpty(getHelperText())) {
            ((TextInputLayout) view.findViewById(
                    R.id.ads_dialog_rename_input_layout)).setHelperText(getHelperText());
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (getDynamicDialog() == null) {
                    return;
                }

                getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE).setEnabled(
                        (mAllowEmpty || !TextUtils.isEmpty(s)) && (TextUtils.isEmpty(mName)
                                || !s.toString().equals(mName)));
            }
        });

        dialogBuilder.setPositiveButton(R.string.ads_rename,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mRenameListener != null) {
                    mRenameListener.onRename(mEditText.getText().toString());
                }
            }
        })
        .setNegativeButton(R.string.ads_cancel, null)
        .setView(view)
        .setViewRoot(view.findViewById(R.id.ads_dialog_rename_root));

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Dynamic.set(mMessage, mName);

                if (savedInstanceState != null) {
                    mAllowEmpty = savedInstanceState.getBoolean(STATE_ALLOW_EMPTY);
                    mEditText.setText(savedInstanceState.getString(STATE_EDIT_TEXT_STRING));
                    mEditText.setSelection(mEditText.getText().length());
                } else {
                    mEditText.setText(mName);
                }

                if (mEditText.getText().toString().equals(mName)) {
                    mEditText.selectAll();
                    DynamicInputUtils.showSoftInput(mEditText);
                }
            }
        });

        return dialogBuilder;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_EDIT_TEXT_STRING, mEditText.getText().toString());
        outState.putBoolean(STATE_ALLOW_EMPTY, mAllowEmpty);
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    /**
     * Get the current name used by this dialog.
     *
     * @return The current name used by this dialog.
     */
    public @Nullable String getName() {
        return mName;
    }

    /**
     * Set the current name for this dialog.
     *
     * @param name The current name to be set.
     *
     * @return The {@link DynamicRenameDialog} object to allow for chaining of calls to set
     *         methods.
     */
    public @NonNull DynamicRenameDialog setName(@Nullable String name) {
        this.mName = name;

        return this;
    }

    /**
     * Get the name helper text used by this dialog.
     *
     * @return The name helper text used by this dialog.
     */
    public @Nullable String getHelperText() {
        return mHelperText;
    }

    /**
     * Set the name helper text for this dialog.
     *
     * @param helperText The name helper text to be set.
     *
     * @return The {@link DynamicRenameDialog} object to allow for chaining of calls to set
     *         methods.
     */
    public @NonNull DynamicRenameDialog setHelperText(@Nullable String helperText) {
        this.mHelperText = helperText;

        return this;
    }

    /**
     * Returns whether the empty string is allowed.
     *
     * @return {@code true} if the empty string is allowed.
     */
    public boolean isAllowEmpty() {
        return mAllowEmpty;
    }

    /**
     * Set whether the empty string is allowed for this dialog.
     *
     * @param allowEmpty {@code true} to allow the empty string.
     *
     * @return The {@link DynamicRenameDialog} object to allow for chaining of calls to set
     *         methods.
     */
    public @NonNull DynamicRenameDialog setAllowEmpty(boolean allowEmpty) {
        this.mAllowEmpty = allowEmpty;

        return this;
    }

    /**
     * Get the rename listener used by this dialog.
     *
     * @return The rename listener used by this dialog.
     */
    public @Nullable RenameListener getRenameDialogListener() {
        return mRenameListener;
    }

    /**
     * Set the rename listener for this dialog.
     *
     * @param renameListener The rename listener to be set.
     *
     * @return The {@link DynamicRenameDialog} object to allow for chaining of calls to set
     *         methods.
     */
    public @NonNull DynamicRenameDialog setRenameDialogListener(
            @Nullable RenameListener renameListener) {
        this.mRenameListener = renameListener;

        return this;
    }
}
