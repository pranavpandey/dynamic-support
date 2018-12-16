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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorListener;
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorPalette;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;

import java.util.Arrays;

/**
 * A {@link PopupWindow} to display a grid of colors.
 * <p>It will be used internally by the
 * {@link com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference}
 * but can be used by the other views also.
 */
public class DynamicColorPopup extends DynamicPopup {

    /**
     * Header view for this popup.
     */
    private View mHeaderView;

    /**
     * Content view for this popup.
     */
    private View mView;

    /**
     * Footer view for this popup.
     */
    private View mFooterView;

    /**
     * Title used by this popup.
     */
    private CharSequence mTitle;

    /**
     * Color entries used by this popup.
     */
    private @ColorInt Integer[] mEntries;

    /**
     * The default color to be shown in footer.
     */
    private @ColorInt int mDefaultColor;

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
    private DynamicColorListener mDynamicColorListener;

    /**
     * On click listener to get the more colors callback.
     */
    private View.OnClickListener mOnMoreColorsListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param entries The color entries for this popup.
     * @param dynamicColorListener The color listener to get the selected color.
     */
    public DynamicColorPopup(@NonNull View anchor, @NonNull Integer[] entries,
            @NonNull DynamicColorListener dynamicColorListener) {
        this.mAnchor = anchor;
        this.mEntries = entries;
        this.mDynamicColorListener = dynamicColorListener;
        this.mDefaultColor = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE;
        this.mSelectedColor = DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE;
        this.mColorShape = DynamicColorShape.CIRCLE;
    }

    @Override
    public @NonNull DynamicColorPopup build() {
        mView = LayoutInflater.from(getAnchor().getContext()).inflate(
                R.layout.ads_color_picker_popup,
                (ViewGroup) getAnchor().getRootView(), false);
        mFooterView = LayoutInflater.from(getAnchor().getContext()).inflate(
                R.layout.ads_color_picker_popup_footer,
                (ViewGroup) getAnchor().getRootView(), false);

        if (getTitle() != null) {
            mHeaderView = new DynamicHeader(getAnchor().getContext());
            ((DynamicHeader) mHeaderView).setTitle(mTitle);
            ((DynamicHeader) mHeaderView).setFillSpace(true);
        }

        final GridView gridView = mView.findViewById(R.id.ads_color_picker_presets);

        if (mSelectedColor == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE
                || Arrays.asList(mEntries).contains(mSelectedColor)) {
            mFooterView.findViewById(R.id.ads_color_picker_popup_footer_image)
                    .setVisibility(View.VISIBLE);
        } else {
            setColorView((DynamicColorView) mFooterView.findViewById(
                    R.id.ads_color_picker_popup_footer_view), mSelectedColor);
        }

        if (mDefaultColor != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE
                && mDefaultColor != mSelectedColor) {
            setColorView((DynamicColorView) mFooterView.findViewById(
                    R.id.ads_color_picker_popup_footer_view_default), mDefaultColor);
        }

        mFooterView.findViewById(R.id.ads_color_picker_popup_footer)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPopupWindow().dismiss();

                        if (mOnMoreColorsListener == null) {
                            DynamicColorDialog.newInstance().setColors(
                                    DynamicColorPalette.MATERIAL_COLORS,
                                    DynamicColorPalette.MATERIAL_COLORS_SHADES)
                                    .setColorShape(mColorShape)
                                    .setAlpha(mAlpha)
                                    .setPreviousColor(mPreviousColor)
                                    .setSelectedColor(mSelectedColor == Theme.AUTO
                                            ? DynamicTheme.getInstance().get().getBackgroundColor()
                                            : mSelectedColor)
                                    .setDynamicColorListener(
                                            new DynamicColorListener() {
                                                @Override
                                                public void onColorSelected(
                                                        @Nullable String tag, int position,
                                                        int color) {
                                                    if (mDynamicColorListener != null) {
                                                        mDynamicColorListener.onColorSelected(
                                                                tag, position, color);
                                                    }
                                                }
                                            })
                                    .setBuilder(new DynamicDialog.Builder(
                                            getAnchor().getContext()).setTitle(getTitle()))
                                    .showDialog((FragmentActivity) getAnchor().getContext());
                        } else {
                            mOnMoreColorsListener.onClick(view);
                        }
                    }
                });

        gridView.setAdapter(new DynamicColorsAdapter(mEntries, mSelectedColor,
                mColorShape, mAlpha, new DynamicColorListener() {
                    @Override
                    public void onColorSelected(@Nullable String tag, int position, int color) {
                        getPopupWindow().dismiss();

                        if (mDynamicColorListener != null) {
                            mDynamicColorListener.onColorSelected(tag, position, color);
                        }
                    }
                }));

        setViewRoot(mView.findViewById(R.id.ads_color_picker));
        return this;
    }

    /**
     * Set color view according to the supplied parameters.
     *
     * @param colorView The color view to be set.
     * @param color The color to be applied.
     */
    private void setColorView(final @NonNull DynamicColorView colorView,
                              final @ColorInt int color) {
        colorView.setVisibility(View.VISIBLE);
        colorView.setColorShape(mColorShape);
        colorView.setSelected(color == mSelectedColor);
        colorView.setColor(color);

        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorView.setSelected(true);
                getPopupWindow().dismiss();

                if (mDynamicColorListener != null) {
                    mDynamicColorListener.onColorSelected(
                            null, 0, colorView.getColor());
                }
            }
        });
        colorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                colorView.showHint();
                return true;
            }
        });
    }

    @Override
    protected @Nullable View getHeaderView() {
        return mHeaderView;
    }

    @Override
    protected @Nullable View getView() {
        return mView;
    }

    @Override
    protected @Nullable View getFooterView() {
        return mFooterView;
    }

    /**
     * Get the title used by this popup.
     *
     * @return The title used by this popup.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by this popup.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;
    }

    /**
     * Get the color entries used by this popup.
     *
     * @return The color entries used by this popup.
     */
    public Integer[] getEntries() {
        return mEntries;
    }

    /**
     * Set the color entries for this popup.
     *
     * @param entries The color entries to be set.
     */
    public void setEntries(Integer[] entries) {
        this.mEntries = entries;
    }

    /**
     * Get the default color to be shown in footer.
     *
     * @return The default color to be shown in footer.
     */
    public @ColorInt int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * Set the default color to be shown in footer.
     *
     * @param defaultColor The default color to be set.
     */
    public void setDefaultColor(@ColorInt int defaultColor) {
        this.mDefaultColor = defaultColor;
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
     * Get the shape of the color swatches.
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
     * Returns whether the color alpha is enabled for the picker.
     *
     * @return {@code true} to enable the color alpha for the picker.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Set the color alpha support for the picker.
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
     * Returns the on click listener to get the more colors callback.
     *
     * @return The on click listener to get the more colors callback.
     */
    public @Nullable View.OnClickListener getOnMoreColorsListener() {
        return mOnMoreColorsListener;
    }

    /**
     * Set the on click listener to get the more colors callback.
     *
     * @param onMoreColorsListener The on click listener to be set.
     */
    public void setOnMoreColorsListener(@Nullable View.OnClickListener onMoreColorsListener) {
        this.mOnMoreColorsListener = onMoreColorsListener;
    }
}
