/*
 * Copyright 2017 Pranav Pandey
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

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;

import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;

/**
 * Base dialog fragment to provide all the functionality of
 * {@link DynamicDialog} inside a fragment. It can be extended to
 * customise it further by overriding the supported methods.
 *
 * @see #onCustomiseBuilder(DynamicDialog.Builder, Bundle)
 * @see #onCustomiseBuilder(DynamicDialog.Builder, Bundle)
 */
public class DynamicDialogFragment extends AppCompatDialogFragment {

    /**
     * Default button color. it will be used internally
     * if there is no button color is applied.
     */
    public static final int ADS_DEFAULT_BUTTON_COLOR = -1;

    /**
     * Button color used by this dialog fragment.
     */
    private @ColorInt int mButtonColor = ADS_DEFAULT_BUTTON_COLOR;

    /**
     * {@code true} to make the dialog cancelable.
     * The default value is {@code true}.
     */
    private boolean mIsCancelable = true;

    /**
     * {@code true} to dismiss the dialog in pause state.
     * The default value is {@code false}.
     */
    private boolean mAutoDismiss = false;

    /**
     * Dialog builder to customise this fragment according
     * to the need.
     */
    private DynamicDialog.Builder mDynamicDialogBuilder;

    /**
     * Callback when this dialog fragment is displayed.
     */
    private DynamicDialog.OnShowListener mOnShowListener;

    /**
     * Callback when this dialog fragment has been dismissed.
     */
    private DynamicDialog.OnDismissListener mOnDismissListener;

    /**
     * Callback when this dialog fragment has been cancelled.
     */
    private DynamicDialog.OnCancelListener mOnCancelListener;

    /**
     * Callback when a key is pressed in this dialog fragment.
     */
    private DynamicDialog.OnKeyListener mOnKeyListener;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return A instance of {@link DynamicDialogFragment}.
     */
    public static DynamicDialogFragment newInstance() {
        return new DynamicDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public @NonNull Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mDynamicDialogBuilder == null) {
            mDynamicDialogBuilder = new DynamicDialog.Builder(getContext());
        }

        final DynamicDialog alertDialog = onCustomiseBuilder(
                mDynamicDialogBuilder, savedInstanceState).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mButtonColor != ADS_DEFAULT_BUTTON_COLOR) {
                    if (alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setTextColor(mButtonColor);
                    }

                    if (alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(mButtonColor);
                    }

                    if (alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL) != null) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                                .setTextColor(mButtonColor);
                    }
                }

                if (mOnShowListener != null) {
                    mOnShowListener.onShow(getDialog());
                }
            }
        });

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface,
                                 int i, KeyEvent keyEvent) {
                if (mOnKeyListener != null) {
                    mOnKeyListener.onKey(dialogInterface, i, keyEvent);
                }

                return false;
            }
        });

        return onCustomiseDialog(alertDialog, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mAutoDismiss) {
            dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onDismiss(@Nullable DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(@Nullable DialogInterface dialog) {
        super.onCancel(dialog);

        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }

    /**
     * Override this method to customise the {@link #mDynamicDialogBuilder}
     * before creating the dialog.
     *
     * @param dialogBuilder The current builder to be customised.
     * @param savedInstanceState The saved state of the fragment to restore it
     *                           later.
     *
     * @return Customised {@link DynamicDialog.Builder}.
     */
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder, @Nullable Bundle savedInstanceState) {
        return dialogBuilder;
    }

    /**
     * Override this method to customise the {@link DynamicDialog}
     * before attaching it with this fragment.
     *
     * @param alertDialog The current dialog to be customised.
     * @param savedInstanceState The saved state of the fragment to restore it
     *                           later.
     *
     * @return Customised {@link DynamicDialog}.
     */
    protected @NonNull DynamicDialog onCustomiseDialog(@NonNull DynamicDialog alertDialog,
                                                       @Nullable Bundle savedInstanceState) {
        return alertDialog;
    }

    /**
     * Finish the parent activity by calling {@link Activity#finish()}.
     */
    protected void finishActivity() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            finishActivity();
        }
    }

    /**
     * @return The button color used by this dialog fragment.
     */
    public int getButtonColor() {
        return mButtonColor;
    }

    /**
     * Sets the button color for this dialog fragment.
     *
     * @param buttonColor The button color to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setButtonColor(@ColorInt int buttonColor) {
        this.mButtonColor = buttonColor;

        return this;
    }

    /**
     * @return {@code true} to make the dialog cancelable.
     *         The default value is {@code true}.
     */
    public boolean isCancelable() {
        return mIsCancelable;
    }

    /**
     * Sets this dialog fragment cancellable or not.
     *
     * @param cancelable {@code true} to make the dialog
     *                   cancelable.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setIsCancelable(boolean cancelable) {
        this.mIsCancelable = cancelable;
        setCancelable(cancelable);

        return this;
    }

    /**
     * @return {@code true} to dismiss the dialog in pause state.
     *         The default value is {@code false}.
     */
    protected boolean isAutoDismiss() {
        return mAutoDismiss;
    }

    /**
     * Sets this dialog fragment auto dismissible or not.
     *
     * @param autoDismiss {@code true} to dismiss the dialog in
     *                    pause state.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setAutoDismiss(boolean autoDismiss) {
        this.mAutoDismiss = autoDismiss;

        return this;
    }

    /**
     * @return The dialog builder to customise this fragment
     *         according to the need.
     */
    protected @Nullable DynamicDialog.Builder getBuilder() {
        return mDynamicDialogBuilder;
    }

    /**
     * Sets a dynamic dialog builder for this dialog fragment.
     *
     * @param dynamicAlertDialogBuilder The dynamic dialog builder
     *                                  to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setBuilder(
            @NonNull DynamicDialog.Builder dynamicAlertDialogBuilder) {
        this.mDynamicDialogBuilder = dynamicAlertDialogBuilder;

        return this;
    }

    /**
     * @return The callback when this dialog fragment is displayed.
     */
    protected @Nullable DynamicDialog.OnShowListener getOnShowListener() {
        return mOnShowListener;
    }

    /**
     * Sets on show listener for this dialog fragment.
     *
     * @param onShowListener The on show listener to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setOnShowListener(
            @Nullable DynamicDialog.OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;

        return this;
    }

    /**
     * @return The callback when this dialog fragment has been
     *         dismissed.
     */
    protected @Nullable DynamicDialog.OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    /**
     * Sets on dismiss listener for this dialog fragment.
     *
     * @param onDismissListener The on dismiss listener to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setOnDismissListener(
            @Nullable DynamicDialog.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;

        return this;
    }

    /**
     * @return The callback when this dialog fragment has been
     *         cancelled.
     */
    protected @Nullable DynamicDialog.OnCancelListener getOnCancelListener() {
        return mOnCancelListener;
    }

    /**
     * Sets on cancel listener for this dialog fragment.
     *
     * @param onCancelListener The on cancel listener to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setOnCancelListener(
            @Nullable DynamicDialog.OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;

        return this;
    }

    /**
     * @return The callback when a key is pressed in this dialog
     *         fragment.
     */
    protected @Nullable DynamicDialog.OnKeyListener getOnKeyListener() {
        return mOnKeyListener;
    }

    /**
     * Sets on key listener for this dialog fragment.
     *
     * @param onKeyListener The on key listener to be set.
     *
     * @return {@link DynamicDialogFragment} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicDialogFragment setOnKeyListener(
            @Nullable DynamicDialog.OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;

        return this;
    }

    /**
     * Show this dialog fragment and attach it to the supplied
     * activity.
     *
     * @param fragmentActivity The fragment activity to attach this
     *                         dialog fragment.
     */
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        show(fragmentActivity.getSupportFragmentManager(), getClass().getName());
    }

    /**
     * @return {@link DynamicDialog} created by this fragment.
     */
    public @Nullable DynamicDialog getDynamicDialog() {
        return (DynamicDialog) getDialog();
    }
}
