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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorDialog;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPopup;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorView;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorPalette;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;

/**
 * A DynamicPreference to implement the functionality of a color
 * picker by using the {@link DynamicColorPopup} and
 * {@link DynamicColorDialog}.
 */
public class DynamicColorPreference extends DynamicSimplePreference
        implements DynamicColorsAdapter.OnColorSelectedListener {

    /**
     * Resource id of the primary colors array.
     */
    private @ArrayRes int mColorsResId;

    /**
     * Resource id of the popup colors array.
     */
    private @ArrayRes int mPopupColorsResId;

    /**
     * Array to store the primary colors.
     */
    private @ColorInt Integer[] mColors;

    /**
     * Array to store the popup colors.
     */
    private @ColorInt Integer[] mPopupColors;

    /**
     * Array to store the shades of primary colors.
     */
    private @ColorInt Integer[][] mShades;

    /**
     * Shape for the {@link #mColorView}.
     *
     * @see DynamicColorShape
     */
    private @DynamicColorShape int mColorShape;

    /**
     * Default color for this preference.
     */
    private @ColorInt int mDefaultColor;

    /**
     * Current color value of this preference.
     */
    private @ColorInt int mColor;

    /**
     * {@code true} to show color popup before requesting for
     * the dialog.
     *
     * @see DynamicColorPopup
     * @see DynamicColorDialog
     * @see DynamicPreference.OnPromptListener
     */
    private boolean mShowColorPopup;

    /**
     * {@code true} to enable alpha component for the custom
     * color.
     */
    private boolean mAlpha;

    /**
     * Color view used by this preference to display the
     * selected color.
     */
    private DynamicColorView mColorView;

    public DynamicColorPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicColorPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorPreference(@NonNull Context context,
                                  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicPreference);
        TypedArray b = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorView);
        TypedArray c = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorPicker);

        try {
            mDefaultColor = a.getColor(
                    R.styleable.DynamicPreference_ads_dynamicPreference_color,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE);
            mShowColorPopup = a.getBoolean(
                    R.styleable.DynamicPreference_ads_dynamicPreference_popup,
                    false);
            mColorShape = b.getInt(
                    R.styleable.DynamicColorView_ads_dynamicColorView_shape,
                    DynamicColorShape.CIRCLE);
            mAlpha = c.getBoolean(
                    R.styleable.DynamicColorPicker_ads_dynamicColorPicker_alpha,
                    false);
            mColorsResId = c.getResourceId(
                    R.styleable.DynamicColorPicker_ads_dynamicColorPicker_colors,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
            mPopupColorsResId = c.getResourceId(
                    R.styleable.DynamicColorPicker_ads_dynamicColorPicker_popupColors,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
        } finally {
            a.recycle();
            b.recycle();
            c.recycle();
        }

        if (getPreferenceKey() != null) {
            mColor = DynamicPreferences.getInstance().loadPrefs(
                    getPreferenceKey(), mDefaultColor);
        }
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mColorView = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_color, this, false)
                .findViewById(R.id.ads_preference_color_view);
        setViewFrame(mColorView, true);

        getPreferenceView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnPromptListener() != null) {
                    if (mShowColorPopup) {
                        if (getOnPromptListener().onPopup()) {
                            showColorPopup(view);
                        }
                    } else {
                        if (getOnPromptListener().onDialog()) {
                            showColorDialog();
                        }
                    }
                } else {
                    if (mShowColorPopup) {
                        showColorPopup(view);
                    } else {
                        showColorDialog();
                    }
                }
            }
        });

        setColorShape(mColorShape);
        setAlpha(mAlpha, false);
        setColor(mColor, false);
    }

    /**
     * Show color popup to select a color.
     *
     * @param view The anchor view for the popup.
     *
     * @see DynamicColorPopup
     */
    private void showColorPopup(@NonNull final View view) {
        DynamicColorPopup dynamicColorPopup = new DynamicColorPopup(
                view, getPopupColors(), this);
        dynamicColorPopup.setColorShape(mColorShape);
        dynamicColorPopup.setAlpha(mAlpha);
        dynamicColorPopup.setTitle(getTitle());
        dynamicColorPopup.setDefaultColor(mDefaultColor);
        dynamicColorPopup.setPreviousColor(mColor);
        dynamicColorPopup.setSelectedColor(mColor);

        dynamicColorPopup.setOnMoreColorsListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnPromptListener() != null) {
                    if (getOnPromptListener().onDialog()) {
                        showColorDialog();
                    }
                } else {
                    showColorDialog();
                }
            }
        });

        dynamicColorPopup.build().show();
    }

    /**
     * Show color dialog to select or create a color.
     *
     * @see DynamicColorDialog
     */
    private void showColorDialog() {
        @ColorInt int color = mColor == DynamicTheme.ADS_THEME_AUTO
                ? DynamicTheme.getInstance().getBackgroundColor() : mColor;

        DynamicColorDialog.newInstance().setColors(getColors(), getShades())
                .setColorShape(mColorShape)
                .setAlpha(mAlpha)
                .setPreviousColor(color)
                .setSelectedColor(color)
                .setOnColorSelectedListener(this)
                .setBuilder(new DynamicDialog.Builder(getContext()).setTitle(getTitle()))
                .showDialog((FragmentActivity) getContext());
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        ((DynamicTextView) getValueView()).setColor(mColor);
    }

    /**
     * Getter for {@link #mColors}.
     */
    public @NonNull @ColorInt Integer[] getColors() {
        if (mColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mColors = DynamicResourceUtils
                    .convertToColorArray(getContext(), mColorsResId);
        }

        if (mColors == null) {
            mColors = DynamicColorPalette.MATERIAL_COLORS;
        }

        return mColors;
    }

    /**
     * Setter for {@link #mColors}.
     */
    public void setColors(@Nullable @ColorInt Integer[] colors) {
        this.mColors = colors;
        this.mColorsResId = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Getter for {@link #mPopupColors}.
     */
    public @NonNull @ColorInt Integer[] getPopupColors() {
        if (mPopupColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mPopupColors = DynamicResourceUtils
                    .convertToColorArray(getContext(), mPopupColorsResId);
        }

        if (mPopupColors == null) {
            mPopupColors = getColors();
        }

        return mPopupColors;
    }

    /**
     * Setter for {@link #mPopupColors}.
     */
    public void setPopupColors(@Nullable @ColorInt Integer[] popupColors) {
        this.mPopupColors = popupColors;
        this.mPopupColorsResId = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Getter for {@link #mShades}.
     */
    public @Nullable @ColorInt Integer[][] getShades() {
        if (mColors == DynamicColorPalette.MATERIAL_COLORS) {
            mShades = DynamicColorPalette.MATERIAL_COLORS_SHADES;
        }

        return mShades;
    }

    /**
     * Setter for {@link #mShades}.
     */
    public void setShades(@Nullable @ColorInt Integer[][] shades) {
        this.mShades = shades;
    }

    /**
     * Getter for {@link #mColorShape}.
     */
    public int getColorShape() {
        return mColorShape;
    }

    /**
     * Setter for {@link #mColorShape}.
     */
    public void setColorShape(int colorShape) {
        this.mColorShape = colorShape;

        mColorView.setColorShape(mColorShape);
    }

    /**
     * Getter for {@link #mDefaultColor}.
     */
    public @ColorInt int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * Setter for {@link #mDefaultColor}.
     */
    public void setDefaultColor(@ColorInt int defaultColor) {
        this.mDefaultColor = defaultColor;

        onUpdate();
    }

    /**
     * Getter for {@link #mColor}.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Setter for {@link #mColor}.
     *
     * @param color The color to be set.
     * @param save {@code true} to update the shared preferences.
     */
    public void setColor(@ColorInt int color, boolean save) {
        this.mColor = color;

        mColorView.setColor(mColor);
        setValueString(DynamicColorView.getColorString(
                getContext(), color, mColorView.isAlpha()));

        if (getPreferenceKey() != null && save) {
            DynamicPreferences.getInstance().savePrefs(getPreferenceKey(), mColor);
        }
    }

    /**
     * Setter for {@link #mColor}.
     */
    public void setColor(@ColorInt int color) {
        setColor(color, true);
    }

    /**
     * Getter for {@link #mAlpha}.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Setter for {@link #mAlpha}.
     *
     * @param alpha {@code true} to enable alpha component for this
     *              preference.
     * @param save {@code true} to update the shared preferences.
     */
    private void setAlpha(boolean alpha, boolean save) {
        this.mAlpha = alpha;

        mColorView.setAlpha(alpha);
        if (save) {
            setColor(mColorView.getColor());
        }
    }
    /**
     * Setter for {@link #mAlpha}.
     */
    public void setAlpha(boolean alpha) {
        setAlpha(alpha, true);
    }

    /**
     * Getter for {@link #mShowColorPopup}.
     */
    public boolean isShowColorPopup() {
        return mShowColorPopup;
    }

    /**
     * Setter for {@link #mShowColorPopup}.
     */
    public void setShowColorPopup(boolean showColorPopup) {
        this.mShowColorPopup = showColorPopup;
    }

    @Override
    public void onColorSelected(int position, @ColorInt int color) {
        setColor(color);
    }
}
