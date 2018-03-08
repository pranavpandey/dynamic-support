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

package com.pranavpandey.android.dynamic.support.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pranavpandey.android.dynamic.support.sample.R;
import com.pranavpandey.android.dynamic.support.sample.controller.Constants;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleController;
import com.pranavpandey.android.dynamic.support.sample.controller.SampleTheme;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicSimpleTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorial;
import com.pranavpandey.android.dynamic.support.tutorial.DynamicTutorialActivity;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Implementing a into screen using {@link DynamicTutorialActivity}.
 */
public class TutorialActivity extends DynamicTutorialActivity {

    /**
     * Constant for welcome intro screen.
     */
    public static final int TUTORIAL_WELCOME = 0;

    /**
     * Constant for finish intro screen.
     */
    public static final int TUTORIAL_FINISH = 1;

    @Override
    public @Nullable Locale getLocale() {
        // TODO: Not implementing multiple locales so, returning null.
        return null;
    }

    @Override
    protected @StyleRes int getThemeRes() {
        // Return activity theme to be applied.
        return SampleTheme.getAppStyle();
    }

    @Override
    protected void onCustomiseTheme() {
        // Customise activity theme after applying the base style.
        SampleTheme.setLocalTheme(getContext());
    }

    @Override
    protected boolean setNavigationBarTheme() {
        // TODO: Return true to apply the navigation bar theme.
        return SampleController.getInstance().isThemeNavigationBar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a view pager listener to perform actions according to the
        // tutorial screen.
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (getViewPagerAdapter() != null
                        && getViewPagerAdapter().getTutorial(position) != null) {
                    setTutorialAction(getViewPagerAdapter()
                            .getTutorial(position).getTutorialId());
                }
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        setSkipAction();
    }

    /**
     * TODO: Set an action button according to the tutorial id.
     */
    private void setTutorialAction(int tutorial) {
        switch (tutorial) {
            default:
                setSkipAction();
                break;
            case TUTORIAL_FINISH:
                setGitHubAction();
                break;
        }
    }

    /**
     * TODO: Set an action button to skip the tutorial.
     */
    private void setSkipAction() {
        setAction(getString(R.string.ads_skip), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialActivity.this.finish();
            }
        });
    }

    /**
     * TODO: Set an action button to view sources on GutHub.
     */
    private void setGitHubAction() {
        setAction(getString(R.string.ads_info_github), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLinkUtils.viewUrl(TutorialActivity.this, Constants.URL_GITHUB);
            }
        });
    }

    @Override
    protected ArrayList<DynamicTutorial> getTutorials() {
        // Initialize an array list for tutorials.
        ArrayList<DynamicTutorial> tutorials = new ArrayList<>();

        // TODO: Add a simple dynamic tutorial.
        tutorials.add(new DynamicSimpleTutorial(TUTORIAL_WELCOME,
                DynamicTheme.getInstance().getPrimaryColor(),
                getString(R.string.tutorial_welcome),
                getString(R.string.tutorial_welcome_subtitle),
                getString(R.string.tutorial_welcome_desc),
                R.drawable.ic_sample_splash, true).build());

        // TODO: Add another simple dynamic tutorial.
        tutorials.add(new DynamicSimpleTutorial(TUTORIAL_FINISH,
                DynamicTheme.getInstance().getAccentColor(),
                getString(R.string.tutorial_finish),
                getString(R.string.tutorial_finish_subtitle),
                getString(R.string.tutorial_finish_desc),
                R.drawable.ic_finish, true).build());

        // Return all the added tutorials.
        return tutorials;
    }
}
