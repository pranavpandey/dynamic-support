/*
 * Copyright 2018-2021 Pranav Pandey
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
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;

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
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        onLoadAttributes(attrs);
        onInflate();
    }

    /**
     * Load values from the supplied attribute set.
     *
     * @param attrs The attribute set to load the values.
     */
    protected void onLoadAttributes(@Nullable AttributeSet attrs) { }

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
     * Runnable to post the update.
     */
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            onUpdate();
        }
    };

    /**
     * Manually update this view by calling {@link #onUpdate()} method.
     * <p>Useful in some situations to restore the view state.
     */
    public void update() {
        onUpdate();
    }

    /**
     * Manually update this view by calling {@link #onUpdate()} method via a {@link Runnable}.
     * <p>Useful in some situations to restore the view state on main thread.
     */
    public void postUpdate() {
        post(mUpdateRunnable);
    }

    /**
     * This method will be called whenever there is a change in the view state.
     * <p>Either {@code enabled} or {@code disabled}, other views like icon, title, color, etc.
     * must be updated here to reflect the overall view state.
     *
     * @param enabled {@code true} if this view is enabled and can receive click events.
     */
    protected void onEnabled(boolean enabled) { }

    /**
     * This method will be called to return the optional background view.
     * <p>It will be useful in tinting the background according to the contrast with color.
     *
     * @return The optional background view.
     */
    public abstract @Nullable View getBackgroundView();

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        onEnabled(enabled);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);

        Dynamic.setClickable(getBackgroundView(), clickable);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);

        Dynamic.setOnClickListener(getBackgroundView(), l);
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(longClickable);

        Dynamic.setClickable(getBackgroundView(), longClickable);
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        super.setOnLongClickListener(l);

        Dynamic.setOnLongClickListener(getBackgroundView(), l);
    }
}
