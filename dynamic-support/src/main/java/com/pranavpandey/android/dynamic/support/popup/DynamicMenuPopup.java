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

package com.pranavpandey.android.dynamic.support.popup;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerChoiceAdapter;
import com.pranavpandey.android.dynamic.support.model.DynamicMenu;
import com.pranavpandey.android.dynamic.support.popup.base.DynamicPopup;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DynamicSimplePopup} to display a list of items.
 * <p>It will be used internally by the {@link DynamicSpinnerPreference} but can be used by
 * the other views also.
 */
public class DynamicMenuPopup extends DynamicSimplePopup {

    /**
     * Content view for this popup.
     */
    private View mView;

    /**
     * Menu icons used by this popup.
     */
    private int[] mIconsRes;

    /**
     * Menu icons used by this popup.
     */
    private Drawable[] mIcons;

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
     * @param menus The menu items for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @NonNull List<DynamicMenu> menus,
            int selectedPosition, @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        super(anchor);

        List<Drawable> drawables = new ArrayList<>();
        List<CharSequence> titles = new ArrayList<>();
        List<CharSequence> subtitles = new ArrayList<>();
        List<Boolean> hasSubMenus = new ArrayList<>();
        for (DynamicMenu menu : menus) {
            drawables.add(menu.getIcon());
            titles.add(menu.getTitle());
            subtitles.add(menu.getSubtitle());
            hasSubMenus.add(menu.isHasSubmenu());
        }

        boolean[] subMenus = null;
        if (selectedPosition == DynamicSpinnerChoiceAdapter.DEFAULT_SELECTED_POSITION) {
            subMenus = new boolean[hasSubMenus.size()];
            int index = 0;
            for (Boolean hasSumMenu : hasSubMenus) {
                subMenus[index++] = hasSumMenu;
            }
        }

        this.mAnchor = anchor;
        this.mIcons = drawables.toArray(new Drawable[0]);
        this.mTitles = titles.toArray(new CharSequence[0]);
        this.mSubtitles = subtitles.toArray(new CharSequence[0]);
        this.mHasSubmenus = subMenus;
        this.mSelectedPosition = selectedPosition;
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param titles The titles for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @NonNull CharSequence[] titles,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, (int[]) null, titles, null, null, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, titles, null, onItemClickListener);
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
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, titles, null, selectedPosition, onItemClickListener);
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
    public DynamicMenuPopup(@NonNull View anchor, @Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, titles, subtitles, selectedPosition,
                onItemClickListener, Type.LIST);
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
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, subtitles, selectedPosition, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, iconsRes, null, titles, subtitles, null,
                selectedPosition, onItemClickListener, viewType);
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
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, null, icons, titles, subtitles, null,
                selectedPosition, onItemClickListener, viewType);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, titles, null, hasSubmenus, onItemClickListener);
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
    public DynamicMenuPopup(@NonNull View anchor, @Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, null, hasSubmenus, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, titles, subtitles, hasSubmenus, onItemClickListener, Type.LIST);
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
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, icons, titles, subtitles, hasSubmenus, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, iconsRes, null, titles, subtitles, hasSubmenus,
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
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor,
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener, @Type int viewType) {
        this(anchor, null, icons, titles, subtitles, hasSubmenus,
                DynamicSpinnerChoiceAdapter.DEFAULT_SELECTED_POSITION,
                onItemClickListener, viewType);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, iconsRes, null, titles, subtitles, hasSubmenus,
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
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(anchor, null, icons, titles, subtitles, hasSubmenus,
                selectedPosition, onItemClickListener, Type.LIST);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param anchor The anchor view for this popup.
     * @param iconsRes The icons for this popup.
     * @param icons The icons for this popup.
     * @param titles The titles for this popup.
     * @param subtitles The subtitles for this popup.
     * @param hasSubmenus The submenu states for this popup.
     * @param selectedPosition The selected menu position for this popup.
     * @param onItemClickListener The on click listener for the list view.
     * @param viewType The view type for the popup.
     */
    public DynamicMenuPopup(@NonNull View anchor, @Nullable int[] iconsRes,
            @Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            int selectedPosition, @Nullable AdapterView.OnItemClickListener onItemClickListener,
            @Type int viewType) {
        super(anchor);

        this.mIconsRes = iconsRes;
        this.mIcons = icons;
        this.mTitles = titles;
        this.mSubtitles = subtitles;
        this.mHasSubmenus = hasSubmenus;
        this.mSelectedPosition = selectedPosition;
        this.mOnItemClickListener = onItemClickListener;
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

        if (mOnItemClickListener != null) {
            listView.setAdapter(new DynamicSpinnerChoiceAdapter(mIconsRes, mIcons,
                    mTitles, mSubtitles, mHasSubmenus, mSelectedPosition,
                    new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView,
                        View view, int position, long id) {
                    mOnItemClickListener.onItemClick(adapterView, view, position, id);

                    dismiss();
                }
            }));
        }

        setViewRoot(listView);
        return this;
    }

    @Override
    protected @Nullable View getView() {
        return mView;
    }

    /**
     * Get the menu icons used by this popup.
     *
     * @return The menu icons used by this popup.
     */
    public @Nullable int[] getIconsRes() {
        return mIconsRes;
    }

    /**
     * Set the menu icons for this popup.
     *
     * @param iconsRes The menu icons to be set.
     */
    public void setIconsRes(@Nullable int[] iconsRes) {
        this.mIconsRes = iconsRes;
        this.mIcons = null;
    }

    /**
     * Get the menu icons used by this popup.
     *
     * @return The menu icons used by this popup.
     */
    public @Nullable Drawable[] getIcons() {
        return mIcons;
    }

    /**
     * Set the menu icons for this popup.
     *
     * @param icons The menu icons to be set.
     */
    public void setIcons(@Nullable Drawable[] icons) {
        this.mIconsRes = null;
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
     * @return The selected position.
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
