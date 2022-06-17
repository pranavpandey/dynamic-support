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

package com.pranavpandey.android.dynamic.support.sample.adapter

import android.view.View
import com.pranavpandey.android.dynamic.support.model.DynamicInfo
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.sample.binder.AppInfoBinder
import com.pranavpandey.android.dynamic.support.sample.binder.AuthorInfoBinder
import com.pranavpandey.android.dynamic.support.sample.binder.TranslatorsBinder

/**
 * A binder adapter to display a list of [DynamicInfo] by using [DynamicSimpleBinderAdapter].
 */
class AppInfoAdapter : DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder<*>>() {

    var onClickListener: View.OnClickListener? = null
        set(onClickListener) {
            field = onClickListener

            notifyBinderDataSetChanged()
        }

    init {
        addDataBinders(AppInfoBinder(this),
                TranslatorsBinder(this),
                AuthorInfoBinder(this))
    }
}
