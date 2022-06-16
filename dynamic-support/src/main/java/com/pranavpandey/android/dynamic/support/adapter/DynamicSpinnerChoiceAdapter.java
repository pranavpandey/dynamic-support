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

package com.pranavpandey.android.dynamic.support.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A simple base adapter to hold an array of char sequence and display them in an adapter view.
 */
public class DynamicSpinnerChoiceAdapter extends BaseAdapter {

    /**
     * Default value for the selected position.
     */
    public static int DEFAULT_SELECTED_POSITION = -1;

    /**
     * Listener to get the callback when an item is clicked.
     */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * Array of icons used by this adapter.
     */
    private int[] mIconsRes;

    /**
     * Array of icons used by this adapter.
     */
    private Drawable[] mIcons;

    /**
     * Array of titles used by this adapter.
     */
    private CharSequence[] mTitles;

    /**
     * Array of subtitles used by this adapter.
     */
    private CharSequence[] mSubtitles;

    /**
     * Array of submenu states used by this adapter.
     */
    private boolean[] mHasSubmenus;

    /**
     * The selected position used by this adapter.
     */
    private int mSelectedPosition;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param titles The titles for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this((int[]) null, titles, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, titles, DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(icons, titles, DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, titles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(icons, titles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            int selectedPosition, @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, titles, subtitles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            int selectedPosition, @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(icons, titles, subtitles, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, titles, null, hasSubmenus, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(icons, titles, null, hasSubmenus, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, titles, subtitles, hasSubmenus,
                DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons, @Nullable CharSequence[] titles,
            @Nullable CharSequence[] subtitles, @Nullable boolean[] hasSubmenus,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(icons, titles, subtitles, hasSubmenus,
                DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(iconsRes, null, titles, subtitles, hasSubmenus,
                selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this(null, icons, titles, subtitles, hasSubmenus,
                selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param iconsRes The icons for this adapter.
     * @param icons The icons for this adapter.
     * @param titles The titles for this adapter.
     * @param subtitles The subtitles for this adapter.
     * @param hasSubmenus The submenu states for this adapter.
     * @param selectedPosition The selected menu position for this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@Nullable int[] iconsRes, @Nullable Drawable[] icons,
            @Nullable CharSequence[] titles, @Nullable CharSequence[] subtitles,
            @Nullable boolean[] hasSubmenus, int selectedPosition,
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this.mIconsRes = iconsRes;
        this.mIcons = icons;
        this.mTitles = titles;
        this.mSubtitles = subtitles;
        this.mHasSubmenus = hasSubmenus;
        this.mSelectedPosition = selectedPosition;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
	public View getView(final int position,
            @Nullable View convertView, final @NonNull ViewGroup parent) {
		ViewHolder viewHolder;

        if (convertView == null) {
        	convertView = LayoutInflater.from(parent.getContext()).inflate(
        	        R.layout.ads_layout_array_item_popup, parent, false);
        	viewHolder = new ViewHolder(convertView);
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mOnItemClickListener != null) {
            Dynamic.setOnClickListener(viewHolder.getRoot(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick((AdapterView<?>) parent,
                            view, position, getItemId(position));
                    mSelectedPosition = position;

                    notifyDataSetChanged();
                }
            });
        } else {
            Dynamic.setClickable(viewHolder.getRoot(), false);
        }

        Dynamic.set(viewHolder.getIcon(), getIcon(parent.getContext(), position));
        Dynamic.set(viewHolder.getTitle(), getTitles() != null
                ? getTitles()[position] : null);
        Dynamic.set(viewHolder.getSubtitle(), getSubtitles() != null
                ? getSubtitles()[position] : null);

        if (getHasSubmenus() != null) {
            Dynamic.setColorType(viewHolder.getSelector(),
                    Defaults.ADS_COLOR_TYPE_SYSTEM_SECONDARY);
            Dynamic.set(viewHolder.getSelector(), getHasSubmenus()[position]
                    ? DynamicResourceUtils.getDrawable(parent.getContext(),
                    R.drawable.ads_ic_arrow_right) : null);
        } else {
            Dynamic.setColorType(viewHolder.getSelector(), Theme.ColorType.ACCENT);
            Dynamic.setResource(viewHolder.getSelector(), R.drawable.ads_ic_check);
            Dynamic.setVisibility(viewHolder.getSelector(),
                    mSelectedPosition == position ? View.VISIBLE : View.INVISIBLE);
        }

	    return convertView;
    }

    @Override
    public int getCount() {
        if (mTitles == null) {
            return mIconsRes != null ? mIconsRes.length : 0;
        }

        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the array of icons used by this adapter.
     *
     * @return The array of icons used by this adapter.
     */
    public @Nullable int[] getIconsRes() {
        return mIconsRes;
    }

    /**
     * Sets the array of icons for this adapter.
     *
     * @param iconsRes The array of drawable resource ids to be set.
     */
    public void setIconsRes(@Nullable int[] iconsRes) {
        this.mIconsRes = iconsRes;
        this.mIcons = null;

        notifyDataSetChanged();
    }

    /**
     * Get the array of icons used by this adapter.
     *
     * @return The array of icons used by this adapter.
     */
    public @Nullable Drawable[] getIcons() {
        return mIcons;
    }

    /**
     * Sets the array of icons for this adapter.
     *
     * @param icons The array of drawable resource ids to be set.
     */
    public void setIcons(@Nullable Drawable[] icons) {
        this.mIconsRes = null;
        this.mIcons = icons;

        notifyDataSetChanged();
    }

    /**
     * Returns the icon for the supplied position.
     *
     * @param context The context to be used.
     * @param position The position of the icon.
     *
     * @return The icon for the supplied position.
     */
    public @Nullable Drawable getIcon(@Nullable Context context, int position) {
        if (context != null && getIconsRes() != null && position <= getIconsRes().length - 1) {
            return DynamicResourceUtils.getDrawable(context, getIconsRes()[position]);
        } else if (getIcons() != null && position <= getIcons().length - 1) {
            return getIcons()[position];
        } else {
            return null;
        }
    }

    /**
     * Get the array of titles used by this adapter.
     *
     * @return The array of titles used by this adapter.
     */
    public @Nullable CharSequence[] getTitles() {
        return mTitles;
    }

    /**
     * Sets the array of titles for this adapter.
     *
     * @param titles The array of titles to be set.
     */
    public void setTitles(@Nullable CharSequence[] titles) {
        this.mTitles = titles;

        notifyDataSetChanged();
    }

    /**
     * Get the array of subtitles used by this adapter.
     *
     * @return The array of subtitles used by this adapter.
     */
    public @Nullable CharSequence[] getSubtitles() {
        return mSubtitles;
    }

    /**
     * Sets the array of subtitles for this adapter.
     *
     * @param entries The array of subtitles to be set.
     */
    public void setSubtitles(@Nullable CharSequence[] entries) {
        this.mSubtitles = entries;

        notifyDataSetChanged();
    }

    /**
     * Get the array of submenu states used by this adapter.
     *
     * @return The array of submenu states used by this adapter.
     */
    public @Nullable boolean[] getHasSubmenus() {
        return mHasSubmenus;
    }

    /**
     * Sets the array of submenu states for this adapter.
     *
     * @param entries The array of submenu states to be set.
     */
    public void setHasSubmenus(@Nullable boolean[] entries) {
        this.mHasSubmenus = entries;

        notifyDataSetChanged();
    }

    /**
     * Get the item click listener.
     *
     * @return The listener to get the callback when an item is clicked.
     */
    public @Nullable AdapterView.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Sets the listener to get the callback when an item is clicked.
     *
     * @param onItemClickListener The listener to be set.
     */
    public void setOnItemClickListener(
            @Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;

        notifyDataSetChanged();
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
     * Sets the selected position.
     *
     * @param selectedPosition The position to be selected.
     */
    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;

        notifyDataSetChanged();
    }

    /**
     * View holder class to hold the item view.
     */
    static class ViewHolder {

        /**
         * Item view root.
         */
        private final ViewGroup root;

        /**
         * Image view to show the icon.
         */
        private final ImageView icon;

        /**
         * Text view to display the title.
         */
        private final TextView title;

        /**
         * Text view to display the subtitle.
         */
        private final TextView subtitle;

        /**
         * Image view to show the selected item.
         */
        private final ImageView selector;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
    	ViewHolder(@NonNull View view) {
    	    root = view.findViewById(R.id.ads_array_item);
    	    icon = view.findViewById(R.id.ads_array_item_icon);
    	    title = view.findViewById(R.id.ads_array_item_title);
    	    subtitle = view.findViewById(R.id.ads_array_item_subtitle);
            selector = view.findViewById(R.id.ads_array_item_selector);
    	}

        /**
         * Get the item view root.
         *
         * @return The item view root.
         */
        @NonNull ViewGroup getRoot() {
            return root;
        }

        /**
         * Get the image view to show the icon.
         *
         * @return The image view to show the icon.
         */
        @Nullable ImageView getIcon() {
            return icon;
        }

        /**
         * Get the text view to display the title.
         *
         * @return The text view to display the title.
         */
        @Nullable TextView getTitle() {
            return title;
        }

        /**
         * Get the text view to display the subtitle.
         *
         * @return The text view to display the subtitle.
         */
        @Nullable TextView getSubtitle() {
            return subtitle;
        }

        /**
         * Get the image view to show the selected item.
         *
         * @return The image view to show the selected item.
         */
        @Nullable ImageView getSelector() {
            return selector;
        }
    }
}
