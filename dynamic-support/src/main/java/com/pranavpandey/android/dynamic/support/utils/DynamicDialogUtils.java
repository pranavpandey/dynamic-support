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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;

/**
 * Helper class to perform {@link DynamicDialog} operations.
 */
public class DynamicDialogUtils {

    /**
     * Set custom view to a dialog with top padding to maintain uniform layout on all
     * Android versions.
     *
     * @param alertDialog The alert dialog to set the custom view.
     * @param view The view to be used as the custom view.
     *
     * @return The alert dialog after setting the custom view.
     */
    public static DynamicDialog setView(@NonNull DynamicDialog alertDialog, @NonNull View view) {
        alertDialog.setView(view, 0, getDialogTopPadding(
                alertDialog.getContext()), 0, 0);

        return alertDialog;
    }

    /**
     * Get top padding of the dialog to maintain uniform layout on all Android versions.
     * <p>Generally, it is required in case of custom view in the dialog.
     *
     * @param context The context to get the resources.
     *
     * @return The top padding of the dialog.
     */
    public static int getDialogTopPadding(@NonNull Context context) {
        return (int) (14 * context.getResources().getDisplayMetrics().density);
    }

    /**
     * Bind the dialog with a window token.
     * <p>Useful to display it from a service.
     *
     * @param view The view to bind the dialog.
     * @param dialog The dialog to be displayed.
     * @param type The dialog type.
     * @param windowAnimations The custom animation used for the window.
     *
     * @return The bound dialog with the supplied view.
     */
    public static Dialog bindDialog(@Nullable View view,
            @NonNull Dialog dialog, int type, @StyleRes int windowAnimations) {
        Window window = dialog.getWindow();

        if (window != null) {
            if (view != null && view.getWindowToken() != null) {
                window.getAttributes().token = view.getWindowToken();
            }

            window.setType(type);
            window.setWindowAnimations(windowAnimations);
            window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        return dialog;
    }

    /**
     * Bind the dialog with a window token. Useful to display it from a service.
     *
     * @param view The view to bind the dialog.
     * @param dialog The dialog to be displayed.
     * @param type The dialog type.
     *
     * @return The bound dialog with the supplied view.
     */
    public static Dialog bindDialog(@Nullable View view, @NonNull Dialog dialog, int type) {
        return bindDialog(view, dialog, type, android.R.style.Animation_InputMethod);
    }
}
