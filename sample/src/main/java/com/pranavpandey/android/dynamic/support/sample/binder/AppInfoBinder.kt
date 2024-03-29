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

package com.pranavpandey.android.dynamic.support.sample.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.adapter.AppInfoAdapter
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView
import com.pranavpandey.android.dynamic.util.DynamicPackageUtils


/**
 * A recycler view binder to display app info by using [DynamicRecyclerViewBinder].
 */
class AppInfoBinder(binderAdapter: AppInfoAdapter)
    : DynamicRecyclerViewBinder<AppInfoBinder.ViewHolder>(binderAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_info_app, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) { }

    override fun getItemCount(): Int {
        // Return item count.
        return 1
    }

    /**
     * Holder class to hold view holder item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val app: DynamicInfoView = view.findViewById(R.id.info_app)

        init {
            app.linksView!!.layoutManager =
                    DynamicLayoutUtils.getGridLayoutManager(app.context,
                            DynamicLayoutUtils.getGridCount(app.context),
                        GridLayoutManager.VERTICAL)
            DynamicLayoutUtils.setFullSpanForPosition(app.linksView)

            if (app.linksSubtitles != null) {
                val subtitles = app.linksSubtitles
                subtitles!![0] = String.format(
                    app.context.getString(R.string.ads_format_separator),
                    DynamicPackageUtils.getVersionName(app.context),
                    DynamicPackageUtils.getVersionCode(app.context)
                )
                app.linksSubtitles = subtitles
                app.onUpdate()
            }
        }
    }
}
