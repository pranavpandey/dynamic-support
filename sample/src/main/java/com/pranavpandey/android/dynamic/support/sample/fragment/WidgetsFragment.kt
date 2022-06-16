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

package com.pranavpandey.android.dynamic.support.sample.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Spinner
import com.pranavpandey.android.dynamic.support.activity.DynamicDrawerActivity
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.util.DynamicMenuUtils
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils
import java.util.*

/**
 * Widgets fragment to show various widgets and their states by using [DynamicFragment].
 */
class WidgetsFragment : DynamicFragment(), DynamicSearchListener, TextWatcher {

    /**
     * First spinner o attach an adapter.
     */
    private var mSpinnerOne: Spinner? = null

    /**
     * Second spinner to attach an adapter.
     */
    private var mSpinnerTwo: Spinner? = null

    /**
     * Third spinner to attach an adapter.
     */
    private var mSpinnerThree: Spinner? = null

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @return The new instance of [WidgetsFragment].
         */
        fun newInstance(): WidgetsFragment {
            return WidgetsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_widgets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSpinnerOne = view.findViewById(R.id.spinner_one)
        mSpinnerTwo = view.findViewById(R.id.spinner_two)
        mSpinnerThree = view.findViewById(R.id.spinner_three)

        // Disable the seek bar.
        view.findViewById<View>(R.id.seek_bar_disabled).isEnabled = false

        // Register search view listener for this fragment.
        dynamicActivity.searchViewListener = this

        // Array list for spinner items with icon.
        val spinnerItemsIcon = ArrayList<DynamicMenu>()
        spinnerItemsIcon.add(DynamicMenu(DynamicResourceUtils.getDrawable(
            requireContext(), R.drawable.ads_ic_extension), "Spinner one"))
        spinnerItemsIcon.add(DynamicMenu(DynamicResourceUtils.getDrawable(
            requireContext(), R.drawable.ads_ic_android), "Spinner two"))
        spinnerItemsIcon.add(DynamicMenu(DynamicResourceUtils.getDrawable(
            requireContext(), R.drawable.ads_ic_check), "Spinner three"))
        spinnerItemsIcon.add(DynamicMenu(DynamicResourceUtils.getDrawable(
            requireContext(), R.drawable.ads_ic_close), "Spinner four"))

        // Set dynamic spinner image adapter with text and image view ids.
        mSpinnerOne!!.adapter = DynamicSpinnerImageAdapter(requireContext(),
            R.layout.ads_layout_spinner_item, R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItemsIcon, mSpinnerOne)

        // Set dynamic spinner image adapter with text view id. Setting a
        // default image view id to disable icons.
        mSpinnerTwo!!.adapter = DynamicSpinnerImageAdapter(requireContext(),
            R.layout.ads_layout_spinner_item, DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID,
            R.id.ads_spinner_item_text, spinnerItemsIcon, mSpinnerTwo)

        // Array list for spinner items without icon.
        val spinnerItems = ArrayList<DynamicMenu>()
        spinnerItems.add(DynamicMenu(null, "Spinner one"))
        spinnerItems.add(DynamicMenu(null, "Spinner two"))
        spinnerItems.add(DynamicMenu(null, "Spinner three"))
        spinnerItems.add(DynamicMenu(null, "Spinner four"))

        // Set dynamic spinner image adapter with text and image view ids.
        // Now, passing an array without icons to hide the image view.
        mSpinnerThree!!.adapter = DynamicSpinnerImageAdapter(requireContext(),
            R.layout.ads_layout_spinner_item, R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItems, mSpinnerThree)

        mSpinnerTwo!!.setSelection(1)
        mSpinnerThree!!.setSelection(2)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        // Try to force the menu icons.
        DynamicMenuUtils.forceMenuIcons(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Inflate menu for this fragment.
        inflater.inflate(R.menu.menu_widgets, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                // Expand search view on search menu selected.
                dynamicActivity.expandSearchView(true)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        // TODO: Enable app bar options menu.
        setHasOptionsMenu(true)
    }

    override fun onSearchViewExpanded() {
        // Animate drawer toggle icon hamburger to back.
        if (dynamicActivity is DynamicDrawerActivity) {
            if ((dynamicActivity as DynamicDrawerActivity).isPersistentDrawer) {
                dynamicActivity.setNavigationClickListener(
                    R.drawable.ads_ic_back, null)
            }

            (dynamicActivity as DynamicDrawerActivity).animateDrawerToggle(0f, 1f)
        }

        // Show menu on search view expanded.
        setMenuVisibility(false)

        // Add on text changed listener for the search view.
        dynamicActivity.searchViewEditText!!.addTextChangedListener(this)
    }

    override fun onSearchViewCollapsed() {
        // Show menu on search view collapsed.
        setMenuVisibility(true)

        // Remove on text changed listener for the search view.
        dynamicActivity.searchViewEditText!!.removeTextChangedListener(this)

        // Animate drawer toggle icon back to hamburger.
        if (dynamicActivity is DynamicDrawerActivity) {
            (dynamicActivity as DynamicDrawerActivity).animateDrawerToggle(1f, 0f)

            if ((dynamicActivity as DynamicDrawerActivity).isPersistentDrawer) {
                dynamicActivity.setNavigationClickListener(
                    R.drawable.ads_ic_extension, null)
            }
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // TODO: Do something before search view text changed.
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // TODO: Do something on search view text changed.
    }

    override fun afterTextChanged(editable: Editable) {
        // TODO: Do something after search view text changed.
    }
}
