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

package com.pranavpandey.android.dynamic.support.theme.view;

import android.content.Context;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicShapeUtils;
import com.pranavpandey.android.dynamic.support.widget.Dynamic;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * A ThemePreview to show the dynamic app theme preview according to the selected values.
 */
public class DynamicThemePreview extends ThemePreview<DynamicAppTheme> {

    /**
     * Background image view used by this preview.
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
    private ImageView mTextTintBackgroundStart;

    /**
     * Background tint text end used by this preview.
     */
    private ImageView mTextTintBackgroundEnd;

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
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);
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
        mHeaderTitle = findViewById(R.id.ads_theme_header_title);
        mHeaderMenu = findViewById(R.id.ads_theme_header_menu);
        mContent = findViewById(R.id.ads_theme_content);
        mSurface = findViewById(R.id.ads_theme_content_start);
        mIcon = findViewById(R.id.ads_theme_icon);
        mTextPrimaryStart = findViewById(R.id.ads_theme_text_primary_start);
        mTextPrimaryEnd = findViewById(R.id.ads_theme_text_primary_end);
        mTextSecondaryStart = findViewById(R.id.ads_theme_text_secondary_start);
        mTextSecondaryEnd = findViewById(R.id.ads_theme_text_secondary_end);
        mTextTintBackgroundStart = findViewById(R.id.ads_theme_text_tint_background_start);
        mTextTintBackgroundEnd = findViewById(R.id.ads_theme_text_tint_background_end);
        mFAB = findViewById(R.id.ads_theme_fab);
    }

    @Override
    protected void onUpdate() {
        MaterialShapeDrawable background = (MaterialShapeDrawable)
                DynamicShapeUtils.getCornerDrawable(
                        getDynamicTheme().getCornerSizeDp(),
                        getDynamicTheme().getBackgroundColor(), false);
        background.setStroke(WidgetDefaults.ADS_STROKE_WIDTH,
                DynamicColorUtils.setAlpha(DynamicColorUtils.getTintColor(
                        getDynamicTheme().getBackgroundColor()), 100));
        mBackground.setImageDrawable(background);
        DynamicDrawableUtils.setBackground(mStatusBar,
                DynamicShapeUtils.getCornerDrawable(
                        getDynamicTheme().getCornerSizeDp(),
                        getDynamicTheme().getPrimaryColorDark(), false, true));
        mHeader.setBackgroundColor(getDynamicTheme().getPrimaryColor());

        MaterialShapeDrawable drawable = (MaterialShapeDrawable)
                DynamicShapeUtils.getCornerDrawable(
                        getDynamicTheme().getCornerSizeDp(),
                        getDynamicTheme().getSurfaceColor(), false, true);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        if (DynamicLocaleUtils.isLayoutRtl()) {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setBottomRightCornerSize(
                    drawable.getShapeAppearanceModel().getTopLeftCornerSize()).build();
        } else {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setBottomLeftCornerSize(
                    drawable.getShapeAppearanceModel().getTopLeftCornerSize()).build();
        }
        drawable.setShapeAppearanceModel(shapeAppearanceModel);

        DynamicDrawableUtils.setBackground(mSurface, drawable);

        if (getDynamicTheme().getCornerSizeDp() < WidgetDefaults.ADS_CORNER_MIN_THEME) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay);
            mTextPrimaryStart.setImageResource(R.drawable.ads_theme_overlay);
            mTextPrimaryEnd.setImageResource(R.drawable.ads_theme_overlay);
            mTextSecondaryStart.setImageResource(R.drawable.ads_theme_overlay);
            mTextSecondaryEnd.setImageResource(R.drawable.ads_theme_overlay);
            mTextTintBackgroundStart.setImageResource(R.drawable.ads_theme_overlay);
            mTextTintBackgroundEnd.setImageResource(R.drawable.ads_theme_overlay);
        } else if (getDynamicTheme().getCornerSizeDp()
                < WidgetDefaults.ADS_CORNER_MIN_THEME_ROUND) {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_rect);
            mTextPrimaryStart.setImageResource(R.drawable.ads_theme_overlay_rect_start);
            mTextPrimaryEnd.setImageResource(R.drawable.ads_theme_overlay_rect_end);
            mTextSecondaryStart.setImageResource(R.drawable.ads_theme_overlay_rect_start);
            mTextSecondaryEnd.setImageResource(R.drawable.ads_theme_overlay_rect_end);
            mTextTintBackgroundStart.setImageResource(R.drawable.ads_theme_overlay_rect_start);
            mTextTintBackgroundEnd.setImageResource(R.drawable.ads_theme_overlay_rect_end);
        } else {
            mHeaderTitle.setImageResource(R.drawable.ads_theme_overlay_round);
            mTextPrimaryStart.setImageResource(R.drawable.ads_theme_overlay_round_start);
            mTextPrimaryEnd.setImageResource(R.drawable.ads_theme_overlay_round_end);
            mTextSecondaryStart.setImageResource(R.drawable.ads_theme_overlay_round_start);
            mTextSecondaryEnd.setImageResource(R.drawable.ads_theme_overlay_round_end);
            mTextTintBackgroundStart.setImageResource(R.drawable.ads_theme_overlay_round_start);
            mTextTintBackgroundEnd.setImageResource(R.drawable.ads_theme_overlay_round_end);
        }

        post(new Runnable() {
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

        Dynamic.setBackgroundAware(mHeaderIcon, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mHeaderTitle, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mHeaderMenu, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mIcon, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextPrimaryStart, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextPrimaryEnd, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextSecondaryStart, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextSecondaryEnd, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextTintBackgroundStart, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mTextTintBackgroundEnd, getDynamicTheme().getBackgroundAware());
        Dynamic.setBackgroundAware(mFAB, getDynamicTheme().getBackgroundAware());

        Dynamic.setContrastWithColor(mHeaderIcon, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mHeaderTitle, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mHeaderMenu, getDynamicTheme().getPrimaryColor());
        Dynamic.setContrastWithColor(mIcon, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextPrimaryStart, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextPrimaryEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextSecondaryStart, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextSecondaryEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextTintBackgroundStart, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mTextTintBackgroundEnd, getDynamicTheme().getBackgroundColor());
        Dynamic.setContrastWithColor(mFAB, getDynamicTheme().getBackgroundColor());

        Dynamic.setColor(mHeaderIcon, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mHeaderTitle, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mHeaderMenu, getDynamicTheme().getTintPrimaryColor());
        Dynamic.setColor(mIcon, getDynamicTheme().getPrimaryColor());
        Dynamic.setColor(mTextPrimaryStart, getDynamicTheme().getTextPrimaryColor());
        Dynamic.setColor(mTextPrimaryEnd, getDynamicTheme().getTextPrimaryColor());
        Dynamic.setColor(mTextSecondaryStart, getDynamicTheme().getTextSecondaryColor());
        Dynamic.setColor(mTextSecondaryEnd, getDynamicTheme().getTextSecondaryColor());
        Dynamic.setColor(mTextTintBackgroundStart, getDynamicTheme().getTintBackgroundColor());
        Dynamic.setColor(mTextTintBackgroundEnd, getDynamicTheme().getTintBackgroundColor());
        Dynamic.setColor(mFAB, getDynamicTheme().getAccentColor());
    }

    @Override
    public @NonNull ImageView getActionView() {
        return getFAB();
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
     * Get the background image view used by this preview.
     *
     * @return The background image view used by this preview.
     */
    public ImageView getBackgroundCard() {
        return mBackground;
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
    public ImageView getHeaderIcon() {
        return mHeaderIcon;
    }

    /**
     * Get the header title used by this preview.
     *
     * @return The header title used by this preview.
     */
    public ImageView getHeaderTitle() {
        return mHeaderTitle;
    }

    /**
     * Get the header menu used by this preview.
     *
     * @return The header menu used by this preview.
     */
    public ImageView getHeaderMenu() {
        return mHeaderMenu;
    }

    /**
     * Get the icon used by this preview.
     *
     * @return The icon used by this preview.
     */
    public ImageView getIcon() {
        return mIcon;
    }

    /**
     * Get the primary text used by this preview.
     *
     * @return The primary text used by this preview.
     */
    public ImageView getTextPrimary() {
        return mTextPrimaryStart;
    }

    /**
     * Ge the secondary text used by this preview.
     *
     * @return The secondary text used by this preview.
     */
    public ImageView getTextSecondary() {
        return mTextSecondaryStart;
    }

    /**
     * Get the background tint text used by this preview.
     *
     * @return The background tint text used by this preview.
     */
    public ImageView getTextTintBackground() {
        return mTextTintBackgroundStart;
    }

    /**
     * Get the FAB used by this preview.
     *
     * @return The FAB used by this preview.
     */
    public FloatingActionButton getFAB() {
        return mFAB;
    }
}
