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

package com.pranavpandey.android.dynamic.support.theme.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicRemoteTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.view.DynamicPresetsView;
import com.pranavpandey.android.dynamic.support.theme.view.ThemePreview;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.util.DynamicThemeUtils;

/**
 * Base theme fragment to provide theme editing functionality.
 * <p>Extend this fragment to implement theme attributes according to the requirements.
 */
public class DynamicRemoteThemeFragment extends ThemeFragment<DynamicRemoteTheme> {
    
    /**
     * View to show the theme presets.
     */
    private DynamicPresetsView<DynamicRemoteTheme> mPresetsView;

    /**
     * Dynamic color preference to control the background colors.
     */
    private DynamicColorPreference mColorBackgroundPreference;

    /**
     * Dynamic color preference to control the surface colors.
     */
    private DynamicColorPreference mColorSurfacePreference;

    /**
     * Dynamic color preference to control the primary colors.
     */
    private DynamicColorPreference mColorPrimaryPreference;

    /**
     * Dynamic color preference to control the accent colors.
     */
    private DynamicColorPreference mColorAccentPreference;

    /**
     * Dynamic color preference to control the dark primary colors.
     */
    private DynamicColorPreference mColorPrimaryDarkPreference;

    /**
     * Dynamic color preference to control the dark accent colors.
     */
    private DynamicColorPreference mColorAccentDarkPreference;

    /**
     * Dynamic color preference to control the error colors.
     */
    private DynamicColorPreference mColorErrorPreference;

    /**
     * Dynamic color preference to control the primary text colors.
     */
    private DynamicColorPreference mTextPrimaryPreference;

    /**
     * Dynamic color preference to control the secondary text colors.
     */
    private DynamicColorPreference mTextSecondaryPreference;

    /**
     * Dynamic slider preference to control the font scale.
     */
    private DynamicSliderPreference mFontScalePreference;

    /**
     * Dynamic slider preference to control the corner radius.
     */
    private DynamicSliderPreference mCornerSizePreference;

    /**
     * Dynamic spinner preference to control the background aware functionality.
     */
    private DynamicSpinnerPreference mBackgroundAwarePreference;

    /**
     * Dynamic slider preference to control the contrast.
     */
    private DynamicSliderPreference mContrastPreference;

    /**
     * Dynamic slider preference to control the opacity.
     */
    private DynamicSliderPreference mOpacityPreference;

    /**
     * Dynamic spinner preference to control the elevation functionality.
     */
    private DynamicSpinnerPreference mElevationPreference;

    /**
     * Dynamic spinner preference to control the style functionality.
     */
    private DynamicSpinnerPreference mStylePreference;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param DynamicRemoteTheme The dynamic app theme.
     * @param DynamicRemoteThemeDefault The default dynamic app theme.
     *
     * @return An instance of {@link DynamicRemoteThemeFragment}.
     *
     * @see #newInstance(String, String, boolean)
     */
    public static @NonNull DynamicRemoteThemeFragment newInstance(
            @Nullable String DynamicRemoteTheme, @Nullable String DynamicRemoteThemeDefault) {
        return newInstance(DynamicRemoteTheme, DynamicRemoteThemeDefault,
                DynamicIntent.EXTRA_THEME_SHOW_PRESETS_DEFAULT);
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param DynamicRemoteTheme The dynamic app theme.
     * @param DynamicRemoteThemeDefault The default dynamic app theme.
     * @param showPresets {@code true} to show the presets.
     *
     * @return An instance of {@link DynamicRemoteThemeFragment}.
     */
    public static @NonNull DynamicRemoteThemeFragment newInstance(
            @Nullable String DynamicRemoteTheme, @Nullable String DynamicRemoteThemeDefault,
            boolean showPresets) {
        DynamicRemoteThemeFragment fragment = new DynamicRemoteThemeFragment();
        Bundle args = new Bundle();
        args.putString(DynamicIntent.EXTRA_THEME, DynamicRemoteTheme);
        args.putString(DynamicIntent.EXTRA_THEME_DEFAULT, DynamicRemoteThemeDefault);
        args.putBoolean(DynamicIntent.EXTRA_THEME_SHOW_PRESETS, showPresets);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(Activity.RESULT_CANCELED, null, false);

        if (getSavedInstanceState() == null) {
            mSettingsChanged = false;
        }

        mDynamicTheme = getThemeFromArguments(DynamicIntent.EXTRA_THEME);
        mDynamicThemeDefault = getThemeFromArguments(DynamicIntent.EXTRA_THEME_DEFAULT);

        if (mDynamicTheme == null) {
            mDynamicTheme = DynamicTheme.getInstance().getRemote();
        }

        if (mDynamicThemeDefault == null) {
            mDynamicThemeDefault = new DynamicRemoteTheme(mDynamicTheme);
        }

        mDynamicTheme.setType(mDynamicThemeDefault.getType());
    }

    @Override
    public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addThemePreview();
        return inflater.inflate(R.layout.ads_fragment_theme_remote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresetsView = view.findViewById(R.id.ads_theme_presets_view);
        mColorBackgroundPreference = view.findViewById(R.id.ads_pref_theme_color_background);
        mColorSurfacePreference = view.findViewById(R.id.ads_pref_theme_color_surface);
        mColorPrimaryPreference = view.findViewById(R.id.ads_pref_theme_color_primary);
        mColorAccentPreference = view.findViewById(R.id.ads_pref_theme_color_accent);
        mColorPrimaryDarkPreference = view.findViewById(R.id.ads_pref_theme_color_primary_dark);
        mColorAccentDarkPreference = view.findViewById(R.id.ads_pref_theme_color_accent_dark);
        mColorErrorPreference = view.findViewById(R.id.ads_pref_theme_color_error);
        mTextPrimaryPreference = view.findViewById(R.id.ads_pref_theme_text_primary);
        mTextSecondaryPreference = view.findViewById(R.id.ads_pref_theme_text_secondary);
        mFontScalePreference = view.findViewById(R.id.ads_pref_theme_font_scale);
        mCornerSizePreference = view.findViewById(R.id.ads_pref_theme_corner_size);
        mBackgroundAwarePreference = view.findViewById(R.id.ads_pref_theme_background_aware);
        mContrastPreference = view.findViewById(R.id.ads_pref_theme_contrast);
        mOpacityPreference = view.findViewById(R.id.ads_pref_theme_opacity);
        mElevationPreference = view.findViewById(R.id.ads_pref_theme_elevation);
        mStylePreference = view.findViewById(R.id.ads_pref_theme_style);

        if (getBooleanFromArguments(DynamicIntent.EXTRA_THEME_SHOW_PRESETS,
                DynamicIntent.EXTRA_THEME_SHOW_PRESETS_DEFAULT)) {
            Dynamic.setVisibility(mPresetsView, View.VISIBLE);

            mPresetsView.setPresetsAdapter(this,
                    R.layout.ads_layout_item_preset_horizontal_remote,
                    new DynamicPresetsView.DynamicPresetsListener<DynamicRemoteTheme>() {
                @Override
                public void onRequestPermissions(@NonNull String[] permissions) {
                    DynamicPermissions.getInstance().isGranted(permissions, true);
                }

                @Override
                public @Nullable DynamicRemoteTheme getDynamicTheme(@NonNull String theme) {
                    try {
                        return new DynamicRemoteTheme(new DynamicWidgetTheme(theme)
                                .setBackgroundColor(Theme.AUTO, false)
                                .setTintBackgroundColor(Theme.AUTO)
                                .setStyle(mDynamicTheme.getStyle())
                                .setType(mDynamicTheme.getType(false)));
                    } catch (Exception ignored) {
                        return null;
                    }
                }

                @Override
                public void onPresetClick(@NonNull View anchor, @NonNull String theme,
                        @NonNull ThemePreview<DynamicRemoteTheme> themePreview) {
                    importTheme(themePreview.getDynamicTheme().toDynamicString(),
                            Theme.Action.IMPORT);
                }
            });
        } else {
            Dynamic.setVisibility(mPresetsView, View.GONE);
        }

        mColorBackgroundPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getBackgroundColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getBackgroundColor();
            }
        });
        mColorBackgroundPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintBackgroundColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintBackgroundColor();
            }
        });

        mColorSurfacePreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getSurfaceColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getSurfaceColor();
            }
        });
        mColorSurfacePreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintSurfaceColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintSurfaceColor();
            }
        });

        mColorPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getPrimaryColor();
            }
        });
        mColorPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintPrimaryColor();
            }
        });

        mColorAccentPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getAccentColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getAccentColor();
            }
        });
        mColorAccentPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintAccentColor();
            }
        });

        mColorPrimaryDarkPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getPrimaryColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getPrimaryColorDark();
            }
        });
        mColorPrimaryDarkPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintPrimaryColorDark();
            }
        });

        mColorAccentDarkPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getAccentColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getAccentColorDark();
            }
        });
        mColorAccentDarkPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintAccentColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintAccentColorDark();
            }
        });

        mColorErrorPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getErrorColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getErrorColor();
            }
        });
        mColorErrorPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintErrorColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintErrorColor();
            }
        });

        mTextPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTextPrimaryColor();
            }
        });
        mTextPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextPrimaryColorInverse(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTextPrimaryColorInverse();
            }
        });

        mTextSecondaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextSecondaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTextSecondaryColor();
            }
        });
        mTextSecondaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextSecondaryColorInverse(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTextSecondaryColorInverse();
            }
        });

        onLoadTheme(mDynamicTheme);
        onSetAction(Theme.Action.APPLY, mThemePreview, true);

        if (getSavedInstanceState() == null) {
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public boolean setHasOptionsMenu() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

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

        Dynamic.addBottomSheet(getActivity(),
                R.layout.ads_theme_preview_remote_bottom_sheet, true);
        mThemePreview = requireActivity().findViewById(R.id.ads_theme_preview);
        ViewCompat.setTransitionName(mThemePreview.getActionView(), ADS_NAME_THEME_PREVIEW_ACTION);

        mThemePreview.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveThemeSettings();
            }
        });

        requireActivity().findViewById(R.id.ads_theme_preview_bottom_sheet)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveThemeSettings();
                    }
                });
    }

    /**
     * Retrieves the theme from the supplies arguments
     *
     * @param key The key to be retrieved.
     *
     * @return The dynamic theme from the supplied arguments.
     */
    public @Nullable DynamicRemoteTheme getThemeFromArguments(@NonNull String key) {
        return DynamicTheme.getInstance().getRemoteTheme(getStringFromArguments(key));
    }

    @Override
    public @NonNull DynamicRemoteTheme onImportTheme(@NonNull String theme) {
        try {
            return new DynamicRemoteTheme(new DynamicWidgetTheme(theme));
        } catch (Exception ignored) {
        }

        return mThemePreview.getDynamicTheme();
    }

    @Override
    public void onLoadTheme(@NonNull DynamicRemoteTheme theme) {
        if (mSettingsChanged) {
            return;
        }

        mColorBackgroundPreference.setColor(theme.getBackgroundColor(false, false));
        mColorBackgroundPreference.setAltColor(theme.getTintBackgroundColor(false, false));
        mColorSurfacePreference.setColor(theme.getSurfaceColor(false, false));
        mColorSurfacePreference.setAltColor(theme.getTintSurfaceColor(false, false));
        mColorPrimaryPreference.setColor(theme.getPrimaryColor(false, false));
        mColorPrimaryPreference.setAltColor(theme.getTintPrimaryColor(false, false));
        mColorAccentPreference.setColor(theme.getAccentColor(false, false));
        mColorAccentPreference.setAltColor(theme.getTintAccentColor(false, false));
        mColorPrimaryDarkPreference.setColor(theme.getPrimaryColorDark(false, false));
        mColorPrimaryDarkPreference.setAltColor(theme.getTintPrimaryColorDark(false, false));
        mColorAccentDarkPreference.setColor(theme.getAccentColorDark(false, false));
        mColorAccentDarkPreference.setAltColor(theme.getTintAccentColorDark(false, false));
        mColorErrorPreference.setColor(theme.getErrorColor(false, false));
        mColorErrorPreference.setAltColor(theme.getTintErrorColor(false, false));
        mTextPrimaryPreference.setColor(theme.getTextPrimaryColor(false, false));
        mTextPrimaryPreference.setAltColor(theme.getTextPrimaryColorInverse(false, false));
        mTextSecondaryPreference.setColor(theme.getTextSecondaryColor(false, false));
        mTextSecondaryPreference.setAltColor(theme.getTextSecondaryColorInverse(false, false));

        if (theme.getFontScale(false) != Theme.AUTO) {
            mFontScalePreference.setPreferenceValue(Theme.ToString.CUSTOM);
            mFontScalePreference.setValue(theme.getFontScale());
        } else {
            mFontScalePreference.setPreferenceValue(Theme.ToString.AUTO);
            mFontScalePreference.setValue(mDynamicThemeDefault.getFontScale());
        }

        if (theme.getCornerRadius(false) != Theme.AUTO) {
            mCornerSizePreference.setPreferenceValue(Theme.ToString.CUSTOM);
            mCornerSizePreference.setValue(theme.getCornerSize());
        } else {
            mCornerSizePreference.setPreferenceValue(Theme.ToString.AUTO);
            mCornerSizePreference.setValue(mDynamicThemeDefault.getCornerSize());
        }

        mBackgroundAwarePreference.setPreferenceValue(
                String.valueOf(theme.getBackgroundAware(false)));
        mElevationPreference.setPreferenceValue(String.valueOf(theme.getElevation(false)));
        mStylePreference.setPreferenceValue(String.valueOf(theme.getStyle()));

        if (theme.getContrast(false) != Theme.AUTO) {
            mContrastPreference.setPreferenceValue(Theme.ToString.CUSTOM);
            mContrastPreference.setValue(theme.getContrast());
        } else {
            mContrastPreference.setPreferenceValue(Theme.ToString.AUTO);
            mContrastPreference.setValue(mDynamicThemeDefault.getContrast());
        }

        if (theme.getOpacity(false) != Theme.AUTO) {
            mOpacityPreference.setPreferenceValue(Theme.ToString.CUSTOM);
            mOpacityPreference.setValue(theme.getOpacity());
        } else {
            mOpacityPreference.setPreferenceValue(Theme.ToString.AUTO);
            mOpacityPreference.setValue(mDynamicThemeDefault.getOpacity());
        }

        updatePreferences();
    }

    @Override
    public void onSetAction(@Theme.Action int themeAction,
            @Nullable ThemePreview<DynamicRemoteTheme> themePreview, boolean enable) {
        if (themePreview == null) {
            return;
        }

        Dynamic.setResource(themePreview.getActionView(), enable
                ? R.drawable.ads_ic_save : themePreview.getDynamicTheme().isDynamicColor()
                ? R.drawable.adt_ic_app : R.drawable.ads_ic_customise);
    }

    @Override
    public @Nullable Bitmap getThemeBitmap(
            @Nullable ThemePreview<DynamicRemoteTheme> themePreview, int themeAction) {
        if (themePreview == null) {
            return null;
        }

        return DynamicThemeUtils.createRemoteThemeBitmap(themePreview);
    }

    /**
     * Returns the resolved font scale from the preference.
     *
     * @return The resolved font scale from the preference.
     */
    private int getFontScale() {
        return Theme.Font.ToString.AUTO.equals(mFontScalePreference.getPreferenceValue())
                ? Theme.Font.AUTO : mFontScalePreference.getValueFromProgress();
    }

    /**
     * Returns the resolved corner size from the preference.
     *
     * @return The resolved corner size from the preference.
     */
    private int getCornerSize() {
        return Theme.Corner.ToString.AUTO.equals(mCornerSizePreference.getPreferenceValue())
                ? Theme.Corner.AUTO : mCornerSizePreference.getValueFromProgress();
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

        return mDynamicTheme.getBackgroundAware(false);
    }

    /**
     * Returns the resolved contrast from the preference.
     *
     * @return The resolved contrast from the preference.
     */
    private int getContrast() {
        return Theme.Contrast.ToString.AUTO.equals(mContrastPreference.getPreferenceValue())
                ? Theme.Contrast.AUTO : mContrastPreference.getValueFromProgress();
    }

    /**
     * Returns the resolved opacity from the preference.
     *
     * @return The resolved opacity from the preference.
     */
    private int getOpacity() {
        return Theme.Opacity.ToString.AUTO.equals(mOpacityPreference.getPreferenceValue())
                ? Theme.Opacity.AUTO : mOpacityPreference.getValueFromProgress();
    }

    /**
     * Returns the resolved elevation from the preference.
     *
     * @return The resolved elevation from the preference.
     */
    private @Theme.Elevation int getElevation() {
        if (mElevationPreference.getPreferenceValue() != null) {
            return Integer.parseInt(mElevationPreference.getPreferenceValue());
        }

        return mDynamicTheme.getElevation(false);
    }

    /**
     * Returns the resolved style from the preference.
     *
     * @return The resolved style from the preference.
     */
    private @Theme.Style int getStyle() {
        if (mStylePreference.getPreferenceValue() != null) {
            return Integer.parseInt(mStylePreference.getPreferenceValue());
        }

        return mDynamicTheme.getStyle();
    }

    /**
     * Update the theme preview.
     */
    private void updateThemePreview() {
        mThemePreview.setDynamicTheme(new DynamicRemoteTheme(new DynamicWidgetTheme(mDynamicTheme)
                .setBackgroundColor(mColorBackgroundPreference.getColor(false), false)
                .setTintBackgroundColor(mColorBackgroundPreference.getAltColor(false))
                .setSurfaceColor(mColorSurfacePreference.getColor(false), false)
                .setTintSurfaceColor(mColorSurfacePreference.getAltColor(false))
                .setPrimaryColor(mColorPrimaryPreference.getColor(false), false)
                .setTintPrimaryColor(mColorPrimaryPreference.getAltColor(false))
                .setPrimaryColorDark(mColorPrimaryDarkPreference.getColor(false), false)
                .setTintPrimaryColorDark(mColorPrimaryDarkPreference.getAltColor(false))
                .setAccentColor(mColorAccentPreference.getColor(false), false)
                .setTintAccentColor(mColorAccentPreference.getAltColor(false))
                .setAccentColorDark(mColorAccentDarkPreference.getColor(false), false)
                .setTintAccentColorDark(mColorAccentDarkPreference.getAltColor(false))
                .setErrorColor(mColorErrorPreference.getColor(false), false)
                .setTintErrorColor(mColorErrorPreference.getAltColor(false))
                .setTextPrimaryColor(mTextPrimaryPreference.getColor(false), false)
                .setTextPrimaryColorInverse(mTextPrimaryPreference.getAltColor(false))
                .setTextSecondaryColor(mTextSecondaryPreference.getColor(false), false)
                .setTextSecondaryColorInverse(mTextSecondaryPreference.getAltColor(false))
                .setFontScale(getFontScale())
                .setCornerSize(getCornerSize())
                .setBackgroundAware(getBackgroundAware())
                .setContrast(getContrast())
                .setOpacity(getOpacity())
                .setElevation(getElevation())
                .setStyle(getStyle())));

        mSettingsChanged = true;
    }

    /**
     * Update all the preferences.
     */
    private void updatePreferences() {
        updateThemePreview();

        mColorBackgroundPreference.update();
        mColorSurfacePreference.update();
        mColorPrimaryPreference.update();
        mColorAccentPreference.update();
        mColorPrimaryDarkPreference.update();
        mColorAccentDarkPreference.update();
        mColorErrorPreference.update();
        mTextPrimaryPreference.update();
        mTextSecondaryPreference.update();
        mFontScalePreference.update();
        mCornerSizePreference.update();
        mBackgroundAwarePreference.update();
        mContrastPreference.update();
        mOpacityPreference.update();
        mElevationPreference.update();
        mStylePreference.update();

        mContrastPreference.setEnabled(mThemePreview.getDynamicTheme().isBackgroundAware());
        mFontScalePreference.setSeekEnabled(getFontScale() != Theme.AUTO);
        mCornerSizePreference.setSeekEnabled(getCornerSize() != Theme.AUTO);
        mContrastPreference.setSeekEnabled(getContrast() != Theme.AUTO);
        mOpacityPreference.setSeekEnabled(getOpacity() != Theme.AUTO);
    }

    @Override
    public boolean isOnSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        switch (key) {
            case ADS_PREF_THEME_COLOR_BACKGROUND:
            case ADS_PREF_THEME_COLOR_TINT_BACKGROUND:
            case ADS_PREF_THEME_COLOR_SURFACE:
            case ADS_PREF_THEME_COLOR_TINT_SURFACE:
            case ADS_PREF_THEME_COLOR_PRIMARY:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY:
            case ADS_PREF_THEME_COLOR_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_ACCENT:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT:
            case ADS_PREF_THEME_COLOR_ACCENT_DARK:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT_DARK:
            case ADS_PREF_THEME_COLOR_ERROR:
            case ADS_PREF_THEME_COLOR_TINT_ERROR:
            case ADS_PREF_THEME_TEXT_PRIMARY:
            case ADS_PREF_THEME_TEXT_INVERSE_PRIMARY:
            case ADS_PREF_THEME_TEXT_SECONDARY:
            case ADS_PREF_THEME_TEXT_INVERSE_SECONDARY:
            case ADS_PREF_THEME_FONT_SCALE:
            case ADS_PREF_THEME_FONT_SCALE_ALT:
            case ADS_PREF_THEME_CORNER_SIZE:
            case ADS_PREF_THEME_CORNER_SIZE_ALT:
            case ADS_PREF_THEME_BACKGROUND_AWARE:
            case ADS_PREF_THEME_CONTRAST:
            case ADS_PREF_THEME_CONTRAST_ALT:
            case ADS_PREF_THEME_OPACITY:
            case ADS_PREF_THEME_OPACITY_ALT:
            case ADS_PREF_THEME_ELEVATION:
            case ADS_PREF_THEME_STYLE:
                updatePreferences();
                break;
        }
    }
}
