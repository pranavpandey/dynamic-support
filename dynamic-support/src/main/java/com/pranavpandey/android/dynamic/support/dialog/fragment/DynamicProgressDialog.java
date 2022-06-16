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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;

/**
 * A dialog fragment to provide the basic progress functionality.
 */
public class DynamicProgressDialog extends DynamicDialogFragment {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "ProgressDialog";

    /**
     * The current name used by this dialog.
     */
    private String mName;

    /**
     * Text view to show the optional message.
     */
    private TextView mMessage;

    /**
     * Initialize the new instance of this dialog fragment.
     *
     * @return An instance of {@link DynamicProgressDialog}.
     */
    public static @NonNull DynamicProgressDialog newInstance() {
        return new DynamicProgressDialog();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder,
            final @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(
                R.layout.ads_dialog_progress, new LinearLayout(requireContext()), false);

        mMessage = view.findViewById(R.id.ads_dialog_progress_message);

        dialogBuilder.setCancelable(false)
                .setView(view)
                .setViewRoot(view.findViewById(R.id.ads_dialog_progress_root));

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mMessage.setText(mName);
            }
        });

        return dialogBuilder;
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
     * @return The {@link DynamicProgressDialog} object to allow for chaining of calls to set
     *         methods.
     */
    public @NonNull DynamicProgressDialog setName(@Nullable String name) {
        this.mName = name;

        return this;
    }
}
