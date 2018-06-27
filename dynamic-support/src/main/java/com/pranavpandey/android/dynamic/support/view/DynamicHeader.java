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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;

/**
 * A FrameLayout to provide the header view with a icon, title
 * and subtitle. It will be used at various locations like for
 * settings category header, popup title, etc.
 */
public class DynamicHeader extends FrameLayout {

    /**
     * Icon used by this view.
     */
    private Drawable mIcon;

    /**
     * Title used by this view.
     */
    private CharSequence mTitle;

    /**
     * Subtitle used by this view.
     */
    private CharSequence mSubtitle;

    /**
     * {@code true} to show the icon.
     */
    private boolean mShowIcon;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the subtitle.
     */
    private TextView mSubtitleView;

    public DynamicHeader(@NonNull Context context) {
        this(context, null);
    }

    public DynamicHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicHeader(@NonNull Context context,
                         @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicHeader);

        try {
            mIcon = a.getDrawable(R.styleable.DynamicHeader_ads_dynamicHeader_icon);
            mTitle = a.getString(R.styleable.DynamicHeader_ads_dynamicHeader_title);
            mSubtitle = a.getString(R.styleable.DynamicHeader_ads_dynamicHeader_subtitle);
            mShowIcon = a.getBoolean(R.styleable.DynamicHeader_ads_dynamicHeader_showIcon, true);
        } finally {
            a.recycle();
        }

        initialize();
    }

    /**
     * @return The layout used by this view. Override this to supply a
     *         different layout.
     */
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_header;
    }

    /**
     * Initialize the layout for this view.
     */
    private void initialize() {
        inflate(getContext(), getLayoutRes(), this);

        mIconView = findViewById(R.id.ads_header_icon);
        mTitleView = findViewById(R.id.ads_header_title);
        mSubtitleView = findViewById(R.id.ads_header_subtitle);

        update();
    }

    /**
     * Load this view according to the supplied parameters.
     */
    public void update() {
        mIconView.setImageDrawable(mIcon);
        mTitleView.setText(mTitle);

        if (mSubtitle != null) {
            mSubtitleView.setText(mSubtitle);
            mSubtitleView.setVisibility(VISIBLE);
        } else {
            mSubtitleView.setVisibility(GONE);
        }

        mIconView.setVisibility(mShowIcon ? VISIBLE : GONE);
    }

    /**
     * @return The icon used by this view.
     */
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon for this view.
     *
     * @param icon The icon to be set.
     *
     * @return The {@link DynamicHeader} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicHeader setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        update();
        return this;
    }

    /**
     * @return The title used by this view.
     */
    public CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title for this view.
     *
     * @param title The title to be set.
     *
     * @return The {@link DynamicHeader} object to allow for
     *         chaining of calls to set methods.*
     */
    public DynamicHeader setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        update();
        return this;
    }

    /**
     * @return The subtitle used by this view.
     */
    public CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Set the subtitle for this view.
     *
     * @param subtitle The subtitle to be set.
     *
     * @return The {@link DynamicHeader} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicHeader setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        update();
        return this;
    }

    /**
     * @return {@code true} to show the icon.
     */
    public boolean isShowIcon() {
        return mShowIcon;
    }

    /**
     * Set the icon visibility for this view.
     *
     * @param showIcon {@code true} to show the icon.
     *
     * @return The {@link DynamicHeader} object to allow for
     *         chaining of calls to set methods.
     */
    public DynamicHeader setShowIcon(boolean showIcon) {
        this.mShowIcon = showIcon;

        update();
        return this;
    }
}
