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

import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;

/**
 * Helper class to perform {@link DynamicDialog} operations.
 */
public class DynamicDialogUtils {

    /**
     * Set custom view to a dialog with top padding to maintain uniform
     * layout on all Android versions.
     *
     * @param alertDialog AlertDialog to set the custom view.
     * @param view View to be used as the custom view.
     */
    public static DynamicDialog setView(@NonNull DynamicDialog alertDialog,
                                        @NonNull View view) {
        alertDialog.setView(view, 0, getDialogTopPadding(
                alertDialog.getContext()), 0, 0);

        return alertDialog;
    }

    /**
     * Get top padding of the dialog to maintain uniform layout on all
     * Android versions. Generally, it is required in case of custom view
     * in the dialog.
     *
     * @param context Context to get the resources.
     *
     * @return Top padding of the dialog.
     */
    public static int getDialogTopPadding(@NonNull Context context) {
        return (int) (14 * context.getResources().getDisplayMetrics().density);
    }

    /**
     * Bind the dialog with a window token. Useful to display it
     * from a service.
     *
     * @param view View to extract the window token.
     * @param dialog Dialog to be displayed.
     * @param type The dialog type.
     *
     * @return The bound dialog with the supplied view.
     */
    public static Dialog bindDialog(@NonNull View view, @NonNull Dialog dialog, int type) {
        IBinder windowToken = view.getWindowToken();
        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.token = windowToken;
            lp.windowAnimations = android.R.style.Animation_InputMethod;
            lp.type = type;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        return dialog;
    }
}
