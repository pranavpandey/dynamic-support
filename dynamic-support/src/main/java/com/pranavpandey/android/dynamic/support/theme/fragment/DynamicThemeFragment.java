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

package com.pranavpandey.android.dynamic.support.theme.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.setting.DynamicSeekBarPreference;
import com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the need.
 */
public class DynamicThemeFragment extends ThemeFragment<DynamicAppTheme> {

    /**
     * Key for background color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_BACKGROUND =
            "ads_pref_settings_theme_color_background";

    /**
     * Key for tint background color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_TINT_BACKGROUND =
            "ads_pref_settings_theme_color_tint_background";

    /**
     * Key for surface color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_SURFACE =
            "ads_pref_settings_theme_color_surface";

    /**
     * Key for tint surface color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_TINT_SURFACE =
            "ads_pref_settings_theme_color_tint_surface";

    /**
     * Key for primary color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_PRIMARY =
            "ads_pref_settings_theme_color_primary";

    /**
     * Key for tint primary color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_TINT_PRIMARY =
            "ads_pref_settings_theme_color_tint_primary";

    /**
     * Key for dark primary color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_PRIMARY_DARK =
            "ads_pref_settings_theme_color_primary_dark";

    /**
     * Key for accent color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_ACCENT =
            "ads_pref_settings_theme_color_accent";

    /**
     * Key for tint accent color preference.
     */
    private static final String ADS_PREF_THEME_COLOR_TINT_ACCENT =
            "ads_pref_settings_theme_color_tint_accent";

    /**
     * Key for primary text color preference.
     */
    private static final String ADS_PREF_THEME_TEXT_PRIMARY =
            "ads_pref_settings_theme_text_primary";

    /**
     * Key for inverse primary text color preference.
     */
    private static final String ADS_PREF_THEME_TEXT_INVERSE_PRIMARY =
            "ads_pref_settings_theme_text_inverse_primary";

    /**
     * Key for secondary text color preference.
     */
    private static final String ADS_PREF_THEME_TEXT_SECONDARY =
            "ads_pref_settings_theme_text_secondary";

    /**
     * Key for inverse secondary text color preference.
     */
    private static final String ADS_PREF_THEME_TEXT_INVERSE_SECONDARY =
            "ads_pref_settings_theme_text_inverse_secondary";

    /**
     * Key for font scale preference.
     */
    private static final String ADS_PREF_THEME_FONT_SCALE =
            "ads_pref_settings_theme_font_scale";

    /**
     * Key for font scale alternate preference.
     */
    private static final String ADS_PREF_THEME_FONT_SCALE_ALT =
            "ads_pref_settings_theme_font_scale_alt";

    /**
     * Key for corner size preference.
     */
    private static final String ADS_PREF_THEME_CORNER_SIZE =
            "ads_pref_settings_theme_corner_size";

    /**
     * Key for corner size alternate preference.
     */
    private static final String ADS_PREF_THEME_CORNER_SIZE_ALT =
            "ads_pref_settings_theme_corner_size_alt";

    /**
     * Key for background aware preference.
     */
    private static final String ADS_PREF_THEME_BACKGROUND_AWARE =
            "ads_pref_settings_theme_background_aware";

    /**
     * Dynamic color preference to control the background color.
     */
    private DynamicColorPreference mColorBackgroundPreference;

    /**
     * Dynamic color preference to control the primary color.
     */
    private DynamicColorPreference mColorPrimaryPreference;

    /**
     * Dynamic color preference to control the dark primary color.
     */
    private DynamicColorPreference mColorSystemPreference;

    /**
     * Dynamic color preference to control the accent color.
     */
    private DynamicColorPreference mColorAccentPreference;

    /**
     * Dynamic color preference to control the primary text color.
     */
    private DynamicColorPreference mTextPrimaryPreference;

    /**
     * Dynamic color preference to control the secondary text color.
     */
    private DynamicColorPreference mTextSecondaryPreference;

    /**
     * Dynamic seek bar preference to control the font scale.
     */
    private DynamicSeekBarPreference mFontScalePreference;

    /**
     * Dynamic seek bar preference to control the corner radius.
     */
    private DynamicSeekBarPreference mCornerSizePreference;

    /**
     * Dynamic spinner preference to control the background aware functionality.
     */
    private DynamicSpinnerPreference mBackgroundAwarePreference;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param dynamicAppTheme The dynamic app theme.
     * @param dynamicAppThemeDefault The default dynamic app theme.
     * @param showPresets {@code true} to show the presets.
     *
     * @return An instance of {@link DynamicThemeFragment}.
     */
    public static Fragment newInstance(@Nullable String dynamicAppTheme,
            @Nullable String dynamicAppThemeDefault, boolean showPresets) {
        DynamicThemeFragment fragment = new DynamicThemeFragment();
        Bundle args = new Bundle();
        args.putString(DynamicIntent.EXTRA_THEME, dynamicAppTheme);
        args.putString(DynamicIntent.EXTRA_THEME_DEFAULT, dynamicAppThemeDefault);
        args.putBoolean(DynamicIntent.EXTRA_THEME_SHOW_PRESETS, showPresets);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param dynamicAppTheme The dynamic app theme.
     * @param dynamicAppThemeDefault The default dynamic app theme.
     *
     * @return An instance of {@link DynamicThemeFragment}.
     *
     * @see #newInstance(String, String, boolean)
     */
    public static Fragment newInstance(@Nullable String dynamicAppTheme,
            @Nullable String dynamicAppThemeDefault) {
        return newInstance(dynamicAppTheme, dynamicAppThemeDefault, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(Activity.RESULT_CANCELED, null, false);

        if (savedInstanceState == null) {
            mSettingsChanged = false;
        }

        mDynamicAppTheme = getThemeFromArguments(DynamicIntent.EXTRA_THEME);
        mDynamicAppThemeDefault = getThemeFromArguments(DynamicIntent.EXTRA_THEME_DEFAULT);

        if (mDynamicAppTheme == null) {
            mDynamicAppTheme = DynamicTheme.getInstance().generateDefaultTheme();
        }

        if (mDynamicAppThemeDefault == null) {
            mDynamicAppThemeDefault = mDynamicAppTheme;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addThemePreview();
        return inflater.inflate(R.layout.ads_fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mColorBackgroundPreference = view.findViewById(R.id.ads_pref_theme_color_background);
        mColorPrimaryPreference = view.findViewById(R.id.ads_pref_theme_color_primary);
        mColorSystemPreference = view.findViewById(R.id.ads_pref_theme_color_system);
        mColorAccentPreference = view.findViewById(R.id.ads_pref_theme_color_accent);
        mTextPrimaryPreference = view.findViewById(R.id.ads_pref_theme_text_primary);
        mTextSecondaryPreference = view.findViewById(R.id.ads_pref_theme_text_secondary);
        mFontScalePreference = view.findViewById(R.id.ads_pref_theme_font_scale);
        mCornerSizePreference = view.findViewById(R.id.ads_pref_theme_corner_size);
        mBackgroundAwarePreference = view.findViewById(R.id.ads_pref_theme_background_aware);

        mColorBackgroundPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicAppThemeDefault.getBackgroundColor();
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicTheme.getInstance().getDefault().getBackgroundColor();
            }
        });
        mColorBackgroundPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicColorUtils.getTintColor(
                        mThemePreview.getDynamicTheme().getBackgroundColor());
            }
        });

        mColorPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicTheme.getInstance().getDefault().getPrimaryColor();
            }
        });
        mColorPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicColorUtils.getTintColor(
                        mThemePreview.getDynamicTheme().getPrimaryColor());
            }
        });

        mColorSystemPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicTheme.getInstance().generateDarkColor(
                        mThemePreview.getDynamicTheme().getPrimaryColor());
            }
        });
        mColorSystemPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return DynamicTheme.getInstance().getDefault().getSurfaceColor();
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicTheme.getInstance().generateSurfaceColor(
                        mThemePreview.getDynamicTheme().getBackgroundColor());
            }
        });

        mColorAccentPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicTheme.getInstance().getDefault().getAccentColor();
            }
        });
        mColorAccentPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                return DynamicColorUtils.getTintColor(
                        mThemePreview.getDynamicTheme().getAccentColor());
            }
        });

        mTextPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                if (DynamicColorUtils.getContrastColor(
                        DynamicTheme.getInstance().getDefault().getTextPrimaryColor(),
                        mThemePreview.getDynamicTheme().getBackgroundColor())
                    != DynamicTheme.getInstance().getDefault().getTextPrimaryColor()) {
                    return DynamicTheme.getInstance().getDefault().getTextPrimaryColorInverse();
                }

                return DynamicTheme.getInstance().getDefault().getTextPrimaryColor();
            }
        });
        mTextPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return DynamicColorUtils.getTintColor(mTextPrimaryPreference.getColor());
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                if (DynamicColorUtils.getContrastColor(
                        DynamicTheme.getInstance().getDefault().getTextPrimaryColorInverse(),
                        mThemePreview.getDynamicTheme().getBackgroundColor())
                        == DynamicTheme.getInstance().getDefault().getTextPrimaryColorInverse()) {
                    return DynamicColorUtils.getTintColor(
                            DynamicTheme.getInstance().getDefault().getTextPrimaryColorInverse());
                }

                return DynamicTheme.getInstance().getDefault().getTextPrimaryColorInverse();
            }
        });

        mTextSecondaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Theme.AUTO;
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                if (DynamicColorUtils.getContrastColor(
                        DynamicTheme.getInstance().getDefault().getTextSecondaryColor(),
                        mThemePreview.getDynamicTheme().getBackgroundColor())
                        != DynamicTheme.getInstance().getDefault().getTextSecondaryColor()) {
                    return DynamicTheme.getInstance().getDefault().getTextSecondaryColorInverse();
                }

                return DynamicTheme.getInstance().getDefault().getTextSecondaryColor();
            }
        });
        mTextSecondaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return DynamicColorUtils.getTintColor(mTextSecondaryPreference.getColor());
            }

            @Override
            public int getAutoColor(@Nullable String tag) {
                if (DynamicColorUtils.getContrastColor(
                        DynamicTheme.getInstance().getDefault().getTextSecondaryColorInverse(),
                        mThemePreview.getDynamicTheme().getBackgroundColor())
                        == DynamicTheme.getInstance().getDefault().getTextSecondaryColorInverse()) {
                    return DynamicColorUtils.getTintColor(
                            DynamicTheme.getInstance().getDefault().getTextSecondaryColorInverse());
                }

                return DynamicTheme.getInstance().getDefault().getTextSecondaryColorInverse();
            }
        });

        loadTheme(mDynamicAppTheme);
        onSetActionIcon(true);

        if (savedInstanceState == null) {
            getDynamicActivity().setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.ads_menu_default) {
            mSettingsChanged = false;
            mDynamicAppTheme = new DynamicAppTheme(mDynamicAppThemeDefault);

            loadTheme(mDynamicAppTheme);
            mFontScalePreference.setValue(mFontScalePreference.getDefaultValue());
            mCornerSizePreference.setValue(mCornerSizePreference.getDefaultValue());

            getDynamicActivity().getSnackBar(R.string.ads_theme_reset_desc).show();
            getDynamicActivity().setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        setHasOptionsMenu(true);

        updateThemePreview();
        updatePreferences();
    }

    /**
     * Add the theme preview and set the shared element transition listener.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addThemePreview() {
        if (getActivity() == null) {
            return;
        }

        getDynamicActivity().addBottomSheet(R.layout.ads_theme_preview_bottom_sheet, true);
        mThemePreview = getDynamicActivity().findViewById(R.id.ads_theme_preview);
        ViewCompat.setTransitionName(mThemePreview.getActionView(), ADS_NAME_THEME_PREVIEW_ACTION);

        mThemePreview.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveThemeSettings();
            }
        });

        if (!DynamicSdkUtils.is21()) {
            return;
        }

        final Transition transition = getActivity().getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    updateThemePreview();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) { }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) { }

                @Override
                public void onTransitionResume(Transition transition) { }
            });
        }
    }

    /**
     * Retrieves the theme from the supplies arguments
     *
     * @param key The key to be retrieved.
     *
     * @return The dynamic theme from the supplied arguments.
     */
    public @Nullable DynamicAppTheme getThemeFromArguments(@NonNull String key) {
        return DynamicTheme.getInstance().getTheme(getStringFromArguments(key));
    }

    @Override
    protected @NonNull DynamicAppTheme onImportTheme(@NonNull String theme) {
        try {
            return new DynamicAppTheme(theme);
        } catch (Exception ignored) {
        }

        return mThemePreview.getDynamicTheme();
    }

    @Override
    protected void loadTheme(@NonNull DynamicAppTheme theme) {
        if (!mSettingsChanged) {
            mColorBackgroundPreference.setColor(theme.getBackgroundColor(false));
            mColorBackgroundPreference.setAltColor(theme.getTintBackgroundColor(false));
            mColorPrimaryPreference.setColor(theme.getPrimaryColor(false));
            mColorPrimaryPreference.setAltColor(theme.getTintPrimaryColor(false));
            mColorSystemPreference.setColor(theme.getPrimaryColorDark(false));
            mColorSystemPreference.setAltColor(theme.getSurfaceColor(false));
            mColorAccentPreference.setColor(theme.getAccentColor(false));
            mColorAccentPreference.setAltColor(theme.getTintAccentColor(false));
            mTextPrimaryPreference.setColor(theme.getTextPrimaryColor(false));
            mTextPrimaryPreference.setAltColor(theme.getTextPrimaryColorInverse(false));
            mTextSecondaryPreference.setColor(theme.getTextSecondaryColor(false));
            mTextSecondaryPreference.setAltColor(theme.getTextSecondaryColorInverse(false));

            if (theme.getFontScale(false) != Theme.AUTO) {
                mFontScalePreference.setPreferenceValue(Theme.ToString.CUSTOM);
                mFontScalePreference.setValue(theme.getFontScale());
            } else {
                mFontScalePreference.setPreferenceValue(Theme.ToString.AUTO);
            }

            if (theme.getCornerRadius(false) != Theme.AUTO) {
                mCornerSizePreference.setPreferenceValue(Theme.ToString.CUSTOM);
                mCornerSizePreference.setValue(theme.getCornerSizeDp());
            } else {
                mCornerSizePreference.setPreferenceValue(Theme.ToString.AUTO);
            }

            mBackgroundAwarePreference.setPreferenceValue(
                    String.valueOf(theme.getBackgroundAware(false)));
        }

        updateThemePreview();
        updatePreferences();
    }

    @Override
    public void onSetActionIcon(boolean enable) {
        mThemePreview.getActionView().setImageResource(enable
                ? R.drawable.ads_ic_save : R.drawable.ads_ic_style);
    }

    /**
     * Returns the resolved font scale from the preference.
     *
     * @return The resolved font scale from the preference.
     */
    private int getFontScale() {
        return mFontScalePreference.getPreferenceValue().equals(Theme.ToString.AUTO)
                ? Theme.AUTO : mFontScalePreference.getValueFromProgress();
    }

    /**
     * Returns the resolved corner size from the preference.
     *
     * @return The resolved corner size from the preference.
     */
    private int getCornerSize() {
        return mCornerSizePreference.getPreferenceValue().equals(Theme.ToString.AUTO)
                ? Theme.AUTO : mCornerSizePreference.getValueFromProgress();
    }

    /**
     * Returns the resolved background aware from the preference.
     *
     * @return The resolved background aware from the preference.
     */
    private @Theme.BackgroundAware int getBackgroundAware() {
        if (mBackgroundAwarePreference.getPreferenceValue() != null) {
            return Integer.parseInt(mBackgroundAwarePreference.getPreferenceValue());
        }

        return Theme.BackgroundAware.AUTO;
    }

    /**
     * Update all the preferences.
     */
    private void updatePreferences() {
        mColorBackgroundPreference.update();
        mColorPrimaryPreference.update();
        mColorSystemPreference.update();
        mColorAccentPreference.update();
        mTextPrimaryPreference.update();
        mTextSecondaryPreference.update();
        mFontScalePreference.setSeekBarEnabled(getFontScale() != Theme.AUTO);
        mCornerSizePreference.setSeekBarEnabled(getCornerSize() != Theme.AUTO);
        mBackgroundAwarePreference.update();
    }

    /**
     * Update the theme preview.
     */
    private void updateThemePreview() {
        mThemePreview.setDynamicTheme(
                new DynamicAppTheme(mDynamicAppTheme)
                        .setBackgroundColor(mColorBackgroundPreference.getColor(false))
                        .setTintBackgroundColor(mColorBackgroundPreference.getAltColor(false))
                        .setSurfaceColor(mColorSystemPreference.getAltColor(false))
                        .setTintSurfaceColor(Theme.AUTO)
                        .setPrimaryColor(mColorPrimaryPreference.getColor(false))
                        .setTintPrimaryColor(mColorPrimaryPreference.getAltColor(false))
                        .setPrimaryColorDark(mColorSystemPreference.getColor(false))
                        .setTintPrimaryColorDark(Theme.AUTO)
                        .setAccentColor(mColorAccentPreference.getColor(false))
                        .setTintAccentColor(mColorAccentPreference.getAltColor(false))
                        .setTextPrimaryColor(mTextPrimaryPreference.getColor(false))
                        .setTextPrimaryColorInverse(mTextPrimaryPreference.getAltColor(false))
                        .setTextSecondaryColor(mTextSecondaryPreference.getColor(false))
                        .setTextSecondaryColorInverse(mTextSecondaryPreference.getAltColor(false))
                        .setFontScale(getFontScale())
                        .setCornerRadiusDp(getCornerSize())
                        .setBackgroundAware(getBackgroundAware()));

        mSettingsChanged = true;
    }

    @Override
    protected boolean setSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case ADS_PREF_THEME_COLOR_BACKGROUND:
            case ADS_PREF_THEME_COLOR_TINT_BACKGROUND:
            case ADS_PREF_THEME_COLOR_SURFACE:
            case ADS_PREF_THEME_COLOR_TINT_SURFACE:
            case ADS_PREF_THEME_COLOR_PRIMARY:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY:
            case ADS_PREF_THEME_COLOR_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_ACCENT:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT:
            case ADS_PREF_THEME_TEXT_PRIMARY:
            case ADS_PREF_THEME_TEXT_INVERSE_PRIMARY:
            case ADS_PREF_THEME_TEXT_SECONDARY:
            case ADS_PREF_THEME_TEXT_INVERSE_SECONDARY:
            case ADS_PREF_THEME_FONT_SCALE:
            case ADS_PREF_THEME_FONT_SCALE_ALT:
            case ADS_PREF_THEME_CORNER_SIZE:
            case ADS_PREF_THEME_CORNER_SIZE_ALT:
            case ADS_PREF_THEME_BACKGROUND_AWARE:
                updateThemePreview();
                updatePreferences();
                break;
        }
    }
}
