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

package com.pranavpandey.android.dynamic.support.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

/**
 * Base preference to provide the basic interface for the extending preference with an icon,
 * title, summary, description, value and an action button.
 *
 * <p><p>It must be extended and the necessary methods should be implemented to create a
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
public abstract class DynamicPreference extends FrameLayout
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Listener to get various callbacks related to the popup and dialog.
     * <p>It will be useful if this preference is displaying a popup or dialog and we have
     * to restrict it from doing that.
     *
     * <p><p>Most possible situation is if we want to display the color picker dialog only
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

        loadFromAttributes(attrs);
    }

    public DynamicPreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    private void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicPreference);

        try {
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

        onLoadAttributes(attrs);
        onInflate();
        onUpdate();
        setEnabled(mEnabled);
        updateDependency();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The attribute set to load the values.
     */
    protected abstract void onLoadAttributes(@Nullable AttributeSet attrs);

    /**
     * Returns the layout resource id for this preference.
     *
     * @return The layout resource id for this preference.
     */
    protected abstract @LayoutRes int getLayoutRes();

    /**
     * This method will be called after loading the attributed.
     * <p>Initialize the preference layout here.
     */
    protected abstract void onInflate();

    /**
     * This method will be called whenever there is a change in the preference attributes
     * or parameters.
     * <p>It is better to do any real time calculation like updating the value string
     * or checked state in this method.
     */
    protected abstract void onUpdate();

    /**
     * This method will be called whenever there is a change in the preference view state.
     * <p>Either {@code enabled} or {@code disabled}, preference views like icon, title, value,
     * etc. must be updated here to reflect the overall preference state.
     *
     * @param enabled {@code true} if this widget is enabled and can receive click events.
     */
    protected abstract void onEnabled(boolean enabled);

    /**
     * Set drawable for a image view.
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
        if (textView != null) {
            if (text != null) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Set a view enabled or disabled.
     *
     * @param view The view to be enabled or disabled.
     * @param enabled {@code true} to enable the view.
     */
    protected void setViewEnabled(@Nullable View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    /**
     * Update this preference according to the dependency.
     */
    private void updateDependency() {
        if (mDependency != null) {
            setEnabled(DynamicPreferences.getInstance()
                    .load(mDependency, isEnabled()));
        }
    }

    /**
     * Manually onUpdate this preference by calling {@link #onUpdate()} method.
     * <p>Useful in some situations to restore the preference state.
     */
    public void update() {
        onUpdate();
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
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        onUpdate();
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
     */
    public void setTitle(@Nullable String title) {
        this.mTitle = title;

        onUpdate();
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
     */
    public void setSummary(@Nullable String summary) {
        this.mSummary = summary;

        onUpdate();
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
     */
    public void setDescription(@Nullable String description) {
        this.mDescription = description;

        onUpdate();
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
            onUpdate();
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

        onUpdate();
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

        onUpdate();
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
     * Set this preference enabled or disabled.
     *
     * @param enabled {@code true} if this preference is enabled.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.mEnabled = enabled;
        onEnabled(enabled);
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
            onUpdate();
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
     * <p><p>Extending preference should implement the functionality in {@link #onUpdate()}
     * method.
     *
     * @param actionString The string to be shown for the action.
     * @param onActionClickListener The on click listener to perform the action when it is clicked.
     */
    public void setActionButton(@Nullable CharSequence actionString,
            @Nullable OnClickListener onActionClickListener) {
        this.mActionString = actionString;
        this.mOnActionClickListener = onActionClickListener;

        onUpdate();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(mDependency)) {
            setEnabled(DynamicPreferences.getInstance().load(key, isEnabled()));
        }
    }
}
