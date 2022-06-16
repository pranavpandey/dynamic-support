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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment
import com.pranavpandey.android.dynamic.support.sample.R

/**
 * App info fragment to show app links and developer info by using [DynamicFragment].
 */
class AppInfoFragment : DynamicFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_info, container, false)
    }

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @return The new instance of [AppInfoFragment].
         */
        fun newInstance(): AppInfoFragment {
            return AppInfoFragment()
        }
    }
}
