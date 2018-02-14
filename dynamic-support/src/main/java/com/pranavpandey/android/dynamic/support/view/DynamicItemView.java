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

import static com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE;
import static com.pranavpandey.android.dynamic.support.widget.WidgetDefaults.ADS_SHOW_DIVIDER;

/**
 * A {@link FrameLayout} with a icon, title and subtitle functionality
 * which can be used to show various informations according to the requirement.
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
     * Icon tint color used by this view.
     */
    private @ColorInt int mColor;

    /**
     * Icon tint color type used by this view.
     */
    private @DynamicColorType int mColorType;

    /**
     * {@code true} to show horizontal divider. Useful to display 
     * in a list view.
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
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicInfo);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_icon, ADS_DEFAULT_RESOURCE_VALUE));
            mTitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_title);
            mSubtitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_subtitle);
            mColor = a.getColor(R.styleable.DynamicInfo_ads_dynamicInfo_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mColorType = a.getInt(
                    R.styleable.DynamicInfo_ads_dynamicInfo_colorType,
                    DynamicColorType.NONE);
            mShowDivider = a.getBoolean(R.styleable.DynamicInfo_ads_dynamicInfo_showDivider,
                    ADS_SHOW_DIVIDER);
        } finally {
            a.recycle();
        }

        initialize();
    }

    /**
     * Constructor to initialize an object of this view by supplying
     * context, icon, title, subtitle, tint color and boolean to show
     * the divider.
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
     * @return The layout used by this view. Override this to supply a
     *         different layout.
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

        if (mColor != ADS_DEFAULT_RESOURCE_VALUE) {
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
     * Getter for {@link #mIcon}.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Setter for {@link #mIcon}.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        update();
    }

    /**
     * Getter for {@link #mTitle}.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Setter for {@link #mTitle}.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        update();
    }

    /**
     * Getter for {@link #mSubtitle}.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Setter for {@link #mSubtitle}.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        update();
    }

    /**
     * Getter for {@link #mColorType}.
     */
    public @DynamicColorType int getColorType() {
        return mColorType;
    }

    /**
     * Setter for {@link #mColorType}.
     */
    public void setColorType(@DynamicColorType int color) {
        this.mColorType = DynamicColorType.CUSTOM;
        this.mColor = color;

        update();
    }

    /**
     * Getter for {@link #mColor}.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Setter for {@link #mColor}.
     */
    public void setColor(@ColorInt int color) {
        this.mColor = color;

        update();
    }

    /**
     * Getter for {@link #mShowDivider}.
     */
    public boolean isShowDivider() {
        return mShowDivider;
    }

    /**
     * Setter for {@link #mShowDivider}.
     */
    public void setShowDivider(boolean showDivider) {
        this.mShowDivider = showDivider;

        update();
    }

    /**
     * Getter for {@link #mItemView}.
     */
    public ViewGroup getItemView() {
        return mItemView;
    }

    /**
     * Getter for {@link #mIconView}.
     */
    public DynamicImageView getIconView() {
        return mIconView;
    }

    /**
     * Getter for {@link #mTitleView}.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Getter for {@link #mSubtitleView}.
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Getter for {@link #mDivider}.
     */
    public View getDivider() {
        return mDivider;
    }
}
