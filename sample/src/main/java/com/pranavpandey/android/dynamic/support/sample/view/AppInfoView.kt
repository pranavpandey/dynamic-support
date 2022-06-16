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

package com.pranavpandey.android.dynamic.support.sample.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.adapter.AppInfoAdapter
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils

/**
 * App info view to display app and developer info by using [AppInfoAdapter]
 * and [DynamicRecyclerViewFrame].
 */
class AppInfoView : DynamicRecyclerViewFrame {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
            : super(context, attrs) {
        setAdapter()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        setAdapter()
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager? {
        return DynamicLayoutUtils.getLinearLayoutManager(context, LinearLayoutManager.VERTICAL)
    }

    override fun getLayoutRes(): Int {
        return R.layout.ads_recycler_view_raw
    }

    private fun setAdapter(): AppInfoView {
        // Set adapter for the recycler view.
        adapter = AppInfoAdapter()

        return this
    }
}
