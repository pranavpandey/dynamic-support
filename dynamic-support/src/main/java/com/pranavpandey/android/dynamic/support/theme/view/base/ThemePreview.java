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
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicRemoteTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.view.base.DynamicView;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicView} to show the theme preview according to the {@link DynamicAppTheme}.
 *
 * @param <T> The type of the dynamic app theme this preview will handle.
 */
public abstract class ThemePreview<T extends AppTheme<?>> extends DynamicView {

    /**
     * Dynamic app theme used by this preview.
     */
    private T mDynamicTheme;

    /**
     * On click listener to receive action view click events.
     */
    private View.OnClickListener mOnActionClickListener;

    public ThemePreview(@NonNull Context context) {
        super(context);
    }

    public ThemePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemePreview(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        this.mDynamicTheme = getDefaultTheme();
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);

        Dynamic.setEnabled(getActionView(), enabled);
        Dynamic.setOnClickListener(getActionView(), enabled ? mOnActionClickListener : null);
        Dynamic.setClickable(getActionView(), enabled && mOnActionClickListener != null);
    }

    /**
     * Returns the default dynamic theme used by this preview.
     *
     * @return The default dynamic theme used by this preview.
     */
    public abstract @NonNull T getDefaultTheme();

    /**
     * Get the dynamic theme used by this preview.
     *
     * @return The dynamic theme used by this preview.
     */
    public @NonNull T getDynamicTheme() {
        return mDynamicTheme;
    }

    /**
     * Set the dynamic theme used by this preview.
     *
     * @param theme the dynamic theme to be set.
     */
    public void setDynamicTheme(@NonNull T theme) {
        this.mDynamicTheme = theme;

        onUpdate();
    }

    /**
     * Get the dynamic theme type used by this preview.
     *
     * @return The dynamic theme type used by this preview.
     */
    public @Theme int getDynamicThemeType() {
        if (getDynamicTheme() instanceof DynamicRemoteTheme) {
            return Theme.REMOTE;
        } else if (getDynamicTheme() instanceof DynamicWidgetTheme) {
            return Theme.WIDGET;
        } else {
            return Theme.APP;
        }
    }

    @Override
    public @Nullable View getBackgroundView() {
        return null;
    }

    /**
     * Returns the action view used by this preview.
     *
     * @return The action view used by this preview.
     */
    public abstract @NonNull View getActionView();

    /**
     * Get the on click listener to receive action view click events.
     *
     * @return The on click listener to receive action view click events.
     */
    public @Nullable OnClickListener getOnActionClickListener() {
        return mOnActionClickListener;
    }

    /**
     * Set the on click listener for action view to receive the click events and to perform edit
     * operation.
     *
     * @param onActionClickListener The on click listener to be set.
     */
    public void setOnActionClickListener(@Nullable View.OnClickListener onActionClickListener) {
        this.mOnActionClickListener = onActionClickListener;

        getActionView().setOnClickListener(mOnActionClickListener);
        onEnabled(isEnabled());
    }
}
