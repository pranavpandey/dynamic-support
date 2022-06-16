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

package com.pranavpandey.android.dynamic.support.theme.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicAppBarLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicBackgroundView;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomAppBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox;
import com.pranavpandey.android.dynamic.support.widget.DynamicCollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicCoordinatorLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicDrawerLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicEditText;
import com.pranavpandey.android.dynamic.support.widget.DynamicExtendedFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicForegroundLinearLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicGridView;
import com.pranavpandey.android.dynamic.support.widget.DynamicHorizontalScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayoutCompat;
import com.pranavpandey.android.dynamic.support.widget.DynamicListView;
import com.pranavpandey.android.dynamic.support.widget.DynamicMaterialCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationMenuItemView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNestedScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicProgressBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicRadioButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicRatingBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView;
import com.pranavpandey.android.dynamic.support.widget.DynamicRelativeLayout;
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

/**
 * A {@link LayoutInflater.Factory2} to replace original views with the dynamic support views
 * during inflation.
 */
public class DynamicLayoutInflater implements LayoutInflater.Factory2 {

    /**
     * Tag to ignore the view during inflation.
     */
    protected static final String ADS_TAG_IGNORE = ":ads_ignore";

    @Override
    public @Nullable View onCreateView(@NonNull String name,
            @NonNull Context context, @NonNull AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    @Override
    public @Nullable View onCreateView(@Nullable View parent, final @NonNull String name,
            final @NonNull Context context, final @NonNull AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "View":
                view = new DynamicBackgroundView(context, attrs);
                break;
            case "ListMenuItemView":
            case "com.android.internal.view.menu.ListMenuItemView":
            case "android.support.v7.view.menu.ListMenuItemView":
            case "androidx.appcompat.view.menu.ListMenuItemView":
                try {
                    view = LayoutInflater.from(context).createView(name, null, attrs);
                    view.post(new MenuInflaterRunnable(view, attrs));
                } catch (Exception ignored) {
                }
                break;
            case "Toolbar":
            case "android.support.v7.widget.Toolbar":
            case "androidx.appcompat.widget.Toolbar":
            case "com.google.android.material.appbar.MaterialToolbar":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicToolbar":
                view = new DynamicToolbar(context, attrs);
                break;
            case "Button":
                view = new Button(context, attrs);
                ((Button) view).setTextColor(DynamicResourceUtils.getColorStateList(
                        DynamicTheme.getInstance().get().getTintBackgroundColor()));
                break;
            case "android.support.v7.widget.AppCompatButton":
            case "androidx.appcompat.widget.AppCompatButton":
            case "com.google.android.material.button.MaterialButton":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicButton":
                view = new DynamicButton(context, attrs);
                break;
            case "ImageButton":
            case "android.support.v7.widget.AppCompatImageButton":
            case "androidx.appcompat.widget.AppCompatImageButton":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicImageButton":
                view = new DynamicImageButton(context, attrs);
                break;
            case "ImageView":
            case "android.support.v7.widget.AppCompatImageView":
            case "androidx.appcompat.widget.AppCompatImageView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicImageView":
                view = new DynamicImageView(context, attrs);
            break;
            case "TextView":
            case "android.support.v7.widget.AppCompatTextView":
            case "androidx.appcompat.widget.AppCompatTextView":
            case "com.google.android.material.textview.MaterialTextView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicTextView":
                view = new DynamicTextView(context, attrs);
                break;
            case "CheckBox":
            case "android.support.v7.widget.AppCompatCheckBox":
            case "androidx.appcompat.widget.AppCompatCheckBox":
            case "com.google.android.material.checkbox.MaterialCheckBox":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox":
                view = new DynamicCheckBox(context, attrs);
                break;
            case "RadioButton":
            case "android.support.v7.widget.AppCompatRadioButton":
            case "androidx.appcompat.widget.AppCompatRadioButton":
            case "com.google.android.material.radiobutton.MaterialRadioButton":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicRadioButton":
                view = new DynamicRadioButton(context, attrs);
                break;
            case "EditText":
            case "android.support.v7.widget.AppCompatEditText":
            case "androidx.appcompat.widget.AppCompatEditText":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicEditText":
                view = new DynamicEditText(context, attrs);
                break;
            case "android.support.v7.widget.SwitchCompat":
            case "androidx.appcompat.widget.SwitchCompat":
            case "com.google.android.material.switchmaterial.SwitchMaterial":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicSwitchCompat":
                view = new DynamicSwitchCompat(context, attrs);
                break;
            case "SeekBar":
            case "android.support.v7.widget.AppCompatSeekBar":
            case "androidx.appcompat.widget.AppCompatSeekBar":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicSeekBar":
                view = new DynamicSeekBar(context, attrs);
                break;
            case "RatingBar":
            case "android.support.v7.widget.AppCompatRatingBar":
            case "androidx.appcompat.widget.AppCompatRatingBar":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicRatingBar":
                view = new DynamicRatingBar(context, attrs);
                break;
            case "Spinner":
            case "android.support.v7.widget.AppCompatSpinner":
            case "androidx.appcompat.widget.AppCompatSpinner":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicSpinner":
                view = new DynamicSpinner(context, attrs);
                break;
            case "ProgressBar":
            case "android.support.v4.widget.ContentLoadingProgressBar":
            case "androidx.core.widget.ContentLoadingProgressBar":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicProgressBar":
                view = new DynamicProgressBar(context, attrs);
                break;
            case "android.support.v4.widget.SwipeRefreshLayout":
            case "androidx.SwipeRefreshLayout.widget.SwipeRefreshLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicSwipeRefreshLayout":
                view = new DynamicSwipeRefreshLayout(context, attrs);
                break;
            case "FrameLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout":
                view = new DynamicFrameLayout(context, attrs);
                break;
            case "LinearLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout":
                view = new DynamicLinearLayout(context, attrs);
                break;
            case "android.support.v7.widget.LinearLayoutCompat":
            case "androidx.appcompat.widget.LinearLayoutCompat":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayoutCompat":
                view = new DynamicLinearLayoutCompat(context, attrs);
                break;
            case "android.support.design.internal.ForegroundLinearLayout":
            case "com.google.android.material.internal.ForegroundLinearLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicForegroundLinearLayout":
                view = new DynamicForegroundLinearLayout(context, attrs);
                break;
            case "RelativeLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicRelativeLayout":
                view = new DynamicRelativeLayout(context, attrs);
                break;
            case "ScrollView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicScrollView":
                view = new DynamicScrollView(context, attrs);
                break;
            case "HorizontalScrollView":
            case "com.pranavpandey.android.dynamic.support.widget.HorizontalScrollView":
                view = new DynamicHorizontalScrollView(context, attrs);
                break;
            case "ListView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicListView":
                view = new DynamicListView(context, attrs);
                break;
            case "GridView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicGridView":
                view = new DynamicGridView(context, attrs);
                break;
            case "android.support.v7.widget.RecyclerView":
            case "androidx.recyclerview.widget.RecyclerView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView":
                view = new DynamicRecyclerView(context, attrs);
                break;
            case "android.support.v4.widget.NestedScrollView":
            case "androidx.core.widget.NestedScrollView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicNestedScrollView":
                view = new DynamicNestedScrollView(context, attrs);
                break;
            case "android.support.v4.view.ViewPager":
            case "androidx.viewpager.widget.ViewPager":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicViewPager":
                view = new DynamicViewPager(context, attrs);
                break;
            case "android.support.design.widget.CoordinatorLayout":
            case "androidx.coordinatorlayout.widget.CoordinatorLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicCoordinatorLayout":
                view = new DynamicCoordinatorLayout(context, attrs);
                break;
            case "android.support.design.widget.AppBarLayout":
            case "com.google.android.material.bottomappbar.AppBarLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicAppBarLayout":
                view = new DynamicAppBarLayout(context, attrs);
                break;
            case "android.support.design.bottomappbar.BottomAppBar":
            case "com.google.android.material.bottomappbar.BottomAppBar":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicBottomAppBar":
                view = new DynamicBottomAppBar(context, attrs);
                break;
            case "android.support.design.widget.CollapsingToolbarLayout":
            case "com.google.android.material.bottomappbar.CollapsingToolbarLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicCollapsingToolbarLayout":
                view = new DynamicCollapsingToolbarLayout(context, attrs);
                break;
            case "android.support.v4.widget.DrawerLayout":
            case "androidx.DrawerLayout.widget.DrawerLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicDrawerLayout":
                view = new DynamicDrawerLayout(context, attrs);
                break;
            case "android.support.design.widget.NavigationView":
            case "com.google.android.material.navigation.NavigationView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView":
                view = new DynamicNavigationView(context, attrs);
                break;
            case "android.support.design.internal.NavigationMenuItemView":
            case "com.google.android.material.internal.NavigationMenuItemView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicNavigationMenuItemView":
                view = new DynamicNavigationMenuItemView(context, attrs);
                break;
            case "android.support.design.widget.BottomNavigationView":
            case "com.google.android.material.bottomnavigation.BottomNavigationView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView":
                view = new DynamicBottomNavigationView(context, attrs);
                break;
            case "android.support.design.widget.TabLayout":
            case "com.google.android.material.tabs.TabLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout":
                view = new DynamicTabLayout(context, attrs);
                break;
            case "CardView":
            case "android.support.v7.widget.CardView":
            case "androidx.cardview.widget.CardView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicCardView":
                view = new DynamicCardView(context, attrs);
                break;
            case "com.google.android.material.card.MaterialCardView":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicMaterialCardView":
                view = new DynamicMaterialCardView(context, attrs);
                break;
            case "android.support.design.widget.TextInputLayout":
            case "com.google.android.material.textfield.TextInputLayout":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicTextInputLayout":
                view = new DynamicTextInputLayout(context, attrs);
                break;
            case "android.support.design.widget.TextInputEditText":
            case "com.google.android.material.textfield.TextInputEditText":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicTextInputEditText":
                view = new DynamicTextInputEditText(context, attrs);
                break;
            case "android.support.design.widget.FloatingActionButton":
            case "com.google.android.material.floatingactionbutton.FloatingActionButton":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton":
                view = new DynamicFloatingActionButton(context, attrs);
                break;
            case "com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton":
            case "com.pranavpandey.android.dynamic.support.widget.DynamicExtendedFloatingActionButton":
                view = new DynamicExtendedFloatingActionButton(context, attrs);
                break;
        }

        return onCustomiseView(view, context, attrs);
    }

    /**
     * Customise the supplied view created by this layout inflater.
     *
     * @param view The view to be customised.
     * @param context The context the view is being created in.
     * @param attrs Inflation attributes as specified in XML file.
     *
     * @return The view after applying the customisations.
     */
    protected @Nullable View onCustomiseView(@Nullable View view,
            @NonNull Context context, @NonNull AttributeSet attrs) {
        if (view != null) {
            if (view instanceof DynamicCardView && ((DynamicCardView) view).isStrokeRequired()) {
                view = new DynamicMaterialCardView(context, attrs);
            }

            if (ADS_TAG_IGNORE.equals(view.getTag())) {
                view = null;
            }
        }

        return view;
    }
}
