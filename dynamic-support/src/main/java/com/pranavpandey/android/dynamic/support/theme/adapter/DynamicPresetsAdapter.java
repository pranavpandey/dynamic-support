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

package com.pranavpandey.android.dynamic.support.theme.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.view.DynamicPresetsView;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.support.utils.DynamicTintUtils;
import com.pranavpandey.android.dynamic.theme.ThemeContract;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pranavpandey.android.dynamic.support.theme.adapter.DynamicPresetsAdapter.Type.HORIZONTAL;
import static com.pranavpandey.android.dynamic.support.theme.adapter.DynamicPresetsAdapter.Type.VERTICAL;

/**
 * A recycler view adapter to show the theme presets.
 */
public class DynamicPresetsAdapter<T extends DynamicAppTheme>
        extends RecyclerView.Adapter<DynamicPresetsAdapter.ViewHolder<T>> {

    /**
     * Interface to hold the presets layout according to the recycler view orientation.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { VERTICAL, HORIZONTAL })
    public @interface Type {

        /**
         * Constant for the vertical orientation.
         */
        int VERTICAL = 1;

        /**
         * Constant for the horizontal orientation.
         */
        int HORIZONTAL = 2;
    }

    /**
     * Layout inflater used by this adapter.
     */
    private final LayoutInflater mInflater;

    /**
     * Cursor for presets handled by this adapter.
     */
    private Cursor mPresets;

    /**
     * Type of the preset layout used by this adapter.
     */
    private @Type int mType;

    /**
     * Layout resource containing the theme preview.
     *
     * @see ThemePreview
     */
    private @LayoutRes int mLayoutRes;

    /**
     * Listener to handle the preset events.
     */
    private DynamicPresetsView.DynamicPresetsListener<T> mDynamicPresetsListener;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to get the layout inflater.
     * @param type The type of the preset layout.
     */
    public DynamicPresetsAdapter(@NonNull Context context, @Type int type) {
        this(context, type, type == VERTICAL ? R.layout.ads_layout_item_preset
                : R.layout.ads_layout_item_preset_horizontal);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to get the layout inflater.
     * @param type The type of the preset layout.
     * @param layoutRes The layout resource containing the theme preview.
     *
     * @see ThemePreview
     */
    public DynamicPresetsAdapter(@NonNull Context context,
            @Type int type, @LayoutRes int layoutRes) {
        this.mInflater = LayoutInflater.from(context);
        this.mType = type;
        this.mLayoutRes = layoutRes;
    }

    @Override
    public @NonNull ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(mInflater.inflate(mLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        if (mPresets == null) {
            Dynamic.setVisibility(holder.getRoot(), View.GONE);
            return;
        }

        Dynamic.setVisibility(holder.getRoot(), View.VISIBLE);

        try {
            T theme = null;
            if (mPresets.moveToPosition(position)) {
                String decodeTheme = DynamicThemeUtils.decodeTheme(
                        mPresets.getString(mPresets.getColumnIndexOrThrow(
                                ThemeContract.Preset.Column.THEME)));

                if (decodeTheme != null) {
                    theme = mDynamicPresetsListener.getDynamicTheme(decodeTheme);
                }
            }

            if (theme == null) {
                Dynamic.setVisibility(holder.getRoot(), View.GONE);
                return;
            }

            holder.getThemePreview().getActionView()
                    .setImageResource(R.drawable.ads_ic_palette);
            holder.getThemePreview().setDynamicTheme(theme);
            Dynamic.setCorner(holder.getRoot(), theme.getCornerRadius());
            DynamicTintUtils.setViewForegroundTint(holder.getRoot(),
                    theme.getBackgroundColor(), false);

            if (mDynamicPresetsListener != null) {
                holder.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDynamicPresetsListener.onPresetClick(v,
                                holder.getThemePreview().getDynamicTheme().toDynamicString(),
                                holder.getThemePreview());
                    }
                });
                holder.getThemePreview().getActionView().setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDynamicPresetsListener.onPresetClick(v,
                                        holder.getThemePreview().getDynamicTheme().toDynamicString(),
                                        holder.getThemePreview());
                            }
                        });
            } else {
                holder.getRoot().setClickable(false);
                holder.getThemePreview().getActionView().setClickable(false);
            }
        } catch (Exception ignored) {
            Dynamic.setVisibility(holder.getRoot(), View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPresets == null ? 0 : mPresets.getCount();
    }

    /**
     * Sets the theme presets for this adapter.
     *
     * @param presets The theme presets to be set.
     */
    public void setPresets(@Nullable Cursor presets) {
        mPresets = presets;

        notifyDataSetChanged();
    }

    /**
     * Returns the layout resource containing the theme preview.
     *
     * @return The layout resource containing the theme preview.
     */
    public @LayoutRes int getLayoutRes() {
        return mLayoutRes;
    }

    /**
     * Get the dynamic preset listener used by this adapter.
     *
     * @return The dynamic preset listener used by this adapter.
     */
    public @Nullable DynamicPresetsView.DynamicPresetsListener<T> getDynamicPresetsListener() {
        return mDynamicPresetsListener;
    }

    /**
     * Sets the dynamic preset listener for this adapter.
     *
     * @param dynamicPresetsListener The listener to be set.
     */
    public void setDynamicPresetsListener(
            @Nullable DynamicPresetsView.DynamicPresetsListener<T> dynamicPresetsListener) {
        this.mDynamicPresetsListener = dynamicPresetsListener;

        notifyDataSetChanged();
    }

    /**
     * View holder class to hold the preset view.
     */
    static class ViewHolder<T extends DynamicAppTheme> extends RecyclerView.ViewHolder {

        /**
         * Item view root.
         */
        private final ViewGroup root;

        /**
         * Theme preview to display the preset.
         */
        private final ThemePreview<T> themePreview;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
        ViewHolder(@NonNull View view) {
            super(view);

            root = view.findViewById(R.id.ads_preset_root);
            themePreview = view.findViewById(R.id.ads_preset_theme_preview);
        }

        /**
         * Get the item view root.
         *
         * @return The item view root.
         */
        ViewGroup getRoot() {
            return root;
        }

        /**
         * Get the theme preview to display the preset.
         *
         * @return The theme preview to display the preset.
         */
        ThemePreview<T> getThemePreview() {
            return themePreview;
        }
    }
}
