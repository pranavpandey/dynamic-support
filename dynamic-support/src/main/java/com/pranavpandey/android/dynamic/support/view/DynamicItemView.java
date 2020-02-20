/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.Dynamic;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A DynamicView with an icon, title and subtitle functionality which can be used to show various
 * information according to the requirement.
 *
 * <p><p>Use {@link #getItemView()} method to set click listeners or to perform other operations.
 */
public class DynamicItemView extends DynamicView {

    /**
     * Icon used by this view.
     */
    private Drawable mIcon;

    /**
     * Title used by this view.
     */
    private CharSequence mTitle;

    /**
     * Subtitle used by this view.
     */
    private CharSequence mSubtitle;

    /**
     * Icon tint color type used by this view.
     */
    private @Theme.ColorType int mColorType;

    /**
     * Icon tint color used by this view.
     */
    private @ColorInt int mColor;

    /**
     * {@code true} to show horizontal divider.
     * <p>Useful to display in a list view.
     */
    private boolean mShowDivider;

    /**
     * {@code true} to fill the empty icon space if applicable.
     */
    private boolean mFillSpace;

    /**
     * Root element of this view.
     */
    private ViewGroup mItemView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the subtitle.
     */
    private TextView mSubtitleView;

    /**
     * View to show the divider.
     */
    private View mDivider;

    public DynamicItemView(@NonNull Context context) {
        super(context);
    }

    public DynamicItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicItemView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context for this view.
     * @param icon The icon for this view.
     * @param title The title for this view.
     * @param subtitle The subtitle for this view.
     * @param color The icon tint color for this view.
     * @param showDivider {@code true} to show horizontal divider.
     */
    public DynamicItemView(@NonNull Context context, @Nullable Drawable icon,
            @Nullable CharSequence title, @Nullable CharSequence subtitle,
            @ColorInt int color, boolean showDivider) {
        super(context);

        this.mIcon = icon;
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mColor = color;
        this.mShowDivider = showDivider;

        onUpdate();
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicInfo);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicInfo_ads_icon,
                    WidgetDefaults.ADS_COLOR_UNKNOWN));
            mTitle = a.getString(R.styleable.DynamicInfo_ads_title);
            mSubtitle = a.getString(R.styleable.DynamicInfo_ads_subtitle);
            mColor = a.getColor(R.styleable.DynamicInfo_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mColorType = a.getInt(R.styleable.DynamicInfo_ads_colorType,
                    Theme.ColorType.NONE);
            mShowDivider = a.getBoolean(R.styleable.DynamicInfo_ads_showDivider,
                    WidgetDefaults.ADS_SHOW_DIVIDER);
            mFillSpace = a.getBoolean(R.styleable.DynamicInfo_ads_fillSpace,
                    WidgetDefaults.ADS_FILL_SPACE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_item_view;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mItemView = findViewById(R.id.ads_item_view);
        mIconView = findViewById(R.id.ads_item_view_icon);
        mTitleView = findViewById(R.id.ads_item_view_title);
        mSubtitleView = findViewById(R.id.ads_item_view_subtitle);
        mDivider = findViewById(R.id.ads_item_view_divider);

        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (mIcon != null) {
            mIconView.setImageDrawable(mIcon);
            mIconView.setVisibility(VISIBLE);
        } else {
            if (mFillSpace) {
                mIconView.setVisibility(GONE);
            }
        }

        if (mIconView != null) {
            Dynamic.setColorType(mIconView, mColorType);

            if (mColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                Dynamic.setColor(mIconView, mColor);
            } else if (mColorType == Theme.ColorType.NONE) {
                mIconView.clearColorFilter();
            }
        }

        if (mTitle != null) {
            mTitleView.setText(mTitle);
            mTitleView.setVisibility(VISIBLE);
        } else {
            mTitleView.setVisibility(GONE);
        }

        if (mSubtitle != null) {
            mSubtitleView.setText(mSubtitle);
            mSubtitleView.setVisibility(VISIBLE);
        } else {
            mSubtitleView.setVisibility(GONE);
        }

        if (mDivider != null) {
            mDivider.setVisibility(mShowDivider ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        mItemView.setEnabled(enabled);
        mIconView.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mSubtitleView.setEnabled(enabled);
    }

    /**
     * Get the icon used by this view.
     *
     * @return The icon used by this view.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon used by this view.
     *
     * @param icon The icon to be set.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        onUpdate();
    }

    /**
     * Get the title used by this view.
     *
     * @return The title used by this view.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by this view.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        onUpdate();
    }

    /**
     * Get the subtitle used by this view.
     *
     * @return The subtitle used by this view.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Set the subtitle used by this view.
     *
     * @param subtitle The subtitle to be set.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        onUpdate();
    }

    /**
     * Get the icon tint color type used by this view.
     *
     * @return The icon tint color type used by this view.
     */
    public @Theme.ColorType int getColorType() {
        return mColorType;
    }

    /**
     * Set the icon tint color type used by this view.
     *
     * @param colorType The icon tint color type to be set.
     *
     * @see Theme.ColorType
     */
    public void setColorType(@Theme.ColorType int colorType) {
        this.mColorType = colorType;

        onUpdate();
    }

    /**
     * Get the icon tint color used by this view.
     *
     * @return The icon tint color used by this view.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Set the icon tint color used by this view.
     *
     * @param color The icon tint color to be set.
     */
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        onUpdate();
    }

    /**
     * Returns whether to show the horizontal divider.
     * <p>Useful to display in a list view.
     *
     * @return {@code true} to show the horizontal divider.
     */
    public boolean isShowDivider() {
        return mShowDivider;
    }

    /**
     * Set the horizontal divider fro this view.
     * <p>Useful to display in a list view.
     *
     * @param showDivider {@code true} to show the horizontal divider.
     */
    public void setShowDivider(boolean showDivider) {
        this.mShowDivider = showDivider;

        onUpdate();
    }

    /**
     * Returns whether to fill the empty icon space if applicable.
     *
     * @return {@code true} to fill the empty icon space if applicable.
     */
    public boolean isFillSpace() {
        return mFillSpace;
    }

    /**
     * Controls the fill empty space behavior for this view.
     *
     * @param fillSpace {@code true} to fill the empty icon space.
     */
    public void setFillSpace(boolean fillSpace) {
        this.mFillSpace = fillSpace;

        onUpdate();
    }

    /**
     * Get the root element of this view.
     *
     * @return The root element of this view.
     */
    public ViewGroup getItemView() {
        return mItemView;
    }

    /**
     * Get the image view to show the icon.
     *
     * @return The image view to show the icon.
     */
    public ImageView getIconView() {
        return mIconView;
    }

    /**
     * Get the text view to show the title.
     *
     * @return The text view to show the title.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the text view to show the subtitle.
     *
     * @return The text view to show the subtitle.
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Get the view to show the divider.
     *
     * @return The view to show the divider.
     */
    public View getDivider() {
        return mDivider;
    }
}
