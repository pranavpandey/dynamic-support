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

package com.pranavpandey.android.dynamic.support.preview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
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
import androidx.lifecycle.ViewModelProvider;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicProgressDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicSizeDialog;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.listener.DynamicValueListener;
import com.pranavpandey.android.dynamic.support.model.DynamicTaskViewModel;
import com.pranavpandey.android.dynamic.support.preview.Preview;
import com.pranavpandey.android.dynamic.support.preview.factory.ImagePreview;
import com.pranavpandey.android.dynamic.support.preview.listener.DynamicPreview;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.DynamicFileUtils;
import com.pranavpandey.android.dynamic.util.DynamicIntentUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;
import com.pranavpandey.android.dynamic.util.DynamicViewUtils;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicResult;
import com.pranavpandey.android.dynamic.util.concurrent.DynamicTask;
import com.pranavpandey.android.dynamic.util.concurrent.task.FileWriteTask;

/**
 * A {@link DynamicActivity} to preview and share the image and text data.
 */
public class DynamicPreviewActivity extends DynamicActivity
        implements DynamicPreview<ImagePreview, Bitmap, Point> {

    /**
     * Preview received from the intent.
     */
    private ImagePreview mPreview;

    /**
     * Dialog fragment to show the progress.
     */
    protected DynamicDialogFragment mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.ads_widget_preview));
        setSubtitle(R.string.ads_theme);
        
        if (getIntent() != null) {
            mPreview = getIntent().getParcelableExtra(DynamicIntent.EXTRA_PREVIEW);
        }

        if (getSavedInstanceState() != null && getSavedInstanceState().containsKey(Preview.KEY)) {
            mPreview = getSavedInstanceState().getParcelable(Preview.KEY);
        }

        addHeader(R.layout.ads_header_appbar_text, true); 
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
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_activity_frame;
    }

    @Override
    protected @Nullable Drawable getDefaultNavigationIcon() {
        return DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_close);
    }

    @Override
    public void onAddHeader(@Nullable View view) {
        super.onAddHeader(view);

        onPreview();
    }

    @Override
    public @NonNull ImagePreview getPreview() {
        if (mPreview == null) {
            this.mPreview = new ImagePreview();
        }

        return mPreview;
    }

    @Override
    public @Nullable Bitmap getPreviewData() {
        if (getPreview().getData(false) != null) {
            return DynamicBitmapUtils.getBitmap(getContext(), getPreview().getData(false));
        }

        return null;
    }

    @Override
    public @NonNull Point getPreviewSize() {
        final Bitmap bitmap;
        if ((bitmap = getPreviewData()) != null) {
            final Point size = new Point(bitmap.getWidth(), bitmap.getHeight());
            bitmap.recycle();

            return size;
        }

        return new Point(Theme.Size.DEFAULT, Theme.Size.DEFAULT);
    }
    
    @Override
    public @Nullable String getPreviewHint() {
        return getString(R.string.ads_theme_code_desc);
    }

    @Override
    public @Nullable Drawable getPreviewPlaceholder() {
        return DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_image);
    }

    @Override
    public @Nullable Drawable getFallbackDrawable() {
        return DynamicResourceUtils.getDrawable(getContext(), R.drawable.adt_ic_app);
    }

    @Override
    public void onPreview() {
        if (getIntent() == null) {
            return;
        }

        if (getPreview().getTitle() != null) {
            setSubtitle(getPreview().getTitle());
        }
        
        Dynamic.set(findViewById(R.id.ads_header_appbar_title), getPreviewHint());
        DynamicViewUtils.addView(getFrameContent(), R.layout.ads_preview_image, true);
        Dynamic.set(findViewById(R.id.ads_preview_fallback_image), getFallbackDrawable());

        if (getPreview().getData(false) != null) {
            setMenuItemVisible(R.id.ads_menu_preview_data, true);
            Dynamic.set(findViewById(R.id.ads_preview_image), getPreviewData());
            Dynamic.setColorType(findViewById(R.id.ads_preview_image), Theme.ColorType.NONE);

            setExtendedFAB(R.drawable.ads_ic_share, R.string.ads_nav_share,
                    getFABVisibility(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicLinkUtils.share(DynamicPreviewActivity.this,
                            getShareTitle(), getPreview().getInfo(), getPreview().getData());
                }
            });
        } else {
            setMenuItemVisible(R.id.ads_menu_preview_data, false);
            Dynamic.set(findViewById(R.id.ads_preview_image), getPreviewPlaceholder());
            Dynamic.setColorType(findViewById(R.id.ads_preview_image), Theme.ColorType.PRIMARY);
            removeExtendedFAB();
        }

        if (!TextUtils.isEmpty(getPreview().getInfo())) {
            setMenuItemVisible(R.id.ads_menu_preview_info, true);
            Dynamic.set(findViewById(R.id.ads_preview_text), getPreview().getInfo());
            
            Dynamic.setOnClickListener(findViewById(R.id.ads_preview_text_content),
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPreviewClick();
                }
            });
        } else {
            setMenuItemVisible(R.id.ads_menu_preview_info, false);
            Dynamic.setClickable(findViewById(R.id.ads_preview_text_content), false);
            Dynamic.setText(findViewById(R.id.ads_preview_text), R.string.ads_data_invalid_desc);
        }

        if (TextUtils.isEmpty(getPreview().getInfo()) 
                && getPreview().getData(false) == null) {
            Dynamic.setText(findViewById(R.id.ads_header_appbar_title), R.string.ads_data_invalid);
            Dynamic.setVisibility(findViewById(R.id.ads_header_appbar_root), View.VISIBLE);
        } else if (getPreviewHint() == null) {
            Dynamic.setVisibility(findViewById(R.id.ads_header_appbar_root), View.GONE);
        }
    }

    @Override
    public void onPreviewClick() {
        if (DynamicThemeUtils.isValidTheme(getPreview().getInfo())) {
            DynamicLinkUtils.viewUrl(DynamicPreviewActivity.this,
                    getPreview().getInfo());
        } else {
            DynamicLinkUtils.share(DynamicPreviewActivity.this, (String) getTitle(),
                    getPreview().getInfo(), null, getProductFlavor());
        }
    }

    @Override
    public @Nullable String getShareTitle() {
        return (String) (getSubtitle() != null ? getSubtitle() : getTitle());
    }

    @Override
    public @NonNull String getFileName(int requestCode, boolean legacy) {
        if (legacy) {
            return DynamicThemeUtils.getFileName(requestCode
                    == DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT
                    ? Theme.Key.SHARE_ALT : Theme.Key.SHARE, Theme.EXTENSION_IMAGE);
        } else {
            return requestCode == DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT
                    ? Theme.Key.SHARE_ALT : Theme.Key.SHARE;
        }
    }

    @Override
    public void onExportComplete(@Nullable Uri file) {
        Dynamic.showSnackbar(this,
                String.format(getString(R.string.ads_theme_format_saved),
                        DynamicFileUtils.getFileNameFromUri(this, file)));
    }

    @Override
    public void onExportError() {
        Dynamic.showSnackbar(this, R.string.ads_theme_export_error);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.ads_menu_preview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        setMenuItemVisible(R.id.ads_menu_preview_data, getPreview().getData() != null
                && DynamicIntentUtils.isFilePicker(getContext(), Theme.MIME_IMAGE, true));
        setMenuItemVisible(R.id.ads_menu_preview_info, !TextUtils.isEmpty(getPreview().getInfo()));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ads_menu_preview_data_default) {
            saveBitmap(getPreview().getData(false), DynamicIntent.REQUEST_PREVIEW_LOCATION);
        } if (item.getItemId() == R.id.ads_menu_preview_data_custom) {
            DynamicSizeDialog.newInstance().setValue(getPreviewSize()).setValueListener(
                    new DynamicValueListener<Integer>() {
                        @Override
                        public void onValueSelected(@Nullable String tag,
                                @NonNull Integer value, @Nullable String unit) {
                            saveBitmapAlt(value, DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT);
                        }
                    }).setBuilder(new DynamicDialog.Builder(
                            getContext()).setTitle(R.string.ads_save))
                    .showDialog(this);
        } else if (item.getItemId() == R.id.ads_menu_preview_info_copy) {
            DynamicTheme.getInstance().copyThemeString(this, getPreview().getInfo());
        } else if (item.getItemId() == R.id.ads_menu_preview_info_share) {
            DynamicLinkUtils.share(this, getShareTitle(), getPreview().getInfo());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Preview.KEY, getPreview());
    }

    /**
     * Set progress for the preview tasks.
     *
     * @param requestCode The request code to be used.
     * @param visible {@code true} to make the progress visible.
     */
    protected void setProgress(int requestCode, boolean visible) {
        if (mProgressDialog != null && mProgressDialog.isAdded()) {
            mProgressDialog.dismiss();
        }

        if (visible) {
            switch (requestCode) {
                case DynamicIntent.REQUEST_PREVIEW_LOCATION:
                case DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT:
                    setAppBarProgressVisible(true);

                    mProgressDialog = DynamicProgressDialog.newInstance()
                            .setName(getString(R.string.ads_file))
                            .setBuilder(new DynamicDialog.Builder(getContext())
                                    .setTitle(getString(R.string.ads_save)));
                    mProgressDialog.showDialog(this);
                    break;
            }
        } else {
            setAppBarProgressVisible(false);
            mProgressDialog = null;
        }
    }

    /**
     * Try to save the supplied bitmap URI to the device storage.
     *
     * @param bitmapUri The bitmap URI to be saved.
     * @param requestCode The file request code to be used.
     */
    public void saveBitmap(@Nullable Uri bitmapUri, int requestCode) {
        final Uri file;
        if ((file = DynamicPickerUtils.saveToFile(this, this,
                bitmapUri, Theme.MIME_IMAGE, requestCode, true,
                getFileName(requestCode, true))) != null) {
            savePreview(requestCode, bitmapUri, file);
        } else if (!DynamicIntentUtils.isFilePicker(this, Theme.MIME_IMAGE)) {
            onExportError();
        }
    }

    /**
     * Save the custom preview bitmap according to the supplied size.
     *
     * @param size The size in pixels to be used.
     * @param requestCode The file request code to be used.
     */
    private void saveBitmapAlt(final int size, final int requestCode) {
        new ViewModelProvider(this).get(DynamicTaskViewModel.class).execute(
                new DynamicTask<Void, Void, Uri>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        setProgress(requestCode, true);
                    }

                    @Override
                    protected @Nullable Uri doInBackground(@Nullable Void params) {
                        try {
                            return DynamicFileUtils.getBitmapUri(getContext(),
                                    DynamicBitmapUtils.resizeBitmap(DynamicBitmapUtils.getBitmap(
                                            getContext(), getPreview().getData(false)),
                                            size, size), getFileName(requestCode, false));
                        } catch (Exception e) {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(@Nullable DynamicResult<Uri> result) {
                        super.onPostExecute(result);

                        setProgress(requestCode, false);

                        if (result != null) {
                            getPreview().setDataCustom(result.getData());
                            saveBitmap(getPreview().getData(), requestCode);
                        } else {
                            onExportError();
                        }
                    }
                });
    }

    /**
     * Save preview according to the supplied parameters.
     *
     * @param bitmapUri The bitmap URI to be saved.
     * @param requestCode The request code to be used.
     * @param file The file URI to be used.
     */
    private void savePreview(int requestCode, @Nullable Uri bitmapUri, @Nullable Uri file) {
        new ViewModelProvider(this).get(DynamicTaskViewModel.class).execute(
                new FileWriteTask(getContext(), bitmapUri, file) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        setProgress(requestCode, true);
                    }

                    @Override
                    protected void onPostExecute(@Nullable DynamicResult<Boolean> result) {
                        super.onPostExecute(result);

                        setProgress(requestCode, false);

                        if (getBooleanResult(result)) {
                            onExportComplete(file);
                        } else {
                            onExportError();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(final int requestCode,
            int resultCode, final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        DynamicTheme.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                switch (requestCode) {
                    case DynamicIntent.REQUEST_PREVIEW_LOCATION:
                    case DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT:
                        savePreview(requestCode, getPreview().getData(requestCode
                                == DynamicIntent.REQUEST_PREVIEW_LOCATION_ALT), data.getData());
                        break;

                }
            }
        });
    }
}
