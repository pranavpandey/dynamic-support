/*
 * Copyright 2017-2022 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.popup;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.popup.base.DynamicPopup;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicPopup} to show message with a title and footer action.
 */
public class DynamicSimplePopup extends DynamicPopup {

    /**
     * Title used by this popup.
     */
    protected CharSequence mTitle;

    /**
     * Message shown by this popup.
     */
    protected CharSequence mMessage;

    /**
     * Action used by this popup.
     */
    protected CharSequence mAction;

    /**
     * Action drawable used by this popup.
     */
    protected Drawable mActionDrawable;

    /**
     * Action click listener used by this popup.
     */
    protected View.OnClickListener mActionOnClickListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     */
    public DynamicSimplePopup(@NonNull View anchor) {
        this.mAnchor = anchor;;
    }

    /**
     * Get the title used by this popup.
     *
     * @return The title used by this popup.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by this popup.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;
    }

    /**
     * Get the message used by this popup.
     *
     * @return The message used by this popup.
     */
    public @Nullable CharSequence getMessage() {
        return mMessage;
    }

    /**
     * Set the message used by this popup.
     *
     * @param message The message to be set.
     */
    public void setMessage(@Nullable CharSequence message) {
        this.mMessage = message;
    }

    /**
     * Get the action used by this popup.
     *
     * @return The action used by this popup.
     */
    public @Nullable CharSequence getAction() {
        return mAction;
    }

    /**
     * Set the action used by this popup.
     *
     * @param action The action to be set.
     */
    public void setAction(@Nullable CharSequence action) {
        this.mAction = action;
    }

    /**
     * Get the action drawable used by this popup.
     *
     * @return The action drawable used by this popup.
     */
    public @Nullable Drawable getActionDrawable() {
        return mActionDrawable;
    }

    /**
     * Set the action drawable used by this popup.
     *
     * @param drawable The drawable to be set.
     */
    public void setActionDrawable(@Nullable Drawable drawable) {
        this.mActionDrawable = drawable;
    }

    /**
     * Get the action click listener used by this popup.
     *
     * @return The click listener used by this popup.
     */
    public @Nullable View.OnClickListener getActionOnClickListener() {
        return mActionOnClickListener;
    }

    /**
     * Set the action click listener used by this popup.
     *
     * @param onClickListener The on click listener to be set.
     */
    public void setActionOnClickListener(@Nullable View.OnClickListener onClickListener) {
        this.mActionOnClickListener = onClickListener;
    }

    @Override
    protected int getMaxWidth() {
        return (int) getAnchor().getContext().getResources()
                .getDimension(R.dimen.ads_popup_max_width);
    }

    @Override
    public @NonNull DynamicPopup build() {
        return this;
    }

    @Override
    protected @Nullable View getHeaderView() {
        if (TextUtils.isEmpty(getTitle())) {
            return super.getHeaderView();
        }

        DynamicHeader headerView = new DynamicHeader(getAnchor().getContext());
        headerView.setColorType(Theme.ColorType.PRIMARY);
        headerView.setContrastWithColorType(Theme.ColorType.SURFACE);
        headerView.setTitle(getTitle());
        headerView.setFillSpace(true);

        return headerView;
    }

    @Override
    protected @Nullable View getView() {
        if (TextUtils.isEmpty(getMessage())) {
            return null;
        }

        View view = LayoutInflater.from(getAnchor().getContext()).inflate(
                R.layout.ads_popup_simple, (ViewGroup) getAnchor().getRootView(), false);
        Dynamic.set(view.findViewById(R.id.ads_popup_message), getMessage());

        setViewRoot(view.findViewById(R.id.ads_popup_simple));
        return view;
    }

    @Override
    protected @Nullable View getFooterView() {
        if (getActionOnClickListener() == null) {
            return super.getFooterView();
        }

        View view = LayoutInflater.from(getAnchor().getContext()).inflate(
                R.layout.ads_popup_footer, (ViewGroup) getAnchor().getRootView(), false);
        Dynamic.set(view.findViewById(R.id.ads_popup_footer_title), getAction());
        Dynamic.set(view.findViewById(R.id.ads_popup_footer_image), getActionDrawable());
        Dynamic.setClickListener(view.findViewById(R.id.ads_popup_footer_root),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActionOnClickListener() != null) {
                    getActionOnClickListener().onClick(v);
                }
            }
        });

        return view;
    }
}
