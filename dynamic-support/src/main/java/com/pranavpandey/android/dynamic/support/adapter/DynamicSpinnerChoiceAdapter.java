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

package com.pranavpandey.android.dynamic.support.adapter;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;

/**
 * A simple base adapter to hold an array of char sequence and display them
 * in a adapter view.
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
    private CharSequence[] mDataSet;

    /**
     * The selected position.
     */
    private int mSelectedPosition;

    public DynamicSpinnerChoiceAdapter(@NonNull CharSequence[] entries,
                                       @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this(entries, DEFAULT_SELECTED_POSITION, onItemClickListener);
    }

    public DynamicSpinnerChoiceAdapter(CharSequence[] entries, @ColorInt int selectedPosition,
                                       @NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.mDataSet = entries;
        this.mSelectedPosition = selectedPosition;
        this.mOnItemClickListener = onItemClickListener;
    }
 
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder viewHolder;

        if (convertView == null) {
        	convertView = LayoutInflater.from(parent.getContext()).inflate(
        	        R.layout.ads_layout_array_item_popup, parent, false);
        	viewHolder = new ViewHolder(convertView);
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getTextView().setText((CharSequence) getItem(position));
        viewHolder.getSelectorView().setVisibility(
                mSelectedPosition == position ? View.VISIBLE : View.INVISIBLE);

        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(
                            (AdapterView<?>) parent, view, position, getItemId(position));
                    mSelectedPosition = position;

                    notifyDataSetChanged();
                }
            }
        });
        
	    return convertView;
    }

    @Override
    public int getCount() {
        return mDataSet.length;
    }

    @Override
    public Object getItem(int position) {
        return mDataSet[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Getter for {@link #mDataSet}.
     */
    public CharSequence[] getDataSet() {
        return mDataSet;
    }

    public void setDataSet(@NonNull CharSequence[] dataSet) {
        this.mDataSet = dataSet;

        notifyDataSetChanged();
    }

    /**
     * Getter for {@link #mOnItemClickListener}.
     */
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Setter for {@link #mOnItemClickListener}.
     */
    public void setOnItemClickListener(
            @NonNull AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;

        notifyDataSetChanged();
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

        notifyDataSetChanged();
    }

    /**
     * Holder class to hold the item view.
     */
    static class ViewHolder {

        /**
         * Item view root.
         */
        private final ViewGroup itemView;

        /**
         * Text view to display the entry.
         */
        private final TextView textView;

        /**
         * Image view to show the selected item.
         */
        private final ImageView selectorView;

        /**
         * Constructor to initialize views from the supplied layout.
         */
    	public ViewHolder(@NonNull View view) {
    	    itemView = view.findViewById(R.id.ads_array_item);
    	    textView = view.findViewById(R.id.ads_array_item_text);
            selectorView = view.findViewById(R.id.ads_array_item_selector);
    	}

        /**
         * Getter for {@link #itemView}.
         */
        ViewGroup getItemView() {
            return itemView;
        }

        /**
         * Getter for {@link #textView}.
         */
        TextView getTextView() {
            return textView;
        }

        /**
         * Getter for {@link #selectorView}.
         */
        ImageView getSelectorView() {
            return selectorView;
        }
    }
}

