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

package com.pranavpandey.android.dynamic.support.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Helper class to handle shared preferences operations like saving or
 * retrieving the values from default shared preferences. It must be
 * initialized once before accessing its methods.
 */
public class DynamicPreferences {

    /**
     * Singleton instance of {@link DynamicPreferences}.
     */
    private static DynamicPreferences sInstance;

    /**
     * Context to retrieve the resources.
     */
    protected Context mContext;

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use {@link #initializeInstance(Context)} instead.
     */
    private DynamicPreferences() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to retrieve the resources.
     */
    private DynamicPreferences(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Initialize preferences when application starts.
     * Must be initialized once.
     *
     * @param context The context to retrieve resources.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        if (sInstance == null) {
            sInstance = new DynamicPreferences(context);
        }
    }

    /**
     * Get instance to access public methods. Must be called before
     * accessing the methods.
     *
     * @return The singleton instance of this class.
     */
    public static synchronized DynamicPreferences getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicPreferences.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * @return The context to retrieve the resources.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Set a boolean value in the supplied preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param preferences The preferences name to store the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public void savePrefs(@NonNull String preferences, @NonNull String key, boolean value) {
        mContext.getSharedPreferences(preferences, Context.MODE_PRIVATE)
                .edit().putBoolean(key, value).apply();
    }

    /**
     * Set a boolean value in the default preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public void savePrefs(@NonNull String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putBoolean(key, value).apply();
    }

    /**
     * Set a integer value in the supplied preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param preferences The preferences name to store the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public void savePrefs(@NonNull String preferences, @NonNull String key, int value) {
        mContext.getSharedPreferences(preferences, Context.MODE_PRIVATE)
                .edit().putInt(key, value).apply();
    }

    /**
     * Set an integer value in the default preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public void savePrefs(@NonNull String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putInt(key, value).apply();
    }

    /**
     * Set a float value in the supplied preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param preferences The preferences name to store the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public void savePrefs(@NonNull String preferences, @NonNull String key, float value) {
        mContext.getSharedPreferences(preferences, Context.MODE_PRIVATE)
                .edit().putFloat(key, value).apply();
    }
    
    /**
     * Set a float value in the default preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public void savePrefs(@NonNull String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putFloat(key, value).apply();
    }

    /**
     * Set a string value in the supplied preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param preferences The preferences name to store the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public void savePrefs(@NonNull String preferences, @NonNull String key, 
                          @Nullable String value) {
        mContext.getSharedPreferences(preferences, Context.MODE_PRIVATE)
                .edit().putString(key, value).apply();
    }

    /**
     * Set a string value in the default preferences editor and call
     * {@link SharedPreferences.Editor#apply()} to apply changes
     * back from this editor.
     *
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public void savePrefs(@NonNull String key, @Nullable String value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putString(key, value).apply();
    }

    /**
     * Retrieve a boolean value from the supplied preferences.
     *
     * @param preferences The preferences name to find the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a boolean.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public boolean loadPrefs(@NonNull String preferences, @NonNull String key, boolean value) {
        return mContext.getSharedPreferences(
                preferences, Context.MODE_PRIVATE).getBoolean(key, value);
    }

    /**
     * Retrieve a boolean value from the default preferences.
     *
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a boolean.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public boolean loadPrefs(@NonNull String key, boolean value) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, value);
    }

    /**
     * Retrieve a integer value from the supplied preferences.
     *
     * @param preferences The preferences name to find the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a boolean.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public int loadPrefs(@NonNull String preferences, @NonNull String key, int value) {
        return mContext.getSharedPreferences(
                preferences, Context.MODE_PRIVATE).getInt(key, value);
    }

    /**
     * Retrieve an integer value from the default preferences.
     *
     * @param key The preference key to retrieve.
     * @param value Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not an integer.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public int loadPrefs(@NonNull String key, int value) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key, value);
    }

    /**
     * Retrieve a float value from the supplied preferences.
     *
     * @param preferences The preferences name to find the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a boolean.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public float loadPrefs(@NonNull String preferences, @NonNull String key, float value) {
        return mContext.getSharedPreferences(
                preferences, Context.MODE_PRIVATE).getFloat(key, value);
    }

    /**
     * Retrieve an float value from the default preferences.
     *
     * @param key The preference key to retrieve.
     * @param value Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not an integer.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public float loadPrefs(@NonNull String key, float value) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getFloat(key, value);
    }

    /**
     * Retrieve a string value from the supplied preferences.
     *
     * @param preferences The preferences name to find the key.
     * @param key The preference key to modify.
     * @param value The value for the preference.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a boolean.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public @Nullable String loadPrefs(@NonNull String preferences, @NonNull String key,
                                      @Nullable String value) {
        return mContext.getSharedPreferences(
                preferences, Context.MODE_PRIVATE).getString(key, value);
    }

    /**
     * Retrieve a string value from the default preferences.
     *
     * @param key The preference key to retrieve.
     * @param value Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a string.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public @Nullable String loadPrefs(@NonNull String key, @Nullable String value) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, value);
    }

    /**
     * Get default shared preferences name to perform other operations
     * like backup and restore.
     *
     * @return The default shared preferences name.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public String getDefaultSharedPreferencesName() {
        return mContext.getPackageName() + "_preferences";
    }

    /**
     * Remove a key from the supplied preferences.
     *
     * @param preferences The preferences name to remove the key.
     * @param key The preference key to remove.
     *
     * @see Context#getSharedPreferences(String, int)
     */
    public void deletePrefs(@NonNull String preferences, @NonNull String key) {
        mContext.getSharedPreferences(
                preferences, Context.MODE_PRIVATE).edit().remove(key).apply();
    }

    /**
     * Remove a key from the default preferences.
     *
     * @param key The preference key to remove.
     *
     * @see PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public void deletePrefs(@NonNull String key) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(key).apply();
    }

    /**
     * Delete a shared preferences.
     *
     * @param preferences The preferences to be deleted.
     */
    public void deleteSharedPreferences(@NonNull String preferences) {
        mContext.getSharedPreferences(preferences, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
