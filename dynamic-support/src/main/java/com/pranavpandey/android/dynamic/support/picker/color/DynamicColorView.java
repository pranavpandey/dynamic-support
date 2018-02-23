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

package com.pranavpandey.android.dynamic.support.picker.color;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.toasts.DynamicHint;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

/**
 * A FrameLayout to display a color in different {@link DynamicColorShape}.
 * It will provide various useful methods for the {@link DynamicColorPicker}
 * to represent a set of colors and select a color from it.
 */
public class DynamicColorView extends FrameLayout {

    /**
     * Constant for color view corners, 2 dip.
     */
    private static final int ADS_SQUARE_CORNERS = DynamicUnitUtils.convertDpToPixels(2);

    /**
     * Constant for color view stroke width, 1 dip.
     */
    private static final int ADS_STROKE_WIDTH = DynamicUnitUtils.convertDpToPixels(1);

    /**
     * Constant for color view icon divisor.
     */
    private static final float ADS_ICON_DIVISOR = 2.2f;

    /**
     * Constant for color view state alpha.
     */
    private static final int ADS_STATE_ALPHA = 60;

    /**
     * RectF to draw the color view.
     */
    private RectF mRectF;

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

    public DynamicColorView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicColorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicColorView(@NonNull Context context,
                            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     */
    protected void loadFromAttributes(@Nullable AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicColorView);

        try {
            mColorShape = a.getInt(R.styleable.DynamicColorView_ads_dynamicColorView_shape,
                    DynamicColorShape.CIRCLE);
            mColor = a.getColor(R.styleable.DynamicColorView_ads_dynamicColorView_color, 0);
            mAlpha = a.getBoolean(R.styleable.DynamicColorView_ads_dynamicColorView_alpha,
                    false);
        } finally {
            a.recycle();
        }

        mSelectorBitmap = DynamicBitmapUtils.getBitmapFormDrawable(
                DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_check));

        mColorPaint = new Paint();
        mColorStrokePaint = new Paint();
        mSelectorPaint = new Paint();
        mRectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

        mColorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectorPaint.setStyle(Paint.Style.FILL);
        mColorStrokePaint.setStrokeWidth(ADS_STROKE_WIDTH);
        mColorStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mColorStrokePaint.setAntiAlias(true);
        mColorPaint.setAntiAlias(true);
        mSelectorPaint.setAntiAlias(true);
        mSelectorPaint.setFilterBitmap(true);

        update(mColor);
        setWillNotDraw(false);
    }

    /**
     * Update this color view according to the current parameters.
     */
    private void update(@ColorInt int color) {
        this.mColor = color;

        if (mColor == DynamicTheme.ADS_THEME_AUTO) {
            mColorStrokePaint.setColor(DynamicTheme.getInstance().getTintBackgroundColor());
            mColorPaint.setShader(new LinearGradient(0, 0,
                    getMeasuredWidth() /2 , getMeasuredHeight() / 2,
                    mColorStrokePaint.getColor(),
                    DynamicTheme.getInstance().getBackgroundColor(), Shader.TileMode.MIRROR));
        } else {
            mColorPaint.setShader(null);
            mColorPaint.setColor(color);
            mColorStrokePaint.setColor(DynamicColorUtils.getTintColor(color));
        }

        mSelectorPaint.setColor(mColorStrokePaint.getColor());
        mSelectorPaint.setColorFilter(new PorterDuffColorFilter(
                mColorStrokePaint.getColor(), PorterDuff.Mode.SRC_ATOP));
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mRectF.set(ADS_STROKE_WIDTH, ADS_STROKE_WIDTH, mRectF.width() - ADS_STROKE_WIDTH,
                mRectF.height() - ADS_STROKE_WIDTH);

        if (mColorShape == DynamicColorShape.CIRCLE) {
            canvas.drawOval(mRectF, mColorPaint);
            canvas.drawOval(mRectF, mColorStrokePaint);
        } else {
            canvas.drawRoundRect(mRectF, ADS_SQUARE_CORNERS, ADS_SQUARE_CORNERS, mColorPaint);
            canvas.drawRoundRect(mRectF, ADS_SQUARE_CORNERS, ADS_SQUARE_CORNERS, mColorStrokePaint);
        }

        if (mSelected) {
            final int selectorSize = (int) (getMeasuredWidth() - getMeasuredWidth() / ADS_ICON_DIVISOR);
            mSelectorBitmap = DynamicBitmapUtils
                    .resizeBitmap(mSelectorBitmap, selectorSize, selectorSize);

            canvas.drawBitmap(mSelectorBitmap, (getMeasuredWidth() - mSelectorBitmap.getWidth()) / 2,
                    (getMeasuredWidth() - mSelectorBitmap.getHeight()) / 2, mSelectorPaint);
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
     * @return A StateListDrawable according to the {@link #mColor}
     *         to be used as the foreground drawable for this color
     *         view.
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
            canvas.drawRoundRect(mRectF, ADS_SQUARE_CORNERS,
                    ADS_SQUARE_CORNERS, mSelectorPaint);
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed},
                new BitmapDrawable(getResources(), bitmap));
        stateListDrawable.setAlpha(ADS_STATE_ALPHA);

        return stateListDrawable;
    }

    /**
     * Show a cheat sheet around (below or above) this color view
     * with hexadecimal color string according to the {@link #mColor}.
     */
    public void showHint() {
        final Toast toast;
        final @ColorInt int color;
        final @ColorInt int tintColor;

        if (mColor == DynamicTheme.ADS_THEME_AUTO) {
            color = mSelectorPaint.getColor();
            tintColor = DynamicColorUtils.getTintColor(color);
        } else {
            color = mColor;
            tintColor = mSelectorPaint.getColor();
        }

        if (mSelected) {
            toast = DynamicHint.make(getContext(), getColorString(),
                    DynamicResourceUtils.getDrawable(getContext(), R.drawable.ads_ic_check),
                    tintColor, color);
        } else {
            toast = DynamicHint.make(getContext(), getColorString(),
                    tintColor, color);
        }

        DynamicHint.show(this, toast);
    }

    /**
     * Getter for {@link #mColor}.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Setter for {@link #mColor}.
     */
    public void setColor(final @ColorInt int color) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                update(color);
                requestLayout();
                invalidate();
            }
        });
    }

    /**
     * Getter for {@link #mColorShape}.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Setter for {@link #mColorShape}.
     */
    public void setColorShape(@DynamicColorShape int mColorShape) {
        this.mColorShape = mColorShape;

        requestLayout();
        invalidate();
    }

    /**
     * Getter for {@link #mSelected}.
     */
    public boolean isSelected() {
        return mSelected;
    }

    /**
     * Setter for {@link #mSelected}.
     */
    public void setSelected(boolean selected) {
        this.mSelected = selected;

        requestLayout();
        invalidate();
    }

    /**
     * Getter for {@link #mAlpha}.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Setter for {@link #mAlpha}.
     */
    public void setAlpha(boolean alpha) {
        this.mAlpha = alpha;

        requestLayout();
        invalidate();
    }

    /**
     * @return The hexadecimal color string according to the {@link #mColor}.
     *         It will return {@link R.string#ads_theme_entry_auto}
     *         if the color value will be {@link DynamicTheme#ADS_THEME_AUTO}.
     */
    public String getColorString() {
        return getColorString(getContext(), mColor, mAlpha);
    }

    /**
     * @return The hexadecimal color string according to the supplied
     *         color. It will return {@link R.string#ads_theme_entry_auto}
     *         if the color value will be {@link DynamicTheme#ADS_THEME_AUTO}.
     *
     * @param context The context to retrieve resources.
     * @param color The color to be converted into string.
     * @param alpha {@code true} to enable alpha string.
     */
    public static String getColorString(@NonNull Context context, int color, boolean alpha) {
        return color == DynamicTheme.ADS_THEME_AUTO
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
        throw new IllegalStateException(
                "Cannot use setBackground(Drawable) on DynamicColorView.");
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
        throw new IllegalStateException(
                "Cannot use setActivated(boolean) on DynamicColorView.");
    }
}
