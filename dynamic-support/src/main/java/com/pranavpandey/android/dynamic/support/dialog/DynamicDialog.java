/*
 * Copyright 2018-2022 Pranav Pandey
 * Copyright 2015 The Android Open Source Project
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

package com.pranavpandey.android.dynamic.support.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;

import com.pranavpandey.android.dynamic.support.R;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * A subclass of Dialog that can display one, two or three buttons. If you only want to
 * display a String in this dialog box, use the setMessage() method.  If you
 * want to display a more complex view, look up the FrameLayout called "custom"
 * and add your view to it:
 *
 * <pre>
 * FrameLayout fl = findViewById(android.R.id.custom);
 * fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
 * </pre>
 *
 * <p>The DynamicDialog class takes care of automatically setting
 * {@link android.view.WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM
 * android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} for you based on whether
 * any views in the dialog return true from {@link View#onCheckIsTextEditor()
 * View.onCheckIsTextEditor()}.  Generally you want this set for a Dialog
 * without text editors, so that it will be placed on top of the current
 * input method UI.  You can modify this behavior by forcing the flag to your
 * desired mode after calling {@link #onCreate}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating dialogs, read the
 * <a href="{@docRoot}guide/topics/ui/dialogs.html">Dialogs</a> developer guide.</p>
 * </div>
 */
public class DynamicDialog extends AppCompatDialog implements DialogInterface {

    final DynamicAlertController mAlert;

    /**
     * No layout hint.
     */
    static final int LAYOUT_HINT_NONE = 0;

    /**
     * Hint layout to the side.
     */
    static final int LAYOUT_HINT_SIDE = 1;

    protected DynamicDialog(@NonNull Context context) {
        this(context, 0);
    }

    /**
     * Construct an DynamicDialog that uses an explicit theme. The actual style
     * that an DynamicDialog uses is a private implementation, however you can
     * here supply either the name of an attribute in the theme from which
     * to get the dialog's style (such as {@link androidx.appcompat.R.attr#alertDialogTheme}.
     *
     * @param context The context for the dialog.
     * @param themeResId The theme resource id fo the dialog.
     */
    protected DynamicDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        mAlert = new DynamicAlertController(getContext(), this, getWindow());
    }

    /**
     * Construct an DynamicDialog that uses an explicit theme. The actual style
     * that an DynamicDialog uses is a private implementation, however you can
     * here supply either the name of an attribute in the theme from which
     * to get the dialog's style (such as {@link androidx.appcompat.R.attr#alertDialogTheme}.
     *
     * @param context The context for the dialog.
     * @param cancelable {@code true} to set the dialog cancelable.
     * @param cancelListener The cancel listener to be set.
     */
    protected DynamicDialog(@NonNull Context context, boolean cancelable,
            @Nullable OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
        // Check to see if this resourceId has a valid package ID.
        if (((resid >>> 24) & 0x000000ff) >= 0x00000001) {   // start of real resource IDs.
            return resid;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    /**
     * Gets one of the buttons used in the dialog. Returns null if the specified
     * button does not exist or the dialog has not yet been fully created (for
     * example, via {@link #show()} or {@link #create()}).
     *
     * @param whichButton The identifier of the button that should be returned.
     *                    For example, this can be
     *                    {@link DialogInterface#BUTTON_POSITIVE}.
     *
     * @return The button from the dialog, or null if a button does not exist.
     */
    public Button getButton(int whichButton) {
        return mAlert.getButton(whichButton);
    }

    /**
     * Gets the list view used in the dialog.
     *
     * @return The {@link ListView} from the dialog.
     */
    public ListView getListView() {
        return mAlert.getListView();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mAlert.setTitle(title);
    }

    /**
     * This method has no effect if called after {@link #show()}.
     *
     * @param customTitleView The custom title view for the dialog.
     *
     * @see Builder#setCustomTitle(View)
     */
    public void setCustomTitle(@Nullable View customTitleView) {
        mAlert.setCustomTitle(customTitleView);
    }

    /**
     * Sets the message to display.
     *
     * @param message The message to display in the dialog.
     */
    public void setMessage(CharSequence message) {
        mAlert.setMessage(message);
    }

    /**
     * Get the view displayed in the dialog.
     */
    public @Nullable View getView() {
        return mAlert.getView();
    }

    /**
     * Set the view to display in the dialog. This method has no effect if called
     * after {@link #show()}.
     *
     * @param view The view to show in the content area of the dialog.
     */
    public void setView(@Nullable View view) {
        mAlert.setView(view);
    }

    /**
     * Set the view to display in the dialog, specifying the spacing to appear around that
     * view.  This method has no effect if called after {@link #show()}.
     *
     * @param view              The view to show in the content area of the dialog
     * @param viewSpacingLeft   Extra space to appear to the left of {@code view}
     * @param viewSpacingTop    Extra space to appear above {@code view}
     * @param viewSpacingRight  Extra space to appear to the right of {@code view}
     * @param viewSpacingBottom Extra space to appear below {@code view}
     */
    public void setView(@Nullable View view, int viewSpacingLeft, int viewSpacingTop,
            int viewSpacingRight, int viewSpacingBottom) {
        mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /**
     * Set the view root to add scroll indicators if the content can be scrolled.
     * <p>This method has no effect if called after {@link #show()}.
     *
     * @param viewRoot The view root to be set.
     */
    public void setViewRoot(@Nullable View viewRoot) {
        mAlert.setViewRoot(viewRoot);
    }

    /**
     * Internal api to allow hinting for the best button panel layout.
     */
    @RestrictTo(LIBRARY_GROUP)
    void setButtonPanelLayoutHint(int layoutHint) {
        mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    /**
     * Sets a message to be sent when a button is pressed. This method has no effect if called
     * after {@link #show()}.
     *
     * @param whichButton Which button to set the message for, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param msg         The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, CharSequence text, Message msg) {
        mAlert.setButton(whichButton, text, null, msg, null);
    }

    /**
     * Sets a listener to be invoked when the positive button of the dialog is pressed. This method
     * has no effect if called after {@link #show()}.
     *
     * @param whichButton Which button to set the listener on, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param listener    The {@link DialogInterface.OnClickListener} to use.
     */
    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null, null);
    }

    /**
     * Sets an icon to be displayed along with the button text and a listener to be invoked when
     * the positive button of the dialog is pressed. This method has no effect if called after
     * {@link #show()}.
     *
     * @param whichButton Which button to set the listener on, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param listener    The {@link DialogInterface.OnClickListener} to use.
     * @param icon        The {@link Drawable} to be set as an icon for the button.
     */
    public void setButton(int whichButton, CharSequence text, Drawable icon,
            OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null,  icon);
    }

    /**
     * Set resId to 0 if you don't want an icon.
     * @param resId the resourceId of the drawable to use as the icon or 0
     * if you don't want an icon.
     */
    public void setIcon(int resId) {
        mAlert.setIcon(resId);
    }

    /**
     * Set the {@link Drawable} to be used in the title.
     *
     * @param icon Drawable to use as the icon or null if you don't want an icon.
     */
    public void setIcon(Drawable icon) {
        mAlert.setIcon(icon);
    }

    /**
     * Sets an icon as supplied by a theme attribute. e.g. android.R.attr.alertDialogIcon
     *
     * @param attrId ID of a theme attribute that points to a drawable resource.
     */
    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        mAlert.setIcon(out.resourceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlert.installContent();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private final DynamicAlertController.AlertParams P;
        private final int mTheme;

        /**
         * Copy constructor to create a builder for another builder.
         * <p>It will use the supplied builder's theme id available otherwise the default alert
         * dialog theme is defined by {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         * @param builder the builder to copy the parameters.
         */
        public Builder(@NonNull Context context, @Nullable Builder builder) {
            this(context, resolveDialogTheme(context, builder != null ? builder.mTheme : 0));

            if (builder != null) {
                P.mIconId = builder.P.mIconId;
                P.mIcon = builder.P.mIcon;
                P.mTitle = builder.P.mTitle;
                P.mCustomTitleView = builder.P.mCustomTitleView;
                P.mMessage = builder.P.mMessage;
                P.mPositiveButtonText = builder.P.mPositiveButtonText;
                P.mPositiveButtonIcon = builder.P.mPositiveButtonIcon;
                P.mPositiveButtonListener = builder.P.mPositiveButtonListener;
                P.mNegativeButtonText = builder.P.mNegativeButtonText;
                P.mNegativeButtonIcon = builder.P.mNegativeButtonIcon;
                P.mNegativeButtonListener = builder.P.mNegativeButtonListener;
                P.mNeutralButtonText = builder.P.mNeutralButtonText;
                P.mNeutralButtonIcon = builder.P.mNeutralButtonIcon;
                P.mNeutralButtonListener = builder.P.mNeutralButtonListener;
                P.mCancelable = builder.P.mCancelable;
                P.mOnCancelListener = builder.P.mOnCancelListener;
                P.mOnDismissListener = builder.P.mOnDismissListener;
                P.mOnKeyListener = builder.P.mOnKeyListener;
                P.mItems = builder.P.mItems;
                P.mAdapter = builder.P.mAdapter;
                P.mOnClickListener = builder.P.mOnClickListener;
                P.mViewLayoutResId = builder.P.mViewLayoutResId;
                P.mView = builder.P.mView;
                P.mViewRootId = builder.P.mViewRootId;
                P.mViewRoot = builder.P.mViewRoot;
                P.mViewSpacingLeft = builder.P.mViewSpacingLeft;
                P.mViewSpacingTop = builder.P.mViewSpacingTop;
                P.mViewSpacingRight = builder.P.mViewSpacingRight;
                P.mViewSpacingBottom = builder.P.mViewSpacingBottom;
                P.mViewSpacingSpecified = builder.P.mViewSpacingSpecified;
                P.mCheckedItems = builder.P.mCheckedItems;
                P.mIsMultiChoice = builder.P.mIsMultiChoice;
                P.mIsSingleChoice = builder.P.mIsSingleChoice;
                P.mCheckedItem = builder.P.mCheckedItem;
                P.mOnCheckboxClickListener = builder.P.mOnCheckboxClickListener;
                P.mCursor = builder.P.mCursor;
                P.mLabelColumn = builder.P.mLabelColumn;
                P.mIsCheckedColumn = builder.P.mIsCheckedColumn;
                P.mForceInverseBackground = builder.P.mForceInverseBackground;
                P.mOnItemSelectedListener = builder.P.mOnItemSelectedListener;
                P.mOnPrepareListViewListener = builder.P.mOnPrepareListViewListener;
                P.mRecycleOnMeasure = builder.P.mRecycleOnMeasure;

                builder = null;
            }
        }

        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p>The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         */
        public Builder(@NonNull Context context) {
            this(context, resolveDialogTheme(context, 0));
        }

        /**
         * Creates a builder for an alert dialog that uses an explicit theme
         * resource.
         * <p>The specified theme resource ({@code themeResId}) is applied on top
         * of the parent {@code context}'s theme. It may be specified as a
         * style resource containing a fully-populated theme, such as
         * {@link androidx.appcompat.R.style#Theme_AppCompat_Dialog}, to replace all
         * attributes in the parent {@code context}'s theme including primary
         * and accent colors.
         * <p>To preserve attributes such as primary and accent colors, the
         * {@code themeResId} may instead be specified as an overlay theme such
         * as {@link androidx.appcompat.R.style#ThemeOverlay_AppCompat_Dialog}. This will
         * override only the window attributes necessary to style the alert
         * window as a dialog.
         * <p>Alternatively, the {@code themeResId} may be specified as {@code 0}
         * to use the parent {@code context}'s resolved value for
         * {@link android.R.attr#alertDialogTheme}.
         *
         * @param context the parent context
         * @param themeResId the resource ID of the theme against which to inflate
         *                   this dialog, or {@code 0} to use the parent
         *                   {@code context}'s default alert dialog theme
         */
        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            P = new DynamicAlertController.AlertParams(new ContextThemeWrapper(
                    context, resolveDialogTheme(context, themeResId)));
            mTheme = themeResId;
        }

        /**
         * Returns a {@link Context} with the appropriate theme for dialogs created by this Builder.
         * Applications should use this Context for obtaining LayoutInflaters for inflating views
         * that will be used in the resulting dialogs, as it will cause views to be inflated with
         * the correct theme.
         *
         * @return A Context for built Dialogs.
         */
        @NonNull
        public Context getContext() {
            return P.mContext;
        }

        /**
         * Set the title using the given resource id.
         *
         * @param titleId The resource id to display the title.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTitle(@StringRes int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @param title The title to be displayed.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTitle(@Nullable CharSequence title) {
            P.mTitle = title;
            return this;
        }

        /**
         * Set the title using the custom view {@code customTitleView}.
         * <p>The methods {@link #setTitle(int)} and {@link #setIcon(int)} should
         * be sufficient for most titles, but this is provided if the title
         * needs more customization. Using this will replace the title and icon
         * set via the other methods.
         * <p><strong>Note:</strong> To ensure consistent styling, the custom view
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param customTitleView the custom view to use as the title.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setCustomTitle(@Nullable View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @param messageId The resource id to display message.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setMessage(@StringRes int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @param message The message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setMessage(@Nullable CharSequence message) {
            P.mMessage = message;
            return this;
        }

        /**
         * Set the resource id of the {@link Drawable} to be used in the title.
         * <p>Takes precedence over values set using {@link #setIcon(Drawable)}.
         *
         * @param iconId The resource id to display the drawable.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setIcon(@DrawableRes int iconId) {
            P.mIconId = iconId;
            return this;
        }

        /**
         * Set the {@link Drawable} to be used in the title.
         * <p><strong>Note:</strong> To ensure consistent styling, the drawable
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param icon The drawable to be used in the title.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setIcon(@Nullable Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        /**
         * Set an icon as supplied by a theme attribute. e.g.
         * {@link android.R.attr#alertDialogIcon}.
         * <p>Takes precedence over values set using {@link #setIcon(int)} or
         * {@link #setIcon(Drawable)}.
         *
         * @param attrId ID of a theme attribute that points to a drawable resource.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setIconAttribute(@AttrRes int attrId) {
            TypedValue out = new TypedValue();
            P.mContext.getTheme().resolveAttribute(attrId, out, true);
            P.mIconId = out.resourceId;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId The resource id of the text to display in the positive button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setPositiveButton(@StringRes int textId,
                @Nullable final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text The text to display in the positive button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setPositiveButton(@Nullable CharSequence text,
                @Nullable final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set an icon to be displayed for the positive button.
         *
         * @param icon The icon to be displayed.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setPositiveButtonIcon(@Nullable Drawable icon) {
            P.mPositiveButtonIcon = icon;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId The resource id of the text to display in the negative button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNegativeButton(@StringRes int textId,
                @Nullable final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text The text to display in the negative button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNegativeButton(@Nullable CharSequence text,
                @Nullable final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set an icon to be displayed for the negative button.
         *
         * @param icon The icon to be displayed.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNegativeButtonIcon(@Nullable Drawable icon) {
            P.mNegativeButtonIcon = icon;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId The resource id of the text to display in the neutral button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNeutralButton(@StringRes int textId,
                @Nullable final OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text The text to display in the neutral button.
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNeutralButton(@Nullable CharSequence text,
                @Nullable final OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set an icon to be displayed for the neutral button.
         *
         * @param icon The icon to be displayed.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNeutralButtonIcon(@Nullable Drawable icon) {
            P.mNeutralButtonIcon = icon;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not. Default is true.
         *
         * @param cancelable {@code true} to set the dialog cancelable.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * setOnDismissListener}.</p>
         *
         * @param onCancelListener The callback to be set.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         *
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public Builder setOnCancelListener(@Nullable OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @param onDismissListener The callback to be set.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @param onKeyListener The callback to be set.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setOnKeyListener(@Nullable OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be
         * notified of the selected item via the supplied listener. This should be an array
         * type i.e. R.array.foo
         *
         * @param itemsId The resource id for the items array.
         * @param listener The click listener to be set.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setItems(@ArrayRes int itemsId, @Nullable final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param items The items array to be displayed.
         * @param listener The click listener to be set.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setItems(@Nullable CharSequence[] items,
                @Nullable final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter The {@link ListAdapter} to supply the list of items.
         * @param listener The listener that will be called when an item is clicked.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setAdapter(@Nullable final ListAdapter adapter,
                @Nullable final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link Cursor}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param cursor The {@link Cursor} to supply the list of items.
         * @param listener The listener that will be called when an item is clicked.
         * @param labelColumn The column name on the cursor containing the string to display
         *          in the label.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setCursor(@Nullable final Cursor cursor,
                @Nullable final OnClickListener listener, @Nullable String labelColumn) {
            P.mCursor = cursor;
            P.mLabelColumn = labelColumn;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * This should be an array type, e.g. R.array.foo. The list will have
         * a check mark displayed to the right of the text for each checked
         * item. Clicking on an item in the list will not dismiss the dialog.
         * Clicking on a button will dismiss the dialog.
         *
         * @param itemsId the resource id of an array i.e. R.array.foo
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *        items are checked. If non null it must be exactly the same length as the array of
         *        items.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setMultiChoiceItems(@ArrayRes int itemsId, @Nullable boolean[] checkedItems,
                @Nullable final OnMultiChoiceClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items the text of the items to be displayed in the list.
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *        items are checked. If non null it must be exactly the same length as the array of
         *        items.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setMultiChoiceItems(@Nullable CharSequence[] items,
                @Nullable boolean[] checkedItems,
                @Nullable final OnMultiChoiceClickListener listener) {
            P.mItems = items;
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor the cursor used to provide the items.
         * @param isCheckedColumn specifies the column name on the cursor to use to determine
         *        whether a checkbox is checked or not. It must return an integer value where 1
         *        means checked and 0 means unchecked.
         * @param labelColumn The column name on the cursor containing the string to display in the
         *        label.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setMultiChoiceItems(@Nullable Cursor cursor,
                @Nullable String isCheckedColumn, @Nullable String labelColumn,
                @Nullable final OnMultiChoiceClickListener listener) {
            P.mCursor = cursor;
            P.mOnCheckboxClickListener = listener;
            P.mIsCheckedColumn = isCheckedColumn;
            P.mLabelColumn = labelColumn;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. This should be an array type i.e.
         * R.array.foo The list will have a check mark displayed to the right of the text for the
         * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
         * button will dismiss the dialog.
         *
         * @param itemsId the resource id of an array i.e. R.array.foo
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem,
                @Nullable final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor the cursor to retrieve the items from.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param labelColumn The column name on the cursor containing the string to display in the
         *        label.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setSingleChoiceItems(@Nullable Cursor cursor, int checkedItem,
                @Nullable String labelColumn, @Nullable final OnClickListener listener) {
            P.mCursor = cursor;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mLabelColumn = labelColumn;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items the items to be displayed.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setSingleChoiceItems(@Nullable CharSequence[] items, int checkedItem,
                @Nullable final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter The {@link ListAdapter} to supply the list of items.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener notified when an item on the list is clicked. The dialog will not be
         *        dismissed when an item is clicked. It will only be dismissed if clicked on a
         *        button, if no buttons are supplied it's up to the user to dismiss the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setSingleChoiceItems(@Nullable ListAdapter adapter, int checkedItem,
                @Nullable final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Sets a listener to be invoked when an item in the list is selected.
         *
         * @param listener the listener to be invoked.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @see AdapterView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
         */
        public Builder setOnItemSelectedListener(
                @Nullable final AdapterView.OnItemSelectedListener listener) {
            P.mOnItemSelectedListener = listener;
            return this;
        }

        /**
         * Set a custom view resource to be the contents of the Dialog. The
         * resource will be inflated, adding all top-level views to the screen.
         *
         * @param layoutResId Resource ID to be inflated.
         * @return this Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setView(@LayoutRes int layoutResId) {
            P.mView = null;
            P.mViewLayoutResId = layoutResId;
            P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * Sets a custom view to be the contents of the alert dialog.
         * <p>When using a pre-Holo theme, if the supplied view is an instance of
         * a {@link ListView} then the light background will be used.
         * <p><strong>Note:</strong> To ensure consistent styling, the custom view
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param view the view to use as the contents of the alert dialog
         * @return this Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setView(@Nullable View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog, specifying the
         * spacing to appear around that view. If the supplied view is an
         * instance of a {@link ListView} the light background will be used.
         *
         * <p>This is currently hidden because it seems like people should just be able to
         * put padding around the view.
         *
         * @param view              The view to use as the contents of the Dialog.
         * @param viewSpacingLeft   Spacing between the left edge of the view and
         *                          the dialog frame
         * @param viewSpacingTop    Spacing between the top edge of the view and
         *                          the dialog frame
         * @param viewSpacingRight  Spacing between the right edge of the view
         *                          and the dialog frame
         * @param viewSpacingBottom Spacing between the bottom edge of the view
         *                          and the dialog frame
         * @return This Builder object to allow for chaining of calls to set
         * methods
         */
        @RestrictTo(LIBRARY_GROUP)
        @Deprecated
        private Builder setView(@Nullable View view, int viewSpacingLeft, int viewSpacingTop,
                int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        /**
         * Set the view root id to add scroll indicators if the content can be scrolled.
         *
         * @param viewRootId The root view id of the custom view.
         *
         * @return this Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setViewRoot(@IdRes int viewRootId) {
            P.mViewRoot = null;
            P.mViewRootId = viewRootId;
            return this;
        }

        /**
         * Set the view root to add scroll indicators if the content can be scrolled.
         *
         * @param viewRoot The root view of the custom view.
         *
         * @return this Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setViewRoot(@Nullable View viewRoot) {
            P.mViewRoot = viewRoot;
            P.mViewRootId = 0;
            return this;
        }

        /**
         * Sets the Dialog to use the inverse background, regardless of what the
         * contents is.
         *
         * @param useInverseBackground Whether to use the inverse background
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         *
         * @deprecated This flag is only used for pre-Material themes. Instead,
         *             specify the window background using on the alert dialog
         *             theme.
         */
        @Deprecated
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        /**
         * Set whether to enable recycle on enabled fo the dialog.
         *
         * @param enabled {@code true} to enable the recycle on measure.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        @RestrictTo(LIBRARY_GROUP)
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            P.mRecycleOnMeasure = enabled;
            return this;
        }

        /**
         * Creates an {@link DynamicDialog} with the arguments supplied to this
         * builder.
         * <p>Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         *
         * @return The {@link DynamicDialog} with the arguments supplied to this builder.
         */
        @NonNull
        public DynamicDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final DynamicDialog dialog = new DynamicDialog(P.mContext, mTheme);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link DynamicDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>Calling this method is functionally identical to:
         * <pre>
         *     DynamicDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         *
         * @return The {@link DynamicDialog} with the arguments supplied to this builder.
         */
        public DynamicDialog show() {
            final DynamicDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
