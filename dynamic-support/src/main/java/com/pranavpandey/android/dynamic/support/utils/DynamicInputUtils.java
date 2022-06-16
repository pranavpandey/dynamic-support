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

package com.pranavpandey.android.dynamic.support.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsSeekBar;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Helper class to tint the various input widgets like {@link EditText}, etc. dynamically
 * by using reflection. It will be used to match the color with the app's theme.
 */
@RestrictTo(LIBRARY_GROUP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public final class DynamicInputUtils {

    /**
     * Set cursor color for the supplied {@link TextView}.
     *
     * @param textView The text view to set the cursor color.
     * @param color The color for the cursor.
     */
    @SuppressLint("PrivateApi")
    @TargetApi(Build.VERSION_CODES.Q)
    public static void setCursorColor(@NonNull TextView textView, @ColorInt int color) {
        if (DynamicSdkUtils.is29()) {
            DynamicDrawableUtils.colorizeDrawable(textView.getTextCursorDrawable(), color);
            DynamicDrawableUtils.colorizeDrawable(textView.getTextSelectHandle(), color);
            DynamicDrawableUtils.colorizeDrawable(textView.getTextSelectHandleLeft(), color);
            DynamicDrawableUtils.colorizeDrawable(textView.getTextSelectHandleRight(), color);
        } else {
            String textFields = Arrays.toString(TextView.class.getDeclaredFields());
            Object editor = null;

            try {
                if (textFields.contains("mEditor")) {
                    Field fEditor = TextView.class.getDeclaredField("mEditor");
                    fEditor.setAccessible(true);
                    editor = fEditor.get(textView);
                }
            } catch (Exception ignored) {
            }

            try {
                String[] drawableResFields = { "mTextSelectHandleLeftRes",
                        "mTextSelectHandleRightRes", "mTextSelectHandleRes" };
                String[] drawableFields = { "mSelectHandleLeft",
                        "mSelectHandleRight", "mSelectHandleCenter" };

                for (int i = 0; i < drawableResFields.length; i++) {
                    Field fSelectHandle = TextView.class.getDeclaredField(drawableResFields[i]);
                    fSelectHandle.setAccessible(true);
                    Drawable selectHandleDrawable = DynamicDrawableUtils.colorizeDrawable(
                            DynamicResourceUtils.getDrawable(textView.getContext(),
                                    fSelectHandle.getInt(textView)), color);

                    if (editor != null) {
                        fSelectHandle = editor.getClass().getDeclaredField(drawableFields[i]);
                        fSelectHandle.setAccessible(true);
                        fSelectHandle.set(editor, selectHandleDrawable);
                    } else {
                        fSelectHandle = TextView.class.getDeclaredField(drawableFields[i]);
                        fSelectHandle.setAccessible(true);
                        fSelectHandle.set(textView, selectHandleDrawable);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Field fCursor = null;
                Drawable cursorDrawable = null;

                if (textFields.contains("mCursorDrawableRes")) {
                    fCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
                    fCursor.setAccessible(true);
                    cursorDrawable = DynamicDrawableUtils.colorizeDrawable(
                            DynamicResourceUtils.getDrawable(textView.getContext(),
                                    fCursor.getInt(textView)), color);
                }

                if (editor != null) {
                    String editorFields = Arrays.toString(editor.getClass().getDeclaredFields());

                    if (editorFields.contains("mCursorDrawable")) {
                        fCursor = editor.getClass().getDeclaredField("mCursorDrawable");
                        fCursor.setAccessible(true);

                        if (cursorDrawable == null) {
                            cursorDrawable = DynamicDrawableUtils.colorizeDrawable(((Drawable[])
                                    Objects.requireNonNull(fCursor.get(editor)))[0], color);
                        }
                        fCursor.set(editor, new Drawable[] { cursorDrawable, cursorDrawable });
                    } else if (editorFields.contains("mDrawableForCursor")) {
                        fCursor = editor.getClass().getDeclaredField("mDrawableForCursor");
                        fCursor.setAccessible(true);

                        if (cursorDrawable == null) {
                            cursorDrawable = DynamicDrawableUtils.colorizeDrawable(
                                    (Drawable) fCursor.get(editor), color);
                        }
                        fCursor.set(editor, cursorDrawable);
                    }
                } else {
                    fCursor = TextView.class.getDeclaredField("mCursorDrawable");
                    fCursor.setAccessible(true);

                    if (cursorDrawable == null) {
                        cursorDrawable = DynamicDrawableUtils.colorizeDrawable(((Drawable[])
                                Objects.requireNonNull(fCursor.get(textView)))[0], color);
                    }
                    fCursor.set(textView, new Drawable[] { cursorDrawable, cursorDrawable });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tint the {@link EditText} by changing its cursor, hint, etc. colors according to the
     * supplied color.
     *
     * @param editText The edit text to be colorized.
     * @param background The background color to be used.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull EditText editText,
            @ColorInt int background, @ColorInt int color) {
        if (editText.getBackground() != null) {
            ViewCompat.setBackground(editText, DynamicDrawableUtils.colorizeDrawable(
                    editText.getBackground(), background));
        }

        setCursorColor(editText, color);
    }

    /**
     * Tint the {@link TextInputLayout} by changing its label and focus colors according to the
     * supplied color.
     *
     * @param textInputLayout The text input layout to be colorized.
     * @param color The color to be used.
     */
    public static void setColor(@NonNull TextInputLayout textInputLayout, @ColorInt int color) {
        try {
            Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("focusedTextColor");
            fFocusedTextColor.setAccessible(true);
            fFocusedTextColor.set(textInputLayout, ColorStateList.valueOf(color));
            Method mUpdateLabelState = TextInputLayout.class.getDeclaredMethod(
                    "updateLabelState", boolean.class, boolean.class);
            mUpdateLabelState.setAccessible(true);
            mUpdateLabelState.invoke(textInputLayout, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (textInputLayout.getEditText() instanceof DynamicWidget) {
            ((DynamicWidget) textInputLayout.getEditText()).setColor();
        } else if (textInputLayout.getEditText() != null) {
            setColor(textInputLayout.getEditText(), textInputLayout.getBoxBackgroundColor(), color);
            textInputLayout.setHintTextColor(textInputLayout.getEditText().getHintTextColors());
        }
    }

    /**
     * Tint the {@link AbsSeekBar} thumb drawable according to the supplied color.
     *
     * @param seekBar The seek bar to be colorized.
     * @param color The color to be used.
     */
    @SuppressLint("PrivateApi")
    public static void setColor(@NonNull AbsSeekBar seekBar, @ColorInt int color) {
        try {
            Field fThumb = AbsSeekBar.class.getDeclaredField("mThumb");
            fThumb.setAccessible(true);
            fThumb.set(seekBar, DynamicDrawableUtils.colorizeDrawable(
                    (Drawable) fThumb.get(seekBar), color));
        } catch (Exception e) {
            e.printStackTrace();
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
                InputMethodManager inputMethodManager = ContextCompat.getSystemService(
                        editText.getContext(), InputMethodManager.class);
                if (inputMethodManager == null) {
                    return;
                }

                inputMethodManager.showSoftInput(editText, 0);
                editText.clearFocus();
                editText.requestFocus();
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

        InputMethodManager inputMethodManager = ContextCompat.getSystemService(
                editText.getContext(), InputMethodManager.class);
        if (inputMethodManager == null) {
            return;
        }

        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
