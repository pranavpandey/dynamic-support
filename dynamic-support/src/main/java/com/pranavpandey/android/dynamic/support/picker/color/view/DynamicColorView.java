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

package com.pranavpandey.android.dynamic.support.picker.color.view;

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

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.graphics.DynamicPaint;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorPicker;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.util.DynamicPickerUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout;
import com.pranavpandey.android.dynamic.support.widget.tooltip.DynamicTooltip;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;
import com.pranavpandey.android.dynamic.util.DynamicUnitUtils;

/**
 * A {@link DynamicFrameLayout} to display a color in different {@link DynamicColorShape}.
 * <p>It will provide various useful methods for the {@link DynamicColorPicker}
 * to represent a set of colors and select a color from it.
 */
public class DynamicColorView extends DynamicFrameLayout {

    /**
     * Constant for the color view icon divisor.
     */
    private static final float ICON_DIVISOR = 2.2f;

    /**
     * Alpha constant for the color view state.
     */
    private static final int ALPHA_STATE = 60;

    /**
     * Alpha constant for the color view selector.
     */
    private static final int ALPHA_SELECTOR = 100;

    /**
     * Size constant for the alpha pattern paint in DP.
     */
    private static final int ALPHA_PATTERN_SIZE = 4;

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
    private float mCornerRadius;

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
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorView);

        try {
            mColorShape = a.getInt(R.styleable.DynamicColorView_ads_shape,
                    DynamicColorShape.CIRCLE);
            mAlpha = a.getBoolean(R.styleable.DynamicColorView_ads_alphaEnabled,
                    false);
            mCornerRadius = a.getDimensionPixelOffset(
                    R.styleable.DynamicColorView_adt_cornerRadius,
                    DynamicTheme.getInstance().get().getCornerRadius());
        } finally {
            a.recycle();
        }

        mAlphaPaint = DynamicPickerUtils.getAlphaPatternPaint(
                DynamicUnitUtils.convertDpToPixels(ALPHA_PATTERN_SIZE));
        mColorPaint = new DynamicPaint();
        mColorStrokePaint = new DynamicPaint();
        mSelectorPaint = new DynamicPaint();
        mRectF = new RectF(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());

        mColorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStrokeWidth(Theme.Size.STROKE_PIXEL);
        mColorStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mSelectorPaint.setFilterBitmap(true);

        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (DynamicSdkUtils.is16()) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        setColor(getColor());
                        setWillNotDraw(false);
                    }
        });

        super.loadFromAttributes(attrs);
    }

    /**
     * Update this color view according to the current parameters.
     */
    private void onUpdate() {
        @ColorInt int color = getColor();

        if (color == Theme.AUTO) {
            color = getContrastWithColor();
            @ColorInt int tintColor = Dynamic.getTintColor(getContrastWithColor());
            mSelectorBitmap = DynamicBitmapUtils.getBitmap(
                    DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_play));
            mColorPaint.setColor(getContrastWithColor());
            mColorStrokePaint.setColor(DynamicColorUtils.removeAlpha(tintColor));

            if (getWidth() > 0 && getHeight() > 0) {
                RadialGradient gradient = new RadialGradient(getWidth() / 2f,
                        getHeight() / 2f, Math.max(getWidth(), getHeight()),
                        new int[] { getContrastWithColor(), tintColor },
                        null, Shader.TileMode.CLAMP);
                mColorPaint.setShader(gradient);
            }
        } else {
            mSelectorBitmap = DynamicBitmapUtils.getBitmap(
                    DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_check));
            mColorPaint.setColor(color);
            mColorStrokePaint.setColor(DynamicColorUtils.removeAlpha(Dynamic.getTintColor(color)));

            mColorPaint.setShader(null);
        }

        if (Color.alpha(color) < ALPHA_SELECTOR) {
            mSelectorPaint.setColor(Dynamic.withContrastRatio(
                    mColorStrokePaint.getColor(), Color.LTGRAY));
        } else {
            mSelectorPaint.setColor(mColorStrokePaint.getColor());
        }
        mSelectorPaint.setColorFilter(new PorterDuffColorFilter(
                mSelectorPaint.getColor(), PorterDuff.Mode.SRC_ATOP));
    }

    @SuppressWarnings("SuspiciousNameCombination")
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

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mColorShape == DynamicColorShape.RECTANGLE) {
            Dynamic.setCornerMin(this, Math.min(
                    getWidth() / Theme.Corner.FACTOR_MAX,
                    getHeight() / Theme.Corner.FACTOR_MAX));
        } else {
            Dynamic.setCornerMin(this, Math.min(
                    getWidth() / Theme.Corner.FACTOR_MAX_LARGE,
                    getHeight() / Theme.Corner.FACTOR_MAX_LARGE));
        }

        mRectF.set(getPaddingLeft(), getPaddingTop(), getWidth(), getHeight());
        mRectF.set(Theme.Size.STROKE_PIXEL, Theme.Size.STROKE_PIXEL,
                mRectF.width() - Theme.Size.STROKE_PIXEL,
                mRectF.height() - Theme.Size.STROKE_PIXEL);

        if (mSelected) {
            int selectorSize = (int) (Math.min(getWidth(), getHeight())
                    - Math.min(getWidth(), getHeight()) / ICON_DIVISOR);
            mSelectorBitmap = DynamicBitmapUtils.resizeBitmap(
                    mSelectorBitmap, selectorSize, selectorSize);
        }

        if (isClickable()) {
            setForeground(getForegroundDrawable());
        }
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
            canvas.drawBitmap(mSelectorBitmap, (getWidth() - mSelectorBitmap.getWidth()) / 2f,
                    (getHeight() - mSelectorBitmap.getHeight()) / 2f, mSelectorPaint);
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

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    /**
     * Returns a state list drawable to use it as the foreground for this color view.
     *
     * @return A state list drawable according to the color to use as the foreground  drawable
     *         for this color view.
     */
    private StateListDrawable getForegroundDrawable() {
        Bitmap bitmap = Bitmap.createBitmap((int) mRectF.width() + Theme.Size.STROKE_PIXEL,
                (int) mRectF.height() + Theme.Size.STROKE_PIXEL, Bitmap.Config.ARGB_8888);

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
        @ColorInt int color = getColor();
        @ColorInt int tintColor;
        Drawable icon = null;

        if (color == Theme.AUTO) {
            color = mSelectorPaint.getColor();
            tintColor = Dynamic.getTintColor(color);
        } else {
            tintColor = mSelectorPaint.getColor();
        }

        if (mSelected) {
            icon = DynamicResourceUtils.getDrawable(getContext(),getColor() == Theme.AUTO
                    ? R.drawable.ads_ic_play : R.drawable.ads_ic_check);
        }

        DynamicTooltip.set(this, color, tintColor, icon, getColorString());
    }

    @Override
    public @ColorInt int getColor() {
        return getColor(false);
    }

    @Override
    public void setColor() {
        onUpdate();

        invalidate();
        requestLayout();
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

        invalidate();
        requestLayout();
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

        invalidate();
        requestLayout();
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

        invalidate();
        requestLayout();
    }

    /**
     * Get the corner radius for the square or rectangle shape.
     *
     * @return The corner radius for the square or rectangle shape.
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * Set the corner radius for the square or rectangle shape.
     *
     * @param cornerRadius The corner radius to be set in pixels.
     */
    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;

        invalidate();
        requestLayout();
    }

    /**
     * Returns the hexadecimal color string according to the supplied color.
     *
     * @return The hexadecimal color string according to the supplied color.
     *         <p>It will return {@link R.string#ads_theme_entry_auto} if the color value is
     *         {@link Theme#AUTO}.
     */
    public @NonNull String getColorString() {
        return getColorString(getContext(), getColor(), mAlpha);
    }

    /**
     * Returns the hexadecimal color string according to the supplied color.
     *
     * @param context The context to be used.
     * @param color The color to be converted into string.
     * @param alpha {@code true} to enable alpha string.
     *
     * @return The hexadecimal color string according to the supplied color.
     *         <p>It will return {@link R.string#ads_theme_entry_auto} if the color value is
     *         {@link Theme#AUTO}.
     */
    public static @NonNull String getColorString(@NonNull Context context,
            int color, boolean alpha) {
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
