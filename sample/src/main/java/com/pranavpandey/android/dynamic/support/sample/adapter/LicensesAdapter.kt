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

package com.pranavpandey.android.dynamic.support.sample.adapter

import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.model.DynamicInfo
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.InfoBigBinder
import com.pranavpandey.android.dynamic.support.sample.binder.LicenseBinder
import java.util.*

/**
 * A binder adapter to display a list of [DynamicInfo] by using [DynamicSimpleBinderAdapter].
 */
class LicensesAdapter(

    /**
     * Data set used by this adapter.
     */
    private val dataSet: ArrayList<DynamicInfo>?)
    : DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder<*>>() {

    init {
        // Add license binder for this adapter.
        addDataBinder(LicenseBinder(this))
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (getDataBinder(getItemViewType(position)) as InfoBigBinder).data = getItem(position)
        super.onBindViewHolder(viewHolder, position)
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }

    /**
     * Returns the item according the position.
     *
     * @param position The position to retrieve the item.
     *
     * @return The item according the position.
     */
    private fun getItem(position: Int): DynamicInfo {
        return dataSet!![position]
    }
}
