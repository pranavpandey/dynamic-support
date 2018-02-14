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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewNested;
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicItemsAdapter;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
import static com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils.ADS_DEFAULT_RESOURCE_VALUE;

/**
 * A {@link FrameLayout} with a icon, title and subtitle, description
 * and links functionality which can be used to show various information
 * according to the requirement. Links can be clickable or pass {@code null}
 * url to just show or completely hide them.
 */
public class DynamicInfoView extends FrameLayout {

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
     * Titles for the links used by this view.
     */
    private CharSequence[] mLinks;

    /**
     * Subtitles for the links used by this view.
     */
    private CharSequence[] mLinksSubtitles;

    /**
     * Urls for the links used by this view.
     */
    private CharSequence[] mLinksUrls;

    /**
     * Icons for the links used by this view.
     */
    private @ArrayRes int mLinksIconsResId;

    /**
     * Icons tint color for the links used by this view.
     */
    private @ArrayRes int mLinksColorsResId;

    /**
     * Array to store links drawable.
     */
    private Drawable[] mLinksDrawables;

    /**
     * Array to store links tint color.
     */
    private @ColorInt Integer[] mLinksColors;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

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
     * RecyclerView to show the links associated with this view.
     */
    private DynamicRecyclerViewNested mLinksView;

    /**
     * RecyclerView to show the links associated with this view.
     */
    private List<DynamicItem> mDynamicItems;

    public DynamicInfoView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicInfoView(@NonNull Context context,
                           @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicInfo);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_icon, ADS_DEFAULT_RESOURCE_VALUE));
            mTitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_title);
            mSubtitle = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_subtitle);
            mDescription = a.getString(R.styleable.DynamicInfo_ads_dynamicInfo_description);
            mIconBig = DynamicResourceUtils.getDrawable(getContext(), a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_iconBig, ADS_DEFAULT_RESOURCE_VALUE));
            mLinks = a.getTextArray(R.styleable.DynamicInfo_ads_dynamicInfo_links);
            mLinksSubtitles = a.getTextArray(R.styleable.DynamicInfo_ads_dynamicInfo_linksSubtitles);
            mLinksUrls = a.getTextArray(R.styleable.DynamicInfo_ads_dynamicInfo_linksUrls);
            mLinksIconsResId = a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_linksIcons, ADS_DEFAULT_RESOURCE_ID);
            mLinksColorsResId = a.getResourceId(
                    R.styleable.DynamicInfo_ads_dynamicInfo_linksColors, ADS_DEFAULT_RESOURCE_ID);
        } finally {
            a.recycle();
        }

        initialize();
    }

    /**
     * @return The layout used by this view. Override this to supply a
     *         different layout.
     */
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_info_view;
    }

    /**
     * Initialize the layout for this view.
     */
    private void initialize() {
        inflate(getContext(), getLayoutRes(), this);

        mIconView = findViewById(R.id.ads_info_view_icon);
        mTitleView = findViewById(R.id.ads_info_view_title);
        mSubtitleView = findViewById(R.id.ads_info_view_subtitle);
        mDescriptionView = findViewById(R.id.ads_info_view_description);
        mIconBigView = findViewById(R.id.ads_info_view_icon_big);
        mLinksView = findViewById(R.id.ads_info_links);

        mLinksView.getRecyclerView().setNestedScrollingEnabled(false);
        mDynamicItems = new ArrayList<>();
        update();
    }

    /**
     * Load this view according to the supplied parameters.
     */
    public void update() {
        if (mIcon != null) {
            mIconView.setImageDrawable(mIcon);
            mIconView.setVisibility(VISIBLE);
        } else {
            mIconView.setVisibility(GONE);
        }

        if (mTitle != null) {
            mTitleView.setText(mTitle);
            mTitleView.setVisibility(VISIBLE);
        } else {
            mTitleView.setVisibility(GONE);
        }

        if (mSubtitle != null) {
            mSubtitleView.setText(mSubtitle);
            mSubtitleView.setVisibility(VISIBLE);
        } else {
            mSubtitleView.setVisibility(GONE);
        }

        if (mDescription != null) {
            mDescriptionView.setText(mDescription);
            mDescriptionView.setVisibility(VISIBLE);
        } else {
            mDescriptionView.setVisibility(GONE);
        }

        if (mIconBig != null) {
            mIconBigView.setImageDrawable(mIconBig);
            findViewById(R.id.ads_info_view_icon_frame).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.ads_info_view_icon_frame).setVisibility(GONE);
        }

        mDynamicItems.clear();
        if (mLinks != null) {
            if (mLinksIconsResId != ADS_DEFAULT_RESOURCE_ID && mLinksDrawables == null) {
                mLinksDrawables = DynamicResourceUtils.convertToDrawableArray(
                        getContext(), mLinksIconsResId);
            }

            if (mLinksColorsResId != ADS_DEFAULT_RESOURCE_ID && mLinksColors == null) {
                mLinksColors = DynamicResourceUtils.convertToColorArray(
                        getContext(), mLinksColorsResId);
            }

            for (int i = 0; i < mLinks.length; i++) {
                final CharSequence title;
                final CharSequence subtitle;
                final CharSequence url;
                final Drawable icon;
                final @ColorInt int color;

                if (mLinks != null && mLinks[i] != null) {
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

                if (mLinksColors != null && mLinksColors[i] != ADS_DEFAULT_RESOURCE_VALUE) {
                    color = mLinksColors[i];
                } else {
                    color = WidgetDefaults.ADS_COLOR_UNKNOWN;
                }

                DynamicItem dynamicItem = new DynamicItem(icon, title, subtitle, color,
                        DynamicColorType.CUSTOM, false);

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
                mLinksView.setAdapter(new DynamicItemsAdapter(mDynamicItems));
            }
        }
    }

    /**
     * Getter for {@link #mIcon}.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Setter for {@link #mIcon}.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        update();
    }

    /**
     * Getter for {@link #mIconBig}.
     */
    public @Nullable Drawable getIconBig() {
        return mIconBig;
    }

    /**
     * Setter for {@link #mIconBig}.
     */
    public void setIconBig(@Nullable Drawable iconBig) {
        this.mIconBig = iconBig;

        update();
    }

    /**
     * Getter for {@link #mTitle}.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Setter for {@link #mTitle}.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        update();
    }

    /**
     * Getter for {@link #mSubtitle}.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Setter for {@link #mSubtitle}.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        update();
    }

    /**
     * Getter for {@link #mDescription}.
     */
    public @Nullable CharSequence getDescription() {
        return mDescription;
    }

    /**
     * Setter for {@link #mDescription}.
     */
    public void setDescription(@Nullable CharSequence description) {
        this.mDescription = description;

        update();
    }

    /**
     * Getter for {@link #mLinks}.
     */
    public @Nullable CharSequence[] getLinks() {
        return mLinks;
    }

    /**
     * Setter for {@link #mLinks}. Automatic refresh is disabled due to
     * the stability reasons. Please call {@link #update()} to refresh
     * the view.
     */
    public void setLinks(@Nullable CharSequence[] links) {
        this.mLinks = links;
    }

    /**
     * Getter for {@link #mLinksSubtitles}.
     */
    public @Nullable CharSequence[] getLinksSubtitles() {
        return mLinksSubtitles;
    }

    /**
     * Setter for {@link #mLinksSubtitles}. Automatic refresh is disabled
     * due to the stability reasons. Please call {@link #update()} to
     * refresh the view.
     */
    public void setLinksSubtitles(@Nullable CharSequence[] linksSubtitles) {
        this.mLinksSubtitles = linksSubtitles;
    }

    /**
     * Getter for {@link #mLinksUrls}.
     */
    public @Nullable CharSequence[] getLinksUrls() {
        return mLinksUrls;
    }

    /**
     * Setter for {@link #mLinksUrls}. Automatic refresh is disabled due to
     * the stability reasons. Please call {@link #update()} to refresh the
     * view.
     */
    public void setLinksUrls(@Nullable CharSequence[] linksUrls) {
        this.mLinksUrls = linksUrls;
    }

    /**
     * Getter for {@link #mLinksIconsResId}.
     */
    public @ArrayRes int getLinksIconsId() {
        return mLinksIconsResId;
    }

    /**
     * Setter for {@link #mLinksIconsResId}. Automatic refresh is disabled
     * due to the stability reasons. Please call {@link #update()} to
     * refresh the view.
     */
    public void setLinksIconsId(@ArrayRes int linksIconsId) {
        this.mLinksIconsResId = linksIconsId;
    }

    /**
     * Getter for {@link #mLinksColorsResId}.
     */
    public @ArrayRes int getLinksColorsId() {
        return mLinksColorsResId;
    }

    /**
     * Setter for {@link #mLinksColorsResId}. Automatic refresh is disabled
     * due to the stability reasons. Please call {@link #update()} to
     * refresh the view.
     */
    public void setLinksColorsId(@ArrayRes int linksColorsId) {
        this.mLinksColorsResId = linksColorsId;
    }

    /**
     * Getter for {@link #mLinksDrawables}.
     */
    public @Nullable Drawable[] getLinksDrawables() {
        return mLinksDrawables;
    }

    /**
     * Setter for {@link #mLinksDrawables}. Automatic refresh is disabled
     * due to the stability reasons. Please call {@link #update()} to
     * refresh the view.
     */
    public void setLinksDrawables(@Nullable Drawable[] linksDrawables) {
        this.mLinksDrawables = linksDrawables;
    }

    /**
     * Getter for {@link #mLinksColors}.
     */
    public @Nullable @ColorInt Integer[] getLinksColors() {
        return mLinksColors;
    }

    /**
     * Setter for {@link #mLinksColors}. Automatic refresh is disabled
     * due to the stability reasons. Please call {@link #update()} to
     * refresh the view.
     */
    public void setLinksColors(@Nullable @ColorInt Integer[] linksColors) {
        this.mLinksColors = linksColors;
    }

    /**
     * Getter for {@link #mIconView}.
     */
    public ImageView getIconView() {
        return mIconView;
    }

    /**
     * Getter for {@link #mIconBigView}.
     */
    public ImageView getIconBigView() {
        return mIconBigView;
    }

    /**
     * Getter for {@link #mTitleView}.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Getter for {@link #mSubtitleView}.
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Getter for {@link #mDescriptionView}.
     */
    public TextView getDescriptionView() {
        return mDescriptionView;
    }

    /**
     * Getter for {@link #mLinksView}.
     */
    public DynamicRecyclerViewFrame getLinksView() {
        return mLinksView;
    }
}
