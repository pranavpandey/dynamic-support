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

package com.pranavpandey.android.dynamic.support.theme.view.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicShapeUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;

/**
 * A {@link ThemePreview} to show the {@link DynamicAppTheme} preview according to the
 * selected values.
 */
public class DynamicThemePreview extends ThemePreview<DynamicAppTheme> {

    /**
     * Background used by this preview.
     */
    private ImageView mBackground;

    /**
     * Status bar used by this preview.
     */
    private ImageView mStatusBar;

    /**
     * Header background used by this preview.
     */
    private ViewGroup mHeader;

    /**
     * Header icon used by this preview.
     */
    private ImageView mHeaderIcon;

    /**
     * Header shadow used by this preview.
     */
    private ImageView mHeaderShadow;

    /**
     * Header title used by this preview.
     */
    private ImageView mHeaderTitle;

    /**
     * Header menu used by this preview.
     */
    private ImageView mHeaderMenu;

    /**
     * Content parent view used by this preview.
     */
    private ViewGroup mContent;

    /**
     * Surface view view used by this preview.
     */
    private ViewGroup mSurface;

    /**
     * Icon used by this preview.
     */
    private ImageView mIcon;

    /**
     * Title used by this preview.
     */
    private ImageView mTitle;

    /**
     * Subtitle used by this preview.
     */
    private ImageView mSubtitle;

    /**
     * Error image used by this preview.
     */
    private ImageView mError;

    /**
     * Primary text start used by this preview.
     */
    private ImageView mTextPrimaryStart;

    /**
     * Primary text end used by this preview.
     */
    private ImageView mTextPrimaryEnd;

    /**
     * Secondary text start used by this preview.
     */
    private ImageView mTextSecondaryStart;

    /**
     * Secondary text end used by this preview.
     */
    private ImageView mTextSecondaryEnd;

    /**
     * Background tint text start used by this preview.
     */
    private ImageView mTextDescriptionStart;

    /**
     * Background tint text end used by this preview.
     */
    private ImageView mTextDescriptionEnd;

    /**
     * FAB used by this preview.
     */
    private FloatingActionButton mFAB;

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
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_theme_preview;
    }

    @Override
    public @NonNull DynamicAppTheme getDefaultTheme() {
        return DynamicTheme.getInstance().get();
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mBackground = findViewById(R.id.ads_theme_background);
        mStatusBar = findViewById(R.id.ads_theme_status_bar);
        mHeader = findViewById(R.id.ads_theme_header);
        mHeaderIcon = findViewById(R.id.ads_theme_header_icon);
        mHeaderShadow = findViewById(R.id.ads_theme_header_shadow);
        mHeaderTitle = findViewById(R.id.ads_theme_header_title);
        mHeaderMenu = findViewById(R.id.ads_theme_header_menu);
        mContent = findViewById(R.id.ads_theme_content);
        mSurface = findViewById(R.id.ads_theme_content_start);
        mIcon = findViewById(R.id.ads_theme_icon);
        mTitle = findViewById(R.id.ads_theme_title);
        mSubtitle = findViewById(R.id.ads_theme_subtitle);
        mError = findViewById(R.id.ads_theme_error);
        mTextPrimaryStart = findViewById(R.id.ads_theme_text_primary_start);
        mTextPrimaryEnd = findViewById(R.id.ads_theme_text_primary_end);
        mTextSecondaryStart = findViewById(R.id.ads_theme_text_secondary_start);
        mTextSecondaryEnd = findViewById(R.id.ads_theme_text_secondary_end);
        mTextDescriptionStart = findViewById(R.id.ads_theme_text_description_start);
        mTextDescriptionEnd = findViewById(R.id.ads_theme_text_description_end);
        mFAB = findViewById(R.id.ads_theme_fab);
    }

    @Override
    protected void onUpdate() {
        Drawable background = DynamicShapeUtils.getCornerDrawableWithStroke(
                getDynamicTheme().getCornerSize(), getDynamicTheme().getBackgroundColor(),
                false, getDynamicTheme().getStrokeColor());
        MaterialShapeDrawable drawable = (MaterialShapeDrawable)
                DynamicShapeUtils.getCornerDrawable(getDynamicTheme().getCornerSize(),
                        getDynamicTheme().getSurfaceColor(), false, true);
        @DrawableRes int overlay = DynamicShapeUtils.getOverlayRes(
                getDynamicTheme().getCornerSize());
        @DrawableRes int overlayStart = DynamicShapeUtils.getOverlayStartRes(
                getDynamicTheme().getCornerSize());
        @DrawableRes int overlayEnd = DynamicShapeUtils.getOverlayEndRes(
                getDynamicTheme().getCornerSize());

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        if (DynamicViewUtils.isLayoutRtl(this)) {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setBottomRightCornerSize(
                    drawable.getShapeAppearanceModel().getTopLeftCornerSize()).build();
        } else {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setBottomLeftCornerSize(
                    drawable.getShapeAppearanceModel().getTopLeftCornerSize()).build();
        }
        drawable.setShapeAppearanceModel(shapeAppearanceModel);

        if (Dynamic.isStroke(getDynamicTheme())) {
            drawable.setStroke(Theme.Size.STROKE_PIXEL,
                    getDynamicTheme().isBackgroundAware() ? Dynamic.withContrastRatio(
                            getDynamicTheme().getTintBackgroundColor(),
                            getDynamicTheme().getBackgroundColor(), getDynamicTheme())
                            : getDynamicTheme().getTintBackgroundColor());
        }

        Dynamic.set(mBackground, Dynamic.withThemeOpacity(background, getDynamicTheme()));
        Dynamic.setBackground(mStatusBar, Dynamic.withThemeOpacity(
                DynamicShapeUtils.getCornerDrawable(getDynamicTheme().getCornerSize(),
                        getDynamicTheme().getPrimaryColorDark(),
                        true, false), getDynamicTheme()));
        mHeader.setBackgroundColor(Dynamic.withThemeOpacity(
                getDynamicTheme().getPrimaryColor(), getDynamicTheme()));
        Dynamic.setBackground(mSurface, Dynamic.withThemeOpacity(drawable, getDynamicTheme()));
        Dynamic.setCorner(mFAB, getDynamicTheme().getCornerRadius());

        Dynamic.setResource(mHeaderTitle, overlay);
        Dynamic.setResource(mHeaderMenu, getDynamicTheme().isBackgroundAware()
                ? R.drawable.ads_ic_background_aware : R.drawable.ads_ic_customise);
        Dynamic.setResource(mIcon, getDynamicTheme().isFontScale()
                ? R.drawable.ads_ic_font_scale : R.drawable.ads_ic_circle);
        Dynamic.setResource(mTitle, overlay);
        Dynamic.setResource(mSubtitle, overlay);
        Dynamic.setResource(mError, overlay);
        Dynamic.setResource(mTextPrimaryStart, overlayStart);
        Dynamic.setResource(mTextPrimaryEnd, overlayEnd);
        Dynamic.setResource(mTextSecondaryStart, overlayStart);
        Dynamic.setResource(mTextSecondaryEnd, overlayEnd);
        Dynamic.setResource(mTextDescriptionStart, overlayStart);
        Dynamic.setResource(mTextDescriptionEnd, overlayEnd);

        Dynamic.setBackgroundAware(mHeaderIcon, getDynamicTheme());
        Dynamic.setBackgroundAware(mHeaderTitle, getDynamicTheme());
        Dynamic.setBackgroundAware(mHeaderMenu, getDynamicTheme());
        Dynamic.setBackgroundAware(mHeaderShadow, getDynamicTheme().isShowDividers()
                ? getDynamicTheme().getBackgroundAware() : Theme.BackgroundAware.DISABLE,
                getDynamicTheme().getContrast());
        Dynamic.setBackgroundAware(mIcon, getDynamicTheme());
        Dynamic.setBackgroundAware(mTitle, getDynamicTheme());
        Dynamic.setBackgroundAware(mSubtitle, getDynamicTheme());
        Dynamic.setBackgroundAware(mError, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextPrimaryStart, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextPrimaryEnd, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextSecondaryStart, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextSecondaryEnd, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextDescriptionStart, getDynamicTheme());
        Dynamic.setBackgroundAware(mTextDescriptionEnd, getDynamicTheme());
        Dynamic.setBackgroundAware(mFAB, getDynamicTheme());

        Dynamic.setContrastWithColor(mHeaderIcon, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mHeaderTitle, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mHeaderMenu, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mHeaderShadow, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mIcon, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mTitle, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mSubtitle, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mError, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mTextPrimaryStart, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mTextPrimaryEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextSecondaryStart, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mTextSecondaryEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextDescriptionStart, getDynamicTheme().getSurfaceColor());
        Dynamic.setContrastWithColor(mTextDescriptionEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mFAB, getDynamicTheme().getBackgroundColor());

        Dynamic.setColor(mHeaderIcon, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mHeaderTitle, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mHeaderMenu, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mHeaderShadow, getDynamicTheme().getAccentColorDark());
        Dynamic.setColor(mIcon, getDynamicTheme().getTintBackgroundColor());
        Dynamic.setColor(mTitle, getDynamicTheme().getPrimaryColor());
        Dynamic.setColor(mSubtitle, getDynamicTheme().getAccentColor());
        Dynamic.setColor(mError, getDynamicTheme().getErrorColor());
        Dynamic.setColor(mTextPrimaryStart, getDynamicTheme().getTextPrimaryColor());
        Dynamic.setColor(mTextPrimaryEnd, getDynamicTheme().getTextPrimaryColor());
        Dynamic.setColor(mTextSecondaryStart, getDynamicTheme().getTextSecondaryColor());
        Dynamic.setColor(mTextSecondaryEnd, getDynamicTheme().getTextSecondaryColor());
        Dynamic.setColor(mTextDescriptionStart, getDynamicTheme().getTintSurfaceColor());
        Dynamic.setColor(mTextDescriptionEnd, getDynamicTheme().getTintBackgroundColor());
        Dynamic.setColor(mFAB, getDynamicTheme().getAccentColor());

        Dynamic.setVisibility(mHeaderShadow,
                getDynamicTheme().isElevation() ? VISIBLE : INVISIBLE);
    }

    @Override
    public @NonNull View getActionView() {
        return getFAB();
    }

    /**
     * Get the status bar used by this preview.
     *
     * @return The status bar used by this preview.
     */
    public @Nullable ImageView getStatusBar() {
        return mStatusBar;
    }

    /**
     * Get the background image view used by this preview.
     *
     * @return The background image view used by this preview.
     */
    public @Nullable ImageView getBackgroundCard() {
        return mBackground;
    }

    /**
     * Get the header background used by this preview.
     *
     * @return The header background used by this preview.
     */
    public @Nullable ViewGroup getHeader() {
        return mHeader;
    }

    /**
     * Get the header icon used by this preview.
     *
     * @return The header title used by this preview.
     */
    public @Nullable ImageView getHeaderIcon() {
        return mHeaderIcon;
    }

    /**
     * Get the header title used by this preview.
     *
     * @return The header title used by this preview.
     */
    public @Nullable ImageView getHeaderTitle() {
        return mHeaderTitle;
    }

    /**
     * Get the header menu used by this preview.
     *
     * @return The header menu used by this preview.
     */
    public @Nullable ImageView getHeaderMenu() {
        return mHeaderMenu;
    }

    /**
     * Get the header shadow used by this preview.
     *
     * @return The header shadow used by this preview.
     */
    public @Nullable ImageView getHeaderShadow() {
        return mHeaderShadow;
    }

    /**
     * Get the icon used by this preview.
     *
     * @return The icon used by this preview.
     */
    public @Nullable ImageView getIcon() {
        return mIcon;
    }

    /**
     * Get the primary text used by this preview.
     *
     * @return The primary text used by this preview.
     */
    public @Nullable ImageView getTextPrimary() {
        return mTextPrimaryStart;
    }

    /**
     * Get the secondary text used by this preview.
     *
     * @return The secondary text used by this preview.
     */
    public @Nullable ImageView getTextSecondary() {
        return mTextSecondaryStart;
    }

    /**
     * Get the background tint text used by this preview.
     *
     * @return The background tint text used by this preview.
     */
    public @Nullable ImageView getTextTintBackground() {
        return mTextDescriptionStart;
    }

    /**
     * Get the FAB used by this preview.
     *
     * @return The FAB used by this preview.
     */
    public @Nullable FloatingActionButton getFAB() {
        return mFAB;
    }
}
