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

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SeekBar;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.picker.DynamicPickerType;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.setting.DynamicSeekBarCompact;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorPalette;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.DynamicEditText;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A color picker inside a FrameLayout to display multiple grids of
 * colors and their shades. It will be used internally by the
 * {@link com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference}
 * but can be used by the other views also.
 */
public class DynamicColorPicker extends FrameLayout {

    /**
     * Shared preference key to save the recently selected control
     * type.
     */
    private static final String ADS_PREF_COLOR_PICKER_CONTROL =
            "ads_pref_color_picker_control";

    /**
     * Shared preference key to save the recently selected colors
     * without alpha.
     */
    private static final String ADS_PREF_COLOR_PICKER_RECENTS =
            "ads_pref_color_picker_recents";

    /**
     * Shared preference key to save the recently selected colors
     * with alpha.
     */
    private static final String ADS_PREF_COLOR_PICKER_RECENTS_ALPHA =
            "ads_pref_color_picker_recents_alpha";

    /**
     * The maximum recent colors count.
     */
    private static final int ADS_COLOR_PICKER_RECENTS_MAX = 8;

    /**
     * Recents color splitter to separate different colors.
     */
    public static final String ADS_COLOR_PICKER_RECENTS_SPLIT = ",";

    /**
     * Color picker view type.
     */
    private @DynamicPickerType int mType;

    /**
     * Color picker view control.
     */
    private @DynamicColorControl int mControl;

    /**
     * Color entries used by this picker.
     */
    private Integer[] mColors;

    /**
     * Shade entries used by this picker.
     */
    private Integer[][] mShades;

    /**
     * Current shade entries used by this picker.
     */
    private Integer[] mShadesCurrent;

    /**
     * Recent entries used by this picker.
     */
    private Integer[] mRecents;

    /**
     * Recent array list to perform calculations.
     */
    private ArrayList<Integer> mRecentsList;

    /**
     * The previous color.
     */
    private @ColorInt int mPreviousColor;

    /**
     * The selected color.
     */
    private @ColorInt int mSelectedColor;

    /**
     * Hue for the HSV color space.
     */
    private float mHSVHue;

    /**
     * Saturation for the HSV color space.
     */
    private float mHSVSaturation;

    /**
     * Value for the HSV color space.
     */
    private float mHSVValue;

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
     * Root view of the color shades.
     */
    private ViewGroup mShadesView;

    /**
     * Grid view to display primary colors.
     */
    private GridView mColorsGridView;

    /**
     * Grid view to display shades of the primary colors.
     */
    private GridView mShadesGridView;

    /**
     * Grid view to display the recently selected colors.
     */
    private GridView mRecentsGridView;

    /**
     * Color view to display the previous color.
     */
    private DynamicColorView mPreviousColorView;

    /**
     * Color view to display the custom color.
     */
    private DynamicColorView mColorView;

    /**
     * Edit text to display the custom color string.
     */
    private DynamicEditText mEditText;

    /**
     * Button to show all the color controls
     */
    private Button mControlAll;

    /**
     * Button to show/hide HSV color controls.
     */
    private Button mControlHSV;

    /**
     * Button to show/hide RGB color controls.
     */
    private Button mControlRGB;

    /**
     * HSV color control view.
     */
    private ViewGroup mViewHSV;

    /**
     * RGB color control view.
     */
    private ViewGroup mViewRGB;

    /**
     * Seek bar used for the color hue.
     */
    private DynamicSeekBarCompact mSeekBarHue;

    /**
     * Seek bar used for the color saturation.
     */
    private DynamicSeekBarCompact mSeekBarSaturation;

    /**
     * Seek bar used for the color value.
     */
    private DynamicSeekBarCompact mSeekBarValue;

    /**
     * Seek bar used for the color alpha value.
     */
    private DynamicSeekBarCompact mSeekBarAlpha;

    /**
     * Seek bar used for the color red value.
     */
    private DynamicSeekBarCompact mSeekBarRed;

    /**
     * Seek bar used for the color green value.
     */
    private DynamicSeekBarCompact mSeekBarGreen;

    /**
     * Seek bar used for the color blue value.
     */
    private DynamicSeekBarCompact mSeekBarBlue;

    /**
     * Edit text watcher to update the values accordingly.
     */
    private TextWatcher mEditTextWatcher;

    /**
     * Seek bar listener for HSV color space to update the values
     * accordingly.
     */
    private SeekBar.OnSeekBarChangeListener mHSVSeekBarListener;

    /**
     * Seek bar listener for RGB color space to update the values
     * accordingly.
     */
    private SeekBar.OnSeekBarChangeListener mRGBSeekBarListener;

    /**
     * {@code true} if color picker is updating the custom color. It
     * will be used to disable any listeners during update process.
     */
    private boolean mUpdatingCustomColor;

    public DynamicColorPicker(@NonNull Context context) {
        this(context, null);
    }

    public DynamicColorPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public DynamicColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    /**
     * Initialize this view with default settings.
     */
    private void initialize() {
        inflate(getContext(), R.layout.ads_color_picker, this);

        mShadesView = findViewById(R.id.ads_color_picker_shades_root);
        mColorsGridView = findViewById(R.id.ads_color_picker_colors);
        mShadesGridView = findViewById(R.id.ads_color_picker_shades);
        mRecentsGridView = findViewById(R.id.ads_color_picker_recents);
        mPreviousColorView = findViewById(R.id.ads_color_picker_color_previous);
        mColorView = findViewById(R.id.ads_color_picker_color);
        mEditText = findViewById(R.id.ads_color_picker_edit);
        mControlAll = findViewById(R.id.ads_color_picker_button_all);
        mControlHSV = findViewById(R.id.ads_color_picker_button_hsv);
        mControlRGB = findViewById(R.id.ads_color_picker_button_rgb);
        mViewHSV = findViewById(R.id.ads_color_picker_view_hsv);
        mViewRGB = findViewById(R.id.ads_color_picker_view_rgb);
        mSeekBarHue = findViewById(R.id.ads_color_picker_seek_hue);
        mSeekBarSaturation = findViewById(R.id.ads_color_picker_seek_saturation);
        mSeekBarValue = findViewById(R.id.ads_color_picker_seek_value);
        mSeekBarAlpha = findViewById(R.id.ads_color_picker_seek_alpha);
        mSeekBarRed = findViewById(R.id.ads_color_picker_seek_red);
        mSeekBarGreen = findViewById(R.id.ads_color_picker_seek_green);
        mSeekBarBlue = findViewById(R.id.ads_color_picker_seek_blue);

        mSeekBarRed.setColor(Color.RED);
        mSeekBarGreen.setColor(Color.GREEN);
        mSeekBarBlue.setColor(Color.BLUE);

        mPreviousColorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedColor(mPreviousColorView.getColor());
                update();
            }
        });

        mEditTextWatcher =
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!mUpdatingCustomColor) {
                            try {
                                mSelectedColor = Color.parseColor("#" + s.toString());
                            } catch (IllegalArgumentException illegalArgumentException) {
                                mSelectedColor = Color.BLACK;
                            }

                            setARGBColor(mSelectedColor);
                            setHSVColor(mSelectedColor, true);
                        }

                        mColorView.setColor(mSelectedColor);
                        mEditText.setColor(mSelectedColor);
                    }

                    @Override
                    public void afterTextChanged(Editable s) { }
                };

        mHSVSeekBarListener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        if (!mUpdatingCustomColor && fromUser) {
                            mHSVHue = mSeekBarHue.getProgress();
                            mHSVSaturation = mSeekBarSaturation.getProgress() / 100f;
                            mHSVValue = mSeekBarValue.getProgress() / 100f;

                            if (mAlpha) {
                                setCustom(Color.HSVToColor(mSeekBarAlpha.getProgress(),
                                        new float[] { mHSVHue, mHSVSaturation, mHSVValue}),
                                        false, true);
                            } else {
                                setCustom(Color.HSVToColor(
                                        new float[] { mHSVHue, mHSVSaturation, mHSVValue }),
                                        false, true);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                };

        mRGBSeekBarListener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        if (!mUpdatingCustomColor && fromUser) {
                            if (mAlpha) {
                                setCustom(Color.argb(mSeekBarAlpha.getProgress(),
                                        mSeekBarRed.getProgress(),
                                        mSeekBarGreen.getProgress(),
                                        mSeekBarBlue.getProgress()), true, true);
                            } else {
                                setCustom(Color.rgb(mSeekBarRed.getProgress(),
                                        mSeekBarGreen.getProgress(),
                                        mSeekBarBlue.getProgress()), true, true);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                };

        mControlAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setControl(DynamicColorControl.ALL);
            }
        });

        mControlHSV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setControl(DynamicColorControl.HSV);
            }
        });

        mControlRGB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setControl(DynamicColorControl.RGB);
            }
        });

        mEditText.addTextChangedListener(mEditTextWatcher);
        mSeekBarHue.setOnSeekBarChangeListener(mHSVSeekBarListener);
        mSeekBarSaturation.setOnSeekBarChangeListener(mHSVSeekBarListener);
        mSeekBarValue.setOnSeekBarChangeListener(mHSVSeekBarListener);
        mSeekBarAlpha.setOnSeekBarChangeListener(mRGBSeekBarListener);
        mSeekBarRed.setOnSeekBarChangeListener(mRGBSeekBarListener);
        mSeekBarGreen.setOnSeekBarChangeListener(mRGBSeekBarListener);
        mSeekBarBlue.setOnSeekBarChangeListener(mRGBSeekBarListener);
        mSeekBarHue.setOnSeekBarControlListener(mHSVSeekBarListener);
        mSeekBarSaturation.setOnSeekBarControlListener(mHSVSeekBarListener);
        mSeekBarValue.setOnSeekBarControlListener(mHSVSeekBarListener);
        mSeekBarAlpha.setOnSeekBarControlListener(mRGBSeekBarListener);
        mSeekBarRed.setOnSeekBarControlListener(mRGBSeekBarListener);
        mSeekBarGreen.setOnSeekBarControlListener(mRGBSeekBarListener);
        mSeekBarBlue.setOnSeekBarControlListener(mRGBSeekBarListener);

        mUpdatingCustomColor = true;
        mPreviousColor = DynamicColorType.UNKNOWN;
        mColorShape = DynamicColorShape.CIRCLE;
        mType = DynamicPickerType.PRESETS;
        mControl = DynamicPreferences.getInstance().loadPrefs(
                ADS_PREF_COLOR_PICKER_CONTROL, DynamicColorControl.HSV);

        update();
    }

    /**
     * Load this view according to the supplied parameters.
     */
    public void update() {
        if (mPreviousColor != DynamicColorType.UNKNOWN) {
            mPreviousColorView.setColor(mPreviousColor);
            mPreviousColorView.setVisibility(VISIBLE);
        } else {
            mPreviousColorView.setVisibility(GONE);
        }

        if (mColors == null) {
            mColors = DynamicColorPalette.MATERIAL_COLORS;
        }

        if (mAlpha) {
            mEditText.setHint("FF123456");
            mSeekBarAlpha.setVisibility(VISIBLE);
        } else {
            mEditText.setHint("123456");
            mSeekBarAlpha.setVisibility(GONE);
        }

        mColorsGridView.setAdapter(new DynamicColorsAdapter(mColors,
                mSelectedColor, mColorShape, mAlpha,
                new DynamicColorsAdapter.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int position, int color) {
                if (mShades != null && position < mShades.length) {
                    setShades(position, color);
                }

                setCustom(color, true, true);
            }
        }));

        mRecents = getRecents();
        setCustom(mSelectedColor, true, true);
        setRecents(mSelectedColor);

        if (mColors != null && Arrays.asList(mColors).contains(mSelectedColor)) {
            setShades(Arrays.asList(mColors).indexOf(mSelectedColor), mSelectedColor);
        } else {
            initializeShades(true);
        }

        setControl(mControl);
    }

    /**
     * Initialize shades to find out the matching color palette.
     *
     * @param showCustom {@code true} to show the custom color view
     *                   if no match is found.
     */
    private void initializeShades(boolean showCustom) {
        if (mShades != null) {
            for (int i = 0; i < mShades.length; i++) {
                if (Arrays.asList(mShades[i]).contains(mSelectedColor)) {
                    setSelectedColor(mColorsGridView, mColors[i]);
                    setShades(i, mSelectedColor);
                    break;
                }

                if (showCustom && i == mShades.length - 1) {
                    showCustom();
                }
            }
        }
    }

    /**
     * Select the color and call
     * {@link DynamicColorsAdapter.OnColorSelectedListener#onColorSelected(int, int)}
     * method.
     *
     * @param position Position of the parent color.
     * @param color The selected color.
     */
    protected void selectColor(int position, @ColorInt int color) {
        if (mOnColorSelectedListener != null) {
            mSelectedColor = color;
            mOnColorSelectedListener.onColorSelected(position, color);

            saveToRecents(color);
        }
    }

    /**
     * Set color string according to the selected color.
     *
     * @param color The selected color.
     * @param setHSV {@code true} to set HSV vales.
     * @param setRGB {@code true} to set RGB values.
     */
    protected void setCustom(@ColorInt int color, boolean setHSV, boolean setRGB) {
        mUpdatingCustomColor = true;
        mSelectedColor = color;
        setPresets(color);

        mEditText.setText(DynamicColorUtils.getColorString(color, mAlpha, false));
        mEditText.setSelection(mEditText.getText().length());
        mSeekBarAlpha.setProgress(Color.alpha(color));

        if (setRGB) {
            setARGBColor(color);
        }

        setHSVColor(color, setHSV);

        mSeekBarHue.setColor(Color.HSVToColor(new float[] { mHSVHue, 1f, 1f }));
        mSeekBarSaturation.setColor(
                Color.HSVToColor(new float[] { mHSVHue, mHSVSaturation, 1f }));
        mSeekBarValue.setColor(color);
        mUpdatingCustomColor = false;
    }

    /**
     * Set ARGB values according to the selected color.
     *
     * @param color The selected color.
     */
    private void setARGBColor(@ColorInt int color) {
        mSeekBarRed.setProgress(Color.red(color));
        mSeekBarGreen.setProgress(Color.green(color));
        mSeekBarBlue.setProgress(Color.blue(color));
    }

    /**
     * Set HSV values according to the selected color.
     *
     * @param color The selected color.
     */
    private void setHSVColor(@ColorInt int color, boolean setProgress) {
        float[] hsv = new float[3];
        Color.colorToHSV(DynamicColorUtils.removeAlpha(color), hsv);
        mHSVHue = hsv[0];
        mHSVSaturation = hsv[1] * 100;
        mHSVValue = hsv[2] * 100;

        if (setProgress) {
            mSeekBarHue.setProgress((int) hsv[0]);
            mSeekBarSaturation.setProgress((int) mHSVSaturation);
            mSeekBarValue.setProgress((int) mHSVValue);
        }
    }

    /**
     * Set selected color for the grid views containing colors.
     *
     * @param gridView The grid view to select the color.
     * @param color The color to be selected.
     */
    private void setSelectedColor(@NonNull GridView gridView, @ColorInt int color) {
        if (gridView.getAdapter() != null) {
            ((DynamicColorsAdapter) gridView.getAdapter()).setSelectedColor(color);
        }
    }

    /**
     * Set presets according to the selected color.
     *
     * @param color The selected color.
     */
    protected void setPresets(@ColorInt int color) {
        setSelectedColor(mColorsGridView, color);
        setSelectedColor(mShadesGridView, color);
        setSelectedColor(mRecentsGridView, color);

        if (mShadesCurrent != null) {
            if (!Arrays.asList(mShadesCurrent).contains(color)) {
                mShadesView.setVisibility(GONE);
            } else {
                setSelectedColor(mColorsGridView,
                        mColors[Arrays.asList(mShades).indexOf(mShadesCurrent)]);
            }
        }

        if (mShadesView.getVisibility() == GONE) {
            initializeShades(false);
        }
    }

    /**
     * Set shades according to the selected color.
     *
     * @param position Position of the parent color.
     * @param color The selected color.
     */
    protected void setShades(int position, @ColorInt int color) {
        if (mShades != null && position < mShades.length) {
            if (mShades[position] != null) {
                mShadesView.setVisibility(VISIBLE);
                mShadesCurrent = mShades[position];
                mShadesGridView.setAdapter(new DynamicColorsAdapter(mShadesCurrent,
                        color, mColorShape, mAlpha,
                        new DynamicColorsAdapter.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int position, int color) {
                        setCustom(color, true, true);
                    }
                }));
            }
        } else {
            mShadesView.setVisibility(GONE);
        }
    }

    /**
     * Set recents according to the selected color.
     *
     * @param color The selected color.
     */
    protected void setRecents(@ColorInt int color) {
        if (mRecents != null && mRecents.length > 0) {
            findViewById(R.id.ads_color_picker_recents_root).setVisibility(VISIBLE);
            mRecentsGridView.setAdapter(new DynamicColorsAdapter(mRecents,
                    color, mColorShape == DynamicColorShape.CIRCLE
                    ? DynamicColorShape.SQUARE : DynamicColorShape.CIRCLE, mAlpha,
                    new DynamicColorsAdapter.OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int position, int color) {
                            setCustom(color, true, true);
                        }
                    }));
        } else {
            findViewById(R.id.ads_color_picker_recents_root).setVisibility(GONE);
        }
    }

    /**
     * Show the presets view.
     */
    public void showPresets() {
        setType(DynamicPickerType.PRESETS);
        setPresets(mSelectedColor);

        findViewById(R.id.ads_color_picker_presets).setVisibility(VISIBLE);
        findViewById(R.id.ads_color_picker_custom).setVisibility(GONE);
    }

    /**
     * Show the custom view.
     */
    public void showCustom() {
        setType(DynamicPickerType.CUSTOM);
        setCustom(mSelectedColor, true, true);

        findViewById(R.id.ads_color_picker_presets).setVisibility(GONE);
        findViewById(R.id.ads_color_picker_custom).setVisibility(VISIBLE);
    }

    private void updateCustomControls() {
        if (mViewHSV.getVisibility() == VISIBLE && mViewRGB.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(WidgetDefaults.ADS_ALPHA_ENABLED);
            mControlHSV.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
            mControlRGB.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
        } else if (mViewHSV.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
            mControlHSV.setAlpha(WidgetDefaults.ADS_ALPHA_ENABLED);
            mControlRGB.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
        } else if (mViewRGB.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
            mControlHSV.setAlpha(WidgetDefaults.ADS_ALPHA_UNCHECKED);
            mControlRGB.setAlpha(WidgetDefaults.ADS_ALPHA_ENABLED);
        }
    }

    /**
     * Get the root view of this picker. It will be used internally
     * to add separators in the dialog.
     *
     * @return The root view of this picker.
     */
    public @NonNull View getViewRoot() {
        return findViewById(R.id.ads_color_picker);
    }

    /**
     * @return The color picker view type.
     */
    public @DynamicPickerType int getType() {
        return mType;
    }

    /**
     * Set the color picker view type.
     *
     * @param type The color picker view type to be set.
     */
    public void setType(@DynamicPickerType int type) {
        this.mType = type;
    }

    /**
     * @return The color picker view control.
     */
    public @DynamicColorControl int getControl() {
        return mControl;
    }

    /**
     * Set the color picker view control.
     *
     * @param control The color picker view control to
     *                be set.
     */
    public void setControl(@DynamicColorControl int control) {
        this.mControl = control;
        DynamicPreferences.getInstance().savePrefs(ADS_PREF_COLOR_PICKER_CONTROL, mControl);

        switch (mControl) {
            case DynamicColorControl.ALL:
                mViewHSV.setVisibility(VISIBLE);
                mViewRGB.setVisibility(VISIBLE);
                break;
            case DynamicColorControl.HSV:
                mViewHSV.setVisibility(VISIBLE);
                mViewRGB.setVisibility(GONE);
                break;
            case DynamicColorControl.RGB:
                mViewHSV.setVisibility(GONE);
                mViewRGB.setVisibility(VISIBLE);
                break;
        }

        updateCustomControls();
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
     */
    public void setColors(@NonNull @ColorInt Integer[] colors,
                          @Nullable @ColorInt Integer[][] shades) {
        this.mColors = colors;
        this.mShades = shades;
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
     */
    public void setPreviousColor(@ColorInt int previousColor) {
        this.mPreviousColor = previousColor;
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
     */
    public void setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;
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
     */
    public void setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;
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
     */
    public void setAlpha(boolean alpha) {
        this.mAlpha = alpha;
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
     */
    public void setOnColorSelectedListener(
            @NonNull DynamicColorsAdapter.OnColorSelectedListener onColorSelectedListener) {
        this.mOnColorSelectedListener = onColorSelectedListener;
    }

    /**
     * Save the selected color to recents list.
     *
     * @param color The selected color.
     */
    protected void saveToRecents(@ColorInt int color) {
        if (color == DynamicTheme.ADS_THEME_AUTO) {
            return;
        }

        mRecentsList = new ArrayList<>();
        mRecents = getRecents();

        if (mRecents != null) {
            mRecentsList.addAll(Arrays.asList(mRecents));
        }

        if (!mRecentsList.contains(color)) {
            mRecentsList.add(0, color);
        } else {
            mRecentsList.remove(mRecentsList.indexOf(color));
            mRecentsList.add(0, color);
        }

        if (mRecentsList.size() > ADS_COLOR_PICKER_RECENTS_MAX) {
            List<Integer> mRecentsTemp = mRecentsList.subList(0, ADS_COLOR_PICKER_RECENTS_MAX);

            mRecentsList = new ArrayList<>();
            mRecentsList.addAll(mRecentsTemp);
        }

        StringBuilder recents = new StringBuilder();
        for (int i = 0; i < mRecentsList.size(); i++) {
            recents.append(mRecentsList.get(i)).append(ADS_COLOR_PICKER_RECENTS_SPLIT);
        }

        if (!mAlpha) {
            DynamicPreferences.getInstance().savePrefs(
                    ADS_PREF_COLOR_PICKER_RECENTS, recents.toString());
        } else {
            DynamicPreferences.getInstance().savePrefs(
                    ADS_PREF_COLOR_PICKER_RECENTS_ALPHA, recents.toString());
        }
    }

    /**
     * @return The saved recent colors from the shared preferences.
     */
    protected Integer[] getRecents() {
        String recentsString;
        if (mAlpha) {
            recentsString = DynamicPreferences.getInstance().loadPrefs(
                    ADS_PREF_COLOR_PICKER_RECENTS_ALPHA, null);
        } else {
            recentsString = DynamicPreferences.getInstance().loadPrefs(
                    ADS_PREF_COLOR_PICKER_RECENTS, null);
        }

         if (recentsString != null) {
             String[] recentsTemp = recentsString.split(ADS_COLOR_PICKER_RECENTS_SPLIT);
             Integer[] recents = new Integer[recentsTemp.length];
             for (int i = 0; i < recentsTemp.length; i++) {
                 recents[i] = Integer.valueOf(recentsTemp[i]);
             }

             return recents;
         }

         return null;
    }
}
