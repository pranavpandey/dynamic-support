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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A FrameLayout with basic functionality to create views according to the need.
 */
public abstract class DynamicView extends FrameLayout {

    public DynamicView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The supplied attribute set to load the values.
     */
    private void loadFromAttributes(@Nullable AttributeSet attrs) {
        onLoadAttributes(attrs);
        onInflate();
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The attribute set to load the values.
     */
    protected void onLoadAttributes(@Nullable AttributeSet attrs) { };

    /**
     * This method will be called to get the layout resource for this view.
     * <p>Supply the view layout resource here to do the inflation.
     *
     * @return The layout resource for this view.
     */
    protected abstract @LayoutRes int getLayoutRes();

    /**
     * This method will be called after loading the attributed.
     * <p>Initialize the view layout here.
     */
    protected abstract void onInflate();

    /**
     * This method will be called whenever there is a change in the view attributes or parameters.
     * <p>It is better to do any real time calculation like updating the value string or checked
     * state in this method.
     */
    protected abstract void onUpdate();

    /**
     * This method will be called whenever there is a change in the view state.
     * <p>Either {@code enabled} or {@code disabled}, other views like icon, title, color, etc.
     * must be updated here to reflect the overall view state.
     *
     * @param enabled {@code true} if this view is enabled and can receive click events.
     */
    protected void onEnabled(boolean enabled) { };

    /**
     * Set this view enabled or disabled.
     *
     * @param enabled {@code true} if this view is enabled.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        onEnabled(enabled);
    }
}
