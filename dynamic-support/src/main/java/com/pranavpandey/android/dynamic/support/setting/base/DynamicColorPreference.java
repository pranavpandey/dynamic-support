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

package com.pranavpandey.android.dynamic.support.setting.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorDialog;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPopup;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorView;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.DynamicPalette;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;

/**
 * A {@link DynamicSimplePreference} to implement the functionality of a color picker by using the
 * {@link DynamicColorPopup} and {@link DynamicColorDialog}.
 */
public class DynamicColorPreference extends DynamicSimplePreference {

    /**
     * Resource id of the primary colors array.
     */
    private @ArrayRes int mColorsResId;

    /**
     * Resource id of the popup colors array.
     */
    private @ArrayRes int mPopupColorsResId;

    /**
     * Resource id of the alternate popup colors array.
     */
    private @ArrayRes int mAltPopupColorsResId;

    /**
     * Color entries used by this preference.
     */
    private Integer[] mColors;

    /**
     * Popup color entries used by this preference.
     */
    private Integer[] mPopupColors;

    /**
     * Alternate popup color entries used by this preference.
     */
    private Integer[] mAltPopupColors;

    /**
     * Shade color entries used by this preference.
     */
    private Integer[][] mShades;

    /**
     * Shape for the color view.
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
     * Default alternate color for this preference.
     */
    private @ColorInt int mAltDefaultColor;

    /**
     * Current alternate color value of this preference.
     */
    private @ColorInt int mAltColor;

    /**
     * {@code true} to enable alpha component for the custom color.
     */
    private boolean mAlpha;

    /**
     * {@code true} to show color popup before requesting for the dialog.
     *
     * @see DynamicColorPopup
     * @see DynamicColorDialog
     * @see DynamicPreference.OnPromptListener
     */
    private boolean mShowColorPopup;

    /**
     * Color view used by this preference to display the selected color.
     */
    private DynamicColorView mColorView;

    /**
     * Dynamic color listener to listen color events.
     */
    private DynamicColorListener mDynamicColorListener;

    /**
     * Dynamic color listener to listen alternate color events.
     */
    private DynamicColorListener mAltDynamicColorListener;

    /**
     * Dynamic color resolver to resolve various colors at runtime.
     */
    private DynamicColorResolver mDynamicColorResolver;

    /**
     * Dynamic color resolver to resolve various alternate colors at runtime.
     */
    private DynamicColorResolver mAltDynamicColorResolver;

    public DynamicColorPreference(@NonNull Context context) {
        super(context);
    }

    public DynamicColorPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicColorPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorPreference);

        try {
            mDefaultColor = a.getColor(
                    R.styleable.DynamicColorPreference_adt_color,
                    Theme.AUTO);
            mAltDefaultColor = a.getColor(
                    R.styleable.DynamicColorPreference_adt_altColor,
                    Theme.AUTO);
            mShowColorPopup = a.getBoolean(
                    R.styleable.DynamicColorPreference_ads_popup,
                    false);
            mColorShape = a.getInt(
                    R.styleable.DynamicColorPreference_ads_shape,
                    DynamicColorShape.CIRCLE);
            mAlpha = a.getBoolean(
                    R.styleable.DynamicColorPicker_ads_alphaEnabled,
                    false);
            mColorsResId = a.getResourceId(
                    R.styleable.DynamicColorPreference_ads_colors,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
            mPopupColorsResId = a.getResourceId(
                    R.styleable.DynamicColorPreference_ads_popupColors,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
            mAltPopupColorsResId = a.getResourceId(
                    R.styleable.DynamicColorPreference_ads_altPopupColors,
                    mPopupColorsResId);
        } finally {
            a.recycle();
        }

        mColor = DynamicPreferences.getInstance().load(getPreferenceKey(), getDefaultColor());
        mAltColor = DynamicPreferences.getInstance().load(getAltPreferenceKey(), mAltDefaultColor);
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        final DynamicColorListener dynamicColorListener = new DynamicColorListener() {
            @Override
            public void onColorSelected(@Nullable String tag, int position, int color) {
                setColor(color);

                if (getDynamicColorListener() != null) {
                    getDynamicColorListener().onColorSelected(tag, position, color);
                }
            }
        };

        final DynamicColorListener altDynamicColorListener = new DynamicColorListener() {
            @Override
            public void onColorSelected(@Nullable String tag, int position, int color) {
                setAltColor(color);

                if (getAltDynamicColorListener() != null) {
                    getAltDynamicColorListener().onColorSelected(tag, position, color);
                }
            }
        };

        mColorView = LayoutInflater.from(getContext()).inflate(
                R.layout.ads_preference_color, this, false)
                .findViewById(R.id.ads_preference_color_view);
        setViewFrame(mColorView, true);

        setOnPreferenceClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnPromptListener() != null) {
                    if (isShowColorPopup()) {
                        if (getOnPromptListener().onPopup()) {
                            showColorPopup(view, getTitle(), getPopupColors(), getDefaultColor(),
                                    getColor(false), getColor(), dynamicColorListener);
                        }
                    } else {
                        if (getOnPromptListener().onDialog()) {
                            showColorDialog(getTitle(),
                                    null, getColor(), dynamicColorListener);
                        }
                    }
                } else {
                    if (isShowColorPopup()) {
                        showColorPopup(view, getTitle(), getPopupColors(), getDefaultColor(),
                                getColor(false), getColor(), dynamicColorListener);
                    } else {
                        showColorDialog(getTitle(),
                                null, getColor(), dynamicColorListener);
                    }
                }
            }
        });

        if (getAltPreferenceKey() != null) {
            Dynamic.setBackgroundAware(getActionView(), Theme.BackgroundAware.DISABLE);

            setActionButton(getActionString(), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnPromptListener() != null) {
                        if (isShowColorPopup()) {
                            if (getOnPromptListener().onPopup()) {
                                showColorPopup(v, getAltTitle(), getAltPopupColors(),
                                        getAltDefaultColor(), getAltColor(false),
                                        getAltColor(), altDynamicColorListener);
                            }
                        } else {
                            if (getOnPromptListener().onDialog()) {
                                showColorDialog(getAltTitle(), null,
                                        getAltColor(), altDynamicColorListener);
                            }
                        }
                    } else {
                        if (isShowColorPopup()) {
                            showColorPopup(v, getAltTitle(), getAltPopupColors(),
                                    getAltDefaultColor(), getAltColor(false),
                                    getAltColor(), altDynamicColorListener);
                        } else {
                            showColorDialog(getAltTitle(), null,
                                    getAltColor(), altDynamicColorListener);
                        }
                    }
                }
            });
        }

        setColorShape(getColorShape());
        setAlpha(isAlpha(), false);
        setColor(getColor(false), false);
        setAltColor(getAltColor(false), false);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        Dynamic.setColor(getColorView(), getColor());
        Dynamic.setColor(getActionView(), DynamicColorUtils.removeAlpha(getAltColor()));
        Dynamic.setColor(getValueView(), DynamicColorUtils.removeAlpha(getColor()));
    }

    /**
     * Show color popup to select a color.
     *
     * @param view The anchor view for the popup.
     * @param title the title for the popup.
     * @param defaultColor The default color for the popup.
     * @param color The color value for the popup.
     * @param dynamicColorListener The listener to listen color events.
     *
     * @see DynamicColorPopup
     */
    private void showColorPopup(final @NonNull View view, final @Nullable CharSequence title,
            @NonNull Integer[] colors, @ColorInt int defaultColor,
            final @ColorInt int color, final @ColorInt int resolvedColor,
            final @NonNull DynamicColorListener dynamicColorListener) {
        final DynamicColorPopup colorPopup = new DynamicColorPopup(
                view, colors, dynamicColorListener);
        colorPopup.setColorShape(getColorShape());
        colorPopup.setAlpha(isAlpha());
        colorPopup.setTitle(title);
        colorPopup.setDefaultColor(defaultColor);
        colorPopup.setPreviousColor(color);
        colorPopup.setSelectedColor(color);

        colorPopup.setOnMoreColorsListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnPromptListener() != null) {
                    if (getOnPromptListener().onDialog()) {
                        showColorDialog(title, colorPopup.getDynamics(),
                                resolvedColor, dynamicColorListener);
                    }
                } else {
                    showColorDialog(title, colorPopup.getDynamics(),
                            resolvedColor, dynamicColorListener);
                }
            }
        });

        colorPopup.build().show();
    }

    /**
     * Show color dialog to select or create a color.
     *
     * @param title The title for the dialog.
     * @param dynamics The dynamic color entries for the dialog.
     * @param color The color value for the dialog.
     * @param dynamicColorListener The listener to listen color events.
     *
     * @see DynamicColorDialog
     */
    private void showColorDialog(@Nullable CharSequence title, @Nullable Integer[] dynamics,
            @ColorInt int color, @NonNull DynamicColorListener dynamicColorListener) {
        color = color == Theme.AUTO
                ? DynamicTheme.getInstance().get().getBackgroundColor() : color;

        DynamicColorDialog.newInstance().setColors(getColors(), getShades())
                .setDynamics(dynamics)
                .setColorShape(getColorShape())
                .setAlpha(isAlpha())
                .setPreviousColor(color)
                .setSelectedColor(color)
                .setDynamicColorListener(dynamicColorListener)
                .setBuilder(new DynamicDialog.Builder(getContext()).setTitle(title))
                .showDialog((FragmentActivity) getContext());
    }

    /**
     * Returns the color entries used by this preference.
     *
     * @return The color entries used by this preference.
     */
    public @NonNull Integer[] getColors() {
        if (mColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mColors = DynamicResourceUtils
                    .convertToColorArray(getContext(), mColorsResId);
        }

        if (mColors == null) {
            mColors = DynamicPalette.MATERIAL_COLORS;
        }

        return mColors;
    }

    /**
     * Set the color entries used by this preference.
     *
     * @param colors The color entries to be set.
     */
    public void setColors(@Nullable Integer[] colors) {
        this.mColors = colors;
        this.mColorsResId = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Returns the popup color entries used by this preference.
     *
     * @return The popup color entries used by this preference.
     */
    public @NonNull Integer[] getPopupColors() {
        if (mPopupColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mPopupColors = DynamicResourceUtils.convertToColorArray(
                    getContext(), mPopupColorsResId);
        }

        if (mPopupColors == null) {
            mPopupColors = getColors();
        }

        return mPopupColors;
    }

    /**
     * Set the popup color entries used by this preference.
     *
     * @param popupColors The popup color entries to be set.
     */
    public void setPopupColors(@Nullable Integer[] popupColors) {
        this.mPopupColors = popupColors;
        this.mPopupColorsResId = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Returns the alternate popup color entries used by this preference.
     *
     * @return The alternate popup color entries used by this preference.
     */
    public @NonNull Integer[] getAltPopupColors() {
        if (mAltPopupColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            mAltPopupColors = DynamicResourceUtils
                    .convertToColorArray(getContext(), mAltPopupColorsResId);
        }

        if (mAltPopupColors == null) {
            mAltPopupColors = getColors();
        }

        return mAltPopupColors;
    }

    /**
     * Set the alternate popup color entries used by this preference.
     *
     * @param altPopupColors The alternate popup color entries to be set.
     */
    public void setAltPopupColors(@Nullable Integer[] altPopupColors) {
        this.mAltPopupColors = altPopupColors;
        this.mAltPopupColorsResId = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Returns the shade color entries used by this preference.
     *
     * @return The shade color entries used by this preference.
     */
    public @Nullable Integer[][] getShades() {
        if (getColors() == DynamicPalette.MATERIAL_COLORS) {
            mShades = DynamicPalette.MATERIAL_COLORS_SHADES;
        }

        return mShades;
    }

    /**
     * Set the shade color entries used by this preference.
     *
     * @param shades The shade color entries to be set.
     */
    public void setShades(@Nullable Integer[][] shades) {
        this.mShades = shades;
    }

    /**
     * Get the shape for the color view.
     *
     * @return The shape for the color view.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Set the shape for the color view.
     *
     * @param colorShape The color shape to be set.
     */
    public void setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        if (getColorView() != null) {
            getColorView().setColorShape(getColorShape());
        }
    }

    /**
     * Returns the default color for this preference.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The default color for this preference.
     */
    public @ColorInt int getDefaultColor(boolean resolve) {
        if (resolve && getDynamicColorResolver() != null) {
            return getDynamicColorResolver().getDefaultColor(getPreferenceKey());
        }

        return mDefaultColor;
    }

    /**
     * Returns the default color for this preference.
     *
     * @return The default color for this preference.
     */
    public @ColorInt int getDefaultColor() {
        return getDefaultColor(true);
    }

    /**
     * Set the default color for this preference.
     *
     * @param defaultColor The default color to be set.
     */
    public void setDefaultColor(@ColorInt int defaultColor) {
        this.mDefaultColor = defaultColor;

        update();
    }

    /**
     * Returns the current color value of this preference.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The current color value of this preference.
     */
    public @ColorInt int getColor(boolean resolve) {
        if (resolve && mColor == Theme.AUTO && getDynamicColorResolver() != null) {
            return getDynamicColorResolver().getAutoColor(getPreferenceKey());
        }

        return mColor;
    }

    /**
     * Returns the current color value of this preference.
     *
     * @return The current color value of this preference.
     */
    public @ColorInt int getColor() {
        return getColor(true);
    }

    /**
     * Set the current color value of this preference.
     *
     * @param color The color value to be set.
     * @param save {@code true} to update the shared preferences.
     */
    public void setColor(@ColorInt int color, boolean save) {
        this.mColor = color;

        setValueString(getColorString());

        if (save) {
            DynamicPreferences.getInstance().save(getPreferenceKey(), getColor(false));
        }
    }

    /**
     * Set the current color value of this preference.
     *
     * @param color The color value to be set.
     */
    public void setColor(@ColorInt int color) {
        setColor(color, true);
    }

    /**
     * Returns the alternate default color for this preference.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The alternate default color for this preference.
     */
    public @ColorInt int getAltDefaultColor(boolean resolve) {
        if (resolve && getAltDynamicColorResolver() != null) {
            return getAltDynamicColorResolver().getDefaultColor(getAltPreferenceKey());
        }

        return mAltDefaultColor;
    }

    /**
     * Returns the alternate default color for this preference.
     *
     * @return The alternate default color for this preference.
     */
    public @ColorInt int getAltDefaultColor() {
        return getAltDefaultColor(true);
    }

    /**
     * Set the alternate default color for this preference.
     *
     * @param altDefaultColor The alternate default color to be set.
     */
    public void setAltDefaultColor(@ColorInt int altDefaultColor) {
        this.mAltDefaultColor = altDefaultColor;

        update();
    }

    /**
     * Returns the current alternate color value of this preference.
     *
     * @param resolve {@code true} to resolve the auto color.
     *
     * @return The current alternate color value of this preference.
     */
    public @ColorInt int getAltColor(boolean resolve) {
        if (resolve && mAltColor == Theme.AUTO && getAltDynamicColorResolver() != null) {
            return getAltDynamicColorResolver().getAutoColor(getAltPreferenceKey());
        }

        return mAltColor;
    }

    /**
     * Returns the current alternate color value of this preference.
     *
     * @return The current alternate color value of this preference.
     */
    public @ColorInt int getAltColor() {
        return getAltColor(true);
    }

    /**
     * Set the current alternate color value of this preference.
     *
     * @param altColor The color value to be set.
     * @param save {@code true} to update the shared preferences.
     */
    public void setAltColor(@ColorInt int altColor, boolean save) {
        this.mAltColor = altColor;

        setValueString(getColorString());

        if (save) {
            DynamicPreferences.getInstance().save(getAltPreferenceKey(), getAltColor(false));
        }
    }

    /**
     * Set the current alternate color value of this preference.
     *
     * @param altColor The alternate color value to be set.
     */
    public void setAltColor(@ColorInt int altColor) {
        setAltColor(altColor, true);
    }

    /**
     * Returns whether the alpha component is enabled for custom color.
     *
     * @return {@code true} to enable alpha component for the custom color.
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
        setAlpha(alpha, true);
    }

    /**
     * Set the alpha support for the custom color.
     *
     * @param alpha {@code true} to enable alpha.
     * @param save {@code true} to update the shared preferences.
     */
    private void setAlpha(boolean alpha, boolean save) {
        this.mAlpha = alpha;

        if (getColorView() != null) {
            getColorView().setAlpha(alpha);

            if (save) {
                setColor(getColorView().getColor());
            }
        }
    }

    /**
     * Returns whether to show color popup before requesting for the dialog.
     *
     * @return {@code true} to show color popup before requesting for the dialog.
     */
    public boolean isShowColorPopup() {
        return mShowColorPopup;
    }

    /**
     * Set the popup to be enabled or disabled.
     *
     * @param showColorPopup {@code true} to show color popup before requesting for the dialog.
     */
    public void setShowColorPopup(boolean showColorPopup) {
        this.mShowColorPopup = showColorPopup;
    }

    /**
     * Returns the dynamic color listener to listen color events.
     *
     * @return The dynamic color listener to listen color events.
     */
    public @Nullable DynamicColorListener getDynamicColorListener() {
        return mDynamicColorListener;
    }

    /**
     * Set the dynamic color listener to listen color events.
     *
     * @param dynamicColorListener The listener to be set.
     */
    public void setDynamicColorListener(@Nullable DynamicColorListener dynamicColorListener) {
        this.mDynamicColorListener = dynamicColorListener;
    }

    /**
     * Returns the dynamic color listener to listen alternate color events.
     *
     * @return The dynamic color listener to listen alternate color events.
     */
    public @Nullable DynamicColorListener getAltDynamicColorListener() {
        return mAltDynamicColorListener;
    }

    /**
     * Set the dynamic color listener to listen alternate color events.
     *
     * @param altDynamicColorListener The alternate listener to be set.
     */
    public void setAltDynamicColorListener(
            @Nullable DynamicColorListener altDynamicColorListener) {
        this.mAltDynamicColorListener = altDynamicColorListener;
    }

    /**
     * Returns the dynamic color resolver to resolve various colors at runtime.
     *
     * @return The dynamic color resolver to resolve various colors at runtime.
     */
    public @Nullable DynamicColorResolver getDynamicColorResolver() {
        return mDynamicColorResolver;
    }

    /**
     * Set the dynamic color resolver to resolve various colors at runtime.
     *
     * @param dynamicColorResolver The resolver to be set.
     */
    public void setDynamicColorResolver(
            @Nullable DynamicColorResolver dynamicColorResolver) {
        this.mDynamicColorResolver = dynamicColorResolver;

        update();
    }

    /**
     * Returns the dynamic color resolver to resolve various alternate colors at runtime.
     *
     * @return The alternate dynamic color resolver to resolve various alternate colors at runtime.
     */
    public @Nullable DynamicColorResolver getAltDynamicColorResolver() {
        return mAltDynamicColorResolver;
    }

    /**
     * Set the dynamic color resolver to resolve various alternate colors at runtime.
     *
     * @param altDynamicColorResolver The alternate resolver to be set.
     */
    public void setAltDynamicColorResolver(
            @Nullable DynamicColorResolver altDynamicColorResolver) {
        this.mAltDynamicColorResolver = altDynamicColorResolver;

        update();
    }

    /**
     * Returns the combined title string according to the action string.
     *
     * @return The combined title string according to the action string.
     */
    public @Nullable String getAltTitle() {
        return String.valueOf(getActionString());
    }

    /**
     * Returns the combined title string according to the alternate color.
     *
     * @return The combined color string according to the alternate color.
     */
    private @NonNull String getColorString() {
        if (getAltPreferenceKey() == null) {
            return DynamicColorView.getColorString(getContext(),
                    getColor(false), isColorViewAlpha());
        }

        return String.format(getContext().getString(R.string.ads_format_separator),
                DynamicColorView.getColorString(getContext(),
                        getColor(false), isColorViewAlpha()),
                DynamicColorView.getColorString(getContext(),
                        getAltColor(false), isColorViewAlpha()));
    }

    /**
     * Get the color view used by this preference to display the selected color.
     *
     * @return The color view used by this preference to display the selected color.
     */
    public @Nullable DynamicColorView getColorView() {
        return mColorView;
    }

    /**
     * Checks whether the alpha is enabled for the color view.
     *
     * @return {@code true} if the alpha is enabled for the color view.
     */
    public boolean isColorViewAlpha() {
        return getColorView() != null ? getColorView().isAlpha() : isAlpha();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(getPreferenceKey())) {
            setColor(DynamicPreferences.getInstance().load(
                    getPreferenceKey(), getDefaultColor(false)), false);
        } else if (key.equals(getAltPreferenceKey())) {
            setAltColor(DynamicPreferences.getInstance().load(
                    getAltPreferenceKey(), getAltDefaultColor(false)), false);
        }
    }
}
