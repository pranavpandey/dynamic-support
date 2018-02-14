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

package com.pranavpandey.android.dynamic.support.picker.color;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.picker.DynamicPickerType;

/**
 * A color picker dialog fragment to display multiple grids of colors
 * and their shades. It will be used internally by the
 * {@link com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference}
 * but can be used by the other views also.
 */
public class DynamicColorDialog extends DynamicDialogFragment {

    /**
     * State key to save the currently selected color so that it can
     * be restored later.
     */
    private static final String ADS_STATE_PICKER_COLOR = "ads_state_picker_color";

    /**
     * State key to save the color picker type so that it can be
     * restored later.
     */
    private static final String ADS_STATE_PICKER_TYPE = "ads_state_picker_type";

    /**
     * State key to save the color picker control so that it can be
     * restored later.
     */
    private static final String ADS_STATE_PICKER_CONTROL = "ads_state_picker_control";

    /**
     * Color picker dialog type to maintain its state.
     */
    private @DynamicPickerType
    int mType;

    /**
     * Color picker dialog control to maintain its state.
     */
    private @DynamicColorControl int mControl;

    /**
     * Color entries used by the picker.
     */
    private Integer[] mColors;

    /**
     * Shade entries used by the picker.
     */
    private Integer[][] mShades;

    /**
     * The selected color.
     */
    private @ColorInt int mSelectedColor;

    /**
     * Shape of the color swatches.
     *
     * @see DynamicColorShape
     */
    private @DynamicColorShape int mColorShape;

    /**
     * {@code true} to enable alpha for the custom color.
     */
    private boolean mAlpha;

    /**
     * Color listener to get the selected color.
     */
    private DynamicColorsAdapter.OnColorSelectedListener mOnColorSelectedListener;

    /**
     * The color picker used by this dialog.
     */
    private DynamicColorPicker mDynamicColorPicker;

    /**
     * Initialize the new instance of this fragment.
     *
     * @return A instance of {@link DynamicColorDialog}.
     */
    public static DynamicColorDialog newInstance() {
        return new DynamicColorDialog();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull final DynamicDialog.Builder dialogBuilder,
            @Nullable final Bundle savedInstanceState) {
        mDynamicColorPicker = new DynamicColorPicker(getContext());
        mControl = mDynamicColorPicker.getControl();

        if (savedInstanceState != null) {
            mSelectedColor = savedInstanceState.getInt(ADS_STATE_PICKER_COLOR);
            mType = savedInstanceState.getInt(ADS_STATE_PICKER_TYPE);
            mControl = savedInstanceState.getInt(ADS_STATE_PICKER_CONTROL);
        }

        mDynamicColorPicker.setColors(mColors, mShades);
        mDynamicColorPicker.setColorShape(mColorShape);
        mDynamicColorPicker.setAlpha(mAlpha);
        mDynamicColorPicker.setSelectedColor(mSelectedColor);
        mDynamicColorPicker.setType(mType);
        mDynamicColorPicker.setControl(mControl);
        mDynamicColorPicker.setOnColorSelectedListener(
                new DynamicColorsAdapter.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int position, int color) {
                dismiss();

                if (mOnColorSelectedListener != null) {
                    mOnColorSelectedListener.onColorSelected(position, color);
                }
            }
        });

        dialogBuilder.setNeutralButton(R.string.ads_custom,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .setPositiveButton(R.string.ads_select,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDynamicColorPicker.selectColor(-1,
                                mDynamicColorPicker.getSelectedColor());
                    }
                })
                .setNegativeButton(R.string.ads_cancel, null)
                .setView(mDynamicColorPicker)
                .setViewRoot(mDynamicColorPicker.getViewRoot());

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mDynamicColorPicker.update();
                if (savedInstanceState == null) {
                    showView(mDynamicColorPicker.getType());
                } else {
                    showView(mType);
                }

                getDynamicDialog().getButton(DynamicDialog.BUTTON_NEUTRAL)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showView(mType == DynamicPickerType.PRESETS
                                        ? DynamicPickerType.CUSTOM
                                        : DynamicPickerType.PRESETS);
                            }
                        });
            }
        });

        return dialogBuilder;
    }

    /**
     * Switch view according to the dialog type.
     */
    protected void showView(@DynamicPickerType int type) {
        if (type == DynamicPickerType.CUSTOM) {
            showCustom();
        } else {
            showPresets();
        }
    }

    /**
     * Show the presets view.
     */
    protected void showPresets() {
        mType = DynamicPickerType.PRESETS;
        getDynamicDialog().getButton(DynamicDialog.BUTTON_NEUTRAL).setText(R.string.ads_custom);
        mDynamicColorPicker.showPresets();
    }

    /**
     * Show the custom view.
     */
    protected void showCustom() {
        mType = DynamicPickerType.CUSTOM;
        getDynamicDialog().getButton(DynamicDialog.BUTTON_NEUTRAL)
                .setText(R.string.ads_picker_presets);
        mDynamicColorPicker.showCustom();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADS_STATE_PICKER_COLOR, mDynamicColorPicker.getSelectedColor());
        outState.putInt(ADS_STATE_PICKER_TYPE, mDynamicColorPicker.getType());
        outState.putInt(ADS_STATE_PICKER_CONTROL, mDynamicColorPicker.getControl());
    }

    /**
     * Getter for {@link #mColors}.
     */
    public Integer[] getColors() {
        return mColors;
    }

    /**
     * Getter for {@link #mShades}.
     */
    public Integer[][] getShades() {
        return mShades;
    }

    /**
     * Setter for {@link #mColors} and {@link #mShades}.
     *
     * @return {@link DynamicColorDialog} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicColorDialog setColors(@NonNull Integer[] mColors,
                                        @Nullable Integer[][] mShades) {
        this.mColors = mColors;
        this.mShades = mShades;

        return this;
    }

    /**
     * Getter for {@link #mOnColorSelectedListener}.
     */
    public @NonNull DynamicColorsAdapter.OnColorSelectedListener getOnColorSelectedListener() {
        return mOnColorSelectedListener;
    }

    /**
     * Setter for {@link #mOnColorSelectedListener}.
     *
     * @return {@link DynamicColorDialog} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicColorDialog setOnColorSelectedListener(
            @NonNull DynamicColorsAdapter.OnColorSelectedListener onItemClickListener) {
        this.mOnColorSelectedListener = onItemClickListener;

        return this;
    }

    /**
     * Getter for {@link #mSelectedColor}.
     */
    public @ColorInt int getSelectedColor() {
        return mSelectedColor;
    }

    /**
     * Setter for {@link #mSelectedColor}.
     *
     * @return {@link DynamicColorDialog} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicColorDialog setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;

        return this;
    }

    /**
     * Getter for {@link #mColorShape}.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Setter for {@link #mColorShape}.
     *
     * @return {@link DynamicColorDialog} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicColorDialog setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        return this;
    }

    /**
     * Getter for {@link #mAlpha}.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Setter for {@link #mAlpha}.
     */
    public DynamicColorDialog setAlpha(boolean alpha) {
        this.mAlpha = alpha;

        return this;
    }
}
