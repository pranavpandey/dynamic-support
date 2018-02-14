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

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox;
import com.pranavpandey.android.dynamic.support.widget.DynamicDrawerLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicEditText;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.DynamicListView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNestedScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicProgressBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicRadioButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView;
import com.pranavpandey.android.dynamic.support.widget.DynamicScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicSeekBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicSpinner;
import com.pranavpandey.android.dynamic.support.widget.DynamicSwipeRefreshLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicSwitchCompat;
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextInputEditText;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;
import com.pranavpandey.android.dynamic.support.widget.DynamicToolbar;
import com.pranavpandey.android.dynamic.support.widget.DynamicViewPager;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * A layout inflater factory2 to inflate replace original views with
 * the dynamic support views during inflation.
 */
@RestrictTo(LIBRARY_GROUP)
final class DynamicLayoutInflater implements LayoutInflater.Factory2 {

    /**
     * Tag to ignore the view during inflation.
     */
    private static final String ADS_TAG_IGNORE = ":ads_ignore";

    @Override
    public View onCreateView(String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    @Override
    @SuppressLint("RestrictedApi")
    public View onCreateView(@Nullable View parent, final String name,
                             @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "android.support.v4.widget.DrawerLayout":
                view = new DynamicDrawerLayout(context, attrs);
                break;
            case "android.support.v4.widget.SwipeRefreshLayout":
                view = new DynamicSwipeRefreshLayout(context, attrs);
                break;
            case "Toolbar":
            case "android.support.v7.widget.Toolbar":
                view = new DynamicToolbar(context, attrs);
                break;
            case "ListMenuItemView":
            case "android.support.v7.view.menu.ListMenuItemView":
                try {
                    final View menuItemView = LayoutInflater.from(context)
                            .createView(name, null, attrs);
                    final Drawable drawable = DynamicResourceUtils.getDrawable(
                            context, R.drawable.ads_background);
                    DynamicDrawableUtils.colorizeDrawable(drawable,
                            DynamicTheme.getInstance().getBackgroundColor());

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            final @ColorInt int backgroundTintColor =
                                    DynamicTheme.getInstance().getTintBackgroundColor();
                            final Drawable icon = ((ListMenuItemView) menuItemView)
                                    .getItemData().getIcon();

                            try {
                                if (icon != null) {
                                    ((ListMenuItemView) menuItemView).setIcon(
                                            DynamicDrawableUtils.colorizeDrawable(
                                                    icon, backgroundTintColor));
                                }

                                ViewCompat.setBackground(
                                        ((View) menuItemView.getParent()), drawable);
                                DynamicScrollUtils.setEdgeEffectColor(
                                        (ListView) menuItemView.getParent(), backgroundTintColor);
                            } catch (Exception ignored) {
                            }
                        }
                    });

                    view = menuItemView;
                } catch (Exception ignored) {
                }
                break;
            case "CardView":
            case "android.support.v7.widget.CardView":
                view = new DynamicCardView(context, attrs);
                break;
            case "ImageView":
            case "android.support.v7.widget.AppCompatImageView":
                view = new DynamicImageView(context, attrs);
            break;
            case "TextView":
            case "android.support.v7.widget.AppCompatTextView":
                view = new DynamicTextView(context, attrs);
                break;
            case "CheckBox":
            case "android.support.v7.widget.AppCompatCheckBox":
                view = new DynamicCheckBox(context, attrs);
                break;
            case "RadioButton":
            case "android.support.v7.widget.AppCompatRadioButton":
                view = new DynamicRadioButton(context, attrs);
                break;
            case "EditText":
            case "android.support.v7.widget.AppCompatEditText":
                view = new DynamicEditText(context, attrs);
                break;
            case "android.support.v7.widget.SwitchCompat":
                view = new DynamicSwitchCompat(context, attrs);
                break;
            case "SeekBar":
            case "android.support.v7.widget.AppCompatSeekBar":
                view = new DynamicSeekBar(context, attrs);
                break;
            case "ProgressBar":
                view = new DynamicProgressBar(context, attrs);
                break;
            case "android.support.v7.widget.RecyclerView":
                view = new DynamicRecyclerView(context, attrs);
                break;
            case "android.support.v4.widget.NestedScrollView":
                view = new DynamicNestedScrollView(context, attrs);
                break;
            case "ListView":
                view = new DynamicListView(context, attrs);
                break;
            case "ScrollView":
                view = new DynamicScrollView(context, attrs);
                break;
            case "android.support.v4.view.ViewPager":
                view = new DynamicViewPager(context, attrs);
                break;
            case "Spinner":
            case "android.support.v7.widget.AppCompatSpinner":
                view = new DynamicSpinner(context, attrs);
                break;
            case "android.support.design.widget.TextInputLayout":
                view = new DynamicTextInputLayout(context, attrs);
                break;
            case "android.support.design.widget.TextInputEditText":
                view = new DynamicTextInputEditText(context, attrs);
                break;
            case "android.support.design.widget.TabLayout":
                view = new DynamicTabLayout(context, attrs);
                break;
            case "android.support.design.widget.NavigationView":
                view = new DynamicNavigationView(context, attrs);
                break;
            case "android.support.design.widget.BottomNavigationView":
                view = new DynamicBottomNavigationView(context, attrs);
                break;
            case "android.support.design.widget.FloatingActionButton":
                view = new DynamicFloatingActionButton(context, attrs);
                break;
        }

        if (view != null && view.getTag() != null && view.getTag().equals(ADS_TAG_IGNORE)) {
            view = null;
        }

        return view;
    }
}
