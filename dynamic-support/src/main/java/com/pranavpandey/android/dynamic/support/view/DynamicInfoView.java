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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicItemsAdapter;
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DynamicView} with an icon, title and subtitle, description and links functionality
 * that can be used to show various information according to the requirement.
 * <p>Links can be clickable or pass {@code null} url to just show or completely hide them.
 */
public class DynamicInfoView extends DynamicView {

    /**
     * Icon used by this view.
     */
    private Drawable mIcon;

    /**
     * Big fallback icon used by this view.
     */
    private Drawable mIconBig;

    /**
     * Title used by this view.
     */
    private CharSequence mTitle;

    /**
     * Subtitle used by this view.
     */
    private CharSequence mSubtitle;

    /**
     * Description used by this view.
     */
    private CharSequence mDescription;

    /**
     * Status used by this view.
     */
    private CharSequence mStatus;

    /**
     * Title for the links used by this view.
     */
    private CharSequence[] mLinks;

    /**
     * Subtitle for the links used by this view.
     */
    private CharSequence[] mLinksSubtitles;

    /**
     * Url for the links used by this view.
     */
    private CharSequence[] mLinksUrls;

    /**
     * Icon drawables array resource for the links used by this view.
     */
    private @ArrayRes int mLinksIconsResId;

    /**
     * Icon tint colors array resource for the links used by this view.
     */
    private @ArrayRes int mLinksColorsResId;

    /**
     * Icon drawable for the links used by this view.
     */
    private Drawable[] mLinksDrawables;

    /**
     * Icon tint color for the links used by this view.
     */
    private @ColorInt Integer[] mLinksColors;

    /**
     * Default visibility of the icon view.
     */
    private int mVisibilityIconView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Image view to show the footer icon.
     */
    private ImageView mIconFooterView;

    /**
     * Image view to show the big fallback icon.
     */
    private ImageView mIconBigView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the subtitle.
     */
    private TextView mSubtitleView;

    /**
     * Text view to show the description.
     */
    private TextView mDescriptionView;

    /**
     * Text view to show the status.
     */
    private TextView mStatusView;

    /**
     * Recycler view to show the links associated with this view.
     */
    private RecyclerView mLinksView;

    /**
     * A list to hold the dynamic items used by this view.
     */
    private List<DynamicItem> mDynamicItems;

    public DynamicInfoView(@NonNull Context context) {
        super(context);
    }

    public DynamicInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicInfoView(@NonNull Context context, 
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicInfoView);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(), 
                    a.getResourceId(
                            R.styleable.DynamicInfoView_ads_icon, 
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE));
            mTitle = a.getString(
                    R.styleable.DynamicInfoView_ads_title);
            mSubtitle = a.getString(
                    R.styleable.DynamicInfoView_ads_subtitle);
            mDescription = a.getString(
                    R.styleable.DynamicInfoView_ads_description);
            mStatus = a.getString(
                    R.styleable.DynamicInfoView_ads_status);
            mIconBig = DynamicResourceUtils.getDrawable(getContext(), 
                    a.getResourceId(
                            R.styleable.DynamicInfoView_ads_iconBig, 
                            DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE));
            mLinks = a.getTextArray(
                    R.styleable.DynamicInfoView_ads_links);
            mLinksSubtitles = a.getTextArray(
                    R.styleable.DynamicInfoView_ads_subtitles);
            mLinksUrls = a.getTextArray(
                    R.styleable.DynamicInfoView_ads_urls);
            mLinksIconsResId = a.getResourceId(
                    R.styleable.DynamicInfoView_ads_icons,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
            mLinksColorsResId = a.getResourceId(
                    R.styleable.DynamicInfoView_ads_colors,
                    DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_info_view;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mIconView = findViewById(R.id.ads_info_view_icon);
        mIconFooterView = findViewById(R.id.ads_info_view_icon_footer);
        mTitleView = findViewById(R.id.ads_info_view_title);
        mSubtitleView = findViewById(R.id.ads_info_view_subtitle);
        mDescriptionView = findViewById(R.id.ads_info_view_description);
        mStatusView = findViewById(R.id.ads_info_view_status);
        mIconBigView = findViewById(R.id.ads_info_view_icon_big);
        mLinksView = findViewById(R.id.ads_recycler_view);

        mVisibilityIconView = mIconView.getVisibility();
        mDynamicItems = new ArrayList<>();

        ViewCompat.setNestedScrollingEnabled(mLinksView, false);
        onUpdate();
    }

    @Override
    public void onUpdate() {
        Dynamic.set(getIconView(), getIcon());
        Dynamic.set(getIconBigView(), getIconBig());
        Dynamic.set(getTitleView(), getTitle());
        Dynamic.set(getSubtitleView(), getSubtitle());
        Dynamic.set(getDescriptionView(), getDescription());
        Dynamic.set(getStatusView(), getStatus());

        if (getVisibilityIconView() != VISIBLE) {
            Dynamic.setVisibility(getIconView(), getVisibilityIconView());
        }
        Dynamic.setVisibility(getIconFooterView(), getIconView());

        mDynamicItems.clear();
        if (mLinks != null) {
            if (mLinksIconsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID
                    && mLinksDrawables == null) {
                mLinksDrawables = DynamicResourceUtils.convertToDrawableArray(
                        getContext(), mLinksIconsResId);
            }

            if (mLinksColorsResId != DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID
                    && mLinksColors == null) {
                mLinksColors = DynamicResourceUtils.convertToColorArray(
                        getContext(), mLinksColorsResId);
            }

            for (int i = 0; i < mLinks.length; i++) {
                final CharSequence title;
                final CharSequence subtitle;
                final CharSequence url;
                final Drawable icon;
                final @ColorInt int color;

                if (mLinks[i] != null) {
                    title = mLinks[i].toString();
                } else {
                    title = null;
                }

                if (mLinksSubtitles != null && mLinksSubtitles[i] != null) {
                    subtitle = mLinksSubtitles[i].toString();
                } else {
                    subtitle = null;
                }

                if (mLinksUrls != null && mLinksUrls[i] != null) {
                    url = mLinksUrls[i].toString();
                } else {
                    url = null;
                }

                if (mLinksDrawables != null && mLinksDrawables[i] != null) {
                    icon = mLinksDrawables[i];
                } else {
                    icon = null;
                }

                if (mLinksColors != null && mLinksColors[i] !=
                        DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE) {
                    color = mLinksColors[i];
                } else {
                    color = Theme.Color.UNKNOWN;
                }

                DynamicItem dynamicItem = new DynamicItem(icon, title, subtitle,
                        color, Theme.ColorType.CUSTOM, false);

                if (url != null) {
                    dynamicItem.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DynamicLinkUtils.viewUrl(getContext(), url.toString());
                        }
                    });
                }

                mDynamicItems.add(dynamicItem);
            }

            if (!mDynamicItems.isEmpty()) {
                if (mLinksView.getLayoutManager() == null) {
                    mLinksView.setLayoutManager(DynamicLayoutUtils.getLinearLayoutManager(
                            getContext(), LinearLayoutManager.VERTICAL));
                }

                mLinksView.setAdapter(new DynamicItemsAdapter(mDynamicItems));
            }
        }
    }

    /**
     * Get the icon used by this view.
     *
     * @return The icon used by this view.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon for this view.
     *
     * @param icon The icon to be set.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        onUpdate();
    }

    /**
     * Ge the big fallback icon used by this view.
     *
     * @return The big fallback icon used by this view.
     */
    public @Nullable Drawable getIconBig() {
        return mIconBig;
    }

    /**
     * Set the big fallback icon for this view.
     *
     * @param iconBig The big fallback icon to be set.
     */
    public void setIconBig(@Nullable Drawable iconBig) {
        this.mIconBig = iconBig;

        onUpdate();
    }

    /**
     * Get the title used by this view.
     *
     * @return The title used by this view.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title for this view.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        onUpdate();
    }

    /**
     * Get the subtitle used by this view.
     *
     * @return The subtitle used by this view.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Set the subtitle for this view.
     *
     * @param subtitle The subtitle to be set.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        onUpdate();
    }

    /**
     * Get the description used by this view.
     *
     * @return The description used by this view.
     */
    public @Nullable CharSequence getDescription() {
        return mDescription;
    }

    /**
     * Set the description for this view.
     *
     * @param description The description to be set.
     */
    public void setDescription(@Nullable CharSequence description) {
        this.mDescription = description;

        onUpdate();
    }

    /**
     * Get the status used by this view.
     *
     * @return The status used by this view.
     */
    public @Nullable CharSequence getStatus() {
        return mStatus;
    }

    /**
     * Set the status for this view.
     *
     * @param status The status to be set.
     */
    public void setStatus(@Nullable CharSequence status) {
        this.mStatus = status;

        onUpdate();
    }

    /**
     * Get the title for the links used by this view.
     *
     * @return The title for the links used by this view.
     */
    public @Nullable CharSequence[] getLinks() {
        return mLinks;
    }

    /**
     * Set the title for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param links The titles for the links to be set.
     */
    public void setLinks(@Nullable CharSequence[] links) {
        this.mLinks = links;
    }

    /**
     * Get the subtitle for the links used by this view.
     *
     * @return The subtitle for the links used by this view.
     */
    public @Nullable CharSequence[] getLinksSubtitles() {
        return mLinksSubtitles;
    }

    /**
     * Set the subtitle for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksSubtitles The subtitles for the links to be set.
     */
    public void setLinksSubtitles(@Nullable CharSequence[] linksSubtitles) {
        this.mLinksSubtitles = linksSubtitles;
    }

    /**
     * Get the url for the links used by this view.
     *
     * @return The url for the links used by this view.
     */
    public @Nullable CharSequence[] getLinksUrls() {
        return mLinksUrls;
    }

    /**
     * Set the url for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksUrls The urls for the links to be set.
     */
    public void setLinksUrls(@Nullable CharSequence[] linksUrls) {
        this.mLinksUrls = linksUrls;
    }

    /**
     * Get the icons array resource for the links used by this view.
     *
     * @return The icons array resource for the links used by this view.
     */
    public @ArrayRes int getLinksIconsId() {
        return mLinksIconsResId;
    }

    /**
     * Set the icons array resource for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksIconsResId The icon drawables array resource for the links to be set.
     */
    public void setLinksIconsId(@ArrayRes int linksIconsResId) {
        this.mLinksIconsResId = linksIconsResId;
    }

    /**
     * Get the icon tint colors array resource for the links used by this view.
     *
     * @return The icon tint colors array resource for the links used by this view.
     */
    public @ArrayRes int getLinksColorsId() {
        return mLinksColorsResId;
    }

    /**
     * Set the icon tint colors array resource for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksColorsResId The icon tint colors array resource for the links to be set.
     */
    public void setLinksColorsId(@ArrayRes int linksColorsResId) {
        this.mLinksColorsResId = linksColorsResId;
    }

    /**
     * Get the icon for the links used by this view.
     *
     * @return The icon for the links used by this view.
     */
    public @Nullable Drawable[] getLinksDrawables() {
        return mLinksDrawables;
    }

    /**
     * Set the icon for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksDrawables The icon drawables for the links to be set.
     */
    public void setLinksDrawables(@Nullable Drawable[] linksDrawables) {
        this.mLinksDrawables = linksDrawables;
    }

    /**
     * Get the icon tint color for the links used by this view.
     *
     * @return The icon tint color for the links used by this view.
     */
    public @Nullable @ColorInt Integer[] getLinksColors() {
        return mLinksColors;
    }

    /**
     * Set the icon tint color for the links used by this view.
     *
     * <p>Automatic refresh is disabled due to the stability reasons.
     * <p>Please call {@link #onUpdate()} to refresh the view.
     *
     * @param linksColors The icon tint color for the links to be set.
     */
    public void setLinksColors(@Nullable @ColorInt Integer[] linksColors) {
        this.mLinksColors = linksColors;
    }

    /**
     * Returns the visibility of the icon view.
     *
     * @return The visibility of the icon view.
     */
    public int getVisibilityIconView() {
        return mVisibilityIconView;
    }

    /**
     * Get the image view to show the icon used by this view.
     *
     * @return The image view to show the icon used by this view.
     */
    public @Nullable ImageView getIconView() {
        return mIconView;
    }

    /**
     * Get the image view to show the footer icon used by this view.
     *
     * @return The image view to show the footer icon used by this view.
     */
    public @Nullable ImageView getIconFooterView() {
        return mIconFooterView;
    }

    /**
     * Get the image view to show big fallback icon used by this view.
     *
     * @return The image view to show big fallback icon used by this view.
     */
    public @Nullable ImageView getIconBigView() {
        return mIconBigView;
    }

    /**
     * Get the text view to show the title used by this view.
     *
     * @return The text view to show the title used by this view.
     */
    public @Nullable TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the text view to show the subtitle used by this view.
     *
     * @return The text view to show the subtitle used by this view.
     */
    public @Nullable TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Get the text view to show the description used by this view.
     *
     * @return The text view to show the description used by this view.
     */
    public @Nullable TextView getDescriptionView() {
        return mDescriptionView;
    }

    /**
     * Get the text view to show the status used by this view.
     *
     * @return The text view to show the status used by this view.
     */
    public @Nullable TextView getStatusView() {
        return mStatusView;
    }

    /**
     * Get the recycler view to show the links associated with this view.
     *
     * @return The recycler view to show the links associated with this view.
     */
    public @Nullable RecyclerView getLinksView() {
        return mLinksView;
    }
}
