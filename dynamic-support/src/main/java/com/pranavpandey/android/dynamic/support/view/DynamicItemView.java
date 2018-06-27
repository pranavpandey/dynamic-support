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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;

/**
 * A FrameLayout with a icon, title and subtitle functionality which
 * can be used to show various informations according to the requirement.
 * Use {@link #getItemView()} method to set click listeners or to perform
 * other operations.
 */
public class DynamicItemView extends FrameLayout {

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
    private @DynamicColorType int mColorType;

    /**
     * Icon tint color used by this view.
     */
    private @ColorInt int mColor;

    /**
     * {@code true} to show horizontal divider.
     * Useful to display in a list view.
     */
    private boolean mShowDivider;

    /**
     * Root element of this view.
     */
    private ViewGroup mItemView;

    /**
     * Image view to show the icon.
     */
    private DynamicImageView mIconView;

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
        this(context, null);
    }

    public DynamicItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicItemView(@NonNull Context context,
                           @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicInfo);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_icon,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE));
            mTitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_title);
            mSubtitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_subtitle);
            mColor = a.getColor(R.styleable.DynamicInfo_ads_dynamicInfo_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mColorType = a.getInt(
                    R.styleable.DynamicInfo_ads_dynamicInfo_colorType,
                    DynamicColorType.NONE);
            mShowDivider = a.getBoolean(R.styleable.DynamicInfo_ads_dynamicInfo_showDivider,
                    WidgetDefaults.ADS_SHOW_DIVIDER);
        } finally {
            a.recycle();
        }

        initialize();
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

        initialize();
    }

    /**
     * @return The layout used by this view. Override this method to
     *         supply a different layout.
     */
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_item_view;
    }

    /**
     * Initialize the layout for this view.
     */
    private void initialize() {
        inflate(getContext(), getLayoutRes(), this);

        mItemView = findViewById(R.id.ads_item_view);
        mIconView = findViewById(R.id.ads_item_view_icon);
        mTitleView = findViewById(R.id.ads_item_view_title);
        mSubtitleView = findViewById(R.id.ads_item_view_subtitle);
        mDivider = findViewById(R.id.ads_item_view_divider);

        update();
    }

    /**
     * Load this view according to the supplied parameters.
     */
    public void update() {
        if (mColorType != DynamicColorType.NONE
                && mColorType != DynamicColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().getColorFromType(mColorType);
        }

        if (mIcon != null) {
            mIconView.setImageDrawable(mIcon);
        }

        if (mColor != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE) {
            mIconView.setColor(mColor);
        } else {
            mIconView.clearColorFilter();
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

        mDivider.setVisibility(mShowDivider ? VISIBLE : GONE);
    }

    /**
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

        update();
    }

    /**
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

        update();
    }

    /**
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

        update();
    }

    /**
     * @return The icon tint color type used by this view.
     */
    public @DynamicColorType int getColorType() {
        return mColorType;
    }

    /**
     * Set the icon tint color type used by this view.
     *
     * @param colorType The icon tint color type to be set.
     *
     * @see DynamicColorType
     */
    public void setColorType(@DynamicColorType int colorType) {
        this.mColorType = colorType;

        update();
    }

    /**
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
        this.mColorType = DynamicColorType.CUSTOM;
        this.mColor = color;

        update();
    }

    /**
     * @return {@code true} to show horizontal divider.
     *         Useful to display in a list view.
     */
    public boolean isShowDivider() {
        return mShowDivider;
    }

    /**
     * Set the horizontal divider fro this view.
     * Useful to display in a list view.
     *
     * @param showDivider {@code true} to show horizontal
     *                    divider.
     */
    public void setShowDivider(boolean showDivider) {
        this.mShowDivider = showDivider;

        update();
    }

    /**
     * @return The root element of this view.
     */
    public ViewGroup getItemView() {
        return mItemView;
    }

    /**
     * @return The image view to show the icon.
     */
    public DynamicImageView getIconView() {
        return mIconView;
    }

    /**
     * @return The text view to show the title.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * @return The text view to show the subtitle.
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * @return The view to show the divider.
     */
    public View getDivider() {
        return mDivider;
    }
}
