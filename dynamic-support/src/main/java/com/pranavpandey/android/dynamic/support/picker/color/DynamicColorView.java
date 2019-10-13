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

package com.pranavpandey.android.dynamic.support.picker.color;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.support.widget.tooltip.DynamicTooltip;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * A FrameLayout to display a color in different {@link DynamicColorShape}.
 * <p>It will provide various useful methods for the {@link DynamicColorPicker}
 * to represent a set of colors and select a color from it.
 */
public class DynamicColorView extends FrameLayout {

    /**
     * Constant for color view stroke width, 1 dip.
     */
    private static final int ADS_STROKE_WIDTH = DynamicUnitUtils.convertDpToPixels(1);

    /**
     * Constant for color view icon divisor.
     */
    private static final float ICON_DIVISOR = 2.2f;

    /**
     * Alpha constant for color view state.
     */
    private static final int ALPHA_STATE = 60;

    /**
     * Alpha constant for color view selector.
     */
    private static final int ALPHA_SELECTOR = 100;

    /**
     * RectF to draw the color view.
     */
    private RectF mRectF;

    /**
     * Paint for the alpha drawable.
     */
    private Paint mAlphaPaint;

    /**
     * Paint for the color.
     */
    private Paint mColorPaint;

    /**
     * Paint for the color stroke.
     */
    private Paint mColorStrokePaint;

    /**
     * Paint for the selector drawable.
     */
    private Paint mSelectorPaint;

    /**
     * Bitmap for the selector drawable.
     */
    private Bitmap mSelectorBitmap;

    /**
     * Shape used by this color view.
     *
     * @see DynamicColorShape
     */
    private @DynamicColorShape int mColorShape;

    /**
     * Color used by this color view.
     */
    private @ColorInt int mColor;

    /**
     * {@code true} if this color view is selected.
     */
    private boolean mSelected;

    /**
     * {@code true} to enable alpha for the color.
     */
    private boolean mAlpha;

    /**
     * Corner radius for the square or rectangle shape.
     */
    private int mCornerRadius;

    public DynamicColorView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicColorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicColorView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorView);

        try {
            mColorShape = a.getInt(R.styleable.DynamicColorView_ads_shape,
                    DynamicColorShape.CIRCLE);
            mColor = a.getColor(R.styleable.DynamicColorView_ads_color, 0);
            mAlpha = a.getBoolean(R.styleable.DynamicColorView_ads_alphaEnabled,
                    false);
            mCornerRadius = a.getDimensionPixelOffset(
                    R.styleable.DynamicColorView_ads_cornerRadius,
                    getResources().getDimensionPixelOffset(R.dimen.ads_corner_radius));
        } finally {
            a.recycle();
        }

        mAlphaPaint = DynamicPickerUtils.getAlphaPatternPaint(DynamicUnitUtils.convertDpToPixels(4));
        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

        mColorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStrokeWidth(ADS_STROKE_WIDTH);
        mColorStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mSelectorPaint.setFilterBitmap(true);

        onUpdate(mColor);
        setWillNotDraw(false);

        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (DynamicSdkUtils.is16()) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        setColor(mColor);
                    }
        });
    }

    /**
     * Update this color view according to the current parameters.
     */
    private void onUpdate(@ColorInt int color) {
        this.mColor = color;

        if (mColor == Theme.AUTO) {
            @ColorInt int tintColor = DynamicColorUtils.getTintColor(
                    DynamicTheme.getInstance().get().getBackgroundColor());

            mSelectorBitmap = DynamicBitmapUtils.getBitmapFromDrawable(
                    DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_play));
            mColorStrokePaint.setColor(tintColor);
            mColorPaint.setColor(DynamicTheme.getInstance().get().getBackgroundColor());

            if (getMeasuredWidth() > 0) {
                RadialGradient gradient =
                        new RadialGradient(getMeasuredWidth() / 2f, getMeasuredWidth() / 2f,
                                getMeasuredWidth(), new int[] {
                                        DynamicTheme.getInstance().get().getBackgroundColor(),
                                tintColor, DynamicTheme.getInstance().get().getPrimaryColor() },
                                null, Shader.TileMode.CLAMP);
                mColorPaint.setShader(gradient);
            }
        } else {
            mSelectorBitmap = DynamicBitmapUtils.getBitmapFromDrawable(
                    DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_check));
            mColorPaint.setColor(color);
            mColorStrokePaint.setColor(DynamicColorUtils.removeAlpha(
                    DynamicColorUtils.getTintColor(color)));

            mColorPaint.setShader(null);
        }

        if (Color.alpha(getColor()) < ALPHA_SELECTOR) {
            mSelectorPaint.setColor(DynamicColorUtils.getContrastColor(
                    mColorStrokePaint.getColor(), Color.LTGRAY));
        } else {
            mSelectorPaint.setColor(mColorStrokePaint.getColor());
        }
        mSelectorPaint.setColorFilter(new PorterDuffColorFilter(
                mSelectorPaint.getColor(), PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mColorShape != DynamicColorShape.RECTANGLE) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mRectF.set(ADS_STROKE_WIDTH, ADS_STROKE_WIDTH,
                mRectF.width() - ADS_STROKE_WIDTH, mRectF.height() - ADS_STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mColorShape == DynamicColorShape.CIRCLE) {
            canvas.drawOval(mRectF, mAlphaPaint);
            canvas.drawOval(mRectF, mColorPaint);
            canvas.drawOval(mRectF, mColorStrokePaint);
        } else {
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mAlphaPaint);
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mColorPaint);
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mColorStrokePaint);
        }

        if (mSelected) {
            int selectorSize = (int) (getMeasuredWidth() - getMeasuredWidth() / ICON_DIVISOR);
            mSelectorBitmap = DynamicBitmapUtils
                    .resizeBitmap(mSelectorBitmap, selectorSize, selectorSize);

            canvas.drawBitmap(mSelectorBitmap,
                    (getMeasuredWidth() - mSelectorBitmap.getWidth()) / 2f,
                    (getMeasuredWidth() - mSelectorBitmap.getHeight()) / 2f, mSelectorPaint);
        }

        if (isClickable()) {
            setForeground(getForegroundDrawable());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        try {
            if (mSelectorBitmap != null && !mSelectorBitmap.isRecycled()) {
                mSelectorBitmap.recycle();
                mSelectorBitmap = null;
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? WidgetDefaults.ADS_ALPHA_ENABLED : WidgetDefaults.ADS_ALPHA_DISABLED);
    }

    /**
     * Returns a state list drawable to use it as the foreground for this color view.
     *
     * @return A state list drawable according to the color to use as the foreground  drawable
     *         for this color view.
     */
    private StateListDrawable getForegroundDrawable() {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) mRectF.width() + ADS_STROKE_WIDTH,
                (int) mRectF.height() + ADS_STROKE_WIDTH,
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        if (mColorShape == DynamicColorShape.CIRCLE) {
            canvas.drawOval(mRectF, mSelectorPaint);
        } else {
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mSelectorPaint);
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed},
                new BitmapDrawable(getResources(), bitmap));
        stateListDrawable.setAlpha(ALPHA_STATE);

        return stateListDrawable;
    }

    /**
     * Set a tooltip for this color view with hexadecimal color string according to its color.
     */
    public void setTooltip() {
        @ColorInt int color;
        @ColorInt int tintColor;
        Drawable icon = null;

        if (mColor == Theme.AUTO) {
            color = mSelectorPaint.getColor();
            tintColor = DynamicColorUtils.getTintColor(color);
        } else {
            color = mColor;
            tintColor = mSelectorPaint.getColor();
        }

        color = DynamicColorUtils.removeAlpha(color);
        tintColor = DynamicColorUtils.removeAlpha(tintColor);

        if (mSelected) {
            icon = DynamicResourceUtils.getDrawable(getContext(), mColor == Theme.AUTO
                            ? R.drawable.ads_ic_play : R.drawable.ads_ic_check);
        }

        DynamicTooltip.set(this, color, tintColor, icon, getColorString());
    }

    /**
     * Get the color used by this color view.
     *
     * @return The color used by this color view.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Set the color for this color view.
     *
     * @param color The color to be set.
     */
    public void setColor(final @ColorInt int color) {
        onUpdate(color);
        requestLayout();
        invalidate();
    }

    /**
     * @return The shape used by this color view.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Set the shape used by this color view.
     *
     * @param colorShape The color shape to be set.
     */
    public void setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        requestLayout();
        invalidate();
    }

    /**
     * Returns whether this color view is selected.
     *
     * @return {@code true} if this color view is selected.
     */
    public boolean isSelected() {
        return mSelected;
    }

    /**
     * Set this color view selected or unselected.
     *
     * @param selected {@code true} to make it selected.
     */
    public void setSelected(boolean selected) {
        this.mSelected = selected;

        requestLayout();
        invalidate();
    }

    /**
     * Returns whether the alpha is enabled for the color.
     *
     * @return {@code true} to enable alpha for the color.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Set the alpha support for the color.
     *
     * @param alpha {@code true} to enable alpha.
     */
    public void setAlpha(boolean alpha) {
        this.mAlpha = alpha;

        requestLayout();
        invalidate();
    }

    /**
     * Get the corner radius for the square or rectangle shape.
     *
     * @return The corner radius for the square or rectangle shape.
     */
    public int getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * Set the corner radius for the square or rectangle shape.
     *
     * @param cornerRadius The corner radius to be set in pixels.
     */
    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = cornerRadius;

        requestLayout();
        invalidate();
    }

    /**
     * Returns the hexadecimal color string according to the supplied color.
     *
     * @return The hexadecimal color string according to the supplied color.
     *         <p>It will return {@link R.string#ads_theme_entry_auto} if the color value is
     *         {@link Theme#AUTO}.
     */
    public String getColorString() {
        return getColorString(getContext(), mColor, mAlpha);
    }

    /**
     * Returns the hexadecimal color string according to the supplied color.
     *
     * @param context The context to retrieve resources.
     * @param color The color to be converted into string.
     * @param alpha {@code true} to enable alpha string.
     *
     * @return The hexadecimal color string according to the supplied color.
     *         <p>It will return {@link R.string#ads_theme_entry_auto} if the color value is
     *         {@link Theme#AUTO}.
     */
    public static String getColorString(@NonNull Context context, int color, boolean alpha) {
        return color == Theme.AUTO
                ? context.getString(R.string.ads_theme_entry_auto)
                : DynamicColorUtils.getColorString(color, alpha, true);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        setColor(color);
    }

    @Override
    public void setBackgroundResource(@ColorRes int color) {
        setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    @Deprecated
    @Override
    public void setBackground(Drawable background) {
        throw new IllegalStateException("Cannot use setBackground(Drawable) on DynamicColorView.");
    }

    @Deprecated
    @Override
    public void setBackgroundDrawable(Drawable background) {
        throw new IllegalStateException(
                "Cannot use setBackgroundDrawable(Drawable) on DynamicColorView.");
    }

    @Deprecated
    @Override
    public void setActivated(boolean activated) {
        throw new IllegalStateException("Cannot use setActivated(boolean) on DynamicColorView.");
    }
}
