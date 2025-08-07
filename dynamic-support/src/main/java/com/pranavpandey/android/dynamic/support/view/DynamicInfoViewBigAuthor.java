/*
 * Copyright 2018-2024 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView;
import com.pranavpandey.android.dynamic.util.DynamicDeviceUtils;
import com.pranavpandey.android.dynamic.util.product.DynamicFlavor;

/**
 * A {@link DynamicInfoView} with bigger fallback icon.
 * <p>Showing author info according to the product flavor.
 *
 * @see DynamicFlavor
 */
public class DynamicInfoViewBigAuthor extends DynamicInfoView {

    public DynamicInfoViewBigAuthor(@NonNull Context context) {
        super(context);
    }

    public DynamicInfoViewBigAuthor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicInfoViewBigAuthor(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_info_view_big;
    }

    @Override
    protected void onInflate() {
        super.onInflate();

        if (DynamicFlavor.EXTERNAL.equals(
                DynamicTheme.getInstance().getProductFlavor())) {
            if (DynamicDeviceUtils.isSamsungOneUI()) {
                setLinksSubtitles(new CharSequence[]{
                        getContext().getString(R.string.ads_info_website_me_desc),
                        String.format(getContext().getString(R.string.ads_info_apps_desc),
                                getContext().getString(R.string.adu_store_samsung_galaxy_store))
                });
                setLinksUrls(new CharSequence[]{
                        getContext().getString(R.string.adu_url_me_website),
                        getContext().getString(R.string.adu_url_me_samsung_galaxy_store)
                });
            } else if (DynamicDeviceUtils.isHuaweiEMUI(getContext())) {
                setLinksSubtitles(new CharSequence[]{
                        getContext().getString(R.string.ads_info_website_me_desc),
                        String.format(getContext().getString(R.string.ads_info_apps_desc),
                                getContext().getString(R.string.adu_store_huawei_app_gallery))
                });
                setLinksUrls(new CharSequence[]{
                        getContext().getString(R.string.adu_url_me_website),
                        getContext().getString(R.string.adu_url_me_huawei_app_gallery)
                });
            }
        } else {
            setLinksSubtitles(new CharSequence[] {
                    getContext().getString(R.string.ads_info_website_me_desc),
                    String.format(getContext().getString(R.string.ads_info_apps_desc),
                            getContext().getString(R.string.adu_store_google_play))
            });
            setLinksUrls(new CharSequence[] {
                    getContext().getString(R.string.adu_url_me_website),
                    getContext().getString(R.string.adu_url_me_google_play)
            });
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        Dynamic.setVisibility(getFooterView(), getStatusView());
    }
}
