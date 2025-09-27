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

package com.pranavpandey.android.dynamic.support.setting.theme.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicThemeResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.view.base.ThemePreview;
import com.pranavpandey.android.dynamic.theme.AppTheme;

/**
 * A {@link DynamicSpinnerPreference} to display and edit the {@link DynamicAppTheme}.
 *
 * @param <T> The type of the dynamic app theme this preference will handle.
 */
public abstract class ThemePreference<T extends AppTheme<?>>
        extends DynamicSpinnerPreference implements DynamicThemeResolver<T> {

    /**
     * Default theme value for this preference.
     */
    protected String mDefaultTheme;

    /**
     * Current theme value for this preference.
     */
    protected String mTheme;

    /**
     * Current dynamic theme value for this preference.
     */
    protected T mDynamicTheme;

    /**
     * {@code true} if the theme preview is enabled.
     */
    protected boolean mThemePreviewEnabled;

    /**
     * Theme preview used by this preference.
     */
    protected ThemePreview<T> mThemePreview;

    /**
     * Theme preview icon used by this preference.
     */
    protected ImageView mThemePreviewIcon;

    /**
     * Theme preview description used by this preference.
     */
    protected TextView mThemePreviewDescription;

    /**
     * On click listener to receive theme click events.
     */
    protected OnClickListener mOnThemeClickListener;

    public ThemePreference(@NonNull Context context) {
        super(context);
    }

    public ThemePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.ThemePreference);

        try {
            mDefaultTheme = a.getString(R.styleable.DynamicThemePreference_adt_theme);
        } finally {
            a.recycle();
        }

        mDefaultTheme = getDefaultTheme(mDefaultTheme);
        mThemePreviewEnabled = true;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mThemePreview = findViewById(R.id.ads_theme_preview);
        mThemePreviewIcon = findViewById(R.id.ads_theme_preview_icon);
        mThemePreviewDescription = findViewById(R.id.ads_theme_preview_description);

        Dynamic.setText(mThemePreviewDescription, R.string.ads_theme_background_aware_desc);
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return super.getAltPreferenceKey();
    }

    @Override
    public @Nullable String getAltPreferenceKey() {
        return super.getPreferenceKey();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        mTheme = DynamicPreferences.getInstance().load(getAltPreferenceKey(), getDefaultTheme());
        mDynamicTheme = getDynamicTheme(getTheme());

        if (getDynamicTheme() != null) {
            mTheme = getDynamicTheme().toJsonString();
            getThemePreview().setDynamicTheme(getDynamicTheme());
            Dynamic.setVisibility(getThemePreviewDescription(),
                    getDynamicTheme().isBackgroundAware() ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getThemePreview(), enabled && isThemePreviewEnabled());
        Dynamic.setEnabled(getThemePreviewIcon(), enabled && isThemePreviewEnabled());
        Dynamic.setEnabled(getThemePreviewDescription(), enabled && isThemePreviewEnabled());
    }

    @Override
    public void setColor() {
        super.setColor();

        Dynamic.setContrastWithColorTypeOrColor(getThemePreviewIcon(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getThemePreviewDescription(),
                getContrastWithColorType(), getContrastWithColor());

        Dynamic.setBackgroundAwareSafe(getThemePreviewIcon(),
                getBackgroundAware(), getContrast(false));
        Dynamic.setBackgroundAwareSafe(getThemePreviewDescription(),
                getBackgroundAware(), getContrast(false));
    }

    /**
     * Returns the current dynamic theme value for this preference.
     *
     * @return The current dynamic theme value for this preference.
     */
    public @Nullable T getDynamicTheme() {
        return mDynamicTheme;
    }

    /**
     * Set the current dynamic theme value for this preference.
     *
     * @param theme The current dynamic theme value to be set.
     */
    public void setDynamicTheme(@Nullable T theme) {
        this.mDynamicTheme = theme;

        update();
    }

    /**
     * Checks whether the theme preview is enabled.
     *
     * @return {@code true} if the theme preview is enabled.
     */
    public boolean isThemePreviewEnabled() {
        return mThemePreviewEnabled;
    }

    /**
     * Enable or disable the theme preview.
     *
     * @param enabled {@code true} to enable the theme preview.
     */
    public void setThemePreviewEnabled(boolean enabled) {
        this.mThemePreviewEnabled = enabled;

        setEnabled(isEnabled());
    }

    /**
     * Get the default theme value for this preference.
     *
     * @return The default theme value for this preference.
     */
    public @Nullable String getDefaultTheme() {
        return mDefaultTheme;
    }

    /**
     * Set the default theme value for this preference.
     *
     * @param defaultTheme The default theme value to be set.
     */
    public void setDefaultTheme(@NonNull String defaultTheme) {
        this.mDefaultTheme = defaultTheme;

        update();
    }

    /**
     * Get the current theme value for this preference.
     *
     * @return The current theme value for this preference.
     */
    public @Nullable String getTheme() {
        return mTheme;
    }

    /**
     * Set the current theme value of this preference.
     *
     * @param theme The theme value to be set.
     * @param save {@code true} to update the shared preferences.
     */
    public void setTheme(@NonNull String theme, boolean save) {
        this.mTheme = theme;

        if (save) {
            DynamicPreferences.getInstance().save(getPreferenceKey(), theme);
        }
    }

    /**
     * Set the current theme value of this preference.
     *
     * @param theme The theme value to be set.
     */
    public void setTheme(@NonNull String theme) {
        setTheme(theme, true);
    }

    /**
     * Returns the on click listener to receive theme click events.
     *
     * @return The on click listener to receive theme click events.
     */
    public @Nullable OnClickListener getOnThemeClickListener() {
        return mOnThemeClickListener;
    }

    /**
     * Set the on click listener for theme to receive the click events and to perform
     * edit operation.
     *
     * @param onThemeClickListener The on click listener to be set.
     */
    public void setOnThemeClickListener(@Nullable OnClickListener onThemeClickListener) {
        this.mOnThemeClickListener = onThemeClickListener;

        getThemePreview().setOnActionClickListener(getOnThemeClickListener());
        onEnabled(isEnabled());
    }

    /**
     * Get the theme preview used by this preference.
     *
     * @return The theme preview used by this preference.
     */
    public @NonNull ThemePreview<T> getThemePreview() {
        return mThemePreview;
    }

    /**
     * Get the root view for the theme preview used by this preference.
     *
     * @return The root view for the theme preview used by this preference.
     */
    public @NonNull ViewGroup getThemePreviewRoot() {
        return findViewById(R.id.ads_theme_preview_root);
    }

    /**
     * Get the theme preview icon used by this preference.
     *
     * @return The theme preview icon used by this preference.
     */
    public @Nullable ImageView getThemePreviewIcon() {
        return mThemePreviewIcon;
    }

    /**
     * Get the theme preview description used by this preference.
     *
     * @return The theme preview description used by this preference.
     */
    public @Nullable TextView getThemePreviewDescription() {
        return mThemePreviewDescription;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            @Nullable String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(getAltPreferenceKey())) {
            update();
        }
    }
}
