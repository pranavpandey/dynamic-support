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

package com.pranavpandey.android.dynamic.support.popup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerChoiceAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;

/**
 * A simple {@link PopupWindow} to display a list of items. It will be used
 * internally by the {@link com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference}
 * but it can also be used by the other views.
 */
public class DynamicArrayPopup extends DynamicPopup {

    /**
     * Header view for this popup.
     */
    private View mHeaderView;

    /**
     * Content view for this popup.
     */
    private View mView;

    /**
     * Title used by this popup.
     */
    private CharSequence mTitle;

    /**
     * List entries used by this popup.
     */
    private CharSequence[] mEntries;

    /**
     * Click DynamicTutorialListener used by the list view.
     */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * The selected position.
     */
    private int mSelectedPosition;

    /**
     * Constructor to initialize an object of this class by supplying
     * anchor, entries, and list item click DynamicTutorialListener.
     */
    public DynamicArrayPopup(@NonNull View anchor, @NonNull CharSequence[] entries,
                             @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.mAnchor = anchor;
        this.mEntries = entries;
        this.mOnItemClickListener = onItemClickListener;
        this.mSelectedPosition = DynamicSpinnerChoiceAdapter.DEFAULT_SELECTED_POSITION;
        this.mViewType = DynamicPopupType.LIST;
    }

    @Override
    public @NonNull DynamicPopup build() {
        mView = LayoutInflater.from(getAnchor().getContext()).inflate(
                mViewType == DynamicPopupType.GRID
                        ? R.layout.ads_preference_spinner_grid
                        : R.layout.ads_preference_spinner,
                (ViewGroup) getAnchor().getRootView(), false);
        AbsListView listView = mView.findViewById(R.id.ads_selector_list_view);

        if (listView instanceof GridView) {
            ((GridView) listView).setNumColumns(DynamicLayoutUtils.getLayoutColumns(
                    mView.getContext(), 2, 2));
        }

        if (mTitle != null) {
            mHeaderView = new DynamicHeader(getAnchor().getContext())
                    .setTitle(mTitle).setShowIcon(false);
        }

        listView.setAdapter(new DynamicSpinnerChoiceAdapter(mEntries, mSelectedPosition,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(adapterView, view, position, id);
                        }

                        getPopupWindow().dismiss();
                    }
                }));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mOnItemClickListener.onItemClick(parent, view, position, id);

                getPopupWindow().dismiss();
            }
        });

        setViewRoot(listView);
        return this;
    }

    @Override
    protected @Nullable View getHeaderView() {
        return mHeaderView;
    }

    @Override
    protected @Nullable View getView() {
        return mView;
    }

    /**
     * Getter for {@link #mTitle}.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Setter for {@link #mTitle}.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;
    }

    /**
     * Getter for {@link #mEntries}.
     */
    public @Nullable CharSequence[] getEntries() {
        return mEntries;
    }

    /**
     * Setter for {@link #mEntries}.
     */
    public void setEntries(@NonNull CharSequence[] entries) {
        this.mEntries = entries;
    }

    /**
     * Getter for {@link #mOnItemClickListener}.
     */
    public @NonNull AdapterView.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Setter for {@link #mOnItemClickListener}.
     */
    public void setOnItemClickListener(
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * Getter for {@link #mSelectedPosition}.
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * Setter for {@link #mSelectedPosition}.
     */
    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }
}
