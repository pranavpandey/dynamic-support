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

package com.pranavpandey.android.dynamic.support.sample.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.adapter.AppInfoAdapter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView

/**
 * A recycler view binder to display author info by using [DynamicRecyclerViewBinder].
 */
class AuthorInfoBinder(binderAdapter: AppInfoAdapter)
    : DynamicRecyclerViewBinder<RecyclerView.ViewHolder>(binderAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_info_author, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        // Return item count.
        return 1
    }

    /**
     * Holder class to hold view holder item.
     */
    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val dynamicInfoView: DynamicInfoView = view.findViewById(R.id.info_author)

        init {
            dynamicInfoView.linksView.recyclerViewLayoutManager =
                    DynamicLayoutUtils.getGridLayoutManager(dynamicInfoView.context,
                            DynamicLayoutUtils.getGridCount(dynamicInfoView.context))

            if (dynamicInfoView.linksColors != null) {
                @ColorInt val colors = dynamicInfoView.linksColors
                colors?.set(0, DynamicTheme.getInstance().get().primaryColor)
                dynamicInfoView.linksColors = colors
                dynamicInfoView.onUpdate()
            }
        }
    }
}
