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

package com.pranavpandey.android.dynamic.support.utils;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Helper class to tint the various input widgets like {@link EditText}, etc.
 * dynamically by using reflection. It will be used to match the color with
 * the app's theme.
 */
@RestrictTo(LIBRARY_GROUP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public final class DynamicInputUtils {

    /**
     * Set cursor color for the supplied {@link EditText}.
     *
     * @param editText The EditText to set the cursor color.
     * @param color The color for the cursor.
     */
    private static void setCursorColor(final @NonNull EditText editText,
                                       @ColorInt final int color) {
        @ColorInt int hintColor = DynamicColorUtils.adjustAlpha(
                color, WidgetDefaults.ADS_ALPHA_UNCHECKED);
        editText.setHintTextColor(hintColor);
        editText.setHighlightColor(DynamicColorUtils.getContrastColor(
                hintColor, editText.getCurrentTextColor()));

        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);

            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = DynamicResourceUtils.getDrawable(
                    editText.getContext(), mCursorDrawableRes);
            drawables[0] = DynamicDrawableUtils.colorizeDrawable(drawables[0], color);
            drawables[1] = DynamicResourceUtils.getDrawable(
                    editText.getContext(), mCursorDrawableRes);
            drawables[1] = DynamicDrawableUtils.colorizeDrawable(drawables[1], color);
            fCursorDrawable.set(editor, drawables);

            String[] resFieldNames = { "mTextSelectHandleLeftRes",
                    "mTextSelectHandleRightRes", "mTextSelectHandleRes" };
            String[] drawableFieldNames = { "mSelectHandleLeft",
                    "mSelectHandleRight", "mSelectHandleCenter" };

            for (int i = 0; i < resFieldNames.length; i++) {
                String resFieldName = resFieldNames[i];
                String drawableFieldName = drawableFieldNames[i];

                fEditor = TextView.class.getDeclaredField(resFieldName);
                fEditor.setAccessible(true);
                int selectHandleRes = fEditor.getInt(editText);

                Drawable selectHandleDrawable = DynamicDrawableUtils.colorizeDrawable(
                        DynamicResourceUtils.getDrawable(
                                editText.getContext(), selectHandleRes), color);

                fEditor = editor.getClass().getDeclaredField(drawableFieldName);
                fEditor.setAccessible(true);
                fEditor.set(editor, selectHandleDrawable);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Tint an {@link EditText} by changing its cursor, hint, etc. colors
     * according to the supplied color.
     *
     * @param editText The EditText to be colorized.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull EditText editText, @ColorInt int color) {
        final ColorStateList editTextColorStateList =
                DynamicResourceUtils.getColorStateListWithStates(color);

        if (editText instanceof AppCompatEditText
                || editText instanceof TintableBackgroundView) {
            ViewCompat.setBackgroundTintList(editText, editTextColorStateList);
        } else if (DynamicVersionUtils.isLollipop()) {
            editText.setBackgroundTintList(editTextColorStateList);
        }

        setCursorColor(editText, color);
    }

    /**
     * Set hint color for the supplied {@link TextInputLayout}.
     *
     * @param textInputLayout The TextInputLayout to set the hint color.
     * @param color The color to be used.
     */
    public static void setHint(@NonNull TextInputLayout textInputLayout, @ColorInt int color) {
        try {
            final Field mDefaultTextColorField =
                    TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            mDefaultTextColorField.setAccessible(true);
            mDefaultTextColorField.set(textInputLayout, ColorStateList.valueOf(color));
            final Method updateLabelStateMethod =
                    TextInputLayout.class.getDeclaredMethod(
                            "updateLabelState", boolean.class, boolean.class);
            updateLabelStateMethod.setAccessible(true);
            updateLabelStateMethod.invoke(textInputLayout, false, true);
        } catch (Throwable t) {
            throw new IllegalStateException(
                    "Unable to set the TextInputLayout hint (collapsed) color: "
                            + t.getLocalizedMessage(), t);
        }
    }

    /**
     * Tint an {@link TextInputLayout} by changing its label and focus
     * colors according to the supplied color.
     *
     * @param textInputLayout The TextInputLayout to be colorized.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull TextInputLayout textInputLayout, @ColorInt int color) {
        try {
            final Field mFocusedTextColorField =
                    TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            mFocusedTextColorField.setAccessible(true);
            mFocusedTextColorField.set(textInputLayout, ColorStateList.valueOf(color));
            final Method updateLabelStateMethod =
                    TextInputLayout.class.getDeclaredMethod(
                            "updateLabelState", boolean.class, boolean.class);
            updateLabelStateMethod.setAccessible(true);
            updateLabelStateMethod.invoke(textInputLayout, false, true);
        } catch (Throwable t) {
            throw new IllegalStateException(
                    "Unable to set the TextInputLayout hint (expanded) color: "
                            + t.getLocalizedMessage(), t);
        }

        if (textInputLayout.getEditText() != null) {
            setColor(textInputLayout.getEditText(), color);
        }
        setHint(textInputLayout, color);
    }

    /**
     * Show the soft input keyboard and focus it on the supplied
     * {@link EditText}.
     *
     * @param editText The EditText to show the soft input.
     */
    public static void showSoftInput(final @NonNull EditText editText) {
        editText.requestFocus();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        editText.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editText, 0);
                    editText.clearFocus();
                    editText.requestFocus();
                }
            }
        });
    }

    /**
     * Hide the soft input keyboard and remove focus from the supplied
     * {@link EditText}.
     *
     * @param editText The EditText to clear the focus.
     */
    public static void hideSoftInput(final @NonNull EditText editText) {
        editText.clearFocus();

        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
