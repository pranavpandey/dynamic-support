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

package com.pranavpandey.android.dynamic.support.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.model.DynamicMenu;

import java.util.List;

/**
 * A simple array adapter for the {@link android.widget.Spinner} which can display an icon
 * and text together as an item. Use the constructor to pass the layout resource, image and the
 * text id with a list of items according to the need.
 */
public class DynamicSpinnerImageAdapter extends ArrayAdapter<DynamicMenu> {

    /**
     * Resource id for the image view.
     */
    private @IdRes int mImageViewResourceId;

    /**
     * Resource id for the text view.
     */
    private @IdRes int mTextViewResourceId;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to retrieve the resources.
     * @param resource The layout resource to be used.
     * @param imageViewResourceId The resource id for the image view.
     * @param textViewResourceId The resource id for the text view.
     * @param data The data to be handled by this adapter.
     */
    public DynamicSpinnerImageAdapter(@NonNull Context context,
            int resource, int imageViewResourceId, int textViewResourceId,
            @NonNull List<DynamicMenu> data) {
        super(context, resource, textViewResourceId, data);

        this.mImageViewResourceId = imageViewResourceId;
        this.mTextViewResourceId = textViewResourceId;
    }

    @Override
    public @NonNull View getView(int position,
            @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);
        ImageView imageView = itemView.findViewById(mImageViewResourceId);
        TextView textView = itemView.findViewById(mTextViewResourceId);

        DynamicMenu item = getItem(position);
        if (item != null) {
            Dynamic.set(imageView, item.getIcon());
            Dynamic.set(textView, item.getTitle());
        }

        return itemView;
    }

    @Override
    public @NonNull View getDropDownView(int position,
            @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
