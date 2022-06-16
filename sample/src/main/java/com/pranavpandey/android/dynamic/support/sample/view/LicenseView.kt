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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pranavpandey.android.dynamic.support.model.DynamicInfo
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.adapter.LicensesAdapter
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import java.util.*

/**
 * License view to display a list of licences by using [LicensesAdapter]
 * and [DynamicRecyclerViewFrame].
 */
class LicenseView : DynamicRecyclerViewFrame {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
            : super(context, attrs) {
        setAdapter()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        setAdapter()
    }

    override fun getLayoutRes(): Int {
        return R.layout.ads_recycler_view_raw
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager? {
        return DynamicLayoutUtils.getStaggeredGridLayoutManager(
                DynamicLayoutUtils.getGridCount(context),
                StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setAdapter(): LicenseView {
        // Add licenses in the array.
        val licenses = ArrayList<DynamicInfo>()
        licenses.add(DynamicInfo()
                .setTitle(context.getString(R.string.ads_license_android))
                .setDescription(context.getString(R.string.ads_license_copy_android_google))
                .setLinks(resources.getStringArray(R.array.ads_license_links_apache_only))
                .setLinksSubtitles(resources.getStringArray(
                        R.array.ads_license_links_subtitles_license))
                .setLinksUrls(resources.getStringArray(R.array.ads_license_links_urls_apache_only))
                .setLinksIconsResId(R.array.ads_license_links_icons_license)
                .setLinksColorsResId(R.array.ads_license_links_colors_license)
                .setIconBig(DynamicResourceUtils.getDrawable(context, R.drawable.ads_ic_android)))
        licenses.add(DynamicInfo()
                .setTitle(context.getString(R.string.ads_license_google))
                .setDescription(context.getString(R.string.ads_license_copy_google))
                .setLinks(resources.getStringArray(R.array.ads_license_links_apache))
                .setLinksSubtitles(resources.getStringArray(R.array.ads_license_links_subtitles))
                .setLinksUrls(resources.getStringArray(R.array.ads_license_links_google))
                .setLinksIconsResId(R.array.ads_license_links_icons)
                .setLinksColorsResId(R.array.ads_license_links_colors)
                .setIconBig(DynamicResourceUtils.getDrawable(context, R.drawable.ads_ic_android)))
        licenses.add(DynamicInfo()
                .setTitle(context.getString(R.string.ads_license_ads_dynamic))
                .setDescription(context.getString(R.string.ads_license_copy_me_17))
                .setLinks(resources.getStringArray(R.array.ads_license_links_ads_dynamic))
                .setLinksSubtitles(resources.getStringArray(
                        R.array.ads_license_links_subtitles_ads_dynamic))
                .setLinksUrls(resources.getStringArray(R.array.ads_license_links_urls_ads_dynamic))
                .setLinksIconsResId(R.array.ads_license_links_icons_website)
                .setLinksColorsResId(R.array.ads_license_links_colors_website)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        context, R.drawable.ads_ic_extension)))

        // Set adapter for the recycler view.
        adapter = LicensesAdapter(licenses)
        return this
    }
}
