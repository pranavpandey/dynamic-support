/*
 * Copyright 2018-2021 Pranav Pandey
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicSliderChangeListener;
import com.pranavpandey.android.dynamic.support.picker.DynamicPickerType;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;
import com.pranavpandey.android.dynamic.support.theme.task.WallpaperColorsTask;
import com.pranavpandey.android.dynamic.support.utils.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.support.view.base.DynamicView;
import com.pranavpandey.android.dynamic.theme.DynamicPalette;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicTaskUtils;
import com.pranavpandey.android.dynamic.utils.concurrent.DynamicResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A color picker inside a DynamicView to display multiple grids of colors and their shades.
 * <p>It will be used internally by the
 * {@link DynamicColorPreference}
 * but can be used by the other views also.
 */
public class DynamicColorPicker extends DynamicView {

    /**
     * Shared preference key to save the recently selected control type.
     */
    private static final String ADS_PREF_COLOR_PICKER_CONTROL = "ads_pref_color_picker_control";

    /**
     * Shared preference key to save the recently selected colors without alpha.
     */
    private static final String ADS_PREF_COLOR_PICKER_RECENTS = "ads_pref_color_picker_recents";

    /**
     * Shared preference key to save the recently selected colors with alpha.
     */
    private static final String ADS_PREF_COLOR_PICKER_RECENTS_ALPHA =
            "ads_pref_color_picker_recents_alpha";

    /**
     * Shared preference key to save the recently copied color.
     */
    public static final String ADS_PREF_COLOR_PICKER_RECENT = "ads_pref_color_picker_recent";

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
     * Dynamic entries used by this picker.
     */
    private Integer[] mDynamics;

    /**
     * Recent entries used by this picker.
     */
    private Integer[] mRecents;

    /**
     * Recent array list to perform calculations.
     */
    private List<Integer> mRecentsList;

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
    private DynamicColorListener mDynamicColorListener;

    /**
     * Edit text watcher to update the values accordingly.
     */
    private TextWatcher mEditTextWatcher;

    /**
     * Slider listener for HSV color space to update the values accordingly.
     */
    private DynamicSliderChangeListener<Slider> mHSVListener;

    /**
     * Slider listener for RGB color space to update the values accordingly.
     */
    private DynamicSliderChangeListener<Slider> mRGBListener;

    /**
     * Slider listener for CMYK color space to update the values accordingly.
     */
    private DynamicSliderChangeListener<Slider> mCMYKListener;

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
     * Grid view to display the dynamic colors.
     */
    private GridView mDynamicGridView;

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
    private EditText mEditText;

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
     * Button to show/hide CMYK color controls.
     */
    private Button mControlCMYK;

    /**
     * HSV color control view.
     */
    private ViewGroup mViewHSV;

    /**
     * RGB color control view.
     */
    private ViewGroup mViewRGB;

    /**
     * CMYK color control view.
     */
    private ViewGroup mViewCMYK;

    /**
     * Slider used for the color hue.
     */
    private DynamicSliderPreference mSliderHue;

    /**
     * Slider used for the color saturation.
     */
    private DynamicSliderPreference mSliderSaturation;

    /**
     * Slider used for the color value.
     */
    private DynamicSliderPreference mSliderValue;

    /**
     * Slider used for the color alpha value.
     */
    private DynamicSliderPreference mSliderAlpha;

    /**
     * Slider used for the color red value.
     */
    private DynamicSliderPreference mSliderRed;

    /**
     * Slider used for the color green value.
     */
    private DynamicSliderPreference mSliderGreen;

    /**
     * Slider used for the color blue value.
     */
    private DynamicSliderPreference mSliderBlue;

    /**
     * Slider used for the color cyan value.
     */
    private DynamicSliderPreference mSliderCyan;

    /**
     * Slider used for the color magenta value.
     */
    private DynamicSliderPreference mSliderMagenta;

    /**
     * Slider used for the color yellow value.
     */
    private DynamicSliderPreference mSliderYellow;

    /**
     * Slider used for the color black value.
     */
    private DynamicSliderPreference mSliderBlack;

    /**
     * Progress bar used by this view.
     */
    private ProgressBar mProgressBar;

    /**
     * {@code true} if color picker is updating the custom color.
     * <p>It will be used to disable any listeners during update process.
     */
    private boolean mUpdatingCustomColor;

    /**
     * Task to retrieve the wallpaper colors.
     */
    private WallpaperColorsTask mWallpaperColorsTask;

    public DynamicColorPicker(@NonNull Context context) {
        super(context);
    }

    public DynamicColorPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorPicker(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_color_picker;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mShadesView = findViewById(R.id.ads_color_picker_shades_root);
        mColorsGridView = findViewById(R.id.ads_color_picker_colors);
        mShadesGridView = findViewById(R.id.ads_color_picker_shades);
        mDynamicGridView = findViewById(R.id.ads_color_picker_dynamics);
        mRecentsGridView = findViewById(R.id.ads_color_picker_recents);
        mPreviousColorView = findViewById(R.id.ads_color_picker_color_previous);
        mColorView = findViewById(R.id.ads_color_picker_color);
        mEditText = findViewById(R.id.ads_color_picker_edit);
        mControlAll = findViewById(R.id.ads_color_picker_button_all);
        mControlHSV = findViewById(R.id.ads_color_picker_button_hsv);
        mControlCMYK = findViewById(R.id.ads_color_picker_button_cmyk);
        mControlRGB = findViewById(R.id.ads_color_picker_button_rgb);
        mViewHSV = findViewById(R.id.ads_color_picker_view_hsv);
        mViewRGB = findViewById(R.id.ads_color_picker_view_rgb);
        mViewCMYK = findViewById(R.id.ads_color_picker_view_cmyk);
        mSliderHue = findViewById(R.id.ads_color_picker_slider_hue);
        mSliderSaturation = findViewById(R.id.ads_color_picker_slider_saturation);
        mSliderValue = findViewById(R.id.ads_color_picker_slider_value);
        mSliderAlpha = findViewById(R.id.ads_color_picker_slider_alpha);
        mSliderRed = findViewById(R.id.ads_color_picker_slider_red);
        mSliderGreen = findViewById(R.id.ads_color_picker_slider_green);
        mSliderBlue = findViewById(R.id.ads_color_picker_slider_blue);
        mSliderCyan = findViewById(R.id.ads_color_picker_slider_cyan);
        mSliderMagenta = findViewById(R.id.ads_color_picker_slider_magenta);
        mSliderYellow = findViewById(R.id.ads_color_picker_slider_yellow);
        mSliderBlack = findViewById(R.id.ads_color_picker_slider_black);
        mProgressBar = findViewById(R.id.ads_color_picker_progress_bar);

        mSliderRed.setColor(Color.RED);
        mSliderGreen.setColor(Color.GREEN);
        mSliderBlue.setColor(Color.BLUE);
        mSliderCyan.setColor(Color.CYAN);
        mSliderMagenta.setColor(Color.MAGENTA);
        mSliderYellow.setColor(Color.YELLOW);
        mSliderBlack.setColor(Color.BLACK);

        mPreviousColorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedColor(mPreviousColorView.getColor());
                onUpdate();
            }
        });

        mEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                    int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mUpdatingCustomColor) {
                    try {
                        mSelectedColor = Color.parseColor("#" + s.toString());
                    } catch (IllegalArgumentException illegalArgumentException) {
                        mSelectedColor = Color.BLACK;
                    }

                    setARGBColor(mSelectedColor);
                    setCMYKColor(mSelectedColor);
                    setHSVColor(mSelectedColor, true);
                }

                mColorView.setColor(mSelectedColor);
                Dynamic.setColor(mEditText, DynamicColorUtils.removeAlpha(mSelectedColor));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        mHSVListener = new DynamicSliderChangeListener<Slider>() {
            @Override
            public void onStartTrackingTouch(@Nullable Slider slider) { }

            @Override
            public void onProgressChanged(@Nullable Slider slider, 
                    float progress, boolean fromUser) {
                if (!mUpdatingCustomColor && fromUser) {
                    mHSVHue = mSliderHue.getProgress();
                    mHSVSaturation = mSliderSaturation.getProgress() / 100f;
                    mHSVValue = mSliderValue.getProgress() / 100f;

                    if (mAlpha) {
                        setCustom(Color.HSVToColor(mSliderAlpha.getProgress(),
                                new float[] { mHSVHue, mHSVSaturation, mHSVValue }),
                                false, true, true);
                    } else {
                        setCustom(Color.HSVToColor(
                                new float[] { mHSVHue, mHSVSaturation, mHSVValue }),
                                false, true, true);
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(@Nullable Slider slider) { }
        };

        mRGBListener = new DynamicSliderChangeListener<Slider>() {
            @Override
            public void onStartTrackingTouch(@Nullable Slider slider) { }

            @Override
            public void onProgressChanged(@Nullable Slider slider, 
                    float progress, boolean fromUser) {
                if (!mUpdatingCustomColor && fromUser) {
                    if (mAlpha) {
                        setCustom(Color.argb(mSliderAlpha.getProgress(),
                                mSliderRed.getProgress(), mSliderGreen.getProgress(),
                                mSliderBlue.getProgress()), true, true, true);
                    } else {
                        setCustom(Color.rgb(mSliderRed.getProgress(),
                                mSliderGreen.getProgress(), mSliderBlue.getProgress()),
                                true, true, true);
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(@Nullable Slider slider) { }
        };

        mCMYKListener = new DynamicSliderChangeListener<Slider>() {
            @Override
            public void onStartTrackingTouch(@Nullable Slider slider) { }

            @Override
            public void onProgressChanged(@Nullable Slider slider, 
                    float progress, boolean fromUser) {
                if (!mUpdatingCustomColor && fromUser) {
                    setCustom(DynamicColorUtils.CMYKToRGB(mSliderCyan.getProgress(),
                            mSliderMagenta.getProgress(), mSliderYellow.getProgress(),
                            mSliderBlack.getProgress()), true, true, false);
                }
            }

            @Override
            public void onStopTrackingTouch(@Nullable Slider slider) { }
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

        mControlCMYK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setControl(DynamicColorControl.CMYK);
            }
        });

        mEditText.addTextChangedListener(mEditTextWatcher);
        mSliderHue.setDynamicSliderResolver(mHSVListener);
        mSliderSaturation.setDynamicSliderResolver(mHSVListener);
        mSliderValue.setDynamicSliderResolver(mHSVListener);
        mSliderAlpha.setDynamicSliderResolver(mRGBListener);
        mSliderRed.setDynamicSliderResolver(mRGBListener);
        mSliderGreen.setDynamicSliderResolver(mRGBListener);
        mSliderBlue.setDynamicSliderResolver(mRGBListener);
        mSliderCyan.setDynamicSliderResolver(mCMYKListener);
        mSliderMagenta.setDynamicSliderResolver(mCMYKListener);
        mSliderYellow.setDynamicSliderResolver(mCMYKListener);
        mSliderBlack.setDynamicSliderResolver(mCMYKListener);
        mSliderHue.setOnSliderControlListener(mHSVListener);
        mSliderSaturation.setOnSliderControlListener(mHSVListener);
        mSliderValue.setOnSliderControlListener(mHSVListener);
        mSliderAlpha.setOnSliderControlListener(mRGBListener);
        mSliderRed.setOnSliderControlListener(mRGBListener);
        mSliderGreen.setOnSliderControlListener(mRGBListener);
        mSliderBlue.setOnSliderControlListener(mRGBListener);
        mSliderCyan.setOnSliderControlListener(mCMYKListener);
        mSliderMagenta.setOnSliderControlListener(mCMYKListener);
        mSliderYellow.setOnSliderControlListener(mCMYKListener);
        mSliderBlack.setOnSliderControlListener(mCMYKListener);

        mUpdatingCustomColor = true;
        mPreviousColor = Theme.Color.UNKNOWN;
        mColorShape = DynamicColorShape.CIRCLE;
        mType = DynamicPickerType.PRESETS;
        mControl = DynamicPreferences.getInstance().load(
                ADS_PREF_COLOR_PICKER_CONTROL, DynamicColorControl.HSV);

        mWallpaperColorsTask = new WallpaperColorsTask(getContext()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Dynamic.setVisibility(findViewById(R.id.ads_color_picker_dynamics_root), GONE);
                Dynamic.setVisibility(mProgressBar, VISIBLE);
            }

            @Override
            protected void onPostExecute(@Nullable DynamicResult<List<Integer>> result) {
                super.onPostExecute(result);

                Dynamic.setVisibility(mProgressBar, GONE);

                if (mDynamicGridView == null || result == null
                        || result.getData() == null || result.getData().isEmpty()) {
                    return;
                }

                mDynamics = result.getData().toArray(new Integer[0]);
                setDynamics(mSelectedColor);
            }
        };
    }

    @Override
    public void onUpdate() {
        if (mPreviousColor != Theme.Color.UNKNOWN) {
            mPreviousColorView.setColor(mPreviousColor);
            Dynamic.setVisibility(mPreviousColorView, VISIBLE);
        } else {
            Dynamic.setVisibility(mPreviousColorView, GONE);
        }

        if (mColors == null) {
            mColors = DynamicPalette.MATERIAL_COLORS;
        }

        if (mAlpha) {
            mEditText.setHint("FF123456");
            Dynamic.setVisibility(mSliderAlpha, VISIBLE);
        } else {
            mEditText.setHint("123456");
            Dynamic.setVisibility(mSliderAlpha, GONE);
        }

        mColorsGridView.setAdapter(new DynamicColorsAdapter(mColors, mSelectedColor,
                mColorShape, mAlpha, Dynamic.getContrastWithColor(
                        mColorsGridView, Theme.Color.UNKNOWN), new DynamicColorListener() {
            @Override
            public void onColorSelected(@Nullable String tag, int position, int color) {
                if (mShades != null && position < mShades.length) {
                    setShades(position, color);
                }

                setCustom(color, true, true, true);
            }
        }));

        mRecents = loadRecents();
        setCustom(mSelectedColor, true, true, true);
        setDynamics(mSelectedColor);
        setRecents(mSelectedColor);

        if (mColors != null && Arrays.asList(mColors).contains(mSelectedColor)) {
            setShades(Arrays.asList(mColors).indexOf(mSelectedColor), mSelectedColor);
        } else {
            initializeShades(true);
        }

        setControl(mControl);

        if (mDynamics == null) {
            DynamicTaskUtils.executeTask(mWallpaperColorsTask);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        DynamicTaskUtils.cancelTask(mWallpaperColorsTask, true);
    }

    @Override
    public @Nullable View getBackgroundView() {
        return null;
    }

    /**
     * Initialize shades to find out the matching color palette.
     *
     * @param showCustom {@code true} to show the custom color view if no match is found.
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
     * Sets the selected color.
     *
     * @param position The position of the parent color.
     * @param color The selected color.
     */
    protected void selectColor(int position, @ColorInt int color) {
        if (mDynamicColorListener != null) {
            mSelectedColor = color;
            DynamicPickerUtils.setRecentColor(color);
            saveToRecents(color);

            mDynamicColorListener.onColorSelected(null, position, color);
        }
    }

    /**
     * Set the color string according to the selected color.
     *
     * @param color The selected color.
     * @param setHSV {@code true} to set HSV vales.
     * @param setRGB {@code true} to set RGB values.
     * @param setCMYK {@code true} to set CMYK values.
     */
    protected void setCustom(@ColorInt int color, boolean setHSV,
            boolean setRGB, boolean setCMYK) {
        mUpdatingCustomColor = true;
        mSelectedColor = color;
        setPresets(color);

        mEditText.setText(DynamicColorUtils.getColorString(color, mAlpha, false));
        mEditText.setSelection(mEditText.getText().length());
        mSliderAlpha.setProgress(Color.alpha(color));

        if (setRGB) {
            setARGBColor(color);
        }

        if (setCMYK) {
            setCMYKColor(color);
        }

        setHSVColor(color, setHSV);
        mUpdatingCustomColor = false;
    }

    /**
     * Set the ARGB values according to the selected color.
     *
     * @param color The selected color.
     */
    private void setARGBColor(@ColorInt int color) {
        mSliderRed.setProgress(Color.red(color));
        mSliderGreen.setProgress(Color.green(color));
        mSliderBlue.setProgress(Color.blue(color));
    }

    /**
     * Set the CMYK values according to the selected color.
     *
     * @param color The selected color.
     */
    private void setCMYKColor(@ColorInt int color) {
        float[] cmyk = new float[4];
        DynamicColorUtils.colorToCMYK(color, cmyk);

        mSliderCyan.setProgress(Math.round(cmyk[0] * 100));
        mSliderMagenta.setProgress(Math.round(cmyk[1] * 100));
        mSliderYellow.setProgress(Math.round(cmyk[2] * 100));
        mSliderBlack.setProgress(Math.round(cmyk[3] * 100));
    }

    /**
     * Set the HSV values according to the selected color.
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
            mSliderHue.setProgress(Math.round(hsv[0]));
            mSliderSaturation.setProgress(Math.round(mHSVSaturation));
            mSliderValue.setProgress(Math.round(mHSVValue));
        }

        mSliderHue.setColor(Color.HSVToColor(new float[] { mSliderHue.getProgress(), 1f, 1f }));
        mSliderSaturation.setColor(Color.HSVToColor(new float[] { mHSVHue, mHSVSaturation, 1f }));
        mSliderValue.setColor(color);
    }

    /**
     * Set the selected color for the grid views containing colors.
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
     * Set the presets according to the selected color.
     *
     * @param color The selected color.
     */
    protected void setPresets(@ColorInt int color) {
        setSelectedColor(mColorsGridView, color);
        setSelectedColor(mShadesGridView, color);
        setSelectedColor(mRecentsGridView, color);
        setSelectedColor(mDynamicGridView, color);

        if (mShadesCurrent != null) {
            if (!Arrays.asList(mShadesCurrent).contains(color)) {
                Dynamic.setVisibility(mShadesView, GONE);
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
     * Set the shades according to the selected color.
     *
     * @param position Position of the parent color.
     * @param color The selected color.
     */
    protected void setShades(int position, @ColorInt int color) {
        if (mShades != null && position < mShades.length) {
            if (mShades[position] != null) {
                Dynamic.setVisibility(mShadesView, VISIBLE);
                mShadesCurrent = mShades[position];
                mShadesGridView.setAdapter(new DynamicColorsAdapter(mShadesCurrent,
                        color, mColorShape, mAlpha, Dynamic.getContrastWithColor(
                                mShadesGridView, Theme.Color.UNKNOWN), new DynamicColorListener() {
                    @Override
                    public void onColorSelected(@Nullable String tag, int position, int color) {
                        setCustom(color, true, true, true);
                    }
                }));
            }
        } else {
            Dynamic.setVisibility(mShadesView, GONE);
        }
    }

    /**
     * Set the recents according to the selected color.
     *
     * @param color The selected color.
     */
    protected void setRecents(@ColorInt int color) {
        if (mRecents != null && mRecents.length > 0) {
            Dynamic.setVisibility(findViewById(R.id.ads_color_picker_recents_root), VISIBLE);
            mRecentsGridView.setAdapter(new DynamicColorsAdapter(mRecents, color,
                    mColorShape == DynamicColorShape.CIRCLE
                            ? DynamicColorShape.SQUARE : DynamicColorShape.CIRCLE, mAlpha,
                    Dynamic.getContrastWithColor(mRecentsGridView, Theme.Color.UNKNOWN),
                    new DynamicColorListener() {
                @Override
                public void onColorSelected(
                        @Nullable String tag, int position, int color) {
                    setCustom(color, true, true, true);
                }
            }));
        } else {
            Dynamic.setVisibility(findViewById(R.id.ads_color_picker_recents_root), GONE);
        }
    }

    /**
     * Set the dynamic colors according to the selected color.
     *
     * @param color The selected color.
     */
    protected void setDynamics(@ColorInt int color) {
        if (mDynamics != null && mDynamics.length > 0) {
            Dynamic.setVisibility(findViewById(R.id.ads_color_picker_dynamics_root), VISIBLE);
            mDynamicGridView.setAdapter(new DynamicColorsAdapter(mDynamics, color,
                    mColorShape == DynamicColorShape.CIRCLE
                            ? DynamicColorShape.SQUARE : DynamicColorShape.CIRCLE, mAlpha,
                    Dynamic.getContrastWithColor(mDynamicGridView, Theme.Color.UNKNOWN),
                    new DynamicColorListener() {
                @Override
                public void onColorSelected(
                        @Nullable String tag, int position, int color) {
                    setCustom(color, true, true, true);
                }
            }));
        } else {
            Dynamic.setVisibility(findViewById(R.id.ads_color_picker_dynamics_root), GONE);
        }
    }

    /**
     * Show the presets view.
     */
    public void showPresets() {
        setType(DynamicPickerType.PRESETS);
        setPresets(mSelectedColor);

        Dynamic.setVisibility(findViewById(R.id.ads_color_picker_presets), VISIBLE);
        Dynamic.setVisibility(findViewById(R.id.ads_color_picker_custom), GONE);
    }

    /**
     * Show the custom view.
     */
    public void showCustom() {
        setType(DynamicPickerType.CUSTOM);
        setCustom(mSelectedColor, true, true, true);

        Dynamic.setVisibility(findViewById(R.id.ads_color_picker_presets), GONE);
        Dynamic.setVisibility(findViewById(R.id.ads_color_picker_custom), VISIBLE);
    }

    private void updateCustomControls() {
        if (mViewHSV.getVisibility() == VISIBLE && mViewRGB.getVisibility() == VISIBLE
                && mViewCMYK.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(Defaults.ADS_ALPHA_ENABLED);
            mControlHSV.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlRGB.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlCMYK.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
        } else if (mViewHSV.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlHSV.setAlpha(Defaults.ADS_ALPHA_ENABLED);
            mControlRGB.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlCMYK.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
        } else if (mViewRGB.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlHSV.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlRGB.setAlpha(Defaults.ADS_ALPHA_ENABLED);
            mControlCMYK.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
        } else if (mViewCMYK.getVisibility() == VISIBLE) {
            mControlAll.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlHSV.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlRGB.setAlpha(Defaults.ADS_ALPHA_UNCHECKED);
            mControlCMYK.setAlpha(Defaults.ADS_ALPHA_ENABLED);
        }
    }

    /**
     * Get the root view of this picker.
     * <p>It will be used internally to add separators in the dialog.
     *
     * @return The root view of this picker.
     */
    public @NonNull View getViewRoot() {
        return findViewById(R.id.ads_color_picker);
    }

    /**
     * Get the color picker view type.
     *
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
     * Get the color picker view control.
     *
     * @return The color picker view control.
     */
    public @DynamicColorControl int getControl() {
        return mControl;
    }

    /**
     * Set the color picker view control.
     *
     * @param control The color picker view control to be set.
     */
    public void setControl(@DynamicColorControl int control) {
        this.mControl = control;
        DynamicPreferences.getInstance().save(ADS_PREF_COLOR_PICKER_CONTROL, mControl);

        switch (mControl) {
            case DynamicColorControl.ALL:
                Dynamic.setVisibility(mViewHSV, VISIBLE);
                Dynamic.setVisibility(mViewRGB, VISIBLE);
                Dynamic.setVisibility(mViewCMYK, VISIBLE);
                break;
            case DynamicColorControl.HSV:
                Dynamic.setVisibility(mViewHSV, VISIBLE);
                Dynamic.setVisibility(mViewRGB, GONE);
                Dynamic.setVisibility(mViewCMYK, GONE);
                break;
            case DynamicColorControl.RGB:
                Dynamic.setVisibility(mViewHSV, GONE);
                Dynamic.setVisibility(mViewRGB, VISIBLE);
                Dynamic.setVisibility(mViewCMYK, GONE);
                break;
            case DynamicColorControl.CMYK:
                Dynamic.setVisibility(mViewHSV, GONE);
                Dynamic.setVisibility(mViewRGB, GONE);
                Dynamic.setVisibility(mViewCMYK, VISIBLE);
                break;
        }

        updateCustomControls();
    }

    /**
     * Get the color entries used by the picker.
     *
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
    public void setColors(@NonNull Integer[] colors, @Nullable Integer[][] shades) {
        this.mColors = colors;
        this.mShades = shades;
    }

    /**
     * Get the dynamic color entries used by this picker.
     *
     * @return The dynamic color entries used by this picker.
     */
    public @Nullable Integer[] getDynamics() {
        return mDynamics;
    }

    /**
     * Set the dynamic color entries used by this picker.
     *
     * @param colors The color entries to be set.
     */
    public void setDynamics(@Nullable Integer[] colors) {
        this.mDynamics = colors;
    }

    /**
     * Get the recent entries used by this popup.
     *
     * @return The recent entries used by this popup.
     */
    public @Nullable Integer[] getRecents() {
        return mRecents;
    }

    /**
     * Get the previous color.
     *
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
     * Get the selected color.
     *
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
     * Ge the shape of the color swatches.
     *
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
     * Returns whether the color alpha is enabled
     *
     * @return {@code true} to enable the color alpha.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Set the alpha support for the color.
     *
     * @param alpha {@code true} to enable alpha.
     */
    public void setAlpha(boolean alpha) {
        this.mAlpha = alpha;
    }

    /**
     * Returns the color listener to get the selected color.
     *
     * @return The color listener to get the selected color.
     */
    public @NonNull DynamicColorListener getDynamicColorListener() {
        return mDynamicColorListener;
    }

    /**
     * Set the color listener to get the selected color.
     *
     * @param dynamicColorListener The listener to be set.
     */
    public void setDynamicColorListener(@NonNull DynamicColorListener dynamicColorListener) {
        this.mDynamicColorListener = dynamicColorListener;
    }

    /**
     * Save the selected color to recents list.
     *
     * @param color The selected color.
     */
    protected void saveToRecents(@ColorInt Integer color) {
        if (color == Theme.AUTO) {
            return;
        }

        mRecentsList = new ArrayList<Integer>();
        mRecents = loadRecents();

        if (mRecents != null) {
            mRecentsList.addAll(Arrays.asList(mRecents));
        }

        mRecentsList.remove(color);
        mRecentsList.add(0, color);

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
            DynamicPreferences.getInstance().save(
                    ADS_PREF_COLOR_PICKER_RECENTS, recents.toString());
        } else {
            DynamicPreferences.getInstance().save(
                    ADS_PREF_COLOR_PICKER_RECENTS_ALPHA, recents.toString());
        }
    }

    /**
     * Returns the saved recent colors from the shared preferences.
     *
     * @return The saved recent colors from the shared preferences.
     */
    protected @Nullable Integer[] loadRecents() {
        String recentsString;
        if (mAlpha) {
            recentsString = DynamicPreferences.getInstance().load(
                    ADS_PREF_COLOR_PICKER_RECENTS_ALPHA, null);
        } else {
            recentsString = DynamicPreferences.getInstance().load(
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
