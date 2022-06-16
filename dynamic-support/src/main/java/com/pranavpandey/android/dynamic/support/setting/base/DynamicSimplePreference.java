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

package com.pranavpandey.android.dynamic.support.setting.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A {@link DynamicPreference} to implement the basic functionality
 *
 * <p>It can be extended to modify according to the need.
 */
public class DynamicSimplePreference extends DynamicPreference {

    /**
     * The preference root view.
     */
    private ViewGroup mPreferenceView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Image view to show the footer icon.
     */
    private ImageView mIconFooterView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the summary.
     */
    private TextView mSummaryView;

    /**
     * Text view to show the description.
     */
    private TextView mDescriptionView;

    /**
     * Text view to show the value.
     */
    private TextView mValueView;

    /**
     * Frame to add a secondary view like another image, etc.
     */
    private ViewGroup mViewFrame;

    /**
     * Button to provide a secondary action like permission request, etc.
     */
    private Button mActionView;

    public DynamicSimplePreference(@NonNull Context context) {
        super(context);
    }

    public DynamicSimplePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSimplePreference(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_preference_simple;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mPreferenceView = findViewById(R.id.ads_preference);
        mIconView = findViewById(R.id.ads_preference_icon);
        mIconFooterView = findViewById(R.id.ads_preference_icon_footer);
        mTitleView = findViewById(R.id.ads_preference_title);
        mSummaryView = findViewById(R.id.ads_preference_summary);
        mDescriptionView = findViewById(R.id.ads_preference_description);
        mValueView = findViewById(R.id.ads_preference_value);
        mViewFrame = findViewById(R.id.ads_preference_view);
        mActionView = findViewById(R.id.ads_preference_action_button);

        setViewFrame(null, true);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        setIcon(getIcon(), false);
        setTitle(getTitle(), false);
        setSummary(getSummary(), false);
        setValueString(getValueString(), false);
        setDescription(getDescription(), false);

        setOnPreferenceClickListener(getOnPreferenceClickListener(), false);
        setActionButton(getActionString(), getOnActionClickListener(), false);
        Dynamic.setVisibility(getIconFooterView(), getIconView());
    }

    @Override
    protected void onEnabled(boolean enabled) {
        super.onEnabled(enabled);

        Dynamic.setEnabled(getPreferenceView(), enabled);
        Dynamic.setEnabled(getIconView(), enabled);
        Dynamic.setEnabled(getTitleView(), enabled);
        Dynamic.setEnabled(getSummaryView(), enabled);
        Dynamic.setEnabled(getDescriptionView(), enabled);
        Dynamic.setEnabled(getValueView(), enabled);
        Dynamic.setEnabled(getActionView(), enabled);
        Dynamic.setEnabled(getIconFooterView(), enabled);

        if (getViewFrame() != null && getViewFrame().getChildCount() > 0) {
            Dynamic.setEnabled(getViewFrame().getChildAt(0), enabled);
        }

        update();
    }

    @Override
    public void setIcon(@Nullable Drawable icon, boolean update) {
        super.setIcon(icon, update);

        if (!update) {
            setImageView(getIconView(), icon);
        }
    }

    @Override
    public void setTitle(@Nullable CharSequence title, boolean update) {
        super.setTitle(title, update);

        if (!update) {
            setTextView(getTitleView(), title);
        }
    }

    @Override
    public void setSummary(@Nullable CharSequence summary, boolean update) {
        super.setSummary(summary, update);

        if (!update) {
            setTextView(getSummaryView(), summary);
        }
    }

    @Override
    public void setDescription(@Nullable CharSequence description, boolean update) {
        super.setDescription(description, update);

        if (!update) {
            setTextView(getDescriptionView(), description);
        }
    }

    @Override
    public void setValueString(@Nullable CharSequence valueString, boolean update) {
        super.setValueString(valueString, update);

        if (!update) {
            setTextView(getValueView(), valueString);
        }
    }

    @Override
    public void setOnPreferenceClickListener(
            @Nullable OnClickListener onPreferenceClickListener, boolean update) {
        super.setOnPreferenceClickListener(onPreferenceClickListener, update);

        if (!update) {
            Dynamic.setClickListener(getPreferenceView(), onPreferenceClickListener);
        }
    }

    @Override
    public void setActionButton(@Nullable CharSequence actionString,
            @Nullable OnClickListener onActionClickListener, boolean update) {
        super.setActionButton(actionString, onActionClickListener, update);

        if (!update) {
            setTextView(getActionView(), actionString);
            Dynamic.setClickListener(getActionView(), onActionClickListener, true);
        }
    }

    @Override
    public void setColor() {
        super.setColor();

        Dynamic.setContrastWithColorTypeOrColor(getIconView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getTitleView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getSummaryView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getDescriptionView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getValueView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getActionView(),
                getContrastWithColorType(), getContrastWithColor());
        Dynamic.setContrastWithColorTypeOrColor(getIconFooterView(),
                getContrastWithColorType(), getContrastWithColor());

        Dynamic.setBackgroundAwareSafe(getIconView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getTitleView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getSummaryView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getDescriptionView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getValueView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getActionView(), getBackgroundAware());
        Dynamic.setBackgroundAwareSafe(getIconFooterView(), getBackgroundAware());
    }

    @Override
    public @Nullable ViewGroup getPreferenceView() {
        return mPreferenceView;
    }

    /**
     * Get the image view to show the icon.
     *
     * @return The image view to show the icon.
     */
    public @Nullable ImageView getIconView() {
        return mIconView;
    }

    /**
     * Get the image view to show the footer icon.
     *
     * @return The image view to show the footer icon.
     */
    public @Nullable ImageView getIconFooterView() {
        return mIconFooterView;
    }

    /**
     * Get the text view to show the title.
     *
     * @return The text view to show the title.
     */
    public @Nullable TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the text view to show the summary.
     *
     * @return The text view to show the summary.
     */
    public @Nullable TextView getSummaryView() {
        return mSummaryView;
    }

    /**
     * Get the text view to show the value.
     *
     * @return The text view to show the value.
     */
    public @Nullable TextView getValueView() {
        return mValueView;
    }

    /**
     * Get the text view to show the description.
     *
     * @return The text view to show the description.
     */
    public @Nullable TextView getDescriptionView() {
        return mDescriptionView;
    }

    /**
     * Get the text view to show the title.
     *
     * @return The text view to show the title.
     */
    public @Nullable ViewGroup getViewFrame() {
        return mViewFrame;
    }

    /**
     * Add a view in the view frame.
     *
     * @param view The view to be added.
     * @param removePrevious {@code true} to remove all the previous views of the view group.
     */
    public void setViewFrame(@Nullable View view, boolean removePrevious) {
        if (getViewFrame() != null) {
            if (view != null) {
                Dynamic.setVisibility(getViewFrame(), View.VISIBLE);
                DynamicViewUtils.addView(getViewFrame(), view, removePrevious);
            } else {
                Dynamic.setVisibility(getViewFrame(), View.GONE);
            }
        }
    }

    /**
     * Get the button to provide a secondary action like permission request, etc.
     *
     * @return The button to provide a secondary action like permission request, etc.
     */
    public @Nullable Button getActionView() {
        return mActionView;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        if (key.equals(getPreferenceKey())) {
            update();
        }
    }
}
