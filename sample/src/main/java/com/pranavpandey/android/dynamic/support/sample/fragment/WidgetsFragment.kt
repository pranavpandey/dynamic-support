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
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils

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
        // Inflate layout resource according to the activity theme style version.
        return inflater.inflate(DynamicResourceUtils.getResourceId(
            dynamicActivity, R.attr.layout_widgets), container, false)
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

    override fun setHasOptionsMenu(): Boolean {
        return true
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateMenu(menu, inflater)

        // Inflate menu for this fragment.
        inflater.inflate(R.menu.menu_widgets, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                // Expand search view on search menu selected.
                dynamicActivity.expandSearchView(true)
            }
        }

        return super.onMenuItemSelected(item)
    }

    override fun isSearchViewListenerListener(): Boolean {
        return true
    }

    override fun getTextWatcher(): TextWatcher? {
        // Return text watcher for app bar search view.
        return this
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
