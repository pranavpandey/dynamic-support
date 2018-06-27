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
     * State key to save the previously selected color so that it can
     * be restored later.
     */
    private static final String ADS_STATE_PICKER_PREVIOUS_COLOR =
            "ads_state_picker_previous_color";

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
    private @DynamicPickerType int mType;

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
     * The previous color.
     */
    private @ColorInt int mPreviousColor;

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
            mPreviousColor = savedInstanceState.getInt(ADS_STATE_PICKER_PREVIOUS_COLOR);
            mSelectedColor = savedInstanceState.getInt(ADS_STATE_PICKER_COLOR);
            mType = savedInstanceState.getInt(ADS_STATE_PICKER_TYPE);
            mControl = savedInstanceState.getInt(ADS_STATE_PICKER_CONTROL);
        }

        mDynamicColorPicker.setColors(mColors, mShades);
        mDynamicColorPicker.setColorShape(mColorShape);
        mDynamicColorPicker.setAlpha(mAlpha);
        mDynamicColorPicker.setPreviousColor(mPreviousColor);
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
     *
     * @param type The type for the dynamic color picker.
     *
     * @see DynamicPickerType
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

        outState.putInt(ADS_STATE_PICKER_PREVIOUS_COLOR, mDynamicColorPicker.getPreviousColor());
        outState.putInt(ADS_STATE_PICKER_COLOR, mDynamicColorPicker.getSelectedColor());
        outState.putInt(ADS_STATE_PICKER_TYPE, mDynamicColorPicker.getType());
        outState.putInt(ADS_STATE_PICKER_CONTROL, mDynamicColorPicker.getControl());
    }

    /**
     * @return The color entries used by the picker.
     */
    public Integer[] getColors() {
        return mColors;
    }

    /**
     * @return The shade entries used by the picker.
     */
    public Integer[][] getShades() {
        return mShades;
    }

    /**
     * Set the colors and their shades.
     *
     * @param colors The color entries to be set.
     * @param shades The shade entries to be set.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setColors(@NonNull @ColorInt Integer[] colors,
                                        @Nullable @ColorInt Integer[][] shades) {
        this.mColors = colors;
        this.mShades = shades;

        return this;
    }

    /**
     * @return The previous color.
     */
    public @ColorInt int getPreviousColor() {
        return mPreviousColor;
    }

    /**
     * Set the previous color.
     *
     * @param previousColor The previous color to be set.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setPreviousColor(@ColorInt int previousColor) {
        this.mPreviousColor = previousColor;

        return this;
    }

    /**
     * @return The selected color.
     */
    public @ColorInt int getSelectedColor() {
        return mSelectedColor;
    }

    /**
     * Set the selected color.
     *
     * @param selectedColor The color to be selected.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;

        return this;
    }

    /**
     * @return The shape of the color swatches.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Set the shape of the color swatches.
     *
     * @param colorShape The color shape to be set.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        return this;
    }

    /**
     * @return {@code true} to enable alpha for the custom color.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Set the alpha support for the custom color.
     *
     * @param alpha {@code true} to enable alpha.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setAlpha(boolean alpha) {
        this.mAlpha = alpha;

        return this;
    }

    /**
     * @return The color listener to get the selected color.
     */
    public @NonNull DynamicColorsAdapter.OnColorSelectedListener getOnColorSelectedListener() {
        return mOnColorSelectedListener;
    }

    /**
     * Set the color listener to get the selected color.
     *
     * @param onColorSelectedListener The listener to be set.
     *
     * @return The {@link DynamicColorDialog} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicColorDialog setOnColorSelectedListener(
            @NonNull DynamicColorsAdapter.OnColorSelectedListener onColorSelectedListener) {
        this.mOnColorSelectedListener = onColorSelectedListener;

        return this;
    }
}
