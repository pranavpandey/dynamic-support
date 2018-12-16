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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Helper class to tint the various input widgets like {@link EditText}, etc. dynamically
 * by using reflection. It will be used to match the color with the app's theme.
 */
@RestrictTo(LIBRARY_GROUP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public final class DynamicInputUtils {

    /**
     * Set cursor color for the supplied {@link EditText}.
     *
     * @param editText The edit text to set the cursor color.
     * @param color The color for the cursor.
     */
    private static void setCursorColor(final @NonNull EditText editText, @ColorInt int color) {
        @ColorInt int hintColor = DynamicColorUtils.adjustAlpha(
                color, WidgetDefaults.ADS_ALPHA_HINT);
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

            if (Arrays.toString(clazz.getDeclaredFields()).contains("mCursorDrawable")) {
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
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Tint an {@link EditText} by changing its cursor, hint, etc. colors according to the
     * supplied color.
     *
     * @param editText The edit text to be colorized.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull EditText editText, @ColorInt int color) {
        ColorStateList editTextColorStateList = DynamicResourceUtils.getColorStateList(color);

        if (editText instanceof TintableBackgroundView) {
            ViewCompat.setBackgroundTintList(editText, editTextColorStateList);
        } else if (DynamicVersionUtils.isLollipop()) {
            editText.setBackgroundTintList(editTextColorStateList);
        }

        setCursorColor(editText, color);
    }

    /**
     * Set hint color for the supplied {@link TextInputLayout}.
     *
     * @param textInputLayout The text input layout to set the hint color.
     * @param color The color to be used.
     */
    public static void setHint(@NonNull TextInputLayout textInputLayout, @ColorInt int color) {
        try {
            final Field defaultHintTextColorField =
                    TextInputLayout.class.getDeclaredField("defaultHintTextColor");
            defaultHintTextColorField.setAccessible(true);
            defaultHintTextColorField.set(textInputLayout, ColorStateList.valueOf(color));
            final Method updateLabelStateMethod =
                    TextInputLayout.class.getDeclaredMethod(
                            "updateLabelState", boolean.class, boolean.class);
            updateLabelStateMethod.setAccessible(true);
            updateLabelStateMethod.invoke(textInputLayout, false, true);
        } catch (Exception ignored) {
        }
    }

    /**
     * Tint an {@link TextInputLayout} by changing its label and focus colors according to the
     * supplied color.
     *
     * @param textInputLayout The text input layout to be colorized.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull TextInputLayout textInputLayout, @ColorInt int color) {
        try {
            Field focusedTextColorField =
                    TextInputLayout.class.getDeclaredField("focusedTextColor");
            focusedTextColorField.setAccessible(true);
            focusedTextColorField.set(textInputLayout, ColorStateList.valueOf(color));
            Method updateLabelStateMethod = TextInputLayout.class.getDeclaredMethod(
                    "updateLabelState", boolean.class, boolean.class);
            updateLabelStateMethod.setAccessible(true);
            updateLabelStateMethod.invoke(textInputLayout, false, true);
        } catch (Exception ignored) {
        }

        if (textInputLayout.getEditText() != null) {
            setColor(textInputLayout.getEditText(), color);
        }
        setHint(textInputLayout, color);
    }

    /**
     * Show the soft input keyboard and focus it on the supplied {@link EditText}.
     *
     * @param editText The edit text to show the soft input.
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
     * Hide the soft input keyboard and remove focus from the supplied {@link EditText}.
     *
     * @param editText The edit text to clear the focus.
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
