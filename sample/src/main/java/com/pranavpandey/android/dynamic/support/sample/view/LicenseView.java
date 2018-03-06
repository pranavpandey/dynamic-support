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

package com.pranavpandey.android.dynamic.support.sample.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.adapter.LicensesAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

import java.util.ArrayList;

/**
 * License view to display a list of licences by using
 * {@link LicensesAdapter} and {@link DynamicRecyclerViewFrame}.
 */
public class LicenseView extends DynamicRecyclerViewFrame {

    public LicenseView(@NonNull Context context) {
        this(context, null);
    }

    public LicenseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setAdapter();
    }

    public LicenseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setAdapter();
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return DynamicLayoutUtils.getStaggeredGridLayoutManager(
                getContext(), StaggeredGridLayoutManager.VERTICAL);
    }

    public LicenseView setAdapter() {
        // Add licenses in the array.
        ArrayList<DynamicInfo> licenses = new ArrayList<>();
        licenses.add(new DynamicInfo()
                .setTitle(getContext().getString(R.string.ads_license_android))
                .setDescription(getContext().getString(R.string.ads_license_copy_android_google))
                .setLinks(getResources().getStringArray(R.array.ads_license_links_apache_only))
                .setLinksSubtitles(getResources().getStringArray(
                        R.array.ads_license_links_subtitles_license))
                .setLinksUrls(getResources().getStringArray(
                        R.array.ads_license_links_urls_apache_only))
                .setLinksIconsId(R.array.ads_license_links_icons_license)
                .setLinksColorsId(R.array.ads_license_links_colors_license)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_android)));
        licenses.add(new DynamicInfo()
                .setTitle(getContext()
                .getString(R.string.ads_license_plaid))
                .setDescription(getContext().getString(R.string.ads_license_copy_google_15))
                .setLinks(getResources().getStringArray(
                        R.array.ads_license_links_apache))
                .setLinksSubtitles(getResources().getStringArray(
                        R.array.ads_license_links_subtitles))
                .setLinksUrls(getResources().getStringArray(
                        R.array.ads_license_links_urls_plaid))
                .setLinksIconsId(R.array.ads_license_links_icons)
                .setLinksColorsId(R.array.ads_license_links_colors)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_android)));
        licenses.add(new DynamicInfo()
                .setTitle(getContext().getString(R.string.ads_license_sas))
                .setDescription(getContext().getString(R.string.ads_license_copy_me_16))
                .setLinks(getResources().getStringArray(R.array.ads_license_links_apache))
                .setLinksSubtitles(getResources().getStringArray(
                        R.array.ads_license_links_subtitles))
                .setLinksUrls(getResources().getStringArray(
                        R.array.ads_license_links_urls_sas))
                .setLinksIconsId(R.array.ads_license_links_icons)
                .setLinksColorsId(R.array.ads_license_links_colors)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_extension)));
        licenses.add(new DynamicInfo()
                .setTitle(getContext()
                .getString(R.string.ads_license_ads_modules))
                .setDescription(getContext().getString(R.string.ads_license_copy_me_17))
                .setLinks(getResources().getStringArray(
                        R.array.ads_license_links_ads_modules))
                .setLinksSubtitles(getResources().getStringArray(
                        R.array.ads_license_links_subtitles_ads_modules))
                .setLinksUrls(getResources().getStringArray(
                        R.array.ads_license_links_urls_ads_modules))
                .setLinksIconsId(R.array.ads_license_links_icons_ads_modules)
                .setLinksColorsId(R.array.ads_license_links_colors_ads_modules)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_extension)));
        licenses.add(new DynamicInfo()
                .setTitle(getContext()
                        .getString(R.string.ads_license_ads))
                .setDescription(getContext().getString(R.string.ads_license_copy_me_18))
                .setLinks(getResources().getStringArray(
                        R.array.ads_license_links_apache))
                .setLinksSubtitles(getResources().getStringArray(
                        R.array.ads_license_links_subtitles))
                .setLinksUrls(getResources().getStringArray(
                        R.array.ads_license_links_urls_ads))
                .setLinksIconsId(R.array.ads_license_links_icons)
                .setLinksColorsId(R.array.ads_license_links_colors)
                .setIconBig(DynamicResourceUtils.getDrawable(
                        getContext(), R.drawable.ads_ic_extension)));

        // Set adapter for the recycler view.
        setAdapter(new LicensesAdapter(licenses));

        return this;
    }
}
