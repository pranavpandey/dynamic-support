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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicRemoteTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicShapeUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A {@link ThemePreview} to show the {@link DynamicRemoteTheme} preview according to the
 * selected values.
 */
public class DynamicRemoteThemePreview extends ThemePreview<DynamicRemoteTheme> {

    /**
     * Background used by this preview.
     */
    private ImageView mBackground;

    /**
     * Header background used by this preview.
     */
    private ImageView mHeaderBackground;

    /**
     * Header icon used by this preview.
     */
    private ImageView mHeaderIcon;

    /**
     * Header title used by this preview.
     */
    private ImageView mHeaderTitle;

    /**
     * Header menu used by this preview.
     */
    private ImageView mHeaderMenu;

    /**
     * First action used by this preview.
     */
    private ImageView mActionOne;

    /**
     * Second action used by this preview.
     */
    private ImageView mActionTwo;

    /**
     * Third action used by this preview.
     */
    private ImageView mActionThree;

    public DynamicRemoteThemePreview(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRemoteThemePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicRemoteThemePreview(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_theme_preview_remote;
    }

    @Override
    public @NonNull DynamicRemoteTheme getDefaultTheme() {
        return DynamicTheme.getInstance().getRemote();
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mBackground = findViewById(R.id.ads_theme_background);
        mHeaderBackground = findViewById(R.id.ads_theme_header_background);
        mHeaderIcon = findViewById(R.id.ads_theme_header_icon);
        mHeaderTitle = findViewById(R.id.ads_theme_header_title);
        mHeaderMenu = findViewById(R.id.ads_theme_header_menu);
        mActionOne = findViewById(R.id.ads_theme_action_one);
        mActionTwo = findViewById(R.id.ads_theme_action_two);
        mActionThree = findViewById(R.id.ads_theme_action_three);
    }

    @Override
    protected void onUpdate() {
        Drawable background;
        Drawable header;
        
        if (getDynamicTheme().getStyle() == Theme.Style.CUSTOM) {
            background = DynamicShapeUtils.getCornerDrawableWithStroke(
                    getDynamicTheme().getCornerSizeDp(),
                    getDynamicTheme().getAccentColor(), false);
            header = DynamicShapeUtils.getCornerDrawable(getDynamicTheme().getCornerSizeDp(),
                    getDynamicTheme().getPrimaryColor(), true, true);

            Dynamic.setContrastWithColor(mHeaderIcon, getDynamicTheme().getPrimaryColor());
            Dynamic.setContrastWithColor(mHeaderTitle, getDynamicTheme().getPrimaryColor());
            Dynamic.setContrastWithColor(mHeaderMenu, getDynamicTheme().getPrimaryColor());
            Dynamic.setContrastWithColor(mActionOne, getDynamicTheme().getAccentColor());
            Dynamic.setContrastWithColor(mActionTwo, getDynamicTheme().getAccentColor());
            Dynamic.setContrastWithColor(mActionThree, getDynamicTheme().getAccentColor());

            Dynamic.setColor(mHeaderIcon, getDynamicTheme().getTintPrimaryColor());
            Dynamic.setColor(mHeaderTitle, getDynamicTheme().getTintPrimaryColor());
            Dynamic.setColor(mHeaderMenu, getDynamicTheme().getTintPrimaryColor());
            Dynamic.setColor(mActionOne, getDynamicTheme().getTintAccentColor());
            Dynamic.setColor(mActionTwo, getDynamicTheme().getTintAccentColor());
            Dynamic.setColor(mActionThree, getDynamicTheme().getTintAccentColor());
        } else {
            background = DynamicShapeUtils.getCornerDrawableWithStroke(
                    getDynamicTheme().getCornerSizeDp(),
                    getDynamicTheme().getBackgroundColor(), false);
            header = DynamicShapeUtils.getCornerDrawable(getDynamicTheme().getCornerSizeDp(),
                    getDynamicTheme().getBackgroundColor(), true, true);

            Dynamic.setContrastWithColor(mHeaderIcon, getDynamicTheme().getBackgroundColor());
            Dynamic.setContrastWithColor(mHeaderTitle, getDynamicTheme().getBackgroundColor());
            Dynamic.setContrastWithColor(mHeaderMenu, getDynamicTheme().getBackgroundColor());
            Dynamic.setContrastWithColor(mActionOne, getDynamicTheme().getBackgroundColor());
            Dynamic.setContrastWithColor(mActionTwo, getDynamicTheme().getBackgroundColor());
            Dynamic.setContrastWithColor(mActionThree, getDynamicTheme().getBackgroundColor());

            Dynamic.setColor(mHeaderIcon, getDynamicTheme().getPrimaryColor());
            Dynamic.setColor(mHeaderTitle, getDynamicTheme().getTextPrimaryColor());
            Dynamic.setColor(mHeaderMenu, getDynamicTheme().getAccentColor());
            Dynamic.setColor(mActionOne, getDynamicTheme().getAccentColor());
            Dynamic.setColor(mActionTwo, getDynamicTheme().getAccentColor());
            Dynamic.setColor(mActionThree, getDynamicTheme().getAccentColor());
        }

        mBackground.setImageDrawable(background);
        DynamicDrawableUtils.setBackground(mHeaderBackground, header);

        mHeaderIcon.setImageResource(getDynamicTheme().isFontScale()
                ? R.drawable.ads_ic_font_scale : R.drawable.ads_ic_circle);
        mHeaderMenu.setImageResource(getDynamicTheme().isBackgroundAware()
                ? R.drawable.ads_ic_background_aware : R.drawable.ads_ic_customise);

        if (getDynamicTheme().getCornerSizeDp() < Defaults.ADS_CORNER_MIN_THEME) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay);
            mActionOne.setImageResource(R.drawable.ads_theme_overlay);
            mActionTwo.setImageResource(R.drawable.ads_theme_overlay);
            mActionThree.setImageResource(R.drawable.ads_theme_overlay);
        } else if (getDynamicTheme().getCornerSizeDp()
                < Defaults.ADS_CORNER_MIN_THEME_ROUND) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_rect);
            mActionOne.setImageResource(R.drawable.ads_theme_overlay_rect);
            mActionTwo.setImageResource(R.drawable.ads_theme_overlay_rect);
            mActionThree.setImageResource(R.drawable.ads_theme_overlay_rect);
        } else {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_round);
            mActionOne.setImageResource(R.drawable.ads_theme_overlay_round);
            mActionTwo.setImageResource(R.drawable.ads_theme_overlay_round);
            mActionThree.setImageResource(R.drawable.ads_theme_overlay_round);
        }

        Dynamic.setBackgroundAware(mHeaderIcon, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mHeaderTitle, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mHeaderMenu, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mActionOne, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mActionTwo, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mActionThree, getDynamicTheme().getBackgroundAware());
    }

    @Override
    public @NonNull View getActionView() {
        return mHeaderMenu;
    }
}
