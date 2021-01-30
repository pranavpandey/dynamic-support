/*
 * Copyright 2018-2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.widget;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.pranavpandey.android.dynamic.support.widget.base.DynamicBackgroundWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicLinkWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicScrollableWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateSelectedWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicStateWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTextWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * Helper class to manipulate inflated views at runtime according to their implemented types.
 *
 * <p>These methods are provided to avoid any exceptions when views are replaced by the
 * {@link com.pranavpandey.android.dynamic.support.theme.DynamicLayoutInflater}.
 * So, this is the recommended way to modify any of the following view types at runtime.
 *
 * @see DynamicWidget
 * @see DynamicBackgroundWidget
 * @see com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget
 * @see DynamicLinkWidget
 * @see DynamicScrollableWidget
 * @see DynamicStateWidget
 * @see DynamicStateSelectedWidget
 * @see DynamicTextWidget
 */
public class Dynamic {

    /**
     * Sets the color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicWidget#setColorType(int)
     */
    public static void setColorType(@Nullable View view, @Theme.ColorType int colorType) {
        if (view instanceof DynamicWidget) {
            ((DynamicWidget) view).setColorType(colorType);
        }
    }

    /**
     * Sets the background color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicBackgroundWidget#setBackgroundColorType(int)
     */
    public static void setBackgroundColorType(@Nullable View view,
            @Theme.ColorType int colorType) {
        if (view instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) view).setBackgroundColorType(colorType);
        }
    }

    /**
     * Sets the contrast with color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicWidget#setContrastWithColorType(int)
     */
    public static void setContrastWithColorType(@Nullable View view,
            @Theme.ColorType int colorType) {
        if (view instanceof DynamicWidget) {
            ((DynamicWidget) view).setContrastWithColorType(colorType);
        }
    }

    /**
     * Sets the text color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicTextWidget#setTextColorType(int)
     */
    public static void setTextColorType(@Nullable View view, @Theme.ColorType int colorType) {
        if (view instanceof DynamicTextWidget) {
            ((DynamicTextWidget) view).setTextColorType(colorType);
        }
    }

    /**
     * Sets the link color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicLinkWidget#setLinkColorType(int)
     */
    public static void setLinkColorType(@Nullable View view, @Theme.ColorType int colorType) {
        if (view instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) view).setLinkColorType(colorType);
        }
    }

    /**
     * Sets the normal state color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicStateWidget#setStateNormalColorType(int)
     */
    public static void setStateNormalColorType(@Nullable View view,
            @Theme.ColorType int colorType) {
        if (view instanceof DynamicStateWidget) {
            ((DynamicStateWidget) view).setStateNormalColorType(colorType);
        }
    }

    /**
     * Sets the selected state color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static void setStateSelectedColorType(@Nullable View view,
            @Theme.ColorType int colorType) {
        if (view instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) view).setStateSelectedColorType(colorType);
        }
    }

    /**
     * Sets the scroll bar color type for the supplied view.
     *
     * @param view The view to be used.
     * @param colorType The color type to be set.
     *
     * @see DynamicScrollableWidget#setScrollBarColorType(int)
     */
    public static void setScrollBarColorType(@Nullable View view,
            @Theme.ColorType int colorType) {
        if (view instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) view).setScrollBarColorType(colorType);
        }
    }

    /**
     * Sets the color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicWidget#setColor(int)
     */
    public static void setColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicWidget) {
            ((DynamicWidget) view).setColor(color);
        }
    }

    /**
     * Sets the background color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicBackgroundWidget#setBackgroundColor(int)
     */
    public static void setBackgroundColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicBackgroundWidget) {
            ((DynamicBackgroundWidget) view).setBackgroundColor(color);
        }
    }

    /**
     * Sets the contrast with color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicWidget#setContrastWithColor(int)
     */
    public static void setContrastWithColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicWidget) {
            ((DynamicWidget) view).setContrastWithColor(color);
        }
    }

    /**
     * Sets the text color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicTextWidget#setTextColor(int)
     */
    public static void setTextColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicTextWidget) {
            ((DynamicTextWidget) view).setTextColor(color);
        }
    }

    /**
     * Sets the link color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicLinkWidget#setLinkColor(int)
     */
    public static void setLinkColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicLinkWidget) {
            ((DynamicLinkWidget) view).setLinkColor(color);
        }
    }

    /**
     * Sets the normal state color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicStateWidget#setStateNormalColor(int)
     */
    public static void setStateNormalColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicStateWidget) {
            ((DynamicStateWidget) view).setStateNormalColor(color);
        }
    }

    /**
     * Sets the selected state color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicStateSelectedWidget#setStateSelectedColorType(int)
     */
    public static void setStateSelectedColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicStateSelectedWidget) {
            ((DynamicStateSelectedWidget) view).setStateSelectedColor(color);
        }
    }

    /**
     * Sets the scroll bar color for the supplied view.
     *
     * @param view The view to be used.
     * @param color The color to be set.
     *
     * @see DynamicScrollableWidget#setScrollBarColor(int)
     */
    public static void setScrollBarColor(@Nullable View view, @ColorInt int color) {
        if (view instanceof DynamicScrollableWidget) {
            ((DynamicScrollableWidget) view).setScrollBarColor(color);
        }
    }

    /**
     * Sets the background aware for the supplied view.
     *
     * @param view The view to be used.
     * @param backgroundAware The color to be set.
     *
     * @see DynamicWidget#setBackgroundAware(int)
     */
    public static void setBackgroundAware(@Nullable View view,
            @Theme.BackgroundAware int backgroundAware) {
        if (view instanceof DynamicWidget) {
            ((DynamicWidget) view).setBackgroundAware(backgroundAware);
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
     * Tint dynamic widgets used by the tutorial according to the supplied color.
     *
     * @param dynamicWidget The dynamic widget to be tinted.
     * @param color The color to tint the widget.
     * @param contrastWithColor The contrast with color for the widget.
     */
    public static void tint(@Nullable View dynamicWidget,
            @ColorInt int color, @ColorInt int contrastWithColor) {
        if (dynamicWidget != null) {
            setContrastWithColor(dynamicWidget, contrastWithColor);
            setColor(dynamicWidget, color);

            if (dynamicWidget instanceof DynamicScrollableWidget) {
                setScrollBarColor(dynamicWidget, color);
            }
        }
    }

    /**
     * Set drawable for the tutorial image view and mange its visibility according to the data.
     *
     * @param imageView The image view to set the drawable.
     * @param drawable The drawable for the image view.
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
}
