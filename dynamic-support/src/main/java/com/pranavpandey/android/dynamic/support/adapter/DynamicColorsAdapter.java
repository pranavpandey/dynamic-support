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
import android.widget.BaseAdapter;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorView;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;

/**
 * A simple base adapter to hold an array of colors and displays them
 * in a adapter view.
 */
public class DynamicColorsAdapter extends BaseAdapter {

    /**
     * Listener to get the callback when a color is selected.
     */
    public interface OnColorSelectedListener {

        /**
         * Called when a color is selected.
         *
         * @param color The selected color.
         */
        void onColorSelected(int position, @ColorInt int color);
    }

    /**
     * Listener to get the callback when a color is selected.
     */
    private OnColorSelectedListener mOnColorSelectedListener;

    /**
     * Array of colors to be handled by this adapter.
     */
    private @ColorInt Integer[] mDataSet;

    /**
     * The selected color.
     */
    private @ColorInt int mSelectedColor;

    /**
     * Shape of the color swatches.
     *
     * @see DynamicColorShape
     */
    private @DynamicColorShape int mColorShape;

    /**
     * {@code true} to enable alpha for the color.
     */
    private boolean mAlpha;

    public DynamicColorsAdapter(@NonNull @ColorInt Integer[] colors,
                                @DynamicColorShape int colorShape, boolean alpha,
                                @NonNull OnColorSelectedListener onColorSelectedListener) {
        this(colors, DynamicColorType.UNKNOWN, colorShape, alpha, onColorSelectedListener);
    }

    public DynamicColorsAdapter(@NonNull @ColorInt Integer[] colors, @ColorInt int selectedColor,
                                @DynamicColorShape int colorShape, boolean alpha,
                                @NonNull OnColorSelectedListener onColorSelectedListener) {
        this.mDataSet = colors;
        this.mSelectedColor = selectedColor;
        this.mColorShape = colorShape;
        this.mAlpha = alpha;
        this.mOnColorSelectedListener = onColorSelectedListener;
    }
 
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

        if (convertView == null) {
        	convertView = LayoutInflater.from(parent.getContext()).inflate(
        	        R.layout.ads_layout_color_item, parent, false);
        	viewHolder = new ViewHolder(convertView);
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }

        final int color = (int) getItem(position);
        final DynamicColorView dynamicColorView = viewHolder.getDynamicColorView();
        dynamicColorView.setColor(color);
        dynamicColorView.setColorShape(mColorShape);
        dynamicColorView.setAlpha(mAlpha);
        if (mSelectedColor != DynamicColorType.UNKNOWN) {
            dynamicColorView.setSelected(mSelectedColor == color);
        }

        dynamicColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnColorSelectedListener != null) {
                    mOnColorSelectedListener.onColorSelected(position, dynamicColorView.getColor());
                    mSelectedColor = dynamicColorView.getColor();

                    notifyDataSetChanged();
                }
            }
        });

        dynamicColorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dynamicColorView.showHint();
                return true;
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
    public @NonNull @ColorInt Integer[] getDataSet() {
        return mDataSet;
    }

    /**
     * Setter for {@link #mDataSet}.
     */
    public void setDataSet(@NonNull @ColorInt Integer[] dataSet) {
        this.mDataSet = dataSet;

        notifyDataSetChanged();
    }

    /**
     * Getter for {@link #mOnColorSelectedListener}.
     */
    public OnColorSelectedListener getOnColorSelectedListener() {
        return mOnColorSelectedListener;
    }

    /**
     * Setter for {@link #mOnColorSelectedListener}.
     */
    public void setOnColorSelectedListener(
            @NonNull OnColorSelectedListener onColorSelectedListener) {
        this.mOnColorSelectedListener = onColorSelectedListener;

        notifyDataSetChanged();
    }

    /**
     * Getter for {@link #mSelectedColor}.
     */
    public int getSelectedColor() {
        return mSelectedColor;
    }

    /**
     * Setter for {@link #mSelectedColor}.
     */
    public void setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;

        notifyDataSetChanged();
    }

    /**
     * Getter for {@link #mColorShape}.
     */
    public int getColorShape() {
        return mColorShape;
    }

    /**
     * Setter for {@link #mColorShape}.
     */
    public void setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        notifyDataSetChanged();
    }

    /**
     * View holder class to hold the color view.
     */
    static class ViewHolder {

        /**
         * Color view to display color on the adapter view.
         */
        private final DynamicColorView dynamicColorView;

        /**
         * Constructor to initialize views from the supplied layout.
         */
    	public ViewHolder(@NonNull View view) {
    	    dynamicColorView = view.findViewById(R.id.ads_color_item_view);
    	}

        /**
         * Getter for {@link #dynamicColorView}.
         */
        DynamicColorView getDynamicColorView() {
            return dynamicColorView;
        }
    }
}

