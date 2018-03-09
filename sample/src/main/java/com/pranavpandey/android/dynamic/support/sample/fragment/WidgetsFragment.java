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

package com.pranavpandey.android.dynamic.support.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity;
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem;
import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;

import java.util.ArrayList;

/**
 * Widgets fragment to show various widgets and their states
 * by using {@link DynamicFragment}.
 */
public class WidgetsFragment extends DynamicFragment implements
        DynamicSearchListener, TextWatcher {

    /**
     * First spinner o attach an adapter.
     */
    private Spinner mSpinnerOne;

    /**
     * Second spinner to attach an adapter.
     */
    private Spinner mSpinnerTwo;

    /**
     * Third spinner to attach an adapter.
     */
    private Spinner mSpinnerThree;

    /**
     * @return The new instance of {@link WidgetsFragment}.
     */
    public static WidgetsFragment newInstance() {
        return new WidgetsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Enable app bar options menu.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_widgets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSpinnerOne = view.findViewById(R.id.spinner_one);
        mSpinnerTwo = view.findViewById(R.id.spinner_two);
        mSpinnerThree = view.findViewById(R.id.spinner_three);

        // Disable the seek bar.
        view.findViewById(R.id.seek_bar_disabled).setEnabled(false);

        // Register search view listener for this fragment.
        getDynamicActivity().setSearchViewListener(this);

        // Array list for spinner items with icon.
        ArrayList<DynamicSpinnerItem> spinnerItemsIcon = new ArrayList<>();
        spinnerItemsIcon.add(new DynamicSpinnerItem(DynamicResourceUtils.getDrawable(getContext(),
                R.drawable.ads_ic_extension), "Spinner one"));
        spinnerItemsIcon.add(new DynamicSpinnerItem(DynamicResourceUtils.getDrawable(getContext(),
                R.drawable.ads_ic_android), "Spinner two"));
        spinnerItemsIcon.add(new DynamicSpinnerItem(DynamicResourceUtils.getDrawable(getContext(),
                R.drawable.ads_ic_check), "Spinner three"));
        spinnerItemsIcon.add(new DynamicSpinnerItem(DynamicResourceUtils.getDrawable(getContext(),
                R.drawable.ads_ic_close), "Spinner four"));

        // Set dynamic spinner image adapter with text and image view ids.
        mSpinnerOne.setAdapter(new DynamicSpinnerImageAdapter(getContext(),
                R.layout.ads_layout_spinner_item, R.id.ads_spinner_item_icon,
                R.id.ads_spinner_item_text, spinnerItemsIcon));

        // Set dynamic spinner image adapter with text view id. Setting a
        // default image view id to disable icons.
        mSpinnerTwo.setAdapter(new DynamicSpinnerImageAdapter(getContext(),
                R.layout.ads_layout_spinner_item,
                DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID,
                R.id.ads_spinner_item_text, spinnerItemsIcon));

        // Array list for spinner items without icon.
        ArrayList<DynamicSpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new DynamicSpinnerItem(null, "Spinner one"));
        spinnerItems.add(new DynamicSpinnerItem(null, "Spinner two"));
        spinnerItems.add(new DynamicSpinnerItem(null, "Spinner three"));
        spinnerItems.add(new DynamicSpinnerItem(null, "Spinner four"));

        // Set dynamic spinner image adapter with text and image view ids.
        // Now, passing an array without icons to dide the image view.
        mSpinnerThree.setAdapter(new DynamicSpinnerImageAdapter(getContext(),
                R.layout.ads_layout_spinner_item,
                R.id.ads_spinner_item_icon,
                R.id.ads_spinner_item_text, spinnerItems));

        mSpinnerTwo.setSelection(1);
        mSpinnerThree.setSelection(2);
    }

    @Override
    public void onPrepareOptionsMenu(@Nullable Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Try to force the menu icons.
        DynamicMenuUtils.forceMenuIcons(menu);
    }

    @Override
    public void onCreateOptionsMenu(@Nullable Menu menu, @Nullable MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate menu for this fragment.
        inflater.inflate(R.menu.menu_widgets, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case R.id.menu_search:
                    // Expand search view on search menu selected.
                    getDynamicActivity().expandSearchView(true);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchViewExpanded() {
        // Animate drawer toggle icon hamburger to back.
        if (getDynamicActivity() instanceof DynamicDrawerActivity) {
            if (((DynamicDrawerActivity) getDynamicActivity()).isPersistentDrawer()) {
                getDynamicActivity().setNavigationClickListener(
                        R.drawable.ads_ic_back, null);
            }

            ((DynamicDrawerActivity) getDynamicActivity()).animateDrawerToggle(0, 1);
        }

        // Show menu on search view expanded.
        setMenuVisibility(false);

        // Add on text changed listener for the search view.
        getDynamicActivity().getSearchViewEditText().addTextChangedListener(this);
    }

    @Override
    public void onSearchViewCollapsed() {
        // Show menu on search view collapsed.
        setMenuVisibility(true);

        // Remove on text changed listener for the search view.
        getDynamicActivity().getSearchViewEditText().removeTextChangedListener(this);

        // Animate drawer toggle icon back to hamburger.
        if (getDynamicActivity() instanceof DynamicDrawerActivity) {
            ((DynamicDrawerActivity) getDynamicActivity()).animateDrawerToggle(1, 0);

            if (((DynamicDrawerActivity) getDynamicActivity()).isPersistentDrawer()) {
                getDynamicActivity().setNavigationClickListener(
                        R.drawable.ads_ic_extension, null);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // TODO: Do something before search view text changed.
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // TODO: Do something on search view text changed.
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO: Do something after search view text changed.
    }
}
