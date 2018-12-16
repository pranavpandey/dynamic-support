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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.preference.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.view.DynamicThemePreview;

/**
 * A DynamicSpinnerPreference to display and edit the dynamic theme.
 */
public class DynamicThemePreference extends DynamicSpinnerPreference {

    /**
     * Default theme value for this preference.
     */
    private String mDefaultTheme;

    /**
     * Current theme value for this preference.
     */
    private String mTheme;

    /**
     * Current dynamic theme value for this preference.
     */
    private DynamicAppTheme mDynamicAppTheme;

    /**
     * {@code true} if theme preview is enabled.
     */
    private boolean mThemeEnabled;

    /**
     * Theme preview used by this preference.
     */
    private DynamicThemePreview mThemePreview;

    /**
     * Theme preview icon used by this preference.
     */
    private ImageView mThemePreviewIcon;

    /**
     * Theme preview description used by this preference.
     */
    private TextView mThemePreviewDescription;

    /**
     * On click listener to receive theme click events.
     */
    private View.OnClickListener mOnThemeClickListener;

    public DynamicThemePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicThemePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicThemePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        super.onLoadAttributes(attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicPreference);

        try {
            mDefaultTheme = a.getString(
                    R.styleable.DynamicPreference_ads_theme);
        } finally {
            a.recycle();
        }

        if (mDefaultTheme == null) {
            mDefaultTheme = DynamicTheme.getInstance().get().toJsonString();
        }

        mThemeEnabled = true;
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_theme;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        mThemePreview = findViewById(R.id.ads_theme_preview);
        mThemePreviewIcon = findViewById(R.id.ads_theme_preview_icon);
        mThemePreviewDescription = findViewById(R.id.ads_theme_preview_description);
    }

    @Override
    public @Nullable String getPreferenceKey() {
        return getAltPreferenceKey();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        mTheme = DynamicPreferences.getInstance()
                .loadPrefs(super.getPreferenceKey(), mDefaultTheme);
        mDynamicAppTheme = DynamicTheme.getInstance().getTheme(mTheme);

        if (mDynamicAppTheme != null) {
            mThemePreview.setDynamicAppTheme(mDynamicAppTheme);
            mThemePreviewDescription.setVisibility(
                    mDynamicAppTheme.isBackgroundAware() ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        mThemePreview.setEnabled(enabled && mThemeEnabled);
        mThemePreviewIcon.setEnabled(enabled && mThemeEnabled);
        mThemePreviewDescription.setEnabled(enabled && mThemeEnabled);
    }

    /**
     * Enable or disable the theme preview.
     *
     * @param enabled {@code true} to enable the theme preview.
     */
    public void setThemeEnabled(boolean enabled) {
        this.mThemeEnabled = enabled;

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

        onUpdate();
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

        if (getPreferenceKey() != null && save) {
            DynamicPreferences.getInstance().savePrefs(getPreferenceKey(), theme);
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
    public OnClickListener getOnThemeClickListener() {
        return mOnThemeClickListener;
    }

    /**
     * Set the on click listener for theme to receive the click events and to perform
     * edit operation.
     *
     * @param onThemeClickListener The on click listener to be set.
     */
    public void setOnThemeClickListener(@Nullable View.OnClickListener onThemeClickListener) {
        this.mOnThemeClickListener = onThemeClickListener;

        mThemePreview.setOnFABClickListener(mOnThemeClickListener);
        onEnabled(isEnabled());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(super.getPreferenceKey())) {
            mTheme = DynamicPreferences.getInstance()
                    .loadPrefs(super.getPreferenceKey(), mTheme);

            onUpdate();
        }
    }
}
