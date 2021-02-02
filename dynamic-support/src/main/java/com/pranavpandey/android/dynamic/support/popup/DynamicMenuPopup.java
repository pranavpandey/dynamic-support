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

package com.pranavpandey.android.dynamic.support.popup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerChoiceAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicHeader;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicPopup} to display a list of items. It will be used internally by the
 * {@link com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference} but it can
 * be used by the other views also.
 */
public class DynamicMenuPopup extends DynamicPopup {

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
     * Menu icons used by this popup.
     */
    private @DrawableRes int[] mIcons;

    /**
     * Menu titles used by this popup.
     */
    private CharSequence[] mTitles;

    /**
     * Menu subtitles used by this popup.
     */
    private CharSequence[] mSubtitles;

    /**
     * Submenu states used by this popup.
     */
    private boolean[] mHasSubmenus;

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
     * @param anchor The anchor view for this popup.
     * @param titles The titles for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @NonNull CharSequence[] titles,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, null, titles, null, null, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable @DrawableRes int[] icons, @Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable @DrawableRes int[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, subtitles, selectedPosition, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable @DrawableRes int[] icons,
            @Nullable CharSequence[] titles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable @DrawableRes int[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, icons, titles, subtitles, null, onItemClickListener, viewType);

        this.mSelectedPosition = selectedPosition;
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable @DrawableRes int[] icons,
            @Nullable CharSequence[] titles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, hasSubmenus, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable @DrawableRes int[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, subtitles, hasSubmenus, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable @DrawableRes int[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, icons, titles, subtitles, hasSubmenus,
                DynamicSpinnerChoiceAdapter.DEFAULT_SELECTED_POSITION,
                onItemClickListener, viewType);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable @DrawableRes int[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, subtitles, hasSubmenus,
                selectedPosition, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable @DrawableRes int[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this.mAnchor = anchor;
        this.mIcons = icons;
        this.mTitles = titles;
        this.mSubtitles = subtitles;
        this.mHasSubmenus = hasSubmenus;
        this.mOnItemClickListener = onItemClickListener;
        this.mSelectedPosition = selectedPosition;
        this.mViewType = viewType;
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
                    mIcons, mTitles, mSubtitles, mHasSubmenus, mSelectedPosition,
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
     * Get the menu icons used by this popup.
     *
     * @return The menu icons used by this popup.
     */
    public @Nullable @DrawableRes int[] getIcons() {
        return mIcons;
    }

    /**
     * Set the menu icons for this popup.
     *
     * @param icons The menu icons to be set.
     */
    public void setIcons(@Nullable @DrawableRes int[] icons) {
        this.mIcons = icons;
    }

    /**
     * Get the menu titles used by this popup.
     *
     * @return The menu titles used by this popup.
     */
    public @Nullable CharSequence[] getTitles() {
        return mTitles;
    }

    /**
     * Set the menu titles for this popup.
     *
     * @param titles The menu titles to be set.
     */
    public void setTitles(@Nullable CharSequence[] titles) {
        this.mTitles = titles;
    }

    /**
     * Get the menu subtitles used by this popup.
     *
     * @return The menu subtitles used by this popup.
     */
    public @Nullable CharSequence[] getSubtitles() {
        return mSubtitles;
    }

    /**
     * Set the menu subtitles for this popup.
     *
     * @param subtitles The menu subtitles to be set.
     */
    public void setSubtitles(@Nullable CharSequence[] subtitles) {
        this.mSubtitles = subtitles;
    }

    /**
     * Get the submenu states used by this popup.
     *
     * @return The submenu states used by this popup.
     */
    public @Nullable boolean[] getHasSubmenus() {
        return mHasSubmenus;
    }

    /**
     * Set the submenu states for this popup.
     *
     * @param hasSubmenus The submenu states to be set.
     */
    public void setHasSubmenus(@Nullable boolean[] hasSubmenus) {
        this.mHasSubmenus = hasSubmenus;
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
