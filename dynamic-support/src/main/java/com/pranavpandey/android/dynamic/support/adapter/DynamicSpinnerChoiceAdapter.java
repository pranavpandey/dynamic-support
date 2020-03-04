/*
 * Copyright 2020 Pranav Pandey
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.R;

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
     * Array of char sequences to be handled by this adapter.
     */
    private CharSequence[] mEntries;

    /**
     * Array of drawable resource ids for the entries.
     */
    private @DrawableRes int[] mIcons;

    /**
     * The selected position.
     */
    private int mSelectedPosition;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param entries The array of texts to be handled by this adapter.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@NonNull CharSequence[] entries,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(entries, null, DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param entries The array of texts to be handled by this adapter.
     * @param icons The array of drawable resource ids for the entries.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@NonNull CharSequence[] entries,
            @Nullable @DrawableRes int[] icons,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(entries, icons, DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param entries The array of texts to be handled by this adapter.
     * @param selectedPosition The selected position.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@NonNull CharSequence[] entries, int selectedPosition,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(entries, null, selectedPosition, onItemClickListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param entries The array of texts to be handled by this adapter.
     * @param icons The array of drawable resource ids for the entries.
     * @param selectedPosition The selected position.
     * @param onItemClickListener The listener to get the callback when an item is clicked.
     */
    public DynamicSpinnerChoiceAdapter(@NonNull CharSequence[] entries,
            @Nullable @DrawableRes int[] icons, int selectedPosition,
            @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.mEntries = entries;
        this.mIcons = icons;
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

        viewHolder.getText().setText((CharSequence) getItem(position));
        viewHolder.getSelector().setVisibility(
                mSelectedPosition == position ? View.VISIBLE : View.INVISIBLE);

        if (mIcons != null && mIcons.length == mEntries.length) {
            viewHolder.getIcon().setImageResource(mIcons[position]);
            viewHolder.getIcon().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getIcon().setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            viewHolder.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick((AdapterView<?>) parent,
                            view, position, getItemId(position));
                    mSelectedPosition = position;

                    notifyDataSetChanged();
                }
            });
        }

	    return convertView;
    }

    @Override
    public int getCount() {
        return mEntries.length;
    }

    @Override
    public Object getItem(int position) {
        return mEntries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the entries handled by this adapter.
     *
     * @return The array of char sequences handled by this adapter.
     */
    public @Nullable CharSequence[] getEntries() {
        return mEntries;
    }

    /**
     * Sets the array of char sequences to be handled by this adapter.
     *
     * @param entries The array of char sequences to be set.
     */
    public void setEntries(@NonNull CharSequence[] entries) {
        this.mEntries = entries;

        notifyDataSetChanged();
    }

    /**
     * Get the icons for the entries handled by this adapter.
     *
     * @return The array of drawable resource ids for the entries.
     */
    public @Nullable @DrawableRes int[] getIcons() {
        return mIcons;
    }


    /**
     * Sets the array of drawable resource ids for the entries.
     *
     * @param icons The array of drawable resource ids to be set.
     */
    public void setIcons(@Nullable @DrawableRes int[] icons) {
        this.mIcons = icons;

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
         * Text view to display the entry.
         */
        private final TextView text;

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
    	    text = view.findViewById(R.id.ads_array_item_text);
            selector = view.findViewById(R.id.ads_array_item_selector);
    	}

        /**
         * Get the item view root.
         *
         * @return The item view root.
         */
        ViewGroup getRoot() {
            return root;
        }

        /**
         * Get the image view to show the icon.
         *
         * @return The image view to show the icon.
         */
        ImageView getIcon() {
            return icon;
        }

        /**
         * Get the text view to display the entry.
         *
         * @return The text view to display the entry.
         */
        TextView getText() {
            return text;
        }

        /**
         * Get the image view to show the selected item.
         *
         * @return The image view to show the selected item.
         */
        ImageView getSelector() {
            return selector;
        }
    }
}

