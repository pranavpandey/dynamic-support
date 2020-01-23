/*
 * Copyright 2019 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.sample.activity

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.viewpager2.widget.ViewPager2
import com.pranavpandey.android.dynamic.support.sample.R
import com.pranavpandey.android.dynamic.support.sample.controller.AppController
import com.pranavpandey.android.dynamic.support.sample.controller.Constants
import com.pranavpandey.android.dynamic.support.sample.controller.ThemeController
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.tutorial.DynamicSimpleTutorial
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial
import com.pranavpandey.android.dynamic.support.tutorial.activity.DynamicTutorialActivity
import com.pranavpandey.android.dynamic.support.tutorial.fragment.DynamicTutorialFragment
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils
import java.util.*

/**
 * Implementing a into screen by using [DynamicTutorialActivity].
 */
class TutorialActivity :
        DynamicTutorialActivity<DynamicSimpleTutorial, DynamicTutorialFragment>() {

    companion object {

        /**
         * Constant for welcome intro screen.
         */
        const val TUTORIAL_WELCOME = 0

        /**
         * Constant for finish intro screen.
         */
        const val TUTORIAL_FINISH = 1
    }

    override fun getLocale(): Locale? {
        // TODO: Not implementing multiple locales so, returning null.
        return null
    }

    @StyleRes
    override fun getThemeRes(): Int {
        // Return activity theme to be applied.
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        ThemeController.setLocalTheme()
    }

    override fun setNavigationBarTheme(): Boolean {
        // TODO: Return true to apply the navigation bar theme.
        return AppController.instance.isThemeNavigationBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add a view pager listener to perform actions according to the tutorial screen.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (viewPagerAdapter.getTutorial(position) != null) {
                    setTutorialAction(viewPagerAdapter.getTutorial(position).tutorialId)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (viewPagerAdapter.getTutorial(position) != null) {
                    setTutorialAction(viewPagerAdapter.getTutorial(position).tutorialId)
                }
            }
        })

        setSkipAction()
    }

    /**
     * TODO: Set an action button according to the tutorial id.
     */
    private fun setTutorialAction(tutorial: Int) {
        when (tutorial) {
            TUTORIAL_FINISH -> setGitHubAction()
            else -> setSkipAction()
        }
    }

    /**
     * TODO: Set an action button to skip the tutorial.
     */
    private fun setSkipAction() {
        setAction(getString(R.string.ads_skip)) { this@TutorialActivity.finish() }
    }

    /**
     * TODO: Set an action button to view sources on GutHub.
     */
    private fun setGitHubAction() {
        setAction(getString(R.string.ads_info_github)) {
            DynamicLinkUtils.viewUrl(this@TutorialActivity, Constants.URL_GITHUB)
        }
    }

    override fun getTutorials():
            ArrayList<DynamicTutorial<DynamicSimpleTutorial, DynamicTutorialFragment>> {
        // Initialize an array list for tutorials.
        val tutorials = ArrayList<DynamicTutorial<DynamicSimpleTutorial, DynamicTutorialFragment>>()

        // TODO: Add a simple dynamic tutorial.
        tutorials.add(DynamicSimpleTutorial(TUTORIAL_WELCOME,
                DynamicTheme.getInstance().get().primaryColor,
                getString(R.string.tutorial_welcome),
                getString(R.string.tutorial_welcome_subtitle),
                getString(R.string.tutorial_welcome_desc),
                R.drawable.ic_sample_splash, true))

        // TODO: Add another simple dynamic tutorial.
        tutorials.add(DynamicSimpleTutorial(TUTORIAL_FINISH,
                DynamicTheme.getInstance().get().accentColor,
                getString(R.string.tutorial_finish),
                getString(R.string.tutorial_finish_subtitle),
                getString(R.string.tutorial_finish_desc),
                R.drawable.ic_finish, true))

        // Return all the added tutorials.
        return tutorials
    }
}
