/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity;
import com.pranavpandey.android.dynamic.support.listener.DynamicSearchListener;
import com.pranavpandey.android.dynamic.support.listener.DynamicSnackbar;
import com.pranavpandey.android.dynamic.support.model.DynamicItem;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorView;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.inflater.DynamicLayoutInflater;
import com.pranavpandey.android.dynamic.support.tutorial.Tutorial;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView;
import com.pranavpandey.android.dynamic.support.view.DynamicItemView;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicMaterialCardView;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicLinkWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicSurfaceWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTextWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * Helper class to manipulate {@link DynamicActivity} and inflated views at runtime according
 * to their implemented types.
 *
 * <p>These methods are provided to avoid any exceptions when views are replaced by the
 * {@link DynamicLayoutInflater} or not using the {@link DynamicActivity}.
 * So, this is the recommended way to modify the activity ot any of the following view types
 * at runtime.
 *
 * @see DynamicWidget
 * @see DynamicBackgroundWidget
 * @see com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget
 * @see DynamicLinkWidget
 * @see DynamicScrollableWidget
 * @see DynamicStateWidget
 * @see DynamicStateSelectedWidget
 * @see DynamicTextWidget
 * @see DynamicColorView
 * @see DynamicItem
 */
public class Dynamic {

    /**
     * Sets the color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setColorType(int)
     * @see DynamicItem#setColorType(int)
     */
    public static <T> void setColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setColorType(colorType);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setColorType(colorType);
        }
    }

    /**
     * Sets the background color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicBackgroundWidget#setBackgroundColorType(int)
     */
    public static <T> void setBackgroundColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) dynamic).setBackgroundColorType(colorType);
        }
    }

    /**
     * Sets the contrast with color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setContrastWithColorType(int)
     * @see DynamicItem#setContrastWithColorType(int)
     */
    public static <T> void setContrastWithColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setContrastWithColorType(colorType);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setContrastWithColorType(colorType);
        }
    }

    /**
     * Sets the text color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTextWidget#setTextColorType(int)
     */
    public static <T> void setTextColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicTextWidget) {
            ((DynamicTextWidget) dynamic).setTextColorType(colorType);
        }
    }

    /**
     * Sets the link color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicLinkWidget#setLinkColorType(int)
     */
    public static <T> void setLinkColorType(@Nullable T dynamic, @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) dynamic).setLinkColorType(colorType);
        }
    }

    /**
     * Sets the normal state color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateWidget#setStateNormalColorType(int)
     */
    public static <T> void setStateNormalColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicStateWidget) {
            ((DynamicStateWidget) dynamic).setStateNormalColorType(colorType);
        }
    }

    /**
     * Sets the selected state color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static <T> void setStateSelectedColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) dynamic).setStateSelectedColorType(colorType);
        }
    }

    /**
     * Sets the scroll bar color type for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollableWidget#setScrollBarColorType(int)
     */
    public static <T> void setScrollBarColorType(@Nullable T dynamic,
            @Theme.ColorType int colorType) {
        if (dynamic instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) dynamic).setScrollBarColorType(colorType);
        }
    }

    /**
     * Returns the color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param defaultColor The default color to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#getColor()
     * @see DynamicColorView#setColor(int)
     */
    public static @ColorInt <T> int getColor(@Nullable T dynamic, @ColorInt int defaultColor) {
        if (dynamic instanceof DynamicWidget) {
            return ((DynamicWidget) dynamic).getColor();
        } else if (dynamic instanceof DynamicColorView) {
            return ((DynamicColorView) dynamic).getColor();
        }

        return defaultColor;
    }

    /**
     * Sets the color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setColor(int)
     * @see DynamicColorView#setColor(int)
     * @see DynamicItem#setColor(int)
     */
    public static <T> void setColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setColor(color);
        } else if (dynamic instanceof DynamicColorView) {
            ((DynamicColorView) dynamic).setColor(color);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setColor(color);
        }
    }

    /**
     * Sets the background color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicBackgroundWidget#setBackgroundColor(int)
     */
    public static <T> void setBackgroundColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) dynamic).setBackgroundColor(color);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setBackgroundColor(color);
        }
    }

    /**
     * Sets the stroke color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The stroke color to be set.
     * @param <T> The type of the dynamic object.
     */
    public static <T> void setStrokeColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof MaterialCardView) {
            ((MaterialCardView) dynamic).setStrokeColor(color);
        }
    }

    /**
     * Returns the contrast with color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param defaultColor The default color to be used.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#getContrastWithColor()
     */
    public static @ColorInt <T> int getContrastWithColor(
            @Nullable T dynamic, @ColorInt int defaultColor) {
        if (dynamic instanceof DynamicWidget) {
            return ((DynamicWidget) dynamic).getContrastWithColor();
        }

        return defaultColor;
    }

    /**
     * Sets the contrast with color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setContrastWithColor(int)
     * @see DynamicItem#setContrastWithColor(int)
     */
    public static <T> void setContrastWithColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setContrastWithColor(color);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setContrastWithColor(color);
        }
    }

    /**
     * Sets the text color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTextWidget#setTextColor(int)
     */
    public static <T> void setTextColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicTextWidget) {
            ((DynamicTextWidget) dynamic).setTextColor(color);
        }
    }

    /**
     * Sets the link color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicLinkWidget#setLinkColor(int)
     */
    public static <T> void setLinkColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) dynamic).setLinkColor(color);
        }
    }

    /**
     * Sets the normal state color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateWidget#setStateNormalColor(int)
     */
    public static <T> void setStateNormalColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicStateWidget) {
            ((DynamicStateWidget) dynamic).setStateNormalColor(color);
        }
    }

    /**
     * Sets the selected state color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static <T> void setStateSelectedColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) dynamic).setStateSelectedColor(color);
        }
    }

    /**
     * Sets the scroll bar color for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollableWidget#setScrollBarColor(int)
     */
    public static <T> void setScrollBarColor(@Nullable T dynamic, @ColorInt int color) {
        if (dynamic instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) dynamic).setScrollBarColor(color);
        }
    }

    /**
     * Checks whether the background aware functionality is enabled for the supplied value.
     *
     * @param backgroundAware The value to be checked.
     *
     * @return {@code true} if the supplied value changes color according to
     *         the background.
     *
     * @see DynamicTheme#resolveBackgroundAware(int)
     */
    public static boolean isBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        return DynamicTheme.getInstance().resolveBackgroundAware(backgroundAware)
                != Theme.BackgroundAware.DISABLE;
    }

    /**
     * Checks whether the background aware functionality is enabled for the supplied
     * dynamic object.
     *
     * @param dynamic The dynamic object to be checked.
     * @param <T> The type of the dynamic object.
     *
     * @return {@code true} if the supplied dynamic object changes color according to
     *         the background.
     *
     * @see #isBackgroundAware(int)
     */
    public static <T> boolean isBackgroundAware(@Nullable T dynamic) {
        if (dynamic instanceof DynamicWidget) {
            return isBackgroundAware(((DynamicWidget) dynamic).getBackgroundAware());
        } else if (dynamic instanceof DynamicItem) {
            return isBackgroundAware(((DynamicItem) dynamic).getBackgroundAware());
        }

        return false;
    }

    /**
     * Sets the background aware for the supplied dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicWidget#setBackgroundAware(int)
     * @see DynamicItem#setBackgroundAware(int)
     */
    public static <T> void setBackgroundAware(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware) {
        if (dynamic instanceof DynamicWidget) {
            ((DynamicWidget) dynamic).setBackgroundAware(backgroundAware);
        } else if (dynamic instanceof DynamicItem) {
            ((DynamicItem) dynamic).setBackgroundAware(backgroundAware);
        }
    }

    /**
     * Sets the color type or color for the supplied dynamic object after doing 
     * the appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setColorType(Object, int)
     * @see #setColor(Object, int)
     */
    public static <T> void setColorTypeOrColor(@Nullable T dynamic,
            @Theme.ColorType int colorType, @ColorInt int color) {
        if (colorType != Theme.ColorType.NONE && colorType != Theme.ColorType.CUSTOM) {
            setColorType(dynamic, colorType);
        } else if (colorType == Theme.ColorType.CUSTOM && color != Theme.Color.UNKNOWN) {
            setColor(dynamic, color);
        }
    }

    /**
     * Sets the contrast with color type or color for the supplied dynamic object after doing
     * the appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param colorType The color type to be set.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setContrastWithColorType(Object, int)
     * @see #setContrastWithColor(Object, int)
     */
    public static <T> void setContrastWithColorTypeOrColor(@Nullable T dynamic,
            @Theme.ColorType int colorType, @ColorInt int color) {
        if (colorType != Theme.ColorType.NONE && colorType != Theme.ColorType.CUSTOM) {
            setContrastWithColorType(dynamic, colorType);
        } else if (colorType == Theme.ColorType.CUSTOM && color != Theme.Color.UNKNOWN) {
            setContrastWithColor(dynamic, color);
        }
    }

    /**
     * Sets the background aware for the supplied dynamic object after doing appropriate checks.
     *
     * @param dynamic The dynamic object to be used.
     * @param backgroundAware The background aware option to be set.
     * @param <T> The type of the dynamic object.
     * 
     * @see #setBackgroundAware(Object, int) 
     */
    public static <T> void setBackgroundAwareSafe(@Nullable T dynamic,
            @Theme.BackgroundAware int backgroundAware) {
        if (backgroundAware != Theme.BackgroundAware.UNKNOWN) {
            setBackgroundAware(dynamic, backgroundAware);
        }
    }

    /**
     * Tint dynamic object according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param color The color to be set.
     * @param contrastWithColor The contrast with color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #setColor(Object, int)
     * @see #setContrastWithColor(Object, int)
     * @see #setScrollBarColor(Object, int)
     */
    public static <T> void tint(@Nullable T dynamic,
            @ColorInt int color, @ColorInt int contrastWithColor) {
        if (dynamic != null) {
            setContrastWithColor(dynamic, contrastWithColor);
            setColor(dynamic, color);

            if (dynamic instanceof DynamicScrollableWidget) {
                setScrollBarColor(dynamic, color);
            }
        }
    }

    /**
     * Tint dynamic object according to the supplied colors.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param color The color to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see #tint(Object, int, int)
     */
    public static <T> void tint(@Nullable T dynamic, @ColorInt int color) {
        tint(dynamic, color, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Tint background according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicTintUtils#setViewBackgroundTint(View, int, boolean)
     */
    public static <T> void tintBackground(@Nullable T dynamic, @ColorInt int contrastWithColor) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        if (dynamic instanceof View && (((View) dynamic).isClickable()
                || ((View) dynamic).isLongClickable())) {
            DynamicTintUtils.setViewBackgroundTint((View) dynamic,
                    contrastWithColor, true);
        }
    }

    /**
     * Tint background according to the default contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param <T> The type of the dynamic object.
     *
     * @see #tintBackground(Object, int)
     */
    public static <T> void tintBackground(@Nullable T dynamic) {
        tintBackground(dynamic, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Tint scrollable according to the supplied contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param contrastWithColor The contrast with color to be considered.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicScrollUtils#tint(Object, int)
     */
    public static <T> void tintScrollable(@Nullable T dynamic, @ColorInt int contrastWithColor) {
        if (contrastWithColor == Theme.Color.UNKNOWN) {
            return;
        }

        DynamicScrollUtils.tint(dynamic, contrastWithColor);
    }

    /**
     * Tint scrollable according to the default contrast with color.
     *
     * @param dynamic The dynamic object to be tinted.
     * @param <T> The type of the dynamic object.
     *
     * @see #tintScrollable(Object, int)
     */
    public static <T> void tintScrollable(@Nullable T dynamic) {
        tintScrollable(dynamic, DynamicTheme.getInstance().getDefaultContrastWith());
    }

    /**
     * Set on click listener for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param onClickListener The click listener to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicItemView#getItemView()
     * @see DynamicInfoView#getInfoView()
     * @see DynamicPreference#getPreferenceView()
     * @see View#setOnClickListener(View.OnClickListener)
     */
    public static <T> void setOnClickListener(@Nullable T dynamic,
            @Nullable View.OnClickListener onClickListener) {
        if (dynamic instanceof DynamicItemView) {
            ((DynamicItemView) dynamic).setOnClickListener(onClickListener);
        } else if (dynamic instanceof DynamicInfoView) {
            ((DynamicInfoView) dynamic).setOnClickListener(onClickListener);
        } else if (dynamic instanceof DynamicPreference) {
            ((DynamicPreference) dynamic).setOnPreferenceClickListener(onClickListener);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setOnClickListener(onClickListener);
        }
    }

    /**
     * Set on long click listener for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param onLongClickListener The long click listener to be set.
     * @param <T> The type of the dynamic object.
     *
     * @see DynamicItemView#getItemView()
     * @see DynamicPreference#getPreferenceView()
     * @see View#setOnLongClickListener(View.OnLongClickListener)
     */
    public static <T> void setOnLongClickListener(@Nullable T dynamic,
            @Nullable View.OnLongClickListener onLongClickListener) {
        if (dynamic instanceof DynamicItemView) {
            ((DynamicItemView) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof DynamicInfoView) {
            ((DynamicInfoView) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof DynamicPreference) {
            ((DynamicPreference) dynamic).setOnLongClickListener(onLongClickListener);
        } else if (dynamic instanceof View) {
            ((View) dynamic).setOnLongClickListener(onLongClickListener);
        }
    }

    /**
     * Set the clickable property for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param clickable {@code true} to make it clickable.
     * @param <T> The type of the dynamic object.
     *
     * @see View#setClickable(boolean)
     */
    public static <T> void setClickable(@Nullable T dynamic, boolean clickable) {
        if (dynamic instanceof View) {
            ((View) dynamic).setClickable(clickable);
        }
    }

    /**
     * Set the long clickable property for the dynamic object.
     *
     * @param dynamic The dynamic object to be used.
     * @param longClickable {@code true} to make it long clickable.
     * @param <T> The type of the dynamic object.
     *
     * @see View#setLongClickable(boolean)
     */
    public static <T> void setLongClickable(@Nullable T dynamic, boolean longClickable) {
        if (dynamic instanceof View) {
            ((View) dynamic).setLongClickable(longClickable);
        }
    }

    /**
     * Sets the prevent corner overlap for the supplied view.
     *
     * @param view The view to be used.
     * @param preventCornerOverlap The value to be set.
     *
     * @see CardView#setPreventCornerOverlap(boolean)
     */
    public static void setPreventCornerOverlap(@Nullable View view, boolean preventCornerOverlap) {
        if (view instanceof CardView) {
            ((CardView) view).setPreventCornerOverlap(preventCornerOverlap);
        }
    }

    /**
     * Sets the corner for the supplied view.
     *
     * @param view The view to be used.
     * @param corner The corner to be set.
     *
     * @see DynamicCardView#setCorner(Float)
     * @see DynamicMaterialCardView#setCorner(Float)
     */
    public static void setCorner(@Nullable View view, float corner) {
        if (view instanceof DynamicCardView) {
            ((DynamicCardView) view).setCorner(corner);
        } else if (view instanceof DynamicMaterialCardView) {
            ((DynamicMaterialCardView) view).setCorner(corner);
        }
    }

    /**
     * Sets the minimum corner value for the supplied view.
     *
     * @param view The view to be used.
     * @param cornerMax The maximum corner value.
     *
     * @see #setCorner(View, float)
     */
    public static void setCornerMin(@Nullable View view, float cornerMax) {
        if (view instanceof DynamicCardView) {
            setCorner(view, Math.min(((DynamicCardView) view).getCorner(), cornerMax));
        } else if (view instanceof DynamicMaterialCardView) {
            setCorner(view, Math.min(((DynamicMaterialCardView) view).getCorner(), cornerMax));
        }
    }

    /**
     * Set the elevation on same background option for the supplied view.
     *
     * @param view The view to be used.
     * @param elevationOnSameBackground {@code true} to enable elevation on the same background.
     */
    public static void setElevationOnSameBackground(@Nullable View view,
            boolean elevationOnSameBackground ) {
        if (view instanceof DynamicSurfaceWidget) {
            ((DynamicSurfaceWidget) view).setElevationOnSameBackground(elevationOnSameBackground );
        }
    }

    /**
     * Set drawable for the image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable to be set.
     */
    public static void set(@Nullable ImageView imageView, @Nullable Drawable drawable) {
        if (imageView == null) {
            return;
        }

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * Set bitmap for the image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param bitmap The bitmap to be set.
     */
    public static void set(@Nullable ImageView imageView, @Nullable Bitmap bitmap) {
        if (imageView == null) {
            return;
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * Set content description for the image view.
     *
     * @param imageView The image view to set the content description.
     * @param text The content description to be set.
     */
    public static void setContentDescription(
            @Nullable ImageView imageView, @Nullable String text) {
        if (imageView == null) {
            return;
        }

        imageView.setContentDescription(text);
    }

    /**
     * Set text for the text view and mange its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void set(@Nullable TextView textView, @Nullable String text) {
        if (textView == null) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set text for the text view and mange its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param text The text to be set.
     */
    public static void set(@Nullable TextView textView, @Nullable CharSequence text) {
        if (textView == null) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Set drawable for the image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawableRes The drawable resource id to be set.
     */
    public static void setResource(@Nullable ImageView imageView, @DrawableRes int drawableRes) {
        if (imageView != null) {
            set(imageView, DynamicResourceUtils.getDrawable(imageView.getContext(), drawableRes));
        }
    }

    /**
     * Set text for the text view and mange its visibility according to the data.
     *
     * @param textView The text view to set the text.
     * @param stringRes The string resource id to be set.
     */
    public static void setText(@Nullable TextView textView, @StringRes int stringRes) {
        if (textView != null) {
            set(textView, textView.getContext().getString(stringRes));
        }
    }

    /**
     * Set click listener for the view and mange its visibility according to the data.
     *
     * @param view The view to set the click listener.
     * @param clickListener The click listener to be set.
     * @param visibility {@code true} to mange the visibility.
     */
    public static void setClickListener(@Nullable View view,
            @Nullable View.OnClickListener clickListener, boolean visibility) {
        if (view == null) {
            return;
        }

        if (clickListener != null) {
            view.setOnClickListener(clickListener);

            if (visibility) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            view.setClickable(false);

            if (visibility) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Set click listener for the view and mange its visibility according to the data.
     *
     * @param view The view to set the click listener.
     * @param clickListener The click listener to be set.
     */
    public static void setClickListener(@Nullable View view,
            @Nullable View.OnClickListener clickListener) {
        setClickListener(view, clickListener, false);
    }

    /**
     * Set visibility for the view.
     *
     * @param view The view to set the visibility.
     * @param visibility The visibility to be set.
     */
    public static void setVisibility(@Nullable View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * Set visibility for the view according to the base view.
     *
     * @param view The view to set the visibility.
     * @param base The view to get the visibility.
     */
    public static void setVisibility(@Nullable View view, @Nullable View base) {
        if (base != null) {
            setVisibility(view, base.getVisibility());
        }
    }

    /**
     * Set a view enabled or disabled.
     *
     * @param view The view to be enabled or disabled.
     * @param enabled {@code true} to enable the view.
     */
    public static void setEnabled(@Nullable View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    /**
     * Set alpha for the view.
     *
     * @param view The view to set the alpha.
     * @param alpha The alpha to be set.
     */
    public static void setAlpha(@Nullable View view, @FloatRange(from = 0f, to = 1f) float alpha) {
        if (view != null) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param text The text for the snackbar.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     */
    public static void showSnackbar(@Nullable Context activity,
            @Nullable CharSequence text, @Snackbar.Duration int duration) {
        if (activity instanceof DynamicSnackbar && text != null) {
            ((DynamicSnackbar) activity).getSnackbar(text, duration).show();
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param text The text for the snackbar.D
     */
    public static void showSnackbar(@Nullable Context activity, @Nullable CharSequence text) {
        if (activity instanceof DynamicSnackbar && text != null) {
            ((DynamicSnackbar) activity).getSnackbar(text).show();
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param stringRes The string resource for the snackbar.
     * @param duration The duration of the snackbar.
     *                 <p>{@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG}
     *                 or {@link Snackbar#LENGTH_INDEFINITE}.
     */
    public static void showSnackbar(@Nullable Context activity,
            @StringRes int stringRes, @Snackbar.Duration int duration) {
        if (activity instanceof DynamicSnackbar) {
            ((DynamicSnackbar) activity).getSnackbar(stringRes, duration).show();
        }
    }

    /**
     * Show the snackbar for the {@link DynamicSnackbar}.
     *
     * @param activity The activity context to be used.
     * @param stringRes The string resource for the snackbar.
     */
    public static void showSnackbar(@Nullable Context activity, @StringRes int stringRes) {
        if (activity instanceof DynamicSnackbar) {
            ((DynamicSnackbar) activity).getSnackbar(stringRes).show();
        }
    }

    /**
     * Sets the bottom sheet state if present for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param bottomSheetState The state to be set.
     */
    public static void setBottomSheetState(@Nullable Context activity,
            @BottomSheetBehavior.State int bottomSheetState) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setBottomSheetState(bottomSheetState);
        }
    }

    /**
     * Sets the transition result code for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param transitionResultCode The transition result code to be set.
     */
    public static void setTransitionResultCode(@Nullable Context activity,
            int transitionResultCode) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setTransitionResultCode(transitionResultCode);
        }
    }

    /**
     * Sets the transition position for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param transitionPosition The transition position to be set.
     */
    public static void setTransitionPosition(@Nullable Context activity, int transitionPosition) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setTransitionPosition(transitionPosition);
        }
    }

    /**
     * Sets the listener to listen the search view expand and collapse callbacks
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param searchListener The search listener to be set.
     */
    public static void setSearchViewListener(@Nullable Context activity,
            @Nullable DynamicSearchListener searchListener) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).setSearchViewListener(searchListener);
        }
    }

    /**
     * Adds the text watcher to listen search view text changes for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param textWatcher The search listener to be added.
     */
    public static void addSearchViewTextChangedListener(@Nullable Context activity,
            @Nullable TextWatcher textWatcher) {
        EditText editText;
        if (activity instanceof DynamicActivity && textWatcher != null
                && (editText = ((DynamicActivity) activity).getSearchViewEditText()) != null) {
            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Removes the text watcher from the search view to stop listening text changes
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param textWatcher The search listener to be removed.
     */
    public static void removeSearchViewTextChangedListener(@Nullable Context activity,
            @Nullable TextWatcher textWatcher) {
        EditText editText;
        if (activity instanceof DynamicActivity && textWatcher != null
                && (editText = ((DynamicActivity) activity).getSearchViewEditText()) != null) {
            editText.removeTextChangedListener(textWatcher);
        }
    }

    /**
     * Expand search view to start searching for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param requestSoftInput {@code true} to request the soft input keyboard.
     */
    public static void expandSearchView(@Nullable Context activity, boolean requestSoftInput) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).expandSearchView(requestSoftInput);
        }
    }

    /**
     * Collapse search view to stop searching for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     */
    public static void collapseSearchView(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).collapseSearchView();
        }
    }

    /**
     * Checks whether the search view is expanded for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     *
     * @return {@code true} if search view is expanded.
     */
    public static boolean isSearchViewExpanded(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).isSearchViewExpanded();
        }

        return false;
    }

    /**
     * Restore the search view state after the configuration change
     * for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     */
    public static void restoreSearchViewState(@Nullable Context activity) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).isSearchViewExpanded();
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     * @param animate {@code true} to animate the changes.
     */
    public static void addHeader(@Nullable Context activity, @Nullable View view,
            boolean removePrevious, boolean animate) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(view, removePrevious, animate);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addHeader(@Nullable Context activity,
            @Nullable View view, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(view, removePrevious);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     * @param animate {@code true} to animate the changes.
     */
    public static void addHeader(@Nullable Context activity, @LayoutRes int layoutRes,
            boolean removePrevious, boolean animate) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(layoutRes, removePrevious, animate);
        }
    }

    /**
     * Add header view just below the app bar for the {@link DynamicActivity}.
     * <p>Useful to add tabs or hints dynamically. Multiple views can be added and the default
     * background will be the app bar background (theme primary color). Please check
     * {@link com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment} to
     * add the tabs automatically.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the header frame.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addHeader(@Nullable Context activity,
            @LayoutRes int layoutRes, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addHeader(layoutRes, removePrevious);
        }
    }

    /**
     * Add view in the bottom sheet frame layout for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param view The view to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addBottomSheet(@Nullable Context activity,
            @Nullable View view, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addBottomSheet(view, removePrevious);
        }
    }

    /**
     * Add view in the bottom sheet frame layout for the {@link DynamicActivity}.
     *
     * @param activity The activity context to be used.
     * @param layoutRes The layout resource to be added in the bottom sheet.
     * @param removePrevious {@code true} to remove the previously added views.
     */
    public static void addBottomSheet(@Nullable Context activity,
            @LayoutRes int layoutRes, boolean removePrevious) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).addBottomSheet(layoutRes, removePrevious);
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior
     * for the {@link DynamicActivity}.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param activity The activity context to be used.
     * @param fragment The calling fragment.
     * @param intent The intent to be used.
     * @param requestCode The request code to be set.
     * @param options The intent to be set.
     * @param finish {@code true} to finish calling activity.
     * @param afterTransition {@code true} to finish the calling activity after transition.
     */
    public static void startMotionActivityFromFragment(@Nullable Context activity,
            @NonNull Fragment fragment, @SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options, boolean finish, boolean afterTransition) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).startMotionActivityFromFragment(
                    fragment, intent, requestCode, options, finish, afterTransition);
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior
     * for the {@link DynamicActivity}.
     *
     * <p>It will allow {@link Bundle} options if {@link DynamicMotion#isMotion()} is enabled.
     *
     * @param activity The activity context to be used.
     * @param fragment The calling fragment.
     * @param intent The intent to be used.
     * @param requestCode The request code to be set.
     * @param options The intent to be set.
     */
    public static void startMotionActivityFromFragment(@Nullable Context activity,
            @NonNull Fragment fragment, @SuppressLint("UnknownNullness") Intent intent,
            int requestCode, @Nullable Bundle options) {
        if (activity instanceof DynamicActivity) {
            ((DynamicActivity) activity).startMotionActivityFromFragment(
                    fragment, intent, requestCode, options);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param position The position index of the first page currently being displayed.
     *                 <p>Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset The value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels The value in pixels indicating the offset from position.
     */
    public static void onPageScrolled(@Nullable ViewPager.OnPageChangeListener pager,
            int position, float positionOffset, @Px int positionOffsetPixels) {
        if (pager != null) {
            pager.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onPageSelected(int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param position Position index of the new selected page.
     */
    public static void onPageSelected(@Nullable ViewPager.OnPageChangeListener pager,
            int position) {
        if (pager != null) {
            pager.onPageSelected(position);
        }
    }

    /**
     * Call {@link ViewPager.OnPageChangeListener#onScreenStateChanged(int)} method
     * for the {@link ViewPager}.
     *
     * @param pager The view pager to be used.
     * @param state The new scroll state.
     *
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    public static void onPageScrollStateChanged(@Nullable ViewPager.OnPageChangeListener pager,
            int state) {
        if (pager != null) {
            pager.onPageScrollStateChanged(state);
        }
    }

    /**
     * Call {@link Tutorial#getBackgroundColor()} method for the {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param defaultColor The default color for the background.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     *
     * @return The background color for the supplied tutorial.
     */
    public static @ColorInt <T, V> int getBackgroundColor(@Nullable Tutorial<T, V> tutorial,
            @ColorInt int defaultColor) {
        if (tutorial != null) {
            return tutorial.getBackgroundColor();
        }

        return defaultColor;
    }

    /**
     * Call {@link Tutorial#onBackgroundColorChanged(int)} method for the
     * {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param color The color of the background.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     */
    public static <T, V> void onBackgroundColorChanged(@Nullable Tutorial<T, V> tutorial,
            @ColorInt int color) {
        if (tutorial != null) {
            tutorial.onBackgroundColorChanged(color);
        }
    }

    /**
     * Call {@link Tutorial#onSetPadding(int, int, int, int)} method for the
     * {@link Tutorial}.
     *
     * @param tutorial The dynamic tutorial to be used.
     * @param left The left padding supplied by the container.
     * @param top The top padding supplied by the container.
     * @param right The right padding supplied by the container.
     * @param bottom The bottom padding supplied by the container.
     * @param <T> The type of the tutorial.
     * @param <V> The type of the tutorial fragment.
     */
    public static <T, V> void onSetPadding(@Nullable Tutorial<T, V> tutorial,
            int left, int top, int right, int bottom) {
        if (tutorial != null) {
            tutorial.onSetPadding(left, top, right, bottom);
        }
    }

    /**
     * Checks whether the stroke is required for the supplied background and surface colors.
     *
     * @return {@code true} if the stroke is required for the supplied background
     *         and surface colors.
     */
    public static boolean isStrokeRequired(@ColorInt int background, @ColorInt int surface) {
        return DynamicSdkUtils.is16()
                && DynamicColorUtils.removeAlpha(background)
                == DynamicColorUtils.removeAlpha(surface)
                && Color.alpha(surface) < Defaults.ADS_ALPHA_SURFACE_STROKE;
    }

    /**
     * Checks whether the stroke is required for the current background and surface colors.
     *
     * @return {@code true} if the stroke is required for the current background
     *         and surface colors.
     */
    public static boolean isStrokeRequired() {
        return isStrokeRequired(DynamicTheme.getInstance().get().getBackgroundColor(),
                DynamicTheme.getInstance().get().getSurfaceColor());
    }
}
