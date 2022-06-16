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

package com.pranavpandey.android.dynamic.support.picker.color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicColorsAdapter;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorListener;
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.task.WallpaperColorsTask;
import com.pranavpandey.android.dynamic.support.util.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;
import com.pranavpandey.android.dynamic.theme.DynamicPalette;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicTaskUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;

import java.util.Arrays;
import java.util.Map;

/**
 * A {@link PopupWindow} to display a grid of colors.
 * <p>It will be used internally by the
 * {@link DynamicColorPreference}
 * but can be used by the other views also.
 */
public class DynamicColorPopup extends DynamicPopup {

    /**
     * Title used by this popup.
     */
    private CharSequence mTitle;

    /**
     * Color entries used by this popup.
     */
    private Integer[] mEntries;

    /**
     * Dynamic color entries generated by this popup.
     */
    private Integer[] mDynamics;

    /**
     * The default color to be shown in footer.
     */
    private @ColorInt int mDefaultColor;

    /**
     * The recent color to be shown in footer.
     */
    private @ColorInt int mRecentColor;

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
     * Task to retrieve the wallpaper colors.
     */
    private WallpaperColorsTask mWallpaperColorsTask;

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
        this.mDefaultColor = Theme.Color.UNKNOWN;
        this.mSelectedColor = Theme.Color.UNKNOWN;
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

        this.mRecentColor = DynamicPickerUtils.getRecentColor();

        if (getTitle() != null) {
            mHeaderView = new DynamicHeader(getAnchor().getContext());
            ((DynamicHeader) mHeaderView).setColorType(Theme.ColorType.PRIMARY);
            ((DynamicHeader) mHeaderView).setContrastWithColorType(Theme.ColorType.SURFACE);
            ((DynamicHeader) mHeaderView).setTitle(mTitle);
            ((DynamicHeader) mHeaderView).setFillSpace(true);
        }

        final GridView gridView = mView.findViewById(R.id.ads_color_picker_presets);
        final ProgressBar progressBar = mView.findViewById(R.id.ads_color_picker_progress_bar);
        final View divider = mView.findViewById(R.id.ads_color_picker_divider);
        final GridView dynamicGridView = mView.findViewById(R.id.ads_color_picker_dynamics);

        if (mSelectedColor == Theme.Color.UNKNOWN
                || Arrays.asList(mEntries).contains(mSelectedColor)) {
            Dynamic.setVisibility(mFooterView.findViewById(
                    R.id.ads_color_picker_popup_footer_image), View.VISIBLE);
        } else {
            setColorView(mFooterView.findViewById(
                    R.id.ads_color_picker_popup_footer_view), mSelectedColor);
        }

        if (mDefaultColor != Theme.Color.UNKNOWN
                && mDefaultColor != mSelectedColor) {
            setColorView(mFooterView.findViewById(
                    R.id.ads_color_picker_popup_footer_view_default), mDefaultColor);
        }

        if (mRecentColor != Theme.Color.UNKNOWN) {
            if (mRecentColor != Theme.AUTO && !mAlpha) {
                mRecentColor = DynamicColorUtils.removeAlpha(mRecentColor);
            }

            if ((mRecentColor != Theme.AUTO || Arrays.asList(mEntries).contains(mRecentColor))
                    && mRecentColor != mDefaultColor && mRecentColor != mSelectedColor) {
                Dynamic.setVisibility(mFooterView.findViewById(
                        R.id.ads_color_picker_popup_footer_recent), View.VISIBLE);
                setColorView(mFooterView.findViewById(
                        R.id.ads_color_picker_popup_footer_view_recent), mRecentColor);
            }
        }

        mFooterView.findViewById(R.id.ads_color_picker_popup_footer)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPopupWindow().dismiss();

                        if (mOnMoreColorsListener == null) {
                            DynamicColorDialog.newInstance().setColors(
                                    DynamicPalette.MATERIAL_COLORS,
                                    DynamicPalette.MATERIAL_COLORS_SHADES)
                                    .setDynamics(getDynamics())
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
                mColorShape, mAlpha, Dynamic.getContrastWithColor(gridView, Theme.Color.UNKNOWN),
                new DynamicColorListener() {
            @Override
            public void onColorSelected(@Nullable String tag, int position, int color) {
                getPopupWindow().dismiss();
                DynamicPickerUtils.setRecentColor(color);

                if (mDynamicColorListener != null) {
                    mDynamicColorListener.onColorSelected(tag, position, color);
                }
            }
        }));

        mWallpaperColorsTask = new WallpaperColorsTask(getAnchor().getContext()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Dynamic.setVisibility(divider, View.GONE);
                Dynamic.setVisibility(dynamicGridView, View.GONE);
                Dynamic.setVisibility(progressBar, View.VISIBLE);
            }

            @Override
            protected void onPostExecute(@Nullable DynamicResult<Map<Integer, Integer>> result) {
                super.onPostExecute(result);

                Dynamic.setVisibility(progressBar, View.GONE);

                if (dynamicGridView == null) {
                    return;
                }

                mDynamics = DynamicTheme.getInstance().getColors()
                        .getAll().toArray(new Integer[0]);
                if (mDynamics.length == 0 && result != null && result.getData() != null) {
                    mDynamics = result.getData().values().toArray(new Integer[0]);
                }

                setDynamics(dynamicGridView, divider);
            }
        };

        setViewRoot(mView.findViewById(R.id.ads_color_picker));
        return this;
    }

    /**
     * Set the dynamic colors to the grid view.
     *
     * @param gridView The grid view to be used.
     * @param divider The divider to be used.
     */
    protected void setDynamics(@Nullable GridView gridView, @Nullable View divider) {
        if (gridView != null && mDynamics != null && mDynamics.length > 0) {
            Dynamic.setVisibility(divider, View.VISIBLE);
            Dynamic.setVisibility(gridView, View.VISIBLE);

            gridView.setAdapter(new DynamicColorsAdapter(mDynamics, mSelectedColor,
                    mColorShape == DynamicColorShape.CIRCLE
                            ? DynamicColorShape.SQUARE : DynamicColorShape.CIRCLE,
                    mAlpha, Dynamic.getContrastWithColor(gridView, Theme.Color.UNKNOWN),
                    new DynamicColorListener() {
                @Override
                public void onColorSelected(@Nullable String tag, int position, int color) {
                    getPopupWindow().dismiss();
                    DynamicPickerUtils.setRecentColor(color);

                    if (mDynamicColorListener != null) {
                        mDynamicColorListener.onColorSelected(tag, position, color);
                    }
                }
            }));
        } else {
            Dynamic.setVisibility(divider, View.GONE);
            Dynamic.setVisibility(gridView, View.GONE);
        }
    }

    @Override
    protected void onCustomisePopup(@NonNull PopupWindow popupWindow,
            @NonNull View content, @ColorInt int backgroundColor) {
        super.onCustomisePopup(popupWindow, content, backgroundColor);

        Dynamic.setContrastWithColor(content.findViewById(
                R.id.ads_color_picker_divider), backgroundColor);
        Dynamic.setContrastWithColor(content.findViewById(
                R.id.ads_color_picker_popup_footer_divider), backgroundColor);
    }

    @Override
    public void show() {
        super.show();

        if (getPopupWindow() == null || getView() == null) {
            return;
        }

        getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DynamicTaskUtils.cancelTask(mWallpaperColorsTask, true);
            }
        });

        if (mDynamics == null) {
            DynamicTaskUtils.executeTask(mWallpaperColorsTask);
        } else if (getView() != null) {
            setDynamics(getView().findViewById(R.id.ads_color_picker_dynamics),
                    getView().findViewById(R.id.ads_color_picker_divider));
        }
    }

    /**
     * Set color view according to the supplied parameters.
     *
     * @param colorView The color view to be set.
     * @param color The color to be applied.
     */
    private void setColorView(final @NonNull DynamicColorView colorView,
            final @ColorInt int color) {
        Dynamic.setVisibility(colorView, View.VISIBLE);
        colorView.setColorShape(mColorShape);
        colorView.setSelected(color == mSelectedColor);
        colorView.setColor(color);

        colorView.setTooltip();
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorView.setSelected(true);
                getPopupWindow().dismiss();
                DynamicPickerUtils.setRecentColor(colorView.getColor());

                if (mDynamicColorListener != null) {
                    mDynamicColorListener.onColorSelected(
                            null, 0, colorView.getColor());
                }
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
    public @Nullable Integer[] getEntries() {
        return mEntries;
    }

    /**
     * Set the color entries for this popup.
     *
     * @param entries The color entries to be set.
     */
    public void setEntries(@Nullable Integer[] entries) {
        this.mEntries = entries;
    }

    /**
     * Get the dynamic color entries used by this popup.
     *
     * @return The dynamic color entries used by this popup.
     */
    public @Nullable Integer[] getDynamics() {
        return mDynamics;
    }

    /**
     * Set the dynamic color entries used by this popup.
     *
     * @param colors The color entries to be set.
     */
    public void setDynamics(@Nullable Integer[] colors) {
        this.mDynamics = colors;
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
