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


package com.pranavpandey.android.dynamic.support.theme.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.setting.DynamicSeekBarPreference;
import com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.Theme;
import com.pranavpandey.android.dynamic.support.theme.dialog.DynamicThemeDialog;
import com.pranavpandey.android.dynamic.support.theme.view.DynamicThemePreview;
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;
import com.pranavpandey.android.dynamic.utils.DynamicBitmapUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicFileUtils;
import com.pranavpandey.android.dynamic.utils.DynamicLinkUtils;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the need.
 */
public class DynamicThemeFragment extends DynamicFragment {

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
     * Dynamic app theme used by this fragment.
     */
    private DynamicAppTheme mDynamicAppTheme;

    /**
     * Default dynamic app theme used by this fragment.
     */
    private DynamicAppTheme mDynamicAppThemeDefault;

    /**
     * {@code true} if the settings has been changed.
     */
    private boolean mSettingsChanged;

    /**
     * Theme preview used by this fragment.
     */
    private DynamicThemePreview mThemePreview;

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
    private DynamicColorPreference mColorPrimaryDarkPreference;

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
     *
     * @return An instance of {@link DynamicThemeFragment}.
     */
    public static Fragment newInstance(@Nullable String dynamicAppTheme,
            @Nullable String dynamicAppThemeDefault) {
        DynamicThemeFragment fragment = new DynamicThemeFragment();
        Bundle args = new Bundle();
        args.putString(DynamicIntent.EXTRA_THEME, dynamicAppTheme);
        args.putString(DynamicIntent.EXTRA_THEME_DEFAULT, dynamicAppThemeDefault);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(Activity.RESULT_CANCELED, null, false);

        setRetainInstance(true);
        setHasOptionsMenu(true);

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
        return inflater.inflate(R.layout.ads_fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mThemePreview = view.findViewById(R.id.ads_theme_preview);
        mColorBackgroundPreference = view.findViewById(R.id.ads_pref_theme_color_background);
        mColorPrimaryPreference = view.findViewById(R.id.ads_pref_theme_color_primary);
        mColorPrimaryDarkPreference = view.findViewById(R.id.ads_pref_theme_color_primary_dark);
        mColorAccentPreference = view.findViewById(R.id.ads_pref_theme_color_accent);
        mTextPrimaryPreference = view.findViewById(R.id.ads_pref_theme_text_primary);
        mTextSecondaryPreference = view.findViewById(R.id.ads_pref_theme_text_secondary);
        mCornerSizePreference = view.findViewById(R.id.ads_pref_theme_corner_size);
        mBackgroundAwarePreference = view.findViewById(R.id.ads_pref_theme_background_aware);

        mThemePreview.getFAB().setImageResource(R.drawable.ads_ic_preview);

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

        mColorPrimaryDarkPreference.setDynamicColorResolver(new DynamicColorResolver() {
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

        getDynamicActivity().setFAB(R.drawable.ads_ic_save,
                getDynamicActivity().getFABVisibility(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveThemeSettings();
                    }
                });

        loadTheme(mDynamicAppTheme);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        DynamicMenuUtils.forceMenuIcons(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.ads_menu_theme, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.ads_menu_theme_copy) {
            DynamicLinkUtils.copyToClipboard(getContext(), getString(R.string.ads_theme),
                    mThemePreview.getDynamicTheme().toDynamicString());

            getDynamicActivity().getSnackBar(R.string.ads_theme_copy_done).show();
        } else if (i == R.id.ads_menu_theme_share) {
            mThemePreview.getFAB().setImageResource(R.drawable.ads_ic_style);
            DynamicLinkUtils.share(getContext(),
                    getSubtitle() != null ? getSubtitle().toString() : null,
                    mThemePreview.getDynamicTheme().toDynamicString(),
                    DynamicFileUtils.getBitmapUri(getContext(),
                            DynamicBitmapUtils.createBitmapFromView(mThemePreview),
                            DynamicThemeUtils.NAME_THEME_SHARE));
            mThemePreview.getFAB().setImageResource(R.drawable.ads_ic_preview);
        } else if (i == R.id.ads_menu_theme_import) {
            importTheme();
        } else if (i == R.id.ads_menu_refresh) {
            mSettingsChanged = false;
            loadTheme(mDynamicAppTheme);
        } else if (i == R.id.ads_menu_default) {
            mSettingsChanged = false;
            mDynamicAppTheme = new DynamicAppTheme(mDynamicAppThemeDefault);
            loadTheme(mDynamicAppTheme);

            getDynamicActivity().getSnackBar(R.string.ads_theme_reset_desc).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show dialog to import the theme.
     *
     * @see DynamicThemeDialog
     * @see DynamicThemeDialog.Type#THEME_IMPORT
     */
    private void importTheme() {
        DynamicThemeDialog.newInstance()
                .setType(DynamicThemeDialog.Type.THEME_IMPORT)
                .setOnImportThemeListener(
                        new DynamicThemeDialog.OnThemeImportListener() {
                            @Override
                            public void onImportTheme(@NonNull String theme) {
                                try {
                                    DynamicAppTheme dynamicAppTheme = new DynamicAppTheme(theme);
                                    mSettingsChanged = false;
                                    loadTheme(dynamicAppTheme);

                                    getDynamicActivity().getSnackBar(
                                            R.string.ads_theme_import_done).show();
                                } catch (Exception ignored) {
                                    invalidTheme();
                                }
                            }
                        }).showDialog(getActivity());
    }

    /**
     * Show dialog for the invalid theme.
     *
     * @see DynamicThemeDialog
     * @see DynamicThemeDialog.Type#THEME_INVALID
     */
    private void invalidTheme() {
        DynamicThemeDialog.newInstance()
                .setType(DynamicThemeDialog.Type.THEME_INVALID)
                .setBuilder(new DynamicDialog.Builder(getContext())
                        .setPositiveButton(R.string.ads_backup_import,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        importTheme();
                                    }
                                })).showDialog(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        updateThemePreview();
        updatePreferences();
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

    /**
     * Update settings according to the supplied theme.
     */
    private void loadTheme(@NonNull DynamicAppTheme dynamicAppTheme) {
        if (!mSettingsChanged) {
            mColorBackgroundPreference.setColor(dynamicAppTheme.getBackgroundColor(false));
            mColorBackgroundPreference.setAltColor(dynamicAppTheme.getTintBackgroundColor(false));
            mColorPrimaryPreference.setColor(dynamicAppTheme.getPrimaryColor(false));
            mColorPrimaryPreference.setAltColor(dynamicAppTheme.getTintPrimaryColor(false));
            mColorPrimaryDarkPreference.setColor(dynamicAppTheme.getPrimaryColorDark(false));
            mColorAccentPreference.setColor(dynamicAppTheme.getAccentColor(false));
            mColorAccentPreference.setAltColor(dynamicAppTheme.getTintAccentColor(false));
            mTextPrimaryPreference.setColor(dynamicAppTheme.getTextPrimaryColor(false));
            mTextPrimaryPreference.setAltColor(dynamicAppTheme.getTextPrimaryColorInverse(false));
            mTextSecondaryPreference.setColor(dynamicAppTheme.getTextSecondaryColor(false));
            mTextSecondaryPreference.setAltColor(dynamicAppTheme.getTextSecondaryColorInverse(false));

            if (dynamicAppTheme.getCornerRadius(false) != Theme.AUTO) {
                mCornerSizePreference.setPreferenceValue(Theme.ToString.CUSTOM);
                mCornerSizePreference.setValue(dynamicAppTheme.getCornerSizeDp());
            } else {
                mCornerSizePreference.setPreferenceValue(Theme.ToString.AUTO);
            }

            mBackgroundAwarePreference.setPreferenceValue(
                    String.valueOf(dynamicAppTheme.getBackgroundAware(false)));
        }

        updateThemePreview();
        updatePreferences();
    }

    /**
     * Get the resolved corner size from the preference.
     */
    private int getCornerSize() {
        return mCornerSizePreference.getPreferenceValue().equals(Theme.ToString.AUTO)
                ? Theme.AUTO : mCornerSizePreference.getValueFromProgress();
    }

    /**
     * Get the resolved background aware from the preference.
     */
    private @Theme.BackgroundAware int getBackgroundAware() {
        if (mBackgroundAwarePreference.getPreferenceValue() != null) {
            return Integer.valueOf(mBackgroundAwarePreference.getPreferenceValue());
        }

        return Theme.BackgroundAware.AUTO;
    }

    /**
     * Update all the preferences.
     */
    private void updatePreferences() {
        mColorBackgroundPreference.update();
        mColorPrimaryPreference.update();
        mColorPrimaryDarkPreference.update();
        mColorAccentPreference.update();
        mTextPrimaryPreference.update();
        mTextSecondaryPreference.update();
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
                        .setPrimaryColor(mColorPrimaryPreference.getColor(false))
                        .setTintPrimaryColor(mColorPrimaryPreference.getAltColor(false))
                        .setPrimaryColorDark(mColorPrimaryDarkPreference.getColor(false))
                        .setTintPrimaryColorDark(Theme.AUTO)
                        .setAccentColor(mColorAccentPreference.getColor(false))
                        .setTintAccentColor(mColorAccentPreference.getAltColor(false))
                        .setTextPrimaryColor(mTextPrimaryPreference.getColor(false))
                        .setTextPrimaryColorInverse(mTextPrimaryPreference.getAltColor(false))
                        .setTextSecondaryColor(mTextSecondaryPreference.getColor(false))
                        .setTextSecondaryColorInverse(mTextSecondaryPreference.getAltColor(false))
                        .setCornerRadiusDp(getCornerSize())
                        .setBackgroundAware(getBackgroundAware()));

        mSettingsChanged = true;
    }

    /**
     * Set the theme and finish this activity.
     */
    public void saveThemeSettings() {
        Intent intent = new Intent();
        intent.putExtra(DynamicIntent.EXTRA_THEME,
                mThemePreview.getDynamicTheme().toJsonString());
        setResult(Activity.RESULT_OK, intent);

        finishActivity();
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
            case ADS_PREF_THEME_COLOR_PRIMARY:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY:
            case ADS_PREF_THEME_COLOR_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_ACCENT:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT:
            case ADS_PREF_THEME_TEXT_PRIMARY:
            case ADS_PREF_THEME_TEXT_INVERSE_PRIMARY:
            case ADS_PREF_THEME_TEXT_SECONDARY:
            case ADS_PREF_THEME_TEXT_INVERSE_SECONDARY:
            case ADS_PREF_THEME_CORNER_SIZE:
            case ADS_PREF_THEME_CORNER_SIZE_ALT:
            case ADS_PREF_THEME_BACKGROUND_AWARE:
                updateThemePreview();
                updatePreferences();
                break;
        }
    }
}
