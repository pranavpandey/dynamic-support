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

package com.pranavpandey.android.dynamic.support.theme.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicViewUtils;

/**
 * A {@link DynamicActivity} to preview and share the {@link DynamicAppTheme}.
 */
public class DynamicThemeActivity extends DynamicActivity {

    /**
     * Text received from the intent.
     */
    private String mThemeUrl;

    /**
     * Bitmap uri received from the intent.
     */
    private Uri mThemeBitmapUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.ads_theme));
        setSubtitle(getString(R.string.ads_widget_preview));
        addHeader(R.layout.ads_header_appbar_text, true);
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_activity_frame;
    }

    @Override
    protected @Nullable Drawable getDefaultNavigationIcon() {
        return DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_close);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && DynamicIntent.ACTION_THEME_SHARE.equals(intent.getAction())) {
            onAppThemeChange();
        }
    }

    @Override
    public @Nullable AppTheme<?> getDynamicTheme() {
        return getDynamicIntentTheme();
    }

    @Override
    public void onAddHeader(@Nullable View view) {
        super.onAddHeader(view);

        onSetupTheme();
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        DynamicMenuUtils.forceMenuIcons(menu);
        setMenuItemVisible(R.id.ads_menu_theme_share,
                !TextUtils.isEmpty(mThemeUrl) || mThemeBitmapUri != null);
        setMenuItemVisible(R.id.ads_menu_theme_data, !TextUtils.isEmpty(mThemeUrl));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ads_menu_theme_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ads_menu_theme_share) {
            DynamicLinkUtils.share(this, (String) getTitle(), mThemeUrl, mThemeBitmapUri);
        } else if (item.getItemId() == R.id.ads_menu_theme_data_copy) {
            DynamicTheme.getInstance().copyThemeString(this, mThemeUrl);
        } else if (item.getItemId() == R.id.ads_menu_theme_data_share) {
            DynamicLinkUtils.share(this, (String) getTitle(), mThemeUrl);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will be called on setting up the theme.
     */
    protected void onSetupTheme() {
        if (getIntent() == null) {
            return;
        }

        mThemeUrl = getIntent().getStringExtra(DynamicIntent.EXTRA_THEME_URL);
        mThemeBitmapUri = getIntent().getParcelableExtra(DynamicIntent.EXTRA_THEME_BITMAP_URI);

        DynamicViewUtils.addView(getFrameContent(),
                R.layout.ads_layout_image, true);
        Dynamic.set(findViewById(R.id.ads_image_text), mThemeUrl);
        Dynamic.setText(findViewById(R.id.ads_header_appbar_title), R.string.ads_theme_code_desc);

        if (mThemeBitmapUri != null) {
            Dynamic.set(findViewById(R.id.ads_image),
                    DynamicBitmapUtils.getBitmap(getContext(), mThemeBitmapUri));
            Dynamic.setColorType(findViewById(R.id.ads_image), Theme.ColorType.NONE);

            setExtendedFAB(R.drawable.ads_ic_share, R.string.ads_nav_share,
                    getFABVisibility(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicLinkUtils.share(DynamicThemeActivity.this,
                            (String) getTitle(), mThemeUrl, mThemeBitmapUri);
                }
            });
        } else {
            removeExtendedFAB();
        }

        if (TextUtils.isEmpty(mThemeUrl)) {
            setMenuItemVisible(R.id.ads_menu_theme_data, false);
            Dynamic.setVisibility(findViewById(R.id.ads_image_text_card), View.GONE);
        } else if (DynamicThemeUtils.isValidTheme(mThemeUrl)) {
            Dynamic.setVisibility(findViewById(R.id.ads_image_text_card), View.VISIBLE);
            findViewById(R.id.ads_image_text_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicLinkUtils.viewUrl(DynamicThemeActivity.this, mThemeUrl);
                }
            });
        }

        if (mThemeBitmapUri == null) {
            Dynamic.setResource(findViewById(R.id.ads_image), R.drawable.ads_ic_style);
            Dynamic.setColorType(findViewById(R.id.ads_image), Theme.ColorType.PRIMARY);
        }

        if (TextUtils.isEmpty(mThemeUrl) && mThemeBitmapUri == null) {
            setMenuItemVisible(R.id.ads_menu_theme_share, false);
            Dynamic.setText(findViewById(R.id.ads_header_appbar_title), R.string.ads_theme_invalid);
            Dynamic.setText(findViewById(R.id.ads_image_text), R.string.ads_theme_invalid_desc);
        }
    }
}
