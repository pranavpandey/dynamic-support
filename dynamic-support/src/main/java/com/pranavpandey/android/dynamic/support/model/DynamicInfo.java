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

package com.pranavpandey.android.dynamic.support.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * A model class to hold the dynamic information which can be used
 * by the {@link com.pranavpandey.android.dynamic.support.view.DynamicInfoView}.
 */
public class DynamicInfo {

    /**
     * Icon used by this view.
     */
    private Drawable icon;

    /**
     * Big fallback icon used by this view.
     */
    private Drawable iconBig;

    /**
     * Title used by this view.
     */
    private CharSequence title;

    /**
     * Subtitle used by this view.
     */
    private CharSequence subtitle;

    /**
     * Description used by this view.
     */
    private CharSequence description;

    /**
     * Title for the links used by this view.
     */
    private CharSequence[] links;

    /**
     * Subtitle for the links used by this view.
     */
    private CharSequence[] linksSubtitles;

    /**
     * Url for the links used by this view.
     */
    private CharSequence[] linksUrls;

    /**
     * Icon for the links used by this view.
     */
    private @ArrayRes int linksIconsId;

    /**
     * Icon tint color for the links used by this view.
     */
    private @ArrayRes int linksColorsId;

    /**
     * Array to store links drawable.
     */
    private Drawable[] linksDrawables;

    /**
     * Array to store links tint color.
     */
    private @ColorInt Integer[] linksColors;

    /**
     * Getter for {@link #icon}.
     */
    public @Nullable Drawable getIcon() {
        return icon;
    }

    /**
     * Setter for {@link #icon}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setIcon(@Nullable Drawable icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Getter for {@link #iconBig}.
     */
    public @Nullable Drawable getIconBig() {
        return iconBig;
    }

    /**
     * Setter for {@link #iconBig}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setIconBig(@Nullable Drawable iconBig) {
        this.iconBig = iconBig;

        return this;
    }

    /**
     * Getter for {@link #title}.
     */
    public @Nullable CharSequence getTitle() {
        return title;
    }

    /**
     * Setter for {@link #title}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setTitle(@Nullable CharSequence title) {
        this.title = title;

        return this;
    }

    /**
     * Getter for {@link #subtitle}.
     */
    public @Nullable CharSequence getSubtitle() {
        return subtitle;
    }

    /**
     * Setter for {@link #subtitle}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setSubtitle(@Nullable CharSequence subtitle) {
        this.subtitle = subtitle;

        return this;
    }

    /**
     * Getter for {@link #description}.
     */
    public @Nullable CharSequence getDescription() {
        return description;
    }

    /**
     * Setter for {@link #description}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setDescription(@Nullable CharSequence description) {
        this.description = description;

        return this;
    }

    /**
     * Getter for {@link #links}.
     */
    public @Nullable CharSequence[] getLinks() {
        return links;
    }

    /**
     * Setter for {@link #links}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinks(@Nullable CharSequence[] links) {
        this.links = links;

        return this;
    }

    /**
     * Getter for {@link #linksSubtitles}.
     */
    public @Nullable CharSequence[] getLinksSubtitles() {
        return linksSubtitles;
    }

    /**
     * Setter for {@link #linksSubtitles}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksSubtitles(@Nullable CharSequence[] linksSubtitles) {
        this.linksSubtitles = linksSubtitles;

        return this;
    }

    /**
     * Getter for {@link #linksUrls}.
     */
    public @Nullable CharSequence[] getLinksUrls() {
        return linksUrls;
    }

    /**
     * Setter for {@link #linksUrls}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksUrls(@Nullable CharSequence[] linksUrls) {
        this.linksUrls = linksUrls;

        return this;
    }

    /**
     * Getter for {@link #linksIconsId}.
     */
    public @ArrayRes int getLinksIconsId() {
        return linksIconsId;
    }

    /**
     * Setter for {@link #linksIconsId}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksIconsId(@ArrayRes int linksIconsId) {
        this.linksIconsId = linksIconsId;

        return this;
    }

    /**
     * Getter for {@link #linksColorsId}.
     */
    public @ArrayRes int getLinksColorsId() {
        return linksColorsId;
    }

    /**
     * Setter for {@link #linksColorsId}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksColorsId(@ArrayRes int linksColorsId) {
        this.linksColorsId = linksColorsId;

        return this;
    }

    /**
     * Getter for {@link #linksDrawables}.
     */
    public @Nullable Drawable[] getLinksDrawables() {
        return linksDrawables;
    }

    /**
     * Setter for {@link #linksDrawables}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksDrawables(@Nullable Drawable[] linksDrawables) {
        this.linksDrawables = linksDrawables;

        return this;
    }

    /**
     * Getter for {@link #linksColors}.
     */
    public @Nullable @ColorInt Integer[] getLinksColors() {
        return linksColors;
    }

    /**
     * Setter for {@link #linksColors}.
     *
     * @return {@link DynamicInfo} object to allow for chaining of
     *         calls to set methods.
     */
    public DynamicInfo setLinksColors(@Nullable @ColorInt Integer[] linksColors) {
        this.linksColors = linksColors;

        return this;
    }
}
