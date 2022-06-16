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

package com.pranavpandey.android.dynamic.support.dialog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicSliderPreference} to provide size picker functionality
 * via {@link DynamicSliderDialog}.
 */
public class DynamicSizeDialog extends DynamicSliderDialog implements View.OnClickListener {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicSizeDialog";

    /**
     * Initialize the new instance of this fragment.
     *
     * @return An instance of {@link DynamicSizeDialog}.
     */
    public static @NonNull DynamicSizeDialog newInstance() {
        return new DynamicSizeDialog();
    }

    @Override
    public @LayoutRes int getLayoutRes() {
        return R.layout.ads_dialog_slider_size;
    }
    
    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder, @Nullable Bundle savedInstanceState) {
        if (getIcon() == null) {
            setIcon(DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_size));
        }
        if (getTitle() == null) {
            setTitle(getString(R.string.ads_size));
        }
        if (getSummary() == null) {
            setSummary(getString(R.string.ads_picker_size_info));
        }
        if (getUnit() == null) {
            setUnit(getString(R.string.ads_unit_pixel));
        }

        setMin(getValue() - (int) (getValue() / Theme.Size.FACTOR_MIN));
        setMax(Math.max(getValue() + (getValue() * Theme.Size.FACTOR_300), Theme.Size.MAX));

        dialogBuilder.setNeutralButton(R.string.ads_default, null);

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (getDynamicDialog() == null) {
                    return;
                }

                getDynamicDialog().getButton(DialogInterface.BUTTON_NEUTRAL)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (getSliderPreference() != null) {
                                    getSliderPreference().animateSlider(getValue());
                                }
                            }
                        });
            }
        });

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState);
    }

    @Override
    protected void onCustomiseDialog(@NonNull DynamicDialog alertDialog,
            @Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCustomiseDialog(alertDialog, view, savedInstanceState);

        if (view != null) {
            Button size50Button = view.findViewById(R.id.ads_dialog_slider_size_50);
            Button size200Button = view.findViewById(R.id.ads_dialog_slider_size_200);
            Button size300Button = view.findViewById(R.id.ads_dialog_slider_size_300);
            Button sizeSmallButton = view.findViewById(R.id.ads_dialog_slider_size_small);
            Button sizeNormalButton = view.findViewById(R.id.ads_dialog_slider_size_normal);
            Button sizeLargeButton = view.findViewById(R.id.ads_dialog_slider_size_large);

            if (Theme.Size.SMALL < getMin() || Theme.Size.SMALL > getMax()) {
                sizeSmallButton.setVisibility(View.GONE);
            }
            if (Theme.Size.NORMAL < getMin() || Theme.Size.NORMAL > getMax()) {
                sizeNormalButton.setVisibility(View.GONE);
            }
            if (Theme.Size.LARGE < getMin() || Theme.Size.LARGE > getMax()) {
                sizeLargeButton.setVisibility(View.GONE);
            }

            size50Button.setOnClickListener(this);
            size200Button.setOnClickListener(this);
            size300Button.setOnClickListener(this);
            sizeSmallButton.setOnClickListener(this);
            sizeNormalButton.setOnClickListener(this);
            sizeLargeButton.setOnClickListener(this);
        }
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    @Override
    public void onClick(View view) {
        if (view == null || getSliderPreference() == null) {
            return;
        }

        int id = view.getId();
        if (id == R.id.ads_dialog_slider_size_50) {
            getSliderPreference().animateSlider(getValue() / Theme.Size.FACTOR_200);
        } else if (id == R.id.ads_dialog_slider_size_200) {
            getSliderPreference().animateSlider(getValue() * Theme.Size.FACTOR_200);
        } else if (id == R.id.ads_dialog_slider_size_300) {
            getSliderPreference().animateSlider(getValue() * Theme.Size.FACTOR_300);
        } else if (id == R.id.ads_dialog_slider_size_small) {
            getSliderPreference().animateSlider(Theme.Size.SMALL);
        } else if (id == R.id.ads_dialog_slider_size_normal) {
            getSliderPreference().animateSlider(Theme.Size.NORMAL);
        } else if (id == R.id.ads_dialog_slider_size_large) {
            getSliderPreference().animateSlider(Theme.Size.LARGE);
        }
    }
}
