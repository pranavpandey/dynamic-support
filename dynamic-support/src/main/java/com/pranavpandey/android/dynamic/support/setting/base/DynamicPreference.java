/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.setting.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicView;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Base preference to provide the basic interface for the extending preference with an icon,
 * title, summary, description, value and an action button.
 *
 * <p>It must be extended and the necessary methods should be implemented to create a
 * dynamic preference.
 *
 * @see DynamicSimplePreference
 * @see DynamicScreenPreference
 * @see DynamicCheckPreference
 * @see DynamicImagePreference
 * @see DynamicSpinnerPreference
 * @see DynamicSeekBarPreference
 * @see DynamicColorPreference
 */
public abstract class DynamicPreference extends DynamicView
        implements SharedPreferences.OnSharedPreferenceChangeListener, DynamicWidget {

    /**
     * Listener to get various callbacks related to the popup and dialog.
     * <p>It will be useful if this preference is displaying a popup or dialog and we have
     * to restrict it from doing that.
     *
     * <p>Most possible situation is if we want to display the color picker dialog only
     * if user has purchased this feature.
     *
     * @see DynamicColorPreference
     * @see DynamicSpinnerPreference
     */
    public interface OnPromptListener {

        /**
         * Returns whether to allow this preference to show the corresponding popup.
         *
         * @return {@code true} to allow this preference to show the corresponding popup.
         */
        boolean onPopup();

        /**
         * This method will be called when a popup item is clicked.
         *
         * @param parent The adapter view displaying the items.
         * @param view The item view which has been clicked.
         * @param position The position of {@code view} inside the {@code parent}.
         * @param id The id of the {@code view}.
         */
        void onPopupItemClick(@Nullable AdapterView<?> parent,
                @Nullable View view, int position, long id);

        /**
         * Returns whether to allow this preference to show the corresponding dialog.
         *
         * @return {@code true} to allow this preference to show the corresponding dialog.
         */
        boolean onDialog();
    }

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int mContrastWithColorType;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The background aware functionality to change this view color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #mContrastWithColor
     */
    private @Theme.BackgroundAware int mBackgroundAware;

    /**
     * Default value for the enabled state.
     */
    public static final boolean DEFAULT_ENABLED = true;

    /**
     * Icon used by this preference.
     */
    private Drawable mIcon;

    /**
     * Title used by this preference.
     */
    private CharSequence mTitle;

    /**
     * Summary used by this preference.
     */
    private CharSequence mSummary;

    /**
     * Description used by this preference.
     */
    private CharSequence mDescription;

    /**
     * Value string used by this preference.
     */
    private CharSequence mValueString;

    /**
     * Shared preferences key for this preference.
     */
    private String mPreferenceKey;

    /**
     * Shared preferences key for alternate preference.
     */
    private String mAltPreferenceKey;

    /**
     * Shared preferences key on which this preference is dependent.
     */
    private String mDependency;

    /**
     * {@code true} if this preference is enabled and can accept click events.
     */
    private boolean mEnabled;

    /**
     * Action string used by this preference.
     */
    private CharSequence mActionString;

    /**
     * On click listener to receive preference click events.
     */
    private View.OnClickListener mOnPreferenceClickListener;

    /**
     * On click listener to receive action click events.
     */
    private View.OnClickListener mOnActionClickListener;

    /**
     * Listener to get various callbacks related to the popup and dialog.
     */
    private OnPromptListener mOnPromptListener;

    public DynamicPreference(@NonNull Context context) {
        this(context, null);
    }

    public DynamicPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @CallSuper
    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicPreference);

        try {
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicPreference_ads_contrastWithColorType,
                    Theme.ColorType.NONE);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicPreference_ads_contrastWithColor,
                    Theme.Color.UNKNOWN);
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicPreference_ads_backgroundAware,
                    Theme.BackgroundAware.UNKNOWN);
            mIcon = DynamicResourceUtils.getDrawable(getContext(),
                    a.getResourceId(R.styleable.DynamicPreference_ads_icon,
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE));
            mTitle = a.getString(R.styleable.DynamicPreference_ads_title);
            mSummary = a.getString(R.styleable.DynamicPreference_ads_summary);
            mDescription = a.getString(R.styleable.DynamicPreference_ads_description);
            mValueString = a.getString(R.styleable.DynamicPreference_ads_value);
            mPreferenceKey = a.getString(R.styleable.DynamicPreference_ads_key);
            mAltPreferenceKey = a.getString(R.styleable.DynamicPreference_ads_altKey);
            mDependency = a.getString(R.styleable.DynamicPreference_ads_dependency);
            mActionString = a.getString(R.styleable.DynamicPreference_ads_action);
            mEnabled = a.getBoolean(R.styleable.DynamicPreference_ads_enabled, DEFAULT_ENABLED);
        } finally {
            a.recycle();
        }
    }

    @CallSuper
    @Override
    protected void onUpdate() {
        setPreferenceListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initialize();
        update();
        setEnabled(mEnabled);
        updateDependency();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setPreferenceListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;

        super.setEnabled(enabled);
    }

    @Override
    public void initialize() {
        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor();
    }

    @Override
    public @Theme.ColorType int getColorType() {
        return Theme.ColorType.NONE;
    }

    @Override
    public void setColorType(@Theme.ColorType int colorType) {
        initialize();
    }

    @Override
    public @Theme.ColorType int getContrastWithColorType() {
        return mContrastWithColorType;
    }

    @Override
    public void setContrastWithColorType(@Theme.ColorType int contrastWithColorType) {
        this.mContrastWithColorType = contrastWithColorType;

        initialize();
    }

    @Override
    public @ColorInt int getColor(boolean resolve) {
        return Theme.Color.UNKNOWN;
    }

    @Override
    public @ColorInt int getColor() {
        return getColor(true);
    }

    @Override
    public void setColor(@ColorInt int color) {
        setColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setColor();
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return mBackgroundAware;
    }

    @Override
    public boolean isBackgroundAware() {
        return Dynamic.isBackgroundAware(this);
    }

    @Override
    public void setBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        this.mBackgroundAware = backgroundAware;

        setColor();
    }

    @Override
    public void setColor() { }

    /**
     * Manage the shared preferences listener according to the preference keys.
     */
    private void setPreferenceListener() {
        if (getPreferenceKey() != null || getAltPreferenceKey() != null
                || getDependency() != null) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .registerOnSharedPreferenceChangeListener(this);
        } else {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    /**
     * Set drawable for an image view.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable to be set.
     */
    protected void setImageView(@Nullable ImageView imageView, @Nullable Drawable drawable) {
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    /**
     * Set text for a text view or hide it if the value is {@code null}.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    protected void setTextView(@Nullable TextView textView, @Nullable CharSequence text) {
        Dynamic.set(textView, text);
    }

    /**
     * Update this preference according to the dependency.
     */
    private void updateDependency() {
        if (mDependency != null) {
            setEnabled(DynamicPreferences.getInstance().load(mDependency, isEnabled()));
        }
    }

    /**
     * Get the icon used by this preference.
     *
     * @return The icon used by this preference.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon used by this preference.
     *
     * @param icon The icon drawable to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting
     *               the icon.
     */
    public void setIcon(@Nullable Drawable icon, boolean update) {
        this.mIcon = icon;

        if (update) {
            update();
        }
    }

    /**
     * Set the icon used by this preference.
     *
     * @param icon The icon drawable to be set.
     */
    public void setIcon(@Nullable Drawable icon) {
        setIcon(icon, true);
    }

    /**
     * Get the title used by this preference.
     *
     * @return The title used by this preference.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by this preference.
     *
     * @param title The title to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting
     *               the title.
     */
    public void setTitle(@Nullable CharSequence title, boolean update) {
        this.mTitle = title;

        if (update) {
            update();
        }
    }

    /**
     * Set the title used by this preference.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        setTitle(title, true);
    }

    /**
     * Get the summary used by this preference.
     *
     * @return The summary used by this preference.
     */
    public @Nullable CharSequence getSummary() {
        return mSummary;
    }

    /**
     * Set the summary used by this preference.
     *
     * @param summary The summary to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting
     *               the summary.
     */
    public void setSummary(@Nullable CharSequence summary, boolean update) {
        this.mSummary = summary;

        if (update) {
            update();
        }
    }

    /**
     * Set the summary used by this preference.
     *
     * @param summary The summary to be set.
     */
    public void setSummary(@Nullable CharSequence summary) {
        setSummary(summary, true);
    }

    /**
     * Get the description used by this preference.
     *
     * @return The description used by this preference.
     */
    public @Nullable CharSequence getDescription() {
        return mDescription;
    }

    /**
     * Set the description used by this preference.
     *
     * @param description The description to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting
     *               the description.
     */
    public void setDescription(@Nullable CharSequence description, boolean update) {
        this.mDescription = description;

        if (update) {
            update();
        }
    }

    /**
     * Set the description used by this preference.
     *
     * @param description The description to be set.
     */
    public void setDescription(@Nullable CharSequence description) {
        setDescription(description, true);
    }

    /**
     * Get the value string used by this preference.
     *
     * @return The value string used by this preference.
     */
    public @Nullable CharSequence getValueString() {
        return mValueString;
    }

    /**
     * Set the value string used by this preference.
     *
     * @param valueString The value string to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting the
     *               value string.
     */
    public void setValueString(@Nullable CharSequence valueString, boolean update) {
        this.mValueString = valueString;

        if (update) {
            update();
        }
    }

    /**
     * Set the value string used by this preference.
     *
     * @param valueString The value string to be set.
     */
    public void setValueString(@Nullable CharSequence valueString) {
        setValueString(valueString, true);
    }

    /**
     * Get the shared preferences key used by this preference.
     *
     * @return The shared preferences key used by this preference.
     */
    public @Nullable String getPreferenceKey() {
        return mPreferenceKey;
    }

    /**
     * Set the shared preferences key used by this preference.
     *
     * @param preferenceKey The shared preferences key to be set.
     */
    public void setPreferenceKey(@Nullable String preferenceKey) {
        this.mPreferenceKey = preferenceKey;

        update();
    }

    /**
     * Get the shared preferences key for alternate preference.
     *
     * @return The shared preferences key for alternate preference.
     */
    public @Nullable String getAltPreferenceKey() {
        return mAltPreferenceKey;
    }

    /**
     * Set the shared preferences key for alternate preference.
     *
     * @param altPreferenceKey The alternate shared preferences key to be set.
     */
    public void setAltPreferenceKey(@Nullable String altPreferenceKey) {
        this.mAltPreferenceKey = altPreferenceKey;

        update();
    }

    /**
     * Returns the shared preferences key on which this preference is dependent.
     *
     * @return The shared preferences key on which this preference is dependent.
     */
    public @Nullable String getDependency() {
        return mDependency;
    }

    /**
     * Set the shared preferences key on which this preference is dependent.
     *
     * @param dependency The shared preferences key to be set.
     */
    public void setDependency(@Nullable String dependency) {
        this.mDependency = dependency;

        updateDependency();
    }

    /**
     * Get the action string used by this preference.
     *
     * @return The action string used by this preference.
     */
    public @Nullable CharSequence getActionString() {
        return mActionString;
    }

    /**
     * Returns the on click listener to receive preference click events.
     *
     * @return The on click listener to receive preference click events.
     */
    public @Nullable View.OnClickListener getOnPreferenceClickListener() {
        return mOnPreferenceClickListener;
    }

    /**
     * Set the on click listener to receive preference click events.
     *
     * @param onPreferenceClickListener The listener to be set.
     * @param update {@code true} to call {@link #onUpdate()} method after setting the listener.
     */
    public void setOnPreferenceClickListener(
            @Nullable View.OnClickListener onPreferenceClickListener, boolean update) {
        this.mOnPreferenceClickListener = onPreferenceClickListener;

        if (update) {
            update();
        }
    }

    /**
     * Set the on click listener to receive preference click events.
     *
     * @param onPreferenceClickListener The listener to be set.
     */
    public void setOnPreferenceClickListener(
            @Nullable View.OnClickListener onPreferenceClickListener) {
        setOnPreferenceClickListener(onPreferenceClickListener, true);
    }

    /**
     * Set an action button for this preference to perform secondary operations like requesting
     * a permission, reset the preference value etc.
     *
     * <p>Extending preference should implement the functionality in {@link #onUpdate()}
     * method.
     *
     * @param actionString The string to be shown for the action.
     * @param onActionClickListener The on click listener to perform the action when it is clicked.
     * @param update {@code true} to call {@link #onUpdate()} method after setting the listener.
     */
    public void setActionButton(@Nullable CharSequence actionString,
            @Nullable OnClickListener onActionClickListener, boolean update) {
        this.mActionString = actionString;
        this.mOnActionClickListener = onActionClickListener;

        if (update) {
            update();
        }
    }

    /**
     * Set an action button for this preference to perform secondary operations like requesting
     * a permission, reset the preference value etc.
     *
     * <p>Extending preference should implement the functionality in {@link #onUpdate()}
     * method.
     *
     * @param actionString The string to be shown for the action.
     * @param onActionClickListener The on click listener to perform the action when it is clicked.
     */
    public void setActionButton(@Nullable CharSequence actionString,
            @Nullable OnClickListener onActionClickListener) {
        setActionButton(actionString, onActionClickListener, true);
    }

    /**
     * Returns the on click listener to receive action click events.
     *
     * @return The on click listener to receive action click events.
     */
    public @Nullable OnClickListener getOnActionClickListener() {
        return mOnActionClickListener;
    }

    /**
     * Returns the listener to get the popup and dialog dialogs.
     *
     * @return The listener to get the popup and dialog dialogs.
     */
    public @Nullable OnPromptListener getOnPromptListener() {
        return mOnPromptListener;
    }

    /**
     * Set the listener to get various callbacks related to the popup and dialog.
     * <p>It will be useful if this preference is displaying a popup or dialog and we have
     * to restrict it from doing that.
     *
     * @param onPromptListener The listener to be set.
     */
    public void setOnPromptListener(@Nullable OnPromptListener onPromptListener) {
        this.mOnPromptListener = onPromptListener;
    }

    /**
     * Get the preference root view.
     *
     * @return The preference root view.
     */
    public abstract @Nullable ViewGroup getPreferenceView();

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(mDependency)) {
            setEnabled(DynamicPreferences.getInstance().load(key, isEnabled()));
        }
    }
}
