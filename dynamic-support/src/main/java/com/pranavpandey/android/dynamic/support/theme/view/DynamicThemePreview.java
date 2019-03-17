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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.content.Context;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A ThemePreview to show the dynamic app theme preview according to the selected values.
 */
public class DynamicThemePreview extends ThemePreview<DynamicAppTheme> {

    /**
     * Status bar used by this preview.
     */
    private ImageView mStatusBar;

    /**
     * Background card used by this preview.
     */
    private DynamicCardView mBackgroundCard;

    /**
     * Header background used by this preview.
     */
    private ViewGroup mHeader;

    /**
     * Header icon used by this preview.
     */
    private DynamicImageView mHeaderIcon;

    /**
     * Header title used by this preview.
     */
    private DynamicImageView mHeaderTitle;

    /**
     * Header menu used by this preview.
     */
    private DynamicImageView mHeaderMenu;

    /**
     * Content parent view used by this preview.
     */
    private ViewGroup mContent;

    /**
     * Icon used by this preview.
     */
    private DynamicImageView mIcon;

    /**
     * Primary text used by this preview.
     */
    private DynamicImageView mTextPrimary;

    /**
     * Secondary text used by this preview.
     */
    private DynamicImageView mTextSecondary;

    /**
     * Background tint text used by this preview.
     */
    private DynamicImageView mTextTintBackground;

    /**
     * FAB used by this preview.
     */
    private DynamicFloatingActionButton mFAB;

    /**
     * On click listener to receive FAB click events.
     */
    private View.OnClickListener mOnFABClickListener;

    public DynamicThemePreview(@NonNull Context context) {
        this(context, null);
    }

    public DynamicThemePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicThemePreview(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_layout_theme_preview;
    }

    @Override
    public @NonNull DynamicAppTheme getDefaultTheme() {
        return DynamicTheme.getInstance().get();
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mStatusBar = findViewById(R.id.ads_theme_status_bar);
        mBackgroundCard = findViewById(R.id.ads_theme_background);
        mHeader = findViewById(R.id.ads_theme_header);
        mHeaderIcon = findViewById(R.id.ads_theme_header_icon);
        mHeaderTitle = findViewById(R.id.ads_theme_header_title);
        mHeaderMenu = findViewById(R.id.ads_theme_header_menu);
        mContent = findViewById(R.id.ads_theme_content);
        mIcon = findViewById(R.id.ads_theme_icon);
        mTextPrimary = findViewById(R.id.ads_theme_text_primary);
        mTextSecondary = findViewById(R.id.ads_theme_text_secondary);
        mTextTintBackground = findViewById(R.id.ads_theme_text_tint_background);
        mFAB = findViewById(R.id.ads_theme_fab);
    }

    @Override
    protected void onUpdate() {
        mStatusBar.setBackgroundColor(getDynamicTheme().getPrimaryColorDark());
        mBackgroundCard.setRadius(getDynamicTheme().getCornerRadius());
        mBackgroundCard.setColor(getDynamicTheme().getBackgroundColor());
        mHeader.setBackgroundColor(getDynamicTheme().getPrimaryColor());
        DynamicDrawableUtils.setBackground(mStatusBar,
                DynamicThemeUtils.getCornerDrawable(
                        getDynamicTheme().getCornerSizeDp(),
                        getDynamicTheme().getPrimaryColorDark(), false));

        if (getDynamicTheme().getCornerSizeDp() < WidgetDefaults.ADS_CORNER_MIN_THEME) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay);
            mTextPrimary.setImageResource(R.drawable.ads_theme_overlay);
            mTextSecondary.setImageResource(R.drawable.ads_theme_overlay);
            mTextTintBackground.setImageResource(R.drawable.ads_theme_overlay);
        } else if (getDynamicTheme().getCornerSizeDp()
                < WidgetDefaults.ADS_CORNER_MIN_THEME_ROUND) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_rect);
            mTextPrimary.setImageResource(R.drawable.ads_theme_overlay_rect);
            mTextSecondary.setImageResource(R.drawable.ads_theme_overlay_rect);
            mTextTintBackground.setImageResource(R.drawable.ads_theme_overlay_rect);
        } else {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_round);
            mTextPrimary.setImageResource(R.drawable.ads_theme_overlay_round);
            mTextSecondary.setImageResource(R.drawable.ads_theme_overlay_round);
            mTextTintBackground.setImageResource(R.drawable.ads_theme_overlay_round);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getDynamicTheme().getBackgroundColor(false) == Theme.AUTO
                        && getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {

                    RadialGradient gradient =
                            new RadialGradient(mContent.getMeasuredWidth() / 2f,
                                    mContent.getMeasuredHeight() / 2f,
                                    getMeasuredWidth() / 2f,
                                    new int[] { DynamicTheme.getInstance().generateDarkColor(
                                            getDynamicTheme().getTintBackgroundColor()),
                                            getDynamicTheme().getBackgroundColor() },
                                    null, Shader.TileMode.CLAMP);
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setShader(gradient);
                    DynamicDrawableUtils.setBackground(mContent, shape);
                } else {
                    DynamicDrawableUtils.setBackground(mContent, null);
                }
            }
        });

        mHeaderIcon.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mHeaderTitle.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mHeaderMenu.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mIcon.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mTextPrimary.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mTextSecondary.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mTextTintBackground.setBackgroundAware(getDynamicTheme().getBackgroundAware());
        mFAB.setBackgroundAware(getDynamicTheme().getBackgroundAware());

        mHeaderIcon.setContrastWithColor(getDynamicTheme().getPrimaryColor());
        mHeaderTitle.setContrastWithColor(getDynamicTheme().getPrimaryColor());
        mHeaderMenu.setContrastWithColor(getDynamicTheme().getPrimaryColor());
        mIcon.setContrastWithColor(getDynamicTheme().getBackgroundColor());
        mTextPrimary.setContrastWithColor(getDynamicTheme().getBackgroundColor());
        mTextSecondary.setContrastWithColor(getDynamicTheme().getBackgroundColor());
        mTextTintBackground.setContrastWithColor(getDynamicTheme().getBackgroundColor());
        mFAB.setContrastWithColor(getDynamicTheme().getBackgroundColor());

        mHeaderIcon.setColor(getDynamicTheme().getTintPrimaryColor());
        mHeaderTitle.setColor(getDynamicTheme().getTintPrimaryColor());
        mHeaderMenu.setColor(getDynamicTheme().getTintPrimaryColor());
        mIcon.setColor(getDynamicTheme().getPrimaryColor());
        mTextPrimary.setColor(getDynamicTheme().getTextPrimaryColor());
        mTextSecondary.setColor(getDynamicTheme().getTextSecondaryColor());
        mTextTintBackground.setColor(getDynamicTheme().getTintBackgroundColor());
        mFAB.setColor(getDynamicTheme().getAccentColor());
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        mFAB.setEnabled(enabled);
        mFAB.setOnClickListener(enabled ? mOnFABClickListener : null);
        mFAB.setClickable(enabled && mOnFABClickListener != null);
    }

    /**
     * Get the on click listener to receive FAB click events.
     *
     * @return The on click listener to receive FAB click events.
     */
    public OnClickListener getOnFABClickListener() {
        return mOnFABClickListener;
    }

    /**
     * Set the on click listener for FAB to receive the click events and to perform edit
     * operation.
     *
     * @param onFABClickListener The on click listener to be set.
     */
    public void setOnFABClickListener(@Nullable View.OnClickListener onFABClickListener) {
        this.mOnFABClickListener = onFABClickListener;

        mFAB.setOnClickListener(mOnFABClickListener);
        onEnabled(isEnabled());
    }

    /**
     * Get the status bar used by this preview.
     *
     * @return The status bar used by this preview.
     */
    public ImageView getStatusBar() {
        return mStatusBar;
    }

    /**
     * Get the background card used by this preview.
     *
     * @return The background card used by this preview.
     */
    public DynamicCardView getBackgroundCard() {
        return mBackgroundCard;
    }

    /**
     * Get the header background used by this preview.
     *
     * @return The header background used by this preview.
     */
    public ViewGroup getHeader() {
        return mHeader;
    }

    /**
     * Get the header icon used by this preview.
     *
     * @return The header title used by this preview.
     */
    public DynamicImageView getHeaderIcon() {
        return mHeaderIcon;
    }

    /**
     * Get the header title used by this preview.
     *
     * @return The header title used by this preview.
     */
    public DynamicImageView getHeaderTitle() {
        return mHeaderTitle;
    }

    /**
     * Get the header menu used by this preview.
     *
     * @return The header menu used by this preview.
     */
    public DynamicImageView getHeaderMenu() {
        return mHeaderMenu;
    }

    /**
     * Get the icon used by this preview.
     *
     * @return The icon used by this preview.
     */
    public DynamicImageView getIcon() {
        return mIcon;
    }

    /**
     * Get the primary text used by this preview.
     *
     * @return The primary text used by this preview.
     */
    public DynamicImageView getTextPrimary() {
        return mTextPrimary;
    }

    /**
     * Ge the secondary text used by this preview.
     *
     * @return The secondary text used by this preview.
     */
    public DynamicImageView getTextSecondary() {
        return mTextSecondary;
    }

    /**
     * Get the background tint text used by this preview.
     *
     * @return The background tint text used by this preview.
     */
    public DynamicImageView getTextTintBackground() {
        return mTextTintBackground;
    }

    /**
     * Get the FAB used by this preview.
     *
     * @return The FAB used by this preview.
     */
    public DynamicFloatingActionButton getFAB() {
        return mFAB;
    }
}
