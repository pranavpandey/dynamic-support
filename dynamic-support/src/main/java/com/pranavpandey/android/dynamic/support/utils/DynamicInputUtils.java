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
import androidx.core.view.ViewCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

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
    @TargetApi(Build.VERSION_CODES.Q)
    public static void setCursorColor(final @NonNull EditText editText, @ColorInt int color) {
        @ColorInt int hintColor = DynamicColorUtils.adjustAlpha(
                color, WidgetDefaults.ADS_ALPHA_HINT);
        editText.setHintTextColor(hintColor);
        editText.setHighlightColor(DynamicColorUtils.getContrastColor(
                hintColor, editText.getCurrentTextColor()));

        if (DynamicSdkUtils.is29()) {
            DynamicDrawableUtils.colorizeDrawable(editText.getTextCursorDrawable(), color);
            DynamicDrawableUtils.colorizeDrawable(editText.getTextSelectHandle(), color);
            DynamicDrawableUtils.colorizeDrawable(editText.getTextSelectHandleLeft(), color);
            DynamicDrawableUtils.colorizeDrawable(editText.getTextSelectHandleRight(), color);
        } else {

            try {
                Field editorField = TextView.class.getDeclaredField("mEditor");
                editorField.setAccessible(true);
                Object editor = editorField.get(editText);

                String[] drawableResFields = { "mTextSelectHandleLeftRes",
                        "mTextSelectHandleRightRes", "mTextSelectHandleRes" };
                String[] drawableFields = { "mSelectHandleLeft",
                        "mSelectHandleRight", "mSelectHandleCenter" };

                for (int i = 0; i < drawableResFields.length; i++) {
                    Field selectHandleField = TextView.class.getDeclaredField(
                            drawableResFields[i]);
                    selectHandleField.setAccessible(true);

                    Drawable selectHandleDrawable = DynamicDrawableUtils.colorizeDrawable(
                            DynamicResourceUtils.getDrawable(editText.getContext(),
                                    selectHandleField.getInt(editText)), color);

                    if (editor != null) {
                        selectHandleField = editor.getClass().getDeclaredField(drawableFields[i]);
                        selectHandleField.setAccessible(true);
                        selectHandleField.set(editor, selectHandleDrawable);
                    }
                }

                if (editor != null) {
                    Field cursorResField = TextView.class
                            .getDeclaredField("mCursorDrawableRes");
                    cursorResField.setAccessible(true);

                    Drawable cursorDrawable = DynamicDrawableUtils.colorizeDrawable(
                            DynamicResourceUtils.getDrawable(editText.getContext(),
                                    cursorResField.getInt(editText)), color);

                    String editorFields = Arrays.toString(editor.getClass().getDeclaredFields());
                    if (editorFields.contains("mCursorDrawable")) {
                        Field cursorDrawableField = editor.getClass()
                                .getDeclaredField("mCursorDrawable");
                        cursorDrawableField.setAccessible(true);
                        cursorDrawableField.set(editor,
                                new Drawable[] { cursorDrawable, cursorDrawable });
                    } else if (editorFields.contains("mDrawableForCursor")) {
                        Field cursorDrawableField = editor.getClass()
                                .getDeclaredField("mDrawableForCursor");
                        cursorDrawableField.setAccessible(true);
                        cursorDrawableField.set(editor, cursorDrawable);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        ViewCompat.setBackgroundTintList(editText, editTextColorStateList);
        setCursorColor(editText, color);
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
            textInputLayout.setHintTextColor(textInputLayout.getEditText().getHintTextColors());
        }
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
