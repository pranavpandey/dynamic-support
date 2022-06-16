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

package com.pranavpandey.android.dynamic.support.recyclerview.binder.factory;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;

/**
 * An {@link EmptyBinder} to bind the search or any other query results inside a
 * {@link androidx.cardview.widget.CardView} that can be used with the {@link DynamicBinderAdapter}.
 */
public class EmptyCardBinder extends EmptyBinder {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public EmptyCardBinder(@NonNull DynamicBinderAdapter<?> binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ads_layout_empty_card, parent, false));
    }
}
