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

package com.pranavpandey.android.dynamic.support.popup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerChoiceAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A simple {@link PopupWindow} to display a list of items. It will be used internally by the
 * {@link com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference} but it can
 * be used by the other views also.
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
     * Icons for the entries.
     */
    private @DrawableRes int[] mIcons;

    /**
     * The selected position.
     */
    private int mSelectedPosition;

    /**
     * Click listener used by the list view.
     */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view used for this popup.
     * @param entries The entries for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicArrayPopup(@NonNull View anchor, @NonNull CharSequence[] entries,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, entries, null, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view used for this popup.
     * @param entries The entries for this popup.
     * @param icons The icons for the entries.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicArrayPopup(@NonNull View anchor,
            @NonNull CharSequence[] entries, @Nullable @DrawableRes int[] icons,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.mAnchor = anchor;
        this.mEntries = entries;
        this.mIcons = icons;
        this.mOnItemClickListener = onItemClickListener;
        this.mSelectedPosition = DynamicSpinnerChoiceAdapter.DEFAULT_SELECTED_POSITION;
        this.mViewType = Type.LIST;
    }

    @Override
    public @NonNull DynamicPopup build() {
        mView = LayoutInflater.from(getAnchor().getContext()).inflate(
                mViewType == Type.GRID
                        ? R.layout.ads_preference_spinner_grid
                        : R.layout.ads_preference_spinner,
                (ViewGroup) getAnchor().getRootView(), false);
        AbsListView listView = mView.findViewById(R.id.ads_selector_list_view);

        if (listView instanceof GridView) {
            ((GridView) listView).setNumColumns(
                    DynamicLayoutUtils.getGridCount(mView.getContext()));
        }

        if (mTitle != null) {
            mHeaderView = new DynamicHeader(getAnchor().getContext());
            ((DynamicHeader) mHeaderView).setColorType(Theme.ColorType.PRIMARY);
            ((DynamicHeader) mHeaderView).setTitle(mTitle);
            ((DynamicHeader) mHeaderView).setFillSpace(true);
        }

        if (mOnItemClickListener != null) {
            listView.setAdapter(new DynamicSpinnerChoiceAdapter(
                    mEntries, mIcons, mSelectedPosition,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView,
                                View view, int position, long id) {
                            mOnItemClickListener.onItemClick(adapterView, view, position, id);

                            getPopupWindow().dismiss();
                        }
                    })
            );
        }

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
     * Get the entries used by this popup.
     *
     * @return The entries used by this popup.
     */
    public @Nullable CharSequence[] getEntries() {
        return mEntries;
    }

    /**
     * Set the entries for this popup.
     *
     * @param entries The entries to be set.
     */
    public void setEntries(@NonNull CharSequence[] entries) {
        this.mEntries = entries;
    }

    /**
     * Get the icons for the entries.
     *
     * @return The icons for the entries.
     */
    public @Nullable @DrawableRes int[] getIcons() {
        return mIcons;
    }

    /**
     * Set the icons for the entries.
     *
     * @param icons The icons for the entries to be set.
     */
    public void setIcons(@Nullable @DrawableRes int[] icons) {
        this.mIcons = icons;
    }

    /**
     * Get the selected position.
     *
     * @return The the selected position.
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * Set the selected position.
     *
     * @param selectedPosition The position to be selected.
     */
    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    /**
     * Get the on click listener used by the list view.
     *
     * @return The on click listener used by the list view.
     */
    public @Nullable AdapterView.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Set the on click listener used by the list view.
     *
     * @param onItemClickListener The listener to be set.
     */
    public void setOnItemClickListener(
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
